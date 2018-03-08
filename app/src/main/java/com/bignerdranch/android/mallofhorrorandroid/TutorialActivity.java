package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class TutorialActivity extends AppCompatActivity {

    private int imageRec[] = {R.drawable.tr1,R.drawable.tr2,R.drawable.tr3,R.drawable.tr4,R.drawable.tr5,R.drawable.tr6,R.drawable.tr7,R.drawable.tr8,R.drawable.tr9,R.drawable.tr10,
            R.drawable.tr13,R.drawable.tr12,R.drawable.tr13};

    private LinearLayout linearLayout;
    private int index = 1;
    private int max = imageRec.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        linearLayout = findViewById(R.id.tr_linear);
        linearLayout.setBackgroundResource(imageRec[0]);

    }

    public void next_tr_page(View view) {
        if (index == max){
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);
        }else{
            index++;
            linearLayout.setBackgroundResource(imageRec[index -1]);
        }
    }
}
