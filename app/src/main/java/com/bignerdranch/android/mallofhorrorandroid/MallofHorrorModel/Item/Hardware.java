package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Game.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.*;


import java.util.ArrayList;
import java.util.List;

public class Hardware extends Item implements Parcelable, java.io.Serializable  {
    public Hardware() {
        super(5, "Hareware");
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Hardware(Parcel in) {
        super(in);
    }

    public static Item createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static Item[] newArray(int size) {
        return CREATOR.newArray(size);
    }

    public void effect(Playable player, Room room) {
        List <String> messages = new ArrayList<>();
        messages.add("The defend of this room has temporarily increased by 1");
        room.zombieKilled();
        messages.add("A zomie has temporarly moved to somewhere else");
        afteraffectedRoomNumber = room.getRoomNum();
//        MultiMessagesWindow.display(messages,"-----------------Hareware--------------------");
    }

    public void afterEffect(GameBroad gameBroad){
        List <String> messages = new ArrayList<>();
        messages.add("Due to Hareware has been used, aftereffect is triggerd");
        gameBroad.matchRoom(afteraffectedRoomNumber).zombieApproached();
        messages.add("The zombie left has returned");
//        MultiMessagesWindow.display(messages,"---------------Hareware----------------------");
    }


}
