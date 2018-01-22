package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SetPlayerNumberActivity extends AppCompatActivity {
    ImageButton mNumberFourButton;
    ImageButton mNumberFiveButton;
    ImageButton mNumberSixButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_player_number);

        mNumberFourButton = findViewById(R.id.number_four_button);
        mNumberFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainIntent(SetPlayerNumberActivity.this,4);
                startActivity(intent);
            }
        });

        mNumberFiveButton = findViewById(R.id.number_five_button);
        mNumberFiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainIntent(SetPlayerNumberActivity.this,5);
                startActivity(intent);
            }
        });

        mNumberSixButton = findViewById(R.id.number_six_button);
        mNumberSixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainIntent(SetPlayerNumberActivity.this,6);
                startActivity(intent);
            }
        });


    }
}
