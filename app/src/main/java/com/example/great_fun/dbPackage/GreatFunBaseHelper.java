package com.example.great_fun.dbPackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.great_fun.Activity;
import com.example.great_fun.dbPackage.ActivityDbSchema.ActivityTable;

public class GreatFunBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "greatFun";

    public GreatFunBaseHelper (Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ActivityTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ActivityTable.Cols.activityId + ", " +
                ActivityTable.Cols.activityTitle + ", " +
                ActivityTable.Cols.activityImgId + ", " +
                ActivityTable.Cols.activityContent + ", " +
                ActivityTable.Cols.activityDate + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
