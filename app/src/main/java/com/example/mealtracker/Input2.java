package com.example.mealtracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealtracker.FoodInfo.Food;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class Input2 extends AppCompatActivity {
    TextView foodDate;
    Button next_page;
    Button back;
    SimpleDateFormat sdf;
    String dateString;
    private EditText foodName, foodReview, foodPrice;
    private FirebaseDataSource firebaseDataSource;
    private ImageView x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input2);

        //input2 -> main 화면으로 이동
        x = findViewById(R.id.x);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Input2.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        foodDate = findViewById(R.id.foodDate);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                foodDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                sdf = new SimpleDateFormat(year + "-" + (month+1) + "-" + dayOfMonth);
                dateString = sdf.format(new Date()); // 현재 날짜를 문자열로 변환
            }
        }, mYear, mMonth, mDay);


        foodDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodDate.isClickable()) {
                    datePickerDialog.show();
                }
            }
        });




        firebaseDataSource = new FirebaseDataSource();

        Intent intent = getIntent();
        String foodType = intent.getStringExtra("foodType");
        String foodPlace = intent.getStringExtra("foodPlace");
        String imageName = intent.getStringExtra("imageName");

        foodName = findViewById(R.id.text_foodname);
        foodReview = findViewById(R.id.text_foodreview);
        foodPrice = findViewById(R.id.text_foodprice);

        //확인 클릭 -> 메인 페이지로 이동
        next_page = findViewById(R.id.submit);

        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!foodType.isEmpty() && !foodPlace.isEmpty() && !foodName.getText().toString().isEmpty()
                        && !foodReview.getText().toString().isEmpty() && !dateString.isEmpty()
                        && !foodPrice.getText().toString().isEmpty() && Integer.parseInt(foodPrice.getText().toString())%1==0) {
                    Intent intent = new Intent(Input2.this, MainActivity.class);
                    firebaseDataSource.addFood(foodType, foodPlace, foodName.getText().toString(), foodReview.getText().toString(), imageName, Integer.parseInt(foodPrice.getText().toString()), dateString);
                    Toast.makeText(Input2.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else{
                    Toast.makeText(Input2.this, "빈 값이 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //뒤로 클릭 -> Input1로 이동
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Input2.this, Input1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public class FirebaseDataSource {
        private DatabaseReference databaseReference;

        public FirebaseDataSource() {
            // "foods"는 데이터베이스의 루트 경로
            databaseReference = FirebaseDatabase.getInstance().getReference("foods");
        }
        public void addFood (String foodType, String foodPlace, String foodName, String
                foodReview, String imageName, Integer foodPrice, String foodDate){
            // 데이터 추가
            // 데이터베이스에 새로운 노드 추가
            String foodId = databaseReference.push().getKey(); // 자동으로 고유한 ID 생성
            if (!foodType.isEmpty() && !foodPlace.isEmpty() && !foodName.isEmpty() && !foodReview.isEmpty() && !imageName.isEmpty()&& foodPrice!=null && !foodDate.isEmpty()) {
                Food food = new Food(foodId, foodType, foodPlace, foodName, foodReview, imageName, foodPrice, foodDate);
                databaseReference.child(foodId).setValue(food);
                Log.d("FirebaseDataSource", "데이터가 성공적으로 추가되었습니다.");
            }else {
                Log.e("FirebaseDataSource", "데이터를 추가할 수 없습니다. 모든 필드는 null 또는 비어 있어서는 안됩니다.");
            }
        }
    }

}
