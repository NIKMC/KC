package com.nikmc.kc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.nikmc.kc.logic.CheckValid;

public class MyProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    AutoCompleteTextView mFIO, mCity, mStreet, mHouse, mPhone;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.profile_layout);
        //setContentView(R.layout.activity_my_profile);
        mFIO = (AutoCompleteTextView) findViewById(R.id.et_fio);
        mCity = (AutoCompleteTextView) findViewById(R.id.et_city);
        mStreet = (AutoCompleteTextView) findViewById(R.id.et_street);
        mHouse = (AutoCompleteTextView) findViewById(R.id.et_house);
        mPhone = (AutoCompleteTextView) findViewById(R.id.et_telephone);
        btnSave = (Button) findViewById(R.id.btnSave);
//        mPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        filterFIO();
        doInt();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSentMessage();

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    private void doInt() {

        mFIO.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_fio", ""));
        mPhone.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_phone", ""));
        mCity.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_city", ""));
        mStreet.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_street", ""));
        mHouse.setText(MyProfile.this.getSharedPreferences("KCpref", MyProfile.this.MODE_PRIVATE).getString("key_preference_house", ""));
    }

    private void attemptSentMessage() {
/*

        mFIO.setError(null);
        mCity.setError(null);
        mStreet.setError(null);
        mHouse.setError(null);
*/        mPhone.setError(null);


        String fio = mFIO.getText().toString();
        String city = mCity.getText().toString();
        String street = mStreet.getText().toString();
        String house = mHouse.getText().toString();
        String phone = mPhone.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!CheckValid.isPhoneValidProfile(phone)) {
            mPhone.setError(getResources().getString(R.string.phone_valid));
            focusView = mPhone;
            cancel = true;
        }
       /* if (CheckValid.isEmptyValid(fio)) {
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
*/
        if (cancel) {
            focusView.requestFocus();
//            btnSave.requestFocusFromTouch();
        } else {
            saveData(fio, phone.replaceAll("[^0-9+]", ""), city, street, house);
            Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(getApplicationContext(), SubmissionForm.class);
            setResult(RESULT_OK, intent);
            finish();*/
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_request) {
            Intent intent = new Intent(getApplicationContext(), SubmissionForm.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_myRequest) {
            Toast.makeText(this, "Закрыто", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), MyProfile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
