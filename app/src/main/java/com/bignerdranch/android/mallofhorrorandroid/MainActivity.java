package com.bignerdranch.android.mallofhorrorandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.Game;
import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.GameData;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.GameCharacter;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Dice.PairofDice;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Dice.TwoPairofDice;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Axe;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Hardware;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Hidden;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Item;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.SecurityCamera;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.ShotGun;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Sprint;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Threat;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.Playable;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.Room;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PLAYER_NUMBER = "player_number";

    private static final String DATABASEGAME = "databasegame";
    private static final String USERNAME = "username";
    private static final String TYPE = "type";

    private static final int REQUEST_CODE_ROOM = 0;
    private static final int REQUEST_CODE_CHARACTER = 1;
    private static final int REQUEST_CODE_MESSAGE = 2;
    private static final int REQUEST_CODE_VOTE = 3;
    private static final int REQUEST_CODE_VIEWRESULT = 4;
    private static final int REQUEST_CODE_YESNO = 5;
    private static final int REQUEST_CODE_VIEWSIMPLERESULT = 6;
    private static final int REQUEST_CODE_CHOOSINGITEM = 7;
    private static final int REQUEST_CODE_VIEWZOMBIE = 8;
    private static final int REQUEST_CODE_VIEWZOMBIECHIEF = 9;
    private static final int REQUEST_CODE_VIEWZOMBIEAll = 10;
    private static final int REQUEST_CODE_VIEWZOMBIEALLMORE = 11;
    private static final int REQUEST_CODE_ITEM = 12;


    private int mPlayerNumber;

    private final String PLAYERINFORM = "PlayerInform";
    private final String TURN = "Turn";
    private final String PREVTURN = "PrevTurn";
    private final String GAMEDATA = "GameData";
    private final String PLAYERBOOLEANANSWERS = "PlayerBooleanAnswers";
    private final String ZOMBIEROOMS = "ZombiesRooms";
    private final String FIRSTPLAYERINDEX = "FirstPlayerIndex";
    private final String ISCHIEFELECTED = "IsChiefElected";
    private final String INDEXS = "Indexs";
    private final String ROOMS = "Rooms";
    private final String VICTIMCOLOR = "VictimColor";
    private final String DEATHCHARACTER = "DeathCharacter";

    private final int DELAYEDSECONDSFORMESSAGEVIE = 3;
    private final int DELAYEDSECONDSFOROPTIONSCHOSEN = 10;
    private final int DELAYEDSECONDSFORLONGMESSAGE = 5;


    private DatabaseReference mDatabaseReference;
    private Game mDatabaseGame;
    private String mUserName;
    private int mMyPlayerID;
    private String mType;
    private boolean mIsDataPushed;

    private final static GameBroad gameBroad = new GameBroad(0);
    private static int mCurrentRoomPickedNumber = 0;
    private static int mCountSetUp;
    private static int mSecondCount;
    private static boolean mCurrentYesNoMain = false;
    private static boolean mCurrentYesNo = false;
    private static String mCurrentGameCharacterSelected = "";
    private static String mCurrentVoteColor = "";
    private static Item mCurrentSelectedItem = new Hardware();
    private static int mCountPhase = 0;
    private static Playable mCurrentVictim = new Playable();
    final static List<String> colors = new ArrayList<>();
    final static List<Item> items = new ArrayList<>();
    final static List<String> actualcolors= new ArrayList<>();
    private ArrayList<Playable> mCurrentTeam = new ArrayList<>();
    private int originalTeamSize ;


    private ConstraintLayout mMainActivityLayout;
    private ImageButton mRedButton, mYellowButton, mBlueButton, mGreenButton, mBrownButton, mBlackButton;
    private ImageButton mContinueButton;
    private ImageButton mYesButton, mNoButton;
    private GridLayout mRestRoomArea, mCachouArea, mMegatoyArea, mParkingArea, mSecurityArea, mSupermarketArea;
    private TextView mRestRoomZombie, mCachouZombie, mMegatoyZombie, mParkingZombie, mSecurityZombie, mSupermarketZombie;
    private List<ImageButton> mPlayerButtons = new ArrayList<>();
    private ImageView mYesShadow, mNoShadow, mOKShadow;
    private List<ImageButton> mActualPlayerButtons = new ArrayList<>();
    private TextView mMessageView;
    private ImageView mLoading;

    private static List<String> votes = new ArrayList<>();
    private static int mThirdCount;
    private static List<Item> mCurrentItemOptions = new ArrayList<>();
    private static ArrayList<Integer> mCurrentZombiesRooms = new ArrayList<>();
    private static int mCurrentStartPlayerIndex;
    private static Playable mCurrentStartPlayer;
    private static boolean mIsChiefSelected;
    private static int mCurrentPlayerNumber;
    private static int mCurrentStartRoom;
    private static List<Integer> roomspicked = new ArrayList<>();
    private static List<Integer> playersIndex =  new ArrayList<>();
    private static ArrayList<Integer> mCurrentMoreZombies = new ArrayList<>();
    private static List<Item> mUsedItem = new ArrayList<>();
    private static List<Playable> mPlayersUsedItem = new ArrayList<>();
    private static int mFourthCount;
    private static int mFifthCount;
    private static int mSixCount;

    final Animation mFlash = new AlphaAnimation(1, 0);


    public static Intent mainIntent(Context packageContext, int playerNumber){
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(PLAYER_NUMBER, playerNumber);
        return intent;
    }

    public static Intent mainIntent(Context packageContext, int playerNumber, Game game, String mUserName, String type){
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(DATABASEGAME, (Parcelable) game);
        intent.putExtra(PLAYER_NUMBER, playerNumber);
        intent.putExtra(USERNAME, mUserName);
        intent.putExtra(TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gettingReady();
        updateRoom(MainActivity.this);
        displayMessage();
        messageHasUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resuming ");
        updateRoom(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference().child("chats").setValue(null);
    }

    private void gettingReady() {
        mPlayerNumber = getIntent().getIntExtra(PLAYER_NUMBER,0);
        gameBroad.setPlayersNumber(mPlayerNumber);
        mDatabaseGame = getIntent().getParcelableExtra(DATABASEGAME);
        mUserName = getIntent().getStringExtra(USERNAME);
        mType = getIntent().getStringExtra(TYPE);
        mIsDataPushed = true;

        fireBaseInitialSetup();
        continueButtonMethod();
        otherCommonSetUp();
        registerMyPlayerId();

        Log.i(TAG, "PlayerNumber: " + mPlayerNumber + " gameDataBase " +  mDatabaseGame.toString() + " username: " + mUserName + " type: " + mType + " myPlayerID:" + mMyPlayerID );

        mMessageView = findViewById(R.id.messageView_main);

        setUpListenerOnFirebase();


    }

    private void fireBaseInitialSetup() {
        if (mDatabaseGame!=null){
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("game").child(mDatabaseGame.getRoomId()+"started");
            FirebaseDatabase.getInstance().getReference().child("game").child(mDatabaseGame.getRoomId()+"started").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()==null && mCountPhase==0 && mType.equals("Host")){
                        createRoomOnFireBase();

                    } else if (dataSnapshot.getValue()==null && mCountPhase==0 ) {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void createRoomOnFireBase() {
        Log.i(TAG, "host created room" );
        mCountPhase=0;
        mCountSetUp=0;
        mSecondCount=0;
        mThirdCount=0;
        mFourthCount=0;
        mFifthCount=0;
        mSixCount=0;
        mDatabaseReference.child(PLAYERINFORM).setValue(mDatabaseGame);
        GameData gameData = new GameData(0,0,0,0,0,0,0);
        mDatabaseReference.child(TURN).setValue(-1);
        mDatabaseReference.child(GAMEDATA).setValue(gameData);
        mDatabaseReference.child(PREVTURN).setValue(-1);

    }

    private void registerMyPlayerId() {
        List<String> userNames = new ArrayList<>();
        userNames.clear();
        userNames.add(mDatabaseGame.getPlayer1());
        userNames.add(mDatabaseGame.getPlayer2());
        userNames.add(mDatabaseGame.getPlayer3());
        userNames.add(mDatabaseGame.getPlayer4());
        Log.i(TAG, "Main Game: " +  mDatabaseGame.toString());
        for (int i=0; i<userNames.size(); i++){
            if (mUserName.equals(userNames.get(i))){
                mMyPlayerID=i;
                if (mMyPlayerID==mPlayerNumber-1){
                    FirebaseDatabase.getInstance().getReference().child("game").child(mDatabaseGame.getRoomId()).setValue(null);
                }
                return;
            }
        }
    }

    private int getControlId(){
        colors.clear();
        colors.add("Red");
        colors.add("Yellow");
        colors.add("Blue");
        colors.add("Green");
        colors.add("Brown");
        colors.add("Black");
        int controlId = 0 ;
        for (int i=0; i<colors.size(); i++){
           if (gameBroad.getPlayers().get(0).getColor().equalsIgnoreCase(colors.get(i))){
               controlId = i;
           }
        }
        return controlId;
    }

    private void setUpListenerOnFirebase() {
        if (mDatabaseGame!=null){
            mDatabaseReference.child(TURN).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i(TAG, "Turn value :  " + dataSnapshot.getValue());
                    if (dataSnapshot.getValue()!=null){
                    disableContinue();
                    int turn = dataSnapshot.getValue(Integer.TYPE);
                    if(turn<0){
                        for (ImageButton imageButton: mPlayerButtons){
                            imageButton.setVisibility(View.INVISIBLE);
                        }
                        gamePhaseChangingAccoringtoFirebase();
                    } else if (turn>=0){
                        rotateTurnAccoridngtoFirebase(turn);
                    }
                    }
                }
            @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void gamePhaseChangingAccoringtoFirebase() {
        mDatabaseReference.child(GAMEDATA).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GameData gameData = dataSnapshot.getValue(GameData.class);
                Log.i(TAG, gameData.toString());
                mCountPhase=gameData.getmCountPhase();
                mCountSetUp=gameData.getmCountSetUp();
                mSecondCount=gameData.getmSecondCount();
                mThirdCount=gameData.getmThirdCount();
                mFourthCount=gameData.getmFourthCount();
                mFifthCount=gameData.getmFifthCount();
                mSixCount=gameData.getmSixCount();
                disableContinue();
                mMessageView.setVisibility(View.VISIBLE);
                mMessageView.setEnabled(false);
                if (mCountPhase==0){
                    messageViewInformPregameChooseRoom();
                } else if (mCountPhase==1 && mCountSetUp == 3*mPlayerNumber*3){
                    messageViewInformMovetoGetItem(gameData);
                } else if (mCountPhase==2 && mCountSetUp==mPlayerNumber*2) {
                    messageViewInformMovetoParksearch();
                } else if (mCountPhase==3) {
                    if (mCountSetUp==0 && (gameBroad.matchRoom(4).isEmpty() || gameBroad.getItemDeck().getItemsDeck().size() < 3)){
                        messageViewInformParkingIsEmptyandMovetoNextPhase();
                    } else {
                        messageViewRelatedParkingSearchWithSearchTeam(gameData);
                    }
                } else if (mCountPhase==4) {
                    if (mCountSetUp==0 && mSecondCount==0 && gameBroad.matchRoom(5).isEmpty()){
                        messageViewInformSecurityIsEmptyandMovetoNextPhase();
                    } else {
                        messageViewRelatedChiefElectionWithElectionTeam(gameData);
                    }
                } else if (mCountPhase==5) {
                    messageViewInformRelatedtoMoveAndView(gameData);
                } else if (mCountPhase==6) {
                    messageViewRelatedtoRevealandAttack(gameData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void messageViewInformPregameChooseRoom() {
        mMessageView.setText("PRE-GAME SETTING PHASE ONE : CHOOSE ROOM");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.setVisibility(View.INVISIBLE);
                if (mMyPlayerID==0){
                    mCountPhase++;
                    GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(TURN).setValue(0);
                }
            }
        },5000);
    }

    private void messageViewInformMovetoGetItem(GameData gameData) {
        disableContinue();
        if (mMyPlayerID!=mPlayerNumber-1){
            updateDataFromFireBase(0, gameData, mPlayerNumber-1);
        }
        updateRoom(MainActivity.this);
        mMainActivityLayout.invalidate();
        mMessageView.setText("PRE-GAME SETTING PHASE TWO: GETTING STARTER ITEM");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.setVisibility(View.INVISIBLE);
                if (mMyPlayerID==0){
                    mCountPhase++;
                    mCountSetUp=0;
                    enableContinue();
                    GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(TURN).setValue(0);
                }
            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformMovetoParksearch() {
        disableContinue();
        updateRoom(MainActivity.this);
        mMainActivityLayout.invalidate();
        mMessageView.setText("Game Phase I: Parking Search");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.setVisibility(View.INVISIBLE);
                if (mMyPlayerID==0){
                    mCountPhase++;
                    mCountSetUp=0;
                    GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(TURN).setValue(-2);
                }
            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformParkingIsEmptyandMovetoNextPhase() {
        updateRoom(MainActivity.this);
        mMainActivityLayout.invalidate();
        mMessageView.setVisibility(View.VISIBLE);
        if (mSecondCount==0){
            mMessageView.setText("Due to Parking is empty (or no more item is available, no election will be performed");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMessageView.setVisibility(View.INVISIBLE);
                    if (mMyPlayerID==0){
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,2,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-1);
                    }
                }
            },DELAYEDSECONDSFORMESSAGEVIE * 1000);
        } else {
            mMessageView.setText("Game Phase II: Security Chief selected");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMessageView.setVisibility(View.INVISIBLE);
                    if (mMyPlayerID==0){
                        mCountPhase++;
                        mCountSetUp=0;
                        mSecondCount=0;
                        mThirdCount=0;
                        votes.clear();
                        mCurrentTeam.clear();
                        mCurrentItemOptions.clear();
                        mCurrentZombiesRooms.clear();
                        mCurrentYesNo=false;
                        mCurrentYesNoMain = false;
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-2);
                        mDatabaseReference.child(PREVTURN).setValue(-1);
                    }
                }
            },DELAYEDSECONDSFORMESSAGEVIE * 1000);
        }
    }

    private void messageViewRelatedParkingSearchWithSearchTeam(GameData gameData) {
        if (mThirdCount==0){
            messageViewInformExistedMembers(4);
        } else if (mCountSetUp == mCurrentTeam.size()*2  && mSecondCount == 0){
            messageViewInformVoteResult(gameData,4);
        } else if (mCountSetUp >= mCurrentTeam.size()*2 && mSecondCount > 0 && mCountSetUp < mCurrentTeam.size()*4) {
            if (mSecondCount==1){
                messageViewInformThreatUsingCanChangedVoteResult();
            } else if (mSecondCount==2){
                messageViewInformIsThreatUsed(gameData);
            }
        } else if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 2 ){
            messageViewInformVoteSummary(gameData,4);
        } else if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 3){
            messageViewInformTieorWinnerForParking(4);
        } else if (mCountSetUp == mCurrentTeam.size()*4+3){
            messageViewInformMovingToChiefElect();
        }
    }

    private void messageViewInformExistedMembers(int roomNumber) {
        HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(roomNumber).existCharacterColor());
        List<Playable> searchTeam = new ArrayList<>();
        for (Playable player : searchteam) {
            searchTeam.add(player);
        }
        mCurrentTeam = (ArrayList<Playable>) searchTeam;
        Collections.sort(mCurrentTeam, new Comparator<Playable>() {
            @Override
            public int compare(Playable o1, Playable o2) {
                return o1.getColor().compareTo(o2.getColor());
            }
        });
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        if (roomNumber==4){
            mMessageView.setText(mCurrentTeam + " are in the parking. ");
        } else if (roomNumber==5){
            mMessageView.setText(mCurrentTeam + " are in the Security Room. ");
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                otherCommonSetUp();
                HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(roomNumber).existCharacterColor());
                List<Playable> searchTeam = new ArrayList<>();
                for (Playable player : searchteam) {
                    searchTeam.add(player);
                }
                mCurrentTeam = (ArrayList<Playable>) searchTeam;
                Collections.sort(mCurrentTeam, new Comparator<Playable>() {
                    @Override
                    public int compare(Playable o1, Playable o2) {
                        return o1.getColor().compareTo(o2.getColor());
                    }
                });

                Log.i(TAG, "mCurrentTeam: " + mCurrentTeam + " searchTeam List: " + searchTeam + " searchTeam Set: " + searchteam);
                Log.i(TAG, "colors: " + colors);
                mMessageView.setVisibility(View.INVISIBLE);
                int firstSearch = 0;
                for (int i=0; i<colors.size(); i++){
                    if (mCurrentTeam.get(0).getColor().equalsIgnoreCase(colors.get(i))){
                        firstSearch = i;
                        break;
                    }
                }
                Log.i(TAG, "firstsearch for parking: " + firstSearch);
                if (mMyPlayerID==firstSearch){
                    mThirdCount++;
                    GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(TURN).setValue(firstSearch);
                }
            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformVoteResult(GameData gameData, int roomNumber) {
        mDatabaseReference.child(PREVTURN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null ){
                    mPlayerButtons.get(dataSnapshot.getValue(Integer.TYPE)).setVisibility(View.INVISIBLE);
                    if (mMyPlayerID!=dataSnapshot.getValue(Integer.TYPE)){
                        updateDataFromFireBase(0,gameData,dataSnapshot.getValue(Integer.TYPE));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText("Voting results are in");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseReference.child(PLAYERBOOLEANANSWERS).setValue(null);
                Log.i(TAG, "vote results: " + votes);
                mMessageView.setVisibility(View.INVISIBLE);
                gameBroad.matchRoom(roomNumber).resetVoteResult();
                gameBroad.matchRoom(roomNumber).voteResultAfterVote(votes);
                ArrayList votes1 = (ArrayList<String>) votes;
                System.out.println("Step III: show vote result");
                System.out.println(votes1);
                Intent intent = ShowVoteResultActivity.newVoteResultIntent(MainActivity.this, votes1,gameBroad.matchRoom(roomNumber).getCurrentVoteResult(),mSecondCount);
                startActivityForResult(intent, REQUEST_CODE_VIEWRESULT);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformThreatUsingCanChangedVoteResult() {
        mMessageView.setText("Voting result can be changed by item THREAT, anyone want to change the result?");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Handler handler1 = new Handler();
                handler1.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        mDatabaseReference.child(PLAYERBOOLEANANSWERS).push().setValue(false);
                    }
                },DELAYEDSECONDSFOROPTIONSCHOSEN * 1000);

                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        disableYesNo();
                        mSecondCount = 2;
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-1);
                    }
                }, DELAYEDSECONDSFOROPTIONSCHOSEN * 1000);

                mMessageView.setVisibility(View.INVISIBLE);
                enableYesNo();
                mYesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler1.removeCallbacksAndMessages(null);
                        mMessageView.setVisibility(View.VISIBLE);
                        mMessageView.setText("Thank you, Please wait for other players");
                        disableYesNo();
                        mDatabaseReference.child(PLAYERBOOLEANANSWERS).push().setValue(true);

                    }
                });
                mNoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disableYesNo();
                        mMessageView.setVisibility(View.VISIBLE);
                        mMessageView.setText("Thank you, Please wait for other players");
                    }
                });

            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformIsThreatUsed(GameData gameData) {
        mMessageView.setText("Collecting Information");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseReference.child(PLAYERBOOLEANANSWERS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Boolean> usersChoises = new ArrayList<>();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            usersChoises.add(snapshot.getValue(Boolean.TYPE));
                        }
                        Boolean isThereTrue = false;
                        for (int i=0; i<usersChoises.size(); i++){
                            if (usersChoises.get(i)){
                                isThereTrue = true;
                                break;
                            }
                        }
                        if (gameData!=null){
                            if (isThereTrue){
                                mMessageView.setText("One of the players will used threat");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMessageView.setVisibility(View.INVISIBLE);
                                        int firstSearch = 0;
                                        for (int i=0; i<colors.size(); i++){
                                            if (mCurrentTeam.get(0).getColor().equalsIgnoreCase(colors.get(i))){
                                                firstSearch = i;
                                            }
                                        }
                                        Log.i(TAG, "firstsearch for parking: " + mCurrentTeam);
                                        Log.i(TAG, "firstsearch for parking: " + firstSearch);
                                        if (mMyPlayerID==firstSearch){
                                            GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                            mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                            mDatabaseReference.child(TURN).setValue(firstSearch);
                                        }
                                    }
                                },DELAYEDSECONDSFORMESSAGEVIE * 1000);
                            }else {
                                mMessageView.setText("Threat will not be used");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMessageView.setVisibility(View.INVISIBLE);
                                        int firstSearch = 0;
                                        for (int i=0; i<colors.size(); i++){
                                            if (mCurrentTeam.get(0).getColor().equalsIgnoreCase(colors.get(i))){
                                                firstSearch = i;
                                                break;
                                            }
                                        }
                                        Log.i(TAG, "firstsearch for parking: " + mCurrentTeam);
                                        Log.i(TAG, "firstsearch for parking: " + firstSearch);
                                        if (mMyPlayerID!=firstSearch){
                                            mSecondCount=2;
                                            mCountSetUp = mCurrentTeam.size() * 4;
                                            GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                            mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                            mDatabaseReference.child(TURN).setValue(-2);
                                        }
                                    }
                                },DELAYEDSECONDSFORMESSAGEVIE * 1000);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        },1000);
    }

    private void messageViewInformVoteSummary(GameData gameData, int roomNumber) {
        mDatabaseReference.child(PREVTURN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null ){
                    mPlayerButtons.get(dataSnapshot.getValue(Integer.TYPE)).setVisibility(View.INVISIBLE);
                    if (mMyPlayerID!=dataSnapshot.getValue(Integer.TYPE)){
                        updateDataFromFireBase(0,gameData,dataSnapshot.getValue(Integer.TYPE));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText("Final voting results are set");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.setVisibility(View.INVISIBLE);
                System.out.println("Show summary");
                HashMap<String, Integer> results = gameBroad.matchRoom(roomNumber).getCurrentVoteResult();
                Intent intent = ShowSimpleVoteResultActivity.newVoteResultIntent(MainActivity.this,results,mSecondCount);
                startActivityForResult(intent,REQUEST_CODE_VIEWSIMPLERESULT);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformTieorWinnerForParking(int roomNumber) {
        if (gameBroad.matchRoom(roomNumber).winner().equals("TIE")){
            System.out.println("Result is Tie");
            disableContinue();
            mMessageView.setText("Result is TIE. " + " No Searching will be performed");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCountSetUp = mCurrentTeam.size()*4+3;
                    GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(TURN).setValue(-1);
                }
            },DELAYEDSECONDSFORMESSAGEVIE*1000);
        } else {
            System.out.println("Winner determined");
            disableContinue();
            String winnercolor = gameBroad.matchRoom(roomNumber).winner();
            mMessageView.setText("Winner is " + gameBroad.matchPlayer(winnercolor) +
                    "\nAnd would search items");
            gameBroad.getItemDeck().shuffle();
            Item item1 = gameBroad.getItemDeck().deal();
            Item item2 = gameBroad.getItemDeck().deal();
            Item item3 = gameBroad.getItemDeck().deal();
            List<Item> itemtemplist = new ArrayList<>();
            itemtemplist.add(item1);
            itemtemplist.add(item2);
            itemtemplist.add(item3);
            mCurrentItemOptions = itemtemplist;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int turnValue = 0 ;
                    for (int i=0; i<colors.size(); i++){
                        if (winnercolor.equalsIgnoreCase(colors.get(i))){
                            turnValue=i;
                            break;
                        }
                    }
                    mSecondCount=4;
                    GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(TURN).setValue(turnValue);
                }
            },DELAYEDSECONDSFORMESSAGEVIE*1000);
        }
    }

    private void messageViewInformMovingToChiefElect() {
        disableContinue();
        String winnercolor = gameBroad.matchRoom(4).winner();
        mDatabaseReference.child(PREVTURN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    int prevTurn = dataSnapshot.getValue(Integer.TYPE);
                    String receivedColor = colors.get(prevTurn);
                    if (gameBroad.matchRoom(4).winner().equals("TIE")){
                        mMessageView.setText("Game Phase II: Security Chief selected");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mMessageView.setVisibility(View.INVISIBLE);
                                mCountPhase++;
                                mCountSetUp=0;
                                mSecondCount=0;
                                mThirdCount=0;
                                votes.clear();
                                mCurrentTeam.clear();
                                mCurrentItemOptions.clear();
                                mCurrentZombiesRooms.clear();
                                mCurrentYesNo=false;
                                mCurrentYesNoMain = false;
                                GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                mDatabaseReference.child(TURN).setValue(-2);
                                mDatabaseReference.child(PREVTURN).setValue(-1);

                            }
                        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
                    }else {
                        mMessageView.setText("Player " + receivedColor + " received an item from Player " + winnercolor);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mMessageView.setText("Game Phase II: Security election");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMessageView.setVisibility(View.INVISIBLE);
                                        mCountPhase=4;
                                        mCountSetUp=0;
                                        mSecondCount=0;
                                        mThirdCount=0;
                                        votes.clear();
                                        mCurrentTeam.clear();
                                        mCurrentItemOptions.clear();
                                        mCurrentZombiesRooms.clear();
                                        mCurrentYesNo=false;
                                        mCurrentYesNoMain = false;
                                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                        mDatabaseReference.child(TURN).setValue(-2);
                                        mDatabaseReference.child(PREVTURN).setValue(-1);

                                    }
                                },DELAYEDSECONDSFORMESSAGEVIE * 1000);
                            }
                        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void messageViewInformSecurityIsEmptyandMovetoNextPhase() {
        updateRoom(MainActivity.this);
        mMainActivityLayout.invalidate();
        mMessageView.setVisibility(View.VISIBLE);
        if (mSecondCount==0){
            mMessageView.setText("Due to Security is empty, no election will be performed");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMessageView.setVisibility(View.INVISIBLE);
                    if (mMyPlayerID==0){
                        mCountSetUp = mCurrentTeam.size()*4;
                        mSecondCount = 3;
                        mThirdCount=2;
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-1);
                    }
                }
            },DELAYEDSECONDSFORMESSAGEVIE * 1000);
        }
    }

    private void messageViewRelatedChiefElectionWithElectionTeam(GameData gameData) {
        mCurrentTeam.clear();
        HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(5).existCharacterColor());
        List<Playable> searchTeam = new ArrayList<>();
        for (Playable player : searchteam) {
            searchTeam.add(player);
        }
        mCurrentTeam = (ArrayList<Playable>) searchTeam;
        Collections.sort(mCurrentTeam, new Comparator<Playable>() {
            @Override
            public int compare(Playable o1, Playable o2) {
                return o1.getColor().compareTo(o2.getColor());
            }
        });
        if (mThirdCount==0){
            messageViewInformExistedMembers(5);
        } else if (mCountSetUp == mCurrentTeam.size()*2  && mSecondCount == 0){
            messageViewInformVoteResult(gameData,5);
        } else if (mCountSetUp >= mCurrentTeam.size()*2 && mSecondCount > 0 && mSecondCount<=2 && mCountSetUp < mCurrentTeam.size()*4) {
            if (mSecondCount==1){
                messageViewInformThreatUsingCanChangedVoteResult();
            } else if (mSecondCount==2){
                messageViewInformIsThreatUsed(gameData);
            }
        } else if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 2 ){
            messageViewInformVoteSummary(gameData,5);
        } else if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 3 ){
            messageViewInformTieorWinnerForSecurity();
        } else if (mCountSetUp < (mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 2) && mSecondCount==4) {
            messageViewInformUsingSecurityCamera();
        } else if (mCountSetUp < (mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 2) && mSecondCount==5) {
            messageViewInformIsCameraUsed(gameData);
        } else if (mCountSetUp == mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 3){
            messageViewInformMovetoViewAndMove();
        }
    }

    private void messageViewInformTieorWinnerForSecurity() {
        disableContinue();
        if (gameBroad.matchRoom(5).winner().equals("TIE") || gameBroad.matchRoom(5).isEmpty()){
            Random generator = new Random();
            mCurrentStartPlayerIndex = generator.nextInt(gameBroad.getPlayers().size());
            mMessageView.setText("No chief is elected." + " A ramdom player will start first");
            mIsChiefSelected=false;
        } else {
            String winnercolor = gameBroad.matchRoom(5).winner();
            mCurrentStartPlayerIndex = gameBroad.getPlayers().indexOf(gameBroad.matchPlayer(winnercolor));
            mMessageView.setText("Winner is " + gameBroad.matchPlayer(winnercolor) +
                    "\nAnd would see the approaching zombies");
            mIsChiefSelected = true;
        }
        mCurrentYesNo=false;
        mCurrentYesNoMain = false;
        System.out.println("Preparing Zombies");
        TwoPairofDice fourdices = new TwoPairofDice();
        int DiceOne = fourdices.rollDieOne();
        int DiceTwo = fourdices.rollDieTwo();
        int DiceThree = fourdices.rollDieThree();
        int DiceFour = fourdices.rollDieFour();
        List<Integer> dices = new ArrayList<>();
        dices.add(DiceOne);
        dices.add(DiceTwo);
        dices.add(DiceThree);
        dices.add(DiceFour);
        mCurrentZombiesRooms = (ArrayList<Integer>) dices;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSecondCount=4;
                GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                mDatabaseReference.child(TURN).setValue(-5);
                mDatabaseReference.child(PLAYERBOOLEANANSWERS).setValue(null);
                mDatabaseReference.child(ZOMBIEROOMS).setValue(mCurrentZombiesRooms);
                mDatabaseReference.child(FIRSTPLAYERINDEX).setValue(mCurrentStartPlayerIndex);
                mDatabaseReference.child(ISCHIEFELECTED).setValue(mIsChiefSelected);

            }
        },DELAYEDSECONDSFORMESSAGEVIE*1000);
    }

    private void messageViewInformUsingSecurityCamera() {
        mMessageView.setText("Please confirm if you want to use security camera to view the approaching zombies");
        mDatabaseReference.child(ZOMBIEROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    mCurrentZombiesRooms.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        mCurrentZombiesRooms.add(snapshot.getValue(Integer.TYPE));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseReference.child(FIRSTPLAYERINDEX).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    mCurrentStartPlayerIndex = dataSnapshot.getValue(Integer.TYPE);
                    mCurrentStartPlayer = gameBroad.getPlayers().get(mCurrentStartPlayerIndex);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Handler handler1 = new Handler();
                handler1.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        mDatabaseReference.child(PLAYERBOOLEANANSWERS).push().setValue(false);
                    }
                },DELAYEDSECONDSFOROPTIONSCHOSEN * 1000);

                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        disableYesNo();
                        mSecondCount = 5;
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-2);
                    }
                }, DELAYEDSECONDSFOROPTIONSCHOSEN * 1000);

                mMessageView.setVisibility(View.INVISIBLE);
                enableYesNo();
                mYesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler1.removeCallbacksAndMessages(null);
                        mMessageView.setVisibility(View.VISIBLE);
                        mMessageView.setText("Thank you, Please wait for other players");
                        disableYesNo();
                        mDatabaseReference.child(PLAYERBOOLEANANSWERS).push().setValue(true);

                    }
                });
                mNoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disableYesNo();
                        mMessageView.setVisibility(View.VISIBLE);
                        mMessageView.setText("Thank you, Please wait for other players");
                    }
                });

            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformIsCameraUsed(GameData gameData) {
        mMessageView.setText("Collecting Information");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseReference.child(PLAYERBOOLEANANSWERS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Boolean> usersChoises = new ArrayList<>();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            usersChoises.add(snapshot.getValue(Boolean.TYPE));
                        }
                        Boolean isThereTrue = false;
                        for (int i=0; i<usersChoises.size(); i++){
                            if (usersChoises.get(i)){
                                isThereTrue = true;
                                break;
                            }
                        }
                        if (gameData!=null){
                            if (isThereTrue){
                                mMessageView.setText("One of the players will used Security Camera");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMessageView.setVisibility(View.INVISIBLE);
                                        int firstMove = 0;
                                        for (int i=0; i<colors.size(); i++){
                                            if (gameBroad.getPlayers().get(0).getColor().equalsIgnoreCase(colors.get(i))){
                                                firstMove = i;
                                            }
                                        }
                                        if (mMyPlayerID==firstMove){
                                            mSecondCount=6;
                                            GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                            mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                            mDatabaseReference.child(TURN).setValue(firstMove);
                                        }
                                    }
                                },DELAYEDSECONDSFORMESSAGEVIE * 1000);
                            } else {
                                mMessageView.setText("Security camera will not be used");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMessageView.setVisibility(View.INVISIBLE);
                                        mCountSetUp = mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 3;
                                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                        mDatabaseReference.child(TURN).setValue(-1);

                                    }
                                },DELAYEDSECONDSFORMESSAGEVIE * 1000);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        },1000);
    }

    private void messageViewInformMovetoViewAndMove() {
        disableContinue();
        mMessageView.setText("Game Phase III: Chief Viewing and Moving");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.setVisibility(View.INVISIBLE);
                mCountPhase=5;
                mCountSetUp=0;
                mSecondCount=0;
                mThirdCount=0;
                mFourthCount=0;
                mFifthCount=0;
                mSixCount=0;
                votes.clear();
                mCurrentTeam.clear();
                mCurrentItemOptions.clear();
                mCurrentYesNoMain = false;
                mCurrentYesNo = false;
                GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                mDatabaseReference.child(TURN).setValue(-2);
                mDatabaseReference.child(PREVTURN).setValue(-1);
            }
        },DELAYEDSECONDSFORMESSAGEVIE*1000);
    }

    private void messageViewInformRelatedtoMoveAndView(GameData gameData) {
        if (mCountSetUp==0){
            messageViewReinformIsChiefElectedAndDetermineNextMove();
        } else if (mCountSetUp==1 && mSecondCount==1 && mThirdCount==1) {
            messageViewInformChiefRoomNumber(gameData);
        } else if (mCountSetUp==gameBroad.getPlayers().size()*2) {
            messageViewInformRoomSelectionSummary(gameData);
        } else if (mCountSetUp==gameBroad.getPlayers().size()*4) {
            messageViewInformMovetoZombieRevealAndAttack(gameData);
        }
    }

    private void messageViewReinformIsChiefElectedAndDetermineNextMove() {
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        mDatabaseReference.child(ISCHIEFELECTED).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    mIsChiefSelected = dataSnapshot.getValue(Boolean.TYPE);
                    if (!mIsChiefSelected){
                        mMessageView.setText("Due to no chief, a ramdom player will start first");
                    } else {
                        mMessageView.setText("Chief was elected, chief will perform view and choose room number");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.setVisibility(View.INVISIBLE);
                mDatabaseReference.child(FIRSTPLAYERINDEX).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null){
                            mCurrentStartPlayerIndex = dataSnapshot.getValue(Integer.TYPE);
                            mCurrentStartPlayer = gameBroad.getPlayers().get(mCurrentStartPlayerIndex);
                            int nextMove = 0;
                            for (int i=0; i<colors.size(); i++){
                                if (mCurrentStartPlayer.getColor().equalsIgnoreCase(colors.get(i))){
                                    nextMove = i;
                                }
                            }
                            mDatabaseReference.child(TURN).setValue(nextMove);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformChiefRoomNumber(GameData gameData) {
        Log.i(TAG, "Displaying Chief Selection");
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        mDatabaseReference.child(ROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    roomspicked.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        roomspicked.add(snapshot.getValue(Integer.TYPE));
                    }
                    int chiefElection = roomspicked.get(0);
                    Room roomName = gameBroad.matchRoom(chiefElection);
                    mMessageView.setText("After reviewing the security cameara, chief will move to Room " + roomName.getRoomNum() + ": " + roomName.getName());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMessageView.setVisibility(View.INVISIBLE);
                            mDatabaseReference.child(FIRSTPLAYERINDEX).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot!=null){
                                        mThirdCount++;
                                        mCountSetUp=2;
                                        mCurrentStartPlayerIndex = dataSnapshot.getValue(Integer.TYPE);
                                        int q = mCurrentStartPlayerIndex + 1;
                                        int i = 0;
                                        if (q < gameBroad.getPlayers().size()) {
                                            i = q;
                                        } else {
                                            i = q - gameBroad.getPlayers().size();
                                        }
                                        int nextMove = 0;
                                        for (int k=0; k<colors.size(); k++){
                                            if (gameBroad.getPlayers().get(i).getColor().equalsIgnoreCase(colors.get(k))){
                                                nextMove = k;
                                            }
                                        }
                                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                        mDatabaseReference.child(PREVTURN).setValue(-1);
                                        mDatabaseReference.child(TURN).setValue(nextMove);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    },DELAYEDSECONDSFORMESSAGEVIE*1000);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "dataerror" + databaseError.toString());
            }
        });


    }

    private void messageViewInformRoomSelectionSummary(GameData gameData) {
        mDatabaseReference.child(PREVTURN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    int prevTurn = dataSnapshot.getValue(Integer.TYPE);
                    if (mMyPlayerID!=prevTurn){
                        updateDataFromFireBase(0,gameData,prevTurn);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText("Room Selection is Finished, now we move character into the Room Individually");
        mMessageView.setEnabled(false);
        mDatabaseReference.child(ROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    roomspicked.clear();
                    Log.i(TAG, "reloading rooms");
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        roomspicked.add(snapshot.getValue(Integer.TYPE));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseReference.child(INDEXS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    playersIndex.clear();
                    Log.i(TAG, "reloading indexs");
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        playersIndex.add(snapshot.getValue(Integer.TYPE));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String message = "";
                Log.i(TAG, "Player Index: " + playersIndex);
                Log.i(TAG, "Room: " + roomspicked);
                for (int i=0; i<roomspicked.size(); i++){
                    message += "\nPlayer " + gameBroad.getPlayers().get(playersIndex.get(i)).getColor() + " to Room " + roomspicked.get(i) ;
                }
                Log.i(TAG,"message: " +  message);
                mMessageView.setText("Players' Choises are: " + message);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int nextMove = 0;
                        for (int i=0; i<colors.size(); i++){
                            if (mCurrentStartPlayer.getColor().equalsIgnoreCase(colors.get(i))){
                                nextMove = i;
                            }
                        }
                        mFourthCount=1;
                        mCountSetUp = 2*gameBroad.getPlayers().size();
                        GameData gameData = new GameData(mCountPhase, mCountSetUp, mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(nextMove);
                    }
                },10*1000);
            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformMovetoZombieRevealAndAttack(GameData gameData) {
        disableContinue();
        mDatabaseReference.child(PREVTURN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    int prevTurn = dataSnapshot.getValue(Integer.TYPE);
                    if (mMyPlayerID!=prevTurn){
                        updateDataFromFireBase(0,gameData,prevTurn);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        updateRoom(MainActivity.this);
        mMainActivityLayout.invalidate();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText("Game Phase IV: Zombies Revealed and Attacked");
        mMessageView.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCountPhase++;
                mCountSetUp=0;
                mSecondCount=0;
                mThirdCount=0;
                mFourthCount=0;
                votes.clear();
                mCurrentTeam.clear();
                mCurrentItemOptions.clear();
                playersIndex.clear();
                roomspicked.clear();
                mCurrentYesNo=false;
                mCurrentYesNoMain = false;
                GameData gameData = new GameData(mCountPhase, mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                mDatabaseReference.child(FIRSTPLAYERINDEX).setValue(null);
                mDatabaseReference.child(ROOMS).setValue(null);
                mDatabaseReference.child(INDEXS).setValue(null);
                mDatabaseReference.child(ISCHIEFELECTED).setValue(null);
                mDatabaseReference.child(TURN).setValue(-2);
                mDatabaseReference.child(PREVTURN).setValue(-1);
            }
        }, DELAYEDSECONDSFORMESSAGEVIE*1000);
    }

    private void messageViewRelatedtoRevealandAttack(GameData gameData) {
        if (mFourthCount==0){
            messageViewShowZombiestoAll();
        } else if (mFourthCount==1){
            messageViewInformMoreZomibies();
        } else if (mFifthCount<6 &&  mFourthCount>=2) {
            messageViewRelatedtoFallenCalculationsAndZombiesAttack(gameData);
        } else if (mFifthCount==6){
            messageViewInformMoveBacktoParkingOrWinnerDetermined();
        }
    }

    private void messageViewShowZombiestoAll() {
        mMessageView.setEnabled(false);
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText("Now zombies approacing room numbers reveal");
        mDatabaseReference.child(ZOMBIEROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    mCurrentZombiesRooms.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        mCurrentZombiesRooms.add(snapshot.getValue(Integer.TYPE));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.setVisibility(View.INVISIBLE);
                System.out.println("Showing Chief the zombies");
                Intent intent = ShowingZombieActivity.newShowZombiesIntent(MainActivity.this, mCurrentZombiesRooms,mFourthCount);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, REQUEST_CODE_VIEWZOMBIEAll);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter,android.support.v7.appcompat.R.anim.abc_popup_exit );
            }
        }, DELAYEDSECONDSFORMESSAGEVIE * 1000);
    }

    private void messageViewInformMoreZomibies() {
        mDatabaseReference.child(PLAYERBOOLEANANSWERS).setValue(null);
        mMessageView.setEnabled(false);
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText("There are more zombies coming");
        mDatabaseReference.child(ZOMBIEROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    mCurrentZombiesRooms.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        mCurrentZombiesRooms.add(snapshot.getValue(Integer.TYPE));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.setVisibility(View.INVISIBLE);
                if (mCurrentMoreZombies.size() == 0) {
                    for (int roomNumber: mCurrentZombiesRooms){
                        gameBroad.matchRoom(roomNumber).zombieApproached();
                    }
                    System.out.println("Showing More Zombie");
                    if (gameBroad.mostPeople().getRoomNum()==7){
                        mCurrentMoreZombies.add(0);
                    } else {
                        gameBroad.mostPeople().zombieApproached();
                        mCurrentMoreZombies.add(gameBroad.mostPeople().getRoomNum());
                    }
                    if (gameBroad.mostModel().getRoomNum()==7){
                        mCurrentMoreZombies.add(0);
                    } else {
                        gameBroad.mostModel().zombieApproached();
                        mCurrentMoreZombies.add(gameBroad.mostModel().getRoomNum());
                    }
                }
                Log.i(TAG, "More zombies: " + mCurrentMoreZombies);
                Intent intent = ShowMoreZombiesActivity.newShowZombiesIntent(MainActivity.this, mCurrentMoreZombies,mFourthCount);
                startActivityForResult(intent, REQUEST_CODE_VIEWZOMBIEALLMORE);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter,android.support.v7.appcompat.R.anim.abc_popup_exit );
            }
        },DELAYEDSECONDSFORMESSAGEVIE*1000);
    }

    private void messageViewRelatedtoFallenCalculationsAndZombiesAttack(GameData gameData) {
        final Room theCurrentRoom = gameBroad.matchRoom(mFifthCount+1);
        HashSet<Playable> playersInTheRoom = gameBroad.WhoCan(theCurrentRoom.existCharacterColor());
        List<Playable> playersInTheRoomList = new ArrayList<>();
        for (Playable player : playersInTheRoom) {
            playersInTheRoomList.add(player);
        }
        if (mFourthCount==2){
            messageViewInformifRoomisFallenAndItemsUsing(theCurrentRoom, playersInTheRoomList, gameData);
        } else if (mFourthCount==3){
            messageViewRelatedtoFallenRoomVoteAndThreatUsedAndCharacterPicked(gameData, theCurrentRoom);
        } else if (mFourthCount==4){
            messageViewDisplayItemAfterEffect(theCurrentRoom);
        } else if (mFourthCount==5){
            messageViewInformPlayerHasNoCharacter();
        } else if (mFourthCount==6){
            messageViewInformMovingToNextRoom();
        }
    }

    private void messageViewInformifRoomisFallenAndItemsUsing(Room theCurrentRoom, List<Playable> playersInTheRoomList, GameData gameData) {
        if (!theCurrentRoom.isFallen() && mCountSetUp==0){
            System.out.println(theCurrentRoom.getName() +  " is not fallen");
            mCountSetUp=0;
            mSecondCount=0;
            mThirdCount=0;
            mFourthCount=6;
            Log.i(TAG, " mMyPlayerId: " + mMyPlayerID + " ControlId: " + getControlId());
            if (mMyPlayerID==getControlId()){
                GameData gameData1 = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                mDatabaseReference.child(GAMEDATA).setValue(gameData1);
                mDatabaseReference.child(TURN).setValue(-20);
            }
        } else if (theCurrentRoom.isFallen() && theCurrentRoom.getRoomNum()==4){
            System.out.println(theCurrentRoom.getName() + "  is fallen");
            disableContinue();
            mMessageView.setVisibility(View.VISIBLE);
            mMessageView.setEnabled(false);
            mMessageView.setText("Parking has fallen, but items can not be triggered here");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMessageView.setVisibility(View.INVISIBLE);
                    mCountSetUp=0;
                    mSecondCount=0;
                    mThirdCount=0;
                    mFourthCount=3;
                    mSixCount++;
                    if (mMyPlayerID==getControlId()){
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-20);
                    }
                }
            },DELAYEDSECONDSFORMESSAGEVIE*1000);
        } else if (theCurrentRoom.isFallen() && theCurrentRoom.getRoomNum()!=4) {
            if (mSecondCount==0){
                mSixCount++;
                messageViewInformItemCanbeUsed(theCurrentRoom);
                originalTeamSize = playersInTheRoomList.size();
                mCurrentTeam = (ArrayList<Playable>) playersInTheRoomList;
                Collections.sort(mCurrentTeam, new Comparator<Playable>() {
                    @Override
                    public int compare(Playable o1, Playable o2) {
                        return o1.getColor().compareTo(o2.getColor());
                    }
                });
            } else if (mSecondCount==1 && mCountSetUp==0){
                messageViewInformIsItemUsing (theCurrentRoom);
            } else if (mCountSetUp>0 && mCountSetUp< originalTeamSize *2){
                messageViewInformWhatItemHasBeenUsed(gameData,theCurrentRoom);
            } else if (mCountSetUp==originalTeamSize*2){
                messageViewInformCurrentIsStillFallenAfterItemUsed(theCurrentRoom);
            }
        } else if (!theCurrentRoom.isFallen() && theCurrentRoom.getRoomNum()!=4 && mCountSetUp!=0){
            messageViewInformCurrentIsStillFallenAfterItemUsed(theCurrentRoom);
        }
    }

    private void messageViewInformItemCanbeUsed(Room theCurrentRoom) {
        disableContinue();
        mMessageView.setText(theCurrentRoom.getName() +  " has fallen and can use item to revised by items, please confirm if you want to use item");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Handler handler1 = new Handler();
                handler1.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        mDatabaseReference.child(PLAYERBOOLEANANSWERS).push().setValue(false);
                    }
                },DELAYEDSECONDSFOROPTIONSCHOSEN * 1000);
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        disableYesNo();
                        mSecondCount = 1;
                        if (mMyPlayerID==getControlId()){
                            GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                            mDatabaseReference.child(GAMEDATA).setValue(gameData);
                            mDatabaseReference.child(TURN).setValue(-1);
                        }
                    }
                }, DELAYEDSECONDSFOROPTIONSCHOSEN * 1000);
                mMessageView.setVisibility(View.INVISIBLE);
                enableYesNo();
                mYesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler1.removeCallbacksAndMessages(null);
                        mMessageView.setVisibility(View.VISIBLE);
                        mMessageView.setText("Thank you, Please wait for other players");
                        disableYesNo();
                        mDatabaseReference.child(PLAYERBOOLEANANSWERS).push().setValue(true);

                    }
                });
                mNoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disableYesNo();
                        mMessageView.setVisibility(View.VISIBLE);
                        mMessageView.setText("Thank you, Please wait for other players");
                    }
                });

            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);
        System.out.println("Confirm if want to use item");
    }

    private void messageViewInformIsItemUsing(Room theCurrentRoom) {
        HashSet<Playable> playersInTheRoom = gameBroad.WhoCan(theCurrentRoom.existCharacterColor());
        List<Playable> playersInTheRoomList = new ArrayList<>();
        for (Playable player : playersInTheRoom) {
            playersInTheRoomList.add(player);
        }
        mCurrentTeam = (ArrayList<Playable>) playersInTheRoomList;
        Collections.sort(mCurrentTeam, new Comparator<Playable>() {
            @Override
            public int compare(Playable o1, Playable o2) {
                return o1.getColor().compareTo(o2.getColor());
            }
        });
        mMessageView.setText("Collecting Information");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseReference.child(PLAYERBOOLEANANSWERS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Boolean> usersChoises = new ArrayList<>();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            usersChoises.add(snapshot.getValue(Boolean.TYPE));
                        }
                        Boolean isThereTrue = false;
                        for (int i=0; i<usersChoises.size(); i++){
                            if (usersChoises.get(i)){
                                isThereTrue = true;
                                break;
                            }
                        }
                        if (isThereTrue){
                            mMessageView.setText("One of the players will used item");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mMessageView.setVisibility(View.INVISIBLE);
                                    int firstSearch = 0;
                                    for (int i=0; i<colors.size(); i++){
                                        if (mCurrentTeam.get(0).getColor().equalsIgnoreCase(colors.get(i))){
                                            firstSearch = i;
                                        }
                                    }
                                    Log.i(TAG, "fallen team for item using: " + mCurrentTeam);
                                    Log.i(TAG, "firstitemuse for item using: " + firstSearch);
                                    if (mMyPlayerID==firstSearch){
                                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                        mDatabaseReference.child(TURN).setValue(firstSearch);
                                    }
                                }
                            },DELAYEDSECONDSFORMESSAGEVIE * 1000);
                        }else {
                            mMessageView.setText("Item will not be used");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mMessageView.setVisibility(View.INVISIBLE);
                                    int firstSearch = 0;
                                    for (int i=0; i<colors.size(); i++){
                                        if (mCurrentTeam.get(0).getColor().equalsIgnoreCase(colors.get(i))){
                                            firstSearch = i;
                                            break;
                                        }
                                    }
                                    Log.i(TAG, "firstsearch for parking: " + mCurrentTeam);
                                    Log.i(TAG, "firstsearch for parking: " + firstSearch);
                                    if (mMyPlayerID!=firstSearch){
                                        mCountSetUp=0;
                                        mSecondCount=0;
                                        mThirdCount=0;
                                        mFourthCount=3;
                                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                        mDatabaseReference.child(TURN).setValue(-2);
                                    }
                                }
                            },DELAYEDSECONDSFORMESSAGEVIE * 1000);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        },1000);
    }

    private void messageViewInformWhatItemHasBeenUsed(GameData gameData,Room theCurrentRoom) {
        Playable prevPlayer = mCurrentTeam.get(gameData.getmPrevICount());
        final boolean isItemUsed  = gameData.getmIsUsedItem();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText("Loading Information");
        mMessageView.setEnabled(false);
        if (isItemUsed) {
            int itemNumber = gameData.getItemNumber();
            for (int i = 0; i < items.size(); i++) {
                if (itemNumber == items.get(i).getItemNum()) {
                    mCurrentSelectedItem = items.get(i);
                }
            }
            String characterName = gameData.getmAffectedGameCharacter();
            GameCharacter affectedCharacter = gameBroad.matchGameCharacter(prevPlayer, characterName);
            mCurrentSelectedItem.setAfteraffectedRoomNumber(gameData.getmAfterAffectedRoomNumber());
            Log.i(TAG, " AfterAffectedRoomNumber From Data: " + gameData.getmAfterAffectedRoomNumber());
            if (gameData.getmAffectedGameCharacter() != null) {
                mCurrentSelectedItem.setAffectedGameCharacter(affectedCharacter);
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if (isItemUsed){
                   if (mCurrentSelectedItem.getItemNum()==3){
                       disableContinue();
                       mMessageView.setText(prevPlayer + " used Axe, one zombie has been killed");
                       theCurrentRoom.zombieKilled();
                   }
                   if (mCurrentSelectedItem.getItemNum()==4){
                       mMessageView.setText(prevPlayer + " used Shotgun, two zombie has been killed");
                       theCurrentRoom.zombieKilled();
                       theCurrentRoom.zombieKilled();
                   }
                   if (mCurrentSelectedItem.getItemNum()==5){
                       mMessageView.setText(prevPlayer + " used Hareware, one zombie has been temporary block");
                       theCurrentRoom.zombieKilled();
                       mUsedItem.add(mCurrentSelectedItem);
                       mPlayersUsedItem.add(prevPlayer);
                   }
                   if (mCurrentSelectedItem.getItemNum()==6){
                       mMessageView.setText(prevPlayer + " used Hidden, his/her" + mCurrentSelectedItem.getAffectedGameCharacter() + " is hiding in the room");
                       theCurrentRoom.leave(gameBroad.matchGameCharacter(prevPlayer,mCurrentSelectedItem.getAffectedGameCharacter().getName()));
                       mUsedItem.add(mCurrentSelectedItem);
                       mPlayersUsedItem.add(prevPlayer);
                   }
                   if (mCurrentSelectedItem.getItemNum()==7){
                       mMessageView.setText(prevPlayer + " used Sprint, his/her" + mCurrentSelectedItem.getAffectedGameCharacter() + " has left the room");
                       theCurrentRoom.leave(gameBroad.matchGameCharacter(prevPlayer,mCurrentSelectedItem.getAffectedGameCharacter().getName()));
                       mUsedItem.add(mCurrentSelectedItem);
                       mPlayersUsedItem.add(prevPlayer);
                   }
                   updateRoom(MainActivity.this);
                   mMainActivityLayout.invalidate();
               } else {
                  mMessageView.setText(prevPlayer +  " did not use any item");
               }
            }
        },DELAYEDSECONDSFORMESSAGEVIE*1000);
        Handler handler1 =  new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"item used: " + mUsedItem + " player used " + mPlayersUsedItem);
                mMessageView.setVisibility(View.INVISIBLE);
                otherCommonSetUp();
                mCountSetUp++;
                int nextMove = 0;
                if (mCountSetUp==originalTeamSize*2){
                    nextMove = -2;
                } else {
                    for (int i=0; i<colors.size(); i++){
                        if (mCurrentTeam.get(gameData.getmPrevICount()+1).getColor().equalsIgnoreCase(colors.get(i))) {
                            nextMove = i;
                        }
                    }
                }
                if (mMyPlayerID==getControlId()){
                    GameData gameData = new GameData(mCountPhase, mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(TURN).setValue(nextMove);
                    mDatabaseReference.child(PREVTURN).setValue(-1);
                }
            }
        },DELAYEDSECONDSFOROPTIONSCHOSEN*1000);
    }

    private void messageViewInformCurrentIsStillFallenAfterItemUsed(Room theCurrentRoom) {
        disableContinue();
        mMessageView.setEnabled(false);
        mMessageView.setVisibility(View.VISIBLE);
        if (theCurrentRoom.isFallen()){
            Log.i(TAG, "still fallen after item used");
            mMessageView.setText(theCurrentRoom.getName() +  " is still fallen after item used");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCountSetUp=0;
                    mSecondCount=0;
                    mThirdCount=0;
                    mFourthCount=3;
                    if (mMyPlayerID==getControlId()){
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-20);
                    }
                }
            },DELAYEDSECONDSFORLONGMESSAGE*1000);
        }else {
            Log.i(TAG, "not fallen after item used");
            mMessageView.setText(theCurrentRoom.getName() +  " is not fallen after item used");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCountSetUp=0;
                    mSecondCount=0;
                    mThirdCount=0;
                    mFourthCount=4;
                    if (mMyPlayerID==getControlId()){
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-20);
                    }
                }
            },DELAYEDSECONDSFORLONGMESSAGE*1000);
            System.out.println("Not fallen after the item used");
        }
    }

    private void messageViewRelatedtoFallenRoomVoteAndThreatUsedAndCharacterPicked(GameData gameData, Room theCurrentRoom) {
        if (mThirdCount==0){
            messageViewInformFallenRoomTeamMembers(theCurrentRoom);
        } else if (mCountSetUp == mCurrentTeam.size()*2  && mSecondCount == 0){
            messageViewInformVoteResult(gameData,theCurrentRoom.getRoomNum());
        } else if (mCountSetUp >= mCurrentTeam.size()*2 && mSecondCount > 0 && mCountSetUp < mCurrentTeam.size()*4) {
            if (mSecondCount==1){
                messageViewInformThreatUsingCanChangedVoteResult();
            } else if (mSecondCount==2){
                messageViewInformIsThreatUsed(gameData);
            }
        } else if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 2 ){
            messageViewInformVoteSummary(gameData,theCurrentRoom.getRoomNum());
        } else if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 3){
            messageViewInformTieorLoserForFallenRoom(theCurrentRoom.getRoomNum());
        } else if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 4) {
            messageViewInformSelectedVictim(theCurrentRoom.getRoomNum());
        } else if (mCountSetUp == mCurrentTeam.size()*4 + 1){
            messageViewInformSelectedDeadCharacter(theCurrentRoom);
        }
    }

    private void messageViewInformFallenRoomTeamMembers(Room theCurrrentRoom) {
        HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(theCurrrentRoom.getRoomNum()).existCharacterColor());
        List<Playable> searchTeam = new ArrayList<>();
        for (Playable player : searchteam) {
            searchTeam.add(player);
        }
        mCurrentTeam = (ArrayList<Playable>) searchTeam;
        Collections.sort(mCurrentTeam, new Comparator<Playable>() {
            @Override
            public int compare(Playable o1, Playable o2) {
                return o1.getColor().compareTo(o2.getColor());
            }
        });
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText(mCurrentTeam + " are in the fallen Room " + theCurrrentRoom.getRoomNum() + ": " + theCurrrentRoom.getName());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                otherCommonSetUp();
                HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(theCurrrentRoom.getRoomNum()).existCharacterColor());
                List<Playable> searchTeam = new ArrayList<>();
                for (Playable player : searchteam) {
                    searchTeam.add(player);
                }
                mCurrentTeam = (ArrayList<Playable>) searchTeam;
                Collections.sort(mCurrentTeam, new Comparator<Playable>() {
                    @Override
                    public int compare(Playable o1, Playable o2) {
                        return o1.getColor().compareTo(o2.getColor());
                    }
                });
                Log.i(TAG, "mCurrentTeam: " + mCurrentTeam + " fallenTeam List: " + searchTeam + " fallenTeam Set: " + searchteam);
                Log.i(TAG, "colors: " + colors);
                mMessageView.setVisibility(View.INVISIBLE);
                int firstSearch = 0;
                for (int i=0; i<colors.size(); i++){
                    if (mCurrentTeam.get(0).getColor().equalsIgnoreCase(colors.get(i))){
                        firstSearch = i;
                        break;
                    }
                }
                Log.i(TAG, "firstvote for fallling: " + firstSearch);
                if (mMyPlayerID==firstSearch){
                    mThirdCount++;
                    GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(TURN).setValue(firstSearch);
                }
            }
        },DELAYEDSECONDSFORLONGMESSAGE * 1000);
    }

    private void messageViewInformTieorLoserForFallenRoom( int roomNumber) {
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setEnabled(false);
        HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(roomNumber).existCharacterColor());
        List<Playable> searchTeam = new ArrayList<>();
        for (Playable player : searchteam) {
            searchTeam.add(player);
        }
        mCurrentTeam = (ArrayList<Playable>) searchTeam;
        String firstcolor =  mCurrentTeam.get(0).getColor();
        int deterId = 0;
        for (int i=0; i<colors.size(); i++){
            if (firstcolor.equalsIgnoreCase(colors.get(i))){
                deterId = i;
            }
        }
        if (gameBroad.matchRoom(roomNumber).winner().equals("TIE")){
            System.out.println("Result is Tie");
            Collections.sort(mCurrentTeam, new Comparator<Playable>() {
                @Override
                public int compare(Playable o1, Playable o2) {
                    return o1.getColor().compareTo(o2.getColor());
                }
            });
            Random generator = new Random();
            int num = generator.nextInt(mCurrentTeam.size());
            mCurrentVictim = mCurrentTeam.get(num);
            mMessageView.setText("Result is TIE. " + "The system will ramdomly select a victim in the team");
        } else {
            System.out.println("Loser determined");
            String losercolor = gameBroad.matchRoom(roomNumber).winner();
            mCurrentVictim = gameBroad.matchPlayer(losercolor);
            mMessageView.setText("Loser is " + gameBroad.matchPlayer(losercolor));
        }
        final int puttingiD = deterId;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSecondCount=4;
                if (mMyPlayerID==getControlId()){
                    GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(TURN).setValue(-5);
                    mDatabaseReference.child(VICTIMCOLOR).setValue(mCurrentVictim.getColor());
                }
            }
        },DELAYEDSECONDSFORMESSAGEVIE*1000);
    }

    private void messageViewInformSelectedVictim(int roomNumber) {
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setEnabled(false);
        mMessageView.setText("Loading Information");
        HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(roomNumber).existCharacterColor());
        List<Playable> searchTeam = new ArrayList<>();
        for (Playable player : searchteam) {
            searchTeam.add(player);
        }
        mCurrentTeam = (ArrayList<Playable>) searchTeam;
        mDatabaseReference.child(VICTIMCOLOR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    String victimColor = dataSnapshot.getValue().toString();
                    for (int i=0; i<gameBroad.getPlayers().size(); i++){
                        if (victimColor.equalsIgnoreCase(gameBroad.getPlayers().get(i).getColor())){
                            mCurrentVictim = gameBroad.getPlayers().get(i);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mMessageView.setText("Victim is " + mCurrentVictim);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSecondCount=5;
                        int nextMove = 0;
                        for (int i=0; i<colors.size(); i++){
                            if (mCurrentVictim.getColor().equalsIgnoreCase(colors.get(i))){
                                nextMove = i;
                            }
                        }
                        if (mMyPlayerID==getControlId()){
                            GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                            mDatabaseReference.child(GAMEDATA).setValue(gameData);
                            mDatabaseReference.child(TURN).setValue(nextMove);
                        }
                    }
                },DELAYEDSECONDSFORLONGMESSAGE * 1000);

            }
        },DELAYEDSECONDSFORMESSAGEVIE * 1000);

    }

    private void messageViewInformSelectedDeadCharacter(Room theCurrentRoom) {
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setEnabled(false);
        mMessageView.setText("Loading Information");
        mDatabaseReference.child(VICTIMCOLOR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    Log.i(TAG, "victim color: " + dataSnapshot.getValue());
                    String victimColor = dataSnapshot.getValue().toString();
                    for (int i=0; i<gameBroad.getPlayers().size(); i++){
                        if (victimColor.equalsIgnoreCase(gameBroad.getPlayers().get(i).getColor())){
                            mCurrentVictim = gameBroad.getPlayers().get(i);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseReference.child(DEATHCHARACTER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    mCurrentGameCharacterSelected  = dataSnapshot.getValue().toString();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseReference.child(VICTIMCOLOR).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot!=null){
                            String victimColor = dataSnapshot.getValue().toString();
                            for (int i=0; i<gameBroad.getPlayers().size(); i++){
                                if (victimColor.equalsIgnoreCase(gameBroad.getPlayers().get(i).getColor())){
                                    mCurrentVictim = gameBroad.getPlayers().get(i);
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabaseReference.child(DEATHCHARACTER).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot!=null){
                            mCurrentGameCharacterSelected  = dataSnapshot.getValue().toString();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                GameCharacter deathCharacter = gameBroad.matchGameCharacter(mCurrentVictim, mCurrentGameCharacterSelected);
                mCurrentVictim.removeCharacter(deathCharacter);
                theCurrentRoom.leave(deathCharacter);
                theCurrentRoom.setCurrentZombienumber(0);
                disableContinue();
                updateRoom(MainActivity.this);
                mMainActivityLayout.invalidate();
                mMessageView.setText(mCurrentVictim + " has lost his/her " + deathCharacter.getName() +
                        "\nZombies have their feast, and returned back to somewhere else to find their next target! ");
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateRoom(MainActivity.this);
                        mMainActivityLayout.invalidate();
                        if (theCurrentRoom.getRoomNum()==4){
                            mFourthCount=5;
                        } else {
                            mFourthCount=4;
                        }
                        mCountSetUp=0;
                        mSecondCount=0;
                        mThirdCount=0;
                        if (mMyPlayerID==getControlId()){
                            GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                            mDatabaseReference.child(GAMEDATA).setValue(gameData);
                            mDatabaseReference.child(TURN).setValue(-20);
                        }
                    }
                },DELAYEDSECONDSFORMESSAGEVIE * 1000);
            }
        },DELAYEDSECONDSFOROPTIONSCHOSEN*1000);
    }

    private void messageViewDisplayItemAfterEffect(Room fallenRoom) {
        Log.i(TAG, "Triggering after effect Items: " + mUsedItem);
        if (mUsedItem.size()>0){
            if (mSecondCount<mUsedItem.size()){
                disableContinue();
                mMessageView.setVisibility(View.VISIBLE);
                mMessageView.setEnabled(false);
                mMessageView.setText("Items has been used, Some Items will triggered after affect");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mUsedItem.get(mSecondCount).getItemNum()==5){
                            mMessageView.setText("The affect of hareware has expired, the blocked zombie has returned");
                            fallenRoom.zombieApproached();
                        } else if (mUsedItem.get(mSecondCount).getItemNum()==6){
                            mMessageView.setText("The affect of hidden has expired, the hidden character has returned");
                            fallenRoom.enter(gameBroad.matchGameCharacter(mPlayersUsedItem.get(mSecondCount),mUsedItem.get(mSecondCount).getAffectedGameCharacter().getName()));
                        } else if (mUsedItem.get(mSecondCount).getItemNum()==7){
                            mMessageView.setText("The affect of sprint has trigger, the left character has moved to the destination");
                            gameBroad.matchRoom(mUsedItem.get(mSecondCount).getAfteraffectedRoomNumber())
                                    .enter(gameBroad.matchGameCharacter(mPlayersUsedItem.get(mSecondCount), mUsedItem.get(mSecondCount).getAffectedGameCharacter().getName()));
                        }
                        updateRoom(MainActivity.this);
                        mMainActivityLayout.invalidate();
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRoom(MainActivity.this);
                                mMainActivityLayout.invalidate();
                                mSecondCount++;
                                if (mMyPlayerID==getControlId()){
                                    GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                    mDatabaseReference.child(TURN).setValue(-mSecondCount-1);
                                }
                            }
                        },DELAYEDSECONDSFORMESSAGEVIE*1000);
                    }
                },DELAYEDSECONDSFORMESSAGEVIE*1000);
            } else if (mSecondCount==mUsedItem.size()){
                mMessageView.setText("Items after effect calculation finished");
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMessageView.setVisibility(View.INVISIBLE);
                        updateRoom(MainActivity.this);
                        mMainActivityLayout.invalidate();
                        mFourthCount=5;
                        mCountSetUp=0;
                        mSecondCount=0;
                        mThirdCount=0;
                        if (mMyPlayerID==getControlId()){
                            GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                            mDatabaseReference.child(GAMEDATA).setValue(gameData);
                            mDatabaseReference.child(TURN).setValue(-20);
                        }

                    }
                },DELAYEDSECONDSFORMESSAGEVIE*1000);
            }
        } else{
            mMessageView.setText("No Item will trigger after effect");
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMessageView.setVisibility(View.INVISIBLE);
                    updateRoom(MainActivity.this);
                    mMainActivityLayout.invalidate();
                    mFourthCount=5;
                    mCountSetUp=0;
                    mSecondCount=0;
                    mThirdCount=0;
                    if (mMyPlayerID == getControlId()){
                        GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-40);
                    }
                }
            },DELAYEDSECONDSFORMESSAGEVIE*1000);
        }
    }

    private void messageViewInformPlayerHasNoCharacter() {
        Log.i (TAG, "inform player has no character ");
        HashSet<Playable> removedPlayers = new HashSet<>();
        removedPlayers.clear();
        for (int i=0; i<gameBroad.getPlayers().size();i++) {
            Playable player = gameBroad.getPlayers().get(i);
            if (player.remaingCharacter() == 0) {
                removedPlayers.add(player);
            }
        }
        List<Playable> removePlayersList = new ArrayList<>();
        removePlayersList.addAll(removedPlayers);
        mCurrentTeam = (ArrayList<Playable>) removePlayersList;
        if (mCurrentTeam.size()>0){
            if (mSecondCount<mCurrentTeam.size()){
                disableContinue();
                mMessageView.setVisibility(View.VISIBLE);
                mMessageView.setEnabled(false);
                mMessageView.setText(mCurrentTeam.get(mSecondCount) +  " lost all his/her characters and has been removed from the game board");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateRoom(MainActivity.this);
                        mMainActivityLayout.invalidate();
                        mSecondCount++;
                        if (mMyPlayerID==getControlId()){
                            GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                            mDatabaseReference.child(GAMEDATA).setValue(gameData);
                            mDatabaseReference.child(TURN).setValue(-mSecondCount);
                        }
                    }
                },DELAYEDSECONDSFORMESSAGEVIE*1000);
            } else if (mSecondCount==mCurrentTeam.size()){
                mMessageView.setText("Player Remove calculation finished");
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (Playable player: removedPlayers){
                            gameBroad.getPlayers().remove(player);
                        }
                        updateRoom(MainActivity.this);
                        mMainActivityLayout.invalidate();
                        mFourthCount=6;
                        mCountSetUp=0;
                        mSecondCount=0;
                        mThirdCount=0;
                        if (mMyPlayerID==getControlId()){
                            GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                            mDatabaseReference.child(GAMEDATA).setValue(gameData);
                            mDatabaseReference.child(TURN).setValue(-20);
                        }
                    }
                },DELAYEDSECONDSFORMESSAGEVIE*1000);
            }
        } else {
            mMessageView.setText("No Player need to be removed this turn");
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateRoom(MainActivity.this);
                    mMainActivityLayout.invalidate();
                    mFourthCount=6;
                    mCountSetUp=0;
                    mSecondCount=0;
                    mThirdCount=0;
                    if (mMyPlayerID==getControlId()){
                        GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-25);
                    }
                }
            },DELAYEDSECONDSFORMESSAGEVIE*1000);
        }


    }

    private void messageViewInformMovingToNextRoom() {
        Log.i(TAG, " Moving to next room");
        mFifthCount++;
        mFourthCount=2;
        mCountSetUp=0;
        mSecondCount=0;
        mThirdCount=0;
        mDatabaseReference.child(FIRSTPLAYERINDEX).setValue(null);
        mDatabaseReference.child(ISCHIEFELECTED).setValue(null);
        mDatabaseReference.child(INDEXS).setValue(null);
        mDatabaseReference.child(ROOMS).setValue(null);
        mDatabaseReference.child(PLAYERBOOLEANANSWERS).setValue(null);
        mPlayersUsedItem.clear();
        mUsedItem.clear();
        mCurrentTeam.clear();
        votes.clear();;
        mCurrentItemOptions.clear();
        playersIndex.clear();
        roomspicked.clear();
        mCurrentYesNo=false;
        mCurrentYesNoMain = false;
        if (mMyPlayerID==getControlId()){
            GameData gameData1 = new GameData(mCountPhase, mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
            mDatabaseReference.child(GAMEDATA).setValue(gameData1);
            mDatabaseReference.child(TURN).setValue(-50);
            mDatabaseReference.child(PREVTURN).setValue(-1);
        }
    }

    private void messageViewInformMoveBacktoParkingOrWinnerDetermined() {
        disableContinue();
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setEnabled(false);
        if (mSixCount==0){
            mMessageView.setText("No Room has fallen this round, but more zombies are approaching");
        }else {
            mMessageView.setText("Congratulations to those survived from the zombies, you earned another round");
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.setText("A round is finished, game will move back to parking search, " +
                        "\nOnce there are four character in game, the player with most victory points won.");

                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (gameBroad.totalCharatersRemain()>4){
                            mCountPhase=3;
                        } else {
                            ++mCountPhase;
                        }
                        mUsedItem.clear();
                        mCountSetUp=0;
                        mSecondCount=0;
                        mThirdCount=0;
                        mFourthCount=0;
                        mFifthCount=0;
                        mSixCount=0;
                        mPlayersUsedItem.clear();;
                        mCurrentTeam.clear();
                        votes.clear();;
                        mCurrentItemOptions.clear();
                        playersIndex.clear();
                        roomspicked.clear();
                        mUsedItem.clear();
                        mCurrentYesNo=false;
                        mCurrentYesNoMain = false;
                        mCurrentMoreZombies.clear();
                        mDatabaseReference.child(FIRSTPLAYERINDEX).setValue(null);
                        mDatabaseReference.child(ISCHIEFELECTED).setValue(null);
                        mDatabaseReference.child(INDEXS).setValue(null);
                        mDatabaseReference.child(ROOMS).setValue(null);
                        mDatabaseReference.child(PLAYERBOOLEANANSWERS).setValue(null);
                        mDatabaseReference.child(ZOMBIEROOMS).setValue(null);
                       if (mMyPlayerID==getControlId()){
                           GameData gameData = new GameData(mCountPhase, mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                           mDatabaseReference.child(GAMEDATA).setValue(gameData);
                           mDatabaseReference.child(TURN).setValue(-30);
                           mDatabaseReference.child(PREVTURN).setValue(-1);
                       }
                    }
                }, DELAYEDSECONDSFORLONGMESSAGE * 1000);


            }
        },DELAYEDSECONDSFORMESSAGEVIE*1000);
    }

    private void rotateTurnAccoridngtoFirebase(int turn) {
        mPlayerButtons.get(turn).setVisibility(View.VISIBLE);
        mDatabaseReference.child(PREVTURN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null ){
                    int prevTurn = dataSnapshot.getValue(Integer.TYPE);
                    if (turn!=prevTurn && prevTurn >= 0){
                        mPlayerButtons.get(prevTurn).setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseReference.child(GAMEDATA).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    GameData gameData = dataSnapshot.getValue(GameData.class);
                    if (mMyPlayerID==turn){
                        Log.i(TAG, "My Turn: " +  gameData.toString());
                        mDatabaseReference.child(PREVTURN).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue()!=null){
                                    updateDataFromFireBase(turn, gameData,dataSnapshot.getValue(Integer.TYPE));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        updateRoom(MainActivity.this);
                        mMainActivityLayout.invalidate();
                        disableContinue();
                        mIsDataPushed = false;
                        mMessageView.setEnabled(true);
                        mMessageView.setVisibility(View.VISIBLE);
                        mMessageView.setText("Your Turn");
                        mMessageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMessageView.setVisibility(View.INVISIBLE);
                                mMessageView.setEnabled(false);
                                enableContinue();
                            }
                        });
                        mCountPhase=gameData.getmCountPhase();
                        mCountSetUp=gameData.getmCountSetUp();
                        mSecondCount=gameData.getmSecondCount();
                        mThirdCount=gameData.getmThirdCount();
                        mFourthCount=gameData.getmFourthCount();
                        mFifthCount=gameData.getmFifthCount();
                        mSixCount=gameData.getmSixCount();
                    }else {
                        mDatabaseReference.child(PREVTURN).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue()!=null){
                                    mMessageView.setVisibility(View.VISIBLE);
                                    int prevTurn = dataSnapshot.getValue(Integer.TYPE);
                                    if (prevTurn!=mMyPlayerID){
                                        updateDataFromFireBase(turn, gameData, prevTurn);
                                    }
                                    String whoseTurn = "Player " +  colors.get(turn) + " 's turn, please wait";
                                    mMessageView.setText(whoseTurn);
                                    mMessageView.setEnabled(false);
                                    disableContinue();
                                    updateRoom(MainActivity.this);
                                    mMainActivityLayout.invalidate();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateDataFromFireBase(int turn, GameData gameData, int prevTurn) {
        if (gameData.getmPassingType()==1){
            int q = turn==0? mPlayerNumber-1: turn-1;
            int selectedRoom = gameData.getmSelectedRoom();
            String selectedCharacter = gameData.getmSelectedCharacter();
            if (gameBroad.matchRoom(selectedRoom).isFull()) {
                gameBroad.matchRoom(4).enter(gameBroad.getPlayers().get(q).selectchoose(selectedCharacter));
                gameBroad.getPlayers().get(q).selectchooseremove(selectedCharacter);
            } else {
                gameBroad.matchRoom(selectedRoom).enter(gameBroad.getPlayers().get(q).selectchoose(selectedCharacter));
                gameBroad.getPlayers().get(q).selectchooseremove(selectedCharacter);
            }
        } else if (gameData.getmPassingType()==2){
            String whoVote = gameData.getWhoVoteColor();
            String voteWhom = gameData.getVoteWhomColor();
            votes.add(whoVote);
            votes.add(voteWhom);
        } else if (gameData.getmPassingType()==3){
            boolean isThreatUsed = gameData.getmUsedItemThreat();
            int affectedRoomNumber = gameData.getmAffectedRoom();
            if (isThreatUsed){
                String color = colors.get(prevTurn);
                String effectedColor = "";
                for (int k = 0; k < votes.size(); k += 2) {
                    if (color.equalsIgnoreCase(votes.get(k))) {
                        effectedColor = votes.get(k + 1);
                    }
                }
                effectedColor = effectedColor.toUpperCase();
                gameBroad.matchRoom(affectedRoomNumber).voteResultAfterItem(effectedColor, 1);
            }
        } else if (gameData.getmPassingType()==4){
            if (mMyPlayerID==turn){
                int itemNumber = gameData.getItemNumber();
                for (int i=0; i<items.size(); i++){
                    if (itemNumber==items.get(i).getItemNum()){
                        mCurrentSelectedItem = items.get(i);
                    }
                }
                mCurrentVoteColor = colors.get(turn);
            }
        } else if (gameData.getmPassingType()==5){
            int choosedRoomNumber = gameData.getSelectedRoomPhaseFive();
            int playerIndex = gameData.getPlayerIndex();
            roomspicked.add(choosedRoomNumber);
            playersIndex.add(playerIndex);
        } else if (gameData.getmPassingType()==6){
            Room destination = gameBroad.matchRoom(gameData.getSelectedRoomPhaseFive());
            Playable actualPlayer = gameBroad.getPlayers().get(gameData.getPlayerIndex());
            GameCharacter selectedCharacter2 = gameBroad.matchGameCharacter(actualPlayer,gameData.getmSelectedCharacter());
            Room leavingRoom2 = gameBroad.inWhichRoom(selectedCharacter2);
            leavingRoom2.leave(selectedCharacter2);
            if (gameBroad.matchRoom(gameData.getSelectedRoomPhaseFive()).isFull()){
                gameBroad.matchRoom(4).enter(selectedCharacter2);
            }else {
                gameBroad.matchRoom(gameData.getSelectedRoomPhaseFive()).enter(selectedCharacter2);
            }
        }
        updateRoom(MainActivity.this);
        mMainActivityLayout.invalidate();
    }

    private void continueButtonMethod() {
        mContinueButton = findViewById(R.id.continue_button1);
        mContinueButton.setVisibility(View.INVISIBLE);
        mContinueButton.setEnabled(false);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRoom(MainActivity.this);
                System.out.println("Phase: " + mCountPhase);
                if (mCountPhase==1) {
                    gameSetUpPickRooms();
                }
                if (mCountPhase==2) {
                    gameSetUpGetItem();
                }
                if (mCountPhase==3) {
                    searchParking();
                }
                if (mCountPhase==4) {
                    electChief();
                }
                if (mCountPhase==5){
                    viewAndMove();
                }
                if (mCountPhase==6){
                    showZombie();
                }
                if (mCountPhase==7){
                    showWinner();
                }
            }
        });
    }

    private void enableContinue() {
        mContinueButton.setVisibility(View.VISIBLE);
        mContinueButton.setEnabled(true);
        mOKShadow.setVisibility(View.VISIBLE);
        mOKShadow.startAnimation(mFlash);
    }

    private void disableContinue() {
        mMessageView.setVisibility(View.VISIBLE);
        mContinueButton.setVisibility(View.INVISIBLE);
        mContinueButton.setEnabled(false);
        mOKShadow.animate().cancel();
        mOKShadow.clearAnimation();
        mOKShadow.setAnimation(null);
        mOKShadow.setVisibility(View.INVISIBLE);
    }

    private void otherCommonSetUp() {

        mMainActivityLayout = findViewById(R.id.main_activity);

        mLoading = findViewById(R.id.loading_main);

        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

        mLoading.startAnimation(animRotate);

        colors.clear();
        colors.add("Red");
        colors.add("Yellow");
        colors.add("Blue");
        colors.add("Green");
        colors.add("Brown");
        colors.add("Black");

        items.clear();
        items.add(new Threat());
        items.add(new SecurityCamera());
        items.add(new Axe());
        items.add(new ShotGun());
        items.add(new Hardware());
        items.add(new Hidden());
        items.add(new Sprint());

        mRedButton = findViewById(R.id.red_button);
        mYellowButton = findViewById(R.id.yellow_button);
        mBlueButton = findViewById(R.id.blue_button);
        mGreenButton = findViewById(R.id.green_button);
        mBrownButton = findViewById(R.id.brown_button);
        mBlackButton = findViewById(R.id.black_button);
        mPlayerButtons.clear();
        mPlayerButtons.add(mRedButton);
        mPlayerButtons.add(mYellowButton);
        mPlayerButtons.add(mBlueButton);
        mPlayerButtons.add(mGreenButton);
        mPlayerButtons.add(mBrownButton);
        mPlayerButtons.add(mBlackButton);

        mFlash.setDuration(500);
        mFlash.setInterpolator(new LinearInterpolator());
        mFlash.setRepeatCount(Animation.INFINITE);
        mFlash.setRepeatMode(Animation.REVERSE);

        mYesShadow = findViewById(R.id.shadow_yesmain);
        mNoShadow = findViewById(R.id.shadow_nomain);
        mOKShadow = findViewById(R.id.shadow_okmain);
        mOKShadow.startAnimation(mFlash);

        mYesButton = findViewById(R.id.yesbutton_main);
//        mYesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCurrentYesNoMain = true;
//                mSecondCount++;
//                disableYesNo();
//                enableContinue();
//            }
//        });
        mNoButton = findViewById(R.id.nobutton_main);
//        mNoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCurrentYesNoMain = false;
//                mSecondCount++;
//                disableYesNo();
//                enableContinue();
//            }
//        });
        disableYesNo();

        for (ImageButton button: mPlayerButtons){
            button.setVisibility(View.INVISIBLE);
            button.setEnabled(false);
        }

//        for (int i=0; i<mPlayerNumber; i++){
//            mPlayerButtons.get(i).setVisibility(View.VISIBLE);
//        }
    }

    private void disableYesNo() {
        mYesButton.setVisibility(View.INVISIBLE);
        mNoButton.setVisibility(View.INVISIBLE);
        mYesButton.setEnabled(false);
        mNoButton.setEnabled(false);
        mYesShadow.animate().cancel();
        mNoShadow.animate().cancel();
        mYesShadow.clearAnimation();
        mYesShadow.setAnimation(null);
        mNoShadow.clearAnimation();
        mNoShadow.setAnimation(null);
        mYesShadow.setVisibility(View.INVISIBLE);
        mNoShadow.setVisibility(View.INVISIBLE);

    }

    private void enableYesNo() {
        mYesButton.setVisibility(View.VISIBLE);
        mNoButton.setVisibility(View.VISIBLE);
        mYesButton.setEnabled(true);
        mNoButton.setEnabled(true);
        mYesShadow.setVisibility(View.VISIBLE);
        mYesShadow.startAnimation(mFlash);
        mNoShadow.setVisibility(View.VISIBLE);
        mNoShadow.startAnimation(mFlash);
    }

    private void updateRoom(Context context) {
        ImageButton redGunMan = new ImageButton(context);
        redGunMan.setImageResource(R.drawable.redgunmanbttn);
        ImageButton redToughGuy = new ImageButton(context);
        redToughGuy.setImageResource(R.drawable.redtoughguybttn);
        ImageButton redModel = new ImageButton(context);
        redModel.setImageResource(R.drawable.redmodelbttn);

        ImageButton yellowGunMan = new ImageButton(context);
        yellowGunMan.setImageResource(R.drawable.yellowgunmanbttn);
        ImageButton yellowToughGuy = new ImageButton(context);
        yellowToughGuy.setImageResource(R.drawable.yellowtoughguybttn);
        ImageButton yellowModel = new ImageButton(context);
        yellowModel.setImageResource(R.drawable.yellowmodelbttn);

        ImageButton blueGunMan = new ImageButton(context);
        blueGunMan.setImageResource(R.drawable.bluegunmanbttn);
        ImageButton blueToughGuy = new ImageButton(context);
        blueToughGuy.setImageResource(R.drawable.bluetoughguybttn);
        ImageButton blueModel = new ImageButton(context);
        blueModel.setImageResource(R.drawable.bluemodelbttn);

        ImageButton greenGunMan = new ImageButton(context);
        greenGunMan.setImageResource(R.drawable.greengunmanbttn);
        ImageButton greenToughGuy = new ImageButton(context);
        greenToughGuy.setImageResource(R.drawable.greentoughguybttn);
        ImageButton greenModel = new ImageButton(context);
        greenModel.setImageResource(R.drawable.greenmodelbttn);

        ImageButton brownGunMan = new ImageButton(context);
        brownGunMan.setImageResource(R.drawable.browngunmanbttn);
        ImageButton brownToughGuy = new ImageButton(context);
        brownToughGuy.setImageResource(R.drawable.browntoughguybttn);
        ImageButton brownModel = new ImageButton(context);
        brownModel.setImageResource(R.drawable.brownmodelbttn);

        ImageButton blackGunMan = new ImageButton(context);
        blackGunMan.setImageResource(R.drawable.blackgunmanbttn);
        ImageButton blackToughGuy = new ImageButton(context);
        blackToughGuy.setImageResource(R.drawable.blacktoughguybttn);
        ImageButton blackModel = new ImageButton(context);
        blackModel.setImageResource(R.drawable.blackmodelbttn);

        List<ImageButton> allGameCharacterButtons = new ArrayList<>();
        allGameCharacterButtons.add(redGunMan);
        allGameCharacterButtons.add(redToughGuy);
        allGameCharacterButtons.add(redModel);
        allGameCharacterButtons.add(yellowGunMan);
        allGameCharacterButtons.add(yellowToughGuy);
        allGameCharacterButtons.add(yellowModel);
        allGameCharacterButtons.add(blueGunMan);
        allGameCharacterButtons.add(blueToughGuy);
        allGameCharacterButtons.add(blueModel);
        allGameCharacterButtons.add(greenGunMan);
        allGameCharacterButtons.add(greenToughGuy);
        allGameCharacterButtons.add(greenModel);
        allGameCharacterButtons.add(brownGunMan);
        allGameCharacterButtons.add(brownToughGuy);
        allGameCharacterButtons.add(brownModel);
        allGameCharacterButtons.add(blackGunMan);
        allGameCharacterButtons.add(blackToughGuy);
        allGameCharacterButtons.add(blackModel);

        for (ImageButton button: allGameCharacterButtons){
            button.setBackgroundResource(R.drawable.round_button);
            button.setScaleType(ImageView.ScaleType.FIT_XY);
            button.setPadding(0,0,0,0);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(100,100);
            button.setLayoutParams(params);
        }

        List<Room> mRooms = gameBroad.getRooms();

        mRestRoomZombie = findViewById(R.id.restroom_zombie_main);
        mCachouZombie = findViewById(R.id.cachou_zombie_main);
        mMegatoyZombie = findViewById(R.id.megatoy_zombie_main);
        mParkingZombie = findViewById(R.id.parking_zombie_main);
        mSecurityZombie = findViewById(R.id.security_zombie_main);
        mSupermarketZombie = findViewById(R.id.supermarket_zombie_main);

        List<TextView> zombies = new ArrayList<>();
        zombies.add(mRestRoomZombie);
        zombies.add(mCachouZombie);
        zombies.add(mMegatoyZombie);
        zombies.add(mParkingZombie);
        zombies.add(mSecurityZombie);
        zombies.add(mSupermarketZombie);

        for (int i=0; i<mRooms.size(); i++){
            zombies.get(i).setText(mRooms.get(i).getCurrentZombienumber() + "");
        }

        mRestRoomArea = findViewById(R.id.restroom_area_main);
        mCachouArea = findViewById(R.id.cachou_area_main);
        mMegatoyArea = findViewById(R.id.megatoy_area_main);
        mParkingArea = findViewById(R.id.parking_area_main);
        mSecurityArea = findViewById(R.id.security_area_main);
        mSupermarketArea = findViewById(R.id.supermarket_area_main);

        List<GridLayout> roomHasList = new ArrayList<>();
        roomHasList.add(mRestRoomArea);
        roomHasList.add(mCachouArea);
        roomHasList.add(mMegatoyArea);
        roomHasList.add(mParkingArea);
        roomHasList.add(mSecurityArea);
        roomHasList.add(mSupermarketArea);

        for (GridLayout gridLayout: roomHasList){
            gridLayout.removeAllViews();
        }


        for (ImageButton imageButton: allGameCharacterButtons){
            if (imageButton!=null && ((ViewGroup) imageButton.getParent())!=null)
            ((ViewGroup) imageButton.getParent()).removeView(imageButton);
        }




        for (int i=0; i<mRooms.size(); i++ ) {
            List<ImageButton> roomLabel = new ArrayList<>();
            for (int q = 0; q < mRooms.get(i).getRoomCharaters().size(); q++) {
                GameCharacter currentCharacter = mRooms.get(i).getRoomCharaters().get(q);
                String characterName = currentCharacter.getName();
                String characterColor = currentCharacter.getOwnercolor();
                if (characterName.equalsIgnoreCase("Gun Man") && characterColor.equalsIgnoreCase("Red")) {
                    roomLabel.add(redGunMan);
                } else if (characterName.equalsIgnoreCase("Tough Guy") && characterColor.equalsIgnoreCase("Red")) {
                    roomLabel.add(redToughGuy);
                } else if (characterName.equalsIgnoreCase("Model") && characterColor.equalsIgnoreCase("Red")) {
                    roomLabel.add(redModel);
                } else if (characterName.equalsIgnoreCase("Gun Man") && characterColor.equalsIgnoreCase("Yellow")) {
                    roomLabel.add(yellowGunMan);
                } else if (characterName.equalsIgnoreCase("Tough Guy") && characterColor.equalsIgnoreCase("Yellow")) {
                    roomLabel.add(yellowToughGuy);
                } else if (characterName.equalsIgnoreCase("Model") && characterColor.equalsIgnoreCase("Yellow")) {
                    roomLabel.add(yellowModel);
                } else if (characterName.equalsIgnoreCase("Gun Man") && characterColor.equalsIgnoreCase("Blue")) {
                    roomLabel.add(blueGunMan);
                } else if (characterName.equalsIgnoreCase("Tough Guy") && characterColor.equalsIgnoreCase("Blue")) {
                    roomLabel.add(blueToughGuy);
                } else if (characterName.equalsIgnoreCase("Model") && characterColor.equalsIgnoreCase("Blue")) {
                    roomLabel.add(blueModel);
                } else if (characterName.equalsIgnoreCase("Gun Man") && characterColor.equalsIgnoreCase("Green")) {
                    roomLabel.add(greenGunMan);
                } else if (characterName.equalsIgnoreCase("Tough Guy") && characterColor.equalsIgnoreCase("Green")) {
                    roomLabel.add(greenToughGuy);
                } else if (characterName.equalsIgnoreCase("Model") && characterColor.equalsIgnoreCase("Green")) {
                    roomLabel.add(greenModel);
                } else if (characterName.equalsIgnoreCase("Gun Man") && characterColor.equalsIgnoreCase("Brown")) {
                    roomLabel.add(brownGunMan);
                } else if (characterName.equalsIgnoreCase("Tough Guy") && characterColor.equalsIgnoreCase("Brown")) {
                    roomLabel.add(brownToughGuy);
                } else if (characterName.equalsIgnoreCase("Model") && characterColor.equalsIgnoreCase("Brown")) {
                    roomLabel.add(brownModel);
                } else if (characterName.equalsIgnoreCase("Gun Man") && characterColor.equalsIgnoreCase("Black")) {
                    roomLabel.add(blackGunMan);
                } else if (characterName.equalsIgnoreCase("Tough Guy") && characterColor.equalsIgnoreCase("Black")) {
                    roomLabel.add(blackToughGuy);
                } else if (characterName.equalsIgnoreCase("Model") && characterColor.equalsIgnoreCase("Black")) {
                    roomLabel.add(blackModel);
                }
            }
            GridLayout currentPane = roomHasList.get(i);
            for (int k=0; k<roomLabel.size(); k++){
                currentPane.setColumnCount(3);
                currentPane.addView(roomLabel.get(k));
            }
        }
    }

    private void gameSetUpPickRooms() {
        if (mCountSetUp % 3 == 0 && mCountSetUp < 3*mPlayerNumber*3){
            updateRoom(MainActivity.this);
            PairofDice pairofDice = new PairofDice();
            pairofDice.rollDieOne();
            pairofDice.rollDieTwo();
            final List<Integer> dices = new ArrayList<>();
            dices.add(pairofDice.getDieOneFace());
            dices.add(pairofDice.getDieTwoFace());
            int q = (mCountSetUp % (mPlayerNumber*3) == 0) ? 0: (mCountSetUp % (mPlayerNumber*3))/3 ;
            String playerColor = gameBroad.getPlayers().get(q).getColor();
            String message = gameBroad.getPlayers().get(q) + " get " + pairofDice.getDieOneFace() + " and "
                    + pairofDice.getDieTwoFace() + " please select your room";
            ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
            ArrayList<Item> items = (ArrayList<Item>) gameBroad.getPlayers().get(q).getCurrentItem();
            ArrayList options = (ArrayList<Integer>)dices;
            Intent intent = PlayerActivity.newChoosingRoomIntent(MainActivity.this,rooms,playerColor,items, options,message,mCountSetUp,1);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent,REQUEST_CODE_ROOM);
            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
        }
        int selectedRoom = mCurrentRoomPickedNumber;
        if (mCountSetUp % 3 == 1 && mCountSetUp < 3*mPlayerNumber*3){
            List<GameCharacter> characterOpitons = new ArrayList<>();
            int q = (mCountSetUp % (mPlayerNumber*3) - 1 ==0)? 0: (mCountSetUp % (mPlayerNumber*3))/3;
            for (int k = 0; k < gameBroad.getPlayers().get(q).getCharactersselect().size(); k++) {
                characterOpitons.add(gameBroad.getPlayers().get(q).getCharactersselect().get(k));
            }
            String playerColor = gameBroad.getPlayers().get(q).getColor();
            ArrayList characters = (ArrayList<GameCharacter>) characterOpitons;
            String message = gameBroad.getPlayers().get(q) + "please select one of these characters into " + ((gameBroad.matchRoom(selectedRoom).isFull())? "Parking":gameBroad.matchRoom(selectedRoom).getName());
            ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
            ArrayList<Item> items = (ArrayList<Item>) gameBroad.getPlayers().get(q).getCurrentItem();
            Intent intent = PlayerActivity.newChoosingCharacterIntent(MainActivity.this,rooms,playerColor,items, characters,message,mCountSetUp,2);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent,REQUEST_CODE_CHARACTER);
            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
        }
        String selectedCharacter = mCurrentGameCharacterSelected;
        if (mCountSetUp>0 && mCountSetUp % 3 == 2 && mCountSetUp < 3*mPlayerNumber*3) {
            int q = (mCountSetUp % (mPlayerNumber * 3) - 2 ==0) ? 0: (mCountSetUp % (mPlayerNumber*3))/3;
            if (gameBroad.matchRoom(selectedRoom).isFull()) {
                gameBroad.matchRoom(4).enter(gameBroad.getPlayers().get(q).selectchoose(selectedCharacter));
                gameBroad.getPlayers().get(q).selectchooseremove(selectedCharacter);
            } else {
                gameBroad.matchRoom(selectedRoom).enter(gameBroad.getPlayers().get(q).selectchoose(selectedCharacter));
                gameBroad.getPlayers().get(q).selectchooseremove(selectedCharacter);
            }
            ++mCountSetUp;
            GameData gameData = new GameData(mCountPhase, mCountSetUp, mSecondCount, mThirdCount, mFourthCount, mFifthCount, mSixCount,
                    selectedRoom, selectedCharacter);
            mDatabaseReference.child(GAMEDATA).setValue(gameData);
            mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
            if (mCountSetUp == 3*mPlayerNumber*3) {
                mDatabaseReference.child(TURN).setValue(-1);
            }else {
                mDatabaseReference.child(TURN).setValue((q==mPlayerNumber-1)?0:q+1);
            }
//            gameSetUpPickRooms();
        }
        if (mCountSetUp == 3*mPlayerNumber*3){
            disableContinue();
            mMessageView.setText("PRE-GAME SETTING PHASE TWO: GETTING STARTER ITEM");
            mMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCountPhase++;
                    mCountSetUp=0;
                    enableContinue();
                }
            });
        }
    }

    private void gameSetUpGetItem() {
        gameBroad.getItemDeck().shuffle();
        if (mCountSetUp < mPlayerNumber*2){
            int i = mCountSetUp /2;
            if (mCountSetUp%2==0){
                ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                String playercolor = gameBroad.getPlayers().get(i).getColor();
                Item starterItem = gameBroad.getItemDeck().deal();
                String message = gameBroad.getPlayers().get(i) +  " get " + starterItem.getName();
                gameBroad.getPlayers().get(i).getItem(starterItem);
                ArrayList<Item> items = (ArrayList<Item>) gameBroad.getPlayers().get(i).getCurrentItem();
                Intent intent = PlayerActivity.newMessageIntent(MainActivity.this,rooms,playercolor,items,message,mCountSetUp,3);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent,REQUEST_CODE_MESSAGE);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                gameBroad.getItemDeck().removeItem(starterItem);
                GameData gameData = new GameData(mCountPhase, mCountSetUp, mSecondCount, mThirdCount, mFourthCount, mFifthCount, mSixCount);
                mDatabaseReference.child(GAMEDATA).setValue(gameData);
            } else if (mCountSetUp%2==1) {
                mCountSetUp++;
                GameData gameData = new GameData(mCountPhase, mCountSetUp, mSecondCount, mThirdCount, mFourthCount, mFifthCount, mSixCount);
                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                if (mCountSetUp == mPlayerNumber*2) {
                    mDatabaseReference.child(TURN).setValue(-1);
                }else {
                    mDatabaseReference.child(TURN).setValue(i+1);
                }

            }

        } else if (mCountSetUp == mPlayerNumber*2){
            disableContinue();
            mMessageView.setText("Game Phase I: Parking Search");
            mMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCountPhase++;
                    mCountSetUp=0;
                    enableContinue();
                }
            });
        }
    }

    private void searchParking(){
        if (!mIsDataPushed){
            System.out.println("mCountSetup: " + mCountSetUp +  " mSecondCount: " + mSecondCount);
            if (mCountSetUp==0 && ( gameBroad.matchRoom(4).isEmpty() || gameBroad.getItemDeck().getItemsDeck().size() < 3)) {
                disableContinue();
                mMessageView.setEnabled(true);
                mMessageView.setText("Due to Parking is empty (or no more item avaiable), no searching will be performed");
                mMessageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMessageView.setVisibility(View.INVISIBLE);
                        mMessageView.setVisibility(View.VISIBLE);
                        mMessageView.setText("Game Phase II: Security Chief selected");
                        mMessageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enableContinue();
                                mCountPhase++;
                                mCountSetUp=0;
                                mSecondCount=0;
                            }
                        });

                    }
                });
            } else {
                if (mThirdCount==0){
                    HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(4).existCharacterColor());
                    List<Playable> searchTeam = new ArrayList<>();
                    for (Playable player : searchteam) {
                        searchTeam.add(player);
                    }
                    mCurrentTeam = (ArrayList<Playable>) searchTeam;
                    disableContinue();
                    mMessageView.setText(mCurrentTeam + " are in the parking");
                    mMessageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enableContinue();
                            mThirdCount++;
                        }
                    });
                }
                if (mThirdCount==1){
                    Log.i(TAG, mCurrentTeam + "");
                    if (mCountSetUp % 2 == 0 && (mCountSetUp < 2 * mCurrentTeam.size())){
                        int i = (mCountSetUp ==0)? 0: mCountSetUp/2;
                        Playable teammember = mCurrentTeam.get(i);
                        ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                        String playercolor = teammember.getColor();
                        String message = teammember +  "  please vote who can search";
                        ArrayList voteOptions =  mCurrentTeam;
                        ArrayList<Item> items = (ArrayList<Item>) mCurrentTeam.get(i).getCurrentItem();
                        System.out.println("Step I: calling vote" + " mCount: " + mCountSetUp + " i: " + i);
                        System.out.println(mCurrentTeam);
                        Intent intent = PlayerActivity.newVotingIntent(MainActivity.this,rooms,playercolor,items, voteOptions ,message,mCountSetUp,4);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent,REQUEST_CODE_VOTE);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                    }
                    String vote = mCurrentVoteColor;
                    if (mCountSetUp % 2 == 1 && (mCountSetUp < 2*mCurrentTeam.size())){
                        int i = (mCountSetUp ==0)? 0: mCountSetUp/2;
                        Playable teammember = mCurrentTeam.get(i);
                        votes.add(teammember.getColor());
                        votes.add(vote);
                        System.out.println("Step II: colleting vote" + " mCount: " + mCountSetUp + " i: " + i);
                        System.out.println(mCurrentTeam);
                        System.out.println(votes);
                        ++mCountSetUp;
                        GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount, mThirdCount, mFourthCount, mFifthCount,
                                mSixCount, teammember.getColor(), vote);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                        if (mCountSetUp==2*mCurrentTeam.size()) {
                            mDatabaseReference.child(TURN).setValue(-1);
                        } else {
                            int nextSearch = 0;
                            Playable nextSearchTeamMember = mCurrentTeam.get(i+1);
                            for (int q=0; q<colors.size(); q++){
                                if (nextSearchTeamMember.getColor().equalsIgnoreCase(colors.get(q))){
                                    nextSearch = q;
                                }
                            }
                            mDatabaseReference.child(TURN).setValue(nextSearch);
                        }
                        mIsDataPushed = true;
