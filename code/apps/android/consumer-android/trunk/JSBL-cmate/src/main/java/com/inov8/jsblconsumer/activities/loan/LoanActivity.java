package com.inov8.jsblconsumer.activities.loan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.model.AdvanceLoanModel;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class LoanActivity extends BaseCommunicationActivity {

    private ProductModel product;
    private TextView lblHeading;
    private EditText inputAmount;
    private Button btnNext;
    private AdvanceLoanModel advanceLoanModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        fetchIntents();
        setUI();
        headerImplementation();
        bottomBarImplementation(LoanActivity.this, "");
    }

    private void fetchIntents() {
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void setUI() {
        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText(product.getName());

        inputAmount = (EditText) findViewById(R.id.inputAmount);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(view -> {
            if (validateFields()) {
                processRequest();
            }
        });
    }

    private boolean validateFields() {
        return !Utility.isNull(inputAmount.getText().toString());
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(), AppMessages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            showLoading("Please Wait", "Processing...");

            new HttpAsyncTask(this).execute(
                    Constants.CMD_ADVANCE_LOAN_INFO + "", product.getId(),
                    ApplicationData.mobileNo, ApplicationData.cnic, inputAmount.getText().toString());

        } catch (Exception e) {
            hideLoading();
//            createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
            e.printStackTrace();
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {

            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            hideLoading();

            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        LoanActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);

                String code = message.getCode();
                AppLogger.i("##Error Code: " + code);

            } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                hideLoading();
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);
                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), "Message",
                        LoanActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                                goToMainMenu();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);

            } else {
                hideLoading();
                advanceLoanModel = new AdvanceLoanModel(
                        table.get(XmlConstants.Attributes.PID).toString(),
                        table.get(XmlConstants.Attributes.CUSTOMER_CNIC).toString(),
                        table.get(XmlConstants.Attributes.CMOB).toString(),
                        table.get(XmlConstants.Attributes.TAMT).toString(),
                        table.get(XmlConstants.Attributes.TAMTF).toString(),
                        table.get(XmlConstants.Attributes.CAMT).toString(),
                        table.get(XmlConstants.Attributes.CAMTF).toString(),
                        table.get(XmlConstants.Attributes.TXAM).toString(),
                        table.get(XmlConstants.Attributes.TXAMF).toString(),
                        table.get(XmlConstants.Attributes.TPAM).toString(),
                        table.get(XmlConstants.Attributes.TPAMF).toString(),
                        table.get(XmlConstants.Attributes.THIRD_PARTY_RRN).toString());
                processNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING,
                    LoanActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);
        }
    }

    @Override
    public void processNext() {
        Intent intent = new Intent(this, LoanConfirmationActivity.class);
        intent.putExtra(Constants.IntentKeys.ADVANCE_LOAN_MODEL, advanceLoanModel);
        intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, product);
        intent.putExtras(mBundle);
        startActivity(intent);
    }
}