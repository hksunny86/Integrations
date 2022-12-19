package com.inov8.agentmate.activities.billPayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.jsblmfs.R;

public class BillPaymentTypeActivity extends BaseCommunicationActivity {
	private TextView lblHeading;
	private String consumer, mobileNumber;
	private byte paymentType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_list_view);

		try {
			fetchintents();

			lblHeading = (TextView) findViewById(R.id.lblHeading);
			lblHeading.setText("Payment Type");

			int[] icons = new int[] { R.drawable.list_icon_arrow,
					R.drawable.list_icon_arrow };

			String[] values = new String[] { "Pay By Account", "Pay By Cash" };

			List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < values.length; i++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("icon", Integer.toString(icons[i]));
				hm.put("txtOption", values[i]);
				aList.add(hm);
			}

			final String[] from = { "icon", "txtOption" };
			final int[] to = { R.id.icon, R.id.txtOption };
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
					R.layout.listview_layout_with_icon, from, to);

			ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.optionsList);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				
					paymentType = (byte) position;
					mpinProcess(null, null);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
		}
		headerImplementation();
	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	public void mpinProcess(Bundle bundle, Class<?> nextClass) {
		processRequest();
	}

	private ProductModel mProduct;

	@Override
	public void processRequest() {
		if (!haveInternet()) {
			Toast.makeText(getApplication(),
					Constants.Messages.INTERNET_CONNECTION_PROBLEM,
					Toast.LENGTH_SHORT).show();
			return;
		}

		showLoading("Please Wait", "Processing...");
		new HttpAsyncTask(BillPaymentTypeActivity.this).execute(
				Constants.CMD_BILL_INQUIRY+"", mProduct.getId(),
				ApplicationData.agentMobileNumber, mobileNumber, consumer, paymentType+"",
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
						false, message);
			} else {
				if (table.get(Constants.ATTR_BPAID).toString().equals("1")) {
					createAlertDialog(Constants.Messages.BILL_ALREADY_PAID,
							Constants.KEY_LIST_ALERT, false, null);
				} else {
					TransactionInfoModel transactoinInfo = TransactionInfoModel.getInstance();
					transactoinInfo.BillInquiry(table.get(
							Constants.ATTR_CMOB).toString(), table.get(
							Constants.ATTR_CNIC).toString(), table.get(
							Constants.ATTR_PNAME).toString(), table.get(
							Constants.ATTR_CONSUMER).toString(), table.get(
							Constants.ATTR_DUEDATE).toString(), table.get(
							Constants.ATTR_DUEDATEF).toString(), table.get(
							Constants.ATTR_BAMT).toString(), table.get(
							Constants.ATTR_BAMTF).toString(), table.get(
							Constants.ATTR_LBAMT).toString(), table.get(
							Constants.ATTR_LBAMTF).toString(), table.get(
							Constants.ATTR_ISOVERDUE).toString(), table.get(
							Constants.ATTR_BPAID).toString());
				
					if (mProduct.getAmtRequired().equals("1")) {
						Intent intent = new Intent(getApplicationContext(),
								BillPaymentConfirmationActivity.class);
						intent.putExtra(Constants.IntentKeys.PAYMENT_TYPE,
								paymentType);
						intent.putExtra(
								Constants.IntentKeys.TRANSACTION_INFO_MODEL,
								transactoinInfo);
						
						if(transactoinInfo.getCnic().equals("-1")){// customer registration
							intent.putExtra(
									Constants.IntentKeys.SHOW_REGISTRATION_POPUP, true);
						}

						intent.putExtras(mBundle);
						startActivity(intent);
					} else {
						Intent intent = new Intent(getApplicationContext(),
								BillPaymentConfirmationActivity.class);
						intent.putExtra(Constants.IntentKeys.PAYMENT_TYPE,
								paymentType);
						intent.putExtra(
								Constants.IntentKeys.TRANSACTION_INFO_MODEL,
								transactoinInfo);
						
						if(transactoinInfo.getIsoverdue().equals("1")){// due date check
							intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT,
									transactoinInfo.getLbamt());
						}else{
							intent.putExtra(Constants.IntentKeys.TRANSACTION_AMOUNT,
									transactoinInfo.getBamt());
						}
						
						if(transactoinInfo.getCnic().equals("-1")){// customer registration
							intent.putExtra(
									Constants.IntentKeys.SHOW_REGISTRATION_POPUP, true);
						}
						intent.putExtras(mBundle);
						startActivity(intent);
					}
				}
			}
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

	private void fetchintents() {
		mProduct = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
		consumer = mBundle.getString("CONSUMER_NO");
		mobileNumber = mBundle.getString("MOBILE_NO");
	}
}