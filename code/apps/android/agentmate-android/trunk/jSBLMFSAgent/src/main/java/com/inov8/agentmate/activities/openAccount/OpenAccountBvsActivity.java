package com.inov8.agentmate.activities.openAccount;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.BvsDeviceSelectorActivity1;
import com.inov8.agentmate.activities.TermsConditionsActivity;
import com.inov8.agentmate.activities.TransactionReceiptActivity;
import com.inov8.agentmate.activities.debitCard.TermsConditionsDebitCardActivity;
import com.inov8.agentmate.activities.fundsTransfer.ReceiveCashConfirmationActivity;
import com.inov8.agentmate.activities.hra.HraRegistrationActivity1;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.bvs.paysys.NadraRestClient;
import com.inov8.agentmate.model.ExciseAndTaxationModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.NadraRequestModel;
import com.inov8.agentmate.model.NadraResponseModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.SegmentModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AesEncryptor;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsblmfs.R;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.model.PersonData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_NADRA_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_VERIFY_NADRA;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_NADRA_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FINGERS_EMPTY;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_NADRA;
import static com.inov8.agentmate.util.Utility.getFormattedDate;

public class OpenAccountBvsActivity extends BaseCommunicationActivity implements View.OnClickListener {

    private String strOrgRel1 = "", strOrgRel2 = "", strOrgRel3 = "", strOrgRel4 = "", strOrgRel5 = "";
    private String strOrgLoc1 = "", strOrgLoc2 = "", strOrgLoc3 = "", strOrgLoc4 = "", strOrgLoc5 = "";
    private String srtIsHr = "";
    private String amount = "";
    private String strNextKin = "", strOccupation = "";
    private String purposeOfAccount = "";
    private String selectedOpenAccountSegment = "";

    private ExciseAndTaxationModel transactionInfo;
    private boolean isExciseAndTaxation = false;
    private String tAmount;

    private boolean isCashWithDraw = false;
    private boolean loToL1Upgrade = false;

    private EditText customerMobileNumber, confirmCustomerMobileNumber,
            customerCNIC, confirmCustomerCNIC, initialDeposit, cardEmbossingName, mailingAddress;
    private RelativeLayout cnicExpiryLayout;
    private Spinner accountType, mobileNetwork,selectSegment;
    private Button btnNext;
    private CheckBox cbInitialDeposit, checkbox_hra;
    private TextView tv_cnicExpiry, initialDepositHint, lblHeading, lblCardEmbossingName, lblMailingAddress;
    private ProductModel product;
    private byte flowId;
    private String cnic, confirmCnic, mobileNumber, confirmMobileNumber,
            rmob, dob, rcnic, trxId, cname, cnicExpiry, strInitialDeposit,
            strOtp, biometricFlow, customerembossingName, customerMailingAddress;
    private String cnicExpiryDate = null;
    private Bundle bundle = new Bundle();
    DatePickerDialog cnic_datePickerDialog;
    private String[] accountTypes = {"Level 0", "Level 1"};
    private String[] mobileNetworks = {"Mobilink", "Ufone", "Telenor", "Zong", "Warid"};
    private String selectedAccountType, encMPIN, selectedMobileNetwork;
    private PersonData nadraPersonData;
    private NadraResponseModel nadraResponseModel;
    private int currentCommand;
    private int isReceiveCash = 0;
    private String cusRegStateId, cusRegState = null;
    private ArrayList<String> fingers;
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private ArrayList<Integer> fingerIndexes;
    private boolean fourFingersReceived = false;
    private boolean discrepantCustomer = false;
    private ArrayList<Integer> discrepantImagesFlags = null;
    private String discrepantCustName, discrepantCustCnic, discrepantCustMob,
            discrepantCustDob, discrepantCustCnicExp;
    private int attemptsCounter = 0;
    private String strErrorCode, strMessage = null;
    private String fileTemplate = "";
    private String fileTemplateType = "";
    private String strFingerIndex, nadraRequestJson,
            nadraResponseJson, requestType;

