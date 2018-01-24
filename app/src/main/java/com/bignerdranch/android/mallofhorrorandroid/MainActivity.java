package com.bignerdranch.android.mallofhorrorandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.GameCharacter;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Dice.PairofDice;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Dice.TwoPairofDice;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Item;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.Playable;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final String PLAYER_NUMBER = "player_number";
    private static final int REQUEST_CODE_ROOM = 0;
    private static final int REQUEST_CODE_CHARACTER = 1;
    private static final int REQUEST_CODE_MESSAGE = 2;
    private static final int REQUEST_CODE_VOTE = 3;
    private static final int REQUEST_CODE_VIEWRESULT = 4;
    private static final int REQUEST_CODE_YESNO = 5;
    private static final int REQUEST_CODE_VIEWSIMPLERESULT = 6;
    private static final int REQUEST_CODE_CHOOSINGITEM = 7;
    private static final int REQUEST_CODE_VIEWZOMBIE = 8;

    private int mPlayerNumber;
    private final static GameBroad gameBroad = new GameBroad(4);
    private static int mCurrentRoomPickedNumber = 0;
    private static int mCountSetUp;
    private static int mSecondCount;
    private static boolean mCurrentYesNoMain = false;
    private static boolean mCurrentYesNo = false;
    private static String mCurrentGameCharacterSelected = "";
    private static String mCurrentVoteColor = "";
    private static Item mCurrentSelectedItem;
    private static int mCountPhase;
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
    private List<ImageButton> mActualPlayerButtons = new ArrayList<>();
    private TextView mMessageView;
    private static List<String> votes = new ArrayList<>();
    private static int mThirdCount;
    private static List<Item> mCurrentItemOptions = new ArrayList<>();
    private static ArrayList<Integer> mCurrentZombiesRooms = new ArrayList<>();
    private static int mCurrentStartPlayerIndex;
    private static int mCurrentStartRoom;
    private static Playable mCurrentStartPlayer;


    public static Intent mainIntent(Context packageContext, int playerNumber){
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(PLAYER_NUMBER, playerNumber);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gettingReady();
        updateRoom(MainActivity.this);

    }

    private void gettingReady() {
        mPlayerNumber = getIntent().getIntExtra(PLAYER_NUMBER,0);
        gameBroad.setPlayersNumber(mPlayerNumber);

        ContinueButtonMethod();
        otherCommonSetUp();

        mMessageView = findViewById(R.id.messageView_main);
        mMessageView.setText("PRE-GAME SETTING PHASE ONE : CHOOSE ROOM");
        mMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableContinue();
                mCountPhase++;
            }
        });

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
            }
        });
    }

    private void enableContinue() {
        mMessageView.setEnabled(false);
        mMessageView.setVisibility(View.INVISIBLE);
        mContinueButton.setVisibility(View.VISIBLE);
        mContinueButton.setEnabled(true);
    }

    private void disableContinue() {
        mContinueButton.setEnabled(false);
        mContinueButton.setVisibility(View.INVISIBLE);
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setEnabled(true);
    }

    private void otherCommonSetUp() {

        mMainActivityLayout = findViewById(R.id.main_activity);

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
    }

    private void enableYesNo() {
        mYesButton.setVisibility(View.VISIBLE);
        mNoButton.setVisibility(View.VISIBLE);
        mYesButton.setEnabled(true);
        mNoButton.setEnabled(true);
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
            currentPane.removeAllViews();
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
            startActivityForResult(intent,REQUEST_CODE_ROOM);
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
            startActivityForResult(intent,REQUEST_CODE_CHARACTER);
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
            startActivityForResult(intent,REQUEST_CODE_MESSAGE);
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
                    enableContinue();
                    mCountPhase++;
                    mCountSetUp=0;
                    mSecondCount=0;
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
                    startActivityForResult(intent,REQUEST_CODE_VOTE);
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
                }
                if (mCountSetUp >= mCurrentTeam.size()*2 && mSecondCount > 0 && mCountSetUp < mCurrentTeam.size()*4) {
                    System.out.println("Step IV: using threat");
                    if (teamHasThreat(mCurrentTeam)) {
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
                                    startActivityForResult(intent, REQUEST_CODE_YESNO);
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
                                    searchParking();
                                }
                            } else {
                                mCountSetUp += mCurrentTeam.size() * 2;
                            }
                        }
                    } else {
                        System.out.println("No threat");
                        mCountSetUp += mCurrentTeam.size() * 2;
                        mSecondCount += 1;
                        System.out.println("mCountSetup: " + mCountSetUp +  " mSecond Count: " + mSecondCount);
                        searchParking();
                    }
                }
                if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 2 ) {
                    System.out.println("Show summary");
                    HashMap<String, Integer> results = gameBroad.matchRoom(4).getCurrentVoteResult();
                    Intent intent = ShowSimpleVoteResultActivity.newVoteResultIntent(MainActivity.this,results,mSecondCount);
                    startActivityForResult(intent,REQUEST_CODE_VIEWSIMPLERESULT);
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
                                mCountPhase++;
                                mCountSetUp=0;
                                mSecondCount=0;
                                mThirdCount=0;
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
                    String message = "Please select below items to keep";
                    Intent intent = ChoosingItemActivity.newChoosingItemIntent(MainActivity.this,mCurrentItemOptions, message, mSecondCount);
                    startActivityForResult(intent,REQUEST_CODE_CHOOSINGITEM);
                }
                Item itemselect = mCurrentSelectedItem;
                if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount==5) {
                    System.out.println("Giving Item");
                    String winnercolor = gameBroad.matchRoom(4).winner();
                    if (gameBroad.matchPlayer(winnercolor).getCurrentItem().size()<6) {
                        gameBroad.matchPlayer(winnercolor).getItem(itemselect);
                    }
                    System.out.println(mCurrentItemOptions);
                    String message = "Please select below items to give";
                    Intent intent = ChoosingItemActivity.newChoosingItemIntent(MainActivity.this, mCurrentItemOptions, message, mSecondCount);
                    startActivityForResult(intent,REQUEST_CODE_CHOOSINGITEM);
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
                    startActivityForResult(intent,REQUEST_CODE_VOTE);
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
                    startActivityForResult(intent,REQUEST_CODE_MESSAGE);
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
                        }
                    });
                }
            }
        }
    }

    private void electChief() {
        System.out.println("mCountSetup: " + mCountSetUp +  " mSecondCount: " + mSecondCount + " mThriedCount: " + mThirdCount);
        if (mCountSetUp==0 && gameBroad.matchRoom(5).isEmpty()) {
            disableContinue();
            mMessageView.setText("Due to Security HQ is empty, no election will be performed");
            mMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableContinue();
                    mCountPhase++;
                    mCountSetUp=0;
                    mSecondCount=0;
                }
            });
        } else {
            if (mThirdCount==0){
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
            if (mThirdCount==1){
                System.out.println(mCurrentTeam);
                if (mCountSetUp % 2 == 0 && (mCountSetUp < 2 * mCurrentTeam.size())){
                    int i = (mCountSetUp ==0 )? 0: mCountSetUp/2;
                    Playable teammember = mCurrentTeam.get(i);
                    ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                    String playercolor = teammember.getColor();
                    String message = teammember +  "  please vote who is the chief";
                    ArrayList voteOptions =  mCurrentTeam;
                    ArrayList<Item> items = (ArrayList<Item>) mCurrentTeam.get(i).getCurrentItem();
                    System.out.println("Step I: calling vote" + " mCount: " + mCountSetUp + " i: " + i);
                    System.out.println(mCurrentTeam);
                    Intent intent = PlayerActivity.newVotingIntent(MainActivity.this,rooms,playercolor,items, voteOptions ,message,mCountSetUp,4);
                    startActivityForResult(intent,REQUEST_CODE_VOTE);
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
                    electChief();
                }
                if (mCountSetUp == mCurrentTeam.size()*2  && mSecondCount == 0){
                    gameBroad.matchRoom(5).resetVoteResult();
                    gameBroad.matchRoom(5).voteResultAfterVote(votes);
                    ArrayList votes1 = (ArrayList<String>) votes;
                    System.out.println("Step III: show vote result");
                    System.out.println(votes1);
                    Intent intent2 = ShowVoteResultActivity.newVoteResultIntent(MainActivity.this, votes1,gameBroad.matchRoom(5).getCurrentVoteResult(),mSecondCount);
                    startActivityForResult(intent2, REQUEST_CODE_VIEWRESULT);
                }
                if (mCountSetUp >= mCurrentTeam.size()*2 && mSecondCount > 0 && mCountSetUp < mCurrentTeam.size()*4) {
                    System.out.println("Step IV: using threat");
                    if (teamHasThreat(mCurrentTeam)) {
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
                                    startActivityForResult(intent, REQUEST_CODE_YESNO);
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
                                    electChief();
                                }
                            } else {
                                mCountSetUp += mCurrentTeam.size() * 2;
                            }
                        }
                    } else {
                        System.out.println("No threat");
                        mCountSetUp += mCurrentTeam.size() * 2;
                        mSecondCount += 1;
                        electChief();
                    }
                }
                if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 2 ) {
                    System.out.println("Show summary");
                    HashMap<String, Integer> results = gameBroad.matchRoom(5).getCurrentVoteResult();
                    Intent intent = ShowSimpleVoteResultActivity.newVoteResultIntent(MainActivity.this,results,mSecondCount);
                    startActivityForResult(intent,REQUEST_CODE_VIEWSIMPLERESULT);
                }
                if (mCountSetUp == mCurrentTeam.size()*4 && mSecondCount == 3) {
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
                    if (gameBroad.matchRoom(5).winner().equals("TIE")){
                        System.out.println("No chief");
                        Random generator = new Random();
                        startplayer = generator.nextInt(gameBroad.getPlayers().size());
                        mCurrentStartPlayerIndex = startplayer;
                        mCurrentStartPlayer = gameBroad.getPlayers().get(mCurrentStartPlayerIndex);
                        disableContinue();
                        mMessageView.setText("Result is TIE. " + "No chief selected. A ramdon player will start first");
                        mMessageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enableContinue();
                                mSecondCount++;
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
                            }
                        });
                    }
                }
                if (mCountSetUp == mCurrentTeam.size()*4 && (mSecondCount==4 || mSecondCount==5)) {
                    if (teamHasSecurityCamera(gameBroad.getPlayers())) {
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
                                    Playable teammember = mCurrentTeam.get(i);
                                    String color = mCurrentTeam.get(i).getColor();
                                    String message = teammember + " please confirm you want to use Security";
                                    ArrayList rooms = (ArrayList<Room>) gameBroad.getRooms();
                                    ArrayList<Item> items = (ArrayList<Item>) mCurrentTeam.get(i).getCurrentItem();
                                    Boolean yesAndNo = false;
                                    if (teammember.hasSecurityCamera()) {
                                        yesAndNo = true;
                                    }
                                    Intent intent = PlayerActivity.newYesNoIntent(MainActivity.this, rooms, color, items, yesAndNo, message, mCountSetUp, 5);
                                    startActivityForResult(intent, REQUEST_CODE_YESNO);
                                }
                                if (mCountSetUp % 2 == 1) {
                                    int q = mCountSetUp - (4 * mCurrentTeam.size());
                                    int i = (q == 0) ? 0 : (q / 2);
                                    Playable teammember = mCurrentTeam.get(i);
                                    if (mCurrentYesNo) {
                                        Intent intent = ShowingZombieActivity.newShowZombiesIntent(MainActivity.this, mCurrentZombiesRooms,mCountSetUp);
                                        startActivityForResult(intent, REQUEST_CODE_VIEWZOMBIE);
                                        Item securityCamera = gameBroad.matchItem(teammember, "SecurityCamera");
                                        teammember.usedItem(securityCamera);
                                        electChief();
                                    } else {
                                        mCountSetUp++;
                                        electChief();
                                    }

                                }
                            } else {
                                mCountSetUp += mCurrentTeam.size() * 2;
                            }
                        }

                    } else {
                        System.out.println("No security Camera");
                        mCountSetUp += mCurrentTeam.size() * 2;
                        mSecondCount += 1;
                        electChief();
                    }
                }
                if (mCountSetUp == mCurrentTeam.size()*6) {
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
                        }
                    });
                }
            }
        }
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
            mCountSetUp = ChoosingItemActivity.getCountedSetUp(data);
        }


    }

}
