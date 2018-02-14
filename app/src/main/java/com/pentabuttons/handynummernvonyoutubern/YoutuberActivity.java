package com.pentabuttons.handynummernvonyoutubern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.penta.games.handynummernvonyoutuber.R;


public class YoutuberActivity extends AppCompatActivity {

    TextView zahlenfeld;
    int klickzahl;
    int normalbild, kleinbild, bildID,zahlenfeldID;
    String zahlname;
    ImageView image;
    RelativeLayout relativeLayout;
    MediaPlayer mp;
    TextView plottwist;
    ImageView rewardButton;
    ImageView ratingButton;
    public RewardedVideoAd mAd;
    ScrollView scrollView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtuber_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bildID = R.id.image;

        //+++++++++++++++++++++++++++

        switch (MainActivity.youtuber+""){
            case "youtuber_layout":
                klickzahl= MainActivity.lukaszahl;
                zahlname="lukaszahl";
                normalbild=R.drawable.lukas;
                kleinbild=R.drawable.lukasklein;
                break;
            case "mikesinger":
                klickzahl= MainActivity.mikezahl;
                zahlname="mikezahl";
                normalbild=R.drawable.mikesinger;
                kleinbild=R.drawable.mikesingerklein;
                break;
            case "julienbam":
                klickzahl= MainActivity.julienzahl;
                zahlname="julienzahl";
                normalbild=R.drawable.juliennew;
                kleinbild=R.drawable.julienkleinnew;
                break;
            case "bibisbeautypalace":
                klickzahl= MainActivity.bibizahl;
                zahlname="bibizahl";
                normalbild=R.drawable.bibi;
                kleinbild=R.drawable.bibiklein;
                break;
            case "tanzverbot":
                klickzahl= MainActivity.tanzizahl;
                zahlname="tanzizahl";
                normalbild=R.drawable.tanzi;
                kleinbild=R.drawable.tanziklein;
                break;
            case "dagibee":
                klickzahl= MainActivity.dagizahl;
                zahlname="dagizahl";
                normalbild=R.drawable.dagi;
                kleinbild=R.drawable.dagiklein;
                break;

        }




        //+++++++++++++++++++++++++++




        //Initialisierung
        plottwist = (TextView)findViewById(R.id.plottwist);
        rewardButton = (ImageView)findViewById(R.id.rewardButton);
        ratingButton = (ImageView)findViewById(R.id.ratingButton);
        zahlenfeld = (TextView)findViewById(R.id.zahlenfeld);
        image = (ImageView)findViewById(bildID);
        scrollView = (ScrollView)findViewById(R.id.mScrollView);


        anfangsLayoutErstellen();

        //Button Klicks
        click();
        rating();
        rewardAd();

        //Werbung
        adListener();
        loadAd();


        ifGameIsOver();

