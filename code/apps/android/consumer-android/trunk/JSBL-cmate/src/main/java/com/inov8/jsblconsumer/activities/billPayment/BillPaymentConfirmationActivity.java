package com.inov8.jsblconsumer.activities.billPayment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class BillPaymentConfirmationActivity extends BaseCommunicationActivity {
    private Button btnNext, btnCancel;
    private TextView lblAmount, lblAlert, lblPartialPay;
    private EditText inputAmount;
    private String productName, consumerNo, dueDate, lateAmountFormatted, totalAmountFormatted, totalAmount, billStatus,
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
            bottomBarImplementation(BillPaymentConfirmationActivity.this, "");
            checkSoftKeyboardD();
            titleImplementation(null, product.getName(), "Confirm Transaction", this);

//            if (billType.equals(XmlConstants.Tags.BILLTYPE_POSTPAID)) {
            if (product.getPpRequired().equals("1")) {
                lblAmount = (TextView) findViewById(R.id.lblField);
                lblAmount.setVisibility(View.VISIBLE);
                inputAmount = (EditText) findViewById(R.id.input);
                disableCopyPaste(inputAmount);
                inputAmount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});
                inputAmount.setVisibility(View.VISIBLE);
                lblPartialPay = (TextView) findViewById(R.id.lblPartialPay);
                lblPartialPay.setText("Enter an amount of PKR " + product.getMinamtf() + " to PKR " + product.getMaxamtf());
                lblPartialPay.setVisibility(View.VISIBLE);
            }

            productName = transactionInfo.getPname();
            consumerNo = transactionInfo.getConsumer();
            mobileNumber = transactionInfo.getCmob();
            dueDate = transactionInfo.getDuedate();
            totalAmount = transactionInfo.getBamt();
            totalAmountFormatted = transactionInfo.getBamtf();
            lateAmountFormatted = transactionInfo.getLbamtf();
            billStatus = "Unpaid";

            lblAlert = (TextView) findViewById(R.id.lblAlert);
            lblAlert.setText("Please verify details.");

            String labels[], data[];

            if (product.getInRequired()!=null&&product.getInRequired().equals("0")) {
                labels = new String[]{"Mobile No.", "Topup Amount","Charges"};
                data = new String[]{consumerNo,
                        Constants.CURRENCY + " " + Utility.getFormatedAmount(totalAmount),Constants.CURRENCY + " "+transactionInfo.getTpamf()};
            } else if (product.getDenomFlag().equals("1")) {
                labels = new String[]{"Mobile No.", "Topup Amount","Charges"};
                data = new String[]{consumerNo,
                        Constants.CURRENCY + " " + Utility.getFormatedAmount(transactionInfo.getBamtf()),Constants.CURRENCY + " "+transactionInfo.getTpamf()};
            } else {
                    labels = new String[]{"Billing Company", product.getLabel(),
                            "Due Date", "Bill Amount", "Late Bill Amount","Charges",
                            "Bill Paid Status", "Customer Mobile No."};
                    data = new String[]{productName, consumerNo, dueDate,
                            Constants.CURRENCY + " " + totalAmountFormatted,
                            Constants.CURRENCY + " " + lateAmountFormatted,Constants.CURRENCY + " "+ transactionInfo.getTpamf(),
                            billStatus, mobileNumber};
                }
