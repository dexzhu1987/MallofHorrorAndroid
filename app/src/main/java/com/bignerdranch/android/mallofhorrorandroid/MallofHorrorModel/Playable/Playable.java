package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.Room;

import java.io.Serializable;
import java.util.*;


public class Playable implements Parcelable, java.io.Serializable, Comparable  {
    protected String name;
    protected String color;
    protected List<GameCharacter> characters;
    protected List<GameCharacter> charactersselect;
    protected List<Item> currentItem;

    public Playable() {
        name = "";
        color = "";

        characters = new ArrayList<>();
        characters.add(new GunMan());
        characters.add(new ToughGuy());
        characters.add(new Model());

        charactersselect = new ArrayList<>();
        charactersselect.add(new GunMan());
        charactersselect.add(new ToughGuy());
        charactersselect.add(new Model());

        currentItem = new ArrayList<Item>();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(color);
        dest.writeSerializable((Serializable) characters);
        dest.writeSerializable((Serializable) charactersselect);
        dest.writeList(currentItem);
    }

    protected Playable (final Parcel in){
        name = in.readString();
        color = in.readString();
        characters = (List<GameCharacter>) in.readSerializable();
        charactersselect = (List<GameCharacter>) in.readSerializable();
        currentItem =  in.readArrayList(Item.class.getClassLoader());
    }


    public static final Creator<Playable> CREATOR = new Creator<Playable>() {
        @Override
        public Playable createFromParcel(Parcel source) {
            return new Playable(source);
        }

        @Override
        public Playable[] newArray(int size) {
            return new Playable[size];
        }
    };

