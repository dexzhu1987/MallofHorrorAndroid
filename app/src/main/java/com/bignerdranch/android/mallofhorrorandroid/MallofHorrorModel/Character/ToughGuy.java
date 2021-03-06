package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character;

import android.os.Parcel;
import android.os.Parcelable;

public class ToughGuy extends GameCharacter implements Parcelable, java.io.Serializable  {
    public ToughGuy() {
        super("Tough Guy", 5, 2, 1);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected ToughGuy(Parcel in) {
        super(in);
    }

    public static GameCharacter createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static GameCharacter[] newArray(int size) {
        return CREATOR.newArray(size);
    }

    @Override
    public int getVote() {
        return super.getVote();
    }

    @Override
    public int getPoints() {
        return super.getPoints();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public int getStrength() {
        return super.getStrength();
    }
}
