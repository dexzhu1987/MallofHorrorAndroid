package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item;

import java.util.*;

public class ItemDeck  {
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
