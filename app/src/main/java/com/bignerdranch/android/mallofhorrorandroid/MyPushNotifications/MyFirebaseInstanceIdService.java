package com.bignerdranch.android.mallofhorrorandroid.MyPushNotifications;

import android.util.Log;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;




public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String LOG_TAG = "MyFirebaseInstanceId";
    @Override
    public void onTokenRefresh() {
        Log.d(LOG_TAG, "onTokenRefresh: ");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            return;
        }

        User.savePushToken(refreshedToken, currentUser.getUid());
    }
}
