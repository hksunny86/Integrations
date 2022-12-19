package com.inov8.agentmate.activities.hra;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.BvsDeviceSelectorActivity1;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.bvs.paysys.NadraRestClient;
import com.inov8.agentmate.model.HraRegistrationModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.NadraRequestModel;
import com.inov8.agentmate.model.NadraResponseModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsbl.sco.R;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.model.PersonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_HRA;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_VERIFY_NADRA;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_NADRA_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_NADRA_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FINGERS_EMPTY;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_NADRA;

public class HraRegistrationActivity1 extends BaseCommunicationActivity implements View.OnClickListener {

    private EditText customerMobileNumber, customerCNIC,
             occupation;
    private Button btnSearch, btnCancel;
    private TextView lblHeading;
    private ProductModel product;
    private String cnic, mobileNumber, mobileNetwork, amount;
    private Bundle bundle = new Bundle();
    private LinearLayout responseList;
    private ListViewExpanded dataList;
    private int currentCommand;
    private Spinner accountPurpose, incomeSrc;
    private String[] accountPurposes = {"Personal", "Business"};
    private String[] sourceOfIncome = {"Family Support", "Freelancer"};
    private String labels[], data[];
    private boolean discrepantCustomer = false, responseReceived = false;
    private String cusRegState, cusRegStateId;
    private PersonData nadraPersonData;
    private NadraResponseModel nadraResponseModel;
    private ArrayList<String> fingers;
    private boolean isCashWithDraw = false;
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private ArrayList<Integer> fingerIndexes;
    private boolean fourFingersReceived = false;
    private int attemptsCounter = 0;
    private String strErrorCode, strMessage = null;
    private String fileTemplate = "", hraLinkedRequest = "";
    private String fileTemplateType = "";
    private String strFingerIndex, nadraRequestJson,
            nadraResponseJson, requestType, biometricFlow, name, fatherName, dob;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hra_registration1);

        fetchIntents();
        setUI();
        headerImplementation();
    }

    private void fetchIntents() {
        try {
            cnic = getIntent().getExtras().getString(Constants.IntentKeys.CNIC);
            mobileNumber = getIntent().getExtras().getString(Constants.IntentKeys.MOBILE);
            isCashWithDraw = getIntent().getExtras().getBoolean(Constants.IntentKeys.IS_CASH_WITHDRAW);
            mobileNetwork = getIntent().getExtras().getString(Constants.ATTR_MOBILE_NETWORK);
            amount = getIntent().getExtras().getString(Constants.IntentKeys.TRANSACTION_AMOUNT);
            hraLinkedRequest = getIntent().getExtras().getString(Constants.IntentKeys.HRA_LINKED_REQUEST);
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        } catch (Exception e) {
        }
    }

    private void setUI() {
        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText("HRA Registration");

        customerMobileNumber = (EditText) findViewById(R.id.input_customer_mobile);
        disableCopyPaste(customerMobileNumber);

        customerCNIC = (EditText) findViewById(R.id.input_customer_cnic);
        disableCopyPaste(customerCNIC);

        if (isCashWithDraw) {
            customerMobileNumber.setText(mobileNumber);
            customerCNIC.setText(cnic);
            customerMobileNumber.setEnabled(false);
            customerCNIC.setEnabled(false);
        }

        customerMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == Constants.MAX_LENGTH_MOBILE)
                    customerCNIC.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        customerCNIC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == Constants.MAX_LENGTH_CNIC)
                    hideKeyboard(getCurrentFocus());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        incomeSrc = (EditText) findViewById(R.id.inputIncomeSource);
