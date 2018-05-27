package com.example.ian.meizitu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ian.meizitu.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher_about)
                .setDescription("每天一张精选妹子图、一系列干货、一个精选小视频")
                .addItem(new Element().setTitle("*  Version 1.1"))
                .addItem(new Element().setTitle("*  Author:Ian"))
                .addItem(new Element().setTitle("*  Email:huashigm@qq.com"))
                .addWebsite("https://github.com/Ian0903/meizitu","开源地址")
                .addGitHub("Ian0903")
                .addEmail("huashigm@qq.com")
                .create();

        setContentView(view);

    }
}
