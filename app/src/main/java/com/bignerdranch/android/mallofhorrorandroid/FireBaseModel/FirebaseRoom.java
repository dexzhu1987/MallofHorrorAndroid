package com.bignerdranch.android.mallofhorrorandroid.FireBaseModel;

import java.util.List;

/**
 * Created by dexunzhu on 2018-03-13.
 */

public class FirebaseRoom {
    private String name;
    private int roomNumber;
    private int zombieNumber;
    private List<String> Characters;


    public FirebaseRoom() {
    }

    public FirebaseRoom(String name, int roomNumber, List<String> characters, int zombieNumber) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.zombieNumber = zombieNumber;
        Characters = characters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getZombieNumber() {
        return zombieNumber;
    }

    public void setZombieNumber(int zombieNumber) {
        this.zombieNumber = zombieNumber;
    }

    public List<String> getCharacters() {
        return Characters;
    }

    public void setCharacters(List<String> characters) {
        Characters = characters;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
