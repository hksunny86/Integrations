//package com.inov8.timepeyconsumer.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.TypedValue;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.RelativeLayout;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//
//import com.inov8.timepeyconsumer.activities.agentTransfer.TransferInInputActivity;
//import com.inov8.timepeyconsumer.activities.agentTransfer.TransferOutInputActivity;
//import com.inov8.timepeyconsumer.activities.atmPinChange.AtmPinChangeInputActivity;
//import com.inov8.timepeyconsumer.activities.atmPinGeneration.AtmPinGenerationConfirmationActivity;
//import com.inov8.timepeyconsumer.activities.billPayment.BillPaymentsUtilityFetchBillInfoActivity;
//import com.inov8.timepeyconsumer.activities.debitCardActivation.DebitCardActivationConfirmationActivity;
//import com.inov8.timepeyconsumer.activities.fundsTransfer.FundsTransferInputActivity;
//import com.inov8.timepeyconsumer.activities.myAccount.MyAccountBalanceInquiryActivity;
//import com.inov8.timepeyconsumer.activities.myAccount.MyAccountSelectMiniStatementActivity;
//import com.inov8.timepeyconsumer.mfs.R;
//import com.inov8.timepeyconsumer.model.CategoryModel;
//import com.inov8.timepeyconsumer.model.ProductModel;
//import com.inov8.timepeyconsumer.ui.components.ListViewExpanded;
//import com.inov8.timepeyconsumer.util.ApplicationData;
//import com.inov8.timepeyconsumer.util.Constants;
//import com.inov8.timepeyconsumer.util.XmlConstants;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class SubMenuActivity extends BaseActivity {
//    private TextView lblHeading;
//    private CategoryModel categoryModel;
//    private boolean isPinResetCounterEnable = true;
//    private byte flowId;
//    private int productId;
//    private ProductModel product;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.activity_list_view);
//
//        fetchIntents();
//        if (categoryModel.getId() != null && categoryModel.getId().equals(Constants.CATEGORY_ID_ZONG_SERVICES)) {
//            titleImplementation("heading_icon_zong_services", categoryModel.getName(), null, SubMenuActivity.this);
//        } else if (categoryModel.getId() != null && categoryModel.getId().equals(Constants.CATEGORY_ID_CARD_SERVICES)) {
//            titleImplementation("heading_icon_card_services", categoryModel.getName(), null, SubMenuActivity.this);
//        } else {
//            lblHeading = (TextView) findViewById(R.id.lblHeading);
//            lblHeading.setText(categoryModel.getName());
//        }
//
//        int size = categoryModel.getProductList().size();
//        int[] icons = new int[size];
//        String[] values = new String[size];
//
//
//        for (int i = 0; i < size; i++) {
//
//            icons[i] = R.drawable.list_icon_arrow;
//            values[i] = categoryModel.getProductList().get(i).getName();
//        }
//
//        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
//
//        for (int i = 0; i < values.length; i++) {
//            HashMap<String, String> hm = new HashMap<String, String>();
//            hm.put("icon", Integer.toString(icons[i]));
//            hm.put("txtOption", values[i]);
//            aList.add(hm);
//        }
//
//        final String[] from = {"icon", "txtOption"};
//        final int[] to = {R.id.icon, R.id.txtOption};
//
//        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
//                R.layout.list_item_with_icon, from, to);
//
//        ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.optionsList);
//        listView.setAdapter(adapter);
//
//        @SuppressWarnings("deprecation")
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                LayoutParams.FILL_PARENT, (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, mListViewOptionItemHeight
//                        * values.length, getResources()
//                        .getDisplayMetrics()));
//
//        params.leftMargin = 18;
//        params.rightMargin = 18;
//        params.topMargin = 20;
//        params.addRule(RelativeLayout.BELOW, R.id.layoutHeading);
//        listView.setLayoutParams(params);
//
//        listView.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                Intent intent = null;
//                flowId = Byte.parseByte(categoryModel.getProductList().get(position).getFlowId());
//                mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);
//
//                productId = Integer.parseInt(categoryModel.getProductList().get(position).getId());
//                mBundle.putInt(Constants.IntentKeys.PRODUCT_ID, productId);
//
//                switch (flowId) {
//
//                    case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
//                    case Constants.FLOW_ID_FT_BLB_TO_IBFT:
//                        intent = new Intent(SubMenuActivity.this, FundsTransferInputActivity.class);
//                        break;
//
//                    case Constants.FLOW_ID_TRANSFER_IN:
//                        intent = new Intent(SubMenuActivity.this, TransferInInputActivity.class);
//                        break;
//
//                    case Constants.FLOW_ID_TRANSFER_OUT:
//                        intent = new Intent(SubMenuActivity.this, TransferOutInputActivity.class);
//                        break;
//
//                    case Constants.FLOW_ID_BALANCE_INQUIRY:
//                        intent = new Intent(SubMenuActivity.this, MyAccountBalanceInquiryActivity.class);
//                        isPinResetCounterEnable = false;
//                        break;
//
//                    case Constants.FLOW_ID_MINI_STATEMENT:
//                        intent = new Intent(SubMenuActivity.this, MyAccountSelectMiniStatementActivity.class);
//                        isPinResetCounterEnable = false;
//                        break;
//
//                    case Constants.FLOW_ID_BILL_PAYMENT_GAS:
//                    case Constants.FLOW_ID_BILL_PAYMENT_TELEPHONE:
//                        intent = new Intent(SubMenuActivity.this, BillPaymentsUtilityFetchBillInfoActivity.class);
//                        intent.putExtra(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_NORMAL);
//                        mBundle.putString(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_NORMAL);
//                        break;
//
//                    case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
//                        intent = new Intent(SubMenuActivity.this, BillPaymentsUtilityFetchBillInfoActivity.class);
//                        intent.putExtra(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_NORMAL);
//                        mBundle.putString(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_NORMAL);
//                        break;
//
//                    case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID:
//                        intent = new Intent(SubMenuActivity.this, BillPaymentsUtilityFetchBillInfoActivity.class);
//                        intent.putExtra(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_PREPAID);
//                        mBundle.putString(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_PREPAID);
//                        break;
//
//                    case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID:
//                        intent = new Intent(SubMenuActivity.this, BillPaymentsUtilityFetchBillInfoActivity.class);
//                        intent.putExtra(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_POSTPAID);
//                        mBundle.putString(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_POSTPAID);
//                        break;
//
//                    case Constants.FLOW_ID_ATM_PIN_CHANGE:
//                        intent = new Intent(SubMenuActivity.this, AtmPinChangeInputActivity.class);
//                        break;
//
//                    case Constants.FLOW_ID_ATM_PIN_GENERATE:
//                        intent = new Intent(SubMenuActivity.this, AtmPinGenerationConfirmationActivity.class);
//                        break;
//
//                    case Constants.FLOW_ID_DEBIT_CARD_BLOCK:
//                        intent = new Intent(SubMenuActivity.this, DebitCardActivationConfirmationActivity.class);
//                        break;
//
//                    case Constants.FLOW_ID_DEBIT_CARD_ACTIVATION:
//                        intent = new Intent(SubMenuActivity.this, DebitCardActivationConfirmationActivity.class);
//                        intent.putExtra(Constants.IntentKeys.DEBIT_CARD_ACTIVATION_TYPE, Constants.DEBIT_CARD_ACTIVATION_CUSTOMER);
//                        break;
//                }
//
//                if (intent != null) {
//                    product = categoryModel.getProductList().get(position);
//                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, product);
//                    intent.putExtras(mBundle);
//                    startActivity(intent);
//                }
//            }
//        });
//
//        headerImplementation();
//    }
//
//    private void fetchIntents() {
//        categoryModel = (CategoryModel) mBundle
//                .get(Constants.IntentKeys.CATEGORY_MODEL);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (isPinResetCounterEnable) {
//            ApplicationData.resetPinRetryCount();
//        }
//    }
//}