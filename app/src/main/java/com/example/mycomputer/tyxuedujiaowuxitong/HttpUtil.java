package com.example.mycomputer.tyxuedujiaowuxitong;

import android.text.TextUtils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by mycomputer on 2017/4/10.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,String session, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request;
        if(TextUtils.isEmpty(session)){
            request = new Request.Builder().url(address).build();
        }else {
            request = new Request.Builder().addHeader("cookie",session).url(address).build();
        }

        client.newCall(request).enqueue(callback);
    }
}
