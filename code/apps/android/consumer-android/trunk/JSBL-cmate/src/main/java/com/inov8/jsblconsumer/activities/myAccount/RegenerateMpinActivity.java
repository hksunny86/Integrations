package com.inov8.jsblconsumer.activities.myAccount;

import android.os.Bundle;
import android.view.View;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by ALI REHAN on 3/15/2018.
 */

public class RegenerateMpinActivity extends BaseCommunicationActivity {
    private ProductModel productModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_generate_mpin);
        fetchIntents();
        headerImplementation();
        bottomBarImplementation(RegenerateMpinActivity.this, "");
        titleImplementation(null, "Regenerate MPIN", null, this);
        processRequest();

    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    RegenerateMpinActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(RegenerateMpinActivity.this).execute(Constants.CMD_GENERATE_MPIN + "", ApplicationData.customerMobileNumber);
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
                        RegenerateMpinActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                                if (message != null && message.getCode() != null) {
                                    goToMainMenu();
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);
            } else if (table != null && table.containsKey("msgs")) {
                List<?> list = (List<?>) table.get("msgs");

                final MessageModel message = (MessageModel) list.get(0);




                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), "MPIN Generated",
                        RegenerateMpinActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                                goToMainMenu();
                            }
                        }, false, PopupDialogs.Status.SUCCESS, false, null);
            }
            hideLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processNext() {

    }

    private void fetchIntents() {
        productModel = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);

    }
}
