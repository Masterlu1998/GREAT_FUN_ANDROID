package com.example.great_fun_http;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private FriendAdapter mAdapter;

    int userId;

    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.friend_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        if (userId == -1) {
            setHasOptionsMenu(false);
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), AppLoginActivity.class);
            startActivityForResult(intent, 1);
        } else {
            setHasOptionsMenu(true);
            String param = String.format("{ \"userId\": %s }", userId);
            new GetFriendListTask().execute(param);
        }


        return view;
    }

    // 回退刷新
    @Override
    public void onResume() {
        super.onResume();
        String param = String.format("{ \"userId\": %s }", userId);
        new GetFriendListTask().execute(param);
        Log.d("状态PostDetailActivity", "huifu");
    }

    private class FriendHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // 获取空间实例
        private Friend mFriend;
        private TextView mFriendName;
        private TextView mFriendDate;
        private TextView mFriendMessage;
        private CircleImageView mFriendHead;
        public FriendHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_friend, parent, false));
            mFriendName = (TextView) itemView.findViewById(R.id.friendName);
            mFriendHead = (CircleImageView) itemView.findViewById(R.id.friendHead);
            mFriendMessage = (TextView) itemView.findViewById(R.id.friendMessage);
            mFriendDate = (TextView) itemView.findViewById(R.id.friendDate);
            itemView.setOnClickListener(this);
        }

        // 数据绑定
        public void bind(Friend friend) {
            mFriend = friend;
            mFriendName.setText(mFriend.getFriendName());
            mFriendMessage.setText(mFriend.getFriendMessage());
            Picasso.get().load(mFriend.getFriendHttpImg()).into(mFriendHead);
            mFriendDate.setText(mFriend.getFriendDate());
        }

        // 点击事件
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), FriendChatActivity.class);
            intent.putExtra("friendId", mFriend.getFriendId());
            intent.putExtra("friendHttpImg", mFriend.getFriendHttpImg());
            intent.putExtra("friendName", mFriend.getFriendName());
            intent.putExtra("friendLastMsg", mFriend.getFriendMessage());
            startActivity(intent);
        }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendHolder> {

        private List<Friend> mFriends;

        public FriendAdapter(List<Friend> Friends) {
            mFriends = Friends;
        }


        @NonNull
        @Override
        public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FriendHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendHolder friendHolder, int i) {
            Friend friend = mFriends.get(i);
            friendHolder.bind(friend);
        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }

    private class GetFriendListTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String params = strings[0];
            return HttpApiHelper.getApiData(params, "http://116.62.156.102:7080/android/getFriendList");
        }

        @Override
        protected void onPostExecute(String s) {
            final List<Friend> apiFriendList = new ArrayList<>();
            JSONObject jsonBody;
            int retcode = 0;
            if (s.equals("")) {
                Toast.makeText(getActivity(), "服务器无返回", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    jsonBody = new JSONObject(s);
                    retcode = jsonBody.getInt("retcode");
                    if (retcode == 0) {
                        JSONObject jsonRes = jsonBody.getJSONObject("obj");
                        JSONArray friendList = jsonRes.getJSONArray("friendList");
                        for (int i = 0; i < friendList.length(); i++) {
                            JSONObject friendItem = friendList.getJSONObject(i);
                            Friend friend = new Friend(
                                    friendItem.getInt("user_id"),
                                    friendItem.getString("friend_user_name"),
                                    friendItem.getString("last_message"),
                                    friendItem.getString("user_head_img"),
                                    friendItem.getString("send_date")
                            );
                            apiFriendList.add(friend);
                        }
                        mAdapter = new FriendAdapter(apiFriendList);
                        mCrimeRecyclerView.setAdapter(mAdapter);
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                userId = data.getExtras().getInt("userId");
                String param = String.format("{ \"userId\": %s }", userId);
                new GetFriendListTask().execute(param);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_friend, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_friend:
                Intent intent = new Intent(getActivity(), SearchFriendActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
