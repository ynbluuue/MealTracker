package com.example.mealtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

public class Show2 extends AppCompatActivity {
    ImageView x;
    TextView foodis2, calorie2, foodplaceis2, foodname2, date2, price2, foodReview2;
    Long kcal;
    DatabaseReference dataReference;
    DatabaseReference caloriesReference;
    private Uri uri;
    private ImageView imageView;
    String imageName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show2_listdetail);

        //show2 -> show1 화면으로 이동
        x = findViewById(R.id.x);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();
        String foodis = intent.getStringExtra("foodis");
        String foodname = intent.getStringExtra("foodname");
        String foodplace = intent.getStringExtra("foodplace");
        String foodreview = intent.getStringExtra("foodreview");
        String foodprice = intent.getStringExtra("foodprice");
        String fooddate = intent.getStringExtra("fooddate");

        foodis2 = findViewById(R.id.foodis2);
        calorie2 = findViewById(R.id.calorie2);
        foodplaceis2 = findViewById(R.id.foodplaceis2);
        foodname2 = findViewById(R.id.foodname2);
        date2 = findViewById(R.id.date2);
        price2 = findViewById(R.id.price2);
        foodReview2 = findViewById(R.id.foodreview2);

        if (!foodis.isEmpty() && !foodname.isEmpty() && !foodplace.isEmpty() && !foodreview.isEmpty() && !foodprice.isEmpty() && !fooddate.isEmpty()) {
            getKcalForFoodName(foodname);
            foodis2.setText(foodis);
            foodname2.setText(foodname);
            foodplaceis2.setText(foodplace);
            foodReview2.setText(foodreview);
            price2.setText(foodprice);
            date2.setText(fooddate);
        }


        imageView = findViewById(R.id.image_view);
        showImage(foodname);
    }


    private void showImage(String foodName) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Food");
        //'foods' 테이블에서 특정 'foodName'에 해당하는 'imageName' 값을 조회
        dataReference = FirebaseDatabase.getInstance().getReference().child("foods");
        dataReference.orderByChild("foodName").equalTo(foodName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 데이터가 존재하는 경우
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // 'imageName' 값을 가져오기
                        imageName = snapshot.child("imageName").getValue(String.class);

                        if (imageName != null) {
                            Log.d("FirebaseTest", "imageName for " + foodName + ": " + imageName);
                            storageReference.child("images").child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(Show2.this).load(uri).override(230, 230).centerCrop().into(imageView);
                                }
                            });
                        }
                    }
                } else {
                    throw new NullPointerException();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }

    public void getKcalForFoodName(String foodName) {
        //'calories' 테이블에서 특정 'foodName'에 해당하는 'kcal' 값을 조회
        caloriesReference = FirebaseDatabase.getInstance().getReference().child("calories");
        caloriesReference.orderByChild("foodName").equalTo(foodName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 데이터가 존재하는 경우
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // 'kcal' 값을 가져오기
                        kcal = snapshot.child("kcal").getValue(Long.class);
                        calorie2.setText(String.format(Locale.getDefault(), "%d kcal", kcal));

                        if (kcal != null) {
                            Log.d("FirebaseTest", "kcal for " + foodName + ": " + kcal);
                        }
                    }
                } else {
                    throw new NullPointerException();
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
