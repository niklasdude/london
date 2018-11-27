package com.niklas.cp.citypopulation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String[] STORAGE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String INSTA_LINK = "https://www.instagram.com/eyeballapps/";

    private int currentApiVersion;

    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    FinalHandler myHandler;

    int hs_butler = 0;

    RelativeLayout btn_play;
    TextView driver_id, licence_level, license_location;
    Button instagram;
    Button customize;

    String name = "";
    String location = "";
    int level = 0;
    int pictureid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyPermissions();

        myHandler = new FinalHandler(this);
        btn_play = (RelativeLayout) findViewById(R.id.btn_play);
        instagram = (Button) findViewById(R.id.instagram);
        customize = (Button)findViewById(R.id.customize);


        hideUI();
        checkLicense();
        loadLicense();
        listenInstagram();
        listen();
        goCustom();
    }


    //License Loading
    private void checkLicense(){
        Cursor cursor = myHandler.showData();
        if(cursor.getCount()<=1){
            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            startActivity(intent);
        }
    }
    private void loadLicense(){

        driver_id = (TextView)findViewById(R.id.driver_id);
        licence_level = (TextView)findViewById(R.id.driver_level);
        license_location = (TextView)findViewById(R.id.driver_location);

        Cursor data  = myHandler.showData();

        if(data.getCount()!=0){
            data.moveToFirst();
            name = data.getString(1);
            location = data.getString(2);
            pictureid = data.getInt(3);
            if(data.getInt(4)!=0){
                level = data.getInt(4);}else{level = 777;}
        }else{
            makeToast("No data found..",1);
        }
        String level_string = "License Level: " + Integer.toString(level);

        driver_id.setText(name);
        license_location.setText(location);
        licence_level.setText(level_string);

    }

    //Listeners
    private void listenInstagram(){
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(INSTA_LINK);
            }
        });
    }
    private void listen(){
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
    }

    //Start Activities
    private void startGame(){
        Intent startIntent = new Intent(MainActivity.this,FinalGameActivity.class);
        startIntent.putExtra("HIGHSCORE",level);
        MainActivity.this.startActivity(startIntent);

    }
    private void goCustom(){
        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });
    }
    private void openLink(String link){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(INSTA_LINK));
        startActivity(intent);



    }

    //Basic

    private void makeToast(String message, int duration){

        Toast toast = Toast.makeText(this,message,duration);
        toast.show();

    }
    private void verifyPermissions(){

        Log.d(TAG, "verifyPermissions: Checking Permissions");

        int permissionExternalMemory = ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionExternalMemory != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this, STORAGE_PERMISSIONS, 1);

        }

    }

    //UI Methods
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }
    private void hideUI(){

        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(uiOptions);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(uiOptions);
                            }
                        }
                    });
        }
    }
}