//            if (product.getFlowId().equals(Constants.FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID + "")) {
//                labels = new String[]{"Mobile No.", "Topup Amount"};
//                data = new String[]{consumerNo,
//                        Constants.CURRENCY + " " + Utility.getFormatedAmount(totalAmount)};
//            } else if (product.getFlowId().equals(Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID + "")) {
//                labels = new String[]{"Billing Company", "Mobile No.",
//                        "Due Date", "Bill Amount", "Late Bill Amount",
//                        "Bill Paid Status", "Customer Mobile No."};
//                data = new String[]{productName, consumerNo, dueDate,
//                        Constants.CURRENCY + " " + totalAmountFormatted,
//                        Constants.CURRENCY + " " + lateAmountFormatted,
//                        billStatus, mobileNumber};
//            } else {
//                labels = new String[]{"Billing Company", "Consumer No.",
//                        "Due Date", "Bill Amount", "Late Bill Amount",
//                        "Bill Paid Status", "Customer Mobile No."};
//                data = new String[]{productName, consumerNo, dueDate,
//                        Constants.CURRENCY + " " + totalAmountFormatted,
//                        Constants.CURRENCY + " " + lateAmountFormatted,
//                        billStatus, mobileNumber};
//            }
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

                    if (inputAmount != null) {
                        if (inputAmount.getText().toString().equals("")) {
                            amount = totalAmount;
                        } else {
                            if (!Utility.isNull(product.getDoValidate())
                                    && product.getDoValidate().equals("1")
                                    && (Integer.parseInt(inputAmount.getText().toString())
                                    > Double.parseDouble(Utility.getUnFormattedAmount(product.getMaxamt()))
                                    || Integer.parseInt(inputAmount.getText().toString())
                                    < Double.parseDouble(Utility.getUnFormattedAmount(product.getMinamt())))) {
                                inputAmount.setError(AppMessages.ERROR_AMOUNT_INVALID);
                                return;
                            }
                            if (Integer.parseInt(inputAmount.getText().toString()) <= 0) {
                                inputAmount.setError(AppMessages.ERROR_AMOUNT_INVALID);
                                return;
                            }
                            if (!isNull(product.getMultiple())) {
                                if (!checkMutliple(inputAmount.getText().toString(), product.getMultiple())) {
                                    inputAmount.setError(AppMessages.ERROR_MULTIPLE);
                                    return;
                                }
                            }
                            amount = inputAmount.getText().toString();
                        }
                    }
                    mBundle.putString(Constants.IntentKeys.TRANSACTION_AMOUNT, amount);
                    askMpin(mBundle, TransactionReceiptActivity.class, false);
                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {


                    dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION, AppMessages.ALERT_HEADING,
                            BillPaymentConfirmationActivity.this, getString(R.string.yes), getString(R.string.no), new OnClickListener() {
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
        paymentType = mBundle.getByte(Constants.IntentKeys.PAYMENT_TYPE);
        transactionInfo = (TransactionInfoModel) mBundle.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
        amount = mBundle.getString(Constants.IntentKeys.TRANSACTION_AMOUNT);
//        billType = mBundle.getString(XmlConstants.Tags.BILLTYPE);
//        if (!billType.equals(XmlConstants.Tags.BILLTYPE)) {
//            amount = mBundle.getString(Constants.IntentKeys.TRANSACTION_AMOUNT);
//        }
        mBundle.clear();
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {

        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    BillPaymentConfirmationActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(BillPaymentConfirmationActivity.this).execute(
                Constants.CMD_BILL_PAYMENT + "", getEncryptedMpin(),
                product.getId(), mobileNumber,
                consumerNo, paymentType + "",
                ApplicationData.bankId, amount,
                ApplicationData.customerMobileNumber);
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {

            AppLogger.i("XML Response: " + response.getXmlResponse() + "\n");

            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
                    .getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);


                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        BillPaymentConfirmationActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                }
                                else  if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                    askMpin(mBundle, TransactionReceiptActivity.class, true);
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);


//

//                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        BillPaymentConfirmationActivity.this, new View.OnClickListener() {
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
//                            }
//                        }, PopupDialogs.Status.ERROR);
            } else {
                if (table.containsKey(Constants.KEY_LIST_TRANS)) {
                    @SuppressWarnings("unchecked")
                    List<TransactionModel> list = (List<TransactionModel>) table.get(Constants.KEY_LIST_TRANS);
                    TransactionModel transaction = list.get(0);

                    Intent intent = new Intent(getApplicationContext(), TransactionReceiptActivity.class);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL, transaction);
//                    intent.putExtra(Constants.IntentKeys.PAYMENT_TYPE, paymentType);
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