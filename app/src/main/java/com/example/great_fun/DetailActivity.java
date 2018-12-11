package com.example.great_fun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    ImageView mActivityDetailImg;
    TextView mActivityDetailName;
    TextView mActivityDetailDate;
    TextView mActivityDetailContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mActivityDetailImg = (ImageView) findViewById(R.id.activityDetailImg);
        mActivityDetailName = (TextView) findViewById(R.id.activityDetailName);
        mActivityDetailDate = (TextView) findViewById(R.id.activityDetailDate);
        mActivityDetailContent = (TextView) findViewById(R.id.activityDetailContent);
        Intent intent = getIntent();
        int activityId = intent.getIntExtra("activityId", 1);
        ActivityCollection activityCollection = ActivityCollection.get(getApplicationContext());
        Activity activity = activityCollection.getActivityById(activityId);
        mActivityDetailImg.setImageResource(activity.getActivityImgId());
        mActivityDetailName.setText(activity.getActivityTitle());
        mActivityDetailDate.setText(activity.getActivityDate());
        mActivityDetailContent.setText(activity.getActivityContent());

    }
}
