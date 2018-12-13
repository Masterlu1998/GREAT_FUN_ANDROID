package com.example.great_fun_http;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ActivityCardFragment extends Fragment {

    private static final String ACTIVITY_ID = "activity_card_id";
    private static final String ACTIVITY_NAME = "activity_name";
    private static final String ACTIVITY_DATE = "activity_date";
    private static final String ACTIVITY_IMG_URL = "activity_img_url";
    private int mActivityId;
    private String mActivityName;
    private String mActivityDate;
    private String mActivityImgUrl;
    private Activity mActivity;


    public static ActivityCardFragment newInstance(int activityId, String activityName, String activityDate, String activityImgUrl) {
        Bundle args = new Bundle();
        args.putSerializable(ACTIVITY_ID, activityId);
        args.putSerializable(ACTIVITY_NAME, activityName);
        args.putSerializable(ACTIVITY_DATE, activityDate);
        args.putSerializable(ACTIVITY_IMG_URL, activityImgUrl);

        ActivityCardFragment activityCardFragment = new ActivityCardFragment();
        activityCardFragment.setArguments(args);
        return activityCardFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityId = (int) getArguments().getSerializable(ACTIVITY_ID);
        mActivityName = (String) getArguments().getSerializable(ACTIVITY_NAME);
        mActivityDate = (String) getArguments().getSerializable(ACTIVITY_DATE);
        mActivityImgUrl = (String) getArguments().getSerializable(ACTIVITY_IMG_URL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 定义控件
        View view = inflater.inflate(R.layout.fragment_activity_card, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.activity_card_image);
        Picasso.get().load(mActivityImgUrl).resize(800, 600).into(imageView);
        TextView textView = (TextView) view.findViewById(R.id.activity_card_title);
        textView.setText(mActivityName);
        TextView textViewDate = (TextView) view.findViewById(R.id.activity_card_date);
        textViewDate.setText(mActivityDate);
        // 点击事件，启动活动详情页
        textView.setOnClickListener(new View.OnClickListener() {
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
