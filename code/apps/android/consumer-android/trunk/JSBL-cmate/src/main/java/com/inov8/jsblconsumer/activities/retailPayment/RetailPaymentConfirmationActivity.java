package com.inov8.jsblconsumer.activities.retailPayment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.TransactionReceiptActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class RetailPaymentConfirmationActivity extends
        BaseCommunicationActivity {
    private String mobileNumber, amount, totalAmount, merchant;
    private String charges;
    private Button btnNext, btnCancel;
    private TextView lblAlert;
    private TransactionInfoModel transactionInfo;
    private ProductModel product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_confirmation);

        try {
            fetchIntents();
            headerImplementation();
            titleImplementation(null, "Retail Payment", getString(R.string.confirm_transaction), this);

            lblAlert = (TextView) findViewById(R.id.lblAlert);
            lblAlert.setText("Please verify details.");

            mobileNumber = transactionInfo.getAmob();
            amount = transactionInfo.getTxamf();
            charges = transactionInfo.getTpamf();
            totalAmount = transactionInfo.getTamtf();
            merchant = transactionInfo.getRaname();

            String labels[] = new String[]{"Merchant", "Merchant Mobile No.", "Amount", "Charges", "Total Amount"};
            String data[] = new String[]{merchant, mobileNumber, Constants.CURRENCY + " " + amount,
                    Constants.CURRENCY + " " + charges, Constants.CURRENCY + " " + totalAmount};

            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < data.length; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("label", labels[i]);
                hm.put("data", data[i]);
                aList.add(hm);
            }

            String[] from = {"label", "data"};
            int[] to = {R.id.txtLabel, R.id.txtData};
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_item_with_data, from, to);
            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);
            listView.setAdapter(adapter);

            // @SuppressWarnings("deprecation")
            // RelativeLayout.LayoutParams params = new
            // RelativeLayout.LayoutParams(
            // LayoutParams.FILL_PARENT, (int) TypedValue.applyDimension(
            // TypedValue.COMPLEX_UNIT_DIP, (mListItemHieght+2) * (data.length),
            // getResources().getDisplayMetrics()));
            //
            // params.leftMargin = 10;
            // params.rightMargin = 10;
            // params.topMargin = 20;
            // params.addRule(RelativeLayout.BELOW, R.id.layoutHeading);
            // listView.setLayoutParams(params);

            Utility.getListViewSize(listView, this, mListItemHieght);

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    // mBundle.putString("MOBILE", mobile_no);
                    // mBundle.putString("TOTAL_AMOUNT", total_amount);
                    // mBundle.putString("AMOUNT", amount);
                    // mBundle.putString("CHARGES", charges + "");

                    askMpin(mBundle, TransactionReceiptActivity.class, false);
                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION, AppMessages.ALERT_HEADING,
                            RetailPaymentConfirmationActivity.this, getString(R.string.yes), getString(R.string.no), new OnClickListener() {
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

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void fetchIntents() {
        transactionInfo = (TransactionInfoModel) mBundle.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, RetailPaymentConfirmationActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(RetailPaymentConfirmationActivity.this).execute(
                Constants.CMD_RETAIL_PAYMENT + "", getEncryptedMpin(),
                product.getId(), transactionInfo.getAmob(),
                transactionInfo.getTxam(), "1", transactionInfo.getCamt(),
                transactionInfo.getTpam(), transactionInfo.getTamt());
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

                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
                        RetailPaymentConfirmationActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                    askMpin(mBundle, TransactionReceiptActivity.class, false);
                                }

                            }
                        }, PopupDialogs.Status.ERROR);
            } else {
                if (table.containsKey(Constants.KEY_LIST_TRANS)) {
                    @SuppressWarnings("unchecked")
                    List<TransactionModel> list = (List<TransactionModel>) table.get(Constants.KEY_LIST_TRANS);
                    TransactionModel transaction = list.get(0);
                    Log.d("amob", "amob: " + transaction.getAmob());
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