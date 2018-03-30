package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Opening_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);
        textView.startAnimation(animation);


        Thread timer = new Thread(){

            @Override
            public void run() {

                try {
                    sleep(5000);
                    Intent intent = new Intent(getApplicationContext(),FirstActivity.class);
                    startActivity(intent);
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.start();
    }
}
