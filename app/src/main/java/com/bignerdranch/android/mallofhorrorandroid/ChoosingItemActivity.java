package com.bignerdranch.android.mallofhorrorandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Axe;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Hardware;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Hidden;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Item;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.SecurityCamera;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.ShotGun;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Sprint;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.Threat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoosingItemActivity extends AppCompatActivity {
    private static final String ITEMOPTIONS = "itemoptions";
    private static final String COUNTSETUP = "countsetup";
    private static final String MESSAGE = "message";

    private static final String SETUPCOUNTED = "setupcounted";
    private static final String CHOOSEDITEM = "chooseditem";
    private static final String REMAINITEMS = "remainitems";

    private ImageButton mImageSelected1, mImageSelected2, mImageSelected3;
    private TextView mItemChoosingTextView;

    private ArrayList<Item> mItems;
    private List<ImageButton> mAllItemsImageButtons = new ArrayList<>();

    private String mMessage;
    private int mCountSetUp;

    public static Intent newChoosingItemIntent(Context context, List<Item> items, String message, int countsetup){
        Intent intent = new Intent(context, ChoosingItemActivity.class);
        intent.putExtra(MESSAGE,message);
        intent.putParcelableArrayListExtra(ITEMOPTIONS,(ArrayList<? extends Parcelable>) items);
        intent.putExtra(COUNTSETUP, countsetup);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_item);

        mItems = getIntent().getParcelableArrayListExtra(ITEMOPTIONS);
        mMessage = getIntent().getStringExtra(MESSAGE);
        mCountSetUp = getIntent().getIntExtra(COUNTSETUP,0);

        mImageSelected1 = findViewById(R.id.itemselected_one);
        mImageSelected2 = findViewById(R.id.itemselected_two);
        mImageSelected3 = findViewById(R.id.itemselected_three);
        mAllItemsImageButtons.add(mImageSelected1);
        mAllItemsImageButtons.add(mImageSelected2);
        mAllItemsImageButtons.add(mImageSelected3);

        mItemChoosingTextView = findViewById(R.id.choosingItemTextView);
        mItemChoosingTextView.setText(mMessage);


        for (int i=0; i<mAllItemsImageButtons.size(); i++){
            mAllItemsImageButtons.get(i).setVisibility(View.INVISIBLE);
            mAllItemsImageButtons.get(i).setEnabled(true);
        }


        for (int i=0; i<mItems.size(); i++){
          mAllItemsImageButtons.get(i).setVisibility(View.VISIBLE);
          mAllItemsImageButtons.get(i).setEnabled(true);
        }


        for (int i=0; i<mItems.size(); i++){
            if (mItems.get(i).getItemNum()==1){
                mAllItemsImageButtons.get(i).setImageResource(R.drawable.threat);
                choosedItem(i, mAllItemsImageButtons.get(i), new Threat());
            } else if (mItems.get(i).getItemNum() == 2){
                mAllItemsImageButtons.get(i).setImageResource(R.drawable.securitycamera);
                choosedItem(i,mAllItemsImageButtons.get(i), new SecurityCamera());
            } else if (mItems.get(i).getItemNum() == 3){
                mAllItemsImageButtons.get(i).setImageResource(R.drawable.axe);
                choosedItem(i, mAllItemsImageButtons.get(i), new Axe());
            }else if (mItems.get(i).getItemNum() == 4){
                mAllItemsImageButtons.get(i).setImageResource(R.drawable.shortgun);
                choosedItem(i, mAllItemsImageButtons.get(i), new ShotGun());
            }else if (mItems.get(i).getItemNum() == 5){
                mAllItemsImageButtons.get(i).setImageResource(R.drawable.hardware);
                choosedItem(i, mAllItemsImageButtons.get(i), new Hardware());
            }else if (mItems.get(i).getItemNum() == 6){
                mAllItemsImageButtons.get(i).setImageResource(R.drawable.hidden);
                choosedItem(i, mAllItemsImageButtons.get(i), new Hidden());
            }else if (mItems.get(i).getItemNum() == 7) {
                mAllItemsImageButtons.get(i).setImageResource(R.drawable.sprint);
                choosedItem(i, mAllItemsImageButtons.get(i), new Sprint());
            }
        }

    }

    private void choosedItem(final int index, ImageButton imageButton, final Item threat) {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++mCountSetUp;
                mItems.remove(index);
                Intent data = new Intent();
                data.putExtra(CHOOSEDITEM, (Serializable) threat);
                data.putExtra(SETUPCOUNTED, mCountSetUp);
                data.putParcelableArrayListExtra(REMAINITEMS, mItems);
                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_grow_fade_in_from_bottom,android.support.v7.appcompat.R.anim.abc_shrink_fade_out_from_bottom );
            }
        });
    }

    public static Item choosedItem (Intent result){
        return (Item) result.getSerializableExtra(CHOOSEDITEM);
    }

    public static int getCountedSetUp (Intent result){
        return result.getIntExtra(SETUPCOUNTED,0);
    }

    public static ArrayList<Item> getRemainedItem (Intent result){
        return result.getParcelableArrayListExtra(REMAINITEMS);
    }
}
