package com.inov8.jsblconsumer.activities.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.PasswordControllerAccountOpening;

public class BusTicking extends BaseCommunicationActivity {
    private TextView headerText;
    private WebView webViewBusTicking;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_ticking);
//        headerImplementation();
        setUI();
        if (!haveInternet()) {
//            navigateToFirstScreen = true;

            PopupDialogs.createAlertDialog(
                    getString(R.string.internet_connection_problem),
                    getString(R.string.alertNotification), this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, PopupDialogs.Status.ERROR);
            return;
        }

        webViewBusTicking.setWebViewClient(new MyWebViewClient());
        webViewBusTicking.getSettings().setJavaScriptEnabled(false);
        webViewBusTicking.loadUrl("https://bookme.pk/widgets/buses?data=");


    }
    private void setUI() {

//        findViewById(R.id.ivHeaderLogo).setVisibility(View.GONE);
//        headerText = (TextView) findViewById(R.id.headerText);
//        headerText.setVisibility(View.VISIBLE);
//        headerText.setText(getString(R.string.pin_change));
        webViewBusTicking = (WebView) findViewById(R.id.webviewBusTicking);

    }

    @Override
    public void onBackPressed() {
        if (webViewBusTicking.canGoBack())
            webViewBusTicking.goBack();
        else
            super.onBackPressed();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }
    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    if (webViewBusTicking.canGoBack()) {
//                        webViewBusTicking.goBack();
//                    } else {
//                        finish();
//                    }
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void processRequest() {

    }

    @Override
    public void processResponse(HttpResponseModel response) {

    }

    @Override
    public void processNext() {

    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Toast.makeText(BusTicking.this, url, Toast.LENGTH_SHORT).show();
        }
    }
}