package com.example.great_fun_http;

public class Activity {

    private int activityId;
    private String activityName;
    private int activityImgId;
    private String activityContent;
    private String activityDate;
    private String activityImgUrl;
    private int activityPostUser;


    public int getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getActivityImgId() {
        return activityImgId;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public String getActivityImgUrl() {
        return activityImgUrl;
    }

    public int getActivityPostUser() {
        return activityPostUser;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public void setActivityImgId(int activityImgId) {
        this.activityImgId = activityImgId;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setActivityImgUrl(String activityImgUrl) {
        this.activityImgUrl = activityImgUrl;
    }

    public void setActivityPostUser(int activityPostUser) {
        this.activityPostUser = activityPostUser;
    }

    public Activity(int mActivityCardId, String mActivityCardTitle, int mActivityCrdImgId, String mActivityCardContent, String mActivityDate, String mActivityImgUrl) {
        this.activityId = mActivityCardId;
        this.activityName = mActivityCardTitle;
        this.activityImgId = mActivityCrdImgId;
        this.activityContent = mActivityCardContent;
        this.activityDate = mActivityDate;
        this.activityImgUrl = mActivityImgUrl;
    }

    public Activity(int activityId, String activityName, String activityContent, String activityDate, String activityImgUrl, int activityPostUser) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityContent = activityContent;
        this.activityDate = activityDate;
        this.activityImgUrl = activityImgUrl;
        this.activityPostUser = activityPostUser;
    }

    public Activity(int mActivityCardId) {
        this.activityId = mActivityCardId;
    }
}
