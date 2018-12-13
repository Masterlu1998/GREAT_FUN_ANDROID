package com.example.great_fun_http;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonDetailFragment extends Fragment {
    private ListView mListView;

    // 定义可以加载图片的simpleAdapter(By stackOverflow)
    public class MySimpleAdapter extends SimpleAdapter {
        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
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

    public static PersonDetailFragment newInstance() {
        PersonDetailFragment fragment = new PersonDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_detail, container, false);
        mListView = (ListView) view.findViewById(R.id.personActivityList);
        String param = String.format("{ \"args\": { \"userId\": 2 } }");
        new PersonDetailFragment.getUserActivityListTask().execute(param);
        return view;
    }

    public class getUserActivityListTask extends AsyncTask<String, Void, String> {

        final List<Activity> mActivityList = new ArrayList<>();


        @Override
        protected String doInBackground(String... params) {
            String param = params[0];
            return HttpApiHelper.getApiData(param, "http://116.62.156.102:7080/android/getUserActivityList");
        }

        @Override
        protected void onPostExecute(String resultObj) {
            JSONObject jsonBody = null;
            int retcode = 0;
            try {
                jsonBody = new JSONObject(resultObj);
                retcode = jsonBody.getInt("retcode");
                if (retcode == 0) {
                    JSONObject resObj = jsonBody.getJSONObject("obj");
                    JSONArray userActivityList = resObj.getJSONArray("activityList");
                    for (int i = 0; i < userActivityList.length(); i++) {
                        JSONObject activityItem = userActivityList.getJSONObject(i);
                        Activity showActivity = new Activity(
                                activityItem.getInt("activityId"),
                                activityItem.getString("activityName"),
                                activityItem.getString("activityContent"),
                                activityItem.getString("activityDate"),
                                activityItem.getString("activityImgUrl"),
                                activityItem.getInt("activityPostUser")
                        );
                        mActivityList.add(showActivity);
                    }

                    List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                    for (Activity activity : mActivityList) {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("activityId", activity.getActivityId());
                        item.put("activityName", activity.getActivityName());
                        item.put("activityContent", activity.getActivityContent());
                        item.put("activityDate", activity.getActivityDate());
                        item.put("activityImgUrl",activity.getActivityImgUrl());
                        data.add(item);
                    }
                    SimpleAdapter adapter = new MySimpleAdapter(
                            getContext(),
                            data,
                            R.layout.fragment_person_activity_item,
                            new String[] { "activityName", "activityContent", "activityDate", "activityImgUrl" },
                            new int[] { R.id.personActivityTitle, R.id.personActivityContent, R.id.personActivityDate, R.id.personActivityImg }
                    );
                    mListView.setAdapter(adapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                            intent.putExtra("activityId", mActivityList.get(position).getActivityId());
                            startActivity(intent);
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_LONG).show();

                }

            } catch(JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
