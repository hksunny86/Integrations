package com.inov8.agentmate.activities.debitCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.model.CategoryModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
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
import com.inov8.jsbl.sco.R;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;

public class DebitCardIssuance extends BaseCommunicationActivity implements View.OnClickListener {


    private EditText customerMobileNumber, customerCNIC, cardEmbossingName, mailingAddress;

    private Button btnNext;
    private TextView lblHeading, tv_mobileNo, tv_cnic, tv_card_embossing_name, tv_mailing_address;
    private String cnic, mobileNumber,
            strOtp, customerembossingName, customerMailingAddress;
    private String encMPIN;
    private int currentCommand;
    private ArrayList<String> fingers;
    private boolean discrepantCustomer = false;
    private boolean otpVerified = false;
    private int menuItemPosition;
    private CheckBox chkFee;
    private double fee;
    private ArrayList<CategoryModel> listCategories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_card_issuance);

        ApplicationData.isTermsAndConditionAccepted = false;
        btnNext = (Button) findViewById(R.id.btnNext);
        cardEmbossingName = (EditText) findViewById(R.id.input_card_embossing_name);
        mailingAddress = (EditText) findViewById(R.id.input_mailing_address);
        lblHeading = (TextView) findViewById(R.id.lblHeading);
        chkFee = (CheckBox) findViewById(R.id.chk_fee);
        tv_card_embossing_name = (TextView) findViewById(R.id.lbl_card_embossing_name);
        tv_mailing_address = (TextView) findViewById(R.id.lbl_mailing_address);
        tv_mobileNo = (TextView) findViewById(R.id.tv_customer_mobile);
        tv_cnic = (TextView) findViewById(R.id.tv_customer_cnic);
        customerMobileNumber = (EditText) findViewById(R.id.input_customer_mobile);
        customerCNIC = (EditText) findViewById(R.id.input_customer_cnic);
        if (!ApplicationData.isLoginFlow) {
            reSetUI();
            tv_mobileNo.setText(ApplicationData.mobileNo);
            tv_cnic.setText(ApplicationData.cnic);
            ApplicationData.isTermsAndConditionAccepted = true;

            currentCommand = Constants.CMD_DEBIT_CARD;
            mobileNumber = ApplicationData.mobileNo;
            cnic = ApplicationData.cnic;
            processRequest();
        }
        lblHeading.setText("Debit Card Request");
        btnNext.setOnClickListener(this);
        headerImplementation();

    }

    private void reSetUI() {

        currentCommand = Constants.CMD_DEBIT_CARD_CONFIRMATION;
        tv_mobileNo.setText(customerMobileNumber.getText());
        tv_cnic.setText(customerCNIC.getText());
        tv_mobileNo.setVisibility(View.VISIBLE);
        tv_cnic.setVisibility(View.VISIBLE);
        tv_card_embossing_name.setVisibility(View.VISIBLE);
        tv_mailing_address.setVisibility(View.VISIBLE);
        cardEmbossingName.setVisibility(View.VISIBLE);
        mailingAddress.setVisibility(View.VISIBLE);
        if (ApplicationData.isLoginFlow && fee > 0.0) {
            chkFee.setVisibility(View.VISIBLE);
            chkFee.setText("On debit card issuance " + fee + " PKR fee will be applied.");
        }
        customerMobileNumber.setVisibility(View.GONE);
        customerCNIC.setVisibility(View.GONE);
    }

    //    private void fetchIntents() {
