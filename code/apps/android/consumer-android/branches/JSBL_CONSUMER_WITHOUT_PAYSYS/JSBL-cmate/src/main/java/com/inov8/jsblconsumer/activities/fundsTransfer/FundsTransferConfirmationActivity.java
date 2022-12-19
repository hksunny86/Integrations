package com.inov8.jsblconsumer.activities.fundsTransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.TransactionReceiptActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.schedule.ScheduleActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.ScheduleModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class FundsTransferConfirmationActivity extends BaseCommunicationActivity {
    private TextView lblAlert, tvSchedulePayment;
    private String strProductId, strAgentMobileNumber, strSenderCnic,
            strSenderMobileNumber, strReceiverCnic, strReceiverMobileNumber,
            strAccountNumber, strAccountTitle, strAmount, strCharges,
            strCommissionAmount, strCommissionAmountF, strTotalAmount, strAmountF, strChargesF,
            strTotalAmountF, bankName, bankImd, recieverNumber, transactionPuropseName, purposeCode;
    private Button btnNext, btnCancel;
    private String labels[], data[];
    private ProductModel product;
    private ScheduleModel scheduleModel = new ScheduleModel();
    private TransactionInfoModel transactionInfo;
    private byte flowId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_confirmation);

        fetchIntents();
        titleImplementation(null, product.getName(), "Confirm Transaction", this);

        bottomBarImplementation(FundsTransferConfirmationActivity.this, "");
        checkSoftKeyboardD();

        lblAlert = (TextView) findViewById(R.id.lblAlert);
        tvSchedulePayment = (TextView) findViewById(R.id.tvSchedulePayment);
        lblAlert.setText("Please verify details.");

        strAgentMobileNumber = ApplicationData.customerMobileNumber;
        strAmount = transactionInfo.getTxam();
        strCharges = transactionInfo.getTpam();
        strCommissionAmount = transactionInfo.getCamt();
        strTotalAmount = transactionInfo.getTamt();

        strAmountF = transactionInfo.getTxamf();
        strChargesF = transactionInfo.getTpamf();
        strTotalAmountF = transactionInfo.getTamtf();
        strCommissionAmountF = transactionInfo.getCamtf();

        strProductId = transactionInfo.getPid();

        Intent schedulingIntent = new Intent(FundsTransferConfirmationActivity.this, ScheduleActivity.class);

        switch (flowId) {
            case Constants.FLOW_ID_FT_BLB_TO_BLB:
      //          tvSchedulePayment.setVisibility(View.VISIBLE);
                strSenderMobileNumber = transactionInfo.getCmob();
                strReceiverMobileNumber = transactionInfo.getRcmob();

                labels = new String[]{"Receiver Mobile No.","Receiver A/C Title",
                        "Amount", "Charges", "Total Amount"};
                data = new String[]{strReceiverMobileNumber, transactionInfo.getCoreactl(),Constants.CURRENCY + " " + strAmountF,
                        Constants.CURRENCY + " " + strChargesF,
                        Constants.CURRENCY + " " + strTotalAmountF};
                scheduleModel.setFlowId(Constants.FLOW_ID_FT_BLB_TO_BLB);
                scheduleModel.setStrProductId(strProductId);
                scheduleModel.setStrSenderMobileNumber(strSenderMobileNumber);
                scheduleModel.setStrAmount(strAmount);
                scheduleModel.setStrCommissionAmount(strCommissionAmount);
                scheduleModel.setStrCharges(strCharges);
                scheduleModel.setStrTotalAmount(strTotalAmount);

                schedulingIntent.putExtra(Constants.IntentKeys.SCHEDULE_MODEL, scheduleModel);
                break;

            case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                tvSchedulePayment.setVisibility(View.GONE);
                strSenderMobileNumber = transactionInfo.getCmob();
                strReceiverCnic = transactionInfo.getRwcnic();
                strReceiverMobileNumber = transactionInfo.getRwmob();

                labels = new String[]{
                        "Receiver Mobile No.", "Receiver CNIC", "Amount", "Charges", "Total Amount"};
                data = new String[]{
                        strReceiverMobileNumber, strReceiverCnic, Constants.CURRENCY + " " + strAmountF,
                        Constants.CURRENCY + " " + strChargesF,
                        Constants.CURRENCY + " " + strTotalAmountF};
                break;

            case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
       //         tvSchedulePayment.setVisibility(View.VISIBLE);
                strAccountNumber = transactionInfo.getCoreacid();
                strAccountTitle = transactionInfo.getCoreactl();
                strReceiverMobileNumber = transactionInfo.getRcmob();

                labels = new String[]{"Receiver Mobile No.", "Receiver A/C No.",
                        "Receiver A/C Title", "Amount",
                        "Charges", "Total Amount"};
                data = new String[]{strReceiverMobileNumber,
                        strAccountNumber, strAccountTitle,
                        Constants.CURRENCY + " " + strAmountF,
                        Constants.CURRENCY + " " + strChargesF,
                        Constants.CURRENCY + " " + strTotalAmountF};

                scheduleModel.setFlowId(Constants.FLOW_ID_FT_BLB_TO_CORE_AC);
                scheduleModel.setStrProductId(strProductId);
                scheduleModel.setStrSenderMobileNumber(strSenderMobileNumber);
                scheduleModel.setStrAmount(strAmount);
                scheduleModel.setStrCommissionAmount(strCommissionAmount);
                scheduleModel.setStrCharges(strCharges);
                scheduleModel.setStrTotalAmount(strTotalAmount);
                scheduleModel.setStrAccountNumber(strAccountNumber);
                scheduleModel.setStrAccountTitle(strAccountTitle);

                schedulingIntent.putExtra(Constants.IntentKeys.SCHEDULE_MODEL, scheduleModel);
                break;

            case Constants.FLOW_ID_FT_BLB_TO_IBFT:
      //          tvSchedulePayment.setVisibility(View.VISIBLE);
                strAccountNumber = transactionInfo.getCoreacid();
                strAccountTitle = transactionInfo.getCoreactl();

                labels = new String[]{"Receiver Bank ", "Receiver Mobile No.",
                        "Receiver A/C No.", "Receiver A/C Title",
                        "Amount", "Charges", "Total Amount"};
                data = new String[]{bankName, transactionInfo.getRcmob(),
                        strAccountNumber, strAccountTitle,
                        Constants.CURRENCY + " " + strAmountF,
                        Constants.CURRENCY + " " + strChargesF,
                        Constants.CURRENCY + " " + strTotalAmountF};

                scheduleModel.setFlowId(Constants.FLOW_ID_FT_BLB_TO_IBFT);
                scheduleModel.setStrProductId(strProductId);
                scheduleModel.setStrSenderMobileNumber(strSenderMobileNumber);
                scheduleModel.setStrAmount(strAmount);
                scheduleModel.setStrCommissionAmount(strCommissionAmount);
                scheduleModel.setStrCharges(strCharges);
                scheduleModel.setStrTotalAmount(strTotalAmount);
                scheduleModel.setBankImd(bankImd);
                scheduleModel.setStrCommissionAmountF(strCommissionAmountF);
                scheduleModel.setStrChargesF(strChargesF);
                scheduleModel.setStrTotalAmountF(strTotalAmountF);
                scheduleModel.setStrAmountF(strAmountF);
                scheduleModel.setBankName(bankName);
                scheduleModel.setBranchName(transactionInfo.getBranchName());
                scheduleModel.setIban(transactionInfo.getIban());
                scheduleModel.setCrDr(transactionInfo.getCrDr());
                scheduleModel.setCoreactl(transactionInfo.getCoreactl());
                scheduleModel.setPurposeCode(purposeCode);

                schedulingIntent.putExtra(Constants.IntentKeys.SCHEDULE_MODEL, scheduleModel);
                break;
        }

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
                R.layout.list_item_with_data, from, to);

        ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);
        listView.setAdapter(adapter);

        Utility.getListViewSize(listView, this, mListItemHieght);

        tvSchedulePayment.setOnClickListener(view -> startActivity(schedulingIntent));

        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                askMpin(mBundle, TransactionReceiptActivity.class, false);
            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION, AppMessages.ALERT_HEADING,
                        FundsTransferConfirmationActivity.this, getString(R.string.yes), getString(R.string.no), new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialogGeneral.dismiss();
                                goToMainMenu();
                            }
                        }, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();


                            }
                        }, false, PopupDialogs.Status.ALERT, false);


            }
        });
        addAutoKeyboardHideFunction();
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
    }

    private void fetchIntents() {
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
        transactionInfo = (TransactionInfoModel) mBundle.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
        bankName = (String) mBundle.get(Constants.IntentKeys.BANK_NAME);
        bankImd = (String) mBundle.get(Constants.IntentKeys.BANK_IMD);
        transactionPuropseName = (String) mBundle.get(Constants.IntentKeys.TRANSACTION_PURPOSE_NAME);
        purposeCode = (String) mBundle.get(Constants.IntentKeys.PURPOSE_CODE);
        recieverNumber = (String) mBundle.get(XmlConstants.Attributes.RWMOB);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    FundsTransferConfirmationActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");

        switch (flowId) {
            case Constants.FLOW_ID_FT_BLB_TO_BLB:
                new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                        Constants.CMD_FUNDS_TRANSFER_BLB2BLB + "",
                        getEncryptedMpin(), strProductId, strReceiverMobileNumber, strAmount,
                        strCommissionAmount, strCharges, strTotalAmount);
                break;

            case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                        Constants.CMD_FUNDS_TRANSFER_BLB2CNIC + "",
                        getEncryptedMpin(), strProductId, strReceiverMobileNumber,
                        strReceiverCnic, strAmount, strCommissionAmount,
                        strCharges, strTotalAmount, transactionPuropseName,purposeCode!=null? purposeCode: "");
                break;

            case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                        Constants.CMD_FUNDS_TRANSFER_BLB2CORE + "",
                        getEncryptedMpin(), strProductId, strReceiverMobileNumber, strAccountNumber, strAccountTitle, strAmount,
                        strCommissionAmount, strCharges, strTotalAmount);
                break;

            case Constants.FLOW_ID_FT_BLB_TO_IBFT:
                new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                        Constants.CMD_FUNDS_TRANSFER_BLB2CORE + "",
                        getEncryptedMpin(), strProductId, transactionInfo.getRcmob(),
                        strAccountNumber, strAccountTitle, bankImd, strCommissionAmount,
                        strCommissionAmountF, strCharges, strChargesF, strTotalAmount,
                        strTotalAmountF, strAmount, strAmountF,transactionInfo.getBankName(),
                        transactionInfo.getBranchName(),transactionInfo.getIban(),transactionInfo.getCrDr(),
                        transactionInfo.getCoreactl(), purposeCode!=null? purposeCode: "");
                break;
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
                final MessageModel message = (MessageModel) list.get(0);



                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        FundsTransferConfirmationActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();

                                if (message != null && message.getCode() != null && message.getCode().equals(Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("2")) {
                                    goToMainMenu();
                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                    askMpin(mBundle, TransactionReceiptActivity.class, false);
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);



//
//                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        FundsTransferConfirmationActivity.this, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGeneral.cancel();
//                                if (message != null
//                                        && message.getCode() != null
//                                        && message.getCode().equals(
//                                        Constants.ErrorCodes.INTERNAL)) {
//                                    goToMainMenu();
//                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
//                                    askMpin(mBundle, TransactionReceiptActivity.class, false);
//                                }
//
//
//                            }
//                        }, PopupDialogs.Status.ERROR);
            } else {
                if (table.containsKey(Constants.KEY_LIST_TRANS)) {
                    @SuppressWarnings("unchecked")
                    List<TransactionModel> list = (List<TransactionModel>) table.get(Constants.KEY_LIST_TRANS);
                    TransactionModel transaction = list.get(0);

                    Intent intent = new Intent(getApplicationContext(), TransactionReceiptActivity.class);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL, transaction);
                    intent.putExtras(mBundle);
                    startActivity(intent);
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
    }
}