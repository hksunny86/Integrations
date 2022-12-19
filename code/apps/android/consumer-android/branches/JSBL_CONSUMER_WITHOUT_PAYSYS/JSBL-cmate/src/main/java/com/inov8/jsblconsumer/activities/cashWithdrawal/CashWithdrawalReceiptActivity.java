package com.inov8.jsblconsumer.activities.cashWithdrawal;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.TransactionInfoModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class CashWithdrawalReceiptActivity extends BaseCommunicationActivity {

    private ImageView tickIcon;
    private ScrollView list;
    private Button btnOk;
    private RelativeLayout layoutAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_receipt);

        headerImplementation();
        bottomBarImplementation(CashWithdrawalReceiptActivity.this, "");
        checkSoftKeyboardD();
        viewLeft.setVisibility(View.VISIBLE);
        titleImplementation("heading_icon_cash_withdrawal", "Cash Withdrawal", null, this);

        try {
            layoutAmount = (RelativeLayout) findViewById(R.id.layoutAmount);
            layoutAmount.setVisibility(View.GONE);

            btnHome = (AppCompatImageView) findViewById(R.id.btnHome);
            btnHome.setVisibility(View.GONE);

            tickIcon = (ImageView) findViewById(R.id.ivIconTick);
            tickIcon.setVisibility(View.GONE);

            list = (ScrollView) findViewById(R.id.scView);
            list.setVisibility(View.GONE);

            btnOk = (Button) findViewById(R.id.btnOK);
            btnOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    goToMainMenu();
                }
            });

            askMpin(mBundle, CashWithdrawalReceiptActivity.class, true);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void onBackPressed() {
        goToStart();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    CashWithdrawalReceiptActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        try {
            showLoading("Please Wait", "Authenticating...");
            new HttpAsyncTask(CashWithdrawalReceiptActivity.this).execute(Constants.CMD_CASH_WITHDRAWAL + "", getEncryptedMpin());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        CashWithdrawalReceiptActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();

                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                    askMpin(mBundle, CashWithdrawalReceiptActivity.class, true);
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);

//                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        CashWithdrawalReceiptActivity.this, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGeneral.cancel();
//                                if (message != null
//                                        && message.getCode() != null
//                                        && message.getCode().equals(
//                                        Constants.ErrorCodes.INTERNAL)) {
//                                    goToMainMenu();
//                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
//                                    askMpin(mBundle, CashWithdrawalReceiptActivity.class, true);
//                                }
//                            }
//                        }, PopupDialogs.Status.ERROR);
            } else {
                tickIcon.setVisibility(View.VISIBLE);
                list.setVisibility(View.VISIBLE);

                TransactionInfoModel trnModel = TransactionInfoModel.getInstance();
                trnModel.setCashWithdrawalInfo(
                        table.get(XmlConstants.Attributes.TRN_DATE).toString(),
                        table.get(XmlConstants.Attributes.TRN_DATE_FORMAT).toString(),
                        table.get(XmlConstants.Attributes.TRN_TIME_FORMAT).toString(),
                        table.get(XmlConstants.Attributes.TRXID).toString(),
                        table.get(XmlConstants.Attributes.EXPIRY).toString());

                String labels[] = new String[]{"Transaction ID", "Date & Time", "Expiry"};
                String data[] = new String[]{
                        trnModel.getTrxid(),
                        trnModel.getDatef(),
                        trnModel.getExpiry()};

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
            }
        } catch (Exception e) {
            hideLoading();


            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING,
                    CashWithdrawalReceiptActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

//            PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE,
//                    getString(R.string.alertNotification), CashWithdrawalReceiptActivity.this, PopupDialogs.Status.ERROR);
        }
        hideLoading();
    }

    @Override
    public void processNext() {
    }
}
