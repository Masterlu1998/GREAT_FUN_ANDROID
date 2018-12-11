package com.example.great_fun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.great_fun.dbPackage.ActivityCursorWrapper;
import com.example.great_fun.dbPackage.ActivityDbSchema.ActivityTable;

import com.example.great_fun.dbPackage.GreatFunBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollection {
    private static ActivityCollection sActivityCollection;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    // 初始化数据库
    public void initActivityTable() {
        for (int i = 0; i < 3; i++) {
            Log.d("测试:", activityCardTitle[i]);
            this.addActivity(new Activity(i, activityCardTitle[i], imgRes[i], activityCardContent[i], activityCardDate[i]));
        }

    }

    // 删除数据库
    public void destroyActivityTable() {
        for (int i = 0; i < 3; i++) {
            String activityId = Integer.toString(i);
            mDatabase.delete(ActivityTable.NAME, ActivityTable.Cols.activityId + "=?", new String[] { activityId });
        }
    }

    // 获取上下文实例化活动集合
    public static ActivityCollection get(Context context) {
        if(sActivityCollection == null) {
            sActivityCollection = new ActivityCollection(context);
        }
        return sActivityCollection;
    }


    public ActivityCollection(Context context) {
        mContext = context;
        mDatabase = new GreatFunBaseHelper(mContext).getWritableDatabase();
    }

    // 添加活动
    public void addActivity(Activity activity) {
        ActivityCursorWrapper cursor = queryActivity(null, null);
        int autoId = cursor.getCount();
        Log.w("测试", Integer.toString(autoId));

        activity.setActivityId(autoId);
        ContentValues values = getContentValues(activity);

        mDatabase.insert(ActivityTable.NAME, null, values);
    }

    // 更新活动
    public void updateActivity(Activity activity) {
        String avtivityIdStr = Integer.toString(activity.getActivityId());
        ContentValues values = getContentValues(activity);
        mDatabase.update(ActivityTable.NAME, values, ActivityTable.Cols.activityId + " = ?", new String[] { avtivityIdStr });
    }

    // 获取所有活动页面
    public List<Activity> getActivities() {
        List<Activity> activities = new ArrayList<>();

        ActivityCursorWrapper cursor = queryActivity(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                activities.add(cursor.getActivity());

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return activities;
    }

    // 根据活动ID寻找参数
    public Activity getActivityById(int activityId) {
        String activityIdStr = Integer.toString(activityId);
        ActivityCursorWrapper cursor = queryActivity(ActivityTable.Cols.activityId + " = ?", new String[] {activityIdStr});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getActivity();
        } finally {
            cursor.close();
        }

    }

    // 获取数量
    public int getCount() {
        ActivityCursorWrapper cursor = queryActivity(null, null);
        return cursor.getCount();
    }

    // CursorWrapper
    private ActivityCursorWrapper queryActivity(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ActivityTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new ActivityCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Activity activity) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.Cols.activityId, Integer.toString(activity.getActivityId()));
        values.put(ActivityTable.Cols.activityTitle, activity.getActivityTitle());
        values.put(ActivityTable.Cols.activityImgId, activity.getActivityImgId());
        values.put(ActivityTable.Cols.activityContent, activity.getActivityContent());
        values.put(ActivityTable.Cols.activityDate, activity.getActivityDate());
        return values;
    }


    private static int size = 3;
    private static String []activityCardTitle = new String[] {
        "伊利奥斯",
        "国王大道",
        "漓江塔"
    };
    private static int []imgRes = new int[] {
            R.mipmap.activity_img_a,
            R.mipmap.activity_img_b,
            R.mipmap.activity_img_c
    };

    private static String []activityCardContent = new String[] {
            "伊利奥斯的简单介绍伊利奥斯的简单介绍伊利奥斯的简单介绍伊利奥斯的简单介绍,伊利奥斯的简单介绍伊利奥",
            "国王大道的简单介绍,国王大道的简单介绍,国王大道的简单介绍,国王大道的简单介绍,国王大道的简单介绍",
            "国王大道的简单介绍,国王大道的简单介绍,国王大道的简单介绍,国王大道的简单介绍,国王大道的"
    };

    private static String []activityCardDate = new String[] {
            "2018-08-12",
            "2018-08-14",
            "2018-08-15",
    };



    public static int getSize() {
        return size;
    }


}
