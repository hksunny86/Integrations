package com.inov8.jsblconsumer.activities.hra;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;

import com.inov8.jsblconsumer.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.jsblconsumer.model.HraRegistrationModel;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.CnicController;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.model.PersonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.jsblconsumer.util.Utility.testValidity;


public class HraRegistrationActivity1 extends BaseCommunicationActivity implements View.OnClickListener {

    private EditText customerMobileNumber, customerCNIC,
            incomeSrc, occupation;
    private Button btnSearch, btnCancel;
    private TextView lblHeading;
    private ProductModel product;
    private String cnic, mobileNumber = "";
    private Bundle bundle = new Bundle();
    private LinearLayout responseList;
    private ListViewExpanded dataList;
    private int currentCommand;
    private Spinner accountPurpose;
    private String[] accountPurposes = {"Personal", "Business"};
    private String labels[], data[];
    private boolean discrepantCustomer = false, responseReceived = false;
    private String cusRegState, cusRegStateId;
    private String name, fatherName, dob;
    private CnicController cnicController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hra_registration1);

        fetchIntents();
        setUI();
        headerImplementation();
    }

    private void fetchIntents() {
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        } catch (Exception e) {
        }
    }

    private void setUI() {

        cnicController = new CnicController(findViewById(R.id.cnicView), true, true, this);
        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText("HRA Registration");


        incomeSrc = (EditText) findViewById(R.id.inputIncomeSource);
        disableCopyPaste(incomeSrc);

        occupation = (EditText) findViewById(R.id.inputOccupation);
        disableCopyPaste(occupation);

        responseList = (LinearLayout) findViewById(R.id.responseDataList);
        dataList = (ListViewExpanded) findViewById(R.id.dataList);

        accountPurpose = (Spinner) findViewById(R.id.spinnerAccountPurpose);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accountPurposes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountPurpose.setAdapter(adapter);
        accountPurpose.setSelection(0);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnSearch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                hideKeyboard(v);
                if (!cnicController.isValidCnic()) {


                    dialogGeneral = popupDialogs.createAlertDialog("Invalid CNIC", AppMessages.ALERT_HEADING,
                            HraRegistrationActivity1.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                    return;
                } else if (!responseReceived) {
                    currentCommand = Constants.CMD_INFO_L1_TO_HRA;
                    cnic = cnicController.getCnic();
                    processRequest();
                } else {


                    if (!testValidity(incomeSrc) || !testValidity(occupation)) {
                        return;
                    }

                    HraRegistrationModel hraModel = new HraRegistrationModel();
                    hraModel.setCnic(cnic);
                    hraModel.setMob(mobileNumber);
                    hraModel.setName(name != null ? name : "");
                    hraModel.setFatherName(fatherName != null ? fatherName : "");
                    hraModel.setDob(dob != null ? dob : "");
                    hraModel.setIncomeSource(incomeSrc.getText().toString());
                    hraModel.setOccupation(occupation.getText().toString());
                    hraModel.setAccountPurpose(accountPurpose.getSelectedItem().toString());

                    Intent intent = new Intent(HraRegistrationActivity1.this, HraRegistrationActivity2.class);
                    intent.putExtra(Constants.IntentKeys.HRA_MODEL, hraModel);
                    if (bundle != null) {
                        intent.putExtras(mBundle);
                    }
                    startActivity(intent);
                }
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }


    @Override
    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(), AppMessages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            showLoading("Please Wait", "Processing...");

            if (currentCommand == Constants.CMD_INFO_L1_TO_HRA) {
                new HttpAsyncTask(this).execute(
                        Constants.CMD_INFO_L1_TO_HRA + "", mobileNumber, cnic);
            }

        } catch (Exception e) {
            hideLoading();
//            createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
            e.printStackTrace();
        }
    }


    @Override
    public void processResponse(HttpResponseModel response) {
        try {

            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            hideLoading();

            switch (currentCommand) {
                case Constants.CMD_INFO_L1_TO_HRA: {
                    if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                        List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                        MessageModel message = (MessageModel) list.get(0);
                        hideLoading();


                        if (message.code.equals("9008")) {
                            Intent intent = new Intent(HraRegistrationActivity1.this, OpenAccountBvsActivity.class);
                            intent.putExtra(Constants.IntentKeys.HRA, true);
                            startActivity(intent);
                        } else {
                            dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                    HraRegistrationActivity1.this, getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGeneral.dismiss();
                                        }
                                    }, false, PopupDialogs.Status.ERROR, false, null);

                        }
                        String code = message.getCode();
                        AppLogger.i("##Error Code: " + code);

                    } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                        discrepantCustomer = false;
                        hideLoading();
                        List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                        MessageModel message = (MessageModel) list.get(0);
//                            PopupDialogs.createAlertDialog(message.getDescr(), "Message",
//                                    PopupDialogs.Status.INFO, this, new PopupDialogs.OnCustomClickListener() {
//                                        @Override
//                                        public void onClick(View v, Object obj) {
//                                            goToMainMenu();
//                                        }
//                                    });
                    }
//                        else if (table != null && table.containsKey(Constants.ATTR_CREG_STATE)) {
//                        hideLoading();
//                            cusRegState = (String) table.get(Constants.ATTR_CREG_STATE);
//                            cusRegStateId = (String) table.get(Constants.ATTR_CREG_STATE_ID);
//
//                            if (cusRegStateId.equals(Constants.REGISTRATION_STATE_BULK_REQUEST_RECEIVED) ||
//                                    cusRegStateId.equals(Constants.REGISTRATION_STATE_REQUEST_RECEIVED) ||
//                                    cusRegStateId.equals(Constants.REGISTRATION_STATE_VERIFIED)){


                    else if (table.containsKey(XmlConstants.ATTR_CNAME) && table.containsKey(XmlConstants.ATTR_CDOB)
                            && table.containsKey(XmlConstants.ATTR_FATHER_NAME)) {
                        hideLoading();
                        name = table.get(XmlConstants.ATTR_CNAME).toString();
                        fatherName = table.get(XmlConstants.ATTR_FATHER_NAME).toString();
                        dob = table.get(XmlConstants.ATTR_CDOB).toString();

                        labels = new String[]{"Name", "Father/Spouse Name", "Date of Birth"};
                        data = new String[]{name, fatherName, dob};


                        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

                        for (int i = 0; i < data.length; i++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("label", labels[i]);
                            hm.put("data", data[i]);
                            aList.add(hm);
                        }

                        String[] from = {"label", "data"};
                        int[] to = {R.id.txtLabel, R.id.txtData};
                        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
                                R.layout.listview_layout_with_data, from, to);

                        dataList.setAdapter(adapter);
                        responseList.setVisibility(View.VISIBLE);

                        findViewById(R.id.lblIncomeSource).setVisibility(View.VISIBLE);
                        findViewById(R.id.lblOccupation).setVisibility(View.VISIBLE);
                        findViewById(R.id.lblAccountPurpose).setVisibility(View.VISIBLE);

                        incomeSrc.setVisibility(View.VISIBLE);
                        occupation.setVisibility(View.VISIBLE);
                        accountPurpose.setVisibility(View.VISIBLE);

                        responseReceived = true;

                        cnicController.setEnable();
                        btnSearch.setText("Next");
                        Utility.getListViewSize(dataList, this, mListItemHieght);
                        break;
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING,
                    HraRegistrationActivity1.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);
        }
    }


    @Override
    public void processNext() {
    }

    @Override
    public void onBackPressed() {
    //    hideKeyboard(getCurrentFocus());
        goToMainMenu();
    }


}