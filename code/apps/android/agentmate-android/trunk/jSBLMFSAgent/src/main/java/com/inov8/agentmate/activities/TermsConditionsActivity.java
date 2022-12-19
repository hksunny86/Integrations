package com.inov8.agentmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.jsblmfs.R;

public class TermsConditionsActivity extends BaseCommunicationActivity {

    private WebView webView;
    private Button btnNext, btnCancel;
    private TextView heading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        setUi();
        showWebView();
    }

    private void setUi() {
        headerImplementation();

        heading = (TextView) findViewById(R.id.lblHeading);
        heading.setVisibility(View.VISIBLE);
        heading.setTextColor(getResources().getColor(R.color.black));
        heading.setText("Terms and Conditions");

        btnNext = (Button) findViewById(R.id.btnNext);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                processNext();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });
    }

    private void showWebView(){
        showLoading(AppMessages.PLEASE_WAIT, AppMessages.PROCESSING);

        webView = (WebView) findViewById(R.id.webViewSuccessMsg);
        webView.setBackgroundColor(getResources().getColor(R.color.transparent));

        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                AppLogger.i("Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                AppLogger.i("Finished loading URL: " + url);
                hideLoading();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                AppLogger.i("Error: " + description);
                webView.setVisibility(View.GONE);
                hideLoading();

                PopupDialogs.OnCustomClickListener clickListenerToMenu = new PopupDialogs.OnCustomClickListener() {
                    @Override
                    public void onClick(View v, Object obj) {
                        finish();
                    }
                };

                PopupDialogs.createAlertDialog(description, getString(R.string.alertNotification),
                        PopupDialogs.Status.ERROR,TermsConditionsActivity.this, clickListenerToMenu);
            }
        });
        webView.loadUrl(Constants.URL_TERMS_OF_USE);
    }

    @Override
    public void processRequest() {
    }

    @Override
    public void processResponse(HttpResponseModel response) {
    }

    @Override
    public void processNext() {
        hideLoading();
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
