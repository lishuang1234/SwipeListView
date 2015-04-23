package com.ls.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by ls on 15-1-29.
 */
public class SwipeItem {
    /**
     * 滑动的菜单项目实体
     */
    private Context mContext;
    private int id;
    private String title;
    private Drawable icon;
    private Drawable background;
    private int titleSize;
    private int titleColor;
    private int width;

    public SwipeItem(Context mContext) {
        this.mContext = mContext;
    }


    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public Drawable getBackground() {
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
