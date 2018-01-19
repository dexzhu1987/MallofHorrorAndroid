package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;



import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.*;

import java.util.ArrayList;
import java.util.List;

public class Axe extends Item {
    public Axe() {
        super(3, "Axe");
    }


    @Override
    public void effect(Playable player, Room room) {
        List<String> messages = new ArrayList<>();
        messages.add("Axe can kill one zomebie in the room");
        int orignalZombieNumber =room.getCurrentZombienumber();
        room.zombieKilled();
        messages.add("Zombies number has dropped from " + orignalZombieNumber + " to "  + room.getCurrentZombienumber());
//        MultiMessagesWindow.display(messages,  "-------------Axe-----------");
    }
}
