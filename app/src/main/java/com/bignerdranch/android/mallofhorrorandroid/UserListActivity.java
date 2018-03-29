package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class UserListActivity extends AppCompatActivity {
    private static final String LOG_TAG = "UserListActivity";
    private static final String TYPE = "type";
    private static final String ROOMID = "roomId";
    private static final String USERNAME = "username";
    private static final String BGMTHEMESET = "bgmthemeset";
    private static final java.lang.String ISUSERRELEASE = "isUserRelease";
    private static final String PLAYERN = "playerN";
    private static final String ISGAMESTARTED = "isgamestarted";
    private static final String ISSERVICESTARTED = "isservicestarted";
    private List<User> users = new ArrayList<>();
    private Adapter adapter;
    private Context userActivity;
    private static String roomId;
    private String username;
    private Game gameMain;
    private String type;
    private boolean isStarted;
    private MediaPlayer waitingRoomBgm;
    private final static int MAX_VOLUME = 100;
    private Spinner mMusicStyleSpinner;
    private int mBgmThemeSet;
    private List<Integer> mBgmSources = new ArrayList<>();
    private boolean mUserIsRelease;
    private ArrayList<String> mPlayersNamesList = new ArrayList<>();
    private boolean isGameStarted;
    private Intent mRoomService;
    private boolean isServiceCreate;

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

        ActivityUserListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(true);
        type = getIntent().getStringExtra(TYPE);
        roomId = getIntent().getStringExtra(ROOMID);
        username = getIntent().getStringExtra(USERNAME);
        userActivity = UserListActivity.this;
        isStarted = false;


        mBgmSources.add(R.raw.waitingroom_bgm_umeneko);
        mBgmSources.add(R.raw.waitingroom_bgm_hellgirl);
        mBgmSources.add(R.raw.waitingroom_bgm_xxxholic);
        mBgmSources.add(R.raw.waitingroom_bgm_fatalframe);

        if (savedInstanceState == null){
            Random random = new Random();
            mBgmThemeSet = random.nextInt(mBgmSources.size());
            mUserIsRelease = false;
            isGameStarted = false;
            isServiceCreate = false;
        } else {
            mBgmThemeSet = savedInstanceState.getInt(BGMTHEMESET);
            mUserIsRelease = savedInstanceState.getBoolean(ISUSERRELEASE);
            isGameStarted = savedInstanceState.getBoolean(ISGAMESTARTED);
            isServiceCreate = savedInstanceState.getBoolean(ISGAMESTARTED);
        }

        mMusicStyleSpinner = findViewById(R.id.music_style_spinner);
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
                Random random = new Random();
                mBgmThemeSet = random.nextInt(mBgmSources.size());
            }
        });

        Log.i(LOG_TAG, "type: " + type  + " roomID: "+ roomId + " username: " + username);
        adapter = new Adapter(this, users);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(this));

        fetchUsers();

        if (type.equals("Host")){
            createRoom(roomId);
            binding.list.setVisibility(View.VISIBLE);
        } else {
            registerNameInRoom(roomId);
            binding.list.setVisibility(View.INVISIBLE);
        }

        updateRoom(binding, roomId);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(true);
//        if (type.equals("Host")){
//            createRoom(roomId);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        playMusic();
        mUserIsRelease = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(false);
        if (waitingRoomBgm.isPlaying()){
            waitingRoomBgm.stop();
            waitingRoomBgm.release();
            waitingRoomBgm = null;
            mUserIsRelease = true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "Destroy: roomId; " + roomId);
        _idleHandler.removeCallbacks(_idleRunnable);
        if (!isGameStarted && isServiceCreate){
            stopService(mRoomService);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(LOG_TAG, "Stop: roomId; " + roomId);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BGMTHEMESET, mBgmThemeSet);
        outState.putBoolean(ISUSERRELEASE, mUserIsRelease);
        outState.putBoolean(ISGAMESTARTED,isGameStarted);
        outState.putBoolean(ISSERVICESTARTED,isServiceCreate);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mBgmThemeSet = savedInstanceState.getInt(BGMTHEMESET);
        mUserIsRelease = savedInstanceState.getBoolean(ISUSERRELEASE);
        isGameStarted = savedInstanceState.getBoolean(ISGAMESTARTED);
        isServiceCreate = savedInstanceState.getBoolean(ISSERVICESTARTED);
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
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(player).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
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
                            break;
                        } else {
                            if (i==3){
                                FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(false);
                                FirebaseDatabase.getInstance().getReference().child("game").child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        gameMain = dataSnapshot.getValue(Game.class);
                                        isGameStarted = true;
                                        Intent intent = MainActivity.mainIntent(UserListActivity.this,4, gameMain, username, type, mBgmThemeSet);
                                        Log.i(LOG_TAG, "start main activity when reached 4 players");
                                        _idleHandler.removeCallbacksAndMessages(null);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!type.equalsIgnoreCase("Host")&& !isGameStarted && intheRoom(mPlayersNamesList)){
                                mRoomService = RoomService.newRoomServiceIntent(UserListActivity.this, roomId, mPlayersNamesList);
                                startService(mRoomService);
                            }
                        }
                    },1000);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private boolean intheRoom(ArrayList<String> names){
        boolean isInTheRoom = false;
        if (username!=null){
            for (int i=0; i<names.size(); i++){
                if (names.get(i).equalsIgnoreCase(username)){
                    return true;
                }
            }
        }
        return isInTheRoom;
    }

    private void createRoom(String roomId) {
        gameMain = new Game(roomId);
        Log.i(LOG_TAG,"Create Room: " + roomId + " : " );
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).setValue(gameMain);
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child("player1").setValue(username);
    }

    private void registerNameInRoom(String roomId) {
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameMain = dataSnapshot.getValue(Game.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("game")
                .child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child("player"+q).setValue(username);
                                if (q==4){
                                    FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(false);
                                    FirebaseDatabase.getInstance().getReference().child("game").child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            gameMain = dataSnapshot.getValue(Game.class);
                                            isGameStarted = true;
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
            if (!UserListActivity.this.isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserListActivity.this);
                builder.setTitle("Idle Room");
                builder.setMessage("Due to no activity, your room has been cleared, please log in again :)");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();
            }
        }
    };

    private void delayedIdle(int delayMinutes) {
        _idleHandler.removeCallbacks(_idleRunnable);
        _idleHandler.postDelayed(_idleRunnable,(delayMinutes * 1000 * 60));
    }

}
