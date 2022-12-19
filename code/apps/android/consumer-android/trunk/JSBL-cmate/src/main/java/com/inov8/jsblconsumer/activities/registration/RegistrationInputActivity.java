package com.inov8.jsblconsumer.activities.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class RegistrationInputActivity extends BaseCommunicationActivity {
    private TextView lblField1, lblField3;
    private EditText mobileNo, cnic_1, cnic_2, cnic_3;
    private Button btnNext, btnCancel;
    private TextView lblHeading;
    private String txtMobileNo, txtTotalCnic, mobileNum, cnic, zongSubs, bvsVerf, regCnic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_register);

        titleImplementation("icon_customer_registration", "Customer Registration", null, this);

        lblField1 = (TextView) findViewById(R.id.lblField1);
        lblField1.setText("Mobile Number");
        lblField1.setVisibility(View.VISIBLE);

        mobileNo = (EditText) findViewById(R.id.input1);
        mobileNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        mobileNo.setVisibility(View.VISIBLE);

        lblField3 = (TextView) findViewById(R.id.lblField3);
        lblField3.setText("CNIC");
        lblField3.setVisibility(View.VISIBLE);

        cnic_1 = (EditText) findViewById(R.id.input3_1);
        cnic_1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        cnic_1.setVisibility(View.VISIBLE);

        cnic_2 = (EditText) findViewById(R.id.input3_2);
        cnic_2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        cnic_2.setVisibility(View.VISIBLE);

        cnic_3 = (EditText) findViewById(R.id.input3_3);
        cnic_3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        cnic_3.setVisibility(View.VISIBLE);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        cnic_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 5) {
                    cnic_2.requestFocus();
                }
                if (s.length() < 5) {
                    cnic_1.requestFocus();
                }
            }
        });

        cnic_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 7) {
                    cnic_3.requestFocus();
                }
                if (s.length() < 7) {
                    cnic_2.requestFocus();
                }
            }
        });

        cnic_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    cnic_3.requestFocus();
                }
                if (s.length() < 1) {
                    cnic_3.requestFocus();
                }
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputManager.isAcceptingText())
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                // verifying mobile no.
                if (TextUtils.isEmpty(mobileNo.getText())) {
                    mobileNo.setError(AppMessages.ERROR_EMPTY_FIELD);
                    return;
                }

                if (TextUtils.isEmpty(cnic_1.getText())) {
                    cnic_1.setError(AppMessages.ERROR_EMPTY_FIELD);
                    return;
                }
                if (TextUtils.isEmpty(cnic_2.getText())) {
                    cnic_2.setError(AppMessages.ERROR_EMPTY_FIELD);
                    return;
                }
                if (TextUtils.isEmpty(cnic_3.getText())) {
                    cnic_3.setError(AppMessages.ERROR_EMPTY_FIELD);
                    return;
                }

                txtMobileNo = mobileNo.getText().toString();
                if (txtMobileNo.length() < 11) {
                    mobileNo.setError(AppMessages.ERROR_MOBILE_NO_LENGTH);
                    return;
                }
                if (mobileNo.getText().charAt(0) != '0' || mobileNo.getText().charAt(1) != '3') {
                    mobileNo.setError(AppMessages.ERROR_MOBILE_NO_START);
                    return;
                }

                txtTotalCnic = cnic_1.getText().toString() + cnic_2.getText().toString() + cnic_3.getText().toString();

                if (txtTotalCnic.length() < 13) {
                    cnic_3.setError(AppMessages.ERROR_CNIC_LENGTH);
                    return;
                }

                processRequest();
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationInputActivity.this.finish();
            }
        });

        headerImplementation();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, RegistrationInputActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        try {
            showLoading("Please Wait", "Processing...");
            new HttpAsyncTask(RegistrationInputActivity.this).execute(Constants.CMD_CUSTOMER_REGISTRATION + "", txtMobileNo, txtTotalCnic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey("errs")) {
                List<?> list = (List<?>) table.get("errs");
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                PopupDialogs.createAlertDialog(message.getDescr(),
                        getString(R.string.alertNotification), RegistrationInputActivity.this, PopupDialogs.Status.ERROR);

                String code = message.getCode();
                AppLogger.i("##Error Code: " + code);
            } else {
                hideLoading();
                if (table != null) {
                    zongSubs = table.get(XmlConstants.Attributes.IS_ZONG_SUBS).toString();
                    bvsVerf = table.get(XmlConstants.Attributes.IS_BVS_VERF).toString();
                    regCnic = table.get(XmlConstants.Attributes.IS_CNIC_REG).toString();
                    mobileNum = table.get(XmlConstants.Attributes.MOBN).toString();
                    cnic = table.get(XmlConstants.Attributes.CNIC).toString();
                    processNext();
                }
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
    }

    @Override
    public void processNext() {
        Intent intent = new Intent(RegistrationInputActivity.this, TermsConditionsActivity.class);
        intent.putExtra(XmlConstants.Attributes.IS_ZONG_SUBS, zongSubs);
        intent.putExtra(XmlConstants.Attributes.IS_BVS_VERF, bvsVerf);
        intent.putExtra(XmlConstants.Attributes.IS_CNIC_REG, regCnic);
        intent.putExtra(XmlConstants.Attributes.MOBN, mobileNum);
        intent.putExtra(XmlConstants.Attributes.CNIC, cnic);
        startActivity(intent);
        hideLoading();
//        resetFields();
    }

    private void resetFields() {
        mobileNo.setText("");
        cnic_1.setText("");
        cnic_2.setText("");
        cnic_3.setText("");
    }
}