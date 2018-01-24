package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.Room;

import java.util.HashMap;

public abstract class GameCharacter implements Parcelable, java.io.Serializable {
    private String name;
    private int points;
    private int strength;
    private int vote;
    private String ownercolor;




    public GameCharacter(String name, int points, int strength, int vote) {
        this.name = name;
        this.points = points;
        this.strength = strength;
        this.vote = vote;
        ownercolor = "" ;
    }

//    public int vote (){
//        int votesum = 0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(points);
        dest.writeInt(strength);
        dest.writeInt(vote);
        dest.writeString(ownercolor);
    }

    protected GameCharacter (final Parcel in){
        name = in.readString();
        points = in.readInt();
        strength = in.readInt();
        vote = in.readInt();
        ownercolor = in.readString();
    }


    public static final Creator<GameCharacter> CREATOR = new Creator<GameCharacter>() {
        @Override
        public GameCharacter createFromParcel(Parcel source) {
            return new GameCharacter(source) {
            };
        }

        @Override
        public GameCharacter[] newArray(int size) {
            return new GameCharacter[size];
        }
    };


    public static GameCharacter createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static GameCharacter[] newArray(int size) {
        return CREATOR.newArray(size);
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getStrength() {
        return strength;
    }

    public int getVote() {
        return vote;
    }

    @Override
    public String toString() {
        return ownercolor + " " +  name ;
    }

    public String getOwnercolor() {
        return ownercolor;
    }

    public void setOwnercolor(String ownercolor) {
        this.ownercolor = ownercolor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameCharacter that = (GameCharacter) o;

        if (this.ownercolor.equalsIgnoreCase(that.ownercolor)&& this.name.equalsIgnoreCase(that.name)){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + points;
        result = 31 * result + strength;
        result = 31 * result + vote;
        result = 31 * result + (ownercolor != null ? ownercolor.hashCode() : 0);
        return result;
    }
}
