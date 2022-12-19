package com.inov8.jsblconsumer.activities.registration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class TermsConditionsActivity extends BaseCommunicationActivity {

    private WebView webViewSuccessMsg;
    private Button btnNext, btnCancel;
    private String strMsg, mobileNum, cnic, zongSubs, bvsVerf, regCnic;
    private String strAndroidVersion, strDeviceVendor, strDeviceModel, strDeviceNetwork, strUniqueDeivceID;
    private TextView subHeading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        fetchIntents();
        titleImplementation("icon_customer_registration", "Customer Registration", "Terms & Conditions", this);

        strAndroidVersion = Build.VERSION.RELEASE;
        strDeviceVendor = Build.MANUFACTURER;
        strDeviceModel = Build.MODEL;
        strDeviceNetwork = getMobileNetwork();
        strUniqueDeivceID = Settings.Secure.getString(TermsConditionsActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        subHeading = (TextView) findViewById(R.id.lblSubHeading);
        subHeading.setTextColor(getResources().getColor(R.color.black));

        webViewSuccessMsg = (WebView) findViewById(R.id.webViewSuccessMsg);
//        webViewSuccessMsg.loadData(Constants.URL_TERMS_OF_USE, "text/html", "UTF-8");
        webViewSuccessMsg.loadUrl(Constants.URL_TERMS_OF_USE);
        webViewSuccessMsg.setBackgroundColor(getResources().getColor(R.color.transparent));

        showLoading(getString(R.string.please_wait), getString(R.string.processing));
        webViewSuccessMsg.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                AppLogger.i("Processing webview url click...");
//                view.loadUrl(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                AppLogger.i("Finished loading URL: ");
                hideLoading();
            }

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                AppLogger.i("Error: " + description);

                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                };
                PopupDialogs.createAlertDialog(description,
                        getString(R.string.alertNotification), TermsConditionsActivity.this,
                        clickListener, PopupDialogs.Status.ERROR);
            }
        });

        btnNext = (Button) findViewById(R.id.btnNext);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                processRequest();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStart();
            }
        });

        headerImplementation();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, TermsConditionsActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        try {
            showLoading("Please Wait", "Processing...");
            new HttpAsyncTask(TermsConditionsActivity.this).execute(Constants.CMD_CUSTOMER_TERMS_CONDITIONS + "", zongSubs,
                    bvsVerf, cnic, mobileNum, regCnic, strUniqueDeivceID, strAndroidVersion, strDeviceVendor, strDeviceNetwork, strDeviceModel);
        } catch (Exception e) {
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
                strMsg = message.getDescr();
            } else {
                hideLoading();
                if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                    MessageModel message = (MessageModel) list.get(0);
                    strMsg = message.getDescr();
                }
            }
            loadingDialog.dismiss();
            processNext();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
    }

    @Override
    public void processNext() {
        Intent intent = new Intent(TermsConditionsActivity.this, RegistrationReceiptActivity.class);
        intent.putExtra(Constants.IntentKeys.TERMS, strMsg);
        startActivity(intent);
        hideLoading();
    }

    private void fetchIntents() {
        mobileNum = (String) mBundle.get(XmlConstants.Attributes.MOBN);
        cnic = (String) mBundle.get(XmlConstants.Attributes.CNIC);
        zongSubs = (String) mBundle.get(XmlConstants.Attributes.IS_ZONG_SUBS);
        bvsVerf = (String) mBundle.get(XmlConstants.Attributes.IS_BVS_VERF);
        regCnic = (String) mBundle.get(XmlConstants.Attributes.IS_CNIC_REG);
    }

    private String getMobileNetwork() {
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tel.getNetworkOperator();
        networkOperator = tel.getNetworkOperatorName();
        return networkOperator;
    }
}
