package com.inov8.agentmate.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.bvs.paysys.OTCClient;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Constants.IntentKeys;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsblmfs.R;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.RemittanceType;
import com.inov8.agentmate.util.ListViewExpanded;


import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;
import static com.inov8.agentmate.util.Constants.REQUEST_CODE_OTC;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_FINGER_INDEXES;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FAILED_OTC_OTHER_ERROR;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_FINGERS_EMPTY;
import static com.inov8.agentmate.util.Constants.RESULT_CODE_SUCCESS_OTC;
import static com.paysyslabs.instascan.RemittanceType.MONENY_TRANSFER_RECEIVE;

public class BOPCardIssuanceReIssuanceConfirmationActivity extends BaseCommunicationActivity implements MpinInterface {

    private ProductModel product;
    private String cnic, cmob, dcno, segmentCode;
    private TextView lblHeading, lblAlert;
    private Button btnNext, btnCancel;
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private boolean fourFingersReceived = false;
    private int currCommand;
    private String strFingerIndex, biometricFlow;
    private String fileTemplate = "", template = "";
    private String fileTemplateType = "";
    private String strWSQ = "";
    private String segmentName;
    private int count = 0;
    private String minutiaeCount = "";
    private String NFIQuality = "", nadraSessionId = null;
    private String[] splited = null;
    private ArrayList<String> fingers;
    private ArrayList<Integer> fingerIndexes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bop_card_issuance_re_issuance_confirmation);

        try {
            fetchIntents();
            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("Confirm Card Details");

            lblAlert = (TextView) findViewById(R.id.lblAlert);
            lblAlert.setText("Please verify card details.");

            for (int i = 0; i < ApplicationData.listSegments.size(); i++) {
                if (ApplicationData.listSegments.get(i).getCode().equals(segmentCode)) {
                    segmentName = ApplicationData.listSegments.get(i).getName();
                    break;
                }
            }

            String labels[], data[];
            labels = new String[]{"Mobile No.", "CNIC", "Card No.", "Segment"};
            data = new String[]{cmob, cnic, dcno, segmentName};
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
            btnNext.setOnClickListener(v -> {

                btnNext.setEnabled(false);
                askMpin(mBundle, TransactionReceiptActivity.class, false, BOPCardIssuanceReIssuanceConfirmationActivity.this);
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(v -> Utility.showConfirmationDialog(
                    BOPCardIssuanceReIssuanceConfirmationActivity.this,
                    Constants.Messages.CANCEL_TRANSACTION,
                    (dialog, id) -> {
                        dialog.cancel();
                        goToMainMenu();
                    }));

        } catch (Exception ex) {
            ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
        headerImplementation();
        ApplicationData.errorCount = 0;
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

        try {
            if (ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.P41M2) || ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.SUPREMA_SLIM_2)) {
//                if (count == 2) {
//                    fileTemplateType = "WSQ";
//                    fileTemplate = ApplicationData.WSQ;
//                } else if (count == 3) {
//                    fileTemplate = template;
//                    fileTemplateType = Constants.FINGER_TEMPLATE_TYPE_ISO;
//                    count = 0;
//                } else {
                    fileTemplate = template;
                    fileTemplateType = Constants.FINGER_TEMPLATE_TYPE_ISO;
//                }
                new HttpAsyncTask(BOPCardIssuanceReIssuanceConfirmationActivity.this).execute(
                        currCommand + "", getEncryptedMpin(),
                        product.getId(), cmob, cnic,
                        dcno, nadraSessionId != null ? nadraSessionId : "",
                        segmentCode, strFingerIndex != null ? strFingerIndex : "",
                        fileTemplateType != null ? fileTemplateType : "",
                        fileTemplate != null ? fileTemplate : "", NFIQuality, minutiaeCount);
            } else {
                new HttpAsyncTask(BOPCardIssuanceReIssuanceConfirmationActivity.this).execute(
                        currCommand + "", getEncryptedMpin(),
                        product.getId(), cmob, cnic,
                        dcno, nadraSessionId != null ? nadraSessionId : "",
                        segmentCode, strFingerIndex != null ? strFingerIndex : "",
                        fileTemplateType != null ? fileTemplateType : "",
                        fileTemplate != null ? fileTemplate : "", "", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
        }
    }


    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            count++;
            hideLoading();
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                final String errorCode = message.getCode();
                if (message.getNadraSessionId() != null && !message.getNadraSessionId().equals("") && (!message.getNadraSessionId().contains("null")
                        || !message.getNadraSessionId().contains("NULL"))) {
                }
                String description;
                if (message.getCode().equals("122") || message.getCode().equals("121") || message.getCode().equals("118")) {
                    if (message.getDescr().contains(",,")) {
                        splited = message.getDescr().split(",,");
                        description = splited[0];
                    } else {
                        description = message.getDescr();
                    }

                    if (message.getCode().equals("118")) {

                        String currentFinger = ApplicationData.currentFingerIndex;
                        for (int i = 1; splited.length > i; i++) {
                            if (splited[i].equals(currentFinger)) {
                                splited = ArrayUtils.remove(splited, i);
                                break;
                            }
                        }
                    }

                    createAlertDialog(description, Constants.KEY_LIST_ALERT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if ((splited != null && splited.length > 1) || errorCode.equals("122")) {
                                openBvsActivity();
                            } else {
                                goToMainMenu();
                            }
                        }
                    });
                } else if (message.getCode().equals("135")) {
                    createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ApplicationData.errorCount == 3) {
                                ApplicationData.errorCount = 0;
                                goToMainMenu();
                            } else {
                                openBvsActivity();
                            }
                        }
                    });

                } else if (message.getCode().equals("9050")) {
                    createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {
                        if (ApplicationData.errorCount == 3) {
                            ApplicationData.errorCount = 0;
                        }
                        finish();
                    });

                } else {
                    createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {
                        if (ApplicationData.errorCount == 3) {
                            ApplicationData.errorCount = 0;
                        }
                    });
                }
                hideLoading();
            } else {
                if (table != null && table.containsKey(Constants.ATTR_MSG)) {
                    createAlertDialog(table.get(Constants.ATTR_MSG).toString(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {
                        goToMainMenu();
                    });
                }
                hideLoading();
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

    @Override
    public void onMpinPopupClosed() {
        btnNext.setEnabled(true);
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {

        currCommand = Constants.CMD_BOP_ISSUANCE_REISSUANCE;
        decideBvs();
    }

    protected void decideBvs() {
        Intent intent = new Intent(BOPCardIssuanceReIssuanceConfirmationActivity.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_OTC) {

            if (resultCode == RESULT_CODE_SUCCESS_OTC) {
                fourFingersReceived = false;
                exhaustedFingers.clear();
                currCommand = Constants.CMD_BOP_ISSUANCE_REISSUANCE;
                processRequest();
            } else if (resultCode == RESULT_CODE_FAILED_OTC_FINGER_INDEXES) {
                if (data != null) {
                    fourFingersReceived = true;
                    fingers = data.getStringArrayListExtra("fingers");
                    fingerIndexes = data.getIntegerArrayListExtra("validFingerIndexes");
                    final String errorCode = data.getStringExtra("code");

                    if (exhaustedFingers.size() == 0) {
                        Intent intent = new Intent(BOPCardIssuanceReIssuanceConfirmationActivity.this, FingerScanActivity.class);
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
                        Intent intent = new Intent(BOPCardIssuanceReIssuanceConfirmationActivity.this, FingerScanActivity.class);
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

                            Intent intent = new Intent(BOPCardIssuanceReIssuanceConfirmationActivity.this, FingerScanActivity.class);
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
                                            Intent intent = new Intent(BOPCardIssuanceReIssuanceConfirmationActivity.this, FingerScanActivity.class);
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

                Intent intent = new Intent(BOPCardIssuanceReIssuanceConfirmationActivity.this, OTCClient.class);
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
                intent.putExtra(Constants.IntentKeys.CONTACT_NUMBER, cmob != null ? cmob : "");
                intent.putExtra(Constants.IntentKeys.SECONDARY_IDENTIFIER, cnic);
                intent.putExtra(Constants.IntentKeys.SECONDARY_CONTACT_NUMBER, cmob);
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
                } else if (biometricFlow.equals(Constants.SUPREMA)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA;
                    currCommand = Constants.CMD_BOP_ISSUANCE_REISSUANCE;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.P41M2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.P41M2;
                    currCommand = Constants.CMD_BOP_ISSUANCE_REISSUANCE;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.SUPREMA_SLIM_2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA_SLIM_2;

                    currCommand = Constants.CMD_BOP_ISSUANCE_REISSUANCE;
                    openBvsActivity();
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_BVS) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    strFingerIndex = (String) data.getExtras().get(XmlConstants.ATTR_FINGER_INDEX);
                    fileTemplate = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE);
                    template = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE);
                    fileTemplateType = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE_TYPE);
                    if (ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.P41M2) || ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.SUPREMA_SLIM_2)) {
                        strWSQ = ApplicationData.WSQ;
                        minutiaeCount = data.getStringExtra(XmlConstants.ATTR_MINUTIAE_COUNT);
                        NFIQuality = data.getStringExtra(XmlConstants.ATTR_NFI_QUALITY);
                    }
                    processRequest();
                }
            }
        }
    }

    private void openBvsActivity() {
        Intent intent = new Intent(BOPCardIssuanceReIssuanceConfirmationActivity.this, BvsActivity.class);
        intent.putExtra(XmlConstants.ATTR_MOB_NO, cmob);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, product.getName());
        if (splited != null)
            intent.putExtra(XmlConstants.FINGERS, splited);
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
    }

    private void openBvsDialog() {
        fingers = Utility.getAllFingers();
        Intent intent = new Intent(BOPCardIssuanceReIssuanceConfirmationActivity.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }

    private void fetchIntents() {
        product = (ProductModel) mBundle.get(IntentKeys.PRODUCT_MODEL);
        cnic = mBundle.getString(IntentKeys.CNIC);
        dcno = mBundle.getString(IntentKeys.DCNO);
        cmob = mBundle.getString(IntentKeys.CMOB);
        segmentCode = mBundle.getString(IntentKeys.SEGMENT_CODE);
    }
}