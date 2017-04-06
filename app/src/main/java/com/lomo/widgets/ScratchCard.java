package com.lomo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.lomo.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 本代码参考启舰demo：http://blog.csdn.net/harvic880925/article/details/51284710
 *
 * Created by Lomo on 2017/4/6.
 *
 * TODO
 */

public class ScratchCard extends View {
    /**
     * 字体大小
     **/
//    private float scratchTextSize;
    /**
     * 文字默认大小
     **/
//    private float defaultTextSize = 45;
    /**
     * 字体颜色
     **/
//    private int scratchTextColor;
    /**
     * 字体默认颜色
     **/
//    private int defaultScratchTextColor = 0xfff;

    /**
     * 文字内容
     **/
//    private String scratchText;

    /**
     * 遮罩层颜色
     **/
    private int mDefaultMaskColor = 0xffcccccc;
    private int mMaskColor;
    /**
     * 遮罩层图片
     **/
    private int mMaskRes;
    private Bitmap mMaskBmp;
    /**
     * 擦除层
     **/
    private Bitmap mEraserBmp;
    private int mEraserWidth;
    private int mDefaultEraserWidth = 60;
    private Path mEraserPath;

    /**
     * 画笔
     **/
    private Paint mPaint;

    /**
     * 刮奖区域
     **/
    private Rect mTextBound;

    private float mPreX;
    private float mPreY;

    /**
     * 蒙层像素存放数组
     **/
    private int mPixels[];

    private EraseStatusListener mEraseListener;

    public interface EraseStatusListener {
        //擦除进度
        void onProgress(int progress);

        //擦除完成
        void onDone();
    }

    public ScratchCard(Context context) {
        this(context, null);
    }

    public ScratchCard(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScratchCard);

//        scratchText = typedArray.getString(R.styleable.ScratchCard_scratchText);
//        scratchTextSize = typedArray.getDimension(R.styleable.ScratchCard_scratchTextSize, defaultTextSize);
//        scratchTextColor = typedArray.getColor(R.styleable.ScratchCard_scratchTextColor, defaultScratchTextColor);

        mMaskColor = typedArray.getColor(R.styleable.ScratchCard_coverColor, mDefaultMaskColor);
        mMaskRes = typedArray.getResourceId(R.styleable.ScratchCard_coverImageResourse, -1);

        mEraserWidth = typedArray.getDimensionPixelSize(R.styleable.ScratchCard_eraser_width, mDefaultEraserWidth);

        typedArray.recycle();

        initParams();

    }

    private void initParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mTextBound = new Rect();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mEraserWidth);

        if (mMaskRes != -1) {
            mMaskBmp = BitmapFactory.decodeResource(getResources(), mMaskRes);
        } else {
            mMaskBmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mMaskBmp);
            Rect rect = new Rect(0, 0, getWidth(), getHeight());
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(mMaskColor);
            canvas.drawRect(rect, paint);
        }
        mEraserBmp = Bitmap.createBitmap(mMaskBmp.getWidth(), mMaskBmp.getHeight(), Bitmap.Config.ARGB_8888);

        mEraserPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        mPaint.setTextSize(scratchTextSize);
//        mPaint.getTextBounds(scratchText, 0, scratchText.length(), mTextBound);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            float textWidth = mTextBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            float textHeight = mTextBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        mPaint.setTextSize(scratchTextSize);
        mPaint.setStrokeWidth(4);
        //让文字水平居中，垂直为1/3高度
//        Rect bounds = new Rect();
//        mPaint.getTextBounds(scratchText, 0, scratchText.length(), bounds);
//        canvas.drawText(scratchText, (getWidth() - bounds.width()) / 2, getHeight() / 3 - bounds.height() / 2, mPaint);

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        //先把手指轨迹画到mEraserBmp上
        mPaint.setStrokeWidth(mEraserWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        Canvas c = new Canvas(mEraserBmp);
        c.drawPath(mEraserPath, mPaint);

        //然后把目标图像画到画布上
        canvas.drawBitmap(mEraserBmp, 0, 0, mPaint);

        //计算源图像区域
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //图片缩放比例
        float scaleWidth = (float) getWidth() / mMaskBmp.getWidth();
        float scaleHeight = (float) getHeight() / mMaskBmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(scaleWidth, scaleHeight, 0, 0);

        canvas.drawBitmap(mMaskBmp, matrix, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mEraserPath.moveTo(event.getX(), event.getY());
                mPreX = event.getX();
                mPreY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX = (mPreX + event.getX()) / 2;
                float endY = (mPreY + event.getY()) / 2;
                mEraserPath.quadTo(mPreX, mPreY, endX, endY);
                mPreX = event.getX();
                mPreY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                updateEraserPercent();
                break;
            default:
                break;
        }

        postInvalidate();

        return super.onTouchEvent(event);
    }

    /**
     * 刷新擦除比例
     */
    private void updateEraserPercent() {

        if (null == mPixels) {
            mPixels = new int[getWidth() * getHeight()];
        }
        mEraserBmp.getPixels(mPixels, 0, getWidth(), 0, 0, getWidth(), getHeight());

        //由于SRC_OUT模式，当目标图像像素不为空时，目标图像与源图像相交之处则为空像素
        //所以只要计算目标图像（即mEraseBmp）所占的像素与该view所有像素的比例，就可以算出所刮出的比例
        Observable.range(0, getWidth() * getHeight())
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return mPixels[integer] != 0;
                    }
                })
                .count()
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return Math.round(integer * 100 / (float) mPixels.length);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("Lomo","progress:" + integer);
                        if (null != mEraseListener) {
                            if (integer == 100) {
                                mEraseListener.onDone();
                            } else {
                                mEraseListener.onProgress(integer);
                            }
                        }
                    }
                });

    }

    /**
     * 设置监听器
     *
     * @param eraseListener
     */
    public void setEraseListener(EraseStatusListener eraseListener) {
        mEraseListener = eraseListener;
    }

//    public void setScratchText(String scratchText) {
//        this.scratchText = scratchText;
//    }
}
