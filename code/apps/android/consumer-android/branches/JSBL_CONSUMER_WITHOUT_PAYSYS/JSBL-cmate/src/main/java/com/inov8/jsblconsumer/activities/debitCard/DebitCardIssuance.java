package com.inov8.jsblconsumer.activities.debitCard;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DebitCardIssuance extends BaseCommunicationActivity implements View.OnClickListener {

    private TextView lblHeading, tv_mobileNo, tv_cnic, tv_cnic_heading, mobile_hint, cnic_hint;
    private ArrayList<CategoryModel> listCategories;
    private CategoryModel categoryModel;
    private int menuItemPosition;
    private EditText mobileId, mobileNo, cardEmbossingName, mailingAddress;
    private String encMPIN;
    private Button btnNext;
    private String cnic, mobileNumber, customerembossingName, customerMailingAddress;

    private CheckBox cbFee;


    private int currentCommand;
    private boolean isHRa = false;

    private int attemptsCounter = 0;
    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private boolean cameraPermission = false;
    private boolean storagePermission = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_debit_card_issuance);

        lblHeading = (TextView) findViewById(R.id.lblHeading);
        cbFee = (CheckBox) findViewById(R.id.cbFee);


        if (ApplicationData.fee != null) {
            cbFee.setVisibility(View.VISIBLE);
            cbFee.setText("On debit card issuance " + ApplicationData.fee + " PKR fee will be applied.");
        }

        mobileId = (EditText) findViewById(R.id.etMobileId);
        mobileNo = (EditText) findViewById(R.id.etMobileNo);
        cardEmbossingName = (EditText) findViewById(R.id.etEmbosingName);
        mailingAddress = (EditText) findViewById(R.id.etMailingAddress);
        mobile_hint = (TextView) findViewById(R.id.mobile_hint);
        tv_mobileNo = (TextView) findViewById(R.id.tv_mobile_No);
        tv_cnic = (TextView) findViewById(R.id.tv_cnic);
        tv_mobileNo.setText(ApplicationData.mobileNo);
        tv_cnic.setText(ApplicationData.cnic);
        headerImplementation();
        if (!ApplicationData.isLogin) {
            View v = (View) findViewById(R.id.bottomBarQR);
            v.setVisibility(View.GONE);
            View logout = (View) findViewById(R.id.btnSignout);
            logout.setVisibility(View.GONE);
            View bottomBar = (View) findViewById(R.id.bottomBar);
            bottomBar.setVisibility(View.GONE);
            lblHeading.setText("Debit Card Issuance");
        }

        if (ApplicationData.isLogin) {
            fetchIntents();
            checkSoftKeyboardD();
            categoryModel = listCategories.get(menuItemPosition);
            lblHeading.setText(categoryModel.getName());
            bottomBarImplementation(DebitCardIssuance.this, categoryModel.getId());
        }

        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

    }

    private void fetchIntents() {
        menuItemPosition = mBundle.getInt(Constants.IntentKeys.MENU_ITEM_POS);
        listCategories = new ArrayList<CategoryModel>();
        listCategories = (ArrayList<CategoryModel>) mBundle.get(Constants.IntentKeys.LIST_CATEGORIES);


    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnNext:

                hideKeyboard(v);


                if (validate()) {

                    currentCommand = Constants.CMD_DEBIT_CARD_CONFIRMATION;
                    attemptsCounter = 0;
                    customerembossingName = cardEmbossingName.getText().toString().toUpperCase();
                    customerMailingAddress = mailingAddress.getText().toString();
                    mobileNumber = tv_mobileNo.getText().toString();
                    cnic = tv_cnic.getText().toString();

                    if (cbFee.getVisibility() == View.VISIBLE && !cbFee.isChecked()){
                        Toast.makeText(DebitCardIssuance.this,"Your consent is required for debit card issuance fee deduction .kindly check the check box.",Toast.LENGTH_LONG).show();
                        return;
                    }


                        if (ApplicationData.isLogin) {
                            askMpin(mBundle, null, false);

                        } else {
                            processRequest();
                        }


                }
        }
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    DebitCardIssuance.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }
        if (currentCommand == Constants.CMD_DEBIT_CARD) {
            try {
                showLoading("Please Wait", "Processing...");
                new HttpAsyncTask(DebitCardIssuance.this).execute(Constants.CMD_DEBIT_CARD + "", mobileNumber, cnic, customerembossingName, customerMailingAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (currentCommand == Constants.CMD_DEBIT_CARD_CONFIRMATION) {
            showLoading("Please Wait", "Processing...");
            if (ApplicationData.isLogin) {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_DEBIT_CARD_CONFIRMATION + "", encMPIN, customerembossingName, customerMailingAddress);
            } else {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_DEBIT_CARD_CONFIRMATION + "", encMPIN, mobileNumber, cnic, customerembossingName, customerMailingAddress);
            }
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            hideLoading();
            if (table != null && table.containsKey("errs")) {
                List<?> list = (List<?>) table.get("errs");
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                String code = message.getCode();
                AppLogger.i("##Error Code: " + code);


                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        DebitCardIssuance.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);


            } else if (table != null && table.containsKey("msgs")) {
                List<?> list = (List<?>) table.get("msgs");
                MessageModel message = (MessageModel) list.get(0);
//                        finish();
                dialogGeneral = PopupDialogs.createSuccessDialog("Congratulations!", message.getDescr(), this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ApplicationData.isLogin) {
                            goToMainMenu();
                        } else {
                            goToLogin();
                        }
                    }
                }, null);

            } else {
                switch (currentCommand) {
                    case Constants.CMD_DEBIT_CARD: {
                        if (table.containsKey(Constants.ATTR_CNIC)) {
                            askMpin(mBundle, null, false);
                        }
                        break;
                    }
                }
            }

        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
    }

    @Override
    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        encMPIN = getEncryptedMpin();
        currentCommand = Constants.CMD_DEBIT_CARD_CONFIRMATION;
        processRequest();
    }

    @Override
    public void processNext() {

    }

    private boolean validate() {

        boolean validate = false;

        if (isHRa) {
            mobileNumber = "";
            return true;
        } else {
            if (!Utility.testValidity(cardEmbossingName)) {
                return false;
            }
            if (!Utility.testValidity(mailingAddress)) {
                return false;
            }
            return true;
        }


    }
}
