package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static final String PLAYER_NUMBER = "player_number";

    private int mPlayerNumber;

    private final GameBroad mGameBroad = new GameBroad(0);

    private TextView mTextView;

    public static Intent mainIntent(Context packageContext, int playerNumber){
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(PLAYER_NUMBER, playerNumber);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerNumber = getIntent().getIntExtra(PLAYER_NUMBER,0);

        mGameBroad.setPlayersNumber(mPlayerNumber);

        mTextView = findViewById(R.id.welcome_players);
        mTextView.setText("Welcome " + mPlayerNumber);

    }
}
