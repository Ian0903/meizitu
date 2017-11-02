package com.example.ian.meizitu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class ContentActivity extends AppCompatActivity {

    private ImageButton imageButton;

    private CoordinatorLayout contentLayout;

    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        imageButton = (ImageButton)findViewById(R.id.video_photo);

        contentLayout = (CoordinatorLayout)findViewById(R.id.contentLayout);

        //获取videoUrl
        Intent videoUrlIntent = getIntent();
        videoUrl = videoUrlIntent.getStringExtra("videoUrl");

        setListener();

    }


    public void setListener(){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(ContentActivity.this,VideoActivity.class);
                videoIntent.putExtra("videoUrl",videoUrl);
                startActivity(videoIntent);
            }
        });
    }





}
