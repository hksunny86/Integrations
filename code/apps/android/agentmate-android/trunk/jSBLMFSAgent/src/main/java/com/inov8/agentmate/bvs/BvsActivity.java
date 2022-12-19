package com.inov8.agentmate.bvs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.agentmate.bvs.P41M2.P41M2Handler;

import com.inov8.agentmate.bvs.suprema.SupremaSlimHandlerNew;
import com.inov8.agentmate.bvs.supremaSlim2.SupremaSlim2Handler;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsblmfs.R;

public class BvsActivity extends BaseActivity implements View.OnClickListener, BvsController.BvsCompleteListner {

    private BvsController bvsController;
    private ImageView fingerPrintImage;
    private Button btn_Scan, btn_reScan, btn_next;
    private LinearLayout ll_scanDone;
    private String hexString, strFingerIndex, strTemplateType, strWSQ, minutiaeCount, NFIQuality;
    private TextView infoMessage;
    private Intent intent;
    private String[] fingers = null;
    private boolean fingersEnabled = true;
    String selectedFinger = null;

    private AppCompatImageView ivFingerOne, ivFingerTwo, ivFingerThree, ivFingerFour, ivFingerFive,
            ivFingerSix, ivFingerSeven, ivFingerEight, ivFingerNine, ivFingerTen;
    private View vFing1, vFing2, vFing3, vFing4, vFing5, vFing6, vFing7, vFing8, vFing9, vFing10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bvs);

        intent = getIntent();
        if (intent.getExtras().containsKey(XmlConstants.FINGERS)) {
            fingers = intent.getStringArrayExtra(XmlConstants.FINGERS);
        }

        init();
        bvsController.onCreate();
        headerImplementation();
    }

    @Override
    protected void onStart() {
        bvsController.onStart();
        super.onStart();
    }

    @Override
    protected void onResume() {
        bvsController.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        bvsController.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        bvsController.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        bvsController.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        bvsController.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public void setButtonText(String text){
        btn_Scan.setText(text);
    }

    @Override
    public void setButtonEnabled(Boolean enabled){
        btn_Scan.setEnabled(enabled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        bvsController.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        bvsController = getDeviceInstance();
        fingerPrintImage = (ImageView) findViewById(R.id.imageViewFingerImpression);
        btn_Scan = (Button) findViewById(R.id.btn_Scan);
        btn_reScan = (Button) findViewById(R.id.btn_reScan);
        btn_next = (Button) findViewById(R.id.btn_next);

        ll_scanDone = (LinearLayout) findViewById(R.id.ll_scanDone);
        infoMessage = (TextView) findViewById(R.id.lblMessage);
        infoMessage.setText(getString(R.string.place_finger));

        btn_Scan.setOnClickListener(this);
        btn_reScan.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        ivFingerOne = (AppCompatImageView) findViewById(R.id.ivfingerone);
        ivFingerTwo = (AppCompatImageView) findViewById(R.id.ivfingertwo);
        ivFingerThree = (AppCompatImageView) findViewById(R.id.ivfingerthree);
        ivFingerFour = (AppCompatImageView) findViewById(R.id.ivfingerfour);
        ivFingerFive = (AppCompatImageView) findViewById(R.id.ivfingerfive);
        ivFingerSix = (AppCompatImageView) findViewById(R.id.ivfingersix);
        ivFingerSeven = (AppCompatImageView) findViewById(R.id.ivfingerseven);
        ivFingerEight = (AppCompatImageView) findViewById(R.id.ivfingereight);
        ivFingerNine = (AppCompatImageView) findViewById(R.id.ivfingernine);
        ivFingerTen = (AppCompatImageView) findViewById(R.id.ivfingerten);

        if (fingers != null) {
            for (String finger : fingers) {
                if (finger.equals("1")) {
                    vFing1 = findViewById(R.id.view_1);
                    ivFingerOne.setVisibility(View.VISIBLE);
                    vFing1.setOnClickListener(this);
                } else if (finger.equals("2")) {
                    vFing2 = findViewById(R.id.view_2);
                    ivFingerTwo.setVisibility(View.VISIBLE);
                    vFing2.setOnClickListener(this);
                } else if (finger.equals("3")) {
                    vFing3 = findViewById(R.id.view_3);
                    ivFingerThree.setVisibility(View.VISIBLE);
                    vFing3.setOnClickListener(this);
                } else if (finger.equals("4")) {
                    vFing4 = findViewById(R.id.view_4);
                    ivFingerFour.setVisibility(View.VISIBLE);
                    vFing4.setOnClickListener(this);
                } else if (finger.equals("5")) {
                    vFing5 = findViewById(R.id.view_5);
                    ivFingerFive.setVisibility(View.VISIBLE);
                    vFing5.setOnClickListener(this);
                } else if (finger.equals("6")) {
                    vFing6 = findViewById(R.id.view_6);
                    ivFingerSix.setVisibility(View.VISIBLE);
                    vFing6.setOnClickListener(this);
                } else if (finger.equals("7")) {
                    vFing7 = findViewById(R.id.view_7);
                    ivFingerSeven.setVisibility(View.VISIBLE);
                    vFing7.setOnClickListener(this);
                } else if (finger.equals("8")) {
                    vFing8 = findViewById(R.id.view_8);
                    ivFingerEight.setVisibility(View.VISIBLE);
                    vFing8.setOnClickListener(this);
                } else if (finger.equals("9")) {
                    vFing9 = findViewById(R.id.view_9);
                    ivFingerNine.setVisibility(View.VISIBLE);
                    vFing9.setOnClickListener(this);
                } else if (finger.equals("10")) {
                    vFing10 = findViewById(R.id.view_10);
                    ivFingerTen.setVisibility(View.VISIBLE);
                    vFing10.setOnClickListener(this);
                }
            }
        } else {
            vFing1 = findViewById(R.id.view_1);
            ivFingerOne.setVisibility(View.VISIBLE);
            vFing1.setOnClickListener(this);
            vFing2 = findViewById(R.id.view_2);
            ivFingerTwo.setVisibility(View.VISIBLE);
            vFing2.setOnClickListener(this);
            vFing3 = findViewById(R.id.view_3);
            ivFingerThree.setVisibility(View.VISIBLE);
            vFing3.setOnClickListener(this);
            vFing4 = findViewById(R.id.view_4);
            ivFingerFour.setVisibility(View.VISIBLE);
            vFing4.setOnClickListener(this);
            vFing5 = findViewById(R.id.view_5);
            ivFingerFive.setVisibility(View.VISIBLE);
            vFing5.setOnClickListener(this);
            vFing6 = findViewById(R.id.view_6);
            ivFingerSix.setVisibility(View.VISIBLE);
            vFing6.setOnClickListener(this);
            vFing7 = findViewById(R.id.view_7);
            ivFingerSeven.setVisibility(View.VISIBLE);
            vFing7.setOnClickListener(this);
            vFing8 = findViewById(R.id.view_8);
            ivFingerEight.setVisibility(View.VISIBLE);
            vFing8.setOnClickListener(this);
            vFing9 = findViewById(R.id.view_9);
            ivFingerNine.setVisibility(View.VISIBLE);
            vFing9.setOnClickListener(this);
            vFing10 = findViewById(R.id.view_10);
            ivFingerTen.setVisibility(View.VISIBLE);
            vFing10.setOnClickListener(this);
        }
        TextView headingMessage = (TextView) findViewById(R.id.lblHeading);
        headingMessage.setText(getIntent().getExtras().getString(XmlConstants.ATTR_APP_FLOW));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_1:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerOne.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "1";
                    selectedFinger = AppFingers.RIGHT_THUMB;
                }
                break;
            case R.id.view_2:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerTwo.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "2";
                    selectedFinger = AppFingers.RIGHT_INDEX;
                }
                break;
            case R.id.view_3:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerThree.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "3";
                    selectedFinger = AppFingers.RIGHT_MIDDLE;
                }
                break;
            case R.id.view_4:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerFour.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "4";
                    selectedFinger = AppFingers.RIGHT_RING;
                }
                break;
            case R.id.view_5:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerFive.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "5";
                    selectedFinger = AppFingers.RIGHT_LITTLE;
                }
                break;
            case R.id.view_6:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerSix.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "6";
                    selectedFinger = AppFingers.LEFT_THUMB;
                }
                break;
            case R.id.view_7:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerSeven.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "7";
                    selectedFinger = AppFingers.LEFT_INDEX;
                }
                break;
            case R.id.view_8:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerEight.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "8";
                    selectedFinger = AppFingers.LEFT_MIDDLE;
                }
                break;
            case R.id.view_9:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerNine.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "9";
                    selectedFinger = AppFingers.LEFT_RING;
                }
                break;
            case R.id.view_10:
                if (fingersEnabled) {
                    selectOneFinger();
                    ivFingerTen.setVisibility(View.GONE);
                    ApplicationData.currentFingerIndex = "10";
                    selectedFinger = AppFingers.LEFT_LITTLE;
                }
                break;

            case R.id.btn_Scan:
                if (selectedFinger != null) {
                    strFingerIndex = getRespectiveFinger(selectedFinger);
                    disableFingers();
                    bvsController.scanImage();
                } else
                    showToast("Please select a finger");
                break;

            case R.id.btn_reScan:
                enableFingers();
                fingerPrintImage.setImageResource(R.drawable.icon_finger_print_scan);
                setInfoMessage("Place finger on Scanner and press Scan Button");
                ApplicationData.errorCount = 0;
                btn_Scan.setVisibility(View.VISIBLE);
                ll_scanDone.setVisibility(View.GONE);
                break;

            case R.id.btn_next:
                bvsController.onDestroy();
                processNext();
                break;
        }
    }

    private void selectOneFinger() {
        if (selectedFinger != null) {
            if (selectedFinger.equals(AppFingers.RIGHT_THUMB)) {
                ivFingerOne.setVisibility(View.VISIBLE);
            } else if (selectedFinger.equals(AppFingers.RIGHT_INDEX)) {
                ivFingerTwo.setVisibility(View.VISIBLE);
            } else if (selectedFinger.equals(AppFingers.RIGHT_MIDDLE)) {
                ivFingerThree.setVisibility(View.VISIBLE);
            } else if (selectedFinger.equals(AppFingers.RIGHT_RING)) {
                ivFingerFour.setVisibility(View.VISIBLE);
            } else if (selectedFinger.equals(AppFingers.RIGHT_LITTLE)) {
                ivFingerFive.setVisibility(View.VISIBLE);
            } else if (selectedFinger.equals(AppFingers.LEFT_THUMB)) {
                ivFingerSix.setVisibility(View.VISIBLE);
            } else if (selectedFinger.equals(AppFingers.LEFT_INDEX)) {
                ivFingerSeven.setVisibility(View.VISIBLE);
            } else if (selectedFinger.equals(AppFingers.LEFT_MIDDLE)) {
                ivFingerEight.setVisibility(View.VISIBLE);
            } else if (selectedFinger.equals(AppFingers.LEFT_RING)) {
                ivFingerNine.setVisibility(View.VISIBLE);
            } else if (selectedFinger.equals(AppFingers.LEFT_LITTLE)) {
                ivFingerTen.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void changeLayoutAfterFirstScan() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disableFingers();
                btn_Scan.setVisibility(View.GONE);
                ll_scanDone.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(BvsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setBitmap(Bitmap bmp) {
        fingerPrintImage.setImageBitmap(bmp);
    }

    @Override
    public void setTemplateType(String type) {
        this.strTemplateType = type;
    }

    @Override
    public void setInfoMessage(String message) {
        infoMessage.setText(message);
    }

    @Override
    public void setHexString(String hexString) {
        this.hexString = hexString;
    }

    @Override
    public void setWSQ(String wsq) {
        strWSQ = wsq;
    }

    @Override
    public void setMinutiaeCount(String minutiaeCount) {
        this.minutiaeCount = minutiaeCount;
    }

    @Override
    public void setNFIQuality(String NFIQuality) {
        this.NFIQuality = NFIQuality;

    }

    private void enableFingers(){
        fingersEnabled = true;
    }

    private void disableFingers(){
        fingersEnabled = false;
    }

    private void processNext() {
        Intent intent = getIntent();
        intent.putExtra(XmlConstants.ATTR_FINGER_INDEX, strFingerIndex);
        intent.putExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE, hexString);
        intent.putExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE_TYPE, strTemplateType);
        intent.putExtra(XmlConstants.ATTR_WSQ, strWSQ);
        intent.putExtra(XmlConstants.ATTR_MINUTIAE_COUNT, minutiaeCount);
        intent.putExtra(XmlConstants.ATTR_NFI_QUALITY, NFIQuality);
        setResult(RESULT_OK, intent);
        finish();
    }

    private BvsController getDeviceInstance() {
        if(ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.SUPREMA))
            return new SupremaSlimHandlerNew(this);
        else if(ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.P41M2)) 
            return new P41M2Handler(this);
        else if(ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.SUPREMA_SLIM_2))
            return new SupremaSlim2Handler(this);
        else
            return null;
    }
}
