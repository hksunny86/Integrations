package com.inov8.jsblconsumer.activities.miniLoad;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class MiniLoadInputActivity extends
        BaseCommunicationActivity {
    private TextView lblField1, lblField2, lblHeading, lblSubHeading, lblFieldAmountHint;
    private EditText input1, input3;
    private Button btnNext;
    private Byte flowId;
    private CategoryModel categoryModel;
    private ProductModel mProduct;

    private String consumer, billAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_general);

        fetchintents();

        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText(categoryModel.getName());

        lblSubHeading = (TextView) findViewById(R.id.lblSubHeading);
        lblSubHeading.setVisibility(View.VISIBLE);
        lblSubHeading.setText(mProduct.getName());

        lblField1 = (TextView) findViewById(R.id.lblField1);
        lblField1.setVisibility(View.VISIBLE);



        if (mProduct.getLabel() != null) {
            lblField1.setText(mProduct.getLabel() + "");
        } else {
            lblField1.setText("Consumer No.");
        }

        input1 = (EditText) findViewById(R.id.input1);
        disableCopyPaste(input1);



        if (mProduct.getMaxConsumerLength() != null)
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Double.parseDouble(mProduct.getMaxConsumerLength()))});
        else
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

        input1.setVisibility(View.VISIBLE);

