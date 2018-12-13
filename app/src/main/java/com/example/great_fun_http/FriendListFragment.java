package com.example.great_fun_http;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private FriendAdapter mAdapter;

    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.friend_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        List<Friend> mFriends = FriendCollection.getFriends();

        mAdapter = new FriendAdapter(mFriends);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    private class FriendHolder extends RecyclerView.ViewHolder {
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
        }

        // 数据绑定
        public void bind(Friend friend) {
            mFriend = friend;
            mFriendName.setText(mFriend.getFriendName());
            mFriendMessage.setText(mFriend.getFriendMessage());
            mFriendHead.setImageResource(mFriend.getFriendImg());
            mFriendDate.setText(mFriend.getFriendDate());
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
            return FriendCollection.getSize();
        }
    }
}
