package com.inov8.agentmate.activities.myAccount;

import java.util.Hashtable;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.TransactionReceiptActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.R;

public class MyAccountBalanceInquiryActivity extends BaseCommunicationActivity {
	private TextView lblHeading, lblBalance, lblDate;
	private Button btnOk;
	private String acctType = "";
	ProductModel mProduct;
	private TextView lblsubheading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_balance_inquiry);

		try {
			fetchIntents();

			lblHeading = (TextView) findViewById(R.id.lblHeading);
			lblHeading.setText("Balance Inquiry");

			lblsubheading = (TextView) findViewById(R.id.lblSubHeading);
			lblsubheading.setText("(" + mProduct.getName() + ")");

			lblBalance = (TextView) findViewById(R.id.lblcurrentbalance);
			lblDate = (TextView) findViewById(R.id.lbldate);
			btnOk = (Button) findViewById(R.id.btnok);
			if (mProduct.getId().equals(Constants.PRODUCT_ID_BLB_BALANCE_INQUIRY)) {
				acctType = "1";
			} else if (mProduct.getId().equals(Constants.PRODUCT_ID_CORE_BALANCE_INQUIRY)) {
				acctType = "0";
			}
			askMpin(mBundle, TransactionReceiptActivity.class, true);

			btnOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					goToMainMenu();
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
		}
		headerImplementation();
	}

	private void fetchIntents() {
		mProduct = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
	}

	public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
		processRequest();
	}

	@Override
	public void processRequest() {
		if (!haveInternet()) {
			Toast.makeText(getApplication(),
					Constants.Messages.INTERNET_CONNECTION_PROBLEM,
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		showLoading("Please Wait", "Processing...");
		new HttpAsyncTask(MyAccountBalanceInquiryActivity.this).execute(
				Constants.CMD_CHECK_BALANCE+"", getEncryptedMpin(),
				acctType, ApplicationData.userId, ApplicationData.accountId);
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
						true, message);
			} else {
				lblBalance.setText(Utility.getFormattedCurrency(Utility
						.getFormatedAmount(table.get(Constants.ATTR_BALF)
								.toString())));
				lblDate.setText(ApplicationData.getFormattedDate() + " "
						+ ApplicationData.getFormattedTime());

				// app balance
				if (acctType.equals("1")) {
					ApplicationData.formattedBalance = table.get(
							Constants.ATTR_BALF).toString();
				}

				RelativeLayout rlt = (RelativeLayout) findViewById(R.id.data_layout);
				rlt.setVisibility(View.VISIBLE);
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
