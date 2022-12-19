package com.inov8.jsblconsumer.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.model.AdModel;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSlideAdapter extends PagerAdapter {
    BaseActivity activity;
    List<AdModel> adModelList;

    public ImageSlideAdapter(BaseActivity activity, List<AdModel> adModelList) {
        this.activity = activity;
        this.adModelList = adModelList;
    }

    @Override
    public int getCount() {
        return adModelList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.vp_image, container, false);

        ImageView mImageView = (ImageView) view
                .findViewById(R.id.image_display);

        Picasso.with(activity).load(Constants.AD_URL
                + Utility.getExtensionIcon(((adModelList
                .get(position)).getImageUrl()))).
                placeholder(R.drawable.img_placeholder).into(mImageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}