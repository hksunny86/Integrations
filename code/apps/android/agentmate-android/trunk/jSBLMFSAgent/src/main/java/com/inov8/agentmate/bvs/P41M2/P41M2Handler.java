package com.inov8.agentmate.bvs.P41M2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.HEROFUN.HAPI;
import com.HEROFUN.LAPI;
import com.inov8.agentmate.bvs.BvsController;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PopupDialogs;

import java.util.Arrays;
import java.util.regex.Pattern;

import static com.HEROFUN.HostUsb.SaveAsFile;
import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import static java.lang.Integer.max;

public class P41M2Handler extends BvsController {

    private LAPI m_cLAPI = null;
    private int m_hDevice = 0;
    private byte[] m_image = new byte[LAPI.WIDTH * LAPI.HEIGHT];
    private byte[] m_iso_template = new byte[LAPI.FPINFO_STD_MAX_SIZE];
    private byte[] bfwsq = new byte[512 * 512];
    private int[] RGBbits = new int[256 * 360];

    public static final int MESSAGE_SHOW_TEXT = 101;
    public static final int MESSAGE_SET_MINUTIAE_COUNT = 103;
    public static final int MESSAGE_SET_NFI_QUALITIY = 104;
    public static final int MESSAGE_SHOW_IMAGE = 200;
    public static final int MESSAGE_SET_ISO_TEMPLATE = 300;
    public static final int MESSAGE_SET_COMPRESS_TO_WSQ = 303;
    public static final int MESSAGE_SET_CHANGE_LAYOUT = 400;
    public static final int MESSAGE_SET_ENABLED_BTN = 401;
    public static final int MESSAGE_SET_TEMPLATE_TYPE = 402;
    public static final int MESSAGE_SHOW_TOAST = 403;

    private int NFIQuality, minutiaeCount;

    private HAPI m_cHAPI = null;

    private boolean DEBUG = true;
    private volatile boolean bContinue = false;
    Activity myThis;

    private Context mContext;

    public P41M2Handler(Activity activity) {
        this.activity = activity;
        this.bvsListner = (BvsCompleteListner) activity;
    }

