package com.niklas.cp.citypopulation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SummitActivity extends AppCompatActivity {

    private int currentApiVersion;

    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    int highscore_db;
    int score = 0;
    TextView tv_scr, tv_msg;
    String message;
    FinalHandler myHandler;

    RelativeLayout newGame, keepGoing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summit);

        hideUI();

        newGame = (RelativeLayout)findViewById(R.id.relNewGame);
        keepGoing = (RelativeLayout)findViewById(R.id.relKeepGoing);

        tv_msg = (TextView)findViewById(R.id.tv_endCardMessage);

        myHandler = new FinalHandler(this);

        getGameIntent();
        checkHighscores();

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(0);
            }
        });
        keepGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(1);
            }
        });
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
    private void checkHighscores(){
        highscore_db = getHighscoreFromDB();
        if (score>highscore_db){message  = "YAY, thats a new highscore!!";myHandler.updateHighscore(score);}
        tv_msg.setText(message);
    }
    private int getHighscoreFromDB(){

        Cursor cursor = myHandler.showData();
        cursor.moveToFirst();
        int x = cursor.getInt(4);
        return x;
    }
    private void startNewGame(int x){

        Intent rose = new Intent(SummitActivity.this, FinalGameActivity.class);

        if(x==0){
            score = 0;
        }else{
        }
        rose.putExtra("scoredLast",score);
        SummitActivity.this.startActivity(rose);
    }
    private void getGameIntent(){

        tv_scr = (TextView)findViewById(R.id.score_endCard);

        Intent intent = getIntent();
        score = intent.getIntExtra("SCORE",0);
        tv_scr.setText(Integer.toString(score));
        updateMessage(score);


    }
    private void updateMessage(int score){
        if(score>=0){message = "That's a poor score.. keep trying!";}
        else if(score>=3){message = "You're getting better! Keep going!";}
        else if(score>=7){message = "Wow, you're better than most players, keep improving!";}
        else if(score>=10){message = "You should get a trophy! Can you beat your own score?";}
        else if(score>=20){message = "That's just ridiculous.. Can you do it again?";}
        tv_msg.setText(message);
    }
    private void makeToast(String msg, int drt){

        Toast toast = Toast.makeText(this,msg,drt);
        toast.show();


    }

}
