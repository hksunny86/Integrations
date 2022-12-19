package com.inov8.agentmate.activities.openAccount;

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

import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALI REHAN on 1/26/2018.
 */

public class OpenAccountHraActivity extends BaseActivity {
    private Button btnNext, btnCancel, btnAddOriginator1, btnAddOriginator2, btnAddOriginator3, btnAddOriginator4, btnAddOriginator5;
    private View layoutOriginator1, layoutOriginator2, layoutOriginator3, layoutOriginator4, layoutOriginator5;
    private ImageView ivRemove1, ivRemove2, ivRemove3, ivRemove4, ivRemove5;
    private EditText etOrgRel1, etOrgRel2, etOrgRel3, etOrgRel4, etOrgRel5;
    private EditText etOrgLoc1, etOrgLoc2, etOrgLoc3, etOrgLoc4, etOrgLoc5;
    private EditText etNextKin, etOccupation;
    private String strOrgRel1 = "", strOrgRel2 = "", strOrgRel3 = "", strOrgRel4 = "", strOrgRel5 = "";
    private String strOrgLoc1 = "", strOrgLoc2 = "", strOrgLoc3 = "", strOrgLoc4 = "", strOrgLoc5 = "";
    private String strNextKin = "", strOccupation = "";
    private String purposeOfAccount = "";
    private TextView lblHeading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_open_account_hra);
        headerImplementation();
        setUI();
        setPurposeOfAccountSpinner();
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

                if (!Utility.testValidity(etOccupation) || !Utility.testValidity(etNextKin)) {
                    return;
                }
                strNextKin = etNextKin.getText().toString();
                strOccupation = etOccupation.getText().toString();

                if (layoutOriginator1.getVisibility() == View.VISIBLE) {

                    if (!Utility.testValidity(etOrgLoc1) || !Utility.testValidity(etOrgRel1)) {
                        return;
                    }
                    strOrgLoc1 = etOrgLoc1.getText().toString();
                    strOrgRel1 = etOrgRel1.getText().toString();
                }

                if (layoutOriginator2.getVisibility() == View.VISIBLE) {
                    if (!Utility.testValidity(etOrgLoc2) || !Utility.testValidity(etOrgRel2)) {
                        return;
                    }
                    strOrgLoc2 = etOrgLoc2.getText().toString();
                    strOrgRel2 = etOrgRel2.getText().toString();

                }
                if (layoutOriginator3.getVisibility() == View.VISIBLE) {
                    if (!Utility.testValidity(etOrgLoc3) || !Utility.testValidity(etOrgRel3)) {
                        return;
                    }
                    strOrgLoc3 = etOrgLoc3.getText().toString();
                    strOrgRel3 = etOrgRel3.getText().toString();
                }
                if (layoutOriginator4.getVisibility() == View.VISIBLE) {
                    if (!Utility.testValidity(etOrgLoc4) || !Utility.testValidity(etOrgRel4)) {
                        return;
                    }
                    strOrgLoc4 = etOrgLoc4.getText().toString();
                    strOrgRel4 = etOrgRel4.getText().toString();
                }
                if (layoutOriginator5.getVisibility() == View.VISIBLE) {
                    if (!Utility.testValidity(etOrgLoc5) || !Utility.testValidity(etOrgRel5)) {
                        return;
                    }
                    strOrgLoc5 = etOrgLoc5.getText().toString();
                    strOrgRel5 = etOrgRel5.getText().toString();
                }


                Intent intent = getIntent();

                intent.putExtra(Constants.IntentKeys.PURPOSE_OF_ACCOUNT, purposeOfAccount);
                intent.putExtra(Constants.IntentKeys.OCCUPATION, strOccupation);
                intent.putExtra(Constants.IntentKeys.NEXT_OF_KIN, strNextKin);

                intent.putExtra(Constants.IntentKeys.ORG_LOC_1, strOrgLoc1);
                intent.putExtra(Constants.IntentKeys.ORG_LOC_2, strOrgLoc2);
                intent.putExtra(Constants.IntentKeys.ORG_LOC_3, strOrgLoc3);
                intent.putExtra(Constants.IntentKeys.ORG_LOC_4, strOrgLoc4);
                intent.putExtra(Constants.IntentKeys.ORG_LOC_5, strOrgLoc5);

                intent.putExtra(Constants.IntentKeys.ORG_REL_1, strOrgRel1);
                intent.putExtra(Constants.IntentKeys.ORG_REL_2, strOrgRel2);
                intent.putExtra(Constants.IntentKeys.ORG_REL_3, strOrgRel3);
                intent.putExtra(Constants.IntentKeys.ORG_REL_4, strOrgRel4);
                intent.putExtra(Constants.IntentKeys.ORG_REL_5, strOrgRel5);


                setResult(RESULT_OK, intent);

                finish();
            }
        });
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
        lblHeading.setText("Account Opening");
        etOccupation = (EditText) findViewById(R.id.etOccupation);
        etNextKin = (EditText) findViewById(R.id.etNextKin);

        disableCopyPaste(etOccupation);
        disableCopyPaste(etNextKin);

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
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void setPurposeOfAccountSpinner() {
        Spinner spPurposeOfAccount = (Spinner) findViewById(R.id.spPurposeOfAccount);
        List<String> list = new ArrayList<String>();
        list.add("Personal Payment");
        list.add("Family support");
        list.add("Medical Payment");
        list.add("Educational fees");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPurposeOfAccount.setAdapter(dataAdapter);


        spPurposeOfAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {

                purposeOfAccount = arg0.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}
