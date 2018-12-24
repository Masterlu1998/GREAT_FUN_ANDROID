package com.example.great_fun_http;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class PostDetailActivity extends AppCompatActivity {

    EditText mPostActivityName;
    EditText mPostActivityDate;
    EditText mPostActivityContent;
    ImageView mPostActivityImg;
    int activityId;
    int modifiedFlag = 0; // 0：表示新增 1：表示修改
    String imgUrl;
    int userId = -1;
    // 全局存储数据类型

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // 从全局变量中获取userId
        SharedPreferences preferences = this.getSharedPreferences("userInfo", this.MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        // 获取控件
        Intent intent = getIntent();
        activityId = intent.getIntExtra("activityId", -1);
        mPostActivityName = (EditText) findViewById(R.id.postActivityName);
        mPostActivityDate = (EditText) findViewById(R.id.postAcitivityDate);
        mPostActivityContent = (EditText) findViewById(R.id.postActivityContent);
        mPostActivityImg = (ImageView) findViewById(R.id.postActivityImg);
        Button postActivityBtn = (Button) findViewById(R.id.postActivityBtn);

        if (activityId != -1) {
            // 说明是修改活动信息
            modifiedFlag = 1;
            String param = String.format("{ \"args\": { \"activityId\": %s } }", activityId);
            new GetActivityDetailTask().execute(param);
            postActivityBtn.setText("修改活动");
        } else {
            // 说明是新增活动
            setTitle("新增活动");
            postActivityBtn.setText("新增活动");

            modifiedFlag = 0;
        }

        // 设定点击事件
        postActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取EditText中的值
                String activityName = mPostActivityName.getText().toString();
                String activityDate = mPostActivityDate.getText().toString();
                String activityContent = mPostActivityContent.getText().toString();

                // 实例化活动控制集
                // TODO: 图片暂时写死，不知道咋办
                if (imgUrl == null) {
                    imgUrl = "http://116.62.156.102:7080/images/activity_img_c.jpeg";
                }
                if (modifiedFlag == 1) {
                    // 修改活动
                    String params = String.format("{ \"args\": { \"activityId\": %s, \"activityName\": \"%s\", \"activityContent\": \"%s\", \"activityDate\": \"%s\", \"activityImgUrl\": \"%s\", \"activityPostUser\": %s } }", activityId, activityName, activityContent, activityDate, imgUrl, userId);
                    new PostDetailActivity.PostActivityTask().execute(params);
                } else {
                    // 新增活动
                    String params = String.format("{ \"args\": { \"activityName\": \"%s\", \"activityContent\": \"%s\", \"activityDate\": \"%s\", \"activityImgUrl\": \"%s\", \"activityPostUser\": %s } }", activityName, activityContent, activityDate, imgUrl, userId);
                    new PostDetailActivity.PostActivityTask().execute(params);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public class GetActivityDetailTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String param = params[0];
            return HttpApiHelper.getApiData(param, "http://116.62.156.102:7080/android/getActivityDetail");
        }

        @Override
        protected void onPostExecute(String jsonResult) {

            JSONObject jsonBody = null;
            int retcode = 0;
            if (jsonResult == null) {
                Toast.makeText(PostDetailActivity.this, "接口调用失败", Toast.LENGTH_LONG).show();
            }
            try {
                jsonBody = new JSONObject(jsonResult);
                retcode = jsonBody.getInt("retcode");
                JSONObject resObj = jsonBody.getJSONObject("obj");
                if (retcode == 0) {
                    imgUrl = resObj.getString("activityImgUrl");
                    Picasso.get().load(imgUrl).resize(800, 600).into(mPostActivityImg);
                    mPostActivityName.setText(resObj.getString("activityName"));
                    mPostActivityDate.setText(resObj.getString("activityDate"));
                    mPostActivityContent.setText(resObj.getString("activityContent"));
                } else {
                    Toast.makeText(PostDetailActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class PostActivityTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String param = params[0];
            return HttpApiHelper.getApiData(param, "http://116.62.156.102:7080/android/postActivity");
        }

        @Override
        protected void onPostExecute(String resultObj) {

            JSONObject jsonBody = null;
            int retcode = 0;
            try {
                jsonBody = new JSONObject(resultObj);
                retcode = jsonBody.getInt("retcode");
                if (modifiedFlag == 1) {
                    if (retcode == 0) {
                        Toast.makeText(PostDetailActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PostDetailActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(PostDetailActivity.this, MainActivity.class);
                    intent.putExtra("tabIndex", 3);
                    startActivity(intent);
                } else {
                    if (retcode == 0) {
                        Toast.makeText(PostDetailActivity.this, "发布成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PostDetailActivity.this, "发布失败", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(PostDetailActivity.this, MainActivity.class);
                    intent.putExtra("tabIndex", 1);
                    startActivity(intent);
                }


            } catch(JSONException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://增加点击事件
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
