//package com.inov8.jsblconsumer.activities.retailPayment.retailpaymentmpass;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import com.inov8.falconlite.activities.BaseConfirmationActivity;
//import com.inov8.falconlite.model.CoreAccountModel;
//import com.inov8.falconlite.model.HttpResponseModel;
//import com.inov8.falconlite.model.MessageModel;
//import com.inov8.falconlite.model.ProductModel;
//import com.inov8.falconlite.model.RequestModel;
//import com.inov8.falconlite.model.TransactionModel;
//import com.inov8.falconlite.net.CommunicationManager;
//import com.inov8.falconlite.net.HttpAsyncTask;
//import com.inov8.falconlite.parser.XmlParser;
//import com.inov8.falconlite.ui.components.ListViewExpanded;
//import com.inov8.falconlite.util.AESEncryptor;
//import com.inov8.falconlite.util.AppLogger;
//import com.inov8.falconlite.util.AppMessages;
//import com.inov8.falconlite.util.Constants;
//import com.inov8.falconlite.util.PopupDialogs;
//import com.inov8.falconlite.util.Utility;
//import com.inov8.falconlite.util.XMLConstants;
//import com.inov8.jsbl.mb.R;
//
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//
///**
// * Created by ALI REHAN on 10/25/2017.
// */
//
//public class RetailPaymentConfirmationActivity extends BaseConfirmationActivity implements
//        CommunicationManager {
//
//    private ListViewExpanded listAccountDetails, listMerchantDetails,
//            listPaymentDetails, listDenomination;
//    private CoreAccountModel fromAccountModel;
//    private TransactionModel transactionModel, transactionReceiptModel;
//    private String[] labelsDetails, dataDetails;
//    private String strService, strServiceType, strDenomination;
//    private TextView lblFirst, lblSecond, lblThird;
//    private int currCmd;
//    private String strFpin;
//    private Dialog dialog;
//    private String merchantId;
//    private String amount = "";
//    private String amountF = "";
//    private String merchantName = "";
//    private ProductModel productModel;
//    private String merchantLaction = "";
//
//
//    @Override
//    public void onClickNext() {
//
//        View.OnClickListener listenerGenerateFpin = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onProcessRequest(Constants.CMD_GENERATE_EXPIRE_FPIN);
//            }
//        };
//
//        PopupDialogs.OnCustomClickListener customClickListener = new PopupDialogs.OnCustomClickListener() {
//
//            @Override
//            public void onClick(View v, Object obj) {
//                strFpin = (String) obj;
//                strFpin = AESEncryptor.encrypt(strFpin);
//                onProcessRequest(Constants.CMD_MERCHANT_TRANSFER);
//            }
//        };
//
//
//        dialogGeneral = popupDialogs.createPinDialog(getString(R.string.enter_fpin), getString(R.string.fpin_verification), RetailPaymentConfirmationActivity.this,
//                getString(R.string.ok), getString(R.string.generate_fpin), customClickListener, listenerGenerateFpin, false, false, PopupDialogs.Status.VERIFY, true);
//
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setCommunicationManager(RetailPaymentConfirmationActivity.this);
//        if (!Utility.checkInternet(this)) {
//            gotoCategoryMenu(AppMessages.INTERNET_CONNECTION_PROBLEM);
//            return;
//        }
//        fetchIntent();
//        setUI();
//
//
//        initHeaderBar(strHeaderHeading, strHeaderSubHeading, false);
//    }
//
//    @Override
//    public void onPreRequest() {
//        showLoading(AppMessages.PLEASE_WAIT, AppMessages.PROCESSING);
//
//    }
//
//    @Override
//    public void onProcessRequest(int command) {
//        currCmd = command;
//        switch (command) {
//            case Constants.CMD_GENERATE_EXPIRE_FPIN:
//                Constants.GENERATE_EXPIRE_FPIN_FLAG = Constants.GENERATE_FPIN_FLAG;
//                paramNames = new String[]{Constants.EXPIREPIN};
//                paramValues = new String[]{Constants.GENERATE_EXPIRE_FPIN_FLAG};
//                new HttpAsyncTask(RetailPaymentConfirmationActivity.this).execute(new RequestModel(command, paramNames, paramValues));
//                break;
//
//            case Constants.CMD_MERCHANT_TRANSFER:
//                paramNames = new String[]{Constants.PID, Constants.TAMT, Constants.SENACCOUNTNO, Constants.FPIN, Constants.MRID, Constants.QRTYPE, Constants.MNAME};
//                paramValues = new String[]{productModel.getId()+"", amount, fromAccountModel.getAccountNo(), strFpin, merchantId, "MPASS", merchantName};
//                new HttpAsyncTask(RetailPaymentConfirmationActivity.this).execute(new RequestModel(command, paramNames, paramValues));
//                break;
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
//                                RetailPaymentConfirmationActivity.this, getString(R.string.ok), null, true, PopupDialogs.Status.ERROR, true, null);
//                    }
//                } else if (table != null
//                        && table.containsKey(XMLConstants.KEY_LIST_MSGS)) {
//                    List<?> list = (List<?>) table
//                            .get(XMLConstants.KEY_LIST_MSGS);
//                    MessageModel messageModel = (MessageModel) list.get(0);
//
//                    popupDialogs.createAlertDialog(messageModel.getDescr(), AppMessages.HEADING_ALERT,
//                            RetailPaymentConfirmationActivity.this, getString(R.string.ok), null, true, PopupDialogs.Status.SUCCESS, true, null);
//                } else {
//
//                    switch (currCmd) {
//
//                        case Constants.CMD_MERCHANT_TRANSFER:
//                            transactionReceiptModel = ((ArrayList<TransactionModel>) table.get(XMLConstants.KEY_LIST_TRANS)).get(0);
//
//                            dialog = popupDialogs.createAlertDialog(getTranscationSuccessfullyMes(transactionReceiptModel.getTransactionAmountFormatted()), AppMessages.HEADING_SUCCES,
//                                    RetailPaymentConfirmationActivity.this, getString(R.string.view_details), new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                            onProcessNext(null);
//
//                                        }
//                                    }, true, PopupDialogs.Status.SUCCESS, true, null);
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
//                            RetailPaymentConfirmationActivity.this, getString(R.string.ok), null, true, PopupDialogs.Status.ERROR, true, null);
//                }
//            });
//            AppLogger.e(exp);
//        }
//        hideLoading();
//
//    }
//
//    @Override
//    public void onProcessNext(ArrayList<Object> listObject) {
//        Intent intent = new Intent(RetailPaymentConfirmationActivity.this, RetailPaymentReceiptActivity.class);
//        intent.putExtra(Constants.IntentKeys.TRANSACTION_RECEIPT_MODEL, transactionReceiptModel);
//        intent.putExtra(Constants.IntentKeys.FROM_ACCOUNT, fromAccountModel);
//        intent.putExtras(bundle);
//        startActivity(intent);
//
//    }
//
//    private void setUI() {
//
//
//        lblFirst = (TextView) findViewById(R.id.lblDetails);
//        lblFirst.setText(getString(R.string.account_detail));
//        lblFirst.setVisibility(View.VISIBLE);
//
//
//        lblSecond = (TextView) findViewById(R.id.lblSecond);
//        lblSecond.setText(getString(R.string.merchant_details));
//        lblSecond.setVisibility(View.VISIBLE);
//
//        lblThird = (TextView) findViewById(R.id.lblThird);
//        lblThird.setText(getString(R.string.payment_details));
//        lblThird.setVisibility(View.VISIBLE);
//
//        listAccountDetails = (ListViewExpanded) findViewById(R.id.listDetails);
//        listMerchantDetails = (ListViewExpanded) findViewById(R.id.listSecond);
//        listPaymentDetails = (ListViewExpanded) findViewById(R.id.listThird);
//        listAccountDetails.setVisibility(View.VISIBLE);
//        listMerchantDetails.setVisibility(View.VISIBLE);
//        listPaymentDetails.setVisibility(View.VISIBLE);
//
//
//        try {
//            labelsDetails = new String[]{getString(R.string.transaction_type), getString(R.string.amount), getString(R.string.transaction_fee)};
//
//            dataDetails = new String[]{getString(R.string.retail_payment), Constants.CURRENCY + " " + amountF, "PKR 1000"};
//            populateData(listPaymentDetails, labelsDetails, dataDetails);
//        } catch (Exception e) {
//            AppLogger.e(e);
//        }
//
//
//        try {
//            labelsDetails = new String[]{getString(R.string.account_title), getString(R.string.account_number)};
//            dataDetails = new String[]{fromAccountModel.getAccountTitle(), Utility.getMaskedAccountNumber(AESEncryptor.decryptWithAES(Constants.SECRET_KEY, fromAccountModel.getAccountNo()))};
//            populateData(listAccountDetails, labelsDetails, dataDetails);
//        } catch (Exception e) {
//            AppLogger.e(e);
//        }
//
//        try {
//            labelsDetails = new String[]{getString(R.string.merchant_id), getString(R.string.merchant_name)};
//            dataDetails = new String[]{merchantId, merchantName};
//            populateData(listMerchantDetails, labelsDetails, dataDetails);
//        } catch (Exception e) {
//            AppLogger.e(e);
//        }
//
//
//    }
//
//    private void fetchIntent() {
//        productModel = (ProductModel) bundle.get(Constants.INTENT_KEY_PRODUCT_MODEL);
//        fromAccountModel = (CoreAccountModel) bundle.getSerializable(Constants.IntentKeys.FROM_ACCOUNT);
//        merchantId = bundle.getString(Constants.IntentKeys.MERCHANT_ID_N);
//        merchantName = bundle.getString(Constants.IntentKeys.MERCHANT_NAME_N);
//        merchantLaction = bundle.getString(Constants.IntentKeys.MERCHANT_LACTION);
//        amount = bundle.getString(Constants.IntentKeys.QR_AMOUNT_N);
//        amountF = bundle.getString(Constants.IntentKeys.QR_AMOUNT_F_N);
//    }
//
//}
//
