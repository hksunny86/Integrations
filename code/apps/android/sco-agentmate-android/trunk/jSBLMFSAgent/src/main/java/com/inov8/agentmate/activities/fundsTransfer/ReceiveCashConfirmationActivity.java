package com.inov8.agentmate.activities.fundsTransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
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
import com.inov8.jsbl.sco.R;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.RemittanceType;

import static com.inov8.agentmate.util.Constants.FLOW_ID_RM_RECEIVE_CASH_NOT_REGISTERED;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_OTC;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FINGERS_EMPTY;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_OTC;
import static com.inov8.agentmate.util.Utility.testValidity;
import static com.paysyslabs.instascan.RemittanceType.MONENY_TRANSFER_RECEIVE;

public class ReceiveCashConfirmationActivity extends BaseCommunicationActivity implements MpinInterface {
    private TextView lblHeading, lblAlert, lblTrxCode;
    private EditText inputTrxCode;
    private ScrollView scView;
    private String strProductId, strAgentMobileNumber, strSenderCnic,
            strSenderMobileNumber, strReceiverCnic, strReceiverMobileNumber,
            strTrxId, strDatef, strTimef, strAmount, strCharges,
            strCommissionAmount, strTotalAmount, strAmountF, strChargesF,
            strTotalAmountF, strIsReg, strTrxCode, bvsFlag;
    private Button btnNext, btnCancel;
    private String labels[], data[];
    private TransactionInfoModel transactionInfo;
    private byte flowId, isReg;
    private static final byte NOT_REGISTERED = 0;
    private static final byte ALREADY_REGISTERED = 1;
    private ArrayList<String> fingers;
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private ArrayList<Integer> fingerIndexes;
    private boolean fourFingersReceived = false;
    private int currCommand;
    private String strFingerIndex, nadraRequestJson,
            nadraResponseJson, requestType, biometricFlow;
    private String fileTemplate = "";
    private String fileTemplateType = "";

    private NadraResponseModel nadraResponseModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.setContentView(R.layout.activity_confirm_funds_transfer);