    private String isUpgrad = "0";
    private Context con;
    ArrayAdapter<String> segmentsOpenAccountAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_account_bvs);

        ApplicationData.isLoginFlow = false;
        fetchIntents();
        setUI();

        headerImplementation();

        con = this;

    }

    private void openAlter() {

    }

    private void fetchIntents() {
        try {

            flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
            rmob = mBundle.getString(Constants.IntentKeys.RCMOB);
            rcnic = mBundle.getString(Constants.IntentKeys.RCNIC);

            cnic = getIntent().getExtras().getString(Constants.IntentKeys.CNIC);
            mobileNumber = getIntent().getExtras().getString(Constants.IntentKeys.MOBILE);

            isCashWithDraw = getIntent().getExtras().getBoolean(Constants.IntentKeys.IS_CASH_WITHDRAW);
            loToL1Upgrade = getIntent().getExtras().getBoolean(Constants.IntentKeys.L0_TO_L1_UPGRADE);

            amount = getIntent().getExtras().getString(Constants.IntentKeys.TRANSACTION_AMOUNT);

            if (mBundle.containsKey(Constants.IntentKeys.TRXID)) {
                findViewById(R.id.checkbox_hra).setVisibility(View.GONE);
                findViewById(R.id.lblHra).setVisibility(View.GONE);
                trxId = mBundle.getString(Constants.IntentKeys.TRXID);
            }
            if (mBundle.containsKey(Constants.IntentKeys.IS_RECEIVE_CASH))
                isReceiveCash = mBundle.getInt(Constants.IntentKeys.IS_RECEIVE_CASH);
            if (mBundle.containsKey(Constants.IntentKeys.IS_EXCISE_AND_TAXATION)) {
                isExciseAndTaxation = true;
                tAmount = mBundle.getString(Constants.IntentKeys.TRANSACTION_AMOUNT);
                transactionInfo = (ExciseAndTaxationModel) mBundle.get(Constants.IntentKeys.EXCISE_AND_TAXATION_INFO_MODEL);
            }
        } catch (Exception e) {

        }
    }

    private void setUI() {
        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText("Account Opening");

        customerMobileNumber = (EditText) findViewById(R.id.input_customer_mobile);
        confirmCustomerMobileNumber = (EditText) findViewById(R.id.input_customer_mobile_confirm);
        disableCopyPaste(customerMobileNumber);
        disableCopyPaste(confirmCustomerMobileNumber);

        findViewById(R.id.lbl_customer_mobile_confirm).setVisibility(View.GONE);
        confirmCustomerMobileNumber.setVisibility(View.GONE);

        if (rmob != null) {
            customerMobileNumber.setText(rmob);
            customerMobileNumber.setEnabled(false);
            confirmCustomerMobileNumber.setText(rmob);
            confirmCustomerMobileNumber.setEnabled(false);
        }

        customerCNIC = (EditText) findViewById(R.id.input_customer_cnic);
        confirmCustomerCNIC = (EditText) findViewById(R.id.input_customer_cnic_confirm);
        disableCopyPaste(customerCNIC);
        disableCopyPaste(confirmCustomerCNIC);

        findViewById(R.id.lbl_customer_cnic_confirm).setVisibility(View.GONE);
        confirmCustomerCNIC.setVisibility(View.GONE);

        if (rcnic != null) {
            customerCNIC.setText(rcnic);
            customerCNIC.setEnabled(false);
            confirmCustomerCNIC.setText(rcnic);
            confirmCustomerCNIC.setEnabled(false);
        }

        initialDeposit = (EditText) findViewById(R.id.input_initial_deposit);
        initialDepositHint = (TextView) findViewById(R.id.txt_initial_deposit);
        disableCopyPaste(initialDeposit);

        cbInitialDeposit = (CheckBox) findViewById(R.id.checkbox_initial_deposit);

        checkbox_hra = (CheckBox) findViewById(R.id.checkbox_hra);
        checkbox_hra.setVisibility(View.GONE);

        findViewById(R.id.lblHra).setVisibility(View.GONE);

        cbInitialDeposit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    initialDeposit.setVisibility(View.VISIBLE);
                    initialDepositHint.setVisibility(View.VISIBLE);
                    findViewById(R.id.lbl_initial_deposit).setVisibility(View.VISIBLE);
                    initialDeposit.requestFocus();
                } else {
                    initialDeposit.setVisibility(View.GONE);
                    initialDepositHint.setVisibility(View.GONE);
                    findViewById(R.id.lbl_initial_deposit).setVisibility(View.GONE);
                }
            }
        });

        checkbox_hra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    initialDeposit.setVisibility(View.GONE);
                    initialDepositHint.setVisibility(View.GONE);
                    cbInitialDeposit.setVisibility(View.GONE);
                    findViewById(R.id.lbl_initial_deposit).setVisibility(View.GONE);
                    findViewById(R.id.lblFieldInitialDeposit).setVisibility(View.GONE);
                    cbInitialDeposit.setChecked(false);

                } else {
                    findViewById(R.id.lblFieldInitialDeposit).setVisibility(View.VISIBLE);
                    cbInitialDeposit.setVisibility(View.VISIBLE);

                    if (cbInitialDeposit.isChecked()) {
                        initialDeposit.setVisibility(View.VISIBLE);
                        initialDepositHint.setVisibility(View.VISIBLE);
                        findViewById(R.id.lbl_initial_deposit).setVisibility(View.VISIBLE);
                        initialDeposit.requestFocus();
                    }
                }
            }
        });

        if (isReceiveCash == 1) {
            initialDeposit.setVisibility(View.GONE);
            initialDepositHint.setVisibility(View.GONE);
            findViewById(R.id.lbl_initial_deposit).setVisibility(View.GONE);
        }
        if (product != null && product.getMinamtf() != null && !product.getMinamtf().equals("") &&
                product.getMaxamtf() != null && !product.getMaxamtf().equals("")) {
            initialDepositHint.setVisibility(View.VISIBLE);
            initialDepositHint.setText("Enter an amount of PKR " + product.getMinamtf() + " to PKR " + product.getMaxamtf());
        }

        if (cbInitialDeposit.getVisibility() == View.VISIBLE) {
            initialDeposit.setVisibility(View.GONE);
            initialDepositHint.setVisibility(View.GONE);
            findViewById(R.id.lbl_initial_deposit).setVisibility(View.GONE);
        }

        cnicExpiryLayout = (RelativeLayout) findViewById(R.id.input_cnic_expiry_date_layout);

        if (!ApplicationData.isCnicExpiryRequired) {
            cnicExpiryLayout.setVisibility(View.GONE);
            findViewById(R.id.lbl_cnic_expiry_date).setVisibility(View.GONE);
        }

        accountType = (Spinner) findViewById(R.id.spinner_account_type);
        mobileNetwork = (Spinner) findViewById(R.id.spinner_mobile_network);
        selectSegment = (Spinner) findViewById(R.id.spinner_select_segment);
        btnNext = (Button) findViewById(R.id.btnNext);
        tv_cnicExpiry = (TextView) findViewById(R.id.input_cnic_expiry_date);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accountTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType.setAdapter(adapter);
        if (accountTypes.length > 1) {
            accountType.setSelection(1);
            accountType.setEnabled(false);
        }
        accountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (accountTypes[position].equals(accountTypes[0])) {
                    selectedAccountType = "1";
                } else {
                    selectedAccountType = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.lbl_mobile_network).setVisibility(View.VISIBLE);
        findViewById(R.id.lbl_select_segment).setVisibility(View.VISIBLE);
        mobileNetwork.setVisibility(View.VISIBLE);
        selectSegment.setVisibility(View.VISIBLE);

