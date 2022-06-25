package com.example.qtrivia;

import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.qtrivia.Data.DataRepo;
import com.example.qtrivia.Model.Question;
import com.example.qtrivia.Model.Score;
import com.example.qtrivia.prefUtil.prefre;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView questionTextView, questionCounter, scoreText,highestScoreText;
    Button next_button, true_button, false_button;
    List<Question> questionList;
    CardView cardView;

    private int currentQuestionIndex = 0;
    private int scoreCounter = 0;
    private Score score;
    private prefre prefre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        questionTextView = findViewById(R.id.question_text);
        next_button = findViewById(R.id.button_next);
        true_button = findViewById(R.id.true_button);
        false_button = findViewById(R.id.button_false);

        questionCounter = findViewById(R.id.text_view_out);
        cardView = findViewById(R.id.cardView);
        scoreText = findViewById(R.id.score_text);

        highestScoreText = findViewById(R.id.highest_score);
        score = new Score();
        prefre = new prefre(MainActivity.this);

        questionList = new DataRepo().getQuestions(questionArrayList -> {
            questionTextView.setText(questionArrayList.get(currentQuestionIndex)
                    .getAnswer());

            updateCounter(questionArrayList);
        });

        currentQuestionIndex = prefre.getState();
        highestScoreText.setText(MessageFormat.format("Highest : {0}", String.valueOf(prefre.getHighest())));
        scoreText.setText(MessageFormat.format("Score : {0}", String.valueOf(score.getScore())));

        next_button.setOnClickListener(view -> {
            goToNext();
        });

        true_button.setOnClickListener(view -> {
            checkAnswer(true);
            updateQuestion();
        });

        false_button.setOnClickListener(view -> {
            checkAnswer(false);
            updateQuestion();
        });
    }

    private void goToNext() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }

    private void checkAnswer(boolean b) {
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMsgId = 0;

        if (b == answer) {
            snackMsgId = R.string.correct;
            fadeAnimation();
            addScore();
        } else {
            snackMsgId = R.string.incorrect;
            shakeAnimation();
            deductScore();
        }

        Snackbar.make(cardView, snackMsgId, Snackbar.LENGTH_SHORT).show();
    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        questionCounter.setText((String.format(getString(R.string.text_formated)
                , currentQuestionIndex, questionArrayList.size())));
    }

    private void updateQuestion() {
        String Question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(Question);
        updateCounter((ArrayList<Question>) questionList);
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                questionTextView.setTextColor(Color.WHITE);
                goToNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        cardView.setAnimation(shakeAnim);
        shakeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                questionTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                questionTextView.setTextColor(Color.WHITE);
                goToNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addScore(){
        scoreCounter += 10;
        score.setScore(scoreCounter);
        scoreText.setText(String.valueOf(score.getScore()));
        scoreText.setText(MessageFormat.format("Score : {0}", String.valueOf(score.getScore())));
    }

    private void deductScore(){
        if(scoreCounter > 0){
            scoreCounter -= 10;
            score.setScore(scoreCounter);
            scoreText.setText(MessageFormat.format("Score : {0}", String.valueOf(score.getScore())));
        }else{
            scoreCounter = 0;
            score.setScore(scoreCounter);
        }
    }

    @Override
    protected void onPause() {
        prefre.saveScore(score.getScore());
        prefre.setState(currentQuestionIndex);
        super.onPause();
    }
}