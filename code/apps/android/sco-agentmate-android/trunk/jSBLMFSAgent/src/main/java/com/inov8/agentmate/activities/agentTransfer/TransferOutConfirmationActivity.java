package com.inov8.agentmate.activities.agentTransfer;

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
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.R;

public class TransferOutConfirmationActivity extends BaseCommunicationActivity {
	private String senderMobileNumber, toAccount, fromAccount, amount,
			totalAmount;
	private Button btnNext, btnCancel;
	private TextView lblHeading;
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

            senderMobileNumber = ApplicationData.agentMobileNumber;
            toAccount = transactionInfo.getCoreacid();
            fromAccount = transactionInfo.getBbacid();

            totalAmount = transactionInfo.getTamtf();
            amount = transactionInfo.getTxamf();

            String labels[] = new String[]{"Agent Mobile No.", "To Account No.",
                    "To Account Title", "From Account", "Amount", "Charges",
                    "Total Amount"};
            String data[] = new String[]{senderMobileNumber, toAccount,
                    transactionInfo.getCoreactl(), fromAccount,
                    amount + Constants.CURRENCY,
                    transactionInfo.getTpam() + Constants.CURRENCY,
                    totalAmount + Constants.CURRENCY};

            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < data.length; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("label", labels[i]);
                hm.put("data", data[i]);
                aList.add(hm);
            }

            String[] from = {"label", "data"};
            int[] to = {R.id.txtLabel, R.id.txtData};
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
                    R.layout.listview_layout_with_data, from, to);

            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);
            listView.setAdapter(adapter);

            Utility.getListViewSize(listView, this, mListItemHieght);

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    askMpin(mBundle, TransactionReceiptActivity.class, false);
                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Utility.showConfirmationDialog(
                            TransferOutConfirmationActivity.this,
                            Constants.Messages.CANCEL_TRANSACTION,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    goToMainMenu();
                                }
                            });
                }
            });

            headerImplementation();
        }catch (Exception e){
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	private void fetchIntents() {
		amount = mBundle.getString(Constants.IntentKeys.TRANSACTION_AMOUNT);
		transactionInfo = (TransactionInfoModel) mBundle
				.get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
		product = (ProductModel) mBundle
				.get(Constants.IntentKeys.PRODUCT_MODEL);
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
		new HttpAsyncTask(TransferOutConfirmationActivity.this).execute(
				Constants.CMD_TRANSFER_OUT + "", getEncryptedMpin(),
				product.getId(), transactionInfo.getAmob(),
				transactionInfo.getBbacid(), transactionInfo.getCoreacid(),
				ApplicationData.bankId, transactionInfo.getTxam(),
				transactionInfo.getCamt(), transactionInfo.getTpam(),
				transactionInfo.getTamt(), transactionInfo.getCoreactl());
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