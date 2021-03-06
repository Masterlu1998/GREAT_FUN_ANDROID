package com.example.great_fun_http;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

// 定义可以加载图片的simpleAdapter(By stackOverflow)
public class ActivitySimpleAdapter extends SimpleAdapter {
    public ActivitySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageView img = (ImageView) view.getTag();
        if (img == null) {
            img = (ImageView) view.findViewById(R.id.personActivityImg);
            view.setTag(img);
        }
        String url = ((Map) getItem(position)).get("activityImgUrl").toString();
        Picasso.get().load(url).resize(800, 600).into(img);
        return view;
    }
}
