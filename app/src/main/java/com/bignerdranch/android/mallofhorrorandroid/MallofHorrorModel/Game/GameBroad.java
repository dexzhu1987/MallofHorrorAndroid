package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Game;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Item.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Playable.*;
import com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Room.*;


import java.io.Serializable;
import java.util.*;



public class GameBroad implements Parcelable,Serializable{
  private List<Room> rooms;
  private List<Playable> players;
  private ItemDeck itemDeck;
  private Playable[] totalPlayerslist;
  private Room extraZombiesPlace;
  public static int playersNumber ;

    public static Creator<GameBroad> getCREATOR() {
        return CREATOR;
    }

    public GameBroad(int numplayer) {
        rooms = new ArrayList<>();
        rooms.add(new RestRoom());
        rooms.add(new Cachou());
        rooms.add(new Megatoys());
        rooms.add(new Parking());
        rooms.add(new SecurityHQ());
        rooms.add(new Supermarket());

        extraZombiesPlace = new ZombiesWonderingPlace();

        totalPlayerslist = new Playable[6];
        for (int i = 0; i < totalPlayerslist.length; i++){
            totalPlayerslist[i] = new Player();
          }
        totalPlayerslist[0].setColor("RED");
        totalPlayerslist[1].setColor("YELLOW");
        totalPlayerslist[2].setColor("BLUE");
        totalPlayerslist[3].setColor("GREEN");
        totalPlayerslist[4].setColor("BROWN");
        totalPlayerslist[5].setColor("BLACK");

        players = new ArrayList<>();

        for (int i = 0; i < numplayer; i++){
            players.add(totalPlayerslist[i]);
            for (int q = 0; q < players.get(i).getGameCharacters().size(); q++){
                players.get(i).getGameCharacters().get(q).setOwnercolor(players.get(i).getColor());
                players.get(i).getCharactersselect().get(q).setOwnercolor(players.get(i).getColor());
            }
        }
        itemDeck = new ItemDeck();

          playersNumber = 0;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(itemDeck,flags);
        dest.writeParcelable(extraZombiesPlace,flags);
        dest.writeList(rooms);
        dest.writeSerializable(totalPlayerslist);
        dest.writeList(players);
        dest.writeInt(playersNumber);
    }


    protected GameBroad (final Parcel in){
        rooms = in.readArrayList(Room.class.getClassLoader());
        players = in.readArrayList(Playable.class.getClassLoader());
        itemDeck = (ItemDeck) in.readSerializable();
        extraZombiesPlace = (Room) in.readSerializable();
        totalPlayerslist = (Playable[]) in.readSerializable();
        playersNumber = in.readInt();
    }


    public static final Parcelable.Creator<GameBroad> CREATOR = new Parcelable.Creator<GameBroad>() {
        @Override
        public GameBroad createFromParcel(Parcel source) {
            return new GameBroad(source);
        }

        @Override
        public GameBroad[] newArray(int size) {
            return new GameBroad[size];
        }
    };

    public static GameBroad createFromParcel(Parcel source) {
        return CREATOR.createFromParcel(source);
    }

