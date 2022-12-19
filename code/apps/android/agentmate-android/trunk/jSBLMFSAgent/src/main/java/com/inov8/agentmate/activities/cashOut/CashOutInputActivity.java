package com.inov8.agentmate.activities.cashOut;

import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.hra.HraRegistrationActivity1;
import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

import static com.inov8.agentmate.util.Utility.testValidity;

public class CashOutInputActivity extends BaseCommunicationActivity {
    private TextView lblField1, lblField2, lblField3, lblHeading;
    private EditText input1, input2, input3;
    private Button btnNext;
    private ProductModel product;
    private String cashOutType;
    private String isHRA = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_general);

        try {
            fetchIntents();

            lblHeading = (TextView) findViewById(R.id.lblHeading);

            if (product.getId().equals(Constants.PRODUCT_ID_CASH_WITH_DRAWAL)) {
                lblHeading.setText("HRA Cash withdrawal");
            } else {
                lblHeading.setText("Cash Out");
            }

            lblField1 = (TextView) findViewById(R.id.lblField1);
            lblField1.setVisibility(View.VISIBLE);
            input1 = (EditText) findViewById(R.id.input1);
            input1.setVisibility(View.VISIBLE);
            disableCopyPaste(input1);

            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField3 = (TextView) findViewById(R.id.lblField3);

            lblField2.setVisibility(View.VISIBLE);

            input3 = (EditText) findViewById(R.id.input3);


            input2 = (EditText) findViewById(R.id.input2);
            input2.setVisibility(View.VISIBLE);
            disableCopyPaste(input2);

            lblField2.setText("Amount");
            input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});

            if (cashOutType.equals(Constants.CASH_OUT_BY_IVR)) {
                lblField1.setText("Customer Mobile Number");
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_MOBILE)});

                if (product.getId().equals(Constants.PRODUCT_ID_CASH_WITH_DRAWAL)) {
                    lblField3.setText("CNIC");
                    lblField3.setVisibility(View.VISIBLE);

                    input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CNIC)});
                    input3.setVisibility(View.VISIBLE);
                    disableCopyPaste(input3);

                }

            } else {
                lblField1.setText("Transaction ID");
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_TRANSACTION_ID)});
            }

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!testValidity(input1))
                        return;

                    if (cashOutType.equals(Constants.CASH_OUT_BY_IVR)) {
                        if (input1.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
                            input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                            return;
                        }

                        if (input1 != null && (input1.getText().toString().charAt(0) != '0'
                                || input1.getText().toString().charAt(1) != '3')) {
                            input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                            return;
                        }

                        if (input3.getVisibility() == View.VISIBLE) {
                            if (!testValidity(input3))
                                return;
                            else if (input3.getText().toString().length() < Constants.MAX_LENGTH_CNIC) {
                                input3.setError(Constants.Messages.INVALID_CNIC);
                                return;
                            }
                        }

                    } else {
                        if (input1.getText().toString().length() < Constants.MAX_LENGTH_TRANSACTION_ID) {
                            input1.setError(Constants.Messages.INVALID_TRANSACTION_ID);
                            return;
                        }
                    }

                    if (!testValidity(input2))
                        return;

                    String str = input2.getText() + "";

                    if (!Utility.isNull(product.getDoValidate())
                            && product.getDoValidate().equals("1")) {
                        if (product.getMaxamt() != null && !product.getMaxamt().equals("")
                                && product.getMinamt() != null && !product.getMinamt().equals("")) {
                            if ((Integer.parseInt(input2.getText().toString()) > Double
                                    .parseDouble(Utility
                                            .getUnFormattedAmount(product
                                                    .getMaxamt())) || Integer
                                    .parseInt(input2.getText().toString()) < Double
                                    .parseDouble(Utility
                                            .getUnFormattedAmount(product
                                                    .getMinamt())))) {
                                input2.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
                                return;
                            }
                        }
                    } else {
                        if (str != null && str.length() > 0) {
                            Double amount = Double.parseDouble(input2.getText()
                                    .toString() + "");
                            if (amount < 10 || amount > Constants.MAX_AMOUNT) {
                                input2.setError(Constants.Messages.AMOUNT_MAX_LIMIT);
                                return;
                            }
                        }
                    }

                    hideKeyboard(v);
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

        if (cashOutType.equals(Constants.CASH_OUT_BY_IVR))
            new HttpAsyncTask(CashOutInputActivity.this).execute(
                    Constants.CMD_CASH_OUT_INFO + "", product.getId(), input1.getText().toString(),
                    ApplicationData.agentMobileNumber, input2.getText().toString(), isHRA, input3.getText().toString(), "1",product.getId().equals(Constants.PRODUCT_ID_CASH_WITH_DRAWAL)? "":"1");
        else
            new HttpAsyncTask(CashOutInputActivity.this).execute(
                    Constants.CMD_CASH_OUT_BY_TRX_ID_INFO + "", product.getId(),
                    input1.getText().toString(), input2.getText().toString());
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
                    .getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);

                if (message.getCode().equals(
                        Constants.ErrorCodes.WALK_IN_CUSTOMER)) {

                    showRegistrationDialog(Constants.Messages.ALERT_HEADING,
                            message.getDescr(), null, new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    gotoAccountOpening(input1.getText().toString());
                                }
                            }
                    );

                } else if (message.getCode().equals("9033")) {
                    Intent intent = new Intent(CashOutInputActivity.this, HraRegistrationActivity1.class);
                    intent.putExtra(Constants.IntentKeys.CNIC, input3.getText().toString());
                    intent.putExtra(Constants.IntentKeys.MOBILE, input1.getText().toString());
                    intent.putExtra(Constants.IntentKeys.IS_CASH_WITHDRAW, true);
                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL,product);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT, input2.getText().toString());
                    startActivity(intent);
                } else if (message.getCode().equals("9041")) {
                    Intent intent = new Intent(CashOutInputActivity.this, OpenAccountBvsActivity.class);
                    mBundle.putByte(Constants.IntentKeys.FLOW_ID, Constants.FLOW_ID_L0_L1);
                    intent.putExtra(Constants.IntentKeys.CNIC, input3.getText().toString());
                    intent.putExtra(Constants.IntentKeys.MOBILE, input1.getText().toString());
                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL,product);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT, input2.getText().toString());
                    intent.putExtra(Constants.IntentKeys.L0_TO_L1_UPGRADE, true);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else if (message.getCode().equals("9042")) {
                    Intent intent = new Intent(CashOutInputActivity.this, OpenAccountBvsActivity.class);
                    intent.putExtra(Constants.IntentKeys.CNIC, input3.getText().toString());
                    intent.putExtra(Constants.IntentKeys.MOBILE, input1.getText().toString());
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT, input2.getText().toString());
                    intent.putExtra(Constants.IntentKeys.IS_CASH_WITHDRAW, true);
                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL,product);
                    startActivity(intent);
                } else {
                    createAlertDialog(message.getDescr(),
                            Constants.KEY_LIST_ALERT, false, null);
                }
            } else {
                TransactionInfoModel transactionInfo = TransactionInfoModel.getInstance();

                if (cashOutType.equals(Constants.CASH_OUT_BY_IVR)) {
                    transactionInfo.CashInOut(table.get(Constants.ATTR_CMOB)
                                    .toString(), table.get(Constants.ATTR_CNAME).toString(),
                            table.get(Constants.ATTR_CNIC).toString(),
                            table.get(Constants.ATTR_TXAM).toString(),
                            table.get(Constants.ATTR_TXAMF).toString(),
                            table.get(Constants.ATTR_TPAM).toString(),
                            table.get(Constants.ATTR_TPAMF).toString(),
                            table.get(Constants.ATTR_CAMT).toString(),
                            table.get(Constants.ATTR_CAMTF).toString(),
                            table.get(Constants.ATTR_TAMT).toString(),
                            table.get(Constants.ATTR_TAMTF).toString());
                } else {
                    transactionInfo.CashOutByTrxId(table.get(Constants.ATTR_CMOB).toString(),
                            table.get(Constants.ATTR_NAME).toString(),
                            table.get(Constants.ATTR_CNIC).toString(),
                            table.get(Constants.ATTR_TXAM).toString(),
                            table.get(Constants.ATTR_TXAMF).toString(),
                            table.get(Constants.ATTR_TPAM).toString(),
                            table.get(Constants.ATTR_TPAMF).toString(),
                            table.get(Constants.ATTR_CAMT).toString(),
                            table.get(Constants.ATTR_CAMTF).toString(),
                            table.get(Constants.ATTR_TAMT).toString(),
                            table.get(Constants.ATTR_TAMTF).toString(),
                            table.get(Constants.ATTR_TRXID).toString());
                }

                Intent intent = new Intent(getApplicationContext(),
                        CashOutConfirmationActivity.class);
                intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL,
                        transactionInfo);
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
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);

            cashOutType = mBundle.getString(Constants.IntentKeys.CASH_OUT_TYPE);

            if (product.getId().equals(Constants.PRODUCT_ID_CASH_WITH_DRAWAL)) {
                isHRA = "1";

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}