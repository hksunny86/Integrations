package com.inov8.jsblconsumer.activities.openAccount;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

//import com.google.gson.Gson;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.SplashActivity;
import com.inov8.jsblconsumer.activities.mangers.SmsReceiver;
import com.inov8.jsblconsumer.activities.mangers.SmsUpdater;
//import com.inov8.jsblconsumer.bvs.FingerScanActivity;
//import com.inov8.jsblconsumer.bvs.NadraRestClientActivity;
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
import com.inov8.jsblconsumer.util.Utility;
//import com.paysyslabs.instascan.Fingers;
//import com.paysyslabs.instascan.model.PersonData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.jsblconsumer.util.Constants.REQUEST_CODE_ACCOUNT_OPEN_OTP;
import static com.inov8.jsblconsumer.util.Constants.REQUEST_CODE_FINGER_SCAN;
import static com.inov8.jsblconsumer.util.Constants.REQUEST_CODE_TERMS_CONDITIONS;
import static com.inov8.jsblconsumer.util.Constants.REQUEST_CODE_VERIFY_NADRA;
import static com.inov8.jsblconsumer.util.Constants.RESULT_CODE_FAILED_NADRA_FINGER_INDEXES;
import static com.inov8.jsblconsumer.util.Constants.RESULT_CODE_FAILED_NADRA_OTHER_ERROR;
import static com.inov8.jsblconsumer.util.Constants.RESULT_CODE_FINGERS_EMPTY;
import static com.inov8.jsblconsumer.util.Constants.RESULT_CODE_SUCCESS_NADRA;
import static com.inov8.jsblconsumer.util.Utility.testValidity;

public class OpenAccountBvsActivity extends BaseCommunicationActivity implements View.OnClickListener, SmsUpdater {
    private EditText mobileId, mobileNo;
    String account;
    private Button btnNext, btnCancel;
    private String cnic, mobileNumber, cname, cnicExpiry;
    private String cnicExpiryDate = null;
    private Bundle bundle = new Bundle();
    private String selectedAccountType, otp;
//    private PersonData nadraPersonData;
    private CnicController cnicController;
    private String custDiscrepanyStatus;
    private TextView headerText;

    private String isHRA = "0";

    private int currentCommand;
    private ArrayList<String> fingers;
    private ArrayList<Integer> fingerIndexes;
    private boolean indexesError = false;
    private SmsReceiver mSmsReceiver;
    private boolean isSmsReceiverRegistered = false;
    private EditText etOtp;
    private boolean isHRa = false;
    private String tType;
    private Spinner mobileNetwork;
    private String[] mobileNetworks = {"Jazz", "Ufone", "Telenor", "Zong", "SCOM"};
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private boolean fourFingersReceived = false;
    private boolean discrepantCustomer = false;
    private ArrayList<Integer> discrepantImagesFlags = new ArrayList<>();
    private String discrepantCustName, discrepantCustCnic, discrepantCustMob,
            discrepantCustDob, discrepantCustCnicExp;
    private String discrepantCus = "0";
    private int attemptsCounter = 0;
    private String strErrorCode, strMessage = null;
    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private boolean cameraPermission = false;
    private boolean storagePermission = false;

    private String cardExpriryFormatted;
    private String intentCusName = null, intentCnicExpiry = null;

