package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

public class FirstActivity extends AppCompatActivity {
    private ImageButton mPlayButton;
    private ImageButton mHowToPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);


        mPlayButton = findViewById(R.id.play_button);
        mPlayButton.startAnimation(animTranslate);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animTranslate);
                Intent intent =  new Intent(FirstActivity.this, SetPlayerNumberActivity.class);
                startActivity(intent);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_grow_fade_in_from_bottom,android.support.v7.appcompat.R.anim.abc_shrink_fade_out_from_bottom );
            }
        });

        mHowToPlayButton = findViewById(R.id.how_to_play_button);
        mHowToPlayButton.startAnimation(animTranslate);
        mHowToPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animTranslate);
            }
        });

    }
}
