package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowSimpleVoteResultActivity extends AppCompatActivity {
    private static final String RESULTS = "results";
    private static final String COUNTSETUP = "countsetup";

    private static final String SETUPCOUNTED = "SETUPCOUNTED";

    private TextView mRedResult, mYellowResult, mBlueResult, mGreenResult, mBrownResult, mBlackResult;
    private ImageButton mContinueButton;

    private HashMap<String, Integer> mResults;
    private int mCountSetUp;



    public static Intent newVoteResultIntent(Context context, HashMap<String, Integer> results, int countsetup){
        Intent intent = new Intent(context, ShowSimpleVoteResultActivity.class);
        intent.putExtra(RESULTS,results);
        intent.putExtra(COUNTSETUP, countsetup);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_simple_vote_result);
        mResults = (HashMap<String, Integer>) getIntent().getSerializableExtra(RESULTS);
        mCountSetUp = getIntent().getIntExtra(COUNTSETUP,0);

        mRedResult = findViewById(R.id.redresult_simple);
        mYellowResult = findViewById(R.id.yellowresult_simple);
        mBlueResult = findViewById(R.id.blueresult_simple);
        mGreenResult = findViewById(R.id.greenresult_simple);
        mBrownResult = findViewById(R.id.brownresult_simple);
        mBlackResult = findViewById(R.id.blackresult_simple);

        ++mCountSetUp;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent data = new Intent();
                data.putExtra(SETUPCOUNTED, mCountSetUp);
                setResult(ShowSimpleVoteResultActivity.RESULT_OK, data);
                finish();
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
            }
        },3 * 1000);

        List<String> keys = new ArrayList<>(mResults.keySet());
        List<Integer> values= new ArrayList<>(mResults.values());
        HashMap<String, TextView> results = new HashMap<>();
        results.put("RED", mRedResult);
        results.put("YELLOW",mYellowResult);
        results.put("BLUE",mBlueResult);
        results.put("GREEN", mGreenResult);
        results.put("BROWN",mBrownResult);
        results.put("BLACK", mBlackResult);

        for (int i=0; i<keys.size(); i++){
            if (results.containsKey(keys.get(i))){
                results.get(keys.get(i)).setText(Integer.toString(values.get(i)));
            }
        }
    }

    public static int getCountedSetUp (Intent result){
        return result.getIntExtra(SETUPCOUNTED,0);
    }
}
