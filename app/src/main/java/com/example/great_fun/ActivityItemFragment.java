package com.example.great_fun;

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

import java.util.List;

public class ActivityItemFragment extends Fragment {

    private static final String ACTIVITY_CARD_ID = "activity_card_id";
    private int mActivityCardId;
    private Activity mActivity;

    // 实例化单个页面的fragment并传入参数
    public static ActivityItemFragment newInstance(int activityCardId) {
        Bundle args = new Bundle();
        args.putSerializable(ACTIVITY_CARD_ID, activityCardId);
        ActivityItemFragment fragment = new ActivityItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCardId = (int) getArguments().getSerializable(ACTIVITY_CARD_ID);
        ActivityCollection activityCollection = ActivityCollection.get(getActivity().getApplicationContext());
        mActivity = (Activity) activityCollection.getActivityById(mActivityCardId);
        Log.d("数据库条数：", Integer.toString(activityCollection.getCount()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.activityImg);
        imageView.setImageResource(mActivity.getActivityImgId());
        TextView textViewTitle = (TextView) view.findViewById(R.id.activityTitle);
        textViewTitle.setText(mActivity.getActivityTitle());
        TextView textViewContent = (TextView) view.findViewById(R.id.activityContent);
        textViewContent.setText(mActivity.getActivityContent());
        TextView textViewDate = (TextView) view.findViewById(R.id.activityDate);
        textViewDate.setText(mActivity.getActivityDate());
        TextView moreActivityDetail = (TextView) view.findViewById(R.id.moreActivityDetail);
        moreActivityDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("activityId", mActivityCardId);
                startActivity(intent);
            }
        });
        return view;
    }
}
