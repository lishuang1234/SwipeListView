package com.ls.main;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ls.adapter.SwipeCreator;
import com.ls.adapter.SwipeItem;
import com.ls.adapter.SwipeMenu;
import com.ls.view.SwipeListView;
import com.ls.view.SwipeView;

import java.util.List;


public class MainActivity extends Activity implements SwipeListView.OnSwipeViewClickListener{
    private SwipeListView swipeListView;
    private List<ApplicationInfo> applicationInfos;
    private AppAdapter appAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeListView = (SwipeListView) findViewById(R.id.swipeListView);
        //    ListView listView = (ListView) findViewById(R.id.test);
        swipeListView.setOnSwipeViewClickListener(this);
        applicationInfos = getPackageManager().getInstalledApplications(0);
        appAdapter = new AppAdapter();
        //    listView.setAdapter(appAdapter);
        swipeListView.setAdapter(appAdapter);
        /**生成滑动的菜单项目*/
        SwipeCreator creator = new SwipeCreator() {
            @Override
            public void createMenu(SwipeMenu swipeMenu) {
                //open
                SwipeItem openItem = new SwipeItem(getApplicationContext());
                openItem.setId(0);
                openItem.setTitle("OPEN");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                openItem.setWidth(dp2px(90));
                openItem.setBackground(new ColorDrawable(Color.RED));
                swipeMenu.setSwipeItems(openItem);
                //delete
                SwipeItem deleteItem = new SwipeItem(getApplicationContext());
                deleteItem.setId(1);
                deleteItem.setTitle("DELETE");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(getResources().getDrawable(R.drawable.ic_delete));
                deleteItem.setBackground(new ColorDrawable(Color.BLUE));
                swipeMenu.setSwipeItems(deleteItem);
            }
        };

        swipeListView.setMenuCreator(creator);
    }


    public void click(int position, int id) {
        ApplicationInfo item = applicationInfos.get(position);
        switch (id) {
            case 0:
                openItem(item);
                break;
            case 1:
                applicationInfos.remove(item);
                appAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "delete " + item.className,
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void openItem(ApplicationInfo item) {

        Toast.makeText(getApplicationContext(), "open " + item.className,
                Toast.LENGTH_SHORT).show();
    }




    /**
     * ListView的适配器
     */
    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return applicationInfos.size();
        }

        @Override
        public ApplicationInfo getItem(int position) {
            return applicationInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.listview_item,
                        null);//加载Item布局
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            ApplicationInfo applicationInfo = getItem(position);
            holder.icon.setImageDrawable(applicationInfo.loadIcon(getPackageManager()));//设置显示图标
            holder.name.setText(applicationInfo.loadLabel(getPackageManager()));//显示名称
            return convertView;
        }

        /**
         * 保存得到的View引用
         */
        class ViewHolder {
            public ViewHolder(View view) {
                name = (TextView) view.findViewById(R.id.name);
                icon = (ImageView) view.findViewById(R.id.icon);
                view.setTag(this);//view设置标签保存引用
            }

            TextView name;
            ImageView icon;
        }
    }

    /**
     * dp转化为px
     */
    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
