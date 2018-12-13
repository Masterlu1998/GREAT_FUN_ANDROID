package com.example.great_fun_http;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        String param = String.format("{\n" +
                "  \"args\": {\n" +
                "    \"activityId\": %s\n" +
                "  }\n" +
                "}", activityId);
        new GetActivityDetailTask().execute(param);
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
                    mActivityDetailName.setText(resObj.getString("activityName"));
                    mActivityDetailDate.setText(resObj.getString("activityDate"));
                    mActivityDetailContent.setText(resObj.getString("activityContent"));
                } else {
                    Toast.makeText(DetailActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
