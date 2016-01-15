package com.nikmc.kc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    AutoCompleteTextView mFIO, mCity, mStreet, mHouse, mPhone;Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.login_layout);
        setContentView(R.layout.app_bar_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFIO = (AutoCompleteTextView) findViewById(R.id.et_fio);
        mCity = (AutoCompleteTextView) findViewById(R.id.et_city);
        mStreet = (AutoCompleteTextView) findViewById(R.id.et_street);
        mHouse = (AutoCompleteTextView) findViewById(R.id.et_house);
        mPhone = (AutoCompleteTextView) findViewById(R.id.et_telephone);
        btnLogin = (Button) findViewById(R.id.btnLogin);
//        mPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        filterFIO();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSentMessage();
            }
        });
    }

    private void filterFIO(){
        InputFilter customFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!(Character.isLetter(source.charAt(i)) || Character.isSpaceChar(source.charAt(i)))) {
                        return "";
                    }
                }
                return null;
            }
        };
        mFIO.setFilters(new InputFilter[]{ customFilter});
    }

    private void attemptSentMessage() {
/*
        mFIO.setError(null);
        mCity.setError(null);
        mStreet.setError(null);
        mHouse.setError(null);
        mPhone.setError(null);
*/

        String fio = mFIO.getText().toString();
        String city = mCity.getText().toString();
        String street = mStreet.getText().toString();
        String house = mHouse.getText().toString();
        String phone = mPhone.getText().toString();

        /*boolean cancel = false;
        View focusView = null;

        if (!CheckValid.isPhoneValid(phone.replaceAll("[^0-9+]",""))) {
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
        } else {*/
            saveData(fio, phone.replaceAll("[^0-9]", ""), city, street, house);
            startActivity(new Intent(LoginActivity.this, SubmissionForm.class));
            finish();
//        }

    }

    private void saveData(String fio, String phone, String city, String street, String house ) {
        SharedPreferences preferences = getSharedPreferences("KCpref", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean("key_first_launch",false);
        prefEditor.putString("key_preference_fio",fio);
        prefEditor.putString("key_preference_phone",phone);
        prefEditor.putString("key_preference_city",city);
        prefEditor.putString("key_preference_street",street);
        prefEditor.putString("key_preference_house",house);
        prefEditor.commit();

    }


}
