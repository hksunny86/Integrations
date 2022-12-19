package com.inov8.jsblconsumer.activities.billPayment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.amazonaws.util.StringUtils;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.fundsTransfer.FundsTransferInputActivity;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class BillPaymentsUtilityFetchBillInfoActivity extends BaseCommunicationActivity {
    List<HashMap<String, String>> aList = null;
    private TextView lblField1, lblField2, lblField5, lblHeading, lblSubHeading, lblFieldAmountHint;
    private Spinner spinnerDenoms;
    private EditText input1, input3;
    private Button btnNext;
    private Byte flowId;
    private CategoryModel categoryModel;
    private ProductModel mProduct;
    private List<String> denomList;

    private ImageView pastePin;

    private String consumer, mobileNumber, billAmount, billType;
    private byte paymentType;

    private EditText editText;
    private int RequestPermissionCode = 112;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_general);

        fetchintents();
        bottomBarImplementation(BillPaymentsUtilityFetchBillInfoActivity.this, "");
        checkSoftKeyboardD();

        aList = new ArrayList<>();

        lblHeading = findViewById(R.id.lblHeading);
        lblHeading.setText(categoryModel.getName());

        lblSubHeading = findViewById(R.id.lblSubHeading);
        lblSubHeading.setVisibility(View.VISIBLE);
        lblSubHeading.setText(mProduct.getName());

        lblField1 = findViewById(R.id.lblField1);
        lblField1.setVisibility(View.VISIBLE);

//        if (mProduct.getInRequired().equals("1")){
        if (mProduct.getLabel() != null) {
            lblField1.setText(mProduct.getLabel() + "");
        } else {
            lblField1.setText("Consumer No.");
        }

        input1 = (EditText) findViewById(R.id.input1);
//        pastePin = (ImageView) findViewById(R.id.pastePin);
//        pastePin.setVisibility(View.VISIBLE);
        disableCopyPaste(input1);
        if (mProduct != null && (mProduct.getId().equals("60027")
                || mProduct.getId().equals("60028")
                || mProduct.getId().equals("60032")
                || mProduct.getId().equals("60033")
                || mProduct.getId().equals("60034")
                || mProduct.getId().equals("60035")
                || mProduct.getId().equals("60036")
                || mProduct.getId().equals("60037")
                || mProduct.getId().equals("60038")
                || mProduct.getId().equals("60039")
                || mProduct.getId().equals("2511348"))) {
            setContactPicker(input1);
        }

        if (mProduct.getMaxConsumerLength() != null && !mProduct.getMaxConsumerLength().isEmpty())
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Double.parseDouble(mProduct.getMaxConsumerLength()))});
        else
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

        input1.setVisibility(View.VISIBLE);
//        }
        if (mProduct.getInRequired() != null && mProduct.getInRequired().equals("0")) {
            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField2.setText("Amount");
            lblField2.setVisibility(View.VISIBLE);

            input3 = (EditText) findViewById(R.id.input3);
            input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});
            input3.setVisibility(View.VISIBLE);

            disableCopyPaste(input3);

            lblFieldAmountHint = (TextView) findViewById(R.id.lblAlert);
            lblFieldAmountHint.setVisibility(View.VISIBLE);

            if (mProduct.getMinamtf() != null && !mProduct.getMinamtf().equals("") && mProduct.getMaxamtf() != null && !mProduct.getMaxamtf().equals(""))
                lblFieldAmountHint.setText("Enter an amount of PKR " + mProduct.getMinamtf() + " to PKR " + mProduct.getMaxamtf());

        }

