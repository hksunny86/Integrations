package com.inov8.jsblconsumer.activities.myAccount;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
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

public class MyAccountMyLimitActivity extends BaseCommunicationActivity {

    LinearLayout list;
    ListViewExpanded listView;
    Button ok;
    String[] limit, debit, credit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_my_limit);

        headerImplementation();
        bottomBarImplementation(MyAccountMyLimitActivity.this, "");
        checkSoftKeyboardD();
        list = (LinearLayout) findViewById(R.id.ll_list);
        listView = (ListViewExpanded) findViewById(R.id.dataList);
        ok = (Button) findViewById(R.id.btnOK);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
                finish();
            }
        });

        processRequest();
    }


    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    MyAccountMyLimitActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");
        new HttpAsyncTask(MyAccountMyLimitActivity.this).execute(Constants.CMD_MY_LIMITS + "", getEncryptedMpin());
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
                        MyAccountMyLimitActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                                if (message != null && message.getCode() != null) {
                                    goToMainMenu();
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);


            } else {

                list.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                ok.setVisibility(View.VISIBLE);

                limit = new String[]{"Daily", "Monthly", "Yearly"};
                debit = new String[]{
                        table.get(XmlConstants.Attributes.DDEBIT).toString(),
                        table.get(XmlConstants.Attributes.MDEBIT).toString(),
                        table.get(XmlConstants.Attributes.YDEBIT).toString()};
                credit = new String[]{
                       table.get(XmlConstants.Attributes.DCREDIT).toString(),
                        table.get(XmlConstants.Attributes.MCREDIT).toString(),
                        table.get(XmlConstants.Attributes.YCREDIT).toString()};

                populateReceipt(listView, limit, debit, credit);
                titleImplementation(null, "My Limits", null, this);
            }
            hideLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processNext() {

    }

    private void populateReceipt(ListViewExpanded listView, String[] limit, String[] debit, String[] credit) {

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < limit.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("limit", limit[i]);
            hm.put("debit", debit[i]);
            hm.put("credit", credit[i]);
            aList.add(hm);
        }

        String[] from = {"limit", "debit", "credit"};
        int[] to = {R.id.tv_limit, R.id.tv_debit, R.id.tv_credit};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
                R.layout.list_item_limits, from, to);

        listView.setAdapter(adapter);
        Utility.getListViewSize(listView, this, mListItemHieght);
    }
}
