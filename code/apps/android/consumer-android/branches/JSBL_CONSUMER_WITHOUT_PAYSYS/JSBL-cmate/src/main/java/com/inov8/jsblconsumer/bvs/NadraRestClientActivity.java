//package com.inov8.jsblconsumer.bvs;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.os.Environment;
//import androidx.appcompat.widget.AppCompatImageView;
//import android.view.View;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//
//import com.inov8.jsblconsumer.R;
//import com.inov8.jsblconsumer.util.AppLogger;
//import com.inov8.jsblconsumer.util.AppMessages;
//import com.inov8.jsblconsumer.util.ApplicationData;
//import com.inov8.jsblconsumer.util.Constants;
//import com.paysyslabs.instascan.Fingers;
//import com.paysyslabs.instascan.NadraActivity;
//import com.paysyslabs.instascan.NadraScanListener;
//import com.paysyslabs.instascan.model.PersonData;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.inov8.jsblconsumer.util.Constants.RESULT_CODE_FAILED_NADRA_FINGER_INDEXES;
//import static com.inov8.jsblconsumer.util.Constants.RESULT_CODE_FAILED_NADRA_OTHER_ERROR;
//import static com.inov8.jsblconsumer.util.Constants.RESULT_CODE_SUCCESS_NADRA;
//
//
//public class NadraRestClientActivity extends NadraActivity implements NadraScanListener {
//
//    private Activity mActivity;
//    private ProgressDialog progressDialog;
//    private AppCompatImageView btnBack, btnHome, btnSignout;
//    private TextView headerText;
//
////    @Override
////    public boolean shouldSegment() {
////        return false;
////    }
////
////    @Override
////    public boolean shouldRetryOnBadCapture() {
////        return false;
////    }
//
//    private TextView tvHint;
//
////    @Override
////    public int getScanFragmentContainer() {
////        return R.id.frag_container;
////    }
//
////    @Override
////    public String getLicenseKey() {
////        return null; // license key will not be used if useCustomProxy = true
////    }
//
//    @Override
//    public String getCustomProxyURL() {
//        return Constants.CUSTOM_PROXY_VERIFY;
//    }
//
//    @Override
//    public String getCustomCookie() {
//        return "JSESSIONID=129283293962936293";  // random value
//    }
//
//    @Override
//    public String getLicenseKey() {
//        return null;
//    }
//
//    @Override
//    public boolean useCustomProxy() {
//        return true;
//    }
//
//    @Override
//    public void onBadCapture() {
//        getIntent().putExtra("code", "700");
//        getIntent().putExtra("msg", "Place your finger correctly and try again.");
//        setResult(RESULT_CODE_FAILED_NADRA_OTHER_ERROR, getIntent());
//        finish();
//    }
//
//    //    @Override
////    public boolean shouldInvert() {
////        return  false;
////    }
////
//    @Override
//    public double getCropFactor() {
//        return 0.6;
//    }
//
//    @Override
//    public int getNfiqThreshold() {
//        return 4;
//
//    }
//
//    @Override
//    public float getOverlapThreshold() {
//        return 0.75f;
//    }
//
//    @Override
//    public double getLivenessValue(){
//        return 0.7;
//    }
//
////    @Override
////    public boolean getLivenessConfidence() {
////        return true;
////    }
//
//    @Override
//    public boolean getForcedTracing() {
//        return true;
//    }
//
//    //
//    @Override
//    public boolean shouldUseLegacyCaptureFrame() {
//        return true;
//    }
////
//
//    @Override
//    public String getDebugStorageBasePath() {
//        return Environment.getExternalStorageDirectory().getPath();
//    }
//
//    @Override
//    public Map<String, String> getCustomAuthenticationData() {
//        HashMap<String, String> authorizationData = new HashMap<>();
//        authorizationData.put("session", "2131100000034258477");  // random value
//        return authorizationData;
//    }
//
//    @Override
//    public void onConfigurationStarted() {
//
//    }
//
//    @Override
//    public void onConfigurationCompleted() {
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan);
//
//        mActivity = this;
//        headerImplementation();
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait..");
//        progressDialog.setCancelable(false);
//
//        tvHint = (TextView) findViewById(R.id.tvHint);
//
//        Bundle extras = getIntent().getExtras();
//
//        String cnic = null;
//        Fingers finger = null;
//
//        if (extras != null) {
//            cnic = extras.getString(Constants.IntentKeys.IDENTIFIER);
//            finger = (Fingers) extras.get(Constants.IntentKeys.FINGER);
//        }
//
//        tvHint.setText("Please scan your " + finger.toString() + ".");
//
//        initializeNadraActivity(this, cnic, finger);
//    }
//
//    @Override
//    public void onRequestStarted() {
//        progressDialog.show();
//    }
//
//    @Override
//    public void onSuccessfulScan(String s, PersonData personData) {
//        String json = new Gson().toJson(personData);
//        AppLogger.i("nadra response: " + json);
//        getIntent().putExtra(Constants.ATTR_PERSON_DATA, json);
//        setResult(RESULT_CODE_SUCCESS_NADRA, getIntent());
//        finish();
//    }
//
//    @Override
//    public void onResponseReceived() {
//        progressDialog.dismiss();
//    }
//
////    @Override
////    public void onSuccessfulScan(PersonData personData) {
////        String json = new Gson().toJson(personData);
////        AppLogger.i("nadra response: " + json);
////        getIntent().putExtra(Constants.ATTR_PERSON_DATA, json);
////        setResult(RESULT_CODE_SUCCESS_NADRA, getIntent());
////        finish();
////    }
//
//    @Override
//    public void onInvalidFingerIndex(String code, String message, List<Fingers> validFingers) {
//
//        String validIndexes = "";
//        ArrayList<String> fingers = new ArrayList<String>();
//        ArrayList<Integer> fingerIndexes = new ArrayList<Integer>();
//
//        for (Fingers finger : validFingers) {
//            validIndexes = validIndexes + finger + "\n";
//            fingers.add(finger.getValue());
//            fingerIndexes.add(getRespectiveFinger(finger.getValue()));
//        }
//
//        AppLogger.i("on Invalid Finger Index, Error code: " + code + " \n Fingers: " + validIndexes);
//
//        getIntent().putExtra(Constants.IntentKeys.TITLE, message);
//        getIntent().putExtra("code", code);
//        getIntent().putExtra(Constants.IntentKeys.VALID_INDEXES, validIndexes);
//        getIntent().putStringArrayListExtra(Constants.IntentKeys.FINGERS, fingers);
//        getIntent().putExtra(Constants.IntentKeys.VALID_FINGER_INDEXES, fingerIndexes);
//
//        setResult(RESULT_CODE_FAILED_NADRA_FINGER_INDEXES, getIntent());
//        finish();
//    }
//
//    @Override
//    public void onError(String code, String message) {
//        AppLogger.i(code + ":" + message);
//        if (code.equals("500")) {
//            getIntent().putExtra("code", code);
//            getIntent().putExtra("msg", AppMessages.EXCEPTION);
//        } else {
//            getIntent().putExtra("code", code);
//            getIntent().putExtra("msg", message);
//        }
//        setResult(RESULT_CODE_FAILED_NADRA_OTHER_ERROR, getIntent());
//        finish();
//    }
//
//    private Integer getRespectiveFinger(String finger) {
//
//        if (finger.equals("RIGHT_THUMB")) return 1;
//        if (finger.equals("RIGHT_INDEX")) return 2;
//        if (finger.equals("RIGHT_MIDDLE")) return 3;
//        if (finger.equals("RIGHT_RING")) return 4;
//        if (finger.equals("RIGHT_LITTLE")) return 5;
//
//        if (finger.equals("LEFT_THUMB")) return 6;
//        if (finger.equals("LEFT_INDEX")) return 7;
//        if (finger.equals("LEFT_MIDDLE")) return 8;
//        if (finger.equals("LEFT_RING")) return 9;
//        if (finger.equals("LEFT_LITTLE")) return 10;
//
//        return 1;
//    }
//
//    public void headerImplementation() {
//
//        findViewById(R.id.ivHeaderLogo).setVisibility(View.GONE);
//        headerText = (TextView) findViewById(R.id.headerText);
//        headerText.setVisibility(View.VISIBLE);
//        headerText.setText(getString(R.string.open_account));
//
//        btnBack = (AppCompatImageView) findViewById(R.id.btnBack);
//        btnHome = (AppCompatImageView) findViewById(R.id.btnHome);
//        btnSignout = (AppCompatImageView) findViewById(R.id.btnSignout);
//
//        btnHome.setVisibility(View.GONE);
//        btnSignout.setVisibility(View.GONE);
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ApplicationData.isWebViewOpen = false;
//                ApplicationData.webUrl = null;
//                finish();
//            }
//        });
//    }
//}
