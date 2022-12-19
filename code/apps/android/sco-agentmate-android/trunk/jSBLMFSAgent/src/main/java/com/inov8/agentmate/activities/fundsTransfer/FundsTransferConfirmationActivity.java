package com.inov8.agentmate.activities.fundsTransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.BvsDeviceSelectorActivity1;
import com.inov8.agentmate.activities.TransactionReceiptActivity;
import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.bvs.paysys.OTCClient;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.NadraRequestModel;
import com.inov8.agentmate.model.NadraResponseModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsbl.sco.R;;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.RemittanceType;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_OTC;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FINGERS_EMPTY;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_OTC;
import static com.paysyslabs.instascan.RemittanceType.IBFT;
import static com.paysyslabs.instascan.RemittanceType.MONENY_TRANSFER_SEND;

public class FundsTransferConfirmationActivity extends
        BaseCommunicationActivity implements MpinInterface {
    private TextView lblHeading, lblAlert;
    private String strProductId, strAgentMobileNumber, strSenderCnic,
            strSenderMobileNumber, strReceiverCnic, strReceiverMobileNumber,
            strAccountNumber, strAccountTitle, strAmount, strCharges,
            strCommissionAmount, strTotalAmount, strAmountF, strChargesF,
            strTotalAmountF, bvsFlag, biometricFlow, senderCity, receiverCity;
    private Button btnNext, btnCancel;
    private String labels[], data[];
    private TransactionInfoModel transactionInfo;
    private byte flowId;
    private ArrayList<String> fingers;
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private ArrayList<Integer> fingerIndexes;
    private boolean fourFingersReceived = false;
    private int currCommand;
    private String strFingerIndex, nadraRequestJson,
            nadraResponseJson, requestType, purposeCode;
    private String fileTemplate = "";
    private String fileTemplateType = "";

    private NadraResponseModel nadraResponseModel;

    private SearchableSpinner receiverCitySpinner;
    private Spinner senderCitySpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_confirm_funds_transfer);

        try {
            fetchIntents();
            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("Confirm Transaction");

            lblAlert = (TextView) findViewById(R.id.lblAlert);
            lblAlert.setText("Please verify customer details.");

            senderCitySpinner = (Spinner)findViewById(R.id.senderCitySpinner);
            receiverCitySpinner = (SearchableSpinner)findViewById(R.id.citiesSpinner);

            if(flowId == Constants.FLOW_ID_FT_CNIC_TO_CNIC) {
                if(transactionInfo.getSenderCity()!=null) {
                    ArrayList<String> senderCityList = new ArrayList<String>();
                    senderCityList.add(transactionInfo.getSenderCity().equals("")? "N/A" : transactionInfo.getSenderCity());

                    findViewById(R.id.tvSenderCity).setVisibility(View.VISIBLE);

                    ArrayAdapter<String> senderCitiesAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, senderCityList);

                    senderCitySpinner.setAdapter(senderCitiesAdapter);
                    senderCitySpinner.setVisibility(View.VISIBLE);
                    senderCitySpinner.setEnabled(false);
                }

                if(ApplicationData.listCities!=null) {
                    receiverCitySpinner.setTitle("Receiver City");

                    ArrayList<String> receiverCityList = new ArrayList<String>();
                    receiverCityList.add("Select City");

                    for(int i=0; i<ApplicationData.listCities.size(); i++){
                        receiverCityList.add(ApplicationData.listCities.get(i).getName());
                    }
                    findViewById(R.id.tvReceiverCity).setVisibility(View.VISIBLE);

                    ArrayAdapter<String> receiverCitiesAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, receiverCityList);
                    receiverCitySpinner.setAdapter(receiverCitiesAdapter);
                    receiverCitySpinner.setPositiveButton("OK");
                    receiverCitySpinner.setVisibility(View.VISIBLE);
                }
            }

            strAgentMobileNumber = ApplicationData.agentMobileNumber;
            strAmount = transactionInfo.getTxam();
            strCharges = transactionInfo.getTpam();
            strCommissionAmount = transactionInfo.getCamt();
            strTotalAmount = transactionInfo.getTamt();
            strAmountF = transactionInfo.getTxamf();
            strChargesF = transactionInfo.getTpamf();
            strTotalAmountF = transactionInfo.getTamtf();
            strProductId = transactionInfo.getPid();
            if (transactionInfo.getIsBvsReq() != null)
                bvsFlag = transactionInfo.getIsBvsReq();

            switch (flowId) {
                case Constants.FLOW_ID_FT_BLB_TO_BLB:
                    strSenderMobileNumber = transactionInfo.getCmob();
                    strReceiverMobileNumber = transactionInfo.getRcmob();

                    labels = new String[]{"Sender Mobile No.", "Receiver Mobile No.",
                            "Amount", "Charges", "Total Amount"};
                    data = new String[]{strSenderMobileNumber,
                            strReceiverMobileNumber, strAmountF + Constants.CURRENCY,
                            strChargesF + Constants.CURRENCY,
                            strTotalAmountF + Constants.CURRENCY};
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                    strSenderMobileNumber = transactionInfo.getCmob();
                    strReceiverCnic = transactionInfo.getRwcnic();
                    strReceiverMobileNumber = transactionInfo.getRwmob();

                    labels = new String[]{"Sender Mobile No.", "Receiver CNIC",
                            "Receiver Mobile No.", "Amount", "Charges", "Total Amount"};
                    data = new String[]{strSenderMobileNumber, strReceiverCnic,
                            strReceiverMobileNumber, strAmountF + Constants.CURRENCY,
                            strChargesF + Constants.CURRENCY,
                            strTotalAmountF + Constants.CURRENCY};
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_BLB:
                    strSenderCnic = transactionInfo.getSwcnic();
                    strSenderMobileNumber = transactionInfo.getSwmob();
                    strReceiverMobileNumber = transactionInfo.getRcmob();
                    strReceiverCnic = transactionInfo.getRwcnic();

                    labels = new String[]{"Sender CNIC", "Sender Mobile No.",
                            "Receiver Mobile No.", "Amount", "Charges", "Total Amount"};
                    data = new String[]{strSenderCnic, strSenderMobileNumber,
                            strReceiverMobileNumber, strAmountF + Constants.CURRENCY,
                            strChargesF + Constants.CURRENCY,
                            strTotalAmountF + Constants.CURRENCY};
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
                    strSenderCnic = transactionInfo.getSwcnic();
                    strSenderMobileNumber = transactionInfo.getSwmob();
                    strReceiverCnic = transactionInfo.getRwcnic();
                    strReceiverMobileNumber = transactionInfo.getRwmob();

                    labels = new String[]{"Sender CNIC", "Sender Mobile No.",
                            "Receiver CNIC", "Receiver Mobile No.", "Amount",
                            "Charges", "Total Amount"};
                    data = new String[]{strSenderCnic, strSenderMobileNumber,
                            strReceiverCnic, strReceiverMobileNumber,
                            strAmountF + Constants.CURRENCY,
                            strChargesF + Constants.CURRENCY,
                            strTotalAmountF + Constants.CURRENCY};
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                    strSenderMobileNumber = transactionInfo.getCmob();
                    strAccountNumber = transactionInfo.getCoreacid();
                    strAccountTitle = transactionInfo.getCoreactl();
                    strReceiverMobileNumber = transactionInfo.getRcmob();

                    labels = new String[]{"Sender Mobile No.", "Account Number",
                            "Account Title", "Receiver Mobile No.", "Amount",
                            "Charges", "Total Amount"};
                    data = new String[]{strSenderMobileNumber, strAccountNumber,
                            strAccountTitle, strReceiverMobileNumber,
                            strAmountF + Constants.CURRENCY,
                            strChargesF + Constants.CURRENCY,
                            strTotalAmountF + Constants.CURRENCY};
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
                    strSenderMobileNumber = transactionInfo.getSwmob();
                    strSenderCnic = transactionInfo.getSwcnic();
                    strAccountNumber = transactionInfo.getCoreacid();
                    strAccountTitle = transactionInfo.getCoreactl();
                    strReceiverMobileNumber = transactionInfo.getRcmob();

                    labels = new String[]{"Sender CNIC", "Sender Mobile No.",
                            "Account Number", "Account Title", "Receiver Mobile No.",
                            "Amount", "Charges", "Total Amount"};
                    data = new String[]{strSenderCnic, strSenderMobileNumber,
                            strAccountNumber, strAccountTitle, strReceiverMobileNumber,
                            strAmountF + Constants.CURRENCY,
                            strChargesF + Constants.CURRENCY,
                            strTotalAmountF + Constants.CURRENCY};
                    break;
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
            listView.setAdapter(adapter);

            Utility.getListViewSize(listView, this, mListItemHieght);

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if(flowId == Constants.FLOW_ID_FT_CNIC_TO_CNIC){
                        if(senderCitySpinner.getVisibility()==View.VISIBLE)
                            senderCity = senderCitySpinner.getSelectedItem().toString();

                        receiverCity = receiverCitySpinner.getSelectedItem().toString();
                        if(receiverCity.equals("Select City"))
                            receiverCity = "";
                    }
                    try {
                        if (flowId == Constants.FLOW_ID_FT_CNIC_TO_CNIC
                                || flowId == Constants.FLOW_ID_FT_CNIC_TO_CORE_AC
                                || flowId == Constants.FLOW_ID_FT_CNIC_TO_BLB) {
                            if (bvsFlag != null && bvsFlag.equals("1")) {

                                if (ApplicationData.isAgentAllowedBvs.equals("1")) {
                                    showLoading("Please Wait", "Processing");

                                    new Thread() {
                                        public void run() {

                                            if (ApplicationData.isBvsEnabledDevice) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hideLoading();
                                                        btnNext.setEnabled(false);
                                                        askMpin(null, null, false, FundsTransferConfirmationActivity.this);
                                                    }
                                                });
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hideLoading();
                                                        PopupDialogs.alertDialog(ApplicationData.bvsErrorMessage,
                                                                PopupDialogs.Status.INFO, FundsTransferConfirmationActivity.this, new PopupDialogs.OnCustomClickListener() {
                                                                    @Override
                                                                    public void onClick(View v, Object obj) {
                                                                        finish();
                                                                    }
                                                                });
                                                    }
                                                });
                                            }

                                        }
                                    }.start();
                                } else {
                                    PopupDialogs.alertDialog(AppMessages.BVS_NOT_ENABLED,
                                            PopupDialogs.Status.INFO, FundsTransferConfirmationActivity.this, new PopupDialogs.OnCustomClickListener() {
                                                @Override
                                                public void onClick(View v, Object obj) {
                                                    finish();
                                                }
                                            });
                                }
                            } else {
                                btnNext.setEnabled(false);
                                askMpin(null, null, false, FundsTransferConfirmationActivity.this);
                            }
                        } else {
                            btnNext.setEnabled(false);
                            askMpin(null, null, false, FundsTransferConfirmationActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideLoading();
                        createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
                    }
                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Utility.showConfirmationDialog(
                            FundsTransferConfirmationActivity.this,
                            Constants.Messages.CANCEL_TRANSACTION,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    goToMainMenu();
                                }
                            });
                }
            });
            addAutoKeyboardHideFunction();
            headerImplementation();
            addAutoKeyboardHideFunctionScrolling();
        } catch (Exception e) {
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
    }

    @Override
    public void onMpinPopupClosed(){
        btnNext.setEnabled(true);
    }

    private void fetchIntents() {
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
        purposeCode = mBundle.getString(Constants.IntentKeys.PURPOSE_CODE,"");
        transactionInfo = (TransactionInfoModel) mBundle.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        ApplicationData.nadraOtcCall = false;
        currCommand = Constants.CMD_VERIFY_PIN;
        processRequest();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_OTC) {

            if (resultCode == RESULT_CODE_SUCCESS_OTC) {
                fourFingersReceived = false;
                exhaustedFingers.clear();
                processRequest();
            } else if (resultCode == RESULT_CODE_FAILED_OTC_FINGER_INDEXES) {
                if (data != null) {
                    fourFingersReceived = true;
                    fingers = data.getStringArrayListExtra("fingers");
                    fingerIndexes = data.getIntegerArrayListExtra("validFingerIndexes");
                    final String errorCode = data.getStringExtra("code");

                    if (exhaustedFingers.size() == 0) {
                        Intent intent = new Intent(FundsTransferConfirmationActivity.this, FingerScanActivity.class);
                        intent.putStringArrayListExtra("fingers", fingers);
                        intent.putExtra("validFingerIndexes", fingerIndexes);
                        intent.putExtra("code", errorCode);
                        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
                    } else {
                        for (int i = 0; i < exhaustedFingers.size(); i++) {
                            for (int j = 0; j < fingers.size(); j++) {
                                if (fingers.get(j).equals(exhaustedFingers.get(i))) {
                                    fingers.remove(j);
                                    break;
                                }
                            }
                        }

                        Intent intent = new Intent(FundsTransferConfirmationActivity.this, FingerScanActivity.class);
                        intent.putStringArrayListExtra("fingers", fingers);
                        intent.putExtra("validFingerIndexes", fingerIndexes);
                        intent.putExtra("code", errorCode);
                        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
                    }
                }
            } else if (resultCode == RESULT_CODE_FAILED_OTC_OTHER_ERROR) {
                if (data != null) {
                    final String errorCode = data.getStringExtra("code");

                    if (errorCode.equals("118")) {
                        if (fourFingersReceived) {
                            Fingers currentFinger = ApplicationData.currentFinger;
                            for (int i = 0; i < fingers.size(); i++) {
                                if (fingers.get(i).equals(currentFinger.getValue())) {
                                    exhaustedFingers.add(exhaustedFingers.size(), fingers.get(i));
                                    fingers.remove(i);
                                    break;
                                }
                            }
                            Intent intent = new Intent(FundsTransferConfirmationActivity.this, FingerScanActivity.class);
                            intent.putStringArrayListExtra("fingers", fingers);
                            intent.putExtra("validFingerIndexes", fingerIndexes);
                            intent.putExtra("code", errorCode);
                            startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
                        } else {
                            openBvsDialog();
                        }
                    } else {
                        createAlertDialog(data.getStringExtra("msg"),
                                Constants.Messages.ALERT_ERROR,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        if (fourFingersReceived) {
                                            Intent intent = new Intent(FundsTransferConfirmationActivity.this, FingerScanActivity.class);
                                            intent.putStringArrayListExtra("fingers", fingers);
                                            intent.putExtra("validFingerIndexes", fingerIndexes);
                                            intent.putExtra("code", errorCode);
                                            startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
                                        } else {
                                            openBvsDialog();
                                        }
                                    }
                                });
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_FINGER_SCAN) {
            if (resultCode == RESULT_OK) {
                if (!haveInternet()) {
                    Toast.makeText(getApplication(), AppMessages.INTERNET_CONNECTION_PROBLEM,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(FundsTransferConfirmationActivity.this, OTCClient.class);
                Bundle extras = data.getExtras();
                Fingers finger = null;
                if (extras != null) {
                    finger = (Fingers) extras.get(Constants.IntentKeys.FINGER);
                }
                intent.putExtra(Constants.IntentKeys.FINGER, finger);
                Utility.setAgentAreaName(intent);
                intent.putExtra(Constants.IntentKeys.IDENTIFIER, strSenderCnic != null ? strSenderCnic : "");
                intent.putExtra(Constants.IntentKeys.REMITTANCE_AMOUNT, "");
                if (flowId == Constants.FLOW_ID_FT_CNIC_TO_CORE_AC)
                    intent.putExtra(Constants.IntentKeys.REMITTANCE_TYPE, (RemittanceType) IBFT);
                else
                    intent.putExtra(Constants.IntentKeys.REMITTANCE_TYPE, (RemittanceType) MONENY_TRANSFER_SEND);
                intent.putExtra(Constants.IntentKeys.CONTACT_NUMBER, strSenderMobileNumber != null ? strSenderMobileNumber : "");
                intent.putExtra(Constants.IntentKeys.SECONDARY_IDENTIFIER, strReceiverCnic != null ? strReceiverCnic : "");
                intent.putExtra(Constants.IntentKeys.SECONDARY_CONTACT_NUMBER, strReceiverMobileNumber != null ? strReceiverMobileNumber : "");
                intent.putExtra(Constants.IntentKeys.ACCOUNT_NUMBER, strAccountNumber != null ? strAccountNumber : "");
                startActivityForResult(intent, REQUEST_CODE_OTC);
            } else if (resultCode == RESULT_CODE_FINGERS_EMPTY) {
                exhaustedFingers.clear();
                fourFingersReceived = false;
                openBvsDialog();
            }
        } else if (requestCode == Constants.REQUEST_CODE_BIOMETRIC_SELECTION) {
            if (resultCode == RESULT_OK) {
                biometricFlow = data.getExtras().getString(Constants.IntentKeys.BIOMETRIC_FLOW);
                if (biometricFlow.equals(Constants.PAYSYS)) {
                    openBvsDialog();
                }  else if (biometricFlow.equals(Constants.SCO)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SCO;
                    openBvsActivity();
                }else if (biometricFlow.equals(Constants.SUPREMA)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA;
                    openBvsActivity();
                }
                else if (biometricFlow.equals(Constants.P41M2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.P41M2;
                    openBvsActivity();
                }
                else if (biometricFlow.equals(Constants.SUPREMA_SLIM_2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA_SLIM_2;
                    openBvsActivity();
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_BVS) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    strFingerIndex = (String) data.getExtras().get(XmlConstants.ATTR_FINGER_INDEX);
                    fileTemplate = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE);
                    fileTemplateType = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE_TYPE);

                    NadraRequestModel requestModel = new NadraRequestModel();
                    requestModel.setFingerIndex(strFingerIndex);
                    requestModel.setTemplateType(fileTemplateType);
                    requestModel.setFingerTemplate(fileTemplate);
                    requestModel.setCitizenNumber(strSenderCnic);
                    requestModel.setContactNumber(strSenderMobileNumber);
                    requestModel.setSecondaryCitizenNumber(strReceiverCnic);
                    requestModel.setSecondaryContactNumber(strReceiverMobileNumber);
                    requestModel.setMobileBankAccountNumber(strAccountNumber != null ? strAccountNumber : "");
                    requestModel.setRemittanceType("MONENY_TRANSFER_SEND");
                    requestModel.setRemittanceAmount("");
                    requestModel.setAreaName("Punjab");

                    Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                    nadraRequestJson = gson.toJson(requestModel);

                    currCommand = Constants.CMD_VERIFY_NADRA_FINGERPRINT;
                    processRequest();
                }
            }
        }
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(),
                    Constants.Messages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading("Please Wait", "Processing...");

        if (currCommand == Constants.CMD_VERIFY_PIN) {
            new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                    Constants.CMD_VERIFY_PIN + "", getEncryptedMpin());
        }
        else if (currCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
            ApplicationData.nadraOtcCall = true;
            new HttpAsyncTask(FundsTransferConfirmationActivity.this)
                    .execute(Constants.CMD_VERIFY_NADRA_FINGERPRINT + "", nadraRequestJson);
        } else {
            switch (flowId) {
                case Constants.FLOW_ID_FT_BLB_TO_BLB:
                    new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_BLB2BLB + "",
                            getEncryptedMpin(), strProductId, strAgentMobileNumber,
                            strSenderMobileNumber, strReceiverMobileNumber, strAmount,
                            strCommissionAmount, strCharges, strTotalAmount);
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                    new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_BLB2CNIC + "",
                            getEncryptedMpin(), strProductId, strAgentMobileNumber,
                            strSenderMobileNumber, strReceiverMobileNumber,
                            strReceiverCnic, strAmount, strCommissionAmount,
                            strCharges, strTotalAmount,purposeCode!=null? purposeCode: "");
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_BLB:
                    new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_CNIC2BLB + "",
                            getEncryptedMpin(), strProductId, strAgentMobileNumber,
                            strReceiverMobileNumber, strSenderMobileNumber,
                            strSenderCnic, strAmount, strCommissionAmount,
                            strCharges, strTotalAmount,
                            bvsFlag != null ? bvsFlag : "",purposeCode!=null? purposeCode: "");
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
                    new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_CNIC2CNIC + "",
                            getEncryptedMpin(), strProductId, strAgentMobileNumber,
                            strSenderCnic, strSenderMobileNumber, strReceiverCnic,
                            strReceiverMobileNumber, strAmount,
                            strCommissionAmount, strCharges, strTotalAmount,
                            bvsFlag != null ? bvsFlag : "", senderCity!=null? senderCity: "",
                            receiverCity!=null? receiverCity: "",purposeCode!=null? purposeCode: "");
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
                    new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_CNIC2CORE + "",
                            getEncryptedMpin(), strProductId, strAgentMobileNumber,
                            strSenderCnic, strSenderMobileNumber, strReceiverMobileNumber,
                            strAccountNumber, strAccountTitle, strAmount,
                            strCommissionAmount, strCharges, strTotalAmount,
                            bvsFlag != null ? bvsFlag : "");
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                    new HttpAsyncTask(FundsTransferConfirmationActivity.this).execute(
                            Constants.CMD_FUNDS_TRANSFER_BLB2CORE + "",
                            getEncryptedMpin(), strProductId, strAgentMobileNumber,
                            strSenderMobileNumber, strReceiverMobileNumber,
                            strAccountNumber, strAccountTitle, strAmount,
                            strCommissionAmount, strCharges, strTotalAmount);
                    break;
            }
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            if (currCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
                hideLoading();
                processNadraResponse(response);
            }
            else {
                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());

                if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);
                    createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, false, message);

                    if (currCommand == Constants.CMD_VERIFY_PIN)
                        ApplicationData.pinRetryCount = ApplicationData.pinRetryCount + 1;
                    hideLoading();
                }
                else if (table != null && !table.containsKey(Constants.KEY_LIST_ERRORS)
                        && !table.containsKey(Constants.KEY_LIST_TRANS)) {
                    currCommand = 0;
                    if (flowId == Constants.FLOW_ID_FT_CNIC_TO_CNIC
                            || flowId == Constants.FLOW_ID_FT_CNIC_TO_BLB
                            || flowId == Constants.FLOW_ID_FT_CNIC_TO_CORE_AC) {
                        if (bvsFlag != null && !bvsFlag.equals("1")) {
                            processRequest();
                        }
                        else {
                            hideLoading();
                            decideBvs();
                        }
                    }
                    else
                        processRequest();
                }
                else {
                    if (table.containsKey(Constants.KEY_LIST_TRANS)) {
                        @SuppressWarnings("unchecked")
                        List<TransactionModel> list = (List<TransactionModel>) table
                                .get(Constants.KEY_LIST_TRANS);
                        TransactionModel transaction = list.get(0);

                        Intent intent = new Intent(getApplicationContext(),
                                TransactionReceiptActivity.class);
                        intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL,
                                transaction);
                        switch (flowId) {
                            case Constants.FLOW_ID_FT_CNIC_TO_BLB:
                            case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
                            case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
                                intent.putExtra(Constants.IntentKeys.SHOW_REGISTRATION_POPUP, true);
                                if (bvsFlag != null)
                                    intent.putExtra(Constants.IntentKeys.BVS_FLAG, bvsFlag);
                                break;
                        }

                        intent.putExtras(mBundle);
                        startActivity(intent);
                        hideLoading();
                    }
                }
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
        }
    }

    @Override
    public void processNext() {
    }

    private void openBvsDialog() {

        fingers = Utility.getAllFingers();
        Intent intent = new Intent(FundsTransferConfirmationActivity.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }

    protected void decideBvs() {
        Intent intent = new Intent(FundsTransferConfirmationActivity.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }

    private void openBvsActivity() {
        Intent intent = new Intent(FundsTransferConfirmationActivity.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, strSenderMobileNumber);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, "Send Money");
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
    }

    private void processNadraResponse(HttpResponseModel response) {
        try {
            nadraResponseJson = response.getXmlResponse();

            if(nadraResponseJson.startsWith("<msg")) {

                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
                hideLoading();

                if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);
                    createAlertDialog(message.getDescr(),
                            AppMessages.ALERT_HEADING);
                }
            }

            else if(nadraResponseJson!=null && !nadraResponseJson.equals("")) {
                nadraResponseModel = new Gson().fromJson
                        (nadraResponseJson, NadraResponseModel.class);

                if (nadraResponseModel.getResponseCode()!=null && nadraResponseModel.getResponseCode().equals("100")) {
                    requestType = Constants.REQUEST_TYPE_NADRA;
                    currCommand=-1;
                    processRequest();
                }
                else {
                    PopupDialogs.alertDialog(nadraResponseModel.getMessage()!=null?
                                    nadraResponseModel.getMessage() : "Exception in Nadra Response", PopupDialogs.Status.ERROR,
                            this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                }
                            });
                }
            }
            else{
                PopupDialogs.alertDialog("Exception in Nadra Response", PopupDialogs.Status.ERROR,
                        this, new PopupDialogs.OnCustomClickListener() {
                            @Override
                            public void onClick(View v, Object obj) {
                            }
                        });
            }

        } catch (Exception e) {
            PopupDialogs.alertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, PopupDialogs.Status.ERROR,
                    this, new PopupDialogs.OnCustomClickListener() {
                        @Override
                        public void onClick(View v, Object obj) {
                        }
                    });
            e.printStackTrace();
        }
    }
}