//        if (ApplicationData.listOpenAccountSegments == null) {
            currentCommand = Constants.CMD_OPEN_ACCOUNT_FETCH_SEGMENTS;
            processRequest();
//        } else {
//            ArrayList<String> segmentsOpenAccountList = new ArrayList<>();
//            for (int i = 0; i < ApplicationData.listOpenAccountSegments.size(); i++) {
//                segmentsOpenAccountList.add(ApplicationData.listOpenAccountSegments.get(i).getName());
//            }
//            segmentsOpenAccountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, segmentsOpenAccountList);
//            selectSegment.setAdapter(segmentsOpenAccountAdapter);
//        }
        setOpenAccountSpinner();
        ArrayAdapter<String> networkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mobileNetworks);
        networkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mobileNetwork.setAdapter(networkAdapter);

        mobileNetwork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMobileNetwork = mobileNetworks[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMobileNetwork = mobileNetworks[0];
            }
        });

        findViewById(R.id.lbl_account_type).setVisibility(View.GONE);
        accountType.setVisibility(View.GONE);
        selectedAccountType = "2";

        cnicExpiryLayout.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        if (flowId == Constants.FLOW_ID_L0_L1) {
            lblHeading.setText("Update L0 Account");
            checkbox_hra.setVisibility(View.GONE);
            initialDeposit.setVisibility(View.GONE);
            findViewById(R.id.lblHra).setVisibility(View.GONE);
            findViewById(R.id.lblFieldInitialDeposit).setVisibility(View.GONE);
            cbInitialDeposit.setVisibility(View.GONE);
            findViewById(R.id.lbl_select_segment).setVisibility(View.GONE);
            selectSegment.setVisibility(View.GONE);
            isUpgrad = "1";

        }

        lblCardEmbossingName = (TextView) findViewById(R.id.lbl_card_embossing_name);
        lblMailingAddress = (TextView) findViewById(R.id.lbl_mailing_address);
        cardEmbossingName = (EditText) findViewById(R.id.input_card_embossing_name);
        mailingAddress = (EditText) findViewById(R.id.input_mailing_address);

        if (isExciseAndTaxation) {

            customerMobileNumber.setText(rmob);
            customerMobileNumber.setEnabled(false);
            customerCNIC.setText(rcnic);
            customerCNIC.setEnabled(false);
            cbInitialDeposit.setChecked(true);
            cbInitialDeposit.setEnabled(false);
            initialDeposit.setText(amount);
            initialDepositHint.setText("Enter an amount of PKR " + tAmount + " to PKR " + product.getMaxamtf());

        }

        if (isCashWithDraw || loToL1Upgrade) {

            customerCNIC.setText(cnic);
            customerCNIC.setEnabled(false);
            confirmCustomerCNIC.setText(cnic);
            confirmCustomerCNIC.setEnabled(false);
            customerMobileNumber.setText(mobileNumber);
            customerMobileNumber.setEnabled(false);
            confirmCustomerMobileNumber.setText(mobileNumber);
            confirmCustomerMobileNumber.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.input_cnic_expiry_date_layout:
                cnic_datePickerDialog = new DatePickerDialog(OpenAccountBvsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv_cnicExpiry.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                String months = (monthOfYear + 1) + "";
                                String day = dayOfMonth + "";

                                if ((dayOfMonth + "").length() == 1) {
                                    day = "0" + dayOfMonth;
                                }
                                if (((monthOfYear + 1) + "").length() == 1) {
                                    months = "0" + (monthOfYear + 1);
                                }

                                tv_cnicExpiry.setText(day + "-" + months + "-" + year);

                                try {
                                    SimpleDateFormat temp = null;
                                    Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(tv_cnicExpiry.getText().toString());
                                    temp = new SimpleDateFormat("yyyy-MM-dd");
                                    cnicExpiryDate = temp.format(initDate);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                cnic_datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);
                cnic_datePickerDialog.show();
                break;
            case R.id.btnNext:
                if (validate()) {

                    ApplicationData.mobileNo = customerMobileNumber.getText().toString();
                    ApplicationData.cnic = customerCNIC.getText().toString();

                    if (checkbox_hra.isChecked()) {
                        srtIsHr = "1";
                        Intent intent = new Intent(OpenAccountBvsActivity.this, OpenAccountHraActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_CODE_HRA);
                    } else {
                        currentCommand = Constants.CMD_OPEN_ACCOUNT_VERIFY_CUSTOMER_REGISTRATION;
                        processRequest();
                    }

//                    PopupDialogs.createAlertDialog("Customer request has been submitted successfully.", "Message",
//                            PopupDialogs.Status.INFO, this, new PopupDialogs.OnCustomClickListener() {
//                                @Override
//                                public void onClick(View v, Object obj) {
//
//                                    PopupDialogs.createConfirmationDialog(AppMessages.DEBIT_CARD_ISSUENCE, "Debit Card Request",
//                                            PopupDialogs.Status.INFO, con, new PopupDialogs.OnCustomClickListener() {
//                                                @Override
//                                                public void onClick(View v, Object obj) {
//                                                    switch (v.getId()) {
//
//                                                        case R.id.btnCancel: {
//                                                            goToMainMenu();
//                                                            break;
//                                                        }
//                                                        case R.id.btnOK: {
//                                                            Intent i = new Intent(OpenAccountBvsActivity.this, TermsConditionsDebitCardActivity.class);
//                                                            startActivity(i);
//                                                            break;
//                                                        }
//                                                    }
//
//                                                }
//                                            });
//                                }
//                            });
                }
        }
    }

    protected void decideBvs() {
        Intent intent = new Intent(OpenAccountBvsActivity.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }

    protected void checkPaysysBvs() {
        if (ApplicationData.isAgentAllowedBvs.equals("1")) {
            if (ApplicationData.isBvsEnabledDevice) {
                openBvsDialog();
            } else {
                if (ApplicationData.bvsErrorMessage != null)
                    PopupDialogs.alertDialog(ApplicationData.bvsErrorMessage,
                            PopupDialogs.Status.INFO, OpenAccountBvsActivity.this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    finish();
                                }
                            });
                else
                    PopupDialogs.alertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE,
                            PopupDialogs.Status.INFO, OpenAccountBvsActivity.this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    finish();
                                }
                            });
            }
        } else {
            PopupDialogs.alertDialog(AppMessages.BVS_NOT_ENABLED,
                    PopupDialogs.Status.INFO, OpenAccountBvsActivity.this, new PopupDialogs.OnCustomClickListener() {
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
                                OpenAccountBvsActivity.this, new PopupDialogs.OnCustomClickListener() {
                                    @Override
                                    public void onClick(View v, Object obj) {
                                        OpenAccountBvsActivity.this.finish();
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

                        if (checkbox_hra.isChecked()) {
                            handleIndexesError();
                        } else if (flowId == Constants.FLOW_ID_L0_L1) {
                            handleOtherError();
                        } else {
                            openConventionalAccountDialog();
                        }
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

                                        if (attemptsCounter < 2)
                                            handleOtherError();
                                        else if (checkbox_hra.isChecked()) {
                                            handleOtherError();
                                        } else if (flowId == Constants.FLOW_ID_L0_L1) {
                                            handleOtherError();
                                        } else {
                                            openConventionalAccountDialog();
                                        }


                                    }
                                });
                    } else {
                        if (attemptsCounter < 2)
                            handleOtherError();
                        else if (checkbox_hra.isChecked()) {
                            handleOtherError();
                        } else if (flowId == Constants.FLOW_ID_L0_L1) {
                            handleOtherError();
                        } else {
                            openConventionalAccountDialog();
                        }
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

                Intent intent = new Intent(OpenAccountBvsActivity.this, NadraRestClient.class);
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
        } else if (requestCode == Constants.REQUEST_CODE_HRA) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    purposeOfAccount = data.getStringExtra(Constants.IntentKeys.PURPOSE_OF_ACCOUNT);
                    strOccupation = data.getStringExtra(Constants.IntentKeys.OCCUPATION);
                    strNextKin = data.getStringExtra(Constants.IntentKeys.NEXT_OF_KIN);

                    strOrgLoc1 = data.getStringExtra(Constants.IntentKeys.ORG_LOC_1);
                    strOrgLoc2 = data.getStringExtra(Constants.IntentKeys.ORG_LOC_2);
                    strOrgLoc3 = data.getStringExtra(Constants.IntentKeys.ORG_LOC_3);
                    strOrgLoc4 = data.getStringExtra(Constants.IntentKeys.ORG_LOC_4);
                    strOrgLoc5 = data.getStringExtra(Constants.IntentKeys.ORG_LOC_5);

                    strOrgRel1 = data.getStringExtra(Constants.IntentKeys.ORG_REL_1);
                    strOrgRel2 = data.getStringExtra(Constants.IntentKeys.ORG_REL_2);
                    strOrgRel3 = data.getStringExtra(Constants.IntentKeys.ORG_REL_3);
                    strOrgRel4 = data.getStringExtra(Constants.IntentKeys.ORG_REL_4);
                    strOrgRel5 = data.getStringExtra(Constants.IntentKeys.ORG_REL_5);

                    currentCommand = Constants.CMD_OPEN_ACCOUNT_VERIFY_CUSTOMER_REGISTRATION;
                    processRequest();
                }
            }
        }
    }

    @Override
    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        encMPIN = getEncryptedMpin();
        ApplicationData.nadraCall = false;

        currentCommand = Constants.CMD_OPEN_ACCOUNT_BVS;
        processRequest();
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
            if (currentCommand == Constants.CMD_OPEN_ACCOUNT_FETCH_SEGMENTS) {
                new HttpAsyncTask(OpenAccountBvsActivity.this).execute(Constants.CMD_OPEN_ACCOUNT_FETCH_SEGMENTS + "");
            }else if (currentCommand == Constants.CMD_OPEN_ACCOUNT_VERIFY_CUSTOMER_REGISTRATION) {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_OPEN_ACCOUNT_VERIFY_CUSTOMER_REGISTRATION + "", mobileNumber, cnic,
                        trxId != null ? trxId : "", isReceiveCash + "", "0", isUpgrad,ApplicationData.userId,product.getId(),selectedOpenAccountSegment);
            } else if (currentCommand == Constants.CMD_OPEN_ACCOUNT_OTP_VERIFICATION) {
                new HttpAsyncTask(OpenAccountBvsActivity.this)
                        .execute(Constants.CMD_OPEN_ACCOUNT_OTP_VERIFICATION + "",
                                mobileNumber, cnic, "0", strOtp);
            } else if (currentCommand == Constants.CMD_DEBIT_CARD) {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_DEBIT_CARD + "", mobileNumber, cnic, customerembossingName, customerMailingAddress);
            } else if (currentCommand == Constants.CMD_OPEN_ACCOUNT_BVS) {
                if (requestType.equals(Constants.REQUEST_TYPE_PAYSYS))
                    new HttpAsyncTask(this).execute(String.valueOf(Constants.CMD_OPEN_ACCOUNT_BVS),
                            encMPIN, mobileNumber, cnic, nadraPersonData.getBirthPlace(), "", nadraPersonData.getName(),
                            nadraPersonData.getMotherName() != null ? nadraPersonData.getMotherName() : "",
                            getFormattedDate(nadraPersonData.getDateOfBirth()), nadraPersonData.getCardExpired(),
                            nadraPersonData.getPresentAddress(), "",
                            nadraPersonData.getPermanentAddress() != null ? nadraPersonData.getPermanentAddress() : "",
                            "", nadraPersonData.getName(), nadraPersonData.getGender() != null ? nadraPersonData.getGender() : "",
                            nadraPersonData.getFatherHusbandName() != null ? nadraPersonData.getFatherHusbandName() : "", "1",
                            nadraPersonData.getExpiryDate() != null ? nadraPersonData.getExpiryDate() : cnicExpiryDate != null ? cnicExpiryDate : "",
                            isReceiveCash == 1 ? "" : initialDeposit.getVisibility() == View.VISIBLE ? initialDeposit.getText().toString() : "",
                            null, selectedAccountType, ApplicationData.agentMobileNumber,
                            product.getId(), ApplicationData.isAgentAllowedBvs, cusRegState != null ? cusRegState : "",
                            cusRegStateId != null ? cusRegStateId : "", trxId != null ? trxId : "",
                            initialDeposit.getVisibility() == View.VISIBLE ? "1" : "0",
                            srtIsHr, strNextKin, purposeOfAccount, strOccupation,
                            strOrgLoc1, strOrgLoc2, strOrgLoc3, strOrgLoc4, strOrgLoc5,
                            strOrgRel1, strOrgRel2, strOrgRel3, strOrgRel4, strOrgRel5, selectedMobileNetwork,selectedOpenAccountSegment);

                else if (requestType.equals(Constants.REQUEST_TYPE_NADRA))
                    new HttpAsyncTask(this).execute(String.valueOf(Constants.CMD_OPEN_ACCOUNT_BVS),
                            encMPIN, mobileNumber, cnic,
                            nadraResponseModel.getBirthPlace() != null ? nadraResponseModel.getBirthPlace() : "", "",
                            nadraResponseModel.getCitizenName() != null ? nadraResponseModel.getCitizenName() : "",
                            nadraResponseModel.getMotherName() != null ? nadraResponseModel.getMotherName() : "",
                            nadraResponseModel.getDateOfBirth() != null ? getFormattedDate(nadraResponseModel.getDateOfBirth()) : "",
                            nadraResponseModel.getCardExpired() != null ? nadraResponseModel.getCardExpired() : "",
                            nadraResponseModel.getPresentAddress() != null ? nadraResponseModel.getPresentAddress() : "", "",
                            nadraResponseModel.getPresentAddress() != null ? nadraResponseModel.getPresentAddress() : "",
                            "", nadraResponseModel.getCitizenName() != null ? nadraResponseModel.getCitizenName() : "",
                            nadraResponseModel.getGender() != null ? nadraResponseModel.getGender() : "", "", "1",
                            nadraResponseModel.getCardExpiry() != null ? nadraResponseModel.getCardExpiry() : cnicExpiryDate != null ? cnicExpiryDate : "",
                            isReceiveCash == 1 ? "" : initialDeposit.getVisibility() == View.VISIBLE ? initialDeposit.getText().toString() : "",
                            null, selectedAccountType, ApplicationData.agentMobileNumber,
                            product.getId(), ApplicationData.isAgentAllowedBvs, cusRegState != null ? cusRegState : "",
                            cusRegStateId != null ? cusRegStateId : "", trxId != null ? trxId : "",
                            initialDeposit.getVisibility() == View.VISIBLE ? "1" : "0",
                            srtIsHr, strNextKin, purposeOfAccount, strOccupation,
                            strOrgLoc1, strOrgLoc2, strOrgLoc3, strOrgLoc4, strOrgLoc5,
                            strOrgRel1, strOrgRel2, strOrgRel3, strOrgRel4, strOrgRel5, selectedMobileNetwork,selectedOpenAccountSegment);
            } else if (currentCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
                ApplicationData.nadraCall = true;
                new HttpAsyncTask(OpenAccountBvsActivity.this)
                        .execute(Constants.CMD_VERIFY_NADRA_FINGERPRINT + "", nadraRequestJson);
            }
            else if (currentCommand == Constants.CMD_BVS_FAIL) {
                new HttpAsyncTask(OpenAccountBvsActivity.this)
                        .execute(Constants.CMD_BVS_FAIL + "", "1");
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
                hideLoading();
                processNadraResponse(response);
            } else {
                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
                hideLoading();

                switch (currentCommand) {
                    case Constants.CMD_BVS_FAIL: {
                        openBvsActivity();
                        return;
                    }
                    case Constants.CMD_OPEN_ACCOUNT_FETCH_SEGMENTS: {
                        ApplicationData.listOpenAccountSegments = null;
                        ApplicationData.listOpenAccountSegments = (ArrayList<SegmentModel>) table.get(Constants.TAG_SEGMENTS);
                        ArrayList<String> segmentsOpenAccountList = new ArrayList<>();
                        for (int i = 0; i < ApplicationData.listOpenAccountSegments.size(); i++) {
                            segmentsOpenAccountList.add(ApplicationData.listOpenAccountSegments.get(i).getName());
                        }
                        segmentsOpenAccountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, segmentsOpenAccountList);
                        selectSegment.setAdapter(segmentsOpenAccountAdapter);
                        break;
                    }
                    case Constants.CMD_OPEN_ACCOUNT_VERIFY_CUSTOMER_REGISTRATION: {
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
                        } else if (table != null && table.containsKey(Constants.ATTR_CREG_STATE)) {
                            hideLoading();
                            cusRegState = (String) table.get(Constants.ATTR_CREG_STATE);

                            if (table.get(Constants.ATTR_CREG_STATE_ID).equals(null)) {
                                verifyOtp();
                            } else if (table.get(Constants.ATTR_CREG_STATE_ID).equals(Constants.REGISTRATION_STATE_VERIFIED)) {
                                verifyOtp();
                            } else
                                cusRegStateId = (String) table.get(Constants.ATTR_CREG_STATE_ID);

                            if (cusRegStateId.equals(Constants.REGISTRATION_STATE_BULK_REQUEST_RECEIVED)) {
                                createAlertDialog("Request Already Received.", Constants.KEY_LIST_ALERT);
                            } else if (cusRegStateId.equals(Constants.REGISTRATION_STATE_REQUEST_RECEIVED)) {
                                createAlertDialog("Request Already Received.", Constants.KEY_LIST_ALERT);
                            }
                            if (cusRegStateId.equals(Constants.REGISTRATION_STATE_DISCREPANT)) {
                                discrepantCustomer = true;
                                cname = (String) table.get(Constants.ATTR_CNAME);
                                cnicExpiry = (String) table.get(Constants.ATTR_CNIC_EXP);

                                // images list
                                discrepantImagesFlags = new ArrayList<Integer>();

                                if (table.containsKey(Constants.ATTR_CUSTOMER_PHOTO_FLAG)) {
                                    if (((String) table.get(Constants.ATTR_CUSTOMER_PHOTO_FLAG)).equals("1")) {
                                        discrepantImagesFlags.add(Constants.AccountOpenningImages.CUSTOMER);
                                    }
                                }

                                if (table.containsKey(Constants.ATTR_CNIC_FRONT_PHOTO_FLAG)) {
                                    if (((String) table.get(Constants.ATTR_CNIC_FRONT_PHOTO_FLAG)).equals("1")) {
                                        discrepantImagesFlags.add(Constants.AccountOpenningImages.CNIC_FRONT);
                                    }
                                }

                                discrepantCustMob = mobileNumber;
                                discrepantCustCnic = cnic;
                                discrepantCustName = cname;
                                discrepantCustCnicExp = cnicExpiry;
                                discrepantCustDob = (String) table.get(Constants.ATTR_CDOB);

                                verifyOtp();
                            }
                        } else {
                            hideLoading();
                            verifyOtp();
                        }
                        break;
                    }
                    case Constants.CMD_OPEN_ACCOUNT_OTP_VERIFICATION: {
                        hideLoading();
                        if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                            List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                            MessageModel message = (MessageModel) list.get(0);
                            hideLoading();
                            String code = message.getCode();
                            AppLogger.i("##Error Code: " + code);
                            if (code.equals(Constants.ErrorCodes.OTP_INVALID)) {
                                PopupDialogs.createAlertDialog(message.getDescr(),
                                        AppMessages.ALERT_HEADING, PopupDialogs.Status.ERROR,
                                        OpenAccountBvsActivity.this, new PopupDialogs.OnCustomClickListener() {
                                            @Override
                                            public void onClick(View v, Object obj) {
                                                verifyOtp();
                                            }
                                        });
                            } else {
                                createAlertDialog(message.getDescr(),
                                        AppMessages.ALERT_HEADING);
                            }
                        } else if (table.containsKey(Constants.ATTR_DTID)) {
//                            checkPaysysBvs();
                            decideBvs();
                        }
                        break;
                    }
                    case Constants.CMD_OPEN_ACCOUNT_BVS: {
                        if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                            List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                            MessageModel message = (MessageModel) list.get(0);
                            hideLoading();
                            String code = message.getCode();
                            AppLogger.i("##Error Code: " + code);

                            createAlertDialog(message.getDescr(),
                                    AppMessages.ALERT_HEADING);

                        } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                            discrepantCustomer = false;
                            hideLoading();
                            List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                            MessageModel message = (MessageModel) list.get(0);

                            if (isCashWithDraw || loToL1Upgrade) {
                                Intent intent = new Intent(OpenAccountBvsActivity.this, HraRegistrationActivity1.class);
                                intent.putExtra(Constants.IntentKeys.CNIC, customerCNIC.getText().toString());
                                intent.putExtra(Constants.IntentKeys.MOBILE, customerMobileNumber.getText().toString());
                                intent.putExtra(Constants.IntentKeys.IS_CASH_WITHDRAW, true);
                                intent.putExtra(Constants.ATTR_MOBILE_NETWORK, selectedMobileNetwork);
                                intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, product);
                                intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT, amount);
                                intent.putExtra(Constants.IntentKeys.HRA_LINKED_REQUEST, "0");
                                startActivity(intent);
                            } else if (isExciseAndTaxation) {
                                showLoading("Please Wait", "Processing...");
                                requestType = String.valueOf(Constants.CMD_EXCISE_AND_TAXATION_PAYMENT);
                                new HttpAsyncTask(OpenAccountBvsActivity.this).execute(
                                        Constants.CMD_EXCISE_AND_TAXATION_PAYMENT + "", product.getId() + "", transactionInfo.getVehChassisNo(),
                                        transactionInfo.getVehRegNo(), transactionInfo.getVehAssesmentNo(), transactionInfo.getVehTaxAmount(), customerMobileNumber.getText().toString());
                            } else {
                                PopupDialogs.createAlertDialog(message.getDescr(), "Message",
                                        PopupDialogs.Status.INFO, this, new PopupDialogs.OnCustomClickListener() {
                                            @Override
                                            public void onClick(View v, Object obj) {
//                                            goToMainMenu();
                                                PopupDialogs.createConfirmationDialog(AppMessages.DEBIT_CARD_ISSUENCE, "Debit Card Request",
                                                        PopupDialogs.Status.INFO, con, new PopupDialogs.OnCustomClickListener() {
                                                            @Override
                                                            public void onClick(View v, Object obj) {
                                                                switch (v.getId()) {

                                                                    case R.id.btnCancel: {
                                                                        goToMainMenu();
                                                                        break;
                                                                    }
                                                                    case R.id.btnOK: {
                                                                        Intent i = new Intent(OpenAccountBvsActivity.this, TermsConditionsDebitCardActivity.class);
                                                                        startActivity(i);
                                                                        break;
                                                                    }
                                                                }

                                                            }
                                                        });
                                            }
                                        });
                            }
                        } else if (requestType.equals(String.valueOf(Constants.CMD_EXCISE_AND_TAXATION_PAYMENT))) {
                            if (table.containsKey(Constants.KEY_LIST_TRANS)) {
                                @SuppressWarnings("unchecked")
                                List<TransactionModel> listTransactionModel = (List<TransactionModel>) table.get(Constants.KEY_LIST_TRANS);
                                TransactionModel transaction = listTransactionModel.get(0);

                                Intent intent = new Intent(getApplicationContext(), TransactionReceiptActivity.class);
                                intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL, transaction);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                            }
                        }
                        break;
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
                    createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING);
                }
            } else if (nadraResponseJson != null && !nadraResponseJson.equals("")) {
                nadraResponseModel = new Gson().fromJson
                        (nadraResponseJson, NadraResponseModel.class);

                if (nadraResponseModel.getResponseCode() != null && nadraResponseModel.getResponseCode().equals("100")) {
                    requestType = Constants.REQUEST_TYPE_NADRA;
                    askMpin(null, null, false);
                }  else if (nadraResponseModel.getResponseCode()!=null && nadraResponseModel.getResponseCode().equals("121") || nadraResponseModel.getResponseCode().equals("122")){
                    PopupDialogs.alertDialog(nadraResponseModel.getMessage()!=null?
                                    nadraResponseModel.getMessage() : "Exception in Nadra Response", PopupDialogs.Status.ERROR,
                            this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    currentCommand = Constants.CMD_BVS_FAIL;
                                    processRequest();
                                }
                            });
                }
                else if (nadraResponseModel.getResponseCode()!=null &&  nadraResponseModel.getResponseCode().equals("118")){
                    PopupDialogs.alertDialog(nadraResponseModel.getMessage()!=null?
                                    nadraResponseModel.getMessage() : "Exception in Nadra Response", PopupDialogs.Status.ERROR,
                            this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    goToMainMenu();
                                }
                            });
                }
                else {
                    PopupDialogs.alertDialog(nadraResponseModel.getMessage() != null ?
                                    nadraResponseModel.getMessage() : "Exception in Nadra Response", PopupDialogs.Status.ERROR,
                            this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                }
                            });
                }
            } else {
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
        Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }

    private void openConventionalAccountDialog() {

        PopupDialogs.conventionalAccountOpeningDialog(OpenAccountBvsActivity.this,
                new PopupDialogs.OnCustomClickListener() {
                    @Override
                    public void onClick(View v, Object obj) {

                        if (v.getId() == R.id.btnYes) {
                            if (!discrepantCustomer) {
                                Intent intent = new Intent(OpenAccountBvsActivity.this,
                                        OpenAccountSecondInputActivity.class);
                                intent.putExtra(Constants.IntentKeys.CAME_FROM_BVS, true);
                                intent.putExtra(Constants.ATTR_CMSISDN, mobileNumber);
                                intent.putExtra(Constants.ATTR_CNIC, cnic);
                                intent.putExtra(Constants.ATTR_DEPOSIT_AMT, strInitialDeposit);
                                startActivity(intent);
                            } else {
                                Intent intent;

                                if (discrepantImagesFlags.size() > 0)
                                    intent = new Intent(OpenAccountBvsActivity.this,
                                            OpenAccountPicInputUploadActivity.class);
                                else
                                    intent = new Intent(OpenAccountBvsActivity.this,
                                            OpenAccountConfirmationActivity.class);

                                intent.putExtra(Constants.IntentKeys.CAME_FROM_BVS, true);
                                intent.putExtra(Constants.ATTR_CMSISDN, discrepantCustMob);
                                intent.putExtra(Constants.ATTR_CNIC, discrepantCustCnic);
                                intent.putExtra(Constants.ATTR_CNAME, discrepantCustName);
                                intent.putExtra(Constants.ATTR_CDOB, discrepantCustDob);
                                intent.putExtra(Constants.ATTR_CNIC_EXP, discrepantCustCnicExp);
                                intent.putExtra(Constants.ATTR_CNIC_EXP_FORMATED, discrepantCustCnicExp);
                                intent.putExtra(Constants.ATTR_CDOB_FORMATED, discrepantCustDob);
                                intent.putExtra(Constants.ATTR_DEPOSIT_AMT, strInitialDeposit);
                                intent.putExtra(Constants.ATTR_CUST_ACC_TYPE, "1");
                                intent.putExtra(Constants.IntentKeys.RCMOB, customerMobileNumber.getText().toString());
                                intent.putExtra(Constants.ATTR_CREG_STATE, cusRegState);
                                intent.putExtra(Constants.ATTR_MOBILE_NETWORK, selectedMobileNetwork);
                                intent.putExtra(Constants.ATTR_CREG_STATE_ID, cusRegStateId);
                                intent.putIntegerArrayListExtra(Constants.IntentKeys.OPEN_ACCOUNT_IMAGES_LIST, discrepantImagesFlags);
                                startActivity(intent);
                            }
                        } else if (v.getId() == R.id.btnRetry) {
                            if (strErrorCode.equals("121") || strErrorCode.equals("122"))
                                handleIndexesError();
                            else
                                handleOtherError();
                        }
                    }
                });
    }

    private boolean validate() {
        boolean validate = false;
        mobileNumber = customerMobileNumber.getText().toString();
        confirmMobileNumber = confirmCustomerMobileNumber.getText().toString();
        cnic = customerCNIC.getText().toString();
        confirmCnic = confirmCustomerCNIC.getText().toString();
        strInitialDeposit = initialDeposit.getText().toString();
        customerembossingName = cardEmbossingName.getText().toString();
        customerMailingAddress = mailingAddress.getText().toString();
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
        if (cardEmbossingName.getVisibility() == View.VISIBLE) {
            if (!Utility.testValidity(cardEmbossingName)) {
                return validate;
            }
        }
        if (mailingAddress.getVisibility() == View.VISIBLE) {
            if (!Utility.testValidity(mailingAddress)) {
                return validate;
            }
        }
        if (confirmCustomerMobileNumber.getVisibility() == View.VISIBLE) {
            if (!Utility.testValidity(confirmCustomerMobileNumber)) {
                return validate;
            } else if (confirmCustomerMobileNumber.getText().toString().length() < 11) {
                confirmCustomerMobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER);
                return validate;
            } else if ((confirmMobileNumber.charAt(0) != '0' || confirmMobileNumber.charAt(1) != '3')) {
                confirmCustomerMobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER_FORMAT);
                return validate;
            } else if (!(mobileNumber).equals(confirmMobileNumber)) {
                Toast.makeText(OpenAccountBvsActivity.this,
                        AppMessages.MOBILE_NO_MISMATCH,
                        Toast.LENGTH_SHORT).show();
                return validate;
            }
        } else if (!Utility.testValidity(customerCNIC)) {
            return validate;
        } else if (customerCNIC.getText().toString().length() < 13) {
            customerCNIC.setError(AppMessages.INVALID_CNIC);
            return validate;
        }
        if (confirmCustomerCNIC.getVisibility() == View.VISIBLE) {
            if (!Utility.testValidity(confirmCustomerCNIC)) {
                return validate;
            } else if (confirmCustomerCNIC.getText().toString().length() < 13) {
                confirmCustomerCNIC.setError(AppMessages.INVALID_CNIC);
                return validate;
            } else if (!(cnic).equals(confirmCnic)) {
                Toast.makeText(OpenAccountBvsActivity.this,
                        AppMessages.CNIC_MISMATCH,
                        Toast.LENGTH_SHORT).show();
                return validate;
            }
        }

        if (cnicExpiryLayout.getVisibility() == View.VISIBLE) {
            if (tv_cnicExpiry.getText().toString().equals(getString(R.string.select_date))) {
                Toast.makeText(this, AppMessages.SELECT_CNIC_EXPIRY, Toast.LENGTH_SHORT).show();
                return validate;
            }
        }

        if (initialDeposit.getVisibility() == View.VISIBLE) {
            if (!Utility.testValidity(initialDeposit)) {
                return validate;
            } else if (Double.parseDouble(initialDeposit.getText().toString()) < 0) {
                Toast.makeText(this, AppMessages.INVALID_INITIAL_DEPOSIT, Toast.LENGTH_SHORT).show();
                return validate;
            } else if (isExciseAndTaxation && Double.parseDouble(initialDeposit.getText().toString()) < Double.parseDouble(amount)) {
                Toast.makeText(this, AppMessages.INVALID_INITIAL_DEPOSIT_AMOUNT + amount, Toast.LENGTH_SHORT).show();
                return validate;
            } else {
                if (product != null && product.getMinamt() != null && product.getMaxamt() != null) {
                    if (Double.parseDouble(initialDeposit.getText().toString()) >
                            Double.parseDouble(Utility.getUnFormattedAmount(product.getMaxamt()))
                            || Double.parseDouble(initialDeposit.getText().toString()) <
                            Double.parseDouble(Utility.getUnFormattedAmount(product.getMinamt()))) {
                        Toast.makeText(this, AppMessages.INVALID_INITIAL_DEPOSIT, Toast.LENGTH_SHORT).show();
                        return validate;
                    } else {
                        validate = true;
                    }
                } else
                    validate = true;
            }
        } else {
            validate = true;
        }

        return validate;
    }

    @Override
    public void onBackPressed() {
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

                Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
                intent.putStringArrayListExtra("fingers", fingers);
                intent.putExtra("validFingerIndexes", fingerIndexes);
                intent.putExtra("code", strErrorCode);
                startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
            } else {
                openBvsDialog();
            }
        } else {
            if (fourFingersReceived) {
                Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
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
            Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
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
            Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
            intent.putStringArrayListExtra("fingers", fingers);
            intent.putExtra("validFingerIndexes", fingerIndexes);
            intent.putExtra("code", strErrorCode);
            startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
        }
    }

    private void verifyOtp() {

        PopupDialogs.otpDialog(PopupDialogs.Status.LOCK, OpenAccountBvsActivity.this,
                new PopupDialogs.OnCustomClickListener() {
                    @Override
                    public void onClick(View v, Object obj) {
                        strOtp = (String) obj;
                        strOtp = AesEncryptor.encrypt(strOtp);

                        currentCommand = Constants.CMD_OPEN_ACCOUNT_OTP_VERIFICATION;
                        processRequest();
                    }
                });

    }

    private void openBvsActivity() {
        Intent intent = new Intent(OpenAccountBvsActivity.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, mobileNumber);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, "Account Opening");
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
    }
    private void setOpenAccountSpinner() {
        selectSegment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                selectedOpenAccountSegment = ApplicationData.listOpenAccountSegments.get(arg0.getSelectedItemPosition()).getSegmentId();
                ApplicationData.selectedOpenAccountSegmentId = selectedOpenAccountSegment;
//                selectedCardType = arg0.getItemAtPosition(position).toString();
//                Toast.makeText(DebitCardIssuance.this, selectedCardType, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}