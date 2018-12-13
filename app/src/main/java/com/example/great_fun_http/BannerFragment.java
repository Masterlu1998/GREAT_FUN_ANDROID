package com.example.great_fun_http;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BannerFragment extends Fragment {

    private static final String RES_ID = "res_id";
    private int mResId;

    public static BannerFragment newInstance(int resId) {
        Bundle args = new Bundle();
        args.putSerializable(RES_ID, resId);

        BannerFragment fragment = new BannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResId = (int) getArguments().getSerializable(RES_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        ImageView mImageView = (ImageView) view.findViewById(R.id.homepage_banner);
        mImageView.setImageResource(mResId);
        return view;
    }
}
