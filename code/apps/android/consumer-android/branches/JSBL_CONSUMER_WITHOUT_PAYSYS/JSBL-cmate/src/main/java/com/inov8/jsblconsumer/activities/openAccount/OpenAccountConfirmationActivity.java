package com.inov8.jsblconsumer.activities.openAccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.LoginActivity;
import com.inov8.jsblconsumer.adapters.BitmapListViewAdaptor;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


public class OpenAccountConfirmationActivity extends BaseCommunicationActivity {
    private TextView lblHeading;
    private String trxid, msisdn, name, cnic, cusRegStateId, cusRegState,
            cusDob, cusAccType, cnicExpiry, initialDeposit, isCnicSeen;
    private Uri uriCustomer, uriCnicFront;
    private String photoPathCustomer, photoPathCnicFront;
    private Button btnNext, btnCancel;
    private String discrepantCus = "0";
    private boolean isBulkRegister = false;
    private ProductModel product;
    private String accountLevel, accountLevelLabel;
    private AlertDialog alertDialog;
    private boolean flagCustomer = false,
            flagCnicFront = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_account_open_confirm);

        TextView lblProgress3 = (TextView) findViewById(R.id.lblProgress3);
        lblProgress3.setText(getText(R.string.customer_details));

        try {
            product = (ProductModel) mBundle
                    .get(Constants.IntentKeys.PRODUCT_MODEL);

            titleImplementation("icon_customer_registration", "Account Opening", null, this);

            trxid = mBundle.getString(Constants.IntentKeys.TRXID);
            msisdn = mBundle.getString(Constants.ATTR_CMSISDN);
            name = mBundle.getString(Constants.ATTR_CNAME);
            cnic = mBundle.getString(Constants.ATTR_CNIC);
            discrepantCus = mBundle.getString(Constants.IntentKeys.DISCREPANT);
            cusRegState = mBundle.getString(Constants.ATTR_CREG_STATE);
            cusRegStateId = mBundle.getString(Constants.ATTR_CREG_STATE_ID);
            isBulkRegister = mBundle.getBoolean("isBulkRegister");
            initialDeposit = mBundle.getString(Constants.ATTR_DEPOSIT_AMT);
            isCnicSeen = mBundle.getString(Constants.ATTR_IS_CNIC_SEEN);

            cusDob = mBundle.getString(Constants.ATTR_CDOB);
            cusAccType = mBundle.getString(Constants.ATTR_CUST_ACC_TYPE);
            cnicExpiry = mBundle.getString(Constants.ATTR_CNIC_EXP);

            accountLevel = mBundle.getString(Constants.ATTR_CUST_ACC_TYPE);


            flagCustomer = mBundle.getBoolean("Customer");
            flagCnicFront = mBundle.getBoolean("NIC Front");

            if (accountLevel.equals("1")) {
                accountLevelLabel = "Level 0";
            } else {
                accountLevelLabel = "Level 1";
            }

            photoPathCustomer = mBundle.getString("customer");
            photoPathCnicFront = mBundle.getString("cnic_front");

            String labels[];
            String data[];

            if (isBulkRegister) {
                labels = new String[]{"Mobile No.", "Name", "CNIC",
                        "Initial Deposit", "D.O.B (dd-mm-yyyy)",
                        "CNIC expiry date (dd-mm-yyyy)", "Account Level"};
                data = new String[]{
                        msisdn,
                        name,
                        cnic,
                        Utility.getFormatedAmount(initialDeposit) + ""
                                + Constants.CURRENCY,
                        mBundle.getString(Constants.ATTR_CDOB_FORMATED),
                        mBundle.getString(Constants.ATTR_CNIC_EXP_FORMATED),
                        accountLevelLabel};
            } else {
                labels = new String[]{"Mobile No.", "Name", "CNIC",
                        "D.O.B (dd-mm-yyyy)", "CNIC expiry date (dd-mm-yyyy)",
                        "Account Level"};
                data = new String[]{msisdn, name, cnic,
                        mBundle.getString(Constants.ATTR_CDOB_FORMATED),
                        mBundle.getString(Constants.ATTR_CNIC_EXP_FORMATED),
                        accountLevelLabel};
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
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
                    R.layout.listview_layout_with_data, from, to);

            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);

            // if (isBulkRegister) {// if flow is bulk register, show details other
            // wise hide listview
            listView.setAdapter(adapter);
            // }

            Utility.getListViewSize(listView, this, 5);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    OpenAccountConfirmationActivity.this);
            alertDialogBuilder.setTitle("Please Wait");
            alertDialogBuilder.setMessage("Processing...");
            alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    loadImages();
                }
            }, 800);

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
//
                    processRequest();
                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    goToLogin();
                }
            });
            headerImplementation();
        } catch (Exception e) {
            e.printStackTrace();
//            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
    }

    private void loadImages() {

        if (photoPathCustomer != null && photoPathCnicFront != null) {
            uriCustomer = Uri.parse(photoPathCustomer);
            uriCnicFront = Uri.parse(photoPathCnicFront);
        } else if (photoPathCustomer != null)
            uriCustomer = Uri.parse(photoPathCustomer);
        else if (photoPathCnicFront != null) {
            uriCnicFront = Uri.parse(photoPathCnicFront);
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Uri uris[] = null;
                String[] data = null;

                if (uriCustomer != null && uriCnicFront != null) {
                    uris = new Uri[]{uriCustomer, uriCnicFront};
                    data = new String[]{"Customer", "CNIC Front"};
                } else if (uriCustomer != null) {
                    uris = new Uri[]{uriCustomer};
                    data = new String[]{"Customer"};
                } else if (uriCnicFront != null) {
                    uris = new Uri[]{uriCnicFront};
                    data = new String[]{"CNIC Front"};
                }

                if (uris != null) {

                    List<HashMap<String, Object>> bList = new ArrayList<HashMap<String, Object>>();

                    for (int i = 0; i < data.length; i++) {
                        HashMap<String, Object> hm = new HashMap<String, Object>();
                        hm.put("uris", Utility.getThumbnail(
                                new File(uris[i].getPath()), 512, 512));
                        hm.put("data", data[i]);
                        bList.add(hm);
                    }

                    String[] from2 = {"uris", "data"};
                    int[] to2 = {R.id.photo, R.id.txtData};
                    BitmapListViewAdaptor adapterbitmap = new BitmapListViewAdaptor(
                            getBaseContext(), bList,
                            R.layout.listview_layout_with_photos, from2, to2);

                    ListViewExpanded listView2 = (ListViewExpanded) findViewById(R.id.picList);
                    listView2.setAdapter(adapterbitmap);
                }
                alertDialog.dismiss();
            }
        });
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    OpenAccountConfirmationActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        try {
            showLoading("Please Wait", "Processing...");
            String uriCnic = "", uriCus = "";
            if (photoPathCnicFront != null) {
                uriCnic = (new File(uriCnicFront.getPath())).getName();
            }
            if (photoPathCustomer != null) {
                uriCus = (new File(uriCustomer.getPath())).getName();
            }


            new HttpAsyncTask(OpenAccountConfirmationActivity.this).execute(Constants.CMD_OPEN_ACCOUNT + "",
                    uriCus, uriCnic,
//                            (new File(uriCustomer.getPath())).getName(),
//                            (new File(uriCnicFront.getPath())).getName(),

                    name, cnic, msisdn,
                    cusDob, cusAccType,
                    cnicExpiry, isCnicSeen != null ? isCnicSeen : "", "", "", "", "", "", "", discrepantCus != null ? discrepantCus : "0","0", ApplicationData.mobileNetwork);

        } catch (Exception e) {
            e.printStackTrace();
//            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            hideLoading();
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
                    .getXmlResponse());
            if (table != null && table.containsKey("errs")) {
                List<?> list = (List<?>) table.get("errs");

                MessageModel message = (MessageModel) list.get(0);


                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        OpenAccountConfirmationActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);



            } else if (table != null && table.containsKey("msgs")) {
                List<?> list = (List<?>) table.get("msgs");

                MessageModel message = (MessageModel) list.get(0);


                dialogGeneral = PopupDialogs.createSuccessDialog("Congratulations!", message.getDescr(), this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("fromOpenAccount",true);
                        intent.putExtra(Constants.IntentKeys.HRA, false);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, null);



                deleteAgentmatePics();

            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            PopupDialogs.createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE,
                    AppMessages.ALERT_HEADING, this, PopupDialogs.Status.ERROR);
        }
    }

    @Override
    public void processNext() {
    }

    public void createMessageAlert(final String reason, final String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alert = new AlertDialog.Builder(
                        OpenAccountConfirmationActivity.this);
                alert.setTitle(title);
                alert.setMessage(reason);
                alert.setCancelable(false);
                alert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                goToMainMenu();
                            }
                        });
                alert.show();
            }
        });
    }
}