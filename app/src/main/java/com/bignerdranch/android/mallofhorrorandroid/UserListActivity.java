package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.Game;
import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.bignerdranch.android.mallofhorrorandroid.databinding.ActivityUserListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.sephiroth.android.library.tooltip.Tooltip;

public class UserListActivity extends AppCompatActivity {
    private static final String LOG_TAG = "UserListActivity";
    private static final String TYPE = "type";
    private static final String ROOMID = "roomId";
    private static final String USERNAME = "username";
    private static final String BGMTHEMESET = "bgmthemeset";
    private static final java.lang.String ISUSERRELEASE = "isUserRelease";
    private static final String PLAYERN = "playerN";
    private static final String ISGAMESTARTED = "isgamestarted";
    private static final String PLAYERSNAMES = "playersnames";
    private static final String USERNAMEFORSAVING = "usingnameforsaving";
    private static final String ROOMIDFORSAVING = "roomIdforSaving";
    private static final String AMIREADY = "amIReady";

    private final String ROOMINFORM = "roomInform";
    private final String ISROOMREADY = "isRoomReady";
    private final String ROOMREADYARRAY = "roomReadyArray";

    private ActivityUserListBinding binding;
    private List<User> users = new ArrayList<>();
    private Adapter adapter;
    private static String roomId;
    private String username;
    private Game gameMain;
    private String type;
    private MediaPlayer waitingRoomBgm;
    private final static int MAX_VOLUME = 100;
    private Spinner mMusicStyleSpinner;
    private int mBgmThemeSet;
    private List<Integer> mBgmSources = new ArrayList<>();
    private boolean mUserIsRelease;
    private ArrayList<String> mPlayersNamesList = new ArrayList<>();
    private boolean isGameStarted;
    private int playerN;
    private boolean mAmIReady;
    private Intent mRoomService;
    private ArrayList<ValueEventListener> mEventListeners = new ArrayList<>();
    private ValueEventListener mListnerForIsRoomReady;
    private ValueEventListener mListenerForReadyArray;
    private ImageButton mReadyButton;
    private RelativeLayout mBorder1, mBorder2, mBorder3, mBorder4;
    private ArrayList<RelativeLayout> mBorders = new ArrayList<>();

