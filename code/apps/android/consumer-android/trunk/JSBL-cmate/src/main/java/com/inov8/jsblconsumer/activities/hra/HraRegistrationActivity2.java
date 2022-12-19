package com.inov8.jsblconsumer.activities.hra;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.model.HraRegistrationModel;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;


public class HraRegistrationActivity2 extends BaseCommunicationActivity implements View.OnClickListener {

    private EditText kinName, kinMob,
            kinCnic;
    private Button btnNext, btnCancel;
    private TextView lblHeading;
    private HraRegistrationModel hraModel;
    private Spinner spinnerRelationship;
    private String[] spRelationshipTypes = {"Spouse", "Children", "Father", "Mother", "Relative", "Friend", "Sister", "Brother", "Others"};

    private String mob, cnic, relationship;

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

        spinnerRelationship = (Spinner) findViewById(R.id.spinner_relationship);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spRelationshipTypes);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRelationship.setAdapter(adapter1);

        spinnerRelationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                relationship = spRelationshipTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                relationship = spRelationshipTypes[0];
            }
        });


        kinMob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==11)
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
                hideKeyboard(v);
                if (!validate())
                    return;
                else{
                    hraModel.setKinName(kinName.getText().toString());
                    hraModel.setKinMob(mob!=null? mob: "");
                    hraModel.setKinCnic(cnic!=null? cnic: "");
                    hraModel.setKinRelationship(relationship);

                    Intent intent = new Intent(HraRegistrationActivity2.this, HraRegistrationActivity3.class);
                    mBundle.putParcelable(Constants.IntentKeys.HRA_MODEL, hraModel);
                    if (mBundle != null) {
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
    }



    @Override
    public void processResponse(HttpResponseModel response) {
    }

    @Override
    public void processNext() {
    }



    private boolean validate() {
        boolean validate = false;

        if (!Utility.testValidity(kinName)) {
            return validate;
        }
        if (kinMob.length() > 0){
            mob = kinMob.getText().toString();
            if(kinMob.length() < 11) {
                kinMob.setError(AppMessages.INVALID_MOBILE_NUMBER);
                return validate;
            }
            if((kinMob.getText().toString().charAt(0) != '0' ||
                    kinMob.getText().toString().charAt(1) != '3')) {
                kinMob.setError(AppMessages.INVALID_MOBILE_NUMBER_FORMAT);
                return validate;
            }
        }
        if(kinCnic.length() > 0){
            cnic = kinCnic.getText().toString();
            if (kinCnic.length() < 13) {
                kinCnic.setError(AppMessages.ERROR_CNIC_LENGTH);
                return validate;
            }
            else{
                validate = true;
            }
        }
        else{
            validate = true;
        }
        return validate;
    }

    @Override
    public void onBackPressed() {
        hideKeyboard(getCurrentFocus());
        goToMainMenu();
    }
}