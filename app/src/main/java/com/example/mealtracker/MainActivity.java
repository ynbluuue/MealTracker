package com.example.mealtracker;

import static com.example.mealtracker.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView foodInput;
    private TextView showFood;
    private TextView analyzeFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        //식사 입력하기 클릭 -> Input1 페이지로 이동
        foodInput = findViewById(id.some_id);
        foodInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Input1.class);
                startActivity(intent);
            }
        });

        //식사 보여주기 클릭 -> Show1 페이지로 이동
        showFood = findViewById(id.some_id2);
        showFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Show1.class);
                startActivity(intent);
            }
        });

        //식사 분석하기 클릭 -> Analyze1 페이지로 이동
        analyzeFood = findViewById(id.some_id3);
        analyzeFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Analyze1.class);
                startActivity(intent);
            }
        });
    }
}