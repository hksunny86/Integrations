package com.inov8.jsblconsumer.activities.debitCardActivation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class DebitCardActivationConfirmationActivity extends BaseCommunicationActivity {
    private ProductModel product;
    private TextView lblAlert;
    private Button btnNext, btnCancel;
    private String cnicNumber, cardProgram, mobileNumber, accountTitle, cardNumber, transactionId;
    private int activationType;
    private boolean isListFetched;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.setContentView(R.layout.activity_confirmation);

        try {
            fetchIntents();

            titleImplementation(null, product.getName(), null, this);

            lblAlert = (TextView) findViewById(R.id.lblAlert);
            lblAlert.setText("Please verify details.");

            processRequest();

            btnNext = (Button) findViewById(R.id.btnNext);
            btnCancel = (Button) findViewById(R.id.btnCancel);

            btnCancel.setVisibility(View.GONE);
            btnCancel.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    dialogGeneral = PopupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION, getString(R.string.alertNotification), DebitCardActivationConfirmationActivity.this, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.cancel();
                            goToMainMenu();
                        }
                    });
                }
            });
            btnNext.setVisibility(View.GONE);
            btnNext.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (isListFetched) {
                        mBundle.putByte("FLOW_ID", Byte.parseByte(product.getFlowId()));
                        askMpin(mBundle, TransactionReceiptActivity.class, false);
                    }
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
        headerImplementation();
    }

    private void fetchIntents() {
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        activationType = mBundle.getInt(Constants.IntentKeys.DEBIT_CARD_ACTIVATION_TYPE);
    }

    @Override
    public void onBackPressed() {
        dialogGeneral = PopupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION,
                getString(R.string.alertNotification),
                DebitCardActivationConfirmationActivity.this, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();
                        goToMainMenu();
                    }
                });
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,AppMessages.ALERT_HEADING,
                    DebitCardActivationConfirmationActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.cancel();
                            finish();

                        }
                    }, PopupDialogs.Status.ERROR);

            return;
        }
        try {
            showLoading("Please Wait", "Processing...");

            if (isListFetched) {
                new HttpAsyncTask(DebitCardActivationConfirmationActivity.this)
                        .execute(Constants.CMD_DEBIT_CARD_ACTIVATION + "",
                                getEncryptedMpin(), mobileNumber,
                                transactionId, activationType + "");
            } else {
                new HttpAsyncTask(DebitCardActivationConfirmationActivity.this)
                        .execute(Constants.CMD_DEBIT_CARD_ACTIVATION_INFO + "",
                                ApplicationData.customerMobileNumber,
                                activationType + "");
            }

        } catch (Exception e) {
            hideLoading();

            PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE,
                    getString(R.string.alertNotification), DebitCardActivationConfirmationActivity.this, PopupDialogs.Status.ERROR);

            e.printStackTrace();
        }
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
                        DebitCardActivationConfirmationActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                goToMainMenu();

                            }
                        }, PopupDialogs.Status.ERROR);
            } else {
                if (isListFetched) {
                    TransactionModel transaction = new TransactionModel();
                    transaction.setDebitCardActivation(
                            table.get(XmlConstants.Attributes.MOBN).toString(),
                            table.get(XmlConstants.Attributes.CNIC).toString(),
                            table.get(XmlConstants.Attributes.ACTITLE).toString(),
                            table.get(XmlConstants.Attributes.CARDNO).toString(),
                            table.get(XmlConstants.Attributes.CPNAME).toString(),
                            table.get(XmlConstants.Attributes.DATEF).toString());

                    Intent intent = new Intent(getApplicationContext(), TransactionReceiptActivity.class);
                    intent.putExtras(mBundle);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL, transaction);
                    startActivity(intent);
                } else {
                    mobileNumber = table.get(XmlConstants.Attributes.MOBN).toString();
                    cnicNumber = table.get(XmlConstants.Attributes.CNIC).toString();
                    accountTitle = table.get(XmlConstants.Attributes.ACTITLE).toString();
                    cardNumber = table.get(XmlConstants.Attributes.CARDNO).toString();
                    cardProgram = table.get(XmlConstants.Attributes.CPNAME).toString();;

                    String labels[] = new String[5];
                    String data[] = new String[5];

                    labels = new String[]{"Mobile No.", "CNIC No.", "Name", "Card No.", "Card Program"};
                    data = new String[]{mobileNumber, cnicNumber, accountTitle, cardNumber, cardProgram};

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
                    isListFetched = true;
                    btnCancel.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }
            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            Dialog d = PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE,
                    getString(R.string.alertNotification), DebitCardActivationConfirmationActivity.this, PopupDialogs.Status.ERROR);
            d.findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToMainMenu();
                }
            });
            e.printStackTrace();
        }
        hideLoading();
    }

    @Override
    public void processNext() {
    }
}