package com.nikmc.kc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.nikmc.kc.api.Request;
import com.nikmc.kc.api.kc.SendRequestClient;
import com.nikmc.kc.logic.CheckValid;
import com.nikmc.kc.logic.GetImagePath;
import com.nikmc.kc.logic.ImageConvert;
import com.nikmc.kc.model.Bid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubmissionForm extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Request> , NavigationView.OnNavigationItemSelectedListener  {

    AutoCompleteTextView/* mFIO,*/ mCity, mStreet, mHouse, mPhone;
    EditText mFIO;//, mCity, mStreet, mHouse, mPhone;
    Button btnSendMessage;
    EditText mMessage;
    ImageButton image1, image2, image3;
    private Bid bidrequest;
    private static final int PROFILE_REQUEST = 0;
    private static final int GALLERY_REQUEST1 = 1;
    private static final int GALLERY_REQUEST2 = 2;
    private static final int GALLERY_REQUEST3 = 3;
    private boolean LoadImage = false;
    private Uri selectedImage;
    private Bitmap mEditImage1 = null, mEditImage2 = null, mEditImage3 = null;
    private ImageView mImageAvatar;
    private static final String LOG = "Форма отправки";
    private ArrayList<String> imageString = new ArrayList<>();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_submission);
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
        getSupportActionBar().setCustomView(R.layout.abc_layout);
        //setContentView(R.layout.activity_submission_form);
        //mFIO = (AutoCompleteTextView) findViewById(R.id.et_fio);
        mCity = (AutoCompleteTextView) findViewById(R.id.et_city);
        mStreet = (AutoCompleteTextView) findViewById(R.id.et_street);
        mHouse = (AutoCompleteTextView) findViewById(R.id.et_house);
        mPhone = (AutoCompleteTextView) findViewById(R.id.et_telephone);
        mFIO = (EditText) findViewById(R.id.et_fio);
        /*mCity = (EditText) findViewById(R.id.et_city);
        mStreet = (EditText) findViewById(R.id.et_street);
        mHouse = (EditText) findViewById(R.id.et_house);
        mPhone = (EditText) findViewById(R.id.et_telephone);
        */
        btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        mMessage = (EditText) findViewById(R.id.et_message);
        image1 = (ImageButton) findViewById(R.id.Image1);
        image2 = (ImageButton) findViewById(R.id.Image2);
        image3 = (ImageButton) findViewById(R.id.Image3);