    public static GameBroad[] newArray(int size) {
        return CREATOR.newArray(size);
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        for (int i = 0; i < playersNumber; i++) {
            players.add(totalPlayerslist[i]);
            for (int q = 0; q < players.get(i).getGameCharacters().size(); q++) {
                players.get(i).getGameCharacters().get(q).setOwnercolor(players.get(i).getColor());
                players.get(i).getCharactersselect().get(q).setOwnercolor(players.get(i).getColor());
            }
        }
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void setPlayers(List<Playable> players) {
        this.players = players;
    }

    public void setItemDeck(ItemDeck itemDeck) {
        this.itemDeck = itemDeck;
    }

    public Room getExtraZombiesPlace() {
        return extraZombiesPlace;
    }

    public void setExtraZombiesPlace(Room extraZombiesPlace) {
        this.extraZombiesPlace = extraZombiesPlace;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Integer> roomsOptions(Playable player){
      List<Integer> roomOptions = new ArrayList<>();
      for (int i=0; i<rooms.size(); i++){
       if (!rooms.get(i).allInThisRoom(player)){
           roomOptions.add(rooms.get(i).getRoomNum());
       }
      }
      return roomOptions;
    }

    public List<Playable> getPlayers() {
        return players;
    }

    public void removePlayer(Playable player){
      players.remove(player);
    }

    public ItemDeck getItemDeck() {
        return itemDeck;
    }

    public int totalCharatersRemain () {

      int sum = 0;
      for (int i = 0; i < players.size(); i++){
          sum += players.get(i).remaingCharacter();
      }
      return sum;
    }

    public Room matchRoom(int roomNum){
      int q = 0;
      for (int i = 0; i < rooms.size(); i++){
          if (roomNum == rooms.get(i).getRoomNum()){
              q = i;
          }
      }
      return rooms.get(q);
    }

    public Playable matchPlayer(String color){
        int q = 0;
        for (int i = 0; i < players.size(); i++){
            if (color.equalsIgnoreCase(players.get(i).getColor())){
                q = i;
            }
        }
        return players.get(q);
    }

    public Item matchItem(Playable player, String name){
        int q = 0;
        for (int i = 0; i < player.getCurrentItem().size(); i++){
            if (name.equalsIgnoreCase(player.getCurrentItem().get(i).getName())){
                q = i;
            }
        }
        return player.getCurrentItem().get(q);
    }


    public GameCharacter matchGameCharacter(Playable player, String gameCharacter){
       int q = 0;
       for (int i = 0; i < player.getGameCharacters().size(); i++){
           if (gameCharacter.equalsIgnoreCase(player.getGameCharacters().get(i).getName())){
               q = i;
           }
       }
       return player.getGameCharacters().get(q);
    }

    public Room inWhichRoom (GameCharacter character){
        int k = 0;
        for (int i = 0; i < rooms.size(); i++){
            for (int q = 0; q < rooms.get(i).getRoomCharaters().size();q++)
            if (character.equals(rooms.get(i).getRoomCharaters().get(q))){
               k = i;
            }
        }
        return rooms.get(k);
    }

    public void printRooms(){
        for (int i = 0; i <rooms.size(); i++){
            System.out.println(rooms.get(i));
        }
    }

    public HashSet<Playable> WhoCan(HashSet<String> existCharacterColors){
        HashSet<Playable> voteplayers = new HashSet<Playable>();
        for (String existCharacterColor: existCharacterColors) {
            for (int q = 0; q < players.size(); q++) {
                if (existCharacterColor.contains(players.get(q).getColor())) {
                    voteplayers.add(players.get(q));
                }
            }
        }
        return  voteplayers;
    }

    public HashSet<Playable> RemainPlayers(Playable winnerplayer){
        HashSet<Playable> remainplayers = new HashSet<>();

            for (int q = 0; q < players.size(); q++) {
                if (!winnerplayer.getColor().contains(players.get(q).getColor())) {
                    remainplayers.add(players.get(q));
                }

        }
        return  remainplayers;
    }

    /**
     * return the room that with the most Gamecharacter
     * @return the room with most people
     */
    public Room mostPeople(){
        int q = 0;
        int maxPeople = rooms.get(0).getRoomCharaters().size();
        int count = 0;
        for (int i = 0; i<rooms.size(); i++){
           if (maxPeople < rooms.get(i).getRoomCharaters().size()){
               maxPeople = rooms.get(i).getRoomCharaters().size();
               q = i;
           }
        }
        for (int i = 0; i < rooms.size(); i++){
            if (maxPeople == rooms.get(i).getRoomCharaters().size()){
                count++;
            }
        }
        if (count > 1){
            return extraZombiesPlace;
        } else {
            return rooms.get(q);
        }

    }

    public Playable[] getTotalPlayerslist() {
        return totalPlayerslist;
    }

    public void setTotalPlayerslist(Playable[] totalPlayerslist) {
        this.totalPlayerslist = totalPlayerslist;
    }

    public Room mostModel(){
        int q = 0;
        int maxPeople = rooms.get(0).modelNumber();
        int count = 0;
        for (int i = 0; i < rooms.size(); i++){
            if (maxPeople < rooms.get(i).modelNumber()){
                maxPeople = rooms.get(i).modelNumber();
                q = i;
            }
        }
        for (int i = 0; i < rooms.size(); i++){
            if (maxPeople == rooms.get(i).modelNumber()){
                count++;
            }
        }
        if (count > 1){
            return extraZombiesPlace;
        } else {
            return rooms.get(q);
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameBroad gameBroad = (GameBroad) o;

        if (rooms != null ? !rooms.equals(gameBroad.rooms) : gameBroad.rooms != null) return false;
        if (players != null ? !players.equals(gameBroad.players) : gameBroad.players != null)
            return false;
        if (itemDeck != null ? !itemDeck.equals(gameBroad.itemDeck) : gameBroad.itemDeck != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(totalPlayerslist, gameBroad.totalPlayerslist)) return false;
        return extraZombiesPlace != null ? extraZombiesPlace.equals(gameBroad.extraZombiesPlace) : gameBroad.extraZombiesPlace == null;
    }

    @Override
    public int hashCode() {
        int result = rooms != null ? rooms.hashCode() : 0;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        result = 31 * result + (itemDeck != null ? itemDeck.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(totalPlayerslist);
        result = 31 * result + (extraZombiesPlace != null ? extraZombiesPlace.hashCode() : 0);
        return result;
    }


    public static void main(String[] args) {
        GameBroad gameBroad = new GameBroad(2);

//        gameBroad.getRooms().get(0).enter(new Model());
//        gameBroad.getRooms().get(1).enter(new Model());
//        gameBroad.getRooms().get(1).enter(new Model());
//        gameBroad.getRooms().get(1).enter(new Model());
//        gameBroad.getRooms().get(2).enter(new Model());
//        gameBroad.getRooms().get(2).enter(new ToughGuy());
//        gameBroad.getRooms().get(2).enter(new ToughGuy());
//
//        gameBroad.printRooms();
//
//        System.out.println(gameBroad.mostPeople().getName());
//        System.out.println(gameBroad.mostModel().getName());
        GameCharacter c1=new Model();
        GameCharacter c2=new GunMan();
        GameCharacter c3=new ToughGuy();
        GameCharacter c4=new GunMan();
        GameCharacter c5=new Model();
        c1.setOwnercolor("RED");
        c2.setOwnercolor("RED");
        c3.setOwnercolor("RED");
        c4.setOwnercolor("YELLOW");
        c5.setOwnercolor("YELLOW");
        gameBroad.matchRoom(6).enter(c1);
        gameBroad.matchRoom(5).enter(c2);
        gameBroad.matchRoom(6).enter(c3);
        gameBroad.matchRoom(5).enter(c4);
        gameBroad.matchRoom(5).enter(c5);



        Playable player = new Playable();
        for (int q = 0; q < player.getGameCharacters().size(); q++) {
            player.getGameCharacters().get(q).setOwnercolor("RED");
            player.getCharactersselect().get(q).setOwnercolor("RED");
        }
//        GameCharacter gunman = new GunMan();
////        gunman.setOwnercolor("RED");
////        player.removeCharacter(gunman);
        System.out.println(gameBroad.roomsOptions(player));

        //launch();///
    }
}
