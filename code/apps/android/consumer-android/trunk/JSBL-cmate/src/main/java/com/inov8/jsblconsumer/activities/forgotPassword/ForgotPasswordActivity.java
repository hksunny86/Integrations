package com.inov8.jsblconsumer.activities.forgotPassword;

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
 * Created by ALI REHAN on 3/22/2018.
 */

public class ForgotPasswordActivity extends BaseCommunicationActivity implements View.OnClickListener {

    private Button btnNext, btnResend;
    private TextView hintTextOtp;
    private TextView lblPassword, headerText;
    private String strOtp, strPassword, strPasswordConfirm;
    private int currentCommand;
    private SmsReceiver mSmsReceiver;
    private boolean isSmsReceiverRegistered = false;
    private String otp, cnic, mobileNumber, password, comfirmPassword;
    PasswordControllerLogin passwordController, passwordControllerCon;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setContentView(R.layout.activity_forgot_password);
        fetchIntent();
        headerImplementation();
        setUI();

    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    ForgotPasswordActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }


        try {
            showLoading("Please Wait", "Processing...");
            new HttpAsyncTask(ForgotPasswordActivity.this).execute(Constants.CMD_FORGOT_PASSWORD_SECOND + "", mobileNumber, "", password, comfirmPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            switch (currentCommand) {
                case Constants.CMD_FORGOT_PASSWORD_SECOND: {
                    hideLoading();
                    if (table != null && table.containsKey("errs")) {
                        List<?> list = (List<?>) table.get("errs");
                        MessageModel message = (MessageModel) list.get(0);
                        hideLoading();
                        String code = message.getCode();
                        AppLogger.i("##Error Code: " + code);
                        if (code.equals("9029")) {


                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    ForgotPasswordActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                        }
                                    }, false, PopupDialogs.Status.ERROR, false, null);


                        } else {
                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    ForgotPasswordActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                        }
                                    }, false, PopupDialogs.Status.ERROR, false, null);

                        }

                    } else if (table.containsKey(Constants.KEY_LIST_MSGS)) {
                        List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                        MessageModel message = (MessageModel) list.get(0);


//                        dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), "Successful!",
//                                ForgotPasswordActivity.this, getString(R.string.ok), new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialogGeneral.dismiss();
//                                        goToLogin();
//                                    }
//                                }, false, PopupDialogs.Status.SUCCESS, false, null);


                        dialogGeneral = PopupDialogs.createSuccessDialog("Successful!", message.getDescr(), this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                goToLogin();
                            }
                        }, null);

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


        passwordController = new PasswordControllerLogin(findViewById(R.id.layoutPasswordPin), true, false, this);
        passwordController.setHeading("New Password");
        passwordController.setHint("");
        passwordController.hideShowPasswordIcon();
        passwordControllerCon = new PasswordControllerLogin(findViewById(R.id.layoutPasswordPinCon), false, false, this);
        passwordControllerCon.setHeading("Confirm New Password");
        passwordControllerCon.setHint("");
        passwordControllerCon.hideShowPasswordIcon();


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
                strPasswordConfirm = passwordControllerCon.getPassword();


                if (TextUtils.isEmpty(strPassword)) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.ERROR_EMPTY_PASSWORD, AppMessages.ALERT_HEADING,
                            ForgotPasswordActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


                    return;
                }
                if (strPassword.length() < Constants.LOGIN_PIN_LENGTH) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INVALID_LOGIN_PASSWORD_LENGTH, AppMessages.ALERT_HEADING,
                            ForgotPasswordActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


                    return;
                }

                if (TextUtils.isEmpty(strPasswordConfirm)) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.ERROR_EMPTY_PASSWORD, AppMessages.ALERT_HEADING,
                            ForgotPasswordActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


                    return;
                }
                if (strPasswordConfirm.length() < Constants.LOGIN_PIN_LENGTH) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INVALID_LOGIN_PASSWORD_LENGTH, AppMessages.ALERT_HEADING,
                            ForgotPasswordActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


                    return;
                }

                if (!strPassword.equals(strPasswordConfirm)) {
                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.PASSWORD_MISMATCH, AppMessages.ALERT_HEADING,
                            ForgotPasswordActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                    return;
                }


                password = AesEncryptor.encrypt(strPassword);
                comfirmPassword = AesEncryptor.encrypt(strPasswordConfirm);
                currentCommand = Constants.CMD_FORGOT_PASSWORD_SECOND;
                processRequest();
        }
    }

    private void fetchIntent() {
        mobileNumber = getIntent().getExtras().getString(Constants.IntentKeys.MOBILE_NO);
//        strOtp = getIntent().getExtras().getString(Constants.IntentKeys.OTP);
//        otp=AesEncryptor.encrypt(strOtp);

    }
}

