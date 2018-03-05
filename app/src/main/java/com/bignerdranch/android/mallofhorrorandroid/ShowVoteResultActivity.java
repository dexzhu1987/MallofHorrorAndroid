package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowVoteResultActivity extends AppCompatActivity {
    private static final String VOTES = "votes";
    private static final String RESULTS = "results";
    private static final String COUNTSETUP = "countsetup";

    private static final String SETUPCOUNTED1 = "SETUPCOUNTED1";

    private ImageView mRedVoted, mYellowVoted, mBlueVoted, mGreenVoted, mBrownVoted, mBlackVoted;
    private TextView mRedResult, mYellowResult, mBlueResult, mGreenResult, mBrownResult, mBlackResult;
    private ImageButton mContinueButton;

    private ArrayList<String> mVotes;
    private HashMap<String, Integer> mResults;
    private int mCountSetUp;

    public static Intent newVoteResultIntent(Context context, ArrayList<String> votes, HashMap<String, Integer> results, int countsetup){
        Intent intent = new Intent(context, ShowVoteResultActivity.class);
        intent.putStringArrayListExtra(VOTES, votes);
        intent.putExtra(RESULTS,results);
        intent.putExtra(COUNTSETUP, countsetup);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vote_result);
        System.out.println("ShowVote Result called");

        mVotes = getIntent().getStringArrayListExtra(VOTES);
        mResults = (HashMap<String, Integer>) getIntent().getSerializableExtra(RESULTS);
        mCountSetUp = getIntent().getIntExtra(COUNTSETUP,0);

        mRedVoted = findViewById(R.id.redvoted);
        mYellowVoted = findViewById(R.id.yellowvoted);
        mBlueVoted = findViewById(R.id.bluevoted);
        mGreenVoted = findViewById(R.id.greenvoted);
        mBrownVoted = findViewById(R.id.brownvoted);
        mBlackVoted = findViewById(R.id.blackvoted);

        mRedResult = findViewById(R.id.redresult);
        mYellowResult = findViewById(R.id.yellowresult);
        mBlueResult = findViewById(R.id.blueresult);
        mGreenResult = findViewById(R.id.greenresult);
        mBrownResult = findViewById(R.id.brownresult);
        mBlackResult = findViewById(R.id.blackresult);

        ++mCountSetUp;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent data = new Intent();
                data.putExtra(SETUPCOUNTED1, mCountSetUp);
                setResult(ShowVoteResultActivity.RESULT_OK, data);
                finish();
                overridePendingTransition(0,0);
            }
        },5 * 1000);


        HashMap<String, ImageView> votedImages = new HashMap<>();
        votedImages.put("RED",mRedVoted);
        votedImages.put("YELLOW",mYellowVoted);
        votedImages.put("BLUE",mBlueVoted);
        votedImages.put("GREEN",mGreenVoted);
        votedImages.put("BROWN",mBrownVoted);
        votedImages.put("BLACK",mBlackVoted);

        for (int i=0; i<mVotes.size(); i+=2){
            if (votedImages.containsKey(mVotes.get(i))){
                if (mVotes.get(i+1).equalsIgnoreCase("Red")){
                    votedImages.get(mVotes.get(i)).setImageResource(R.drawable.votingredbttn);
                } else if (mVotes.get(i+1).equalsIgnoreCase("Yellow")){
                    votedImages.get(mVotes.get(i)).setImageResource(R.drawable.votingyellowbttn);
                } else if (mVotes.get(i+1).equalsIgnoreCase("Blue")){
                    votedImages.get(mVotes.get(i)).setImageResource(R.drawable.votingbluebttn);
                } else if (mVotes.get(i+1).equalsIgnoreCase("Green")){
                    votedImages.get(mVotes.get(i)).setImageResource(R.drawable.votinggreenbttn);
                } else if (mVotes.get(i+1).equalsIgnoreCase("Brown")){
                    votedImages.get(mVotes.get(i)).setImageResource(R.drawable.votingbrownbttn);
                } else if (mVotes.get(i+1).equalsIgnoreCase("Black")){
                    votedImages.get(mVotes.get(i)).setImageResource(R.drawable.votingblackbttn);
                }
            }
        }

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
        return result.getIntExtra(SETUPCOUNTED1,0);
    }
}
