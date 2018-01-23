package com.bignerdranch.android.mallofhorrorandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.GameCharacter;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Item;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.Playable;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.Room;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {
    protected static final String ROOMS = "rooms";
    protected static final String ITEMS = "items";
    protected static final String GAMECHARACTERSOPTIONS = "gamecharacters";
    protected static final String ROOMSOPTIONS = "roomsoptions";
    protected static final String VOTINGOPTIONS = "votingopions";
    protected static final String MESSAGE = "message";
    protected static final String COUNTSETUP = "countsetup";
    protected static final String TYPE = "type";
    protected static final String PLAYERCOLOR = "playercolor";

    protected static final String CHOOSEDROOM = "choosedroom";
    protected static final String SETUPCOUNTED = "SETUPCOUNTED";
    protected static final String CHOOSEDCHARACTER = "choosedcharacter";
    protected static final String CHOOSEDCOLOR = "choosedcolor";

    private ImageButton mEnterRestRoomButton, mEnterCachouButton, mEnterMegatoyButton, mEnterParkingButton, mEnterSecurityButton,
            mEnterSupermarketButton;
    private ImageButton mGunManButton, mToughGunButton, mModelButton;
    private ImageButton mVoteRedButton, mVoteYellowButton, mVoteBlueButton, mVoteGreenButton, mVoteBrownButton, mVoteBlackButton;
    private GridLayout mRestRoomArea, mCachouArea, mMegatoyArea, mParkingArea, mSecurityArea, mSupermarketArea;
    private ImageButton mYesButton, mNoButton;
    private TextView mMessageTextView;
    private ImageButton mPlayerButton;
    private TextView mRestRoomZombie, mCachouZombie, mMegatoyZombie, mParkingZombie, mSecurityZombie, mSupermarketZombie;
    private ImageView mItemSolt1, mItemSolt2, mItemSolt3, mItemSolt4, mItemSolt5, mItemSolt6;
    private List<ImageButton> mVotingButtons = new ArrayList<>();
    private List<ImageButton> mEnterButtons = new ArrayList<>();
    private List<ImageButton> mGameCharaterButtons = new ArrayList<>();
    private List<ImageView> mAllItemSlots = new ArrayList<>();

    private ArrayList<Integer> mRoomOptions;
    private ArrayList<? extends Room> mRooms;
    private ArrayList<? extends Item> mItems;
    private String mMessage;
    private ArrayList<GameCharacter> mGameCharactersOptions;
    private ArrayList<Playable> mVoteOptions;
    private int mCountSetUp;
    private int mType;
    private String mColor;




    public static Intent newChoosingRoomIntent (Context context, List<Room> rooms, String playercolor, List<Item> items,  ArrayList<Integer> roomOptions,
                                                String message, int countsetup, int type){
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putParcelableArrayListExtra (ROOMS, (ArrayList<? extends Parcelable>) rooms);
        intent.putParcelableArrayListExtra(ITEMS,(ArrayList<? extends Parcelable>) items);
        intent.putExtra(PLAYERCOLOR,playercolor);
        intent.putIntegerArrayListExtra(ROOMSOPTIONS, roomOptions);
        intent.putExtra(MESSAGE,message);
        intent.putExtra(COUNTSETUP,countsetup);
        intent.putExtra(TYPE, type);
        return intent;
    }

    public static Intent newChoosingCharacterIntent (Context context, List<Room> rooms, String playercolor, List<Item> items, ArrayList<GameCharacter> gameCharactersOptions,
                                                     String message, int countsetup, int type){
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putParcelableArrayListExtra (ROOMS, (ArrayList<? extends Parcelable>) rooms);
        intent.putParcelableArrayListExtra(ITEMS,(ArrayList<? extends Parcelable>) items);
        intent.putExtra(PLAYERCOLOR,playercolor);
        intent.putParcelableArrayListExtra(GAMECHARACTERSOPTIONS, gameCharactersOptions);
        intent.putExtra(MESSAGE,message);
        intent.putExtra(COUNTSETUP,countsetup);
        intent.putExtra(TYPE, type);
        return intent;
    }

    public static Intent newMessageIntent (Context context, List<Room> rooms, String playercolor, List<Item> items,
                                                     String message, int countsetup, int type){
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putParcelableArrayListExtra (ROOMS, (ArrayList<? extends Parcelable>) rooms);
        intent.putParcelableArrayListExtra(ITEMS,(ArrayList<? extends Parcelable>) items);
        intent.putExtra(PLAYERCOLOR,playercolor);
        intent.putExtra(MESSAGE,message);
        intent.putExtra(COUNTSETUP,countsetup);
        intent.putExtra(TYPE, type);
        return intent;
    }

    public static Intent newVotingIntent (Context context, List<Room> rooms, String playercolor, List<Item> items, ArrayList<Playable> votingOptions,
                                           String message, int countsetup, int type){
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putParcelableArrayListExtra (ROOMS, (ArrayList<? extends Parcelable>) rooms);
        intent.putParcelableArrayListExtra(ITEMS,(ArrayList<? extends Parcelable>) items);
        intent.putExtra(PLAYERCOLOR,playercolor);
        intent.putExtra(MESSAGE,message);
        intent.putParcelableArrayListExtra(VOTINGOPTIONS, votingOptions);
        intent.putExtra(COUNTSETUP,countsetup);
        intent.putExtra(TYPE, type);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        gettingReady();
    }

    private void gettingReady() {
        mType = getIntent().getIntExtra(TYPE,0);
        mMessage = getIntent().getStringExtra(MESSAGE);
        mRooms = getIntent().getParcelableArrayListExtra(ROOMS);
        mItems = getIntent().getParcelableArrayListExtra(ITEMS);
        mCountSetUp = getIntent().getIntExtra(COUNTSETUP,0);
        mColor = getIntent().getStringExtra(PLAYERCOLOR);

        mMessageTextView = findViewById(R.id.messageView);
        mMessageTextView.setText(mMessage);
        if (mType==1){
            mRoomOptions = getIntent().getIntegerArrayListExtra(ROOMSOPTIONS);
            mMessageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessageTextView.setVisibility(View.INVISIBLE);
                    for (int i=0; i<mRoomOptions.size(); i++){
                        int roomNumber = mRoomOptions.get(i);
                        mEnterButtons.get(roomNumber-1).setVisibility(View.VISIBLE);
                        mEnterButtons.get(roomNumber-1).setEnabled(true);
                    }
                }
            });
        }
        if (mType==2){
            mGameCharactersOptions = getIntent().getParcelableArrayListExtra(GAMECHARACTERSOPTIONS);
            mMessageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessageTextView.setVisibility(View.INVISIBLE);
                    for (int i=0; i<mGameCharactersOptions.size(); i++){
                        if (mGameCharactersOptions.get(i).getName().equalsIgnoreCase("Gun Man")){
                            mGunManButton.setVisibility(View.VISIBLE);
                            mGunManButton.setEnabled(true);
                        } else if (mGameCharactersOptions.get(i).getName().equalsIgnoreCase("Tough Guy")){
                            mToughGunButton.setVisibility(View.VISIBLE);
                            mToughGunButton.setEnabled(true);
                        } else if (mGameCharactersOptions.get(i).getName().equalsIgnoreCase("Model")){
                            mModelButton.setVisibility(View.VISIBLE);
                            mModelButton.setEnabled(true);
                        }
                    }
                }
            });
        }
        if (mType == 3){
            mMessageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCountSetUp++;
                    Intent data = new Intent();
                    data.putExtra(SETUPCOUNTED, mCountSetUp );
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
        }
        if (mType == 4){
            mVoteOptions = getIntent().getParcelableArrayListExtra(VOTINGOPTIONS);
            mMessageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessageTextView.setVisibility(View.INVISIBLE);
                    for (int i=0; i<mVoteOptions.size(); i++){
                        if (mVoteOptions.get(i).getColor().equalsIgnoreCase("RED")){
                            mVoteRedButton.setVisibility(View.VISIBLE);
                            mVoteRedButton.setEnabled(true);
                        } else if (mVoteOptions.get(i).getColor().equalsIgnoreCase("YELLOW")){
                            mVoteYellowButton.setVisibility(View.VISIBLE);
                            mVoteYellowButton.setEnabled(true);
                        } else if (mVoteOptions.get(i).getColor().equalsIgnoreCase("BLUE")){
                            mVoteBlueButton.setVisibility(View.VISIBLE);
                            mVoteBlueButton.setEnabled(true);
                        } else if (mVoteOptions.get(i).getColor().equalsIgnoreCase("GREEN")){
                            mVoteGreenButton.setVisibility(View.VISIBLE);
                            mVoteGreenButton.setEnabled(true);
                        } else if (mVoteOptions.get(i).getColor().equalsIgnoreCase("BROWN")){
                            mVoteBrownButton.setVisibility(View.VISIBLE);
                            mVoteBrownButton.setEnabled(true);
                        } else if (mVoteOptions.get(i).getColor().equalsIgnoreCase("BLACK")) {
                            mVoteBlackButton.setVisibility(View.VISIBLE);
                            mVoteBlackButton.setEnabled(true);
                        }
                    }
                }
            });
        }

        otherCommonSetUp();
        settingUpColor();
        updateRoom(PlayerActivity.this);
        updateItem();
    }

    private void otherCommonSetUp() {
        mPlayerButton = findViewById(R.id.play);


        mEnterRestRoomButton = findViewById(R.id.enter_restroom);
        mEnterRestRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingRoom(1);
            }
        });
        mEnterCachouButton = findViewById(R.id.enter_cachou);
        mEnterCachouButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingRoom(2);
            }
        });
        mEnterMegatoyButton = findViewById(R.id.enter_megatoy);
        mEnterMegatoyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingRoom(3);
            }
        });
        mEnterParkingButton = findViewById(R.id.enter_parking);
        mEnterParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingRoom(4);
            }
        });
        mEnterSecurityButton = findViewById(R.id.enter_security);
        mEnterSecurityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingRoom(5);
            }
        });
        mEnterSupermarketButton = findViewById(R.id.enter_supermarket);
        mEnterSupermarketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingRoom(6);
            }
        });
        mEnterButtons.add(mEnterRestRoomButton);
        mEnterButtons.add(mEnterCachouButton);
        mEnterButtons.add(mEnterMegatoyButton);
        mEnterButtons.add(mEnterParkingButton);
        mEnterButtons.add(mEnterSecurityButton);
        mEnterButtons.add(mEnterSupermarketButton);


        mGunManButton = findViewById(R.id.redgunman_button);
        mGunManButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingCharacter("Gun Man"); ;
            }
        });
        mToughGunButton = findViewById(R.id.redtoughman_button);
        mToughGunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingCharacter("Tough Guy");
            }
        });
        mModelButton = findViewById(R.id.redmodel_button);
        mModelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingCharacter("Model");
            }
        });
        mGameCharaterButtons.add(mGunManButton);
        mGameCharaterButtons.add(mToughGunButton);
        mGameCharaterButtons.add(mModelButton);


        mVoteRedButton = findViewById(R.id.votered_button);
        mVoteRedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingColor("Red");
            }
        });
        mVoteYellowButton = findViewById(R.id.voteyellow_button);
        mVoteYellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingColor("Yellow");
            }
        });
        mVoteBlueButton = findViewById(R.id.voteblue_button);
        mVoteBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingColor("Blue");
            }
        });
        mVoteGreenButton = findViewById(R.id.votegreen_button);
        mVoteGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingColor("Green");
            }
        });
        mVoteBrownButton = findViewById(R.id.votebrown_button);
        mVoteBrownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingColor("Brown");
            }
        });
        mVoteBlackButton = findViewById(R.id.voteblack_button);
        mVoteBlackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingColor("Black");
            }
        });
        mVotingButtons.add(mVoteRedButton);
        mVotingButtons.add(mVoteYellowButton);
        mVotingButtons.add(mVoteBlueButton);
        mVotingButtons.add(mVoteGreenButton);
        mVotingButtons.add(mVoteBrownButton);
        mVotingButtons.add(mVoteBlackButton);

        mItemSolt1 = findViewById(R.id.itemSolt1);
        mItemSolt2 = findViewById(R.id.itemSolt2);
        mItemSolt3 = findViewById(R.id.itemSolt3);
        mItemSolt4 = findViewById(R.id.itemSolt4);
        mItemSolt5 = findViewById(R.id.itemSolt5);
        mItemSolt6 = findViewById(R.id.itemSolt6);
        mAllItemSlots.add(mItemSolt1);
        mAllItemSlots.add(mItemSolt2);
        mAllItemSlots.add(mItemSolt3);
        mAllItemSlots.add(mItemSolt4);
        mAllItemSlots.add(mItemSolt5);
        mAllItemSlots.add(mItemSolt6);

        mYesButton = findViewById(R.id.yesbutton);
        mYesButton.setEnabled(false);
        mYesButton.setVisibility(View.INVISIBLE);
        mNoButton = findViewById(R.id.nobutton);
        mNoButton.setEnabled(false);
        mNoButton.setVisibility(View.INVISIBLE);


        for (ImageButton button: mVotingButtons){
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }

        for (ImageButton button: mEnterButtons){
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }

        for (ImageButton button: mGameCharaterButtons){
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }
    }

    private void settingUpColor() {
        if (mColor.equalsIgnoreCase("Yellow")){
            mPlayerButton.setImageResource(R.drawable.yellowbg);
            mGunManButton.setImageResource(R.drawable.yellowgunmanbttn);
            mToughGunButton.setImageResource(R.drawable.yellowtoughguybttn);
            mModelButton.setImageResource(R.drawable.yellowmodelbttn);
        }

        if (mColor.equalsIgnoreCase("Blue")){
            mPlayerButton.setImageResource(R.drawable.bluebg);
            mGunManButton.setImageResource(R.drawable.bluegunmanbttn);
            mToughGunButton.setImageResource(R.drawable.bluetoughguybttn);
            mModelButton.setImageResource(R.drawable.bluemodelbttn);
        }

        if (mColor.equalsIgnoreCase("Green")){
            mPlayerButton.setImageResource(R.drawable.greenbg);
            mGunManButton.setImageResource(R.drawable.greengunmanbttn);
            mToughGunButton.setImageResource(R.drawable.greentoughguybttn);
            mModelButton.setImageResource(R.drawable.greenmodelbttn);
        }

        if (mColor.equalsIgnoreCase("Brown")){
            mPlayerButton.setImageResource(R.drawable.brownbg);
            mGunManButton.setImageResource(R.drawable.browngunmanbttn);
            mToughGunButton.setImageResource(R.drawable.browntoughguybttn);
            mModelButton.setImageResource(R.drawable.brownmodelbttn);
        }

        if (mColor.equalsIgnoreCase("Black")){
            mPlayerButton.setImageResource(R.drawable.blackbg);
            mGunManButton.setImageResource(R.drawable.blackgunmanbttn);
            mToughGunButton.setImageResource(R.drawable.blacktoughguybttn);
            mModelButton.setImageResource(R.drawable.blackmodelbttn);
        }
    }

    private void updateItem() {
        for (int i=0; i<mItems.size(); i++){
           if (mItems.get(i).getItemNum()==1){
               mAllItemSlots.get(i).setImageResource(R.drawable.threat);
           } else if (mItems.get(i).getItemNum() == 2){
               mAllItemSlots.get(i).setImageResource(R.drawable.securitycamera);
           } else if (mItems.get(i).getItemNum() == 3){
               mAllItemSlots.get(i).setImageResource(R.drawable.axe);
           }else if (mItems.get(i).getItemNum() == 4){
               mAllItemSlots.get(i).setImageResource(R.drawable.shortgun);
           }else if (mItems.get(i).getItemNum() == 5){
               mAllItemSlots.get(i).setImageResource(R.drawable.hardware);
           }else if (mItems.get(i).getItemNum() == 6){
               mAllItemSlots.get(i).setImageResource(R.drawable.hidden);
           }else if (mItems.get(i).getItemNum() == 7) {
               mAllItemSlots.get(i).setImageResource(R.drawable.sprint);
           }
        }
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


        mRestRoomZombie = findViewById(R.id.restroom_zombie);
        mCachouZombie = findViewById(R.id.cachou_zombie);
        mMegatoyZombie = findViewById(R.id.megatoy_zombie);
        mParkingZombie = findViewById(R.id.parking_zombie);
        mSecurityZombie = findViewById(R.id.security_zombie);
        mSupermarketZombie = findViewById(R.id.supermarket_zombie);

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

        mRestRoomArea = findViewById(R.id.restroom_area);
        mCachouArea = findViewById(R.id.cachou_area);
        mMegatoyArea = findViewById(R.id.megatoy_area);
        mParkingArea = findViewById(R.id.parking_area);
        mSecurityArea = findViewById(R.id.security_area);
        mSupermarketArea = findViewById(R.id.supermarket_area);

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
            for (int k=0; k<roomLabel.size(); k++){
                currentPane.setColumnCount(3);
                currentPane.addView(roomLabel.get(k));
            }
        }
    }

    private void choosingRoom(int roomNumber) {
        mCountSetUp++;
        Intent data = new Intent();
        data.putExtra(CHOOSEDROOM, roomNumber);
        data.putExtra(SETUPCOUNTED, mCountSetUp );
        setResult(RESULT_OK, data);
        finish();
    }

    private void choosingCharacter(String character){
        mCountSetUp++;
        Intent data = new Intent();
        data.putExtra(CHOOSEDCHARACTER, character);
        data.putExtra(SETUPCOUNTED, mCountSetUp);
        setResult(RESULT_OK, data);
        finish();
    }

    private void choosingColor(String color) {
        mCountSetUp++;
        Intent data = new Intent();
        data.putExtra(CHOOSEDCOLOR, color);
        data.putExtra(SETUPCOUNTED, mCountSetUp);
        setResult(RESULT_OK, data);
        finish();
    }

    public static int choosedRoomNumber (Intent result){
        return result.getIntExtra(CHOOSEDROOM,0);
    }

    public static int getCountedSetUp (Intent result){
        return result.getIntExtra(SETUPCOUNTED,0);
    }

    public static String choosedCharacter (Intent result){
        return result.getStringExtra(CHOOSEDCHARACTER);
    }

    public static String votedColor(Intent result){
        return result.getStringExtra(CHOOSEDCOLOR);
    }



}
