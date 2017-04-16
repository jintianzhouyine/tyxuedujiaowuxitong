package com.example.mycomputer.tyxuedujiaowuxitong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public ImageView yzm;
    public Button refreshYzm;
    public Button submit;
    public Button reset;
    public EditText username;
    public EditText password;
    public EditText yzmString;
    public String sessionValue=null;
    public String rootUrl="http://jwgl.tyu.edu.cn:8084/";
    public Button changeRootUrl;
    public int changeUrl=1;//更改url入口地址


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        refreshYzmImage();
        refreshYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshYzmImage();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetInfo();
            }
        });
        changeRootUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    changeUrl(changeUrl);
                    Toast.makeText(MainActivity.this, "更改为"+changeUrl + "号入口。"+"请手动刷新验证码", Toast.LENGTH_SHORT).show();
                    changeUrl++;
                    if(changeUrl==7){
                        changeUrl=1;
                    }
            }
        });

    }

    public void init(){
        username =(EditText)findViewById(R.id.username);
        password =(EditText)findViewById(R.id.password);
        yzmString =(EditText)findViewById(R.id.yzm_string);
        yzm = (ImageView)findViewById(R.id.yzm);
        refreshYzm = (Button)findViewById(R.id.refresh);
        submit = (Button)findViewById(R.id.submit);
        reset = (Button)findViewById(R.id.reset);
        changeRootUrl = (Button)findViewById(R.id.changeUrl);

        OkHttpClient client = new OkHttpClient();

    }
    public void refreshYzmImage(){
        Random random = new Random();
        int r =random.nextInt(1000);
        if(r<=100){
            r =random.nextInt(999);
        }
        final String requestYzm =rootUrl+"validateCodeAction.do?random=0.8383557303327"+r;
        HttpUtil.sendOkHttpRequest(requestYzm,null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "验证码获取失败", Toast.LENGTH_SHORT).show();
                resetInfo();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final byte[] bytes =response.body().bytes();
                final Headers headers = response.headers();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(bytes).into(yzm);
                        List<String> cookies = headers.values("Set-Cookie");
                        String session = cookies.get(0);
                        sessionValue = session.substring(0, session.indexOf(";"));
                    }
                });
            }
        });

    }

    public void loading(){

        if(TextUtils.isEmpty(username.getText())){
            Toast.makeText(MainActivity.this, "用户名不予许为空", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(username.getText())){
            Toast.makeText(MainActivity.this, "验证码不予许为空", Toast.LENGTH_SHORT).show();
        }else{
            if(TextUtils.isEmpty(password.getText())){
                Toast.makeText(MainActivity.this, "密码默认等于用户名", Toast.LENGTH_SHORT).show();
                password.setText(username.getText());
            }
            final String lodingUrl =rootUrl +
                    "loginAction.do?zjh1=&tips=&lx=&evalue=&eflag=&fs=&dzslh=&" +
                    "zjh=" +
                    username.getText() +
                    "&mm=" +
                    password.getText() +
                    "&v_yzm=" +
                    yzmString.getText();

            HttpUtil.sendOkHttpRequest(lodingUrl,sessionValue, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String s = response.body().string();
                    if(s.length()<1800){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Log.w("MainActivity.this", "run: "+s);
                            }
                        });

                        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("session_Value",sessionValue);
                        intent.putExtra("rootUrl",rootUrl);
                        startActivity(intent);
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                        refreshYzmImage();
                    }

                }
            });
        }
    }

    public void resetInfo(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                username.setText("");
                password.setText("");
                yzmString.setText("");
            }
        });

        refreshYzmImage();
    }

    public void changeUrl(int i){
        rootUrl="http://jwgl.tyu.edu.cn:808" +
                i+
                "/";

    }

}
