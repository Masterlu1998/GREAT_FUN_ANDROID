package com.example.great_fun;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TabGenerator {
    public static final int []mTabImg = new int[] {
            R.mipmap.ic_homepage,
            R.mipmap.ic_discover,
            R.mipmap.ic_friend,
            R.mipmap.ic_person
    };
    public static final int []mTabImgSelected = new int[] {
            R.mipmap.ic_homepage_selected,
            R.mipmap.ic_discover_selected,
            R.mipmap.ic_friend_selected,
            R.mipmap.ic_person_selected
    };
    public static final String []mTabTitle = new String[] {
            "首页",
            "发现",
            "好友",
            "个人"
    };

    public static Fragment[] getFragments() {
        Fragment fragments[] = new Fragment[4];
        fragments[0] = HomepageFragment.newInstance();
        fragments[1] = ActivityListFragment.newInstance();
        fragments[2] = FriendListFragment.newInstance();
        fragments[3] = PersonDetailFragment.newInstance();
        return fragments;
    }

    // 获取Tab的fragment内容
    public static View getTabContent(Context context, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_tab_content,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(mTabImg[i]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[i]);
        return view;
    }
}
