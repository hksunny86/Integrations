package com.inov8.agentmate.activities.debitCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsblmfs.R;


public class TermsConditionsDebitCardActivity extends BaseActivity {

    private WebView webView;
    private RadioButton btnConfirm, btnCancel;
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
//        titleImplementation("icon_customer_registration", "Debit Card Issuance", null, this);
        headerImplementation();
//        btnSignout.setVisibility(View.GONE);
        btnHome.setVisibility(View.GONE);

        btnConfirm = (RadioButton) findViewById(R.id.btnNext);
        btnCancel = (RadioButton) findViewById(R.id.btnCancel);
        heading = (TextView)findViewById(R.id.tvTermsConditions);
        heading.setText("");

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideLoading();
                if (ApplicationData.isLoginFlow) {
                    Intent intent = getIntent();
                    ApplicationData.isTermsAndConditionAccepted = true;
                    setResult(RESULT_OK, intent);

                    finish();

                }
                if (!ApplicationData.isLoginFlow) {
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
//                    goToLogin();
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
                heading.setText(R.string.terms_link_js_agentmate);
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
