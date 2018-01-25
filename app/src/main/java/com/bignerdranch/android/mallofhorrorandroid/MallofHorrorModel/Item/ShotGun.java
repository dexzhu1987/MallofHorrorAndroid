package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.*;


import java.util.ArrayList;
import java.util.List;

public class ShotGun extends Item implements Parcelable, java.io.Serializable  {
    public ShotGun() {
        super(4, "ShotGun");
    }

    public void effect(Playable player, Room room) {
        List<String> messeges = new ArrayList<>();
        messeges.add("ShortGun can kill two zomebie in the room");
        int orignalZombieNumber =room.getCurrentZombienumber();
        room.zombieKilled();
        room.zombieKilled();
        messeges.add("Zombies number has dropped from " + orignalZombieNumber + " to "  + room.getCurrentZombienumber());
//        MultiMessagesWindow.display(messeges,"---------------ShortGun------------------");
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected ShotGun(Parcel in) {
        super(in);
    }

    public static Item createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static Item[] newArray(int size) {
        return CREATOR.newArray(size);
    }
}
