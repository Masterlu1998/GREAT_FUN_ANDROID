package com.example.great_fun_http.dbPackage;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.great_fun_http.Activity;
import com.example.great_fun_http.dbPackage.ActivityDbSchema.ActivityTable;

public class ActivityCursorWrapper extends CursorWrapper {
    public ActivityCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Activity getActivity() {
        int activityId = getInt(getColumnIndex(ActivityTable.Cols.activityId));
        String activityTitle = getString(getColumnIndex(ActivityTable.Cols.activityTitle));
        int activityImgId = getInt(getColumnIndex(ActivityTable.Cols.activityImgId));
        String activityContent = getString(getColumnIndex(ActivityTable.Cols.activityContent));
        String activityDate = getString(getColumnIndex(ActivityTable.Cols.activityDate));

        Activity activity = new Activity(activityId);
        activity.setActivityName(activityTitle);
        activity.setActivityImgId(activityImgId);
        activity.setActivityContent(activityContent);
        activity.setActivityDate(activityDate);
        return activity;
    }
}
