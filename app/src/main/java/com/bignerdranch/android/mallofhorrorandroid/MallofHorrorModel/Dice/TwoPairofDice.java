package com.bignerdranch.android.mallofhorrorandroid.MallofHorrorModel.Dice;

public class TwoPairofDice {
    private int faceOne;
    private int faceTwo;
    private int faceThree;
    private int faceFour;
    final private int MAXFACE = 6;


    public TwoPairofDice () {
        faceOne = 1;
        faceTwo = 1;
        faceThree=1;
        faceFour=1;
    }

    public int rollDieOne() {
        this.faceOne = ((int)(Math.random()*6))+1;
        return faceOne;
    }

    public int rollDieTwo() {
        this.faceTwo = ((int)(Math.random()*6))+1;
        return faceTwo;
    }

    public int rollDieThree() {
        this.faceThree = ((int)(Math.random()*6))+1;
        return faceThree;
    }

    public int rollDieFour() {
        this.faceFour = ((int)(Math.random()*6))+1;
        return faceFour;
    }
    public int getDieOneFace () {
        return faceOne;
    }

    public int getDieTwoFace () {
        return  faceTwo;
    }

    public int getFaceThree() {
        return faceThree;
    }

    public int getFaceFour() {
        return faceFour;
    }

    public int sumofFour() {
        return faceOne + faceTwo + faceThree + faceFour;
    }

    @Override
    public String toString() {
        return "Model.Dice One has a value of " + faceOne + "\t Model.Dice Two has value of " + faceTwo
                + "Model.Dice Three has a value of " + faceThree + "\t Model.Dice Four has value of " + faceFour ;
    }
}
