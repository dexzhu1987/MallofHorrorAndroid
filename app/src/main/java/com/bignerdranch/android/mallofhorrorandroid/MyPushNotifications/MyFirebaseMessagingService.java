package com.bignerdranch.android.mallofhorrorandroid.MyPushNotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.bignerdranch.android.mallofhorrorandroid.FirstActivity;
import com.bignerdranch.android.mallofhorrorandroid.R;
import com.bignerdranch.android.mallofhorrorandroid.UserListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = "MyFirebaseMessaging";
    public static final String INVITE = "invite";
    private Context mContext;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        mContext = MyFirebaseMessagingService.this;

        String fromPushId = remoteMessage.getData().get("fromPushId");
        String fromId = remoteMessage.getData().get("fromId");
        String fromName = remoteMessage.getData().get("fromName");
        String type = remoteMessage.getData().get("type");
        Log.d(LOG_TAG, "onMessageReceived: " + remoteMessage.getData());

        if (type.equals("invite")) {
            handleInviteIntent(fromPushId, fromId, fromName);
        } else if (type.equals("accept")) {
            Toast.makeText(this, fromName + " joined your room ",Toast.LENGTH_LONG);
        } else if (type.equals("reject")) {
            // todo update to Oreo notifications
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setPriority(PRIORITY_MAX)
                            .setContentTitle(String.format("%s rejected your invite!", fromName));


            FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("name").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = (String) dataSnapshot.getValue();
                            String type = "Host";
                            Intent resultIntent = UserListActivity.newIntent(mContext,type,fromPushId,name);

                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
                            stackBuilder.addParentStack(FirstActivity.class);
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                            0,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            mBuilder.setContentIntent(resultPendingIntent);
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(1, mBuilder.build());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



        }
    }

    private void handleInviteIntent(String fromPushId, String fromId, String fromName) {
        Intent rejectIntent = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("reject")
                .putExtra("withId", fromId)
                .putExtra("to", fromPushId);

        PendingIntent pendingIntentReject = PendingIntent.getBroadcast(this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent acceptIntent = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("accept")
                .putExtra("withId", fromId)
                .putExtra("to", fromPushId);
        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(this, 2, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("name").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = (String) dataSnapshot.getValue();
                        String type = "Host";
                        Intent resultIntent = UserListActivity.newIntent(mContext,type,fromPushId,name);

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
                        stackBuilder.addParentStack(FirstActivity.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent =
                                stackBuilder.getPendingIntent(
                                        0,
                                        PendingIntent.FLAG_UPDATE_CURRENT);

                        android.app.Notification build = new NotificationCompat.Builder(mContext, INVITE)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setPriority(PRIORITY_MAX)
                                .setContentTitle(String.format("%s invites you to play!", fromName))
                                .addAction(R.drawable.accept, "Accept", pendingIntentAccept)
                                .setVibrate(new long[3000])
                                .setChannelId(INVITE)
                                .setContentIntent(resultPendingIntent)
                                .addAction(R.drawable.cancel, "Reject", pendingIntentReject)
                                .build();

                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (notificationManager == null) {
                            return;
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = new NotificationChannel(INVITE, INVITE, importance);
                            notificationManager.createNotificationChannel(mChannel);
                        }

                        notificationManager.notify(1, build);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
