package com.example.great_fun_http;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendChatActivity extends AppCompatActivity {

    int userId;
    int friendId;
    String friendHttpImg;
    String friendName;
    String friendLastMsg;

    CircleImageView friendHeadIV;
    TextView friendMsgTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);
        Intent intent = getIntent();
        friendId = intent.getIntExtra("friendId", -1);
        friendHttpImg = intent.getStringExtra("friendHttpImg");
        friendName = intent.getStringExtra("friendName");
        friendLastMsg = intent.getStringExtra("friendLastMsg");

        SharedPreferences preferences = this.getSharedPreferences("userInfo", this.MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        friendHeadIV = (CircleImageView) findViewById(R.id.friendHttpImg);
        friendMsgTV = (TextView) findViewById(R.id.friendLastMsg);

        Picasso.get().load(friendHttpImg).into(friendHeadIV);
        friendMsgTV.setText(friendLastMsg);

        setTitle(friendName);

    }
}
