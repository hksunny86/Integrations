package com.inov8.jsblconsumer.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.fundsTransfer.FundsTransferConfirmationActivity;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionReceiptActivity extends BaseActivity {
    private String totalAmount, bankName;
    private ImageView ivTickIcon;
    private Byte flowId;
    private TextView txtAmount, txtPaidLabel, txtHeading;
    private String[] labels, data;
    private TransactionModel transaction;
    private Dialog dialog;
    private String productName;
    private boolean doShowRegistrationPopup = false;
    private byte paymentType;
    private int mSubFlowId;
    private ProductModel product;
    private TransactionInfoModel transactionInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_transaction_receipt);
        headerImplementation();
        bottomBarImplementation(TransactionReceiptActivity.this, "");
        checkSoftKeyboardD();

        btnBack.setVisibility(View.GONE);
        try {
            fetchIntents();
            ivTickIcon = (ImageView) findViewById(R.id.ivIconTick);
            ivTickIcon.setVisibility(View.VISIBLE);
            txtPaidLabel = (TextView) findViewById(R.id.txtPaidlabel);
            txtAmount = (TextView) findViewById(R.id.txtAmount);
            txtHeading = (TextView) findViewById(R.id.lblHeading);
            if (totalAmount == null || totalAmount.equals("")) {
                txtAmount.setVisibility(View.GONE);
            } else {
                txtAmount.setText(Utility.getFormattedCurrency(Utility.getFormatedAmount(totalAmount)));
            }

            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);

            switch (flowId) {

                case Constants.FLOW_ID_SI_SCHEDULE:
                    txtHeading.setText("Successful Card Activation");
                    labels = new String[]{"Mobile No.", "CNIC No.", "Name",
                            "Card No.", "Card Program", "Date & Time"};
                    data = new String[]{
                            transaction.getMobn(), transaction.getCnic(), transaction.getActitle(),
                            transaction.getCardNo(), transaction.getCpname(), transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_DEBIT_CARD_ACTIVATION:
                    txtHeading.setText("Successful Card Activation");
                    labels = new String[]{"Mobile No.", "CNIC No.", "Name",
                            "Card No.", "Card Program", "Date & Time"};
                    data = new String[]{
                            transaction.getMobn(), transaction.getCnic(), transaction.getActitle(),
                            transaction.getCardNo(), transaction.getCpname(), transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_TRANSFER_IN:
                    txtHeading.setText("Transfer In Successful");
                    labels = new String[]{"From Account", "To Account",
                            "Amount", "Charges", "Transaction ID",
                            "Date & Time"};
                    data = new String[]{
                            transaction.getCoreacid(), transaction.getBbacid(),
                            Constants.CURRENCY + " " + transaction.getTxamf(),
                            Constants.CURRENCY + " " + transaction.getTpamf(),
                            transaction.getId(), transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_TRANSFER_OUT:
                    txtHeading.setText("Transfer Out Successful");
                    labels = new String[]{"From Account", "To Account",
                            "Amount", "Charges", "Transaction ID", "Date & Time"};
                    data = new String[]{
                            transaction.getBbacid(), transaction.getCoreacid(),
                            Constants.CURRENCY + " " + transaction.getTxamf(),
                            Constants.CURRENCY + " " + transaction.getTpamf(),
                            transaction.getId(), transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_BLB:
                    txtHeading.setText(transaction.getProd()+" Transaction Successful");
//                    txtHeading.setText("Money Transfer Successful");
                    labels = new String[]{
                            "Receiver Mobile No.", "Receiver A/C Title", "Amount",
                            "Charges", "Transaction ID", "Date & Time"};
                    data = new String[]{
                            transaction.getRcmob(), transactionInfo.getCoreactl(),
                            Constants.CURRENCY + " " + transaction.getTxamf(),
                            Constants.CURRENCY + " " + transaction.getTpamf(),
                            transaction.getId(),
                            transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                    txtHeading.setText(transaction.getProd()+" Transaction Successful");
//                    txtHeading.setText("Money Transfer Successful");
                    labels = new String[]{
                            "Receiver Mobile No.", "Receiver CNIC", "Amount",
                            "Charges", "Transaction ID", "Date & Time"};
                    data = new String[]{
                            transaction.getRcmob(), transaction.getRwcnic(),
                            Constants.CURRENCY + " " + transaction.getTxamf(),
                            Constants.CURRENCY + " " + transaction.getTpamf(),
                            transaction.getId(),
                            transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                    txtHeading.setText(transaction.getProd()+" Transaction Successful");
//                    txtHeading.setText("Money Transfer Successful");
                    labels = new String[]{"Receiver Mobile No.", "Receiver A/C No.",
                            "Receiver A/C Title", "Amount", "Charges", "Transaction ID",
                            "Date & Time"};
                    data = new String[]{transaction.getRcmob(),
                            transaction.getCoreacid(), transaction.getCoreactl(),
                            Constants.CURRENCY + " " + transaction.getTxamf(),
                            Constants.CURRENCY + " " + transaction.getTpamf(),
                            transaction.getId(), transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_IBFT:
                    txtHeading.setText(product.getName()+" Transaction Successful");
                    labels = new String[]{"Receiver Bank", "Receiver Mobile No.",
                            "Receiver A/C No.", "Receiver A/C Title",
                            "Amount", "Charges", "Transaction ID", "Date & Time"};
                    data = new String[]{bankName, transaction.getRcmob(),
                            transaction.getCoreacid(), transaction.getCoreactl(),
                            Constants.CURRENCY + " " + transaction.getTxamf(),
                            Constants.CURRENCY + " " + transaction.getTpamf(),
                            transaction.getId(),
                            transaction.getDatef()};
                    break;


                case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
                    txtHeading.setText(transaction.getProd() + " Transaction Successful");

//                    txtHeading.setText(product.getName() + " Successful");

                    labels = new String[]{"Billing Company", "Consumer No.","Charges",
                            "Transaction ID", "Date & Time"};
                    data = new String[]{transaction.getProductName(),
                            transaction.getConsumer(),
                            Constants.CURRENCY + " " + transaction.getTpamf(),
                            transaction.getId(),
                            transaction.getDatef()};
                    ApplicationData.formattedBalance = transaction.getBalf();
                    break;

                case Constants.FLOW_ID_HRA_TO_WALLET:
                    txtHeading.setText(transaction.getProd() + " Transaction Successful");
                    labels = new String[]{"Amount", "Charges", "Transaction ID", "Date & Time"};
                    data = new String[]{
                            transaction.getTamtf(),
                            transaction.getTpamf(),
                            transaction.getId(),
                            transaction.getDatef() + " " + transaction.getTimef()};
                    ApplicationData.formattedBalance = transaction.getBalf();
                    break;

                case Constants.FLOW_ID_ADVANCE_LOAN:
                    txtHeading.setText(transaction.getProd() + " Transaction Successful");
                     labels = new String[]{"Transaction ID", "Amount", "Charges", "Total Amount", "Date & Time"};
                    data = new String[]{
                            transaction.getId(),
                            transaction.getTxamf(),
                            transaction.getCamtf(),
                            transaction.getTamtf(),
                            transaction.getDatef()};
                    ApplicationData.formattedBalance = transaction.getBalf();
                    break;

                case Constants.FLOW_ID_MINI_LOAD:
                    txtHeading.setText("Mini Load Successful");
                    labels = new String[]{"Mobile No.", "Transaction ID", "Date & Time"};
                    data = new String[]{
                            transaction.getTmob(),
                            transaction.getId(),
                            transaction.getDatef()};
                    ApplicationData.formattedBalance = transaction.getBalf();
                    break;

                case Constants.FLOW_ID_E_TICKETING:
                    txtHeading.setText(transaction.getProd() + " Transaction Successful");
                    labels = new String[]{"Mobile No.",
                            "Challan No.", "Transaction ID", "Amount", "Charges", "Total Amount", "Date & Time"};
                    data = new String[]{
                            transaction.getMobNo(),
                            transactionInfo.getConsumer(),
                            transaction.getId(),
                            Constants.CURRENCY + transaction.getTxamf(),
                            Constants.CURRENCY + transaction.getTpamf(),
                            Constants.CURRENCY + transaction.getTamtf(),
                            transaction.getDatef()};
                    ApplicationData.formattedBalance = transaction.getBalf();
                    break;

                case Constants.FLOW_ID_RETAIL_PAYMENT:
                    txtHeading.setText("Retail Payment Successful");
                    labels = new String[]{"Merchant", "Merchant Mobile No.",
                            "Amount", "Charges", "Transaction ID", "Date & Time"};
                    data = new String[]{transaction.getRaname(), transaction.getAmob(),
                            Constants.CURRENCY + " " + transaction.getTxamf(),
                            Constants.CURRENCY + " " + transaction.getTpamf(),
                            transaction.getId(),
                            transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_BALANCE_INQUIRY:
                    txtPaidLabel.setText("Current Account Balance");
                    labels = new String[]{"Current Account Balance",
                            "Date & Time"};
                    data = new String[]{
                            Constants.CURRENCY + " " + totalAmount,
                            Utility.getFormattedDate() + " "
                                    + Utility.getFormattedTime()};
                    break;
            }

            updateBalance();
            populateReceipt(listView, labels, data);

            Button btnOK = (Button) findViewById(R.id.btnOK);
            btnOK.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (doShowRegistrationPopup) {
                        showRegistrationPopup();
                    } else {
                        goToMainMenu();
                    }
                }
            });

            ScrollView scView = (ScrollView) findViewById(R.id.scView);
            scView.scrollTo(0, 0);
            scView.smoothScrollTo(0, 0);
        } catch (Exception ex) {
            ex.getMessage();
        }

        headerImplementation();
    }

    private void fetchIntents() {
        transaction = (TransactionModel) mBundle.get(Constants.IntentKeys.TRANSACTION_MODEL);
        transactionInfo = (TransactionInfoModel) mBundle.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
        doShowRegistrationPopup = mBundle.getBoolean(Constants.IntentKeys.SHOW_REGISTRATION_POPUP);
        productName = mBundle.getString(Constants.IntentKeys.PRODUCT_NAME);
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        paymentType = mBundle.getByte(Constants.IntentKeys.PAYMENT_TYPE);
        mSubFlowId = mBundle.getInt(Constants.IntentKeys.SUB_FLOW_ID);
        bankName = (String) mBundle.get(Constants.IntentKeys.BANK_NAME);

        if (transaction != null) {
            totalAmount = transaction.getTamtf();
        } else {
            totalAmount = "";
        }
    }

    @Override
    public void onBackPressed() {
        if (doShowRegistrationPopup) {
            showRegistrationPopup();
        } else {
            goToMainMenu();
        }
    }

    private void showRegistrationPopup() {
        dialogGeneral = PopupDialogs.createConfirmationDialog(AppMessages.REGISTERATION_REQUIRED
                        + transaction.getSwmob() + ".", getString(R.string.alertNotification)
                , TransactionReceiptActivity.this, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();
                        switch (flowId) {
//                            case Constants.FLOW_ID_BILL_PAYMENT_GAS:
                            case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
//                            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID:
//                            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID:
//                            case Constants.FLOW_ID_BILL_PAYMENT_TELEPHONE:
                                switch (paymentType) {
                                    case Constants.PAYMENT_TYPE_ACCOUNT:
                                        break;
                                    case Constants.PAYMENT_TYPE_CASH:
                                        gotoAccountOpening(transaction.getSwmob());
                                        break;
                                }
                                break;
                            default:
                                gotoAccountOpening();
                                break;
                        }
                    }
                }, "REGISTER",
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();
                        goToMainMenu();
                    }
                }, "CANCEL", PopupDialogs.Status.INFO);

    }