        if(MainActivity.userRatedUs){
            ratingButton.setVisibility(View.INVISIBLE);
        }
    }

    /** +++++++++ BUTTONS Start +++++++++++++ */

    //Click auf den Kopf vom YouTuber
    public void click(){

        image.setImageResource(normalbild);
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        image.setImageResource(kleinbild);
                        if(klickzahl>0){
                            klickzahl--;
                        }

                        ifGameIsOver();

                        MainActivity.cleanUpMediaPlayer(mp);
                        mp = MediaPlayer.create(getApplication(), MainActivity.klicksound);
                        mp.start();

                        zahlenfeld.setText(klickzahl+"");
                        break;
                    case MotionEvent.ACTION_UP:


                        image.setImageResource(normalbild);

                        SharedPreferences prefs = getSharedPreferences("werte", 0);
                        SharedPreferences.Editor editor =prefs.edit();
                        editor.putInt(zahlname, klickzahl);
                        editor.commit();
                        break;
                }

                return true;
            }
        });
    }

    //Rating Button (Google Play Store)
    public void rating (){

        ratingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //Setzung des Startbildes vom Rating Button
                        ratingButton.setImageResource(R.drawable.rating3);

                        // Button Click Sound
                        MainActivity.cleanUpMediaPlayer(mp);
                        mp = MediaPlayer.create(getApplication(), MainActivity.klicksound);
                        mp.start();



                        break;

                    case MotionEvent.ACTION_UP:


                        ratingButton.setImageResource(R.drawable.ratingone);

                        if(internetAvailabel()){
                            //Intent zum Google Play store
                            String url = "https://play.google.com/store/apps/details?id=com.pentabuttons.handynummernvonyoutubern&hl=de";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            onPause();
                            startActivity(intent);



                            //Handler für verzögerung des Dialogs
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    //Dialog
                                    AlertDialog.Builder a_builder = new AlertDialog.Builder(YoutuberActivity.this);
                                    a_builder.setMessage(R.string.ratingText)
                                            .setCancelable(false)
                                            .setPositiveButton("Belohnug bekommen", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int i) {

                                                    //Abzug und speicherung der Punkte
                                                    dialog.cancel();
                                                    Toast.makeText(getApplicationContext(), MainActivity.ratingtext, Toast.LENGTH_SHORT).show();
                                                    MainActivity.cleanUpMediaPlayer(mp);
                                                    mp = MediaPlayer.create(getApplication(), MainActivity.lotofklicks);
                                                    mp.start();
                                                    if(klickzahl>MainActivity.klicksForRating){
                                                        klickzahl = klickzahl-MainActivity.klicksForRating;
                                                    }else{
                                                        klickzahl=0;
                                                    }




                                                    ifGameIsOver();

                                                    zahlenfeld.setText(klickzahl+"");
                                                    SharedPreferences prefs = getSharedPreferences("werte", 0);
                                                    SharedPreferences.Editor editor =prefs.edit();
                                                    editor.putInt(zahlname, klickzahl);
                                                    editor.putBoolean("userRatedUs", true);
                                                    editor.commit();

                                                    MainActivity.userRatedUs=true;
                                                    ratingButton.setVisibility(View.INVISIBLE);

                                                }
                                            });

                                    AlertDialog alert = a_builder.create();
                                    alert.setTitle(R.string.ratingTitle);
                                    alert.show();


                                }

                                //Delay Zeit in Millisekunden
                            }, 1000);
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.checkInternetConnection, Toast.LENGTH_SHORT).show();
                        }




                        break;
                }

                return true;
            }
        });






    }

    //RewardAd Button
    public void rewardAd(){


        MainActivity.reward=false;

        rewardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        rewardButton.setImageResource(R.drawable.reward3);

                        MainActivity.cleanUpMediaPlayer(mp);
                        mp = MediaPlayer.create(getApplication(), MainActivity.klicksound);
                        mp.start();



                        if(mAd.isLoaded()){
                            startAd();
                        }else if(internetAvailabel()){
                            Toast.makeText(YoutuberActivity.this, "Werbung noch nicht geladen, versuche es später erneut", Toast.LENGTH_SHORT).show();


                        }else{
                            Toast.makeText(YoutuberActivity.this, "Überprüfe deine Internetverbindung", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    case MotionEvent.ACTION_UP:

                        rewardButton.setImageResource(R.drawable.rewardone);

                        break;
                }

                return true;
            }
        });





    }

    //Überweisung der onBackPressed Eigenschaften an den Backbutton
    public void backbutton(View view){
        onBackPressed();
    }

    /** +++++++++ BUTTONS Ende +++++++++++++ */





    /** +++++++++ Abfragen etc. Start +++++++++++++ */

    //Anzeigen des Ads
    public void startAd(){
        mAd.show();
        MainActivity.lastAdTime=System.currentTimeMillis();
        loadAd();
    }

    //Laden des Ads und MarginTOP setzung
    public void adListener(){

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        relativeLayout = (RelativeLayout)findViewById(R.id.relativ);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(0, MainActivity.adMargin, 0, 0);
        relativeLayout.setLayoutParams(param);
    }

    public void onBackPressed() {
        super.onBackPressed();

        MainActivity.cleanUpMediaPlayer(mp);
        mp = MediaPlayer.create(getApplication(), MainActivity.backsound);
        mp.start();

        Intent i = new Intent(YoutuberActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    //Laden des Ads + AdListener
    public void loadAd(){

        AdRequest adRequest = new AdRequest.Builder().build();
        mAd = MobileAds.getRewardedVideoAdInstance(YoutuberActivity.this);
        mAd.loadAd("ca-app-pub-8919538156550588/1410808346", adRequest);

        mAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                if(MainActivity.reward){
                    MainActivity.reward=false;
                    Toast.makeText(getApplicationContext(), MainActivity.rewardtext, Toast.LENGTH_SHORT).show();
                    MainActivity.cleanUpMediaPlayer(mp);
                    mp = MediaPlayer.create(getApplication(), MainActivity.lotofklicks);
                    mp.start();
                    if(klickzahl>MainActivity.klicksForAd){
                        klickzahl = klickzahl-MainActivity.klicksForAd;
                    }else{
                        klickzahl=0;
                    }

                    zahlenfeld.setText(klickzahl+"");

                    ifGameIsOver();

                    SharedPreferences prefs = getSharedPreferences("werte", 0);
                    SharedPreferences.Editor editor =prefs.edit();
                    editor.putInt(zahlname, klickzahl);
                    editor.commit();
                    loadAd();
                }

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                MainActivity.reward=true;



            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                loadAd();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }
        });

    }

    //Abfrage ob Internet verfügbar ist
    public boolean internetAvailabel(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    //Abfrage ob 0 erreicht wurde
    public void ifGameIsOver(){
        ImageView normalbild = (ImageView)findViewById(bildID);
        if(klickzahl <= 0){
            normalbild.setVisibility(View.INVISIBLE);
            zahlenfeld.setVisibility(View.INVISIBLE);
            rewardButton.setVisibility(View.INVISIBLE);
            ratingButton.setVisibility(View.INVISIBLE);



            plottwist.setVisibility(View.VISIBLE);

        }
    }

    /** +++++++++ Abfragen etc. Ende +++++++++++++ */

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void anfangsLayoutErstellen(){
        image.setImageResource(normalbild);
        zahlenfeld.setText(klickzahl+"");
    }

}