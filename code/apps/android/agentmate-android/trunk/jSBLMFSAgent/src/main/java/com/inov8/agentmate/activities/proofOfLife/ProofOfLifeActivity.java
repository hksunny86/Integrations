package com.inov8.agentmate.activities.proofOfLife;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.BvsDeviceSelectorActivity1;
import com.inov8.agentmate.activities.TransactionReceiptActivity;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.bvs.paysys.FingerScanActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsblmfs.R;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Constants.REQUEST_CODE_FINGER_SCAN;

public class ProofOfLifeActivity extends BaseCommunicationActivity implements MpinInterface {

    private ProductModel product;
    private TextView msg, lblHeading;
    private ArrayList<String> exhaustedFingers = new ArrayList<String>();
    private CheckBox acknowledged;
    private String nadraSessionId = null, thirdPartyTransactionId = null;
    private ArrayList<Integer> fingerIndexes;
    private int currCommand;
    private int count = 0;
    private String fileTemplate = "", template = "";
    private String fileTemplateType = "";
    private String strWSQ = "";
    private String minutiaeCount = "";
    private String NFIQuality = "";
    private Button btnNext;
    private boolean fourFingersReceived = false;
    private String[] splited = null;
    private ArrayList<String> fingers;
    private String message, biometricFlow, strFingerIndex, cmob, cnic, segmentId, macAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_of_life);

        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
            message = mBundle.getString(Constants.IntentKeys.MSG, "");
            cmob = mBundle.getString(Constants.IntentKeys.CMOB, "");
            cnic = mBundle.getString(Constants.IntentKeys.CNIC, "");
            segmentId = mBundle.getString(Constants.IntentKeys.SEGMENT_CODE, "");
            macAddress = mBundle.getString(Constants.IntentKeys.MAC_ADDRESS, "");

            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText(product.getName());

            msg = (TextView) findViewById(R.id.msg);
            msg.setText(message);

            acknowledged = (CheckBox) findViewById(R.id.checkAcknowledged);

            exhaustedFingers.clear();

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(v -> {

                if (!acknowledged.isChecked()) {
                    createAlertDialog("Please agree to the statement.", Constants.KEY_LIST_ALERT, false);
                    return;
                }
                decideBvs();
            });

            addAutoKeyboardHideFunction();

        } catch (Exception ex) {
            ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
        headerImplementation();
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
        if (currCommand == Constants.CMD_PROOF_OF_LIFE) {
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
 //               }
            }
            new HttpAsyncTask(ProofOfLifeActivity.this).execute(currCommand + "", getEncryptedMpin(),product.getId(), cmob,
                    cnic, segmentId, ApplicationData.latitude, ApplicationData.longitude, android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL, macAddress,
                    strFingerIndex, template, fileTemplateType, "yes", nadraSessionId != null ? nadraSessionId : "");
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            count++;
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                final String errorCode = message.getCode();
                if (message.getNadraSessionId() != null && !message.getNadraSessionId().equals("") && (!message.getNadraSessionId().contains("null")
                        || !message.getNadraSessionId().contains("NULL")))
                    nadraSessionId = message.getNadraSessionId();
                if (message.getThirdPartyTransactionId() != null && !message.getThirdPartyTransactionId().equals("") && (!message.getThirdPartyTransactionId().contains("null")
                        || !message.getThirdPartyTransactionId().contains("NULL")))
                    thirdPartyTransactionId = message.getThirdPartyTransactionId();
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
            } else {
                createAlertDialog(table.get(Constants.ATTR_MSG).toString(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {
                  goToMainMenu();
                });
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
        Intent intent = new Intent(ProofOfLifeActivity.this, FingerScanActivity.class);
        intent.putStringArrayListExtra("fingers", fingers);
        startActivityForResult(intent, REQUEST_CODE_FINGER_SCAN);
    }

    protected void decideBvs() {
        Intent intent = new Intent(ProofOfLifeActivity.this, BvsDeviceSelectorActivity1.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_BIOMETRIC_SELECTION);
    }

    private void openBvsActivity() {
        Intent intent = new Intent(ProofOfLifeActivity.this, BvsActivity.class);
//        intent.putExtra(XmlConstants.ATTR_MOB_NO, cmob);
        intent.putExtra(XmlConstants.ATTR_APP_FLOW, product.getName());
        if (splited != null)
            intent.putExtra(XmlConstants.FINGERS, splited);
        startActivityForResult(intent, Constants.REQUEST_CODE_BVS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_BIOMETRIC_SELECTION) {
            if (resultCode == RESULT_OK) {
                biometricFlow = data.getExtras().getString(Constants.IntentKeys.BIOMETRIC_FLOW);
                if (biometricFlow.equals(Constants.PAYSYS)) {
                    openBvsDialog();
                } else if (biometricFlow.equals(Constants.SUPREMA)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA;
                    currCommand = Constants.CMD_PROOF_OF_LIFE;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.P41M2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.P41M2;
                    currCommand = Constants.CMD_PROOF_OF_LIFE;
                    openBvsActivity();
                } else if (biometricFlow.equals(Constants.SUPREMA_SLIM_2)) {
                    ApplicationData.bvsDeviceName = Constants.BvsDevices.SUPREMA_SLIM_2;
                    currCommand = Constants.CMD_PROOF_OF_LIFE;
                    openBvsActivity();
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_BVS) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    strFingerIndex = (String) data.getExtras().get(XmlConstants.ATTR_FINGER_INDEX);
                    template = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE);
                    fileTemplateType = data.getStringExtra(XmlConstants.ATTR_FINGER_PRINT_TEMPLATE_TYPE);
                    if (ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.P41M2) || ApplicationData.bvsDeviceName.equals(Constants.BvsDevices.SUPREMA_SLIM_2)) {
                        strWSQ = ApplicationData.WSQ;
                        minutiaeCount = data.getStringExtra(XmlConstants.ATTR_MINUTIAE_COUNT);
                        NFIQuality = data.getStringExtra(XmlConstants.ATTR_NFI_QUALITY);
                    }
                    askMpin(mBundle, TransactionReceiptActivity.class, false, ProofOfLifeActivity.this);
                }
            }
        }
    }

    @Override
    public void onMpinPopupClosed() {
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        currCommand = Constants.CMD_PROOF_OF_LIFE;
        processRequest();
    }
}