//        if (mProduct.getLabel() != null) {
//            lblField1.setText(mProduct.getLabel() + "");
//        } else {
//            lblField1.setText("Consumer No.");
//        }
//
//        input1 = (EditText) findViewById(R.id.input1);
//
//        if (mProduct.getMaxConsumerLength() != null && !mProduct.getMaxConsumerLength().isEmpty())
//            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Double.parseDouble(mProduct.getMaxConsumerLength()))});
//        else
//            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
//
//        input1.setVisibility(View.VISIBLE);
//
//        if (mProduct.getAmtRequired().equals("1") && billType.equals(XmlConstants.Tags.BILLTYPE_PREPAID)) {
//            lblField2 = (TextView) findViewById(R.id.lblField2);
//            lblField2.setText("Amount");
//            lblField2.setVisibility(View.VISIBLE);
//
//            input3 = (EditText) findViewById(R.id.input3);
//            input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});
//            input3.setVisibility(View.VISIBLE);
//
//            lblFieldAmountHint = (TextView) findViewById(R.id.lblAlert);
//            lblFieldAmountHint.setVisibility(View.VISIBLE);
//
//            if (mProduct.getMinamtf() != null && !mProduct.getMinamtf().equals(""))
//                lblFieldAmountHint.setText("Enter an amount of PKR " + mProduct.getMinamtf() + " to PKR " + mProduct.getMaxamtf());
//        }

        setProductTypeFilter();

        if (mProduct.getDenomFlag().equals("1")) {

            List<String> denomStrList = Arrays.asList(mProduct.getDenomString().split(","));
            denomList = Arrays.asList(mProduct.getProdDenom().split(","));

            lblField5 = (TextView) findViewById(R.id.lblField5);
            lblField5.setText("Amount");
            lblField5.setVisibility(View.VISIBLE);

            spinnerDenoms = findViewById(R.id.spinner_denoms);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, denomStrList);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDenoms.setAdapter(adapter1);
            spinnerDenoms.setVisibility(View.VISIBLE);

            spinnerDenoms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    billAmount = denomList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    billAmount = denomList.get(0);
                }
            });
        }

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {

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

                if ((mProduct.getMinConsumerLength() != null && !mProduct.getMinConsumerLength().equals("") && input1.getText().toString().length() < (int) Double.parseDouble(mProduct.getMinConsumerLength()))
                        || (mProduct.getMaxConsumerLength() != null && !mProduct.getMaxConsumerLength().equals("") && input1.getText().toString().length() > (int) Double.parseDouble(mProduct.getMaxConsumerLength()))) {
                    if (mProduct.getMinConsumerLength().equals(mProduct.getMaxConsumerLength())) {
                        input1.setError(mProduct.getLabel() + " number length should be " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
                                + " digits.");
                    } else {
                        input1.setError(mProduct.getLabel() + " number should be between " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
                                + " to " + (int) Double.parseDouble(mProduct.getMaxConsumerLength()) + " digits.");
                    }
                    return;
                }
                if (mProduct.getInRequired() != null && mProduct.getInRequired().equals("0") && !mProduct.getInRequired().equals("")) {
                    if (input3 != null) {
                        if (TextUtils.isEmpty(input3.getText())) {
                            input3.setError(AppMessages.ERROR_EMPTY_FIELD);
                            return;
                        }
//                        if (!Utility.isNull(mProduct.getDoValidate()) &&
//                                mProduct.getDoValidate().equals("1") &&
//                                (Integer.parseInt(input3.getText().toString()) >
//                                        Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMaxamt())) ||
//                                        Integer.parseInt(input3.getText().toString()) <
//                                                Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMinamt())))) {
//                            input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
//                            return;
//                        } else if (Integer.parseInt(input3.getText().toString()) <= 0) {
//                            input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
//                            return;
//                        }

                        if (!Utility.isNull(mProduct.getMinamt()) &&
                                !mProduct.getMinamt().equals("") && !Utility.isNull(mProduct.getMaxamt()) &&
                                !mProduct.getMaxamt().equals("") &&
                                (Integer.parseInt(input3.getText().toString()) > Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMaxamt()))
                                        ||
                                        Integer.parseInt(input3.getText().toString()) <
                                                Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMinamt())))) {
                            input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
                            return;
                        } else if (Integer.parseInt(input3.getText().toString()) <= 0) {
                            input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
                            return;
                        }


