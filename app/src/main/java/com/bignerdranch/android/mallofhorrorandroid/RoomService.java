package com.bignerdranch.android.mallofhorrorandroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Dexter on 2018-03-28.
 */

public class RoomService extends Service {
    private static String ROOMSERVICEROOMID = "roomId";
    private static String PLAYERSNAMES = "playersnames";
    private String roomId;
    private ArrayList<String> playersnames;

    public static Intent newRoomServiceIntent(Context context, String roomID, ArrayList<String> playersnames){
        Intent intent = new Intent(context, RoomService.class);
        intent.putExtra(ROOMSERVICEROOMID, roomID);
        intent.putStringArrayListExtra(PLAYERSNAMES, playersnames);
        return intent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        roomId = intent.getStringExtra(ROOMSERVICEROOMID);
        playersnames = intent.getStringArrayListExtra(PLAYERSNAMES);
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).
                child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    String myName = (String) dataSnapshot.getValue();
                    for (int i=0; i<playersnames.size(); i++){
                        if (playersnames.get(i).equalsIgnoreCase(myName)){
                            Log.d("RoomService", "setting my name to blank");
                            playersnames.set(i,"");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("RoomService", "Service Started, roomId: " + roomId + ", playersnames:" + playersnames);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("RoomService", "Service Destroyed, roomId: " + roomId + ", playersnames:" + playersnames);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        for (int i=0; i<playersnames.size(); i++){
            if (playersnames.get(i).equalsIgnoreCase("")){
                for (int k=i; k<playersnames.size();k++){
                    if (!playersnames.get(k).equalsIgnoreCase("")){
                        String temp = playersnames.get(k);
                        playersnames.set(i,temp);
                        playersnames.set(k, "");
                        Log.d("RoomService", "setting blank name to move forward");
                    }
                }
            }
        }
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).
                child("currentRoomId").setValue(null);
        for (int i=0; i<playersnames.size(); i++){
            FirebaseDatabase.getInstance().getReference().child("game").child(roomId).child("roomInform").child("player"+(i+1)).setValue(playersnames.get(i));
        }
        Log.d("RoomService", "Service End, roomId: " + roomId + ", playersnames:" + playersnames);
        stopSelf();
    }
}
