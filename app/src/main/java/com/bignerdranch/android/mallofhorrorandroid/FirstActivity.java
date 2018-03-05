package com.bignerdranch.android.mallofhorrorandroid;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.databinding.DataBindingUtil;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import com.bignerdranch.android.mallofhorrorandroid.databinding.ActivityFirstBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.bignerdranch.android.mallofhorrorandroid.FireBaseModel.User.*;


public class FirstActivity extends AppCompatActivity {
    private static final String LOG_TAG = "StartActivity";
    private ImageButton mPlayButton;
    private ImageButton mHowToPlayButton;
    private ImageButton mSettingButton;
    private ActivityFirstBinding binding;
    private boolean loggedIn;
    private String roomID;
    private Context firstActivity ;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        mPlayButton = findViewById(R.id.multiplayer_button);
        mPlayButton.startAnimation(animTranslate);

        mHowToPlayButton = findViewById(R.id.how_to_play_button);
        mSettingButton = findViewById(R.id.btn_setting);

//        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_grow_fade_in_from_bottom,android.support.v7.appcompat.R.anim.abc_shrink_fade_out_from_bottom );


        firstActivity = FirstActivity.this;


//        mPlayButton = findViewById(R.id.play_button);
//        mPlayButton.startAnimation(animTranslate);
//        mPlayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =  new Intent(FirstActivity.this, SetPlayerNumberActivity.class);
//                startActivity(intent);
//                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_grow_fade_in_from_bottom,android.support.v7.appcompat.R.anim.abc_shrink_fade_out_from_bottom );
//            }
//        });

//        mHowToPlayButton = findViewById(R.id.how_to_play_button);
//        mHowToPlayButton.startAnimation(animTranslate);
//        mHowToPlayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    public void startMultilayer(View view){

        if (!arePlayServicesOk()) {
            return;
        }
        if (isAnonymous()) {
            if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
//                final TextView nameInput = new EditText(FirstActivity.this);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                );
//                nameInput.setLayoutParams(params);
//                nameInput.setText("For Xiaomi User, please got to Setting--Permission--Enable auto start to ensure the app will be running properly. Thanks");

                AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                builder.setTitle("Important notice");
                builder.setMessage("For Xiaomi User, please got to Setting--Permission--Enable auto start to ensure the app will be running properly. Thanks");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();
            }
            binding.inputEmail.setVisibility(VISIBLE);
            binding.inputName.setVisibility(VISIBLE);
            binding.login.setVisibility(VISIBLE);
            binding.inputPassword.setVisibility(VISIBLE);
        } else {
            Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_bounce);
            mPlayButton.startAnimation(animTranslate);
            startUserListActivity();
        }
    }

    public void loginWithEmail(View view) {
        String email = binding.inputEmail.getText().toString();
        String name = binding.inputName.getText().toString();
        String password = binding.inputPassword.getText().toString();
        if (loggedIn) {
            return;
        }
        if (isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter correct email", LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(name)) {
            Toast.makeText(this, "Enter correct name", LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password should have at least 6 characters", LENGTH_SHORT).show();
            return;
        }

        loggedIn = true;
        showProgressDialog();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "loginWithEmail: ");
                        String uid = auth.getCurrentUser().getUid();

                        User user = new User(name);
                        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                                .setValue(user);
                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                        savePushToken(refreshedToken, uid);
                        Log.i(LOG_TAG, "ReshsedToken: " + refreshedToken + "| UID: " +uid);
                        startUserListActivity();
                    } else {
                        Log.d(LOG_TAG, "loginWithEmail: unsuccessful");
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task1 -> {
                                    if (!isAnonymous()) {
                                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                                        savePushToken(refreshedToken, getCurrentUserId());
                                        System.out.println("ReshsedToken: " + refreshedToken + "| UID: " +getCurrentUserId());
                                        startUserListActivity();
                                    }
                                });
                    }
                });
    }

    private void startUserListActivity() {
        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).child("name").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i(LOG_TAG, "login with email");
                        final String name = (String) dataSnapshot.getValue();
                        FirebaseDatabase.getInstance().getReference().child("users").child(User.getCurrentUserId()).
                                child("pushId").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                roomID = (String) dataSnapshot.getValue();
                                String type = "Host";
                                Intent intent = UserListActivity.newIntent(firstActivity, type, roomID,name);
                                Intent serviceintent = OnClearFromRecentService.newServiceIntent(FirstActivity.this, roomID);
                                startService(serviceintent);
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

    private void showProgressDialog() {
        binding.progress.setVisibility(VISIBLE);
    }


    private boolean isAnonymous() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser == null || currentUser.isAnonymous();
    }

    private boolean arePlayServicesOk() {
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        final int resultCode = googleAPI.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog(this, resultCode, 5000).show();
            }
            return false;
        }
        return true;
    }

    public void start_how_to_play(View view) {
        Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_bounce);
        mHowToPlayButton.startAnimation(animTranslate);
    }

    public void start_settings(View view) {
        Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_bounce);
        mSettingButton.startAnimation(animTranslate);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.setting_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        SeekBar mSoundBar = alertLayout.findViewById(R.id.sound_seekbar);
//        TextView mSeekBar_Value = alertLayout.findViewById(R.id.seekbar_value);
//        mSeekBar_Value.setText("0");

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSoundBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        mSoundBar.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));

        mSoundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        alert.setView(alertLayout);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
}