//                        if (!isNull(mProduct.getMultiple())&&!mProduct.getMultiple().equals("")) {
//                            if (!checkMutliple(input3.getText().toString(), mProduct.getMultiple())) {
//                                input3.setError(AppMessages.ERROR_MULTIPLE);
//                                return;
//                            }
//                        }
                        billAmount = input3.getText().toString();
                    }
                    mBundle.putString(Constants.IntentKeys.TRANSACTION_AMOUNT, input3.getText().toString());
                }

//                if (billType != null && !billType.equals(XmlConstants.Tags.BILLTYPE_NORMAL)) {

//                    if ((mProduct.getMinConsumerLength() != null && input1.getText().toString().length() < (int) Double.parseDouble(mProduct.getMinConsumerLength()))
//                            || (mProduct.getMaxConsumerLength() != null && input1.getText().toString().length() > (int) Double.parseDouble(mProduct.getMaxConsumerLength()))) {
//                        if (mProduct.getMinConsumerLength().equals(mProduct.getMaxConsumerLength())) {
//                            input1.setError("Mobile number length should be " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
//                                    + " digits.");
//                        } else {
//                            input1.setError("Mobile number should be between " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
//                                    + " to " + (int) Double.parseDouble(mProduct.getMaxConsumerLength()) + " digits.");
//                        }
//                        return;
//                    }
//
//                    if (input1.getText().charAt(0) != '0' || input1.getText().charAt(1) != '3') {
//                        input1.setError(AppMessages.ERROR_MOBILE_NO_START);
//                        return;
//                    }

//                    if (!billType.equals(XmlConstants.Tags.BILLTYPE_POSTPAID)) {
//                        if (input3 != null) {
//                            if (TextUtils.isEmpty(input3.getText())) {
//                                input3.setError(AppMessages.ERROR_EMPTY_FIELD);
//                                return;
//                            }
//                            if (!Utility.isNull(mProduct.getDoValidate()) &&
//                                    mProduct.getDoValidate().equals("1") &&
//                                    (Integer.parseInt(input3.getText().toString()) >
//                                            Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMaxamt())) ||
//                                            Integer.parseInt(input3.getText().toString()) <
//                                                    Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMinamt())))) {
//                                input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
//                                return;
//                            } else if (Integer.parseInt(input3.getText().toString()) <= 0) {
//                                input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
//                                return;
//                            }
//
//                            if (!isNull(mProduct.getMultiple())) {
//                                if (!checkMutliple(input3.getText().toString(), mProduct.getMultiple())) {
//                                    input3.setError(AppMessages.ERROR_MULTIPLE);
//                                    return;
//                                }
//                            }
//
//                            mBundle.putString(Constants.IntentKeys.TRANSACTION_AMOUNT, input3.getText().toString());
//                            billAmount = input3.getText().toString();
//                            paymentType = 1;
//                        }
//                    }
//                }
//
//                if (billType.equals(XmlConstants.Tags.BILLTYPE_NORMAL)) {
//
//                    if ((mProduct.getMinConsumerLength() != null && input1.getText().toString().length() < (int) Double.parseDouble(mProduct.getMinConsumerLength()))
//                            || (mProduct.getMaxConsumerLength() != null && input1.getText().toString().length() > (int) Double.parseDouble(mProduct.getMaxConsumerLength()))) {
//                        if (mProduct.getMinConsumerLength().equals(mProduct.getMaxConsumerLength())) {
//                            input1.setError("Consumer number length should be " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
//                                    + " digits.");
//                        } else {
//                            input1.setError("Consumer number should be between " + (int) Double.parseDouble(mProduct.getMinConsumerLength())
//                                    + " to " + (int) Double.parseDouble(mProduct.getMaxConsumerLength()) + " digits.");
//                        }
//                        return;
//                    }
//
//                    if (mProduct.getId().equals("2510747")) {
//                        if (input3 != null) {
//                            if (TextUtils.isEmpty(input3.getText())) {
//                                input3.setError(AppMessages.ERROR_EMPTY_FIELD);
//                                return;
//                            }
//
//                            if (Integer.parseInt(input3.getText().toString()) >
//                                    Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMaxamt())) ||
//                                    Integer.parseInt(input3.getText().toString()) <
//                                            Double.parseDouble(Utility.getUnFormattedAmount(mProduct.getMinamt()))) {
//                                input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
//                                return;
//                            } else if (Integer.parseInt(input3.getText().toString()) <= 0) {
//                                input3.setError(AppMessages.ERROR_AMOUNT_INVALID);
//                                return;
//                            }
//
//                            mBundle.putString(Constants.IntentKeys.TRANSACTION_AMOUNT, input3.getText().toString());
//                            billAmount = input3.getText().toString();
//                        }
//                    }
//                }

                consumer = input1.getText().toString();
                mobileNumber = ApplicationData.userId;

                mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);
                mBundle.putString("CONSUMER_NO", input1.getText().toString());
                mBundle.putString("MOBILE_NO", ApplicationData.userId);

