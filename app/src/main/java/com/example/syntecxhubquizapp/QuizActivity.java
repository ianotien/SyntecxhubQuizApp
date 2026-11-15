package com.example.syntecxhubquizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.content.Intent;
import android.os.CountDownTimer;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestionNumber, tvQuestion, tvFeedback;
    TextView tvTimer;

    CountDownTimer countDownTimer;

    private static final long QUESTION_TIME = 15000;
    ProgressBar quizProgress;
    RadioGroup rgOptions;
    RadioButton[] optionButtons;
    Button btnNext;

    List<Question> questions;

    int currentQuestionIndex = 0;
    int score = 0;

    boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Connect XML components
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvTimer = findViewById(R.id.tvTimer);

        quizProgress = findViewById(R.id.quizProgress);

        rgOptions = findViewById(R.id.rgOptions);

        btnNext = findViewById(R.id.btnNext);

        optionButtons = new RadioButton[]{
                findViewById(R.id.rbOption1),
                findViewById(R.id.rbOption2),
                findViewById(R.id.rbOption3),
                findViewById(R.id.rbOption4)
        };

        // Load questions
        loadQuestions();

        // Shuffle questions
        Collections.shuffle(questions);

        // Display first question
        displayQuestion();

        // Next button functionality
        btnNext.setOnClickListener(v -> handleNextButton());
    }

    // Question class
    static class Question {

        String question;
        List<String> options;
        String correctAnswer;

        Question(String question, String[] options,
                 String correctAnswer) {

            this.question = question;
            this.options = new ArrayList<>(Arrays.asList(options));
            this.correctAnswer = correctAnswer;
        }
    }

    // Load quiz questions
    private void loadQuestions() {

        questions = new ArrayList<>();

        questions.add(new Question(
                "What is the capital city of Kenya?",
                new String[]{"Mombasa", "Nairobi", "Kisumu", "Nakuru"},
                "Nairobi"
        ));

        questions.add(new Question(
                "Which language is primarily used for Android development in this project?",
                new String[]{"Python", "Java", "C++", "PHP"},
                "Java"
        ));

        questions.add(new Question(
                "Which planet is known as the Red Planet?",
                new String[]{"Earth", "Venus", "Mars", "Jupiter"},
                "Mars"
        ));

        questions.add(new Question(
                "How many days are there in a leap year?",
                new String[]{"364", "365", "366", "367"},
                "366"
        ));

        questions.add(new Question(
                "Which device is used to input text into a computer?",
                new String[]{"Monitor", "Keyboard", "Speaker", "Printer"},
                "Keyboard"
        ));

        questions.add(new Question(
                "What does CPU stand for?",
                new String[]{"Central Processing Unit",
                        "Computer Personal Unit",
                        "Central Program Utility",
                        "Control Processing User"},
                "Central Processing Unit"
        ));

        questions.add(new Question(
                "Which ocean is the largest?",
                new String[]{"Atlantic", "Indian", "Arctic", "Pacific"},
                "Pacific"
        ));

        questions.add(new Question(
                "Which of these is an operating system?",
                new String[]{"Microsoft Word", "Android", "Google Chrome", "Facebook"},
                "Android"
        ));

        questions.add(new Question(
                "What is 12 multiplied by 5?",
                new String[]{"50", "55", "60", "65"},
                "60"
        ));

        questions.add(new Question(
                "Which company developed Java?",
                new String[]{"Microsoft", "Sun Microsystems",
                        "Apple", "IBM"},
                "Sun Microsystems"
        ));
    }

    // Display current question
    private void displayQuestion() {

        Question currentQuestion = questions.get(currentQuestionIndex);

        tvQuestionNumber.setText(
                "Question " + (currentQuestionIndex + 1)
                        + " of " + questions.size()
        );

        quizProgress.setProgress(currentQuestionIndex + 1);

        tvQuestion.setText(currentQuestion.question);

        // Shuffle answer options
        Collections.shuffle(currentQuestion.options);

        // Clear previous selections
        rgOptions.clearCheck();

        // Enable answer options
        for (int i = 0; i < optionButtons.length; i++) {

            optionButtons[i].setText(
                    currentQuestion.options.get(i)
            );

            optionButtons[i].setEnabled(true);
        }
        // Animate question appearance
        tvQuestion.setAlpha(0f);
        tvQuestion.setTranslationY(20f);

        tvQuestion.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .start();

// Animate answer options
        rgOptions.setAlpha(0f);

        rgOptions.animate()
                .alpha(1f)
                .setDuration(500)
                .start();

        tvFeedback.setText("");

        answered = false;

        btnNext.setText("CHECK ANSWER");
        startQuestionTimer();
    }
    private void startQuestionTimer() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(QUESTION_TIME, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                long seconds = (millisUntilFinished + 999) / 1000;

                tvTimer.setText("Time left: " + seconds + "s");
            }

            @Override
            public void onFinish() {

                if (!answered) {

                    tvTimer.setText("Time's up!");

                    String correctAnswer =
                            questions.get(currentQuestionIndex).correctAnswer;

                    tvFeedback.setText(
                            "Time's up! Correct answer: " + correctAnswer
                    );

                    tvFeedback.setTextColor(
                            getColor(android.R.color.holo_red_dark)
                    );

                    for (RadioButton button : optionButtons) {
                        button.setEnabled(false);
                    }

                    answered = true;

                    if (currentQuestionIndex == questions.size() - 1) {
                        btnNext.setText("VIEW RESULTS");
                    } else {
                        btnNext.setText("NEXT QUESTION");
                    }
                }
            }

        }.start();
    }

    // Handle button clicks
    private void handleNextButton() {

        if (!answered) {

            int selectedId = rgOptions.getCheckedRadioButtonId();

            if (selectedId == -1) {

                Toast.makeText(this,
                        "Please select an answer",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            RadioButton selectedButton = findViewById(selectedId);

            String selectedAnswer =
                    selectedButton.getText().toString();

            String correctAnswer =
                    questions.get(currentQuestionIndex).correctAnswer;

            if (selectedAnswer.equals(correctAnswer)) {

                score++;

                tvFeedback.setText("Correct! Well done.");

                tvFeedback.setTextColor(
                        getColor(android.R.color.holo_green_dark)
                );

            } else {

                tvFeedback.setText(
                        "Incorrect! Correct answer: " + correctAnswer
                );

                tvFeedback.setTextColor(
                        getColor(android.R.color.holo_red_dark)
                );
            }

            // Disable answers after checking
            for (RadioButton button : optionButtons) {
                button.setEnabled(false);
            }

            answered = true;

            if (currentQuestionIndex == questions.size() - 1) {

                btnNext.setText("VIEW RESULTS");

            } else {

                btnNext.setText("NEXT QUESTION");
            }

        } else {

            if (currentQuestionIndex < questions.size() - 1) {

                currentQuestionIndex++;

                displayQuestion();

            } else {

                // Open Results Screen
                Intent intent = new Intent(
                        QuizActivity.this,
                        ResultsActivity.class
                );

                // Transfer quiz results
                intent.putExtra("score", score);
                intent.putExtra("totalQuestions", questions.size());

                startActivity(intent);

                finish();
            }

        }
    }
    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }
}