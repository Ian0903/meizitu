package com.example.ian.meizitu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.ian.meizitu.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String photoUrl;
    private String title;
    private PhotoView photoView;
    private PhotoViewAttacher photoViewAttacher;
    private AppBarLayout mAppbar;
    private boolean mIsHidden = false;
    private RelativeLayout pictureLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picture);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        photoView = (PhotoView) findViewById(R.id.picture);
        mAppbar = (AppBarLayout) findViewById(R.id.appBar);
        pictureLayout = (RelativeLayout) findViewById(R.id.picture_layout);
        getPhotoAndTitle();
        init();
    }

    private void init(){
        //初始化toolbar
        toolbar.setTitle(title);
        toolbar.inflateMenu(R.menu.menu_pitcure);
        toolbar.setTitleTextColor(getResources().getColor(R.color.actionMenuColor));
        toolbar.setNavigationIcon(R.mipmap.ic_back_white_24dp);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if(menuItemId == R.id.picture_save){
                    savePicture();
                }
                else if(menuItemId == R.id.picture_share){
                    sharePicture();
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //加载图片
        Glide.with(this).load(photoUrl).into(photoView);

        //设置图片点击事件
        photoViewAttacher = new PhotoViewAttacher(photoView);
        photoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                hideOrShowToolbar();
            }
        });
        photoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(PictureActivity.this)
                        .setMessage("是否保存照片？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                savePicture();
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });
    }
    private void getPhotoAndTitle(){
        Intent intent = getIntent();
        photoUrl = intent.getStringExtra("photoUrl");
        title = intent.getStringExtra("title");
    }

    private Observable<Uri> savePictureAndGetpath(){
        return Observable.create(new Observable.OnSubscribe<Uri>() {

            @Override
            public void call(Subscriber<? super Uri> subscriber) {
                Bitmap bitmap = null;
                try{
                    bitmap = Picasso.with(PictureActivity.this).load(photoUrl).get();
                }catch(IOException e){
                    subscriber.onError(e);
                }
                if(bitmap == null){
                    subscriber.onError(new Exception("无法下载图片"));
                }

                //将bimap保存到手机目录
                File appDir = new File(Environment.getExternalStorageDirectory(),"meizitu");
                if(!appDir.exists()){
                    appDir.mkdir();
                }
                String fileName = title.replace('/','-') + ".jpg";
                File file = new File(appDir,fileName);
                try{
                    FileOutputStream fos = new FileOutputStream(file);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                    fos.flush();
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

                //通知图库更新
                Uri uri = Uri.fromFile(file);
                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri);
                sendBroadcast(scannerIntent);

                subscriber.onNext(uri);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }

    private void savePicture(){
        savePictureAndGetpath()
         .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(pictureLayout,e.getMessage()+",请重试",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Uri uri) {
                        File appDir = new File(Environment.getExternalStorageDirectory(),"meizitu");
                        String msg = "图片保存在"+appDir.getAbsolutePath();
                        Snackbar.make(pictureLayout,msg,Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    private void sharePicture(){
        savePictureAndGetpath()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(pictureLayout,e.getMessage(),Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Uri uri) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
                        shareIntent.setType("image/jpeg");
                        startActivity(Intent.createChooser(shareIntent,"将图片发送给.."));
                    }
                });
    }


    private void hideOrShowToolbar(){
        mAppbar.animate()
                .translationY(mIsHidden ? 0 : -mAppbar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        photoViewAttacher.cleanup();
    }
}
