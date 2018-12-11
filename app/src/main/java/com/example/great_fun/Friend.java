package com.example.great_fun;

public class Friend {
    private int friendId;
    private String friendName;
    private String friendMessage;
    private int friendImg;
    private String friendDate;

    public int getFriendId() {
        return friendId;
    }

    public int getFriendImg() {
        return friendImg;
    }

    public String getFriendMessage() {
        return friendMessage;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendDate() {
        return friendDate;
    }

    public Friend(int mFriendId, String mFriendName, String mFriendMessage, int mFriendImg, String mFriendDate) {
        this.friendId = mFriendId;
        this.friendName = mFriendName;
        this.friendMessage = mFriendMessage;
        this.friendImg = mFriendImg;
        this.friendDate = mFriendDate;
    }
}
