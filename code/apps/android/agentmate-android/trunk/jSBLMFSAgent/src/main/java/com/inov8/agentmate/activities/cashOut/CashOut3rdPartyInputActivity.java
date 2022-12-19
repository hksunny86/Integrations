package com.inov8.agentmate.activities.cashOut;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.BvsDeviceSelectorActivity1;
import com.inov8.agentmate.activities.LoginActivity;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.bvs.paysys.OTCClient;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.NadraRequestModel;
import com.inov8.agentmate.model.NadraResponseModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.SegmentModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PreferenceConnector;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsblmfs.R;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.RemittanceType;

import org.apache.commons.lang3.ArrayUtils;

import java.net.NetworkInterface;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_OTC;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FINGERS_EMPTY;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_OTC;
import static com.inov8.agentmate.util.Utility.testValidity;
import static com.paysyslabs.instascan.RemittanceType.MONENY_TRANSFER_RECEIVE;

public class CashOut3rdPartyInputActivity extends BaseCommunicationActivity implements LocationListener {
    private TextView lblField1, lblField2, lblHeading, lblSpinner1;
    private EditText input1, input2;
    private Button btnNext;
    private ProductModel product;
    private String amount, isCard, imei, macAddress = null;

    private ArrayList<String> fingers;
    private Handler handler;
    private boolean stop = true;
    private Runnable run;
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private ArrayList<Integer> fingerIndexes;
    private boolean fourFingersReceived = false;
    private boolean callNadraFromMicrobank = false;
    private int currCommand;
    private String strFingerIndex, nadraRequestJson,
            nadraResponseJson, biometricFlow;
    private String fileTemplate = "", template = "";
    private String fileTemplateType = "";
    private String strWSQ = "";
    private String minutiaeCount = "";
    private String NFIQuality = "";

    private String isHRA = "";

    ArrayAdapter<String> segmentsAdapter;

    private NadraResponseModel nadraResponseModel;
    private String[] splited = null;
    private int count = 0;

    private String isBvsReq = "0", nadraSessionId = null, thirdPartyTransactionId = null;

    private String locationAllowed = "";
    TransactionInfoModel transactionInfo;

    LocationManager locationManager;

    private CheckBox checkTermsAndConditions;
    private TextView termsAndConditions;
    private LinearLayout linearLayout;
    private RadioButton mobileNumber, cardNumber;
    private RadioGroup radioGroup;
    private Spinner inputSpinner1;
    private boolean storagePermission = false;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    LocationListener locationListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_general);

        try {
            fetchIntents();

            lblHeading = (TextView) findViewById(R.id.lblHeading);
            checkTermsAndConditions = (CheckBox) findViewById(R.id.checkTermsAndConditions);

            termsAndConditions = findViewById(R.id.termsHyperText);
            termsAndConditions.setClickable(true);
            termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href= " + Constants.URL_TERMS_OF_BISP + "> terms and conditions </a>";
            termsAndConditions.setText(Html.fromHtml(text));

            linearLayout = findViewById(R.id.llRadioGroup);
            radioGroup = findViewById(R.id.radioGroup);
            mobileNumber = findViewById(R.id.mobileNumber);
            cardNumber = findViewById(R.id.cardNumber);
            inputSpinner1 = findViewById(R.id.inputSpinner1);
            lblSpinner1 = findViewById(R.id.lblSpinner1);

            mobileNumber.setOnClickListener(view -> {
                lblSpinner1.setVisibility(View.VISIBLE);
                inputSpinner1.setVisibility(View.VISIBLE);
                lblField1.setText("Mobile Number");
                input1.setText("");
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_MOBILE)});
                isCard = "0";
            });

            cardNumber.setOnClickListener(view -> {
                lblSpinner1.setVisibility(View.GONE);
                inputSpinner1.setVisibility(View.GONE);
                lblField1.setText("Card Number");
                input1.setText("");
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CARD)});
                isCard = "1";
            });

            lblHeading.setText("3rd Party Cash Out");

            lblField1 = (TextView) findViewById(R.id.lblField1);
            lblField1.setVisibility(View.VISIBLE);

            input1 = (EditText) findViewById(R.id.input1);
            input1.setVisibility(View.VISIBLE);
            disableCopyPaste(input1);

            if (product.getId() != null && product.getId().equals(Constants.PRODUCT_ID_EOBI_CASH_OUT)) {
                lblField1.setText("Customer Mobile Number");
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_MOBILE)});
            } else if (product.getId() != null && product.getId().equals(Constants.PRODUCT_ID_BOP_CASH_OUT)) {
                lblHeading.setText("BOP Cash Out");
                linearLayout.setVisibility(View.VISIBLE);
                lblField1.setText("Card Number");
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CARD)});
                isCard = "1";
                if (ApplicationData.listSegments == null) {
                    currCommand = Constants.CMD_FETCH_SEGMENTS;
                    processRequest();
                } else {
                    ArrayList<String> segmentsList = new ArrayList<>();
                    for (int i = 0; i < ApplicationData.listSegments.size(); i++) {
                        segmentsList.add(ApplicationData.listSegments.get(i).getName());
                    }
                    segmentsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, segmentsList);
                    inputSpinner1.setAdapter(segmentsAdapter);
                }
