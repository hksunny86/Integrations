package com.inov8.agentmate.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.agentmate.activities.collection.CollectionPaymentInputActivity;
import com.inov8.agentmate.activities.fundsTransfer.FundsTransferInputActivity;
import com.inov8.agentmate.activities.fundsTransfer.ReceiveCashInputActivity;
import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.agentmate.model.CategoryModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsblmfs.R;

public class CategoryMenuActivity extends BaseActivity {
	private TextView lblHeading;
	private CategoryModel category;
	private HashMap<Integer, Object> optionsMap = new HashMap<Integer, Object>(); 
	private byte flowId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_list_view);

		fetchIntents();

		lblHeading = (TextView) findViewById(R.id.lblHeading);
		lblHeading.setText(category.getName());

		int sizeCatList = category.getCategoryList().size();
		int sizePrdList = 0;
		int size = 0;
		if ( category.getProductList() != null && category.getProductList().size() > 0 ) {			
			sizePrdList = category.getProductList().size();
		}		
		size = sizeCatList + sizePrdList;
		
		int[] icons = new int[size];
		String[] values = new String[size];
		
		CategoryModel cat;
		for (int i = 0; i < sizeCatList; i++) {
			icons[i] = R.drawable.list_icon_arrow;
			cat = category.getCategoryList().get(i);
			values[i] = cat.getName();
			optionsMap.put(i, cat);
		}
		
		ProductModel prd;
		for (int i = sizeCatList, j = 0; j < sizePrdList; j++, i++) {
			icons[i] = R.drawable.list_icon_arrow;
			prd = category.getProductList().get(j);
			values[i] = prd.getName();
			optionsMap.put(i, prd);
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

		ListView listView = (ListView) findViewById(R.id.optionsList);
		listView.setAdapter(adapter);

		@SuppressWarnings("deprecation")
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, mListViewOptionItemHeight
								* values.length, getResources()
								.getDisplayMetrics()));
		params.leftMargin = 18;
		params.rightMargin = 18;
		params.topMargin = 20;
		params.addRule(RelativeLayout.BELOW, R.id.layoutHeading);
		listView.setLayoutParams(params);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null;
				
				Object obj = optionsMap.get(position);

				if (obj instanceof CategoryModel) {					
					CategoryModel subCategory = (CategoryModel)obj;
					if (subCategory.getIsProduct().equals("0")) {
						if (subCategory.getProductList() != null) {
							intent = new Intent(CategoryMenuActivity.this,
									SubMenuAcitivity.class);
						} else if (subCategory.getCategoryList() != null) {
							intent = new Intent(CategoryMenuActivity.this,
									CategoryMenuActivity.class);
						}
					} else if (subCategory.getIsProduct().equals("1")) {
						byte flowId = Byte.parseByte(subCategory.getProductList()
								.get(0).getFlowId());
						mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);

						switch (flowId) {
						case Constants.FLOW_ID_RM_SENDER_REDEEM:
						case Constants.FLOW_ID_RM_RECEIVE_CASH:
							intent = new Intent(CategoryMenuActivity.this,
									ReceiveCashInputActivity.class);
							break;

						}
						intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL,
								subCategory.getProductList().get(0));
					}
					else if (subCategory.getProductList() != null
							&& subCategory.getProductList().size() > 1) {
						intent = new Intent(CategoryMenuActivity.this,
								SubMenuAcitivity.class);
					} else if (subCategory.getCategoryList() != null) {
						intent = new Intent(CategoryMenuActivity.this,
								CategoryMenuActivity.class);
					}
	
					intent.putExtra(Constants.IntentKeys.CATEGORY_MODEL, subCategory);
				} else if (obj instanceof ProductModel) {
					ProductModel product = (ProductModel)obj;
					
					flowId = Byte.parseByte(product.getFlowId());
					mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);
					
					switch (flowId) {
					case Constants.FLOW_ID_FT_BLB_TO_BLB:
					case Constants.FLOW_ID_FT_BLB_TO_CNIC:
					case Constants.FLOW_ID_FT_CNIC_TO_BLB:
					case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
					case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
					case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
						intent = new Intent(CategoryMenuActivity.this, FundsTransferInputActivity.class);
						break;
                    case Constants.FLOW_ID_COLLECTION_PAYMENT:
                        intent = new Intent(CategoryMenuActivity.this,
                                CollectionPaymentInputActivity.class);
                        break;
					}
					intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, product);					
				}				
				
				if (intent != null) {
					intent.putExtras(mBundle);
					startActivity(intent);
				}
			}
		});

		headerImplementation();
	}

	private void fetchIntents() {
		category = (CategoryModel) mBundle
				.get(Constants.IntentKeys.CATEGORY_MODEL);
		
		mBundle.clear();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ApplicationData.resetPinRetryCount();
	}

	@Override
	public void onBackPressed() {

		this.finish();
		super.onBackPressed();
	}
}