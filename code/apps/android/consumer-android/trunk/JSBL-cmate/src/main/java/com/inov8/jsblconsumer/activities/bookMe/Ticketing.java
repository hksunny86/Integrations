package com.inov8.jsblconsumer.activities.bookMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.RsaUtil;
import com.inov8.jsblconsumer.util.XmlConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.List;

public class Ticketing extends BaseCommunicationActivity {

    private String ticketingType;
    private RelativeLayout screenWhite;
    private ProductModel product;
    private WebView webViewTicketing;
    private String orderRefId,amount,type,serviceProvider,date,time,eventVenue,hotelName,cusName,cusEmail,cusCnic,cusPhone;
    private String bookMeBaseFare,bookMeAprAmount,bookMeDisAmount,bookMeTax,bookMeFee,flightTo,eventName = "";
    private boolean bookingCall = false;
    private String data;
    private LinearLayout linearLayoutBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);
        fetchIntents();
        if (!haveInternet()) {
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
        setUI();
        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webViewTicketing.getUrl().startsWith(Constants.BOOKME_BASE_URL+ticketingType+"?data=")){
                    finish();
                }
            }
        });
    }
    private void setUI() {
        //loader
        showLoading("Please Wait", "Loading...");

        // set webview
        webViewTicketing = (WebView) findViewById(R.id.webViewTicketing);
        linearLayoutBack =  findViewById(R.id.linearBack);
        webViewTicketing.setWebViewClient(new MyWebViewClient());
        webViewTicketing.getSettings().setJavaScriptEnabled(false);
        webViewTicketing.addJavascriptInterface(new MyJavaScriptInterface(this), "JSBookME");
        screenWhite =  findViewById(R.id.screen_while);


        // set user data
        String name = ApplicationData.firstName + ApplicationData.lastName;
        String cnic = ApplicationData.cnic;
        String email = "";
        String phoneNumber = ApplicationData.mobileNo;
        String apiKey = Constants.API_KEY_BOOKME;
         data = Uri.encode(RsaUtil.encryptedCall("name="+name+"&cnic="+cnic+"&email="+email+"&mobile_no="+phoneNumber+"&api_key="+apiKey));
        webViewTicketing.loadUrl(Constants.BOOKME_BASE_URL+ticketingType+"?data="+data);

    }
    private void fetchIntents() {
        ticketingType =  mBundle.getString(Constants.IntentKeys.BOOKME_TICKETING_TYPE);
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        } catch (Exception e) {
        }
    }
    private class MyWebViewClient extends WebViewClient {

        public MyWebViewClient() {
            showLoading("Please Wait", "Loading...");
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
//            Log.d("TAG3",url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showLoading("Please Wait", "Loading...");
            if (url.startsWith("https://bookme.pk/widgets/jsmb/shiftControl?order_ref_id="))
            {
                showLoading("Please Wait", "Processing...");
                bookingCall = true;
                webViewTicketing.setVisibility(View.GONE);
                screenWhite.setVisibility(View.VISIBLE);
                return;
            }
            if(webViewTicketing.getUrl().startsWith(Constants.BOOKME_BASE_URL+ticketingType+"?data=")){
                linearLayoutBack.setVisibility(View.VISIBLE);
            }else {
                linearLayoutBack.setVisibility(View.GONE);
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            webViewTicketing.loadUrl("javascript:JSBookME.showJson" + "('<html>'+document.getElementsByTagName('body')[0].innerHTML+'</html>');");
            if (url.startsWith("https://bookme.pk/widgets/jsmb/shiftControl?order_ref_id=")) {
              return;
            }
            hideLoading();
        }

    }

    @Override
    public void onBackPressed() {
        if (webViewTicketing.canGoBack())
            webViewTicketing.goBack();
        else
            super.onBackPressed();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webViewTicketing.canGoBack()) {
                        webViewTicketing.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    class MyJavaScriptInterface {

        private Context ctx;
        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showJson(String json) {
//            Toast.makeText(ctx, json, Toast.LENGTH_SHORT).show();
            json = json.replaceAll("<html>","");
            json = json.replaceAll("</html>","");
            try {
                JSONObject jObject2 = new JSONObject(json);
                orderRefId = jObject2.getString("ORDER_REF_ID");
                type = jObject2.getString("TYPE");
                serviceProvider = jObject2.getString("SERVICE_PROVIDER");
                amount = jObject2.getString("AMOUNT");
                JSONObject jsonObjectDetails = jObject2.getJSONObject("ORDER_DETAILS");
                cusName = jsonObjectDetails.getString("name");
                cusEmail = jsonObjectDetails.getString("email");
                if(ticketingType.equals("buses")){
                    type = "bus";
                    date = jsonObjectDetails.getString("date") +" "+jsonObjectDetails.getString("dep_time");
                    time = jsonObjectDetails.getJSONObject("departure_city").getString("city_name");
                    flightTo = jsonObjectDetails.getJSONObject("arrival_city").getString("city_name");
                    cusPhone = jsonObjectDetails.getString("contact_no");
                    cusCnic = jsonObjectDetails.getString("cnic");
                } else if(ticketingType.equals("events")) {
                    JSONObject jsonObjectEventDetails = jsonObjectDetails.getJSONObject("event_name");
                    date = jsonObjectDetails.getString("event_date");
                    time = jsonObjectEventDetails.getString("duration");
                    eventVenue = jsonObjectEventDetails.getString("venue");
                    eventName = jsonObjectEventDetails.getString("name");
                    cusPhone = jsonObjectDetails.getString("contact_no");
                    cusCnic = jsonObjectDetails.getString("cnic");
                }else if(ticketingType.equals("flights")){
                    date = jsonObjectDetails.getString("initial_departure_datetime");
                    time = jsonObjectDetails.getString("flight_from");
                    flightTo = jsonObjectDetails.getString("flight_to");
                    bookMeAprAmount = jsonObjectDetails.getString("approx_total_price");
                    bookMeBaseFare = jsonObjectDetails.getString("approx_base_price");
                    bookMeDisAmount = jsonObjectDetails.getString("ep_discount");
                    bookMeFee = jsonObjectDetails.getString("fees");
                    bookMeTax = jsonObjectDetails.getString("taxes");
                    cusPhone = jsonObjectDetails.getString("phone_number");
                    cusCnic = jsonObjectDetails.getString("cnic");
                }else if(ticketingType.equals("hotels")){
                    date = jsonObjectDetails.getString("checkin_datetime");
                    time = jsonObjectDetails.getString("checkout_datetime");
                    hotelName = jsonObjectDetails.getString("hotel_name");
                    cusPhone = jsonObjectDetails.getString("phone_number");
                    cusCnic = jsonObjectDetails.getString("cnic");

                }else if(ticketingType.equals("movies")){
                    date = jsonObjectDetails.getString("show_time");
                    JSONObject jsonObjectCinemaDetails = jsonObjectDetails.getJSONObject("cinema_movie");
                    time = jsonObjectCinemaDetails.getString("title");
                    cusPhone = jsonObjectDetails.getString("contact_no");
                    cusCnic = jsonObjectDetails.getString("cnic");
                }
                if(bookingCall)
                processRequest();
            } catch (JSONException e) {
                hideLoading();
                e.printStackTrace();
                screenWhite.setVisibility(View.GONE);
                webViewTicketing.setVisibility(View.VISIBLE);
                webViewTicketing.loadUrl(Constants.BOOKME_BASE_URL+ticketingType+"?data="+data);
            }
        }

    }


    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    Ticketing.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(Ticketing.this).execute(Constants.CMD_BOOKME_INQUIRY + "", product.getId(),ApplicationData.mobileNo,amount,"0",orderRefId,type,serviceProvider,bookMeBaseFare,bookMeAprAmount,bookMeDisAmount,bookMeTax,bookMeFee,cusName,cusPhone,cusCnic,cusEmail);
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
                    .getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);

                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
                        Ticketing.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                }else {
                                    finish();
                                }

                            }
                        }, PopupDialogs.Status.ERROR);
            } else {

                TransactionInfoModel transactoinInfo = TransactionInfoModel.getInstance();
                transactoinInfo.BookmePaymentInquiry(
                        table.get(XmlConstants.Attributes.CMOB).toString(), table.get(
                                XmlConstants.Attributes.PNAME).toString(), table.get(
                                XmlConstants.Attributes.BAMT).toString(), table.get(
                                XmlConstants.Attributes.BAMTF).toString(), table.get(
                                XmlConstants.Attributes.TPAM).toString(), table.get(
                                XmlConstants.Attributes.CAMT).toString(), table.get(
                                XmlConstants.Attributes.TAMT).toString());

                Intent intent = new Intent(Ticketing.this,BookMePaymentConfirmationActivity.class);
                intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactoinInfo);
                intent.putExtra(Constants.IntentKeys.BOOKME_ORDER_REF_ID, orderRefId);
                intent.putExtra(Constants.IntentKeys.BOOKME_SERVICE_PROVIDER, serviceProvider);
                intent.putExtra(Constants.IntentKeys.BOOKME_DATE, date);
                intent.putExtra(Constants.IntentKeys.BOOKME_TYPE, type);
                intent.putExtra(Constants.IntentKeys.BOOKME_TIME, time);
                intent.putExtra(Constants.IntentKeys.BOOKME_EVENT_VENUE, eventVenue);
                intent.putExtra(Constants.IntentKeys.BOOKME_EVENT_NAME, eventName);
                intent.putExtra(Constants.IntentKeys.BOOKME_APR_AMOUNT, bookMeAprAmount);
                intent.putExtra(Constants.IntentKeys.BOOKME_BASE_FARE, bookMeBaseFare);
                intent.putExtra(Constants.IntentKeys.BOOKME_DIS_AMOUNT, bookMeDisAmount);
                intent.putExtra(Constants.IntentKeys.BOOKME_TAX, bookMeTax);
                intent.putExtra(Constants.IntentKeys.BOOKME_FEE, bookMeFee);
                intent.putExtra(Constants.IntentKeys.BOOKME_HOTEL_NAME, hotelName);
                intent.putExtra(Constants.IntentKeys.BOOKME_CUSTOMER_PHONE, cusPhone);
                intent.putExtra(Constants.IntentKeys.BOOKME_CUSTOMER_CNIC, cusCnic);
                intent.putExtra(Constants.IntentKeys.BOOKME_CUSTOMER_EMAIL, cusEmail);
                intent.putExtra(Constants.IntentKeys.BOOKME_CUSTOMER_NAME, cusName);
                intent.putExtra(Constants.IntentKeys.BOOKME_FLIGHT_TO, flightTo);
                intent.putExtras(mBundle);
                startActivity(intent);
                finish();
                screenWhite.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            hideLoading();
            screenWhite.setVisibility(View.GONE);
            dialogGeneral = PopupDialogs.createAlertDialog(getResources().getString(R.string.general_error), getString(R.string.alertNotification),
                    Ticketing.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.cancel();
                                goToMainMenu();
                        }
                    }, PopupDialogs.Status.ERROR);
            e.printStackTrace();
        }
        hideLoading();
    }

    @Override
    public void processNext() {

    }
}