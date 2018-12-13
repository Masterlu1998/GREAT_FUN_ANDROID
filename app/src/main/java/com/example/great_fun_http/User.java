package com.example.great_fun_http;

public class User {
    private int userId;
    private String userName;
    private String userContent;
    private String userHeadImg;

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserContent() {
        return userContent;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserContent(String userContent) {
        this.userContent = userContent;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public User(int userId, String userName, String userContent, String userHeadImg) {
        this.userId = userId;
        this.userName = userName;
        this.userContent = userContent;
        this.userHeadImg = userHeadImg;
    }

    public User(int userId) {
        this.userId = userId;
    }
}
