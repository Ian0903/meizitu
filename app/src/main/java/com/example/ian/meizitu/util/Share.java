package com.example.ian.meizitu.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.ian.meizitu.R;

/**
 * Created by Ian on 2018/6/1.
 */

public class Share {

    public static void shareUrl(Context context,String url,String title){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,title +"\n" + url);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent,"分享"));
    }

    public static void shareApp(Context context){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,context.getString(R.string.share_app));
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent,"分享"));
    }

    public static void shareImage(Context context,Uri uri){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,uri);
        intent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(intent,"分享"));
    }
}
