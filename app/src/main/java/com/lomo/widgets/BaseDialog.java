package com.lomo.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.lomo.R;

/**
 * Created by linjunjie on 2016/11/15.
 */

public abstract class BaseDialog extends AlertDialog {
    protected Context mContext;

    protected BaseDialog(Context context) {
        this(context, R.style.ActionSheetDialogStyle);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = getContentView(LayoutInflater.from(mContext));
        initView(contentView);
        setContentView(contentView);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        LayoutParams wParams = getWindow().getAttributes();
        wParams.gravity = getWindowParams(wParams).gravity;
        wParams.width = getWindowParams(wParams).width;
        wParams.height = getWindowParams(wParams).height;
        getWindow().setAttributes(wParams);

    }

    public abstract View getContentView(LayoutInflater inflater);

    public abstract LayoutParams getWindowParams(LayoutParams params);

    protected abstract void initView(View content);
}
