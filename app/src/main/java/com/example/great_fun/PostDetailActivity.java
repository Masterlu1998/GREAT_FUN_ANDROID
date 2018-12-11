package com.example.great_fun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class PostDetailActivity extends AppCompatActivity {

    EditText mPostActivityName;
    EditText mPostActivityDate;
    EditText mPostActivityContent;
    ImageView mPostActivityImg;
    int activityId;
    int modifiedFlag = 0; // 0：表示新增 1：表示修改
    int imgRes = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // 获取控件
        Intent intent = getIntent();
        activityId = intent.getIntExtra("activityId", -1);
        mPostActivityName = (EditText) findViewById(R.id.postActivityName);
        mPostActivityDate = (EditText) findViewById(R.id.postAcitivityDate);
        mPostActivityContent = (EditText) findViewById(R.id.postActivityContent);
        mPostActivityImg = (ImageView) findViewById(R.id.postActivityImg);

        if (activityId != -1) {
            // 说明是修改活动信息
            modifiedFlag = 1;

            // 根据id提取相应活动信息并赋值
            ActivityCollection activityCollection = ActivityCollection.get(getApplicationContext());
            Activity activity = activityCollection.getActivityById(activityId);
            mPostActivityImg.setImageResource(activity.getActivityImgId());
            mPostActivityName.setText(activity.getActivityTitle());
            mPostActivityDate.setText(activity.getActivityDate());
            mPostActivityContent.setText(activity.getActivityContent());
            // TODO:暂时将当前图片记录下来
            imgRes = activity.getActivityImgId();
        } else {
            // 说明是新增活动
            modifiedFlag = 0;
        }

        // 设定点击事件
        Button postActivityBtn = (Button) findViewById(R.id.postActivityBtn);
        postActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取EditText中的值
                String activityName = mPostActivityName.getText().toString();
                String activityDate = mPostActivityDate.getText().toString();
                String activityContent = mPostActivityContent.getText().toString();

                // 实例化活动控制集
                ActivityCollection activityCollection = ActivityCollection.get(getApplicationContext());
                // TODO: 图片暂时写死，不知道咋办
                if (modifiedFlag == 1) {
                    // 修改活动
                    Activity newActivity = new Activity(activityId, activityName, imgRes, activityContent, activityDate);
                    activityCollection.updateActivity(newActivity);
                    getSupportFragmentManager()
                              .beginTransaction()
                              .replace(R.id.fragment_container,PersonDetailFragment.newInstance(),null)
                              .commit();
                } else {
                    // 新增活动
                    Activity newActivity = new Activity(activityId, activityName, R.mipmap.default_img, activityContent, activityDate);
                    activityCollection.addActivity(newActivity);
                }
            }
        });
    }


}