//        if (mProduct.getAmtRequired().equals("1")) {
        if (mProduct.getInRequired()!=null && mProduct.getInRequired().equals("0")){
            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField2.setText("Amount");
            lblField2.setVisibility(View.VISIBLE);

            input3 = (EditText) findViewById(R.id.input3);
            disableCopyPaste(input3);
            input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});
            input3.setVisibility(View.VISIBLE);

            lblFieldAmountHint = (TextView) findViewById(R.id.lblAlert);
            lblFieldAmountHint.setVisibility(View.VISIBLE);

            if (mProduct.getMinamtf() != null && !mProduct.getMinamtf().equals(""))
                lblFieldAmountHint.setText("Enter an amount of PKR " + mProduct.getMinamtf() + " to PKR " + mProduct.getMaxamtf());
        }

		/* for products which have alpha numeric reference number */
        setProductTypeFilter();

        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if (input1 != null) {
                    if (TextUtils.isEmpty(input1.getText())) {
                        input1.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                }
                if (input3 != null) {
                    if (TextUtils.isEmpty(input3.getText())) {
                        input3.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                }

                if ((mProduct.getMinConsumerLength() != null && input1.getText().toString().length() < (int) Double.parseDouble(mProduct.getMinConsumerLength()))
                        || (mProduct.getMaxConsumerLength() != null && input1.getText().toString().length() > (int) Double.parseDouble(mProduct.getMaxConsumerLength()))) {
                    if (mProduct.getMinConsumerLength().equals(mProduct.getMaxConsumerLength())) {
                        input1.setError("Mobile number length should be " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
                                + " digits.");
                    } else {
                        input1.setError("Mobile number should be between " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
                                + " to " + (int) Double.parseDouble(mProduct.getMaxConsumerLength()) + " digits.");
                    }
                    return;
                }

                if (input1.getText().charAt(0) != '0' || input1.getText().charAt(1) != '3') {
                    input1.setError(AppMessages.ERROR_MOBILE_NO_START);
                    return;
                }


                if (input3 != null) {
                    if (TextUtils.isEmpty(input3.getText())) {
                        input3.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    if (!Utility.isNull(mProduct.getDoValidate()) &&
                            mProduct.getDoValidate().equals("1") &&
                            (Integer.parseInt(input3.getText().toString()) >
                                    Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMaxamt())) ||
                                    Integer.parseInt(input3.getText().toString()) <
                                            Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMinamt())))) {
                        input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
                        return;
                    } else if (Integer.parseInt(input3.getText().toString()) <= 0) {
                        input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
                        return;
                    }

                    if (!isNull(mProduct.getMultiple())) {
                        if (!checkMutliple(input3.getText().toString(), mProduct.getMultiple())) {
                            input3.setError(AppMessages.ERROR_MULTIPLE);
                            return;
                        }
                    }
                    billAmount = input3.getText().toString();
                }
                consumer = input1.getText().toString();
                mpinProcess(null, null);
            }
        });
        addAutoKeyboardHideFunction();
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
    }

    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        processRequest();
    }

    void fetchintents() {
        categoryModel = (CategoryModel) mBundle.get(Constants.IntentKeys.CATEGORY_MODEL);
        mProduct = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, MiniLoadInputActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        showLoading("Please Wait", "Processing...");

        new HttpAsyncTask(MiniLoadInputActivity.this).execute(
                Constants.CMD_MINI_LOAD_INFO + "", mProduct.getId(),consumer, billAmount);
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
                        MiniLoadInputActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                }
                            }
                        }, PopupDialogs.Status.ERROR);
            } else {

                TransactionInfoModel transactoinInfo = TransactionInfoModel.getInstance();
                transactoinInfo.MiniLoadInfo(
                        table.get(XmlConstants.Attributes.TRN_PROD).toString(),
                        table.get(XmlConstants.Attributes.PNAME).toString(),
                        table.get(XmlConstants.Attributes.TMOB).toString(),
                        table.get(XmlConstants.Attributes.TXAM).toString(),
                        table.get(XmlConstants.Attributes.TXAMF).toString(),
                        table.get(XmlConstants.Attributes.TPAM).toString(),
                        table.get(XmlConstants.Attributes.TPAMF).toString(),
                        table.get(XmlConstants.Attributes.TAMT).toString(),
                        table.get(XmlConstants.Attributes.TAMTF).toString(),
                        table.get(XmlConstants.Attributes.CAMT).toString(),
                        table.get(XmlConstants.Attributes.CAMTF).toString());

                Intent intent = new Intent(getApplicationContext(), MiniLoadConfirmationActivity.class);
                intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, mProduct);
                intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactoinInfo);
                mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
        hideLoading();
    }

    @Override
    public void processNext() {
    }

    @SuppressWarnings("unused")
    private void clearBillInfo() {
        try {
        } catch (Exception e) {
            AppLogger.e(e);
        }
    }

    private void setProductTypeFilter() {
        if (mProduct.getType() != null && mProduct.getType().equals("ALPHANUMERIC")) {
            InputFilter filter = new InputFilter() {
                public CharSequence filter(CharSequence source, int start,
                                           int end, Spanned dest, int dstart, int dend) {
                    if (end > start) {
                        for (int i = start; i < end; i++) {
                            for (int index = start; index < end; index++) {
                                if (!new String(
                                        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-/")
                                        .contains(String.valueOf(source.charAt(index)))) {
                                    return "";
                                }
                            }
                        }
                    }
                    return null;
                }
            };

            input1.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(50)});
            input1.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    private void setAmountInput() {
        lblField2 = (TextView) findViewById(R.id.lblField2);
        lblField2.setVisibility(View.VISIBLE);
        lblFieldAmountHint = (TextView) findViewById(R.id.lblAlert);
        lblFieldAmountHint.setVisibility(View.VISIBLE);

        if (mProduct.getMinamtf() != null && !mProduct.getMinamtf().equals("")) {
            lblField2.setText("Amount ");
            lblFieldAmountHint.setText("(" + mProduct.getMinamtf() + " PKR to " + mProduct.getMaxamtf() + " PKR)");
        } else {
            lblField2.setText("Amount");
        }

        input3 = (EditText) findViewById(R.id.input3);
        input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});
        input3.setVisibility(View.VISIBLE);

        if (!isNull(mProduct.getMultiple())) {
            TextView txtview_multiple = (TextView) findViewById(R.id.lblAlertmultiple);
            txtview_multiple.setVisibility(View.VISIBLE);
            txtview_multiple.setText("(Multiple of " + mProduct.getMultiple() + ")");
        }
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
}