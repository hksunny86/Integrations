package com.inov8.agentmate.activities.myAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.jsbl.sco.R;

public class MyAccountSelectMiniStatementActivity extends
		BaseCommunicationActivity {
	private TextView lblHeading;
	private ProductModel product;
	private String AcctType;
	private List<TransactionModel> transactions = new ArrayList<TransactionModel>();
	private TextView lblSubheading;
	private Button btnNext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_mini_statement);

		try {
			fetchIntents();

			lblHeading = (TextView) findViewById(R.id.lblHeading);
			lblHeading.setText("Mini Statement");

			lblSubheading = (TextView) findViewById(R.id.lblSubHeading);
			lblSubheading.setText("(" + product.getName() + ")");

			if (product.getId().equals("10029")) {
				AcctType = "1";
			} else if (product.getId().equals("10030")) {
				AcctType = "0";
			}

			askMpin(mBundle, TransactionReceiptActivity.class, true);

			btnNext = (Button) findViewById(R.id.btnNext);
			btnNext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToMainMenu();
				}
			});

		} catch (Exception ex) {
			ex.getMessage();
		}
		headerImplementation();
	}

	private void loadData() {

		int size = transactions.size();

		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < size; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("txtLabel", transactions.get(i).getDate());
			hm.put("txtdes", transactions.get(i).descr);
			hm.put("txtData", transactions.get(i).getTamtf() + " "
					+ Constants.CURRENCY);

			aList.add(hm);
		}

		final String[] from = { "txtLabel", "txtdes", "txtData" };
		final int[] to = { R.id.txtLabel, R.id.txtdes, R.id.txtData };
		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
				R.layout.listview_mini_statement, from, to);

		ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.optionsList);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				}
			}
		});
	}

	private void fetchIntents() {
		product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
	}

	public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
		processRequest();
	}

	public void processRequest() {
		if (!haveInternet()) {
			Toast.makeText(getApplication(),
					Constants.Messages.INTERNET_CONNECTION_PROBLEM,
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		try {
			showLoading("Please Wait", "Processing...");
			new HttpAsyncTask(MyAccountSelectMiniStatementActivity.this)
					.execute(Constants.CMD_MINI_STATMENT+"",
							getEncryptedMpin(), AcctType,
							ApplicationData.userId, ApplicationData.accountId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processResponse(HttpResponseModel response) {
		try {
			XmlParser xmlParser = new XmlParser();
			Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
					.getXmlResponse());
			if (table.containsKey("errs")) {
				List<?> list = (List<?>) table.get("errs");
				MessageModel message = (MessageModel) list.get(0);
				hideLoading();
				createAlertDialog(message.getDescr(), "Alert", true, message);
			} else if (table.containsKey("msgs")) {
				List<?> list = (List<?>) table.get("msgs");
				MessageModel message = (MessageModel) list.get(0);
				hideLoading();
				createAlertDialog(message.getDescr(), "Message", true, message);
			} else {
				if (table.get(Constants.KEY_LIST_TRANS) != null) {
                    hideLoading();
					List<?> vec = (List<?>) table.get(Constants.KEY_LIST_TRANS);
					for (Object object : vec) {
						TransactionModel transaction = (TransactionModel) object;
						transactions.add(transaction);
						btnNext.setVisibility(View.VISIBLE);

					}
					loadData();
				}
				else {// alert message
                    hideLoading();
				}
			}
		} catch (Exception e) {
			hideLoading();
			e.printStackTrace();
		}
	}

	@Override
	public void processNext() {
	}
}