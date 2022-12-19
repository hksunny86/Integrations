//package com.inov8.jsblconsumer.activities.bookMe;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.inov8.jsblconsumer.R;
//import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
//import com.inov8.jsblconsumer.model.HttpResponseModel;
//import com.inov8.jsblconsumer.model.MessageModel;
//import com.inov8.jsblconsumer.model.ProductModel;
//import com.inov8.jsblconsumer.model.TransactionInfoModel;
//import com.inov8.jsblconsumer.net.HttpAsyncTask;
//import com.inov8.jsblconsumer.parser.XmlParser;
//import com.inov8.jsblconsumer.ui.components.PopupDialogs;
//import com.inov8.jsblconsumer.util.AppMessages;
//import com.inov8.jsblconsumer.util.ApplicationData;
//import com.inov8.jsblconsumer.util.Constants;
//import com.inov8.jsblconsumer.util.RsaUtil;
//import com.inov8.jsblconsumer.util.XmlConstants;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Hashtable;
//import java.util.List;
//
//public class BusTicketing extends BaseCommunicationActivity {
//    private ProductModel product;
//    private TextView headerText;
//    private WebView webViewBusTicking;
//    private String orderRefId,amount,type,serviceProvider,date,time;
//    private TransactionInfoModel transactionInfo;
//    private RelativeLayout screenWhite;
//    private boolean bookingCall = false;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bus_ticking);
////        headerImplementation();
//        fetchIntents();
//        if (!haveInternet()) {
//            PopupDialogs.createAlertDialog(
//                    getString(R.string.internet_connection_problem),
//                    getString(R.string.alertNotification), this, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            finish();
//                        }
//                    }, PopupDialogs.Status.ERROR);
//            return;
//        }
//        setUI();
//
//
//    }
//    private void setUI() {
//        //loader
//        showLoading("Please Wait", "Loading...");
//
//        // set webview
//        webViewBusTicking = (WebView) findViewById(R.id.webviewBusTicking);
//        webViewBusTicking.setWebViewClient(new MyWebViewClient());
//        webViewBusTicking.getSettings().setJavaScriptEnabled(true);
//        webViewBusTicking.addJavascriptInterface(new MyJavaScriptInterface(this), "JSBookME");
//        screenWhite =  findViewById(R.id.screen_while);
//
//
//        // set user data
//        String name = ApplicationData.firstName + ApplicationData.lastName;
//        String cnic = ApplicationData.cnic;
//        String email = "faisal@inov8.com";
//        String phoneNumber = ApplicationData.mobileNo;
//        String apiKey = Constants.API_KEY_BOOKME;
//        String data = Uri.encode(RsaUtil.encryptedCall("name="+name+"&cnic="+cnic+"&email="+email+"&mobile_no="+phoneNumber+"&api_key="+apiKey));
//        webViewBusTicking.loadUrl(Constants.BOOKME_BASE_URL+"buses?data="+data);
//
//    }
//    private void fetchIntents() {
//        try {
//            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
//        } catch (Exception e) {
//        }
//    }
//
//    private class MyWebViewClient extends WebViewClient {
//
//        public MyWebViewClient() {
//        }
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
////            Log.d("TAG3",url);
//            return true;
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            showLoading("Please Wait", "Loading...");
//            if (url.startsWith("https://bookme.pk/widgets/jsmb/shiftControl?order_ref_id="))
//            {
//                bookingCall = true;
//                webViewBusTicking.setVisibility(View.GONE);
//                screenWhite.setVisibility(View.VISIBLE);
//                return;
//            }
//            super.onPageStarted(view, url, favicon);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            webViewBusTicking.loadUrl("javascript:JSBookME.showJson" + "('<html>'+document.getElementsByTagName('body')[0].innerHTML+'</html>');");
//            hideLoading();
//        }
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (webViewBusTicking.canGoBack())
//            webViewBusTicking.goBack();
//        else
//            super.onBackPressed();
//
//    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
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
//    @Override
//    public void processRequest() {
//        if (!haveInternet()) {
//            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
//                    BusTicketing.this, getString(R.string.ok), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialogGeneral.dismiss();
//                        }
//                    }, false, PopupDialogs.Status.ERROR, false, null);
//
//            return;
//        }
//
//        showLoading("Please Wait", "Processing...");
//        new HttpAsyncTask(BusTicketing.this).execute(Constants.CMD_BOOKME_INQUIRY + "", product.getId(),ApplicationData.mobileNo,amount,"0",orderRefId,type,serviceProvider,"0.0","0.0","0.0","0.0","0.0");
//    }
//
//    @Override
//    public void processResponse(HttpResponseModel response) {
//        try {
//            XmlParser xmlParser = new XmlParser();
//            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
//                    .getXmlResponse());
//            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
//                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
//                final MessageModel message = (MessageModel) list.get(0);
//
//                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        BusTicketing.this, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGeneral.cancel();
//                                if (message != null
//                                        && message.getCode() != null
//                                        && message.getCode().equals(
//                                        Constants.ErrorCodes.INTERNAL)) {
//                                    goToMainMenu();
//                                }
//
//                            }
//                        }, PopupDialogs.Status.ERROR);
//            } else {
//
//                    TransactionInfoModel transactoinInfo = TransactionInfoModel.getInstance();
//                    transactoinInfo.BookmePaymentInquiry(
//                            table.get(XmlConstants.Attributes.CMOB).toString(), table.get(
//                            XmlConstants.Attributes.PNAME).toString(), table.get(
//                            XmlConstants.Attributes.BAMT).toString(), table.get(
//                            XmlConstants.Attributes.BAMTF).toString(), table.get(
//                            XmlConstants.Attributes.TPAM).toString(), table.get(
//                            XmlConstants.Attributes.CAMT).toString(), table.get(
//                            XmlConstants.Attributes.TAMT).toString());
//
////                    transactoinInfo.setTpamf(table.get(XmlConstants.Attributes.TPAMF).toString());
////
//
//                Intent intent = new Intent(BusTicketing.this,BookMePaymentConfirmationActivity.class);
//                     intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactoinInfo);
//                     intent.putExtra(Constants.IntentKeys.BOOKME_ORDER_REF_ID, orderRefId);
//                     intent.putExtra(Constants.IntentKeys.BOOKME_SERVICE_PROVIDER, serviceProvider);
//                     intent.putExtra(Constants.IntentKeys.BOOKME_DATE, date);
//                     intent.putExtra(Constants.IntentKeys.BOOKME_TYPE, type);
//                     intent.putExtra(Constants.IntentKeys.BOOKME_TIME, time);
//                     intent.putExtras(mBundle);
//                     startActivity(intent);
//                     finish();
//                screenWhite.setVisibility(View.GONE);
//
//            }
//        } catch (Exception e) {
//            hideLoading();
//            e.printStackTrace();
//        }
//        hideLoading();
//    }
//
//    @Override
//    public void processNext() {
//
//    }
//
//
//
//    class MyJavaScriptInterface {
//
//        private Context ctx;
//        MyJavaScriptInterface(Context ctx) {
//            this.ctx = ctx;
//        }
//
//        @JavascriptInterface
//        public void showJson(String json) {
//            json = json.replaceAll("<html>","");
//            json = json.replaceAll("</html>","");
//            try {
//                JSONObject jObject2 = new JSONObject(json);
//                 orderRefId = jObject2.getString("ORDER_REF_ID");
//                 type = jObject2.getString("TYPE");
//                 serviceProvider = jObject2.getString("SERVICE_PROVIDER");
//                JSONObject jsonObjectDetails = jObject2.getJSONObject("ORDER_DETAILS");
//                 amount = jsonObjectDetails.getString("amount");
//                 date = jsonObjectDetails.getString("dep_date");
//                 time = jsonObjectDetails.getString("dep_time");
//                if(bookingCall)
//                processRequest();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                screenWhite.setVisibility(View.GONE);
//                webViewBusTicking.setVisibility(View.VISIBLE);
//            }
//        }
//
//    }
//
//}