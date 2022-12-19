package com.inov8.jsblconsumer.activities.openAccount;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import static com.inov8.jsblconsumer.util.Utility.testValidity;


public class OpenAccountSecondInputActivity extends BaseActivity {
    private TextView lblHeading;
    private TextView cardExpiry;
    private TextView dob;
    private Button btnNext;
    private EditText firstName;
    private EditText lastName;
    private EditText initialAmount;
    private CheckBox isCnicSeen;
    private DatePickerDialog datePickerDOB, datePickerExpiry;
    private int year;
    private int month;
    private int day;
    private String accountLevel = "1";
    private String cardExpriryFormatted, dobFormatted, trxid;
    private String intentCusName = null, intentCnicExpiry = null;
    private boolean cameFromBvs = false;
    private ProductModel product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setSoftInputMode(
        // WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.setContentView(R.layout.acitivity_open_account_second_input);

        try {

            fetchIntents();

            titleImplementation("icon_customer_registration", "Account Opening", null, this);

            setEditText();// first name, last name

            setDatePicker();// dob, card expire date

//            setAccountLevelSpinner();// account level

            TextView lblProgress3=(TextView) findViewById(R.id.lblProgress3);
            lblProgress3.setText(getText(R.string.customer_details));

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (!testValidity(firstName) || !testValidity(lastName)) {
                        return;
                    }


                    long daysDifference = Utility.daysDifference(day + "-"
                            + (month + 1) + "-" + year, dob.getText().toString());
                    // int age = Utility.ageDifference(day + "-" + (month + 1) + "-"
                    // + year, dob.getText().toString());

                    if ((daysDifference / 365) < 18) { // age should not be less
                        // then 18

                        Toast.makeText(OpenAccountSecondInputActivity.this,
                                Constants.Messages.ERROR_INVALID_CUSTOMER_DOB,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    daysDifference = Utility.daysDifference(day + "-" + (month + 1)
                            + "-" + year, cardExpiry.getText().toString());

                    if (daysDifference >= 0) { // card expiry check

                        Toast.makeText(OpenAccountSecondInputActivity.this,
                                Constants.Messages.ERROR_INVALID_CARD_EXPEIRY,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Intent intent = new Intent(OpenAccountSecondInputActivity.this,
                            OpenAccountPicInputUploadActivity.class);

                    mBundle.putString(Constants.ATTR_CNAME, firstName.getText()
                            .toString() + " " + lastName.getText().toString());
                    intent.putExtra(Constants.ATTR_CDOB, dobFormatted);
                    intent.putExtra(Constants.ATTR_CNIC_EXP, cardExpriryFormatted);
                    intent.putExtra(Constants.ATTR_CUST_ACC_TYPE, accountLevel);

                    intent.putExtra(Constants.ATTR_CNIC_EXP_FORMATED, cardExpiry
                            .getText().toString());
                    intent.putExtra(Constants.ATTR_CDOB_FORMATED, dob.getText()
                            .toString());


                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });

            headerImplementation();
            addAutoKeyboardHideFunctionScrolling();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchIntents() {
        try {
            cameFromBvs = getIntent().getBooleanExtra(Constants.IntentKeys.CAME_FROM_BVS, false);

            product = (ProductModel) mBundle
                    .get(Constants.IntentKeys.PRODUCT_MODEL);

            intentCusName = mBundle.getString(Constants.ATTR_CNAME);
            intentCnicExpiry = mBundle.getString(Constants.ATTR_CNIC_EXP
                    + "_server");
            trxid = mBundle.getString(Constants.IntentKeys.TRXID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDatePicker() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        cardExpiry = (TextView) findViewById(R.id.tv_dateExpiry);
        //cardExpiry.setText(day + "-" + (month + 1) + "-" + year);

        datePickerExpiry = new DatePickerDialog(
                OpenAccountSecondInputActivity.this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        cardExpiry.setText(dayOfMonth + "-" + (monthOfYear + 1)
                                + "-" + year);
                        String monthst = (monthOfYear + 1) + "";
                        String day = dayOfMonth + "";

                        if ((dayOfMonth + "").length() == 1) {
                            day = "0" + dayOfMonth;
                        }
                        if (((monthOfYear + 1) + "").length() == 1) {
                            monthst = "0" + (monthOfYear + 1);
                        }

                        cardExpriryFormatted = year + "-" + monthst + "-" + day;
                    }
                }, year, month, day);

        datePickerExpiry.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);

        datePickerDOB = new DatePickerDialog(
                OpenAccountSecondInputActivity.this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-"
                                + year);
                        String monthst = (monthOfYear + 1) + "";
                        String day = dayOfMonth + "";

                        if ((dayOfMonth + "").length() == 1) {
                            day = "0" + dayOfMonth;
                        }
                        if (((monthOfYear + 1) + "").length() == 1) {
                            monthst = "0" + (monthOfYear + 1);
                        }

                        dobFormatted = year + "-" + monthst + "-" + day;
                    }
                }, year, month, day);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);

        datePickerDOB.getDatePicker().setMaxDate(cal.getTime().getTime());

        View card_expiry_layout = findViewById(R.id.rl_cnic);
        card_expiry_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (datePickerExpiry != null && !datePickerExpiry.isShowing()) {
                    datePickerExpiry.show();
                }
            }
        });

        dob = (TextView) findViewById(R.id.tv_date);
        //dob.setText(day + "-" + (month + 1) + "-" + year);

        String monthst = (month + 1) + "";
        String daylocal = day + "";

        if ((day + "").length() == 1) {
            daylocal = "0" + day;
        }
        if (((month + 1) + "").length() == 1) {
            monthst = "0" + (month + 1);
        }

        dobFormatted = year + "-" + monthst + "-" + daylocal;
        cardExpriryFormatted = year + "-" + monthst + "-" + daylocal;

        View dob_layout = findViewById(R.id.rl_date);
        dob_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (datePickerDOB != null && !datePickerDOB.isShowing()) {
                    hideKeyboard(v);
                    datePickerDOB.show();
                }
            }
        });

        if (intentCnicExpiry != null) {// in case of bulk registration
            cardExpiry.setText(intentCnicExpiry);
            StringTokenizer strk = new StringTokenizer(intentCnicExpiry, "-");
            String dd = strk.nextToken();
            String mm = strk.nextToken();
            String yy = strk.nextToken();
            cardExpriryFormatted = yy + "-" + mm + "-" + dd;
        }
    }

    private void setEditText() {
        firstName = (EditText) findViewById(R.id.input1);
        disableCopyPaste(firstName);
        firstName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

        lastName = (EditText) findViewById(R.id.input2);
        disableCopyPaste(lastName);
        lastName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});


        if (intentCusName != null) {// in case of bulk registration
            StringTokenizer stk = new StringTokenizer(intentCusName, " ");
            if (stk.hasMoreElements()) {
                firstName.setText(stk.nextToken());
            }
            String temp_lastname = "";
            int i = 0;
            while (stk.hasMoreElements()) {
                if (i == 0)
                    temp_lastname = temp_lastname + (String) stk.nextToken();
                else
                    temp_lastname = temp_lastname + " "
                            + (String) stk.nextToken();
                i++;
            }

            lastName.setText(temp_lastname);
        }
    }


}