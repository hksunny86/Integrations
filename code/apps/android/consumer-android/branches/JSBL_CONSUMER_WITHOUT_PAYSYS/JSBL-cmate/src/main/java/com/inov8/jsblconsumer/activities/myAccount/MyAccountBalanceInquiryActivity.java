package com.inov8.jsblconsumer.activities.myAccount;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.TransactionReceiptActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.atmPinChange.AtmPinChangeInputActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class MyAccountBalanceInquiryActivity extends BaseCommunicationActivity {
    RelativeLayout layout;
    ProductModel mProduct;
    private TextView lblHeading, lblBalance, lblDate;
    private Button btnOk;
    private String acctType = "";
    private String isHRA="";
    private TextView lblsubheading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_balance_inquiry);

        try {
            fetchIntents();
            headerImplementation();
            bottomBarImplementation(MyAccountBalanceInquiryActivity.this, "");
            checkSoftKeyboardD();
            titleImplementation(null, "Check Balance", mProduct.getName(), this);

            layout = (RelativeLayout) findViewById(R.id.data_layout);
//            lblsubheading = (TextView) findViewById(R.id.lblSubHeading1);
//            lblsubheading.setText("(" + mProduct.getName() + ")");
            lblBalance = (TextView) findViewById(R.id.lblcurrentbalance);
            lblDate = (TextView) findViewById(R.id.lbldate);
            btnOk = (Button) findViewById(R.id.btnok);

            if (mProduct.getId().equals(Constants.PRODUCT_ID_BLB_BALANCE_INQUIRY)) {
                acctType = "1";
            } else if (mProduct.getId().equals(Constants.PRODUCT_ID_HRA_BALANCE_INQUIRY)) {
                isHRA="1";
            }

            //Todo
            processRequest();
//            askMpin(mBundle, TransactionReceiptActivity.class, true);

            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    goToMainMenu();
                }
            });

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void fetchIntents() {
        mProduct = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    MyAccountBalanceInquiryActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(MyAccountBalanceInquiryActivity.this).execute(
                Constants.CMD_CHECK_BALANCE + "", getEncryptedMpin(),
                acctType, ApplicationData.userId, ApplicationData.accountId,isHRA);
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);




                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        MyAccountBalanceInquiryActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.cancel();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
//                                    askMpin(mBundle, TransactionReceiptActivity.class, false);
                                    goToMainMenu();
                                }
                                else {
//                                    assert message.getCode() != null;
                                    assert message.getCode() != null;
                                    if(message.getCode().equals("9033")){
                                        finish();
                                    }
                                    else  if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                        askMpin(mBundle, TransactionReceiptActivity.class, true);
                                    }
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);


//                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        MyAccountBalanceInquiryActivity.this, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGeneral.cancel();
//                                if (message != null
//                                        && message.getCode() != null
//                                        && message.getCode().equals(
//                                        Constants.ErrorCodes.INTERNAL)) {
////                                    askMpin(mBundle, TransactionReceiptActivity.class, false);
//                                    goToMainMenu();
//                                }
//                                else  if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
//                                    askMpin(mBundle, TransactionReceiptActivity.class, true);
//                                }
////                                finish();
//                            }
//                        }, PopupDialogs.Status.ERROR);
            } else {
                layout.setVisibility(View.VISIBLE);
                lblBalance.setText(Utility.getFormattedCurrency(Utility.getFormatedAmount(table.get(XmlConstants.Attributes.BALF).toString())));
                lblDate.setText(table.get(XmlConstants.Attributes.DATEF).toString());
                if (acctType.equals("1")) {
                    ApplicationData.formattedBalance = table.get(XmlConstants.Attributes.BALF).toString();
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
