//package com.inov8.jsblconsumer.bvs;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.appcompat.widget.AppCompatImageView;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.TextView;
//
//import com.inov8.jsblconsumer.R;
//import com.inov8.jsblconsumer.activities.BaseActivity;
//import com.inov8.jsblconsumer.util.ApplicationData;
//import com.inov8.jsblconsumer.util.Constants;
//import com.paysyslabs.instascan.Fingers;
//
//import java.util.ArrayList;
//
//public class FingerScanActivity extends BaseActivity implements View.OnClickListener {
//
//    private AppCompatImageView ivFingerOne, ivFingerTwo, ivFingerThree, ivFingerFour, ivFingerFive,
//            ivFingerSix, ivFingerSeven, ivFingerEight, ivFingerNine, ivFingerTen;
//    private View vFing1, vFing2, vFing3, vFing4, vFing5, vFing6, vFing7, vFing8, vFing9, vFing10;
//    private Intent intent;
//    private Fingers[] fingerNames;
//    private TextView headerText;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_finger_scan1);
//        hideSoftKeyboard();
//        headerImplementation();
//        setUI();
//
//        intent = getIntent();
//        ArrayList<String> fingers = intent.getStringArrayListExtra("fingers");
//
//        if (fingers.size() == 0) {
//            setResult(Constants.RESULT_CODE_FINGERS_EMPTY, getIntent());
//            finish();
//        }
//
//        fingerNames = Fingers.values();
//
//        if (fingers != null) {
//            for (String finger : fingers) {
//                if (finger.equals("RIGHT_THUMB")) {
//                    ivFingerOne.setVisibility(View.VISIBLE);
//                    vFing1.setOnClickListener(this);
//                } else if (finger.equals("RIGHT_INDEX")) {
//                    ivFingerTwo.setVisibility(View.VISIBLE);
//                    vFing2.setOnClickListener(this);
//                } else if (finger.equals("RIGHT_MIDDLE")) {
//                    ivFingerThree.setVisibility(View.VISIBLE);
//                    vFing3.setOnClickListener(this);
//                } else if (finger.equals("RIGHT_RING")) {
//                    ivFingerFour.setVisibility(View.VISIBLE);
//                    vFing4.setOnClickListener(this);
//                } else if (finger.equals("RIGHT_LITTLE")) {
//                    ivFingerFive.setVisibility(View.VISIBLE);
//                    vFing5.setOnClickListener(this);
//                } else if (finger.equals("LEFT_THUMB")) {
//                    ivFingerSix.setVisibility(View.VISIBLE);
//                    vFing6.setOnClickListener(this);
//                } else if (finger.equals("LEFT_INDEX")) {
//                    ivFingerSeven.setVisibility(View.VISIBLE);
//                    vFing7.setOnClickListener(this);
//                } else if (finger.equals("LEFT_MIDDLE")) {
//                    ivFingerEight.setVisibility(View.VISIBLE);
//                    vFing8.setOnClickListener(this);
//                } else if (finger.equals("LEFT_RING")) {
//                    ivFingerNine.setVisibility(View.VISIBLE);
//                    vFing9.setOnClickListener(this);
//                } else if (finger.equals("LEFT_LITTLE")) {
//                    ivFingerTen.setVisibility(View.VISIBLE);
//                    vFing10.setOnClickListener(this);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.view_1:
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[0]);
//                ApplicationData.currentFinger = fingerNames[0];
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.view_2:
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[1]);
//                ApplicationData.currentFinger = fingerNames[1];
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.view_3:
//                ApplicationData.currentFinger = fingerNames[2];
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[2]);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.view_4:
//                ApplicationData.currentFinger = fingerNames[3];
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[3]);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.view_5:
//                ApplicationData.currentFinger = fingerNames[4];
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[4]);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.view_6:
//                ApplicationData.currentFinger = fingerNames[5];
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[5]);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.view_7:
//                ApplicationData.currentFinger = fingerNames[6];
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[6]);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.view_8:
//                ApplicationData.currentFinger = fingerNames[7];
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[7]);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.view_9:
//                ApplicationData.currentFinger = fingerNames[8];
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[8]);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.view_10:
//                ApplicationData.currentFinger = fingerNames[9];
//                intent.putExtra(Constants.IntentKeys.FINGER, fingerNames[9]);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//        }
//    }
//
//    public void setUI() {
//
//        findViewById(R.id.ivHeaderLogo).setVisibility(View.GONE);
//        headerText = (TextView) findViewById(R.id.headerText);
//        headerText.setVisibility(View.VISIBLE);
//        headerText.setText(getString(R.string.open_account));
//
//        btnHome.setVisibility(View.GONE);
//        btnSignout.setVisibility(View.GONE);
//        ivFingerOne = (AppCompatImageView) findViewById(R.id.ivfingerone);
//        ivFingerTwo = (AppCompatImageView) findViewById(R.id.ivfingertwo);
//        ivFingerThree = (AppCompatImageView) findViewById(R.id.ivfingerthree);
//        ivFingerFour = (AppCompatImageView) findViewById(R.id.ivfingerfour);
//        ivFingerFive = (AppCompatImageView) findViewById(R.id.ivfingerfive);
//        ivFingerSix = (AppCompatImageView) findViewById(R.id.ivfingersix);
//        ivFingerSeven = (AppCompatImageView) findViewById(R.id.ivfingerseven);
//        ivFingerEight = (AppCompatImageView) findViewById(R.id.ivfingereight);
//        ivFingerNine = (AppCompatImageView) findViewById(R.id.ivfingernine);
//        ivFingerTen = (AppCompatImageView) findViewById(R.id.ivfingerten);
//
//        vFing1 = (View) findViewById(R.id.view_1);
//        vFing2 = (View) findViewById(R.id.view_2);
//        vFing3 = (View) findViewById(R.id.view_3);
//        vFing4 = (View) findViewById(R.id.view_4);
//        vFing5 = (View) findViewById(R.id.view_5);
//        vFing6 = (View) findViewById(R.id.view_6);
//        vFing7 = (View) findViewById(R.id.view_7);
//        vFing8 = (View) findViewById(R.id.view_8);
//        vFing9 = (View) findViewById(R.id.view_9);
//        vFing10 = (View) findViewById(R.id.view_10);
//    }
//
//
//    public void hideSoftKeyboard() {
//        if(getCurrentFocus()!=null) {
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//    }
//}
