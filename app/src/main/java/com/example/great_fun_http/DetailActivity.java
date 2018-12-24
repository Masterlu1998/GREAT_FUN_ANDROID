package com.example.great_fun_http;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    ImageView mActivityDetailImg;
    TextView mActivityDetailName;
    TextView mActivityDetailDate;
    TextView mActivityDetailContent;
    Button mJoinActivity;
    int activityId;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mActivityDetailImg = (ImageView) findViewById(R.id.activityDetailImg);
        mActivityDetailName = (TextView) findViewById(R.id.activityDetailName);
        mActivityDetailDate = (TextView) findViewById(R.id.activityDetailDate);
        mActivityDetailContent = (TextView) findViewById(R.id.activityDetailContent);
        mJoinActivity = (Button) findViewById(R.id.joinActivity);
        // 获取userId
        SharedPreferences preferences = this.getSharedPreferences("userInfo", this.MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        // 获取acticityId
        Intent intent = getIntent();
        activityId = intent.getIntExtra("activityId", 1);
        String param = String.format("{ \"args\": { \"activityId\": %s, \"userId\": %s } }", activityId, userId);
        new GetActivityDetailTask().execute(param);
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
            try {
                jsonBody = new JSONObject(jsonResult);
                retcode = jsonBody.getInt("retcode");
                if (retcode == 0) {
                    JSONObject resObj = jsonBody.getJSONObject("obj");

                    Picasso.get().load(resObj.getString("activityImgUrl")).into(mActivityDetailImg);
                    int isJoinFlag = resObj.getInt("isJoinFlag");
                    mActivityDetailName.setText(resObj.getString("activityName"));
                    mActivityDetailDate.setText(resObj.getString("activityDate"));
                    mActivityDetailContent.setText(resObj.getString("activityContent"));
                    if (isJoinFlag == 1) {
                        mJoinActivity.setText("取消活动");
                        mJoinActivity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mJoinActivity.setText("参加活动");
                                String params = String.format("{ \"args\": { \"activityId\": %s, \"userId\": %s, \"operation\": -1 } }", activityId, userId);
                                new DetailActivity.ModifyActivityTask().execute(params);
                            }
                        });
                    } else {
                        mJoinActivity.setText("参加活动");
                        mJoinActivity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mJoinActivity.setText("取消活动");
                                String params = String.format("{ \"args\": { \"activityId\": %s, \"userId\": %s, \"operation\": 1 } }", activityId, userId);
                                new DetailActivity.ModifyActivityTask().execute(params);
                            }
                        });
                    }

                } else {
                    Toast.makeText(DetailActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class ModifyActivityTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String param = strings[0];
            return HttpApiHelper.getApiData(param, "http://116.62.156.102:7080/android/modifyJoinStatus");
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == "") {
                Toast.makeText(DetailActivity.this,"服务器无数据", Toast.LENGTH_LONG).show();
            } else {
                JSONObject jsonBody;
                int retcode = 0;
                try {
                    jsonBody = new JSONObject(s);
                    retcode = jsonBody.getInt("retcode");
                    if (retcode == 0) {
                        Toast.makeText(DetailActivity.this,"修改成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
                        intent.putExtra("activityId", activityId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(DetailActivity.this,"服务器请求失败", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