//                    if (mCountSetUp != 2*mCurrentTeam.size() )
//                    searchParking();
                    }
//                if (mCountSetUp == mCurrentTeam.size()*2  && mSecondCount == 0){
//                    gameBroad.matchRoom(4).resetVoteResult();
//                    gameBroad.matchRoom(4).voteResultAfterVote(votes);
//                    ArrayList votes1 = (ArrayList<String>) votes;
//                    System.out.println("Step III: show vote result");
//                    System.out.println(votes1);
//                    Intent intent2 = ShowVoteResultActivity.newVoteResultIntent(MainActivity.this, votes1,gameBroad.matchRoom(4).getCurrentVoteResult(),mSecondCount);
//                    startActivityForResult(intent2, REQUEST_CODE_VIEWRESULT);
//                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
//                }
                    if (mCountSetUp >= mCurrentTeam.size()*2 && mSecondCount > 0 && mCountSetUp < mCurrentTeam.size()*4) {
                        System.out.println("Step IV: using threat");
                        if (mSecondCount==1) {
                            disableContinue();
                            mMessageView.setText("Voting result can be changed by item THREAT, anyone want to change the result?");
                            mMessageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMessageView.setVisibility(View.INVISIBLE);
                                    mMessageView.setEnabled(false);
                                    enableYesNo();
                                }
                            });
                        }
                        if (mSecondCount==2) {
                            if (mCountSetUp % 2 == 0) {
                                int q = mCountSetUp - (2 * mCurrentTeam.size());
                                int i = (q == 0) ? 0 : (q / 2);
                                Playable teammember = mCurrentTeam.get(i);
                                String color = mCurrentTeam.get(i).getColor();
                                String message = teammember + " please confirm you want to use THREAT";
                                ArrayList rooms = (ArrayList<Room>) gameBroad.getRooms();
                                ArrayList<Item> items = (ArrayList<Item>) mCurrentTeam.get(i).getCurrentItem();
                                Boolean yesAndNo = false;
                                if (teammember.hasThreat()) {
                                    yesAndNo = true;
                                }
                                Intent intent = PlayerActivity.newYesNoIntent(MainActivity.this, rooms, color, items, yesAndNo, message, mCountSetUp, 5);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivityForResult(intent, REQUEST_CODE_YESNO);
                                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                            }
                            if (mCountSetUp % 2 == 1) {
                                int q = mCountSetUp - (2 * mCurrentTeam.size());
                                int i = (q == 0) ? 0 : (q / 2);
                                Playable teammember = mCurrentTeam.get(i);
                                if (mCurrentYesNo) {
                                    String effectedColor = "";
                                    for (int k = 0; k < votes.size(); k += 2) {
                                        if (teammember.getColor().equalsIgnoreCase(votes.get(k))) {
                                            effectedColor = votes.get(k + 1);
                                        }
                                    }
                                    effectedColor = effectedColor.toUpperCase();
                                    gameBroad.matchRoom(4).voteResultAfterItem(effectedColor, 1);
                                    Item threat = gameBroad.matchItem(teammember, "Threat");
                                    teammember.usedItem(threat);

                                }
                                mCountSetUp++;
                                GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount, mThirdCount, mFourthCount, mFifthCount, mSixCount,mCurrentYesNo,4);
                                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                                if (mCountSetUp==4*mCurrentTeam.size()) {
                                    mDatabaseReference.child(TURN).setValue(-1);
                                } else {
                                    int nextSearch = 0;
                                    for (int k=0; k<colors.size(); k++){
                                        if (mCurrentTeam.get(i+1).getColor().equalsIgnoreCase(colors.get(k))){
                                            nextSearch = k;
                                            break;
                                        }
                                    }
                                    mDatabaseReference.child(TURN).setValue(nextSearch);
                                }
                                mIsDataPushed = true;
//                                    if (mCountSetUp != mCurrentTeam.size()*4)
//                                    searchParking();
                            }
                        }
