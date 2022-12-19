package com.inov8.agentmate.activities.dormancy;

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.debitCard.TermsConditionsDebitCardActivity;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.model.CategoryModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.SegmentModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AesEncryptor;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsblmfs.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class DormancyActivity extends BaseCommunicationActivity implements View.OnClickListener {


    private EditText customerMobileNumber, customerCNIC;
    private Button btnNext;
    private TextView lblHeading;
    private String cnic, mobileNumber;
    private int currentCommand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormancy);
        btnNext = findViewById(R.id.btnNext);
        lblHeading = findViewById(R.id.lblHeading);
        customerMobileNumber = findViewById(R.id.input_customer_mobile);
        customerCNIC = findViewById(R.id.input_customer_cnic);


        lblHeading.setText("Dormancy Removal");
        btnNext.setOnClickListener(this);
        headerImplementation();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnNext:
                hideKeyboard(v);

                if (validate()) {
                    mobileNumber = customerMobileNumber.getText().toString();
                    cnic = customerCNIC.getText().toString();
                    currentCommand = Constants.CMD_DORMANCY;
                    processRequest();
                }

        }
    }


    @Override
    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(), AppMessages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            showLoading("Please Wait", "Processing...");
            if (currentCommand == Constants.CMD_DORMANCY) {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_DORMANCY + "", mobileNumber, cnic);
            }
        } catch (Exception e) {
            hideLoading();
            createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
            e.printStackTrace();
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {

            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            hideLoading();
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                String code = message.getCode();
                AppLogger.i("##Error Code: " + code);
                PopupDialogs.createAlertDialog(message.getDescr(),
                        AppMessages.ALERT_HEADING, PopupDialogs.Status.ERROR,
                        DormancyActivity.this, new PopupDialogs.OnCustomClickListener() {
                            @Override
                            public void onClick(View v, Object obj) {
                            }
                        });
            } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                hideLoading();
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);
                PopupDialogs.createAlertDialog(message.getDescr(), "Message",
                        PopupDialogs.Status.INFO, this, new PopupDialogs.OnCustomClickListener() {
                            @Override
                            public void onClick(View v, Object obj) {
                                goToMainMenu();
                            }
                        });
            } else {
                cnic = (String) table.get(Constants.ATTR_CNIC);
                mobileNumber = (String) table.get(Constants.ATTR_CMOB);
                processNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void processNext() {
//        currentCommand = Constants.CMD_DEBIT_CARD_FETCH_SEGMENTS;
//        processRequest();
        Intent intent = new Intent(DormancyActivity.this, DormancyConfirmationActivity.class);
        intent.putExtra(Constants.IntentKeys.CNIC, cnic);
        intent.putExtra(Constants.IntentKeys.CMOB, mobileNumber);
        startActivity(intent);

    }

    @Override
    public void headerImplementation() {
        btnHome = findViewById(R.id.imageViewHome);
        btnExit = findViewById(R.id.buttonsignout);

        if (!ApplicationData.isLogin) {
            btnExit.setVisibility(View.GONE);
        }
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                ApplicationData.webUrl = null;
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showConfirmExitDialog(null);
            }
        });
    }


    private boolean validate() {
        boolean validate = false;

        if (!Utility.testValidity(customerMobileNumber)) {
            return validate;
        }
        if (customerMobileNumber.getText().toString().length() < 11) {
            customerMobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER);
            return validate;
        } else if ((customerMobileNumber.getText().charAt(0) != '0' || customerMobileNumber.getText().charAt(1) != '3')) {
            customerMobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER_FORMAT);
            return validate;
        }
        if (!Utility.testValidity(customerCNIC)) {
            return validate;
        } else if (customerCNIC.getText().toString().length() < 13) {
            customerCNIC.setError(AppMessages.INVALID_CNIC);
            return validate;
        } else {
            validate = true;
        }

        return validate;
    }

    @Override
    public void onBackPressed() {
        goToMainMenu();
    }
}