    public static Playable createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static Playable[] newArray(int size) {
        return CREATOR.newArray(size);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int remaingCharacter() {
        return characters.size();
    }

    public List<GameCharacter> getGameCharacters() {
        return characters;
    }

    public void removeCharacter(GameCharacter character){
        characters.remove(character);
    }

    public int totalVictoryPoints (){
        int sum = 0 ;
        for (GameCharacter character: characters){
            sum+=character.getPoints();
        }
        return sum;
    }

    public GameCharacter choose(String charname) {
        int q = 0;
        for (int i = 0; i < characters.size(); i++) {
            if (charname.equalsIgnoreCase(characters.get(i).getName())) {
                q = i;
            }
        }
        return characters.get(q);
    }

    public void chardeath(String charname) {
        int q=0;
        for (int i = 0; i < charactersselect.size(); i++) {
            if (charname.equalsIgnoreCase(characters.get(i).getName())) {
                q=i;
            }
        };
        characters.remove(q);
    }

    public List<GameCharacter> getCharactersselect() {
        return charactersselect;
    }

    public String getCharactersselectstr() {
        String fin="";
        if (charactersselect.size()>1){
            for (int i=0; i<charactersselect.size(); i++){
                if (i==charactersselect.size()-1){
                    fin+=charactersselect.get(i).getName();
                }
                else {
                    fin+=charactersselect.get(i).getName()+ " and ";
                }
            }
        }
        else if(charactersselect.size()==1){
            fin += charactersselect.get(0).getName();
        }
        return fin;
    }

    public GameCharacter selectchoose(String charname) {
        int q = 0;
        for (int i = 0; i < charactersselect.size(); i++) {
            if (charname.equalsIgnoreCase(charactersselect.get(i).getName())) {
                q = i;
            }
        }
        return charactersselect.get(q);
    }

    public void selectchooseremove(String charname) {
        int q=0;
        for (int i = 0; i < charactersselect.size(); i++) {
            if (charname.equalsIgnoreCase(charactersselect.get(i).getName())) {
                q=i;
            }
        };
        charactersselect.remove(q);
    }

    public void getItem(Item item){
        currentItem.add(item);
    }

    public void usedItem(Item item){
       currentItem.remove(item);
    }

    public List<Item> getCurrentItem() {
        return currentItem;
    }

    public boolean hasItem(){
        return currentItem.size()==0;
    }

    public boolean hasThreat(){
        String threat = "Threat";
        for (int i=0; i<currentItem.size(); i++){
            if (threat.equalsIgnoreCase(currentItem.get(i).getName())) {
               return true;
            }
        }
        return false;
    }

    public boolean hasSecurityCamera(){
        String itemName = "SecurityCamera";
        for (int i=0; i<currentItem.size(); i++){
            if (itemName.equalsIgnoreCase(currentItem.get(i).getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOthersItems(){
        String itemName =  "ShotGun";
        String itemName1 = "Sprint";
        String itemName2 = "Axe";
        String itemName3 = "Hareware";
        String itemName4 = "Hidden";

        for (int i=0; i<currentItem.size(); i++){

            if (itemName.equalsIgnoreCase(currentItem.get(i).getName())||itemName1.equalsIgnoreCase(currentItem.get(i).getName())
                    ||itemName2.equalsIgnoreCase(currentItem.get(i).getName())||itemName3.equalsIgnoreCase(currentItem.get(i).getName())||
                    itemName4.equalsIgnoreCase(currentItem.get(i).getName())) {
                return true;
            }
        }
        return false;
    }

    public List<Item> otherItemsList(){
        List<Item> otherItemList = new ArrayList<>();
        for (Item item: currentItem){
            if (!item.getName().equalsIgnoreCase("SecurityCamera") && !item.getName().equalsIgnoreCase("Threat")){
                otherItemList.add(item);
            }
        }
        return otherItemList;
    }

    public int threatNum(){
        String item = "Threat";
        int count=0;
        for (int i=0; i<currentItem.size(); i++){
            if (item.equalsIgnoreCase(currentItem.get(i).getName())) {
                count++;
            }
        }
        return count;
    }

    public int securityCameraNum(){
        String item = "SecurityCamera";
        int count=0;
        for (int i=0; i<currentItem.size(); i++){
            if (item.equalsIgnoreCase(currentItem.get(i).getName())) {
                count++;
            }
        }
        return count;
    }


    @Override
    public String toString() {
       return  "Player " + color + ": " + name;
    }



    public static void main(String[] args) {
        Playable p1 = new Player();
        System.out.println( p1.choose("Gunman"));
        p1.chardeath("tough guy");
        System.out.println(p1.remaingCharacter());
        System.out.println(p1.getGameCharacters());
//
//        System.out.println();
//        System.out.println(p1.selectchoose("Model"));
//        System.out.println(p1.getCharactersselect());
//        p1.selectchooseremove("Gun Man");
//        System.out.println(p1.getCharactersselect());
//        p1.selectchooseremove("Tough Guy");
//        System.out.println(p1.getCharactersselect());
//        p1.selectchooseremove("Model");
//        System.out.println(p1.getCharactersselect());
//
        Item shortgun= new ShotGun();
        Item threat = new Threat();
        Item security = new SecurityCamera();
        Item hardware = new Hardware();
        Item hidden = new Hidden();

        p1.getItem(shortgun);
        p1.getItem(threat);
        p1.getItem(threat);
        p1.getItem(security);
        p1.getItem(hardware);
        p1.getItem(hidden);
        System.out.println(p1.getCurrentItem());
        System.out.println(p1.hasOthersItems());
        System.out.println(p1.threatNum());
        System.out.println(p1.securityCameraNum());
        System.out.println(p1.otherItemsList());
        Item threat1 = new Threat();
        p1.usedItem(threat);
        System.out.println(p1.getCurrentItem());
    }


    @Override
    public int compareTo(@NonNull Object o) {
        if (this.color.equalsIgnoreCase("RED") && !((Playable)o).getColor().equalsIgnoreCase("RED")){
            return 1;
        } else if (this.color.equalsIgnoreCase("YELLOW") && !((Playable)o).getColor().equalsIgnoreCase("YELLOW")){
            return 1;
        } else if(this.color.equalsIgnoreCase("BLUE") && !((Playable)o).getColor().equalsIgnoreCase("BLUE")){
            return 1;
        } else  {
            return -1;
        }
    }
}
