package com.example.mealtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Input1 extends AppCompatActivity {
    String[] item = {"조식", "중식", "석식", "음료"};
    String[] item2 = {"상록원", "기숙사", "가든쿡", "그루터기", "폴바셋", "블루팟", "가온누리"};
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView foodType, foodPlace;
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView autoCompleteTextView2;
    ArrayAdapter<String> adapterItems2;
    Button next_page;
    private ImageView x;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input1);

        //Input1 -> main 화면으로 이동
        x = findViewById(R.id.x);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //조식/중식/석식/음료 중 선택
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Input1.this, "Item" + item, Toast.LENGTH_SHORT).show();
            }
        });


        //학식 장소 선택
        autoCompleteTextView2 = findViewById(R.id.auto_complete_txt2);
        adapterItems2 = new ArrayAdapter<String>(this, R.layout.list_item, item2);

        autoCompleteTextView2.setAdapter(adapterItems2);
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Input1.this, "Item" + item, Toast.LENGTH_SHORT).show();
            }
        });


        //사진 선택
        Button buttonSelect = findViewById(R.id.selectphoto);

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
            }
        });


        //사진 업로드
        Button buttonupload = findViewById(R.id.uploadphoto);

        buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

        //다음 클릭 -> Input2 페이지로 이동
        foodType = findViewById(R.id.auto_complete_txt);
        foodPlace = findViewById(R.id.auto_complete_txt2);

        next_page = findViewById(R.id.next);
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Input1.this, Input2.class);
                intent.putExtra("foodType", foodType.getText().toString());
                intent.putExtra("foodPlace", foodPlace.getText().toString());
                intent.putExtra("imageName", imageName);
                startActivity(intent);
            }
        });

    }


    private void select() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT );
        intent.setType("image/*");
        launcher.launch(intent);
    }

    private void upload() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Food");
        imageName = "image_" + System.currentTimeMillis() + ".jpg";
        storageReference.child("images").child(imageName).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(Input1.this, "업로드에 성공했습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Input1.this, "업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Uri uri;
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        Log.d("test", uri.toString());

                    }
                }
            });

}
