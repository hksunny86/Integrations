package com.inov8.agentmate.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.AppValidator;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.BuildConfig;
import com.inov8.jsbl.sco.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends Activity {
	private Thread mSplashThread;
	private static final int REQUEST_CAMERA = 111;
    private String message;
    private AppValidator validator;
    private boolean isAlert = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		screenDesity();
        updateAndroidSecurityProvider(SplashActivity.this);

//        getHashKey();

		// The thread to wait for splash screen events
		mSplashThread = new Thread() {
			@Override
			public void run() {
				ApplicationData.isLogin = false;
				try {
					synchronized (this) {
						// Wait given period of time or exit on touch
						wait(4000);

                        validator = new AppValidator(SplashActivity.this);
                        message = validator.validateApp();

                        if (message != null) {
                            isAlert = true;
                        }

                        if(Build.VERSION.SDK_INT >=19)  // kitkat check
                        {
                            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))  // camera check
                            {
                                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) // camera autofocus check
                                {
                                    if (android.os.Build.VERSION.SDK_INT >= 23)  // camera permission for marshmallow
                                    {
                                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                            return;
                                        }
                                        else
                                            ApplicationData.isBvsEnabledDevice = true;
                                    }
                                    else
                                        ApplicationData.isBvsEnabledDevice = true;
                                }
                                else{
                                    ApplicationData.isBvsEnabledDevice = true;
//                                    ApplicationData.bvsErrorMessage = Constants.Messages.CAMERA_AUTO_FOCUS_ERROR;
                                }
                            }
                            else{
                                ApplicationData.isBvsEnabledDevice = false;
                                ApplicationData.bvsErrorMessage = Constants.Messages.CAMERA_NOT_AVAILABLE_ERROR;
                            }
                        }
                        else
                        {
                            ApplicationData.isBvsEnabledDevice = false;
                            ApplicationData.bvsErrorMessage = Constants.Messages.OS_ERROR;
                        }
					}
                    AppLogger.i("BVS Enabled: " +ApplicationData.isBvsEnabledDevice+"");

					processNext();
				}
				catch (InterruptedException ex) {
				}			
			}
		};
		mSplashThread.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void screenDesity() {
		int density = getResources().getDisplayMetrics().densityDpi;

		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			break;
		}
	}

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ApplicationData.isBvsEnabledDevice = true;
                    processNext();
                }
                else
                {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        showMessageOKCancel("You need to allow access to Camera to take Photos for Account Opening.",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(SplashActivity.this, "Please allow camera permissions from app settings to proceed further.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle(AppMessages.ALERT_HEADING)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }
	
	private void processNext() {
        if(!isAlert) {
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            runOnUiThread(new Runnable() {
                public void run() {
                    Utility.createAlertDialog(
                            message,
                            AppMessages.ALERT_HEADING,
                            SplashActivity.this,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {
                                    finish();
                                }
                            });
                }
            });
        }
	}

    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                AppLogger.i("Hash KEY: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("package name", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("package name", e.toString());
        }
    }


////
    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
//            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
//            Log.e("SecurityException", "Google Play Services not available.");
        }
    }
}