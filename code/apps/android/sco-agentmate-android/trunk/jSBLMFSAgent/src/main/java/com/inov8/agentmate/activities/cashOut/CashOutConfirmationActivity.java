package com.inov8.agentmate.activities.cashOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.BvsDeviceSelectorActivity1;
import com.inov8.agentmate.activities.TransactionReceiptActivity;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.bvs.paysys.OTCClient;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.NadraRequestModel;
import com.inov8.agentmate.model.NadraResponseModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AesEncryptor;
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

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_OTC;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FINGERS_EMPTY;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_OTC;
import static com.paysyslabs.instascan.RemittanceType.IBFT;
import static com.paysyslabs.instascan.RemittanceType.MONENY_TRANSFER_RECEIVE;

public class CashOutConfirmationActivity extends BaseCommunicationActivity implements MpinInterface {
    private String account, totalAmount, cnic, otpCode = null, cashOutType;
    private Button btnNext, btnCancel;
    private TextView lblHeading, lblAlert, lblField1;
    private EditText et1;

    private TransactionInfoModel transactionInfo;
    private ProductModel product;

    private ArrayList<String> fingers;
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private ArrayList<Integer> fingerIndexes;
    private boolean fourFingersReceived = false;
    private int currCommand;
    private String strFingerIndex, nadraRequestJson,
            nadraResponseJson, requestType, biometricFlow;
    private String fileTemplate = "";
    private String fileTemplateType = "";

    private String isHRA = "";

