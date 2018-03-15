package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.*;

public class ItemDeck implements Parcelable, Serializable{
    private List<Item> itemsDeck;


    public ItemDeck() {
        itemsDeck = new ArrayList<>();
        for (int i=0; i<3; i++){
            itemsDeck.add(new Threat());
        }
        for (int i=0; i<3; i++){
            itemsDeck.add(new SecurityCamera());
        }
        for (int i=0; i<3; i++){
            itemsDeck.add(new ShotGun());
        }
        for (int i=0; i<3; i++){
            itemsDeck.add(new Axe());
        }
        for (int i=0; i<3; i++){
            itemsDeck.add(new Hardware());
        }
        for (int i=0; i<3; i++){
            itemsDeck.add(new Sprint());
        }
        for (int i=0; i<3; i++){
            itemsDeck.add(new Hidden());
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable((Serializable) itemsDeck);
    }

    protected ItemDeck (Parcel in){
       itemsDeck = (List<Item>) in.readSerializable();
    }

    public static final Creator<ItemDeck> CREATOR = new Creator<ItemDeck>() {
        @Override
        public ItemDeck createFromParcel(Parcel source) {
            return new ItemDeck(source);
        }

        @Override
        public ItemDeck[] newArray(int size) {
            return new ItemDeck[size];
        }
    };

    public static ItemDeck createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static ItemDeck[] newArray(int size) {
        return CREATOR.newArray(size);
    }

    public void setItemsDeck(List<Item> itemsDeck) {
        this.itemsDeck = itemsDeck;
    }

    public List<Item> getItemsDeck() {
        return itemsDeck;
    }

    protected void printItemDeck(){
        for (Item item: itemsDeck){
            System.out.println(item);
        }
    }

    public void shuffle(){
        Random ram = new Random();
        for (int i=0; i< itemsDeck.size(); i++){
            int ramNum = ram.nextInt(itemsDeck.size());
            Item temp = itemsDeck.get(ramNum) ;
            itemsDeck.set(ramNum,itemsDeck.get(i));
            itemsDeck.set(i, temp);
        }
    }

    public Item deal(){
        Item item = new ShotGun();
        if (itemsDeck.size()>0){
         item = itemsDeck.get(0);
         itemsDeck.remove(0);
        }
        return item;
    }

    public void addBackItem (Item item){
        itemsDeck.add(item);
    }

    public void removeItem(Item item) { itemsDeck.remove(item);}

    public static void main(String[] args) {
        ItemDeck i1=new ItemDeck();

        i1.shuffle();
        i1.printItemDeck();
        System.out.println(i1.deal());
        System.out.println(i1.deal());
        System.out.println(i1.deal());
        System.out.println(i1.deal());
    }

}
