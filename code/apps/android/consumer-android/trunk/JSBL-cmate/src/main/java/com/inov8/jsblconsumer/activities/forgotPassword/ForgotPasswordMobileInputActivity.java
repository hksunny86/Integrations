package com.inov8.jsblconsumer.activities.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AesEncryptor;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by ALI REHAN on 3/20/2018.
 */

public class ForgotPasswordMobileInputActivity extends BaseCommunicationActivity {
    private EditText mobileId, mobileNo;
    private String mobileNumber;
    private Button btnNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_mobile_input);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        headerImplementation();
        setUI();

    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    ForgotPasswordMobileInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(ForgotPasswordMobileInputActivity.this).execute(Constants.CMD_FORGOT_PASSWORD_FIRST + "", mobileNumber);
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);


                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        ForgotPasswordMobileInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
//                                if (message != null && message.getCode() != null) {
//                                    goToMainMenu();
//                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);
            } else if (table != null && table.containsKey("msgs")) {
                List<?> list = (List<?>) table.get("msgs");

                final MessageModel message = (MessageModel) list.get(0);


                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        ForgotPasswordMobileInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
//                                goToMainMenu();
                            }
                        }, false, PopupDialogs.Status.SUCCESS, false, null);
            } else {


                hideLoading();
                if (table != null) {

                    if (table.containsKey(Constants.ATTR_DTID)) {
                        Intent intent = new Intent(ForgotPasswordMobileInputActivity.this, ForgotPasswordOtpActivity.class);
                        intent.putExtra(Constants.IntentKeys.MOBILE_NO, mobileNumber);
                        startActivity(intent);
                    }
                }


            }
            hideLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processNext() {

    }

    private void setUI() {


        btnHome.setVisibility(View.GONE);
        btnSignout.setVisibility(View.GONE);

        findViewById(R.id.ivHeaderLogo).setVisibility(View.GONE);
        headerText = (TextView) findViewById(R.id.headerText);
        headerText.setVisibility(View.VISIBLE);
        headerText.setText(getString(R.string.forgot_pin));

        mobileId = (EditText) findViewById(R.id.etMobileId);
        mobileNo = (EditText) findViewById(R.id.etMobileNo);
        mobileNo.setEnabled(false);


        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);


        mobileId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() == 4) {
                    mobileNo.requestFocus();
                    mobileNo.setEnabled(true);
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
                    hideKeyboard(mobileNo);
                } else if (s.length() == 0) {
                    mobileId.requestFocus();
                    mobileId.setSelection(mobileId.getText().length());
                    mobileNo.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (validate()) {
                    processRequest();
                }
        }
    }


    private boolean validate() {
        boolean validate = false;
        mobileNumber = mobileId.getText() + "" + mobileNo.getText() + "";

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

        return true;
    }
}


