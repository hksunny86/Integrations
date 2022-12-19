package com.inov8.jsblconsumer.activities.collection;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class CollectionPaymentsInfoActivity extends
        BaseCommunicationActivity {
    private TextView lblField1, lblField2, lblHeading, lblSubHeading, lblFieldAmountHint;
    private EditText input1, input3;
    private Button btnNext;
    private Byte flowId;
    private CategoryModel categoryModel;
    private ProductModel mProduct;

    private String consumer, mobileNumber, amount, billType;
    private byte paymentType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_general);
        bottomBarImplementation(CollectionPaymentsInfoActivity.this, "");
        checkSoftKeyboardD();

        fetchintents();


        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText(categoryModel.getName());

        lblSubHeading = (TextView) findViewById(R.id.lblSubHeading);
        lblSubHeading.setVisibility(View.VISIBLE);
        lblSubHeading.setText(mProduct.getName());

        lblField1 = (TextView) findViewById(R.id.lblField1);
        lblField2 = (TextView) findViewById(R.id.lblField2);
        input1 = (EditText) findViewById(R.id.input1);
        input3 = (EditText) findViewById(R.id.input3);
        disableCopyPaste(input1);
        disableCopyPaste(input3);


//        if (mProduct.getInRequired().equals("0")) {
//            setAmountInput();
//        } else {
        if (mProduct.getLabel() != null) {
            lblField1.setText(mProduct.getLabel() + "");
            lblField1.setVisibility(View.VISIBLE);
            input1 = (EditText) findViewById(R.id.input1);
            if (mProduct.getMaxConsumerLength() != null && !mProduct.getMaxConsumerLength().isEmpty())
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Double.parseDouble(mProduct.getMaxConsumerLength()))});
            else
                input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

            input1.setVisibility(View.VISIBLE);
        }

//        }

		/* for products which have alpha numeric reference number */
        setProductTypeFilter();

        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(v);

                //input Amount
//                if (mProduct.getInRequired().equals("0")) {
//                    if (input3 != null) {
//                        if (TextUtils.isEmpty(input3.getText())) {
//                            input3.setError(AppMessages.ERROR_EMPTY_FIELD);
//                            return;
//                        }
//                    }
//
//                    if (!Utility.isNull(mProduct.getMinamt()) &&
//                            !mProduct.getMinamt().equals("") &&!Utility.isNull(mProduct.getMaxamt()) &&
//                            !mProduct.getMaxamt().equals("") &&
//                            (Integer.parseInt(input3.getText().toString()) > Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMaxamt()))
//                                    ||
//                                    Integer.parseInt(input3.getText().toString()) <
//                                            Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMinamt())))) {
//                        input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
//                        return;
//                    } else if (Integer.parseInt(input3.getText().toString()) <= 0) {
//                        input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
//                        return;
//                    }
//
//                    if (!isNull(mProduct.getMultiple())&&mProduct.getMultiple().equals("")) {
//                        if (!checkMutliple(input3.getText().toString(), mProduct.getMultiple())) {
//                            input3.setError(AppMessages.ERROR_MULTIPLE);
//                            return;
//                        }
//                    }
//                    mBundle.putString(Constants.IntentKeys.TRANSACTION_AMOUNT, input3.getText().toString());
//                    mBundle.putString("MOBILE_NO", ApplicationData.userId);
//                    amount = input3.getText().toString();
//                    mpinProcess(null, null);
//
//                 //input Consumer number
//                } else {
                if (input1 != null) {
                    if (TextUtils.isEmpty(input1.getText())) {
                        input1.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }

                    if ((mProduct.getMinConsumerLength() != null && !mProduct.getMinConsumerLength().isEmpty() && input1.getText().toString().length() < (int) Double.parseDouble(mProduct.getMinConsumerLength()))
                            || (mProduct.getMaxConsumerLength() != null && !mProduct.getMaxConsumerLength().isEmpty() && input1.getText().toString().length() > (int) Double.parseDouble(mProduct.getMaxConsumerLength()))) {
                        if (mProduct.getMinConsumerLength().equals(mProduct.getMaxConsumerLength())) {
                            input1.setError(mProduct.getLabel() + " length should be " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
                                    + " digits.");
                        } else {
                            input1.setError(mProduct.getLabel() + " should be between " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
                                    + " to " + (int) Double.parseDouble(mProduct.getMaxConsumerLength()) + " digits.");
                        }
                        return;
                    }
                }
                consumer = input1.getText().toString();
                mobileNumber = ApplicationData.userId;

                mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);
                mBundle.putString("CONSUMER_NO", input1.getText().toString());
                mBundle.putString("MOBILE_NO", ApplicationData.userId);
                processRequest();
            }