//        menuItemPosition = mBundle.getInt(Constants.IntentKeys.MENU_ITEM_POS);
//        listCategories = new ArrayList<CategoryModel>();
//        listCategories = (ArrayList<CategoryModel>) mBundle.get(Constants.IntentKeys.LIST_CATEGORIES);
//
//
//    }
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        switch (v.getId()) {

            case R.id.btnNext:
                hideKeyboard(v);

                if (ApplicationData.isLoginFlow) {
                    mobileNumber = customerMobileNumber.getText().toString();
                    cnic = customerCNIC.getText().toString();
                } else {
                    mobileNumber = ApplicationData.mobileNo;
                    cnic = ApplicationData.cnic;
                }


                if (validate()) {
                    customerembossingName = cardEmbossingName.getText().toString().toUpperCase();
                    customerMailingAddress = mailingAddress.getText().toString();
                    if (!ApplicationData.isTermsAndConditionAccepted) {
                        currentCommand = Constants.CMD_DEBIT_CARD;
                        processRequest();
                    } else {
                        if (!chkFee.isChecked() && fee > 0.0)
                            Toast.makeText(this, "Your consent is required for debit card issuance fee deduction. kindly check the check box.", Toast.LENGTH_SHORT).show();
                        else {
                            if (!otpVerified) {
                                verifyOtp();
                            } else {
                                askMpin(mBundle, null, false);
                            }
                        }
                    }
                }
        }
    }


    @Override
    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        encMPIN = getEncryptedMpin();
        currentCommand = Constants.CMD_DEBIT_CARD_CONFIRMATION;
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

            if (currentCommand == Constants.CMD_OPEN_ACCOUNT_OTP_VERIFICATION) {
                new HttpAsyncTask(DebitCardIssuance.this)
                        .execute(Constants.CMD_OPEN_ACCOUNT_OTP_VERIFICATION + "",
                                mobileNumber, cnic, "0", strOtp);
            } else if (currentCommand == Constants.CMD_DEBIT_CARD) {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_DEBIT_CARD + "", mobileNumber, cnic);
            } else if (currentCommand == Constants.CMD_DEBIT_CARD_CONFIRMATION) {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_DEBIT_CARD_CONFIRMATION + "", encMPIN, mobileNumber, cnic, customerembossingName, customerMailingAddress);
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
                if (code.equals(Constants.ErrorCodes.OTP_INVALID)) {
                    PopupDialogs.createAlertDialog(message.getDescr(),
                            AppMessages.ALERT_HEADING, PopupDialogs.Status.ERROR,
                            DebitCardIssuance.this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    verifyOtp();
                                }
                            });
                } else if (code.equals(Constants.ErrorCodes.INSUFFICIENT_BALANCE_ERROR)) {
                    PopupDialogs.createAlertDialog(message.getDescr(),
                            AppMessages.ALERT_HEADING, PopupDialogs.Status.ERROR,
                            DebitCardIssuance.this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    goToMainMenu();
                                }
                            });
                } else {
                    createAlertDialog(message.getDescr(),
                            AppMessages.ALERT_HEADING);
                }
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
            } else {
                switch (currentCommand) {

                    case Constants.CMD_OPEN_ACCOUNT_OTP_VERIFICATION: {
                        if (table.containsKey(Constants.ATTR_DTID)) {
                            otpVerified = true;
                            askMpin(mBundle, null, false);
                        }
                    }
                    break;
                    case Constants.CMD_DEBIT_CARD: {
                        if (ApplicationData.isLoginFlow) {
                            if (table.containsKey(Constants.ATTR_CNIC)) {
                                Intent intent = new Intent(DebitCardIssuance.this, TermsConditionsDebitCardActivity.class);
                                startActivityForResult(intent, Constants.CMD_DEBIT_CARD);
//                            verifyOtp();
                            }
                        }
                        try {
                            fee = Double.parseDouble(table.get(Constants.ATTR_FEE).toString());
                            if (0.0 < fee) {
                                chkFee.setVisibility(View.VISIBLE);
                                chkFee.setText("On debit card issuance " + fee + " PKR fee will be applied.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    break;

                }
            }

        } catch (Exception e) {
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
        Intent intent = new Intent(DebitCardIssuance.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }


    private boolean validate() {
        boolean validate = false;

        customerembossingName = cardEmbossingName.getText().toString();
        customerMailingAddress = mailingAddress.getText().toString();

        if (customerMobileNumber.getVisibility() == View.VISIBLE) {
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
        if (customerCNIC.getVisibility() == View.VISIBLE) {
            if (!Utility.testValidity(customerCNIC)) {
                return validate;
            } else if (customerCNIC.getText().toString().length() < 13) {
                customerCNIC.setError(AppMessages.INVALID_CNIC);
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
        goToMainMenu();
    }


    private void verifyOtp() {

        PopupDialogs.otpDialog(PopupDialogs.Status.LOCK, DebitCardIssuance.this,
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
        Intent intent = new Intent(DebitCardIssuance.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, mobileNumber);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, "Account Opening");
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == Constants.CMD_DEBIT_CARD) {
            if (ApplicationData.isTermsAndConditionAccepted) {
                currentCommand = Constants.CMD_DEBIT_CARD_CONFIRMATION;
                reSetUI();
            }
//            Intent i = new Intent(MainMenuActivity.this,DebitCardIssuance.class);
//            i.putExtra(Constants.IntentKeys.LIST_CATEGORIES, ApplicationData.listCategories);
//            i.putExtra(Constants.IntentKeys.MENU_ITEM_POS, catPostion);
//            startActivity(i);
        }
    }
}