    private DatePickerDialog datePickerExpiry;
    private int year;
    private int month;
    private int day;
    private TextView cardExpiry;
    private EditText etEmail;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_open_account_bvs);
        ApplicationData.mobileNetwork = null;
        fetchIntent();
        headerImplementation();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if (isHRa) {
                    finish();
                } else {
                    Intent intent = new Intent(OpenAccountBvsActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        setUI();


        setCnicExpiry();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            checkPermissions();
        } else {
            cameraPermission = true;
            storagePermission = true;

        }
        checkPermissionsOS();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        if (currentCommand == Constants.CMD_CUSTOMER_REGISTRATION) {
            try {
                showLoading("Please Wait", "Processing...");
                new HttpAsyncTask(OpenAccountBvsActivity.this).execute(Constants.CMD_CUSTOMER_REGISTRATION + "", mobileNumber, cnic, isHRA);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (currentCommand == Constants.CMD_OPEN_ACCOUNT) {
            try {
                showLoading("Please Wait", "Processing...");

                if (!isHRa) {

                    new HttpAsyncTask(OpenAccountBvsActivity.this).execute(Constants.CMD_OPEN_ACCOUNT + "",

                            cnic, mobileNumber, "1", cardExpiry.getText().toString(), ApplicationData.mobileNetwork, etEmail.getText().toString(),
                            "", "", "", "",
                            "", "", "", "",
                            "", "", isHRA, "");
                } else {
//                    new HttpAsyncTask(OpenAccountBvsActivity.this).execute(Constants.CMD_OPEN_ACCOUNT + "",
//
//                            "", "", nadraPersonData.getName(), cnic, mobileNumber,
//                            Utility.getFormattedDateAll(nadraPersonData.getDateOfBirth()), "2",
//                            nadraPersonData.getExpiryDate() != null ? nadraPersonData.getExpiryDate() : cnicExpiryDate != null ? cnicExpiryDate : "",
//                            "1", nadraPersonData.getBirthPlace(), nadraPersonData.getPresentAddress(), nadraPersonData.getCardExpired(),
//                            nadraPersonData.getFatherHusbandName() != null ? nadraPersonData.getFatherHusbandName() : "",
//                            nadraPersonData.getMotherName() != null ? nadraPersonData.getMotherName() : "",
//                            nadraPersonData.getGender() != null ? nadraPersonData.getGender() : "", discrepantCus, isHRA, ApplicationData.mobileNetwork);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            switch (currentCommand) {

                case Constants.CMD_CUSTOMER_REGISTRATION: {

                    if (table != null && table.containsKey("errs")) {
                        List<?> list = (List<?>) table.get("errs");
                        MessageModel message = (MessageModel) list.get(0);
                        hideLoading();

                        String code = message.getCode();
                        AppLogger.i("##Error Code: " + code);

                        dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


//                        PopupDialogs.createAlertDialog(message.getDescr(),
//                                AppMessages.ALERT_HEADING, OpenAccountBvsActivity.this, PopupDialogs.Status.ERROR);

                    } else {
                        hideLoading();
                        if (table != null) {

                            if (table.get(Constants.ATTR_CUSTOMER_PHOTO_FLAG) != null && ((String) table.get(Constants.ATTR_CUSTOMER_PHOTO_FLAG)).equals("1") || table.get(Constants.ATTR_CNIC_FRONT_PHOTO_FLAG) != null && ((String) table.get(Constants.ATTR_CNIC_FRONT_PHOTO_FLAG)).equals("1")) {
                                discrepantCustomer = true;
                                discrepantCus = "1";
                                cname = (String) table.get(Constants.ATTR_CNAME);
                                cnicExpiry = (String) table.get(Constants.ATTR_CNIC_EXP);

                                // images list
                                discrepantImagesFlags = new ArrayList<Integer>();

                                if (((String) table.get(Constants.ATTR_CUSTOMER_PHOTO_FLAG)).equals("1")) {
                                    discrepantImagesFlags.add(Constants.AccountOpenningImages.CUSTOMER);
                                }

                                if (((String) table.get(Constants.ATTR_CNIC_FRONT_PHOTO_FLAG)).equals("1")) {
                                    discrepantImagesFlags.add(Constants.AccountOpenningImages.CNIC_FRONT);
                                }

                                discrepantCustMob = mobileNumber;
                                discrepantCustCnic = cnic;
                                discrepantCustName = cname;
                                discrepantCustCnicExp = cnicExpiry;
                                discrepantCustDob = (String) table.get(Constants.ATTR_CDOB);
                                openOtpScreen();

                            } else if (table.containsKey(Constants.KEY_LIST_MSGS)) {
                                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                                MessageModel message = (MessageModel) list.get(0);

                            }
//                            else if (table.get(Constants.ATTR_CREG_STATE_ID).equals(Constants.REGISTRATION_STATE_VERIFIED)) {
//                                discrepantCustomer = false;
//                                openOtpScreen();
//                            }
                            else if (table.containsKey(Constants.ATTR_DTID)) {
                                discrepantCustomer = false;
                                openOtpScreen();
                            } else if (table.get(Constants.ATTR_CREG_STATE_ID) != null && table.get(Constants.ATTR_CREG_STATE_ID).equals("3") && table.get(Constants.ATTR_CREG_STATE_ID).equals("3") && table.get(Constants.ATTR_CREG_STATE_ID).equals("3")) {
                                discrepantCustomer = false;
                                openBvsDialog();
                            }
                        }
                    }
                    break;
                }

                case Constants.CMD_OPEN_ACCOUNT: {
                    hideLoading();
                    if (table != null && table.containsKey("errs")) {
                        List<?> list = (List<?>) table.get("errs");
                        MessageModel message = (MessageModel) list.get(0);
                        hideLoading();
                        String code = message.getCode();
                        AppLogger.i("##Error Code: " + code);


                        dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


                    } else if (table != null && table.containsKey("msgs")) {
                        List<?> list = (List<?>) table.get("msgs");
                        MessageModel message = (MessageModel) list.get(0);
//                        finish();
                        ApplicationData.cnic = cnic;
                        ApplicationData.mobileNo = mobileNumber;

                        Intent intent = new Intent(OpenAccountBvsActivity.this, OpenAccountSuccessActivity.class);
                        intent.putExtra(Constants.IntentKeys.MESSAGE, message.getDescr());
                        intent.putExtra(Constants.IntentKeys.HRA, isHRa);
                        finish();
                        startActivity(intent);


//                        dialogGeneral = PopupDialogs.createSuccessDialog(message.getDescr(), this, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGeneral.cancel();
////                                finish();
//                                goToLogin();
//                            }
//                        }, null);
//                        finish();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_VERIFY_NADRA) {
            if (resultCode == RESULT_CODE_SUCCESS_NADRA) {
                if (data != null) {

                    fourFingersReceived = false;
                    exhaustedFingers.clear();
                    attemptsCounter = 0;

                    String json = data.getStringExtra(Constants.ATTR_PERSON_DATA);
//                    nadraPersonData = new Gson().fromJson(json, PersonData.class);
//                    String strCardExpired = nadraPersonData.getCardExpired();

//                    nadraPersonData =new PersonData();
//
//                    nadraPersonData.setBirthPlace("4456546");
//                    nadraPersonData.setCardExpired("4456546");
//                    nadraPersonData.setDateOfBirth("4456546");
//                    nadraPersonData.setExpiryDate("4456546");
//                    nadraPersonData.setFatherHusbandName("4456546");
//                    nadraPersonData.setGender("4456546");
//                    nadraPersonData.setMotherName("4456546");
//                    nadraPersonData.setName("4456546");
//                    nadraPersonData.setPermanentAddress("4456546");
//                    nadraPersonData.setPhotograph("4456546");
//                    nadraPersonData.setPresentAddress("4456546");
//
//                    if (strCardExpired != null && strCardExpired.equalsIgnoreCase("yes")) {
//                        dialogGeneral = PopupDialogs.createAlertDialog(
//                                AppMessages.INVALID_CARD_EXPIRY, AppMessages.ALERT_HEADING, this, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialogGeneral.dismiss();
//                                        goToLogin();
//                                    }
//                                }, PopupDialogs.Status.ERROR);
//                    }
//                    else {
                    currentCommand = Constants.CMD_OPEN_ACCOUNT;
                    processRequest();
//                        askTermsAndConditions();
//                    }
                }
            } else if (resultCode == RESULT_CODE_FAILED_NADRA_FINGER_INDEXES) {
                if (data != null) {
                    fourFingersReceived = true;
                    fingers = data.getStringArrayListExtra("fingers");
                    fingerIndexes = data.getIntegerArrayListExtra("validFingerIndexes");
                    strErrorCode = data.getStringExtra("code");
                    strMessage = data.getStringExtra("msg");

                    attemptsCounter++;
//                    if (attemptsCounter < 2) {
//                        handleIndexesError();
//                    } else if (isHRa) {
//                        handleIndexesError();
//                    } else {
                        openConventionalAccountDialog();
                    }

                }
            } else if (resultCode == RESULT_CODE_FAILED_NADRA_OTHER_ERROR) {
                if (data != null) {


                    strErrorCode = data.getStringExtra("code");
                    strMessage = data.getStringExtra("msg");

//                    if (!strErrorCode.equals("700") && !strErrorCode.equals("500"))

                    if (!strErrorCode.equals("700"))
                        attemptsCounter++;

                    if (!strErrorCode.equals("118")) {


                        dialogGeneral = popupDialogs.createAlertDialog(strMessage, AppMessages.ALERT_ERROR,
                                OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.dismiss();
//                                        if (attemptsCounter < 2)
//                                            handleOtherError();
//
//                                        else if (isHRa) {
//                                            handleIndexesError();
//                                        } else
                                            openConventionalAccountDialog();

                                    }
                                }, false, null, false, null);

                    } else {
//                        if (attemptsCounter < 2)
//                            handleOtherError();

//                        else if (isHRa) {
//                            handleIndexesError();
//                        } else
                            openConventionalAccountDialog();
//                    }

                }
            }

        } else if (requestCode == REQUEST_CODE_FINGER_SCAN) {
            if (resultCode == RESULT_OK) {
//                Intent intent = new Intent(OpenAccountBvsActivity.this, NadraRestClientActivity.class);
//                Bundle extras = data.getExtras();
//                Fingers finger = null;
//                if (extras != null) {
//                    finger = (Fingers) extras.get(Constants.IntentKeys.FINGER);
//                }
//                intent.putExtra(Constants.IntentKeys.FINGER, finger);
//                intent.putExtra(Constants.IntentKeys.IDENTIFIER, cnic != null ? cnic : "");
//                intent.setData(null);
//                startActivityForResult(intent, REQUEST_CODE_VERIFY_NADRA);
            } else if (resultCode == RESULT_CODE_FINGERS_EMPTY) {
                exhaustedFingers.clear();
                fourFingersReceived = false;
                openBvsDialog();
            }
        } else if (requestCode == REQUEST_CODE_TERMS_CONDITIONS && resultCode == RESULT_OK) {
            currentCommand = Constants.CMD_OPEN_ACCOUNT;
            processRequest();
        } else if (requestCode == REQUEST_CODE_ACCOUNT_OPEN_OTP && resultCode == RESULT_OK) {


            if (isHRa) {
                openBvsDialog();
            } else {
                currentCommand = Constants.CMD_OPEN_ACCOUNT;
                processRequest();
            }

            //Rehan Aslam

//
        }
//        if(requestCode == Constants.CMD_DEBIT_CARD) {
//            Intent i = new Intent(OpenAccountBvsActivity.this, DebitCardIssuance.class);
//            i.putExtra(Constants.IntentKeys.LIST_CATEGORIES, ApplicationData.listCategories);
//            i.putExtra(Constants.IntentKeys.MENU_ITEM_POS, catPostion);
//            startActivity(i);
//        }
    }

    private void openOtpScreen() {
        Intent intent = new Intent(OpenAccountBvsActivity.this, OpenAccountBvsOtpActivity.class);
        intent.putExtra(Constants.IntentKeys.MOBILE_NO, mobileNumber);
        intent.putExtra(Constants.IntentKeys.CNIC, cnic);
        intent.putExtra(Constants.IntentKeys.HRA, isHRa);
        startActivityForResult(intent, REQUEST_CODE_ACCOUNT_OPEN_OTP);
    }

    public void askTermsAndConditions() {
        Intent intent = new Intent(OpenAccountBvsActivity.this, TermsConditionsJsActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TERMS_CONDITIONS);
    }

    private void openBvsDialog() {
        fingers = new ArrayList<>();
//        Fingers[] list = Fingers.values();
//        for (Fingers finger : list) {
//            fingers.add(finger.getValue());
//        }
//        Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
//        intent.putStringArrayListExtra("fingers", fingers);
//        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }

    private void setUI() {


        titleImplementation("icon_customer_registration", "Account Opening", null, this);
        btnHome.setVisibility(View.GONE);
        btnSignout.setVisibility(View.GONE);

        findViewById(R.id.ivHeaderLogo).setVisibility(View.GONE);
        headerText = (TextView) findViewById(R.id.headerText);
        headerText.setVisibility(View.VISIBLE);


        mobileId = (EditText) findViewById(R.id.etMobileId);
        mobileNo = (EditText) findViewById(R.id.etMobileNo);

        mobileNetwork = findViewById(R.id.spinner_mobile_network);

        etEmail = (EditText) findViewById(R.id.etEmail);
        if (isHRa) {
            headerText.setText("Update L0 A/C");
            findViewById(R.id.mobile_hint).setVisibility(View.GONE);
            findViewById(R.id.tvMobileHint).setVisibility(View.GONE);
            mobileId.setVisibility(View.GONE);
            mobileNo.setVisibility(View.GONE);
            etEmail.setVisibility(View.GONE);
            mobileNetwork.setVisibility(View.GONE);
            findViewById(R.id.expiry_date_heading).setVisibility(View.GONE);
            findViewById(R.id.tvEmailHint).setVisibility(View.GONE);
            findViewById(R.id.tvNetwork).setVisibility(View.GONE);
            findViewById(R.id.rl_cnic).setVisibility(View.GONE);


        } else {

            headerText.setText(getString(R.string.open_account));
        }


        disableCopyPaste(mobileId);
        disableCopyPaste(mobileNo);
//        mobileNo.setEnabled(false);

        cnicController = new CnicController(findViewById(R.id.cnicView), true, true, this);

        TextView lblCnic = (TextView) findViewById(R.id.lblCnic);
        lblCnic.setText("CNIC *");
        btnNext = (Button) findViewById(R.id.btnNext);
//        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnNext.setOnClickListener(this);
//        btnCancel.setOnClickListener(this);

        ArrayAdapter<String> networkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mobileNetworks);
        networkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mobileNetwork.setAdapter(networkAdapter);

        mobileNetwork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ApplicationData.mobileNetwork = mobileNetworks[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ApplicationData.mobileNetwork = mobileNetworks[0];
            }
        });

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.btnCancel: {
//                goToLogin();
//                break;
//            }

            case R.id.btnNext:

                hideKeyboard(v);
                if (validate()) {

                    if (!cameraPermission) {
                        checkPermissions();
                        return;
                    }

                    if (!storagePermission) {
                        checkPermissions();
                        return;
                    }


                    if (!cnicController.isValidCnic()) {


                        dialogGeneral = popupDialogs.createAlertDialog("Invalid CNIC", AppMessages.ALERT_HEADING,
                                OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


                        return;
                    }

                    if (!ApplicationData.isBvsEnabledDevice) {


                        dialogGeneral = popupDialogs.createAlertDialog(ApplicationData.bvsErrorMessage, AppMessages.ALERT_HEADING,
                                OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


                        return;
                    }

                    if (!isHRa) {
                        if (!testValidity(etEmail)) {
                            return;
                        }

                        if (!Utility.isValidEmail(etEmail)) {
                            return;
                        }

                        if (TextUtils.isEmpty(cardExpiry.getText().toString())) {
                            dialogGeneral = popupDialogs.createAlertDialog("Select CNIC Issue Date.", AppMessages.ALERT_HEADING,
                                    OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                        }
                                    }, false, PopupDialogs.Status.ERROR, false, null);
                            return;
                        }
                    }
                    cnic = cnicController.getCnic();
                    currentCommand = Constants.CMD_CUSTOMER_REGISTRATION;
                    attemptsCounter = 0;

                    processRequest();


                }
        }
    }

    private boolean validate() {


        if (isHRa) {
            mobileNumber = ApplicationData.mobileNo;
            return true;
        } else {
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

    private void checkPermissions() {

        if (Build.VERSION.SDK_INT < 23) {
            cameraPermission = true;
            storagePermission = true;
            return;
        }

        int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
        int hasStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        } else {
            cameraPermission = true;
            if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            } else {
                storagePermission = true;
            }
        }
    }

    public void checkPermissionsOS() {
        if (Build.VERSION.SDK_INT >= 19)  // kitkat check
        {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))  // camera check
            {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) // camera autofocus check
                {
                    ApplicationData.isBvsEnabledDevice = true;
                } else {
                    ApplicationData.isBvsEnabledDevice = false;
                    ApplicationData.bvsErrorMessage = AppMessages.CAMERA_AUTO_FOCUS_ERROR;
                }
            } else {
                ApplicationData.isBvsEnabledDevice = false;
                ApplicationData.bvsErrorMessage = AppMessages.CAMERA_NOT_AVAILABLE_ERROR;
            }
        } else {
            ApplicationData.isBvsEnabledDevice = false;
            ApplicationData.bvsErrorMessage = AppMessages.OS_ERROR;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {

                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            cameraPermission = true;
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        } else {
                            // Permission Denied
                            //Toast.makeText(OpenAccountMobileInputActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();

                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

                                dialogGeneral = popupDialogs.createAlertDialog("You need to allow access to Camera to take Photos for Account Opening.", AppMessages.ALERT_HEADING,
                                        OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogGeneral.dismiss();
                                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                                        REQUEST_CODE_ASK_PERMISSIONS);
                                            }
                                        }, false, PopupDialogs.Status.ERROR, false, null);