    private NadraResponseModel nadraResponseModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_confirm);

        try {
            fetchIntents();

            lblField1 = (TextView) findViewById(R.id.lblField1);
            et1 = (EditText) findViewById(R.id.input1);
            disableCopyPaste(et1);

            if (cashOutType.equals(Constants.CASH_OUT_BY_TRX_ID) || product.getId().equals(Constants.PRODUCT_ID_CASH_WITH_DRAWAL)) {
                lblField1.setText("OTP");
                lblField1.setVisibility(View.VISIBLE);
                et1.setFilters(new InputFilter[]{new InputFilter.LengthFilter
                        (Constants.MAX_LENGTH_OTP)});
                et1.setVisibility(View.VISIBLE);
                et1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                et1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }

            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("Confirm Transaction");

            lblAlert = (TextView) findViewById(R.id.lblAlert);
            lblAlert.setText("Please verify transaction details.");

            account = transactionInfo.getCmob();
            cnic = transactionInfo.getCnic();
            totalAmount = transactionInfo.getTamtf();

            String labels[] = new String[]{"Customer Mobile No.",
                    "Customer NIC", "Customer Title", "Amount", "Charges",
                    "Total Amount"};
            String data[] = new String[]{account, cnic,
                    transactionInfo.getCname(),
                    transactionInfo.getTxamf() + " " + Constants.CURRENCY,
                    transactionInfo.getTpamf() + Constants.CURRENCY,
                    totalAmount + " " + Constants.CURRENCY};

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

                    if (cashOutType.equals(Constants.CASH_OUT_BY_TRX_ID) || product.getId().equals(Constants.PRODUCT_ID_CASH_WITH_DRAWAL)) {
                        if (!Utility.testValidity(et1))
                            return;

                        if (et1.getText().toString().length() < Constants.MAX_LENGTH_OTP) {
                            et1.setError(Constants.Messages.INVALID_TRX_CODE);
                            return;
                        } else
                            otpCode = et1.getText().toString();
                    }
                    btnNext.setEnabled(false);
                    askMpin(mBundle, TransactionReceiptActivity.class, false, CashOutConfirmationActivity.this);
                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Utility.showConfirmationDialog(
                            CashOutConfirmationActivity.this,
                            Constants.Messages.CANCEL_TRANSACTION,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                    goToMainMenu();
                                }
                            });
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
        headerImplementation();
        btnHome.setEnabled(false);
    }

    private void fetchIntents() {
        transactionInfo = (TransactionInfoModel) mBundle.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
        product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        cashOutType = mBundle.getString(Constants.IntentKeys.CASH_OUT_TYPE);
    }

    @Override
    public void onMpinPopupClosed() {
        btnNext.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        if (product.getId().equals(Constants.PRODUCT_ID_CASH_WITH_DRAWAL)) {
            isHRA = "1";
            currCommand = Constants.CMD_HRA_CASH_WITHDRAWAL;
            processRequest();
//            decideBvs();
        } else {

            if (product.getId().equals(Constants.PRODUCT_ID_CASH_WITH_DRAWAL)) {
                currCommand = Constants.CMD_HRA_CASH_WITHDRAWAL;

            } else {
                currCommand = Constants.CMD_CASH_OUT;
            }

            processRequest();
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

//        if (cashOutType.equals(Constants.CASH_OUT_BY_IVR))

        if (currCommand == Constants.CMD_HRA_CASH_WITHDRAWAL || currCommand == Constants.CMD_CASH_OUT) {
            new HttpAsyncTask(CashOutConfirmationActivity.this).execute(
                    currCommand + "", getEncryptedMpin(),
                    product.getId(), transactionInfo.getCmob(),
                    transactionInfo.getCnic(), ApplicationData.agentMobileNumber,
                    transactionInfo.getTxam(), transactionInfo.getCamt(),
                    transactionInfo.getTpam(), transactionInfo.getTamt(), isHRA,
                    cashOutType.equals(Constants.CASH_OUT_BY_TRX_ID)? transactionInfo.getTrxid(): "", otpCode != null? AesEncryptor.encrypt(otpCode): "");
        } else if (currCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
            ApplicationData.nadraOtcCall = true;
            new HttpAsyncTask(CashOutConfirmationActivity.this)
                    .execute(Constants.CMD_VERIFY_NADRA_FINGERPRINT + "", nadraRequestJson);
        } else
            new HttpAsyncTask(CashOutConfirmationActivity.this).execute(
                    Constants.CMD_CASH_OUT_BY_TRX_ID + "", getEncryptedMpin(),
                    AesEncryptor.encrypt(otpCode), product.getId(), transactionInfo.getTxam(),
                    transactionInfo.getTrxid(), transactionInfo.getCamt(),
                    transactionInfo.getTpam(), transactionInfo.getTamt(), account);
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {


            if (currCommand == Constants.CMD_VERIFY_NADRA_FINGERPRINT) {
//                hideLoading();
                processNadraResponse(response);
            } else {
                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
                        .getXmlResponse());
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

                        Intent intent = new Intent(getApplicationContext(), TransactionReceiptActivity.class);
                        intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL, transaction);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                }
            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
        }
        hideLoading();
    }

    @Override
    public void processNext() {
    }

    private void openBvsDialog() {
        fingers = Utility.getAllFingers();
        Intent intent = new Intent(CashOutConfirmationActivity.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }

    protected void decideBvs() {
        Intent intent = new Intent(CashOutConfirmationActivity.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }

    private void openBvsActivity() {
        Intent intent = new Intent(CashOutConfirmationActivity.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, account);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, "Cash Out");
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
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
                    currCommand = Constants.CMD_HRA_CASH_WITHDRAWAL;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_OTC) {

            if (resultCode == RESULT_CODE_SUCCESS_OTC) {
                fourFingersReceived = false;
                exhaustedFingers.clear();
                currCommand = Constants.CMD_HRA_CASH_WITHDRAWAL;
                processRequest();
            } else if (resultCode == RESULT_CODE_FAILED_OTC_FINGER_INDEXES) {
                if (data != null) {
                    fourFingersReceived = true;
                    fingers = data.getStringArrayListExtra("fingers");
                    fingerIndexes = data.getIntegerArrayListExtra("validFingerIndexes");
                    final String errorCode = data.getStringExtra("code");

                    if (exhaustedFingers.size() == 0) {
                        Intent intent = new Intent(CashOutConfirmationActivity.this, FingerScanActivity.class);
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
                        Intent intent = new Intent(CashOutConfirmationActivity.this, FingerScanActivity.class);
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

                            Intent intent = new Intent(CashOutConfirmationActivity.this, FingerScanActivity.class);
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
                                            Intent intent = new Intent(CashOutConfirmationActivity.this, FingerScanActivity.class);
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

                Intent intent = new Intent(CashOutConfirmationActivity.this, OTCClient.class);
                Bundle extras = data.getExtras();
                Fingers finger = null;
                if (extras != null) {
                    finger = (Fingers) extras.get(Constants.IntentKeys.FINGER);
                }
                intent.putExtra(Constants.IntentKeys.FINGER, finger);
                Utility.setAgentAreaName(intent);
                intent.putExtra(Constants.IntentKeys.IDENTIFIER, cnic != null ? cnic : "");
                intent.putExtra(Constants.IntentKeys.REMITTANCE_AMOUNT, "");
                intent.putExtra(Constants.IntentKeys.REMITTANCE_TYPE, (RemittanceType) MONENY_TRANSFER_RECEIVE);
                intent.putExtra(Constants.IntentKeys.CONTACT_NUMBER, account != null ? account : "");
                intent.putExtra(Constants.IntentKeys.SECONDARY_IDENTIFIER, cnic);
                intent.putExtra(Constants.IntentKeys.SECONDARY_CONTACT_NUMBER, account);
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
                    currCommand = Constants.CMD_VERIFY_NADRA_FINGERPRINT;
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
                    requestModel.setContactNumber(account);
                    requestModel.setSecondaryCitizenNumber(cnic);
                    requestModel.setSecondaryContactNumber(account);
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
}