    public static Intent newIntent(Context context, String type, String roomID, String username) {
        Intent intent = new Intent(context, UserListActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(ROOMID, roomID);
        intent.putExtra(USERNAME,username);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        type = getIntent().getStringExtra(TYPE);
        roomId = getIntent().getStringExtra(ROOMID);
        username = getIntent().getStringExtra(USERNAME);
        mReadyButton = binding.readyButton;
        mReadyButton.setEnabled(false);
        mBorder1 = findViewById(R.id.border_1);
        mBorder2 = findViewById(R.id.border_2);
        mBorder3 = findViewById(R.id.border_3);
        mBorder4 = findViewById(R.id.border_4);
        mBorders.add(mBorder1);
        mBorders.add(mBorder2);
        mBorders.add(mBorder3);
        mBorders.add(mBorder4);

        mBgmSources.add(R.raw.waitingroom_bgm_umeneko);
        mBgmSources.add(R.raw.waitingroom_bgm_hellgirl);
        mBgmSources.add(R.raw.waitingroom_bgm_xxxholic);
        mBgmSources.add(R.raw.waitingroom_bgm_fatalframe);

        if (savedInstanceState == null){
            Random random = new Random();
            mBgmThemeSet = random.nextInt(mBgmSources.size());
            mUserIsRelease = false;
            isGameStarted = false;
            mAmIReady = false;
        } else {
            mBgmThemeSet = savedInstanceState.getInt(BGMTHEMESET);
            mUserIsRelease = savedInstanceState.getBoolean(ISUSERRELEASE);
            isGameStarted = savedInstanceState.getBoolean(ISGAMESTARTED);
            mAmIReady = savedInstanceState.getBoolean(AMIREADY);
        }


        mMusicStyleSpinner = binding.musicStyleSpinner;
        ArrayAdapter<CharSequence> adapterMusic = ArrayAdapter.createFromResource(this,
                R.array.music_style, android.R.layout.simple_spinner_item);
        adapterMusic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMusicStyleSpinner.setAdapter(adapterMusic);
        mMusicStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    Random random = new Random();
                    mBgmThemeSet = random.nextInt(mBgmSources.size());
                    playMusic();
                }else {
                    mBgmThemeSet = position-1;
                    playMusic();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.i(LOG_TAG, "type: " + type  + " roomID: "+ roomId + " username: " + username);

        if (type.equals("Host")){
            playerN = 0;
            createRoom(roomId);
            binding.list.setVisibility(View.VISIBLE);
        } else {
            registerNameInRoom(roomId);
            binding.list.setVisibility(View.INVISIBLE);
            registerMyCurrentRoomIdAndRemovetheLastRoom();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(true);
        fetchUsers();
        adapter = new Adapter(this, users);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        updateRoom(binding, roomId);
        setUpListenningForIsRoomReady();
        playMusic();
        mUserIsRelease = false;
        if (mAmIReady){
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).
                    child(ROOMREADYARRAY).child(Integer.toString(playerN)).setValue(playerN);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i=1; i<=4; i++) {
            String player = "player" + i;
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).
                    child(ROOMINFORM).child(player).removeEventListener(mEventListeners.get(i-1));
        }
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).
                child(ISROOMREADY).removeEventListener(mListnerForIsRoomReady);
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(false);
        if (waitingRoomBgm.isPlaying()){
            waitingRoomBgm.stop();
            waitingRoomBgm.release();
            waitingRoomBgm = null;
            mUserIsRelease = true;
        }
        if (mAmIReady){
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).
                    child(ROOMREADYARRAY).child(Integer.toString(playerN)).setValue(null);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(LOG_TAG, "Stop: roomId; " + roomId);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "Destroy: roomId; " + roomId);
        _idleHandler.removeCallbacks(_idleRunnable);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BGMTHEMESET, mBgmThemeSet);
        outState.putBoolean(ISUSERRELEASE, mUserIsRelease);
        outState.putBoolean(ISGAMESTARTED,isGameStarted);
        outState.putString(USERNAMEFORSAVING, username);
        outState.putStringArrayList(PLAYERSNAMES, mPlayersNamesList);
        outState.putString(ROOMIDFORSAVING,roomId);
        outState.putBoolean(AMIREADY,mAmIReady);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mBgmThemeSet = savedInstanceState.getInt(BGMTHEMESET);
        mUserIsRelease = savedInstanceState.getBoolean(ISUSERRELEASE);
        isGameStarted = savedInstanceState.getBoolean(ISGAMESTARTED);
        username = savedInstanceState.getString(USERNAMEFORSAVING);
        mPlayersNamesList = savedInstanceState.getStringArrayList(PLAYERSNAMES);
        roomId = savedInstanceState.getString(ROOMIDFORSAVING);
        mAmIReady = savedInstanceState.getBoolean(AMIREADY);
    }

    private void registerMyCurrentRoomIdAndRemovetheLastRoom() {
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).
                child("currentRoomId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    String previousRoomId = (String) dataSnapshot.getValue();
                    if (!previousRoomId.equalsIgnoreCase(roomId)){
                        FirebaseDatabase.getInstance().getReference().child("game").child(previousRoomId).child(ROOMINFORM).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue()!=null){
                                    Game game = dataSnapshot.getValue(Game.class);
                                    ArrayList<String> previousPlayersNames = new ArrayList<>();
                                    if (game.getPlayer1()==null ||  game.getPlayer1().equalsIgnoreCase("") || game.getPlayer1().equalsIgnoreCase(username)){
                                        previousPlayersNames.add("");
                                    }else {
                                        previousPlayersNames.add(game.getPlayer1());
                                    }

                                    if (game.getPlayer2()==null||  game.getPlayer2().equalsIgnoreCase("") || game.getPlayer2().equalsIgnoreCase(username)){
                                        previousPlayersNames.add("");
                                    }else {
                                        previousPlayersNames.add(game.getPlayer2());
                                    }

                                    if (game.getPlayer3()==null||  game.getPlayer3().equalsIgnoreCase("") || game.getPlayer3().equalsIgnoreCase(username)){
                                        previousPlayersNames.add("");
                                    }else {
                                        previousPlayersNames.add(game.getPlayer3());
                                    }

                                    if (game.getPlayer4()==null||  game.getPlayer4().equalsIgnoreCase("") || game.getPlayer4().equalsIgnoreCase(username)){
                                        previousPlayersNames.add("");
                                    }else {
                                        previousPlayersNames.add(game.getPlayer4());
                                    }

                                    FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).
                                            child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue()!=null){
                                                String myName = (String) dataSnapshot.getValue();
                                                for (int i=0; i<previousPlayersNames.size(); i++){
                                                    if (previousPlayersNames.get(i).equalsIgnoreCase(myName)){
                                                        previousPlayersNames.set(i,"");
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    for (int i=0; i<previousPlayersNames.size(); i++){
                                        if (previousPlayersNames.get(i).equalsIgnoreCase("")){
                                            for (int k=i; k<previousPlayersNames.size();k++){
                                                if (!previousPlayersNames.get(k).equalsIgnoreCase("")){
                                                    String temp = previousPlayersNames.get(k);
                                                    previousPlayersNames.set(i,temp);
                                                    previousPlayersNames.set(k, "");
                                                }
                                            }
                                        }
                                    }

                                    for (int i=0; i<previousPlayersNames.size(); i++){
                                        FirebaseDatabase.getInstance().getReference().child("game").child(previousRoomId).child(ROOMINFORM).child("player"+(i+1)).setValue(previousPlayersNames.get(i));
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).
                        child("currentRoomId").setValue(roomId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpListenningForIsRoomReady() {
        mListnerForIsRoomReady = FirebaseDatabase.getInstance().getReference().child("game").child(roomId).
                child(ISROOMREADY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    boolean isRoomReady = (boolean) dataSnapshot.getValue();
                    if (isRoomReady){
                        if (!mAmIReady){
                            Toast.makeText(UserListActivity.this, "Game is about to start, click continue to be ready",Toast.LENGTH_LONG).show();
                        }
                        mReadyButton.setEnabled(true);
                        mReadyButton.setImageResource(R.drawable.ok_button);
                        mListenerForReadyArray = FirebaseDatabase.getInstance().getReference().child("game").child(roomId).
                                child(ROOMREADYARRAY).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue()!=null) {
                                    ArrayList<Integer> readyArray = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        readyArray.add(snapshot.getValue(Integer.TYPE));
                                    }
                                    for (int whoIsReady : readyArray) {
                                        mBorders.get(whoIsReady).
                                                setBackground(ContextCompat.getDrawable(UserListActivity.this, R.drawable.border_layout_green));
                                    }
                                    ArrayList<Integer> notReadyArray = new ArrayList<>();
                                    notReadyArray.add(0);
                                    notReadyArray.add(1);
                                    notReadyArray.add(2);
                                    notReadyArray.add(3);
                                    for (Integer whoIsReady : readyArray) {
                                        notReadyArray.remove(whoIsReady);
                                    }
                                    for (int notReady : notReadyArray) {
                                        mBorders.get(notReady).
                                                setBackground(ContextCompat.getDrawable(UserListActivity.this, R.drawable.border_layout));
                                    }
                                    if (readyArray.size()==4){
                                        startMainActivity();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        RemoveReadyForMain();
                    }
                } else {
                    RemoveReadyForMain();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void RemoveReadyForMain() {
        if (type.equalsIgnoreCase("Host")){
           FirebaseDatabase.getInstance().getReference().child("game").child(roomId).
                    child(ROOMREADYARRAY).setValue(null);
        }
        mAmIReady = false;
        mReadyButton.setEnabled(false);
        mReadyButton.setImageResource(R.drawable.okbttnhover);
        if (mListenerForReadyArray!=null){
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).
                    child(ROOMREADYARRAY).removeEventListener(mListenerForReadyArray);
        }
        for (RelativeLayout whoIsReady : mBorders) {
           whoIsReady.
                    setBackground(ContextCompat.getDrawable(UserListActivity.this, R.drawable.border_layout));
        }
    }

    private void playMusic() {
        if (!mUserIsRelease && waitingRoomBgm!=null){
            if (waitingRoomBgm.isPlaying()) {
                waitingRoomBgm.stop();
                waitingRoomBgm.release();
            }
        }
        waitingRoomBgm = MediaPlayer.create(UserListActivity.this, mBgmSources.get(mBgmThemeSet));
        waitingRoomBgm.start();
        waitingRoomBgm.setLooping(true);
        final float volume = (float) (1 - (Math.log(MAX_VOLUME - 70) / Math.log(MAX_VOLUME)));
        waitingRoomBgm.setVolume(volume,volume);
    }

    private void updateRoom(ActivityUserListBinding binding, String roomId) {
        mPlayersNamesList.clear();
        mPlayersNamesList.add("");
        mPlayersNamesList.add("");
        mPlayersNamesList.add("");
        mPlayersNamesList.add("");

        ArrayList<TextView> usersNames = new ArrayList<>();
        usersNames.add(binding.user1);
        usersNames.add(binding.user2);
        usersNames.add(binding.user3);
        usersNames.add(binding.user4);

        ArrayList<ImageView> usersIcon = new ArrayList<>();
        usersIcon.add(binding.redIcon);
        usersIcon.add(binding.yellowIcon);
        usersIcon.add(binding.blueIcon);
        usersIcon.add(binding.greenIcon);

        for (int i=1; i<=4; i++){
            String player = "player"+i;
            final int j = i;
            final int k = i;
            ValueEventListener listener = FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ROOMINFORM).child(player).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()!=null){
                        readNamesFromFirebase(dataSnapshot, usersNames, j, usersIcon, roomId);
                    }else{
                        ArrayList<TextView> usersNames = new ArrayList<>();
                        usersNames.add(binding.user1);
                        usersNames.add(binding.user2);
                        usersNames.add(binding.user3);
                        usersNames.add(binding.user4);
                        for (int i=0; i<usersNames.size(); i++){
                            usersNames.get(i).setText("");
                        }
                        informGuestHostHasLeftAndRoomNull();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mEventListeners.add(listener);
        }

    }

    private void readNamesFromFirebase(DataSnapshot dataSnapshot, ArrayList<TextView> usersNames, int j, ArrayList<ImageView> usersIcon, String roomId) {
        if (dataSnapshot.getValue()==username){
            usersNames.get(j-1).setText(dataSnapshot.getValue() + " (Me)");
            usersIcon.get(j-1).setVisibility(View.VISIBLE);
            mPlayersNamesList.set(j-1,(String) dataSnapshot.getValue());
            return;
        } else {
            usersNames.get(j-1).setText((String) dataSnapshot.getValue());
            usersIcon.get(j-1).setVisibility(View.VISIBLE);
            mPlayersNamesList.set(j-1,(String) dataSnapshot.getValue());
        }

        for (int i = 0; i < usersNames.size(); i++){
            if (usersNames.get(i).getText().toString().equals("")){
                usersIcon.get(i).setVisibility(View.INVISIBLE);
            }
        }

        for (int i=0; i<usersNames.size(); i++){
            if (usersNames.get(i).getText().toString().equals("")){
                FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ISROOMREADY).setValue(false);
                break;
            } else {
                if (i==3){
//                    startMainActivity();
                    if (type.equalsIgnoreCase("Host")){
                        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ISROOMREADY).setValue(true);
                    }
                }
            }
        }

        if(!type.equalsIgnoreCase("Host")&& !isGameStarted){
            mRoomService = RoomService.newRoomServiceIntent(UserListActivity.this, roomId, mPlayersNamesList);
            startService(mRoomService);
        }
    }

    private void informGuestHostHasLeftAndRoomNull() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserListActivity.this);
        if(!UserListActivity.this.isFinishing() && !type.equalsIgnoreCase("Host")){
            ActivityUserListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list);
            for (int i=1; i<=4; i++) {
                String player = "player" + i;
                FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ROOMINFORM).child(player).removeEventListener(mEventListeners.get(i-1));
            }
            builder.setTitle("Room has been cleared");
            builder.setMessage("Looks like the game owner has left the room");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    restartGame();
                }
            });
            AlertDialog mAlertDialog = builder.create();
            mAlertDialog.show();
        }
    }

    private void createRoom(String roomId) {
        gameMain = new Game(roomId);
        Log.i(LOG_TAG,"Create Room: " + roomId + " : " );
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ISROOMREADY).setValue(false);
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ROOMINFORM).setValue(gameMain);
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ROOMINFORM).child("player1").setValue(username);
    }

    private void registerNameInRoom(String roomId) {
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ROOMINFORM).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameMain = dataSnapshot.getValue(Game.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("game")
                .child(roomId).child(ROOMINFORM).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    Game game = dataSnapshot.getValue(Game.class);
                    Log.i(LOG_TAG, game.toString() + " ");
                    ArrayList<String> players = new ArrayList();
                    players.add(game.getPlayer2());
                    players.add(game.getPlayer3());
                    players.add(game.getPlayer4());
                    for (int i=0, q=2; i<players.size(); i++,q++){
                        if (players.get(i).equals(username)){
                            return;
                        } else {
                            if (players.get(i).equals("")){
                                FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ROOMINFORM).child("player"+q).setValue(username);
                                playerN = q-1;
//                                if (q==4){
//                                    startMainActivity();
//                                }
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void set_ready_to_main(View view) {
        if (!mAmIReady){
            mAmIReady = true;
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).
                    child(ROOMREADYARRAY).child(Integer.toString(playerN)).setValue(playerN);
        }else {
            Toast.makeText(UserListActivity.this, "You ready have been set, please wait for other players",Toast.LENGTH_LONG)
            .show();
        }
    }

    private void startMainActivity() {
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(false);
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(ROOMINFORM).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameMain = dataSnapshot.getValue(Game.class);
                isGameStarted = true;
                stopService(mRoomService);
                FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).
                        child("currentRoomId").setValue(null);
                Intent intent = MainActivity.mainIntent(UserListActivity.this,4, gameMain, username, type,mBgmThemeSet);
                _idleHandler.removeCallbacksAndMessages(null);
                Log.i(LOG_TAG, "start main activity when reached 4 players");
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchUsers() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        users.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (!snapshot.getKey().equals(User.getCurrentUserId())&&user.isOn()) {
                                users.add(user);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
        delayedIdle(5);
    }

    Handler _idleHandler = new Handler();
    Runnable _idleRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(LOG_TAG, "set the data null due to inactivy");
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).setValue(null);
            if (!UserListActivity.this.isFinishing() && type.equalsIgnoreCase("Host")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserListActivity.this);
                builder.setTitle("Idle Room");
                builder.setMessage("Due to no activity, your room has been cleared, please log in again :)");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restartGame();
                    }
                });
                AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();
            }
        }
    };

    private void restartGame() {
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("name").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String name = (String) dataSnapshot.getValue();
                        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).
                                child("pushId").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String roomID = (String) dataSnapshot.getValue();
                                String type = "Host";
                                Intent intent = UserListActivity.newIntent(UserListActivity.this, type, roomID,name);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void delayedIdle(int delayMinutes) {
        _idleHandler.removeCallbacks(_idleRunnable);
        _idleHandler.postDelayed(_idleRunnable,(delayMinutes * 1000 * 60));
    }

}
