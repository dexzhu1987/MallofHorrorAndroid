package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.*;


import java.util.ArrayList;
import java.util.List;

public class ShotGun extends Item {
    public ShotGun() {
        super(4, "ShotGun");
    }

    public void effect(Playable player, Room room) {
        List<String> messeges = new ArrayList<>();
        messeges.add("ShortGun can kill two zomebie in the room");
        int orignalZombieNumber =room.getCurrentZombienumber();
        room.zombieKilled();
        room.zombieKilled();
        messeges.add("Zombies number has dropped from " + orignalZombieNumber + " to "  + room.getCurrentZombienumber());
//        MultiMessagesWindow.display(messeges,"---------------ShortGun------------------");
    }
}
