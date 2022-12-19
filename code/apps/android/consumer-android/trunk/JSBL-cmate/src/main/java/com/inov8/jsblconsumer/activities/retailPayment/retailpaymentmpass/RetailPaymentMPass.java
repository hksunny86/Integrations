//package com.inov8.jsblconsumer.activities.retailPayment.retailpaymentmpass;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.AppCompatSpinner;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.inov8.falconlite.activities.BaseActivity;
//import com.inov8.falconlite.adapters.SpinnerCustomAdaptor;
//import com.inov8.falconlite.model.CoreAccountModel;
//import com.inov8.falconlite.model.HttpResponseModel;
//import com.inov8.falconlite.model.MessageModel;
//import com.inov8.falconlite.model.ProductModel;
//import com.inov8.falconlite.model.RequestModel;
//import com.inov8.falconlite.net.CommunicationManager;
//import com.inov8.falconlite.net.HttpAsyncTask;
//import com.inov8.falconlite.parser.XmlParser;
//import com.inov8.falconlite.util.AESEncryptor;
//import com.inov8.falconlite.util.AppLogger;
//import com.inov8.falconlite.util.AppMessages;
//import com.inov8.falconlite.util.ApplicationData;
//import com.inov8.falconlite.util.Constants;
//import com.inov8.falconlite.util.PopupDialogs;
//import com.inov8.falconlite.util.XMLConstants;
//import com.inov8.jsbl.mb.R;
//
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//
///**
// * Created by ALI REHAN on 11/13/2017.
// */
//
//public class RetailPaymentMPass extends BaseActivity implements View.OnClickListener, CommunicationManager {
//
//
//    private String merchantId;
//    private ProductModel productModel;
//    private String amount = "";
//    private String merchantName = "";
//    private int currCmd;
//    private String merchantLaction = "";
//    private TextView tvMerchantName, tvPaymentAmount, tvMerchantLocation, tvAmount, tvAmountText, etAmount;
//    private AppCompatSpinner spFromAccount;
//    private CoreAccountModel fromAccountModel;
//    private String[] labelsDetails, dataDetails;
//    private Button btnNext;
//    private String amountF;
//
//    private ArrayList<CoreAccountModel> coreAccountModelArrayList;
//
//    @Override
//    public void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.activity_confirmation_retail_payment);
//        fetchIntent();
//        setUI();
//        initHeaderBar(strHeaderHeading, strHeaderSubHeading, false);
//        setCommunicationManager(RetailPaymentMPass.this);
//        if (ApplicationData.listCoreAccounts == null
//                || ApplicationData.listCoreAccounts.size() <= 0) {
//            onProcessRequest(Constants.CMD_ACCOUNT_SUMMARY);
//        } else {
//            populateFromAccountSpinner();
//        }
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (tvAmount.getVisibility() == View.VISIBLE) {
//                    amount = tvAmount.getText().toString();
//                } else {
//                    amount = etAmount.getText().toString();
//
//                }
//
//                onProcessRequest(Constants.CMD_MERCHANT_VERIFICATION);
//
//            }
//        });
//    }
//
//    @Override
//    public void onPreRequest() {
//        showLoading(AppMessages.PLEASE_WAIT, AppMessages.PROCESSING);
//
//
//    }
//
//    @Override
//    public void onProcessRequest(int command) {
//        currCmd = command;
//        switch (command) {
//
//            case Constants.CMD_ACCOUNT_SUMMARY:
//                new HttpAsyncTask(RetailPaymentMPass.this).execute(new RequestModel(command, null, null));
//                break;
//
//            case Constants.CMD_MERCHANT_VERIFICATION:
//                paramNames = new String[]{Constants.PID, Constants.TAMT, Constants.SENACCOUNTNO, Constants.MRID, Constants.QRTYPE};
//                paramValues = new String[]{productModel.getId() + "", amount, fromAccountModel.getAccountNo(), merchantId, "MPASS"};
//                new HttpAsyncTask(RetailPaymentMPass.this).execute(new RequestModel(command, paramNames, paramValues));
//                break;
//
//
//        }
//    }
//
//    @Override
//    public void onResponse(HttpResponseModel response) {
//        try {
//            if (response != null) {
//                XmlParser xmlParser = new XmlParser();
//                String xmlResponse = response.getXmlResponse();
//                Hashtable<?, ?> table = xmlParser
//                        .convertXmlToTable(xmlResponse);
//                if (table != null
//                        && table.containsKey(XMLConstants.KEY_LIST_ERRS)) {
//                    List<?> list = (List<?>) table
//                            .get(XMLConstants.KEY_LIST_ERRS);
//                    MessageModel messageModel = (MessageModel) list.get(0);
//                    if (messageModel
//                            .getCode()
//                            .equals(Constants.ErrorCodes.TERMINATE_TRANSACTION_EXECUTION)) {
//                        gotoLoginPage(messageModel.getDescr(), PopupDialogs.Status.ERROR);
//                    } else if (messageModel.getCode().equals(
//                            Constants.ErrorCodes.TERMINATE_APP)) {
//                        gotoLoginPage(messageModel.getDescr(), PopupDialogs.Status.ERROR);
//                    } else {
//
//                        popupDialogs.createAlertDialog(messageModel.getDescr(), AppMessages.HEADING_ALERT,
//                                RetailPaymentMPass.this, getString(R.string.ok), null, true, PopupDialogs.Status.ERROR, true, null);
//                    }
//                } else if (table != null
//                        && table.containsKey(XMLConstants.KEY_LIST_MSGS)) {
//                    List<?> list = (List<?>) table
//                            .get(XMLConstants.KEY_LIST_MSGS);
//                    MessageModel messageModel = (MessageModel) list.get(0);
//
//                    popupDialogs.createAlertDialog(messageModel.getDescr(), AppMessages.HEADING_ALERT,
//                            RetailPaymentMPass.this, getString(R.string.ok), null, true, PopupDialogs.Status.SUCCESS, true, null);
//                } else {
//
//                    switch (currCmd) {
//                        case Constants.CMD_ACCOUNT_SUMMARY:
//                            coreAccountModelArrayList = (ArrayList<CoreAccountModel>) table.get(XMLConstants.KEY_LIST_CORE_ACCOUNTS);
//                            ApplicationData.listCoreAccounts = coreAccountModelArrayList;
//                            populateFromAccountSpinner();
//                            break;
//                        case Constants.CMD_MERCHANT_VERIFICATION:
//
//                            amountF = table.get(XMLConstants.ATTR_TAMTF) != null ? table.get(XMLConstants.ATTR_TAMTF).toString() : "";
//                            amount = table.get(XMLConstants.ATTR_TAMT
//                            ) != null ? table.get(XMLConstants.ATTR_TAMT).toString() : "";
//                            merchantId = table.get(XMLConstants.ATTR_MRID) != null ? table.get(XMLConstants.ATTR_MRID).toString() : "";
//                            merchantName = table.get(XMLConstants.ATTR_MNAME) != null ? table.get(XMLConstants.ATTR_MNAME).toString() : "";
//
//                            onProcessNext(null);
//
//
//                    }
//
//
//                }
//            }
//            hideLoading();
//        } catch (Exception exp) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    hideLoading();
//                    popupDialogs.createAlertDialog(AppMessages.EXCEPTION_PROCESSING_ERROR, AppMessages.HEADING_ALERT,
//                            RetailPaymentMPass.this, getString(R.string.ok), null, true, PopupDialogs.Status.ERROR, true, null);
//                }
//            });
//            AppLogger.e(exp);
//        }
//        hideLoading();
//
//
//    }
//
//    @Override
//    public void onProcessNext(ArrayList<Object> listObject) {
//        Intent intent = new Intent(RetailPaymentMPass.this, RetailPaymentConfirmationActivity.class);
//        intent.putExtra(Constants.IntentKeys.FROM_ACCOUNT, fromAccountModel);
//        intent.putExtra(Constants.IntentKeys.MERCHANT_ID_N, merchantId);
//        intent.putExtra(Constants.IntentKeys.MERCHANT_NAME_N, merchantName);
//        intent.putExtra(Constants.IntentKeys.QR_AMOUNT_N, amount);
//        intent.putExtra(Constants.IntentKeys.QR_AMOUNT_F_N, amountF);
//        intent.putExtras(bundle);
//        startActivity(intent);
//
//    }
//
//
//    private void fetchIntent() {
//
//
//        productModel = (ProductModel) bundle
//                .get(Constants.INTENT_KEY_PRODUCT_MODEL);
//        merchantId = bundle.getString(Constants.IntentKeys.MERCHANT_ID);
//        merchantName = bundle.getString(Constants.IntentKeys.MERCHANT_NAME);
//        merchantLaction = bundle.getString(Constants.IntentKeys.MERCHANT_LACTION);
//        amount = bundle.getString(Constants.IntentKeys.QR_AMOUNT);
//
//    }
//
//    private void setUI() {
//        btnNext = (Button) findViewById(R.id.btnNext);
//        tvMerchantName = (TextView) findViewById(R.id.tvMerchantName);
//        tvMerchantLocation = (TextView) findViewById(R.id.tvMerchantLocation);
//        tvAmount = (TextView) findViewById(R.id.tvAmount);
//        tvAmountText = (TextView) findViewById(R.id.tvAmountText);
//        spFromAccount = (AppCompatSpinner) findViewById(R.id.spFromAccount);
//        tvPaymentAmount = (TextView) findViewById(R.id.tvPaymentAmount);
//        etAmount = (EditText) findViewById(R.id.etAmount);
//
//        if (merchantName != null && !merchantName.equals("") && merchantLaction != null && !merchantLaction.equals("")) {
//            tvMerchantName.setText(merchantName);
//            tvMerchantLocation.setText(merchantLaction);
//        } else {
//            tvMerchantName.setVisibility(View.GONE);
//            tvMerchantLocation.setVisibility(View.GONE);
//        }
//
//        if (amount != null && !amount.equals("")) {
//            tvAmount.setText(amount);
//        } else {
//            tvPaymentAmount.setText(getString(R.string.enter_payment_amount));
//            tvAmount.setVisibility(View.GONE);
//            etAmount.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void populateFromAccountSpinner() {
//        ArrayList<String> listFromAccounts = new ArrayList<String>();
//        for (int i = 0; i < ApplicationData.listCoreAccounts.size(); i++) {
//            listFromAccounts.add(AESEncryptor.decryptWithAES(Constants.SECRET_KEY, ApplicationData.listCoreAccounts.get(i)
//                    .getAccountNo()));
//        }
//
//        SpinnerCustomAdaptor secretFromAccountsAdapter = new SpinnerCustomAdaptor(this,
//                R.layout.simple_spinner_item, listFromAccounts);
//        spFromAccount.setAdapter(secretFromAccountsAdapter);
//        spFromAccount
//                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent,
//                                               View view, int position, long id) {
//                        fromAccountModel = ApplicationData.listCoreAccounts.get(position);
//
//                    }
//
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                    }
//                });
//    }
//
//}
