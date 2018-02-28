package com.bignerdranch.android.mallofhorrorandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.Game;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.GameCharacter;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Dice.PairofDice;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Dice.TwoPairofDice;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Item;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.Playable;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
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

    private DatabaseReference mDatabaseReference;
    private Game mDatabaseGame;
    private String mUserName;
    private int mMyPlayerID;
    private String mType;


    private final static GameBroad gameBroad = new GameBroad(0);
    private static int mCurrentRoomPickedNumber = 0;
    private static int mCountSetUp;
    private static int mSecondCount;
    private static boolean mCurrentYesNoMain = false;
    private static boolean mCurrentYesNo = false;
    private static String mCurrentGameCharacterSelected = "";
    private static String mCurrentVoteColor = "";
    private static Item mCurrentSelectedItem;
    private static int mCountPhase;
    private static Playable mCurrentVictim;
    final static List<String> colors = new ArrayList<>();
    final static List<String> actualcolors= new ArrayList<>();
    private ArrayList<Playable> mCurrentTeam = new ArrayList<>();


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

    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resuming ");
        updateRoom(MainActivity.this);
    }

    private void gettingReady() {
        mPlayerNumber = getIntent().getIntExtra(PLAYER_NUMBER,0);
        gameBroad.setPlayersNumber(mPlayerNumber);

        mDatabaseGame = getIntent().getParcelableExtra(DATABASEGAME);
        mUserName = getIntent().getStringExtra(USERNAME);
        mType = getIntent().getStringExtra(TYPE);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("game").child(mDatabaseGame.getRoomId()+"started");

        if (mDatabaseGame!=null && mCountPhase==0 && mType.equals("Host")){
            createRoomOnFireBase();
            registerMyPlayerId();
        } else if (mDatabaseGame!=null && mCountPhase==0 ){
            registerMyPlayerId();
        }

        ContinueButtonMethod();
        otherCommonSetUp();

        mMessageView = findViewById(R.id.messageView_main);
        mMessageView.setText("PRE-GAME SETTING PHASE ONE : CHOOSE ROOM");
        mMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child("mCount").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mDatabaseGame!=null&& mMyPlayerID!= dataSnapshot.getValue(Integer.TYPE)){
                            mMessageView.setVisibility(View.INVISIBLE);
                        }else {
                            enableContinue();
                            mCountPhase++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private void createRoomOnFireBase() {

        mDatabaseGame.setRoomId(mDatabaseGame.getRoomId()+"started");
        mDatabaseReference.setValue(mDatabaseGame);
        mDatabaseReference.child("mCount").setValue(0);
    }

    private void registerMyPlayerId() {
        List<String> userNames = new ArrayList<>();
        userNames.add(mDatabaseGame.getPlayer1());
        userNames.add(mDatabaseGame.getPlayer2());
        userNames.add(mDatabaseGame.getPlayer3());
        userNames.add(mDatabaseGame.getPlayer4());
        for (int i=0; i<userNames.size(); i++){
            if (mUserName.equals(userNames.get(i))){
                mMyPlayerID=i;

            }
        }
    }

    private void ContinueButtonMethod() {
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
        mMessageView.setEnabled(false);
        mMessageView.setVisibility(View.INVISIBLE);
        mContinueButton.setVisibility(View.VISIBLE);
        mContinueButton.setEnabled(true);
        mOKShadow.setVisibility(View.VISIBLE);
        mOKShadow.startAnimation(mFlash);
    }

    private void disableContinue() {
        mContinueButton.setEnabled(false);
        mContinueButton.setVisibility(View.INVISIBLE);
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setEnabled(true);
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

        colors.add("Red");
        colors.add("Yellow");
        colors.add("Blue");
        colors.add("Green");
        colors.add("Brown");
        colors.add("Black");

        mRedButton = findViewById(R.id.red_button);
        mYellowButton = findViewById(R.id.yellow_button);
        mBlueButton = findViewById(R.id.blue_button);
        mGreenButton = findViewById(R.id.green_button);
        mBrownButton = findViewById(R.id.brown_button);
        mBlackButton = findViewById(R.id.black_button);
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
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentYesNoMain = true;
                mSecondCount++;
                disableYesNo();
                enableContinue();
            }
        });
        mNoButton = findViewById(R.id.nobutton_main);
        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentYesNoMain = false;
                mSecondCount++;
                disableYesNo();
                enableContinue();
            }
        });
        disableYesNo();

        for (ImageButton button: mPlayerButtons){
            button.setVisibility(View.INVISIBLE);
            button.setEnabled(false);
        }

        for (int i=0; i<mPlayerNumber; i++){
            mPlayerButtons.get(i).setVisibility(View.VISIBLE);
        }
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
            gameSetUpPickRooms();
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
        if (mCountSetUp < mPlayerNumber){
            int i = mCountSetUp;
            ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
            String playercolor = gameBroad.getPlayers().get(i).getColor();
            Item starterItem = gameBroad.getItemDeck().deal();
            String message = gameBroad.getPlayers().get(i) +  " get " + starterItem;
            gameBroad.getPlayers().get(i).getItem(starterItem);
            ArrayList<Item> items = (ArrayList<Item>) gameBroad.getPlayers().get(i).getCurrentItem();
            Intent intent = PlayerActivity.newMessageIntent(MainActivity.this,rooms,playercolor,items,message,mCountSetUp,3);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent,REQUEST_CODE_MESSAGE);
            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
            gameBroad.getItemDeck().removeItem(starterItem);
        } else if (mCountSetUp == mPlayerNumber){
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
        System.out.println("mCountSetup: " + mCountSetUp +  " mSecondCount: " + mSecondCount);
        if (mCountSetUp==0 && ( gameBroad.matchRoom(4).isEmpty() || gameBroad.getItemDeck().getItemsDeck().size() < 3)) {
            disableContinue();
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
                System.out.println(mCurrentTeam);
                if (mCountSetUp % 2 == 0 && (mCountSetUp < 2 * mCurrentTeam.size())){
                    int i = (mCountSetUp ==0 )? 0: mCountSetUp/2;
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
                    int i = (mCountSetUp ==0 )? 0: mCountSetUp/2;
                    Playable teammember = mCurrentTeam.get(i);
                    votes.add(teammember.getColor());
                    votes.add(vote);
                    System.out.println("Step II: colleting vote" + " mCount: " + mCountSetUp + " i: " + i);
                    System.out.println(mCurrentTeam);
                    System.out.println(votes);
                    ++mCountSetUp;
                    if (mCountSetUp != 2*mCurrentTeam.size() )
                    searchParking();
                }
                if (mCountSetUp == mCurrentTeam.size()*2  && mSecondCount == 0){
                    gameBroad.matchRoom(4).resetVoteResult();
                    gameBroad.matchRoom(4).voteResultAfterVote(votes);
                    ArrayList votes1 = (ArrayList<String>) votes;
                    System.out.println("Step III: show vote result");
                    System.out.println(votes1);
                    Intent intent2 = ShowVoteResultActivity.newVoteResultIntent(MainActivity.this, votes1,gameBroad.matchRoom(4).getCurrentVoteResult(),mSecondCount);
                    startActivityForResult(intent2, REQUEST_CODE_VIEWRESULT);
                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                }
                if (mCountSetUp >= mCurrentTeam.size()*2 && mSecondCount > 0 && mCountSetUp < mCurrentTeam.size()*4) {
                    System.out.println("Step IV: using threat");
                    if (teamHasThreat(mCurrentTeam) || mCurrentYesNoMain ) {
                        System.out.println("has threat");
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
                            if (mCurrentYesNoMain) {
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
//                                    for (int q = 0; q < numThreat; q++) {
                                        Item threat = gameBroad.matchItem(teammember, "Threat");
                                        teammember.usedItem(threat);
                                    }
                                    mCountSetUp++;
                                    if (mCountSetUp != mCurrentTeam.size()*4)
                                    searchParking();
                                }
                            } else {
                                mCountSetUp += mCurrentTeam.size() * 2;
                            }
                        }
                    } else {
                        System.out.println("No threat");
                        mCountSetUp = mCurrentTeam.size() * 4;
                        mSecondCount = 2;
                        System.out.println("mCountSetup: " + mCountSetUp +  " mSecond Count: " + mSecondCount);
                        if (mCountSetUp != mCurrentTeam.size()*4)
                        searchParking();
                    }
                }
                if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 2 ) {
                    System.out.println("Show summary");
                    HashMap<String, Integer> results = gameBroad.matchRoom(4).getCurrentVoteResult();
                    Intent intent = ShowSimpleVoteResultActivity.newVoteResultIntent(MainActivity.this,results,mSecondCount);
                    startActivityForResult(intent,REQUEST_CODE_VIEWSIMPLERESULT);
                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                }
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
                if (mCountSetUp == mCurrentTeam.size()*4+1 && mSecondCount==6 ) {
                    System.out.println("Display giving message");
                    disableContinue();
                    String winnercolor = gameBroad.matchRoom(4).winner();
                    mMessageView.setText(gameBroad.matchPlayer(givecolor) + " you have received an item from " + gameBroad.matchPlayer(winnercolor)
                            + " (keep it to yourself)");
                    mMessageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enableContinue();
                            mSecondCount++;
                        }
                    });
                }
                if (mCountSetUp == mCurrentTeam.size()*4+1 && mSecondCount==7) {
                    System.out.println("Player receiving confiming item");
                    Playable teammember = gameBroad.matchPlayer(givecolor);
                    ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                    String playercolor = teammember.getColor();
                    String message = "";
                    if (gameBroad.matchPlayer(givecolor).getCurrentItem().size()<6){
                        message = "You received " + itemgiveselect;
                        teammember.getItem(itemgiveselect);
                    } else {
                        message = "You should have received " + itemgiveselect + ". Howver, due to your bag is full. You cannot carry more items.";
                    }
                    ArrayList<Item> items = (ArrayList<Item>) teammember.getCurrentItem();
                    Intent intent = PlayerActivity.newMessageIntent(MainActivity.this,rooms,playercolor,items,message,mCountSetUp,3);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent,REQUEST_CODE_MESSAGE);
                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                }
                if (mCountSetUp == mCurrentTeam.size()*4+2) {
                    System.out.println("Display message again");
                    disableContinue();
                    String winnercolor = gameBroad.matchRoom(4).winner();
                    mMessageView.setText("Game Phase II: Security Chief selected");
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
                                    mCurrentZombiesRooms.clear();
                                    mCurrentYesNo=false;
                                    mCurrentYesNoMain = false;
                        }
                    });
                }
            }
        }
    }

    private void electChief() {
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
                    if (mCountSetUp != mCurrentTeam.size() * 2 )
                    electChief();
                }
                if (mCountSetUp == mCurrentTeam.size() * 2 && mSecondCount == 0) {
                    gameBroad.matchRoom(5).resetVoteResult();
                    gameBroad.matchRoom(5).voteResultAfterVote(votes);
                    ArrayList votes1 = (ArrayList<String>) votes;
                    System.out.println("Step III: show vote result");
                    System.out.println(votes1);
                    Intent intent2 = ShowVoteResultActivity.newVoteResultIntent(MainActivity.this, votes1, gameBroad.matchRoom(5).getCurrentVoteResult(), mSecondCount);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent2, REQUEST_CODE_VIEWRESULT);
                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                }
                if (mCountSetUp >= mCurrentTeam.size() * 2 && mSecondCount > 0 && mCountSetUp < mCurrentTeam.size() * 4) {
                    System.out.println("Step IV: using threat");
                    if (teamHasThreat(mCurrentTeam) || mCurrentYesNoMain) {
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
                            if (mCurrentYesNoMain) {
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
                                    if (mCountSetUp != mCurrentTeam.size() * 4)
                                    electChief();
                                }
                            } else {
                                mCountSetUp += mCurrentTeam.size() * 2;
                            }
                        }
                    } else {
                        System.out.println("No threat");
                        mCountSetUp = mCurrentTeam.size() * 4;
                        mSecondCount = 2;
                        if (mCountSetUp != mCurrentTeam.size() * 4)
                        electChief();
                    }
                }
                if (mCountSetUp == mCurrentTeam.size() * 4 && mSecondCount == 2) {
                    System.out.println("Show summary");
                    HashMap<String, Integer> results = gameBroad.matchRoom(5).getCurrentVoteResult();
                    Intent intent = ShowSimpleVoteResultActivity.newVoteResultIntent(MainActivity.this, results, mSecondCount);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, REQUEST_CODE_VIEWSIMPLERESULT);
                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                }
            }
        }
        if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 3) {
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
                int startplayer = 0;
                int startplayerroomnumber = 0;
                mCurrentZombiesRooms = (ArrayList<Integer>) dices;
                mCurrentStartPlayerIndex = startplayer;
                mCurrentStartRoom = startplayerroomnumber;
                if (gameBroad.matchRoom(5).winner().equals("TIE") || gameBroad.matchRoom(5).isEmpty() ){
                    System.out.println("No chief");
                    Random generator = new Random();
                    startplayer = generator.nextInt(gameBroad.getPlayers().size());
                    mCurrentStartPlayerIndex = startplayer;
                    mCurrentStartPlayer = gameBroad.getPlayers().get(mCurrentStartPlayerIndex);
                    disableContinue();
                    mMessageView.setText("No chief selected. A ramdon player will start first");
                    mMessageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enableContinue();
                            mSecondCount++;
                            mIsChiefSelected = false;
                        }
                    });
                } else {
                    System.out.println("Chief selected");
                    disableContinue();
                    String winnercolor = gameBroad.matchRoom(5).winner();
                    startplayer = gameBroad.getPlayers().indexOf(gameBroad.matchPlayer(winnercolor));
                    mCurrentStartPlayerIndex = startplayer;
                    mCurrentStartPlayer = gameBroad.getPlayers().get(mCurrentStartPlayerIndex);
                    mMessageView.setText("Winner is " + gameBroad.matchPlayer(winnercolor) +
                            "\nAnd would see the approaching zombies");
                    mMessageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enableContinue();
                            mSecondCount++;
                            mIsChiefSelected  = true;
                        }
                    });
                }
            }
            if (mCountSetUp < (mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 2) && (mSecondCount==4 || mSecondCount==5)) {
                if (teamHasSecurityCamera(gameBroad.getPlayers()) || mCurrentYesNoMain) {
                    System.out.println("Has Camera");
                    if (mSecondCount==4){
                        disableContinue();
                        mMessageView.setText("Other Players: You can also see the results by using Security Camera, please confirm you want to use it");
                        mMessageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMessageView.setVisibility(View.INVISIBLE);
                                mMessageView.setEnabled(false);
                                enableYesNo();
                            }
                        });
                    }
                    if (mSecondCount==5){
                        if (mCurrentYesNoMain) {
                            if (mCountSetUp % 2 == 0) {
                                int q = mCountSetUp - (4 * mCurrentTeam.size());
                                int i = (q == 0) ? 0 : (q / 2);
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
                            if (mCountSetUp % 2 == 1) {
                                int q = mCountSetUp - (4 * mCurrentTeam.size());
                                int i = (q == 0) ? 0 : (q / 2);
                                Playable teammember = gameBroad.getPlayers().get(i);
                                if (mCurrentYesNo) {
                                    Intent intent = ShowingZombieActivity.newShowZombiesIntent(MainActivity.this, mCurrentZombiesRooms,mCountSetUp);
                                    startActivityForResult(intent, REQUEST_CODE_VIEWZOMBIE);
                                    overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter,android.support.v7.appcompat.R.anim.abc_popup_exit );
                                    Item securityCamera = gameBroad.matchItem(teammember, "SecurityCamera");
                                    teammember.usedItem(securityCamera);
                                } else {
                                    mCountSetUp++;
                                    if (mCountSetUp != mCurrentTeam.size()*4)
                                    electChief();
                                }

                            }
                        } else {
                            mCountSetUp += gameBroad.getPlayers().size() * 2;
                        }
                    }

                } else {
                    System.out.println("No security Camera");
                    mCountSetUp = mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 2;
                    mSecondCount += 1;
                    if (mCountSetUp != mCurrentTeam.size()*4)
                    electChief();
                }
            }
            if (mCountSetUp == mCurrentTeam.size()*4 + gameBroad.getPlayers().size() * 2) {
                System.out.println("Display message again");
                disableContinue();
                String winnercolor = gameBroad.matchRoom(4).winner();
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

    private void viewAndMove() {
        mCurrentPlayerNumber = gameBroad.getPlayers().size();
        System.out.println("mCountSetup: " + mCountSetUp +  " mSecondCount: " + mSecondCount + " mThirdCount: " + mThirdCount);
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
                playersIndex.add(mCurrentStartPlayerIndex);
            }
            if (mCountSetUp==1) {
                System.out.println("Colleting first player room");
                roomspicked.add(mCurrentRoomPickedNumber);
                ++mCountSetUp;
                if (mCountSetUp!=2)
                viewAndMove();
            }
        }
        if (mCountSetUp<2 && mIsChiefSelected && mSecondCount<2){
            System.out.println("With chief, first player move");
            if (mSecondCount==0 && mCountSetUp==0){
                System.out.println("Showing Chief the zombies");
                Intent intent = ShowingZombieActivity.newShowZombiesIntent(MainActivity.this, mCurrentZombiesRooms,mSecondCount);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, REQUEST_CODE_VIEWZOMBIECHIEF);
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter,android.support.v7.appcompat.R.anim.abc_popup_exit );
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
                System.out.println("Chief Room revealing");
                roomspicked.add(mCurrentRoomPickedNumber);
                disableContinue();
                mMessageView.setText("The chief will move to room " + mCurrentRoomPickedNumber);
                mMessageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enableContinue();
                        ++mCountSetUp;
                    }
                });
            }
        }
        if (mCountSetUp>=2 && mCountSetUp<mCurrentPlayerNumber*2) {
            if (mCountSetUp % 2 == 0  && mCountSetUp<mCurrentPlayerNumber*2) {
                System.out.println("Selecting Room");
                int q = mCurrentStartPlayerIndex + mCountSetUp / 2;
                int i = 0;
                if (q < mCurrentPlayerNumber) {
                    i = q;
                } else {
                    i = q - mCurrentPlayerNumber;
                }
                playersIndex.add(i);
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
                System.out.println("Collecting the Rooms");
                roomspicked.add(mCurrentRoomPickedNumber);
                ++mCountSetUp;
                if (mCountSetUp!=mCurrentPlayerNumber*2 )
                viewAndMove();
            }
        }
        if (mCountSetUp>=mCurrentPlayerNumber*2 && mCountSetUp<mCurrentPlayerNumber*4){
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
                if (mCountSetUp!=mCurrentPlayerNumber*4)
                viewAndMove();
            }
        }
        if (mCountSetUp==mCurrentPlayerNumber*4){
            System.out.println("Display message again");
            disableContinue();
            String winnercolor = gameBroad.matchRoom(4).winner();
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
        System.out.println("mCountSetup: " + mCountSetUp +  " mSecondCount: " + mSecondCount + " mThirdCount: " + mThirdCount + " mFourthCount: " + mFourthCount + " mFifth Count: " + mFifthCount + " mSixth Count: " + mSixCount);
        if (mFourthCount==0){
            System.out.println("Showing Zombie to all");
            Intent intent = ShowingZombieActivity.newShowZombiesIntent(MainActivity.this, mCurrentZombiesRooms,mFourthCount);
            startActivityForResult(intent, REQUEST_CODE_VIEWZOMBIEAll);
            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter,android.support.v7.appcompat.R.anim.abc_popup_exit );
        }
        if (mFourthCount==1){
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
            Intent intent = ShowMoreZombiesActivity.newShowZombiesIntent(MainActivity.this, mCurrentMoreZombies,mFourthCount);
            startActivityForResult(intent, REQUEST_CODE_VIEWZOMBIEALLMORE);
            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter,android.support.v7.appcompat.R.anim.abc_popup_exit );
        }
        if (mFifthCount<6 &&  mFourthCount>=2){
            final Room fallenRoom = gameBroad.matchRoom(mFifthCount+1);
            HashSet<Playable> playersInTheRoom = gameBroad.WhoCan(fallenRoom.existCharacterColor());
            List<Playable> playersInTheRoomList = new ArrayList<>();
            for (Playable player : playersInTheRoom) {
                playersInTheRoomList.add(player);
            }
            System.out.println(fallenRoom.getName());
            if (mFourthCount==2){
                if (!fallenRoom.isFallen()){
                    System.out.println(fallenRoom.getName() +  " is not fallen");
                    mFourthCount=4;
                }
                if (fallenRoom.isFallen() && fallenRoom.getRoomNum()==4){
                    System.out.println(fallenRoom.getName() + " is parking");
                    mCountSetUp=0;
                    mSecondCount=0;
                    mThirdCount=0;
                    mFourthCount++;
                    mSixCount++;
                }
                if (fallenRoom.isFallen() && fallenRoom.getRoomNum()!=4) {
                    System.out.println(fallenRoom.getName() + " is not parking");
                    mSixCount++;
                    if (mSecondCount==0){
                        System.out.println("Confirm if want to use item");
                        disableContinue();
                        mMessageView.setText(fallenRoom.getName() +  " has fallen, please confirm anyone want to use item");
                        mMessageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMessageView.setVisibility(View.INVISIBLE);
                                mMessageView.setEnabled(false);
                                enableYesNo();
                            }
                        });
                    }
                    if (mSecondCount==1 && mCountSetUp<playersInTheRoomList.size()*2){
                        System.out.println("Determining if use item");
                        if (mCurrentYesNoMain){
                            System.out.println("One player will use ITEM");
                            if (mCountSetUp%2==0){
                                System.out.println("using Item");
                                int i = mCountSetUp==0? 0: mCountSetUp/2;
                                String playerColor = playersInTheRoomList.get(i).getColor();
                                String message =  playersInTheRoomList.get(i) +  " please select your item (press no to choose nothing)";
                                ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                                ArrayList<Item> items = (ArrayList<Item>) playersInTheRoomList.get(i).otherItemsList();
                                HashSet<GameCharacter> existedCharacters = fallenRoom.existChracterForThatPlayer(playersInTheRoomList.get(i));
                                List<GameCharacter> existedCharactersList = new ArrayList<>();
                                for (GameCharacter character: existedCharacters){
                                    existedCharactersList.add(character);
                                }
                                Intent intent = PlayerActivity.newChoosingItemIntent(MainActivity.this,rooms,playerColor,items,fallenRoom.getRoomNum(), (ArrayList<GameCharacter>) existedCharactersList,  message,mCountSetUp,6);
                                startActivityForResult(intent,REQUEST_CODE_ITEM);
                                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                            }
                            if (mCountSetUp%2==1){
                                int i = mCountSetUp/2;
                                final Playable player = playersInTheRoomList.get(i);
                                System.out.println("triggering the item or show the player did not use item");
                                if (mCurrentYesNo){
                                    System.out.println("Player use item");
                                    mUsedItem.add(mCurrentSelectedItem);
                                    mPlayersUsedItem.add(player);
                                    if (mCurrentSelectedItem.getItemNum()==3){
                                        disableContinue();
                                        mMessageView.setText(player + " used Axe, one zombie has been killed");
                                        mMessageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                fallenRoom.zombieKilled();
                                                enableContinue();
                                                mCountSetUp++;
                                            }
                                        });
                                    }
                                    if (mCurrentSelectedItem.getItemNum()==4){
                                        disableContinue();
                                        mMessageView.setText(player + " used Shotgun, two zombie has been killed");
                                        mMessageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                fallenRoom.zombieKilled();
                                                fallenRoom.zombieKilled();
                                                enableContinue();
                                                mCountSetUp++;
                                            }
                                        });
                                    }
                                    if (mCurrentSelectedItem.getItemNum()==5){
                                        disableContinue();
                                        mMessageView.setText(player + " used Hareware, one zombie has been temporary block");
                                        mMessageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                fallenRoom.zombieKilled();
                                                enableContinue();
                                                mCountSetUp++;
                                            }
                                        });
                                    }
                                    if (mCurrentSelectedItem.getItemNum()==6){
                                        disableContinue();
                                        mMessageView.setText(player + " used Hidden, his/her" + mCurrentSelectedItem.getAffectedGameCharacter() + " is hiding in the room");
                                        mMessageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                fallenRoom.leave(gameBroad.matchGameCharacter(player,mCurrentSelectedItem.getAffectedGameCharacter().getName()));
                                                enableContinue();
                                                mCountSetUp++;
                                            }
                                        });
                                    }
                                    if (mCurrentSelectedItem.getItemNum()==7){
                                        disableContinue();
                                        mMessageView.setText(player + " used Sprint, his/her" + mCurrentSelectedItem.getAffectedGameCharacter() + " has left the room");
                                        mMessageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                fallenRoom.leave(gameBroad.matchGameCharacter(player,mCurrentSelectedItem.getAffectedGameCharacter().getName()));
                                                enableContinue();
                                                mCountSetUp++;
                                            }
                                        });
                                    }
                                } else {
                                    System.out.println("Player do not use item");
                                    disableContinue();
                                    mMessageView.setText(player + " did not use any item");
                                    mMessageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            enableContinue();
                                            mCountSetUp++;
                                        }
                                    });
                                }
                            }
                        }else {
                            mCountSetUp=playersInTheRoomList.size()*2;
                        }
                    }
                } else {
                    mCountSetUp= playersInTheRoomList.size()*2;
                    mSecondCount+=1;
                }
                if (mCountSetUp==playersInTheRoomList.size()*2 && fallenRoom.isFallen()){
                    System.out.println("still fallen after the item used");
                    disableContinue();
                    mMessageView.setText(fallenRoom.getName() +  " is still fallen. /nPlease vote who will be eaten");
                    mMessageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enableContinue();
                            mCountSetUp=0;
                            mSecondCount=0;
                            mThirdCount=0;
                            mFourthCount++;
                        }
                    });
                }
                if (mCountSetUp==playersInTheRoomList.size()*2 && !fallenRoom.isFallen()){
                    System.out.println("Not fallen after the item used");
                    mCountSetUp=0;
                    mSecondCount=0;
                    mThirdCount=0;
                    mFourthCount+=2;
                }
            }
            if (mFourthCount==3){
                HashSet<Playable> searchteam = gameBroad.WhoCan(fallenRoom.existCharacterColor());
                List<Playable> searchTeam = new ArrayList<>();
                for (Playable player : searchteam) {
                    searchTeam.add(player);
                }
                System.out.println("Calculated searchteam: " +  searchTeam);
                mCurrentTeam = (ArrayList<Playable>) searchTeam;
                System.out.println("Doing the fallen calculation");
                    if (mThirdCount==0 ){
                        System.out.println("getting the team");
                        disableContinue();
                        mMessageView.setText("For " + fallenRoom.getName() +  ": please select the victim");
                        mMessageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enableContinue();
                                mThirdCount++;
                                mCountSetUp=0;
                                mSecondCount=0;

                            }
                        });
                    }
                    if (mThirdCount==1) {
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
                            if (mCountSetUp != mCurrentTeam.size() * 2)
                            showZombie();
                        }
                        if (mCountSetUp == mCurrentTeam.size() * 2 && mSecondCount == 0) {
                            fallenRoom.resetVoteResult();
                            fallenRoom.voteResultAfterVote(votes);
                            ArrayList votes1 = (ArrayList<String>) votes;
                            System.out.println("Step III: show vote result");
                            System.out.println(votes1);
                            Intent intent2 = ShowVoteResultActivity.newVoteResultIntent(MainActivity.this, votes1, gameBroad.matchRoom(5).getCurrentVoteResult(), mSecondCount);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivityForResult(intent2, REQUEST_CODE_VIEWRESULT);
                            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                            mCurrentYesNoMain =false;
                        }
                        if (mCountSetUp >= mCurrentTeam.size() * 2 && mSecondCount > 0 && mCountSetUp < mCurrentTeam.size() * 4) {
                            System.out.println("Step IV: using threat");
                            if (teamHasThreat(mCurrentTeam) || mCurrentYesNoMain) {
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
                                    if (mCurrentYesNoMain) {
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
//                                    for (int q = 0; q < numThreat; q++) {
                                                Item threat = gameBroad.matchItem(teammember, "Threat");
                                                teammember.usedItem(threat);
                                            }
                                            mCountSetUp++;
                                            if (mCountSetUp != mCurrentTeam.size() * 4)
                                            showZombie();
                                        }
                                    } else {
                                        mCountSetUp = mCurrentTeam.size() * 4;
                                    }
                                }
                            } else {
                                System.out.println("No threat");
                                mCountSetUp = mCurrentTeam.size() * 4;
                                mSecondCount = 2;
                                if (mCountSetUp != mCurrentTeam.size() * 4)
                                showZombie();
                            }
                        }
                        if (mCountSetUp == mCurrentTeam.size() * 4 && mSecondCount == 2) {
                            System.out.println("Show summary");
                            HashMap<String, Integer> results = fallenRoom.getCurrentVoteResult();
                            Intent intent = ShowSimpleVoteResultActivity.newVoteResultIntent(MainActivity.this, results, mSecondCount);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivityForResult(intent, REQUEST_CODE_VIEWSIMPLERESULT);
                            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,android.support.v7.appcompat.R.anim.abc_fade_out );
                        }
                    }
                if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 3) {
                    if (fallenRoom.winner().equals("TIE")){
                        System.out.println("Tie");
                        List<Playable> losers = new ArrayList<>();
                        for (Playable player : mCurrentTeam) {
                            losers.add(player);
                        }
                        Random generator = new Random();
                        int num = generator.nextInt(losers.size());
                        mCurrentVictim = mCurrentTeam.get(num);
                        disableContinue();
                        mMessageView.setText("Result is TIE, System has choosed " + mCurrentVictim + " to be the victim");
                        mMessageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enableContinue();
                                mSecondCount++;
                            }
                        });
                    } else {
                        System.out.println("Victim selected");
                        disableContinue();
                        String winnercolor = fallenRoom.winner();
                        mCurrentVictim = gameBroad.matchPlayer(winnercolor);
                        mMessageView.setText("Victim is " + gameBroad.matchPlayer(winnercolor));
                        mMessageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enableContinue();
                                mSecondCount++;
                            }
                        });
                    }
                }
                if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 4){
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
                    GameCharacter deathCharacter = gameBroad.matchGameCharacter(mCurrentVictim, mCurrentGameCharacterSelected);
                    mCurrentVictim.removeCharacter(deathCharacter);
                    fallenRoom.leave(deathCharacter);
                    fallenRoom.setCurrentZombienumber(0);
                    disableContinue();
                    mMessageView.setText(mCurrentVictim + " has lost his/her " + deathCharacter.getName() + "\nZombies have their feast, and returned back to somewhere else to find their next target! \n(The number of zombies in this room has returned to zero)");
                    mMessageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enableContinue();
                            mFourthCount++;
                            mCountSetUp=0;
                            mSecondCount=0;
                            mThirdCount=0;
                        }
                    });
                }
            }
            if (mFourthCount==4){
                if (mUsedItem.size()>0){
                    System.out.println("Triggering after effect");
                    if (mSecondCount<mUsedItem.size()){
                       if (mUsedItem.get(mSecondCount).getItemNum()==5){
                           disableContinue();
                           mMessageView.setText("The affect of hareware has expired, the blocked zombie has returned");
                           mMessageView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   enableContinue();
                                   fallenRoom.zombieApproached();
                                   mSecondCount++;
                               }
                           });
                       } else if (mUsedItem.get(mSecondCount).getItemNum()==6){
                           disableContinue();
                           mMessageView.setText("The affect of hidden has expired, the hidden character has returned");
                           mMessageView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   enableContinue();
                                   fallenRoom.enter(gameBroad.matchGameCharacter(mPlayersUsedItem.get(mSecondCount),mUsedItem.get(mSecondCount).getAffectedGameCharacter().getName()));
                                   mSecondCount++;
                               }
                           });
                       } else if (mUsedItem.get(mSecondCount).getItemNum()==7){
                           mMessageView.setText("The affect of sprint has trigger, the left character has moved to the destination");
                           mMessageView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   enableContinue();
                                   gameBroad.matchRoom(mUsedItem.get(mSecondCount).getAfteraffectedRoomNumber()).enter(gameBroad.matchGameCharacter(mPlayersUsedItem.get(mSecondCount),mUsedItem.get(mSecondCount).getAffectedGameCharacter().getName()));
                                   mSecondCount++;
                               }
                           });
                        } else {
                           mSecondCount++;
                           showZombie();
                       }
                    }
                    if (mSecondCount==mUsedItem.size()){
                        mFourthCount++;
                        showZombie();
                    }
                }else{
                    mFourthCount++;
                    showZombie();
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

                    mUsedItem.clear();  mCountSetUp=0;
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

    public static boolean teamHasThreat(ArrayList<Playable> players) {
        for (Playable player : players) {
            if (player.hasThreat())
                return true;
        }
        return false;
    }

    public static boolean teamHasSecurityCamera(List<Playable> players) {
        for (Playable player : players) {
            if (player.hasSecurityCamera()) {
                return true;
            }
        }
        return false;
    }

    public static boolean teamHasOtherItems(HashSet<Playable> players) {
        for (Playable player : players) {
            if (player.hasOthersItems()) {
                return true;
            }
        }
        return false;
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
                System.out.println("Data null");
                return;
            }
            mSecondCount = ShowVoteResultActivity.getCountedSetUp(data);
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
        }
        if (requestCode == REQUEST_CODE_VIEWZOMBIEALLMORE){
            if (data == null){
                return;
            }
            mFourthCount = ShowMoreZombiesActivity.getCountedSetUp(data);
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


}
