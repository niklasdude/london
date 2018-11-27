package com.niklas.cp.citypopulation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EndCardActivity extends AppCompatActivity {

    FinalHandler myHandler;

    FrameLayout startGame, keepGoing;

    TextView tvGameScore, tvPerMsg;

    private int currentApiVersion, gameScore, highScore;

    String msg="Not bad for a round trip!";

    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endcard);
        hideUI();

        myHandler = new FinalHandler(this);

        gameScore = getGameScore();
        highScore = getHighscoreFromDB();
        checkHighscores();
        setMainContent();
        listenPlay();
    }
    private void startNewGame(boolean x){
        Intent intent = new Intent(EndCardActivity.this,FinalGameActivity.class);
        int score;

        if(x==true){
            score = gameScore;
        }else{
            score = 0;
        }
        intent.putExtra("scoredLast",score);
        startActivity(intent);

    }
    private void setMainContent(){
        tvGameScore = (TextView)findViewById(R.id.tvGameScore);

        String gScore = Integer.toString(gameScore);
        tvGameScore.setText(gScore);
    }
    private void listenPlay(){

        startGame = (FrameLayout)findViewById(R.id.frameStartGame);
        keepGoing = (FrameLayout)findViewById(R.id.frameKeepGoing);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(false);
            }
        });
        keepGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(true);
            }
        });
    }
    private int getGameScore(){
        Intent intent = getIntent();
        int x;
        x = intent.getIntExtra("SCORE",0);
        return x;
    }
    private int getHighscoreFromDB(){
        Cursor cursor = myHandler.showData();
        cursor.moveToFirst();
        int x = cursor.getInt(4);
        return x;
    }
    private void checkHighscores(){
        tvPerMsg = (TextView)findViewById(R.id.tvPersonalMessage);
        if (gameScore>highScore){msg  = "YAY, thats a new highscore!!";myHandler.updateHighscore(gameScore);}
        tvPerMsg.setText(msg);
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
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }
    private void makeToast(String msg, int drt){

        Toast toast = Toast.makeText(this,msg,drt);
        toast.show();


    }
}