//                mBundle.putString(XmlConstants.Tags.BILLTYPE, billType);

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
//        billType = mBundle.getString(XmlConstants.Tags.BILLTYPE);
    }

    @Override
    public void processRequest() {

        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    BillPaymentsUtilityFetchBillInfoActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");

        new HttpAsyncTask(BillPaymentsUtilityFetchBillInfoActivity.this).execute(
                Constants.CMD_BILL_INQUIRY + "", mProduct.getId(),
                ApplicationData.customerMobileNumber, mobileNumber, consumer, billAmount != null ? billAmount : "", "", paymentType + "", ApplicationData.bankId);
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
                        BillPaymentsUtilityFetchBillInfoActivity.this, getString(R.string.ok), new View.OnClickListener() {
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


//
//                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        BillPaymentsUtilityFetchBillInfoActivity.this, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGeneral.cancel();
//                                if (message != null
//                                        && message.getCode() != null
//                                        && message.getCode().equals(
//                                        Constants.ErrorCodes.INTERNAL)) {
//                                    goToMainMenu();
//                                }
//                            }
//                        }, PopupDialogs.Status.ERROR);
            } else {
                if (table.containsKey(XmlConstants.Attributes.BPAID)) {
                    if (table.get(XmlConstants.Attributes.BPAID).toString().equals("1")) {

                        dialogGeneral = popupDialogs.createAlertDialog(AppMessages.BILL_ALREADY_PAID, AppMessages.ALERT_HEADING,
                                BillPaymentsUtilityFetchBillInfoActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


//                        PopupDialogs.createAlertDialog(AppMessages.BILL_ALREADY_PAID, getString(R.string.alertNotification),
//                                BillPaymentsUtilityFetchBillInfoActivity.this, PopupDialogs.Status.ERROR);

                    } else {
                        TransactionInfoModel transactoinInfo = TransactionInfoModel.getInstance();
                        transactoinInfo.BillInquiry(
                                table.get(XmlConstants.Attributes.CMOB).toString(),
                                table.get(XmlConstants.Attributes.CNIC).toString(),
                                table.get(XmlConstants.Attributes.PNAME).toString(),
                                table.get(XmlConstants.Attributes.CONSUMER).toString(),
                                table.get(XmlConstants.Attributes.DUEDATE).toString(),
                                table.get(XmlConstants.Attributes.DUEDATEF).toString(),
                                table.get(XmlConstants.Attributes.BAMT).toString(),
                                table.get(XmlConstants.Attributes.BAMTF).toString(),
                                table.get(XmlConstants.Attributes.LBAMT).toString(),
                                table.get(XmlConstants.Attributes.LBAMTF).toString(),
                                table.get(XmlConstants.Attributes.ISOVERDUE).toString(),
                                table.get(XmlConstants.Attributes.BPAID).toString());

                        transactoinInfo.setTpamf(table.get(XmlConstants.Attributes.TPAMF).toString());

                        if (mProduct.getInRequired().equals("0")) {
                            Intent intent = new Intent(getApplicationContext(), BillPaymentConfirmationActivity.class);

                            intent.putExtra(Constants.IntentKeys.PAYMENT_TYPE, paymentType);
                            intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactoinInfo);

                            if (transactoinInfo.getCnic().equals("-1")) // customer registration
                            {
                                intent.putExtra(Constants.IntentKeys.SHOW_REGISTRATION_POPUP, true);
                            }

                            intent.putExtras(mBundle);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), BillPaymentConfirmationActivity.class);

                            intent.putExtra(Constants.IntentKeys.PAYMENT_TYPE, paymentType);
                            intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactoinInfo);

                            if (transactoinInfo.getIsoverdue().equals("1")) // due date check
                            {
                                intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT, transactoinInfo.getLbamt());
                            } else {
                                intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT, transactoinInfo.getBamt());
                            }

                            if (transactoinInfo.getCnic().equals("-1")) // customer registration
                            {
                                intent.putExtra(Constants.IntentKeys.SHOW_REGISTRATION_POPUP, true);
                            }
                            intent.putExtras(mBundle);
                            startActivity(intent);
                        }
                    }
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

        input3 = findViewById(R.id.input3);
        input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});
        input3.setVisibility(View.VISIBLE);

        if (!isNull(mProduct.getMultiple())) {
            TextView txtview_multiple = findViewById(R.id.lblAlertmultiple);
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

    private void setContactPicker(EditText editText) {

        this.editText = editText;

        editText.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.mipmap.contact_picker, 0);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable drawableRight = editText.getCompoundDrawables()[DRAWABLE_RIGHT];
//                    if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (event.getRawX() >= (editText.getRight() - drawableRight.getBounds().width())) {
//                        if(event.getAction() == MotionEvent.ACTION_UP) {
//                            if(event.getRawX() <= (mTvTitle.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()))  {

                        // your action here
                        checkPermission();
                        return true;
                    }
                }
                return false;
            }
        });


