package com.inov8.jsblconsumer.activities.retailPayment.retailpaymentmpass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.MainMenuActivity;
import com.inov8.jsblconsumer.activities.openAccount.OpenAccountBvsOtpActivity;
import com.inov8.jsblconsumer.activities.retailPayment.RetailPaymentConfirmationActivity;
import com.inov8.jsblconsumer.activities.retailPayment.RetailPaymentInputActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.QRCodeEnum;
import com.inov8.jsblconsumer.util.QRcodeGenerator1;
import com.inov8.jsblconsumer.util.XmlConstants;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class RetailPaymentActivity extends BaseCommunicationActivity implements View.OnClickListener {
    private String merchantId;
    private String amount = "";
    private String merchantName = "";
    private String merchantLaction = "";
    private String contents;
    Activity activity;
    private Button btnScanQrCode;
    private Button btnNext;
    private EditText etMerchantId;


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_retail_payment);
        headerImplementation();
        bottomBarImplementation(RetailPaymentActivity.this, "");
//        checkSoftKeyboardD();
        fetchIntent();
        activity = this;
        setUI();


        btnScanQrCode = (Button) findViewById(R.id.btnScanQrCode);
        btnScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount = "";
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("");
                intentIntegrator.setCameraId(0);
                intentIntegrator.addExtra("SCAN_WIDTH", 800);
                intentIntegrator.addExtra("SCAN_HEIGHT", 800);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.setCaptureActivity(new QRScanInputMPassActivity().getClass());
                intentIntegrator.initiateScan();

            }
        });

    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_CANCELED) {
            retailPaymentRequest(requestCode, resultCode, intent);
        } else
            super.onActivityResult(requestCode, resultCode, intent);
    }

    public void retailPaymentRequest(int requestCode, int resultCode, Intent intent) {

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            contents = scanResult.getContents();
            if (contents != null) {
                AppLogger.i("##Encrypted Contents: " + contents);
//                thirdPartyFlag = false;
                try {
                    AppLogger.i("##Decrypted Contents: " + contents);

                    if (isValidQR(contents)) {

//                        Map map = QRcodeGeneration.genericQRCodeBreak(contents);

                        Map map = QRcodeGenerator1.genericQRCodeBreak(contents);

                        if (map.get(Constants.MastercardQR.QR_PAN_TAG) != null || map.get(Constants.MastercardOldQR.QR_PAN_TAG) != null) {

                            if (map.get(Constants.MastercardQR.QR_MERCHANT_ID_TAG) != null || map.get(Constants.MastercardOldQR.QR_MERCHANT_ID_TAG) != null) {
                                merchantId = map.get(Constants.MastercardQR.QR_MERCHANT_ID_TAG) != null ? map.get(Constants.MastercardQR.QR_MERCHANT_ID_TAG).toString() : map.get(Constants.MastercardOldQR.QR_MERCHANT_ID_TAG).toString();

                            }
                        }
                        if (map.get(Constants.MastercardQR.QR_AMOUNT_TAG) != null || map.get(Constants.MastercardOldQR.QR_AMOUNT_TAG) != null) {
                            amount = map.get(Constants.MastercardQR.QR_AMOUNT_TAG) != null ? map.get(Constants.MastercardQR.QR_AMOUNT_TAG).toString() : map.get(Constants.MastercardOldQR.QR_AMOUNT_TAG).toString();
//                            Toast.makeText(getApplicationContext(), amount, Toast.LENGTH_LONG).show();
                        }

                        if (map.get(Constants.MastercardQR.MERCHANT_NAME) != null || map.get(QRCodeEnum.MERCHANT_NAME) != null) {
                            merchantName = map.get(Constants.MastercardQR.MERCHANT_NAME) != null ? map.get(Constants.MastercardQR.MERCHANT_NAME).toString() : map.get(QRCodeEnum.MERCHANT_NAME).toString();
//                            Toast.makeText(getApplicationContext(), merchantName, Toast.LENGTH_LONG).show();
                        }

                        if (map.get(Constants.MastercardQR.MERCHANT_LACTION) != null || map.get(QRCodeEnum.MERCHANT_NAME) != null) {
                            merchantLaction = map.get(Constants.MastercardQR.MERCHANT_LACTION) != null ? map.get(Constants.MastercardQR.MERCHANT_LACTION).toString() : map.get(QRCodeEnum.MERCHANT_NAME).toString();
//                            Toast.makeText(getApplicationContext(), merchantLaction, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid QR code.", Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (Exception iae) {
                    AppLogger.e(iae);
                    return;
                }
            } else {
                contents = "";
            }
        }
        processNext();
    }


    private boolean isValidQR(String qrCode) {
        boolean isValid = false;
        final String QR_MERCHANT_ID = "00";

        try {

            if ((qrCode.startsWith(QR_MERCHANT_ID))) {
                isValid = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnNext: {


                amount = "";


                if (TextUtils.isEmpty(etMerchantId.getText())) {
                    etMerchantId.setError(AppMessages.ERROR_EMPTY_FIELD);
                    return;
                }

                merchantId = etMerchantId.getText().toString();

//                processRequest();

                processNext();

                break;
            }
        }
    }

    private void setUI() {
        etMerchantId = (EditText) findViewById(R.id.etMerchantId);
        disableCopyPaste(etMerchantId);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);


        ivHeaderLog.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerText.setText(getString(R.string.qr_payment));

        contents = "";
    }

    private void fetchIntent() {

    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    RetailPaymentActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(RetailPaymentActivity.this).execute(
                Constants.CMD_RETAIL_PAYMENT_VERIFICATION + "", merchantId,
                amount);

    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            hideLoading();
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);
                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        RetailPaymentActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                    askMpin(mBundle, RetailPaymentActivity.class, true);
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);

            } else if (table.containsKey(Constants.KEY_LIST_MSGS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);

                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        RetailPaymentActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);

            } else {


                merchantId = (table.get(XmlConstants.Attributes.MRID).toString());
                merchantName = (table.get(XmlConstants.Attributes.MNAME).toString());
                amount = (table.get(XmlConstants.Attributes.TAMT).toString());
                amount = (table.get(XmlConstants.Attributes.TAMTF).toString());
                processNext();

            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
        hideLoading();

    }

    @Override
    public void processNext() {
        Intent intent = new Intent(RetailPaymentActivity.this, RetailPaymentComfirmationMPassActivity.class);
        intent.putExtra(Constants.IntentKeys.MERCHANT_ID, merchantId);
        intent.putExtra(Constants.IntentKeys.MERCHANT_NAME, merchantName);
        intent.putExtra(Constants.IntentKeys.MERCHANT_LACTION, merchantLaction);
        intent.putExtra(Constants.IntentKeys.QR_AMOUNT, amount);
        intent.putExtra(Constants.IntentKeys.QR_STRING, contents);
        intent.putExtras(mBundle);
        startActivity(intent);

    }

}
