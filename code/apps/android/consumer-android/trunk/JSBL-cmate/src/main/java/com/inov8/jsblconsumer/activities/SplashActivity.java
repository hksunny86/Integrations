package com.inov8.jsblconsumer.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.guardsquare.dexguard.runtime.detection.CertificateChecker;
//import com.guardsquare.dexguard.runtime.detection.FileChecker;
//import com.guardsquare.dexguard.runtime.detection.TamperDetector;
import com.inov8.jsblconsumer.BuildConfig;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.AppValidator;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.BuildConstants;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.DecodingUtility;
import com.inov8.jsblconsumer.util.PreferenceConnector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity implements View.OnClickListener {
    private Thread mSplashThread;
    private boolean isCompatible = true;
    private String message;
    private boolean isAlert = false;
    private AppValidator validator;
    private Button btnContinue, btnLogin;
    private ViewPager mViewPager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private int screenPosition = 0;
    private final int REQUEST_CODE_PERMISSIONS = 100;
    int firstTime;
//    private String[] permissions = new String[]{Manifest.permission.RECEIVE_SMS};

    static {
        System.loadLibrary("keys");
    }

    public native String getScanner1();

    public native String getScanner2();

    public native String getScanner3();

    public native String getScanner4();

    public native String getScanner5();

    public native String getScanner6();

    public native String getScanner7();

    public native String  getScanner8();

    public native String getScanner9();

    public native String getScanner10();

    public native String getScanner11();

    public native String getScanner12();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        new Delegate().checkAndInitialize();
        updateAndroidSecurityProvider(SplashActivity.this);

        String[] a = new String[4];
        a[0] = getScanner1();
        a[1] = getScanner2();
        a[2] = getScanner3();
        a[3] = getScanner4();
        Constants.SECRET_KEY = new DecodingUtility().decode(a);
        ApplicationData.fee=null;

//        String[] b = new String[4];
//        b[0] = getScanner5();
//        b[1] = getScanner6();
//        b[2] = getScanner7();
//        b[3] = getScanner8();
//        Constants.SECURITY_KEY = new DecodingUtility().decode(b);

        String[] c = new String[4];
        c[0] = getScanner9();
        c[1] = getScanner10();
        c[2] = getScanner11();
        c[3] = getScanner12();
        Constants.HANDSHAKE_KEY = new DecodingUtility().decode(c);

//        checkPermissions();

        if (!BuildConstants.isDebuggingMode) {
//            rootCheck();
        }

        btnContinue = (Button) findViewById(R.id.continuue);
        btnLogin = (Button) findViewById(R.id.login);

        firstTime = PreferenceConnector.readInteger(SplashActivity.this, PreferenceConnector.FIRST_TIME, 0);

        if (firstTime == 1) {
            btnContinue.setText("OPEN ACCOUNT");
            btnLogin.setVisibility(View.VISIBLE);
        }

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                VideoFragment.videoView.stopPlayback();
                screenPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                screenPosition = position;

                if (firstTime == 0) {
                    if (screenPosition == 3) {
                        btnContinue.setText("OPEN ACCOUNT");
                        btnLogin.setVisibility(View.VISIBLE);
                        PreferenceConnector.writeInteger(
                                SplashActivity.this, PreferenceConnector.FIRST_TIME, 1);
                    } else {
                        btnContinue.setText("CONTINUE");
                        btnLogin.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        btnLogin.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continuue:
                processContinue();
                break;
            case R.id.login:
                processLogin();
                break;
        }
    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {
        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                screenPosition = 0;
                return new VideoFragment();
            } else if (position == 1) {
                screenPosition = 1;
                return new ContinueFragment1();
            } else if (position == 2) {
                screenPosition = 2;
                return new ContinueFragment2();
            } else if (position == 3) {
                screenPosition = 3;
                return new ContinueFragment3();
            }
//            else if (position == 4) {
//                screenPosition = 4;
//                return new ContinueFragment4();
//            } else if (position == 5) {
//                screenPosition = 5;
//                return new ContinueFragment5();
//            } else if (position == 6) {
//                screenPosition = 6;
//
//                return new ContinueFragment6();
//            }
            return null;
        }

        @Override
        public int getCount() {
            if (firstTime == 0)
                return 4;
            else
                return 1;
        }
    }

    private void processLogin() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void processContinue() {

        if (firstTime == 1) {
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, OpenAccountBvsActivity.class);
            startActivity(intent);
        } else {
            if (screenPosition != 3) {
                mViewPager.setCurrentItem(screenPosition + 1);
            } else {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, OpenAccountBvsActivity.class);
                startActivity(intent);
            }
        }
    }

    private void getHashKey() {

        try {

            PackageInfo info = getPackageManager().getPackageInfo(
                    BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                // Log.d("KeyHash:",
                // Base64.encodeToString(md.digest(), Base64.DEFAULT));
                AppLogger.i("Hash KEY: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("package name", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("package name", e.toString());
        }
    }

    private void rootCheck() {

        validator = new AppValidator(SplashActivity.this);
        message = validator.validateApp();

        if (message != null) {
            isAlert = true;
        }

//        else if (isCompatible) {
//            isAlert = false;
//        }

//        else {
//            message = AppMessages.DEVICE_NOT_SUPPORTED;
//            isAlert = true;
//        }

        if (isAlert) {

            popupDialogs.createAlertDialog(message, AppMessages.ALERT_HEADING,
                    SplashActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);
        }

    }

//    private void checkPermissions() {
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return;
//        }
//
//        int result;
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String p : permissions) {
//            result = ContextCompat.checkSelfPermission(this, p);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(p);
//            }
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSIONS);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

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


    /**
     * This utility class performs the tamper detection and creates the view.
     * <p>
     * We're putting this functionality in a separate class so we can encrypt
     * it, as an extra layer of protection around the tamper detection and
     * some essential code. We can't encrypt the activity itself, for
     * technical reasons, but an inner class or another class are fine.
     */
    private class Delegate {
        public void checkAndInitialize() {
            // We need a context for most methods.
            final Context context = SplashActivity.this;

            // You can pick your own value or values for OK,
            // to make the code less predictable.
            final int OK = 1;

            // Let the DexGuard runtime library detect whether the apk has
            // been modified or repackaged in any way (with jar, zip,
            // jarsigner, zipalign, or any other tool), after DexGuard has
            // packaged it. The return value is the value of the optional
            // integer argument OK (default=0) if the apk is unchanged.
//            int apkChanged =
//                    TamperDetector.checkApk(SplashActivity.this, OK);

            // Let the DexGuard runtime library detect whether the apk has
            // been re-signed with a different certificate, after DexGuard has
            // packaged it.  The return value is the value of the optional
            // integer argument OK (default=0) if the certificate is still
            // the same.
//            int certificateChanged =
//                    CertificateChecker.checkCertificate(SplashActivity.this, OK);

            // You can also explicitly pass the SHA-256 hash of a certificate,
            // if the application is only signed after DexGuard has packaged
            // it.
            // You can print out the SHA-256 hash of the certificate of your
            // key store with
            //   keytool -list -v -keystore my.keystore
            //
            // If you want to extract the SHA-256 hash from an earlier version
            // of the application, you can print it out with
            //   keytool -printcert -v -jarfile my.apk
            //
            // If you are publishing on the Amazon Store, you can find the
            // SHA-256 hash in
            //   Amazon Apps & Games Developer Portal
            //     > Binary File(s) Tab > Settings > My Account.
            //
            // With your SHA-256 hash, you can then use one of
            // CertificateChecker.checkCertificate(context,
            //   "EB:B0:FE:DF:19:42:A0:99:B2:87:C3:DB:00:FF:73:21:" +
            //   "62:15:24:81:AB:B2:B6:C7:CB:CD:B2:BA:58:94:A7:68", OK);
            // CertificateChecker.checkCertificate(context,
            //   "EBB0FEDF1942A099B287C3DB00FF7321" +
            //   "62152481ABB2B6C7CBCDB2BA5894A768", OK);
            // CertificateChecker.checkCertificate(context,
            //   0xEBB0FEDF, 0x1942A099, 0xB287C3DB, 0x00FF7321,
            //   0x62152481, 0xABB2B6C7, 0xCBCDB2BA, 0x5894A768, OK);
            // CertificateChecker.checkCertificate(context,
            //   0xEBB0FEDF1942A099L, 0xB287C3DB00FF7321L,
            //   0x62152481ABB2B6C7L, 0xCBCDB2BA5894A768L, OK);
            //
            // If you specify a string, you should make sure it is encrypted,
            // for good measure.

            // Let the DexGuard runtime library determine whether the primary
            // DEX has been modified, after DexGuard has packaged the APK. The
            // return value is the value of the optional integer argument OK
            // (default=0) if the file is still the same.
            //
            // You can check any file, except for files in the META-INF
            // directory. When creating app bundles, you also can't check the
            // resources.arsc, AndroidManifest.xml, and other xml files, since
            // bundletool will still manipluate them when creating the final
            // APK.
            //
//            FileChecker fileChecker = new FileChecker(SplashActivity.this);
//
//            int primaryDexChanged =
//                    fileChecker.checkFile("classes.dex", OK);
//
//            // we can also simply check all files.
//            int anyFileChanged =
//                    fileChecker.checkAllFiles(OK);

            // Display a message.
//            TextView view = new TextView(SplashActivity.this);
//            view.setText(apkChanged == OK &&
//                    certificateChanged == OK &&
//                    primaryDexChanged == OK &&
//                    anyFileChanged == OK ?
//                    "Hello, world!" :
//                    "Tamper alert!");
//            view.setGravity(Gravity.CENTER);

            // Change the background color if someone has tampered with the
            // application archive.
//            if (apkChanged != OK ||
//                    anyFileChanged != OK ||
//                    primaryDexChanged != OK) {
//                message = AppMessages.INVALID_ENVIRONMENT;
//            }
//
//            setContentView(view);

            // Briefly display a comment.
//            String comment =
//                    primaryDexChanged != OK ? "The primary DEX has been modified" :
//                            anyFileChanged != OK ? "Some file in the APK has been modified" :
//                                    certificateChanged != OK ? "The certificate is not the expected certificate" :
//                                            apkChanged != OK ? "The application archive has been modified" :
//                                                    "The application has not been modified";

//            Toast.makeText(SplashActivity.this, comment, Toast.LENGTH_LONG).show();
        }
    }
}