//                    } else {
//                        System.out.println("No threat");
//                        mCountSetUp = mCurrentTeam.size() * 4;
//                        mSecondCount = 2;
//                        System.out.println("mCountSetup: " + mCountSetUp +  " mSecond Count: " + mSecondCount);
//                        if (mCountSetUp != mCurrentTeam.size()*4)
//                        searchParking();
//                    }
                    }
//                if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 2 ) {
//                    System.out.println("Show summary");
//                    HashMap<String, Integer> results = gameBroad.matchRoom(4).getCurrentVoteResult();
//                    Intent intent = ShowSimpleVoteResultActivity.newVoteResultIntent(MainActivity.this,results,mSecondCount);
//                    startActivityForResult(intent,REQUEST_CODE_VIEWSIMPLERESULT);
//                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
//                }
                    if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 3) {
                        if (gameBroad.matchRoom(4).winner().equals("TIE")){
                            System.out.println("Result is Tie");
                            disableContinue();
                            mMessageView.setText("Result is TIE. " + " No Searching will be performed");
                            mMessageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    enableContinue();
                                    mCountSetUp = mCurrentTeam.size()*4+2;
                                }
                            });
                        } else {
                            System.out.println("Winner determined");
                            disableContinue();
                            String winnercolor = gameBroad.matchRoom(4).winner();
                            mMessageView.setText("Winner is " + gameBroad.matchPlayer(winnercolor) +
                                    "\nAnd would search items");
                            gameBroad.getItemDeck().shuffle();
                            Item item1 = gameBroad.getItemDeck().deal();
                            Item item2 = gameBroad.getItemDeck().deal();
                            Item item3 = gameBroad.getItemDeck().deal();
                            List<Item> itemtemplist = new ArrayList<>();
                            itemtemplist.add(item1);
                            itemtemplist.add(item2);
                            itemtemplist.add(item3);
                            mCurrentItemOptions = itemtemplist;
                            mMessageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    enableContinue();
                                    mSecondCount++;
                                }
                            });
                        }
                    }
                    if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount==4) {
                        System.out.println("Keep items");
                        String message = "Please select one item to keep";
                        Intent intent = ChoosingItemActivity.newChoosingItemIntent(MainActivity.this,mCurrentItemOptions, message, mSecondCount);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent,REQUEST_CODE_CHOOSINGITEM);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                    }
                    Item itemselect = mCurrentSelectedItem;
                    if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount==5) {
                        System.out.println("Giving Item");
                        String winnercolor = gameBroad.matchRoom(4).winner();
                        if (gameBroad.matchPlayer(winnercolor).getCurrentItem().size()<6) {
                            gameBroad.matchPlayer(winnercolor).getItem(itemselect);
                        }
                        System.out.println(mCurrentItemOptions);
                        String message = "Please select one item to give";
                        Intent intent = ChoosingItemActivity.newChoosingItemIntent(MainActivity.this, mCurrentItemOptions, message, mSecondCount);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent,REQUEST_CODE_CHOOSINGITEM);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                    }
                    Item itemgiveselect = mCurrentSelectedItem;
                    if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount==6) {
                        System.out.println("Select who to give");
                        String winnercolor = gameBroad.matchRoom(4).winner();
                        System.out.println(mCurrentItemOptions);
                        gameBroad.getItemDeck().addBackItem(mCurrentItemOptions.get(0));
                        HashSet<Playable> others = gameBroad.RemainPlayers(gameBroad.matchPlayer(winnercolor));
                        List<Playable> othersList = new ArrayList<>();
                        for (Playable other: others){
                            othersList.add(other);
                        }
                        Playable teammember = gameBroad.matchPlayer(winnercolor);
                        ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                        String playercolor = teammember.getColor();
                        String message = teammember +  "  please select who you want to give";
                        ArrayList voteOptions = (ArrayList<Playable>) othersList;
                        ArrayList<Item> items = (ArrayList<Item>) teammember.getCurrentItem();
                        Intent intent = PlayerActivity.newVotingIntent(MainActivity.this, rooms,playercolor,items,voteOptions,message,mCountSetUp,4);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent,REQUEST_CODE_VOTE);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                    }
                    String givecolor = mCurrentVoteColor;
                    if (mCountSetUp == mCurrentTeam.size()*4+1 && mSecondCount==6) {
                        mSecondCount++;
                        int turnValue = 0;
                        for (int i=0; i<colors.size(); i++){
                            if (givecolor.equalsIgnoreCase(colors.get(i))){
                                turnValue = i;
                                break;
                            }
                        }
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount,mCurrentSelectedItem.getItemNum());
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(turnValue);
                        mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                        mIsDataPushed = true;
                    }
                    if (mCountSetUp == mCurrentTeam.size()*4+1 && mSecondCount==7 ) {
                        System.out.println("Display giving message");
                        disableContinue();
                        String winnercolor = gameBroad.matchRoom(4).winner();
                        mMessageView.setEnabled(true);
                        mMessageView.setText("You have received an item from " + gameBroad.matchPlayer(winnercolor));
                        mMessageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMessageView.setVisibility(View.INVISIBLE);
                                mMessageView.setEnabled(false);
                                enableContinue();
                                mSecondCount++;
                            }
                        });
                    }
                    if (mCountSetUp == mCurrentTeam.size()*4+1 && mSecondCount==8) {
                        System.out.println("Player receiving confiming item");
                        Playable teammember = gameBroad.matchPlayer(givecolor);
                        ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                        String playercolor = teammember.getColor();
                        String message = "";
                        if (gameBroad.matchPlayer(givecolor).getCurrentItem().size()<6){
                            message = "You received " + itemgiveselect.getName();
                            teammember.getItem(itemgiveselect);
                        } else {
                            message = "You should have received " + itemgiveselect.getName() + ". Howver, due to your bag is full. You cannot carry more items.";
                        }
                        ArrayList<Item> items = (ArrayList<Item>) teammember.getCurrentItem();
                        Intent intent = PlayerActivity.newMessageIntent(MainActivity.this,rooms,playercolor,items,message,mCountSetUp,3);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent,REQUEST_CODE_MESSAGE);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                    }
                    if (mCountSetUp == mCurrentTeam.size()*4+2){
                        mCountSetUp++;
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(TURN).setValue(-1);
                        mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                        mIsDataPushed = true;
                    }
