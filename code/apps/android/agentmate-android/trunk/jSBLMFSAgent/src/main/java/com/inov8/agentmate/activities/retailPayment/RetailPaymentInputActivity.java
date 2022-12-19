package com.inov8.agentmate.activities.retailPayment;

import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

import static com.inov8.agentmate.util.Utility.testValidity;

public class RetailPaymentInputActivity extends BaseCommunicationActivity {
	private TextView lblField1, lblField2, lblHeading;
	private EditText input1, input2;
	private Button btnNext;
	private ProductModel product;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_input_general);

		try {
			product = (ProductModel) mBundle
					.get(Constants.IntentKeys.PRODUCT_MODEL);

			lblHeading = (TextView) findViewById(R.id.lblHeading);
			lblHeading.setText("Retail Payment");

			lblField1 = (TextView) findViewById(R.id.lblField1);
			lblField1.setText("Mobile Number");
			lblField1.setVisibility(View.VISIBLE);

            input1 = (EditText) findViewById(R.id.input1);
            input1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
                    Constants.MAX_LENGTH_MOBILE) });
            input1.setVisibility(View.VISIBLE);
            disableCopyPaste(input1);

            lblField2 = (TextView) findViewById(R.id.lblField2);
			lblField2.setText("Amount");
			lblField2.setVisibility(View.VISIBLE);

			input2 = (EditText) findViewById(R.id.input2);
			input2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					Constants.MAX_AMOUNT_LENGTH) });
			input2.setVisibility(View.VISIBLE);
            disableCopyPaste(input2);

			btnNext = (Button) findViewById(R.id.btnNext);
			btnNext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!testValidity(input1)) {
						return;
					}

					if (input1.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
                        input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
						return;
					}

                    if (input1 != null && (input1.getText().toString().charAt(0) != '0'
                            || input1.getText().toString().charAt(1) != '3')) {
                        input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                        return;
                    }

                    if(!testValidity(input2)){
                        return;
                    }

					String str = input2.getText() + "";
					if (!Utility.isNull(product.getDoValidate())
							&& product.getDoValidate().equals("1")) {
						if ((Integer.parseInt(input2.getText().toString()) > Double
								.parseDouble(Utility
										.getUnFormattedAmount(product
												.getMaxamt())) || Integer
								.parseInt(input2.getText().toString()) < Double
								.parseDouble(Utility
										.getUnFormattedAmount(product
												.getMinamt())))) {
                            input2.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
							return;
						}
					} else {
						if (str != null && str.length() > 0) {
							Double amount = Double.parseDouble(input2.getText()
									.toString() + "");
							if (amount < 10 || amount > Constants.MAX_AMOUNT) {
                                input2.setError(Constants.Messages.AMOUNT_MAX_LIMIT);
								return;
							}
						}
					}

					mpinProcess(null, null);
				}
			});

			addAutoKeyboardHideFunction();
		} catch (Exception ex) {
			ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
		}
		headerImplementation();
		addAutoKeyboardHideFunctionScrolling();
	}

	public void mpinProcess(Bundle bundle, Class<?> nextClass) {
		processRequest();
	}

	@Override
	public void processRequest() {
		if (!haveInternet()) {
			Toast.makeText(getApplication(),
					Constants.Messages.INTERNET_CONNECTION_PROBLEM,
					Toast.LENGTH_SHORT).show();
			return;
		}

		showLoading("Please Wait", "Processing...");
		new HttpAsyncTask(RetailPaymentInputActivity.this).execute(
				Constants.CMD_RETAIL_PAYMENT_INFO + "", product.getId(), input1
						.getText().toString(), input2.getText().toString());
	}

	@Override
	public void processResponse(HttpResponseModel response) {
		try {
			XmlParser xmlParser = new XmlParser();
			Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
					.getXmlResponse());
			if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
				List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
				MessageModel message = (MessageModel) list.get(0);
				if (message.getCode().equals(
						Constants.ErrorCodes.WALK_IN_CUSTOMER)) {

					showRegistrationDialog(Constants.Messages.ALERT_HEADING,
							message.getDescr(), null, new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									gotoAccountOpening(input1.getText()
											.toString());
								}
							});
				} else {
					createAlertDialog(message.getDescr(),
							Constants.KEY_LIST_ALERT, false, null);
				}
			} else {
				TransactionInfoModel transactionInfo = TransactionInfoModel
						.getInstance();
				transactionInfo.RetailPayment(table.get(Constants.ATTR_CMOB)
						.toString(), table.get(Constants.ATTR_TXAM).toString(),
						table.get(Constants.ATTR_TXAMF).toString(),
						table.get(Constants.ATTR_TPAM).toString(),
						table.get(Constants.ATTR_TPAMF).toString(),
						table.get(Constants.ATTR_CAMT).toString(),
						table.get(Constants.ATTR_CAMTF).toString(),
						table.get(Constants.ATTR_TAMT).toString(),
						table.get(Constants.ATTR_TAMTF).toString());

				Intent intent = new Intent(getApplicationContext(),
						RetailPaymentConfirmationActivity.class);
				intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL,
						transactionInfo);
				intent.putExtras(mBundle);
				startActivity(intent);
			}
			hideLoading();
		} catch (Exception e) {
			hideLoading();
			e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
		}
		hideLoading();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void processNext() {
	}
}