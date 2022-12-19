package com.inov8.jsblconsumer.activities.retailPayment.retailpaymentmpass;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class RetailPaymentComfirmationMPassActivity extends BaseCommunicationActivity implements View.OnClickListener {

    private String merchantId;

    private String amount = "",qrString="";
    private String merchantName = "";
    private int currCmd;
    private String merchantLaction = "";
    private TextView tvMerchantName, tvPaymentAmount, tvMerchantLocation, tvAmount, tvAmountText;
    private EditText etAmount;
    private String[] labelsDetails, dataDetails;
    private Button btnConfirm, btnCancel;
    private String amountF, strFee;

    private ListViewExpanded listMerchantDetails, listPaymentDetails;

    private LinearLayout llAmount;


    private TransactionModel transaction;


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_confirmation_retail_payment);
        currCmd = Constants.CMD_RETAIL_PAYMENT_VERIFICATION;
        fetchIntent();
        headerImplementation();
        bottomBarImplementation(RetailPaymentComfirmationMPassActivity.this, "");
        checkSoftKeyboardD();
        setUI();


    }


    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }


    private void fetchIntent() {
        merchantId = mBundle.getString(Constants.IntentKeys.MERCHANT_ID);
        merchantName = mBundle.getString(Constants.IntentKeys.MERCHANT_NAME);
        merchantLaction = mBundle.getString(Constants.IntentKeys.MERCHANT_LACTION);
        amount = mBundle.getString(Constants.IntentKeys.QR_AMOUNT);
        qrString = mBundle.getString(Constants.IntentKeys.QR_STRING);

    }

    private void setUI() {

        ivHeaderLog.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerText.setText(getString(R.string.qr_payment));


        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnCancel = (Button) findViewById(R.id.btnCancel);


        btnConfirm.setText(getString(R.string.next));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currCmd == Constants.CMD_RETAIL_PAYMENT_VERIFICATION) {
                    hideKeyboard(v);
                    processRequest();
                } else {

                    askMpin(mBundle, RetailPaymentComfirmationMPassActivity.class, false);
                }


            }
        });

        tvAmount = (TextView) findViewById(R.id.tvAmount);
        llAmount = (LinearLayout) findViewById(R.id.llAmount);
        tvPaymentAmount = (TextView) findViewById(R.id.tvPaymentAmount);
        etAmount = (EditText) findViewById(R.id.etAmount);
        disableCopyPaste(etAmount);


        if (amount != null && !amount.equals("")) {
            tvAmount.setText(Utility.getFormatedAmount(amount));
            btnConfirm.setText(getString(R.string.pay_now));
            currCmd = Constants.CMD_RETAIL_PAYMENT_VERIFICATION;
            processRequest();
        } else {
            tvPaymentAmount.setText(getString(R.string.enter_payment_amount));
            llAmount.setVisibility(View.GONE);
            tvAmount.setVisibility(View.GONE);
            etAmount.setVisibility(View.VISIBLE);
        }

