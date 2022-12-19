package com.inov8.jsblconsumer.harToWallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.agentTransfer.TransferInConfirmationActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class HraToWalletInputActivity extends BaseCommunicationActivity {
    private TextView lblField1;
    private EditText input1;
    private Button btnNext;
    private ProductModel product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_input_general);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);

            lblField1 = (TextView) findViewById(R.id.lblField1);
            input1 = (EditText) findViewById(R.id.input1);
            disableCopyPaste(input1);

            titleImplementation(null, product.getName(), null, HraToWalletInputActivity.this);
            bottomBarImplementation(HraToWalletInputActivity.this, "");
            lblField1.setText("Amount");
            lblField1.setVisibility(View.VISIBLE);
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_AMOUNT_LENGTH)});
            input1.setVisibility(View.VISIBLE);

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(input1.getText())) {
                        input1.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    }
                    String str = input1.getText() + "";

                    if (!Utility.isNull(product.getDoValidate())
                            && product.getDoValidate().equals("1")) {
                        if ((Integer.parseInt(input1.getText().toString()) > Double
                                .parseDouble(Utility
                                        .getUnFormattedAmount(product
                                                .getMaxamt())) || Integer
                                .parseInt(input1.getText().toString()) < Double
                                .parseDouble(Utility
                                        .getUnFormattedAmount(product
                                                .getMinamt())))) {
                            input1.setError(AppMessages.ERROR_AMOUNT_INVALID);

                            return;
                        }
                    } else {
                        if (str != null && str.length() > 0) {
                            Double amount = Double.parseDouble(input1.getText()
                                    .toString() + "");
                            if (amount < 1 || amount > Constants.MAX_AMOUNT) {
                                input1.setError(AppMessages.ERROR_AMOUNT_INVALID);

                                return;
                            }
                        }
                    }

                    hideKeyboard(v);

                    mpinProcess(null, null);
                }
            });

            addAutoKeyboardHideFunction();

        } catch (Exception ex) {
            ex.getMessage();
        }
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, HraToWalletInputActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(HraToWalletInputActivity.this).execute(
                Constants.CMD_HRA_TO_WALLET_INFO + "", product.getId(), input1.getText().toString());
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);

                PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
                        HraToWalletInputActivity.this, PopupDialogs.Status.ERROR);

            } else {
                TransactionInfoModel transactionInfo = TransactionInfoModel.getInstance();
                transactionInfo.TransferIn(
                        "",
                        "",
                        table.get(XmlConstants.Attributes.TXAM).toString(),
                        table.get(XmlConstants.Attributes.TXAMF).toString(),
                        table.get(XmlConstants.Attributes.TPAM).toString(),
                        table.get(XmlConstants.Attributes.TPAMF).toString(),
                        table.get(XmlConstants.Attributes.CAMT).toString(),
                        table.get(XmlConstants.Attributes.CAMTF).toString(),
                        table.get(XmlConstants.Attributes.TAMT).toString(),
                        table.get(XmlConstants.Attributes.TAMTF).toString());

                Intent intent = new Intent(HraToWalletInputActivity.this, HraToWalletConfirmationActivity.class);
                intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactionInfo);
                intent.putExtras(mBundle);
                startActivity(intent);
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