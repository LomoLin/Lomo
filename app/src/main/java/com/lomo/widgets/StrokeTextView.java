package com.lomo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lomo.R;


/**
 * 自定义可描边TextView
 * Created by Lomo on 2017/9/12.
 */

public class StrokeTextView extends AppCompatTextView {
    //用于描边的Text/view
    private TextView mStrokeText = null;
    //描边宽度
    private static final float STROKE_WIDTH_DEFAULT = 4;
    private float mStrokeWidth = STROKE_WIDTH_DEFAULT;
    //描边颜色
    private int mStrokeColor = getResources().getColor(R.color.white);

    public StrokeTextView(Context context) {
        this(context, null);
    }

    public StrokeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrokeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mStrokeText = new TextView(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.StrokeTextView);
        mStrokeWidth = typedArray.getFloat(R.styleable.StrokeTextView_stvStrokeWidth, STROKE_WIDTH_DEFAULT);
        mStrokeColor = typedArray.getColor(R.styleable.StrokeTextView_stvStokeColor, getResources().getColor(R.color.white));
        typedArray.recycle();

        TextPaint textPaint = mStrokeText.getPaint();
        textPaint.setStrokeWidth(mStrokeWidth);
        textPaint.setStyle(Paint.Style.STROKE);
        mStrokeText.setTextColor(mStrokeColor);
        mStrokeText.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        mStrokeText.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        CharSequence charSequence = mStrokeText.getText();

        //两个TextView文字一致
        if (null == charSequence || !charSequence.equals(getText())) {
            mStrokeText.setText(getText());
            postInvalidate();
        }
        mStrokeText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mStrokeText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mStrokeText.draw(canvas);
        super.onDraw(canvas);
    }
}
