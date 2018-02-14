package com.pentabuttons.handynummernvonyoutubern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.penta.games.handynummernvonyoutuber.R;

public class MainActivity extends AppCompatActivity {

    String text[] = {
            "Lukas Rieger",
            "Mike Singer",
            "Julien Bam",
            "Bibis Beauty-\npalace",
            "Tanzverbot",
            "Dagi Bee"};


    Integer[] image = {
            R.drawable.prevlukas,
            R.drawable.prevmike,
            R.drawable.julienprev,
            R.drawable.prevbibi,
            R.drawable.prevtanzi,
            R.drawable.prevdagi,
    };


    int gesamtklicks;
    public static int klicksound = R.raw.push;
    public static int backsound = R.raw.pull;
    public static int lotofklicks = R.raw.rewardsound;


    public static int lukaszahl;
    public static int mikezahl;
    public static int julienzahl;
    public static int bibizahl;
    public static int tanzizahl;
    public static int dagizahl;

    public static int adMargin;

    public static boolean reward;
    public static String rewardtext;
    public static String ratingtext;

    public static long lastAdTime;
    public static String werbePause;
    public static int timeHowOftenAdIsAvailable;


    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public Toolbar mToolbar;
    public NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    public static int klicksForRating;
    public static int klicksForAd;

    public static boolean userRatedUs;

    public static String youtuber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auswahl_activity);

        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        mNavigationView.setItemIconTintList(null);
        navigationItemSelectedListener();

        klicksForAd=3000;
        klicksForRating = 10000;

        rewardtext = "3000 Klicks gutgeschrieben !";
        ratingtext = "10000 Klicks gutgeschrieben !";

        if (internetAvailabel()) {
            adMargin = 150;
        } else {
            adMargin = 0;
        }


        gesamtklicks = 100000;


        SharedPreferences prefs = getSharedPreferences("werte", 0);
        lukaszahl = prefs.getInt("lukaszahl", gesamtklicks);
        mikezahl = prefs.getInt("mikezahl", gesamtklicks);
        julienzahl = prefs.getInt("julienzahl", gesamtklicks);
        bibizahl = prefs.getInt("bibizahl", gesamtklicks);
        tanzizahl = prefs.getInt("tanzizahl", gesamtklicks);
        dagizahl = prefs.getInt("dagizahl", gesamtklicks);
        userRatedUs = prefs.getBoolean("userRatedUs", false);


        int[] progresses = {cP(lukaszahl), cP(mikezahl), cP(julienzahl), cP(bibizahl), cP(tanzizahl), cP(dagizahl)};


        ListView list = (ListView) findViewById(R.id.simpleList);
        ListViewAdapter listViewAdapter = new ListViewAdapter(this, text, image, progresses);
        list.setAdapter(listViewAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                switch (position) {
                    case 0:
                        youtuber = "youtuber_layout";
                        break;
                    case 1:
                        youtuber = "mikesinger";
                        break;
                    case 2:
                        youtuber = "julienbam";
                        break;
                    case 3:
                        youtuber = "bibisbeautypalace";
                        break;
                    case 4:
                        youtuber = "tanzverbot";
                        break;
                    case 5:
                        youtuber = "dagibee";
                        break;

                }


                Intent a = new Intent(MainActivity.this, YoutuberActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(a);

            }
        });

        firstRunDialog();

    }


    public int cP(int klicksleft) {
        int progress;
        double helper;
        helper = (gesamtklicks - klicksleft) * 100 / gesamtklicks;

        progress = (int) helper;

        Log.e("debugComputProgress", "progress/gesklicks=" + helper);

        return progress;
    }

    public boolean internetAvailabel() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public static void cleanUpMediaPlayer(MediaPlayer mp) {
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
                mp = null;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public void firstRunDialog() {
        SharedPreferences prefs = getSharedPreferences("werte", 0);
        boolean b = prefs.getBoolean("firstrun", true);

        if (b) {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
            a_builder.setMessage(R.string.begruessungsText)
                    .setCancelable(true)
                    .setPositiveButton("Versteh ich nicht", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = a_builder.create();
            alert.setTitle(R.string.begeruessungsTitle);
            alert.show();
        }


        SharedPreferences sh = getSharedPreferences("werte", 0);
        SharedPreferences.Editor editor = sh.edit();
        editor.putBoolean("firstrun", false);
        editor.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (mToggle.onOptionsItemSelected(item))
            return true;
        {


            return super.onOptionsItemSelected(item);
        }
    }


    public void navigationItemSelectedListener(){

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.youtuberauswahl) {
//                    Intent a = new Intent(MainActivity.this, MainActivity.class);
//                    a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(a);
                }


                if(menuItem.getItemId() == R.id.teilen){
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "✶Youtuber Handdynummern✶");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "✶Bekomme Handynummern beliebter YouTuber✶\n\nhttps://play.google.com/store/apps/details?id=com.pentabuttons.handynummernvonyoutubern&hl=de");
                    startActivity(Intent.createChooser(shareIntent,  "Teilen über..."));
                }
                if (menuItem.getItemId() == R.id.erklaerung) {

                    AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
                    a_builder.setMessage(R.string.begruessungsText)
                            .setCancelable(true)
                            .setPositiveButton("Versteh ich nicht", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = a_builder.create();
                    alert.setTitle(R.string.begeruessungsTitle);
                    alert.show();
                }


                if (menuItem.getItemId() == R.id.impressum) {

                    Intent a = new Intent(MainActivity.this, ImpressumActivity.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(a);
                }


                if (menuItem.getItemId() == R.id.instagram) {
                    String url = "https://instagram.com/_u/coding.empire/?r=sun1";

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    onPause();
                    startActivity(intent);
                }




                if (menuItem.getItemId() == R.id.email) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","pentabuttons@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n\n\n\n\n(nicht löschen!)\ncom.handynummernvonyoutubern");

                    startActivity(Intent.createChooser(emailIntent, "E-Mail senden..."));
                }


                return false;
            }

        });
    }


}