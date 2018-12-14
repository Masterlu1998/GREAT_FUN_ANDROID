package com.example.great_fun_http;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.great_fun_http.httpHelper.HttpApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class AppLoginActivity extends AppCompatActivity {

    Button mLoginBtn;
    EditText mUserAccountET;
    EditText mUserPwdET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_login);
        mLoginBtn = (Button) findViewById(R.id.btnLogin);
        mUserAccountET = (EditText) findViewById(R.id.userAccount);
        mUserPwdET = (EditText) findViewById(R.id.userPwd);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAccount = mUserAccountET.getText().toString();
                String userPwd = mUserPwdET.getText().toString();
                String param = String.format("{ \"args\": { \"userAccount\": \"%s\", \"userPwd\": \"%s\" } }", userAccount, userPwd);
                new AppLoginActivity.UserLogin().execute(param);
            }
        });

    }

    // 用户登录任务
    public class UserLogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String param = params[0];
            return HttpApiHelper.getApiData(param, "http://116.62.156.102:7080/android/userLogin");
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject jsonBody = null;
            int retcode = 0;
            if (s == null) {
                Toast.makeText(AppLoginActivity.this, "接口调用失败", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jsonBody = new JSONObject(s);
                    retcode = jsonBody.getInt("retcode");
                    if (retcode == 0) {
                        Toast.makeText(AppLoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                        JSONObject resJson = jsonBody.getJSONObject("obj");
                        String userName = resJson.getString("userName");
                        int userId = resJson.getInt("userId");
                        String userHeadImg = resJson.getString("userHeadImg");
                        String userContent = resJson.getString("userContent");
                        // 在全局变量中记录
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                        editor.putString("userName", userName);
                        editor.putInt("userId", userId);
                        editor.putString("userHeadImg", userHeadImg);
                        editor.putString("userContent", userContent);
                        editor.apply();
                        // 返回结果
                        Intent intent = new Intent();
                        intent.putExtra("userName", userName);
                        intent.putExtra("userId", userId);
                        intent.putExtra("userHeadImg", userHeadImg);
                        intent.putExtra("userContent", userContent);

                        AppLoginActivity.this.setResult(1, intent);
                        AppLoginActivity.this.finish();

                    } else {
                        Toast.makeText(AppLoginActivity.this, "登陆失败", Toast.LENGTH_LONG).show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }



        }
    }
}
