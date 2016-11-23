package com.lomo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.lomo.R;


/**
 * 时间线
 *
 * Author:
 * https://github.com/qiujuer/BeFoot/tree/master/blog/sample/TimeLine
 *
 * Created by linjunjie on 2016/11/18.
 */

public class TimeLineMarker extends View {
    private int mMarkerSize = 24;
    private int mLineSize = 12;
    private Drawable mBeginLine;
    private Drawable mEndLine;
    private Drawable mMarker;


    public TimeLineMarker(Context context) {
        this(context, null);
    }

    public TimeLineMarker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineMarker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Load attributes
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TimeLineMarker);
        mMarkerSize = typedArray.getDimensionPixelSize(R.styleable.TimeLineMarker_markerSize, mMarkerSize);
        mMarker = typedArray.getDrawable(R.styleable.TimeLineMarker_marker);
        mBeginLine = typedArray.getDrawable(R.styleable.TimeLineMarker_beginLine);
        mEndLine = typedArray.getDrawable(R.styleable.TimeLineMarker_endLine);
        mLineSize = typedArray.getDimensionPixelSize(R.styleable.TimeLineMarker_lineSize, mLineSize);

        typedArray.recycle();

        if (null != mMarker) {
            mMarker.setCallback(this);
        }

        if (null != mBeginLine) {
            mBeginLine.setCallback(this);
        }

        if (null != mEndLine) {
            mEndLine.setCallback(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != mBeginLine) {
            mBeginLine.draw(canvas);
        }

        if (null != mEndLine) {
            mEndLine.draw(canvas);
        }

        if (null != mMarker) {
            mMarker.draw(canvas);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = getPaddingLeft() + getPaddingRight();
        int h = getPaddingTop() + getPaddingBottom();

        if (null != mMarker) {
            w += mMarkerSize;
            h += mMarkerSize;
        }

        w = Math.max(w, getMeasuredWidth());
        h = Math.max(h, getMeasuredHeight());

        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDrawableSize();
    }

    private void initDrawableSize() {
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();

        int cWidth = width - pLeft - pRight;
        int cHeight = height - pTop - pBottom;

        Rect bounds;

        if (null != mMarker) {
            mMarker.setBounds(pLeft, pTop, pLeft + mMarkerSize, pTop + mMarkerSize);
            bounds = mMarker.getBounds();
        } else {
            bounds = new Rect(pLeft, pTop, pLeft + cWidth, pRight + cHeight);
        }

        int halfLineSize = mLineSize >> 1;
        int lineLeft = bounds.centerX() - halfLineSize;

        if (null != mBeginLine) {
            mBeginLine.setBounds(lineLeft, 0, lineLeft + mLineSize, bounds.top);
        }

        if (null != mEndLine) {
            mEndLine.setBounds(lineLeft, bounds.bottom, lineLeft + mLineSize, height);
        }
    }

    public void setMarkerSize(int markerSize) {
        if (mMarkerSize != markerSize) {
            mMarkerSize = markerSize;
            initDrawableSize();
            invalidate();
        }
    }

    public void setLineSize(int lineSize) {
        if (mLineSize != lineSize) {
            mLineSize = lineSize;
            initDrawableSize();
            invalidate();
        }
    }

    public void setBeginLine(Drawable beginLine) {
        if (mBeginLine != beginLine) {
            mBeginLine = beginLine;
            if (null != mBeginLine) {
                mBeginLine.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

    public void setEndLine(Drawable endLine) {
        if (mEndLine != endLine) {
            mEndLine = endLine;
            if (null != mEndLine) {
                mEndLine.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

    public void setMarker(Drawable marker) {
        if (mMarker != marker) {
            mMarker = marker;
            if (null != mMarker) {
                mMarker.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }
}
