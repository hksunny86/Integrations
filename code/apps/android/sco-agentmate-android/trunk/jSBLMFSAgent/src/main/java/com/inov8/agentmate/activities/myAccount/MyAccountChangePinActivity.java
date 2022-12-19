package com.inov8.agentmate.activities.myAccount;

import java.util.Hashtable;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.MainMenuActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AesEncryptor;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsbl.sco.R;

import static com.inov8.agentmate.util.Utility.testValidity;

public class MyAccountChangePinActivity extends BaseCommunicationActivity {
	private EditText oldPIN, newPIN, confirmPIN;
	private Button btnChangePIN;
	private TextView lblHeading;
	private TextView lblOldPIN;
	private TextView lblNewPIN;
	private TextView lblConfirmPIN;
	private String mPinChangeType = "";
	private ProductModel mProduct;
	private boolean mBothPinchange = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_my_account_change_pin);

		mPinChangeType = mBundle.getString(Constants.IntentKeys.PIN_CHANGE_TYPE);

		mProduct = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
		mBothPinchange = mBundle
				.getBoolean(Constants.IntentKeys.BOTH_PIN_CHANGE);

		lblHeading = (TextView) findViewById(R.id.lblHeading);
		lblHeading.setText(mProduct.getName());

		oldPIN = ((EditText) findViewById(R.id.txtOldPIN));
		newPIN = ((EditText) findViewById(R.id.txtNewPIN));
		confirmPIN = ((EditText) findViewById(R.id.txtConfirmPIN));
		lblOldPIN = ((TextView) findViewById(R.id.lblOldPIN));
		lblNewPIN = ((TextView) findViewById(R.id.lblNewPIN));
		lblConfirmPIN = ((TextView) findViewById(R.id.lblConfirmPIN));

		disableCopyPaste(oldPIN);
		disableCopyPaste(newPIN);
		disableCopyPaste(confirmPIN);

		btnChangePIN = (Button) findViewById(R.id.btnChangePIN);
		if (mPinChangeType.equals(Constants.SET_MPIN)) {
			btnChangePIN.setText("SET MPIN");
			lblOldPIN.setText("New MPIN");
			lblNewPIN.setText("Confirm New MPIN");
			lblConfirmPIN.setVisibility(View.GONE);
			confirmPIN.setVisibility(View.GONE);
			oldPIN.setMinEms(4);
			oldPIN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
			oldPIN.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			newPIN.setMinEms(4);
			newPIN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
			newPIN.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
		}
		btnChangePIN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean valid = true;
				String temp = "Invalid:";
				hideKeyboard(v);
				if (!testValidity(oldPIN) & !testValidity(newPIN)
						& !testValidity(confirmPIN)) {
					return;
				}
				if (oldPIN.isShown()) {
					if (oldPIN.length() < Constants.MAX_LENGTH_PIN) {
						oldPIN.setError(Constants.Messages.INVALID_PIN);
						return;
					}
				}
				if (newPIN.length() < Constants.MAX_LENGTH_PIN) {
					newPIN.setError(Constants.Messages.INVALID_PIN);
					return;
				}
				if (confirmPIN.isShown()) {
					if (confirmPIN.length() < Constants.MAX_LENGTH_PIN) {
						confirmPIN.setError(Constants.Messages.INVALID_PIN);
						return;
					}
				}

				if (valid) {

					if (!confirmPIN.isShown()) {
						if (!(oldPIN.getText() + "").equals(newPIN.getText()
								+ "")) {
							valid = false;
							createAlertDialog("New and Confirm PIN Mismatch.",
									"PIN Change");
							return;
						}
					} else {
						if (!(newPIN.getText() + "").equals(confirmPIN.getText()
								+ "")) {
							valid = false;
							createAlertDialog("New and Confirm PIN Mismatch.",
									"PIN Change");
							return;
						}
					}

					if (valid) {
						processRequest();
						return;
					}
				}
			}
		});
		addAutoKeyboardHideFunction();
		headerImplementation();
		addAutoKeyboardHideFunctionScrolling();
	}

	@Override
	public void processRequest() {
		if (!haveInternet()) {
			Toast.makeText(getApplication(),
					Constants.Messages.INTERNET_CONNECTION_PROBLEM,
					Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			String encryptedOldPin = AesEncryptor.encrypt(oldPIN.getText()
					.toString());
			String encryptedNewPin = AesEncryptor.encrypt(newPIN.getText()
					.toString());
			String encryptedConfirmPin = AesEncryptor.encrypt(confirmPIN
					.getText().toString());
			showLoading("Please Wait", "Processing...");

			int command = -1;
			if (mPinChangeType.equals("0")) {// zero is agentmate pin
				command = Constants.CMD_CHANGE_PIN;
			} else if (mPinChangeType.equals(Constants.SET_MPIN)) {
				command = Constants.CMD_SET_MPIN;
			} else {
				command = Constants.CMD_CHANGE_MPIN;
			}

			new HttpAsyncTask(MyAccountChangePinActivity.this).execute(
					command + "", encryptedOldPin, encryptedNewPin,
					encryptedConfirmPin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processResponse(HttpResponseModel response) {
		try {
			XmlParser xmlParser = new XmlParser();
			String xmlResponse = response.getXmlResponse();
			Hashtable<?, ?> table = xmlParser.convertXmlToTable(xmlResponse);

			AppLogger.i("\nXMLResponse: " + xmlResponse);
			if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
				List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
				MessageModel message = (MessageModel) list.get(0);
				hideLoading();
				createAlertDialog(message.getDescr(), "Alert");
			} else {
				try {
					if (table != null
							&& table.containsKey(Constants.KEY_LIST_MSGS)) {
						List<?> list = (List<?>) table
								.get(Constants.KEY_LIST_MSGS);
						MessageModel message = (MessageModel) list.get(0);
						String msgStr = message.getDescr();
						hideLoading();

						ApplicationData.isPinChangeRequired = "1";// why
						// are we changing this (ask soofia farooq)
						alert = new AlertDialog.Builder(
								MyAccountChangePinActivity.this);
						alert.setTitle("PIN Changed");
						alert.setMessage(msgStr);
						alert.setCancelable(false);
						alert.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
														int which) {
										dialog.cancel();
										processNext();
									}
								});
						alert.show();
					}
				} catch (Exception e) {
					hideLoading();
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			hideLoading();
			e.printStackTrace();
			createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
		}
	}

	@Override
	public void processNext() {
		if (!mBothPinchange) {
			Intent intent = new Intent(getApplicationContext(),
					MainMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else if (!mBothPinchange && (mPinChangeType.equals(Constants.SET_MPIN))) {
			Intent intent;
			ApplicationData.isLogin = true;
			intent = new Intent(MyAccountChangePinActivity.this, MainMenuActivity.class);

			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else {

			mBundle = new Bundle();

			Intent intent = new Intent(MyAccountChangePinActivity.this,
					MyAccountChangePinActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, new ProductModel(
					Constants.PRODUCT_ID_CHANGE_MPIN, "15", "Change MPIN"));
			intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, "1");

			intent.putExtras(mBundle);

			startActivity(intent);
			finish();
		}
	}
}