package com.example.mycomputer.tyxuedujiaowuxitong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {
    public String sessionValue=null;
    public String rootUrl="http://jwgl.tyu.edu.cn:8084/";
    public List<Course> courses = new ArrayList<>();
    public ListView listView;
    private String data[];
    private TextView userName;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = (ListView)findViewById(R.id.list_view);
        userName=(TextView)findViewById(R.id.StudentName);

        Intent intent = getIntent();
        sessionValue=intent.getStringExtra("session_Value");
        rootUrl=intent.getStringExtra("rootUrl");

        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final String url = rootUrl+"gradeLnAllAction.do?type=ln&oper=qbinfo&lnxndm=2016-2017";
        HttpUtil.sendOkHttpRequest(rootUrl + "menu/top.jsp", sessionValue, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Main2Activity.this, "用户名获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String usernameString = response.body().string();
                final String username = usernameString.substring(usernameString.indexOf("当前用户:"),usernameString.indexOf("&nbsp;",usernameString.indexOf("当前用户:")));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userName.setText(username);

                    }
                });
            }
        });
        HttpUtil.sendOkHttpRequest(url,sessionValue, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Main2Activity.this, "成绩获取失败了", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                JXHTML.JxHtml(string,courses);
                Log.w("Main2Activity.this", "onResponse: "+string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data=new String[courses.size()];
                        for(int i=0;i<courses.size();i++){
                            Course c;
                            c=courses.get(i);
                            data[i]=c.getName()+"的成绩为"+c.getScore();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_list_item_1,data);
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
