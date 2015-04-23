package com.ls.view;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ls.adapter.SwipeItem;
import com.ls.adapter.SwipeMenu;

import java.util.List;

/**
 * Created by ls on 15-1-29.
 */
public class SwipeView extends LinearLayout implements View.OnClickListener {
    private List<SwipeItem> swipeItems;
    private int position;
private SwipeViewClickListener clickListener;
    /**
     * 生成滑动的菜单View
     */

    public SwipeView(SwipeMenu swipeMenu, ViewGroup listView,int position ) {
        super(swipeMenu.getContext());
        this.position = position;
        swipeItems = swipeMenu.getSwipeItems();
        int id = 0;
        for (SwipeItem item : swipeItems) {//添加View
            addItems(item, id++);
        }
    }

    private void addItems(SwipeItem item, int id) {
        LinearLayout.LayoutParams layoutParams = new LayoutParams(item.getWidth(),
                ViewGroup.LayoutParams.MATCH_PARENT);//孩子View参数
        LinearLayout child = new LinearLayout(getContext());//新建子View
        child.setId(id);
        child.setBackgroundDrawable(item.getBackground());
        child.setLayoutParams(layoutParams);
        child.setOrientation(VERTICAL);
        child.setGravity(Gravity.CENTER);

        if (item.getIcon() != null) {
            child.addView(createIcon(item.getIcon()));
        }
        if (!TextUtils.isEmpty(item.getTitle())) {
            child.addView(createTitle(item));
        }
        child.setOnClickListener(this);
        addView(child);

    }

    private View createTitle(SwipeItem item) {
        TextView textView = new TextView(getContext());
        textView.setText(item.getTitle());
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(item.getTitleColor());
        textView.setTextSize(item.getTitleSize());
        return textView;
    }

    private View createIcon(Drawable icon) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(icon);
        return imageView;
    }

    /**
     * 菜单项的点击监听
     */
    @Override
    public void onClick(View v) {
        System.out.println("click  -- is" +v.getId());
        if (clickListener!=null)
            clickListener.click(position,v.getId());
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setClickListener(SwipeViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface SwipeViewClickListener {

        void click(int position,int id);
    }
}
