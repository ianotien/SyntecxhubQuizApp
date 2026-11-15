package com.example.syntecxhubquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect the Start Quiz button
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        // Open QuizActivity when clicked
        btnStartQuiz.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    QuizActivity.class
            );

            startActivity(intent);
        });
    }
}