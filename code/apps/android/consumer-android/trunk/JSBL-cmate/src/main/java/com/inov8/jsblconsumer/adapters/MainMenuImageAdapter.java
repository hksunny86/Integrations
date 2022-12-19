package com.inov8.jsblconsumer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.CategoryMenuActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

public class MainMenuImageAdapter extends BaseAdapter {
    private Context mContext;
    private Object[] objects = new Object[10];
    public int counter = 0;
    public boolean flag = false;
    private Bundle bundle;

    private ViewHolder holder = null;

    public MainMenuImageAdapter(Context c) {
        mContext = c;
        bundle = new Bundle();
        loadImages();
    }


    private void loadImages() {
        mThumbIds = new Integer[ApplicationData.listCategories.size()];
        mThumbIds_hover = new Integer[ApplicationData.listCategories.size()];
        for (int i = 0; i < ApplicationData.listCategories.size(); i++) {
            if (ApplicationData.listCategories.get(i).getIcon().equals("")) {
                mThumbIds[i] = mContext.getResources().getIdentifier(
                        "main_icon_js_placeholder", "drawable",
                        mContext.getPackageName());
                mThumbIds_hover[i] = mContext.getResources().getIdentifier(
                        "main_icon_js_placeholder_hover", "drawable", mContext.getPackageName());
            } else {
                mThumbIds[i] = mContext.getResources().getIdentifier(
                        ApplicationData.listCategories.get(i).getIcon()
                                .replace(".png", ""), "drawable",
                        mContext.getPackageName());
                mThumbIds_hover[i] = mContext.getResources().getIdentifier(
                        ApplicationData.listCategories.get(i).getIcon()
                                .replace(".png", "")
                                + "_hover", "drawable", mContext.getPackageName());
            }
        }
    }

    public int getCount() {
        return ApplicationData.listCategories.size();
    }

    public Object getItem(int position) {
        return objects[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            final int pos = position;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.grid_item_main_menu, parent, false);

                holder = new ViewHolder();

                holder.image = (AppCompatImageView) convertView
                        .findViewById(R.id.menuItemImgView);

                holder.image.setColorFilter
                        (ContextCompat.getColor(mContext, R.color.menu_icon_normal));

                holder.text = (TextView) convertView
                        .findViewById(R.id.menuItemTxtView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            holder.image.setImageResource(mThumbIds[position]);

            holder.image.setImageDrawable(AppCompatResources.getDrawable(mContext,mThumbIds[position]));

//             holder.text.setText("cddkv mddk");



            holder.image.setOnTouchListener(new OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                public boolean onTouch(View arg0, MotionEvent arg1) {

                    if (arg1.getAction() == MotionEvent.ACTION_DOWN) {

                       // arg0.setBackgroundColor(ContextCompat.getColor(mContext,R.color.offwhite));
                        ((ImageView) arg0).setColorFilter
                                (ContextCompat.getColor(mContext, R.color.menu_icon_pressed));


//                        ((ImageView) arg0).setColorFilter(ContextCompat.getColor(mContext, R.color.light_grey));
//                        ((ImageView) arg0).setBackgroundColor(ContextCompat.getColor(mContext, R.color.offwhite));
//                        ((ImageView) arg0).setBackgroundColor(R.color.blue);
//                        ((ImageView) arg0).setImageResource(mThumbIds_hover[pos]);
                        return true;
                    }

                    if (arg1.getAction() == MotionEvent.ACTION_CANCEL) {
                      //  arg0.setBackgroundColor(ContextCompat.getColor(mContext,R.color.transparent_new));
                        ((ImageView) arg0).setColorFilter
                                (ContextCompat.getColor(mContext, R.color.menu_icon_normal));

//                        ((GridView) arg0).setSelector(new ColorDrawable(Color.TRANSPARENT));
//                        ((ImageView) arg0).setColorFilter(ContextCompat.getColor(mContext, R.color.transparent_new));
//                        ((ImageView) arg0).setBackgroundColor(R.color.white);
                    }

                    if (arg1.getAction() == MotionEvent.ACTION_UP) {

                        counter = counter + 1;
                        flag = true;
                      //  arg0.setBackgroundColor(ContextCompat.getColor(mContext,R.color.transparent_new));
                        ((ImageView) arg0).setColorFilter
                                (ContextCompat.getColor(mContext, R.color.menu_icon_normal));
//                        ((GridView) arg0).setSelector(new ColorDrawable(Color.TRANSPARENT));
//                        ((ImageView) arg0).setColorFilter(ContextCompat.getColor(mContext, R.color.transparent_new));
//                        setBackgroundColor(R.color.white);

                        if (counter == 1 && flag) {
                            Intent intent = new Intent(mContext,
                                    CategoryMenuActivity.class);
                            intent.putExtras(bundle);
                            intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS, pos);
                            intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS_PARENT, pos);
                            intent.putExtra(Constants.IntentKeys.LIST_CATEGORIES,
                                    ApplicationData.listCategories);
                            mContext.startActivity(intent);
                        }
                        return false;
                    }
                    return true;
                }

            });
        } catch (Exception ex) {
            ex.getMessage();
        }

        return convertView;
    }

    public Integer[] mThumbIds;
    public Integer[] mThumbIds_hover;

    class ViewHolder {
        public AppCompatImageView image;
        public TextView text;
    }
}