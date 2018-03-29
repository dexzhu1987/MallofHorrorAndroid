package com.bignerdranch.android.mallofhorrorandroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.Service.START_NOT_STICKY;

/**
 * Created by dexunzhu on 2018-02-28.
 */

public class OnClearFromRecentService extends Service {
    private static String ROOMID = "roomId";
    private static String ROOMIDSELF = "roomIdSelf";
    private String roomId;
    private String roomIdSelf;

    public static Intent newServiceIntent(Context context, String roomID, String roomIdSelf){
        Intent intent = new Intent(context, OnClearFromRecentService.class);
        intent.putExtra(ROOMID, roomID);
        intent.putExtra(ROOMIDSELF, roomIdSelf);
        return intent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        roomId = intent.getStringExtra(ROOMID);
        roomIdSelf = intent.getStringExtra(ROOMIDSELF);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        FirebaseDatabase.getInstance().getReference().child("game").child(roomId).setValue(null);
        FirebaseDatabase.getInstance().getReference().child("game").child(roomIdSelf).setValue(null);
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(false);
        Log.e("ClearFromRecentService", " " + User.getCurrentUserId());
        stopSelf();
    }
}
