package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class ShowMoreZombiesActivity extends AppCompatActivity {
    private static final String COUNTSETUP = "countsetup";;
    private static final String ROOMS = "rooms";
    private static final String SETUPCOUNTED = "countedsetup";

    private ImageView mShowMore1, mShowMore2;
    private ImageButton mOKButton;

    private ArrayList<ImageView> mAllEnterView = new ArrayList<>();
    private ArrayList<Integer> mRooms;
    private int mCountSetUp;

    public static Intent newShowZombiesIntent(Context context, ArrayList<Integer> rooms, int countsetup){
        Intent intent = new Intent(context, ShowMoreZombiesActivity.class);
        intent.putExtra(COUNTSETUP,countsetup);
        intent.putIntegerArrayListExtra(ROOMS,rooms);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more_zombies);

        mCountSetUp = getIntent().getIntExtra(COUNTSETUP,0);
        mRooms = getIntent().getIntegerArrayListExtra(ROOMS);

        mShowMore1 = findViewById(R.id.morezombie1);
        mShowMore2 = findViewById(R.id.morezombie2);
        mAllEnterView.add(mShowMore1);
        mAllEnterView.add(mShowMore2);

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
