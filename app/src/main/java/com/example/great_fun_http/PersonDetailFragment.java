package com.example.great_fun_http;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonDetailFragment extends Fragment {
    private ListView mListView;
    private TextView mUserContentTV;
    private TextView mUserNameTV;
    private CircleImageView mUserHeadImg;
    private Button mLogoutBtn;
    int userId;
    String userHeadImg;
    String userContent;
    String userName;


    public static PersonDetailFragment newInstance() {
        PersonDetailFragment fragment = new PersonDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        View view = inflater.inflate(R.layout.fragment_person_detail, container, false);
        mListView = (ListView) view.findViewById(R.id.personActivityList);
        mUserContentTV = (TextView) view.findViewById(R.id.userContent);
        mUserNameTV = (TextView) view.findViewById(R.id.userName);
        mUserHeadImg = (CircleImageView) view.findViewById(R.id.userHeadImg);
        mUserHeadImg.setImageResource(R.mipmap.default_img);
        mLogoutBtn = (Button) view.findViewById(R.id.logoutBtn);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = -1;
                userHeadImg = null;
                userContent = "未登录";
                userName = "未登录";
                Intent intent = new Intent(getActivity(), AppLoginActivity.class);
                startActivityForResult(intent, 1);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putInt("userId", userId);
                editor.apply();
            }
        });
        if (userId == -1) {
            // 未登录
            Intent intent = new Intent(getActivity(), AppLoginActivity.class);
            startActivityForResult(intent, 1);
        } else {
            // 已登录
            userHeadImg = preferences.getString("userHeadImg", "");
            userName = preferences.getString("userName", "");
            userContent = preferences.getString("userContent", "");

            String param = String.format("{ \"args\": { \"userId\": %s } }", userId);
            new PersonDetailFragment.getUserActivityListTask().execute(param);
            Picasso.get().load(userHeadImg).into(mUserHeadImg);
            mUserNameTV.setText(userName);
            mUserContentTV.setText(userContent);
        }

        return view;
    }


    // 完成登录回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                userId = data.getExtras().getInt("userId");
                userName = data.getExtras().getString("userName");
                userHeadImg = data.getExtras().getString("userHeadImg");
                userContent = data.getExtras().getString("userContent");
                Picasso.get().load(userHeadImg).into(mUserHeadImg);
                mUserNameTV.setText(userName);
                mUserContentTV.setText(userContent);
                String param = String.format("{ \"args\": { \"userId\": %s } }", userId);
                new PersonDetailFragment.getUserActivityListTask().execute(param);
        }
    }

    // 获取用户发布的活动列表
    public class getUserActivityListTask extends AsyncTask<String, Void, String> {

        final List<Activity> mActivityList = new ArrayList<>();


        @Override
        protected String doInBackground(String... params) {
            String param = params[0];
            return HttpApiHelper.getApiData(param, "http://116.62.156.102:7080/android/getUserActivityList");
        }

        @Override
        protected void onPostExecute(String resultObj) {
            JSONObject jsonBody = null;
            int retcode = 0;
            try {
                jsonBody = new JSONObject(resultObj);
                retcode = jsonBody.getInt("retcode");
                if (retcode == 0) {
                    JSONObject resObj = jsonBody.getJSONObject("obj");
                    JSONArray userActivityList = resObj.getJSONArray("activityList");
                    for (int i = 0; i < userActivityList.length(); i++) {
                        JSONObject activityItem = userActivityList.getJSONObject(i);
                        Activity showActivity = new Activity(
                                activityItem.getInt("activityId"),
                                activityItem.getString("activityName"),
                                activityItem.getString("activityContent"),
                                activityItem.getString("activityDate"),
                                activityItem.getString("activityImgUrl"),
                                activityItem.getInt("activityPostUser")
                        );
                        mActivityList.add(showActivity);
                    }

                    List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                    for (Activity activity : mActivityList) {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("activityId", activity.getActivityId());
                        item.put("activityName", activity.getActivityName());
                        item.put("activityContent", activity.getActivityContent());
                        item.put("activityDate", activity.getActivityDate());
                        item.put("activityImgUrl",activity.getActivityImgUrl());
                        data.add(item);
                    }
                    SimpleAdapter adapter = new ActivitySimpleAdapter(
                            getContext(),
                            data,
                            R.layout.fragment_person_activity_item,
                            new String[] { "activityName", "activityContent", "activityDate", "activityImgUrl" },
                            new int[] { R.id.personActivityTitle, R.id.personActivityContent, R.id.personActivityDate, R.id.personActivityImg }
                    );
                    mListView.setAdapter(adapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                            intent.putExtra("activityId", mActivityList.get(position).getActivityId());
                            startActivity(intent);
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_LONG).show();

                }

            } catch(JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
