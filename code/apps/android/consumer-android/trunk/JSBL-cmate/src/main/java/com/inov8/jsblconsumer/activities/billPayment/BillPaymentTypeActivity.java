package com.inov8.jsblconsumer.activities.billPayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.R;
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
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class BillPaymentTypeActivity extends BaseCommunicationActivity {
    private TextView lblHeading;
    private String consumer, mobileNumber;
    private byte paymentType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_list_view);

        try {
            fetchintents();


//			lblHeading = (TextView) findViewById(R.id.lblHeading);
//			lblHeading.setText("Payment Type");
//
//			int[] icons = new int[] { R.drawable.list_icon_arrow,
//					R.drawable.list_icon_arrow };
//
//			String[] values = new String[] { "Pay By Account", "Pay By Cash" };
//
//			List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
//
//			for (int i = 0; i < values.length; i++) {
//				HashMap<String, String> hm = new HashMap<String, String>();
//				hm.put("icon", Integer.toString(icons[i]));
//				hm.put("txtOption", values[i]);
//				aList.add(hm);
//			}
//
//			final String[] from = { "icon", "txtOption" };
//			final int[] to = { R.id.icon, R.id.txtOption };
//			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
//					R.layout.list_item_with_icon, from, to);
//
//			ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.optionsList);
//			listView.setAdapter(adapter);
//
//			@SuppressWarnings("deprecation")
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, (int) TypedValue.applyDimension(
//							TypedValue.COMPLEX_UNIT_DIP, mListViewOptionItemHeight * (values.length),
//							getResources().getDisplayMetrics()));
//
//			params.leftMargin = 10;
//			params.rightMargin = 10;
//			params.topMargin = 20;
//			params.addRule(RelativeLayout.BELOW, R.id.layoutHeading);
//			listView.setLayoutParams(params);
//
//			listView.setOnItemClickListener(new OnItemClickListener() {
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//
            //	paymentType = (byte) position;
            paymentType = 1;
            mpinProcess(null, null);

        } catch (Exception ex) {
            ex.getMessage();
        }

        headerImplementation();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        processRequest();
    }

    private ProductModel mProduct;

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, BillPaymentTypeActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(BillPaymentTypeActivity.this).execute(
                Constants.CMD_BILL_INQUIRY + "", mProduct.getId(),
                ApplicationData.customerMobileNumber, mobileNumber, consumer, paymentType + "",
                ApplicationData.bankId);
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
                        BillPaymentTypeActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                }

                            }
                        }, PopupDialogs.Status.ERROR);
            } else {
                if (table.get(XmlConstants.Attributes.BPAID).toString().equals("1")) {
                    PopupDialogs.createAlertDialog(AppMessages.BILL_ALREADY_PAID, getString(R.string.alertNotification),
                            BillPaymentTypeActivity.this, PopupDialogs.Status.ERROR);

                } else {
                    TransactionInfoModel transactoinInfo = TransactionInfoModel.getInstance();
                    transactoinInfo.BillInquiry(table.get(
                            XmlConstants.Attributes.CMOB).toString(), table.get(
                            XmlConstants.Attributes.CNIC).toString(), table.get(
                            XmlConstants.Attributes.PNAME).toString(), table.get(
                            XmlConstants.Attributes.CONSUMER).toString(), table.get(
                            XmlConstants.Attributes.DUEDATE).toString(), table.get(
                            XmlConstants.Attributes.DUEDATEF).toString(), table.get(
                            XmlConstants.Attributes.BAMT).toString(), table.get(
                            XmlConstants.Attributes.BAMTF).toString(), table.get(
                            XmlConstants.Attributes.LBAMT).toString(), table.get(
                            XmlConstants.Attributes.LBAMTF).toString(), table.get(
                            XmlConstants.Attributes.ISOVERDUE).toString(), table.get(
                            XmlConstants.Attributes.BPAID).toString());

                    transactoinInfo.setTpamf(table.get(XmlConstants.Attributes.TPAMF).toString());

                    if (mProduct.getAmtRequired().equals("1")) {
                        Intent intent = new Intent(getApplicationContext(),
                                BillPaymentConfirmationActivity.class);
                        intent.putExtra(Constants.IntentKeys.PAYMENT_TYPE,
                                paymentType);
                        intent.putExtra(
                                Constants.IntentKeys.TRANSACTION_INFO_MODEL,
                                transactoinInfo);

                        if (transactoinInfo.getCnic().equals("-1")) {// customer registration
                            intent.putExtra(
                                    Constants.IntentKeys.SHOW_REGISTRATION_POPUP, true);
                        }

                        intent.putExtras(mBundle);
                        this.finish();
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(),
                                BillPaymentConfirmationActivity.class);
                        intent.putExtra(Constants.IntentKeys.PAYMENT_TYPE,
                                paymentType);
                        intent.putExtra(
                                Constants.IntentKeys.TRANSACTION_INFO_MODEL,
                                transactoinInfo);

                        if (transactoinInfo.getIsoverdue().equals("1")) {// due date check
                            intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT,
                                    transactoinInfo.getLbamt());
                        } else {
                            intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT,
                                    transactoinInfo.getBamt());
                        }

                        if (transactoinInfo.getCnic().equals("-1")) {// customer registration
                            intent.putExtra(
                                    Constants.IntentKeys.SHOW_REGISTRATION_POPUP, true);
                        }
                        intent.putExtras(mBundle);
                        this.finish();
                        startActivity(intent);
                    }
                }
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
        hideLoading();
    }

    @Override
    public void processNext() {
    }

    private void fetchintents() {
        mProduct = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        consumer = mBundle.getString("CONSUMER_NO");
        mobileNumber = mBundle.getString("MOBILE_NO");
    }
}