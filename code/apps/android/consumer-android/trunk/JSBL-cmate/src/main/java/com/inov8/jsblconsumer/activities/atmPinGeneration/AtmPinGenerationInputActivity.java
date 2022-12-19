package com.inov8.jsblconsumer.activities.atmPinGeneration;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.MainMenuActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AesEncryptor;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class AtmPinGenerationInputActivity extends BaseCommunicationActivity {
    private TextView oldPinLabel;
    private EditText oldPin, newPin, confirmPin;
    private Button btnNext, btnCancel;
    private ProductModel product;
    private String pinRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_pin_change);
        try {
            fetchIntents();
            headerImplementation();
            titleImplementation(null, product.getName(), null, this);

            oldPinLabel = (TextView) findViewById(R.id.lblField1);
            oldPin = (EditText) findViewById(R.id.input1);
            newPin = (EditText) findViewById(R.id.input2);
            confirmPin = (EditText) findViewById(R.id.input3);
            btnNext = (Button) findViewById(R.id.btnNext);
            btnCancel = (Button) findViewById(R.id.btnCancel);

            oldPinLabel.setVisibility(View.GONE);
            oldPin.setVisibility(View.GONE);
            newPin.setMinEms(4);
            newPin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            newPin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            confirmPin.setMinEms(4);
            confirmPin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            confirmPin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialogGeneral = PopupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION, getString(R.string.alertNotification), AtmPinGenerationInputActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.cancel();
                            goToMainMenu();
                        }
                    });
                }
            });

            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (TextUtils.isEmpty(newPin.getText())) {
                        newPin.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    if (TextUtils.isEmpty(confirmPin.getText())) {
                        confirmPin.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    if (newPin.getText().toString().length() < 4) {
                        newPin.setError(AppMessages.PIN_LENGTH);
                        return;
                    }
                    if (confirmPin.getText().toString().length() < 4) {
                        confirmPin.setError(AppMessages.PIN_LENGTH);
                        return;
                    }
                    if (!newPin.getText().toString().equals(confirmPin.getText().toString())) {
                        newPin.setError(AppMessages.ATM_PIN_MISMATCH);
                        return;
                    }
                    askMpin(mBundle, null, false);
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void fetchIntents() {
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        pinRequest = (String) mBundle.get(XmlConstants.Tags.ATM_PIN_GENERATE);
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,AppMessages.ALERT_HEADING,
                    AtmPinGenerationInputActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.cancel();
                            finish();

                        }
                    }, PopupDialogs.Status.ERROR);

            return;
        }

        try {
            String encryptedNewPin = AesEncryptor.encrypt(newPin.getText().toString());
            String encryptedConfirmPin = AesEncryptor.encrypt(confirmPin.getText().toString());

            showLoading("Please Wait", "Processing...");
            new HttpAsyncTask(AtmPinGenerationInputActivity.this).execute(
                    Constants.CMD_ATM_PIN_GENERATE_CHANGE + "", getEncryptedMpin(),
                    "", encryptedNewPin, encryptedConfirmPin, pinRequest);
        } catch (Exception e) {
            hideLoading();
            PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE,
                    getString(R.string.alertNotification), AtmPinGenerationInputActivity.this, PopupDialogs.Status.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            String xmlResponse = response.getXmlResponse();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(xmlResponse);

            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
               final MessageModel message = (MessageModel) list.get(0);
                loadingDialog.dismiss();
                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
                        AtmPinGenerationInputActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                goToMainMenu();

                            }
                        }, PopupDialogs.Status.ERROR);

            } else {
                try {
                    if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                        List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                        MessageModel message = (MessageModel) list.get(0);

                        String dialogTitle;
                        if (pinRequest.equals("true")) {
                            dialogTitle = "Pin Generated";
                        } else {
                            dialogTitle = "Pin Changed";
                        }

                        dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), dialogTitle, AtmPinGenerationInputActivity.this,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.cancel();
                                        processNext();
                                    }
                                }, PopupDialogs.Status.SUCCESS);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
    }

    @Override
    public void processNext() {
        Intent intent = new Intent(AtmPinGenerationInputActivity.this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
