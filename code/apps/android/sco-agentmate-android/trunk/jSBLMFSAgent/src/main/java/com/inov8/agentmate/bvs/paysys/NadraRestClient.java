package com.inov8.agentmate.bvs.paysys;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsbl.sco.BuildConstants;
import com.inov8.jsbl.sco.R;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.NadraActivity;
import com.paysyslabs.instascan.NadraScanListener;
import com.paysyslabs.instascan.model.PersonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_NADRA_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_NADRA_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_NADRA;

public class NadraRestClient extends NadraActivity implements NadraScanListener {

    private ProgressDialog progressDialog;
    private TextView tvHint;

    @Override
    public String getCustomProxyURL() {
        return Constants.PAYSYS_VERIFY_URL;
    }

    @Override
    public String getCustomCookie() {
        return "JSESSIONID="+ ApplicationData.sessionId;
    }

    @Override
    public String getLicenseKey() {
        return null;
    }

    @Override
    public boolean useCustomProxy() { return true; }

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
        setResult(RESULT_CODE_FAILED_NADRA_OTHER_ERROR, getIntent());
        finish();
    }

    @Override
    public void onConfigurationStarted() {

    }

    @Override
    public void onConfigurationCompleted() {

    }

    @Override
    public boolean getForcedTracing() {
        return BuildConstants.isDebuggingMode;
    }

    @Override
    public boolean shouldUseLegacyCaptureFrame() {
        return true;
    }

    @Override
    public double getCropFactor() {
        return 0.6;
    }

    @Override
    public int getNfiqThreshold() {
        return 4;
    }

    @Override
    public float getOverlapThreshold() {
        return 0.90f;
    }

    @Override
    public double getLivenessValue(){
        return 0.7;
    }

//    @Override
//    public boolean shouldSegment() { return false; }
//
    //  @Override
    // public boolean shouldRetryOnBadCapture() { return true; }

    @Override
    public String getDebugStorageBasePath(){
        return Environment.getExternalStorageDirectory().getPath();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        tvHint = (TextView) findViewById(R.id.tvHint);

        Bundle extras = getIntent().getExtras();

        String cnic = null;
        Fingers finger = null;

        if (extras != null) {
            cnic = extras.getString(Constants.IntentKeys.IDENTIFIER);
            finger = (Fingers) extras.get(Constants.IntentKeys.FINGER);
        }

        tvHint.setText("Please scan your "+Fingers.valueOf(finger.getValue())+".");

        initializeNadraActivity(this, cnic, finger);
    }

    @Override
    public void onRequestStarted() {
        progressDialog.show();
    }

    @Override
    public void onResponseReceived() {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccessfulScan(String s, PersonData personData) {
        String json = new Gson().toJson(personData);
        AppLogger.i("nadra response: " + json);
        getIntent().putExtra(XmlConstants.ATTR_PERSON_DATA, json);
        setResult(RESULT_CODE_SUCCESS_NADRA, getIntent());
        finish();
    }

    @Override
    public void onError(String code, String message) {
        AppLogger.i("Error code: "+code + " Message: " + message);
        if (code.equals("500")) {
            getIntent().putExtra("code", code);
            getIntent().putExtra("msg", AppMessages.EXCEPTION);
        } else {
            getIntent().putExtra("code", code);
            getIntent().putExtra("msg", message);
        }
        setResult(RESULT_CODE_FAILED_NADRA_OTHER_ERROR, getIntent());
        finish();
    }

    @Override
    public void onInvalidFingerIndex(String code, String message, List<Fingers> validFingers) {

        String validIndexes = "";
        ArrayList<String> fingers=new ArrayList<String>();
        ArrayList<Integer> fingerIndexes =new ArrayList<Integer>();

        for (Fingers finger:validFingers) {
            validIndexes = validIndexes + finger + "\n";
            fingers.add(finger.getValue());
            fingerIndexes.add(getRespectiveFinger(finger.getValue()));
        }

        AppLogger.i("on Invalid Finger Index, Error code: "+code+ " \n Fingers: "+validIndexes);

        getIntent().putExtra("title", message);
        getIntent().putExtra("code", code);
        getIntent().putExtra("validIndexes", validIndexes);
        getIntent().putStringArrayListExtra("fingers",fingers);
        getIntent().putExtra("validFingerIndexes", fingerIndexes);

        setResult(RESULT_CODE_FAILED_NADRA_FINGER_INDEXES, getIntent());
        finish();
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
