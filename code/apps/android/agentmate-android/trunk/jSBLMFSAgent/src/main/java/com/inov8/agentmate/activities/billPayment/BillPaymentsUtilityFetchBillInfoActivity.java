package com.inov8.agentmate.activities.billPayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

import static com.inov8.agentmate.util.Utility.testValidity;

public class BillPaymentsUtilityFetchBillInfoActivity extends
		BaseCommunicationActivity {
	private TextView lblField1, lblField2, lblHeading, lblSubHeading;
	private EditText input1, input2, input3;
	private Button btnNext;
	List<HashMap<String, String>> aList = null;
	ListView listView = null;
	private Byte flowId;
	private ProductModel mProduct;
	private TextView lblField3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_input_general);

        try {
            fetchintents();

            aList = new ArrayList<HashMap<String, String>>();

            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("Bill Payment");

            lblSubHeading = (TextView) findViewById(R.id.lblSubHeading);
            lblSubHeading.setVisibility(View.VISIBLE);
            lblSubHeading.setText("(" + mProduct.getName() + ")");

            lblField1 = (TextView) findViewById(R.id.lblField1);
            if (mProduct.getLabel() != null) {
                lblField1.setText(mProduct.getLabel() + "");
            } else {
                lblField1.setText("Consumer No.");
            }
            lblField1.setVisibility(View.VISIBLE);

            input1 = (EditText) findViewById(R.id.input1);
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CONSUMER_NUMBER)});
            input1.setVisibility(View.VISIBLE);
            disableCopyPaste(input1);

            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField2.setText("Customer Mobile Number");
            lblField2.setVisibility(View.VISIBLE);

		/* for products which have alpha numeric reference number */
            setProductTypeFilter();

            input2 = (EditText) findViewById(R.id.input2);
            input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_MOBILE)});
            input2.setVisibility(View.VISIBLE);
            disableCopyPaste(input2);

            setAmountInput();

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!testValidity(input1) || !testValidity(input2)) {
                        return;
                    }

                    if (input2.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
                        input2.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                        return;
                    }

                    if (input2.getText().toString().charAt(0)!='0' || input2.getText().toString().charAt(1) !='3' ){
                        input2.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                        return;
                    }

                    if (input3 != null) {
                        if (!testValidity(input3)) {
                            return;
                        }

                        if (!Utility.isNull(mProduct.getDoValidate()) && mProduct.getDoValidate().equals("1")
                                && (Integer.parseInt(input3.getText().toString()) > Double
                                .parseDouble(Utility
                                        .getUnFormattedAmount(mProduct
                                                .getMaxamt())) || Integer
                                .parseInt(input3.getText().toString()) < Double
                                .parseDouble(Utility
                                        .getUnFormattedAmount(mProduct
                                                .getMinamt())))) {
                            input3.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
                            return;
                        }
                        if (Integer.parseInt(input3.getText().toString()) <= 0) {
                            input3.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
                            return;
                        }

                        if (!isNull(mProduct.getMultiple())) { // multiple check
                            if (!checkMutliple(input3.getText().toString(), mProduct.getMultiple())) {
                                input3.setError(Constants.Messages.ERROR_MULTIPLE);
                                return;
                            }
                        }

                        mBundle.putString(Constants.IntentKeys.TRANSACTION_AMOUNT,
                                input3.getText().toString());
                    }

                    mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);
                    mBundle.putString("CONSUMER_NO", input1.getText().toString());
                    mBundle.putString("MOBILE_NO", input2.getText().toString());

                    Intent intent = new Intent(
                            BillPaymentsUtilityFetchBillInfoActivity.this,
                            BillPaymentTypeActivity.class);

                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
            addAutoKeyboardHideFunction();
            headerImplementation();
            addAutoKeyboardHideFunctionScrolling();

        }catch (Exception e){
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
	}

	private void setProductTypeFilter() {
		if (mProduct.getType()!= null && mProduct.getType().equals("ALPHANUMERIC")) {
			InputFilter filter = new InputFilter() {
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					if (end > start) {
						for (int i = start; i < end; i++) {

							for (int index = start; index < end; index++) {
								if (!new String(
										"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-/")
										.contains(String.valueOf(source
												.charAt(index)))) {
									return "";
								}
							}
						}
					}
					return null;
				}
			};

			input1.setFilters(new InputFilter[] { filter,
					new InputFilter.LengthFilter(50) });
			input1.setInputType(InputType.TYPE_CLASS_TEXT);
		}
	}

	private void setAmountInput() {
		if (mProduct.getAmtRequired().equals("1")) {
			lblField3 = (TextView) findViewById(R.id.lblField3);
			lblField3.setVisibility(View.VISIBLE);
			if (mProduct.getMinamtf() != null
					&& !mProduct.getMinamtf().equals("")) {
				lblField3.setText("Amount (" + mProduct.getMinamtf()
						+ " PKR to " + mProduct.getMaxamtf() + " PKR)");
			} else {
				lblField3.setText("Amount");
			}

			input3 = (EditText) findViewById(R.id.input3);
			input3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					Constants.MAX_AMOUNT_LENGTH) });
			input3.setVisibility(View.VISIBLE);
            disableCopyPaste(input3);

			if (!isNull( mProduct.getMultiple())) {
				TextView txtview_multiple = (TextView) findViewById(R.id.lblAlertmultiple);
				txtview_multiple.setVisibility(View.VISIBLE);
				txtview_multiple.setText("(Multiple of "+mProduct.getMultiple()+")");
			}
		}
	}


