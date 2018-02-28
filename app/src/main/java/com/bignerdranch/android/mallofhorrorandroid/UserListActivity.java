package com.bignerdranch.android.mallofhorrorandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.Game;
import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.bignerdranch.android.mallofhorrorandroid.databinding.ActivityUserListBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class UserListActivity extends AppCompatActivity {
    private static final String LOG_TAG = "UserListActivity";
    private static final String TYPE = "type";
    private static final String ROOMID = "roomId";
    private static final String USERNAME = "username";
    private List<User> users = new ArrayList<>();
    private Adapter adapter;
    private Context userActivity;
    private String roomId;
    private String username;
    private Game gameMain;
    private String type;

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

        Log.i(LOG_TAG, "type: " + type  + " roomID: "+ roomId + " username: " + username);

        adapter = new Adapter(this, users);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(this));

        fetchUsers();

        if (type.equals("Host")){
            createRoom(roomId);
        } else {
            registerNameInRoom(roomId);
        }

        updateRoom(binding, roomId);


    }

    private void updateRoom(ActivityUserListBinding binding, String roomId) {
        ArrayList<TextView> usersNames = new ArrayList<>();
        usersNames.add(binding.user1);
        usersNames.add(binding.user2);
        usersNames.add(binding.user3);
        usersNames.add(binding.user4);
        for (int i=1; i<=4; i++){
            String player = "player"+i;
            final int j = i;
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child(player).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()==username){
                        usersNames.get(j-1).setText(dataSnapshot.getValue() + " (Me)");
                        return;
                    } else {
                        usersNames.get(j-1).setText((String) dataSnapshot.getValue());
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
                                        Intent intent = MainActivity.mainIntent(UserListActivity.this,4, gameMain, username);
                                        Log.i(LOG_TAG, "start main activity when reached 4 players");
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

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
                Game game = dataSnapshot.getValue(Game.class);
                Log.i(LOG_TAG, game.toString() + "");
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
                            return;
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
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (!snapshot.getKey().equals(User.getCurrentUserId())) {
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
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(true);
        if (type.equals("Host")){
            createRoom(roomId);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (type.equals("Host")) {
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).setValue(null);
            Log.i(LOG_TAG, "deleting data when leaving the room");
        }
    }



    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
