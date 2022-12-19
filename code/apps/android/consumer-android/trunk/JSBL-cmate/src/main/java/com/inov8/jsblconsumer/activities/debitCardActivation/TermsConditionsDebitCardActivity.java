package com.inov8.jsblconsumer.activities.debitCardActivation;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.activities.debitCard.DebitCardIssuance;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

public class TermsConditionsDebitCardActivity extends BaseActivity {

    private WebView webView;
    private RadioButton btnConfirm, btnCancel;
    private AppCompatImageView btnBack;
    private TextView heading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions_debit_card);
        setUi();
//        navigationDrawer = new NavigationDrawerManager(this, null);
//        navigationDrawer.initDrawer(this, savedInstanceState, backButtonListener);
//        initHeaderBar();
        showWebView();
    }

    private void setUi() {

//        TextView headerText = (TextView) findViewById(R.id.tvAppName);
//        headerText.setText("Account Opening");
        titleImplementation("icon_customer_registration", "Debit Card Issuance", null, this);
        headerImplementation();
        btnSignout.setVisibility(View.GONE);
        btnHome.setVisibility(View.GONE);

        btnConfirm = (RadioButton) findViewById(R.id.btnNext);
        btnCancel = (RadioButton) findViewById(R.id.btnCancel);
        btnBack = (AppCompatImageView) findViewById(R.id.btnBack);

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideLoading();
                if (ApplicationData.isLogin) {
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                if (!ApplicationData.isLogin) {
                    Intent intent = new Intent(TermsConditionsDebitCardActivity.this, DebitCardIssuance.class);
                    startActivity(intent);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationData.isLogin) {
                    goToMainMenu();
                } else {
                    goToLogin();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationData.isLogin) {
                    goToMainMenu();
                } else {
                    goToLogin();
                }
            }
        });
    }

    private void showWebView() {
        showLoading("Please Wait", "Processing...");
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
                hideLoading();
            }
        });
        webView.loadUrl(Constants.URL_TERMS_OF_DEBIT_CARD_ISSUENCE);
    }

}
