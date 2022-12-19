package com.inov8.agentmate.activities.myAccount;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.R;

public class MyAccountViewTransactionsActivity extends
		BaseCommunicationActivity {
	private List<TransactionModel> transactions = new ArrayList<TransactionModel>();
	private int dip = 0;

	private TextView lblHeading;
	private String accountType = "", pin = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_view_transactions);

		dip = 18;

		accountType = mBundle.getString(Constants.IntentKeys.ACCOUNT_TYPE);
		pin = mBundle.getString(Constants.IntentKeys.ENCRPYED_KEY);

		lblHeading = (TextView) findViewById(R.id.lblHeading);
		lblHeading.setText("View Transactions");

		processRequest();
		headerImplementation();
	}

	public void processRequest() {
		if (!haveInternet()) {
			Toast.makeText(getApplication(),
					Constants.Messages.INTERNET_CONNECTION_PROBLEM,
					Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			showLoading("Please Wait", "Processing...");
			new HttpAsyncTask(MyAccountViewTransactionsActivity.this).execute(
					Constants.CMD_MINI_STATMENT+"", pin, accountType,
					"apid", "bbacid");
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
				createAlertDialog(message.getDescr(), "Alert");
			} else if (table.containsKey("msgs")) {
				List<?> list = (List<?>) table.get("msgs");
				MessageModel message = (MessageModel) list.get(0);
				hideLoading();
				createAlertDialog(message.getDescr(), "Message");
			} else {
				if (table.get(Constants.KEY_LIST_TRANS) != null) {
                    hideLoading();
					List<?> vec = (List<?>) table.get(Constants.KEY_LIST_TRANS);
					for (Object object : vec) {
						TransactionModel transaction = (TransactionModel) object;
						transactions.add(transaction);

					}
					loadDataOnView();
				} else {// alert message
                        hideLoading();
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
	}

	public void loadDataOnView() {
		boolean flag = true;
		TableLayout table = (TableLayout) findViewById(R.id.tableLayoutTransactions);
		table.removeAllViews();
		for (int i = 0; i < transactions.size(); i++) {
			final TransactionModel transaction = (TransactionModel) transactions.get(i);
			final TableRow row = new TableRow(this);
			row.setId(i);
			row.setPadding(0, 40, 0, 10);

			if (flag) {
				flag = false;
				row.setBackgroundResource(R.color.bg_table_row_color1);
			} else if (!flag) {
				flag = true;
				row.setBackgroundResource(R.color.bg_table_row_color2);
			}

			RelativeLayout col1 = new RelativeLayout(this);
			TextView txtDate = new TextView(this);
			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					Utility.getScaledPixels(50,
							MyAccountViewTransactionsActivity.this));
			params1.leftMargin = 10;
			params1.rightMargin = 0;
			txtDate.setLayoutParams(params1);
			txtDate.setText(transaction.getDatef());
			txtDate.setTextColor(getResources().getColor(R.color.text_grey));
			txtDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dip);
			col1.addView(txtDate, params1);
			row.addView(col1);

			TextView txtName = new TextView(this);
			txtName.setGravity(Gravity.LEFT);
			txtName.setText(transaction.descr);
			txtName.setTextColor(getResources().getColor(R.color.text_grey));
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dip);
			row.addView(txtName);

			RelativeLayout col2 = new RelativeLayout(this);
			TextView txtAmount = new TextView(this);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			col2.setGravity(Gravity.RIGHT);

			params2.leftMargin = 0;
			params2.rightMargin = 10;
			txtAmount.setGravity(Gravity.RIGHT);
			txtAmount.setText(transaction.getTamtf() + Constants.CURRENCY);
			txtAmount.setLayoutParams(params2);
			txtAmount.setTextColor(getResources().getColor(R.color.text_grey));
			txtAmount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dip);
			col2.addView(txtAmount, params2);
			row.addView(col2);

			row.setClickable(true);
			table.addView(row);
		}
	}
}