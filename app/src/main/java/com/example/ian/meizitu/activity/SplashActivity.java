package com.example.ian.meizitu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.ian.meizitu.R;

public class SplashActivity extends AppCompatActivity {
    private boolean isFirstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstStart = isFirstOpen();
        //如果第一次启动则进入引导页面
        if(isFirstStart){
                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
                return;
        }

        //否则进入主页面
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterMainActivity();
            }
        },2000);

    }

    private void enterMainActivity(){
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isFirstOpen(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        return pref.getBoolean("isFirstOpen",true);
    }
}
