package com.example.syntecxhubquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    TextView tvFinalScore, tvPercentage, tvPerformance, tvHighScore;
    Button btnPlayAgain, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        // Animate results screen
        View resultsRoot = findViewById(R.id.resultsRoot);

        resultsRoot.setAlpha(0f);
        resultsRoot.setTranslationY(30f);

        resultsRoot.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .start();

        // Connect XML components
        tvFinalScore = findViewById(R.id.tvFinalScore);
        tvPercentage = findViewById(R.id.tvPercentage);
        tvPerformance = findViewById(R.id.tvPerformance);
        tvHighScore = findViewById(R.id.tvHighScore);

        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnHome = findViewById(R.id.btnHome);

        // Receive quiz results
        int score = getIntent().getIntExtra("score", 0);

        int totalQuestions = getIntent().getIntExtra(
                "totalQuestions", 10
        );

        // Calculate percentage
        int percentage = totalQuestions > 0
                ? (score * 100) / totalQuestions
                : 0;

        // Display results
        tvFinalScore.setText(
                score + " / " + totalQuestions
        );

        tvPercentage.setText(
                percentage + "%"
        );
        // Access SharedPreferences
        SharedPreferences preferences =
                getSharedPreferences("QuizPreferences", MODE_PRIVATE);

// Retrieve previously saved high score
        int highScore = preferences.getInt("high_score", 0);

// Update high score if current score is higher
        if (score > highScore) {

            highScore = score;

            preferences.edit()
                    .putInt("high_score", highScore)
                    .apply();
        }

// Display highest score
        tvHighScore.setText(
                "Best Score: " + highScore + " / " + totalQuestions
        );

        // Display performance message
        if (percentage >= 80) {

            tvPerformance.setText("Excellent Performance!");

        } else if (percentage >= 60) {

            tvPerformance.setText("Good Job! Keep Practicing.");

        } else if (percentage >= 40) {

            tvPerformance.setText("Not Bad! You Can Improve.");

        } else {

            tvPerformance.setText("Keep Learning and Try Again!");
        }

        // Play Again button
        btnPlayAgain.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ResultsActivity.this,
                    QuizActivity.class
            );

            startActivity(intent);

            finish();
        });

        // Back to Home button
        btnHome.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ResultsActivity.this,
                    MainActivity.class
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);

            finish();
        });
    }
}