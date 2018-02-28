package com.bignerdranch.android.mallofhorrorandroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.Service.START_NOT_STICKY;

/**
 * Created by dexunzhu on 2018-02-28.
 */

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
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
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("on").setValue(false);
        Log.e("ClearFromRecentService", " " + User.getCurrentUserId());
        stopSelf();
    }
}