        try {

            fetchIntents();
            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("Confirm Transaction");

            lblAlert = (TextView) findViewById(R.id.lblAlert);
            lblAlert.setText("Please verify customer details.");

            lblTrxCode = (TextView) findViewById(R.id.lblField1);
            lblTrxCode.setVisibility(View.GONE);

            inputTrxCode = (EditText) findViewById(R.id.input1);
            inputTrxCode.setVisibility(View.GONE);
            disableCopyPaste(inputTrxCode);

            scView = (ScrollView) findViewById(R.id.scView);

            strAgentMobileNumber = ApplicationData.agentMobileNumber;

            strSenderCnic = transactionInfo.getSwcnic();
            strSenderMobileNumber = transactionInfo.getSwmob();
            strReceiverCnic = transactionInfo.getRwcnic();
            strReceiverMobileNumber = transactionInfo.getRwmob();
            strTrxId = transactionInfo.getTrxid();
            strDatef = transactionInfo.getDatef();
            strTimef = transactionInfo.getTimef();

            strAmount = transactionInfo.getTxam();
            strCharges = transactionInfo.getTpam();
            strCommissionAmount = transactionInfo.getCamt();
            strTotalAmount = transactionInfo.getTamt();
            strAmountF = transactionInfo.getTxamf();
            strChargesF = transactionInfo.getTpamf();
            strTotalAmountF = transactionInfo.getTamtf();
            strProductId = transactionInfo.getPid();
            strIsReg = transactionInfo.getIsreg();

            if (strIsReg != null) {
                isReg = Byte.parseByte(strIsReg);
            }
            if (transactionInfo.getIsBvsReq() != null)
                bvsFlag = transactionInfo.getIsBvsReq();

            if (strProductId.equals(Constants.ProductIds.BULK2CNIC + "")) {
                labels = new String[]{"Receiver CNIC", "Receiver Mobile No.",
                        "Transaction ID", "Total Amount", "Date & Time"};
                data = new String[]{strReceiverCnic, strReceiverMobileNumber,
                        strTrxId, strTotalAmountF + Constants.CURRENCY,
                        strDatef + " " + strTimef};
            } else {
                labels = new String[]{"Sender CNIC", "Sender Mobile No.",
                        "Receiver CNIC", "Receiver Mobile No.", "Transaction ID",
                        "Total Amount", "Date & Time"};
                data = new String[]{strSenderCnic, strSenderMobileNumber,
                        strReceiverCnic, strReceiverMobileNumber, strTrxId,
                        strTotalAmountF + Constants.CURRENCY,
                        strDatef + " " + strTimef};
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
                    if(inputTrxCode.getVisibility()==View.VISIBLE) {
                        if (!testValidity(inputTrxCode)) {
                            return;
                        }

                        if (inputTrxCode.getText().toString().length() < Constants.MAX_LENGTH_TRX_CODE) {
                            inputTrxCode.setError(Constants.Messages.INVALID_TRX_CODE);
                            inputTrxCode.requestFocus();
                            scView.scrollTo(0, 0);
                            return;
                        }
                        strTrxCode = inputTrxCode.getText().toString();
                    }

                    try {
                        if (flowId == FLOW_ID_RM_RECEIVE_CASH_NOT_REGISTERED ||
                                flowId == Constants.FLOW_ID_RM_SENDER_REDEEM) {
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
                                                        askMpin(null, null, false, ReceiveCashConfirmationActivity.this);
                                                    }
                                                });
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hideLoading();
                                                        PopupDialogs.alertDialog(ApplicationData.bvsErrorMessage,
                                                                PopupDialogs.Status.INFO, ReceiveCashConfirmationActivity.this, new PopupDialogs.OnCustomClickListener() {
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
                                            PopupDialogs.Status.INFO, ReceiveCashConfirmationActivity.this, new PopupDialogs.OnCustomClickListener() {
                                                @Override
                                                public void onClick(View v, Object obj) {
                                                    finish();
                                                }
                                            });
                                }
                            } else {
                                btnNext.setEnabled(false);
                                askMpin(mBundle, TransactionReceiptActivity.class, false, ReceiveCashConfirmationActivity.this);
                            }
                        } else {
                            btnNext.setEnabled(false);
                            askMpin(mBundle, TransactionReceiptActivity.class, false, ReceiveCashConfirmationActivity.this);
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
                            ReceiveCashConfirmationActivity.this,
                            Constants.Messages.CANCEL_TRANSACTION,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    goToMainMenu();
                                }
                            });
                }
            });

            switch (flowId) {
                case Constants.FLOW_ID_RM_RECEIVE_CASH:
                    switch (isReg) {
                        case NOT_REGISTERED:
                            showNotRegisteredOptionsPopup();
                            break;
                        case ALREADY_REGISTERED:
                            showAlreadyRegisteredOptionsPopup();
                            break;
                    }
                    break;
            }

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
        transactionInfo = (TransactionInfoModel) mBundle
                .get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {

        if (flowId == FLOW_ID_RM_RECEIVE_CASH_NOT_REGISTERED ||
                flowId == Constants.FLOW_ID_RM_SENDER_REDEEM) {
            if (bvsFlag != null && !bvsFlag.equals("1")) {
                processRequest();
            } else {
                decideBvs();
            }
        }
        else
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
                        Intent intent = new Intent(ReceiveCashConfirmationActivity.this, FingerScanActivity.class);
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
                        Intent intent = new Intent(ReceiveCashConfirmationActivity.this, FingerScanActivity.class);
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

                            Intent intent = new Intent(ReceiveCashConfirmationActivity.this, FingerScanActivity.class);
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
                                            Intent intent = new Intent(ReceiveCashConfirmationActivity.this, FingerScanActivity.class);
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

                Intent intent = new Intent(ReceiveCashConfirmationActivity.this, OTCClient.class);
                Bundle extras = data.getExtras();
                Fingers finger = null;
                if (extras != null) {
                    finger = (Fingers) extras.get(Constants.IntentKeys.FINGER);
                }
                intent.putExtra(Constants.IntentKeys.FINGER, finger);
                Utility.setAgentAreaName(intent);
                if(flowId == Constants.FLOW_ID_RM_SENDER_REDEEM) {
                    intent.putExtra(Constants.IntentKeys.IDENTIFIER, strSenderCnic != null ? strSenderCnic : "");
                    intent.putExtra(Constants.IntentKeys.SECONDARY_IDENTIFIER, strReceiverCnic != null ? strReceiverCnic : "");
                }
                else{
                    intent.putExtra(Constants.IntentKeys.IDENTIFIER, strReceiverCnic != null ? strReceiverCnic : "");
                    intent.putExtra(Constants.IntentKeys.SECONDARY_IDENTIFIER, strSenderCnic != null ? strSenderCnic : "");
                }

                intent.putExtra(Constants.IntentKeys.REMITTANCE_AMOUNT, "");
                intent.putExtra(Constants.IntentKeys.REMITTANCE_TYPE, (RemittanceType) MONENY_TRANSFER_RECEIVE);
                intent.putExtra(Constants.IntentKeys.CONTACT_NUMBER, strReceiverMobileNumber != null ? strReceiverMobileNumber : "");
                intent.putExtra(Constants.IntentKeys.SECONDARY_CONTACT_NUMBER, strSenderMobileNumber != null ? strSenderMobileNumber : "");
                intent.putExtra(Constants.IntentKeys.ACCOUNT_NUMBER, "");

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
                } else if (biometricFlow.equals(Constants.SCO)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SCO;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.SUPREMA)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.P41M2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.P41M2;
                    currCommand = Constants.CMD_VERIFY_NADRA_FINGERPRINT;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.SUPREMA_SLIM_2)) {
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

                    if(flowId == Constants.FLOW_ID_RM_SENDER_REDEEM) {
                        requestModel.setCitizenNumber(strSenderCnic);
                        requestModel.setSecondaryCitizenNumber(strReceiverCnic);
                    }
                    else {
                        requestModel.setCitizenNumber(strReceiverCnic);
                        requestModel.setSecondaryCitizenNumber(strSenderCnic);
                    }
                    requestModel.setContactNumber(strReceiverMobileNumber);
                    requestModel.setSecondaryContactNumber(strSenderMobileNumber);
                    requestModel.setMobileBankAccountNumber("");
                    requestModel.setRemittanceType("MONENY_TRANSFER_RECEIVE");
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

        if (currCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
            ApplicationData.nadraOtcCall = true;
            new HttpAsyncTask(ReceiveCashConfirmationActivity.this)
                    .execute(Constants.CMD_VERIFY_NADRA_FINGERPRINT + "", nadraRequestJson);
        } else {

            switch (flowId) {
                case Constants.FLOW_ID_RM_SENDER_REDEEM:
                    new HttpAsyncTask(ReceiveCashConfirmationActivity.this).execute(
                            Constants.CMD_RECEIVE_MONEY_SENDER_REDEEM_PAYMENT + "",
                            getEncryptedMpin(), strProductId, strAgentMobileNumber,
                            strSenderCnic, strSenderMobileNumber, strReceiverCnic,
                            strReceiverMobileNumber, strTrxCode!=null? strTrxCode: "", strTrxId,
                            strTotalAmount, bvsFlag != null ? bvsFlag : "");
                    break;

                case Constants.FLOW_ID_RM_RECEIVE_CASH:
                    new HttpAsyncTask(ReceiveCashConfirmationActivity.this).execute(
                            Constants.CMD_RECEIVE_MONEY_PENDING_TRX_PAYMENT + "",
                            getEncryptedMpin(), strProductId, strAgentMobileNumber,
                            strSenderCnic, strSenderMobileNumber, strReceiverCnic,
                            strReceiverMobileNumber, strTrxCode!=null? strTrxCode: "", strTrxId, strAmount,
                            strCommissionAmount, strCharges, strTotalAmount);
                    break;

                case FLOW_ID_RM_RECEIVE_CASH_NOT_REGISTERED:
                    new HttpAsyncTask(ReceiveCashConfirmationActivity.this).execute(
                            Constants.CMD_RECEIVE_MONEY_RECEIVE_CASH + "",
                            getEncryptedMpin(), strProductId, strAgentMobileNumber,
                            strSenderCnic, strSenderMobileNumber, strReceiverCnic,
                            strReceiverMobileNumber, strTrxCode!=null? strTrxCode: "", strTrxId, strAmount,
                            strCommissionAmount, strCharges, strTotalAmount,
                            bvsFlag != null ? bvsFlag : "");
                    break;
            }
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {

            if (currCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
//                hideLoading();
                processNadraResponse(response);
            } else {


                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
                if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);
                    createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT,
                            false, message);
                } else {
                    if (table.containsKey(Constants.KEY_LIST_TRANS)) {
                        @SuppressWarnings("unchecked")
                        List<TransactionModel> list = (List<TransactionModel>) table
                                .get(Constants.KEY_LIST_TRANS);
                        TransactionModel transaction = list.get(0);

                        Intent intent = new Intent(getApplicationContext(),
                                TransactionReceiptActivity.class);
                        intent.putExtras(mBundle);
                        intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL,
                                transaction);
                        intent.putExtra(Constants.IntentKeys.PRODUCT_ID, strProductId);
                        startActivity(intent);
                    }
                }
                hideLoading();
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
        }
