package com.inov8.agentmate.bvs.supremaSlim2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Base64;

import com.common.pos.api.util.posutil.PosUtil;
import com.example.ftransisdk.FrigerprintControl;
import com.inov8.agentmate.bvs.BvsController;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PopupDialogs;
import com.suprema.BioMiniFactory;
import com.suprema.CaptureResponder;
import com.suprema.IBioMiniDevice;
import com.suprema.IUsbEventHandler;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class SupremaSlim2Handler extends BvsController {

    private Activity activity;
    byte[] fpImage = null;
    private  int count = 0;
    private int minutiaeCount, nfiQuality;
    public IBioMiniDevice mCurrentDevice = null;
    private static BioMiniFactory mBioMiniFactory = null;
    private IBioMiniDevice.CaptureOption mCaptureOptionDefault = new IBioMiniDevice.CaptureOption();
    private CaptureResponder mCaptureResponseDefault = new CaptureResponder() {
        @Override
        public boolean onCaptureEx(final Object context, final Bitmap capturedImage,
                                   final IBioMiniDevice.TemplateData capturedTemplate,
                                   final IBioMiniDevice.FingerState fingerState) {
            runOnUiThread(() -> {
                bvsListner.setButtonEnabled(true);
                if (capturedImage != null) {
                    count++;
                    getDetails();
                    validateData(capturedImage);
                }
            });
            return true;
        }

        @Override
        public void onCaptureError(Object contest, int errorCode, String error) {
            bvsListner.setButtonEnabled(true);
            runOnUiThread(() -> {
                bvsListner.setButtonEnabled(true);
                bvsListner.setInfoMessage(error);
            });
        }
    };

    public SupremaSlim2Handler(Activity activity) {
        this.activity = activity;
        this.bvsListner = (BvsCompleteListner) activity;
    }

    @Override
    protected void onCreate() {
        new OpenTask().execute();
        mCaptureOptionDefault.frameRate = IBioMiniDevice.FrameRate.SHIGH;
        if (mBioMiniFactory != null) {
            mBioMiniFactory.close();
        }
        restartBioMini();
    }

    @Override
    protected void onStart() {
        AppLogger.i("Called onStart()");
    }

    @Override
    protected void onResume() {
        AppLogger.i("Called onResume()");
    }

    @Override
    protected void onPause() {
        AppLogger.i("Called onPause()");
    }

    @Override
    protected void onStop() {
        AppLogger.i("Called onStop()");
    }

    @Override
    protected void onDestroy() {
        try {
            new CloseTask().execute();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        if (mBioMiniFactory != null) {
            mBioMiniFactory.close();
            mBioMiniFactory = null;
        }
        AppLogger.i("Called onDestroy()");
    }

    @Override
    protected void onBackPressed() {

    }

    @Override
    protected void scanImage() {
        if (mCurrentDevice != null) {
            bvsListner.setInfoMessage("Put your finger");
            bvsListner.setButtonEnabled(false);
            mCurrentDevice.captureSingle(mCaptureOptionDefault, mCaptureResponseDefault, true);
        } else {
            PopupDialogs.alertDialog("The scanner could not be initialized because it was not found. Please check if the scanner is properly attached and try again.", PopupDialogs.Status.ERROR,
                    activity, (v, obj) -> {
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    void restartBioMini() {
        if (mBioMiniFactory != null) {
            mBioMiniFactory.close();
        }
        mBioMiniFactory = new BioMiniFactory(activity) {
            @Override
            public void onDeviceChange(DeviceChangeEvent event, Object dev) {
                handleDevChange(event, dev);
            }
        };
    }

    void handleDevChange(IUsbEventHandler.DeviceChangeEvent event, Object dev) {
        if (event == IUsbEventHandler.DeviceChangeEvent.DEVICE_ATTACHED && mCurrentDevice == null) {
            new Thread(() -> {
                int cnt = 0;
                while (mBioMiniFactory == null && cnt < 20) {
                    SystemClock.sleep(1000);
                    cnt++;
                }
                if (mBioMiniFactory != null) {
                    mCurrentDevice = mBioMiniFactory.getDevice(0);
                    if (mCurrentDevice != null) {
                        // mCurrentDevice.getDeviceInfo().deviceName);
                        // mCurrentDevice.getDeviceInfo().deviceSN);
                        // mCurrentDevice.getDeviceInfo().versionSDK);
                        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.SECURITY_LEVEL, 7));
                        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.SENSITIVITY, 4));
                        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.TIMEOUT, 4));
                        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.DETECT_FAKE, 5));
                        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.FAST_MODE, false ? 1 : 0));
                        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.SCANNING_MODE, false ? 1 : 0));
                        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.EXT_TRIGGER, false ? 1 : 0));
                        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.ENABLE_AUTOSLEEP, false ? 1 : 0));
                        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.EXTRACT_MODE_BIOSTAR, false ? 1 : 0));
                    }
                }
            }).start();
        } else if (mCurrentDevice != null && event == IUsbEventHandler.DeviceChangeEvent.DEVICE_DETACHED && mCurrentDevice.isEqual(dev)) {
            mCurrentDevice = null;
        }
    }

    void getDetails() {

        if ((mCurrentDevice != null && (fpImage = mCurrentDevice.getCaptureImageAsRAW_8()) != null)) {
            // NFIQuality
            nfiQuality = mCurrentDevice.getFPQuality(fpImage, mCurrentDevice.getImageWidth(), mCurrentDevice.getImageHeight(), 1);
            bvsListner.setNFIQuality(String.valueOf(nfiQuality));
        }

        // ISO template
        mCurrentDevice.setParameter(new IBioMiniDevice.Parameter(IBioMiniDevice.ParameterType.TEMPLATE_TYPE, IBioMiniDevice.TemplateType.ISO19794_2.value()));
        IBioMiniDevice.TemplateData tmp = mCurrentDevice.extractTemplate();
        byte hexi[] = Base64.encode(tmp.data, Base64.NO_WRAP);
        String hexString = new String(hexi);
        bvsListner.setTemplateType(Constants.FINGER_TEMPLATE_TYPE_ISO);
        bvsListner.setHexString(hexString);

        // Minutiae Count
        minutiaeCount = mCurrentDevice.getFeatureNumber(tmp.data, tmp.data.length);
        bvsListner.setMinutiaeCount(String.valueOf(minutiaeCount));

        // WSQ
        byte[] wsq = mCurrentDevice.getCaptureImageAsWsq(-1, -1, 3.5f, 0);
        byte hex[] = Base64.encode(wsq, Base64.NO_WRAP);
        String WSQhexString = new String(hex);
        ApplicationData.WSQ = WSQhexString;
        bvsListner.setWSQ(WSQhexString);
    }

    void validateData(Bitmap bitmap) {
        if (count <= 3 && (minutiaeCount < 13 || minutiaeCount > 255 || nfiQuality > 3 || nfiQuality < 1)) {
            PopupDialogs.alertDialog("Poor fingerprint quality. Put your finger on scanner and press scan again.", PopupDialogs.Status.ERROR,
                    activity, (v, obj) -> {
                    });
        } else {
            bvsListner.setBitmap(bitmap);
            bvsListner.setInfoMessage("Fingerprint Scanned!");
            bvsListner.changeLayoutAfterFirstScan();
        }
    }

    private class OpenTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                PosUtil.setFingerPrintPower(PosUtil.FINGERPRINT_POWER_ON);
                FrigerprintControl.frigerprint_power_on();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class CloseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                PosUtil.setFingerPrintPower(PosUtil.FINGERPRINT_POWER_OFF);
                FrigerprintControl.frigerprint_power_off();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
