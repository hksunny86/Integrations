package com.inov8.jsblconsumer.activities.retailPayment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;

public class RetailPaymentScanItemActivity extends BaseActivity {

    Button btnScan;
    String info, mobileNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail_payment_scan_item);

        titleImplementation(null, "Retail Payment", "", this);

        btnScan = (Button) findViewById(R.id.btn_scan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });

        headerImplementation();
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            StartCamera();
        } else {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RetailPaymentScanItemActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                StartCamera();
            }
        }
    }

    private void StartCamera() {
        IntentIntegrator integrator = new IntentIntegrator(RetailPaymentScanItemActivity.this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            info = scanResult.getContents();
            try {
                mobileNumber = info.substring(info.lastIndexOf("0A11") + 4, info.lastIndexOf("0B"));
            } catch (Exception e) {
                PopupDialogs.createAlertDialog(AppMessages.ERROR_INVALID_QR_CODE, AppMessages.ALERT_HEADING,
                        RetailPaymentScanItemActivity.this, PopupDialogs.Status.ERROR);
                return;
            }
        } else {
            PopupDialogs.createAlertDialog(AppMessages.ERROR_SCANNING_DATA, AppMessages.ALERT_HEADING,
                    RetailPaymentScanItemActivity.this, PopupDialogs.Status.ERROR);
            return;
        }
        intent = new Intent(getApplicationContext(), RetailPaymentInputActivity.class);
        intent.putExtra(Constants.IntentKeys.AMOB, mobileNumber);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            } else {
                showSettingsAlert();
            }
        } else {
            StartCamera();
        }
    }

    public void showSettingsAlert() {
        View.OnClickListener enableLocationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                startActivity(intent);
                dialogGeneral.dismiss();
            }
        };
        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGeneral.dismiss();
            }
        };
        dialogGeneral = PopupDialogs.createConfirmationDialog(
                AppMessages.CAMERA_PERMISSION_NOT_AVAILABLE,
                AppMessages.HEADING_CAMERA_PERMISSION, RetailPaymentScanItemActivity.this,
                enableLocationListener, "SETTINGS", cancelListener, "CANCEL",
                PopupDialogs.Status.ALERT);
    }
}
