package com.inov8.jsblconsumer.activities.openAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.activities.LoginActivity;
import com.inov8.jsblconsumer.activities.debitCardActivation.TermsConditionsDebitCardActivity;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

/**
 * Created by ALI REHAN on 1/23/2018.
 */

public class OpenAccountSuccessActivity extends BaseActivity {
    private String strMessage;
    private boolean isHRA;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_success);
        Button btnOk = (Button) findViewById(R.id.btnOK);
        TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
        TextView tvHeading = (TextView) findViewById(R.id.tvHeading);
        fetchIntents();
        tvMessage.setText(strMessage);
        tvHeading.setText("Congratulations!");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApplicationData.isLogin = false;
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("fromOpenAccount", true);
                intent.putExtra(Constants.IntentKeys.HRA, isHRA);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


//                goToLogin();

//                dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.DEBIT_CARD_ISSUENCE, AppMessages.DEBIT_CARD_REQUEST,
//                        OpenAccountSuccessActivity.this, getString(R.string.ok), getString(R.string.cancel), new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
////                                dialogGeneral.dismiss();
//                                finish();
//                                Intent intent = new Intent(getApplicationContext(), TermsConditionsDebitCardActivity.class);
//                                startActivity(intent);
//
//                            }
//                        }, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                ApplicationData.isLogin = false;
//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                intent.putExtra("fromOpenAccount",true);
//                                intent.putExtra(Constants.IntentKeys.HRA, isHRA);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//
//
//                            }
//                        }, false, PopupDialogs.Status.ALERT, false);

            }
        });
    }

    private void fetchIntents() {
        strMessage = mBundle.getString(Constants.IntentKeys.MESSAGE);
        isHRA = mBundle.getBoolean(Constants.IntentKeys.HRA);
    }
}
