package com.bignerdranch.android.mallofhorrorandroid.MyPushNotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.bignerdranch.android.mallofhorrorandroid.FirstActivity;
import com.bignerdranch.android.mallofhorrorandroid.OnClearFromRecentService;
import com.bignerdranch.android.mallofhorrorandroid.OnClearFromRecentServiceForUserActivity;
import com.bignerdranch.android.mallofhorrorandroid.UserListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MyReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive: " + intent.getAction() + " Data: " + intent.getStringExtra("to") + " Data: " + intent.getStringExtra("withId"));
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(User.getCurrentUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User me = dataSnapshot.getValue(User.class);

                        OkHttpClient client = new OkHttpClient();

                        String to = intent.getExtras().getString("to");

                        String format = String
                                .format("https://us-central1-mallofhorrorandroid.cloudfunctions.net/sendNotification?to=%s&fromPushId=%s&fromId=%s&fromName=%s&type=%s", to, me.getPushId(), User.getCurrentUserId(), me.getName(), intent.getAction());

                        Log.d(LOG_TAG, "onDataChange: " + format);
                        Request request = new Request.Builder()
                                .url(format)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                            }
                        });

                        if (intent.getAction().equals("accept")) {
                            String ownername =  me.getName();
                            String roomID = intent.getStringExtra("to");
                            Intent serviceintent = OnClearFromRecentServiceForUserActivity.newServiceIntent(context, roomID);
                            context.startService(serviceintent);
                            context.startActivity(UserListActivity.newIntent(context,"Guest",roomID,ownername));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
