package com.example.great_fun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonDetailFragment extends Fragment {
    public static PersonDetailFragment newInstance() {
        PersonDetailFragment fragment = new PersonDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_detail, container, false);
        ListView listView = (ListView) view.findViewById(R.id.personActivityList);
        ActivityCollection activityCollection = ActivityCollection.get(getActivity().getApplicationContext());
        List<Activity> activities = activityCollection.getActivities();
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (Activity activity : activities) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("activityId", activity.getActivityId());
            item.put("activityCardTitle", activity.getActivityTitle());
            item.put("activityCardContent", activity.getActivityContent());
            item.put("activityCardDate", activity.getActivityDate());
            item.put("activityCardImgId",activity.getActivityImgId());
            data.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(
                getContext(),
                data,
                R.layout.fragment_person_activity_item,
                new String[] { "activityCardTitle", "activityCardContent", "activityCardDate", "activityCardImgId" },
                new int[] { R.id.personActivityTitle, R.id.personActivityContent, R.id.personActivityDate, R.id.personActivityImg }
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra("activityId", position);
                startActivity(intent);
            }
        });
        return view;
    }
}