//                                dialogGeneral = PopupDialogs.createConfirmationDialog("You need to allow access to Camera to take Photos for Account Opening.",
//                                        getString(R.string.alertNotification),
//                                        OpenAccountBvsActivity.this, new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                dialogGeneral.cancel();
//                                                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                                        REQUEST_CODE_ASK_PERMISSIONS);
//                                            }
//                                        });
//

                            } else {

                                dialogGeneral = popupDialogs.createAlertDialog("Please allow camera permissions from app settings to proceed further.", AppMessages.ALERT_HEADING,
                                        OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogGeneral.dismiss();
                                                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(myAppSettings);
                                            }
                                        }, false, PopupDialogs.Status.ERROR, false, null);


//                                dialogGeneral = PopupDialogs.createAlertDialog(
//                                        "Please allow camera permissions from app settings to proceed further.", AppMessages.ALERT_HEADING, this, new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                dialogGeneral.dismiss();
//                                                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
//                                                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
//                                                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                startActivity(myAppSettings);
//
//
//                                            }
//                                        }, PopupDialogs.Status.ERROR);


//                                Toast.makeText(getApplicationContext(), "Please allow camera permissions from app settings to proceed further.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            storagePermission = true;
                        } else {
                            // Permission Denied
                            //Toast.makeText(OpenAccountMobileInputActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();

                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


                                dialogGeneral = popupDialogs.createAlertDialog("You need to allow Storage access to save Account Opening Photos", AppMessages.ALERT_HEADING,
                                        OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogGeneral.cancel();
                                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                        REQUEST_CODE_ASK_PERMISSIONS);
                                            }
                                        }, false, PopupDialogs.Status.ERROR, false, null);


