package com.inov8.agentmate.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.agentmate.activities.agentTransfer.AgentToAgentInputActivity;
import com.inov8.agentmate.activities.agentTransfer.IBFTInputActivity;
import com.inov8.agentmate.activities.agentTransfer.TransferInInputActivity;
import com.inov8.agentmate.activities.agentTransfer.TransferOutInputActivity;
import com.inov8.agentmate.activities.billPayment.BillPaymentsUtilityFetchBillInfoActivity;
import com.inov8.agentmate.activities.cashOut.CashOut3rdPartyInputActivity;
import com.inov8.agentmate.activities.cashOut.CashOutInputActivity;
import com.inov8.agentmate.activities.collection.CollectionPaymentInputActivity;
import com.inov8.agentmate.activities.fundsTransfer.FundsTransferInputActivity;
import com.inov8.agentmate.activities.myAccount.MyAccountBalanceInquiryActivity;
import com.inov8.agentmate.activities.myAccount.MyAccountChangePinActivity;
import com.inov8.agentmate.activities.myAccount.MyAccountSelectMiniStatementActivity;
import com.inov8.agentmate.model.CategoryModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.jsbl.sco.R;

public class SubMenuAcitivity extends BaseActivity {
	private TextView lblHeading;
	private CategoryModel categoryModel;
	private boolean isPinResetCounterEnable = true;
	private byte flowId;
	private ProductModel product;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_list_view);

		fetchIntents();

		lblHeading = (TextView) findViewById(R.id.lblHeading);
		lblHeading.setText(categoryModel.getName());

		int size = categoryModel.getProductList().size();

		int[] icons = new int[size];
		String[] values = new String[size];

		for (int i = 0; i < size; i++) {
			icons[i] = R.drawable.list_icon_arrow;
			values[i] = categoryModel.getProductList().get(i).getName();
		}

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

				Intent intent = null;
				flowId = Byte.parseByte(categoryModel.getProductList()
						.get(position).getFlowId());
				mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);

				switch (flowId) {
					case Constants.FLOW_ID_FT_BLB_TO_BLB:
					case Constants.FLOW_ID_FT_BLB_TO_CNIC:
					case Constants.FLOW_ID_FT_CNIC_TO_BLB:
					case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
					case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
					case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
						intent = new Intent(SubMenuAcitivity.this,
								FundsTransferInputActivity.class);
						break;

					case Constants.FLOW_ID_TRANSFER_IN:
						intent = new Intent(SubMenuAcitivity.this,
								TransferInInputActivity.class);
						break;

					case Constants.FLOW_ID_COLLECTION_PAYMENT:
						intent = new Intent(SubMenuAcitivity.this,
								CollectionPaymentInputActivity.class);
						break;

					case Constants.FLOW_ID_TRANSFER_OUT:
						intent = new Intent(SubMenuAcitivity.this,
								TransferOutInputActivity.class);
						break;

					case Constants.FLOW_ID_AGENT_TO_AGENT:
						intent = new Intent(SubMenuAcitivity.this,
								AgentToAgentInputActivity.class);
						break;

					case Constants.FLOW_ID_IBFT_AGENT:
						intent = new Intent(SubMenuAcitivity.this,
								IBFTInputActivity.class);
						break;

					case Constants.FLOW_ID_BALANCE_INQUIRY:
						intent = new Intent(SubMenuAcitivity.this,
								MyAccountBalanceInquiryActivity.class);
						isPinResetCounterEnable = false;
						break;

					case Constants.FLOW_ID_MINI_STATEMENT:
						intent = new Intent(SubMenuAcitivity.this,
								MyAccountSelectMiniStatementActivity.class);
						isPinResetCounterEnable = false;
						break;

					case Constants.FLOW_ID_BILL_PAYMENT_GAS:
					case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
					case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID:
					case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID:
					case Constants.FLOW_ID_BILL_PAYMENT_TELEPHONE:
						intent = new Intent(SubMenuAcitivity.this,
								BillPaymentsUtilityFetchBillInfoActivity.class);
						break;

					case Constants.FLOW_ID_CHANGE_PIN:
						intent = new Intent(SubMenuAcitivity.this,
								MyAccountChangePinActivity.class);
						if (categoryModel.getProductList().get(position).getId()
								.equals(Constants.PRODUCT_ID_CHANGE_LOGIN_PIN)) {
							intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, "0");
						} else {
							intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, "1");
						}
						break;

					case Constants.FLOW_ID_CASH_OUT_BY_IVR:
						intent = new Intent(SubMenuAcitivity.this,
								CashOutInputActivity.class);
						intent.putExtra(Constants.IntentKeys.CASH_OUT_TYPE,
								Constants.CASH_OUT_BY_IVR);
						break;

					case Constants.FLOW_ID_CASH_OUT_BY_TRX_ID:
						intent = new Intent(SubMenuAcitivity.this,
								CashOutInputActivity.class);
						intent.putExtra(Constants.IntentKeys.CASH_OUT_TYPE,
								Constants.CASH_OUT_BY_TRX_ID);
						break;

					case Constants.FLOW_ID_3RD_PARTY_CASH_OUT:
						intent = new Intent(SubMenuAcitivity.this,
								CashOut3rdPartyInputActivity.class);
						break;
				}

				if (intent != null) {
					product = categoryModel.getProductList().get(position);
					intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, product);

					intent.putExtras(mBundle);
					startActivity(intent);
				}
			}
		});

		headerImplementation();
	}

	private void fetchIntents() {
		categoryModel = (CategoryModel) mBundle
				.get(Constants.IntentKeys.CATEGORY_MODEL);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isPinResetCounterEnable) {
			ApplicationData.resetPinRetryCount();
		}
	}
}