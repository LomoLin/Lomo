package com.lomo.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lomo.R;
import com.lomo.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linjunjie on 2016/11/11.
 */

public class ImageGallery extends RelativeLayout implements OnPageChangeListener{
    private static final String TAG = "ImageGallery";
    private Context mContext;
    private ViewPager vpImages;
    private LinearLayout llIndicator;

    private List<View> mAllViews;

    /**
     * 标记滑动到第几块的点
     */
    private ImageView[] mIndicators;
    /**
     * 图片集合
     */
    private ImageView[] mImgs;

    private Drawable mIndicatorUnSelected;
    private Drawable mIndicatorSelected;
    // 默认显示指示器
    private boolean mShowIndicator;

    private int mCurrentIndex = -1;
    private float mCurrentMove = -1;

    public ImageGallery(Context context) {
        this(context, null);
    }

    public ImageGallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageGallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageGallery);
        mIndicatorUnSelected = typedArray.getDrawable(R.styleable.ImageGallery_indicatorUnSelected);
        mIndicatorSelected = typedArray.getDrawable(R.styleable.ImageGallery_indicatorSelected);
        mShowIndicator = typedArray.getBoolean(R.styleable.ImageGallery_showIndicator, true);

        typedArray.recycle();

        LayoutInflater.from(context).inflate(R.layout.view_image_gallery, this, true);
        vpImages = (ViewPager) findViewById(R.id.vpImages);
        llIndicator = (LinearLayout) findViewById(R.id.llIndicator);
        llIndicator.setVisibility(mShowIndicator ? View.VISIBLE : View.GONE);
        mAllViews = new ArrayList<View>();
    }

    /**
     * 设置选中的indicator
     *
     * @param index
     */
    private void setIndicator(int index) {
        for (int i = 0;i < mIndicators.length;i++) {
            if (i == index) {
                if (null == mIndicatorSelected) {
                    mIndicators[i].setImageResource(R.drawable.ic_guidance_selected);
                } else {
                    mIndicators[i].setImageDrawable(mIndicatorSelected);
                }
            } else {
                if (null == mIndicatorUnSelected) {
                    mIndicators[i].setImageResource(R.drawable.ic_guidance_unselected);
                } else {
                    mIndicators[i].setImageDrawable(mIndicatorUnSelected);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentIndex = position;
        mCurrentMove = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {
        if (mShowIndicator) {
            setIndicator(position);
        }
//        if (position == mAllViews.size()) {
//            // 如果是最后一张图片，则返回第一张图片
//            vpImages.setCurrentItem(0);
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void show(List<String> imgUrls, int defRes) {
        // 底部indicator
        mIndicators = new ImageView[imgUrls.size()];
        for (int i = 0;i < imgUrls.size();i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            mIndicators[i] = imageView;
            if (i == 0) {
                if (null == mIndicatorSelected) {
                    mIndicators[i].setImageResource(R.drawable.ic_guidance_selected);
                } else {
                    mIndicators[i].setImageDrawable(mIndicatorSelected);
                }
            } else {
                if (null == mIndicatorUnSelected) {
                    mIndicators[i].setImageResource(R.drawable.ic_guidance_unselected);
                } else {
                    mIndicators[i].setImageDrawable(mIndicatorUnSelected);
                }
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            llIndicator.addView(imageView, layoutParams);
        }

        // 图片展示
        mImgs = new ImageView[imgUrls.size()];
        for (int i = 0;i < imgUrls.size();i++) {
            ImageView iv = new ImageView(mContext);
            mImgs[i] = iv;
            // 图片缓存框架载入图片
            GlideUtil.setImage(iv, imgUrls.get(i), defRes, defRes);
            mAllViews.add(iv);
        }

        vpImages.setAdapter(new GalleryAdapter());
        vpImages.setOnPageChangeListener(this);
        vpImages.setCurrentItem(0);
    }

    public class GalleryAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mAllViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(mAllViews.get(position % mAllViews.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(mAllViews.get(position % mAllViews.size()), 0);
            return mAllViews.get(position % mAllViews.size());
        }
    }

    public boolean ismShowIndicator() {
        return mShowIndicator;
    }

    public void setShowIndicator(boolean mShowIndicator) {
        this.mShowIndicator = mShowIndicator;
    }

    public Drawable getmIndicatorSelected() {
        return mIndicatorSelected;
    }

    public void setIndicatorSelected(Drawable mIndicatorSelected) {
        this.mIndicatorSelected = mIndicatorSelected;
    }

    public Drawable getIndicatorUnSelected() {
        return mIndicatorUnSelected;
    }

    public void setIndicatorUnSelected(Drawable mIndicatorUnSelected) {
        this.mIndicatorUnSelected = mIndicatorUnSelected;
    }
}
