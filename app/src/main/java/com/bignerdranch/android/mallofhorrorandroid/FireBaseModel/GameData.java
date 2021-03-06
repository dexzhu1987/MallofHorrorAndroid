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
    private String mSelectedCharacter;
    private int mSelectedRoom;
    private int mPassingType;
    private String whoVoteColor;
    private String voteWhomColor;
    private Boolean mUsedItemThreat;
    private int mAffectedRoom;
    private int mItemNumber;
    private int mSelectedRoomPhaseFive;
    private int mPlayerIndex;
    private Boolean mIsUsedItem;
    private int mPrevICount;
    private int mAfterAffectedRoomNumber;
    private String mAffectedGameCharacter;


    public String getmSelectedCharacter() {
        return mSelectedCharacter;
    }

    public void setmSelectedCharacter(String mSelectedCharacter) {
        this.mSelectedCharacter = mSelectedCharacter;
    }

    public int getmSelectedRoom() {
        return mSelectedRoom;
    }

    public void setmSelectedRoom(int mSelectedRoom) {
        this.mSelectedRoom = mSelectedRoom;
    }

    public int getmPassingType() {
        return mPassingType;
    }

    public void setmPassingType(int mPassingType) {
        this.mPassingType = mPassingType;
    }

    public GameData() {

    }

    public GameData(int mCountPhase, int mCountSetUp, int mSecondCount, int mThirdCount, int mFourthCount, int mFifthCount,
                    int mSixCount) {
        this.mCountPhase = mCountPhase;
        this.mCountSetUp = mCountSetUp;
        this.mSecondCount = mSecondCount;
        this.mThirdCount = mThirdCount;
        this.mFourthCount = mFourthCount;
        this.mFifthCount = mFifthCount;
        this.mSixCount = mSixCount;
        this.mPassingType = 0;
        this.mSelectedCharacter = mSelectedCharacter;
        this.mSelectedRoom = mSelectedRoom;
    }

    public GameData(int mCountPhase, int mCountSetUp, int mSecondCount, int mThirdCount, int mFourthCount, int mFifthCount,
                    int mSixCount, int mSelectedRoom, String mSelectedCharacter) {
        this.mCountPhase = mCountPhase;
        this.mCountSetUp = mCountSetUp;
        this.mSecondCount = mSecondCount;
        this.mThirdCount = mThirdCount;
        this.mFourthCount = mFourthCount;
        this.mFifthCount = mFifthCount;
        this.mSixCount = mSixCount;
        this.mPassingType = 1;
        this.mSelectedCharacter = mSelectedCharacter;
        this.mSelectedRoom = mSelectedRoom;
    }

    public GameData(int mCountPhase, int mCountSetUp, int mSecondCount, int mThirdCount, int mFourthCount, int mFifthCount,
                    int mSixCount, String whoVoteColor, String voteWhomColor) {
        this.mCountPhase = mCountPhase;
        this.mCountSetUp = mCountSetUp;
        this.mSecondCount = mSecondCount;
        this.mThirdCount = mThirdCount;
        this.mFourthCount = mFourthCount;
        this.mFifthCount = mFifthCount;
        this.mSixCount = mSixCount;
        this.whoVoteColor = whoVoteColor;
        this.voteWhomColor = voteWhomColor;
        this.mPassingType=2;
    }

    public GameData(int mCountPhase, int mCountSetUp, int mSecondCount, int mThirdCount, int mFourthCount, int mFifthCount,
                    int mSixCount, boolean mUsedItem, int mAffectedRoom) {
        this.mCountPhase = mCountPhase;
        this.mCountSetUp = mCountSetUp;
        this.mSecondCount = mSecondCount;
        this.mThirdCount = mThirdCount;
        this.mFourthCount = mFourthCount;
        this.mFifthCount = mFifthCount;
        this.mSixCount = mSixCount;
        this.mPassingType = 3;
        this.mUsedItemThreat = mUsedItem;
        this.mAffectedRoom = mAffectedRoom;
    }

    public int getItemNumber() {
        return mItemNumber;
    }

    public void setItemNumber(int itemNumber) {
        mItemNumber = itemNumber;
    }

    public GameData(int mCountPhase, int mCountSetUp, int mSecondCount, int mThirdCount, int mFourthCount, int mFifthCount,
                    int mSixCount, int mItemNumber) {
        this.mCountPhase = mCountPhase;
        this.mCountSetUp = mCountSetUp;
        this.mSecondCount = mSecondCount;
        this.mThirdCount = mThirdCount;
        this.mFourthCount = mFourthCount;
        this.mFifthCount = mFifthCount;
        this.mSixCount = mSixCount;
        this.mItemNumber = mItemNumber;
        this.mPassingType = 4;
    }

    public int getSelectedRoomPhaseFive() {
        return mSelectedRoomPhaseFive;
    }

    public void setSelectedRoomPhaseFive(int selectedRoomPhaseFive) {
        mSelectedRoomPhaseFive = selectedRoomPhaseFive;
    }

    public int getPlayerIndex() {
        return mPlayerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        mPlayerIndex = playerIndex;
    }

    public GameData(int mCountPhase, int mCountSetUp, int mSecondCount, int mThirdCount, int mFourthCount, int mFifthCount,
                    int mSixCount, int mSelectRoomPhaseFive, int mPlayerIndex) {
        this.mCountPhase = mCountPhase;
        this.mCountSetUp = mCountSetUp;
        this.mSecondCount = mSecondCount;
        this.mThirdCount = mThirdCount;
        this.mFourthCount = mFourthCount;
        this.mFifthCount = mFifthCount;
        this.mSixCount = mSixCount;
        this.mPassingType = 5;
        this.mSelectedRoomPhaseFive = mSelectRoomPhaseFive;
        this.mPlayerIndex = mPlayerIndex;
    }

    public GameData(int mCountPhase, int mCountSetUp, int mSecondCount, int mThirdCount, int mFourthCount, int mFifthCount,
                    int mSixCount,int mPlayerIndex,  int mSelectRoomPhaseFive, String mSelectedCharacter) {
        this.mCountPhase = mCountPhase;
        this.mCountSetUp = mCountSetUp;
        this.mSecondCount = mSecondCount;
        this.mThirdCount = mThirdCount;
        this.mFourthCount = mFourthCount;
        this.mFifthCount = mFifthCount;
        this.mSixCount = mSixCount;
        this.mPassingType = 6;
        this.mSelectedRoomPhaseFive = mSelectRoomPhaseFive;
        this.mPlayerIndex = mPlayerIndex;
        this.mSelectedCharacter = mSelectedCharacter;
    }

    public Boolean getmIsUsedItem() {
        return mIsUsedItem;
    }

    public void setmIsUsedItem(Boolean mIsUsedItem) {
        this.mIsUsedItem = mIsUsedItem;
    }

    public int getmPrevICount() {
        return mPrevICount;
    }

    public void setmPrevICount(int mPrevICount) {
        this.mPrevICount = mPrevICount;
    }

    public int getmAfterAffectedRoomNumber() {
        return mAfterAffectedRoomNumber;
    }

    public void setmAfterAffectedRoomNumber(int mAfterAffectedRoomNumber) {
        this.mAfterAffectedRoomNumber = mAfterAffectedRoomNumber;
    }

    public String getmAffectedGameCharacter() {
        return mAffectedGameCharacter;
    }

    public void setmAffectedGameCharacter(String mAffectedGameCharacter) {
        this.mAffectedGameCharacter = mAffectedGameCharacter;
    }

    public GameData(int mCountPhase, int mCountSetUp, int mSecondCount, int mThirdCount, int mFourthCount, int mFifthCount,
                    int mSixCount, boolean mIsUsedItem, int mItemNumber, String mAffectedGameCharacter, int mAfterAffectedRoomNumber, int mPrevICount) {
        this.mCountPhase = mCountPhase;
        this.mCountSetUp = mCountSetUp;
        this.mSecondCount = mSecondCount;
        this.mThirdCount = mThirdCount;
        this.mFourthCount = mFourthCount;
        this.mFifthCount = mFifthCount;
        this.mSixCount = mSixCount;
        this.mPassingType = 7;
        this.mIsUsedItem = mIsUsedItem;
        this.mItemNumber = mItemNumber;
        this.mAffectedGameCharacter = mAffectedGameCharacter;
        this.mAfterAffectedRoomNumber = mAfterAffectedRoomNumber;
        this.mPrevICount = mPrevICount;
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

    public Boolean getmUsedItemThreat() {
        return mUsedItemThreat;
    }

    public void setmUsedItemThreat(Boolean mUsedItemThreat) {
        this.mUsedItemThreat = mUsedItemThreat;
    }

    public String getWhoVoteColor() {
        return whoVoteColor;
    }

    public void setWhoVoteColor(String whoVoteColor) {
        this.whoVoteColor = whoVoteColor;
    }

    public String getVoteWhomColor() {
        return voteWhomColor;
    }

    public void setVoteWhomColor(String voteWhomColor) {
        this.voteWhomColor = voteWhomColor;
    }

    public int getmAffectedRoom() {
        return mAffectedRoom;
    }

    public void setmAffectedRoom(int mAffectedRoom) {
        this.mAffectedRoom = mAffectedRoom;
    }

    @Override
    public String toString() {
        return "GameData: mCountPhase: " + mCountPhase + " mCountSetup: " + mCountSetUp + " mSecondCount: " + mSecondCount + " mThirdCount: " + mThirdCount +
                " mFourthCount: " + mFourthCount + " mFifthCount: " + mFifthCount + " mSixthCount: " + mSixCount;
    }
}
