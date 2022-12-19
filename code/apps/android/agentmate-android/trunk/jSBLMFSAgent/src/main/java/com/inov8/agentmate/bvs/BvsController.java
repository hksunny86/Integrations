package com.inov8.agentmate.bvs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

public abstract class BvsController {

    protected Activity activity;
    protected BvsCompleteListner bvsListner;

    protected abstract void onCreate();
    protected abstract void onStart();
    protected abstract void onResume();
    protected abstract void onPause();
    protected abstract void onStop();
    protected abstract void onDestroy();
    protected abstract void onBackPressed();
    protected abstract void scanImage();
    protected abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public interface BvsCompleteListner{
        public void changeLayoutAfterFirstScan();
        public void showToast(String error);
        public void setInfoMessage(String message);
        public void setButtonText(String text);
        public void setButtonEnabled(Boolean enabled);
        public void setHexString(String hexString);
        public void setWSQ(String wsq);
        public void setMinutiaeCount(String minutiaeCount);
        public void setNFIQuality(String NFIQuality);
        public void setBitmap(Bitmap bmp);
        public void setTemplateType(String type);
    }
}
