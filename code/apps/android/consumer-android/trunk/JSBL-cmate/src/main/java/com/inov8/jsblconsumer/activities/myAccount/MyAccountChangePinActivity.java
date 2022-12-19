package com.inov8.jsblconsumer.activities.myAccount;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.LoginActivity;
import com.inov8.jsblconsumer.activities.MainMenuActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AesEncryptor;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

import java.util.Hashtable;
import java.util.List;

public class MyAccountChangePinActivity extends BaseCommunicationActivity {
    private EditText oldPIN, newPIN, confirmPIN;
    private Button btnChangePIN,btnSetMpinLater;
    private TextView lblHeading;
    private TextView lblOldPIN;
    private TextView lblNewPIN;
    private TextView lblConfirmPIN;
    private String mPinChangeType = "";
    private String mNextPinChangeType = "";
    private ProductModel mProduct;
    private boolean mBothPinchange = false;
    private boolean isFromLoginMpin = false;
    private boolean isLaterButton = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_my_account_change_pin);

        mPinChangeType = mBundle.getString(Constants.IntentKeys.PIN_CHANGE_TYPE);
        mNextPinChangeType = mBundle.getString(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE);
        mProduct = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        mBothPinchange = mBundle.getBoolean(Constants.IntentKeys.BOTH_PIN_CHANGE);
        isFromLoginMpin = mBundle.getBoolean(Constants.IntentKeys.MPIN_FROM_LOGIN);


        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText(mProduct.getName());

        oldPIN = ((EditText) findViewById(R.id.txtOldPIN));
        newPIN = ((EditText) findViewById(R.id.txtNewPIN));
        confirmPIN = ((EditText) findViewById(R.id.txtConfirmPIN));

        lblOldPIN = ((TextView) findViewById(R.id.lblOldPIN));
        lblNewPIN = ((TextView) findViewById(R.id.lblNewPIN));
        lblConfirmPIN = ((TextView) findViewById(R.id.lblConfirmPIN));

        btnChangePIN = (Button) findViewById(R.id.btnChangePIN);
        btnSetMpinLater = (Button) findViewById(R.id.btnSetMpinLater);
        if (mPinChangeType.equals(Constants.LOGIN_PIN)) {
            btnChangePIN.setText("CHANGE PIN");
        } else if (mPinChangeType.equals(Constants.SET_MPIN)) {
            btnChangePIN.setText("SET MPIN");
            lblOldPIN.setText("New MPIN");
            lblNewPIN.setText("Confirm New MPIN");
            lblConfirmPIN.setVisibility(View.GONE);
            confirmPIN.setVisibility(View.GONE);
            if(isFromLoginMpin)
            btnSetMpinLater.setVisibility(View.VISIBLE);
            oldPIN.setMinEms(4);
            oldPIN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            oldPIN.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            newPIN.setMinEms(4);
            newPIN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            newPIN.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        } else {
            btnChangePIN.setText("CHANGE MPIN");
            lblOldPIN.setText("Old MPIN");
            lblNewPIN.setText("New MPIN");
            lblConfirmPIN.setText("Confirm New MPIN");
            oldPIN.setMinEms(4);
            oldPIN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            oldPIN.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            newPIN.setMinEms(4);
            newPIN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            newPIN.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            confirmPIN.setMinEms(4);
            confirmPIN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            confirmPIN.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }
        btnChangePIN.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (oldPIN.isShown()) {
                    if (TextUtils.isEmpty(oldPIN.getText())) {
                        oldPIN.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                }
                if (newPIN.isShown()) {
                    if (TextUtils.isEmpty(newPIN.getText())) {
                        newPIN.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                }
                if (confirmPIN.isShown()) {
                    if (TextUtils.isEmpty(confirmPIN.getText())) {
                        confirmPIN.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                }
                if (mPinChangeType.equals(Constants.LOGIN_PIN)) {
                    if (oldPIN.length() < 4) {
                        oldPIN.setError(AppMessages.INVLAID_PASSWORD_LENGTH);
                        return;
                    }

//                    if (oldPIN.getText().toString().matches("[0-9]+") && oldPIN.getText().toString().matches("[A-Za-z]+")) {
//                        oldPIN.setError(AppMessages.INVLAID_PASSWORD_TYPE);
//                        return;
//                    }

                    if (newPIN.length() < 4) {
                        newPIN.setError(AppMessages.INVLAID_PASSWORD_LENGTH);
                        return;
                    }

//                    if (newPIN.getText().toString().matches("[0-9]+") && newPIN.getText().toString().matches("[A-Za-z]+")) {
//                        newPIN.setError(AppMessages.INVLAID_PASSWORD_TYPE);
//                        return;
//                    }

                    if (confirmPIN.length() < 4) {
                        confirmPIN.setError(AppMessages.INVLAID_PASSWORD_LENGTH);
                        return;
                    }

//                    if (confirmPIN.getText().toString().matches("[0-9]+") && confirmPIN.getText().toString().matches("[A-Za-z]+")) {
//                        confirmPIN.setError(AppMessages.INVLAID_PASSWORD_TYPE);
//                        return;
//                    }

                    if (!(newPIN.getText() + "").equals(confirmPIN.getText() + "")) {
                        newPIN.setError(AppMessages.PASSWORD_MISMATCH);
                        return;
                    }
                    if ((oldPIN.getText() + "").equals(newPIN.getText() + "")) {
                        oldPIN.setError(AppMessages.SAME_PASSWORDS);
                        return;
                    }
                } else {
                    if (oldPIN.isShown()) {
                        if (oldPIN.length() < 4) {
                            oldPIN.setError(AppMessages.INVLAID_MPIN_LENGTH);
                            return;
                        }
                    }

                    if (newPIN.length() < 4) {
                        newPIN.setError(AppMessages.INVLAID_MPIN_LENGTH);
                        return;
                    }
                    if (mPinChangeType.equals(Constants.MPIN)) {
                        if (confirmPIN.length() < 4) {
                            confirmPIN.setError(AppMessages.INVLAID_MPIN_LENGTH);
                            return;
                        }
                        if (!(newPIN.getText() + "").equals(confirmPIN.getText() + "")) {
                            newPIN.setError(AppMessages.MPIN_MISMATCH);
                            return;
                        }
                        if (oldPIN.isShown()) {
                            if ((oldPIN.getText() + "").equals(newPIN.getText() + "")) {
                                oldPIN.setError(AppMessages.SAME_MPINS);
                                return;
                            }
                        }
                    } else {
                        if (oldPIN.isShown()) {
                            if (!(oldPIN.getText() + "").equals(newPIN.getText() + "")) {
                                oldPIN.setError(AppMessages.MPIN_MISMATCH);
                                return;
                            }
                        }
                    }
                }
                processRequest();
            }
        });
        btnSetMpinLater.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isLaterButton = true;
                processRequest();
            }
        });

        if (ApplicationData.isLogin){
            headerImplementation();
            findViewById(R.id.bottomBar).setVisibility(View.VISIBLE);
            findViewById(R.id.bottomBarQR).setVisibility(View.VISIBLE);
            bottomBarImplementation(MyAccountChangePinActivity.this, "");
            checkSoftKeyboardD();
        }
        else {
            headerImplementation();
            btnSignout.setVisibility(View.GONE);
        }

        addAutoKeyboardHideFunction();
        addAutoKeyboardHideFunctionScrolling();

    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    MyAccountChangePinActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }


        try {
            String encryptedOldPin = AesEncryptor.encrypt(oldPIN.getText().toString());
            String encryptedNewPin = AesEncryptor.encrypt(newPIN.getText().toString());
            String encryptedConfirmPin = AesEncryptor.encrypt(confirmPIN.getText().toString());
            showLoading("Please Wait", "Processing...");

            int command = -1;
            if(isLaterButton){
                command = Constants.CMD_SET_MPIN_LATER;
                new HttpAsyncTask(MyAccountChangePinActivity.this).execute(command + "", ApplicationData.mobileNo,ApplicationData.cnic,"1");
            }else if (mPinChangeType.equals(Constants.LOGIN_PIN)) {
                command = Constants.CMD_CHANGE_PIN;
                new HttpAsyncTask(MyAccountChangePinActivity.this).execute(command + "", encryptedOldPin, encryptedNewPin, encryptedConfirmPin);
            } else if (mPinChangeType.equals(Constants.SET_MPIN)) {
                command = Constants.CMD_SET_MPIN;
                new HttpAsyncTask(MyAccountChangePinActivity.this).execute(command + "",encryptedOldPin ,encryptedNewPin);
            } else {
                command = Constants.CMD_CHANGE_MPIN;
                new HttpAsyncTask(MyAccountChangePinActivity.this).execute(command + "", encryptedOldPin, encryptedNewPin, encryptedConfirmPin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            String xmlResponse = response.getXmlResponse();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(xmlResponse);

            AppLogger.i("\nXMLResponse: " + xmlResponse);
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                loadingDialog.dismiss();


                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        MyAccountChangePinActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);

//                PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        MyAccountChangePinActivity.this, PopupDialogs.Status.ERROR);
            } else {
                try {
                    if (table != null && table.containsKey(Constants.ATTR_DTID)) {
                        ApplicationData.isMpinSet = false;
                        Intent intent;
                        ApplicationData.isLogin = true;
                        if (mNextPinChangeType.equals(Constants.IGNORE_AND_GOTO_LOGIN)) {
                            intent = new Intent(MyAccountChangePinActivity.this, LoginActivity.class);
                        } else {
                            intent = new Intent(MyAccountChangePinActivity.this, MainMenuActivity.class);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                        List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                        MessageModel message = (MessageModel) list.get(0);
                        String msgStr = message.getDescr();

                        ApplicationData.isPinChangeRequired = "1";

                        if (mPinChangeType.equals(Constants.LOGIN_PIN)) {

                            dialogGeneral = popupDialogs.createAlertDialog(msgStr, "PIN Changed",
                                    MyAccountChangePinActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                            processNext();
                                        }
                                    }, false, PopupDialogs.Status.SUCCESS, false, null);

                        } else if (mPinChangeType.equals(Constants.MPIN)) {

                            dialogGeneral = popupDialogs.createAlertDialog(msgStr, "MPIN Changed",
                                    MyAccountChangePinActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                            processNext();
                                        }
                                    }, false, PopupDialogs.Status.SUCCESS, false, null);


                        } else if (mPinChangeType.equals(Constants.SET_MPIN)) {


                            dialogGeneral = popupDialogs.createAlertDialog(msgStr, "SET MPIN",
                                    MyAccountChangePinActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                            processNext();
                                        }
                                    }, false, PopupDialogs.Status.SUCCESS, false, null);


                        } else {

                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    MyAccountChangePinActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                        }
                                    }, false, PopupDialogs.Status.SUCCESS, false, null);

                        }
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
        if (!mBothPinchange && mPinChangeType.equals(Constants.LOGIN_PIN)) {
            Intent intent = new Intent(MyAccountChangePinActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if (!mBothPinchange && (mPinChangeType.equals(Constants.MPIN) || mPinChangeType.equals(Constants.SET_MPIN ))) {
            if(mPinChangeType.equals(Constants.SET_MPIN)) {
                ApplicationData.isMpinSet = true;
                removeSetMpin();
            }
            Intent intent;
            ApplicationData.isLogin = true;
            if (mNextPinChangeType.equals(Constants.IGNORE_AND_GOTO_LOGIN)) {
                intent = new Intent(MyAccountChangePinActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(MyAccountChangePinActivity.this, MainMenuActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if (mBothPinchange) {
            mBundle = new Bundle();
            Intent intent = new Intent(MyAccountChangePinActivity.this, MyAccountChangePinActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (mNextPinChangeType.equals(Constants.SET_MPIN)) {
                intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, new ProductModel("10029", Constants.FLOW_ID_CHANGE_PIN + "", "Set MPIN"));
                intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.SET_MPIN);
            }
            if (mNextPinChangeType.equals(Constants.MPIN)) {
                intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, new ProductModel("10028", Constants.FLOW_ID_CHANGE_PIN + "", "Change MPIN"));
                intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.MPIN);
            }
            intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.IGNORE_AND_GOTO_LOGIN);
            intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, false);
            intent.putExtras(mBundle);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        oldPIN.setText("");
        newPIN.setText("");
        confirmPIN.setText("");
    }

}