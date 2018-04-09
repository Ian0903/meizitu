package com.example.ian.meizitu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ian.meizitu.R;
import com.example.ian.meizitu.adapter.WelcomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private WelcomeAdapter adapter;
    private List<View> viewList;
    private Button startBtn;

    //引导页图片资源
    private static final int[] pics = {R.layout.welcome_view1,R.layout.welcome_view2,R.layout.welcome_view3};
    //底部小点图片
    private ImageView[] dots;
    //记录当前位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        viewList = new ArrayList<>();

        //初始化页面视图列表
        for(int i = 0;i<pics.length;i++){
            View view = LayoutInflater.from(this).inflate(pics[i],null);
            if(i == pics.length-1){
                startBtn = (Button) view.findViewById(R.id.startBtn);
                startBtn.setTag("enter");
                startBtn.setOnClickListener(this);
            }
            viewList.add(view);
        }

        viewPager = (ViewPager)findViewById(R.id.vp_welcome);
        adapter = new WelcomeAdapter(viewList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initDots();
    }

    private void initDots(){
        LinearLayout pointlayout = (LinearLayout)findViewById(R.id.point_layout);
        dots = new ImageView[pics.length];
        for(int i = 0;i<pics.length;i++){
            dots[i] = (ImageView) pointlayout.getChildAt(i);
            dots[i].setEnabled(false);//都设置点为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置标志位tag
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true);//设置当前位置点为白色

    }

    public void onClick(View v){
        if(v.getTag().equals("enter")){
            enterMainActivity();
            return;
        }
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    private void setCurView(int position){
        if(position<0 || position>=pics.length){
            return ;
        }
        viewPager.setCurrentItem(position);
    }

    private void setCurDot(int position){
        if(position<0 || position>=pics.length || currentIndex == position){
            return;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);//上一个点设置为灰色
        currentIndex = position;//更新当前位置
    }

    private void enterMainActivity(){
        Intent intent = new Intent(WelcomeActivity.this,
                SplashActivity.class);
        startActivity(intent);
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean("isFirstOpen",false);
        editor.commit();
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean("isFirstOpen",false);
        editor.commit();
        finish();
    }
}