//                                dialogGeneral = PopupDialogs.createConfirmationDialog("You need to allow Storage access to save Account Opening Photos",
//                                        getString(R.string.alertNotification),
//                                        OpenAccountBvsActivity.this, new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                dialogGeneral.cancel();
//                                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                                        REQUEST_CODE_ASK_PERMISSIONS);
//                                            }
//                                        });

                            } else {


                                dialogGeneral = popupDialogs.createAlertDialog("Please allow storage permissions from app settings to proceed further.", AppMessages.ALERT_HEADING,
                                        OpenAccountBvsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogGeneral.dismiss();
                                                dialogGeneral.dismiss();
                                                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(myAppSettings);
                                            }
                                        }, false, PopupDialogs.Status.ERROR, false, null);


//                                dialogGeneral = PopupDialogs.createAlertDialog(
//                                        "Please allow storage permissions from app settings to proceed further.", AppMessages.ALERT_HEADING, this, new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                dialogGeneral.dismiss();
//                                                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
//                                                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
//                                                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                startActivity(myAppSettings);
//
//
//                                            }
//                                        }, PopupDialogs.Status.ERROR);
//                                Toast.makeText(getApplicationContext(), "Please allow storage permissions from app settings to proceed further", Toast.LENGTH_SHORT).show();
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


    private void fetchIntent() {
        mBundle = getIntent().getExtras();
        isHRa = getIntent().getBooleanExtra(Constants.IntentKeys.HRA, false);
        if (isHRa) {
            isHRA = "1";
        }
    }

