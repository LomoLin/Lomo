package com.lomo.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lomo.R;

/**
 * 自定义Toast
 *
 * Created by linjunjie on 2016/11/23.
 */

public class MyToast {

    private Toast mToast;

    private MyToast(Context context, CharSequence text, int duration) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.tvToastMsg);
        tv.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(view);
    }

    public static MyToast makeText(Context context, CharSequence text, int duration) {
        return new MyToast(context, text, duration);
    }

    public void show() {
        if (null != mToast) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        mToast.setGravity(gravity, xOffset, yOffset);
    }

}
