package com.inov8.jsblconsumer.harToWallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.TransactionReceiptActivity;
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
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class HraToWalletConfirmationActivity extends BaseCommunicationActivity {
    private String senderMobileNumber, amount, totalAmount;
    private Button btnNext, btnCancel;
    private TextView lblConfirmDetails;
    private TransactionInfoModel transactionInfo;
    private ProductModel product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_confirmation);

        fetchIntents();
        lblConfirmDetails = (TextView) findViewById(R.id.lblAlert);

        titleImplementation(null, product.getName(), "Confirm Transaction", HraToWalletConfirmationActivity.this);
        lblConfirmDetails.setText(getString(R.string.confirm_details));

        senderMobileNumber = ApplicationData.customerMobileNumber;
//        toAccount = transactionInfo.getBbacid();
//        fromAccount = transactionInfo.getCoreacid();

        totalAmount = transactionInfo.getTamtf();
        amount = transactionInfo.getTxamf();

        String labels[] = new String[]{
                "Amount", "Charges", "Total Amount"};
        String data[] = new String[]{
                Constants.CURRENCY + " " + amount,
                Constants.CURRENCY + " " + transactionInfo.getTpam(),
                Constants.CURRENCY + " " + totalAmount};

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
                        HraToWalletConfirmationActivity.this, getString(R.string.yes), getString(R.string.no), new OnClickListener() {
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

        headerImplementation();
    }

    private void fetchIntents() {
        amount = mBundle.getString(Constants.IntentKeys.TRANSACTION_AMOUNT);
        transactionInfo = (TransactionInfoModel) mBundle.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
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
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, HraToWalletConfirmationActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(HraToWalletConfirmationActivity.this).execute(
                Constants.CMD_HRA_TO_WALLET + "",getEncryptedMpin(),
                product.getId(), transactionInfo.getTxam(),
                transactionInfo.getTamt(),transactionInfo.getCamt(), transactionInfo.getTpam());
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);

                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
                        HraToWalletConfirmationActivity.this, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                }

                            }
                        }, PopupDialogs.Status.ERROR);
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