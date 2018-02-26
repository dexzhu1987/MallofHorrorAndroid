package com.bignerdranch.android.mallofhorrorandroid.FireBaseModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.GameCharacter;

/**
 * Created by Dexter on 2018-02-24.
 */

public class Game implements Parcelable, java.io.Serializable {
   private String roomId;
   private String player1;
   private String player2;
   private String player3;
   private String player4;

    public Game() {
    }

    public Game(String roomId){
        this.roomId = roomId;
        player1 = "";
        player2 = "";
        player3 = "";
        player4 = "";
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer3() {
        return player3;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPlayer4() {

        return player4;
    }

    public void setPlayer4(String player4) {
        this.player4 = player4;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomId);
        dest.writeString(player1);
        dest.writeString(player2);
        dest.writeString(player3);
        dest.writeString(player4);
    }

    protected Game (final Parcel in){
        roomId = in.readString();
        player1 = in.readString();
        player2 = in.readString();
        player3 = in.readString();
        player4 = in.readString();
    }


    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source) {
            };
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };


    public static Game createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static Game[] newArray(int size) {
        return CREATOR.newArray(size);
    }
}
