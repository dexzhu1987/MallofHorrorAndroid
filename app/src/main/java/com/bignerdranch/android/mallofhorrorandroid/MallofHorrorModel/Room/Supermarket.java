package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.GameCharacter;


public class Supermarket extends Room {
    public Supermarket() {
        super(6, "Supermarket", 6);
    }

    @Override
    public boolean isFallen() {
        if (roomCharaters.size()>0){
            if (currentZombienumber>4){
                return true;
            } else {
                if (roomCharaters.size()>0) {
                    int defend = 0;
                    for (GameCharacter character : roomCharaters) {
                        defend += character.getStrength();
                    }
                    if (currentZombienumber > defend) {
                        return true;
                    } else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }


}
