package com.nikmc.kc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.nikmc.kc.logic.CheckValid;

public class MyProfile extends AppCompatActivity {
    AutoCompleteTextView mFIO, mCity, mStreet, mHouse, mPhone;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.profile_layout);
        setContentView(R.layout.activity_my_profile);
        mFIO = (AutoCompleteTextView) findViewById(R.id.et_fio);
        mCity = (AutoCompleteTextView) findViewById(R.id.et_city);
        mStreet = (AutoCompleteTextView) findViewById(R.id.et_street);
        mHouse = (AutoCompleteTextView) findViewById(R.id.et_house);
        mPhone = (AutoCompleteTextView) findViewById(R.id.et_telephone);
        btnSave = (Button) findViewById(R.id.btnSave);
        mPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        doInt();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSentMessage();

            }
        });
    }

    private void doInt() {

        mFIO.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_fio", ""));
        mPhone.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_phone", ""));
        mCity.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_city", ""));
        mStreet.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_street", ""));
        mHouse.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_house", ""));
    }

    private void attemptSentMessage() {

        mFIO.setError(null);
        mCity.setError(null);
        mStreet.setError(null);
        mHouse.setError(null);
        mPhone.setError(null);

        String fio = mFIO.getText().toString();
        String city = mCity.getText().toString();
        String street = mStreet.getText().toString();
        String house = mHouse.getText().toString();
        String phone = mPhone.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!CheckValid.isPhoneValid(phone.replaceAll("[^0-9+]", ""))) {
            mPhone.setError(getResources().getString(R.string.phone_valid));
            focusView = mPhone;
            cancel = true;
        }
        if (CheckValid.isEmptyValid(fio)) {
            mFIO.setError(getResources().getString(R.string.empty_field));
            focusView = mFIO;
            cancel = true;
        }
        if (CheckValid.isEmptyValid(city)) {
            mCity.setError(getResources().getString(R.string.empty_field));
            focusView = mCity;
            cancel = true;
        }
        if (CheckValid.isEmptyValid(street)) {
            mStreet.setError(getResources().getString(R.string.empty_field));
            focusView = mStreet;
            cancel = true;
        }
        if (CheckValid.isEmptyValid(house)) {
            mHouse.setError(getResources().getString(R.string.empty_field));
            focusView = mHouse;
            cancel = true;
        }
        if (CheckValid.isEmptyValid(phone)) {
            mPhone.setError(getResources().getString(R.string.empty_field));
            focusView = mPhone;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            saveData(fio, phone, city, street, house);
            Intent intent = new Intent(getApplicationContext(), SubmissionForm.class);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    private void saveData(String fio, String phone, String city, String street, String house ) {
        SharedPreferences preferences = getSharedPreferences("KCpref", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString("key_preference_fio",fio);
        prefEditor.putString("key_preference_phone",phone);
        prefEditor.putString("key_preference_city",city);
        prefEditor.putString("key_preference_street",street);
        prefEditor.putString("key_preference_house",house);
        prefEditor.commit();

    }


}
