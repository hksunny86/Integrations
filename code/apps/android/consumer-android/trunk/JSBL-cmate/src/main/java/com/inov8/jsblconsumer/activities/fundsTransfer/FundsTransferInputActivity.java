package com.inov8.jsblconsumer.activities.fundsTransfer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MbankModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.PaymentReasonModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TpurpsModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.DrawableTextView;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class FundsTransferInputActivity extends BaseCommunicationActivity {
    ArrayList<MbankModel> mBanksList;
    ArrayList<TpurpsModel> mTransList;
    private TextView lblField1, lblField2,
            lblField3, lblField4, lblField5, lblField6, lblSpinner2, lblFieldAmountHint, bankTitle, bankDes, transTitle;
    private EditText input1, input2, input3, input4, input5, input6;
    private String strSenderCnic, strSenderMobileNumber, strReceiverCnic, strReceiverBankId, strReceiverBankName,
            strTransactionPurposeName, strReceiverMobileNumber, strAccountNumber, strAmount, strAccMinLength, strAccMaxLength;
    private Button btnNext;
    private ProductModel product;
    private byte flowId;
    private Spinner banksList, transList, spinner2;
    private boolean fetchPaymentReasons;
    private int currCommand;
    private EditText editText;
    private int RequestPermissionCode = 112;

    private ImageView pastePin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_funds_transfer);


        try {
            fetchIntents();

            titleImplementation(null, product.getName(), null, FundsTransferInputActivity.this);
            bottomBarImplementation(FundsTransferInputActivity.this, "");
            checkSoftKeyboardD();

            lblField1 = (TextView) findViewById(R.id.lblField1);
            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField3 = (TextView) findViewById(R.id.lblField3);
            lblField4 = (TextView) findViewById(R.id.lblField4);
            lblField5 = (TextView) findViewById(R.id.lblField5);
            lblField6 = (TextView) findViewById(R.id.lblField6);
            lblSpinner2 = findViewById(R.id.lblSpinner2);
            lblFieldAmountHint = (TextView) findViewById(R.id.lblField_initial_amt_hint);
            bankTitle = (TextView) findViewById(R.id.spinnerText);
            banksList = (Spinner) findViewById(R.id.spinner);
            bankDes = (TextView) findViewById(R.id.spinnerMsg);
            transTitle = (TextView) findViewById(R.id.spinnerText1);
            transList = (Spinner) findViewById(R.id.spinner1);
            spinner2 = findViewById(R.id.spinner2);

            switch (flowId) {
                case Constants.FLOW_ID_FT_BLB_TO_BLB:
                    // sender cnic, sender mobile, receiver cnic, receiver mobile,
                    // account number
                    loadInputFields(false, false, false, true, false, false);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                    // sender cnic, sender mobile, receiver cnic, receiver mobile,
                    // account number
                    transTitle.setText("Purpose of Transaction");
                    transTitle.setVisibility(View.GONE);
                    transList.setVisibility(View.GONE);
                    loadInputFields(false, false, true, true, false, true);
                    if (ApplicationData.listPaymentReasons == null) {
                        fetchPaymentReasons = true;
                        processRequest();
                    }

                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                    // sender cnic, sender mobile, receiver cnic, receiver mobile,
                    // account number
//                    if (ApplicationData.listPaymentReasons == null) {
////                        fetchPaymentReasons = true;
//                        processRequest();
//                    }
                    loadInputFields(false, false, false, true, true, false);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_IBFT:
                    // sender cnic, sender mobile, receiver cnic, receiver mobile,
                    // account number
                    if (ApplicationData.listPaymentReasons == null) {
                        fetchPaymentReasons = true;
                        processRequest();
                    } else {
                        if (ApplicationData.listMbanks == null) {
                            currCommand = Constants.CMD_GET_BANKS;
                            processRequest();
                        } else {
                            mBanksList = ApplicationData.listMbanks;
                            final ArrayAdapter<MbankModel> dataAdapterBanks = new ArrayAdapter<MbankModel>(
                                    this, android.R.layout.simple_spinner_item, mBanksList);
                            dataAdapterBanks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            banksList.setAdapter(dataAdapterBanks);
                            banksList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    MbankModel bank = dataAdapterBanks.getItem(position);
                                    strReceiverBankId = bank.getImd();
                                    strReceiverBankName = bank.getName();
                                    strAccMinLength = bank.getMinLength();
                                    strAccMaxLength = "24";
//                                    strAccMaxLength = bank.getMaxLength();
                                    if (strAccMinLength != null && strAccMaxLength != null && strAccMaxLength.equals(strAccMinLength)) {
                                        bankDes.setText(String.format(getString(R.string.label_invalid_account_number_min), strAccMinLength, strReceiverBankName));
                                    } else if (strAccMinLength != null && strAccMaxLength != null) {
                                        bankDes.setText(String.format(getString(R.string.label_invalid_account_number), strAccMinLength, strAccMaxLength, strReceiverBankName));
                                    }
                                    bankDes.setVisibility(View.VISIBLE);
                                    input4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(strAccMaxLength))});
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }
                    bankTitle.setText("Receiver Bank");
                    bankTitle.setVisibility(View.VISIBLE);
                    banksList.setVisibility(View.VISIBLE);

                    loadInputFields(false, false, false, true, true, true);
                    break;
            }

            lblField6.setText("Amount");
            lblField6.setVisibility(View.VISIBLE);


            input6 = (EditText) findViewById(R.id.input6);
            disableCopyPaste(input6);
            input6.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    Constants.MAX_AMOUNT_LENGTH)});
            input6.setVisibility(View.VISIBLE);


            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (input1 != null && TextUtils.isEmpty(input1.getText())) {
                        input1.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    if (input2 != null && TextUtils.isEmpty(input2.getText())) {
                        input2.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    if (input3 != null && TextUtils.isEmpty(input3.getText())) {
                        input3.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    if (input4 != null && TextUtils.isEmpty(input4.getText())) {
                        input4.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    if (input5 != null && TextUtils.isEmpty(input5.getText())) {
                        input5.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    if (input6 != null && TextUtils.isEmpty(input6.getText())) {
                        input6.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }

                    if (input1 != null) {
                        strSenderCnic = input1.getText().toString();
                    }
                    if (input2 != null) {
                        strSenderMobileNumber = input2.getText().toString();
                    }
                    if (input3 != null) {
                        strReceiverCnic = input3.getText().toString();
                    }
                    if (input4 != null) {
                        if (flowId == Constants.FLOW_ID_FT_BLB_TO_IBFT)
                            strAccountNumber = input4.getText().toString();
                        else
                            strReceiverMobileNumber = input4.getText().toString();
                    }
                    if (input5 != null) {
                        if (flowId == Constants.FLOW_ID_FT_BLB_TO_IBFT)
                            strReceiverMobileNumber = input5.getText().toString();
                        else
                            strAccountNumber = input5.getText().toString();
                    }
                    if (input6 != null) {
                        strAmount = input6.getText().toString();
                    }

                    if ((strSenderCnic != null && strSenderCnic.length() < 13)) {
                        input1.setError(AppMessages.INVLAID_SENDER_CNIC);
                        input1.requestFocus();
                        return;
                    }

                    if (strSenderMobileNumber != null
                            && strSenderMobileNumber.length() < 11) {
                        input2.setError(AppMessages.INVLAID_SENDER_MOBILE_NUMBER);
                        input2.requestFocus();
                        return;
                    }

                    if (strSenderMobileNumber != null
                            && (strSenderMobileNumber.charAt(0) != '0' || strSenderMobileNumber.charAt(1) != '3')) {
                        input2.setError(AppMessages.ERROR_MOBILE_NO_START);
                        return;
                    }

                    if (strReceiverCnic != null
                            && strReceiverCnic.length() < 13) {
                        input3.setError(AppMessages.INVLAID_RECEIVER_CNIC);
                        input3.requestFocus();
                        return;
                    }

                    if (strReceiverMobileNumber != null
                            && strReceiverMobileNumber.length() < 11) {
                        if (flowId == Constants.FLOW_ID_FT_BLB_TO_IBFT) {
                            input5.setError(AppMessages.INVLAID_RECEIVER_MOBILE_NUMBER);
                            input5.requestFocus();
                        } else {
                            input4.setError(AppMessages.INVLAID_RECEIVER_MOBILE_NUMBER);
                            input4.requestFocus();
                        }
                        return;
                    }

                    if (strReceiverMobileNumber != null
                            && (strReceiverMobileNumber.charAt(0) != '0' || strReceiverMobileNumber.charAt(1) != '3')) {
                        if (flowId == Constants.FLOW_ID_FT_BLB_TO_IBFT) {
                            input5.setError(AppMessages.ERROR_MOBILE_NO_START);
                            input5.requestFocus();
                        } else {
                            input4.setError(AppMessages.ERROR_MOBILE_NO_START);
                            input4.requestFocus();
                        }
                        return;
                    }

                    if (flowId == Constants.FLOW_ID_FT_BLB_TO_IBFT) {
                        if (strAccountNumber != null &&
                                (
                                        (strAccMinLength != null && strAccountNumber.length() < Integer.parseInt(strAccMinLength)) ||
                                                (strAccMaxLength != null && strAccountNumber.length() > Integer.parseInt(strAccMaxLength))
                                )
                        ) {
                            input4.setError(AppMessages.INVLAID_ACCOUNT_NUMBER);
                            input4.requestFocus();
                            return;
                        }
                    } else {
//                        if (strAccountNumber != null && strAccountNumber.length() < 14) {
//                            input5.setError(AppMessages.INVLAID_ACCOUNT_NUMBER);
//                            input5.requestFocus();
//                            return;
//                        }
                    }

                    int amount = Integer.parseInt(strAmount);
                    double productMaxAmount = Double.parseDouble(Utility
                            .getUnFormattedAmount(product.getMaxamt()));
                    double productMinAmount = Double.parseDouble(Utility
                            .getUnFormattedAmount(product.getMinamt()));

                    if (!Utility.isNull(product.getDoValidate())
                            && product.getDoValidate().equals("1")) {
                        if ((amount > productMaxAmount)
                                || (amount < productMinAmount)) {
                            input6.setError(AppMessages.ERROR_AMOUNT_INVALID);
                            input6.requestFocus();
                            return;
                        }
                    } else if (strAmount != null && strAmount.length() > 0) {
                        if (amount < 1 || amount > Constants.MAX_AMOUNT) {
                            input6.setError(AppMessages.ERROR_AMOUNT_INVALID);
                            input6.requestFocus();
                            return;
                        }
                    }

                    mpinProcess(null, null);
                }
            });

            addAutoKeyboardHideFunction();
        } catch (Exception ex) {
            ex.getMessage();
        }
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {

        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    FundsTransferInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");

        if (fetchPaymentReasons) {
            new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                    Constants.CMD_TRANS_PURPOSE_CODE + "");
        } else if (currCommand == Constants.CMD_GET_BANKS) {
            new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                    Constants.CMD_GET_BANKS + "");

        } else {

            switch (flowId) {
                case Constants.FLOW_ID_FT_BLB_TO_BLB:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_BLB2BLB_INFO + "",
                            product.getId(), strReceiverMobileNumber, strAmount);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_BLB2CNIC_INFO + "",
                            product.getId(), strReceiverMobileNumber,
                            strReceiverCnic, strAmount);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_2CORE_INFO + "",
                            product.getId(),
                            strReceiverMobileNumber, strAccountNumber, strAmount);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_IBFT:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_2CORE_INFO + "",
                            product.getId(), strReceiverMobileNumber,
                            strAccountNumber, strAmount, strReceiverBankId, strReceiverBankName, ApplicationData.listPaymentReasons.get(spinner2.getSelectedItemPosition()).getCode());
                    break;
            }
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


                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        FundsTransferInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);


            } else if (fetchPaymentReasons) {
                ApplicationData.listPaymentReasons = (ArrayList<PaymentReasonModel>) table.get(Constants.KEY_LIST_PAYMENT_REASONS);
                ArrayList<String> paymentReasonsList = new ArrayList<>();
                for (int i = 0; i < ApplicationData.listPaymentReasons.size(); i++) {
                    paymentReasonsList.add(ApplicationData.listPaymentReasons.get(i).getName());
                }
                ArrayAdapter<String> paymentReasonsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentReasonsList);
                spinner2.setAdapter(paymentReasonsAdapter);
                fetchPaymentReasons = false;

                if (flowId == Constants.FLOW_ID_FT_BLB_TO_IBFT) {
                    currCommand = Constants.CMD_GET_BANKS;
                    processRequest();
                }

            } else if ((currCommand == Constants.CMD_GET_BANKS && flowId == Constants.FLOW_ID_FT_BLB_TO_IBFT)) {
                ApplicationData.listMbanks = (ArrayList<MbankModel>) table.get(Constants.KEY_LIST_MEMBERS_BANKS);
                mBanksList = ApplicationData.listMbanks;
                currCommand = 0;
                final ArrayAdapter<MbankModel> dataAdapterBanks = new ArrayAdapter<MbankModel>(
                        this, android.R.layout.simple_spinner_item, mBanksList);
                dataAdapterBanks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                banksList.setAdapter(dataAdapterBanks);
                banksList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        MbankModel bank = dataAdapterBanks.getItem(position);
                        strReceiverBankId = bank.getImd();
                        strReceiverBankName = bank.getName();
                        strAccMinLength = bank.getMinLength();
                        strAccMaxLength = "24";
//                        strAccMaxLength = bank.getMaxLength();
                        if (strAccMinLength != null && strAccMaxLength != null && strAccMaxLength.equals(strAccMinLength)) {
                            bankDes.setText(String.format(getString(R.string.label_invalid_account_number_min), strAccMinLength, strReceiverBankName));
                        } else if (strAccMinLength != null && strAccMaxLength != null) {
                            bankDes.setText(String.format(getString(R.string.label_invalid_account_number), strAccMinLength, strAccMaxLength, strReceiverBankName));
                        }
                        bankDes.setVisibility(View.VISIBLE);
                        input4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(strAccMaxLength))});
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                TransactionInfoModel transactionInfo = TransactionInfoModel.getInstance();

                switch (flowId) {
                    case Constants.FLOW_ID_FT_BLB_TO_BLB:
                        transactionInfo.FundsTransferBlb2Blb(
                                strReceiverMobileNumber,
                                table.get(XmlConstants.Attributes.TXAM).toString(),
                                table.get(XmlConstants.Attributes.TXAMF).toString(),
                                table.get(XmlConstants.Attributes.TPAM).toString(),
                                table.get(XmlConstants.Attributes.TPAMF).toString(),
                                table.get(XmlConstants.Attributes.CAMT).toString(),
                                table.get(XmlConstants.Attributes.CAMTF).toString(),
                                table.get(XmlConstants.Attributes.TAMT).toString(),
                                table.get(XmlConstants.Attributes.TAMTF).toString(),
                                table.get(XmlConstants.Attributes.PID).toString());

                        transactionInfo.setCoreactl(table.get("RECACCTITLE").toString());
                        break;

                    case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                        transactionInfo.FundsTransferBlb2Cnic(
                                table.get(XmlConstants.Attributes.RCMOB).toString(),
                                table.get(XmlConstants.Attributes.RWCNIC).toString(),
                                table.get(XmlConstants.Attributes.TXAM).toString(),
                                table.get(XmlConstants.Attributes.TXAMF).toString(),
                                table.get(XmlConstants.Attributes.TPAM).toString(),
                                table.get(XmlConstants.Attributes.TPAMF).toString(),
                                table.get(XmlConstants.Attributes.CAMT).toString(),
                                table.get(XmlConstants.Attributes.CAMTF).toString(),
                                table.get(XmlConstants.Attributes.TAMT).toString(),
                                table.get(XmlConstants.Attributes.TAMTF).toString(),
                                table.get(XmlConstants.Attributes.PID).toString());
                        break;

                    case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                        transactionInfo.FundsTransferBlb2Core(
//                                strReceiverMobileNumber,
                                table.get(XmlConstants.Attributes.RCMOB).toString(),
                                table.get(XmlConstants.Attributes.COREACID).toString(),
                                table.get(XmlConstants.Attributes.COREACTL).toString(),
                                table.get(XmlConstants.Attributes.TXAM).toString(),
                                table.get(XmlConstants.Attributes.TXAMF).toString(),
                                table.get(XmlConstants.Attributes.TPAM).toString(),
                                table.get(XmlConstants.Attributes.TPAMF).toString(),
                                table.get(XmlConstants.Attributes.CAMT).toString(),
                                table.get(XmlConstants.Attributes.CAMTF).toString(),
                                table.get(XmlConstants.Attributes.TAMT).toString(),
                                table.get(XmlConstants.Attributes.TAMTF).toString(),
                                table.get(XmlConstants.Attributes.PID).toString());
                        break;

                    case Constants.FLOW_ID_FT_BLB_TO_IBFT:
                        transactionInfo.FundsTransferBlb2Ibft(
                                table.get(XmlConstants.Attributes.RCMOB).toString(),
                                table.get(XmlConstants.Attributes.COREACID).toString(),
                                table.get(XmlConstants.Attributes.COREACTL).toString(),
                                table.get(XmlConstants.Attributes.TXAM).toString(),
                                table.get(XmlConstants.Attributes.TXAMF).toString(),
                                table.get(XmlConstants.Attributes.TPAM).toString(),
                                table.get(XmlConstants.Attributes.TPAMF).toString(),
                                table.get(XmlConstants.Attributes.CAMT).toString(),
                                table.get(XmlConstants.Attributes.CAMTF).toString(),
                                table.get(XmlConstants.Attributes.TAMT).toString(),
                                table.get(XmlConstants.Attributes.TAMTF).toString(),
                                table.get(XmlConstants.Attributes.PID).toString(),
                                "",
                                table.get(XmlConstants.Attributes.ATTR_BENE_BANK_NAME).toString(),
                                table.get(XmlConstants.Attributes.ATTR_BENE_BRANCH_NAME).toString(),
                                table.get(XmlConstants.Attributes.ATTR_BENE_IBAN).toString(),
                                table.get(XmlConstants.Attributes.ATTR_CR_DR).toString());
                        break;
                }

                Intent intent = new Intent(FundsTransferInputActivity.this, FundsTransferConfirmationActivity.class);
                intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactionInfo);
                intent.putExtra(Constants.IntentKeys.FLOW_ID, flowId);
                intent.putExtra(Constants.IntentKeys.BANK_NAME, strReceiverBankName);
                intent.putExtra(Constants.IntentKeys.BANK_IMD, strReceiverBankId);
                intent.putExtra(Constants.IntentKeys.TRANSACTION_PURPOSE_NAME, strTransactionPurposeName);
                intent.putExtra(XmlConstants.Attributes.RWMOB, strReceiverMobileNumber);
                if (ApplicationData.listPaymentReasons != null && !fetchPaymentReasons && spinner2.getVisibility() == View.VISIBLE)
                    intent.putExtra(Constants.IntentKeys.PURPOSE_CODE, ApplicationData.listPaymentReasons.get(spinner2.getSelectedItemPosition()).getCode());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
        hideLoading();
    }

    @Override
    public void processNext() {
    }

    private void fetchIntents() {
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
    }

    private void loadInputFields(boolean field1, boolean field2,
                                 boolean field3, boolean field4, boolean field5, boolean inputSpinner2) {
        if (field1) {
            lblField1.setText("Sender CNIC");
            lblField1.setVisibility(View.VISIBLE);

            input1 = (EditText) findViewById(R.id.input1);
            disableCopyPaste(input1);
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
            input1.setVisibility(View.VISIBLE);
        }
        if (field2) {
            lblField2.setText("Sender Mobile Number");
            lblField2.setVisibility(View.VISIBLE);
            input2 = (EditText) findViewById(R.id.input2);
            disableCopyPaste(input2);
            input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            input2.setVisibility(View.VISIBLE);
            setContactPicker(input2);
        }
        if (field3) {
            lblField3.setText("Receiver CNIC");
            lblField3.setVisibility(View.VISIBLE);

            input3 = (EditText) findViewById(R.id.input3);
            disableCopyPaste(input3);
            input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
            input3.setVisibility(View.VISIBLE);
        }
        if (field4) {
            lblField4.setVisibility(View.VISIBLE);
            input4 = (EditText) findViewById(R.id.input4);
            disableCopyPaste(input4);
            input4.setVisibility(View.VISIBLE);
            if (flowId == Constants.FLOW_ID_FT_BLB_TO_IBFT) {
                lblField4.setText("Account No / IBAN No");
                input4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
//                input4.setKeyListener(DigitsKeyListener.getInstance("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "));
//                input4.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                lblField4.setText("Receiver Mobile Number");
                input4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                setContactPicker(input4);
                input4.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }
        if (field5) {
            input5 = (EditText) findViewById(R.id.input5);
            disableCopyPaste(input5);
            input5.setVisibility(View.VISIBLE);
            lblField5.setVisibility(View.VISIBLE);
            if (flowId == Constants.FLOW_ID_FT_BLB_TO_IBFT) {
                lblField5.setText("Receiver Mobile Number");
                input5.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                setContactPicker(input5);
            } else {
                lblField5.setText("Account Number");
                input5.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            }
        }
        if (inputSpinner2) {
            lblSpinner2.setText("Payment Purpose");
            lblSpinner2.setVisibility(View.VISIBLE);

            if (ApplicationData.listPaymentReasons != null) {
                ArrayList<String> paymentReasonsList = new ArrayList<>();
                for (int i = 0; i < ApplicationData.listPaymentReasons.size(); i++) {
                    paymentReasonsList.add(ApplicationData.listPaymentReasons.get(i).getName());
                }
                ArrayAdapter<String> paymentReasonsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentReasonsList);
                spinner2.setAdapter(paymentReasonsAdapter);
            }

            spinner2.setVisibility(View.VISIBLE);
        }
    }

    private void setContactPicker(EditText editText) {

        this.editText = editText;

        editText.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.mipmap.contact_picker, 0);


        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable drawableRight = editText.getCompoundDrawables()[DRAWABLE_RIGHT];
