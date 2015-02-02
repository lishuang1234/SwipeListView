package com.ls.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.ls.main.R;
import com.ls.view.SwipeLayout;
import com.ls.view.SwipeListView;
import com.ls.view.SwipeView;

/**
 * Created by ls on 15-1-29.
 */
public class SwipeAdapter implements WrapperListAdapter,SwipeView.SwipeViewClickListener {
    private ListAdapter mAdapter;
    private Context mContext;

    /**
     * 封装滑动的View
     */

    public SwipeAdapter(ListAdapter adapter, Context context) {
        this.mAdapter = adapter;
        this.mContext = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return (ApplicationInfo) mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    /**
     * 重组客户提供的View与菜单项View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwipeLayout swipeLayout = null;
        if (convertView == null) {
            View view = mAdapter.getView(position, convertView, parent);
            System.out.println("view width "+view.getWidth());
            SwipeMenu swipeMenu = new SwipeMenu(mContext);
            swipeMenu.setViewType(mAdapter.getItemViewType(position));
            createMenu(swipeMenu);//生成菜单项
            SwipeView swipeView = new SwipeView(swipeMenu, parent,position);//生成菜单View
            //此处应该生成监听
            swipeView.setClickListener(this);

            SwipeListView listView = (SwipeListView) parent;
            swipeLayout = new SwipeLayout(view, swipeView, listView.getOpenInterpolator(),
                    listView.getCloseInterpolator());//组装菜单与List Item View
            swipeLayout.setPosition(position);
        } else {
            swipeLayout = (SwipeLayout) convertView;
            swipeLayout.setPosition(position);
            swipeLayout.closeMenu();//默认关闭

        }

        return swipeLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    public void createMenu(SwipeMenu swipeMenu) {
        //open
        SwipeItem openItem = new SwipeItem(mContext);
        openItem.setId(0);
        openItem.setTitle("OPEN");
        openItem.setTitleSize(18);
        openItem.setTitleColor(Color.WHITE);
        openItem.setWidth(100);
        swipeMenu.setSwipeItems(openItem);
        //delete
        SwipeItem deleteItem = new SwipeItem(mContext);
        deleteItem.setId(0);
        deleteItem.setTitle("DELETE");
        deleteItem.setTitleSize(18);
        deleteItem.setTitleColor(Color.WHITE);
        deleteItem.setWidth(100);
        swipeMenu.setSwipeItems(deleteItem);

    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return mAdapter;
    }


    @Override
    public void click(int position, int id) {

    }
}
