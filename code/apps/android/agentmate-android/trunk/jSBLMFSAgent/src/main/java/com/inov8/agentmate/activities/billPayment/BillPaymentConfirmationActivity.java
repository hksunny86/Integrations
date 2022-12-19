package com.inov8.agentmate.activities.billPayment;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
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
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

public class BillPaymentConfirmationActivity extends BaseCommunicationActivity implements MpinInterface {

	private Button btnNext, btnCancel;
	private TextView lblHeading, lblField1;
	private EditText input1;
	private String productName, consumerNo, dueDate, totalAmount, billStatus, mobileNumber, amount;
	private byte paymentType;
	private TransactionInfoModel transactionInfo;
	private ProductModel product;
	private Byte flowId;
	private Double d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_confirm);

		try {

			lblField1 = findViewById(R.id.lblField1);
			input1 = findViewById(R.id.input1);

			fetchIntents();

			productName = transactionInfo.getPname();
			consumerNo = transactionInfo.getConsumer();
			mobileNumber = transactionInfo.getCmob();
			dueDate = transactionInfo.getDuedate();
			totalAmount = transactionInfo.getBamtf();
			billStatus = "Unpaid";

			lblHeading = (TextView) findViewById(R.id.lblHeading);
			lblHeading.setText("Confirm Transaction");

			if (product.getPpAllowed().equals("1")) {
			lblField1.setText("Enter Amount");
			lblField1.setVisibility(View.VISIBLE);
			input1.setVisibility(View.VISIBLE);
			}

			String labels[], data[];
			if (product.getFlowId().equals("7")
					|| product.getFlowId().equals("8")) {
				labels = new String[] { "Product Name", product.getLabel(),
						"Bill Amount", "Customer Mobile Number" };
				data = new String[] { productName, consumerNo,
						Utility.getFormatedAmount(amount) + Constants.CURRENCY,
						mobileNumber };
			} else {
				labels = new String[] { "Product Name", product.getLabel(),
						"Due Date", "Bill Amount", "Late Bill Amount",
						"Bill Paid Status", "Customer Mobile Number" };
				data = new String[] { productName, consumerNo, dueDate,
						totalAmount + Constants.CURRENCY,
						transactionInfo.getLbamtf() + Constants.CURRENCY,
						billStatus, mobileNumber };
			}
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

			ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);
			listView.setAdapter(adapter);

			Utility.getListViewSize(listView, this, mListItemHieght);

			btnNext = (Button) findViewById(R.id.btnNext);
			btnNext.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
                    if (product.getFlowId().equals("7")
                            || product.getFlowId().equals("8")) {
                        d = Double.parseDouble(Utility.getUnFormattedAmount(amount));   // 0.00
                    }
                    else{
                        d = Double.parseDouble(Utility.getUnFormattedAmount(totalAmount));
                    }
                    String[] arr = String.valueOf(d).split("\\.");
                    int[] intArr=new int[2];
                    intArr[0]=Integer.parseInt(arr[0]);

				    if(intArr[0]==0){
                        createAlertDialog(Constants.Messages.INVALID_BILL_AMOUNT, Constants.KEY_LIST_ALERT);
                        return;
                    }
                    else {
                        btnNext.setEnabled(false);
                        askMpin(mBundle, TransactionReceiptActivity.class, false, BillPaymentConfirmationActivity.this);
                    }
				}
			});

			btnCancel = (Button) findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Utility.showConfirmationDialog(
							BillPaymentConfirmationActivity.this,
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

    @Override
    public void onMpinPopupClosed(){
        btnNext.setEnabled(true);
    }

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	private void fetchIntents() {
		paymentType = mBundle.getByte(Constants.IntentKeys.PAYMENT_TYPE);
		transactionInfo = (TransactionInfoModel) mBundle
				.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
		product = (ProductModel) mBundle
				.get(Constants.IntentKeys.PRODUCT_MODEL);
		amount = mBundle.getString(Constants.IntentKeys.TRANSACTION_AMOUNT);
		flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
		mBundle.clear();
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
		new HttpAsyncTask(BillPaymentConfirmationActivity.this).execute(
				Constants.CMD_BILL_PAYMENT + "", getEncryptedMpin(),
				product.getId(), transactionInfo.getCmob(),
				transactionInfo.getConsumer(), paymentType + "",
				ApplicationData.bankId, product.getPpAllowed().equals("1") && !input1.getText().toString().equals("") ? input1.getText().toString() : amount,
				ApplicationData.agentMobileNumber);
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
					intent.putExtra(Constants.IntentKeys.PAYMENT_TYPE,
							paymentType);
					intent.putExtra(Constants.IntentKeys.FLOW_ID, flowId);
					intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, product);

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