//                if (mCountSetUp == mCurrentTeam.size()*4+3) {
//                    System.out.println("Display message again");
//                    disableContinue();
//                    String winnercolor = gameBroad.matchRoom(4).winner();
//                    mMessageView.setText("Game Phase II: Security Chief selected");
//                    mMessageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                                    enableContinue();
//                                    mCountPhase++;
//                                    mCountSetUp=0;
//                                    mSecondCount=0;
//                                    mThirdCount=0;
//                                    votes.clear();
//                                    mCurrentTeam.clear();
//                                    mCurrentItemOptions.clear();
//                                    mCurrentZombiesRooms.clear();
//                                    mCurrentYesNo=false;
//                                    mCurrentYesNoMain = false;
//                        }
//                    });
//                }
                }
            }
        }
    }

    private void electChief() {
        if (!mIsDataPushed) {
            System.out.println("mCountSetup: " + mCountSetUp +  " mSecondCount: " + mSecondCount + " mThriedCount: " + mThirdCount);
            if (mCountSetUp==0 && gameBroad.matchRoom(5).isEmpty() && mSecondCount<3) {
                disableContinue();
                mMessageView.setText("Due to Security HQ is empty, no election will be performed");
                mMessageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enableContinue();
                        mCountSetUp = mCurrentTeam.size()*4;
                        mSecondCount = 3;
                    }
                });
            } else {
                if (mThirdCount==0 ){
                    HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(5).existCharacterColor());
                    List<Playable> searchTeam = new ArrayList<>();
                    for (Playable player : searchteam) {
                        searchTeam.add(player);
                    }
                    mCurrentTeam = (ArrayList<Playable>) searchTeam;
                    disableContinue();
                    mMessageView.setText(mCurrentTeam + " are in the SecurityHQ");
                    mMessageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enableContinue();
                            mThirdCount++;
                        }
                    });
                }
                if (mThirdCount==1) {
                    System.out.println(mCurrentTeam);
                    if (mCountSetUp % 2 == 0 && (mCountSetUp < 2 * mCurrentTeam.size())) {
                        int i = (mCountSetUp == 0) ? 0 : mCountSetUp / 2;
                        Playable teammember = mCurrentTeam.get(i);
                        ArrayList rooms = (ArrayList<Room>) gameBroad.getRooms();
                        String playercolor = teammember.getColor();
                        String message = teammember + "  please vote who is the chief";
                        ArrayList voteOptions = mCurrentTeam;
                        ArrayList<Item> items = (ArrayList<Item>) mCurrentTeam.get(i).getCurrentItem();
                        System.out.println("Step I: calling vote" + " mCount: " + mCountSetUp + " i: " + i);
                        System.out.println(mCurrentTeam);
                        Intent intent = PlayerActivity.newVotingIntent(MainActivity.this, rooms, playercolor, items, voteOptions, message, mCountSetUp, 4);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, REQUEST_CODE_VOTE);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                    }
                    String vote = mCurrentVoteColor;
                    if (mCountSetUp % 2 == 1 && (mCountSetUp < 2 * mCurrentTeam.size())) {
                        int i = (mCountSetUp == 0) ? 0 : mCountSetUp / 2;
                        Playable teammember = mCurrentTeam.get(i);
                        votes.add(teammember.getColor());
                        votes.add(vote);
                        System.out.println("Step II: colleting vote" + " mCount: " + mCountSetUp + " i: " + i);
                        System.out.println(mCurrentTeam);
                        System.out.println(votes);
                        ++mCountSetUp;
                        GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount, mThirdCount, mFourthCount, mFifthCount,
                                mSixCount, teammember.getColor(), vote);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                        if (mCountSetUp==2*mCurrentTeam.size()) {
                            mDatabaseReference.child(TURN).setValue(-1);
                        } else {
                            int nextSearch = 0;
                            Playable nextSearchTeamMember = mCurrentTeam.get(i+1);
                            for (int q=0; q<colors.size(); q++){
                                if (nextSearchTeamMember.getColor().equalsIgnoreCase(colors.get(q))){
                                    nextSearch = q;
                                }
                            }
                            mDatabaseReference.child(TURN).setValue(nextSearch);
                        };
                        mIsDataPushed = true;
                    }
                    if (mCountSetUp >= mCurrentTeam.size() * 2 && mSecondCount > 0 && mCountSetUp < mCurrentTeam.size() * 4) {
                        System.out.println("Step IV: using threat");
                        if (mSecondCount == 1) {
                            disableContinue();
                            mMessageView.setText("Voting result can be changed by item THREAT, anyone want to change the result?");
                            mMessageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMessageView.setVisibility(View.INVISIBLE);
                                    mMessageView.setEnabled(false);
                                    enableYesNo();
                                }
                            });
                        }
                        if (mSecondCount == 2) {
                            if (mCountSetUp % 2 == 0) {
                                int q = mCountSetUp - (2 * mCurrentTeam.size());
                                int i = (q == 0) ? 0 : (q / 2);
                                Playable teammember = mCurrentTeam.get(i);
                                String color = mCurrentTeam.get(i).getColor();
                                String message = teammember + " please confirm you want to use THREAT";
                                ArrayList rooms = (ArrayList<Room>) gameBroad.getRooms();
                                ArrayList<Item> items = (ArrayList<Item>) mCurrentTeam.get(i).getCurrentItem();
                                Boolean yesAndNo = false;
                                if (teammember.hasThreat()) {
                                    yesAndNo = true;
                                }
                                Intent intent = PlayerActivity.newYesNoIntent(MainActivity.this, rooms, color, items, yesAndNo, message, mCountSetUp, 5);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivityForResult(intent, REQUEST_CODE_YESNO);
                                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                            }
                            if (mCountSetUp % 2 == 1) {
                                int q = mCountSetUp - (2 * mCurrentTeam.size());
                                int i = (q == 0) ? 0 : (q / 2);
                                Playable teammember = mCurrentTeam.get(i);
                                if (mCurrentYesNo) {
                                    String effectedColor = "";
                                    for (int k = 0; k < votes.size(); k += 2) {
                                        if (teammember.getColor().equalsIgnoreCase(votes.get(k))) {
                                            effectedColor = votes.get(k + 1);
                                        }
                                    }
                                    effectedColor = effectedColor.toUpperCase();
                                    gameBroad.matchRoom(5).voteResultAfterItem(effectedColor, 1);
//                                    for (int q = 0; q < numThreat; q++) {
                                    Item threat = gameBroad.matchItem(teammember, "Threat");
                                    teammember.usedItem(threat);
                                }
                                mCountSetUp++;
                                GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount, mThirdCount, mFourthCount, mFifthCount, mSixCount,mCurrentYesNo,5);
                                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                                if (mCountSetUp==4*mCurrentTeam.size()) {
                                    mDatabaseReference.child(TURN).setValue(-1);
                                } else {
                                    int nextSearch = 0;
                                    for (int k=0; k<colors.size(); k++){
                                        if (mCurrentTeam.get(i+1).getColor().equalsIgnoreCase(colors.get(k))){
                                            nextSearch = k;
                                            break;
                                        }
                                    }
                                    mDatabaseReference.child(TURN).setValue(nextSearch);
                                }
                                mIsDataPushed = true;
                            }
                        }

                    }
                }
            }
