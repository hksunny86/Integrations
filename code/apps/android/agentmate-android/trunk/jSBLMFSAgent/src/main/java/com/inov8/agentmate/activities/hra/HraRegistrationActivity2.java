package com.inov8.agentmate.activities.hra;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.HraRegistrationModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

public class HraRegistrationActivity2 extends BaseCommunicationActivity implements View.OnClickListener {

    private EditText kinName, kinMob,
            kinCnic, kinRelationship;
    private Button btnNext, btnCancel;
    private TextView lblHeading;
    private boolean isCashWithDraw;
    private ProductModel product;
    private HraRegistrationModel hraModel;
    private String mob, cnic, relationship, mobileNetwork, amount, hraLinkedRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hra_registration2);

        fetchIntents();
        setUI();
        headerImplementation();
    }

    private void fetchIntents() {
        try {
            hraModel = getIntent().getParcelableExtra(Constants.IntentKeys.HRA_MODEL);
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
            mobileNetwork = getIntent().getExtras().getString(Constants.ATTR_MOBILE_NETWORK);
            isCashWithDraw = getIntent().getExtras().getBoolean(Constants.IntentKeys.IS_CASH_WITHDRAW);
            amount = getIntent().getExtras().getString(Constants.IntentKeys.TRANSACTION_AMOUNT);
            hraLinkedRequest = getIntent().getExtras().getString(Constants.IntentKeys.HRA_LINKED_REQUEST);
        } catch (Exception e) {
        }
    }

    private void setUI() {
        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText("HRA Registration");

        kinName = (EditText) findViewById(R.id.input_kin_name);
        disableCopyPaste(kinName);

        kinMob = (EditText) findViewById(R.id.input_kin_mobile);
        disableCopyPaste(kinMob);

        kinCnic = (EditText) findViewById(R.id.input_kin_cnic);
        disableCopyPaste(kinCnic);

        kinRelationship = (EditText) findViewById(R.id.input_kin_relationship);
        disableCopyPaste(kinRelationship);

        kinMob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == Constants.MAX_LENGTH_MOBILE)
                    kinCnic.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        kinCnic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == Constants.MAX_LENGTH_CNIC)
                    kinRelationship.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnNext = (Button) findViewById(R.id.btnNext);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnNext.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (!validate())
                    return;
                else {
                    hraModel.setKinName(kinName.getText().toString());
                    hraModel.setKinMob(mob != null ? mob : "");
                    hraModel.setKinCnic(cnic != null ? cnic : "");
                    hraModel.setKinRelationship(relationship != null ? relationship : "");

                    Intent intent = new Intent(HraRegistrationActivity2.this, HraRegistrationActivity3.class);
                    mBundle.putParcelable(Constants.IntentKeys.HRA_MODEL, hraModel);
                    mBundle.putSerializable(Constants.IntentKeys.PRODUCT_MODEL, product);
                    if (mBundle != null) {
                        intent.putExtras(mBundle);
                        intent.putExtra(Constants.ATTR_MOBILE_NETWORK, mobileNetwork);
                        intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT, amount);
                        intent.putExtra(Constants.IntentKeys.HRA_LINKED_REQUEST, hraLinkedRequest);
                        intent.putExtra(Constants.IntentKeys.IS_CASH_WITHDRAW, isCashWithDraw);
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
    }

    @Override
    public void processResponse(HttpResponseModel response) {
    }

    @Override
    public void processNext() {
    }

    @Override
    public void headerImplementation() {
        btnHome = (ImageView) findViewById(R.id.imageViewHome);
        btnExit = (Button) findViewById(R.id.buttonsignout);

        if (!ApplicationData.isLogin) {
            btnExit.setVisibility(View.GONE);
        }
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                ApplicationData.webUrl = null;
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showConfirmExitDialog(null);
            }
        });
    }

    private boolean validate() {
        boolean validate = false;

        if (!Utility.testValidity(kinName)) {
            return validate;
        }
        if (kinMob.length() > 0) {
            mob = kinMob.getText().toString();
            if (kinMob.length() < 11) {
                kinMob.setError(AppMessages.INVALID_MOBILE_NUMBER);
                return validate;
            }
            if ((kinMob.getText().toString().charAt(0) != '0' ||
                    kinMob.getText().toString().charAt(1) != '3')) {
                kinMob.setError(AppMessages.INVALID_MOBILE_NUMBER_FORMAT);
                return validate;
            }
        }
        if (kinCnic.length() > 0) {
            cnic = kinCnic.getText().toString();
            if (kinCnic.length() < 13) {
                kinCnic.setError(AppMessages.INVALID_CNIC);
                return validate;
            } else {
                validate = true;
            }
        } else {
            validate = true;
        }
        relationship = kinRelationship.getText().toString();
        return validate;
    }

    @Override
    public void onBackPressed() {
        hideKeyboard(getCurrentFocus());
        goToMainMenu();
    }
}