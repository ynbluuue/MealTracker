package com.example.mealtracker;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealtracker.FoodInfo.CustomCalendarActivity;
import com.example.mealtracker.FoodInfo.FoodRecord;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Show1 extends AppCompatActivity {
    private DatabaseReference foodsReference; // 파이어베이스 DatabaseReference
    private ListView listView;
    private List<FoodRecord> foodRecords;  // 선택한 날짜에 해당하는 데이터 리스트
    private ImageView x;
    private Button all;
    String selectedYearMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show1);

        //show1 -> main 화면으로 이동
        x = findViewById(R.id.x);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Show1.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        // Firebase에서 데이터 읽어오기
        foodsReference = FirebaseDatabase.getInstance().getReference().child("foods");
        readFoodsData(calendarView);

        //선택한 날짜에 대한 음식 리스트뷰
        calendarView = findViewById(R.id.calendarView);
        listView = findViewById(R.id.foodlist);

        // MaterialCalendarView에서 날짜를 선택하는 이벤트 처리
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
                selectedYearMonth = dateFormat.format(date.getDate());
                // 선택한 날짜에 해당하는 데이터 찾기
                findDataForSelectedDate(date);
            }
        });


        //show1 -> show3 화면으로 이동
        //selectedDate 값 넘겨줌
        all = findViewById(R.id.all);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedYearMonth != null) {
                    Intent intent = new Intent(Show1.this, Show3.class);
                    intent.putExtra("selectedYearMonth", selectedYearMonth);
                    startActivity(intent);
                }else{
                    Toast.makeText(Show1.this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //리스트뷰의 음식 클릭시 작동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(Show1.this, Show2.class);
                FoodRecord clickedRecord = selectedDateRecords.get(position);
                /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                intent.putExtra("foodname", clickedRecord.getFoodName());
                intent.putExtra("foodis", clickedRecord.getFoodType());
                intent.putExtra("foodplace", clickedRecord.getFoodPlace());
                intent.putExtra("foodreview", clickedRecord.getFoodReview());
                intent.putExtra("foodprice", Long.toString(clickedRecord.getFoodPrice()));
                intent.putExtra("fooddate", clickedRecord.getDateTime());
                startActivity(intent);
            }
        });

    }


    // 선택한 날짜에 해당하는 데이터 찾기 및 ListView에 표시
    List<FoodRecord> selectedDateRecords = new ArrayList<>();
    private void findDataForSelectedDate(CalendarDay selectedDate) {
        selectedDateRecords.clear();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        if (foodRecords == null) {adapter.clear();}
        listView.setAdapter(adapter);

        // 여기서 선택한 날짜에 해당하는 데이터를 가져오는 작업을 수행
        // 이 예제에서는 foodRecords 리스트에 있는 데이터 중 선택한 날짜와 일치하는 것만 가져온다고 가정
        for (FoodRecord record : foodRecords) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = dateFormat.parse(record.getDateTime());
                Calendar calendar = Calendar.getInstance();
                Calendar calendarSelected = Calendar.getInstance();
                calendar.setTime(date);
                calendarSelected.setTime(selectedDate.getDate());

                // 날짜 비교 로직을 사용하여 선택한 날짜에 해당하는 데이터를 찾음
                if (record.getDateTime() != null && isSameDay(calendar, calendarSelected)) {
                    selectedDateRecords.add(record);
                } else{
                    throw new NullPointerException();
                }

                // ListView에 데이터 바인딩
                bindDataToListView(selectedDateRecords);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 두 날짜가 같은 날인지 확인하는 메서드
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    // ListView에 데이터 바인딩
    private void bindDataToListView(List<FoodRecord> records) {
        // records를 사용하여 ListView에 데이터를 표시하는 작업을 수행
        // 이 예제에서는 간단히 ArrayAdapter를 사용하여 String 형태로 표시
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (FoodRecord record : records) {
            adapter.add(record.getFoodName() + " (" + record.getFoodType() + ", " + record.getFoodPrice() + "원)");  // FoodRecord에 있는 데이터를 가져와서 사용
        }
        listView.setAdapter(adapter);
    }


    private void readFoodsData(final MaterialCalendarView calendarView) {
        foodsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodRecords = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodRecord foodRecord = snapshot.getValue(FoodRecord.class);
                    if (foodRecord != null) {
                        foodRecords.add(foodRecord);
                    }
                }

                // FoodRecordDecorator를 생성하여 MaterialCalendarView에 추가
                CustomCalendarActivity.FoodRecordDecorator foodRecordDecorator = new CustomCalendarActivity.FoodRecordDecorator(foodRecords);
                calendarView.addDecorator(foodRecordDecorator);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
            }
        });
    }
}


