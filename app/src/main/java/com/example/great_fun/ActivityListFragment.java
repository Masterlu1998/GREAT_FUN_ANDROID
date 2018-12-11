package com.example.great_fun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.magicviewpager.transformer.ScaleInTransformer;

public class ActivityListFragment extends Fragment {

    ViewPager mViewPager;

    public static ActivityListFragment newInstance() {
        ActivityListFragment fragment = new ActivityListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_activity:
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_list, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.activity_list_viewpager);
        mViewPager.setPageMargin(30);
        mViewPager.setOffscreenPageLimit(3);
        FragmentManager fragmentManager = getChildFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            ActivityCollection activityCollection = ActivityCollection.get(getActivity().getApplicationContext());
            @Override
            public Fragment getItem(int i) {
                return ActivityItemFragment.newInstance(i);
            }

            @Override
            public int getCount() {
                return activityCollection.getCount();
            }
        });
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
