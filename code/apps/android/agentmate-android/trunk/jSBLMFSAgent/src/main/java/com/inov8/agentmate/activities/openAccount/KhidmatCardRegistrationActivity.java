package com.inov8.agentmate.activities.openAccount;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.BvsDeviceSelectorActivity1;
import com.inov8.agentmate.activities.TransactionReceiptActivity;
import com.inov8.agentmate.activities.cashOut.CashOut3rdPartyConfirmationActivity;
import com.inov8.agentmate.activities.cashOut.CashOut3rdPartyInputActivity;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.KhidmatCardRegistrationModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.NadraRequestModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.SegmentModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsblmfs.R;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;

public class KhidmatCardRegistrationActivity extends BaseCommunicationActivity implements View.OnClickListener {

    private Context context;
    private ProductModel product;
    private byte flowId;
    private String[] splited = null;
    private int currCommand;
    private EditText customerMobileNumber, customerCNIC, customerCardNumber;
    private String mobileNumber, biometricFlow, encMPIN;
    private String responseMobileNumber, responseCNIC, responseCardNumber, responseSegmentId, nadraSessionId = null;
    private TextView lblHeading;
    private CheckBox cnicSeen;
    private Button btnNext;
    private Spinner spinnerSegment;
    ArrayAdapter<String> segmentsAdapter;
    private ArrayList<String> fingers;
    private String fileTemplate = "";
    private String fileTemplateType = "";
    private String strFingerIndex = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khidmat_card_registration);

        fetchIntents();
        setUI();

        headerImplementation();

        context = this;
    }

    @Override
    public void onClick(View view) {

        if (validate()) {
            currCommand = Constants.CMD_KHIDMAT_CARD_REGISTRATION;
            processRequest();
//            askMpin(null, null, false);
            //  decideBvs();
        }
    }

    @Override
    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        encMPIN = getEncryptedMpin();
        currCommand = Constants.CMD_KHIDMAT_CARD_CONFIRMATION;
        processRequest();
        //  decideBvs();
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
            if (currCommand == Constants.CMD_KHIDMAT_CARD_REGISTRATION) {
                new HttpAsyncTask(this).execute(Constants.CMD_KHIDMAT_CARD_REGISTRATION + "",
                        customerMobileNumber.getText().toString(), customerCNIC.getText().toString(), customerCardNumber.getText().toString(),
                        ApplicationData.listSegments.get(spinnerSegment.getSelectedItemPosition()).getCode());
            } else if (currCommand == Constants.CMD_KHIDMAT_CARD_CONFIRMATION) {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_KHIDMAT_CARD_CONFIRMATION + "", encMPIN, strFingerIndex, fileTemplate, fileTemplateType, getEncryptedOtp(),
                        responseMobileNumber, responseCNIC, responseCardNumber, responseSegmentId, product.getId(), nadraSessionId != null ? nadraSessionId : "");
            } else if (currCommand == Constants.CMD_FETCH_SEGMENTS) {
                new HttpAsyncTask(KhidmatCardRegistrationActivity.this).execute(Constants.CMD_FETCH_SEGMENTS + "");
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
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                final String errorCode = message.getCode();
                String description;
                if (message.getNadraSessionId() != null && !message.getNadraSessionId().equals("") && (!message.getNadraSessionId().contains("null")
                        || !message.getNadraSessionId().contains("NULL")))
                    nadraSessionId = message.getNadraSessionId();
                if (message.getCode().equals("122") || message.getCode().equals("121") || message.getCode().equals("118")) {
                    if (message.getDescr().contains(",")) {
                        splited = message.getDescr().split(",");
                        description = splited[0];
                    } else {
                        description = message.getDescr();

                    }
                    if (message.getCode().equals("118")) {

                        String currentFinger = ApplicationData.currentFingerIndex;
                        for (int i = 1; splited.length > i; i++) {
                            if (splited[i].equals(currentFinger)) {
                                splited = ArrayUtils.remove(splited, i);
                                break;
                            }
                        }
                    }

                    createAlertDialog(description, Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {
                        if ((splited != null && splited.length > 1) || errorCode.equals("122")) {
                            openBvsActivity();
                        } else {
                            goToMainMenu();
                        }
                    });

                } else {
                    createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, false, message);
                }
            } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                hideLoading();
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);
                createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> { goToMainMenu(); });
            } else {

                if (currCommand == Constants.CMD_KHIDMAT_CARD_REGISTRATION) {

                    responseMobileNumber = table.get(Constants.ATTR_CMOB).toString();
                    responseCNIC = table.get(Constants.ATTR_CNIC).toString();
                    responseCardNumber = table.get(Constants.ATTR_DCNO).toString();
                    responseSegmentId = table.get(Constants.ATTR_THIRD_PARTY_CUST_SEGMENT_CODE).toString();

                    askOtp(null, null, false, product.getId());

                }
                else if (currCommand == Constants.CMD_KHIDMAT_CARD_CONFIRMATION) {
                    createAlertDialog(table.get(Constants.ATTR_MSG).toString(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {
                        goToMainMenu();
                    });
                }
                else if (currCommand == Constants.CMD_FETCH_SEGMENTS) {
                    ApplicationData.listSegments = (ArrayList<SegmentModel>) table.get(Constants.TAG_SEGMENTS);
                    ArrayList<String> segmentsList = new ArrayList<>();
                    for (int i = 0; i < ApplicationData.listSegments.size(); i++) {
                        segmentsList.add(ApplicationData.listSegments.get(i).getName());
                    }
                    segmentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, segmentsList);
                    spinnerSegment.setAdapter(segmentsAdapter);
                }
            }
            hideLoading();
        } catch (
                Exception e) {
            hideLoading();
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
        }

        hideLoading();

    }

    @Override
    public void processNext() {

    }

    public void otpProcess(final Bundle bundle, final Class<?> nextClass) {
        decideBvs();
    }

    private void fetchIntents() {
        try {
            flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        } catch (Exception e) {

        }
    }

    private void setUI() {
        lblHeading = findViewById(R.id.lblHeading);
        lblHeading.setText("Khidmat Card Registration");

        customerMobileNumber = findViewById(R.id.input_customer_mobile);
        customerCNIC = findViewById(R.id.input_customer_cnic);
        customerCardNumber = findViewById(R.id.input_customer_card_number);
        disableCopyPaste(customerMobileNumber);
        disableCopyPaste(customerCNIC);
        disableCopyPaste(customerCardNumber);

        customerMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_MOBILE)});
        customerCNIC.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CNIC)});
        customerCardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CARD)});

        cnicSeen = findViewById(R.id.checkbox_cnic_seen);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        spinnerSegment = findViewById(R.id.spinner_segment);

        if (ApplicationData.listSegments == null) {
            currCommand = Constants.CMD_FETCH_SEGMENTS;
            processRequest();
        } else {
            ArrayList<String> segmentsList = new ArrayList<>();
            for (int i = 0; i < ApplicationData.listSegments.size(); i++) {
                segmentsList.add(ApplicationData.listSegments.get(i).getName());
            }
            segmentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, segmentsList);
            spinnerSegment.setAdapter(segmentsAdapter);
        }
    }

    protected void decideBvs() {
        Intent intent = new Intent(KhidmatCardRegistrationActivity.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }

    @Override
    public void headerImplementation() {
        btnHome = findViewById(R.id.imageViewHome);
        btnExit = findViewById(R.id.buttonsignout);

        if (!ApplicationData.isLogin) {
            btnExit.setVisibility(View.GONE);
        }
        btnHome.setOnClickListener(v -> {
            hideKeyboard(v);
            ApplicationData.webUrl = null;
            finish();
        });
        btnExit.setOnClickListener(arg0 -> showConfirmExitDialog(null));
    }

    private boolean validate() {

        boolean validate = false;
        mobileNumber = customerMobileNumber.getText().toString();

        if (!Utility.testValidity(customerMobileNumber)) {
            return validate;
        }
        if (customerMobileNumber.getText().toString().length() < 11) {
            customerMobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER);
            return validate;
        } else if ((mobileNumber.charAt(0) != '0' || mobileNumber.charAt(1) != '3')) {
            customerMobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER_FORMAT);
            return validate;
        }

        if (!Utility.testValidity(customerCNIC)) {
            return validate;
        } else if (customerCNIC.getText().toString().length() < 13) {
            customerCNIC.setError(AppMessages.INVALID_CNIC);
            return validate;
        }

        if (!cnicSeen.isChecked()) {
            Toast.makeText(this, "Please check CNIC original seen", Toast.LENGTH_SHORT).show();
            return validate;
        }

        if (!customerCardNumber.getText().toString().equals("") && customerCardNumber.getText().toString().length() < 16) {
            customerCardNumber.setError(AppMessages.INVALID_CARD_NUMBER);
            return validate;
        } else {
            validate = true;
        }

        return validate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_BIOMETRIC_SELECTION) {
            if (resultCode == RESULT_OK) {
                biometricFlow = data.getExtras().getString(Constants.IntentKeys.BIOMETRIC_FLOW);
                if (biometricFlow.equals(Constants.PAYSYS)) {
                    checkPaysysBvs();
                } else if (biometricFlow.equals(Constants.SUPREMA)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.P41M2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.P41M2;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.SUPREMA_SLIM_2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA_SLIM_2;
                    openBvsActivity();
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_BVS) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    strFingerIndex = (String) data.getExtras().get(XmlConstants.ATTR_FINGER_INDEX);
                    fileTemplate = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE);
                    fileTemplateType = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE_TYPE);
                    askMpin(null, null, false);
