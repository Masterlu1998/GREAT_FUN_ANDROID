package com.example.great_fun_http;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener {

    private Fragment []mFragments = new Fragment[4];
    private FragmentTabHost mTabHost;
    private int tabIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragments = TabGenerator.getFragments();
        // 获取可能的参数
        Intent intent = getIntent();
        tabIndex = intent.getIntExtra("tabIndex", 0);
        // 初始化视图
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        // 关联tabHost
        mTabHost.setup(this, getSupportFragmentManager(), R.id.home_container);
        mTabHost.setOnTabChangedListener(this);

        for (int i = 0; i < 4; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(TabGenerator.mTabTitle[i]).setIndicator(TabGenerator.getTabContent(this, i));
            Bundle bundle = new Bundle();
            bundle.putString("from", "FragmentTabHost Tab");
            mTabHost.addTab(tabSpec,mFragments[i].getClass(),bundle);
        }

        //去掉Tab 之间的分割线
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setCurrentTab(tabIndex);
    }

    @Override
    public void onTabChanged(String tabId) {
        TabWidget tabWidget = mTabHost.getTabWidget();
        for (int i=0;i<tabWidget.getTabCount();i++){
            View view = tabWidget.getChildTabViewAt(i);
            ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
            TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
            if(i == mTabHost.getCurrentTab()){
                setTitle(TabGenerator.mTabTitle[i]);
                tabIcon.setImageResource(TabGenerator.mTabImgSelected[i]);
                tabText.setTextColor(getResources().getColor(R.color.colorPrimaryAlpha));
            }else{
                tabIcon.setImageResource(TabGenerator.mTabImg[i]);
                tabText.setTextColor(getResources().getColor(R.color.colorSupportText));
            }
        }
    }
}
