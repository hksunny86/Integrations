package com.inov8.jsblconsumer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.CategoryMenuActivity;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

public class SubMenuAdapter extends BaseAdapter {
    private Context mContext;
    private Object[] objects = new Object[20];
    public int counter = 0;
    public boolean flag = false;
    private Bundle bundle;

    private String[] values;
    private String[] logos;

    private ViewHolder holder = null;

    public SubMenuAdapter(Context c, String[] values, String[] logos) {
        mContext = c;
        this.values = values;
        this.logos = logos;
    }



    public int getCount() {
        return values.length;
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_with_icon, parent, false);

                holder = new ViewHolder();

                holder.image = convertView.findViewById(R.id.image);
                holder.icon = convertView.findViewById(R.id.icon);
                holder.txtOption = convertView.findViewById(R.id.txtOption);
                holder.lyt_parent = convertView.findViewById(R.id.cardView);

                if (logos != null) {
                   if (logos[position] != null && !logos[position].equals("null"))
                Glide.with(mContext).load(Constants.PROD_IMG_URL + logos[position]).placeholder(R.drawable.js_placeholder).into(holder.image);
                } else {
                    holder.image.setVisibility(View.GONE);
                }
                holder.txtOption.setText(values[position]);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


        } catch (Exception ex) {
            ex.getMessage();
        }

        return convertView;
    }



    class ViewHolder {
        public ImageView image, icon;
        public TextView txtOption;
        public View lyt_parent;

    }
}