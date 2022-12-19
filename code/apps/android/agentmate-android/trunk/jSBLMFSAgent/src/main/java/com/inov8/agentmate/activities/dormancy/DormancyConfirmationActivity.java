package com.inov8.agentmate.activities.dormancy;

import static com.inov8.agentmate.util.Constants.FLOW_ID_RM_RECEIVE_CASH_NOT_REGISTERED;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_OTC;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_VERIFY_NADRA;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FINGERS_EMPTY;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_OTC;
import static com.inov8.agentmate.util.Utility.testValidity;
import static com.paysyslabs.instascan.RemittanceType.IBFT;
import static com.paysyslabs.instascan.RemittanceType.MONENY_TRANSFER_RECEIVE;
import static com.paysyslabs.instascan.RemittanceType.MONENY_TRANSFER_SEND;

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
import com.inov8.agentmate.activities.fundsTransfer.FundsTransferConfirmationActivity;
import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.bvs.paysys.NadraRestClient;
import com.inov8.agentmate.bvs.paysys.OTCClient;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.NadraRequestModel;
import com.inov8.agentmate.model.NadraResponseModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsblmfs.R;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.RemittanceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class DormancyConfirmationActivity extends BaseCommunicationActivity implements MpinInterface {
    private TextView lblHeading;
    private Button btnNext, btnCancel;
    private String labels[], data[];
    private String cnic, mobileNumber;
    private ArrayList<String> fingers;
    private boolean fourFingersReceived = false;
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private ArrayList<Integer> fingerIndexes;
    private String strProductId, strAgentMobileNumber, strSenderCnic,
            strSenderMobileNumber, strReceiverCnic, strReceiverMobileNumber,
            strAccountNumber, strAccountTitle, strAmount, strCharges,
            strCommissionAmount, strTotalAmount, strAmountF, strChargesF,
            strTotalAmountF, bvsFlag, biometricFlow, senderCity, receiverCity;
    private String strFingerIndex, nadraRequestJson,
            nadraResponseJson, requestType, purposeCode;
    private String fileTemplate = "";
    private String fileTemplateType = "";
    private int currCommand;
    private NadraResponseModel nadraResponseModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.setContentView(R.layout.activity_confirm_dormancy);

        try {

            fetchIntents();
            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("Confirm Details");

            labels = new String[]{"CNIC", "Mobile No."};
            data = new String[]{cnic, mobileNumber};


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

            btnNext = findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    decideBvs();
                }
            });

            btnCancel = findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Utility.showConfirmationDialog(
                            DormancyConfirmationActivity.this,
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
    public void onMpinPopupClosed() {
        btnNext.setEnabled(true);
    }

    private void fetchIntents() {
        cnic = mBundle.getString(Constants.IntentKeys.CNIC);
        mobileNumber = mBundle.getString(Constants.IntentKeys.CMOB);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
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
            ApplicationData.nadraCall = true;
            new HttpAsyncTask(DormancyConfirmationActivity.this)
                    .execute(Constants.CMD_VERIFY_NADRA_FINGERPRINT + "", nadraRequestJson);
        }  else if (currCommand == Constants.CMD_DORMANCY_CONFIRMATION) {
            new HttpAsyncTask(this).execute(
                    Constants.CMD_DORMANCY_CONFIRMATION + "", mobileNumber, cnic);
        }

    }

    private void openBvsDialog() {

        fingers = Utility.getAllFingers();
        Intent intent = new Intent(DormancyConfirmationActivity.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }

    protected void decideBvs() {
        Intent intent = new Intent(DormancyConfirmationActivity.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }

    private void openBvsActivity() {
        Intent intent = new Intent(DormancyConfirmationActivity.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, strSenderMobileNumber);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, "Dormancy Removal");
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
    }

    protected void checkPaysysBvs() {
        if (ApplicationData.isAgentAllowedBvs.equals("1")) {
            if (ApplicationData.isBvsEnabledDevice) {
                openBvsDialog();
            } else {
                if (ApplicationData.bvsErrorMessage != null)
                    PopupDialogs.alertDialog(ApplicationData.bvsErrorMessage,
                            PopupDialogs.Status.INFO, DormancyConfirmationActivity.this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    finish();
                                }
                            });
                else
                    PopupDialogs.alertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE,
                            PopupDialogs.Status.INFO, DormancyConfirmationActivity.this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    finish();
                                }
                            });
            }
        } else {
            PopupDialogs.alertDialog(AppMessages.BVS_NOT_ENABLED,
                    PopupDialogs.Status.INFO, DormancyConfirmationActivity.this, new PopupDialogs.OnCustomClickListener() {
                        @Override
                        public void onClick(View v, Object obj) {
                            finish();
                        }
                    });
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {

            if (currCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
                hideLoading();
                processNadraResponse(response);
            } else {


                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
                if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);
                    createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT,
                            false, message);
                }  else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                    hideLoading();
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                    MessageModel message = (MessageModel) list.get(0);
                    PopupDialogs.createAlertDialog(message.getDescr(), "Message",
                            PopupDialogs.Status.INFO, this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                    goToMainMenu();
                                }
                            });
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
                        Intent intent = new Intent(DormancyConfirmationActivity.this, FingerScanActivity.class);
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

                        Intent intent = new Intent(DormancyConfirmationActivity.this, FingerScanActivity.class);
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
                            Intent intent = new Intent(DormancyConfirmationActivity.this, FingerScanActivity.class);
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
                                            Intent intent = new Intent(DormancyConfirmationActivity.this, FingerScanActivity.class);
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

                Intent intent = new Intent(DormancyConfirmationActivity.this, NadraRestClient.class);
                Bundle extras = data.getExtras();
                Fingers finger = null;
                if (extras != null) {
                    finger = (Fingers) extras.get(Constants.IntentKeys.FINGER);
                }
                intent.putExtra(Constants.IntentKeys.FINGER, finger);
                intent.putExtra(Constants.IntentKeys.IDENTIFIER, cnic != null ? cnic : "");
                intent.setData(null);
                startActivityForResult(intent, REQUEST_CODE_VERIFY_NADRA);
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
                } else if (biometricFlow.equals(Constants.SUPREMA)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.P41M2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.P41M2;
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
                    requestModel.setCitizenNumber(cnic);
                    requestModel.setContactNumber(mobileNumber);
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

    private void processNadraResponse(HttpResponseModel response) {
        try {
            nadraResponseJson = response.getXmlResponse();

            if (nadraResponseJson.startsWith("<msg")) {

                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
                hideLoading();

                if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);
                    createAlertDialog(message.getDescr(),
                            AppMessages.ALERT_HEADING);
                }
            } else if (nadraResponseJson != null && !nadraResponseJson.equals("")) {
                nadraResponseModel = new Gson().fromJson
                        (nadraResponseJson, NadraResponseModel.class);

                if (nadraResponseModel.getResponseCode() != null && nadraResponseModel.getResponseCode().equals("100")) {
                    requestType = Constants.REQUEST_TYPE_NADRA;
                    currCommand = Constants.CMD_DORMANCY_CONFIRMATION;
                  processRequest();
//                    Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();

                } else {
                    PopupDialogs.alertDialog(nadraResponseModel.getMessage() != null ?
                                    nadraResponseModel.getMessage() : "Exception in Nadra Response", PopupDialogs.Status.ERROR,
                            this, new PopupDialogs.OnCustomClickListener() {
                                @Override
                                public void onClick(View v, Object obj) {
                                }
                            });
                }
            } else {
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