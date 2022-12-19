//package com.inov8.agentmate.bvs.suprema;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.biomini.BioMiniAndroid;
//import com.android.biomini.IBioMiniCallback;
//import com.inov8.agentmate.bvs.BvsController;
//import com.inov8.agentmate.util.AppLogger;
//import com.inov8.agentmate.util.Constants;
//import com.inov8.agentmate.util.Utility;
//import com.inov8.jsbl.sco.R;
//
//import java.nio.ByteBuffer;
//
//public class SupremaHandler extends BvsController {
//
//    // BioMini SDK variable
//    private static BioMiniAndroid mBioMiniHandle;
//
//    private int ufa_res;
//    private String errmsg = "Hi";
//    private byte[] pImage = new byte[320 * 480];
//    private boolean sdkInitialized = false;
//
//    // Input Template Buffer
//    private byte[] ptemplate2 = new byte[1024];
//    // Input Template Size
//    private int[] ntemplateSize2 = new int[4];
//    // Quality of template
//    private int[] nquality = new int[4];
//
//    private int nenrolled = 0;
//
//    private boolean isname = false;
//
//    private int nsensitivity;
//    private int ntimeout;
//    private int nsecuritylevel;
//    private int bfastmode;
//
//    public SupremaHandler(Activity activity) {
//        this.activity = activity;
//        this.bvsListner = (BvsCompleteListner) activity;
//    }
//
//    @Override
//    protected void onCreate() {
//
//        AppLogger.i("Called onCreate()");
//
//        // allocate SDK instance
//        if (mBioMiniHandle == null) {
//            mBioMiniHandle = new com.android.biomini.BioMiniAndroid(activity);
//        }
//        // find BioMini device and request permission
//        ufa_res = mBioMiniHandle.UFA_FindDevice();
//
//        if (ufa_res == -205) {
//            bvsListner.setInfoMessage("Device not Found");
//
//            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);
//            dlgAlert.setMessage("Please connect Suprema device and continue!");
//            dlgAlert.setTitle("Device not Found");
//            dlgAlert.setPositiveButton("OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            activity.finish();
//                            return;
//                        }
//                    }
//
//            );
//            dlgAlert.setCancelable(false);
//            dlgAlert.create().show();
//        }
//
//        errmsg = mBioMiniHandle.UFA_GetErrorString(ufa_res);
//    }
//
//    @Override
//    protected void onStart() {
//        AppLogger.i("Called onStart()");
//    }
//
//    @Override
//    protected void onResume() {
//
//        AppLogger.i("Called onResume()");
//    }
//
//    @Override
//    protected void scanImage() {
//
//        if (mBioMiniHandle == null) {
//            AppLogger.i("Error: " + String.valueOf("BioMini SDK Handler with NULL!"));
//        } else {
//            if (!sdkInitialized) {
//                if (ufa_res == 0) {
//                    // SDK initialization
//                    ufa_res = mBioMiniHandle.UFA_Init();
//                    String errmsg1 = mBioMiniHandle.UFA_GetErrorString(ufa_res);
//
//                    if (ufa_res == 0) {
//                        nsensitivity = 7;
//                        ntimeout = 10;
//                        nsecuritylevel = 4;
//                        bfastmode = 1;
//
//                        mBioMiniHandle.UFA_SetParameter(mBioMiniHandle.UFA_PARAM_SENSITIVITY, nsensitivity);
//                        mBioMiniHandle.UFA_SetParameter(mBioMiniHandle.UFA_PARAM_TIMEOUT, ntimeout * 1000);
//                        mBioMiniHandle.UFA_SetParameter(mBioMiniHandle.UFA_PARAM_SECURITY_LEVEL, nsecuritylevel);
//                        mBioMiniHandle.UFA_SetParameter(mBioMiniHandle.UFA_PARAM_FAST_MODE, bfastmode);
//
//                        // set callback
//                        mBioMiniHandle.UFA_SetCallback(mBioMiniCallbackHandler);
//                    }
//                    if (ufa_res == 0) {
//                        sdkInitialized = true;
//                    }
//                    //bvsListner.setInfoMessage("UFA_Init res: " + errmsg1 + "Code: " + ufa_res);
//                    ufa_res = mBioMiniHandle.UFA_SetTemplateType(mBioMiniHandle.UFA_TEMPLATE_TYPE_ISO19794_2);
//                    errmsg = mBioMiniHandle.UFA_GetErrorString(ufa_res);
//                }
//
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getImage();
//                    }
//                }, 300);
//            } else {
//
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getImage();
//                    }
//                }, 100);
//            }
//        }
//    }
//
//    private void getImage() {
//        if (mBioMiniHandle == null) {
//            Log.e(">==< Main Activity >==<", String.valueOf("BioMini SDK Handler with NULL!"));
//        } else {
//            if (nenrolled == 50) {
//                bvsListner.setInfoMessage("out of memory");
//                return;
//            }
//
//            // capture fingerprint image
//            ufa_res = mBioMiniHandle.UFA_CaptureSingle(pImage);
//
//            if (ufa_res != 0) {
//                errmsg = mBioMiniHandle.UFA_GetErrorString(ufa_res);
//                bvsListner.setInfoMessage("UFA_CaptureSingle res: " + errmsg);
//                return;
//            }
//
//            mBioMiniCallbackHandler.onCaptureCallback(pImage, 320, 480, 500, true);
//
//            // extract fingerprint template from captured image
//
//            ufa_res = mBioMiniHandle.UFA_ExtractTemplate(ptemplate2, ntemplateSize2, nquality, 1024);
//
//            if (ufa_res != 0) {
//                errmsg = mBioMiniHandle.UFA_GetErrorString(ufa_res);
//                bvsListner.setInfoMessage("UFA_ExtractTemplate res: " + errmsg);
//                return;
//            } else {
//                errmsg = mBioMiniHandle.UFA_GetErrorString(ufa_res);
//                bvsListner.setInfoMessage("UFA_ExtractTemplate res: " + errmsg);
//
//                byte hexi[] = Base64.encode(ptemplate2, Base64.NO_WRAP);
//                String hexString = new String(hexi);
//                bvsListner.setTemplateType(Constants.FINGER_TEMPLATE_TYPE_ISO);
//                bvsListner.setHexString(hexString);
//                bvsListner.setInfoMessage("Fingerprint Scanned!");
//                bvsListner.changeLayoutAfterFirstScan();
//
//                nenrolled++;
//            }
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        AppLogger.i("Called onPause()");
//
//        if (mBioMiniHandle == null) {
//            Log.e(">==< Main Activity >==<", String.valueOf("BioMini SDK Handler with NULL!"));
//        } else {
//            mBioMiniHandle.UFA_AbortCapturing();
//            mBioMiniHandle.UFA_Uninit();
//            nsensitivity = 0;
//            ntimeout = 0;
//            nsecuritylevel = 0;
//            bfastmode = 0;
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        AppLogger.i("Called onStop()");
//    }
//
//    @Override
//    protected void onDestroy() {
//        AppLogger.i("Called onDestroy()");
//    }
//
//    @Override
//    protected void onBackPressed() {
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    }
//
//    // Callback
//    private final IBioMiniCallback mBioMiniCallbackHandler = new IBioMiniCallback() {
//        @Override
//        public void onCaptureCallback(final byte[] capturedimage, int width, int height, int resolution, boolean bfingeron) {
//            Log.e(">==< Main Activity >==<", String.valueOf("onCaptureCallback called!" + " width:" + width + " height:" + height + " fingerOn:" + bfingeron));
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    byte[] Bits = new byte[320 * 480 * 4];
//                    for (int i = 0; i < 320 * 480; i++) {
//                        Bits[i * 4] =
//                                Bits[i * 4 + 1] =
//                                        Bits[i * 4 + 2] = capturedimage[i];
//                        Bits[i * 4 + 3] = -1;
//                    }
//                    Bitmap bm = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
//                    bm.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
//
//                    bvsListner.setBitmap(bm);
//
//                }
//            });
//        }
//
//        @Override
//        public void onErrorOccurred(String msg) {
//
//        }
//    };
//}
