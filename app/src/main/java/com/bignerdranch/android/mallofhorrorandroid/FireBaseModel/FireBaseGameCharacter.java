package com.bignerdranch.android.mallofhorrorandroid.FireBaseModel;

/**
 * Created by dexunzhu on 2018-03-13.
 */

public class FireBaseGameCharacter {
    private String ownerColor;
    private String characterName;

    public FireBaseGameCharacter(String ownerColor, String characterName) {
        this.ownerColor = ownerColor;
        this.characterName = characterName;
    }

    public FireBaseGameCharacter() {
    }

    public String getOwnerColor() {
        return ownerColor;
    }

    public void setOwnerColor(String ownerColor) {
        this.ownerColor = ownerColor;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
}
