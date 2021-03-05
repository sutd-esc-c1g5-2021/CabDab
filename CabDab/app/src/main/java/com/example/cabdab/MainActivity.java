package com.example.cabdab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void uploadImage(View view){
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }
    public void download(View view){
        Intent intent = new Intent(this, DownloadActivity.class);
        startActivity(intent);
    }
}