//                lblSpinner1.setVisibility(View.VISIBLE);
//                inputSpinner1.setVisibility(View.VISIBLE);
            } else if (product.getId() != null && product.getId().equals(Constants.PRODUCT_ID_BOP_CASH_OUT_BY_CNIC)) {
                lblHeading.setText("BOP Cash Out");
                lblField1.setText("CNIC Number");
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CNIC)});
                isCard = "2";
            } else {
                lblField1.setText("Customer CNIC");
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CNIC)});
            }

            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField2.setText("Amount");

            input2 = (EditText) findViewById(R.id.input2);
            input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});

            if (product != null && product.getAmtRequired() != null && !product.getAmtRequired().equals("")) {
                if (product.getAmtRequired().equals("1")) {
                    lblField2.setVisibility(View.VISIBLE);
                    input2.setVisibility(View.VISIBLE);
                } else {
                    lblField2.setVisibility(View.GONE);
                    input2.setVisibility(View.GONE);
                }
            }

            if (product.getId() != null && product.getId().equals(Constants.PRODUCT_ID_3RD_PARTY_CASH_OUT)) {
                checkTermsAndConditions.setVisibility(View.VISIBLE);
                termsAndConditions.setVisibility(View.VISIBLE);
                input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                lblField2.setVisibility(View.VISIBLE);
                input2.setVisibility(View.VISIBLE);
                lblField2.setText("Mobile Number");
            } else if (product.getId() != null && (product.getId().equals(Constants.PRODUCT_ID_BOP_CASH_OUT) || product.getId().equals(Constants.PRODUCT_ID_BOP_CASH_OUT_BY_CNIC))) {
                lblField2.setVisibility(View.VISIBLE);
                input2.setVisibility(View.VISIBLE);
            } else {
                lblField2.setVisibility(View.GONE);
                input2.setVisibility(View.GONE);
            }

            disableCopyPaste(input2);

            input1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (product.getId() != null && product.getId().equals(Constants.PRODUCT_ID_EOBI_CASH_OUT)) {
                        if (charSequence.length() == Constants.MAX_LENGTH_MOBILE)
                            input2.requestFocus();
                    } else if (product.getId() != null && product.getId().equals(Constants.PRODUCT_ID_BOP_CASH_OUT)) {
                        if (charSequence.length() == Constants.MAX_LENGTH_CARD)
                            input2.requestFocus();
                } else {
                        if (charSequence.length() == Constants.MAX_LENGTH_CNIC)
                            input2.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            btnNext = findViewById(R.id.btnNext);
            btnNext.setOnClickListener(v -> {
                if (!testValidity(input1))
                    return;

                if (product.getId() != null && product.getId().equals(Constants.PRODUCT_ID_EOBI_CASH_OUT)) {
                    if (input1.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
                        input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                        return;
                    }

                    if (input1.getText().toString().charAt(0) != '0' || input1.getText().toString().charAt(1) != '3') {
                        input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                        return;
                    }
                } else if (product.getId() != null && product.getId().equals(Constants.PRODUCT_ID_BOP_CASH_OUT)) {
                    if (lblField1.getText().equals("Mobile Number")) {
                        if (input1.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
                            input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                            return;
                        }

                        if (input1.getText().toString().charAt(0) != '0' || input1.getText().toString().charAt(1) != '3') {
                            input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                            return;
                        }
                    } else {
                        if (input1.getText().toString().length() < Constants.MAX_LENGTH_CARD) {
                            input1.setError(Constants.Messages.INVALID_CARD);
                            return;
                        }
                    }
                } else {
                    if (input1.getText().toString().length() < Constants.MAX_LENGTH_CNIC) {
                        input1.setError(Constants.Messages.INVALID_CNIC);
                        return;
                    }
                }

                if (checkTermsAndConditions.getVisibility() == View.VISIBLE) {

                    if (!checkTermsAndConditions.isChecked()) {
                        createAlertDialog("Please agree to the Terms and Conditions.", Constants.KEY_LIST_ALERT, false);
                        return;
                    }

                }

                if (input2.getVisibility() == View.VISIBLE) {


                    if (product.getId() != null && !product.getId().equals(Constants.PRODUCT_ID_3RD_PARTY_CASH_OUT)) {

                        if (!testValidity(input2))
                            return;
                        amount = input2.getText() + "";

                        if (!Utility.isNull(product.getDoValidate())
                                && product.getDoValidate().equals("1")) {
                            if (product.getMaxamt() != null && !product.getMaxamt().equals("")
                                    && product.getMinamt() != null && !product.getMinamt().equals("")) {
                                if ((Integer.parseInt(input2.getText().toString()) > Double
                                        .parseDouble(Utility
                                                .getUnFormattedAmount(product
                                                        .getMaxamt())) || Integer
                                        .parseInt(input2.getText().toString()) < Double
                                        .parseDouble(Utility
                                                .getUnFormattedAmount(product
                                                        .getMinamt())))) {
                                    input2.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
                                    return;
                                }
                            }
                        } else {
                            if (amount != null && amount.length() > 0) {
                                Double amounts = Double.parseDouble(input2.getText().toString() + "");
                                if (amounts < 10 || amounts > Constants.MAX_AMOUNT) {
                                    input2.setError(Constants.Messages.AMOUNT_MAX_LIMIT);
                                    return;
                                }
                            }
                        }
                    }
                }

                hideKeyboard(v);

                macAddress = PreferenceConnector.readString(CashOut3rdPartyInputActivity.this, PreferenceConnector.MAC_ADDRESS, null);
                if (product.getId().equals(Constants.PRODUCT_ID_3RD_PARTY_CASH_OUT) && macAddress == null) {
                    if (getMacAddress() == null)
                        createAlertDialog(Constants.Messages.EXCEPTION_MAC_ADDRESS_NOT_FOUND, Constants.KEY_LIST_ALERT);
                    return;
                }

                if (isBvsReq.equals("1")) {
                    if (product.getId().equals(Constants.PRODUCT_ID_3RD_PARTY_CASH_OUT)) {
                        ApplicationData.bvsDeviceName = Constants.BvsDevices.P41M2;
                        callNadraFromMicrobank = false;
                        currCommand = Constants.CMD_3RD_PARTY_AGENT_BVS;
                        openBvsActivity();
                    } else {
                        decideBvs();
                    }
                } else if (product.getId() != null && (product.getId().equals(Constants.PRODUCT_ID_BOP_CASH_OUT) || product.getId().equals(Constants.PRODUCT_ID_BOP_CASH_OUT_BY_CNIC))) {
                    currCommand = Constants.CMD_BOP_CASH_OUT_INFO;
                    processRequest();
                } else {
                    currCommand = Constants.CMD_3RD_PARTY_CASH_OUT_INFO;
                    processRequest();
                }
            });

            addAutoKeyboardHideFunction();
        } catch (Exception ex) {
            ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();

        if (product.getId().equals(Constants.PRODUCT_ID_3RD_PARTY_CASH_OUT)) {

            checkPermissions();
        }
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(),
                    Constants.Messages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading("Please Wait", "Processing...");
        if (currCommand == Constants.CMD_CHECK_BVS) {
            new HttpAsyncTask(CashOut3rdPartyInputActivity.this).execute(
                    Constants.CMD_CHECK_BVS + "", ApplicationData.latitude, ApplicationData.longitude);
        } else if (currCommand == Constants.CMD_FETCH_SEGMENTS) {
            new HttpAsyncTask(CashOut3rdPartyInputActivity.this).execute(
                    Constants.CMD_FETCH_SEGMENTS + "");
        } else if (currCommand == Constants.CMD_BOP_CASH_OUT_INFO) {
            new HttpAsyncTask(CashOut3rdPartyInputActivity.this).execute(
                    currCommand + "", isCard.equals("0") ? input1.getText().toString() : "",
                    product.getId(), input2.getText().toString(),
                    isCard, isCard.equals("0") ? ApplicationData.listSegments.get(inputSpinner1.getSelectedItemPosition()).getCode() : "0",
                    isCard.equals("1") ? input1.getText().toString() : "", isCard.equals("2") ? input1.getText().toString() : "");

        } else if (currCommand == Constants.CMD_3RD_PARTY_AGENT_BVS) {
            if (ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.P41M2) || ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.SUPREMA_SLIM_2)) {
                if (count == 2) {
                    fileTemplateType = "WSQ";
                    fileTemplate = ApplicationData.WSQ;

                } else if (count == 3) {
                    fileTemplate = template;
                    fileTemplateType = Constants.FINGER_TEMPLATE_TYPE_ISO;
                    count = 0;
                } else {
                    fileTemplate = template;
                    fileTemplateType = Constants.FINGER_TEMPLATE_TYPE_ISO;
                }
                new HttpAsyncTask(CashOut3rdPartyInputActivity.this).execute(
                        currCommand + "", input1.getText().toString(),
                        product.getId(),
                        nadraSessionId != null ? nadraSessionId : "",
                        strFingerIndex != null ? strFingerIndex : "",
                        fileTemplateType != null ? fileTemplateType : "",
                        fileTemplate != null ? fileTemplate : "", NFIQuality, minutiaeCount, getIMEI(CashOut3rdPartyInputActivity.this));
            } else {
                new HttpAsyncTask(CashOut3rdPartyInputActivity.this).execute(
                        currCommand + "", input1.getText().toString(),
                        product.getId(),
                        nadraSessionId != null ? nadraSessionId : "",
                        strFingerIndex != null ? strFingerIndex : "",
                        fileTemplateType != null ? fileTemplateType : "",
                        fileTemplate != null ? fileTemplate : "", "", "", getIMEI(CashOut3rdPartyInputActivity.this));
            }

        } else {
            new HttpAsyncTask(CashOut3rdPartyInputActivity.this).execute(
                    Constants.CMD_3RD_PARTY_CASH_OUT_INFO + "", product.getId(),
                    input1.getText().toString(), amount != null ? amount : "", input2.getVisibility() == View.VISIBLE ? input2.getText().toString() : "", getIMEI(CashOut3rdPartyInputActivity.this) != null ? imei : "", macAddress);
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (currCommand == Constants.CMD_3RD_PARTY_AGENT_BVS)
                count++;
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                final String errorCode = message.getCode();
                if (message.getNadraSessionId() != null && !message.getNadraSessionId().equals("") && (!message.getNadraSessionId().contains("null")
                        || !message.getNadraSessionId().contains("NULL")))
                    nadraSessionId = message.getNadraSessionId();
                if (message.getThirdPartyTransactionId() != null && !message.getThirdPartyTransactionId().equals("") && (!message.getThirdPartyTransactionId().contains("null")
                        || !message.getThirdPartyTransactionId().contains("NULL")))
                    thirdPartyTransactionId = message.getThirdPartyTransactionId();
                String description;

                if (message.getCode().equals("1009")) {
                    isBvsReq = "1";




                    if (isBvsReq.equals("1")) {
                        lblHeading.setText("Agent BVS");
                        lblField1.setText("Agent CNIC");
                        lblField2.setVisibility(View.GONE);
                        input2.setVisibility(View.GONE);
                        input1.setText("");
                        checkTermsAndConditions.setVisibility(View.GONE);
                        termsAndConditions.setVisibility(View.GONE);
                    }
                }

                if (message.getCode().equals("122") || message.getCode().equals("121") || message.getCode().equals("118")) {
                    if (message.getDescr().contains(",,")) {
                        splited = message.getDescr().split(",,");
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

                    createAlertDialog(description, Constants.KEY_LIST_ALERT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if ((splited != null && splited.length > 1) || errorCode.equals("122")) {
                                openBvsActivity();
                            } else {
                                goToMainMenu();
                            }
                        }
                    });
                } else if (message.getCode().equals("135")) {
                    createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ApplicationData.errorCount == 3) {
                                ApplicationData.errorCount = 0;
                                goToMainMenu();
                            } else {
                                openBvsActivity();
                            }
                        }
                    });

                } else {
                    createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {

                        if (message.getCode().equals("9001")) {
                            goToMainMenu();
                        }
                    });
                    if (ApplicationData.errorCount == 3) {
                        ApplicationData.errorCount = 0;
                    }
                }
            } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                hideLoading();
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);

                createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {
                    if (currCommand == Constants.CMD_CHECK_BVS) {
                        lblHeading.setText("3rd Party Cash Out");
                        input1.setText("Customer CNIC");
                        lblField2.setVisibility(View.VISIBLE);
                        input2.setVisibility(View.VISIBLE);
                        checkTermsAndConditions.setVisibility(View.VISIBLE);
                        termsAndConditions.setVisibility(View.VISIBLE);
                        lblField2.setText("Mobile Number");
                    } else {
                        goToMainMenu();
                    }
                });
            } else {

                if (currCommand == Constants.CMD_CHECK_BVS) {

                    if (table.get("IS_VALID_LOCATION") != null) {
                        locationAllowed = table.get("IS_VALID_LOCATION").toString();

                        if (locationAllowed.equals("0")) {

                            createAlertDialog("You are not currently at the authorized location to conduct this transaction, please change your location and try again", Constants.KEY_LIST_ALERT, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    goToMainMenu();

                                }
                            });
                            return;
                        }
                    }
                    if (table.get("IS_BVS_REQ") != null) {
                        isBvsReq = table.get("IS_BVS_REQ").toString();

                        if (isBvsReq.equals("1")) {
                            lblHeading.setText("Agent BVS");
                            lblField1.setText("Agent CNIC");
                            lblField2.setVisibility(View.GONE);
                            input2.setVisibility(View.GONE);
                            checkTermsAndConditions.setVisibility(View.GONE);
                            termsAndConditions.setVisibility(View.GONE);
                        }
                    }

                } else if (currCommand == Constants.CMD_FETCH_SEGMENTS) {
                    ApplicationData.listSegments = (ArrayList<SegmentModel>) table.get(Constants.TAG_SEGMENTS);
                    ArrayList<String> segmentsList = new ArrayList<>();
                    for (int i = 0; i < ApplicationData.listSegments.size(); i++) {
                        segmentsList.add(ApplicationData.listSegments.get(i).getName());
                    }
                    segmentsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, segmentsList);
                    inputSpinner1.setAdapter(segmentsAdapter);

                } else if (currCommand == Constants.CMD_BOP_CASH_OUT_INFO) {

                    transactionInfo = TransactionInfoModel.getInstance();

                    transactionInfo.thirdPartyCashOut(table.get(Constants.ATTR_CMOB).toString(),
                            table.get(Constants.ATTR_TXID).toString(), table.get(Constants.ATTR_IS_CARD).toString(),
                            table.get(Constants.ATTR_IS_BVS_REQUIRED).toString(),
                            table.get(Constants.ATTR_CAMT).toString(),
                            table.get(Constants.ATTR_CAMTF).toString(),
                            table.get(Constants.ATTR_TPAM).toString(),
                            table.get(Constants.ATTR_TPAMF).toString(),
                            table.get(Constants.ATTR_TAMT).toString(),
                            table.get(Constants.ATTR_TAMTF).toString(),
                            table.get(Constants.ATTR_TXAM).toString(),
                            table.get(Constants.ATTR_TXAMF).toString(),
                            table.get(Constants.ATTR_DCNO).toString(),
                            table.get(Constants.ATTR_THIRD_PARTY_CUST_SEGMENT_CODE).toString(),
                            table.get(Constants.ATTR_CNIC).toString());
                    transactionInfo.setIsOtpRequired("0");

                    Intent intent = new Intent(getApplicationContext(), CashOut3rdPartyConfirmationActivity.class);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactionInfo);
                    intent.putExtras(mBundle);
                    startActivity(intent);

                } else {

                    transactionInfo = TransactionInfoModel.getInstance();

                    if (table.containsKey(Constants.ATTR_IS_OTP_REQUIRED)) {
                        if (table.get(Constants.ATTR_IS_OTP_REQUIRED) != null &&
                                !table.get(Constants.ATTR_IS_OTP_REQUIRED).equals("")) {
                            if (table.get(Constants.ATTR_IS_OTP_REQUIRED).equals("1")) {
                                transactionInfo.setIsOtpRequired("1");
                            } else
                                transactionInfo.setIsOtpRequired("0");
                        }
                    } else
                        transactionInfo.setIsOtpRequired("0");

                    transactionInfo.thirdPartyCashOut(table.get(Constants.ATTR_CMOB).toString(),
                            table.get(Constants.ATTR_ACC_NO).toString(),
                            table.get(Constants.ATTR_COREACTL).toString(),
                            table.get(Constants.ATTR_IS_BVS_REQUIRED).toString(),
                            table.get(Constants.ATTR_CAMT).toString(),
                            table.get(Constants.ATTR_CAMTF).toString(),
                            table.get(Constants.ATTR_TPAM).toString(),
                            table.get(Constants.ATTR_TPAMF).toString(),
                            table.get(Constants.ATTR_TAMT).toString(),
                            table.get(Constants.ATTR_TAMTF).toString(),
                            table.get(Constants.ATTR_TXAM).toString(),
                            table.get(Constants.ATTR_TXAMF).toString(),
                            table.get(Constants.ATTR_3RD_PARTY_SESSION_ID).toString(),
                            input1.getText().toString());
                    transactionInfo.setIsWalletExist(table.get("IS_BAFL_WALLET_EXISTS").toString());
                    transactionInfo.setWalletNumber(table.get("BAFL_WALLET_ACCOUNT_ID").toString());
                    transactionInfo.setWalletBal(table.get("BAFL_WALLET_BALANCE").toString());

                    if (table.get("FINGER_INDEX") != null)
                        transactionInfo.setFingerIndex(table.get("FINGER_INDEX").toString());
                    else {
                        transactionInfo.setFingerIndex("0");
                    }

                    Intent intent = new Intent(getApplicationContext(), CashOut3rdPartyConfirmationActivity.class);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactionInfo);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
        }
        hideLoading();
    }

    @Override
    public void processNext() {
    }

    private void fetchIntents() {
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_OTC) {

            if (resultCode == RESULT_CODE_SUCCESS_OTC) {
                fourFingersReceived = false;
                exhaustedFingers.clear();
                currCommand = Constants.CMD_3RD_PARTY_AGENT_BVS;
                processRequest();
            } else if (resultCode == RESULT_CODE_FAILED_OTC_FINGER_INDEXES) {
                if (data != null) {
                    fourFingersReceived = true;
                    fingers = data.getStringArrayListExtra("fingers");
                    fingerIndexes = data.getIntegerArrayListExtra("validFingerIndexes");
                    final String errorCode = data.getStringExtra("code");

                    if (exhaustedFingers.size() == 0) {
                        Intent intent = new Intent(CashOut3rdPartyInputActivity.this, FingerScanActivity.class);
                        intent.putStringArrayListExtra("fingers", fingers);
                        intent.putExtra("validFingerIndexes", fingerIndexes);
                        intent.putExtra("code", errorCode);
                        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
                    } else {
                        for (int i = 0; i < exhaustedFingers.size(); i++) {
                            for (int j = 0; j < fingers.size(); j++) {
                                if (fingers.get(j).equals(exhaustedFingers.get(i))) {
                                    fingers.remove(j);
                                    break;
                                }
                            }
                        }
                        Intent intent = new Intent(CashOut3rdPartyInputActivity.this, FingerScanActivity.class);
                        intent.putStringArrayListExtra("fingers", fingers);
                        intent.putExtra("validFingerIndexes", fingerIndexes);
                        intent.putExtra("code", errorCode);
                        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
                    }
                }
            } else if (resultCode == RESULT_CODE_FAILED_OTC_OTHER_ERROR) {
                if (data != null) {
                    final String errorCode = data.getStringExtra("code");

                    if (errorCode.equals("118")) {
                        if (fourFingersReceived) {
                            Fingers currentFinger = ApplicationData.currentFinger;
                            for (int i = 0; i < fingers.size(); i++) {
                                if (fingers.get(i).equals(currentFinger.getValue())) {
                                    exhaustedFingers.add(exhaustedFingers.size(), fingers.get(i));
                                    fingers.remove(i);
                                    break;
                                }
                            }

                            Intent intent = new Intent(CashOut3rdPartyInputActivity.this, FingerScanActivity.class);
                            intent.putStringArrayListExtra("fingers", fingers);
                            intent.putExtra("validFingerIndexes", fingerIndexes);
                            intent.putExtra("code", errorCode);
                            startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
                        } else {
                            openBvsDialog();
                        }
                    } else {
                        createAlertDialog(data.getStringExtra("msg"),
                                Constants.Messages.ALERT_ERROR,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        if (fourFingersReceived) {
                                            Intent intent = new Intent(CashOut3rdPartyInputActivity.this, FingerScanActivity.class);
                                            intent.putStringArrayListExtra("fingers", fingers);
                                            intent.putExtra("validFingerIndexes", fingerIndexes);
                                            intent.putExtra("code", errorCode);
                                            startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
                                        } else {
                                            openBvsDialog();
                                        }
                                    }
                                });
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_FINGER_SCAN) {
            if (resultCode == RESULT_OK) {
                if (!haveInternet()) {
                    Toast.makeText(getApplication(), AppMessages.INTERNET_CONNECTION_PROBLEM,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(CashOut3rdPartyInputActivity.this, OTCClient.class);
                Bundle extras = data.getExtras();
                Fingers finger = null;
                if (extras != null) {
                    finger = (Fingers) extras.get(Constants.IntentKeys.FINGER);
                }
                intent.putExtra(Constants.IntentKeys.FINGER, finger);
                Utility.setAgentAreaName(intent);
                intent.putExtra(Constants.IntentKeys.IDENTIFIER, input1.getText().toString() != null ? input1.getText().toString() : "");
                intent.putExtra(Constants.IntentKeys.REMITTANCE_AMOUNT, "");
                intent.putExtra(Constants.IntentKeys.REMITTANCE_TYPE, (RemittanceType) MONENY_TRANSFER_RECEIVE);
                intent.putExtra(Constants.IntentKeys.CONTACT_NUMBER, ApplicationData.agentMobileNumber != null ? ApplicationData.agentMobileNumber : "");
                intent.putExtra(Constants.IntentKeys.SECONDARY_IDENTIFIER, input1.getText().toString());
                intent.putExtra(Constants.IntentKeys.SECONDARY_CONTACT_NUMBER, "");
                intent.putExtra(Constants.IntentKeys.ACCOUNT_NUMBER, "");
                startActivityForResult(intent, REQUEST_CODE_OTC);
            } else if (resultCode == RESULT_CODE_FINGERS_EMPTY) {
                exhaustedFingers.clear();
                fourFingersReceived = false;
                openBvsDialog();
            }
        } else if (requestCode == Constants.REQUEST_CODE_BIOMETRIC_SELECTION) {
            if (resultCode == RESULT_OK) {
                biometricFlow = data.getExtras().getString(Constants.IntentKeys.BIOMETRIC_FLOW);
                if (biometricFlow.equals(Constants.PAYSYS)) {
                    openBvsDialog();
                } else if (biometricFlow.equals(Constants.SUPREMA)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA;
                    callNadraFromMicrobank = true;
                    currCommand = Constants.CMD_VERIFY_NADRA_FINGERPRINT;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.P41M2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.P41M2;
                    callNadraFromMicrobank = false;
                    currCommand = Constants.CMD_3RD_PARTY_AGENT_BVS;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.SUPREMA_SLIM_2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA_SLIM_2;
                    callNadraFromMicrobank = false;
                    currCommand = Constants.CMD_3RD_PARTY_AGENT_BVS;
                    openBvsActivity();
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_BVS) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    strFingerIndex = (String) data.getExtras().get(XmlConstants.ATTR_FINGER_INDEX);
                    fileTemplate = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE);
                    template = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE);
                    fileTemplateType = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE_TYPE);
                    if (ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.P41M2) || ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.SUPREMA_SLIM_2)) {
                        strWSQ = ApplicationData.WSQ;
                        minutiaeCount = data.getStringExtra(XmlConstants.ATTR_MINUTIAE_COUNT);
                        NFIQuality = data.getStringExtra(XmlConstants.ATTR_NFI_QUALITY);
                    }
                    if (callNadraFromMicrobank) {
                        currCommand = Constants.CMD_3RD_PARTY_AGENT_BVS;
                        processRequest();
                    } else if (currCommand == Constants.CMD_3RD_PARTY_AGENT_BVS) {
                        processRequest();
                    } else {

                        NadraRequestModel requestModel = new NadraRequestModel();
                        requestModel.setFingerIndex(strFingerIndex);
                        requestModel.setTemplateType(fileTemplateType);
                        requestModel.setFingerTemplate(fileTemplate);
                        requestModel.setCitizenNumber("");
                        requestModel.setContactNumber(ApplicationData.agentMobileNumber);
                        requestModel.setSecondaryCitizenNumber("");
                        requestModel.setSecondaryContactNumber(ApplicationData.agentMobileNumber);
                        requestModel.setMobileBankAccountNumber("");
                        requestModel.setRemittanceType("MONENY_TRANSFER_RECEIVE");
                        requestModel.setRemittanceAmount("");
                        requestModel.setAreaName("Punjab");

                        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                        nadraRequestJson = gson.toJson(requestModel);

                        currCommand = Constants.CMD_VERIFY_NADRA_FINGERPRINT;
                        processRequest();
                    }
                }
            }
        }
    }

    private void openBvsDialog() {
        fingers = Utility.getAllFingers();
        Intent intent = new Intent(CashOut3rdPartyInputActivity.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }

    protected void decideBvs() {
        Intent intent = new Intent(CashOut3rdPartyInputActivity.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }

    private void openBvsActivity() {
        Intent intent = new Intent(CashOut3rdPartyInputActivity.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, ApplicationData.agentMobileNumber);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, "Cash Out");
        if (splited != null)
            intent.putExtra(XmlConstants.FINGERS, splited);
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
    }

    private void setupLocationUpdates() {

        showLoading("Please Wait", "fetching your location");

        handler = new Handler();
        handler.postDelayed(run = () -> {

            if (stop) {
                locationManager.removeUpdates(locationListener);
                hideLoading();
                loadingDialog = null;
                createAlertDialog("Unable to fetch your location, please change your location and try again.", Constants.KEY_LIST_ALERT, (dialogInterface, i) -> goToMainMenu());
            }
        }, 60000);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0f,
                this
        );

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0f,
                this
        );
        locationListener = this;

    }

    @Override
    public void onLocationChanged(Location location) {
        hideLoading();
        loadingDialog = null;
        if (stop) {
            if (location != null) {
                ApplicationData.latitude = String.valueOf(location.getLatitude());
                ApplicationData.longitude = String.valueOf(location.getLongitude());
                locationManager.removeUpdates(this);
                currCommand = Constants.CMD_CHECK_BVS;
                handler.removeCallbacks(run);
                stop = false;
                processRequest();
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private void checkPermissions() {

        if (Build.VERSION.SDK_INT < 23) {
            storagePermission = true;
            return;
        }

        int hasStoragePermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);

        if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        } else {
            if (product.getId().equals(Constants.PRODUCT_ID_3RD_PARTY_CASH_OUT)) {

                setupLocationUpdates();

//                ApplicationData.latitude = String.valueOf("31.5250023");
//                ApplicationData.longitude = String.valueOf("74.3475984");
//                currCommand = Constants.CMD_CHECK_BVS;
//                processRequest();

            }
            storagePermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {

                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.READ_PHONE_STATE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            storagePermission = true;


                            if (product.getId().equals(Constants.PRODUCT_ID_3RD_PARTY_CASH_OUT)) {
                                setupLocationUpdates();

//                                ApplicationData.latitude = String.valueOf("10.51277167");
//                                ApplicationData.longitude = String.valueOf("38.841655");
//                                currCommand = Constants.CMD_CHECK_BVS;
//                                processRequest();

                            }

//                            new DownloadFile().execute(Constants.PROTOCOL + Constants.BASE_URL + "/accountstatement/" + pdfUrl, pdfUrl);
                        } else {

                            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {


                                createAlertDialog("Unable to fetch device IMEI, please contact your service provider", Constants.KEY_LIST_ALERT, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        goToMainMenu();

                                    }
                                });


                            } else {


                                createAlertDialog("Unable to fetch device IMEI, please contact your service provider", Constants.KEY_LIST_ALERT, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        goToMainMenu();
                                    }
                                });

//                                dialogGeneral = PopupDialogs.createAlertDialog("Please allow storage permissions from app settings to proceed further.",
//                                        AppMessages.HEADING_ALERT,
//                                        AccountStatementFullListActivity.this, new OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                dialogGeneral.dismiss();
//                                                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
//                                                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
//                                                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                startActivity(myAppSettings);
//                                            }
//                                        }, PopupDialogs.Status.ALERT);

                            }
                        }
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        imei = telephonyManager.getDeviceId();
        ApplicationData.imei = imei;
        return imei;
    }

    public String getMacAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    macAddress = null;
                    return macAddress;
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02x:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                macAddress = buf.toString();
                PreferenceConnector.writeString(CashOut3rdPartyInputActivity.this, PreferenceConnector.MAC_ADDRESS, macAddress);
                return macAddress;
            }
        } catch (Exception ex) {

        }
        macAddress = null;
        return macAddress;
    }
}