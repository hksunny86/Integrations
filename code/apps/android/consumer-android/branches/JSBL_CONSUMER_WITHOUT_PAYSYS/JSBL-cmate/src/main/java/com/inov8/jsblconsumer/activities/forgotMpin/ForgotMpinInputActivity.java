package com.inov8.jsblconsumer.activities.forgotMpin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.forgotPassword.ForgotPasswordMobileInputActivity;
import com.inov8.jsblconsumer.activities.forgotPassword.ForgotPasswordOtpActivity;
import com.inov8.jsblconsumer.activities.loginPinChange.LoginPinChangeInputActivity;
import com.inov8.jsblconsumer.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.jsblconsumer.activities.openAccount.OpenAccountBvsOtpActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.CnicController;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

import java.util.Hashtable;
import java.util.List;

import static com.inov8.jsblconsumer.util.Constants.REQUEST_CODE_ACCOUNT_OPEN_OTP;

public class ForgotMpinInputActivity extends BaseCommunicationActivity implements View.OnClickListener {
    private Button forgotMpinBtn;
    private CnicController cnicController;
    private int currentCommand;
    private String cnic,mobileNumber;
    private boolean isHRa = false;
    private String isHRA = "0";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_mpin_input);
        headerImplementation();
        titleImplementation(null, "Forgot MPIN", null, this);
        setUI();

    }
    private void setUI() {
        cnicController = new CnicController(findViewById(R.id.cnicView), true, true, this);
        currentCommand = Constants.CMD_FORGOT_MPIN;

        forgotMpinBtn =  findViewById(R.id.btnNextForgotMpin);
        forgotMpinBtn.setOnClickListener(this);
    }
    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    ForgotMpinInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(ForgotMpinInputActivity.this).execute(Constants.CMD_FORGOT_MPIN + "",cnic,mobileNumber);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNextForgotMpin:
                if (validate()) {
                    processRequest();
                }
        }
    }

    private boolean validate() {
        if(cnicController.getCnic().length()==0){
            dialogGeneral = popupDialogs.createAlertDialog("CNIC field must not be empty", AppMessages.ALERT_HEADING,
                    ForgotMpinInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return false;
        }
        if (!cnicController.isValidCnic()) {
            dialogGeneral = popupDialogs.createAlertDialog("CNIC length should be of 13 digits", AppMessages.ALERT_HEADING,
                    ForgotMpinInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);


            return false;
        }
        mobileNumber = ApplicationData.mobileNo;
        cnic = cnicController.getCnic();

        return true;
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
                        ForgotMpinInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
//                                if (message != null && message.getCode() != null) {
//                                    goToMainMenu();
//                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);
            } else if (table != null && table.containsKey("msgs")) {
                List<?> list = (List<?>) table.get("msgs");

                final MessageModel message = (MessageModel) list.get(0);


                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        ForgotMpinInputActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
//                                goToMainMenu();
                            }
                        }, false, PopupDialogs.Status.SUCCESS, false, null);
            } else {


                hideLoading();
                if (table != null) {

                    if (table.containsKey(Constants.ATTR_DTID)) {
                        openOtpScreen();
                    }
                }


            }
            hideLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openOtpScreen() {
        Intent intent = new Intent(ForgotMpinInputActivity.this, OpenAccountBvsOtpActivity.class);
        intent.putExtra(Constants.IntentKeys.MOBILE_NO, mobileNumber);
        intent.putExtra(Constants.IntentKeys.CNIC, cnic);
        intent.putExtra(Constants.IntentKeys.HRA, isHRa);
        intent.putExtra(Constants.IntentKeys.FORGOT_MPIN, true);
        startActivity(intent);
    }
    @Override
    public void processNext() {

    }

    private void fetchIntent() {
        mBundle = getIntent().getExtras();
        isHRa = getIntent().getBooleanExtra(Constants.IntentKeys.HRA, false);
        if (isHRa) {
            isHRA = "1";
        }
    }
}