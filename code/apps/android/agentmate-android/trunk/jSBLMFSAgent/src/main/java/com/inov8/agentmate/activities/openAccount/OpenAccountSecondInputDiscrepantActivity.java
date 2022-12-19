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

import static com.inov8.agentmate.util.Utility.testValidity;

public class OpenAccountSecondInputDiscrepantActivity extends BaseActivity {
	private TextView lblHeading, cardExpiry, dob;
	private Button btnNext;
	private EditText firstName, lastName, initialAmount;
	private CheckBox isCnicSeen;
	private int year, month, day;
	private String accountLevel = "1";
	private String cardExpiryFormatted, dobFormatted;
	private String intentCusName = null, intentCnicExpiry = null,
			intentDob = null;
	private String strInitialAmount;
	private ProductModel product;
	private ArrayList<String> imageList;
	private DatePickerDialog datePickerDob, datePickerExpiry;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_acount_open_input_second_discrepant);

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
						|| !testValidity(initialAmount)) {
					return;
				}

				/*
				 * Bug Fixed 3559 - For Discrepant Initial Amount 
				 * can be zero.
				 */
				if (product.getDoValidate().equals("1")
						&& (Integer.parseInt(initialAmount.getText()
								.toString()) > Double.parseDouble(Utility
								.getUnFormattedAmount(product.getMaxamt())) || Integer
								.parseInt(initialAmount.getText()
										.toString()) < 0)) {
                    initialAmount.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
					return;
				}
				if (Integer.parseInt(initialAmount.getText().toString()) < 0) {
                    initialAmount.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
					return;
				}

				long daysDifference = Utility.daysDifference(day + "-"
						+ (month + 1) + "-" + year, dob.getText().toString());
//				int age = Utility.ageDifference(day + "-" + (month + 1) + "-"
//						+ year, dob.getText().toString());

				if ((daysDifference / 365) < 18) { // age should not be less
													// then 18

					Toast.makeText(
							OpenAccountSecondInputDiscrepantActivity.this,
							Constants.Messages.ERROR_INVALID_CUSTOMER_DOB,
							Toast.LENGTH_SHORT).show();
					return;
				}

				daysDifference = Utility.daysDifference(day + "-" + (month + 1)
						+ "-" + year, cardExpiry.getText().toString());

				if (daysDifference >= 0) { // card expiry check

					Toast.makeText(
							OpenAccountSecondInputDiscrepantActivity.this,
							Constants.Messages.ERROR_INVALID_CARD_EXPEIRY,
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!isCnicSeen.isChecked()) {
					Toast.makeText(
							OpenAccountSecondInputDiscrepantActivity.this,
							Constants.Messages.ERROR_INVALID_ISCNIC_SEEN,
							Toast.LENGTH_SHORT).show();
					return;
				}

				Intent intent;

				if (imageList.size() > 0) {
					intent = new Intent(
							OpenAccountSecondInputDiscrepantActivity.this,
							OpenAccountPicInputUploadActivity.class);
				} else {
					intent = new Intent(
							OpenAccountSecondInputDiscrepantActivity.this,
							OpenAccountConfirmationDiscrepantActivity.class);
				}

				mBundle.putString(Constants.ATTR_CNAME, firstName.getText()
						.toString() + " " + lastName.getText().toString());
				intent.putExtra(Constants.ATTR_CDOB, dobFormatted);
				intent.putExtra(Constants.ATTR_CNIC_EXP, cardExpiryFormatted);
				intent.putExtra(Constants.ATTR_CUST_ACC_TYPE, accountLevel);
				intent.putExtra(Constants.ATTR_DEPOSIT_AMT, strInitialAmount);

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
	}

	private void fetchIntents() {
		product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);

		intentCusName = mBundle.getString(Constants.ATTR_CNAME);
		intentCnicExpiry = mBundle
				.getString(Constants.ATTR_CNIC_EXP + "_server");
		intentDob = mBundle.getString(Constants.ATTR_CDOB + "_server");
		strInitialAmount = mBundle.getString(Constants.ATTR_DEPOSIT_AMT
				+ "_server");
		accountLevel = mBundle.getString(Constants.ATTR_CUST_ACC_TYPE
				+ "_server");
		imageList = mBundle
				.getStringArrayList(Constants.IntentKeys.OPEN_ACCOUNT_IMAGES_LIST);

	}

	private void setDatePicker() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		cardExpiry = (TextView) findViewById(R.id.input4);