//                    if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (event.getRawX() >= (editText.getRight() - drawableRight.getBounds().width())) {
//                        if(event.getAction() == MotionEvent.ACTION_UP) {
//                            if(event.getRawX() <= (mTvTitle.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()))  {

                        // your action here
                        checkPermission();
                        return true;
                    }
                }
                return false;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RequestPermissionCode) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = intent.getData();
                Cursor c = getContentResolver().query
                        (contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String contactId = c.getString(c.getColumnIndex
                            (ContactsContract.Contacts._ID));
                    String hasNumber = c.getString(c.getColumnIndex
                            (ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String num = "";
                    String name = "";
                    if (Integer.valueOf(hasNumber) == 1) {
                        Cursor numbers = getContentResolver().query
                                (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (numbers.moveToNext()) {
                            num = numbers.getString(numbers.getColumnIndex
                                    (ContactsContract.CommonDataKinds.Phone.NUMBER));
                            num = num.replace(" ", "");
                            num = num.trim();
                            AppLogger.e(num);
                            if (num.contains("0092")) {
                                num = num.replace("0092", "0");
                            } else if (num.contains("+92")) {
                                num = num.replace("+92", "0");
                            }

                            if (num.length() == 11 && num.contains("03") && num.matches("[0-9]+")) {
                                editText.setText(num);
                                editText.setSelection(editText.getText().length());
                            }
                            else{
                                Toast.makeText(FundsTransferInputActivity.this,"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
            }

        } else {
            throw new IllegalStateException(getString(R.string.unexp_value) + requestCode);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {

                Toast.makeText(FundsTransferInputActivity.this, getString(R.string.contacts_permission_denied_msg), Toast.LENGTH_LONG).show();

            }
        } else {
            throw new IllegalStateException(getString(R.string.unexp_value) + requestCode);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // requesting to the user for permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        } else {
            //if app already has permission this block will execute.
            readContacts();
        }
    }

    private void readContacts() {
        editText.setText("");
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, RequestPermissionCode);
    }


}