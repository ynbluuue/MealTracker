package com.example.mealtracker;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealtracker.FoodInfo.FoodRecord;
import com.example.mealtracker.FoodInfo.FoodRecordAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Show3 extends AppCompatActivity {
    TextView show3title;
    private RecyclerView recyclerView;
    private FoodRecordAdapter adapter;
    private List<FoodRecord> foodlist;
    private String message;
    DatabaseReference foodsReference;
    String selectedYearMonth;
    ImageView x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show3);


        // 리사이클러뷰 초기화
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1)); // 한 주에 7일

        // 최근 한 달 간의 식사 데이터 가져오기 (Firebase에서 가져오는 코드)
        foodsReference = FirebaseDatabase.getInstance().getReference().child("foods");

        Intent intent = getIntent();
        selectedYearMonth = intent.getStringExtra("selectedYearMonth");
        Log.d(message, "선택하신 날짜는 " + selectedYearMonth + "입니다.");

        // 해당 월에 해당하는 음식 데이터를 가져오는 함수 호출
        monthlyFoodsData(selectedYearMonth);

        //제목 설정
        show3title = findViewById(R.id.show3title);
        show3title.setText(Html.fromHtml("<b>" + selectedYearMonth.substring(0, 4) + "년 " + selectedYearMonth.substring(5, 7) + "월<br>동안 하신 식사입니다</b>"));

        // 어댑터 초기화
        foodlist = new ArrayList<>();
        adapter = new FoodRecordAdapter(foodlist);
        recyclerView.setAdapter(adapter);


        //Show3 -> Show1 화면으로 이동
        x = findViewById(R.id.x);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Show3.this, Show1.class);
                startActivity(intent);
            }
        });
    }



    private void monthlyFoodsData(String yearMonth) {
        foodsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodlist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodRecord foodRecord = snapshot.getValue(FoodRecord.class);
                    if (foodRecord != null) {
                        // 해당 음식의 날짜를 가져옴
                        String foodDate = foodRecord.getDateTime();
                        String date = foodDate.substring(0, 7);

                        Log.d(message, "음식을 드신 날짜는 " + date + "이고 " + yearMonth + "는 선택하신 날짜입니다.");
                        // 사용자가 입력한 년월과 일치하는 경우에만 리스트에 추가
                        if (date.equals(yearMonth)) {
                            foodlist.add(foodRecord);
                            adapter.notifyDataSetChanged();
                            Log.d(message, "음식이 리스트에 추가되었습니다.");
                        }
                    }
                }
                // foodlist를 DateTimeComparator를 사용하여 정렬
                Collections.sort(foodlist, new DateTimeComparator());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
            }
    });
}


    public static class DateTimeComparator implements Comparator<FoodRecord> {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        @Override
        public int compare(FoodRecord foodRecord1, FoodRecord foodRecord2) {
            try {
                Date date1 = dateFormat.parse(foodRecord1.getDateTime());
                Date date2 = dateFormat.parse(foodRecord2.getDateTime());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}