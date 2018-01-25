package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Game.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.*;

import java.util.*;


public class Sprint extends Item implements Parcelable, java.io.Serializable  {
    public Sprint() {
        super(7,"Sprint");
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Sprint(Parcel in) {
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
        int roompicked = 0;
        boolean loop = false;
        do {
            loop=false;
            List<Integer> opitons = new ArrayList<>();
            opitons.add(1);
            opitons.add(2);
            opitons.add(3);
            opitons.add(4);
            opitons.add(5);
            opitons.add(6);
//            roompicked = ChoosingRoomWindow.display(opitons, "-------------Sprint----------------"+
//                    "\nPlease choose your room that you want to eacape to");
            if (roompicked == room.getRoomNum()){
//                   SimpleMessageWindow.display("You are already in the room, please select other number");
                    loop=true;
            }
        }
        while (loop);
        String charselect = "";
        HashSet<GameCharacter> existedCharacters = room.existChracterForThatPlayer(player);
        List<GameCharacter> existedCharactersList = new ArrayList<>();
        for (GameCharacter character: existedCharacters){
            existedCharactersList.add(character);
        }
//        charselect = GameCharacterWindow.display(player, existedCharactersList, "Please select one of below characters to go to that room" );
        GameCharacter selectedCharacter = new ToughGuy();
        for (GameCharacter character: existedCharacters){
            if (charselect.equalsIgnoreCase(character.getName())){
                selectedCharacter = character;
            }
        }
        room.leave(selectedCharacter);
//        SimpleMessageWindow.display(selectedCharacter + " has lefted " + room.getName());
        affectedGameCharacter = selectedCharacter;

    }

    public void afterEffect(GameBroad gameBroad){

    }

    public static void main(String[] args) {
        GameBroad gb = new GameBroad(1);
        Room r1 = gb.matchRoom(4);
        Playable p1 = gb.getPlayers().get(0);

        Item item = new Sprint();
        p1.setColor("RED");
        r1.enter(p1.getGameCharacters().get(0));
        r1.enter(p1.getGameCharacters().get(1));

        item.effect(p1, r1);
        item.afterEffect(gb);

    }


}