//    public void headerImplementation() {
//        btnBack = (AppCompatImageView) findViewById(R.id.btnBack);
//        btnHome = (AppCompatImageView) findViewById(R.id.btnHome);
//        btnSignout = (AppCompatImageView) findViewById(R.id.btnSignout);
//        viewLeft = findViewById(R.id.viewLeft);
//
//        btnBack.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (doShowRegistrationPopup) {
//                    showRegistrationPopup();
//                } else {
//                    goToMainMenu();
//                }
//            }
//        });
//
//        if (!ApplicationData.isLogin) {
//            btnHome.setVisibility(View.GONE);
//            btnSignout.setVisibility(View.GONE);
//        } else {
//            btnHome.setVisibility(View.VISIBLE);
//            btnSignout.setVisibility(View.VISIBLE);
//            btnBack.setVisibility(View.GONE);
//            viewLeft.setVisibility(View.VISIBLE);
//
//            btnHome.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ApplicationData.webUrl = null;
//                    Intent intent = new Intent(TransactionReceiptActivity.this, MainMenuActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                }
//            });
//        }
//
//        btnSignout.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//                dialog = PopupDialogs.createConfirmationDialog(
//                        getString(R.string.logout__text),
//                        getString(R.string.alertNotification), TransactionReceiptActivity.this,
//                        new OnClickListener() {
//                            @Override
//                            public void onClick(View arg0) {
//                                dialog.cancel();
//                                Intent intent = new Intent(TransactionReceiptActivity.this,
//                                        SignOutActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }, "Yes", PopupDialogs.Status.INFO);
//
//            }
//        });
//
//    }

    private void updateBalance() {
        //TextView txtViewAuthMessage = (TextView) findViewById(R.id.textviewauthenticate);
        switch (flowId) {
//            case Constants.FLOW_ID_BILL_PAYMENT_GAS:
            case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
//            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID:
//            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID:
//            case Constants.FLOW_ID_BILL_PAYMENT_TELEPHONE:

                switch (paymentType) {
                    case Constants.PAYMENT_TYPE_ACCOUNT:
//                        		txtViewAuthMessage.setVisibility(View.VISIBLE);
                        break;
                    case Constants.PAYMENT_TYPE_CASH:
                        ApplicationData.formattedBalance = transaction.getBalf();
                        break;
                }
                break;
            case Constants.FLOW_ID_DEBIT_CARD_ACTIVATION:
                break;
            default:
                ApplicationData.formattedBalance = transaction.getBalf();
                break;
        }
    }

    public void gotoAccountOpening(String CMOB) {
        Intent intent = new Intent(getApplicationContext(),
                MainMenuActivity.class);
        intent.putExtra(Constants.IntentKeys.FLAG_TRANSITION,
                Constants.IntentKeys.FLAG_OPEN_ACCOUNT);
        intent.putExtra(Constants.IntentKeys.RCMOB, CMOB);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void populateReceipt(ListViewExpanded listView, String[] labels,
                                 String[] data) {

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

        listView.setAdapter(adapter);
        Utility.getListViewSize(listView, this, mListItemHieght);
    }
}