package com.inov8.jsblconsumer.activities.retailPayment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class RetailPaymentInputActivity extends BaseCommunicationActivity {
    private TextView lblField1, lblField2;
    private EditText input1, input2;
    private Button btnNext;
    private ProductModel product;
    private String mobileNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_general);

        try {
            fetchIntents();

            titleImplementation(null, product.getName(), null, this);
            lblField1 = (TextView) findViewById(R.id.lblField1);
            lblField1.setText("Receiver Mobile Number");
            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField2.setText("Amount");
            lblField2.setVisibility(View.VISIBLE);
            lblField1.setVisibility(View.VISIBLE);
            input1 = (EditText) findViewById(R.id.input1);
            input1.setVisibility(View.VISIBLE);

            input2 = (EditText) findViewById(R.id.input2);
            input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            input2.setVisibility(View.VISIBLE);


            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(input1.getText())) {
                        input1.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    if (TextUtils.isEmpty(input2.getText())) {
                        input2.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }


                    if (input1.length() < 11) {
                        input1.setError(AppMessages.INVALID_LOGIN_LENGTH);
                        return;
                    }
                    if (input1.getText().charAt(0) != '0' || input1.getText().charAt(1) != '3') {
                        input1.setError(AppMessages.ERROR_MOBILE_NO_START);
                        return;
                    }
                    String str = input2.getText() + "";

                    if (!Utility.isNull(product.getDoValidate()) && product.getDoValidate().equals("1")) {
                        if ((Integer.parseInt(input2.getText().toString()) >
                                Double.parseDouble(Utility.getUnFormattedAmount(product.getMaxamt()))
                                ||
                                Integer.parseInt(input2.getText().toString()) <
                                        Double.parseDouble(Utility.getUnFormattedAmount(product.getMinamt())))) {
                            input2.setError(AppMessages.ERROR_AMOUNT_INVALID);
                            return;
                        }
                    } else {
                        if (str != null && str.length() > 0) {
                            Double amount = Double.parseDouble(input2.getText().toString() + "");
                            if (amount < 1 || amount > Constants.MAX_AMOUNT) {
                                input2.setError(AppMessages.ERROR_AMOUNT_INVALID);

                                return;
                            }
                        }
                    }

                    mobileNumber = input1.getText().toString();
                    mpinProcess(null, null);
                }
            });
            addAutoKeyboardHideFunction();
        } catch (Exception ex) {
            ex.getMessage();
        }
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
    }

    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, RetailPaymentInputActivity.this, PopupDialogs.Status.ERROR);
            return;
        }
        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(RetailPaymentInputActivity.this).execute(
                Constants.CMD_RETAIL_PAYMENT_INFO + "", product.getId(), mobileNumber, input2.getText().toString());
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                if (message.getCode().equals(Constants.ErrorCodes.WALK_IN_CUSTOMER)) {

                    PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
                            RetailPaymentInputActivity.this, PopupDialogs.Status.ERROR);

                } else {

                    PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
                            RetailPaymentInputActivity.this, PopupDialogs.Status.ERROR);
                }
            } else {
                TransactionInfoModel transactionInfo = TransactionInfoModel.getInstance();
                transactionInfo.RetailPayment(
                        table.get(XmlConstants.Attributes.AMOB).toString(),
                        table.get(XmlConstants.Attributes.TXAM).toString(),
                        table.get(XmlConstants.Attributes.TXAMF).toString(),
                        table.get(XmlConstants.Attributes.TPAM).toString(),
                        table.get(XmlConstants.Attributes.TPAMF).toString(),
                        table.get(XmlConstants.Attributes.CAMT).toString(),
                        table.get(XmlConstants.Attributes.CAMTF).toString(),
                        table.get(XmlConstants.Attributes.TAMT).toString(),
                        table.get(XmlConstants.Attributes.TAMTF).toString(),
                        table.get(XmlConstants.Attributes.RANAME).toString());

                Intent intent = new Intent(getApplicationContext(), RetailPaymentConfirmationActivity.class);
                intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactionInfo);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
        hideLoading();
    }

    private void fetchIntents() {
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        mobileNumber = (String) mBundle.get(Constants.IntentKeys.AMOB);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void processNext() {
    }
}