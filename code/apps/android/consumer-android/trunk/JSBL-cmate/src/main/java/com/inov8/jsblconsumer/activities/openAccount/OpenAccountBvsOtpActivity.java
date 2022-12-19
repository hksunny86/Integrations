package com.inov8.jsblconsumer.activities.openAccount;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.CategoryMenuActivity;
import com.inov8.jsblconsumer.activities.mangers.SmsReceiver;
import com.inov8.jsblconsumer.activities.mangers.SmsUpdater;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountChangePinActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AesEncryptor;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.PasswordControllerAccountOpening;

import java.util.Hashtable;
import java.util.List;

public class OpenAccountBvsOtpActivity extends BaseCommunicationActivity implements View.OnClickListener, SmsUpdater {

    private Button btnNext, btnResend;
    private TextView hintTextOtp;
    private TextView lblPassword, headerText;
    private PasswordControllerAccountOpening passwordController;
    private String strPassword;
    private int currentCommand;
    private SmsReceiver mSmsReceiver;
    private boolean isSmsReceiverRegistered = false;
    private String otp, cnic, mobileNumber;
    private boolean isHRa,isForgotMpin;
    private View headingView;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setContentView(R.layout.activity_open_account_bvs_otp);
        fetchIntent();
        headerImplementation();
        setUI();
        initializeSmsReciever();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    OpenAccountBvsOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        if (currentCommand == Constants.CMD_CUSTOMER_REGISTER_OTP) {
            try {
                showLoading("Please Wait", "Authenticating...");
                new HttpAsyncTask(OpenAccountBvsOtpActivity.this).execute(
                        Constants.CMD_CUSTOMER_REGISTER_OTP + "", cnic, mobileNumber, otp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            switch (currentCommand) {
                case Constants.CMD_CUSTOMER_REGISTER_OTP: {
                    hideLoading();
                    if (table != null && table.containsKey("errs")) {
                        List<?> list = (List<?>) table.get("errs");
                        MessageModel message = (MessageModel) list.get(0);
                        hideLoading();
                        String code = message.getCode();
                        AppLogger.i("##Error Code: " + code);
                        if (code.equals("9029")) {


                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    OpenAccountBvsOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
//                                            finish();
                                        }
                                    }, false, PopupDialogs.Status.ERROR, false, null);


                        } else {
                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    OpenAccountBvsOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                        }
                                    }, false, PopupDialogs.Status.ERROR, false, null);

                        }

                    } else if (table.containsKey(Constants.KEY_LIST_MSGS)) {
                        List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                        MessageModel message = (MessageModel) list.get(0);

                        dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                OpenAccountBvsOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


                    } else if (table.containsKey(Constants.ATTR_DTID)) {
                        if(isForgotMpin==true){
                            Intent intent = new Intent(OpenAccountBvsOtpActivity.this, MyAccountChangePinActivity.class);
                            intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, new ProductModel("10028", Constants.FLOW_ID_GENERATE_MPIN + "", "SET MPIN"));
                            intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.SET_MPIN);
                            intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.IGNORE_AND_GOTO_MENU);
                            intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, false);
                            startActivity(intent);
                        }
                        setResult(RESULT_OK);
                        finish();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        passwordController.resetPassword();
    }

    @Override
    public void processNext() {
    }

    private void setUI() {

        findViewById(R.id.ivHeaderLogo).setVisibility(View.GONE);
        headerText = (TextView) findViewById(R.id.headerText);
        headerText.setVisibility(View.VISIBLE);
        headingView = findViewById(R.id.container_heading1);

        if(isHRa){
            headerText.setText("Update L0 A/C");
        }
        else if (isForgotMpin){
            headerText.setText(getString(R.string.forgot_mpin));
            headingView.setVisibility(View.GONE);
        }
        else{

            headerText.setText(getString(R.string.open_account));
        }
        hintTextOtp = (TextView) findViewById(R.id.hint_text_otp);
        hintTextOtp.setText(getString(R.string.otp_hint_text));

        lblPassword = (TextView) findViewById(R.id.lbl_password);
        lblPassword.setText(getString(R.string.otp));

        passwordController = new PasswordControllerAccountOpening
                (findViewById(R.id.layoutPassword), false, false, this);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnResend = (Button) findViewById(R.id.btnResend);

        btnResend.setVisibility(View.GONE);

        btnNext.setOnClickListener(this);
        btnResend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnResend: {
                // implement resend functionality here...
                break;
            }

            case R.id.btnNext:
                hideKeyboard(v);
                strPassword = passwordController.getPassword();

                if (TextUtils.isEmpty(strPassword)) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.ERROR_EMPTY_PIN, AppMessages.ALERT_HEADING,
                            OpenAccountBvsOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


//                    popupDialogs.createAlertDialog(AppMessages.ERROR_EMPTY_PIN,
//                            AppMessages.ALERT_HEADING, OpenAccountBvsOtpActivity.this, PopupDialogs.Status.ERROR);
                    return;
                }
                if (strPassword.length() < Constants.ACCOUNT_OPENING_OTP_LENGTH) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INVALID_ACCOUNT_OPEN_PIN_LENGTH, AppMessages.ALERT_HEADING,
                            OpenAccountBvsOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


//                    popupDialogs.createAlertDialog(AppMessages.INVALID_ACCOUNT_OPEN_PIN_LENGTH,
//                            AppMessages.ALERT_HEADING, OpenAccountBvsOtpActivity.this, PopupDialogs.Status.ERROR);
                    return;
                }

                otp = AesEncryptor.encrypt(strPassword);
                passwordController.resetPassword();
                passwordController.setEnable();
                currentCommand = Constants.CMD_CUSTOMER_REGISTER_OTP;
                processRequest();
        }
    }

    private void fetchIntent() {
        mobileNumber = getIntent().getExtras().getString(Constants.IntentKeys.MOBILE_NO);
        cnic = getIntent().getExtras().getString(Constants.IntentKeys.CNIC);
        isHRa = getIntent().getBooleanExtra(Constants.IntentKeys.HRA, false);
        isForgotMpin = getIntent().getBooleanExtra(Constants.IntentKeys.FORGOT_MPIN, false);
    }

    private void initializeSmsReciever() {
        mSmsReceiver = new SmsReceiver(this);
        if (!isSmsReceiverRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(mSmsReceiver, filter);
            isSmsReceiverRegistered = true;
        }
    }

    private void unregisterSmsReceiver() {
        if (isSmsReceiverRegistered) {
            unregisterReceiver(mSmsReceiver);
            isSmsReceiverRegistered = false;
        }
    }

    @Override
    public void gotSms(String otpCode) {
        if (otpCode != null) {
            passwordController.setOTP(otpCode);
            unregisterSmsReceiver();
            this.otp = AesEncryptor.encrypt(otpCode);
            currentCommand = Constants.CMD_CUSTOMER_REGISTER_OTP;
            View view = this.getCurrentFocus();
            hideKeyboard(view);
            processRequest();
//            passwordController.resetPassword();
        }
    }
}

