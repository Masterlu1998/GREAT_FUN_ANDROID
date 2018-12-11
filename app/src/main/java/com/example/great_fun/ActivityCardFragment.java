package com.example.great_fun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ActivityCardFragment extends Fragment {

    private static final String ACTIVITY_CARD_ID = "activity_card_id";
    private int mActivityCardId;
    private Activity mActivity;


    public static ActivityCardFragment newInstance(int activityCardId) {
        Bundle args = new Bundle();
        args.putSerializable(ACTIVITY_CARD_ID, activityCardId);
        ActivityCardFragment activityCardFragment = new ActivityCardFragment();
        activityCardFragment.setArguments(args);
        return activityCardFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCardId = (int) getArguments().getSerializable(ACTIVITY_CARD_ID);
        ActivityCollection activityCollection = ActivityCollection.get(getActivity().getApplicationContext());
        mActivity = (Activity) activityCollection.getActivityById(mActivityCardId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 定义控件
        View view = inflater.inflate(R.layout.fragment_activity_card, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.activity_card_image);
        imageView.setImageResource(mActivity.getActivityImgId());
        TextView textView = (TextView) view.findViewById(R.id.activity_card_title);
        textView.setText(mActivity.getActivityTitle());
        // 点击事件，启动活动详情页
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("activityId", mActivityCardId);
                startActivity(intent);
            }
        });
        TextView textViewDate = (TextView) view.findViewById(R.id.activity_card_date);
        textViewDate.setText(mActivity.getActivityDate());
        return view;
    }
}