    @Override
    protected void onCreate() {

        AppLogger.i("Called onCreate()");

        myThis = activity;

        m_cLAPI = new LAPI(activity);
        m_cHAPI = new HAPI(activity, null);

        mContext = activity.getApplicationContext();

        Runnable r = () -> OPEN_DEVICE();
        Thread s = new Thread(r);
        s.start();

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
    protected void scanImage() {

        if (bContinue) {
            bContinue = false;
            return;
        }
        bContinue = true;
        bvsListner.setButtonEnabled(false);

        bContinue = true;
        Runnable r = () -> ON_VIDEO();
        Thread s = new Thread(r);
        s.start();
//        final Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                //           GET_IMAGE();
//                ON_VIDEO();
//            }
//        });
    }

    @Override
    protected void onPause() {
        m_cHAPI.DoCancel();
        bContinue = false;
        AppLogger.i("Called onPause()");
    }

    @Override
    protected void onStop() {
        AppLogger.i("Called onStop()");
    }

    @Override
    protected void onDestroy() {
        if (m_hDevice != 0) CLOSE_DEVICE();
        AppLogger.i("Called onDestroy()");
    }

    @Override
    protected void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    protected void OPEN_DEVICE() {
        m_hDevice = m_cLAPI.OpenDeviceEx();
        if (m_hDevice == 0)
            bvsListner.setInfoMessage("Can't open device !");
        else
            AppLogger.i("OpenDevice() = OK");

        m_cHAPI.m_hDev = m_hDevice;
    }

    protected void CLOSE_DEVICE() {
        m_cHAPI.DoCancel();
        if (m_hDevice != 0)
            m_cLAPI.CloseDeviceEx(m_hDevice);

        AppLogger.i("CloseDevice() = OK");
        m_hDevice = 0;
        m_cHAPI.m_hDev = m_hDevice;
    }

    protected void GET_IMAGE() {
        bvsListner.setInfoMessage("Put your finger");
        int count = 0;
        while (bContinue) {
            int ret = m_cLAPI.GetImage(m_hDevice, m_image);
            count++;
            if (ret == LAPI.NOTCALIBRATED) {
                AppLogger.i("not Calibrated!");
                break;
            }
            if (ret != LAPI.TRUE) {
                bvsListner.setInfoMessage("Can't get image!");
                bContinue = false;
                break;
            }
            ret = m_cLAPI.IsPressFinger(m_hDevice, m_image);
            if (ret >= LAPI.DEF_FINGER_SCORE) {
                AppLogger.i("GetImage() = OK");
                ApplicationData.errorCount++;
                ret = m_cLAPI.CreateISOTemplate(m_hDevice, m_image, m_iso_template);
                bvsListner.setHexString(CREATE_ISO_TEMP(ret));
                AppLogger.i("GetMinutiae() = OK");
                byte[] rawChannel3 = Arrays.copyOf(m_image, LAPI.WIDTH * LAPI.HEIGHT * 3);
                int i = m_cLAPI.DrawMinutiaePoint(m_hDevice, rawChannel3);
                minutiaeCount = i;
                if (i > 0) {
                    AppLogger.i("GetMinutiae() = OK ，minutiae point count = " + minutiaeCount);
                    bvsListner.setMinutiaeCount(Integer.toString(minutiaeCount));
                } else {
                    AppLogger.i("GetMinutiae() fail!");
                }
                break;
            }
            if (ret <= LAPI.DEF_FINGER_SCORE && ret > 0) {
                bContinue = false;
                Toast.makeText(activity, "Put your finger on scanner correctly and press scan again.", Toast.LENGTH_SHORT).show();
  //              bvsListner.setButtonEnabled(true);
                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_ENABLED_BTN, 0, 0, true));

                break;
            }
            if (count == 2 && m_cLAPI.IsPressFinger(m_hDevice, m_image) == 0) {
                bContinue = false;
                Toast.makeText(activity, "Put your finger on scanner and press scan again.", Toast.LENGTH_SHORT).show();
 //               bvsListner.setButtonEnabled(true);
                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_ENABLED_BTN, 0, 0, true));
                break;
            }
        }
        if (!bContinue)
            return;
        else
            bContinue = false;

        GET_NFI_QUALITY();

        if (ApplicationData.errorCount < 3 && (minutiaeCount < 13 || minutiaeCount > 255 || NFIQuality > 3 || NFIQuality < 1)) {
            PopupDialogs.alertDialog("Poor fingerprint quality. Put your finger on scanner and press scan again.", PopupDialogs.Status.ERROR,
                    activity, (v, obj) -> {
                    });
            bContinue = false;
 //           bvsListner.setButtonEnabled(true);
            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_ENABLED_BTN, 0, 0, true));

            return;
        } else {
            bvsListner.setInfoMessage("Fingerprint Scanned!");
            bvsListner.changeLayoutAfterFirstScan();
            ShowFingerBitmap(m_image, LAPI.WIDTH, LAPI.HEIGHT);
            bvsListner.setTemplateType(Constants.FINGER_TEMPLATE_TYPE_ISO);
            COMPRESS_TO_WSQ();
        }
    }

    protected String CREATE_ISO_TEMP(int res) {
        int i;
        String msg, str;
        if (res == 0) msg = "Can't create ISO template!";
        else msg = "CreateISOTemplate() = OK";

        msg = "";
        for (i = 0; i < LAPI.FPINFO_STD_MAX_SIZE; i++) {
            msg += String.format("%02x", m_iso_template[i]);
        }

        byte hexi[] = Base64.encode(m_iso_template, Base64.NO_WRAP);
        String hexString = new String(hexi);
        return hexString;
    }

    protected void COMPRESS_TO_WSQ() {
        long wsqsize = m_cLAPI.CompressToWSQImage(m_hDevice, m_image, bfwsq);
        boolean res = SaveAsFile("image.wsq", bfwsq, (int) wsqsize);
        if (wsqsize != 0) AppLogger.i("CompressToWSQImage() = OK.");
        else AppLogger.i("CompressToWSQImage() = Fail.");
        byte hexi[] = Base64.encode(bfwsq, Base64.NO_WRAP);
        String hexString = new String(hexi);
        ApplicationData.WSQ = hexString;
 //       bvsListner.setWSQ(" ");
    }

    protected void COMPRESS_TO_WSQ2() {
        long wsqsize = m_cLAPI.CompressToWSQImage(m_hDevice, m_image, bfwsq);
//        boolean res = SaveAsFile("image.wsq", bfwsq, (int) wsqsize);
        if (wsqsize != 0) AppLogger.i("CompressToWSQImage() = OK.");
        else AppLogger.i("CompressToWSQImage() = Fail.");
        String wsqBase64Inner = Base64.encodeToString(bfwsq, Base64.DEFAULT).replace("\n", "");
        ApplicationData.WSQ = handleWsqCase(wsqBase64Inner);
    }

    protected int GET_NFI_QUALITY() {
        int qr;
        String[] degree = {"excellent", "very good", "good", "poor", "fair"};
        qr = m_cLAPI.GetNFIQuality(m_hDevice, m_image);
        NFIQuality = qr;
        AppLogger.i("GetNFIQuality() = %d : %s" + NFIQuality + degree[NFIQuality - 1]);
//        bvsListner.setNFIQuality(Integer.toString(NFIQuality));
        return NFIQuality;
    }

    protected void ON_VIDEO() {

        m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SHOW_TEXT, 0, 0, "Put your finger"));
        int count = 0;
        while (bContinue) {
            count++;
            int ret = m_cLAPI.GetImage(m_hDevice, m_image);
            if (ret == LAPI.NOTCALIBRATED) {
                m_fEvent.obtainMessage(MESSAGE_SHOW_TEXT, 0, 0, "not Calibrated !").sendToTarget();
                break;
            }
            if (ret != LAPI.TRUE) {
                m_fEvent.obtainMessage(MESSAGE_SHOW_TEXT, 0, 0, "Can't get image !").sendToTarget();
                break;
            }
            ret = m_cLAPI.IsPressFinger(m_hDevice, m_image);
            String msg = "Image Quality: " + GET_IMAGE_QUALITY() + " %";
            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SHOW_TEXT, 0, 0, msg));
            m_fEvent.obtainMessage(MESSAGE_SHOW_IMAGE, LAPI.WIDTH, LAPI.HEIGHT, m_image).sendToTarget();
            if (count == 3 && ret == 0) {
                bContinue = false;
                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SHOW_TOAST, 0, 0, "Put your finger on scanner and press scan again."));
         //       bvsListner.setButtonEnabled(true);
                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_ENABLED_BTN, 0, 0, true));
                break;
            }

            if (ret >= LAPI.DEF_FINGER_SCORE) {
                AppLogger.i("GetImage() = OK");
                if (GET_IMAGE_QUALITY() >= 60 || (count >= 3 && GET_IMAGE_QUALITY() >= 40)) {
                    byte[] rawChannel3 = Arrays.copyOf(m_image, LAPI.WIDTH * LAPI.HEIGHT * 3);
                    int i = m_cLAPI.DrawMinutiaePoint(m_hDevice, rawChannel3);
                    minutiaeCount = i;
                    if (minutiaeCount >= 13 && minutiaeCount <= 255) {
                        AppLogger.i("GetMinutiae() = OK ，minutiae point count = " + minutiaeCount);
                        NFIQuality = GET_NFI_QUALITY();
                        if (NFIQuality >= 1 && NFIQuality <= 3) {
                            bContinue = false;
                            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_MINUTIAE_COUNT, 0, 0, Integer.toString(minutiaeCount)));
                            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_NFI_QUALITIY, 0, 0, Integer.toString(NFIQuality)));
                            ret = m_cLAPI.CreateISOTemplate(m_hDevice, m_image, m_iso_template);
                            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_ISO_TEMPLATE, 0, 0, CREATE_ISO_TEMP(ret)));
                            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_TEMPLATE_TYPE, 0, 0, Constants.FINGER_TEMPLATE_TYPE_ISO));
                            COMPRESS_TO_WSQ2();
                            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_COMPRESS_TO_WSQ, 0, 0, ""));
                            ApplicationData.errorCount++;
                            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_CHANGE_LAYOUT, 0, 0, ""));
                            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SET_ENABLED_BTN, 0, 0, true));
                            m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_SHOW_TEXT, 0, 0, "Fingerprint Scanned!"));
                            break;
                        }
                    }
                } else {
                    AppLogger.i("GetMinutiae() fail!");
                }
            }
        }
        bContinue = false;
    }

    private void ShowFingerBitmap(byte[] image, int width, int height) {
        if (width == 0) return;
        if (height == 0) return;
        for (int i = 0; i < width * height; i++) {
            int v;
            if (image != null) v = image[i] & 0xff;
            else v = 0;
            RGBbits[i] = Color.rgb(v, v, v);
        }
        Bitmap bmp = Bitmap.createBitmap(RGBbits, width, height, Bitmap.Config.RGB_565);
        bvsListner.setBitmap(bmp);
    }

    protected int GET_IMAGE_QUALITY() {
        return m_cLAPI.GetImageQuality(m_hDevice, m_image);
    }

    private final Handler m_fEvent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SHOW_TEXT:
                    bvsListner.setInfoMessage((String) msg.obj);
                    break;
                case MESSAGE_SET_MINUTIAE_COUNT:
                    bvsListner.setMinutiaeCount((String) msg.obj);
                    break;
                case MESSAGE_SET_NFI_QUALITIY:
                    bvsListner.setNFIQuality((String) msg.obj);
                    break;
                case MESSAGE_SET_ISO_TEMPLATE:
                    bvsListner.setHexString((String) msg.obj);
                    break;
                case MESSAGE_SET_TEMPLATE_TYPE:
                    bvsListner.setTemplateType((String) msg.obj);
                    break;
                    case MESSAGE_SET_COMPRESS_TO_WSQ:
                    bvsListner.setWSQ((String) msg.obj);
                    break;
                    case MESSAGE_SET_CHANGE_LAYOUT:
                        bvsListner.changeLayoutAfterFirstScan();
                    break;
                    case MESSAGE_SET_ENABLED_BTN:
                        bvsListner.setButtonEnabled(true);
                    break;
                case MESSAGE_SHOW_IMAGE:
                    ShowFingerBitmap((byte[]) msg.obj, msg.arg1, msg.arg2);
                    break;
                    case MESSAGE_SHOW_TOAST:
                        Toast.makeText(mContext,(String) msg.obj, Toast.LENGTH_SHORT).show();
                        break;
            }
        }
    };

    protected String handleWsqCase(String base64WsqFormat) {

        String resultantWsqMessage = "";
        try {
            String substring = base64WsqFormat.substring(max(base64WsqFormat.length() - 2, 0));

            if (substring.equals("==")) {
                resultantWsqMessage = base64WsqFormat.substring(0, base64WsqFormat.length() - 2);
            }

            Log.d("After Substring", "length: " + resultantWsqMessage.length());
            Log.d("After Substring", ": " + resultantWsqMessage);


            resultantWsqMessage = resultantWsqMessage.replace(Pattern.quote("[A\\s]+'\'$"), "");
            resultantWsqMessage += "AA==";

            Log.d("After Regex", "length: " + resultantWsqMessage.length());
            Log.d("After Regex", ": " + resultantWsqMessage);

        } catch (Exception ex){
            Log.e("handleWsqCase2()", " Exception : ", ex);
        }

        return resultantWsqMessage;

    }

}
