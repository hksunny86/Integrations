package com.inov8.jsblconsumer.activities;

import android.os.Bundle;
import android.view.View;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountBalanceInquiryActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

import java.util.Hashtable;
import java.util.List;

public class SignOutActivity extends BaseCommunicationActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_signout);

        processRequest();
    }

    @Override
    public void processRequest() {
        ApplicationData.resetData();
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    SignOutActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);
            loginPage();
            return;
        }

        try {
            showLoading("Please Wait", "Processing...");
            new HttpAsyncTask(SignOutActivity.this).execute(Constants.CMD_SIGN_OUT + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table.containsKey("errs")) {
                List<?> list = (List<?>) table.get("errs");
                final MessageModel message = (MessageModel) list.get(0);
                if (message.getCode().equals(Constants.ErrorCodes.SESSION_EXPIRED)) {
                    loginPage();
                } else {
                    dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
                            SignOutActivity.this, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.cancel();
                                    finish();
                                }
                            }, PopupDialogs.Status.ERROR);
                }
            } else {
                loginPage();
            }
            hideLoading();
            ApplicationData.isLogin = false;
        } catch (Exception e) {
            hideLoading();
            loginPage();
        }
    }

    @Override
    public void processNext() {
        hideLoading();
    }
}