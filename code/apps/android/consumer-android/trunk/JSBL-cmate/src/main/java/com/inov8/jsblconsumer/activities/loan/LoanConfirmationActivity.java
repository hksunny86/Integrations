package com.inov8.jsblconsumer.activities.loan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.TransactionReceiptActivity;
import com.inov8.jsblconsumer.model.AdvanceLoanModel;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class LoanConfirmationActivity extends BaseCommunicationActivity {

    private TextView lblAlert;
    private Button btnNext, btnCancel;
    private String labels[], data[];
    private ProductModel product;
    private AdvanceLoanModel loanModel;
    private byte flowId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_confirmation);

        fetchIntents();
        titleImplementation(null, product.getName(), "Confirm Transaction", this);

        bottomBarImplementation(LoanConfirmationActivity.this, "");
        checkSoftKeyboardD();

        lblAlert = (TextView) findViewById(R.id.lblAlert);
        lblAlert.setText("Please verify details.");

        labels = new String[]{"CNIC","Mobile No.",
                "Amount", "Charges", "Total Amount"};
        data = new String[]{loanModel.getCnic(),loanModel.getCmob(), Constants.CURRENCY + " " + loanModel.getTxamF(),
                Constants.CURRENCY + " " + loanModel.getcAmtF(),
                Constants.CURRENCY + " " + loanModel.gettAmtF()};
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
                askMpin(mBundle, TransactionReceiptActivity.class, false);
            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION, AppMessages.ALERT_HEADING,
                LoanConfirmationActivity.this, getString(R.string.yes), getString(R.string.no), v12 -> {

                    dialogGeneral.dismiss();
                    goToMainMenu();
                }, v1 -> dialogGeneral.dismiss(), false, PopupDialogs.Status.ALERT, false));
        addAutoKeyboardHideFunction();
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
    }

    private void fetchIntents() {
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        loanModel = (AdvanceLoanModel) mBundle.get(Constants.IntentKeys.ADVANCE_LOAN_MODEL);
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
                    LoanConfirmationActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
                new HttpAsyncTask(LoanConfirmationActivity.this).execute(
                        Constants.CMD_ADVANCE_LOAN + "",
                        getEncryptedMpin(),product.getId(),loanModel.getCnic(),loanModel.getCmob(),loanModel.getTxam(),
                        loanModel.getcAmt(),loanModel.getTpam(),loanModel.gettAmt(), loanModel.getThirdPartyRRN());
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
                        LoanConfirmationActivity.this, v -> {
                            dialogGeneral.cancel();
                            if (message != null
                                    && message.getCode() != null
                                    && message.getCode().equals(
                                    Constants.ErrorCodes.INTERNAL)) {
                                goToMainMenu();
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
    public void processNext() { }
}