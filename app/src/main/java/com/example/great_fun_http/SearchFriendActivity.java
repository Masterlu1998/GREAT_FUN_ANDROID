package com.example.great_fun_http;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFriendActivity extends AppCompatActivity {

    Button mSearchBtn;
    EditText mSearchKeywordET;
    ListView mSearchFriendList;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        SharedPreferences preferences = this.getSharedPreferences("userInfo", this.MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        mSearchBtn = (Button) findViewById(R.id.search);
        mSearchKeywordET = (EditText) findViewById(R.id.searchKeywords);
        mSearchFriendList = (ListView) findViewById(R.id.searchFriendList);


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywords = mSearchKeywordET.getText().toString();
                String params = String.format("{ \"keywords\": \"%s\", \"userId\": %s }", keywords, userId);
                new GetSearchFriendResult().execute(params);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public class GetSearchFriendResult extends AsyncTask<String, Void, String> {

        final List<User> mUserList = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            String params = strings[0];
            return HttpApiHelper.getApiData(params, "http://116.62.156.102:7080/android/getSearchFriendResult");
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject jsonBody = null;
            int retcode = 0;
            try {
                jsonBody = new JSONObject(s);
                retcode = jsonBody.getInt("retcode");
                if (retcode != 0) {
                    Toast.makeText(SearchFriendActivity.this, "服务器无返回", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject resObj = jsonBody.getJSONObject("obj");
                    JSONArray friendSearchList = resObj.getJSONArray("searchFriendList");
                    if (friendSearchList.length() == 0) {
                        Toast.makeText(SearchFriendActivity.this, "无用户", Toast.LENGTH_LONG).show();
                    }
                    for (int i = 0; i < friendSearchList.length(); i++) {
                        JSONObject friendItem = friendSearchList.getJSONObject(i);
                        User user = new User(
                                friendItem.getInt("userId"),
                                friendItem.getString("userName"),
                                friendItem.getString("userContent"),
                                friendItem.getString("userHeadImg")
                        );
                        mUserList.add(user);
                    }

                    List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                    for (User user : mUserList) {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("userId", user.getUserId());
                        item.put("userName", user.getUserName());
                        item.put("userContent", user.getUserContent());
                        item.put("userHeadImg", user.getUserHeadImg());
                        data.add(item);
                    }
                    SimpleAdapter adapter = new FriendSimpleAdapter(
                            SearchFriendActivity.this,
                            data,
                            R.layout.list_item_search_friend,
                            new String[] { "userName", "userContent", "userHeadImg" },
                            new int[] { R.id.searchFriendName, R.id.searchFriendContent, R.id.searchFriendImg }
                    );
                    mSearchFriendList.setAdapter(adapter);

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
