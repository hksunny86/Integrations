package com.inov8.agentmate.activities.fundsTransfer;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.CitiesModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.PaymentReasonModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsbl.sco.R;

import static com.inov8.agentmate.util.Utility.testValidity;

public class FundsTransferInputActivity extends BaseCommunicationActivity {
    private TextView lblHeading, lblSubHeading, lblField1, lblField2,
            lblField3, lblField4, lblField5, lblField6, lblSpinner1;
    private Spinner inputSpinner1;
    private EditText input1, input2, input3, input4, input5, input6;
    private String strSenderCnic, strSenderMobileNumber, strReceiverCnic,
            strReceiverMobileNumber, strAccountNumber, strAmount;
    private Button btnNext;
    private ProductModel product;
    private boolean fetchPaymentReasons = false;
    private byte flowId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_funds_transfer);

        try {
            fetchIntents();

            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("Money Transfer");

            lblSubHeading = (TextView) findViewById(R.id.lblSubHeading);
            lblSubHeading.setVisibility(View.VISIBLE);
            lblSubHeading.setText("(" + product.getName() + ")");

            lblField1 = (TextView) findViewById(R.id.lblField1);
            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField3 = (TextView) findViewById(R.id.lblField3);
            lblField4 = (TextView) findViewById(R.id.lblField4);
            lblField5 = (TextView) findViewById(R.id.lblField5);
            lblField6 = (TextView) findViewById(R.id.lblField6);
            lblSpinner1 = (TextView) findViewById(R.id.lblSpinner1);
            inputSpinner1 = (Spinner) findViewById(R.id.inputSpinner1);

            if (strSenderMobileNumber != null && strSenderMobileNumber
                    .length() < Constants.MAX_LENGTH_MOBILE) {
                input2.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                return;
            }

            if (strSenderMobileNumber != null && (strSenderMobileNumber.charAt(0) != '0'
                    || strSenderMobileNumber.charAt(1) != '3')) {
                input2.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                return;
            }

            if (strReceiverMobileNumber != null && strReceiverMobileNumber
                    .length() < Constants.MAX_LENGTH_MOBILE) {
                input4.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                return;
            }

            if (strReceiverMobileNumber != null && (strReceiverMobileNumber.charAt(0) != '0'
                    || strReceiverMobileNumber.charAt(1) != '3')) {
                input4.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                return;
            }

            switch (flowId) {
                case Constants.FLOW_ID_FT_BLB_TO_BLB:
                    loadInputFields(false, true, false, true, false, false);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                    loadInputFields(false, true, true, true, false, true);
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_BLB:
                    // sender cnic, sender mobile, receiver cnic, receiver mobile,
                    // account number
                    loadInputFields(true, true, false, true, false, true);
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
                    // sender cnic, sender mobile, receiver cnic, receiver mobile,
                    // account number
                    loadInputFields(true, true, true, true, false, true);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                    // sender cnic, sender mobile, receiver cnic, receiver mobile,
                    // account number
                    loadInputFields(false, true, false, true, true, false);
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
                    // sender cnic, sender mobile, receiver cnic, receiver mobile,
                    // account number
                    loadInputFields(true, true, false, true, true, false);
                    break;
            }

            lblField6.setText("Amount");
            lblField6.setVisibility(View.VISIBLE);

            input6 = (EditText) findViewById(R.id.input6);
            input6.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    Constants.MAX_AMOUNT_LENGTH)});
            input6.setVisibility(View.VISIBLE);

            if (ApplicationData.listPaymentReasons == null) {
                fetchPaymentReasons = true;
                processRequest();
            }

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((input1 != null && !testValidity(input1))) {
                        return;
                    }

                    if (input1 != null) {
                        strSenderCnic = input1.getText().toString();
                    }

                    if ((strSenderCnic != null && strSenderCnic.length() < Constants.MAX_LENGTH_CNIC)) {
                        input1.setError(Constants.Messages.INVALID_CNIC);
                        input1.requestFocus();
                        return;
                    }
                    if (input2 != null && !testValidity(input2)) {
                        return;
                    }
                    if (input2 != null) {
                        strSenderMobileNumber = input2.getText().toString();
                    }

                    if (input2 != null && (strSenderMobileNumber.charAt(0) != '0' || strSenderMobileNumber.charAt(1) != '3')) {
                        input2.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                        return;
                    }

                    if (strSenderMobileNumber != null
                            && strSenderMobileNumber.length() < Constants.MAX_LENGTH_MOBILE) {
                        input2.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                        input2.requestFocus();
                        return;
                    }

                    if (input3 != null && !testValidity(input3)) {
                        return;
                    }
                    if (input3 != null) {
                        strReceiverCnic = input3.getText().toString();
                    }
                    if (strReceiverCnic != null
                            && strReceiverCnic.length() < Constants.MAX_LENGTH_CNIC) {
                        input3.setError(Constants.Messages.INVALID_CNIC);
                        input3.requestFocus();
                        return;
                    }

                    if (input4 != null && !testValidity(input4)) {
                        return;
                    }
                    if (input4 != null) {
                        strReceiverMobileNumber = input4.getText().toString();
                    }

                    if (input4 != null && (strReceiverMobileNumber.charAt(0) != '0' || strReceiverMobileNumber.charAt(1) != '3')) {
                        input4.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                        return;
                    }

                    if (strReceiverMobileNumber != null
                            && strReceiverMobileNumber.length() < Constants.MAX_LENGTH_MOBILE) {
                        input4.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                        input4.requestFocus();
                        return;
                    }

                    if (input5 != null && !testValidity(input5)) {
                        return;
                    }
                    if (input5 != null) {
                        strAccountNumber = input5.getText().toString();
                    }
                    if (input6 != null && !testValidity(input6)) {
                        return;
                    }
                    if (input6 != null) {
                        strAmount = input6.getText().toString();
                    }

                    int amount = Integer.parseInt(strAmount);
                    double productMaxAmount = Double.parseDouble(Utility
                            .getUnFormattedAmount(product.getMaxamt()));
                    double productMinAmount = Double.parseDouble(Utility
                            .getUnFormattedAmount(product.getMinamt()));

                    if (!Utility.isNull(product.getDoValidate())
                            && product.getDoValidate().equals("1")) {
                        if ((amount > productMaxAmount)
                                || (amount < productMinAmount)) {
                            input6.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
                            input6.requestFocus();
                            return;
                        }
                    } else if (strAmount != null && strAmount.length() > 0) {
                        if (amount < 10 || amount > Constants.MAX_AMOUNT) {
                            input6.setError(Constants.Messages.AMOUNT_MAX_LIMIT);
                            input6.requestFocus();
                            return;
                        }
                    }

                    mpinProcess(null, null);
                }
            });

            addAutoKeyboardHideFunction();
        } catch (Exception ex) {
            ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(),
                    Constants.Messages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading("Please Wait", "Processing...");

        if (fetchPaymentReasons) {
            new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                    Constants.CMD_TRANS_PURPOSE_CODE + "");
        } else {

            switch (flowId) {
                case Constants.FLOW_ID_FT_BLB_TO_BLB:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_BLB2BLB_INFO + "",
                            product.getId(), ApplicationData.agentMobileNumber,
                            strSenderMobileNumber, strReceiverMobileNumber, strAmount);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_BLB2CNIC_INFO + "",
                            product.getId(), ApplicationData.agentMobileNumber,
                            strSenderMobileNumber, strReceiverMobileNumber,
                            strReceiverCnic, strAmount);
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_BLB:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_CNIC2BLB_INFO + "",
                            product.getId(), ApplicationData.agentMobileNumber,
                            strSenderMobileNumber, strSenderCnic,
                            strReceiverMobileNumber, strAmount);
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_CNIC2CNIC_INFO + "",
                            product.getId(), ApplicationData.agentMobileNumber,
                            strSenderMobileNumber, strSenderCnic,
                            strReceiverMobileNumber, strReceiverCnic, strAmount);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_2CORE_INFO + "",
                            product.getId(), ApplicationData.agentMobileNumber,
                            strSenderMobileNumber, strAccountNumber, strAmount);
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
                    new HttpAsyncTask(FundsTransferInputActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_2CORE_INFO + "",
                            product.getId(), ApplicationData.agentMobileNumber,
                            strSenderMobileNumber, strSenderCnic, strAccountNumber,
                            strAmount);
                    break;
            }
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, false, null);
            } else if (fetchPaymentReasons) {
                ApplicationData.listPaymentReasons = (ArrayList<PaymentReasonModel>) table.get(Constants.KEY_LIST_PAYMENT_REASONS);
                ArrayList<String> paymentReasonsList = new ArrayList<>();
                for (int i = 0; i < ApplicationData.listPaymentReasons.size(); i++) {
                    paymentReasonsList.add(ApplicationData.listPaymentReasons.get(i).getName());
                }
                ArrayAdapter<String> paymentReasonsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentReasonsList);
                inputSpinner1.setAdapter(paymentReasonsAdapter);
                fetchPaymentReasons = false;
            } else {
                TransactionInfoModel transactionInfo = TransactionInfoModel.getInstance();

                switch (flowId) {
                    case Constants.FLOW_ID_FT_BLB_TO_BLB:
                        transactionInfo.FundsTransferBlb2Blb(
                                table.get(Constants.ATTR_CMOB).toString(), table
                                        .get(Constants.ATTR_RCMOB).toString(),
                                table.get(Constants.ATTR_TXAM).toString(), table
                                        .get(Constants.ATTR_TXAMF).toString(),
                                table.get(Constants.ATTR_TPAM).toString(), table
                                        .get(Constants.ATTR_TPAMF).toString(),
                                table.get(Constants.ATTR_CAMT).toString(), table
                                        .get(Constants.ATTR_CAMTF).toString(),
                                table.get(Constants.ATTR_TAMT).toString(), table
                                        .get(Constants.ATTR_TAMTF).toString(),
                                table.get(Constants.ATTR_PID).toString());
                        break;

                    case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                        transactionInfo.FundsTransferBlb2Cnic(
                                table.get(Constants.ATTR_CMOB).toString(), table
                                        .get(Constants.ATTR_RWMOB).toString(),
                                table.get(Constants.ATTR_RWCNIC).toString(), table
                                        .get(Constants.ATTR_TXAM).toString(), table
                                        .get(Constants.ATTR_TXAMF).toString(),
                                table.get(Constants.ATTR_TPAM).toString(), table
                                        .get(Constants.ATTR_TPAMF).toString(),
                                table.get(Constants.ATTR_CAMT).toString(), table
                                        .get(Constants.ATTR_CAMTF).toString(),
                                table.get(Constants.ATTR_TAMT).toString(), table
                                        .get(Constants.ATTR_TAMTF).toString(),
                                table.get(Constants.ATTR_PID).toString());
                        break;

                    case Constants.FLOW_ID_FT_CNIC_TO_BLB:
                        transactionInfo.FundsTransferCnic2Blb(
                                table.get(Constants.ATTR_SWMOB).toString(), table
                                        .get(Constants.ATTR_SWCNIC).toString(),
                                table.get(Constants.ATTR_RCMOB).toString(), table
                                        .get(Constants.ATTR_TXAM).toString(), table
                                        .get(Constants.ATTR_TXAMF).toString(),
                                table.get(Constants.ATTR_TPAM).toString(), table
                                        .get(Constants.ATTR_TPAMF).toString(),
                                table.get(Constants.ATTR_CAMT).toString(), table
                                        .get(Constants.ATTR_CAMTF).toString(),
                                table.get(Constants.ATTR_TAMT).toString(), table
                                        .get(Constants.ATTR_TAMTF).toString(),
                                table.get(Constants.ATTR_PID).toString());
                        if (table.containsKey(Constants.ATTR_RWCNIC))
                            transactionInfo.setRwcnic(table.get(Constants.ATTR_RWCNIC).toString());
                        if (table.containsKey(XmlConstants.ATTR_IS_BVS_REQ))
                            transactionInfo.setIsBvsReq(table.get(XmlConstants.ATTR_IS_BVS_REQ).toString());
                        break;

                    case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
                        transactionInfo.FundsTransferCnic2Cnic(
                                table.get(Constants.ATTR_SWMOB).toString(), table
                                        .get(Constants.ATTR_SWCNIC).toString(),
                                table.get(Constants.ATTR_RWMOB).toString(), table
                                        .get(Constants.ATTR_RWCNIC).toString(),
                                table.get(Constants.ATTR_TXAM).toString(), table
                                        .get(Constants.ATTR_TXAMF).toString(),
                                table.get(Constants.ATTR_TPAM).toString(), table
                                        .get(Constants.ATTR_TPAMF).toString(),
                                table.get(Constants.ATTR_CAMT).toString(), table
                                        .get(Constants.ATTR_CAMTF).toString(),
                                table.get(Constants.ATTR_TAMT).toString(), table
                                        .get(Constants.ATTR_TAMTF).toString(),
                                table.get(Constants.ATTR_PID).toString());
                        if (table.containsKey(XmlConstants.ATTR_IS_BVS_REQ))
                            transactionInfo.setIsBvsReq(table.get(XmlConstants.ATTR_IS_BVS_REQ).toString());

                        if (table.containsKey(XmlConstants.ATTR_SENDER_CITY))
                            transactionInfo.setSenderCity(table.get(XmlConstants.ATTR_SENDER_CITY).toString());

                        if (table != null && table.containsKey(Constants.KEY_LIST_CITIES)) {
                            ApplicationData.listCities = (ArrayList<CitiesModel>) table
                                    .get(Constants.KEY_LIST_CITIES);
                        }
                        break;

                    case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                        transactionInfo.FundsTransferBlb2Core(
                                table.get(Constants.ATTR_CMOB).toString(),
                                strReceiverMobileNumber,
                                table.get(Constants.ATTR_COREACID).toString(),
                                table.get(Constants.ATTR_COREACTL).toString(),
                                table.get(Constants.ATTR_TXAM).toString(), table
                                        .get(Constants.ATTR_TXAMF).toString(),
                                table.get(Constants.ATTR_TPAM).toString(), table
                                        .get(Constants.ATTR_TPAMF).toString(),
                                table.get(Constants.ATTR_CAMT).toString(), table
                                        .get(Constants.ATTR_CAMTF).toString(),
                                table.get(Constants.ATTR_TAMT).toString(), table
                                        .get(Constants.ATTR_TAMTF).toString(),
                                table.get(Constants.ATTR_PID).toString());
                        break;

                    case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
                        transactionInfo.FundsTransferCnic2Core(
                                table.get(Constants.ATTR_SWMOB).toString(), table
                                        .get(Constants.ATTR_SWCNIC).toString(),
                                strReceiverMobileNumber,
                                table.get(Constants.ATTR_COREACID).toString(),
                                table.get(Constants.ATTR_COREACTL).toString(),
                                table.get(Constants.ATTR_TXAM).toString(), table
                                        .get(Constants.ATTR_TXAMF).toString(),
                                table.get(Constants.ATTR_TPAM).toString(), table
                                        .get(Constants.ATTR_TPAMF).toString(),
                                table.get(Constants.ATTR_CAMT).toString(), table
                                        .get(Constants.ATTR_CAMTF).toString(),
                                table.get(Constants.ATTR_TAMT).toString(), table
                                        .get(Constants.ATTR_TAMTF).toString(),
                                table.get(Constants.ATTR_PID).toString());
                        if (table.containsKey(XmlConstants.ATTR_IS_BVS_REQ))
                            transactionInfo.setIsBvsReq(table.get(XmlConstants.ATTR_IS_BVS_REQ).toString());
                        break;
                }

                Intent intent = new Intent(FundsTransferInputActivity.this,
                        FundsTransferConfirmationActivity.class);
                intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactionInfo);
                intent.putExtra(Constants.IntentKeys.FLOW_ID, flowId);
                if (ApplicationData.listPaymentReasons != null && inputSpinner1.getSelectedItemPosition() >= 0)
                    intent.putExtra(Constants.IntentKeys.PURPOSE_CODE, ApplicationData.listPaymentReasons.get(inputSpinner1.getSelectedItemPosition()).getCode());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
        }
        hideLoading();
    }

    @Override
    public void processNext() {
    }

    private void fetchIntents() {
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
        product = (ProductModel) mBundle
                .get(Constants.IntentKeys.PRODUCT_MODEL);
    }

    private void loadInputFields(boolean field1, boolean field2,
                                 boolean field3, boolean field4, boolean field5, boolean spinner1) {
        if (field1) {
            lblField1.setText("Sender CNIC");
            lblField1.setVisibility(View.VISIBLE);

            input1 = (EditText) findViewById(R.id.input1);
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    Constants.MAX_LENGTH_CNIC)});
            input1.setVisibility(View.VISIBLE);
            disableCopyPaste(input1);
        }
        if (field2) {
            lblField2.setText("Sender Mobile Number");
            lblField2.setVisibility(View.VISIBLE);

            input2 = (EditText) findViewById(R.id.input2);
            input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    Constants.MAX_LENGTH_MOBILE)});
            input2.setVisibility(View.VISIBLE);
            disableCopyPaste(input2);
        }
        if (field3) {
            lblField3.setText("Receiver CNIC");
            lblField3.setVisibility(View.VISIBLE);

            input3 = (EditText) findViewById(R.id.input3);
            input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    Constants.MAX_LENGTH_CNIC)});
            input3.setVisibility(View.VISIBLE);
            disableCopyPaste(input3);
        }
        if (field4) {
            lblField4.setText("Receiver Mobile Number");
            lblField4.setVisibility(View.VISIBLE);

            input4 = (EditText) findViewById(R.id.input4);
            input4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    Constants.MAX_LENGTH_MOBILE)});
            input4.setVisibility(View.VISIBLE);
            disableCopyPaste(input4);
        }
        if (field5) {
            lblField5.setText("Account Number");
            lblField5.setVisibility(View.VISIBLE);

            input5 = (EditText) findViewById(R.id.input5);
            input5.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    Constants.MAX_LENGTH_ACCOUNT_NO)});
            input5.setVisibility(View.VISIBLE);
            disableCopyPaste(input5);
        }
        if (spinner1) {
            lblSpinner1.setText("Payment Purpose");
            lblSpinner1.setVisibility(View.VISIBLE);

            if (ApplicationData.listPaymentReasons != null) {
                ArrayList<String> paymentReasonsList = new ArrayList<>();
                for (int i = 0; i < ApplicationData.listPaymentReasons.size(); i++) {
                    paymentReasonsList.add(ApplicationData.listPaymentReasons.get(i).getName());
                }
                ArrayAdapter<String> paymentReasonsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentReasonsList);
                inputSpinner1.setAdapter(paymentReasonsAdapter);
            }

            inputSpinner1.setVisibility(View.VISIBLE);
        }
    }
}