package com.inov8.jsblconsumer.activities.retailPayment.retailpaymentmpass;

import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;


/**
 * This activity has a margin.
 */
public class QRScanInputMPassActivity extends CaptureActivity implements View.OnClickListener {

    private PopupDialogs popupDialogs;
    private Dialog dialog;
    private String strMerchantId = "";
    protected String strHeaderHeading, strHeaderSubHeading;
    private TextView tvHeaderHeading, tvHeaderSubHeading, headerText;
    private AppCompatImageView ivHeaderLog;
    private Intent intent;


    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_qr_scan_mpass_input);
        popupDialogs = new PopupDialogs(this);
        intent = getIntent();
        setUI();

        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }

    private void setUI() {


        AppCompatImageView btnBack = (AppCompatImageView) findViewById(R.id.btnBack);
        AppCompatImageView btnLogout = (AppCompatImageView) findViewById(R.id.btnSignout);
        AppCompatImageView btnHome = (AppCompatImageView) findViewById(R.id.btnHome);


        headerText = (TextView) findViewById(R.id.headerText);
        ivHeaderLog = (AppCompatImageView) findViewById(R.id.ivHeaderLogo);


        ivHeaderLog.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerText.setText(getString(R.string.qr_payment));

        btnHome.setVisibility(View.GONE);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });
        btnLogout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {


    }


    public void goToMainMenu() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainMenu();
    }


}