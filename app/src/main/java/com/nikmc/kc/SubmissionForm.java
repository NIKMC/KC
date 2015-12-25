package com.nikmc.kc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.nikmc.kc.logic.CheckValid;
import com.nikmc.kc.logic.GetImagePath;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SubmissionForm extends AppCompatActivity {

    AutoCompleteTextView mFIO, mCity, mStreet, mHouse, mPhone;
    Button btnSendMessage;
    EditText mMessage;
    ImageButton image1, image2, image3;


    private static final int PROFILE_REQUEST = 0;
    private static final int GALLERY_REQUEST1 = 1;
    private static final int GALLERY_REQUEST2 = 2;
    private static final int GALLERY_REQUEST3 = 3;
    private boolean LoadImage = false;
    private Uri selectedImage;
    private Bitmap mEditImage;
    private ImageView mImageAvatar;
    private static final String LOG = "Форма отправки";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abc_layout);
        setContentView(R.layout.activity_submission_form);
        mFIO = (AutoCompleteTextView) findViewById(R.id.et_fio);
        mCity = (AutoCompleteTextView) findViewById(R.id.et_city);
        mStreet = (AutoCompleteTextView) findViewById(R.id.et_street);
        mHouse = (AutoCompleteTextView) findViewById(R.id.et_house);
        mPhone = (AutoCompleteTextView) findViewById(R.id.et_telephone);
        btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        mMessage = (EditText) findViewById(R.id.et_message);
        image1 = (ImageButton) findViewById(R.id.Image1);
        image2 = (ImageButton) findViewById(R.id.Image2);
        image3 = (ImageButton) findViewById(R.id.Image3);
        mPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        doInt();

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST1);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST2);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST3);
            }
        });



        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSentMessage();
            }
        });

    }

    private void doInt() {

        mFIO.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_fio", ""));
        mPhone.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_phone", ""));
        mCity.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_city", ""));
        mStreet.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_street", ""));
        mHouse.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_house", ""));
    }

    private void attemptSentMessage() {

        mFIO.setError(null);
        mCity.setError(null);
        mStreet.setError(null);
        mHouse.setError(null);
        mPhone.setError(null);
        mMessage.setError(null);

        String fio = mFIO.getText().toString();
        String city = mCity.getText().toString();
        String street = mStreet.getText().toString();
        String house = mHouse.getText().toString();
        String phone = mPhone.getText().toString();
        String message = mMessage.getText().toString();

        boolean cancel = false;
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
        if (CheckValid.isEmptyValid(message)) {
            mMessage.setError(getResources().getString(R.string.empty_field));
            focusView = mMessage;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            Toast.makeText(SubmissionForm.this,"Успешно отправлено",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SubmissionForm.this, ConfirmationActivity.class));
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case PROFILE_REQUEST:
                if(resultCode == RESULT_OK) {
                doInt();
                }
                break;
            case GALLERY_REQUEST1:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    Log.d(LOG, "PATH = " + GetImagePath.getRealPathFromURI(getApplicationContext(), selectedImage));
                    image1.setImageURI(null);
                    image1.setImageURI(selectedImage);
                    LoadImage = true;

                    try {
                        if (selectedImage != null)
                            mEditImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // imageHome.setImageBitmap(galleryPic);
                }
                else if (resultCode == RESULT_CANCELED) LoadImage = false;
                break;
            case GALLERY_REQUEST2:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    Log.d(LOG, "PATH = " + GetImagePath.getRealPathFromURI(getApplicationContext(), selectedImage));
                    image2.setImageURI(null);
                    image2.setImageURI(selectedImage);
                    LoadImage = true;

                    try {
                        if (selectedImage != null)
                            mEditImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // imageHome.setImageBitmap(galleryPic);
                }
                else if (resultCode == RESULT_CANCELED) LoadImage = false;
                break;
            case GALLERY_REQUEST3:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    Log.d(LOG, "PATH = " + GetImagePath.getRealPathFromURI(getApplicationContext(), selectedImage));
                    image3.setImageURI(null);
                    image3.setImageURI(selectedImage);
                    LoadImage = true;

                    try {
                        if (selectedImage != null)
                            mEditImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // imageHome.setImageBitmap(galleryPic);
                }
                else if (resultCode == RESULT_CANCELED) LoadImage = false;
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            startActivityForResult(new Intent(SubmissionForm.this, MyProfile.class), PROFILE_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }

}