//        pastePin.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkPermission();
//            }
//        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RequestPermissionCode) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = intent.getData();
                Cursor c = getContentResolver().query
                        (contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String contactId = c.getString(c.getColumnIndex
                            (ContactsContract.Contacts._ID));
                    String hasNumber = c.getString(c.getColumnIndex
                            (ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String num = "";
                    String name = "";
                    if (Integer.valueOf(hasNumber) == 1) {
                        Cursor numbers = getContentResolver().query
                                (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (numbers.moveToNext()) {
                            num = numbers.getString(numbers.getColumnIndex
                                    (ContactsContract.CommonDataKinds.Phone.NUMBER));
                            num = num.replace(" ", "");
                            num = num.trim();
                            AppLogger.e(num);
                            if (num.contains("0092")) {
                                num = num.replace("0092", "0");
                            } else if (num.contains("+92")) {
                                num = num.replace("+92", "0");
                            }
                            if (num.length() == 11 && num.contains("03") && num.matches("[0-9]+")) {
                                editText.setText(num);
                                editText.setSelection(editText.getText().length());

                            } else {
                                Toast.makeText(BillPaymentsUtilityFetchBillInfoActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
            }

        } else {
            throw new IllegalStateException(getString(R.string.unexp_value) + requestCode);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {

                Toast.makeText(BillPaymentsUtilityFetchBillInfoActivity.this, getString(R.string.contacts_permission_denied_msg), Toast.LENGTH_LONG).show();

            }
        } else {
            throw new IllegalStateException(getString(R.string.unexp_value) + requestCode);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // requesting to the user for permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        } else {
            //if app already has permission this block will execute.
            readContacts();
        }
    }

    private void readContacts() {
        editText.setText("");
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, RequestPermissionCode);
    }
}