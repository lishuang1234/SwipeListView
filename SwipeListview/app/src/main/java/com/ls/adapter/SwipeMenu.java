package com.ls.adapter;

import android.content.Context;

import com.ls.view.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ls on 15-1-29.
 */
public class SwipeMenu {
    /**
     * 滑动菜单条目集合,滑动之后展示的条目集合
     */

    private List<SwipeItem> swipeItems;
    private Context mContext;
    private int viewType;

    public SwipeMenu( Context mContext) {
        this.mContext = mContext;
        swipeItems=new ArrayList<SwipeItem>();
    }


    public List<SwipeItem> getSwipeItems() {
        return swipeItems;
    }

    public void setSwipeItems(SwipeItem swipeItem) {
        this.swipeItems.add(swipeItem);
    }

    public Context getContext() {
        return mContext;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
