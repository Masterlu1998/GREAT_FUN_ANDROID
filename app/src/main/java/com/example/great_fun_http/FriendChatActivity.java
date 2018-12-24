package com.example.great_fun_http;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.Socket;
import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class FriendChatActivity extends AppCompatActivity {

    int userId;
    String userHeadImg;
    int friendId;
    String friendHttpImg;
    String friendName;
    String friendLastMsg;

    EditText userMsgET;
    CircleImageView friendHeadIV;
    TextView friendMsgTV;
    Button emitBtn;
    LinearLayout mLinearLayout;
    io.socket.client.Socket mSocket;


    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket("http://116.62.156.102:7080/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();

        setContentView(R.layout.activity_friend_chat);
        Intent intent = getIntent();
        friendId = intent.getIntExtra("friendId", -1);
        friendHttpImg = intent.getStringExtra("friendHttpImg");
        friendName = intent.getStringExtra("friendName");
        friendLastMsg = intent.getStringExtra("friendLastMsg");

        SharedPreferences preferences = this.getSharedPreferences("userInfo", this.MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userHeadImg = preferences.getString("userHeadImg", "");

        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        userMsgET = (EditText) findViewById(R.id.userMsg);

        friendHeadIV = (CircleImageView) findViewById(R.id.friendHttpImg);
        friendMsgTV = (TextView) findViewById(R.id.friendLastMsg);

        Picasso.get().load(friendHttpImg).into(friendHeadIV);
        friendMsgTV.setText(friendLastMsg);

        emitBtn = findViewById(R.id.emit);
        emitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout newLinearLayout = new LinearLayout(FriendChatActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                newLinearLayout.setGravity(Gravity.RIGHT);
                newLinearLayout.setLayoutParams(layoutParams);

                // 新建一个TextView
                TextView newTextView = new TextView(FriendChatActivity.this);
                String userMsg = userMsgET.getText().toString();
                String msgObj = String.format("{ \"fromUserId\": %s , \"userId\": %s, \"message\": \"%s\" }", userId, friendId, userMsg);
                mSocket.emit("msg", msgObj);
                newTextView.setText(userMsg);
                newTextView.setTextSize(18);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        dp2px(FriendChatActivity.this, 25)
                );
                lp.gravity = Gravity.CENTER_VERTICAL;
                newTextView.setLayoutParams(lp);
                newLinearLayout.addView(newTextView);

                // 新建一个头像
                CircleImageView newCircleImageView = new CircleImageView(FriendChatActivity.this);
                LinearLayout.LayoutParams IVlayoutParams = new LinearLayout.LayoutParams(
                        dp2px(FriendChatActivity.this, 60),
                        dp2px(FriendChatActivity.this, 60)
                );
                IVlayoutParams.setMargins(
                        dp2px(FriendChatActivity.this, 20),
                        dp2px(FriendChatActivity.this, 10),
                        dp2px(FriendChatActivity.this, 20),
                        dp2px(FriendChatActivity.this, 10)
                );
                newCircleImageView.setLayoutParams(IVlayoutParams);
                Picasso.get().load(userHeadImg).into(newCircleImageView);
                newLinearLayout.addView(newCircleImageView);

                mLinearLayout.addView(newLinearLayout, -1);
            }
        });
        mSocket.on(Integer.toString(userId), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String msg = args[0].toString();
                Log.d("msg", args[0].toString());
                new GetMessage().execute(msg);
            }
        });
        setTitle(friendName);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    public class GetMessage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            String msg = s;
            LinearLayout newLinearLayout = new LinearLayout(FriendChatActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            newLinearLayout.setLayoutParams(layoutParams);

            CircleImageView newCircleImageView = new CircleImageView(FriendChatActivity.this);
            LinearLayout.LayoutParams IVlayoutParams = new LinearLayout.LayoutParams(
                    dp2px(FriendChatActivity.this, 60),
                    dp2px(FriendChatActivity.this, 60)
            );
            IVlayoutParams.setMargins(
                    dp2px(FriendChatActivity.this, 20),
                    dp2px(FriendChatActivity.this, 10),
                    dp2px(FriendChatActivity.this, 20),
                    dp2px(FriendChatActivity.this, 10)
            );
            newCircleImageView.setLayoutParams(IVlayoutParams);
            Picasso.get().load(friendHttpImg).into(newCircleImageView);
            newLinearLayout.addView(newCircleImageView, -1);

            TextView newTextView = new TextView(FriendChatActivity.this);
            newTextView.setText(msg);
            newTextView.setTextSize(18);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    dp2px(FriendChatActivity.this, 25)
            );

            newLinearLayout.addView(newTextView, -1);
            lp.gravity = Gravity.CENTER_VERTICAL;
            newTextView.setLayoutParams(lp);


            mLinearLayout.addView(newLinearLayout, -1);
        }
    }
}

