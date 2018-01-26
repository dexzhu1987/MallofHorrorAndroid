package com.bignerdranch.android.mallofhorrorandroid;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;


public class SetPlayerNumberActivity extends AppCompatActivity {
    private ImageButton mNumberFourButton;
    private ImageButton mNumberFiveButton;
    private ImageButton mNumberSixButton;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_player_number);


        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animBounce = AnimationUtils.loadAnimation(this, R.anim.anim_bounce);
        final Animation milkshake = AnimationUtils.loadAnimation(this, R.anim.milkshake);

        mTextView = findViewById(R.id.text_setplayer);
        mTextView.startAnimation(animAlpha);


        mNumberFourButton = findViewById(R.id.number_four_button);
        mNumberFourButton.startAnimation(animAlpha);
        mNumberFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = MainActivity.mainIntent(SetPlayerNumberActivity.this,4);
                startActivity(intent);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_grow_fade_in_from_bottom,android.support.v7.appcompat.R.anim.abc_shrink_fade_out_from_bottom );
            }
        });

        mNumberFiveButton = findViewById(R.id.number_five_button);
        mNumberFiveButton.startAnimation(animAlpha);
        mNumberFiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = MainActivity.mainIntent(SetPlayerNumberActivity.this,5);
                startActivity(intent);
            }
        });

        mNumberSixButton = findViewById(R.id.number_six_button);
        mNumberSixButton.startAnimation(animAlpha);
        mNumberSixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                Intent intent = MainActivity.mainIntent(SetPlayerNumberActivity.this,6);
                startActivity(intent);
            }
        });


    }
}
