package com.inov8.agentmate.activities.collection;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsbl.sco.R;

import java.util.Hashtable;
import java.util.List;

public class CollectionPaymentInputActivity extends BaseCommunicationActivity {
    private TextView lblField1, lblField2, lblHeading;
    private EditText input1, input2;
    private String strMobileNo, strConsumerNo;
    private Button btnNext;
    private ProductModel product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_general);

        try {
            fetchIntents();

            lblHeading = (TextView) findViewById(R.id.lblHeading);

            if(product != null){
                lblHeading.setText(product.getName());
            }

            lblField1 = (TextView) findViewById(R.id.lblField1);
            lblField1.setText("Customer Mobile Number");
            lblField1.setVisibility(View.VISIBLE);

            input1 = (EditText) findViewById(R.id.input1);
            input1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
            input1.setVisibility(View.VISIBLE);
            disableCopyPaste(input1);

            lblField2 = (TextView) findViewById(R.id.lblField2);
            if (product.getLabel() != null && !product.getLabel().equals(""))
                lblField2.setText(product.getLabel());
            else
                lblField2.setText("Consumer No.");
            lblField2.setVisibility(View.VISIBLE);

            input2 = (EditText) findViewById(R.id.input2);
            input2.setVisibility(View.VISIBLE);
            disableCopyPaste(input2);

            if (product.getMaxConsumerLength() != null && !product.getMaxConsumerLength().equals("")) {
                if (product.getType() != null && !product.getType().equals("")) {
                    if (product.getType().equals("Alphanumeric")) {
                        input2.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter((int)
                                Double.parseDouble(product.getMaxConsumerLength()))});
                        input2.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                    else if (product.getType().equals("Numeric")) {
                        input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int)
                                Double.parseDouble(product.getMaxConsumerLength()))});
                    }
                } else {
                    input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int)
                            Double.parseDouble(product.getMaxConsumerLength()))});
                }
            }
            else {
                input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter
                        (Constants.MAX_LENGTH_CARD)});
            }


            input1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()==11){
                        if(input2!=null)
                            input2.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Utility.testValidity(input1)) {
                        return;
                    }

                    strMobileNo = input1.getText().toString();

                    if (input1.getText().charAt(0) != '0' || input1.getText().charAt(1) != '3') {
                        input1.setError(AppMessages.INVALID_MOBILE_NUMBER_FORMAT);
                        input1.requestFocus();
                        return;
                    }

                    if (strMobileNo.length() < 11) {
                        input1.setError(AppMessages.INVALID_MOBILE_NUMBER);
                        input1.requestFocus();
                        return;
                    }

                    if(!Utility.testValidity(input2))
                        return;

                    strConsumerNo = input2.getText().toString();

                    if((product.getMinConsumerLength()!=null && !product.getMinConsumerLength().equals(""))
                            && (product.getMaxConsumerLength()!=null && !product.getMaxConsumerLength().equals(""))) {
                        if (strConsumerNo.length() < (int) Double.parseDouble(Utility
                                .getUnFormattedAmount(product.getMinConsumerLength()))
                                || (strConsumerNo.length() > (int) Double.parseDouble(Utility
                                .getUnFormattedAmount(product.getMaxConsumerLength())))) {

                            input2.setError((product.getLabel() != null ? product.getLabel() : "Consumer No.") + " should be between "
                                    + (int) Double.parseDouble(Utility
                                    .getUnFormattedAmount(product.getMinConsumerLength())) + " to " + (int) Double.parseDouble(Utility
                                    .getUnFormattedAmount(product.getMaxConsumerLength())) + " digits.");
                            input2.requestFocus();
                            return;
                        }
                    }

                    hideKeyboard(v);
                    processRequest();
                }
            });
            addAutoKeyboardHideFunction();
        } catch (Exception ex) {
            createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
            ex.printStackTrace();
        }
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(),
                    AppMessages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            showLoading("Please Wait", "Processing...");
            new HttpAsyncTask(CollectionPaymentInputActivity.this).execute(
                    Constants.CMD_COLLECTION_PAYMENT_INFO+"", product.getId()+"",
                    strMobileNo!=null? strMobileNo: "", strConsumerNo!=null? strConsumerNo: "");
        } catch (Exception e) {
            hideLoading();
            createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE,
                    AppMessages.ALERT_HEADING);
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
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING, false, message);
            }
            else
            {
                TransactionInfoModel trnModel = TransactionInfoModel.getInstance();
                trnModel.setPid(table.get(Constants.ATTR_PID).toString());
                trnModel.setPname(table.get(Constants.ATTR_PNAME).toString());
                trnModel.setTxamf(table.get(Constants.ATTR_BAMTF).toString());
                trnModel.setTxam(table.get(Constants.ATTR_BAMT).toString());
                trnModel.setTpam(table.get(Constants.ATTR_TPAM).toString());
                trnModel.setTpamf(table.get(Constants.ATTR_TPAMF).toString());
                trnModel.setTamt(table.get(Constants.ATTR_TAMT).toString());
                trnModel.setTamtf(table.get(Constants.ATTR_TAMTF).toString());
                trnModel.setDuedate(table.get(Constants.ATTR_DUEDATE).toString());
                trnModel.setDuedatef(table.get(Constants.ATTR_DUEDATEF).toString());
                trnModel.setStatus(table.get(Constants.ATTR_BPAID).toString());
                trnModel.setCmob(table.get(Constants.ATTR_CMOB).toString());
                trnModel.setConsumer(table.get(Constants.ATTR_CONSUMER).toString());

                if(trnModel.getStatus()!=null && trnModel.getStatus().equals("1")){
                    hideLoading();
                    createAlertDialog(AppMessages.PAYMENT_ALREADY_DONE, AppMessages.ALERT_HEADING);
                    return;
                }
                else {
                    hideLoading();
                    Intent intent = new Intent(getApplicationContext(),
                            CollectionPaymentConfirmationActivity.class);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, trnModel);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }
        }
        catch (Exception e) {
            hideLoading();
            createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
            e.printStackTrace();
        }
    }

    @Override
    public void processNext() {
    }

    private void fetchIntents() {
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence src, int start,
                                   int end, Spanned dst, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".contains(src.charAt(i) + "")) {
                    return "";
                }
            }
            return null;

        }
    };
}