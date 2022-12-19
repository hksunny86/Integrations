package com.inov8.agentmate.activities.openAccount;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

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
import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

import static com.inov8.agentmate.util.Utility.createAlertDialog;
import static com.inov8.agentmate.util.Utility.testValidity;

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
		super.setContentView(R.layout.activity_open_account_second_input);

        try {

            fetchIntents();

            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("Account Opening");

            setEditText();// first name, last name

            setDatePicker();// dob, card expire date

            setAccountLevelSpinner();// account level

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (!testValidity(firstName) || !testValidity(lastName)
                            || ((initialAmount.getVisibility() == View.VISIBLE) && (!testValidity(initialAmount)))) {
                        return;
                    }

                    if (initialAmount.getVisibility() == View.VISIBLE) {
                        if (product.getDoValidate().equals("1")
                                && (Integer
                                .parseInt(initialAmount.getText().toString()) > Double
                                .parseDouble(Utility
                                        .getUnFormattedAmount(product
                                                .getMaxamt())) || Integer
                                .parseInt(initialAmount.getText().toString()) < Double
                                .parseDouble(Utility
                                        .getUnFormattedAmount(product
                                                .getMinamt())))) {
                            initialAmount.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
                            return;
                        }
                        if (Integer.parseInt(initialAmount.getText().toString()) <= 0) {
                            initialAmount.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
                            return;
                        }
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

                    if (!isCnicSeen.isChecked()) {
                        Toast.makeText(OpenAccountSecondInputActivity.this,
                                Constants.Messages.ERROR_INVALID_ISCNIC_SEEN,
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
                    intent.putExtra(Constants.ATTR_DEPOSIT_AMT, initialAmount
                            .getText().toString());

                    intent.putExtra(Constants.ATTR_CNIC_EXP_FORMATED, cardExpiry
                            .getText().toString());
                    intent.putExtra(Constants.ATTR_CDOB_FORMATED, dob.getText()
                            .toString());

                    if (isCnicSeen.isChecked()) {
                        intent.putExtra(Constants.ATTR_IS_CNIC_SEEN, "1");
                    } else {
                        intent.putExtra(Constants.ATTR_IS_CNIC_SEEN, "0");
                    }
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });

            headerImplementation();
            addAutoKeyboardHideFunctionScrolling();
        }catch (Exception e){
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

        }catch (Exception e){
            e.printStackTrace();
        }
	}

	private void setDatePicker() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		cardExpiry = (TextView) findViewById(R.id.input4);
		//cardExpiry.setText(day + "-" + (month + 1) + "-" + year);

		datePickerExpiry = new DatePickerDialog(
				OpenAccountSecondInputActivity.this,
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
				OpenAccountSecondInputActivity.this,
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

		View card_expiry_layout = findViewById(R.id.input4_layout);
		card_expiry_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (datePickerExpiry != null && !datePickerExpiry.isShowing()) {
					datePickerExpiry.show();
				}
			}
		});

		dob = (TextView) findViewById(R.id.input3);
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

		View dob_layout = findViewById(R.id.input3_layout);
		dob_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (datePickerDOB != null && !datePickerDOB.isShowing()) {
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
		firstName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		disableCopyPaste(firstName);

		lastName = (EditText) findViewById(R.id.input2);
		lastName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        disableCopyPaste(lastName);

        TextView lblField_initial_amt = (TextView)findViewById(R.id.lblField_initial_amt);

		initialAmount = (EditText) findViewById(R.id.input_initial_amt);
		initialAmount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
        disableCopyPaste(initialAmount);
		
		TextView txtViewAmtHint = (TextView) findViewById(R.id.lblField_initial_amt_hint);

        if((product!=null && product.getMinamtf()!=null && !product.getMinamtf().equals(""))
                && (product.getMaxamtf()!=null && !product.getMaxamtf().equals("")))
        {
            txtViewAmtHint.setText("(" + product.getMinamtf() + " PKR to "
                    + product.getMaxamtf() + " PKR)");
        }
        else
            txtViewAmtHint.setVisibility(View.GONE);

		if (trxid != null || cameFromBvs) {
			lblField_initial_amt.setVisibility(View.GONE);
			txtViewAmtHint.setVisibility(View.GONE);
			initialAmount.setVisibility(View.GONE);
		}
		
		isCnicSeen = (CheckBox) findViewById(R.id.checkbox_cnicseen);

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

	private void setAccountLevelSpinner() {
		Spinner spinnergps = (Spinner) findViewById(R.id.spinneraccountLevel);
		List<String> list = new ArrayList<String>();
		list.add("Level 0");
		list.add("Level 1");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnergps.setAdapter(dataAdapter);

        if(cameFromBvs){
            findViewById(R.id.lblField5).setVisibility(View.GONE);
            spinnergps.setVisibility(View.GONE);
        }

		spinnergps.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {
					accountLevel = "1";
				} else if (position == 1) {
					accountLevel = "2";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}
}