//        if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 3) {
//                mCurrentYesNo=false;
//                mCurrentYesNoMain = false;
//                System.out.println("Preparing Zombies");
//                TwoPairofDice fourdices = new TwoPairofDice();
//                int DiceOne = fourdices.rollDieOne();
//                int DiceTwo = fourdices.rollDieTwo();
//                int DiceThree = fourdices.rollDieThree();
//                int DiceFour = fourdices.rollDieFour();
//                List<Integer> dices = new ArrayList<>();
//                dices.add(DiceOne);
//                dices.add(DiceTwo);
//                dices.add(DiceThree);
//                dices.add(DiceFour);
//                int startplayer = 0;
//                int startplayerroomnumber = 0;
//                mCurrentZombiesRooms = (ArrayList<Integer>) dices;
//                mCurrentStartPlayerIndex = startplayer;
//                mCurrentStartRoom = startplayerroomnumber;
//                if (gameBroad.matchRoom(5).winner().equals("TIE") || gameBroad.matchRoom(5).isEmpty() ){
//                    System.out.println("No chief");
//                    Random generator = new Random();
//                    startplayer = generator.nextInt(gameBroad.getPlayers().size());
//                    mCurrentStartPlayerIndex = startplayer;
//                    mCurrentStartPlayer = gameBroad.getPlayers().get(mCurrentStartPlayerIndex);
//                    disableContinue();
//                    mMessageView.setText("No chief selected. A ramdon player will start first");
//                    mMessageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            enableContinue();
//                            mSecondCount++;
//                            mIsChiefSelected = false;
//                        }
//                    });
//                } else {
//                    System.out.println("Chief selected");
//                    disableContinue();
//                    String winnercolor = gameBroad.matchRoom(5).winner();
//                    startplayer = gameBroad.getPlayers().indexOf(gameBroad.matchPlayer(winnercolor));
//                    mCurrentStartPlayerIndex = startplayer;
//                    mCurrentStartPlayer = gameBroad.getPlayers().get(mCurrentStartPlayerIndex);
//                    mMessageView.setText("Winner is " + gameBroad.matchPlayer(winnercolor) +
//                            "\nAnd would see the approaching zombies");
//                    mMessageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            enableContinue();
//                            mSecondCount++;
//                            mIsChiefSelected  = true;
//                        }
//                    });
//                }
//            }
            if (mCountSetUp < (mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 3) && mSecondCount==6) {
                int q = mCountSetUp - (4 * mCurrentTeam.size());
                int i = (q == 0) ? 0 : (q / 3);
                if (q % 3 == 0) {
                    Playable teammember = gameBroad.getPlayers().get(i);
                    String color = gameBroad.getPlayers().get(i).getColor();
                    String message = teammember + " please confirm you want to use Security";
                    ArrayList rooms = (ArrayList<Room>) gameBroad.getRooms();
                    ArrayList<Item> items = (ArrayList<Item>) gameBroad.getPlayers().get(i).getCurrentItem();
                    Boolean yesAndNo = false;
                    if (teammember.hasSecurityCamera()) {
                        yesAndNo = true;
                    }
                    Intent intent = PlayerActivity.newYesNoIntent(MainActivity.this, rooms, color, items, yesAndNo, message, mCountSetUp, 5);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, REQUEST_CODE_YESNO);
                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                }
                if (q % 3 == 1) {
                    Playable teammember = gameBroad.getPlayers().get(i);
                    if (mCurrentYesNo) {
                        mDatabaseReference.child(ZOMBIEROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue()!=null){
                                    mCurrentZombiesRooms.clear();
                                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        mCurrentZombiesRooms.add(snapshot.getValue(Integer.TYPE));
                                    }
                                    Intent intent = ShowingZombieActivity.newShowZombiesIntent(MainActivity.this, mCurrentZombiesRooms,mCountSetUp);
                                    startActivityForResult(intent, REQUEST_CODE_VIEWZOMBIE);
                                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter,android.support.v7.appcompat.R.anim.abc_popup_exit );
                                    Item securityCamera = gameBroad.matchItem(teammember, "SecurityCamera");
                                    teammember.usedItem(securityCamera);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else {
                        mCountSetUp+=2;
                        GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                        if (mCountSetUp == mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 3){
                            mDatabaseReference.child(TURN).setValue(-1);
                        } else {
                            int nextSearch = 0;
                            for (int k=0; k<colors.size(); k++){
                                if (gameBroad.getPlayers().get(i+1).getColor().equalsIgnoreCase(colors.get(k))){
                                    nextSearch = k;
                                    break;
                                }
                            }
                            mDatabaseReference.child(TURN).setValue(nextSearch);
                        }
                    }
                }
                if (q % 3==2){
                    mCountSetUp++;
                    GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                    if (mCountSetUp == mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 3){
                        mDatabaseReference.child(TURN).setValue(-1);
                    } else {
                        int nextSearch = 0;
                        for (int k=0; k<colors.size(); k++){
                            if (gameBroad.getPlayers().get(i+1).getColor().equalsIgnoreCase(colors.get(k))){
                                nextSearch = k;
                                break;
                            }
                        }
                        mDatabaseReference.child(TURN).setValue(nextSearch);
                    }
                    mIsDataPushed = true;
                }
            }
            if (mCountSetUp == mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 3) {
                disableContinue();
                mMessageView.setText("Game Phase III: Chief Viewing and Moving");
                mMessageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enableContinue();
                        mCountPhase++;
                        mCountSetUp=0;
                        mSecondCount=0;
                        mThirdCount=0;
                        votes.clear();
                        mCurrentTeam.clear();
                        mCurrentItemOptions.clear();
                        mCurrentYesNoMain = false;
                        mCurrentYesNo = false;
                    }
                });
            }
        }
    }

    private void viewAndMove() {
        mCurrentPlayerNumber = gameBroad.getPlayers().size();
        System.out.println("mCountSetup: " + mCountSetUp +  " mSecondCount: " + mSecondCount + " mThirdCount: " + mThirdCount + " mFourthCount: " + mFourthCount);
        if (mCountSetUp<2 && !mIsChiefSelected){
            if (mCountSetUp == 0) {
                System.out.println("No chief first player move");
                String playerColor = mCurrentStartPlayer.getColor();
                String message = mCurrentStartPlayer +  " please select your room to move to";
                ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                ArrayList<Item> items = (ArrayList<Item>) mCurrentStartPlayer.getCurrentItem();
                ArrayList options = (ArrayList) gameBroad.roomsOptions(mCurrentStartPlayer);
                Intent intent = PlayerActivity.newChoosingRoomIntent(MainActivity.this,rooms,playerColor,items, options,message,mCountSetUp,1);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent,REQUEST_CODE_ROOM);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                playersIndex.add(mMyPlayerID);
            }
            if (mCountSetUp==1) {
                System.out.println("Colleting first player room");
                roomspicked.add(mCurrentRoomPickedNumber);
                ++mCountSetUp;
                int q = mCurrentStartPlayerIndex + 1;
                int i = 0;
                if (q < mCurrentPlayerNumber) {
                    i = q;
                } else {
                    i = q - mCurrentPlayerNumber;
                }
                int nextMove = 0;
                for (int k=0; k<colors.size(); k++){
                    if (gameBroad.getPlayers().get(i).getColor().equalsIgnoreCase(colors.get(k))){
                        nextMove = k;
                    }
                }
                mIsDataPushed = true;
                GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                mDatabaseReference.child(INDEXS).push().setValue(mCurrentStartPlayerIndex);
                mDatabaseReference.child(ROOMS).push().setValue(mCurrentRoomPickedNumber);
                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                mDatabaseReference.child(TURN).setValue(nextMove);
            }
        }
        if (mCountSetUp<2 && mIsChiefSelected && mSecondCount<2){
            System.out.println("With chief, first player move");
            if (mSecondCount==0 && mCountSetUp==0){
                mDatabaseReference.child(ZOMBIEROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null){
                            mCurrentZombiesRooms.clear();
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                mCurrentZombiesRooms.add(snapshot.getValue(Integer.TYPE));
                            }
                        }
                        System.out.println("Showing Chief the zombies");
                        Intent intent = ShowingZombieActivity.newShowZombiesIntent(MainActivity.this, mCurrentZombiesRooms,mSecondCount);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, REQUEST_CODE_VIEWZOMBIECHIEF);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter,android.support.v7.appcompat.R.anim.abc_popup_exit );
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if (mSecondCount==1 && mCountSetUp==0){
                System.out.println("Chief Seleting Room");
                playersIndex.add(mCurrentStartPlayerIndex);
                String playerColor = mCurrentStartPlayer.getColor();
                String message = mCurrentStartPlayer +  " please select your room to move to";
                ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                ArrayList<Item> items = (ArrayList<Item>) mCurrentStartPlayer.getCurrentItem();
                ArrayList options = (ArrayList) gameBroad.roomsOptions(mCurrentStartPlayer);
                Intent intent = PlayerActivity.newChoosingRoomIntent(MainActivity.this,rooms,playerColor,items, options,message,mCountSetUp,1);
                startActivityForResult(intent,REQUEST_CODE_ROOM);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
            }
            if (mCountSetUp==1 && mSecondCount==1){
                mThirdCount++;
                roomspicked.add(mCurrentRoomPickedNumber);
                GameData gameData =  new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                mDatabaseReference.child(INDEXS).push().setValue(mCurrentStartPlayerIndex);
                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                mDatabaseReference.child(TURN).setValue(-5);
                mDatabaseReference.child(ROOMS).push().setValue(mCurrentRoomPickedNumber);
            }
        }
        String mycolor = colors.get(mMyPlayerID);
        String firstPlayerColor = gameBroad.getPlayers().get(mCurrentStartPlayerIndex).getColor();
        boolean isFirstPlayer = mycolor.equalsIgnoreCase(firstPlayerColor);
        if (mCountSetUp>=2 && mCountSetUp<mCurrentPlayerNumber*2 && !isFirstPlayer) {
            int q = mCurrentStartPlayerIndex + mCountSetUp / 2;
            int i = 0;
            if (q < mCurrentPlayerNumber) {
                i = q;
            } else {
                i = q - mCurrentPlayerNumber;
            }
            if (mCountSetUp % 2 == 0  && mCountSetUp<mCurrentPlayerNumber*2 ) {
                System.out.println("Selecting Room");
//                        playersIndex.add(i);
                String playerColor = gameBroad.getPlayers().get(i).getColor();
                String message = gameBroad.getPlayers().get(i) + " please select your room to move to";
                ArrayList rooms = (ArrayList<Room>) gameBroad.getRooms();
                ArrayList<Item> items = (ArrayList<Item>) gameBroad.getPlayers().get(i).getCurrentItem();
                List<Integer> options = gameBroad.roomsOptions(gameBroad.getPlayers().get(i));
                Intent intent = PlayerActivity.newChoosingRoomIntent(MainActivity.this, rooms, playerColor, items, (ArrayList<Integer>) options, message, mCountSetUp, 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, REQUEST_CODE_ROOM);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
            }
            if (mCountSetUp % 2 == 1 && mCountSetUp<mCurrentPlayerNumber*2) {
//                        roomspicked.add(mCurrentRoomPickedNumber);
                ++mCountSetUp;
                int z = 0;
                if (i+1 < mCurrentPlayerNumber){
                    z = i+1;
                } else {
                    z = i+1 - mCurrentPlayerNumber;
                }
                int nextMove = 0;
                for (int k=0; k<colors.size(); k++){
                    if (gameBroad.getPlayers().get(z).getColor().equalsIgnoreCase(colors.get(k))){
                        nextMove = k;
                    }
                }
                GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                mDatabaseReference.child(INDEXS).push().setValue(i);
                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                mDatabaseReference.child(ROOMS).push().setValue(mCurrentRoomPickedNumber);
                if (mCountSetUp==mCurrentPlayerNumber*2){
                    mDatabaseReference.child(TURN).setValue(-1);
                } else {
                    mDatabaseReference.child(TURN).setValue(nextMove);
                }
            }
        }
        if (mCountSetUp>=mCurrentPlayerNumber*2 && mCountSetUp<mCurrentPlayerNumber*4 && mFourthCount==1){
            if (mCountSetUp%2==0  && mCountSetUp<mCurrentPlayerNumber*4){
                System.out.println("Selecting Character");
                int q = ((mCountSetUp -mCurrentPlayerNumber*2)/2 == 0)? 0 : (mCountSetUp -mCurrentPlayerNumber*2)/2;
                Room destination = gameBroad.matchRoom(roomspicked.get(q));
                Playable actualPlayer = gameBroad.getPlayers().get(playersIndex.get(q));
                List<GameCharacter> characterOpitons = characterNotInTheRoom(destination, actualPlayer);
                String playerColor = actualPlayer.getColor();
                ArrayList characters = (ArrayList<GameCharacter>) characterOpitons;
                String message = actualPlayer + " please select one of these characters into " + (destination.isFull()? "Parking":destination.getName());
                ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                ArrayList<Item> items = (ArrayList<Item>) gameBroad.getPlayers().get(q).getCurrentItem();
                Intent intent = PlayerActivity.newChoosingCharacterIntent(MainActivity.this,rooms,playerColor,items, characters,message,mCountSetUp,2);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent,REQUEST_CODE_CHARACTER);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
            }
            if (mCountSetUp%2==1  && mCountSetUp<mCurrentPlayerNumber*4){
                System.out.println("Moving Actual Character into the room");
                int q = ((mCountSetUp -mCurrentPlayerNumber*2)/2 == 0)? 0 : (mCountSetUp -mCurrentPlayerNumber*2)/2;
                Room destination = gameBroad.matchRoom(roomspicked.get(q));
                Playable actualPlayer = gameBroad.getPlayers().get(playersIndex.get(q));
                GameCharacter selectedCharacter2 = gameBroad.matchGameCharacter(actualPlayer,mCurrentGameCharacterSelected);
                Room leavingRoom2 = gameBroad.inWhichRoom(selectedCharacter2);
                leavingRoom2.leave(selectedCharacter2);

                if (gameBroad.matchRoom(roomspicked.get(q)).isFull()){
                    gameBroad.matchRoom(4).enter(selectedCharacter2);
                }else {
                    gameBroad.matchRoom(roomspicked.get(q)).enter(selectedCharacter2);
                }
                ++mCountSetUp;

                if (mCountSetUp==mCurrentPlayerNumber*4){
                    mDatabaseReference.child(TURN).setValue(-1);
                } else {
                    Log.i(TAG, "q: " + q);
                    int next_index =  playersIndex.get(q+1);
                    int nextMove = 0;
                    for (int k=0; k<colors.size(); k++){
                        if (gameBroad.getPlayers().get(next_index).getColor().equalsIgnoreCase(colors.get(k))){
                            nextMove = k;
                        }
                    }
                    mDatabaseReference.child(TURN).setValue(nextMove);
                }
                GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount, playersIndex.get(q),roomspicked.get(q), mCurrentGameCharacterSelected);
                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
            }
        }
        if (mCountSetUp==mCurrentPlayerNumber*4){
            System.out.println("Display message again");
            disableContinue();
            mMessageView.setText("Game Phase IV: Zombies Revealed and Attacked");
            mMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableContinue();
                    mCountPhase++;
                    mCountSetUp=0;
                    mSecondCount=0;
                    mThirdCount=0;
                    votes.clear();
                    mCurrentTeam.clear();
                    mCurrentItemOptions.clear();
                    playersIndex.clear();
                    roomspicked.clear();
                    mCurrentYesNo=false;
                    mCurrentYesNoMain = false;
                }
            });
        }
    }

    private void showZombie() {
        System.out.println("mCountSetup: " + mCountSetUp +  " mSecondCount: " + mSecondCount + " mThirdCount: " + mThirdCount + " mFourthCount: "
                + mFourthCount + " mFifth Count: " + mFifthCount + " mSixth Count: " + mSixCount);
        if (mFifthCount<6 &&  mFourthCount>=2){
            final Room fallenRoom = gameBroad.matchRoom(mFifthCount+1);
            System.out.println(fallenRoom.getName());
            if (mFourthCount==2){
                if (mSecondCount==1 && mCountSetUp<originalTeamSize*2){
                    if (mCountSetUp%2==0){
                        System.out.println("using Item");
                        int i = mCountSetUp==0? 0: mCountSetUp/2;
                        String playerColor = mCurrentTeam.get(i).getColor();
                        String message =  mCurrentTeam.get(i) +  " please select your item (press no to choose nothing)";
                        ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                        ArrayList<Item> items = (ArrayList<Item>) mCurrentTeam.get(i).otherItemsList();
                        HashSet<GameCharacter> existedCharacters = fallenRoom.existChracterForThatPlayer(mCurrentTeam.get(i));
                        List<GameCharacter> existedCharactersList = new ArrayList<>();
                        for (GameCharacter character: existedCharacters){
                            existedCharactersList.add(character);
                        }
                        Intent intent = PlayerActivity.newChoosingItemIntent(MainActivity.this,rooms,playerColor,items,fallenRoom.getRoomNum(),
                                (ArrayList<GameCharacter>) existedCharactersList,  message,mCountSetUp,6);
                        startActivityForResult(intent,REQUEST_CODE_ITEM);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                    }
                    if (mCountSetUp%2==1){
                        int i = mCountSetUp/2;
                        if (mCurrentSelectedItem!=null){
                            Playable playable = mCurrentTeam.get(i);
                            Item usedItem = gameBroad.matchItem(playable, mCurrentSelectedItem.getName());
                            playable.usedItem(usedItem);
                        }
                        System.out.println("putting item into Firebase");
                        Log.i(TAG, "item: " + mCurrentSelectedItem);
                        int itemNumber =  mCurrentSelectedItem.getItemNum();
                        int afterRoomNumber = mCurrentSelectedItem.getAfteraffectedRoomNumber();
                        String characterName =  mCurrentSelectedItem.getAffectedGameCharacter().getName();
                        boolean isItemUsed =  mCurrentYesNo;
                        GameData gameData = new GameData(mCountPhase, mCountSetUp, mSecondCount, mThirdCount,mFourthCount,mFifthCount,mSixCount,
                                isItemUsed, itemNumber, characterName,afterRoomNumber,i);
                        mDatabaseReference.child(GAMEDATA).setValue(gameData);
                        mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                        mDatabaseReference.child(TURN).setValue(-1);
                    }
                }
            }
            if (mFourthCount==3){
                HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(fallenRoom.getRoomNum()).existCharacterColor());
                List<Playable> searchTeam = new ArrayList<>();
                for (Playable player : searchteam) {
                    searchTeam.add(player);
                }
                mCurrentTeam = (ArrayList<Playable>) searchTeam;
                Collections.sort(mCurrentTeam, new Comparator<Playable>() {
                    @Override
                    public int compare(Playable o1, Playable o2) {
                        return o1.getColor().compareTo(o2.getColor());
                    }
                });
                System.out.println(mCurrentTeam.size());
                if (mCountSetUp % 2 == 0 && (mCountSetUp < 2 * mCurrentTeam.size())) {
                    int i = (mCountSetUp == 0) ? 0 : mCountSetUp / 2;
                    Playable teammember = mCurrentTeam.get(i);
                    ArrayList rooms = (ArrayList<Room>) gameBroad.getRooms();
                    String playercolor = teammember.getColor();
                    String message = teammember + "  please vote whose character will be eaten";
                    ArrayList voteOptions = mCurrentTeam;
                    ArrayList<Item> items = (ArrayList<Item>) mCurrentTeam.get(i).getCurrentItem();
                    System.out.println("Step I: calling vote" + " mCount: " + mCountSetUp + " i: " + i);
                    System.out.println(mCurrentTeam);
                    Intent intent = PlayerActivity.newVotingIntent(MainActivity.this, rooms, playercolor, items, voteOptions, message, mCountSetUp, 4);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, REQUEST_CODE_VOTE);
                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter,android.support.v7.appcompat.R.anim.abc_popup_exit );
                }
                String vote = mCurrentVoteColor;
                if (mCountSetUp % 2 == 1 && (mCountSetUp < 2 * mCurrentTeam.size())) {
                    int i = (mCountSetUp == 0) ? 0 : mCountSetUp / 2;
                    Playable teammember = mCurrentTeam.get(i);
                    votes.add(teammember.getColor());
                    votes.add(vote);
                    System.out.println("Step II: colleting vote" + " mCount: " + mCountSetUp + " i: " + i);
                    System.out.println(mCurrentTeam);
                    System.out.println(votes);
                    ++mCountSetUp;
                    GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount, mThirdCount, mFourthCount, mFifthCount,
                            mSixCount, teammember.getColor(), vote);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                    if (mCountSetUp==2*mCurrentTeam.size()) {
                        mDatabaseReference.child(TURN).setValue(-1);
                    } else {
                        int nextSearch = 0;
                        Playable nextSearchTeamMember = mCurrentTeam.get(i + 1);
                        for (int q = 0; q < colors.size(); q++) {
                            if (nextSearchTeamMember.getColor().equalsIgnoreCase(colors.get(q))) {
                                nextSearch = q;
                            }
                        }
                        mDatabaseReference.child(TURN).setValue(nextSearch);
                    }
                }
                if (mCountSetUp >= mCurrentTeam.size() * 2 &&  mCountSetUp < mCurrentTeam.size() * 4) {
                    System.out.println("Step IV: using threat");
                        System.out.println("has threat");
                        if (mSecondCount == 1) {
                            disableContinue();
                            mMessageView.setText("Voting result can be changed by item THREAT, anyone want to change the result?");
                            mMessageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMessageView.setVisibility(View.INVISIBLE);
                                    mMessageView.setEnabled(false);
                                    enableYesNo();
                                }
                            });
                        }
                        if (mSecondCount == 2) {
                            if (mCountSetUp % 2 == 0) {
                                int q = mCountSetUp - (2 * mCurrentTeam.size());
                                int i = (q == 0) ? 0 : (q / 2);
                                Playable teammember = mCurrentTeam.get(i);
                                String color = mCurrentTeam.get(i).getColor();
                                String message = teammember + " please confirm you want to use THREAT";
                                ArrayList rooms = (ArrayList<Room>) gameBroad.getRooms();
                                ArrayList<Item> items = (ArrayList<Item>) mCurrentTeam.get(i).getCurrentItem();
                                Boolean yesAndNo = false;
                                if (teammember.hasThreat()) {
                                    yesAndNo = true;
                                }
                                Intent intent = PlayerActivity.newYesNoIntent(MainActivity.this, rooms, color, items, yesAndNo, message, mCountSetUp, 5);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivityForResult(intent, REQUEST_CODE_YESNO);
                                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                            }
                            if (mCountSetUp % 2 == 1) {
                                int q = mCountSetUp - (2 * mCurrentTeam.size());
                                int i = (q == 0) ? 0 : (q / 2);
                                Playable teammember = mCurrentTeam.get(i);
                                if (mCurrentYesNo) {
                                    String effectedColor = "";
                                    for (int k = 0; k < votes.size(); k += 2) {
                                        if (teammember.getColor().equalsIgnoreCase(votes.get(k))) {
                                            effectedColor = votes.get(k + 1);
                                        }
                                    }
                                    effectedColor = effectedColor.toUpperCase();
                                    fallenRoom.voteResultAfterItem(effectedColor, 1);
                                    Item threat = gameBroad.matchItem(teammember, "Threat");
                                    teammember.usedItem(threat);
                                }
                                mCountSetUp++;
                                GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount, mThirdCount, mFourthCount, mFifthCount,
                                        mSixCount,mCurrentYesNo,4);
                                mDatabaseReference.child(GAMEDATA).setValue(gameData);
                                mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                                if (mCountSetUp==4*mCurrentTeam.size()) {
                                    mDatabaseReference.child(TURN).setValue(-1);
                                } else {
                                    int nextSearch = 0;
                                    for (int k=0; k<colors.size(); k++){
                                        if (mCurrentTeam.get(i+1).getColor().equalsIgnoreCase(colors.get(k))){
                                            nextSearch = k;
                                            break;
                                        }
                                    }
                                    mDatabaseReference.child(TURN).setValue(nextSearch);
                                }
                            }
                        }
                    }
                if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 5){
                    HashSet<GameCharacter> availableOptionsSet = fallenRoom.existChracterForThatPlayer(mCurrentVictim);
                    List<GameCharacter> availableOptions = new ArrayList<GameCharacter>();
                    for (GameCharacter character : availableOptionsSet) {
                        availableOptions.add(character);
                    }
                    String playerColor = mCurrentVictim.getColor();
                    ArrayList characters = (ArrayList<GameCharacter>) availableOptions;
                    String message = mCurrentVictim + " please select the victim ";
                    ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                    ArrayList<Item> items = (ArrayList<Item>) mCurrentVictim.getCurrentItem();
                    Intent intent = PlayerActivity.newChoosingCharacterIntent(MainActivity.this,rooms,playerColor,items, characters,message,mCountSetUp,2);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent,REQUEST_CODE_CHARACTER);
                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                }
                if (mCountSetUp == mCurrentTeam.size()*4 +1){
                    GameData gameData = new GameData(mCountPhase,mCountSetUp, mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
                    mDatabaseReference.child(GAMEDATA).setValue(gameData);
                    mDatabaseReference.child(DEATHCHARACTER).setValue(mCurrentGameCharacterSelected);
                    mDatabaseReference.child(PREVTURN).setValue(mMyPlayerID);
                    mDatabaseReference.child(TURN).setValue(-5);
                }
            }
        if (mFourthCount>=5){
            HashSet<Playable> removedPlayers = new HashSet<>();
            removedPlayers.clear();
            for (int i=0; i<gameBroad.getPlayers().size();i++) {
                Playable player = gameBroad.getPlayers().get(i);
                if (player.remaingCharacter() == 0) {
                    removedPlayers.add(player);
                }
            }
            for (Playable player: removedPlayers){
                gameBroad.getPlayers().remove(player);
            }
            mFourthCount=2;
            mFifthCount++;
            mUsedItem.clear();
            showZombie();
        }
        }
        if (mFifthCount==6){
            if (mSixCount==0){
                disableContinue();
                mMessageView.setText("No Room has fallen this turn, but more zombies are approaching");
                mMessageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enableContinue();
                        mFifthCount++;
                    }
                });
            }else {
                mFifthCount++;
            }
        }
        if (mFifthCount==7){
            disableContinue();
            mMessageView.setText("A round is finished, game will move back to parking search, " +
                    "\nOnce there are four character in game, the player with most victory points won.");
            mMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableContinue();
                    if (gameBroad.totalCharatersRemain()>4){
                        mCountPhase=3;
                    } else {
                        ++mCountPhase;
                    }

                    mUsedItem.clear();
                    mCountSetUp=0;
                    mSecondCount=0;
                    mThirdCount=0;
                    mFourthCount=0;
                    mFifthCount=0;
                    mSixCount=0;
                    mPlayersUsedItem.clear();;
                    mCurrentTeam.clear();
                    votes.clear();;
                    mCurrentItemOptions.clear();
                    playersIndex.clear();
                    roomspicked.clear();
                    mCurrentYesNo=false;
                    mCurrentYesNoMain = false;
                    mCurrentMoreZombies.clear();

                }
            });

        }
    }

    private void showWinner() {
        int mostPoints = gameBroad.getPlayers().get(0).totalVictoryPoints();
        int q = 0;
        int count =0;
        for (int i=0; i<gameBroad.getPlayers().size();i++){
            if (mostPoints<gameBroad.getPlayers().get(i).totalVictoryPoints()){
                mostPoints = gameBroad.getPlayers().get(i).totalVictoryPoints();
                q = i;
            }
        }
        for (Playable player: gameBroad.getPlayers()){
            if(player.totalVictoryPoints() == mostPoints){
                count++;
            }
        }
        String message = "";
        if (count>1){
            message = "Result is TIE, ";
        } else {
            Playable winner  = gameBroad.getPlayers().get(q);
            message = "Congratulations! Winner is " + winner + " with a victory points: " + mostPoints;
        }
        disableContinue();
        mMessageView.setText("We have less than four player in the mall now. We will reveal who is the winner");
        final String finalMessage = message;
        mMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageView.setText(finalMessage);
            }
        });
    }

    public static List<GameCharacter> characterNotInTheRoom(Room room, Playable player) {
        List<GameCharacter> notInTheRoomList = new ArrayList<GameCharacter>();
        for (GameCharacter character : player.getGameCharacters()) {
            if (!room.inTheRoom(character)) {
                notInTheRoomList.add(character);
            }
        }
        return notInTheRoomList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_ROOM){
            if (data == null){
                return;
            }
            mCurrentRoomPickedNumber = PlayerActivity.choosedRoomNumber(data);
            mCountSetUp = PlayerActivity.getCountedSetUp(data);
        }
        if (requestCode == REQUEST_CODE_CHARACTER){
            if (data == null){
                return;
            }
            mCurrentGameCharacterSelected = PlayerActivity.choosedCharacter(data);
            mCountSetUp = PlayerActivity.getCountedSetUp(data);
        }
        if (requestCode == REQUEST_CODE_MESSAGE){
            if (data == null){
                return;
            }
            mCountSetUp = PlayerActivity.getCountedSetUp(data);
        }
        if (requestCode == REQUEST_CODE_VOTE){
            if (data == null){
                return;
            }
            mCountSetUp = PlayerActivity.getCountedSetUp(data);
            mCurrentVoteColor = PlayerActivity.votedColor(data);
        }
        if (requestCode == REQUEST_CODE_VIEWRESULT){
            if (data == null){
                return;
            }
            mSecondCount = ShowVoteResultActivity.getCountedSetUp(data);
            GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
            mDatabaseReference.child(GAMEDATA).setValue(gameData);
            mDatabaseReference.child(TURN).setValue(-10);
        }
        if (requestCode == REQUEST_CODE_YESNO){
            if (data == null){
                return;
            }
            mCurrentYesNo = PlayerActivity.choosedBoolean(data);
            mCountSetUp = PlayerActivity.getCountedSetUp(data);
        }
        if (requestCode == REQUEST_CODE_VIEWSIMPLERESULT){
            if (data == null){
                System.out.println("data null");
                return;
            }
            mSecondCount = ShowSimpleVoteResultActivity.getCountedSetUp(data);
            GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
            mDatabaseReference.child(GAMEDATA).setValue(gameData);
            mDatabaseReference.child(TURN).setValue(-10);
        }
        if (requestCode == REQUEST_CODE_CHOOSINGITEM){
            if (data == null){
                return;
            }
            mSecondCount = ChoosingItemActivity.getCountedSetUp(data);
            mCurrentSelectedItem = ChoosingItemActivity.choosedItem(data);
            mCurrentItemOptions = ChoosingItemActivity.getRemainedItem(data);
        }
        if (requestCode == REQUEST_CODE_VIEWZOMBIE){
            if (data == null){
                return;
            }
            mCountSetUp = ShowingZombieActivity.getCountedSetUp(data);
        }
        if (requestCode == REQUEST_CODE_VIEWZOMBIECHIEF){
            if (data == null){
                return;
            }
            mSecondCount = ShowingZombieActivity.getCountedSetUp(data);
        }
        if (requestCode == REQUEST_CODE_VIEWZOMBIEAll){
            if (data == null){
                return;
            }
            mFourthCount = ShowingZombieActivity.getCountedSetUp(data);
            GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
            mDatabaseReference.child(GAMEDATA).setValue(gameData);
            mDatabaseReference.child(TURN).setValue(-10);
        }
        if (requestCode == REQUEST_CODE_VIEWZOMBIEALLMORE){
            if (data == null){
                return;
            }
            mFourthCount = ShowMoreZombiesActivity.getCountedSetUp(data);
            GameData gameData = new GameData(mCountPhase,mCountSetUp,mSecondCount,mThirdCount,mFourthCount,mFifthCount,mSixCount);
            mDatabaseReference.child(GAMEDATA).setValue(gameData);
            mDatabaseReference.child(TURN).setValue(-15);
        }
        if (requestCode == REQUEST_CODE_ITEM){
            if (data == null){
                return;
            }
            mCurrentSelectedItem = PlayerActivity.choosedItem(data);
            mCountSetUp = PlayerActivity.getCountedSetUp(data);
            mCurrentYesNo = PlayerActivity.choosedBoolean(data);
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        delayedIdle(30);
    }

    Handler _idleHandler = new Handler();
    Runnable _idleRunnable = new Runnable() {
        @Override
        public void run() {
            if (mDatabaseGame!=null){
                Log.i(TAG, "set the data null due to inactivy");
                FirebaseDatabase.getInstance().getReference().child("game").child(mDatabaseGame.getRoomId()+"started").setValue(null);
            }
            //handle your IDLE state
        }
    };

    private void delayedIdle(int delayMinutes) {
        _idleHandler.removeCallbacks(_idleRunnable);
        _idleHandler.postDelayed(_idleRunnable, (delayMinutes * 1000 * 60));
    }

    FirebaseListAdapter<ChatMessage> adapter;

    public void open_chat(View view) {

        LayoutInflater inflater = getLayoutInflater();
        View chatLayout = inflater.inflate(R.layout.chat_layout, null);
        AlertDialog.Builder chatbox = new AlertDialog.Builder(this);


        FloatingActionButton fab = chatLayout.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)chatLayout.findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("chats")
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                mUserName)
                        );
                // Clear the input
                input.setText("");
            }
        });

        ListView listOfMessages = chatLayout.findViewById(R.id.list_of_messages);
        listOfMessages.setAdapter(adapter);

        chatbox.setView(chatLayout);
        AlertDialog dialog = chatbox.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    private void displayMessage() {
        Query query = FirebaseDatabase.getInstance().getReference().child("chats");
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.message)
                .build();
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };
    }

    private void messageHasUpdate(){
        FirebaseDatabase.getInstance().getReference().child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Toast.makeText(MainActivity.this, "You have new message", Toast.LENGTH_SHORT).show();
                }else{
                    return;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
