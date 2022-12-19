package com.inov8.jsblconsumer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountBalanceInquiryActivity;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;


public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private String strUrl, strHeading;
    private TextView tvHeading, tvSubheading;
    private ImageView ivIcon;
    private int icon;
    private View layoutBottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        headerImplementation();
        bottomBarImplementation(this,"");
        fetchIntents();
        setUI();
        layoutBottom =(View) findViewById(R.id.bottomBar);
        checkSoftKeyboard(layoutBottom);


        if (!haveInternet()) {
//            navigateToFirstScreen = true;

            PopupDialogs.createAlertDialog(
                    getString(R.string.internet_connection_problem),
                    getString(R.string.alertNotification), this, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, PopupDialogs.Status.ERROR);
            return;
        }

        ApplicationData.isWebViewOpen = true;
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setJavaScriptEnabled(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        webView.setBackgroundColor(getResources().getColor(R.color.transparent));

        showLoading(getString(R.string.please_wait), getString(R.string.processing));

        webView.setWebViewClient(new WebViewClient() {
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

                 webView.setVisibility(View.GONE);
                OnClickListener clickListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                };
                PopupDialogs.createAlertDialog(description,
                        getString(R.string.alertNotification), WebViewActivity.this,
                        clickListener, PopupDialogs.Status.ERROR);
            }
        });

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };

        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    WebViewActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);
        } else {
            webView.loadUrl(strUrl);
            ApplicationData.webUrl = strUrl;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        ApplicationData.isWebViewOpen = false;
        ApplicationData.webUrl = null;
        finish();
        super.onBackPressed();
    }

    private void fetchIntents() {
        strUrl = getIntent().getExtras().get(Constants.IntentKeys.WEB_URL)
                .toString();
        strHeading = getIntent().getExtras().get(Constants.IntentKeys.HEADING)
                .toString();
        icon = getIntent().getExtras().getInt(Constants.IntentKeys.ICON);
    }

    private void setUI() {
        tvHeading = (TextView) findViewById(R.id.lblHeading);
        tvSubheading = (TextView) findViewById(R.id.lblSubHeading);
        ivIcon = (ImageView) findViewById(R.id.icon);
        btnHome.setVisibility(View.GONE);

        tvHeading.setText(strHeading);
        tvSubheading.setVisibility(View.GONE);
        ivIcon.setImageDrawable(AppCompatResources.getDrawable(this,icon));
    }
}