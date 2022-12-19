package com.inov8.jsblconsumer.activities.registration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.R;

public class RegistrationReceiptActivity extends BaseActivity {

    private Button btnOK;
    private TextView tvMsg;
    private String msg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_receipt);

        fetchIntents();
        headerImplementation();
        titleImplementation(null, "Registration Successful", null, this);

        tvMsg = (TextView) findViewById(R.id.txtMsg);
        tvMsg.setText(msg);

        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStart();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goToStart();
    }

    private void fetchIntents() {
        msg = (String) mBundle.get(Constants.IntentKeys.TERMS);
    }
}
