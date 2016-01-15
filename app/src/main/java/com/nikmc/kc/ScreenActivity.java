package com.nikmc.kc;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class ScreenActivity extends FragmentActivity {

    private static final int MILLIS_PER_SECOND = 1000;
    private static final int SECONDS_TO_COUNTDOWN = 3;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_screen);

    }

    @Override
    protected void onStart() {
        super.onStart();

        showTimer(SECONDS_TO_COUNTDOWN * MILLIS_PER_SECOND);


        /*if (ScreenActivity.this.getSharedPreferences("KCpref", ScreenActivity.this.MODE_PRIVATE).getString("key_preference_phone", "").equals("")) {
            startActivity(new Intent(ScreenActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(ScreenActivity.this, SubmissionForm.class));
        }
        finish();*/
    }

    private void showTimer(int countdownMillis) {
        if(timer != null) { timer.cancel(); }
        timer = new CountDownTimer(countdownMillis, MILLIS_PER_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {

                if (ScreenActivity.this.getSharedPreferences("KCpref", ScreenActivity.this.MODE_PRIVATE).getBoolean("key_first_launch", true)) {
                    timer.cancel();
                    startActivity(new Intent(ScreenActivity.this, LoginActivity.class));
                } else {
                    timer.cancel();
                    startActivity(new Intent(ScreenActivity.this, SubmissionForm.class));
                }
                finish();
            }
        }.start();
    }
}