//            }
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
        billType = mBundle.getString(XmlConstants.Tags.BILLTYPE);
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    CollectionPaymentsInfoActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");

        new HttpAsyncTask(CollectionPaymentsInfoActivity.this).execute(
                Constants.CMD_COLLECTION_INFO + "",
                mProduct.getId(),
                consumer != null ? consumer : "",
                amount != null ? amount : "");
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);

                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        CollectionPaymentsInfoActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);
            } else {


                if (table.get(XmlConstants.Attributes.BPAID).toString().equals("1")) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.CHALLAN_ALREADY_PAID, AppMessages.ALERT_HEADING,
                            CollectionPaymentsInfoActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ALERT, false, null);


//                } else if (table.get(XmlConstants.Attributes.ISOVERDUE).toString().equals("1")) {
//
//                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.BILL_ALREADY_PAID, AppMessages.ALERT_HEADING,
//                            CollectionPaymentsInfoActivity.this, getString(R.string.ok), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialogGeneral.dismiss();
//                                }
//                            }, false, PopupDialogs.Status.ALERT, false, null);

                } else {

                    TransactionInfoModel transactoinInfo = TransactionInfoModel.getInstance();
                    transactoinInfo.CollectionInquiry(
                           mProduct.getId(),
                            table.get(XmlConstants.Attributes.PNAME).toString(),
                            table.get(XmlConstants.Attributes.CONSUMER).toString(),
                            table.get(XmlConstants.Attributes.BAMT).toString(),
                            table.get(XmlConstants.Attributes.BAMTF).toString(),
                            table.get(XmlConstants.Attributes.TPAM).toString(),
                            table.get(XmlConstants.Attributes.TPAMF).toString(),
                            table.get(XmlConstants.Attributes.TAMT).toString(),
                            table.get(XmlConstants.Attributes.TAMTF).toString(),
                            table.get(XmlConstants.Attributes.CMOB).toString(),
                            "",
                            table.get(XmlConstants.Attributes.DUEDATE) != null ? table.get(XmlConstants.Attributes.DUEDATE).toString() : ""
                    );
                    transactoinInfo.setDuedatef(table.get(XmlConstants.Attributes.DUEDATEF).toString());
                    transactoinInfo.setBpaid(table.get(XmlConstants.Attributes.BPAID).toString());
                    Intent intent = new Intent(getApplicationContext(), CollectionPaymentConfirmationActivity.class);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactoinInfo);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
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


            if (mProduct.getMaxConsumerLength() != null && !mProduct.getMaxConsumerLength().isEmpty())
                input1.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter((int) Double.parseDouble(mProduct.getMaxConsumerLength()))});
            else
                input1.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(30)});

//            input1.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(50)});
            input1.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    private void setAmountInput() {
        lblField2 = (TextView) findViewById(R.id.lblField2);
        lblField2.setVisibility(View.VISIBLE);
        lblFieldAmountHint = (TextView) findViewById(R.id.lblAlert);
        lblFieldAmountHint.setVisibility(View.VISIBLE);

        if (mProduct.getMinamtf() != null && !mProduct.getMinamtf().equals("") || mProduct.getMaxamtf() != null && !mProduct.getMaxamtf().equals("")) {
            lblField2.setText("Amount ");
            lblFieldAmountHint.setText("(" + "Enter" + mProduct.getMinamtf() + " PKR to " + mProduct.getMaxamtf() + " PKR)");
        } else {
            lblField2.setText("Amount");
        }

        input3 = (EditText) findViewById(R.id.input3);
        input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});
        input3.setVisibility(View.VISIBLE);

        if (!isNull(mProduct.getMultiple()) && mProduct.getMultiple().equals("")) {
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