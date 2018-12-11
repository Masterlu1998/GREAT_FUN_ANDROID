package com.example.great_fun;

public class Activity {

    private int activityId;
    private String activityTitle;
    private int activityImgId;
    private String activityContent;
    private String activityDate;


    public int getActivityId() {
        return activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
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

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public void setActivityImgId(int activityImgId) {
        this.activityImgId = activityImgId;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public Activity(int mActivityCardId, String mActivityCardTitle, int mActivityCrdImgId, String mActivityCardContent, String mActivityDate) {
        this.activityId = mActivityCardId;
        this.activityTitle = mActivityCardTitle;
        this.activityImgId = mActivityCrdImgId;
        this.activityContent = mActivityCardContent;
        this.activityDate = mActivityDate;
    }

    public Activity(int mActivityCardId) {
        this.activityId = mActivityCardId;
    }
}
