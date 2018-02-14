package com.pentabuttons.handynummernvonyoutubern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.penta.games.handynummernvonyoutuber.R;

public class ImpressumActivity extends AppCompatActivity {


    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);


        Toolbar toolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onBackPressed() {
        super.onBackPressed();


        MainActivity.cleanUpMediaPlayer(mp);
        mp = MediaPlayer.create(getApplication(), MainActivity.backsound);
        mp.start();

        Intent i = new Intent(ImpressumActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