private boolean isNull(String edittxt) {

	if (edittxt != null && !edittxt.equals("null") && !edittxt.equals(""))
		return false;

	else
		return true;
}

	private boolean checkMutliple(String amountstring, String mutiplestring) {
		if (amountstring.equals(""))
			return true;

		Double amount = Double.parseDouble(amountstring);
		Double mutiple = Double.parseDouble(Utility
				.getUnFormattedAmount(mutiplestring));

		if (amount % mutiple == 0)
			return true;
		else
			return false;
	}

	void fetchintents() {
		mProduct = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
		flowId = mBundle.getByte("FLOW_ID");
	}

	@Override
	public void processRequest() {
	}

	@Override
	public void processResponse(HttpResponseModel response) {
		try {
			// XmlParser xmlParser = new XmlParser();
			// Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
			// .getXmlResponse());
			// if (table != null && table.containsKey("errs")) {
			// List<?> list = (List<?>) table.get("errs");
			// Message message = (Message) list.get(0);
			// hideLoading();
			// createAlertDialog(message.getDescr(), "Alert");
			// // clearBillInfo(); issue#13 on issue tracking sheet
			// } else {
			// BAMT = table.get("BAMT").toString();
			// BAMTF = table.get("BAMTF").toString();
			// CAMT = table.get("CAMT").toString();
			// TAMT = table.get("TAMT").toString();
			// TPAM = table.get("TPAM").toString();
			// dueDate = table.get("BDATE").toString();
			// CSCD = table.get("CSCD").toString();
			// int i = Integer.parseInt(table.get("BPAID").toString());
			// switch (i) {
			// case 0:
			// billStatus = "Unpaid";
			// break;
			// case 1:
			// billStatus = "Paid";
			// break;
			// }
			//
			// processNext();
			// }
			// hideLoading();
		} catch (Exception e) {
			// hideLoading();
			e.printStackTrace();
		}
	}

	@Override
	public void processNext() {
		// layoutBillingInformation = (RelativeLayout)
		// findViewById(R.id.layoutBillingInformation);
		// layoutBillingInformation.setVisibility(RelativeLayout.VISIBLE);
		// hideKeyboard(layoutBillingInformation);
		//
		// String[] labels = new String[] { "Consumer / \nRef No.", "Due Date",
		// "Bill Amount", "Status" };
		// final String[] data = new String[] { CSCD, dueDate, BAMTF +
		// Constants.CURRENCY,
		// billStatus };
		//
		// aList.clear();
		// // List<HashMap<String, String>> aList = new
		// ArrayList<HashMap<String, String>>();
		//
		//
		// lblBillingInfo= (TextView) findViewById(R.id.lblBillingInfo);
		// lblBillingInfo.setVisibility(View.VISIBLE);
		//
		// for (int i = 0; i < data.length; i++) {
		// HashMap<String, String> hm = new HashMap<String, String>();
		// hm.put("label", labels[i]);
		// hm.put("data", data[i]);
		// aList.add(hm);
		// }
		//
		// String[] from = { "label", "data" };
		// int[] to = { R.id.txtLabel, R.id.txtData };
		// adapter = new SimpleAdapter(getBaseContext(), aList,
		// R.layout.listview_layout_with_data, from, to);
		//
		//
		//
		// listView = (ListView) findViewById(R.id.dataList);
		// listView.setVisibility(View.VISIBLE);
		// listView.setAdapter(adapter);
		//
		// btnNext = (Button) findViewById(R.id.btnNext);
		// btnNext.setVisibility(View.VISIBLE);
		// btnNext.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(
		// BillPaymentsUtilityFetchBillInfoActivity.this,
		// BillPaymentUtilityInputActivity.class);
		// bundle.putString("PID", PID);
		// bundle.putString("CSCD", CSCD);
		// bundle.putString("dueDate", dueDate);
		// bundle.putString("BAMT", BAMT);
		// bundle.putString("BAMTF", BAMTF);
		// bundle.putString("CAMT", CAMT);
		// bundle.putString("TAMT", TAMT);
		// bundle.putString("TPAM", TPAM);
		// bundle.putString("billStatus", billStatus);
		//
		// intent.putExtras(bundle);
		// startActivity(intent);
		// }
		// });
	}

	@SuppressWarnings("unused")
	private void clearBillInfo() {
		try {
			// aList.clear();
			// adapter.notifyDataSetChanged();
			// listView.setVisibility(View.GONE);
			// btnNext.setVisibility(View.GONE);
			// lblBillingInfo.setVisibility(View.GONE);
		} catch (Exception e) {
			AppLogger.e(e);
		}
	}

}