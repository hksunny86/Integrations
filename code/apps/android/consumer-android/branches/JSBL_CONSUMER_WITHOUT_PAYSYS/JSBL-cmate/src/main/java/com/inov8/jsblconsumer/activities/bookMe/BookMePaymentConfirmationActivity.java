package com.inov8.jsblconsumer.activities.bookMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.TransactionReceiptActivity;
import com.inov8.jsblconsumer.activities.retailPayment.RetailPaymentConfirmationActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class BookMePaymentConfirmationActivity extends BaseCommunicationActivity {
    private String orderRedId, type, mobileNumber, amount, totalAmount, merchant;
    private String charges, bookMeServiceProvider, bookMeDate, bookMeTime, eventVenue,bookHotelName,bookMeCusPhone,bookMeCusEmail,bookMeCusCnic,bookMeCusName;
    private Button btnNext, btnCancel;
    private TextView lblAlert;
    private TransactionInfoModel transactionInfo;
    private ProductModel product;
    private Byte flowId;
    private String labels[], data[];
    private String bookMeBaseFare, bookMeAprAmount, bookMeDisAmount, bookMeTax, bookMeFee,flightTo,eventName = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_me_confirmation);
        try {
            fetchIntents();
            headerImplementation();
            titleImplementation(null, transactionInfo.getPname(), getString(R.string.confirm_transaction), this);

            lblAlert = (TextView) findViewById(R.id.lblAlert);
            lblAlert.setText("Please verify details.");

            if (flowId == Constants.FLOW_ID_BOOKME_AIR) {
                labels = new String[]{"Order Ref Id", "Mobile No.", "CNIC No.","Bookme Mobile No.", "Bookme CNIC No.", "Service Provider", "Departure Date & Time", "Departure","Arrival",  "Amount", "Charges", "Total Amount"};
                data = new String[]{orderRedId, ApplicationData.mobileNo, ApplicationData.cnic, bookMeCusPhone,bookMeCusCnic,bookMeServiceProvider, bookMeDate, bookMeTime,flightTo, Constants.CURRENCY + " " + transactionInfo.getBamt(),
                        Constants.CURRENCY + " " + transactionInfo.getTpam(), Constants.CURRENCY + " " + transactionInfo.getTamt()};

            } else if (flowId == Constants.FLOW_ID_BOOKME_CINEMA) {
                labels = new String[]{"Order Ref Id", "Mobile No.", "CNIC No.","Bookme Mobile No.", "Bookme CNIC No.", "Service Provider", "Date", "Title", "Amount", "Charges", "Total Amount"};
                data = new String[]{orderRedId, ApplicationData.mobileNo, ApplicationData.cnic,  bookMeCusPhone,bookMeCusCnic,bookMeServiceProvider, bookMeDate, bookMeTime, Constants.CURRENCY + " " + transactionInfo.getBamt(),
                        Constants.CURRENCY + " " + transactionInfo.getTpam(), Constants.CURRENCY + " " + transactionInfo.getTamt()};

            } else if (flowId == Constants.FLOW_ID_BOOKME_EVENT) {
                labels = new String[]{"Order Ref Id", "Mobile No.", "CNIC No.","Bookme Mobile No.", "Bookme CNIC No.",  "Service Provider","Event Name", "Event Date", "Event Duration", "Event Venue", "Amount", "Charges", "Total Amount"};
                data = new String[]{orderRedId,  ApplicationData.mobileNo, ApplicationData.cnic, bookMeCusPhone,bookMeCusCnic,bookMeServiceProvider, eventName,bookMeDate, bookMeTime, eventVenue, Constants.CURRENCY + " " + transactionInfo.getBamt(),
                        Constants.CURRENCY + " " + transactionInfo.getTpam(), Constants.CURRENCY + " " + transactionInfo.getTamt()};

            } else if (flowId == Constants.FLOW_ID_BOOKME_HOTEL) {
                labels = new String[]{"Order Ref Id", "Mobile No.", "CNIC No.","Bookme Mobile No.","Bookme CNIC No.", "Service Provider", "CheckIn Date & Time", "CheckOut Date & Time", "Hotel Name", "Amount", "Charges", "Total Amount"};
                data = new String[]{orderRedId, ApplicationData.mobileNo, ApplicationData.cnic,  bookMeCusPhone,bookMeCusCnic,bookMeServiceProvider, bookMeDate, bookMeTime, bookHotelName, Constants.CURRENCY + " " + transactionInfo.getBamt(),
                        Constants.CURRENCY + " " + transactionInfo.getTpam(), Constants.CURRENCY + " " + transactionInfo.getTamt()};

            } else {
                labels = new String[]{"Order Ref Id", "Mobile No.", "CNIC No.","Bookme Mobile No.", "Bookme CNIC No.", "Service Provider", "Departure Date & Time", "Departure","Arrival", "Amount", "Charges", "Total Amount"};
                data = new String[]{orderRedId, ApplicationData.mobileNo, ApplicationData.cnic, bookMeCusPhone,bookMeCusCnic,bookMeServiceProvider, bookMeDate, bookMeTime,flightTo, Constants.CURRENCY + " " + transactionInfo.getBamt(),
                        Constants.CURRENCY + " " + transactionInfo.getTpam(), Constants.CURRENCY + " " + transactionInfo.getTamt()};
            }
            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < data.length; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("label", labels[i]);
                hm.put("data", data[i]);
                aList.add(hm);
            }

            String[] from = {"label", "data"};
            int[] to = {R.id.txtLabel, R.id.txtData};
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_item_with_data, from, to);
            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);
            listView.setAdapter(adapter);


            Utility.getListViewSize(listView, this, mListItemHieght);

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // mBundle.putString("MOBILE", mobile_no);
                    // mBundle.putString("TOTAL_AMOUNT", total_amount);
