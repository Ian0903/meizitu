package com.example.ian.meizitu.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;

/**
 * Created by Ian on 2018/4/3.
 */

public class StringStyles {

    public static SpannableString format(Context context,String text,int style){
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context,style),0,text.length(),0);
        return spannableString;
    }
}
