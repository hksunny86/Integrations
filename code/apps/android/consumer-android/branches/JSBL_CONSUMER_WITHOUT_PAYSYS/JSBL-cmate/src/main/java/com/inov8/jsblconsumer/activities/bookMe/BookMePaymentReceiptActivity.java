package com.inov8.jsblconsumer.activities.bookMe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.MainMenuActivity;
import com.inov8.jsblconsumer.activities.TransactionReceiptActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
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

public class BookMePaymentReceiptActivity extends BaseCommunicationActivity {

    private String totalAmount, bankName, orderRedId, bookMeServiceProvider, type, bookMeDate, bookMeTime, bookMeEventVenue, bookHotelName, bookMeCusPhone, bookMeCusEmail, bookMeCusCnic, bookMeCusName;
    private ImageView ivTickIcon;
    private Byte flowId;
    private TextView txtAmount, txtPaidLabel, txtHeading;
    private String[] labels, data;
    private Dialog dialog;
    private String productName;
    private boolean doShowRegistrationPopup = false;
    private byte paymentType;
    private int mSubFlowId;
    private ProductModel product;
    private TransactionInfoModel transactionInfo;
    private TransactionModel transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_me_payment_receipt);
        headerImplementation();
        bottomBarImplementation(BookMePaymentReceiptActivity.this, "");
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
                txtAmount.setText(Utility.getFormattedCurrency(Utility.getFormatedAmount(transactionInfo.getTamt())));
            }

            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);

            txtHeading.setText(transaction.getProd() + " Transaction Successful");
            if (flowId == Constants.FLOW_ID_BOOKME_AIR) {
                labels = new String[]{"Transaction ID", "Mobile No.", "CNIC No.", "Bookme Mobile No.", "Bookme CNIC No.", "Transaction Type", "Date & Time", "Amount", "Charges", "Total Amount"};
                data = new String[]{transaction.getId(), ApplicationData.mobileNo, ApplicationData.cnic, bookMeCusPhone, bookMeCusCnic, type, transaction.getDatef(), Constants.CURRENCY + " " + transaction.getTxam(),
                        Constants.CURRENCY + " " + transaction.getTpam(), Constants.CURRENCY + " " + transaction.getTamt()};

            } else if (flowId == Constants.FLOW_ID_BOOKME_CINEMA) {
                labels = new String[]{"Transaction ID", "Mobile No.", "CNIC No.", "Bookme Mobile No.", "Bookme CNIC No.", "Transaction Type", "Date & Time", "Amount", "Charges", "Total Amount"};
                data = new String[]{transaction.getId(), ApplicationData.mobileNo, ApplicationData.cnic, bookMeCusPhone, bookMeCusCnic, type, transaction.getDatef(), Constants.CURRENCY + " " + transaction.getTxam(),
                        Constants.CURRENCY + " " + transaction.getTpam(), Constants.CURRENCY + " " + transaction.getTamt()};

            } else if (flowId == Constants.FLOW_ID_BOOKME_EVENT) {
                labels = new String[]{"Transaction ID", "Mobile No.", "CNIC No.", "Bookme Mobile No.", "Bookme CNIC No.", "Transaction Type", "Date & Time", "Amount", "Charges", "Total Amount"};
                data = new String[]{transaction.getId(), ApplicationData.mobileNo, ApplicationData.cnic, bookMeCusPhone, bookMeCusCnic, type, transaction.getDatef(), Constants.CURRENCY + " " + transaction.getTxam(),
                        Constants.CURRENCY + " " + transaction.getTpam(), Constants.CURRENCY + " " + transaction.getTamt()};

            } else if (flowId == Constants.FLOW_ID_BOOKME_HOTEL) {
                labels = new String[]{"Transaction ID", "Mobile No.", "CNIC No.", "Bookme Mobile No.", "Bookme CNIC No.","Transaction Type", "Date & Time", "Amount", "Charges", "Total Amount"};
                data = new String[]{transaction.getId(), ApplicationData.mobileNo, ApplicationData.cnic, bookMeCusPhone, bookMeCusCnic,type, transaction.getDatef(), Constants.CURRENCY + " " + transaction.getTxam(),
                        Constants.CURRENCY + " " + transaction.getTpam(), Constants.CURRENCY + " " + transaction.getTamt()};

            } else {
                labels = new String[]{"Transaction ID", "Mobile No.", "CNIC No.", "Bookme Mobile No.", "Bookme CNIC No.", "Transaction Type", "Date & Time", "Amount", "Charges", "Total Amount"};
                data = new String[]{transaction.getId(), ApplicationData.mobileNo, ApplicationData.cnic, bookMeCusPhone, bookMeCusCnic, type, transaction.getDatef(), Constants.CURRENCY + " " + transaction.getTxam(),
                        Constants.CURRENCY + " " + transaction.getTpam(), Constants.CURRENCY + " " + transaction.getTamt()};
            }

//            updateBalance();
            populateReceipt(listView, labels, data);

            Button btnOK = (Button) findViewById(R.id.btnOK);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (doShowRegistrationPopup) {
                        showRegistrationPopup();
                    } else {
                        updateBalance();
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
        orderRedId = mBundle.getString(Constants.IntentKeys.BOOKME_ORDER_REF_ID);
        type = mBundle.getString(Constants.IntentKeys.BOOKME_TYPE);
        bookMeDate = mBundle.getString(Constants.IntentKeys.BOOKME_DATE);
        bookMeTime = mBundle.getString(Constants.IntentKeys.BOOKME_TIME);
        bookMeEventVenue = mBundle.getString(Constants.IntentKeys.BOOKME_EVENT_VENUE);
        bookMeServiceProvider = mBundle.getString(Constants.IntentKeys.BOOKME_SERVICE_PROVIDER);
        bookHotelName = mBundle.getString(Constants.IntentKeys.BOOKME_HOTEL_NAME);
        bookMeCusPhone = mBundle.getString(Constants.IntentKeys.BOOKME_CUSTOMER_PHONE);
        bookMeCusEmail = mBundle.getString(Constants.IntentKeys.BOOKME_CUSTOMER_EMAIL);
        bookMeCusCnic = mBundle.getString(Constants.IntentKeys.BOOKME_CUSTOMER_CNIC);
        bookMeCusName = mBundle.getString(Constants.IntentKeys.BOOKME_CUSTOMER_NAME);

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
                        + "transaction.getSwmob()" + ".", getString(R.string.alertNotification)
                , BookMePaymentReceiptActivity.this, new View.OnClickListener() {
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
//                                        gotoAccountOpening(transaction.getSwmob());
                                        break;
                                }
                                break;
                            default:
                                gotoAccountOpening();
                                break;
                        }
                    }
                }, "REGISTER",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();
                        goToMainMenu();
                    }
                }, "CANCEL", PopupDialogs.Status.INFO);

    }

    private void updateBalance() {
        ApplicationData.formattedBalance = transaction.getBalf();
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

    @Override
    public void processRequest() {

    }

    @Override
    public void processResponse(HttpResponseModel response) {

    }

    @Override
    public void processNext() {

    }

}