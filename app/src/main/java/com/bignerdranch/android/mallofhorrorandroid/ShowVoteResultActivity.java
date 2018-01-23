package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Context;
import android.content.Intent;
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

    private static final String SETUPCOUNTED = "SETUPCOUNTED";

    private ImageView mRedVoted, mYellowVoted, mBlueVoted, mGreenVoted, mBrownVoted, mBlackVoted;
    private TextView mRedResult, mYellowResult, mBlueResult, mGreenResult, mBrownResult, mBlackResult;
    private ImageButton mContinueButton;

    private ArrayList<String> mVotes;
    private HashMap<String, Integer> mResults;
    private int mCountSetUp;

    public static Intent newVoteResultIntent(Context context, ArrayList<String> votes, HashMap<String, Integer> results, int countsetup){
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putStringArrayListExtra(VOTES, votes);
        intent.putExtra(RESULTS,results);
        intent.putExtra(COUNTSETUP, countsetup);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vote_result);

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

        mContinueButton = findViewById(R.id.okButton_voteDetailed);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountSetUp++;
                Intent data = new Intent();
                data.putExtra(SETUPCOUNTED, mCountSetUp);
                setResult(RESULT_OK, data);
                finish();
            }
        });


        HashMap<String, ImageView> votedImages = new HashMap<>();
        votedImages.put("Red",mRedVoted);
        votedImages.put("Yellow",mYellowVoted);
        votedImages.put("Blue",mBlueVoted);
        votedImages.put("Green",mGreenVoted);
        votedImages.put("Brown",mBrownVoted);
        votedImages.put("Black",mBlackVoted);

        for (int i=0; i<mVotes.size(); i++){
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
                    votedImages.get(mVotes.get(i)).setImageResource(R.drawable.votingBlackBttn);
                }
            }
        }

        List<String> keys = new ArrayList<>(mResults.keySet());
        List<Integer> values= new ArrayList<>(mResults.values());
        HashMap<String, TextView> results = new HashMap<>();
        results.put("Red", mRedResult);
        results.put("Yellow",mYellowResult);
        results.put("Blue",mBlueResult);
        results.put("Green", mGreenResult);
        results.put("Brown",mBrownResult);
        results.put("Black", mBlackResult);

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
