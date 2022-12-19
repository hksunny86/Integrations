package com.inov8.jsblconsumer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.contactus.ContactUsActivity;
import com.inov8.jsblconsumer.activities.faq.FaqsActivity;
import com.inov8.jsblconsumer.activities.openAccount.TermsConditionsJsActivity;
import com.inov8.jsblconsumer.adapters.SubMenuAdapter;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BottomBarActivity extends BaseActivity {
    private TextView lblHeading;
    private String[] values;
    private String[] logos;
//    private HashMap<Integer, Object> optionsMap = new HashMap<Integer, Object>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_list_view);

        headerImplementation();

        checkSoftKeyboardD();

        bottomBarImplementation(BottomBarActivity.this, "57");

        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText("");

        titleImplementation(null, getString(R.string.help), null, BottomBarActivity.this);

        values = new String[3];
        setList();
    }

    private void setList() {

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("icon", Integer.toString(R.drawable.list_icon_arrow));
        hm.put("txtOption", getString(R.string.contact_us));
        values[0] = getString(R.string.contact_us);
        aList.add(hm);

        HashMap<String, String> hm1 = new HashMap<String, String>();
        hm1.put("icon", Integer.toString(R.drawable.list_icon_arrow));
        hm1.put("txtOption", getString(R.string.faqs));
        values[1] = getString(R.string.faqs);
        aList.add(hm1);

        HashMap<String, String> hm2 = new HashMap<String, String>();
        hm2.put("icon", Integer.toString(R.drawable.list_icon_arrow));
        hm2.put("txtOption", getString(R.string.terms_and_conditions));
        values[2] = getString(R.string.terms_and_conditions);
        aList.add(hm2);

        final String[] from = {"icon", "txtOption"};
        final int[] to = {R.id.icon, R.id.txtOption};

        SubMenuAdapter adapter = new SubMenuAdapter(getBaseContext(),values, logos);
        ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.optionsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Object obj = optionsMap.get(position);
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(BottomBarActivity.this, ContactUsActivity.class);
                        intent.putExtra(Constants.IntentKeys.HEADING, getResources().getString(R.string.contact_us));
                        intent.putExtra(Constants.IntentKeys.ICON, R.drawable.heading_icon_contact);

                        startActivity(intent);
                        if (ApplicationData.isWebViewOpen) {
                            ApplicationData.isWebViewOpen = false;
                            ApplicationData.webUrl = null;
                            finish();
                        }

                        break;

                    case 1:
                        intent = new Intent(BottomBarActivity.this, FaqsActivity.class);
                        startActivity(intent);
                        if (ApplicationData.isWebViewOpen) {
                            ApplicationData.isWebViewOpen = false;
                            ApplicationData.webUrl = null;
                            finish();
                        }
                        break;
                    case 2:
                        intent = new Intent(BottomBarActivity.this, TermsConditionsJsActivity.class);
                        startActivity(intent);
                        if (ApplicationData.isWebViewOpen) {
                            ApplicationData.isWebViewOpen = false;
                            ApplicationData.webUrl = null;
                            finish();
                        }
                        break;
                }
            }
        });
    }


}

