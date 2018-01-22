package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Game.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public abstract class Item implements Parcelable {
    protected int itemNum;
    protected String name;
    protected int affectedRoomNumber;
    protected GameCharacter affectedGameCharacter;

    public Item(int itemNum, String name) {
        this.itemNum = itemNum;
        this.name = name;
        affectedRoomNumber = 0;
        affectedGameCharacter = new ToughGuy();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemNum);
        dest.writeInt(affectedRoomNumber);
        dest.writeString(name);
        dest.writeSerializable(affectedGameCharacter);
    }

    protected Item (final Parcel in){
        itemNum = in.readInt();
        affectedRoomNumber = in.readInt();
        name = in.readString();
        affectedGameCharacter = (GameCharacter) in.readSerializable();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source) {
            };
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAffectedRoomNumber() {
        return affectedRoomNumber;
    }

    public void setAffectedRoomNumber(int affectedRoomNumber) {
        this.affectedRoomNumber = affectedRoomNumber;
    }

    public GameCharacter getGameCharacter() {
        return affectedGameCharacter;
    }

    public void setGameCharacter(GameCharacter gameCharacter) {
        this.affectedGameCharacter = gameCharacter;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return this.name.equalsIgnoreCase(item.name);
    }

    @Override
    public int hashCode() {
        int result = itemNum;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + affectedRoomNumber;
        result = 31 * result + (affectedGameCharacter != null ? affectedGameCharacter.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name ;
    }

    /**
     * the effect that once the item is used. It takes two param. One is the player, who is using the item and the room, in which the item is used
     * @param player who is using it
     * @param room where it is used
     */
    public void effect(Playable player, Room room){
    }

    /**
     * some item has effect that delayed later at the time, this will be triggered at a certain time
     * @param gameBroad
     */
    public void afterEffect(GameBroad gameBroad){
    }
}
