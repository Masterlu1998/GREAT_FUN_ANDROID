package com.example.great_fun_http;

public class Friend {
    private int friendId;
    private String friendName;
    private String friendMessage;
    private int friendImg;
    private String friendDate;
    private String friendHttpImg;

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


    public Friend(int mFriendId, String mFriendName, String mFriendMessage, String friendHttpImg, String mFriendDate) {
        this.friendId = mFriendId;
        this.friendName = mFriendName;
        this.friendMessage = mFriendMessage;
        this.friendHttpImg = friendHttpImg;
        this.friendDate = mFriendDate;
    }

    public String getFriendHttpImg() {
        return friendHttpImg;
    }

    public void setFriendHttpImg(String friendHttpImg) {
        this.friendHttpImg = friendHttpImg;
    }
}
