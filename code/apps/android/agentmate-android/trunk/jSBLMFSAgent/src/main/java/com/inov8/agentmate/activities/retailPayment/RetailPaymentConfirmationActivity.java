package com.inov8.agentmate.activities.retailPayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.TransactionReceiptActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

public class RetailPaymentConfirmationActivity extends
		BaseCommunicationActivity implements MpinInterface{
	private String mobileNumber, amount, totalAmount, charges;
	private Button btnNext, btnCancel;
	private TextView lblHeading, lblAlert;
	private TransactionInfoModel transactionInfo;
	private ProductModel product;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_confirm);

		try {
			fetchIntents();
			lblHeading = (TextView) findViewById(R.id.lblHeading);
			lblHeading.setText("Confirm Transaction");

			lblAlert = (TextView) findViewById(R.id.lblAlert);
			lblAlert.setText("Please verify customer details.");

			mobileNumber = transactionInfo.getCmob();
			amount = transactionInfo.getTxamf();
			charges = transactionInfo.getTpamf();
			totalAmount = transactionInfo.getTamtf();

			String labels[] = new String[] { "Customer Mobile No.", "Amount",
					"Charges", "Total Amount" };
			String data[] = new String[] { mobileNumber,
					amount + Constants.CURRENCY, charges + Constants.CURRENCY,
					totalAmount + Constants.CURRENCY };

			List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < data.length; i++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("label", labels[i]);
				hm.put("data", data[i]);
				aList.add(hm);
			}

			String[] from = { "label", "data" };
			int[] to = { R.id.txtLabel, R.id.txtData };
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
					R.layout.listview_layout_with_data, from, to);

			ListView listView = (ListView) findViewById(R.id.dataList);
			listView.setAdapter(adapter);

			Utility.getListViewSize(listView, this, mListItemHieght);

			btnNext = (Button) findViewById(R.id.btnNext);
			btnNext.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				    btnNext.setEnabled(false);
					askMpin(mBundle, TransactionReceiptActivity.class, false, RetailPaymentConfirmationActivity.this);
				}
			});

			btnCancel = (Button) findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Utility.showConfirmationDialog(
							RetailPaymentConfirmationActivity.this,
							Constants.Messages.CANCEL_TRANSACTION,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									goToMainMenu();
								}
							});
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
		}
		headerImplementation();
	}

	private void fetchIntents() {
		transactionInfo = (TransactionInfoModel) mBundle
				.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
		product = (ProductModel) mBundle
				.get(Constants.IntentKeys.PRODUCT_MODEL);
	}

    @Override
    public void onMpinPopupClosed(){
        btnNext.setEnabled(true);
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
			return;
		}

		showLoading("Please Wait", "Processing...");
		new HttpAsyncTask(RetailPaymentConfirmationActivity.this).execute(
				Constants.CMD_RETAIL_PAYMENT + "", getEncryptedMpin(),
				product.getId(), transactionInfo.getCmob(),
				transactionInfo.getTxam(), "1", transactionInfo.getCamt(),
				transactionInfo.getTpam(), transactionInfo.getTamt());
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
				if (table.containsKey(Constants.KEY_LIST_TRANS)) {
					@SuppressWarnings("unchecked")
					List<TransactionModel> list = (List<TransactionModel>) table
							.get(Constants.KEY_LIST_TRANS);
					TransactionModel transaction = list.get(0);

					Intent intent = new Intent(getApplicationContext(),
							TransactionReceiptActivity.class);
					intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL,
							transaction);
					intent.putExtras(mBundle);

					startActivity(intent);
				}
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