package com.inov8.agentmate.activities.fundsTransfer;

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
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsbl.sco.R;

import static com.inov8.agentmate.util.Utility.testValidity;

public class ReceiveCashInputActivity extends BaseCommunicationActivity {
	private TextView lblHeading, lblSubHeading, lblField1, lblField2,
			lblField3, lblField4, lblField5;
	private EditText input1, input2, input3, input4, input5;
	private String strTransactionId, strSenderCnic, strSenderMobileNumber,
			strReceiverCnic, strReceiverMobileNumber;
	private Button btnNext;
	private ProductModel product;
	private byte flowId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_input_funds_transfer);

		try {
			fetchIntents();

			lblHeading = (TextView) findViewById(R.id.lblHeading);
			lblHeading.setText("Receive Money");

			lblSubHeading = (TextView) findViewById(R.id.lblSubHeading);
			lblSubHeading.setVisibility(View.VISIBLE);
			lblSubHeading.setText("(" + product.getName() + ")");

			lblField1 = (TextView) findViewById(R.id.lblField1);
			lblField2 = (TextView) findViewById(R.id.lblField2);
			lblField3 = (TextView) findViewById(R.id.lblField3);
			lblField4 = (TextView) findViewById(R.id.lblField4);
			lblField5 = (TextView) findViewById(R.id.lblField5);

			switch (flowId) {
				case Constants.FLOW_ID_RM_SENDER_REDEEM:
					loadInputFields(true, true, true, false, false);
					break;

				case Constants.FLOW_ID_RM_RECEIVE_CASH:
					loadInputFields(true, false, false, true, true);
					break;
			}

			btnNext = (Button) findViewById(R.id.btnNext);
			btnNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					hideKeyboard(v);
					if ((input1 != null && !testValidity(input1))) {
						return;
					}
					if (input1 != null) {
						strTransactionId = input1.getText().toString();
					}
					if (strTransactionId != null
							&& strTransactionId.length() < Constants.MAX_LENGTH_TRANSACTION_ID) {
						input1.setError(Constants.Messages.INVALID_TRANSACTION_ID);
						return;
					}

					if(input2 != null && !testValidity(input2)){
						return;
					}
					if (input2 != null) {
						strSenderCnic = input2.getText().toString();
					}
					if (strSenderCnic != null && strSenderCnic.length() < Constants.MAX_LENGTH_CNIC){
						input2.setError(Constants.Messages.INVALID_CNIC);
						return;
					}

					if(input3 != null && !testValidity(input3)){
						return;
					}
					if (input3 != null) {
						strSenderMobileNumber = input3.getText().toString();
					}
					if (strSenderMobileNumber != null && strSenderMobileNumber
							.length() < Constants.MAX_LENGTH_MOBILE) {
						input3.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
						return;
					}
					if (strSenderMobileNumber != null && (strSenderMobileNumber.charAt(0) != '0'
							|| strSenderMobileNumber.charAt(1) != '3')) {
						input3.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
						return;
					}

					if(input4 != null && !testValidity(input4)){
						return;
					}
					if (input4 != null) {
						strReceiverCnic = input4.getText().toString();
					}
					if(strReceiverCnic != null && strReceiverCnic.length() < Constants.MAX_LENGTH_CNIC){
						input4.setError(Constants.Messages.INVALID_CNIC);
						return;
					}

					if(input5 != null && !testValidity(input5)){
						return;
					}
					if (input5 != null) {
						strReceiverMobileNumber = input5.getText().toString();
					}
					if(strReceiverMobileNumber != null &&
							strReceiverMobileNumber.length() < Constants.MAX_LENGTH_MOBILE){
						input5.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
						return;
					}
					if (strReceiverMobileNumber != null && (strReceiverMobileNumber.charAt(0) != '0'
							|| strReceiverMobileNumber.charAt(1) != '3')) {
						input5.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
						return;
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

		switch (flowId) {
			case Constants.FLOW_ID_RM_SENDER_REDEEM:
				new HttpAsyncTask(ReceiveCashInputActivity.this).execute(
						Constants.CMD_RECEIVE_MONEY_SENDER_REDEEM_INFO + "",
						product.getId(), ApplicationData.agentMobileNumber,
						strTransactionId, strSenderCnic, strSenderMobileNumber);
				break;

			case Constants.FLOW_ID_RM_RECEIVE_CASH:
				new HttpAsyncTask(ReceiveCashInputActivity.this).execute(
						Constants.CMD_RECEIVE_MONEY_RECEIVE_CASH_INFO + "",
						product.getId(), ApplicationData.agentMobileNumber,
						strTransactionId, strReceiverCnic, strReceiverMobileNumber);
				break;
		}
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
						false, message);
			} else {
				TransactionInfoModel transactionInfo = TransactionInfoModel
						.getInstance();

				switch (flowId) {
					case Constants.FLOW_ID_RM_SENDER_REDEEM:
						transactionInfo.ReceiveMoneySenderRedeem(
								table.get(Constants.ATTR_SWMOB).toString(),
								table.get(Constants.ATTR_SWCNIC).toString(),
								table.get(Constants.ATTR_RWMOB).toString(),
								table.get(Constants.ATTR_RWCNIC).toString(),
								table.get(Constants.ATTR_TRXID).toString(),
								table.get(Constants.ATTR_DATEF).toString(),
								table.get(Constants.ATTR_TIMEF).toString(),
								table.get(Constants.ATTR_TAMT).toString(), table
										.get(Constants.ATTR_TAMTF).toString(),
								table.get(Constants.ATTR_PID).toString());
						if(table.containsKey(XmlConstants.ATTR_IS_BVS_REQ))
							transactionInfo.setIsBvsReq(table.get(XmlConstants.ATTR_IS_BVS_REQ).toString());
						break;

					case Constants.FLOW_ID_RM_RECEIVE_CASH:
						transactionInfo.ReceiveMoneyReceiveCash(
								table.get(Constants.ATTR_SWMOB).toString(), table
										.get(Constants.ATTR_SWCNIC).toString(),
								table.get(Constants.ATTR_RWMOB).toString(), table
										.get(Constants.ATTR_RWCNIC).toString(),
								table.get(Constants.ATTR_TRXID).toString(), table
										.get(Constants.ATTR_DATEF).toString(),
								table.get(Constants.ATTR_TIMEF).toString(), table
										.get(Constants.ATTR_TXAM).toString(), table
										.get(Constants.ATTR_TXAMF).toString(),
								table.get(Constants.ATTR_TPAM).toString(), table
										.get(Constants.ATTR_TPAMF).toString(),
								table.get(Constants.ATTR_CAMT).toString(), table
										.get(Constants.ATTR_CAMTF).toString(),
								table.get(Constants.ATTR_TAMT).toString(), table
										.get(Constants.ATTR_TAMTF).toString(),
								table.get(Constants.ATTR_PID).toString(), table
										.get(Constants.ATTR_ISREG).toString());
						if(table.containsKey(XmlConstants.ATTR_IS_BVS_REQ))
							transactionInfo.setIsBvsReq(table.get(XmlConstants.ATTR_IS_BVS_REQ).toString());
						break;
				}

				Intent intent = new Intent(ReceiveCashInputActivity.this,
						ReceiveCashConfirmationActivity.class);
				intent.putExtras(mBundle);
				intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL,
						transactionInfo);
				intent.putExtra(Constants.IntentKeys.FLOW_ID, flowId);
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

	private void fetchIntents() {
		flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
		product = (ProductModel) mBundle
				.get(Constants.IntentKeys.PRODUCT_MODEL);
	}

	private void loadInputFields(boolean field1, boolean field2,
								 boolean field3, boolean field4, boolean field5) {
		if (field1) {
			lblField1.setText("Transaction ID");
			lblField1.setVisibility(View.VISIBLE);

			input1 = (EditText) findViewById(R.id.input1);
			input1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					Constants.MAX_LENGTH_TRANSACTION_ID) });
			input1.setVisibility(View.VISIBLE);
			disableCopyPaste(input1);
		}
		if (field2) {
			lblField2.setText("Sender CNIC");
			lblField2.setVisibility(View.VISIBLE);

			input2 = (EditText) findViewById(R.id.input2);
			input2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					Constants.MAX_LENGTH_CNIC) });
			input2.setVisibility(View.VISIBLE);
			disableCopyPaste(input2);
		}
		if (field3) {
			lblField3.setText("Sender Mobile Number");
			lblField3.setVisibility(View.VISIBLE);

			input3 = (EditText) findViewById(R.id.input3);
			input3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					Constants.MAX_LENGTH_MOBILE) });
			input3.setVisibility(View.VISIBLE);
			disableCopyPaste(input3);
		}
		if (field4) {
			lblField4.setText("Receiver CNIC");
			lblField4.setVisibility(View.VISIBLE);

			input4 = (EditText) findViewById(R.id.input4);
			input4.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					Constants.MAX_LENGTH_CNIC) });
			input4.setVisibility(View.VISIBLE);
			disableCopyPaste(input4);
		}
		if (field5) {
			lblField5.setText("Receiver Mobile Number");
			lblField5.setVisibility(View.VISIBLE);

			input5 = (EditText) findViewById(R.id.input5);
			input5.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					Constants.MAX_LENGTH_MOBILE) });
			input5.setVisibility(View.VISIBLE);
			disableCopyPaste(input5);
		}
	}
}