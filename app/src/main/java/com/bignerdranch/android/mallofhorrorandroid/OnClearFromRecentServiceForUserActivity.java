package com.bignerdranch.android.mallofhorrorandroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dexunzhu on 2018-03-01.
 */

public class OnClearFromRecentServiceForUserActivity extends Service {
    private static String ROOMID = "roomId";
    private String roomId;

    public static Intent newServiceIntent(Context context, String roomID){
        Intent intent = new Intent(context, OnClearFromRecentService.class);
        intent.putExtra(ROOMID, roomID);
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
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(false);
        Log.e("ClearFromRecentService", " " + User.getCurrentUserId());
        stopSelf();
    }
}
