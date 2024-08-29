package com.firstapp.quizapplication;


import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler; // Import the Handler class
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView questionTextView;
    TextView totalQuestionTextView;
    Button ansA, ansB, ansC, ansD;
    Button btn_submit;

    int score = 0;
    int totalQuestion = Question.question.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalQuestionTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_a);
        ansB = findViewById(R.id.ans_b);
        ansC = findViewById(R.id.ans_c);
        ansD = findViewById(R.id.ans_d);
        btn_submit = findViewById(R.id.btn_submit);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        totalQuestionTextView.setText("Total question: " + totalQuestion);

        loadNewQuestion();
    }

    private void loadNewQuestion() {
        if (currentQuestionIndex == totalQuestion) {
            finishQuiz();
            return;
        }
        questionTextView.setText(Question.question[currentQuestionIndex]);
        ansA.setText(Question.choices[currentQuestionIndex][0]);
        ansB.setText(Question.choices[currentQuestionIndex][1]);
        ansC.setText(Question.choices[currentQuestionIndex][2]);
        ansD.setText(Question.choices[currentQuestionIndex][3]);

        selectedAnswer = "";
        resetButtonColors();
    }

    private void resetButtonColors() {
        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);
    }

    private void finishQuiz() {
        String passStatus;
        if (score >= totalQuestion * 0.6) {
            passStatus = "Passed";
        } else {
            passStatus = "Failed";
        }
        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Your Score is " + score + " Out of " + totalQuestion)
                .setPositiveButton("Restart", (dialog, i) -> restartQuiz())
                .setCancelable(false)
                .show();
    }

    private void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            if (!selectedAnswer.isEmpty()) {
                Button correctButton = getButtonByAnswer(Question.correctAnswers[currentQuestionIndex]);
                if (selectedAnswer.equals(Question.correctAnswers[currentQuestionIndex])) {
                    score++;
                    correctButton.setBackgroundColor(Color.GREEN);
                } else {
                    Button selectedButton = getButtonByAnswer(selectedAnswer);
                    selectedButton.setBackgroundColor(Color.RED);
                    correctButton.setBackgroundColor(Color.GREEN);
                }
                // Use Handler to add a delay before loading the next question
                handler.postDelayed(() -> {
                    currentQuestionIndex++;
                    loadNewQuestion();
                }, 1000); // Delay of 1 second (1000 milliseconds)
            }
        } else {
            selectedAnswer = ((Button) view).getText().toString();
            resetButtonColors();
            ((Button) view).setBackgroundColor(Color.BLUE);
        }
    }

    private Button getButtonByAnswer(String answer) {
        if (answer.equals(ansA.getText().toString())) return ansA;
        if (answer.equals(ansB.getText().toString())) return ansB;
        if (answer.equals(ansC.getText().toString())) return ansC;
        return ansD; // answer.equals(ansD.getText().toString())
    }
}


