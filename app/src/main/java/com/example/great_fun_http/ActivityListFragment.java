package com.example.great_fun_http;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ActivityListFragment extends Fragment {
    private static final String SHOW_TYPE = "show_type";

    ViewPager mViewPager;
    Button mDiscoverBtn;
    Button mMineBtn;
    int userId;
    int typeFlag = -1;


    public static ActivityListFragment newInstance() {
        ActivityListFragment fragment = new ActivityListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_activity:
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // 后台执行获取数据代码
    private class GetActivitiesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return HttpApiHelper.getApiData("", "http://116.62.156.102:7080/android/getActivityList");
        }

        // 执行完成的回调
        @Override
        protected void onPostExecute(String jsonResult) {
            final List<Activity> activityAppList = new ArrayList<>();

            JSONObject jsonBody=null;
            int retCode=0;
            try {
                jsonBody=new JSONObject(jsonResult);
                retCode=jsonBody.getInt("retcode");
                if (retCode==0){
                    // 获取接口数据
                    JSONObject resObj = jsonBody.getJSONObject("obj");
                    JSONArray activityList = resObj.getJSONArray("activityList");
                    // 循环读取json数组的中的值
                    for (int i = 0; i < activityList.length(); i++) {
                        JSONObject activityObj = activityList.getJSONObject(i);
                        int activityId = activityObj.getInt("activityId");
                        String activityName = activityObj.getString("activityName");
                        String activityDate = activityObj.getString("activityDate");
                        String activityImgUrl = activityObj.getString("activityImgUrl");
                        String activityContent = activityObj.getString("activityContent");
                        Activity activity = new Activity(activityId);
                        activity.setActivityName(activityName);
                        activity.setActivityDate(activityDate);
                        activity.setActivityImgUrl(activityImgUrl);
                        activity.setActivityContent(activityContent);
                        activityAppList.add(activity);
                    }
                    // 设置控制器
                    FragmentManager fragmentManager = getChildFragmentManager();
                    mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
                        @Override
                        public Fragment getItem(int i) {
                            Activity showActivity = activityAppList.get(i);
                            return ActivityItemFragment.newInstance(
                                    showActivity.getActivityId(),
                                    showActivity.getActivityName(),
                                    showActivity.getActivityContent(),
                                    showActivity.getActivityImgUrl(),
                                    showActivity.getActivityDate()
                            );
                        }

                        @Override
                        public int getCount() {
                            return activityAppList.size();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    // 获取个人所有活动
    private class GetUserActivityTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String params = strings[0];
            return HttpApiHelper.getApiData(params, "http://116.62.156.102:7080/android/getUserActivity");
        }

        @Override
        protected void onPostExecute(String s) {
            final List<Activity> myActivityAppList = new ArrayList<>();
            JSONObject jsonBody;
            int retcode = 0;
            if (s.equals("")) {
                Toast.makeText(getActivity(), "服务器无返回", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jsonBody = new JSONObject(s);
                    retcode = jsonBody.getInt("retcode");
                    if (retcode == 0) {
                        JSONObject jsonRes = jsonBody.getJSONObject("obj");
                        JSONArray activityList = jsonRes.getJSONArray("activityList");
                        for (int i = 0; i < activityList.length(); i++) {
                            JSONObject activityItem = activityList.getJSONObject(i);
                            Activity activity = new Activity(
                                    activityItem.getInt("activityId"),
                                    activityItem.getString("activityName"),
                                    activityItem.getString("activityContent"),
                                    activityItem.getString("activityDate"),
                                    activityItem.getString("activityImgUrl"),
                                    activityItem.getInt("activityPostUser")
                            );
                            myActivityAppList.add(activity);
                        }
                        FragmentManager fragmentManager = getChildFragmentManager();
                        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
                            @Override
                            public Fragment getItem(int i) {
                                Activity showActivity = myActivityAppList.get(i);
                                return ActivityItemFragment.newInstance(
                                        showActivity.getActivityId(),
                                        showActivity.getActivityName(),
                                        showActivity.getActivityContent(),
                                        showActivity.getActivityImgUrl(),
                                        showActivity.getActivityDate()
                                );
                            }

                            @Override
                            public int getCount() {
                                return myActivityAppList.size();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_LONG).show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_list, container, false);
        // 设置ViewPager控件
        mViewPager = (ViewPager) view.findViewById(R.id.activity_list_viewpager);
        mViewPager.setPageMargin(30);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        mViewPager.setPageTransformer(true, new ScaleInTransformer(0.8f));

        // 默认执行发现视图
        getActivity().setTitle("发现");
        new GetActivitiesTask().execute();

        // 获取userId判断是否显示加号,能够发布活动
        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        if (userId == -1) {
            setHasOptionsMenu(false);
        } else {
            setHasOptionsMenu(true);
        }

        // "我的"按钮监听事件
        mMineBtn = (Button) view.findViewById(R.id.mineBtn);
        mMineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeFlag = 1;
                getActivity().setTitle("我的");
                String param = String.format("{ \"args\": { \"userId\": %s } }", userId);
                new GetUserActivityTask().execute(param);
            }
        });

        // "发现"按钮监听事件
        mDiscoverBtn = (Button) view.findViewById(R.id.discoverBtn);
        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeFlag = -1;
                getActivity().setTitle("发现");
                new GetActivitiesTask().execute();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        if (typeFlag == 1) {
            typeFlag = -1;
            getActivity().setTitle("我的");
            String param = String.format("{ \"args\": { \"userId\": %s } }", userId);
            new GetUserActivityTask().execute(param);
        }
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_activity_actions, menu);
    }
}
