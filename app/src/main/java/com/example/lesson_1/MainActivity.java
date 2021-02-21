package com.example.lesson_1;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonSesond = findViewById(R.id.button);
        buttonSesond.setOnClickListener(view -> {setContentView(R.layout.second); });


    }
}