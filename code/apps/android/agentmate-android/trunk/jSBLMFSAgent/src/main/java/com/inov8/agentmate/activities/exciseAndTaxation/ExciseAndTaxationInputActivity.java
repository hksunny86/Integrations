package com.inov8.agentmate.activities.exciseAndTaxation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.collection.CollectionPaymentConfirmationActivity;
import com.inov8.agentmate.model.ExciseAndTaxationModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

import java.util.Hashtable;
import java.util.List;

public class ExciseAndTaxationInputActivity extends BaseCommunicationActivity {
    private TextView lblField1, lblField2, lblField3, lblField4, lblHeading;
    private EditText input1, input2, input3, input4;
    private String strVehicleRegistrationNo, strChassisNo, strMobileNo, strCustomerCNIC;
    private Button btnNext;
    private ProductModel product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_general);

        try {
            fetchIntents();

            lblHeading = (TextView) findViewById(R.id.lblHeading);

            if (product != null) {
                lblHeading.setText(product.getName());
            }

            lblField1 = (TextView) findViewById(R.id.lblField1);
            lblField1.setText("Vehicle Registration Number");
            lblField1.setVisibility(View.VISIBLE);

            input1 = (EditText) findViewById(R.id.input1);
            input1.setVisibility(View.VISIBLE);
            input1.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(20)});
            input1.setInputType(InputType.TYPE_CLASS_TEXT);
            disableCopyPaste(input1);

            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField2.setText("Chassis Number");
            lblField2.setVisibility(View.VISIBLE);

            input2 = (EditText) findViewById(R.id.input2);
            input2.setVisibility(View.VISIBLE);
            input2.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(20)});
            input2.setInputType(InputType.TYPE_CLASS_TEXT);
            disableCopyPaste(input2);

            lblField3 = (TextView) findViewById(R.id.lblField3);
            lblField3.setText("Customer Mobile Number");
            lblField3.setVisibility(View.VISIBLE);

            input3 = (EditText) findViewById(R.id.input3);
            input3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            input3.setVisibility(View.VISIBLE);
            disableCopyPaste(input3);


            lblField4 = (TextView) findViewById(R.id.lblField4);
            lblField4.setText("Customer CNIC");
            lblField4.setVisibility(View.VISIBLE);

            input4 = (EditText) findViewById(R.id.input4);
            input4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
            input4.setVisibility(View.VISIBLE);
            disableCopyPaste(input4);

            input3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 11) {
                        if (input4 != null)
                            input4.requestFocus();
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

                    if (!Utility.testValidity(input1))
                        return;

                    strVehicleRegistrationNo = input1.getText().toString();

                    if (!Utility.testValidity(input2))
                        return;

                    strChassisNo = input2.getText().toString();

                    if (!Utility.testValidity(input3)) {
                        return;
                    }

                    strMobileNo = input3.getText().toString();

                    if (input3.getText().charAt(0) != '0' || input3.getText().charAt(1) != '3') {
                        input3.setError(AppMessages.INVALID_MOBILE_NUMBER_FORMAT);
                        input3.requestFocus();
                        return;
                    }

                    if (strMobileNo.length() < 11) {
                        input3.setError(AppMessages.INVALID_MOBILE_NUMBER);
                        input3.requestFocus();
                        return;
                    }

                    if (!Utility.testValidity(input4)) {
                        return;
                    }

                    strCustomerCNIC = input4.getText().toString();

                    if (strCustomerCNIC.length() < 13) {
                        input4.setError(AppMessages.INVALID_CNIC);
                        input4.requestFocus();
                        return;
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
            new HttpAsyncTask(ExciseAndTaxationInputActivity.this).execute(
                    Constants.CMD_EXCISE_AND_TAXATION_INFO + "", product.getId() + "",
                    strVehicleRegistrationNo != null ? strVehicleRegistrationNo : "", strChassisNo != null ? strChassisNo : ""
                    , strMobileNo != null ? strMobileNo : "");
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
            } else {
                ExciseAndTaxationModel taxModel = ExciseAndTaxationModel.getInstance();
                taxModel.setVehRegNo(table.get(Constants.VEH_REG_NO).toString());
                taxModel.setVehChassisNo(table.get(Constants.VEH_CHASSIS_NO).toString());
                taxModel.setVehAssNo(table.get(Constants.VEH_ASS_NO).toString());
                taxModel.setVehRegDate(table.get(Constants.VEH_REG_DATE).toString());
                taxModel.setMakerMake(table.get(Constants.MAKER_MAKE).toString());
                taxModel.setVehCat(table.get(Constants.VEH_CAT).toString());
                taxModel.setVehBodyType(table.get(Constants.VEH_BODY_TYPE).toString());
                taxModel.setVehEngCapacity(table.get(Constants.VEH_ENG_CAPACITY).toString());
                taxModel.setVehSeats(table.get(Constants.VEH_SEATS).toString());
                taxModel.setVehCylinders(table.get(Constants.VEH_CYLINDERS).toString());
                taxModel.setVehOwnerName(table.get(Constants.VEH_OWNER_NAME).toString());
                taxModel.setVehOwnerCNIC(table.get(Constants.VEH_OWNER_CNIC).toString());
                taxModel.setVehFileerStatus(table.get(Constants.VEH_FILEER_STATUS).toString());
                taxModel.setTaxPaidFrom(table.get(Constants.TAX_PAID_FROM).toString());
                taxModel.setVehAssesmentNo(table.get(Constants.VEH_ASSESMENT_NO).toString());
                taxModel.setTaxPaidUpto(table.get(Constants.TAX_PAID_UPTO).toString());
                taxModel.setVehTaxPaidLifetime(table.get(Constants.VEH_TAX_PAID_LIFETIME).toString());
                taxModel.setVehStatus(table.get(Constants.VEH_STATUS).toString());
                taxModel.setVehFitnessDate(table.get(Constants.VEH_FITNESS_DATE).toString());
                taxModel.setVehTaxAmount(table.get(Constants.VEH_TAX_AMOUNT).toString());
                taxModel.setCharges(table.get(Constants.ATTR_TPAM).toString());
                taxModel.setVehTaxAmountF(table.get(Constants.ATTR_TXAMF).toString());
                taxModel.setChargesF(table.get(Constants.ATTR_TPAMF).toString());
                taxModel.setTotalAmount(table.get(Constants.ATTR_TAMT).toString());
                taxModel.setTotalAmountf(table.get(Constants.ATTR_TAMTF).toString());
                taxModel.setVehAssesmentDate(table.get(Constants.VEH_ASSESMENT_DATE).toString());

//                if (trnModel.getStatus() != null && trnModel.getStatus().equals("1")) {
//                    hideLoading();
//                    createAlertDialog(AppMessages.PAYMENT_ALREADY_DONE, AppMessages.ALERT_HEADING);
//                    return;
//                } else {
                    hideLoading();
                    Intent intent = new Intent(getApplicationContext(),
                            ExciseAndTaxationConfirmationActivity.class);
                    intent.putExtra(Constants.IntentKeys.EXCISE_AND_TAXATION_INFO_MODEL, taxModel);
                intent.putExtra(Constants.IntentKeys.RCNIC, strCustomerCNIC);
                intent.putExtra(Constants.IntentKeys.RCMOB, strMobileNo);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
        } catch (Exception e) {
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
        } catch (Exception e) {
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