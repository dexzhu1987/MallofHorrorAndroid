package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;

import android.os.Parcel;
import android.os.Parcelable;

public class SecurityCamera extends Item implements Parcelable, java.io.Serializable  {
    public SecurityCamera() {
        super(2, "SecurityCamera");
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected SecurityCamera(Parcel in) {
        super(in);
    }

    public static Item createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static Item[] newArray(int size) {
        return CREATOR.newArray(size);
    }
}
