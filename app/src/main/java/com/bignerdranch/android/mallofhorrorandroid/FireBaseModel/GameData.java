package com.bignerdranch.android.mallofhorrorandroid.FireBaseModel;

/**
 * Created by Dexter on 2018-02-28.
 */

public class GameData {
    private int mCountPhase;
    private int mCountSetUp;
    private int mSecondCount;
    private int mThirdCount;
    private int mFourthCount;
    private int mFifthCount;
    private int mSixCount;

    public GameData() {
    }

    public GameData(int mCountPhase, int mCountSetUp, int mSecondCount, int mThirdCount, int mFourthCount, int mFifthCount, int mSixCount) {
        this.mCountPhase = mCountPhase;
        this.mCountSetUp = mCountSetUp;
        this.mSecondCount = mSecondCount;
        this.mThirdCount = mThirdCount;
        this.mFourthCount = mFourthCount;
        this.mFifthCount = mFifthCount;
        this.mSixCount = mSixCount;
    }

    public int getmCountPhase() {
        return mCountPhase;
    }

    public void setmCountPhase(int mCountPhase) {
        this.mCountPhase = mCountPhase;
    }

    public int getmCountSetUp() {
        return mCountSetUp;
    }

    public void setmCountSetUp(int mCountSetUp) {
        this.mCountSetUp = mCountSetUp;
    }

    public int getmSecondCount() {
        return mSecondCount;
    }

    public void setmSecondCount(int mSecondCount) {
        this.mSecondCount = mSecondCount;
    }

    public int getmThirdCount() {
        return mThirdCount;
    }

    public void setmThirdCount(int mThirdCount) {
        this.mThirdCount = mThirdCount;
    }

    public int getmFourthCount() {
        return mFourthCount;
    }

    public void setmFourthCount(int mFourthCount) {
        this.mFourthCount = mFourthCount;
    }

    public int getmFifthCount() {
        return mFifthCount;
    }

    public void setmFifthCount(int mFifthCount) {
        this.mFifthCount = mFifthCount;
    }

    public int getmSixCount() {
        return mSixCount;
    }

    public void setmSixCount(int mSixCount) {
        this.mSixCount = mSixCount;
    }
}