//    private void handleIndexesError() {
//        if (exhaustedFingers.size() == 0) {
//            Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
//            intent.putStringArrayListExtra("fingers", fingers);
//            intent.putExtra("validFingerIndexes", fingerIndexes);
//            intent.putExtra("code", strErrorCode);
//            startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
//        } else {
//            for (int i = 0; i < exhaustedFingers.size(); i++) {
//                for (int j = 0; j < fingers.size(); j++) {
//                    if (fingers.get(j).equals(exhaustedFingers.get(i))) {
//                        fingers.remove(j);
//                        break;
//                    }
//                }
//            }
//            Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
//            intent.putStringArrayListExtra("fingers", fingers);
//            intent.putExtra("validFingerIndexes", fingerIndexes);
//            intent.putExtra("code", strErrorCode);
//            startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
//        }
//    }


//    private void handleOtherError() {
//
//        if (strErrorCode.equals("118")) {
//            if (fourFingersReceived) {
//                Fingers currentFinger = ApplicationData.currentFinger;
//                for (int i = 0; i < fingers.size(); i++) {
//                    if (fingers.get(i).equals(currentFinger.getValue())) {
//                        exhaustedFingers.add(exhaustedFingers.size(), fingers.get(i));
//                        fingers.remove(i);
//                        break;
//                    }
//                }
//
//                Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
//                intent.putStringArrayListExtra("fingers", fingers);
//                intent.putExtra("validFingerIndexes", fingerIndexes);
//                intent.putExtra("code", strErrorCode);
//                startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
//            } else {
//                openBvsDialog();
//            }
//        } else {
//            if (fourFingersReceived) {
//                Intent intent = new Intent(OpenAccountBvsActivity.this, FingerScanActivity.class);
//                intent.putStringArrayListExtra("fingers", fingers);
//                intent.putExtra("validFingerIndexes", fingerIndexes);
//                intent.putExtra("code", strErrorCode);
//                startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
//            } else {
//                openBvsDialog();
//            }
//        }
//    }

    private void openConventionalAccountDialog() {

        dialogGeneral = popupDialogs.createConfirmationDialog(getString(R.string.open_l0_account), AppMessages.ALERT_HEADING,
                OpenAccountBvsActivity.this, getString(R.string.yes), getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();
                        if (!discrepantCustomer) {
                            Intent intent = new Intent(OpenAccountBvsActivity.this,
                                    OpenAccountSecondInputActivity.class);
                            intent.putExtra(Constants.IntentKeys.CAME_FROM_BVS, true);
                            intent.putExtra(Constants.ATTR_CMSISDN, mobileNumber);
                            intent.putExtra(Constants.ATTR_CNIC, cnic);
//                                intent.putExtra(Constants.ATTR_DEPOSIT_AMT, strInitialDeposit);
                            startActivity(intent);
                            finish();
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
//                                intent.putExtra(Constants.ATTR_DEPOSIT_AMT, strInitialDeposit);
                            intent.putExtra(Constants.ATTR_CUST_ACC_TYPE, "1");
                            intent.putExtra(Constants.IntentKeys.DISCREPANT, discrepantCus);


                            intent.putIntegerArrayListExtra
                                    (Constants.IntentKeys.OPEN_ACCOUNT_IMAGES_LIST, discrepantImagesFlags);
                            startActivity(intent);
                            finish();
                        }

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();


//                        if (strErrorCode.equals("121") || strErrorCode.equals("122"))
//                            handleIndexesError();
//                        else
//                            handleOtherError();


                    }
                }, false, PopupDialogs.Status.ALERT, false);

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
    public void gotSms(String otp) {


        if (otp != null && etOtp != null) {
            etOtp.setText(otp);
            unregisterSmsReceiver();
        }
        this.otp = otp;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (isHRa) {
            finish();
        } else {
            Intent intent = new Intent(OpenAccountBvsActivity.this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void setCnicExpiry() {

        setDatePicker();
        View card_expiry_layout = findViewById(R.id.rl_cnic);
        card_expiry_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (datePickerExpiry != null && !datePickerExpiry.isShowing()) {
                    datePickerExpiry.show();
                }
            }
        });


    }

    private void setDatePicker() {

        final Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.YEAR, -20);
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);

        final long minDate, maxdate;
        minDate = c1.getTimeInMillis();


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        maxdate = c.getTimeInMillis();
        cardExpiry = (TextView) findViewById(R.id.tv_dateExpiry);


        datePickerExpiry = new DatePickerDialog(
                OpenAccountBvsActivity.this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        cardExpiry.setText(dayOfMonth + "-" + (monthOfYear + 1)
                                + "-" + year);
                        String monthst = (monthOfYear + 1) + "";
                        String day = dayOfMonth + "";

                        if ((dayOfMonth + "").length() == 1) {
                            day = "0" + dayOfMonth;
                        }
                        if (((monthOfYear + 1) + "").length() == 1) {
                            monthst = "0" + (monthOfYear + 1);
                        }

                        cardExpriryFormatted = year + "-" + monthst + "-" + day;
                    }
                }, year, month, day);

        datePickerExpiry.getDatePicker().setMinDate(minDate);
        datePickerExpiry.getDatePicker().setMaxDate(maxdate);

//
//        View card_expiry_layout = findViewById(R.id.rl_cnic);
//        card_expiry_layout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (datePickerExpiry != null && !datePickerExpiry.isShowing()) {
//                    datePickerExpiry.show();
//                }
//            }
//        });

        //dob.setText(day + "-" + (month + 1) + "-" + year);

        String monthst = (month + 1) + "";
        String daylocal = day + "";

        if ((day + "").length() == 1) {
            daylocal = "0" + day;
        }
        if (((month + 1) + "").length() == 1) {
            monthst = "0" + (month + 1);
        }
        cardExpriryFormatted = year + "-" + monthst + "-" + daylocal;

//        }
    }

}

