package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room;


public class Parking extends Room {
    public Parking() {
        super(4, "Parking", 99);
    }



    @Override
    public boolean isFallen(){
        if (roomCharaters.size()>0 && currentZombienumber>0){
            return true;
        }
        else {
            return false;
        }
    }

    public String toString() {
        String spaces="";
        for (int i=0; i<70-(roomCharaters.toString().length()); i++){
            spaces+=" ";
        }
        String spaces2="";
        for (int i=0; i<12-name.length(); i++){
            spaces2+=" ";
        }
        return "ROOM "+roomNum + ". " + name + " (Capability: " + capability + ") " + spaces2 + " has " + roomCharaters + spaces + "Current Zombies number: " + currentZombienumber;
    }

}
