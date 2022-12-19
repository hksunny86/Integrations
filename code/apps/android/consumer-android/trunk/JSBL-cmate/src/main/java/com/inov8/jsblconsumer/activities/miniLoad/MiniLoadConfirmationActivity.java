package com.inov8.jsblconsumer.activities.miniLoad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;

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
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class MiniLoadConfirmationActivity extends BaseCommunicationActivity {
    private Button btnNext, btnCancel;
    private TextView lblAmount, lblAlert, lblPartialPay;
    private EditText inputAmount;
    private String consumerNo, dueDate, lateAmountFormatted, totalAmountFormatted, totalAmount, billStatus,
            mobileNumber, amount, billType;
    private byte paymentType;
    private TransactionInfoModel transactionInfo;
    private ProductModel product;
    private Byte flowId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_confirmation);

        try {
            fetchIntents();
            headerImplementation();
            titleImplementation(null, product.getName(), "Confirm Transaction", this);

//            consumerNo = transactionInfo.getTmob();
//            mobileNumber = transactionInfo.getTxam();
//            dueDate = transactionInfo.getCamt();
//            totalAmount = transactionInfo.getTpam();
//            totalAmountFormatted = transactionInfo.getTamt();

            lblAlert = (TextView) findViewById(R.id.lblAlert);
            lblAlert.setText("Please verify details.");

            String labels[], data[];
            labels = new String[]{"Mobile No.", "Topup Amount"};
            data = new String[]{transactionInfo.getTmob(),
                    Constants.CURRENCY + " " + Utility.getFormatedAmount(transactionInfo.getTxam())};

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
            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    mBundle.putString(Constants.IntentKeys.TRANSACTION_AMOUNT, transactionInfo.getTxam());
                    askMpin(mBundle, TransactionReceiptActivity.class, false);
                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION, AppMessages.ALERT_HEADING,
                            MiniLoadConfirmationActivity.this, getString(R.string.yes), getString(R.string.no), new OnClickListener() {
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

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    private void fetchIntents() {
        transactionInfo = (TransactionInfoModel) mBundle.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
        mBundle.clear();
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, MiniLoadConfirmationActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(MiniLoadConfirmationActivity.this).execute(
                Constants.CMD_MINI_LOAD + "", getEncryptedMpin(), product.getId(),
                transactionInfo.getTmob(), transactionInfo.getTxam(), transactionInfo.getCamt(),
                transactionInfo.getTpam(), transactionInfo.getTamt());
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {

            AppLogger.i("XML Response: " + response.getXmlResponse() + "\n");

            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);

                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
                        MiniLoadConfirmationActivity.this, new View.OnClickListener() {
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
                    intent.putExtra(Constants.IntentKeys.FLOW_ID, flowId);
                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, product);
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

    private boolean isNull(String edittxt) {
        if (edittxt != null && !edittxt.equals("null") && !edittxt.equals(""))
            return false;

        else
            return true;
    }

    private boolean checkMutliple(String amountstring, String mutiplestring) {
        if (amountstring.equals(""))
            return true;

        Double amount = Double.parseDouble(amountstring);
        Double mutiple = Double.parseDouble(Utility
                .getUnFormattedAmount(mutiplestring));

        if (amount % mutiple == 0)
            return true;
        else
            return false;
    }

    @Override
    public void processNext() {
    }
}