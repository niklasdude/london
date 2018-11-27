package com.niklas.cp.citypopulation;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class FinalGameActivity extends AppCompatActivity {

    private int currentApiVersion;

    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    int bound;

    int secondDelay = 500;

    int buttonID;

    int low;

    ArrayList<String> al_cities = new ArrayList<>();
    ArrayList<String> al_countries = new ArrayList<>();
    ArrayList<String> al_populations = new ArrayList<>();

    InputStream stream;

    boolean startGame;

    int scoredLast;
    int rand1 = 0;
    int rand2 = 0;
    int rand3 = 0;

    int highscore = 0;

    RelativeLayout rMore, rLess;

    TextView tv_upperLocation, tv_upperPopulation, tv_lowerLocation, tv_lowerPopulation, tv_Back, tv_less,  tv_more, tv_score, tv_highscore;
    ImageView iv_upperImage, iv_lowerImage;

    FrameLayout lowerCard, upperCard;

    String[] dataA = new String[3];
    String[] dataB = new String[3];
    String locationX, locationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        makeInvisible();
        hideUI();

        setHighscore();
        setScore();

        rMore = (RelativeLayout)findViewById(R.id.rel_More);
        rLess = (RelativeLayout)findViewById(R.id.rel_Less);

        slideCards();

        tv_less = (TextView)findViewById(R.id.less_btn);
        tv_more = (TextView)findViewById(R.id.more_btn);

        al_cities = readFiles(0);
        al_countries = readFiles(1);
        al_populations = readFiles(2);

        bound = al_cities.size()-1;

        startGame = gameStarter(al_cities,al_countries,al_populations);

        rLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rLess.setClickable(false);
                rMore.setClickable(false);
                animateTextView(0,low,tv_lowerPopulation);
                buttonID = view.getId();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(startGame==true){
                            playAnimations(false);
                            Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    goToEndScreen(highscore);
                                }
                            },secondDelay);

                        }else{
                            playAnimations(true);
                            Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateScore();
                                    makeInvisible();
                                    startGame = nextData(dataB);
                                    slideCards();
                                }
                            },secondDelay);
                        }
                    }
                }, 1000);
            }
        });
        rMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rLess.setClickable(false);
                rMore.setClickable(false);
                animateTextView(0,low,tv_lowerPopulation);
                buttonID = view.getId();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(startGame==false){
                            playAnimations(false);
                            Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    goToEndScreen(highscore);
                                }
                            },secondDelay);
                        }else{
                            playAnimations(true);
                            Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateScore();
                                    makeInvisible();
                                    startGame = nextData(dataB);
                                    slideCards();
                                }
                            },secondDelay);

                        }
                    }
                }, 1000);
            }
        });
        backToMainMenu();
    }

    //Get Data

    private ArrayList<String> readFiles(int x){
        ArrayList<String> daisy = new ArrayList<>();

        if(x==0){stream = getResources().openRawResource(R.raw.file_cities);}
        else if(x==1){ stream = getResources().openRawResource(R.raw.file_countries);}
        else{stream = getResources().openRawResource(R.raw.file_numbers);}

        Scanner scanner = new Scanner(stream);
        while(scanner.hasNextLine()){
            String element = scanner.nextLine();
            daisy.add(element); }
        scanner.close();

        return daisy;
    }

    //Animations

    private void playAnimations(boolean roW){

        lowerCard = (FrameLayout)findViewById(R.id.lower_card);

        Drawable backgrounds[] = new Drawable[2];
        Resources res = getResources();

        backgrounds[0]=res.getDrawable(R.drawable.button_white);
        if(roW==true){
            backgrounds[1]=res.getDrawable(R.drawable.button_green);
        }else{
            backgrounds[1]=res.getDrawable(R.drawable.button_red);
        }

        final TransitionDrawable crossfader = new TransitionDrawable(backgrounds);

        lowerCard.setBackgroundDrawable(crossfader);
        final int duration = 250;
        crossfader.startTransition(50);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                crossfader.reverseTransition(duration);
            }
        }, duration);

    }
    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                String number = valueAnimator.getAnimatedValue().toString();
                int reference = Integer.parseInt(number);

                StringBuilder str = new StringBuilder(number);
                int idx = str.length() - 3;

                while (idx > 0)
                {
                    str.insert(idx, ".");
                    idx = idx - 3;
                }
                String myText = str.toString();

                textview.setText(myText);


            }
        });
        valueAnimator.start();
    }
    private void makeInvisible(){
        upperCard = (FrameLayout)findViewById(R.id.upperCard);
        lowerCard = (FrameLayout)findViewById(R.id.lower_card);

        upperCard.setVisibility(View.INVISIBLE);

        lowerCard.setVisibility(View.INVISIBLE);

    }
    private void slideCards(){

        upperCard = (FrameLayout)findViewById(R.id.upperCard);
        lowerCard = (FrameLayout)findViewById(R.id.lower_card);

        upperCard.setAlpha(0.0f);
        upperCard.setVisibility(View.VISIBLE);
        upperCard.animate()
                .setDuration(500)
                .alpha(1.0f)
                .setListener(null);
        lowerCard.setAlpha(0.0f);
        lowerCard.setVisibility(View.VISIBLE);
        lowerCard.animate()
                .setDuration(1000)
                .alpha(1.0f)
                .setListener(null);

        rLess.setClickable(true);
        rMore.setClickable(true);


    }

    //Game Functions

    public boolean gameStarter(ArrayList<String> cities, ArrayList<String> countries, ArrayList<String> populations){

        rand1 = getRandomNumber();
        int checker = 0;
        while(checker==0){
            rand2 = getRandomNumber();
            if(rand1!=rand2){checker=1;}else{checker=0;makeToast("Mr Burns was shot",0);}
        }

        dataA[0] = cities.get(rand1);
        dataA[1] = countries.get(rand1);
        dataA[2] = populations.get(rand1);
        dataB[0] = cities.get(rand2);
        dataB[1] = countries.get(rand2);
        dataB[2] = populations.get(rand2);

        setContent(dataA,dataB);

        int up = stringToInt(dataA[2]);
        low = stringToInt(dataB[2]);

        if (low>up){return true;}else{return false;}
    }
    private boolean nextData(String[]lastData){

        upperCard = (FrameLayout)findViewById(R.id.upperCard);
        lowerCard = (FrameLayout)findViewById(R.id.lower_card);

        dataA[0]=lastData[0];
        dataA[1]=lastData[1];
        dataA[2]=lastData[2];

        int checker = 0;

        while(checker==0){
            rand3 = getRandomNumber();
            if(rand2!=rand3){checker=1;}else{checker=0;makeToast("Mr burns was shot..",0);}
        }

        rand2 = rand3;

        dataB[0]=al_cities.get(rand2);
        dataB[1]=al_countries.get(rand2);
        dataB[2]=al_populations.get(rand2);

        setContent(dataA,dataB);

        int up = stringToInt(dataA[2]);
        low = stringToInt(dataB[2]);
        tv_lowerPopulation.setText("?.???.???");
        if (low>up){return true;}else{return false; }
    }
    private void setContent(String[] a, String[] b){

        tv_upperLocation = (TextView)findViewById(R.id.tv_upper_location);
        tv_upperPopulation = (TextView)findViewById(R.id.tv_upper_population);
        tv_lowerLocation = (TextView)findViewById(R.id.tv_lowerLocation);
        tv_lowerPopulation = (TextView)findViewById(R.id.tv_lowerPopulation);

        tv_upperLocation.setText(makeHeader(a[0],a[1]));
        tv_lowerLocation.setText(makeHeader(b[0],b[1]));
        tv_upperPopulation.setText(a[2]);

        setImages(a,b);
    }
    private void setImages(String[]a,String[]b){

        iv_upperImage = (ImageView)findViewById(R.id.upper_imageview);
        iv_lowerImage = (ImageView)findViewById(R.id.iv_lowerImage);

        locationX = a[0].toLowerCase();
        locationY = b[0].toLowerCase();

        String helperX = removeSpaces(locationX);
        String helperY = removeSpaces(locationY);



        int resA = createResID(helperX);
        int resB = createResID(helperY);

        iv_upperImage.setImageResource(resA);
        iv_lowerImage.setImageResource(resB);
    }
    private int createResID(String locResID){

        int resID = getResources().getIdentifier(locResID,"drawable","com.niklas.cp.citypopulation");
        return resID;

    }


    //Score Functions

    private void updateScore(){
        tv_score = (TextView)findViewById(R.id.tv_score);
        highscore = highscore +1;
        String scoreTXT = "Score: " + String.valueOf(highscore);
        tv_score.setText(scoreTXT);
    }
    private void setScore(){
        tv_score = (TextView)findViewById(R.id.tv_score);
        Intent intent = getIntent();
        scoredLast = intent.getIntExtra("scoredLast",0);
        String score = "Score: " + scoredLast;
        tv_score.setText(score);
    }
    public void setHighscore(){
        tv_highscore = (TextView)findViewById(R.id.tv_highscore);
        Intent intent = getIntent();
        int myHS = intent.getIntExtra("HIGHSCORE",0);
        String highscoreText = "Highscore: " + String.valueOf(myHS);
        tv_highscore.setText(highscoreText);
    }

    //Navigation

    private void goToEndScreen(int hs){

        Intent intent = new Intent(FinalGameActivity.this, EndCardActivity.class);
        intent.putExtra("SCORE",hs);
        startActivity(intent);

    }
    private void backToMainMenu(){
        tv_Back = (TextView)findViewById(R.id.tv_back);
        tv_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalGameActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Basic Text and Numbers

    private int getRandomNumber(){
        Random rand = new Random();
        int rn = rand.nextInt(bound)+1;
        return rn;
    }
    private String makeHeader(String a, String b){
        String header = a + " , " + b;
        return header;
    }
    private int stringToInt(String input){
        String mystring = input.replaceAll("\\.","");
        final int myint = Integer.parseInt(mystring);
        return myint;

    }
    private String removeSpaces(String myString){
        String output = myString.replaceAll("\\s","");
        return output;
    }
    private void makeToast(String msg,int drt){
        Toast toast = Toast.makeText(this,msg,drt);
        toast.show();
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
