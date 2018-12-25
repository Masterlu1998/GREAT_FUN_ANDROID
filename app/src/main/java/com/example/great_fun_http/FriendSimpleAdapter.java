package com.example.great_fun_http;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

//public class FriendSimpleAdapter extends SimpleAdapter {
//    public FriendSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
//        super(context, data, resource, from, to);
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = super.getView(position, convertView, parent);
//
//        ImageView img = (ImageView) view.getTag();
//        Button button = (Button) view.getTag();
//        if (img == null) {
//            img = (ImageView) view.findViewById(R.id.searchFriendImg);
//            view.setTag(img);
//        }
//        if (button == null) {
//            button = (Button) view.findViewById(R.id.addFriend);
//            view.setTag(button);
//        }
//        String url = ((Map) getItem(position)).get("userHeadImg").toString();
//        Picasso.get().load(url).resize(800, 600).into(img);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String friendId = ((Map) getItem(position)).get("userId").toString();
//
//            }
//        });
//        return view;
//    }
//
//    public class PostFriend extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//        }
//    }
//}
