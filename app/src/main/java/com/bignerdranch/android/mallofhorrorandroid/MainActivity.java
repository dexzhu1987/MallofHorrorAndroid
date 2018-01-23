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
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Item;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.Playable;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.Room;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String PLAYER_NUMBER = "player_number";
    private static final int REQUEST_CODE_ROOM = 0;
    private static final int REQUEST_CODE_CHARACTER = 1;
    private static final int REQUEST_CODE_MESSAGE = 2;
    private static final int REQUEST_CODE_VOTE = 3;
    private static final int REQUEST_CODE_VIEWRESULT = 4;

    private int mPlayerNumber;
    private final static GameBroad gameBroad = new GameBroad(4);
    private static int mCurrentRoomPickedNumber = 0;
    private static int mCountSetUp;
    private static int mSecondCount;
    private static boolean mCurrentYesNoMain = false;
    private static String mCurrentGameCharacterSelected = "";
    private static String mCurrentVoteColor = "";
    private static int mCountPhase;
    final static List<String> colors = new ArrayList<>();
    final static List<String> actualcolors= new ArrayList<>();

    private ConstraintLayout mMainActivityLayout;
    private ImageButton mRedButton, mYellowButton, mBlueButton, mGreenButton, mBrownButton, mBlackButton;
    private ImageButton mContinueButton;
    private ImageButton mYesButton, mNoButton;
    private GridLayout mRestRoomArea, mCachouArea, mMegatoyArea, mParkingArea, mSecurityArea, mSupermarketArea;
    private TextView mRestRoomZombie, mCachouZombie, mMegatoyZombie, mParkingZombie, mSecurityZombie, mSupermarketZombie;
    private List<ImageButton> mPlayerButtons = new ArrayList<>();
    private List<ImageButton> mActualPlayerButtons = new ArrayList<>();
    private TextView mMessageView;


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
            }
        });
        mNoButton = findViewById(R.id.nobutton_main);
        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentYesNoMain = false;
                mSecondCount++;
                disableYesNo();
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
        if (gameBroad.matchRoom(4).isEmpty() || gameBroad.getItemDeck().getItemsDeck().size() < 3) {
            disableContinue();
            mMessageView.setText("Due to Parking is empty (or no more item avaiable), no searching will be performed");
            mMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableContinue();
                    mCountPhase++;
                }
            });
        } else {
            disableContinue();
            HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(4).existCharacterColor());
            List<Playable> searchTeam = new ArrayList<>();
            for (Playable player : searchteam) {
                searchTeam.add(player);
            }
            mMessageView.setText(searchTeam + " is/are in the parking");
            mMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableContinue();
                }
            });
            List<String> votes = new ArrayList<>();
            if (mCountSetUp % 2 == 0 && mCountSetUp < 2*searchTeam.size()){
                Playable teammember = searchTeam.get(mCountSetUp);
                ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();
                String playercolor = teammember.getColor();
                String message = teammember +  "  please vote who can search";
                ArrayList voteOptions = (ArrayList<Playable>) searchTeam;
                ArrayList<Item> items = (ArrayList<Item>) searchTeam.get(mCountSetUp).getCurrentItem();
                Intent intent = PlayerActivity.newVotingIntent(MainActivity.this,rooms,playercolor,items, voteOptions ,message,mCountSetUp,4);
                startActivityForResult(intent,REQUEST_CODE_VOTE);
            }
            String vote = mCurrentVoteColor;
            if (mCountSetUp % 2 == 1 && mCountSetUp < 2*searchTeam.size()){
                Playable teammember = searchTeam.get(mCountSetUp-1);
                votes.add(teammember.getColor());
                votes.add(vote);
                mCountSetUp++;
                searchParking();
            }
            if (mCountSetUp == searchTeam.size() * 2  && mSecondCount == 0){
                gameBroad.matchRoom(4).resetVoteResult();
                gameBroad.matchRoom(4).voteResultAfterVote(votes);
                ArrayList votes1 = (ArrayList<String>) votes;
                Intent intent = ShowVoteResultActivity.newVoteResultIntent(MainActivity.this, votes1,gameBroad.matchRoom(4).getCurrentVoteResult(),mSecondCount);
                startActivityForResult(intent, REQUEST_CODE_VIEWRESULT);
            }
            if (mCountSetUp == searchTeam.size() * 2 && mSecondCount > 0) {
                if (teamHasThreat(searchteam)) {
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
                        int i = searchTeam.size()% mCountSetUp;
                        Playable teammember = searchTeam.get(i);
                        String color = searchTeam.get(i).getColor();
                        String message = teammember + " please confirm you want to use THREAT";
                        ArrayList rooms = (ArrayList<Room>)gameBroad.getRooms();

                        }

                    }

                } else {
                     mCountSetUp += searchTeam.size();
                     mSecondCount = 0;
                }
            }
            if (mCountSetUp == searchTeam.size() *3 )
            //using Threat to change result;

                if (voteYes) {
                        moreThreatused =false;
                        boolean memberUsedThreat = false;
                        for (int i=0; i<searchTeam.size();i++) {
                            ;
                            List<Integer> threadOptions = new ArrayList<>();
                            for (int q=0; q<=teammember.threatNum(); q++){
                                threadOptions.add(q);
                            }
                            memberUsedThreat = YesNoWindow.display();
                            if (memberUsedThreat){
                                if (teammember.hasThreat()){
                                    String effectedColor = "";
                                    for (int q = 0; q < votes.size(); q += 2) {
                                        if (teammember.getColor().equalsIgnoreCase(votes.get(q))) {
                                            effectedColor = votes.get(q + 1);
                                        }
                                    }
                                    effectedColor = effectedColor.toUpperCase();
                                    gameBroad.matchRoom(4).voteResultAfterItem(effectedColor, 1);
                                    Item threat = gameBroad.matchItem(teammember, "Threat");
                                    teammember.usedItem(threat);
                                    SimpleMessageWindow.display("You vote has increased by one");
                                } else {
                                    SimpleMessageWindow.display("You do not have THREAT");
                                }

                            }

                        }
                        ViewVotingSummaryWindow.display(gameBroad.matchRoom(4).getCurrentVoteResult(), "Voting " +
                                "Result after Threat");
                }
            }
            //result print
            if (gameBroad.matchRoom(4).winner().equals("TIE")) {
                SimpleMessageWindow.display("Result is TIE. " + " No Searching will be performed");
            } else {
                //searching begins
                mainWindow.setScene(parkingSearchScene);
                String winnercolor = gameBroad.matchRoom(4).winner();
                SimpleMessageWindow.display("Winner is " + gameBroad.matchPlayer(winnercolor));
                SimpleMessageWindow.display(gameBroad.matchPlayer(winnercolor) + " searched the parking and " +
                        "found below items (only winning player can see the result and arrange items)");
                gameBroad.getItemDeck().shuffle();
                Item item1 = gameBroad.getItemDeck().deal();
                Item item2 = gameBroad.getItemDeck().deal();
                Item item3 = gameBroad.getItemDeck().deal();
                List<Item> itemtemplist = new ArrayList<>();
                itemtemplist.add(item1);
                itemtemplist.add(item2);
                itemtemplist.add(item3);
                Item itemselect = ChoosingItemWindow.display(itemtemplist,"Please choose the item you want to keep");
                if (gameBroad.matchPlayer(winnercolor).getCurrentItem().size()<6){
                    ItemGettingWindow.display(itemselect, "You get");
                    gameBroad.matchPlayer(winnercolor).getItem(itemselect);
                }
                else {
                    SimpleMessageWindow.display("You get " + itemselect + ". However, due to your bag is full, you cannot carry more items.(Your throw the item on the ground)");
                }
                itemtemplist.remove(itemselect);
                Item itemgiveselect = ChoosingItemWindow.display(itemtemplist, "Please select the item you want to give");
                HashSet<Playable> others = gameBroad.RemainPlayers(gameBroad.matchPlayer(winnercolor));
                List<Playable> othersList = new ArrayList<>();
                for (Playable other: others){
                    othersList.add(other);
                }
                String givecolor =ChoosingColorWindow.display(othersList, "Please select who you want give in the List: " + others);
                if (gameBroad.matchPlayer(givecolor).getCurrentItem().size()<6){
                    gameBroad.matchPlayer(givecolor).getItem(itemgiveselect);
                }
                itemtemplist.remove(itemgiveselect);
                gameBroad.getItemDeck().addBackItem(itemtemplist.get(0));
                String ok1 = "";
                SimpleMessageWindow.display(gameBroad.matchPlayer(givecolor) + " you have received an item from " + gameBroad.matchPlayer(winnercolor)
                        + " (keep it to yourself)");
                if (gameBroad.matchPlayer(givecolor).getCurrentItem().size()<6){
                    ItemGettingWindow.display(itemgiveselect, gameBroad.matchPlayer(givecolor) + ": You get");
                } else {
                    SimpleMessageWindow.display("You should have received " + itemgiveselect + ". Howver, due to your bag is full. You cannot carry more items.");
                }
                SimpleMessageWindow.display("Other players will be joining the game now");
                SimpleMessageWindow.display("Player " + givecolor + " get an item from player " + winnercolor);
            }
        }
    }

    public static boolean teamHasThreat(HashSet<Playable> players) {
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

    }

}
