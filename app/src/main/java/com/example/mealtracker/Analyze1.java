package com.example.mealtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mealtracker.FoodInfo.FoodRecord;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class Analyze1 extends AppCompatActivity {
    DatabaseReference foodsReference;
    TextView title, calorie, breakfast, lunch, dinner, drinks;
    ImageView x;
    List<FoodRecord> foodList;
    String message;
    Long totalCal;
    Long breakFastCost = 0L;
    Long lunchCost = 0L;
    Long dinnerCost = 0L;
    Long drinksCost = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyze1);

        // 최근 한 달 간의 식사 데이터 가져오기 (Firebase에서 가져오는 코드)
        foodsReference = FirebaseDatabase.getInstance().getReference().child("foods");

        // 현재 날짜 가져오기
        Date currentDate = new Date();

        // SimpleDateFormat을 사용하여 "yyyy-MM" 형식으로 포맷팅
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String currentMonth = dateFormat.format(currentDate);
        Log.d(message, "현재 날짜는: " + currentMonth);


        //제목 설정
        title = findViewById(R.id.title);
        title.setText(Html.fromHtml("<b>" + currentMonth.substring(0, 4) + "년 " + currentMonth.substring(5, 7) + "월<br>식사 분석입니다</b>"));
        Log.d(message, "제목은: " + title.getText());

        calorie = findViewById(R.id.calorie);
        breakfast =findViewById(R.id.breakfast);
        lunch = findViewById(R.id.lunch);
        dinner = findViewById(R.id.dinner);
        drinks = findViewById(R.id.drinks);


        // 현재 월에 해당하는 음식 데이터를 가져오는 함수 호출
        foodList = new ArrayList<>();
        monthlyFoodsData(currentMonth, new OnDataLoadListener() {
            @Override
            public void onDataLoad() {
                totalCalorie(foodList);
                totalCost(foodList);
            }
        });


        //Analyze1 -> Main 화면으로 이동
        x = findViewById(R.id.x);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Analyze1.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }




    private void monthlyFoodsData(String yearMonth, OnDataLoadListener listener) {
        foodsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodRecord foodRecord = snapshot.getValue(FoodRecord.class);
                    if (foodRecord != null) {
                        // 해당 음식의 날짜를 가져옴
                        String foodDate = foodRecord.getDateTime();
                        String date = foodDate.substring(0, 7);

                        Log.d(message, "음식을 드신 날짜는 " + date + "이고 " + yearMonth + "는 선택하신 날짜입니다.");
                        // 사용자가 입력한 년월과 일치하는 경우에만 리스트에 추가
                        if (date.equals(yearMonth)) {
                            foodList.add(foodRecord);
                            Log.d(message, "음식이 리스트에 추가되었습니다.");
                        }
                    }
                }
                listener.onDataLoad(); // 데이터를 가져온 후에 리스너 호출
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
            }
        });
    }

    // 데이터를 가져온 후에 호출할 리스너 인터페이스
    private interface OnDataLoadListener {
        void onDataLoad();
    }




    private void totalCalorie(List<FoodRecord> foodList) {
        DatabaseReference caloriesReference = FirebaseDatabase.getInstance().getReference().child("calories");
        totalCal = 0L; // 총 칼로리 초기화

        int foodCount = foodList.size(); // 음식의 개수를 세기 위한 변수
        AtomicInteger counter = new AtomicInteger(0); // 반복문에서 사용할 AtomicInteger

        for (FoodRecord food : foodList) {
            caloriesReference.orderByChild("foodName").equalTo(food.getFoodName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 데이터가 존재하는 경우
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // 'kcal' 값을 가져오기
                            Long kcal = snapshot.child("kcal").getValue(Long.class);
                            if (kcal != null) {
                                totalCal += kcal;
                            }
                        }
                    }

                    // 모든 데이터를 확인했을 때 처리
                    if (counter.incrementAndGet() == foodCount) {
                        String cal = String.format(Locale.getDefault(), "%d kcal", totalCal);
                        calorie.setText(Html.fromHtml("<b>" + cal + "</b>"));
                        Log.d(message, "총 칼로리는 " + calorie.getText());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 오류 처리
                    System.out.println("Error: " + databaseError.getMessage());
                }
            });
        }
    }




    private void totalCost(List<FoodRecord> foodList) {
        int foodCount = foodList.size(); // 음식의 개수를 세기 위한 변수
        AtomicInteger counter = new AtomicInteger(0); // 반복문에서 사용할 AtomicInteger

        for (FoodRecord food : foodList) {
            foodsReference.orderByChild("foodType").equalTo(food.getFoodType()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 데이터가 존재하는 경우
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String type = snapshot.child("foodType").getValue(String.class);
                            if (type.equals("조식")){
                                breakFastCost += food.getFoodPrice();
                            } else if (type.equals("중식")) {
                                lunchCost += food.getFoodPrice();
                            } else if (type.equals("석식")) {
                                dinnerCost += food.getFoodPrice();
                            } else {
                                drinksCost += food.getFoodPrice();
                            }
                        }
                    }

                    // 모든 데이터를 확인했을 때 처리
                    if (counter.incrementAndGet() == foodCount) {
                        breakfast.setText(Html.fromHtml("<b>" + breakFastCost.toString() + " 원</b>"));
                        lunch.setText(Html.fromHtml("<b>" + lunchCost.toString() + " 원</b>"));
                        dinner.setText(Html.fromHtml("<b>" + dinnerCost.toString() + " 원</b>"));
                        drinks.setText(Html.fromHtml("<b>" + drinksCost.toString() + " 원</b>"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 오류 처리
                    System.out.println("Error: " + databaseError.getMessage());
                }
            });
        }
    }
}