//        hideLoading();
    }

    @Override
    public void processNext() {
    }

    public void showNotRegisteredOptionsPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Receive Cash",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                flowId = FLOW_ID_RM_RECEIVE_CASH_NOT_REGISTERED;
                                mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);
                            }
                        })
                .setNegativeButton("Account Opening",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(ReceiveCashConfirmationActivity.this, OpenAccountBvsActivity.class);
                                intent.putExtra(Constants.IntentKeys.TRXID, strTrxId);
                                intent.putExtra(Constants.IntentKeys.RCMOB, strReceiverMobileNumber);
                                intent.putExtra(Constants.IntentKeys.RCNIC, strReceiverCnic);
                                intent.putExtra(Constants.IntentKeys.IS_RECEIVE_CASH, Constants.IS_RECEIVE_CASH);

                                intent.putExtras(mBundle);

                                startActivity(intent);
                                finish();
                            }
                            // set dialog message
                        }).setMessage("Select an option");

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showAlreadyRegisteredOptionsPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                goToMainMenu();
                            }
                            // set dialog message
                        })
                .setMessage(
                        "Already registered. Do you want to transfer the amount in your account?");

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void openBvsDialog() {
        fingers = Utility.getAllFingers();
        Intent intent = new Intent(ReceiveCashConfirmationActivity.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }


    protected void decideBvs() {
        Intent intent = new Intent(ReceiveCashConfirmationActivity.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }


    private void openBvsActivity() {
        Intent intent = new Intent(ReceiveCashConfirmationActivity.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, strSenderMobileNumber);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, "Receive Money");
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

            else if (nadraResponseJson != null && !nadraResponseJson.equals("")) {
                nadraResponseModel = new Gson().fromJson
                        (nadraResponseJson, NadraResponseModel.class);

                if (nadraResponseModel.getResponseCode() != null && nadraResponseModel.getResponseCode().equals("100")) {
                    requestType = Constants.REQUEST_TYPE_NADRA;
                    currCommand = -1;
                    processRequest();
                } else {
                    hideLoading();
                    PopupDialogs.alertDialog(nadraResponseModel.getMessage() != null ?
                                    nadraResponseModel.getMessage() : "Exception in Nadra Response", PopupDialogs.Status.ERROR,
                            this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                }
                            });
                }
            } else {
                hideLoading();
                PopupDialogs.alertDialog("Exception in Nadra Response", PopupDialogs.Status.ERROR,
                        this, new PopupDialogs.OnCustomClickListener() {
                            @Override
                            public void onClick(View v, Object obj) {
                            }
                        });
            }

        } catch (Exception e) {
            hideLoading();
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