//        listMerchantDetails = (ListViewExpanded) findViewById(R.id.listMerchantDetails);
//
//
//        try {
//            labelsDetails = new String[]{getString(R.string.merchant_id), getString(R.string.merchant_name)};
//            dataDetails = new String[]{merchantId, merchantName};
//
//            populateData(listMerchantDetails, labelsDetails, dataDetails);
//        } catch (Exception e) {
//            AppLogger.e(e);
//        }
//
//
//        listPaymentDetails = (ListViewExpanded) findViewById(R.id.listPaymentDetails);
//
//
//        try {
//            labelsDetails = new String[]{getString(R.string.transaction_type), getString(R.string.transaction_fee)};
//            dataDetails = new String[]{"QR Payment", ""};
//
//            populateData(listPaymentDetails, labelsDetails, dataDetails);
//        } catch (Exception e) {
//            AppLogger.e(e);
//        }


    }


    @Override
    public void processRequest() {

        if (tvAmount.getVisibility() == View.VISIBLE) {
            amount = tvAmount.getText().toString();
        } else if (TextUtils.isEmpty(etAmount.getText())) {
            etAmount.setError(AppMessages.ERROR_EMPTY_FIELD);
            return;
        } else {
            amount = etAmount.getText().toString();
        }


        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    RetailPaymentComfirmationMPassActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");

        switch (currCmd) {
            case Constants.CMD_RETAIL_PAYMENT_VERIFICATION:
                new HttpAsyncTask(RetailPaymentComfirmationMPassActivity.this).execute(
                        Constants.CMD_RETAIL_PAYMENT_VERIFICATION + "", merchantId,
                        Utility.getUnFormattedAmount(amount),qrString);
                break;
            case Constants.CMD_RETAIL_PAYMENT_MPASS:
                new HttpAsyncTask(RetailPaymentComfirmationMPassActivity.this).execute(
                        Constants.CMD_RETAIL_PAYMENT_MPASS + "", getEncryptedMpin(), merchantId,
                        Utility.getUnFormattedAmount(amount), merchantName,qrString);

                break;
        }


//        Intent intent = new Intent(RetailPaymentComfirmationMPassActivity.this, RetailPaymentReceiptActivity.class);
////        intent.putExtra(Constants.IntentKeys.TRANSACTION_RECEIPT_MODEL, transactionReceiptModel);
////        intent.putExtra(Constants.IntentKeys.FROM_ACCOUNT, fromAccountModel);
//        intent.putExtras(mBundle);
//        startActivity(intent);

    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            hideLoading();
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);
                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        RetailPaymentComfirmationMPassActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                    askMpin(mBundle, RetailPaymentActivity.class, true);
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);

            } else if (table.containsKey(Constants.KEY_LIST_MSGS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);

                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        RetailPaymentComfirmationMPassActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);

            } else {


                switch (currCmd) {
                    case Constants.CMD_RETAIL_PAYMENT_VERIFICATION:

                        merchantId = (table.get(XmlConstants.Attributes.MRID).toString());
                        merchantName = (table.get(XmlConstants.Attributes.MNAME).toString());
                        amount = (table.get(XmlConstants.Attributes.TAMT).toString());
//                        amount = (table.get(XmlConstants.Attributes.TAMTF).toString());
                        setListView();

                        break;
                    case Constants.CMD_RETAIL_PAYMENT_MPASS:

                        if (table.containsKey(Constants.KEY_LIST_TRANS)) {
                            @SuppressWarnings("unchecked")
                            List<TransactionModel> list = (List<TransactionModel>) table.get(Constants.KEY_LIST_TRANS);
                            transaction = list.get(0);
                            processNext();
                        }
                        break;
                }


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
        Intent intent = new Intent(RetailPaymentComfirmationMPassActivity.this, RetailPaymentReceiptActivity.class);
        intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL, transaction);
        intent.putExtra(Constants.IntentKeys.MERCHANT_NAME_F, merchantName);
        intent.putExtra(Constants.IntentKeys.MERCHANT_ID, merchantId);
        intent.putExtra(Constants.IntentKeys.MERCHANT_NAME, merchantName);
        intent.putExtra(Constants.IntentKeys.MERCHANT_LACTION, merchantLaction);
        intent.putExtra(Constants.IntentKeys.QR_AMOUNT, amount);
//        intent.putExtras(mBundle);
        startActivity(intent);


    }

    private void setListView() {
        tvAmount.setText(Utility.getFormatedAmount(amount));
        tvPaymentAmount.setText(getString(R.string.enter_payment_amount));
        llAmount.setVisibility(View.VISIBLE);
        tvAmount.setVisibility(View.VISIBLE);
        etAmount.setVisibility(View.GONE);
        tvPaymentAmount.setText(getString(R.string.payment_amount));
        btnConfirm.setText(getString(R.string.pay_now));
        currCmd = Constants.CMD_RETAIL_PAYMENT_MPASS;
        listMerchantDetails = (ListViewExpanded) findViewById(R.id.listMerchantDetails);
        listPaymentDetails = (ListViewExpanded) findViewById(R.id.listPaymentDetails);
        TextView lblConfirmInfo = (TextView) findViewById(R.id.lblConfirmInfo);
        TextView lblDetails = (TextView) findViewById(R.id.lblDetails);
        TextView lblSecond = (TextView) findViewById(R.id.lblSecond);
        listMerchantDetails.setVisibility(View.VISIBLE);
        listPaymentDetails.setVisibility(View.VISIBLE);
        lblConfirmInfo.setVisibility(View.VISIBLE);
        lblDetails.setVisibility(View.VISIBLE);
        lblSecond.setVisibility(View.VISIBLE);

        try {
            labelsDetails = new String[]{getString(R.string.merchant_id), getString(R.string.merchant_name)};
            dataDetails = new String[]{merchantId, merchantName};

            populateData(listMerchantDetails, labelsDetails, dataDetails);
        } catch (Exception e) {
            AppLogger.e(e);
        }


        try {
            labelsDetails = new String[]{getString(R.string.transaction_type), getString(R.string.transaction_fee)};
            dataDetails = new String[]{"QR Payment", Constants.CURRENCY + " 0.0"};

            populateData(listPaymentDetails, labelsDetails, dataDetails);
        } catch (Exception e) {
            AppLogger.e(e);
        }
    }
}