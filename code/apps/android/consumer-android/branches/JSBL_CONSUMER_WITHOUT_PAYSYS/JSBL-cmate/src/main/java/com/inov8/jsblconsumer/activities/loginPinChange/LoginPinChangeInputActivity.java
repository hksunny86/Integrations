package com.inov8.jsblconsumer.activities.loginPinChange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.LoginActivity;
import com.inov8.jsblconsumer.activities.SplashActivity;
import com.inov8.jsblconsumer.activities.forgotPassword.ForgotPasswordActivity;
import com.inov8.jsblconsumer.activities.forgotPassword.ForgotPasswordMobileInputActivity;
import com.inov8.jsblconsumer.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.CnicController;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class LoginPinChangeInputActivity extends BaseCommunicationActivity {

    private EditText mobileId, mobileNo;
    private Button loginPinChangeBtn;
    private CnicController cnicController;
    private int currentCommand;
    private String cnic, mobileNumber;
    private String mobileIdStr, mobileNoStr;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pin_change_input);
        headerImplementation();
        titleImplementation(null, "Reset Login Pin", null, this);
        fetchIntents();
        setUI();

        loginPinChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    processRequest();
                }

            }
        });


    }
    private void fetchIntents() {
        mobileIdStr = mBundle.getString(Constants.IntentKeys.MOBILE_ID);
        mobileNoStr = mBundle.getString(Constants.IntentKeys.MOBILE_NO);

    }
    private void setUI() {
        loginPinChangeBtn =  findViewById(R.id.btnNextLoginPinChange);
        mobileId =  findViewById(R.id.etMobileId);
        mobileNo =  findViewById(R.id.etMobileNo);

        mobileId.setText(mobileIdStr,TextView.BufferType.EDITABLE);
        mobileNo.setText(mobileNoStr,TextView.BufferType.EDITABLE);
        mobileId.setEnabled(false);
        mobileNo.setEnabled(false);

//        findViewById(R.id.ivHeaderLogo).setVisibility(View.GONE);
//        headerText = (TextView) findViewById(R.id.headerText);
//        headerText.setVisibility(View.VISIBLE);
//        headerText.setText(getString(R.string.pin_change));

        cnicController = new CnicController(findViewById(R.id.cnicView), true, true, this);

        mobileId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() == 4) {
                    mobileNo.requestFocus();
//                    mobileNo.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 7) {
                    cnicController.requestFocus();
                } else if (s.length() == 0) {
                    mobileId.requestFocus();
                    mobileId.setSelection(mobileId.getText().length());
//                    mobileNo.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        currentCommand = Constants.CMD_RESET_LOGIN_PIN;

    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    LoginPinChangeInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(LoginPinChangeInputActivity.this).execute(Constants.CMD_RESET_LOGIN_PIN + "", mobileNumber,cnic);
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            switch (currentCommand) {
                case Constants.CMD_RESET_LOGIN_PIN: {
                    hideLoading();
                    if (table != null && table.containsKey("errs")) {
                        List<?> list = (List<?>) table.get("errs");
                        MessageModel message = (MessageModel) list.get(0);
                        hideLoading();
                        String code = message.getCode();
                        AppLogger.i("##Error Code: " + code);
                        if (code.equals("9029")) {


                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    LoginPinChangeInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                        }
                                    }, false, PopupDialogs.Status.ERROR, false, null);


                        } else {
                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    LoginPinChangeInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
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
    public void processNext() {

    }

    private boolean validate() {
            if (TextUtils.isEmpty(mobileId.getText())) {
                mobileId.setError(AppMessages.ERROR_EMPTY_FIELD);
                return false;
            }
            if (mobileId.length() < 4) {
                mobileId.setError(AppMessages.INVALID_MOBILE_ID_LENGTH);
                return false;
            }
            if (mobileId.getText().charAt(0) != '0' || mobileId.getText().charAt(1) != '3') {
                mobileId.setError(AppMessages.ERROR_MOBILE_NO_START);
                return false;
            }
            if (TextUtils.isEmpty(mobileNo.getText())) {
                mobileNo.setError(AppMessages.ERROR_EMPTY_FIELD);
                return false;
            }
            if (mobileNo.length() < 7) {
                mobileNo.setError(AppMessages.INVALID_MOBILE_NO_LENGTH);
                return false;
            }
        if(cnicController.getCnic().length()==0){
                dialogGeneral = popupDialogs.createAlertDialog("CNIC field must not be empty", AppMessages.ALERT_HEADING,
                        LoginPinChangeInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);

                return false;
            }
            if (!cnicController.isValidCnic()) {
                dialogGeneral = popupDialogs.createAlertDialog("CNIC length should be of 13 digits", AppMessages.ALERT_HEADING,
                        LoginPinChangeInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);


                return false;
            }
            mobileNumber = mobileId.getText() + "" + mobileNo.getText() + "";
            cnic = cnicController.getCnic();

        return true;
    }



}