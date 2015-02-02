package com.ls.view;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by ls on 15-1-29.
 */
public class SwipeLayout extends FrameLayout {
    /**
     * 重新布置ListView的Item与滑动菜单View,处理点击事件
     */
    private int position;
    private SwipeView mSwipeView;
    private View mContentView;
    private Interpolator openInterpolator;
    private Interpolator closeInterpolator;
    private GestureDetectorCompat detectorCompat;
    private GestureDetector.OnGestureListener gestureListener;
    private boolean isFling;
    private int MIN_FLING = dp2px(15);
    private int MAX_VELOCITY_X = -dp2px(500);
    private ScrollerCompat openScrollerCompat;
    private ScrollerCompat closeScrollerCompat;
    private float mDownX;
    private boolean open;
    private int mBaseX;



    public SwipeLayout(View view, SwipeView swipeView) {
        super(view.getContext());
        init();
    }


    public SwipeLayout(View view, SwipeView swipeView, Interpolator openInterpolator,
                       Interpolator closeInterpolator) {

        super(view.getContext());
        this.mContentView = view;
        this.mSwipeView = swipeView;
        this.openInterpolator = openInterpolator;
        this.closeInterpolator = closeInterpolator;
        init();
    }

    private void init() {
        setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));//设置布局参数
        gestureListener = new GestureDetector.SimpleOnGestureListener() {//设置手势监听
            @Override
            public boolean onDown(MotionEvent e) {
                isFling = false;
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                if (e1.getX() - e2.getX() > MIN_FLING && velocityX < MAX_VELOCITY_X) {
                    isFling = true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };

        detectorCompat = new GestureDetectorCompat(getContext(), gestureListener);
        if (openInterpolator != null)
            openScrollerCompat = ScrollerCompat.create(getContext(), openInterpolator);
        else openScrollerCompat = ScrollerCompat.create(getContext());
        if (closeInterpolator != null)
            closeScrollerCompat = ScrollerCompat.create(getContext(), closeInterpolator);
        else closeScrollerCompat = ScrollerCompat.create(getContext());

        //添加View
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(layoutParams);
        addView(mContentView);

        mSwipeView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));
        addView(mSwipeView);


    }

    private int dp2px(int dp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void closeMenu() {

    }

    /**
     * 菜单是否打开
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * 处理传递事件
     */
    public void swipe(MotionEvent ev) {
        detectorCompat.onTouchEvent(ev);
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                isFling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float distance = mDownX - ev.getX();
                if (open) {
                    distance += mSwipeView.getMeasuredWidth();
                }
                swipeView((int) distance);
                break;
            case MotionEvent.ACTION_UP:
                float lastDistance = mDownX - ev.getX();
                if (lastDistance < 0) {
                    if (lastDistance < -mSwipeView.getMeasuredWidth() / 2) {
                        open = false;
                        smoothClose();
                    } else smoothOpen();
                } else if (lastDistance > 0) {
                    if (lastDistance > mSwipeView.getMeasuredWidth() / 2) {
                        open = true;
                        smoothOpen();
                    } else smoothClose();
                }
                System.out.println("layout up isopen  " + open);
                break;

        }
    }

    /**
     * 更改子view的布局参数
     */
    private void swipeView(int distance) {

        int maxDistance = mSwipeView.getMeasuredWidth();
        if (distance > maxDistance) {
            distance = maxDistance;
        }
        if (distance < 0) {//注意此处判断菜单是否打开
            distance = 0;
        }

        mContentView.layout(-distance, 0, getMeasuredWidth() - distance,
                mContentView.getMeasuredHeight());
        mSwipeView.layout(getMeasuredWidth() - distance, 0, getMeasuredWidth() + mSwipeView
                .getMeasuredWidth() - distance, mContentView.getMeasuredHeight());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSwipeView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(mSwipeView.getHeight(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        System.out.println(" mContentView width  " + mContentView.getWidth());
        mContentView.layout(0, 0, mContentView.getMeasuredWidth(), getMeasuredHeight());
        mSwipeView.layout(getMeasuredWidth(), 0, mSwipeView.getMeasuredWidth() + getMeasuredWidth
                (), mContentView.getMeasuredHeight());//初始化布局
    }

    public void smoothClose() {
        open = false;
        mBaseX = -mContentView.getLeft();
        closeScrollerCompat.startScroll(0, 0, mBaseX, 0, 350);
        postInvalidate();
    }

    public void smoothOpen() {
        open = true;
        openScrollerCompat.startScroll(-mContentView.getLeft(), 0,
                mSwipeView.getWidth(), 0, 350);
        postInvalidate();

    }

    @Override
    public void computeScroll() {
        if (open) {
            if (openScrollerCompat.computeScrollOffset()) {
                swipeView(openScrollerCompat.getCurrX());
                postInvalidate();
            }
        } else {
            if (closeScrollerCompat.computeScrollOffset()) {
                swipeView(mBaseX - closeScrollerCompat.getCurrX());
                postInvalidate();
            }
        }

    }
}
