package com.nikmc.kc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmationActivity extends FragmentActivity {

    TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_confirmation);
        number = (TextView) findViewById(R.id.NumberText);
        if(getIntent()!= null)
        number.setText(getIntent().getStringExtra("number"));
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmationActivity.this, SubmissionForm.class));
                finish();
            }
        });
    }
}
