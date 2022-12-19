package com.inov8.agentmate.bvs.paysys;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsbl.sco.BuildConstants;
import com.inov8.jsbl.sco.R;
import com.paysyslabs.instascan.AreaName;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.OTCActivity;
import com.paysyslabs.instascan.OTCScanListener;
import com.paysyslabs.instascan.RemittanceType;
import com.paysyslabs.instascan.model.OTCResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_OTC;

public class OTCClient extends OTCActivity implements OTCScanListener {

    private ProgressDialog progressDialog;
    private TextView tvHint;

//    @Override
//    public int getScanFragmentContainer() {
//        return R.id.frag_container;
//    }

    @Override
    public String getLicenseKey() {
        return null; // // license key will not be used if useCustomProxy = true
    }

    @Override
    public String getCustomProxyURL() {
        return Constants.PAYSYS_OTC_URL;
    }

    @Override
    public String getCustomCookie() {
        return "JSESSIONID="+ApplicationData.sessionId;     }

    @Override
    public boolean useCustomProxy() {
        return true;
    }

    @Override
    public Map<String, String> getCustomAuthenticationData() {
        HashMap<String, String> authorizationData = new HashMap<>();
        authorizationData.put("session", ApplicationData.sessionId);
        return authorizationData;
    }

    @Override
    public void onBadCapture(){
        getIntent().putExtra("code", "700");
        getIntent().putExtra("msg", "Place your finger correctly and try again.");
        setResult(RESULT_CODE_FAILED_OTC_OTHER_ERROR, getIntent());
        finish();
    }

//    @Override
//    public int getNfiqThreshold() {
//        return 3;
//    }
//
//    @Override
//    public float getOverlapThreshold() {
//        return 0.90f;
//    }

//    @Override
//    public boolean shouldSegment() { return false; }
//

    @Override
    public boolean getForcedTracing() {
        return BuildConstants.isDebuggingMode;
    }

    @Override
    public boolean shouldUseLegacyCaptureFrame() {
        return false;
    }
//
//    @Override
//    public boolean shouldRetryOnBadCapture() { return true; }

    @Override
    public String getDebugStorageBasePath(){
        return Environment.getExternalStorageDirectory().getPath();
    }

    @Override
    public void onConfigurationStarted() {

    }

    @Override
    public void onConfigurationCompleted() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        tvHint = (TextView) findViewById(R.id.tvHint);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        String identifier = getIntent().getStringExtra(Constants.IntentKeys.IDENTIFIER);
        String remittanceAmount = getIntent().getStringExtra(Constants.IntentKeys.REMITTANCE_AMOUNT);
        RemittanceType remittanceType = (RemittanceType) getIntent().getSerializableExtra(Constants.IntentKeys.REMITTANCE_TYPE);
        String contactNumber = getIntent().getStringExtra(Constants.IntentKeys.CONTACT_NUMBER);
        AreaName areaName = (AreaName) getIntent().getSerializableExtra(Constants.IntentKeys.AREA_NAME);
        String secondaryIdentifier = getIntent().getStringExtra(Constants.IntentKeys.SECONDARY_IDENTIFIER);
        String accountNumber = getIntent().getStringExtra(Constants.IntentKeys.ACCOUNT_NUMBER);
        String secondaryContactNumber = getIntent().getStringExtra(Constants.IntentKeys.SECONDARY_CONTACT_NUMBER);
        Fingers finger = (Fingers) getIntent().getSerializableExtra(Constants.IntentKeys.FINGER);

        tvHint.setText("Please scan your "+Fingers.valueOf(finger.getValue())+".");

        initializeOTCActivity(this, identifier, remittanceAmount, remittanceType,
                contactNumber, areaName, secondaryIdentifier, accountNumber, secondaryContactNumber, finger);
    }

    @Override
    public void onOTCRequestStarted() {
        progressDialog.show();
    }

    @Override
    public void onOTCSuccessfulScan(OTCResult otcResult) {
        String data = new Gson().toJson(otcResult);
        getIntent().putExtra("tags", data);
        AppLogger.i("tags"+ data);
        setResult(RESULT_CODE_SUCCESS_OTC, getIntent());
        finish();
    }

    @Override
    public void onOTCInvalidFingerIndex(String code,
                                        String message, List<Fingers> list) {
        String validIndexes = "";
        ArrayList<String> fingers=new ArrayList<String>();
        ArrayList<Integer> fingerIndexes =new ArrayList<Integer>();

        for (Fingers finger:list) {
            validIndexes = validIndexes + finger + "\n";
            fingers.add(finger.getValue());
            fingerIndexes.add(getRespectiveFinger(finger.getValue()));
        }

        AppLogger.i("on Invalid Finger Index, Error code: "+code+ "Fingers: "+validIndexes);

        getIntent().putExtra("title", message);
        getIntent().putExtra("code", code);
        getIntent().putExtra("validIndexes", validIndexes);
        getIntent().putStringArrayListExtra("fingers",fingers);
        setResult(RESULT_CODE_FAILED_OTC_FINGER_INDEXES, getIntent());
        finish();
    }

    @Override
    public void onOTCError(String code, String error) {
        AppLogger.i(code + ":" + error);
        getIntent().putExtra("code", code);
        getIntent().putExtra("msg", error);
        setResult(RESULT_CODE_FAILED_OTC_OTHER_ERROR, getIntent());
        finish();
    }

    @Override
    public void onOTCResponseReceived() {
        progressDialog.dismiss();
    }

    private Integer getRespectiveFinger(String finger) {

        if(finger.equals("RIGHT_THUMB")) return 1;
        if(finger.equals("RIGHT_INDEX")) return 2;
        if(finger.equals("RIGHT_MIDDLE")) return 3;
        if(finger.equals("RIGHT_RING")) return 4;
        if(finger.equals("RIGHT_LITTLE")) return 5;
        if(finger.equals("LEFT_THUMB")) return 6;
        if(finger.equals("LEFT_INDEX")) return 7;
        if(finger.equals("LEFT_MIDDLE")) return 8;
        if(finger.equals("LEFT_RING")) return 9;
        if(finger.equals("LEFT_LITTLE")) return 10;

        return 1;
    }
}

