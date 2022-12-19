package com.inov8.jsblconsumer.activities.retailPayment.retailpaymentmpass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.activities.LoginActivity;
import com.inov8.jsblconsumer.activities.SplashActivity;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

public class RetailPaymentReceiptActivity extends BaseActivity {
    private TextView lblFirst, lblSecond, lblThird, lblForth, lblAmount;
    private String strAmount, strComments;

    private String merchantId;
    private String amount = "";
    private String amountF = "", strFee = "";
    private String merchantName = "";
    private String merchantLaction = "";
    private TransactionModel transaction;
    private String[] labelsDetails, dataDetails;
    private Button btnOk;


    private ListViewExpanded listAccountDetails, listMerchantDetails,
            listPaymentDetails;


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_receipt);
        fetchIntent();
        headerImplementation();
        bottomBarImplementation(RetailPaymentReceiptActivity.this, "");
        checkSoftKeyboardD();
        setUI();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });
        ivHeaderLog.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerText.setText(getString(R.string.qr_payment));

    }


    private void fetchIntent() {


        merchantId = mBundle.getString(Constants.IntentKeys.MERCHANT_ID);
        merchantName = mBundle.getString(Constants.IntentKeys.MERCHANT_NAME_F);
        merchantLaction = mBundle.getString(Constants.IntentKeys.MERCHANT_LACTION);
        amount = mBundle.getString(Constants.IntentKeys.QR_AMOUNT);
        strFee = mBundle.getString(Constants.IntentKeys.QR_FEE);
        amountF = mBundle.getString(Constants.IntentKeys.QR_AMOUNT_F);
        transaction = (TransactionModel) mBundle.get(Constants.IntentKeys.TRANSACTION_MODEL);

    }

    private void setUI() {

        btnOk=(Button) findViewById(R.id.btnOk);

        lblAmount = (TextView) findViewById(R.id.lblAmount);

        lblAmount.setText(Constants.CURRENCY + " "
                + Utility.getFormatedAmount(transaction.getTxamf()));
        lblAmount.setVisibility(View.VISIBLE);

        lblFirst = (TextView) findViewById(R.id.lblDetails);
        lblFirst.setText(getString(R.string.merchant_details));
        lblFirst.setVisibility(View.VISIBLE);
//
        lblSecond = (TextView) findViewById(R.id.lblSecond);
        lblSecond.setText(getString(R.string.payment_details));
        lblSecond.setVisibility(View.VISIBLE);

//
//        lblThird = (TextView) findViewById(R.id.lblThird);
//        lblThird.setText(getString(R.string.payment_details));
//        lblThird.setVisibility(View.VISIBLE);
//
//        lblForth = (TextView) findViewById(R.id.lblForth);
////        lblForth.setText(getString(R.string.request_details));
////        lblForth.setVisibility(View.VISIBLE);
//
        listMerchantDetails = (ListViewExpanded) findViewById(R.id.listDetails);
        listPaymentDetails = (ListViewExpanded) findViewById(R.id.listSecond);
//        listPaymentDetails = (ListViewExpanded) findViewById(R.id.listThird);
////        listRequestDetail = (ListViewExpanded) findViewById(R.id.listForth);
//        listAccountDetails.setVisibility(View.VISIBLE);
        listMerchantDetails.setVisibility(View.VISIBLE);
        listPaymentDetails.setVisibility(View.VISIBLE);
////        listRequestDetail.setVisibility(View.VISIBLE);
//
//
        try {
            labelsDetails = new String[]{getString(R.string.transaction_type), getString(R.string.amount), getString(R.string.transaction_fee)};

            dataDetails = new String[]{"QR Payment", Constants.CURRENCY + " " + transaction.getTxamf(), Constants.CURRENCY+" 0.0"};
            populateData(listPaymentDetails, labelsDetails, dataDetails);
        } catch (Exception e) {
            AppLogger.e(e);
        }
//
//
//        try {
//            labelsDetails = new String[]{getString(R.string.account_title), getString(R.string.account_number)};
//            dataDetails = new String[]{fromAccountModel.getAccountTitle(), Utility.getMaskedAccountNumber(AESEncryptor.decryptWithAES(Constants.SECRET_KEY, fromAccountModel.getAccountNo()))};
//            populateData(listAccountDetails, labelsDetails, dataDetails);
//        } catch (Exception e) {
//            AppLogger.e(e);
//        }
//
        try {
            labelsDetails = new String[]{getString(R.string.merchant_id), getString(R.string.merchant_name)};
            dataDetails = new String[]{merchantId, merchantName};
            populateData(listMerchantDetails, labelsDetails, dataDetails);
        } catch (Exception e) {
            AppLogger.e(e);
        }


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainMenu();
    }
}