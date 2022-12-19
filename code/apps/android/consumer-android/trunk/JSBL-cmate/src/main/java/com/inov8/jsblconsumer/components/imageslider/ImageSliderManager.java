package com.inov8.jsblconsumer.components.imageslider;

import android.app.AlertDialog;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


import androidx.viewpager.widget.ViewPager;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.adapters.ImageSlideAdapter;
import com.inov8.jsblconsumer.model.AdModel;

import java.util.ArrayList;

public class ImageSliderManager {
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;
    // TextView imgNameTxt;
    PageIndicator mIndicator;
    AlertDialog alertDialog;
    ArrayList<AdModel> adModelList;
    boolean stopSliding = false;
    String message;
    BaseActivity activity;
    View rootView;
    // UI References
    private ViewPager mViewPager;
    private Runnable animateViewPager;
    private Handler handler;

    public ImageSliderManager(BaseActivity pMain, View pRootView,
                              ArrayList<AdModel> pAdModel) {

        activity = pMain;
        rootView = pRootView;
        this.adModelList = pAdModel;

        findViewById(rootView);

        mIndicator.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager
                        if (adModelList != null && adModelList.size() != 0) {
                            stopSliding = false;
                            runnable(adModelList.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY_USER_VIEW);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                            handler.removeCallbacks(animateViewPager);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void findViewById(View view) {
        if (view != null) {
            mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
            mIndicator = (CirclePageIndicator) view
                    .findViewById(R.id.pageIndicator);
            // imgNameTxt = (TextView) view.findViewById(R.id.img_name);
        } else {
            mViewPager = (ViewPager) activity.findViewById(R.id.viewPager);
            mIndicator = (CirclePageIndicator) activity
                    .findViewById(R.id.pageIndicator);
        }
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }

    public void onResume() {
        if (adModelList == null) {
            sendRequest();
        } else {
            if (adModelList.size() > 0) {

                mViewPager.setAdapter(new ImageSlideAdapter(activity, adModelList));

            } else {
                adModelList.add(new AdModel("abc", "abcabcabc_abc"));
                mViewPager.setAdapter(new ImageSlideAdapter(activity, adModelList));
            }

            mIndicator.setViewPager(mViewPager);
            // imgNameTxt.setText(""
            // + ((AdModel) products.get(mViewPager.getCurrentItem()))
            // .getName());
            runnable(adModelList.size());
            // Re-run callback
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
    }

    public void onPause() {
        if (handler != null) {
            // Remove callback
            handler.removeCallbacks(animateViewPager);
        }
    }

    private void sendRequest() {

        if (adModelList != null && adModelList.size() > 0) {

            mViewPager.setAdapter(new ImageSlideAdapter(activity, adModelList));

        } else {
            adModelList = new ArrayList<>();
            adModelList.add(new AdModel("abc", "abcabcabc_abc"));
            mViewPager.setAdapter(new ImageSlideAdapter(activity, adModelList));
        }

        mIndicator.setViewPager(mViewPager);
        // imgNameTxt.setText(""
        // + ((AdModel) products.get(mViewPager.getCurrentItem()))
        // .getName());
        runnable(adModelList.size());
        handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);

    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (adModelList != null) {
                    // imgNameTxt.setText(""
                    // + ((AdModel) products.get(mViewPager
                    // .getCurrentItem())).getName());
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
        }
    }



}