//        disableCopyPaste(incomeSrc);

        occupation = (EditText) findViewById(R.id.inputOccupation);
        disableCopyPaste(occupation);

        responseList = (LinearLayout) findViewById(R.id.responseDataList);
        dataList = (ListViewExpanded) findViewById(R.id.dataList);

        accountPurpose = (Spinner) findViewById(R.id.spinnerAccountPurpose);
        incomeSrc = (Spinner) findViewById(R.id.inputIncomeSource);

        ArrayAdapter<String> incomeSrcAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sourceOfIncome);
        incomeSrcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomeSrc.setAdapter(incomeSrcAdapter);
        incomeSrc.setSelection(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accountPurposes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountPurpose.setAdapter(adapter);
        accountPurpose.setSelection(0);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnSearch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                if (!validate())
                    return;
                else if (!responseReceived) {
                    currentCommand = Constants.CMD_INFO_L1_TO_HRA;
                    processRequest();
                } else {

                    if (!Utility.testValidity(occupation)) {
                        return;
                    }
                    HraRegistrationModel hraModel = new HraRegistrationModel();
                    hraModel.setCnic(customerCNIC.getText().toString());
                    hraModel.setMob(customerMobileNumber.getText().toString());
                    hraModel.setName(name != null ? name : "");
                    hraModel.setFatherName(fatherName != null ? fatherName : "");
                    hraModel.setDob(dob != null ? dob : "");
                    hraModel.setIncomeSource(incomeSrc.getSelectedItem().toString());
                    hraModel.setOccupation(occupation.getText().toString());
                    hraModel.setAccountPurpose(accountPurpose.getSelectedItem().toString());

                    Intent intent = new Intent(HraRegistrationActivity1.this, HraRegistrationActivity2.class);
                    intent.putExtra(Constants.IntentKeys.HRA_MODEL, hraModel);
                    intent.putExtra(Constants.IntentKeys.IS_CASH_WITHDRAW,isCashWithDraw);
                    intent.putExtra(Constants.ATTR_MOBILE_NETWORK, mobileNetwork);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT, amount);
                    intent.putExtra(Constants.IntentKeys.HRA_LINKED_REQUEST, hraLinkedRequest);
                    mBundle.putSerializable(Constants.IntentKeys.PRODUCT_MODEL, product);
                    if (bundle != null) {
                        intent.putExtras(mBundle);
                    }
                    startActivity(intent);
                }
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    protected void decideBvs() {
        Intent intent = new Intent(HraRegistrationActivity1.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }

    protected void checkPaysysBvs() {
        if (ApplicationData.isAgentAllowedBvs.equals("1")) {
            if (ApplicationData.isBvsEnabledDevice) {
                openBvsDialog();
            } else {
                if (ApplicationData.bvsErrorMessage != null)
                    PopupDialogs.alertDialog(ApplicationData.bvsErrorMessage,
                            PopupDialogs.Status.INFO, HraRegistrationActivity1.this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    finish();
                                }
                            });
                else
                    PopupDialogs.alertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE,
                            PopupDialogs.Status.INFO, HraRegistrationActivity1.this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    finish();
                                }
                            });
            }
        } else {
            PopupDialogs.alertDialog(AppMessages.BVS_NOT_ENABLED,
                    PopupDialogs.Status.INFO, HraRegistrationActivity1.this, new PopupDialogs.OnCustomClickListener() {
                        @Override
                        public void onClick(View v, Object obj) {
                            finish();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_VERIFY_NADRA) {

            if (resultCode == RESULT_CODE_SUCCESS_NADRA) {
                if (data != null) {
                    fourFingersReceived = false;
                    exhaustedFingers.clear();
                    attemptsCounter = 0;

                    String json = data.getStringExtra(XmlConstants.ATTR_PERSON_DATA);
                    nadraPersonData = new Gson().fromJson(json, PersonData.class);
                    String strCardExpired = nadraPersonData.getCardExpired();
                    if (strCardExpired != null && strCardExpired.equalsIgnoreCase("yes")) {
                        PopupDialogs.createAlertDialog(AppMessages.INVALID_CARD_EXPIRY,
                                AppMessages.ALERT_HEADING, PopupDialogs.Status.ERROR,
                                HraRegistrationActivity1.this, new PopupDialogs.OnCustomClickListener() {
                                    @Override
                                    public void onClick(View v, Object obj) {
                                        HraRegistrationActivity1.this.finish();
                                    }
                                });
                        return;
                    } else {
                        requestType = Constants.REQUEST_TYPE_PAYSYS;
                        askMpin(null, null, false);
                    }
                }
            } else if (resultCode == RESULT_CODE_FAILED_NADRA_FINGER_INDEXES) {
                if (data != null) {
                    fourFingersReceived = true;
                    fingers = data.getStringArrayListExtra("fingers");
                    fingerIndexes = data.getIntegerArrayListExtra("validFingerIndexes");
                    strErrorCode = data.getStringExtra("code");
                    strMessage = data.getStringExtra("msg");

                    attemptsCounter++;
                    if (attemptsCounter < 2)
                        handleIndexesError();
                    else {
                        handleIndexesError();
                    }
                }
            } else if (resultCode == RESULT_CODE_FAILED_NADRA_OTHER_ERROR) {
                if (data != null) {
                    strErrorCode = data.getStringExtra("code");
                    strMessage = data.getStringExtra("msg");

                    if (!haveInternet()) {
                        Toast.makeText(getApplication(), AppMessages.INTERNET_CONNECTION_PROBLEM,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!strErrorCode.equals("700"))
                        attemptsCounter++;

                    if (!strErrorCode.equals("118")) {
                        createAlertDialog(strMessage,
                                Constants.Messages.ALERT_ERROR,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        handleOtherError();
                                    }
                                });
                    } else {
                        handleOtherError();
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

                Intent intent = new Intent(HraRegistrationActivity1.this, NadraRestClient.class);
                Bundle extras = data.getExtras();
                Fingers finger = null;
                if (extras != null) {
                    finger = (Fingers) extras.get(Constants.IntentKeys.FINGER);
                }
                intent.putExtra(Constants.IntentKeys.FINGER, finger);
                intent.putExtra(Constants.IntentKeys.IDENTIFIER, cnic != null ? cnic : "");
                intent.setData(null);
                startActivityForResult(intent, REQUEST_CODE_VERIFY_NADRA);
            } else if (resultCode == RESULT_CODE_FINGERS_EMPTY) {
                exhaustedFingers.clear();
                fourFingersReceived = false;
                openBvsDialog();
            }
        } else if (requestCode == Constants.REQUEST_CODE_BIOMETRIC_SELECTION) {
            if (resultCode == RESULT_OK) {
                biometricFlow = data.getExtras().getString(Constants.IntentKeys.BIOMETRIC_FLOW);
                if (biometricFlow.equals(Constants.PAYSYS)) {
                    checkPaysysBvs();
                } else if (biometricFlow.equals(Constants.SCO)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SCO;
                    openBvsActivity();
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

                    NadraRequestModel requestModel = new NadraRequestModel();
                    requestModel.setFingerIndex(strFingerIndex);
                    requestModel.setTemplateType(fileTemplateType);
                    requestModel.setFingerTemplate(fileTemplate);
                    requestModel.setCitizenNumber(cnic);
                    requestModel.setContactNumber(mobileNumber);
                    requestModel.setAreaName("Punjab");

                    Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                    nadraRequestJson = gson.toJson(requestModel);

                    currentCommand = Constants.CMD_VERIFY_NADRA_FINGERPRINT;
                    processRequest();
                }
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

            if (currentCommand == Constants.CMD_INFO_L1_TO_HRA) {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_INFO_L1_TO_HRA + "", mobileNumber, cnic);
            } else if (currentCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
                ApplicationData.nadraCall = true;
                new HttpAsyncTask(HraRegistrationActivity1.this)
                        .execute(Constants.CMD_VERIFY_NADRA_FINGERPRINT + "", nadraRequestJson);
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
            if (currentCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
//                hideLoading();
                processNadraResponse(response);
            } else {
                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
                hideLoading();

                switch (currentCommand) {
                    case Constants.CMD_INFO_L1_TO_HRA: {
                        if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                            List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                            MessageModel message = (MessageModel) list.get(0);
                            hideLoading();
                            if (message.getCode().equals(Constants.ErrorCodes.PHOENIX)) {
                                createAlertDialog(message.getDescr(),
                                        AppMessages.ALERT_HEADING);
                            } else {
                                createAlertDialog(message.getDescr(),
                                        AppMessages.ALERT_HEADING);
                            }

                            String code = message.getCode();
                            AppLogger.i("##Error Code: " + code);

                        } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                            discrepantCustomer = false;
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
                        }
//                        else if (table != null && table.containsKey(Constants.ATTR_CREG_STATE)) {
//                        hideLoading();
//                            cusRegState = (String) table.get(Constants.ATTR_CREG_STATE);
//                            cusRegStateId = (String) table.get(Constants.ATTR_CREG_STATE_ID);
//
//                            if (cusRegStateId.equals(Constants.REGISTRATION_STATE_BULK_REQUEST_RECEIVED) ||
//                                    cusRegStateId.equals(Constants.REGISTRATION_STATE_REQUEST_RECEIVED) ||
//                                    cusRegStateId.equals(Constants.REGISTRATION_STATE_VERIFIED)){


                        else if (table.containsKey(XmlConstants.ATTR_CNAME) && table.containsKey(XmlConstants.ATTR_CDOB)
                                && table.containsKey(XmlConstants.ATTR_FATHER_NAME)) {
                            hideLoading();
                            name = table.get(XmlConstants.ATTR_CNAME).toString();
                            fatherName = table.get(XmlConstants.ATTR_FATHER_NAME).toString();
                            dob = table.get(XmlConstants.ATTR_CDOB).toString();

                            labels = new String[]{"Name", "Father/Spouse Name", "Date of Birth"};
                            data = new String[]{name, fatherName, dob};


                            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

                            for (int i = 0; i < data.length; i++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("label", labels[i]);
                                hm.put("data", data[i]);
                                aList.add(hm);
                            }

                            String[] from = {"label", "data"};
                            int[] to = {R.id.txtLabel, R.id.txtData};
                            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
                                    R.layout.listview_layout_with_data, from, to);

                            dataList.setAdapter(adapter);
                            responseList.setVisibility(View.VISIBLE);

                            findViewById(R.id.lblIncomeSource).setVisibility(View.VISIBLE);
                            findViewById(R.id.lblOccupation).setVisibility(View.VISIBLE);
                            findViewById(R.id.lblAccountPurpose).setVisibility(View.VISIBLE);

                            incomeSrc.setVisibility(View.VISIBLE);
                            occupation.setVisibility(View.VISIBLE);
                            accountPurpose.setVisibility(View.VISIBLE);

                            responseReceived = true;
                            customerMobileNumber.setEnabled(false);
                            customerCNIC.setEnabled(false);
                            btnSearch.setText("Next");
                            Utility.getListViewSize(dataList, this, mListItemHieght);
                            break;
                        }

//                            if (cusRegStateId.equals(Constants.REGISTRATION_STATE_DISCREPANT)) {
//                                discrepantCustomer = true;
//                                decideBvs();
//                            }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processNadraResponse(HttpResponseModel response) {
        try {
            nadraResponseJson = response.getXmlResponse();

            if (nadraResponseJson.startsWith("<msg")) {

                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
                hideLoading();

                if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);
                    createAlertDialog(message.getDescr(),
                            AppMessages.ALERT_HEADING);
                }
            } else if (nadraResponseJson != null && !nadraResponseJson.equals("")) {
                hideLoading();
                nadraResponseModel = new Gson().fromJson
                        (nadraResponseJson, NadraResponseModel.class);

                if (nadraResponseModel.getResponseCode() != null && nadraResponseModel.getResponseCode().equals("100")) {
                    requestType = Constants.REQUEST_TYPE_NADRA;
                    askMpin(null, null, false);
                } else {
                    PopupDialogs.alertDialog(nadraResponseModel.getMessage() != null ?
                                    nadraResponseModel.getMessage() : "Exception in Nadra Response", PopupDialogs.Status.ERROR,
                            this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                }
                            });
                }
            } else {
                hideLoading();
                PopupDialogs.alertDialog("Exception in Nadra Response", PopupDialogs.Status.ERROR,
                        this, new PopupDialogs.OnCustomClickListener() {
                            @Override
                            public void onClick(View v, Object obj) {
                            }
                        });
            }

        } catch (Exception e) {
            PopupDialogs.alertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, PopupDialogs.Status.ERROR,
                    this, new PopupDialogs.OnCustomClickListener() {
                        @Override
                        public void onClick(View v, Object obj) {
                        }
                    });
            e.printStackTrace();
        }
    }

    @Override
    public void processNext() {
    }

    @Override
    public void headerImplementation() {
        btnHome = (ImageView) findViewById(R.id.imageViewHome);
        btnExit = (Button) findViewById(R.id.buttonsignout);

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

    private void openBvsDialog() {
        fingers = Utility.getAllFingers();
        Intent intent = new Intent(HraRegistrationActivity1.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }


    private boolean validate() {
        boolean validate = false;
        mobileNumber = customerMobileNumber.getText().toString();
        cnic = customerCNIC.getText().toString();

        if (!Utility.testValidity(customerMobileNumber)) {
            return validate;
        }
        if (customerMobileNumber.getText().toString().length() < 11) {
            customerMobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER);
            return validate;
        } else if ((mobileNumber.charAt(0) != '0' || mobileNumber.charAt(1) != '3')) {
            customerMobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER_FORMAT);
            return validate;
        } else if (!Utility.testValidity(customerCNIC)) {
            return validate;
        } else if (customerCNIC.getText().toString().length() < 13) {
            customerCNIC.setError(AppMessages.INVALID_CNIC);
            return validate;
        } else if (responseReceived) {
//            if (!Utility.testValidity(incomeSrc.getSelectedItem().t)) {
//                return validate;
//            } else
            if (!Utility.testValidity(occupation)) {
                return validate;
            } else {
                validate = true;
            }
        } else {
            validate = true;
        }

        return validate;
    }

    @Override
    public void onBackPressed() {
        hideKeyboard(getCurrentFocus());
        goToMainMenu();
    }

    private void handleOtherError() {

        if (strErrorCode.equals("118")) {
            if (fourFingersReceived) {
                Fingers currentFinger = ApplicationData.currentFinger;
                for (int i = 0; i < fingers.size(); i++) {
                    if (fingers.get(i).equals(currentFinger.getValue())) {
                        exhaustedFingers.add(exhaustedFingers.size(), fingers.get(i));
                        fingers.remove(i);
                        break;
                    }
                }

                Intent intent = new Intent(HraRegistrationActivity1.this, FingerScanActivity.class);
                intent.putStringArrayListExtra("fingers", fingers);
                intent.putExtra("validFingerIndexes", fingerIndexes);
                intent.putExtra("code", strErrorCode);
                startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
            } else {
                openBvsDialog();
            }
        } else {
            if (fourFingersReceived) {
                Intent intent = new Intent(HraRegistrationActivity1.this, FingerScanActivity.class);
                intent.putStringArrayListExtra("fingers", fingers);
                intent.putExtra("validFingerIndexes", fingerIndexes);
                intent.putExtra("code", strErrorCode);
                startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
            } else {
                openBvsDialog();
            }
        }
    }

    private void handleIndexesError() {
        if (exhaustedFingers.size() == 0) {
            Intent intent = new Intent(HraRegistrationActivity1.this, FingerScanActivity.class);
            intent.putStringArrayListExtra("fingers", fingers);
            intent.putExtra("validFingerIndexes", fingerIndexes);
            intent.putExtra("code", strErrorCode);
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
            Intent intent = new Intent(HraRegistrationActivity1.this, FingerScanActivity.class);
            intent.putStringArrayListExtra("fingers", fingers);
            intent.putExtra("validFingerIndexes", fingerIndexes);
            intent.putExtra("code", strErrorCode);
            startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
        }
    }

    private void openBvsActivity() {
        Intent intent = new Intent(HraRegistrationActivity1.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, mobileNumber);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, "HRA Registration");
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
    }
}