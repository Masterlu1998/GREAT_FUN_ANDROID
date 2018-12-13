package com.example.great_fun_http;

import java.util.ArrayList;
import java.util.List;

public class FriendCollection {
    private static String []friendName = new String[] {
            "夏天乐",
            "余沈铨",
            "张航",
            "可乐",
            "往事随风",
            "不羁",
            "鲁大师",
            "蒋泽林",
            "试试看"
    };
    private static String []friendMessage = new String[] {
            "什么时候出发？",
            "你在哪儿？",
            "有内鬼，交易终止！",
            "天呐，这是什么怪物。",
            "这里出了个bug你过来看下",
            "我这里是好的",
            "前端开发没人要了",
            "后端工资好低啊",
            "安卓好难"
    };
    private static int []friendImg = new int[] {
            R.mipmap.head_img_a,
            R.mipmap.head_img_a,
            R.mipmap.head_img_a,
            R.mipmap.head_img_a,
            R.mipmap.head_img_a,
            R.mipmap.head_img_a,
            R.mipmap.user_head,
            R.mipmap.head_img_a,
            R.mipmap.head_img_a,
    };
    private static String []friendDate = new String[] {
            "2018-08-12 00:00",
            "2018-08-12 00:00",
            "2018-08-12 00:00",
            "2018-08-12 00:00",
            "2018-08-12 00:00",
            "2018-08-12 00:00",
            "2018-08-12 00:00",
            "2018-08-12 00:00",
            "2018-08-12 00:00",
    };
    private static final int size = friendName.length;
    public static List<Friend> getFriends() {
        List<Friend> mFriends = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            mFriends.add(new Friend(i, friendName[i], friendMessage[i], friendImg[i], friendDate[i]));
        }

        return mFriends;
    }

    public static int getSize() {
        return size;
    }
}
