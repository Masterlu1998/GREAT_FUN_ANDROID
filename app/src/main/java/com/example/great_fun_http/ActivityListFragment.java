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
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityListFragment extends Fragment {

    ViewPager mViewPager;
    int userId;


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





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_list, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.activity_list_viewpager);
        mViewPager.setPageMargin(30);
        mViewPager.setOffscreenPageLimit(3);

        new GetActivitiesTask().execute();

        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        Log.d("userId", Integer.toString(userId));
        if (userId == -1) {
            setHasOptionsMenu(false);
        } else {
            setHasOptionsMenu(true);
        }

        mViewPager.setCurrentItem(0);
        mViewPager.setPageTransformer(true, new ScaleInTransformer(0.8f));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_activity_actions, menu);
    }
}
