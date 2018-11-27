package com.niklas.cp.citypopulation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";

    private int currentApiVersion;

    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    FinalHandler myHandler;

    Button Germany, Italy, Australia;
    EditText enterName;
    FrameLayout pic_one, pic_two, box_one, box_two;

    TextView tvBack,tvSave;

    String name = "";
    String license_location = "";

    int picID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        hideUI();


        myHandler = new FinalHandler(this);
        Germany = (Button)findViewById(R.id.select_germany);
        Italy = (Button)findViewById(R.id.select_italy);
        Australia = (Button)findViewById(R.id.select_australia);

        enterName = (EditText)findViewById(R.id.name_enter);

        pic_one = (FrameLayout)findViewById(R.id.picture_one);
        pic_two = (FrameLayout)findViewById(R.id.picture_two);

        box_one = (FrameLayout)findViewById(R.id.colorbox_one);
        box_two = (FrameLayout)findViewById(R.id.colorbox_two);

        tvBack = (TextView)findViewById(R.id.tv_backCustom);
        tvSave = (TextView)findViewById(R.id.tv_saveOptions);

        Cursor cursor = myHandler.showData();
        if(cursor.getCount()!=0){showData();}else{makeToast("No data was found",0);
            Log.d(TAG,"No data was found");}
        listenPics();
        listenCountries();
        listenName();
        listenBack();
        saveAndExit();



    }
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
    public void listenBack(){

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });

    }
    public void showData(){
        Cursor cursor = myHandler.showData();
        cursor.moveToFirst();
        name = cursor.getString(1);
        license_location = cursor.getString(2);
        picID = cursor.getInt(3);
        setColors(picID);
        enterName.setText(name);
    }
    public void setColors(int picID){
            if(picID==1){
                box_one.setBackgroundColor(getResources().getColor(R.color.reddy));
                box_two.setBackgroundColor(getResources().getColor(R.color.whiterthanwhite));
            }else{
                box_two.setBackgroundColor(getResources().getColor(R.color.reddy));
                box_one.setBackgroundColor(getResources().getColor(R.color.whiterthanwhite));
            }
    }
    public void save() {
        name = enterName.getText().toString();
        Cursor cursor = myHandler.showData();
        cursor.moveToFirst();
        boolean hasData;
        if(cursor.getCount()<=1){hasData=false;}else{hasData=true;}
        if(name.equals("")||license_location.equals("")||picID==0){
            makeToast("You are missing information.. ;)",1);
        }else{

            if(hasData==false){
                myHandler.addData(name,license_location,picID);
                makeToast("Data saved succesfully!",1);
            }else{
                int id = 1;
                myHandler.updateName(name,license_location,picID,id);
                makeToast("Data updated succesfully!",1);
                Log.d(TAG,"New player data was saved sucessfull: " + name + " from " + license_location + " , picID + " + picID);

            }
            exit();
        }
    }
    public void exit(){
        Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void saveAndExit(){
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }
    public void listenName(){
        name = enterName.getText().toString();
    }
    public void listenCountries(){
        Germany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                license_location="Germany";
            }
        });
        Italy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                license_location="Italy";
            }
        });
        Australia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                license_location="Australia";
            }
        });


    }
    public void listenPics(){

        pic_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picID = 1;
                box_one.setBackgroundColor(getResources().getColor(R.color.reddy));
                box_two.setBackgroundColor(getResources().getColor(R.color.whiterthanwhite));
            }
        });
        pic_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picID = 2;
                box_two.setBackgroundColor(getResources().getColor(R.color.reddy));
                box_one.setBackgroundColor(getResources().getColor(R.color.whiterthanwhite));
            }
        });




    }
    public void makeToast(String msg, int drt){

        Toast toast = Toast.makeText(this,msg,drt);
        toast.show();

    }

}
