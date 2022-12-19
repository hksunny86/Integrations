package com.inov8.agentmate.activities.exciseAndTaxation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.TransactionReceiptActivity;
import com.inov8.agentmate.activities.fundsTransfer.ReceiveCashConfirmationActivity;
import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.agentmate.model.ExciseAndTaxationModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class ExciseAndTaxationConfirmationActivity extends BaseCommunicationActivity {
    private TextView lblHeading, lblAlert;
    private String stRegNo, strChassisNo, strVehMaker, strEngCapacity, strStatus, strOwnerName,
            strTaxPaidUpto, strTotalAmount, strCustomerCNIC, strMobileNo, strTotalTaxAmount, strCharges;
    private Button btnNext, btnCancel;
    private String labels[], data[];
    private ExciseAndTaxationModel transactionInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_confirm_funds_transfer);

        fetchIntents();

        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText("Confirm Transaction");

        lblAlert = (TextView) findViewById(R.id.lblAlert);
        lblAlert.setText(Constants.Messages.VERIFY_CUSTOMER_DETAILS);

        try {
            stRegNo = transactionInfo.getVehRegNo();
            strChassisNo = transactionInfo.getVehChassisNo();
            strVehMaker = transactionInfo.getMakerMake();
            strEngCapacity = transactionInfo.getVehEngCapacity();
            strStatus = transactionInfo.getVehFileerStatus();
            strOwnerName = transactionInfo.getVehOwnerName();
            strTaxPaidUpto = transactionInfo.getTaxPaidUpto();
            strTotalTaxAmount = transactionInfo.getVehTaxAmountF();
            strCharges = transactionInfo.getChargesF();
            strTotalAmount = transactionInfo.getTotalAmountf();

            labels = new String[]{"Registration No.", "Chassis No.", "Vehicle Maker",
                    "Engine Capacity", "Owner Name", "Filer Status", "Tax Paid Upto", "Tax Amount", "Charges", "Total Amount"};

            data = new String[]{stRegNo, strChassisNo, strVehMaker, strEngCapacity, strOwnerName,
                    strStatus, strTaxPaidUpto, strTotalTaxAmount, strCharges, strTotalAmount};


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
                    R.layout.listview_layout_with_data, from, to);

            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);
            listView.setAdapter(adapter);

            Utility.getListViewSize(listView, this, mListItemHieght);

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ExciseAndTaxationConfirmationActivity.this, OpenAccountBvsActivity.class);
                    intent.putExtra(Constants.IntentKeys.RCNIC, strCustomerCNIC);
                    intent.putExtra(Constants.IntentKeys.RCMOB, strMobileNo);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT, transactionInfo.getTotalAmount());
                    intent.putExtra(Constants.IntentKeys.IS_EXCISE_AND_TAXATION, true);
                    intent.putExtra(Constants.IntentKeys.EXCISE_AND_TAXATION_INFO_MODEL, transactionInfo);
                    intent.putExtras(mBundle);
                    startActivity(intent);

                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Utility.showConfirmationDialog(
                            ExciseAndTaxationConfirmationActivity.this,
                            AppMessages.CANCEL_TRANSACTION,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    goToMainMenu();
                                }
                            });
                }
            });
            addAutoKeyboardHideFunction();
            headerImplementation();
            addAutoKeyboardHideFunctionScrolling();
        } catch (Exception e) {
            createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
            e.printStackTrace();
        }
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

    @Override
    public void headerImplementation() {
        btnHome = (ImageView) findViewById(R.id.imageViewHome);
        btnExit = (Button) findViewById(R.id.buttonsignout);

        if (!ApplicationData.isLogin) {
            btnExit.setVisibility(View.GONE);
        }
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                ApplicationData.webUrl = null;
                finish();
            }
        });
        btnExit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showConfirmExitDialog(null);
            }
        });
    }

    private void fetchIntents() {
        try {
            transactionInfo = (ExciseAndTaxationModel) mBundle.get(Constants.IntentKeys.EXCISE_AND_TAXATION_INFO_MODEL);
            strCustomerCNIC = getIntent().getStringExtra(Constants.IntentKeys.RCNIC);
            strMobileNo = getIntent().getStringExtra(Constants.IntentKeys.RCMOB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}