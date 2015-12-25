package com.nikmc.kc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class ScreenActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_screen);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ScreenActivity.this.getSharedPreferences("KCpref", ScreenActivity.this.MODE_PRIVATE).getString("key_preference_phone","").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this,SubmissionForm.class));
        }
        finish();
    }
}