//                     mBundle.putString("AMOUNT", "377.00");
//                     mBundle.putString("CHARGES", "9.0" + "");
//
                    askMpin(mBundle, BookMePaymentReceiptActivity.class, false);
//                    Intent intent = new Intent(BookMePaymentConfirmationActivity.this,BookMePaymentReceiptActivity.class);
//                    startActivity(intent);
                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.CANCEL_TRANSACTION, AppMessages.ALERT_HEADING,
                            BookMePaymentConfirmationActivity.this, getString(R.string.yes), getString(R.string.no), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogGeneral.dismiss();
                                    goToMainMenu();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();


                                }
                            }, false, PopupDialogs.Status.ALERT, false);

                }
            });

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void fetchIntents() {
        orderRedId = mBundle.getString(Constants.IntentKeys.BOOKME_ORDER_REF_ID);
        type = mBundle.getString(Constants.IntentKeys.BOOKME_TYPE);
        bookMeServiceProvider = mBundle.getString(Constants.IntentKeys.BOOKME_SERVICE_PROVIDER);
        bookMeDate = mBundle.getString(Constants.IntentKeys.BOOKME_DATE);
        bookMeTime = mBundle.getString(Constants.IntentKeys.BOOKME_TIME);
        eventVenue = mBundle.getString(Constants.IntentKeys.BOOKME_EVENT_VENUE);
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
        bookMeAprAmount = mBundle.getString(Constants.IntentKeys.BOOKME_APR_AMOUNT);
        bookMeBaseFare = mBundle.getString(Constants.IntentKeys.BOOKME_BASE_FARE);
        bookMeDisAmount = mBundle.getString(Constants.IntentKeys.BOOKME_DIS_AMOUNT);
        bookMeTax = mBundle.getString(Constants.IntentKeys.BOOKME_TAX);
        bookMeFee = mBundle.getString(Constants.IntentKeys.BOOKME_FEE);
        bookHotelName = mBundle.getString(Constants.IntentKeys.BOOKME_HOTEL_NAME);
        bookMeCusPhone = mBundle.getString(Constants.IntentKeys.BOOKME_CUSTOMER_PHONE);
        bookMeCusEmail = mBundle.getString(Constants.IntentKeys.BOOKME_CUSTOMER_EMAIL);
        bookMeCusCnic = mBundle.getString(Constants.IntentKeys.BOOKME_CUSTOMER_CNIC);
        bookMeCusName = mBundle.getString(Constants.IntentKeys.BOOKME_CUSTOMER_NAME);
        flightTo = mBundle.getString(Constants.IntentKeys.BOOKME_FLIGHT_TO);
        eventName = mBundle.getString(Constants.IntentKeys.BOOKME_EVENT_NAME);
        transactionInfo = (TransactionInfoModel) mBundle.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        } catch (Exception e) {
        }
        if (flowId != Constants.FLOW_ID_BOOKME_AIR) {
            bookMeBaseFare = "0.0";
            bookMeAprAmount = "0.0";
            bookMeDisAmount = "0.0";
            bookMeTax = "0.0";
            bookMeFee = "0.0";
        }

    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }


    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, BookMePaymentConfirmationActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(BookMePaymentConfirmationActivity.this).execute(Constants.CMD_BOOKME_PAYMENT + "", getEncryptedMpin(), product.getId(), ApplicationData.mobileNo, transactionInfo.getBamt(), "0", orderRedId, type, transactionInfo.getTpam(), transactionInfo.getTamt(), transactionInfo.getCamt(), bookMeServiceProvider, bookMeBaseFare, bookMeAprAmount, bookMeDisAmount, bookMeTax, bookMeFee,bookMeCusName,bookMeCusPhone,bookMeCusCnic,bookMeCusEmail);

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
                        BookMePaymentConfirmationActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                    askMpin(mBundle, TransactionReceiptActivity.class, false);
                                }

                            }
                        }, PopupDialogs.Status.ERROR);
            } else {
                if (table.containsKey(Constants.KEY_LIST_TRANS)) {
                    @SuppressWarnings("unchecked")
                    List<TransactionModel> list = (List<TransactionModel>) table.get(Constants.KEY_LIST_TRANS);
                    TransactionModel transaction = list.get(0);
                    Log.d("amob", "amob: " + transaction.getAmob());
                    Intent intent = new Intent(getApplicationContext(), BookMePaymentReceiptActivity.class);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL, transaction);
                    intent.putExtra(Constants.IntentKeys.BOOKME_ORDER_REF_ID, orderRedId);
                    intent.putExtra(Constants.IntentKeys.BOOKME_SERVICE_PROVIDER, bookMeServiceProvider);
                    intent.putExtra(Constants.IntentKeys.BOOKME_DATE, bookMeDate);
                    intent.putExtra(Constants.IntentKeys.BOOKME_TYPE, type);
                    intent.putExtra(Constants.IntentKeys.BOOKME_TIME, bookMeTime);
                    intent.putExtra(Constants.IntentKeys.BOOKME_HOTEL_NAME, bookHotelName);
                    intent.putExtra(Constants.IntentKeys.BOOKME_CUSTOMER_PHONE, bookMeCusPhone);
                    intent.putExtra(Constants.IntentKeys.BOOKME_CUSTOMER_CNIC, bookMeCusCnic);
                    intent.putExtra(Constants.IntentKeys.BOOKME_CUSTOMER_EMAIL, bookMeCusEmail);
                    intent.putExtra(Constants.IntentKeys.BOOKME_CUSTOMER_NAME, bookMeCusName);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
        hideLoading();
    }

    @Override
    public void processNext() {

    }
}