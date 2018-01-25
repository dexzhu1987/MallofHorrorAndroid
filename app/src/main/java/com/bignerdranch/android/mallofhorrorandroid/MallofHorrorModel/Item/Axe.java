package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;



import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.*;

import java.util.ArrayList;
import java.util.List;

public class Axe extends Item implements Parcelable, java.io.Serializable  {
    public Axe() {
        super(3, "Axe");
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Axe(Parcel in) {
        super(in);
    }

    public static Item createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static Item[] newArray(int size) {
        return CREATOR.newArray(size);
    }

    @Override
    public void effect(Playable player, Room room) {
        List<String> messages = new ArrayList<>();
        messages.add("Axe can kill one zomebie in the room");
        int orignalZombieNumber =room.getCurrentZombienumber();
        room.zombieKilled();
        messages.add("Zombies number has dropped from " + orignalZombieNumber + " to "  + room.getCurrentZombienumber());
//        MultiMessagesWindow.display(messages,  "-------------Axe-----------");
    }
}
