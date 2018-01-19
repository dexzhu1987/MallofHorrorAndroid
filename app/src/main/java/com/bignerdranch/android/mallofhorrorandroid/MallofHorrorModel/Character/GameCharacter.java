package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Character;


public abstract class GameCharacter {
    private String name;
    private int points;
    private int strength;
    private int vote;
    private String ownercolor;


    public GameCharacter(String name, int points, int strength, int vote) {
        this.name = name;
        this.points = points;
        this.strength = strength;
        this.vote = vote;
        ownercolor = "" ;
    }

//    public int vote (){
//        int votesum = 0;
//        return votesum += getVote();
//    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getStrength() {
        return strength;
    }

    public int getVote() {
        return vote;
    }

    @Override
    public String toString() {
        return ownercolor + " " +  name ;
    }

    public String getOwnercolor() {
        return ownercolor;
    }

    public void setOwnercolor(String ownercolor) {
        this.ownercolor = ownercolor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameCharacter that = (GameCharacter) o;

        if (this.ownercolor.equalsIgnoreCase(that.ownercolor)&& this.name.equalsIgnoreCase(that.name)){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + points;
        result = 31 * result + strength;
        result = 31 * result + vote;
        result = 31 * result + (ownercolor != null ? ownercolor.hashCode() : 0);
        return result;
    }
}
