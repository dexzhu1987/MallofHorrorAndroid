package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class ShowingZombieActivity extends AppCompatActivity {
    private static final String COUNTSETUP = "countsetup";
    private static final String ROOMS = "rooms";

    private static final String SETUPCOUNTED = "countedsetup";

    private ImageView mEnterRoomOne, mEnterRoomTwo, mEnterRoomThree, mEnterRoomFour;
    private ImageButton mContinueButton;

    private ArrayList<ImageView> mAllEnterView = new ArrayList<>();
    private ArrayList<Integer> mRooms;
    private int mCountSetUp;

    public static Intent newShowZombiesIntent(Context context, ArrayList<Integer> rooms, int countsetup){
        Intent intent = new Intent(context, ShowingZombieActivity.class);
        intent.putExtra(COUNTSETUP,countsetup);
        intent.putIntegerArrayListExtra(ROOMS,rooms);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_zombie);

        mCountSetUp = getIntent().getIntExtra(COUNTSETUP,0);
        mRooms = getIntent().getIntegerArrayListExtra(ROOMS);

        mEnterRoomOne = findViewById(R.id.enterroom1);
        mEnterRoomTwo = findViewById(R.id.enterroom2);
        mEnterRoomThree = findViewById(R.id.enterroom3);
        mEnterRoomFour = findViewById(R.id.enterroom4);
        mAllEnterView.add(mEnterRoomOne);
        mAllEnterView.add(mEnterRoomTwo);
        mAllEnterView.add(mEnterRoomThree);
        mAllEnterView.add(mEnterRoomFour);

        ++mCountSetUp;


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent data = new Intent();
                data.putExtra(SETUPCOUNTED, mCountSetUp);
                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
            }
        }, 10*1000);


        for (int i=0; i<mRooms.size(); i++){
            if (mRooms.get(i)==1){
                mAllEnterView.get(i).setImageResource(R.drawable.restroombttn);
            } else if (mRooms.get(i)==2){
                mAllEnterView.get(i).setImageResource(R.drawable.cachoubttn);
            } else if (mRooms.get(i)==3){
                mAllEnterView.get(i).setImageResource(R.drawable.megatoybttn);
            } else if (mRooms.get(i)==4){
                mAllEnterView.get(i).setImageResource(R.drawable.parkbttn);
            } else if (mRooms.get(i)==5){
                mAllEnterView.get(i).setImageResource(R.drawable.securityhqbttn);
            } else if (mRooms.get(i)==6){
                mAllEnterView.get(i).setImageResource(R.drawable.superstorebttn);
            }

        }

    }

    public static int getCountedSetUp (Intent result){
        return result.getIntExtra(SETUPCOUNTED,0);
    }
}