//		cardExpiry.setText(day + "-" + (month + 1) + "-" + year);

		datePickerExpiry = new DatePickerDialog(
				OpenAccountSecondInputDiscrepantActivity.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						cardExpiry.setText(dayOfMonth + "-"
								+ (monthOfYear + 1) + "-" + year);
						String monthst = (monthOfYear + 1) + "";
						String day = dayOfMonth + "";

						if ((dayOfMonth + "").length() == 1) {
							day = "0" + dayOfMonth;
						}
						if (((monthOfYear + 1) + "").length() == 1) {
							monthst = "0" + (monthOfYear + 1);
						}

						cardExpiryFormatted = year + "" + monthst + "" + day;
					}
				}, year, month, day);

        datePickerExpiry.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);

		datePickerDob = new DatePickerDialog(
				OpenAccountSecondInputDiscrepantActivity.this,
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

						dobFormatted = year + "" + monthst + "" + day;
					}
				}, year, month, day);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);

        datePickerDob.getDatePicker().setMaxDate(cal.getTime().getTime());

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
//		dob.setText(day + "-" + (month + 1) + "-" + year);

		String monthst = (month + 1) + "";
		String daylocal = day + "";

		if ((day + "").length() == 1) {
			daylocal = "0" + day;
		}
		if (((month + 1) + "").length() == 1) {
			monthst = "0" + (month + 1);
		}

		dobFormatted = year + "" + monthst + "" + daylocal;
		cardExpiryFormatted = year + "" + monthst + "" + daylocal;

		View dob_layout = findViewById(R.id.input3_layout);
		dob_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (datePickerDob != null && !datePickerDob.isShowing()) {
					datePickerDob.show();
				}
			}
		});
		if (intentDob != null) {// in case of bulk registration
			dob.setText(intentDob);
			StringTokenizer strk = new StringTokenizer(intentDob, "-");
			String dd = strk.nextToken();
			String mm = strk.nextToken();
			String yy = strk.nextToken();
			dobFormatted = yy + mm + dd;
		}

		if (intentCnicExpiry != null) {// in case of bulk registration
			cardExpiry.setText(intentCnicExpiry);
			StringTokenizer strk = new StringTokenizer(intentCnicExpiry, "-");
			String dd = strk.nextToken();
			String mm = strk.nextToken();
			String yy = strk.nextToken();
			cardExpiryFormatted = yy + mm + dd;
		}
	}

	private void setEditText() {
		firstName = (EditText) findViewById(R.id.input1);
		firstName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        disableCopyPaste(firstName);

		lastName = (EditText) findViewById(R.id.input2);
		lastName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        disableCopyPaste(lastName);

		initialAmount = (EditText) findViewById(R.id.input_initial_amt);
		// initial_amt_field
		// .setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
		initialAmount.setKeyListener(null);
		initialAmount.setText(strInitialAmount);
        disableCopyPaste(initialAmount);

		TextView txtview_hint_amt = (TextView) findViewById(R.id.lblField_initial_amt_hint);
		txtview_hint_amt.setText("(" + product.getMinamtf() + "PKR to "
				+ product.getMaxamtf() + "PKR)");

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
		if (accountLevel.equals("1")) {
			list.add("Level 0");
		} else {
			list.add("Level 1");
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnergps.setAdapter(dataAdapter);

	}
}