package com.example.great_fun_http;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

public class ActivityItemFragment extends Fragment {

    private static final String ACTIVITY_ID = "activity_id";
    private static final String ACTIVITY_NAME = "activity_name";
    private static final String ACTIVITY_CONTENT = "activity_content";
    private static final String ACTIVITY_DATE = "activity_date";
    private static final String ACTIVITY_IMG_URL = "activity_img_url";

    int mActivityId;
    String mActivityName;
    String mActivityContent;
    String mActivityDate;
    String mActivityImgUrl;

    // 实例化单个页面的fragment并传入参数
    public static ActivityItemFragment newInstance(int activityId, String activityName, String activityContent, String activityImgUrl, String activityDate) {
        Bundle args = new Bundle();
        args.putSerializable(ACTIVITY_ID, activityId);
        args.putSerializable(ACTIVITY_NAME, activityName);
        args.putSerializable(ACTIVITY_CONTENT, activityContent);
        args.putSerializable(ACTIVITY_DATE, activityDate);
        args.putSerializable(ACTIVITY_IMG_URL, activityImgUrl);
        ActivityItemFragment fragment = new ActivityItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityId = (int) getArguments().getSerializable(ACTIVITY_ID);
        mActivityName = (String) getArguments().getSerializable(ACTIVITY_NAME);
        mActivityContent = (String) getArguments().getSerializable(ACTIVITY_CONTENT);
        mActivityDate = (String) getArguments().getSerializable(ACTIVITY_DATE);
        mActivityImgUrl = (String) getArguments().getSerializable(ACTIVITY_IMG_URL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取模板
        View view = inflater.inflate(R.layout.fragment_activity_item, container, false);
        // 获取元素并赋值
        ImageView imageView = (ImageView) view.findViewById(R.id.activityImg);
        Picasso.get().load(mActivityImgUrl).resize(800, 600).into(imageView);
        TextView textViewTitle = (TextView) view.findViewById(R.id.activityName);
        textViewTitle.setText(mActivityName);
        TextView textViewContent = (TextView) view.findViewById(R.id.activityContent);
        textViewContent.setText(mActivityContent);
        TextView textViewDate = (TextView) view.findViewById(R.id.activityDate);
        textViewDate.setText(mActivityDate);
        TextView moreActivityDetail = (TextView) view.findViewById(R.id.moreActivityDetail);
        // 设置点击事件
        moreActivityDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("activityId", mActivityId);
                startActivity(intent);
            }
        });
        return view;
    }
}