//                    currCommand = Constants.CMD_KHIDMAT_CARD_REGISTRATION;
//                    processRequest();
                }
            }
        }
    }

    private void openBvsActivity() {
        Intent intent = new Intent(KhidmatCardRegistrationActivity.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, mobileNumber);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, "Khidmat Card Registration");
        if (splited != null)
            intent.putExtra(XmlConstants.FINGERS, splited);
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
    }

    protected void checkPaysysBvs() {
        if (ApplicationData.isAgentAllowedBvs.equals("1")) {
            if (ApplicationData.isBvsEnabledDevice) {
                openBvsDialog();
            } else {
                if (ApplicationData.bvsErrorMessage != null)
                    PopupDialogs.alertDialog(ApplicationData.bvsErrorMessage,
                            PopupDialogs.Status.INFO, KhidmatCardRegistrationActivity.this, (v, obj) -> finish());
                else
                    PopupDialogs.alertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE,
                            PopupDialogs.Status.INFO, KhidmatCardRegistrationActivity.this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    finish();
                                }
                            });
            }
        } else {
            PopupDialogs.alertDialog(AppMessages.BVS_NOT_ENABLED,
                    PopupDialogs.Status.INFO, KhidmatCardRegistrationActivity.this, new PopupDialogs.OnCustomClickListener() {
                        @Override
                        public void onClick(View v, Object obj) {
                            finish();
                        }
                    });
        }
    }

    private void openBvsDialog() {
        fingers = Utility.getAllFingers();
        Intent intent = new Intent(KhidmatCardRegistrationActivity.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }
}
