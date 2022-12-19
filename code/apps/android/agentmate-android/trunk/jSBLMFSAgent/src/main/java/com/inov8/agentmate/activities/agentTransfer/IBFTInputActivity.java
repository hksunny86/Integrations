package com.inov8.agentmate.activities.agentTransfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.fundsTransfer.FundsTransferInputActivity;
import com.inov8.agentmate.model.BankModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.PaymentReasonModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Utility.testValidity;

public class IBFTInputActivity extends BaseCommunicationActivity {
    private TextView lblHeading;
    private EditText mobileNumber, accountNumber, amount;
    private Spinner bank, paymentPurpose;
    private Button btnNext;
    ArrayAdapter<String> banksAdapter, paymentPurposeAdaptor;
    private String encMPIN;
    private int currCommand;
    private ProductModel product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_ibft_input);

        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);

            lblHeading = findViewById(R.id.lblHeading);
            lblHeading.setText("Agent IBFT");

            mobileNumber = findViewById(R.id.input_mobile_number);
            accountNumber = findViewById(R.id.input_account_number);
            amount = findViewById(R.id.input_amount);

            mobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_MOBILE)});
    //        accountNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(ApplicationData.banks.get(bank.getSelectedItemPosition()).getMax()))});
            amount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});

            disableCopyPaste(mobileNumber);
            disableCopyPaste(accountNumber);
            disableCopyPaste(amount);

            bank = findViewById(R.id.input_bank_spinner);
            paymentPurpose = findViewById(R.id.input_purpose_spinner);

            mobileNumber.setText(ApplicationData.agentMobileNumber);
            mobileNumber.setEnabled(false);

            if (ApplicationData.banks == null) {
                currCommand = Constants.CMD_BANKS;
                processRequest();
            } else {
                ArrayList<String> banksList = new ArrayList<>();
                for (int i = 0; i < ApplicationData.banks.size(); i++) {
                    banksList.add(ApplicationData.banks.get(i).getLabel());
                }
                banksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, banksList);
                bank.setAdapter(banksAdapter);
                if (ApplicationData.listPaymentReasons == null) {
                    currCommand = Constants.CMD_TRANS_PURPOSE_CODE;
                    processRequest();
                } else {
                    ArrayList<String> paymentPurposeList = new ArrayList<>();
                    for (int i = 0; i < ApplicationData.listPaymentReasons.size(); i++) {
                        paymentPurposeList.add(ApplicationData.listPaymentReasons.get(i).getName());
                    }
                    paymentPurposeAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentPurposeList);
                    paymentPurpose.setAdapter(paymentPurposeAdaptor);
                    accountNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(ApplicationData.banks.get(bank.getSelectedItemPosition()).getMax()))});
                }
            }

            btnNext = findViewById(R.id.btnNext);
            btnNext.setOnClickListener(v -> {

                if (validate()) {
                    //          askMpin(null, null, false);
                    currCommand = Constants.CMD_IBFT_AGENT;
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

        bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                accountNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(ApplicationData.banks.get(bank.getSelectedItemPosition()).getMax()))});
                accountNumber.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @Override
