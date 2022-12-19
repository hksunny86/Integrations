package com.inov8.agentmate.activities.agentTransfer;

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
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.R;

import static com.inov8.agentmate.util.Utility.testValidity;

public class TransferInInputActivity extends BaseCommunicationActivity {
	private TextView lblField1, lblHeading;
	private EditText input1;
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
			lblHeading.setText("Transfer In");
			
			lblField1 = (TextView) findViewById(R.id.lblField1);
			lblField1.setText("Amount");
			lblField1.setVisibility(View.VISIBLE);

			input1 = (EditText) findViewById(R.id.input1);
			input1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					Constants.MAX_AMOUNT_LENGTH) });
			input1.setVisibility(View.VISIBLE);
            disableCopyPaste(input1);

			btnNext = (Button) findViewById(R.id.btnNext);
			btnNext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!testValidity(input1)) {
						return;
					}
					String str = input1.getText() + "";

					if (!Utility.isNull(product.getDoValidate())
							&& product.getDoValidate().equals("1")) {
						if ((Integer.parseInt(input1.getText().toString()) > Double
								.parseDouble(Utility
										.getUnFormattedAmount(product
												.getMaxamt())) || Integer
								.parseInt(input1.getText().toString()) < Double
								.parseDouble(Utility
										.getUnFormattedAmount(product
												.getMinamt())))) {
                            input1.setError(Constants.Messages.ERROR_AMOUNT_INVALID);
							return;
						}
					} else {
						if (str != null && str.length() > 0) {
							Double amount = Double.parseDouble(input1.getText()
									.toString() + "");
							if (amount < 10 || amount > Constants.MAX_AMOUNT) {
                                input1.setError(Constants.Messages.AMOUNT_MAX_LIMIT);
								return;
							}
						}
					}

					hideKeyboard(v);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
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
		new HttpAsyncTask(TransferInInputActivity.this).execute(
				Constants.CMD_TRANSFER_IN_INFO + "", product.getId(),
				ApplicationData.agentMobileNumber, input1.getText().toString(),
				ApplicationData.bankId);
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
				createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT,
						false, null);
			} else {
				TransactionInfoModel transactionInfo = TransactionInfoModel.getInstance();
				transactionInfo.TransferIn(
						table.get(Constants.ATTR_BBACID).toString(), table.get(
								Constants.ATTR_COREACID).toString(), table.get(
								Constants.ATTR_TXAM).toString(), table.get(
								Constants.ATTR_TXAMF).toString(), table.get(
								Constants.ATTR_TPAM).toString(), table.get(
								Constants.ATTR_TPAMF).toString(), table.get(
								Constants.ATTR_CAMT).toString(), table.get(
								Constants.ATTR_CAMTF).toString(), table.get(
								Constants.ATTR_TAMT).toString(), table.get(
								Constants.ATTR_TAMTF).toString());
							
				Intent intent = new Intent(getApplicationContext(),
						TransferInConfirmationActivity.class);
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
	public void processNext() {
	}
}