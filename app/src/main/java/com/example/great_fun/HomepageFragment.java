package com.example.great_fun;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.magicviewpager.transformer.ScaleInTransformer;

public class HomepageFragment extends Fragment {

    ViewPager mBannerViewPager;
    ViewPager mActivityViewPager;

    private static final int []mPics = new int[] {
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

        // 活动卡片轮播
        mActivityViewPager = (ViewPager) view.findViewById(R.id.activity_view_pager);
        mActivityViewPager.setPageMargin(60);
        mActivityViewPager.setOffscreenPageLimit(3);
        FragmentManager activityFragmentManager = getChildFragmentManager();
        mActivityViewPager.setAdapter(new FragmentStatePagerAdapter(activityFragmentManager) {
            @Override
            public Fragment getItem(int i) {
                ActivityCollection activityCollection = ActivityCollection.get(getActivity().getApplicationContext());
//                activityCollection.getCount();
                int remain = i % activityCollection.getCount();
                return ActivityCardFragment.newInstance(remain);
            }

            @Override
            public int getCount() {
//                ActivityCollection activityCollection = ActivityCollection.get(getActivity().getApplicationContext());
//                int remain = i % activityCollection.getCount();
//                return activityCollection.getCount();
                return 1000;
            }
        });
        mActivityViewPager.setCurrentItem(1);
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
                int remain =  i % mPics.length;
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
        return view;
    }
}