//        mPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        filterFIO();

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

    @Override
    protected void onResume() {
        super.onResume();
        doInt();
    }

    private void doInt() {

        mFIO.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_fio", ""));
        mPhone.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_phone", ""));
        mCity.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_city", ""));
        mStreet.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_street", ""));
        mHouse.setText(SubmissionForm.this.getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_preference_house", ""));
        field();
    }

    private void field() {
        mFIO.setError(null);
        mCity.setError(null);
        mStreet.setError(null);
        mHouse.setError(null);
        mPhone.setError(null);
        mMessage.setError(null);

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

        if (CheckValid.isValid(fio.trim())) {
            mFIO.setError(CheckValid.isEmptyFIOValid(fio.trim()));
            focusView = mFIO;
            cancel = true;
        }
        if (!CheckValid.isPhoneValid(phone)) {
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
//            btnSendMessage.requestFocusFromTouch();
        } else {
            bidrequest = new Bid(fio.trim().replaceAll("[!\"#$%&'()*+,-./:;<=>?@^_`{|}~]", "").trim(), phone.replaceAll("[^0-9]", ""), city, street, house, message);
            if(mEditImage1 != null)
                imageString.add(ImageConvert.convertIntoBase64(mEditImage1));
            if(mEditImage2 != null)
                imageString.add(ImageConvert.convertIntoBase64(mEditImage2));
            if(mEditImage3 != null)
                imageString.add(ImageConvert.convertIntoBase64(mEditImage3));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());

            if (getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_send_message", "").isEmpty() ){

            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Отправка заявки. Подождите...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Loader loader = getSupportLoaderManager().initLoader(0, null, this);
            loader.forceLoad();

            } else  if(Integer.parseInt(currentDateandTime.substring(currentDateandTime.indexOf('_')+1)) - Integer.parseInt(getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_send_message", "").substring(getSharedPreferences("KCpref", SubmissionForm.this.MODE_PRIVATE).getString("key_send_message", "").indexOf('_')+1)) >100  ){
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Отправка заявки. Подождите...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Loader loader = getSupportLoaderManager().initLoader(0, null, this);
                loader.forceLoad();

            } else {
                Toast.makeText(this, "Подождите 1 мин.", Toast.LENGTH_SHORT).show();
            }
//            btnSendMessage.setVisibility(View.INVISIBLE);
            /*startActivity(new Intent(SubmissionForm.this, ConfirmationActivity.class));
            finish();*/
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            /*case PROFILE_REQUEST:
                if(resultCode == RESULT_OK) {
                doInt();
                }
                break;*/
            case GALLERY_REQUEST1:
                if(resultCode == RESULT_OK){
                    int px = getResources().getDimensionPixelSize(R.dimen.imageSize);
                    selectedImage = imageReturnedIntent.getData();
                    Log.d(LOG, "PATH = " + GetImagePath.getRealPathFromURI(getApplicationContext(), selectedImage));
                    image1.setImageURI(null);
                    image1.setImageBitmap(ImageConvert.decodeSampledBitmapFromResource(getApplicationContext(), selectedImage, px, px));
                    LoadImage = true;


                        if (selectedImage != null)
                            mEditImage1 = ImageConvert.decodeSampledBitmapFromResource(getApplicationContext(), selectedImage, px, px);//MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                    // imageHome.setImageBitmap(galleryPic);
                }
                else if (resultCode == RESULT_CANCELED) LoadImage = false;
                break;
            case GALLERY_REQUEST2:
                if(resultCode == RESULT_OK){
                    int px = getResources().getDimensionPixelSize(R.dimen.imageSize);
                    selectedImage = imageReturnedIntent.getData();
                    Log.d(LOG, "PATH = " + GetImagePath.getRealPathFromURI(getApplicationContext(), selectedImage));
                    image2.setImageURI(null);
                    image2.setImageBitmap(ImageConvert.decodeSampledBitmapFromResource(getApplicationContext(), selectedImage, px, px));
                    LoadImage = true;


                        if (selectedImage != null)
                            mEditImage2 = ImageConvert.decodeSampledBitmapFromResource(getApplicationContext(), selectedImage, px, px);//MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                    // imageHome.setImageBitmap(galleryPic);
                }
                else if (resultCode == RESULT_CANCELED) LoadImage = false;
                break;
            case GALLERY_REQUEST3:
                if(resultCode == RESULT_OK){
                    int px = getResources().getDimensionPixelSize(R.dimen.imageSize);
                    selectedImage =  imageReturnedIntent.getData();
                    Log.d(LOG, "PATH = " + GetImagePath.getRealPathFromURI(getApplicationContext(), selectedImage));
                    image3.setImageURI(null);
                    image3.setImageBitmap(ImageConvert.decodeSampledBitmapFromResource(getApplicationContext(), selectedImage, px, px));
                    LoadImage = true;


                        if (selectedImage != null)
                            mEditImage3 = ImageConvert.decodeSampledBitmapFromResource(getApplicationContext(), selectedImage, px, px);//MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                    // imageHome.setImageBitmap(galleryPic);
                }
                else if (resultCode == RESULT_CANCELED) LoadImage = false;
                break;

        }
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


  /*  @Override
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
    }*/


    @Override
    public Loader<Request> onCreateLoader(int id, Bundle args) {
        return (new SendRequestClient(this, "http://tempuri.org/SendReq", bidrequest, imageString));
    }

    @Override
    public void onLoadFinished(Loader<Request> loader, Request data) {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if (data.enumType != EnumType.ERROR) {
            if(data.enumType != EnumType.NOINTERNET) {
                if (!data.arrayList.isEmpty()) {
                    startActivity(new Intent(SubmissionForm.this, ConfirmationActivity.class).putExtra("number",(String)data.arrayList.get(0)));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    Log.d("DATATIME", "time = " + currentDateandTime);
                    SharedPreferences preferences = getSharedPreferences("KCpref", MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = preferences.edit();
                    prefEditor.putString("key_send_message", currentDateandTime);
                    prefEditor.commit();


                    finish();
                } else {
                    Toast.makeText(this, "Ошибка отправки данных, попробуйте снова", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Отсутствует подключение к сети", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Произошла ошибка, попробуйте снова", Toast.LENGTH_SHORT).show();
        }
//        btnSendMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Request> loader) {

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_request) {
            Intent intent = new Intent(getApplicationContext(), SubmissionForm.class);
            startActivity(intent);
        } else if (id == R.id.nav_myRequest) {
            Toast.makeText(this, "Закрыто", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
//            startActivityForResult(new Intent(SubmissionForm.this, MyProfile.class), PROFILE_REQUEST);
            startActivity(new Intent(SubmissionForm.this, MyProfile.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
