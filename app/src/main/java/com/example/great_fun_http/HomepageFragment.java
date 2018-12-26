package com.example.great_fun_http;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;
import com.squareup.picasso.Picasso;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomepageFragment extends Fragment {

    // 两个轮播
    ViewPager mBannerViewPager;
    ViewPager mActivityViewPager;
    int isInit = 0;

    // 四个用户卡片
    int []userHeadImgIdArray = new int[] {
            R.id.user_head_a,
            R.id.user_head_b,
            R.id.user_head_c,
            R.id.user_head_d,
    };

    int []userContentIdArray = new int[] {
            R.id.user_content_a,
            R.id.user_content_b,
            R.id.user_content_c,
            R.id.user_content_d,
    };

    int []userNameIdArray = new int[] {
            R.id.user_name_a,
            R.id.user_name_b,
            R.id.user_name_c,
            R.id.user_name_d,
    };

    List<CircleImageView> mUserHeadList = new ArrayList<>();
    List<TextView> mUserNameList = new ArrayList<>();
    List<TextView> mUserContentList = new ArrayList<>();



    private static final int[] mPics = new int[]{
            R.mipmap.banner_alpha,
            R.mipmap.banner_beta,
            R.mipmap.banner_gama
    };

    // fragment实例化方法
    public static Fragment newInstance() {
        HomepageFragment fragment = new HomepageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        // 获取用户卡片元素
        if (isInit == 0 ) {
            isInit = 1;
            for (int i = 0; i < 4; i++) {
                CircleImageView userHeadImageView = (CircleImageView) view.findViewById(userHeadImgIdArray[i]);
                TextView userNameTextView = (TextView) view.findViewById(userNameIdArray[i]);
                TextView userContentTextView = (TextView) view.findViewById(userContentIdArray[i]);
                mUserHeadList.add(userHeadImageView);
                mUserNameList.add(userNameTextView);
                mUserContentList.add(userContentTextView);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                CircleImageView userHeadImageView = (CircleImageView) view.findViewById(userHeadImgIdArray[i]);
                TextView userNameTextView = (TextView) view.findViewById(userNameIdArray[i]);
                TextView userContentTextView = (TextView) view.findViewById(userContentIdArray[i]);
                mUserHeadList.set(i, userHeadImageView);
                mUserNameList.set(i, userNameTextView);
                mUserContentList.set(i, userContentTextView);
            }
        }


        // 活动卡片轮播
        mActivityViewPager = (ViewPager) view.findViewById(R.id.activity_view_pager);
        mActivityViewPager.setPageMargin(60);
        mActivityViewPager.setOffscreenPageLimit(3);
        mActivityViewPager.setPageTransformer(true, new ScaleInTransformer(0.8f));

        // 头部卡片轮播
        mBannerViewPager = (ViewPager) view.findViewById(R.id.banner_view_pager);
        // 设置轮播图间距
        mBannerViewPager.setPageMargin(60);
        // 设置缓存轮播图数量
        mBannerViewPager.setOffscreenPageLimit(3);
        // 防止二次进入空白
        FragmentManager bannerFragmentManager = getChildFragmentManager();
        mBannerViewPager.setAdapter(new FragmentStatePagerAdapter(bannerFragmentManager) {
            @Override
            public Fragment getItem(int i) {
                int remain = i % mPics.length;
                return BannerFragment.newInstance(mPics[remain]);
            }

            @Override
            public int getCount() {
                return 10;
            }
        });
        // 设置初始化banner
        mBannerViewPager.setCurrentItem(2 * 3);
        // 设置伸缩动画效果
        mBannerViewPager.setPageTransformer(true, new ScaleInTransformer(0.8f));

        // 启动后台任务
        new HomepageFragment.GetDataTask().execute();

        return view;
    }

    private class GetDataTask extends AsyncTask<String, Void, String> {

        final List<User> mUserList = new ArrayList<>();
        final List<Activity> mActivityList = new ArrayList<>();
        private String activityJsonResult = "";
        private String userJsonResult = "";

        @Override
        protected String doInBackground(String... strings) {
            userJsonResult = HttpApiHelper.getApiData(
                    "{\n" +
                    "\"args\": {\n" +
                        "\"limit\": 4\n" +
                    "    }\n" +
                    "}", "http://116.62.156.102:7080/android/getUserList");
            activityJsonResult = HttpApiHelper.getApiData("", "http://116.62.156.102:7080/android/getActivityList");
            return "";
        }

        @Override
        protected void onPostExecute(String s) {


            JSONObject jsonUserBody=null;
            JSONObject jsonActivityBody = null;
            int userRetCode=0;
            int activityRetCode = 0;
            try {
                jsonUserBody = new JSONObject(userJsonResult);
                userRetCode = jsonUserBody.getInt("retcode");
                jsonActivityBody = new JSONObject(activityJsonResult);
                activityRetCode = jsonActivityBody.getInt("retcode");
                if (userRetCode == 0) {
                    // 获取接口数据
                    JSONObject resObj = jsonUserBody.getJSONObject("obj");
                    JSONArray userJsonArray = resObj.getJSONArray("userList");
                    for (int i = 0; i < userJsonArray.length(); i++) {
                        Log.d("执行", Integer.toString(i));
                        JSONObject userObj = userJsonArray.getJSONObject(i);
                        int userId = userObj.getInt("userId");
                        String userName = userObj.getString("userName");
                        String userContent = userObj.getString("userContent");
                        String userHeadImg = userObj.getString("userHeadImg");

                        Picasso.get().load(userHeadImg).resize(75, 75).into(mUserHeadList.get(i));
                        mUserNameList.get(i).setText(userName);
                        mUserContentList.get(i).setText(userContent);
                    }
                } else {
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_LONG).show();

                }
                if (activityRetCode == 0) {
                    JSONObject resObj = jsonActivityBody.getJSONObject("obj");
                    JSONArray activityJsonArray = resObj.getJSONArray("activityList");
                    // 循环读取json数组的中的值
                    for (int i = 0; i < activityJsonArray.length(); i++) {
                        JSONObject activityObj = activityJsonArray.getJSONObject(i);
                        Activity activityItem = new Activity(
                                activityObj.getInt("activityId"),
                                activityObj.getString("activityName"),
                                activityObj.getString("activityContent"),
                                activityObj.getString("activityDate"),
                                activityObj.getString("activityImgUrl"),
                                activityObj.getInt("activityPostUser")
                        );
                        mActivityList.add(activityItem);
                    }
                    FragmentManager activityFragmentManager = getChildFragmentManager();
                    mActivityViewPager.setAdapter(new FragmentStatePagerAdapter(activityFragmentManager) {
                        @Override
                        public Fragment getItem(int i) {
                            int remain = i % mActivityList.size();
                            Activity showActivity = mActivityList.get(remain);
                            return ActivityCardFragment.newInstance(
                                    showActivity.getActivityId(),
                                    showActivity.getActivityName(),
                                    showActivity.getActivityDate(),
                                    showActivity.getActivityImgUrl()
                            );
                        }

                        @Override
                        public int getCount() {
                            return 1000;
                        }
                    });
                    mActivityViewPager.setCurrentItem(200);


                } else {
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


