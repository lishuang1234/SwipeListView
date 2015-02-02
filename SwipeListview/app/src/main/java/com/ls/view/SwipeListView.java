package com.ls.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ls.adapter.SwipeAdapter;
import com.ls.adapter.SwipeCreator;
import com.ls.adapter.SwipeMenu;

/**
 * Created by ls on 15-1-29.
 */
public class SwipeListView extends ListView {
    /**
     * 自定义ListView，动态代理Adapter
     */
    private SwipeCreator mCreator;
    private Interpolator openInterpolator;
    private Interpolator closeInterpolator;
    private SwipeLayout mTouchView;
    private float mDownX;
    private float mDownY;
    private int mTouchPosition;//触摸的item位置
    public static final int TOUCH_STATE_NONE = 0;
    public static final int TOUCH_STATE_X = 1;
    public static final int TOUCH_STATE_Y = 2;
    private int mTouchState;
    public static final int MAX_X = 3;
    public static final int MAX_Y = 5;
    private OnSwipeViewClickListener onSwipeViewClickListener;


    public SwipeListView(Context context) {
        super(context);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 注意此处的代理实现，同时覆写相关方法
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(new SwipeAdapter(adapter, getContext()) {
            @Override
            public void createMenu(SwipeMenu swipeMenu) {
                if (mCreator != null) mCreator.createMenu(swipeMenu);
            }

            @Override
            public void click(int position, int id) {
                if (onSwipeViewClickListener!=null){
                    onSwipeViewClickListener.click(position,id);
                }
            }
        });
    }

    /**
     * 触摸监听，判断触摸位置
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = MotionEventCompat.getActionMasked(ev);
        //按下时如果滑动View为空，则不处理事件
        //  if (action == MotionEvent.ACTION_DOWN && mTouchView == null) return super
        // .onTouchEvent(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mDownX = ev.getX();
                mDownY = ev.getY();
                int oldPosition = mTouchPosition;
                mTouchPosition = pointToPosition((int) mDownX, (int) mDownY);//坐标转换为位置
                mTouchState = TOUCH_STATE_NONE;//初始化是菜单关闭状态
                //1.如果是触摸到打开的菜单项，使mTouchView跟随手指滑动
                if (mTouchPosition == oldPosition && mTouchView != null && mTouchView.isOpen()) {
                    System.out.println("触摸到打开的菜单项 ");
                    mTouchState = TOUCH_STATE_X;//菜单打开
                    mTouchView.swipe(ev);
                    return true;//事件已处理
                }

                //2.如果是触摸到其他未打开菜单,关闭菜单已打开
                if (mTouchView != null && mTouchView.isOpen()) {
                    System.out.println("触摸到其他未打开菜单,关闭菜单已打开");
                    mTouchView.smoothClose();
                    return super.onTouchEvent(ev);
                }


                View view = getChildAt(mTouchPosition);//获得触摸的View
                if (view instanceof SwipeLayout) {
                    System.out.println("get touch view !!!");
                    mTouchView = (SwipeLayout) view;
                    if (mTouchView != null) mTouchView.swipe(ev);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                System.out.println(" menu is open ? "+mTouchView.isOpen());
                float dx = Math.abs(ev.getX() - mDownX);
                float dy = Math.abs(ev.getY() - mDownY);
                if (dy > MAX_Y && mTouchState == TOUCH_STATE_NONE)//第一次判断
                    return super.onTouchEvent(ev);
                else if (dx > MAX_X) {
                    mTouchState = TOUCH_STATE_X;
                    if (mTouchView != null) mTouchView.swipe(ev);
                }
                getSelector().setState(new int[]{0});

                break;
            case MotionEvent.ACTION_UP:

                if (mTouchState == TOUCH_STATE_X && mTouchView != null) {
                    mTouchView.swipe(ev);
                    if (!mTouchView.isOpen()) {//关闭
                        mTouchView = null;
                    }
                    return true;
                }

                break;

        }


        return super.onTouchEvent(ev);
    }


    public void setMenuCreator(SwipeCreator creator) {
        this.mCreator = creator;
    }


    public Interpolator getOpenInterpolator() {
        return openInterpolator;
    }

    public void setOpenInterpolator(Interpolator openInterpolator) {
        this.openInterpolator = openInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return closeInterpolator;
    }

    public void setCloseInterpolator(Interpolator closeInterpolator) {
        this.closeInterpolator = closeInterpolator;
    }

    public void setOnSwipeViewClickListener(OnSwipeViewClickListener onSwipeViewClickListener) {
        this.onSwipeViewClickListener = onSwipeViewClickListener;
    }

    public interface OnSwipeViewClickListener {
        void click(int position, int id);
    }

}
