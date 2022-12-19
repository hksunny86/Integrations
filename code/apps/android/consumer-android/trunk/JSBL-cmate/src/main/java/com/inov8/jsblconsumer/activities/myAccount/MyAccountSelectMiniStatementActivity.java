package com.inov8.jsblconsumer.activities.myAccount;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.TransactionReceiptActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class MyAccountSelectMiniStatementActivity extends
        BaseCommunicationActivity {
    private TextView lblHeading;
    private ProductModel product;
    private String AcctType;
    private List<TransactionModel> transactions = new ArrayList<TransactionModel>();
    private TextView lblSubheading;
    private Button btnNext;

    private String isHRA="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_mini_statement);

        try {
            fetchIntents();
            headerImplementation();
            bottomBarImplementation(MyAccountSelectMiniStatementActivity.this, "");
            checkSoftKeyboardD();
            titleImplementation(null, "Mini Statement", null, this);

            if (product.getId().equals(Constants.PRODUCT_ID_BLB_MINI_STATEMENT)) {
                AcctType = "1";
            } else if (product.getId().equals(Constants.PRODUCT_ID_HRA_MINI_STATEMENT))
            {
                isHRA="1";
//                AcctType = "0";
            }

            askMpin(mBundle, TransactionReceiptActivity.class, true);

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    goToMainMenu();
                }
            });

            // loadBalance();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void loadData() {

        int size = transactions.size();

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < size; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txtLabel", transactions.get(i).getDatef());
            hm.put("txtdes", transactions.get(i).descr);
            hm.put("txtData", Constants.CURRENCY + " " + transactions.get(i).getTamtf());
            aList.add(hm);
        }

        final String[] from = {"txtLabel", "txtdes", "txtData"};
        final int[] to = {R.id.txtLabel, R.id.txtdes, R.id.txtData};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
                R.layout.list_item_mini_statement, from, to);

        ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.optionsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                }
            }
        });
    }

    private void fetchIntents() {
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    MyAccountSelectMiniStatementActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        try {
            showLoading("Please Wait", "Processing...");
            new HttpAsyncTask(MyAccountSelectMiniStatementActivity.this)
                    .execute(Constants.CMD_MINI_STATMENT + "",
                            getEncryptedMpin(), AcctType,
                            ApplicationData.userId, ApplicationData.accountId,isHRA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
                    .getXmlResponse());
            if (table.containsKey("errs")) {
                List<?> list = (List<?>) table.get("errs");
                final MessageModel message = (MessageModel) list.get(0);
                loadingDialog.dismiss();


                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        MyAccountSelectMiniStatementActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                    askMpin(mBundle, TransactionReceiptActivity.class, true);
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);
//
//                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        MyAccountSelectMiniStatementActivity.this, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGeneral.cancel();
//                                if (message != null
//                                        && message.getCode() != null
//                                        && message.getCode().equals(
//                                        Constants.ErrorCodes.INTERNAL)) {
//                                    goToMainMenu();
//                                }
//                                else  if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
//                                    askMpin(mBundle, TransactionReceiptActivity.class, true);
//                                }
//
////                                finish();
//
//                            }
//                        }, PopupDialogs.Status.ERROR);

            } else if (table.containsKey("msgs")) {
                List<?> list = (List<?>) table.get("msgs");
                final MessageModel message = (MessageModel) list.get(0);
                loadingDialog.dismiss();

                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        MyAccountSelectMiniStatementActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                }

                                finish();
                            }
                        }, false, PopupDialogs.Status.INFO, false, null);

//                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        MyAccountSelectMiniStatementActivity.this, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGeneral.cancel();
//                                if (message != null
//                                        && message.getCode() != null
//                                        && message.getCode().equals(
//                                        Constants.ErrorCodes.INTERNAL)) {
//                                    goToMainMenu();
//                                }
//
//                                finish();
//
//                            }
//                        }, PopupDialogs.Status.INFO);
            } else {
                if (table.get(Constants.KEY_LIST_TRANS) != null) {
                    List<?> vec = (List<?>) table.get(Constants.KEY_LIST_TRANS);
                    for (Object object : vec) {
                        TransactionModel transaction = (TransactionModel) object;
                        transactions.add(transaction);
                        btnNext.setVisibility(View.VISIBLE);
                    }
                    loadData();
                } else {
                    // alert message
                }
            }
            hideLoading();

        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
    }

    @Override
    public void processNext() {
    }
}