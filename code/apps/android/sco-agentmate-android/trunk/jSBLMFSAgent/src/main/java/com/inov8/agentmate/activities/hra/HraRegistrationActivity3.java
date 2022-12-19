package com.inov8.agentmate.activities.hra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.cashOut.CashOutConfirmationActivity;
import com.inov8.agentmate.model.HraRegistrationModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.R;

import java.util.Hashtable;
import java.util.List;

public class HraRegistrationActivity3 extends BaseCommunicationActivity implements MpinInterface {
    private Button btnNext, btnCancel, btnAddOriginator1, btnAddOriginator2, btnAddOriginator3, btnAddOriginator4, btnAddOriginator5;
    private View layoutOriginator1, layoutOriginator2, layoutOriginator3, layoutOriginator4, layoutOriginator5;
    private ImageView ivRemove1, ivRemove2, ivRemove3, ivRemove4, ivRemove5;
    private ProductModel product;
    private EditText etOrgRel1, etOrgRel2, etOrgRel3, etOrgRel4, etOrgRel5;
    private EditText etOrgLoc1, etOrgLoc2, etOrgLoc3, etOrgLoc4, etOrgLoc5;
    private Spinner spRelationship1, spRelationship2, spRelationship3, spRelationship4, spRelationship5;
    private String[] spRelationshipTypes = {"Spouse", "Children", "Father", "Mother", "Relative", "Friend", "Sister", "Brother", "Others"};
    private EditText etNextKin, etOccupation;
    private String strOrgRel1 = "", strOrgRel2 = "", strOrgRel3 = "", strOrgRel4 = "", strOrgRel5 = "";
    private String strOrgLoc1 = "", strOrgLoc2 = "", strOrgLoc3 = "", strOrgLoc4 = "", strOrgLoc5 = "";
    private String mobileNetwork = "", amount = "", hraLinkedRequest = "" ;
    private TextView lblHeading;
    private boolean isCashWithDraw;
    private TextView lblRelationship1, lblRelationship2, lblRelationship3, lblRelationship4, lblRelationship5;
    private HraRegistrationModel hraModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_hra_registration3);
        headerImplementation();
        fetchIntents();
        setUI();

        btnNext = (Button) findViewById(R.id.btnNext);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        addOrigintor();
        removeOrigintor();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layoutOriginator1.getVisibility() == View.VISIBLE) {

                    if (!Utility.testValidity(etOrgLoc1)) {
                        return;
                    }
                    if (etOrgRel1.getVisibility() == View.VISIBLE) {
                        if (!Utility.testValidity(etOrgRel1))
                            return;
                    }
                    strOrgLoc1 = etOrgLoc1.getText().toString();
                    if (etOrgRel1.getVisibility() == View.VISIBLE)
                        strOrgRel1 = etOrgRel1.getText().toString();

                }

                if (layoutOriginator2.getVisibility() == View.VISIBLE) {
                    if (!Utility.testValidity(etOrgLoc2)) {
                        return;
                    }
                    if (etOrgRel2.getVisibility() == View.VISIBLE) {
                        if (!Utility.testValidity(etOrgRel2))
                            return;
                    }
                    strOrgLoc2 = etOrgLoc2.getText().toString();
                    if (etOrgRel2.getVisibility() == View.VISIBLE)
                        strOrgRel2 = etOrgRel2.getText().toString();

                }

                if (layoutOriginator3.getVisibility() == View.VISIBLE) {
                    if (!Utility.testValidity(etOrgLoc3)) {
                        return;
                    }
                    if (etOrgRel3.getVisibility() == View.VISIBLE) {
                        if (!Utility.testValidity(etOrgRel3))
                            return;
                    }
                    strOrgLoc3 = etOrgLoc3.getText().toString();
                    if (etOrgRel3.getVisibility() == View.VISIBLE)
                        strOrgRel3 = etOrgRel3.getText().toString();
                }
                if (layoutOriginator4.getVisibility() == View.VISIBLE) {
                    if (!Utility.testValidity(etOrgLoc4)) {
                        return;
                    }
                    if (etOrgRel4.getVisibility() == View.VISIBLE) {
                        if (!Utility.testValidity(etOrgRel4))
                            return;
                    }
                    strOrgLoc4 = etOrgLoc4.getText().toString();
                    if (etOrgRel4.getVisibility() == View.VISIBLE)
                        strOrgRel4 = etOrgRel4.getText().toString();
                }
                if (layoutOriginator5.getVisibility() == View.VISIBLE) {
                    if (!Utility.testValidity(etOrgLoc5)) {
                        return;
                    }
                    if (etOrgRel5.getVisibility() == View.VISIBLE) {
                        if (!Utility.testValidity(etOrgRel5))
                            return;
                    }
                    strOrgLoc5 = etOrgLoc5.getText().toString();
                    if (etOrgRel5.getVisibility() == View.VISIBLE)
                        strOrgRel5 = etOrgRel5.getText().toString();
                }

                hraModel.setOrg1Location(strOrgLoc1);
                hraModel.setOrg2Location(strOrgLoc2);
                hraModel.setOrg3Location(strOrgLoc3);
                hraModel.setOrg4Location(strOrgLoc4);
                hraModel.setOrg5Location(strOrgLoc5);
                hraModel.setOrg1Relation(strOrgRel1);
                hraModel.setOrg2Relation(strOrgRel2);
                hraModel.setOrg3Relation(strOrgRel3);
                hraModel.setOrg4Relation(strOrgRel4);
                hraModel.setOrg5Relation(strOrgRel5);

                btnNext.setEnabled(false);
                askMpin(null, null, false, HraRegistrationActivity3.this);

            }
        });
    }

    private void fetchIntents() {
        try {
            hraModel = mBundle.getParcelable(Constants.IntentKeys.HRA_MODEL);
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
            mobileNetwork = getIntent().getExtras().getString(Constants.ATTR_MOBILE_NETWORK);
            amount = getIntent().getExtras().getString(Constants.IntentKeys.TRANSACTION_AMOUNT);
            hraLinkedRequest = getIntent().getExtras().getString(Constants.IntentKeys.HRA_LINKED_REQUEST);
            isCashWithDraw = getIntent().getExtras().getBoolean(Constants.IntentKeys.IS_CASH_WITHDRAW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMpinPopupClosed() {
        btnNext.setEnabled(true);
    }

    @Override
    public void mpinProcess(Bundle bundle, Class<?> nextClass) {
        processRequest();
    }

    private void addOrigintor() {
        btnAddOriginator5.setVisibility(View.GONE);

        btnAddOriginator1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOriginator2.setVisibility(View.VISIBLE);
                btnAddOriginator1.setVisibility(View.GONE);
            }
        });

        btnAddOriginator2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOriginator3.setVisibility(View.VISIBLE);
                btnAddOriginator2.setVisibility(View.GONE);
            }
        });

        btnAddOriginator3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOriginator4.setVisibility(View.VISIBLE);
                btnAddOriginator3.setVisibility(View.GONE);
            }
        });

        btnAddOriginator4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOriginator5.setVisibility(View.VISIBLE);
                btnAddOriginator4.setVisibility(View.GONE);
            }
        });
    }


    private void removeOrigintor() {

        ivRemove1.setVisibility(View.GONE);

        ivRemove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOriginator2.setVisibility(View.GONE);
                btnAddOriginator1.setVisibility(View.VISIBLE);
            }
        });
        ivRemove3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOriginator3.setVisibility(View.GONE);
                btnAddOriginator2.setVisibility(View.VISIBLE);
            }
        });
        ivRemove4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOriginator4.setVisibility(View.GONE);
                btnAddOriginator3.setVisibility(View.VISIBLE);
            }
        });
        ivRemove5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOriginator5.setVisibility(View.GONE);
                btnAddOriginator4.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setUI() {
        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText("HRA Registration");

        layoutOriginator1 = (View) findViewById(R.id.layoutOriginator1);
        layoutOriginator2 = (View) findViewById(R.id.layoutOriginator2);
        layoutOriginator3 = (View) findViewById(R.id.layoutOriginator3);
        layoutOriginator4 = (View) findViewById(R.id.layoutOriginator4);
        layoutOriginator5 = (View) findViewById(R.id.layoutOriginator5);

        TextView tvHint1 = (TextView) layoutOriginator1.findViewById(R.id.tvHint);
        TextView tvHint2 = (TextView) layoutOriginator2.findViewById(R.id.tvHint);
        TextView tvHint3 = (TextView) layoutOriginator3.findViewById(R.id.tvHint);
        TextView tvHint4 = (TextView) layoutOriginator4.findViewById(R.id.tvHint);
        TextView tvHint5 = (TextView) layoutOriginator5.findViewById(R.id.tvHint);

        ivRemove1 = (ImageView) layoutOriginator1.findViewById(R.id.ivRemove);
        ivRemove2 = (ImageView) layoutOriginator2.findViewById(R.id.ivRemove);
        ivRemove3 = (ImageView) layoutOriginator3.findViewById(R.id.ivRemove);
        ivRemove4 = (ImageView) layoutOriginator4.findViewById(R.id.ivRemove);
        ivRemove5 = (ImageView) layoutOriginator5.findViewById(R.id.ivRemove);

        btnAddOriginator1 = (Button) layoutOriginator1.findViewById(R.id.btnAddOriginator);
        btnAddOriginator2 = (Button) layoutOriginator2.findViewById(R.id.btnAddOriginator);
        btnAddOriginator3 = (Button) layoutOriginator3.findViewById(R.id.btnAddOriginator);
        btnAddOriginator4 = (Button) layoutOriginator4.findViewById(R.id.btnAddOriginator);
        btnAddOriginator5 = (Button) layoutOriginator5.findViewById(R.id.btnAddOriginator);

        lblRelationship1 = (TextView) layoutOriginator1.findViewById(R.id.lbl_other_relationship);
        lblRelationship2 = (TextView) layoutOriginator2.findViewById(R.id.lbl_other_relationship);
        lblRelationship3 = (TextView) layoutOriginator3.findViewById(R.id.lbl_other_relationship);
        lblRelationship4 = (TextView) layoutOriginator4.findViewById(R.id.lbl_other_relationship);
        lblRelationship5 = (TextView) layoutOriginator5.findViewById(R.id.lbl_other_relationship);

        spRelationship1 = (Spinner) layoutOriginator1.findViewById(R.id.spinner_relationship);
        spRelationship2 = (Spinner) layoutOriginator2.findViewById(R.id.spinner_relationship);
        spRelationship3 = (Spinner) layoutOriginator3.findViewById(R.id.spinner_relationship);
        spRelationship4 = (Spinner) layoutOriginator4.findViewById(R.id.spinner_relationship);
        spRelationship5 = (Spinner) layoutOriginator5.findViewById(R.id.spinner_relationship);

        tvHint1.setText("Originator 1");
        tvHint2.setText("Originator 2");
        tvHint3.setText("Originator 3");
        tvHint4.setText("Originator 4");
        tvHint5.setText("Originator 5");

        etOrgLoc1 = (EditText) layoutOriginator1.findViewById(R.id.etOrgLoc);
        etOrgLoc2 = (EditText) layoutOriginator2.findViewById(R.id.etOrgLoc);
        etOrgLoc3 = (EditText) layoutOriginator3.findViewById(R.id.etOrgLoc);
        etOrgLoc4 = (EditText) layoutOriginator4.findViewById(R.id.etOrgLoc);
        etOrgLoc5 = (EditText) layoutOriginator5.findViewById(R.id.etOrgLoc);

        etOrgRel1 = (EditText) layoutOriginator1.findViewById(R.id.etOrgRel);
        etOrgRel2 = (EditText) layoutOriginator2.findViewById(R.id.etOrgRel);
        etOrgRel3 = (EditText) layoutOriginator3.findViewById(R.id.etOrgRel);
        etOrgRel4 = (EditText) layoutOriginator4.findViewById(R.id.etOrgRel);
        etOrgRel5 = (EditText) layoutOriginator5.findViewById(R.id.etOrgRel);

        disableCopyPaste(etOrgLoc1);
        disableCopyPaste(etOrgLoc2);
        disableCopyPaste(etOrgLoc3);
        disableCopyPaste(etOrgLoc4);
        disableCopyPaste(etOrgLoc5);
        disableCopyPaste(etOrgRel1);
        disableCopyPaste(etOrgRel2);
        disableCopyPaste(etOrgRel3);
        disableCopyPaste(etOrgRel4);
        disableCopyPaste(etOrgRel5);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(layoutOriginator1.getContext(), android.R.layout.simple_spinner_item, spRelationshipTypes);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRelationship1.setAdapter(adapter1);

        spRelationship1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spRelationshipTypes[position].equals(spRelationshipTypes[8])) {

                    lblRelationship1.setVisibility(View.VISIBLE);
                    etOrgRel1.setVisibility(View.VISIBLE);
                } else {

                    strOrgRel1 = spRelationshipTypes[position];
                    lblRelationship1.setVisibility(View.GONE);
                    etOrgRel1.setVisibility(View.GONE);
                    etOrgRel1.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strOrgRel1 = spRelationshipTypes[0];
            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(layoutOriginator2.getContext(), android.R.layout.simple_spinner_item, spRelationshipTypes);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRelationship2.setAdapter(adapter2);

        spRelationship2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spRelationshipTypes[position].equals(spRelationshipTypes[8])) {

                    lblRelationship2.setVisibility(View.VISIBLE);
                    etOrgRel2.setVisibility(View.VISIBLE);
                } else {

                    strOrgRel2 = spRelationshipTypes[position];
                    lblRelationship2.setVisibility(View.GONE);
                    etOrgRel2.setVisibility(View.GONE);
                    etOrgRel2.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strOrgRel2 = spRelationshipTypes[0];
            }
        });

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(layoutOriginator3.getContext(), android.R.layout.simple_spinner_item, spRelationshipTypes);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRelationship3.setAdapter(adapter3);

        spRelationship3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spRelationshipTypes[position].equals(spRelationshipTypes[8])) {

                    lblRelationship3.setVisibility(View.VISIBLE);
                    etOrgRel3.setVisibility(View.VISIBLE);
                } else {

                    strOrgRel3 = spRelationshipTypes[position];
                    lblRelationship3.setVisibility(View.GONE);
                    etOrgRel3.setVisibility(View.GONE);
                    etOrgRel3.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strOrgRel3 = spRelationshipTypes[0];
            }
        });

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(layoutOriginator4.getContext(), android.R.layout.simple_spinner_item, spRelationshipTypes);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRelationship4.setAdapter(adapter4);

        spRelationship4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spRelationshipTypes[position].equals(spRelationshipTypes[8])) {

                    lblRelationship4.setVisibility(View.VISIBLE);
                    etOrgRel4.setVisibility(View.VISIBLE);
                } else {

                    strOrgRel4 = spRelationshipTypes[position];
                    lblRelationship4.setVisibility(View.GONE);
                    etOrgRel4.setVisibility(View.GONE);
                    etOrgRel4.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strOrgRel4 = spRelationshipTypes[0];
            }
        });

        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(layoutOriginator5.getContext(), android.R.layout.simple_spinner_item, spRelationshipTypes);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRelationship5.setAdapter(adapter5);

        spRelationship5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spRelationshipTypes[position].equals(spRelationshipTypes[8])) {

                    lblRelationship5.setVisibility(View.VISIBLE);
                    etOrgRel5.setVisibility(View.VISIBLE);
                } else {

                    strOrgRel5 = spRelationshipTypes[position];
                    lblRelationship5.setVisibility(View.GONE);
                    etOrgRel5.setVisibility(View.GONE);
                    etOrgRel5.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strOrgRel5 = spRelationshipTypes[0];
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
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

            new HttpAsyncTask(this).execute(String.valueOf(Constants.CMD_L1_TO_HRA),
                    getEncryptedMpin(), hraModel.getMob(), hraModel.getCnic(), hraModel.getName(), hraModel.getDob(),
                    hraModel.getFatherName(), ApplicationData.agentMobileNumber,
                   "10245105", hraModel.getAccountPurpose(), hraModel.getOccupation(),
                    strOrgLoc1, strOrgLoc2, strOrgLoc3, strOrgLoc4, strOrgLoc5,
                    strOrgRel1, strOrgRel2, strOrgRel3, strOrgRel4, strOrgRel5,
                    hraModel.getIncomeSource(), hraModel.getKinName(), hraModel.getKinMob(),
                    hraModel.getKinCnic(), hraModel.getKinRelationship(), mobileNetwork);

        } catch (Exception e) {
            hideLoading();
            createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
            e.printStackTrace();
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            hideLoading();

            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                String code = message.getCode();
                AppLogger.i("##Error Code: " + code);

                createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, false, message);

            } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                hideLoading();
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);

                if (isCashWithDraw){

                    showLoading("Please Wait", "Processing...");
                    try {
                        new HttpAsyncTask(HraRegistrationActivity3.this).execute(
                                Constants.CMD_CASH_OUT_INFO + "", "10245106", hraModel.getMob(),
                                ApplicationData.agentMobileNumber, amount, "1", hraModel.getCnic(),hraLinkedRequest);
                    } catch (Exception e) {
                        hideLoading();
                        createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
                        e.printStackTrace();
                    }
                }
                else {
                PopupDialogs.createAlertDialog(message.getDescr(), "Message",
                        PopupDialogs.Status.INFO, this, new PopupDialogs.OnCustomClickListener() {
                            @Override
                            public void onClick(View v, Object obj) {

                                    goToMainMenu();

                            }
                        });}
            } else {
                TransactionInfoModel transactionInfo = TransactionInfoModel.getInstance();

                if (isCashWithDraw) {
                    transactionInfo.CashInOut(table.get(Constants.ATTR_CMOB)
                                    .toString(), table.get(Constants.ATTR_CNAME).toString(),
                            table.get(Constants.ATTR_CNIC).toString(),
                            table.get(Constants.ATTR_TXAM).toString(),
                            table.get(Constants.ATTR_TXAMF).toString(),
                            table.get(Constants.ATTR_TPAM).toString(),
                            table.get(Constants.ATTR_TPAMF).toString(),
                            table.get(Constants.ATTR_CAMT).toString(),
                            table.get(Constants.ATTR_CAMTF).toString(),
                            table.get(Constants.ATTR_TAMT).toString(),
                            table.get(Constants.ATTR_TAMTF).toString());

                    Intent intent = new Intent(getApplicationContext(), CashOutConfirmationActivity.class);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL, transactionInfo);
                    mBundle.putSerializable(Constants.IntentKeys.PRODUCT_MODEL,product);
                    mBundle.putByte(Constants.IntentKeys.FLOW_ID, Byte.parseByte(product.getFlowId()));
                    mBundle.putString(Constants.IntentKeys.CASH_OUT_TYPE,Constants.CASH_OUT_BY_IVR);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void processNext() {
    }
}