//    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
//        encMPIN = getEncryptedMpin();
//        currCommand = Constants.CMD_IBFT_AGENT;
//        processRequest();
//    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(),
                    Constants.Messages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading("Please Wait", "Processing...");
        if (currCommand == Constants.CMD_BANKS) {
            new HttpAsyncTask(this).execute(Constants.CMD_BANKS + "");

        } else if (currCommand == Constants.CMD_TRANS_PURPOSE_CODE) {
            new HttpAsyncTask(this).execute(Constants.CMD_TRANS_PURPOSE_CODE + "");

        } else if (currCommand == Constants.CMD_IBFT_AGENT) {
            new HttpAsyncTask(this).execute(
                    Constants.CMD_IBFT_AGENT + "", product.getId(), ApplicationData.agentMobileNumber, ApplicationData.banks.get(bank.getSelectedItemPosition()).getId(),
                    accountNumber.getText().toString(), amount.getText().toString(), ApplicationData.listPaymentReasons.get(paymentPurpose.getSelectedItemPosition()).getCode(),ApplicationData.banks.get(bank.getSelectedItemPosition()).getLabel());
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
                    .getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, false, null);
            } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                hideLoading();
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);
                createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {
                    goToMainMenu();
                });
            } else {
                if (currCommand == Constants.CMD_BANKS) {

                    ApplicationData.banks = (ArrayList<BankModel>) table.get(Constants.KEY_LIST_MEMBERS_BANKS);
                    ArrayList<String> banksList = new ArrayList<>();
                    for (int i = 0; i < ApplicationData.banks.size(); i++) {
                        banksList.add(ApplicationData.banks.get(i).getLabel());
                    }

                    banksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, banksList);
                    bank.setAdapter(banksAdapter);
                    accountNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(ApplicationData.banks.get(bank.getSelectedItemPosition()).getMax()))});

                    if (ApplicationData.listPaymentReasons == null) {
                        currCommand = Constants.CMD_TRANS_PURPOSE_CODE;
                        processRequest();
                    } else {
                        ArrayList<String> paymentPurposeList = new ArrayList<>();
                        for (int i = 0; i < ApplicationData.listPaymentReasons.size(); i++) {
                            paymentPurposeList.add(ApplicationData.listPaymentReasons.get(i).getName());
                        }
                        paymentPurposeAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentPurposeList);
                        paymentPurpose.setAdapter(paymentPurposeAdaptor);
                    }
                } else if (currCommand == Constants.CMD_TRANS_PURPOSE_CODE) {

                    ApplicationData.listPaymentReasons = (ArrayList<PaymentReasonModel>) table.get(Constants.KEY_LIST_PAYMENT_REASONS);
                    ArrayList<String> paymentReasonsList = new ArrayList<>();
                    for (int i = 0; i < ApplicationData.listPaymentReasons.size(); i++) {
                        paymentReasonsList.add(ApplicationData.listPaymentReasons.get(i).getName());
                    }
                    paymentPurposeAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentReasonsList);
                    paymentPurpose.setAdapter(paymentPurposeAdaptor);
                } else if (currCommand == Constants.CMD_IBFT_AGENT) {
                    TransactionInfoModel transactionInfo = TransactionInfoModel
                            .getInstance();
                    transactionInfo.AgentIBFT(
                            table.get(Constants.ATTR_COREACID).toString(),
                            table.get(Constants.ATTR_COREACTITLE).toString(),
                            table.get(Constants.ATTR_BBACID).toString(),
                            table.get(Constants.ATTR_TXAM).toString(),
                            table.get(Constants.ATTR_TXAMF).toString(),
                            table.get(Constants.ATTR_TPAM).toString(),
                            table.get(Constants.ATTR_TPAMF).toString(),
                            table.get(Constants.ATTR_CAMT).toString(),
                            table.get(Constants.ATTR_CAMTF).toString(),
                            table.get(Constants.ATTR_TAMT).toString(),
                            table.get(Constants.ATTR_TAMTF).toString(),
                            ApplicationData.banks.get(bank.getSelectedItemPosition()).getId(),
                            ApplicationData.listPaymentReasons.get(paymentPurpose.getSelectedItemPosition()).getCode(),
                            table.get(Constants.ATTR_BENE_BANK_NAME).toString(),
                            table.get(Constants.ATTR_BENE_BRANCH_NAME).toString(),
                            table.get(Constants.ATTR_BENE_IBAN).toString(),
                            table.get(Constants.ATTR_CR_DR).toString());
                    Intent intent = new Intent(IBFTInputActivity.this, IBFTConfirmationActivity.class);
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

    private boolean validate() {

        boolean validate = false;

        if (!Utility.testValidity(mobileNumber)) {
            return validate;
        }
        if (mobileNumber.getText().toString().length() < 11) {
            mobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER);
            return validate;
        } else if ((mobileNumber.getText().toString().charAt(0) != '0' || mobileNumber.getText().toString().charAt(1) != '3')) {
            mobileNumber.setError(AppMessages.INVALID_MOBILE_NUMBER_FORMAT);
            return validate;
        }

        if (!Utility.testValidity(accountNumber)) {
            return validate;
        } else if (accountNumber.getText().toString().length() < Integer.parseInt(ApplicationData.banks.get(bank.getSelectedItemPosition()).getMin())) {
            accountNumber.setError("Account no. must be between " + ApplicationData.banks.get(bank.getSelectedItemPosition()).getMin() + " and " +
                    ApplicationData.banks.get(bank.getSelectedItemPosition()).getMax());
            return validate;
        }

        if (!Utility.testValidity(amount)) {
            return validate;
        } else {
            validate = true;
        }

        return validate;
    }

}