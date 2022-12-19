package com.inov8.jsblconsumer.activities.openAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;

public class TermsConditionsJsActivity extends BaseActivity {

    private WebView webView;
    private Button btnConfirm, btnCancel;
    private TextView heading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        setUi();
//        navigationDrawer = new NavigationDrawerManager(this, null);
//        navigationDrawer.initDrawer(this, savedInstanceState, backButtonListener);
//        initHeaderBar();
        showWebView();
    }

    private void setUi() {

//        TextView headerText = (TextView) findViewById(R.id.tvAppName);
//        headerText.setText("Account Opening");
        titleImplementation("icon_customer_registration", "Account Opening", null, this);
        headerImplementation();
        btnSignout.setVisibility(View.GONE);
        btnHome.setVisibility(View.GONE);

        btnConfirm = (Button) findViewById(R.id.btnNext);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideLoading();
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
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
        webView.loadUrl(Constants.URL_TERMS_OF_USE);
    }

}
