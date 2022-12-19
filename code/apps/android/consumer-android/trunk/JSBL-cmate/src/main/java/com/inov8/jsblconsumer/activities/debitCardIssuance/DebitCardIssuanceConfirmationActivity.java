//package com.inov8.timepeyconsumer.activities.debitCardIssuance;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.InputFilter;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.andreabaccega.widget.FormEditText;
//import com.inov8.timepeyconsumer.activities.BaseCommunicationActivity;
//import com.inov8.timepeyconsumer.activities.TransactionReceiptActivity;
//import com.inov8.timepeyconsumer.model.HttpResponseModel;
//import com.inov8.timepeyconsumer.model.MessageModel;
//import com.inov8.timepeyconsumer.model.ProductModel;
//import com.inov8.timepeyconsumer.model.TransactionInfoModel;
//import com.inov8.timepeyconsumer.model.TransactionModel;
//import com.inov8.timepeyconsumer.net.HttpAsyncTask;
//import com.inov8.timepeyconsumer.parser.XmlParser;
//import com.inov8.timepeyconsumer.ui.components.ListViewExpanded;
//import com.inov8.timepeyconsumer.ui.components.PopupDialogs;
//import com.inov8.timepeyconsumer.util.AppMessages;
//import com.inov8.timepeyconsumer.util.Constants;
//import com.inov8.timepeyconsumer.util.Utility;
//import com.inov8.timepeyconsumer.mfs.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.List;
//
//public class DebitCardIssuanceConfirmationActivity extends
//        BaseCommunicationActivity {
//    private TransactionInfoModel transactionInfo;
//    private ProductModel mProduct;
//    private TextView lblHeading, lblAlert, lblField1;
//    private FormEditText inputField1;
//    private Button btnNext;
//    private String productId, cardCategoryId, cardCategoryName, cardTypeId,
//            cardTypeName, cardRankId, cardRankName, applicantTypeName,
//            applicantTypeId, mobileNumber, accountTitle, branchCode,
//            branchName, cardNumber, amount, amountf, commission, charges,
//            chargesf, totalAmount, totalAmountf, otp, trxId;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        super.setContentView(R.layout.activity_confirmation);
//
//        try {
//            fetchIntents();
//
//            lblHeading = (TextView) findViewById(R.id.lblHeading);
//            lblHeading.setText("Confirm Issuance");
//
//            lblAlert = (TextView) findViewById(R.id.lblAlert);
//            lblAlert.setText("");
//
//            productId = mProduct.getId();
//            cardCategoryId = transactionInfo.getCid();
//            cardCategoryName = transactionInfo.getCname();
//            cardTypeId = transactionInfo.getCtid();
//            cardTypeName = transactionInfo.getCtname();
//            cardRankId = transactionInfo.getCrid();
//            cardRankName = transactionInfo.getCrname();
//            applicantTypeId = transactionInfo.getCatid();
//            applicantTypeName = transactionInfo.getCatname();
//            mobileNumber = transactionInfo.getMobn();
//            accountTitle = transactionInfo.getActitle();
//            branchCode = transactionInfo.getBcode();
//            branchName = transactionInfo.getBname();
//            cardNumber = transactionInfo.getCnumber();
//            amount = transactionInfo.getTxam();
//            amountf = transactionInfo.getTxamf();
//            commission = transactionInfo.getCamt();
//            charges = transactionInfo.getTpam();
//            chargesf = transactionInfo.getTpamf();
//            totalAmount = transactionInfo.getTamt();
//            totalAmountf = transactionInfo.getTamtf();
//            trxId = transactionInfo.getTrxid();
//
//            lblField1 = (TextView) findViewById(R.id.lblField1);
//            inputField1 = (FormEditText) findViewById(R.id.input1);
//
//            String labels[] = new String[11];
//            String data[] = new String[11];
//
//            switch (Integer.parseInt(cardCategoryId)) {
//                case Constants.DEBIT_CARD_CATEGORY_NONPERSONALIZED:
//                    lblField1.setVisibility(View.VISIBLE);
//                    lblField1.setText("One Time PIN");
//                    inputField1.setVisibility(View.VISIBLE);
//                    inputField1
//                            .setFilters(new InputFilter[]{new InputFilter.LengthFilter(
//                                    4)});
//                    labels = new String[]{"Category", "Type", "Applicant Type",
//                            "Mobile No.", "Account Title", "Card No.",
//                            "Amount", "Charges", "Total Amount"};
//                    data = new String[]{cardCategoryName, cardTypeName,
//                            applicantTypeName, mobileNumber, accountTitle,
//                            cardNumber, amountf + " " + Constants.CURRENCY,
//                            chargesf + " " + Constants.CURRENCY,
//                            totalAmountf + " " + Constants.CURRENCY};
//                    break;
//                case Constants.DEBIT_CARD_CATEGORY_PERSONALIZED:
//                    lblField1.setVisibility(View.GONE);
//                    inputField1.setVisibility(View.GONE);
//                    labels = new String[]{"Category", "Type", "Applicant Type",
//                            "Mobile No.", "Account Title", "Branch", "Amount",
//                            "Charges", "Total Amount"};
//                    data = new String[]{cardCategoryName, cardTypeName,
//                            applicantTypeName, mobileNumber, accountTitle,
//                            branchName, amountf + " " + Constants.CURRENCY,
//                            chargesf + " " + Constants.CURRENCY,
//                            totalAmountf + " " + Constants.CURRENCY};
//                    break;
//            }
//
//            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
//
//            for (int i = 0; i < data.length; i++) {
//                HashMap<String, String> hm = new HashMap<String, String>();
//                hm.put("label", labels[i]);
//                hm.put("data", data[i]);
//                aList.add(hm);
//            }
//
//            String[] from = {"label", "data"};
//            int[] to = {R.id.txtLabel, R.id.txtData};
//            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
//                    R.layout.list_item_with_data, from, to);
//
//            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);
//            listView.setAdapter(adapter);
//
//            Utility.getListViewSize(listView, this, mListItemHieght);
//
//            btnNext = (Button) findViewById(R.id.btnNext);
//            btnNext.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    if ((inputField1.getVisibility() == View.VISIBLE)
//                            && (!inputField1.testValidity())) {
//                        Toast.makeText(
//                                DebitCardIssuanceConfirmationActivity.this,
//                                "Please fill all the required fields.",
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    otp = inputField1.getText().toString();
//                    mBundle.putString("AMOUNT", totalAmount);
//                    mBundle.putByte("FLOW_ID",
//                            Constants.FLOW_ID_DEBIT_CARD_ISSUANCE);
//                    askMpin(mBundle, TransactionReceiptActivity.class, false);
//                }
//            });
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//        headerImplementation();
//    }
//
//    private void fetchIntents() {
//        transactionInfo = (TransactionInfoModel) mBundle
//                .get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
//        mProduct = (ProductModel) mBundle
//                .get(Constants.IntentKeys.PRODUCT_MODEL);
//    }
//
//    @Override
//    public void onBackPressed() {
//        dialogGeneral = PopupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION,
//                getString(R.string.alertNotification),
//                DebitCardIssuanceConfirmationActivity.this, new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogGeneral.cancel();
//                        goToMainMenu();
//                    }
//                });
//    }
//
//    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
//        processRequest();
//    }
//
//    @Override
//    public void processRequest() {
//        if (!haveInternet()) {
//            Toast.makeText(getApplication(),
//                    AppMessages.INTERNET_CONNECTION_PROBLEM,
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            showLoading("Please Wait", "Processing...");
//
//            new HttpAsyncTask(DebitCardIssuanceConfirmationActivity.this)
//                    .execute(Constants.CMD_DEBIT_CARD_ISSUANCE + "",
//                            getEncryptedMpin(), productId, cardCategoryId,
//                            cardTypeId, cardRankId, applicantTypeId,
//                            mobileNumber, cardNumber, otp, branchCode, amount,
//                            commission, charges, totalAmount, trxId);
//        } catch (Exception e) {
//            hideLoading();
//            PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE,
//                    getString(R.string.alertNotification), DebitCardIssuanceConfirmationActivity.this, PopupDialogs.Status.ERROR);
//
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void processResponse(HttpResponseModel response) {
//        try {
//            XmlParser xmlParser = new XmlParser();
//            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
//                    .getXmlResponse());
//            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
//                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
//                MessageModel message = (MessageModel) list.get(0);
//
//                if (message.getCode().equals(Constants.ErrorCodes.OTP_EXPIRE)) {
//                    PopupDialogs.createAlertDialog(message.getDescr(),
//                            getString(R.string.alertNotification), DebitCardIssuanceConfirmationActivity.this, PopupDialogs.Status.ERROR);
//
//                } else {
//
//                    PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                            DebitCardIssuanceConfirmationActivity.this, PopupDialogs.Status.ERROR);
//                }
//            } else {
//                if (table.containsKey(Constants.KEY_LIST_TRANS)) {
//                    @SuppressWarnings("unchecked")
//                    List<TransactionModel> list = (List<TransactionModel>) table
//                            .get(Constants.KEY_LIST_TRANS);
//                    TransactionModel transaction = list.get(0);
//
//                    Intent intent = new Intent(getApplicationContext(),
//                            TransactionReceiptActivity.class);
//                    intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL,
//                            transaction);
//                    intent.putExtras(mBundle);
//
//                    startActivity(intent);
//                }
//            }
//            hideLoading();
//        } catch (Exception e) {
//            hideLoading();
//            PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE,
//                    getString(R.string.alertNotification), DebitCardIssuanceConfirmationActivity.this, PopupDialogs.Status.ERROR);
//
//            e.printStackTrace();
//        }
//        hideLoading();
//    }
//
//    @Override
//    public void processNext() {
//    }
//}