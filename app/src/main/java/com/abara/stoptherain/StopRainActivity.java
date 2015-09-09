package com.abara.stoptherain;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.abara.stoptherain.utils.PreferenceIds;

import java.util.Random;

/**
 * Created by Abb on 8/28/2015.
 */
public class StopRainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView quizText, stopRain;
    private AppCompatEditText answerText;
    private String[] quizes;
    private String[] answers;

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_rain);

        quizText = (TextView) findViewById(R.id.quiz_desc);
        stopRain = (TextView) findViewById(R.id.stop_rain_btn);
        answerText = (AppCompatEditText) findViewById(R.id.answer_textbox);
        stopRain.setOnClickListener(this);

        quizes = getResources().getStringArray(R.array.quizes);
        answers = getResources().getStringArray(R.array.answers);

    }

    @Override
    protected void onResume() {
        super.onResume();

        quizText.setText(getQuiz(quizes));

    }


    private String getQuiz(String[] items) {
        index = new Random().nextInt(items.length);
        return items[index];
    }

    @Override
    public void onClick(View v) {

        String ans = answerText.getText().toString().toLowerCase();

        if(index == 4){
            if (ans.contains("-40")) {
                stopRain();
            } else {
                cannotStopRain();
            }
        }else{

            if (ans.contentEquals(answers[index])) {
                stopRain();
            } else {
                cannotStopRain();
            }
        }

    }

    private void cannotStopRain() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Incorrect!");
        builder.setMessage("Your answer to the question is incorrect. The rain cannot be stopped by the app. Try again later!");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        builder.show();

    }

    private void stopRain() {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().putInt(PreferenceIds.USER_STOPPED_COUNT_KEY,prefs.getInt(PreferenceIds.USER_STOPPED_COUNT_KEY,0)+1).commit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success!");
        builder.setMessage("A Request has been sent to stop the rain. It will be stopped soon. Visit My profile section for more details.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        builder.show();



    }
}
