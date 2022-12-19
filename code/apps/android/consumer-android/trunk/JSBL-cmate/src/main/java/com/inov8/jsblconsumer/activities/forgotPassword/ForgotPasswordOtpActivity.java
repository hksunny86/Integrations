package com.inov8.jsblconsumer.activities.forgotPassword;

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
import com.inov8.jsblconsumer.activities.mangers.SmsReceiver;
import com.inov8.jsblconsumer.activities.mangers.SmsUpdater;
import com.inov8.jsblconsumer.activities.myAccount.RegenerateMpinActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AesEncryptor;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.PasswordControllerAccountOpening;
import com.inov8.jsblconsumer.util.PasswordControllerLogin;

import java.util.Hashtable;
import java.util.List;


/**
 * Created by ALI REHAN on 3/21/2018.
 */

public class ForgotPasswordOtpActivity extends BaseCommunicationActivity implements View.OnClickListener, SmsUpdater {

    private Button btnNext, btnResend;
    private TextView hintTextOtp;
    private TextView lblPassword, headerText;
    private PasswordControllerAccountOpening passwordControllerOtp;
    private String strOtp, strPassword, strPasswordConfirm;
    private int currentCommand;
    private SmsReceiver mSmsReceiver;
    private boolean isSmsReceiverRegistered = false;
    private String otp, cnic, mobileNumber, password, comfirmPassword;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setContentView(R.layout.activity_forgot_password_otp);
        fetchIntent();
        headerImplementation();
        setUI();

        initializeSmsReciever();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    ForgotPasswordOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");

        try {
            switch (currentCommand) {

                case Constants.CMD_CUSTOMER_REGISTER_OTP: {


                    new HttpAsyncTask(ForgotPasswordOtpActivity.this).execute(
                            Constants.CMD_CUSTOMER_REGISTER_OTP + "", "", mobileNumber, otp);
                }

                break;
                case Constants.CMD_REGENERATE_OTP: {

                    new HttpAsyncTask(ForgotPasswordOtpActivity.this).execute(
                            Constants.CMD_REGENERATE_OTP + "", mobileNumber);
                }
            }


        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            switch (currentCommand) {
                case Constants.CMD_CUSTOMER_REGISTER_OTP:
                case Constants.CMD_REGENERATE_OTP: {
                    hideLoading();
                    if (table != null && table.containsKey("errs")) {
                        List<?> list = (List<?>) table.get("errs");
                        MessageModel message = (MessageModel) list.get(0);
                        hideLoading();
                        String code = message.getCode();
                        AppLogger.i("##Error Code: " + code);
                        if (code.equals("9029")) {


                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    ForgotPasswordOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                            finish();
                                        }
                                    }, false, PopupDialogs.Status.ERROR, false, null);


                        } else {
                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    ForgotPasswordOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
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
                                ForgotPasswordOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


                    } else if (table.containsKey(Constants.ATTR_DTID)) {
                        Intent intent = new Intent(ForgotPasswordOtpActivity.this, ForgotPasswordActivity.class);
                        intent.putExtra(Constants.IntentKeys.MOBILE_NO, mobileNumber);
                        intent.putExtra(Constants.IntentKeys.OTP, otp);
                        startActivity(intent);
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
    }

    @Override
    public void processNext() {
    }

    private void setUI() {

        findViewById(R.id.ivHeaderLogo).setVisibility(View.GONE);
        headerText = (TextView) findViewById(R.id.headerText);
        headerText.setVisibility(View.VISIBLE);
        headerText.setText(getString(R.string.forgot_pin));

        hintTextOtp = (TextView) findViewById(R.id.hint_text_otp);
        hintTextOtp.setText("Enter your 05 digits One Time password (OTP)");

        lblPassword = (TextView) findViewById(R.id.lbl_password);
        lblPassword.setText(getString(R.string.otp));

        passwordControllerOtp = new PasswordControllerAccountOpening(findViewById(R.id.layoutPassword), false, false, this);
        passwordControllerOtp.setHeading("OTP Verification");

        btnNext = (Button) findViewById(R.id.btnNext);
        btnResend = (Button) findViewById(R.id.btnRegenerate);

//        btnResend.setVisibility(View.GONE);

        btnNext.setOnClickListener(this);
        btnResend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnRegenerate: {
                // implement resend functionality here...
                passwordControllerOtp.resetPassword();
                passwordControllerOtp.setEnable();
                currentCommand = Constants.CMD_REGENERATE_OTP;
                processRequest();
                break;
            }

            case R.id.btnNext:
                hideKeyboard(v);
                strOtp = passwordControllerOtp.getPassword();

                if (TextUtils.isEmpty(strOtp)) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.ERROR_EMPTY_PIN, AppMessages.ALERT_HEADING,
                            ForgotPasswordOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


                    return;
                }
                if (strOtp.length() < Constants.ACCOUNT_OPENING_OTP_LENGTH) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INVALID_ACCOUNT_OPEN_PIN_LENGTH, AppMessages.ALERT_HEADING,
                            ForgotPasswordOtpActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


                    return;
                }


                passwordControllerOtp.resetPassword();
                passwordControllerOtp.setEnable();
                currentCommand = Constants.CMD_CUSTOMER_REGISTER_OTP;
                otp = AesEncryptor.encrypt(strOtp);
                processRequest();
//                Intent intent = new Intent(ForgotPasswordOtpActivity.this, ForgotPasswordActivity.class);
//                intent.putExtra(Constants.IntentKeys.MOBILE_NO, mobileNumber);
//                intent.putExtra(Constants.IntentKeys.OTP, strOtp);
                unregisterSmsReceiver();
//                startActivity(intent);
        }
    }

    private void fetchIntent() {
        mobileNumber = getIntent().getExtras().getString(Constants.IntentKeys.MOBILE_NO);

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
            passwordControllerOtp.setOTP(otpCode);
            unregisterSmsReceiver();
            this.otp = AesEncryptor.encrypt(otpCode);
//            currentCommand = Constants.CMD_CUSTOMER_REGISTER_OTP;
//            processRequest();

//            Intent intent = new Intent(ForgotPasswordOtpActivity.this, ForgotPasswordActivity.class);
//            intent.putExtra(Constants.IntentKeys.MOBILE_NO, mobileNumber);
//            intent.putExtra(Constants.IntentKeys.OTP, otpCode);
//            startActivity(intent);

            currentCommand = Constants.CMD_CUSTOMER_REGISTER_OTP;
            View view = this.getCurrentFocus();
            hideKeyboard(view);
            processRequest();
        }
    }
}

