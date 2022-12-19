package com.inov8.jsblconsumer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.jsblconsumer.activities.agentTransfer.TransferInInputActivity;
import com.inov8.jsblconsumer.activities.agentTransfer.TransferOutInputActivity;
import com.inov8.jsblconsumer.activities.atmPinGeneration.AtmPinGenerationConfirmationActivity;
import com.inov8.jsblconsumer.activities.billPayment.BillPaymentsUtilityFetchBillInfoActivity;
import com.inov8.jsblconsumer.activities.bookMe.Ticketing;
import com.inov8.jsblconsumer.activities.cashWithdrawal.CashWithdrawalReceiptActivity;
import com.inov8.jsblconsumer.activities.collection.CollectionPaymentsInfoActivity;
import com.inov8.jsblconsumer.activities.debitCard.DebitCardIssuance;
//import com.inov8.jsblconsumer.activities.debitCard.DebitCardManagement;
import com.inov8.jsblconsumer.activities.debitCardActivation.DebitCardActivationConfirmationActivity;
import com.inov8.jsblconsumer.activities.debitCardBlock.DebitCardBlockConfirmationActivity;
import com.inov8.jsblconsumer.activities.forgotMpin.ForgotMpinInputActivity;
import com.inov8.jsblconsumer.activities.fundsTransfer.FundsTransferInputActivity;
import com.inov8.jsblconsumer.activities.hra.HraRegistrationActivity1;
import com.inov8.jsblconsumer.activities.loan.LoanActivity;
import com.inov8.jsblconsumer.activities.miniLoad.MiniLoadInputActivity;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountBalanceInquiryActivity;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountChangePinActivity;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountCheckIban;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountMyLimitActivity;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountSelectMiniStatementActivity;
import com.inov8.jsblconsumer.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.jsblconsumer.activities.retailPayment.RetailPaymentInputActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.adapters.ProductAdaptor;
import com.inov8.jsblconsumer.adapters.ProductAdaptor.OnItemClickListener;
import com.inov8.jsblconsumer.adapters.SubMenuAdapter;

import com.inov8.jsblconsumer.harToWallet.HraToWalletInputActivity;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryMenuActivity extends BaseActivity implements OnItemClickListener {
    private static final int LIST_TYPE_CATEGORY = 1;
    private static final int LIST_TYPE_PRODUCT = 2;
    private static final int LIST_TYPE_HYBRID = 3;
    private TextView lblHeading, lblSubHeading;
    private ArrayList<CategoryModel> listCategories;
    private CategoryModel categoryModel;
    private ProductModel productModel;
    private HashMap<Integer, Object> optionsMap = new HashMap<>();
    private Intent intent;
    private int menuItemPosition, flowType;
    private Byte flowId;
    private Context context;
    private boolean isPinResetCounterEnable = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_list_view);

        fetchIntents();
        headerImplementation();

        checkSoftKeyboardD();
        categoryModel = listCategories.get(menuItemPosition);

        bottomBarImplementation(CategoryMenuActivity.this, categoryModel.getId());

        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblSubHeading = (TextView) findViewById(R.id.lblSubHeading);
        lblHeading.setText(categoryModel.getName());

        if (categoryModel.getId() != null && categoryModel.getId().equals(Constants.CATEGORY_ID_PAY_BILL)) {
            titleImplementation("heading_icon_pay_bill", categoryModel.getName(), null, CategoryMenuActivity.this);
        } else if (categoryModel.getId() != null && categoryModel.getId().equals(Constants.CATEGORY_ID_PAYMENTS)) {
            titleImplementation("heading_icon_payment", categoryModel.getName(), null, CategoryMenuActivity.this);
        } else if (categoryModel.getId() != null && categoryModel.getId().equals(Constants.CATEGORY_ID_MY_ACCOUNT)) {
            titleImplementation("heading_icon_my_account", categoryModel.getName(), null, CategoryMenuActivity.this);
            lblSubHeading.setVisibility(View.VISIBLE);
            lblSubHeading.setText(ApplicationData.accoutTittle);
        } else if (categoryModel.getId() != null && categoryModel.getId().equals(Constants.CATEGORY_ID_MONEY_TRANSFER)) {
            titleImplementation("heading_icon_money_transfer", categoryModel.getName(), null, CategoryMenuActivity.this);
        } else if (categoryModel.getId() != null && categoryModel.getId().equals(Constants.CATEGORY_ID_CARD_SERVICES)) {
            titleImplementation("heading_icon_card_services", categoryModel.getName(), null, CategoryMenuActivity.this);
        } else if (categoryModel.getId() != null && categoryModel.getId().equals(Constants.CATEGORY_ID_ZONG_SERVICES)) {
            titleImplementation("heading_icon_zong_services", categoryModel.getName(), null, CategoryMenuActivity.this);
        } else if (categoryModel.getId() != null && categoryModel.getId().equals(Constants.CATEGORY_ID_COLLECTION)) {
            titleImplementation("heading_icon_collection", categoryModel.getName(), null, CategoryMenuActivity.this);
        }

        if (Integer.parseInt(categoryModel.getIsProduct()) == Constants.CATEGORY_IS_PRODUCT) {
            productModel = categoryModel.getProductList().get(0);
            flowId = Byte.parseByte(productModel.getFlowId());
            executeFlow(flowId, LIST_TYPE_PRODUCT);
        } else if (categoryModel.getCategoryList() != null
                && categoryModel.getCategoryList().size() >= 1
                && categoryModel.getProductList() != null
                && categoryModel.getProductList().size() >= 1) {
            renderListMenu(categoryModel, LIST_TYPE_HYBRID);
        } else if (categoryModel.getProductList() != null
                && categoryModel.getProductList().size() >= 1) {
            renderListMenu(categoryModel, LIST_TYPE_PRODUCT);
        } else if (categoryModel.getCategoryList() != null
                && categoryModel.getCategoryList().size() >= 1) {
            renderListMenu(categoryModel, LIST_TYPE_CATEGORY);
        }
//        else if (categoryModel.getCategoryList() != null
//                && categoryModel.getCategoryList().size() == 1) {
//            categoryModel = categoryModel.getCategoryList().get(0);
//            flowId = Integer.parseInt(categoryModel.getProductList().get(0).getFlowId());
//            executeFlow(flowId, LIST_TYPE_CATEGORY);
//        } else if (categoryModel.getCategoryList() == null) {
//            flowId = categoryModel.getFlowId();
//            executeFlow(flowId, LIST_TYPE_CATEGORY);
//        } else if (categoryModel.getProductList() == null) {
//            flowId = categoryModel.getFlowId();
//            executeFlow(flowId, LIST_TYPE_PRODUCT);
//        }
    }

    private void executeFlow(Byte flowId, int listType) {
        if (mBundle == null)
            mBundle = ApplicationData.mBundleCatalog;
        mBundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);
        if(ApplicationData.isLogin== true && !ApplicationData.isMpinSet && flowId!=Constants.FLOW_ID_GENERATE_MPIN && flowId!=Constants.FLOW_ID_CHANGE_PIN )
        {
            setMpinDialog(false);
            return;
        }
        switch (flowId) {
            case Constants.FLOW_ID_RETAIL_PAYMENT:
                intent = new Intent(CategoryMenuActivity.this, RetailPaymentInputActivity.class);
                finish();
                break;

            case Constants.FLOW_ID_ADVANCE_LOAN:
                intent = new Intent(CategoryMenuActivity.this, LoanActivity.class);
                finish();
                break;

            case Constants.FLOW_ID_CHANGE_PIN:
                if (categoryModel.getProductList().get(0).getId().equals(Constants.PRODUCT_ID_CHANGE_LOGIN_PASSWORD)) {
                    intent = new Intent(CategoryMenuActivity.this, MyAccountChangePinActivity.class);
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.LOGIN_PIN);
                } else {
                    if(ApplicationData.isLogin== true && !ApplicationData.isMpinSet)
                    {
                        setMpinDialog(false);
                        return;
                    }
                    intent = new Intent(CategoryMenuActivity.this, MyAccountChangePinActivity.class);
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.MPIN);
                    intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.IGNORE_AND_GOTO_MENU);
                }
                finish();
                break;

            case Constants.FLOW_ID_MY_LIMITS:
                intent = new Intent(CategoryMenuActivity.this, MyAccountMyLimitActivity.class);
                finish();
                break;

            case Constants.FLOW_ID_GENERATE_MPIN:
//                intent = new Intent(CategoryMenuActivity.this, RegenerateMpinActivity.class);
                intent = new Intent(CategoryMenuActivity.this, MyAccountChangePinActivity.class);
                intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, new ProductModel("10028", Constants.FLOW_ID_GENERATE_MPIN + "", "SET MPIN"));
                intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.SET_MPIN);
                intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.IGNORE_AND_GOTO_MENU);
                intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, false);
                finish();
                break;


            case Constants.FLOW_ID_FT_BLB_TO_BLB:
            case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                intent = new Intent(CategoryMenuActivity.this, FundsTransferInputActivity.class);
                break;

            case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
            case Constants.FLOW_ID_FT_BLB_TO_IBFT:
                intent = new Intent(CategoryMenuActivity.this, FundsTransferInputActivity.class);
                break;

            case Constants.FLOW_ID_BALANCE_INQUIRY:
                intent = new Intent(CategoryMenuActivity.this, MyAccountBalanceInquiryActivity.class);
                isPinResetCounterEnable = false;
                finish();
                break;

            case Constants.FLOW_ID_HRA_ACCOUNT:
                intent = new Intent(CategoryMenuActivity.this, HraRegistrationActivity1.class);
                isPinResetCounterEnable = false;
                finish();
                break;

            case Constants.FLOW_ID_DEBIT_CARD_ISSUANCE:
                intent = new Intent(CategoryMenuActivity.this, DebitCardIssuance.class);
                isPinResetCounterEnable = false;
                finish();
                break;

            case Constants.FLOW_ID_L0_TO_L1_ACCOUNT:
                intent = new Intent(CategoryMenuActivity.this, OpenAccountBvsActivity.class);
                intent.putExtra(Constants.IntentKeys.HRA, true);
                isPinResetCounterEnable = false;
                finish();
                break;
            case Constants.FLOW_ID_FORGOT_MPIN:
                intent = new Intent(CategoryMenuActivity.this, ForgotMpinInputActivity.class);
                finish();
                break;

            case Constants.FLOW_ID_MINI_STATEMENT:
                intent = new Intent(CategoryMenuActivity.this, MyAccountSelectMiniStatementActivity.class);
                isPinResetCounterEnable = false;
                finish();
                break;

//            case Constants.FLOW_ID_BILL_PAYMENT_GAS:
//            case Constants.FLOW_ID_BILL_PAYMENT_TELEPHONE:
//                intent = new Intent(CategoryMenuActivity.this, BillPaymentsUtilityFetchBillInfoActivity.class);
//                intent.putExtra(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_NORMAL);
//                mBundle.putString(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_NORMAL);
//                break;

//            case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
//                intent = new Intent(CategoryMenuActivity.this, BillPaymentsUtilityFetchBillInfoActivity.class);
//                intent.putExtra(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_NORMAL);
//                mBundle.putString(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_NORMAL);
//                break;

            case Constants.FLOW_ID_MINI_LOAD:
                intent = new Intent(CategoryMenuActivity.this, MiniLoadInputActivity.class);
                break;

            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID:
//            case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
//            case Constants.FLOW_ID_BILL_PAYMENT_GAS:
//            case Constants.FLOW_ID_BILL_PAYMENT_TELEPHONE:
//            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID:
                intent = new Intent(CategoryMenuActivity.this, BillPaymentsUtilityFetchBillInfoActivity.class);
//                intent.putExtra(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_PREPAID);
//                mBundle.putString(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_PREPAID);
                break;
//
//            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID:
//                intent = new Intent(CategoryMenuActivity.this, BillPaymentsUtilityFetchBillInfoActivity.class);
//                intent.putExtra(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_POSTPAID);
//                mBundle.putString(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_POSTPAID);
//                break;

            case Constants.FLOW_ID_ATM_PIN_CHANGE:
                intent = new Intent(CategoryMenuActivity.this, AtmPinGenerationConfirmationActivity.class);
                mBundle.putString(XmlConstants.Tags.ATM_PIN_GENERATE, "false");
                break;

            case Constants.FLOW_ID_ATM_PIN_GENERATE:
                intent = new Intent(CategoryMenuActivity.this, AtmPinGenerationConfirmationActivity.class);
                mBundle.putString(XmlConstants.Tags.ATM_PIN_GENERATE, "true");
                break;

            case Constants.FLOW_ID_DEBIT_CARD_BLOCK:
                intent = new Intent(CategoryMenuActivity.this, DebitCardBlockConfirmationActivity.class);
                break;

            case Constants.FLOW_ID_DEBIT_CARD_ACTIVATION:
                intent = new Intent(CategoryMenuActivity.this, DebitCardActivationConfirmationActivity.class);
                intent.putExtra(Constants.IntentKeys.DEBIT_CARD_ACTIVATION_TYPE, Constants.DEBIT_CARD_ACTIVATION_CUSTOMER);
                break;
            case Constants.FLOW_ID_TRANSFER_IN:
                intent = new Intent(CategoryMenuActivity.this, TransferInInputActivity.class);
                break;
            case Constants.FLOW_ID_HRA_TO_WALLET:
                intent = new Intent(CategoryMenuActivity.this, HraToWalletInputActivity.class);
                break;
            case Constants.FLOW_ID_TRANSFER_OUT:
                intent = new Intent(CategoryMenuActivity.this, TransferOutInputActivity.class);
                break;
            case Constants.FLOW_ID_CASH_WITHDRAWAL:
                intent = new Intent(CategoryMenuActivity.this, CashWithdrawalReceiptActivity.class);
                isPinResetCounterEnable = false;
                finish();
                break;
            case Constants.FLOW_ID_E_TICKETING:
                intent = new Intent(CategoryMenuActivity.this, CollectionPaymentsInfoActivity.class);
//                finish();
//                mBundle.putString(XmlConstants.Tags.BILLTYPE, XmlConstants.Tags.BILLTYPE_POSTPAID);
                break;
            case Constants.FLOW_ID_BUS_TICKETING:
                intent = new Intent(CategoryMenuActivity.this, Ticketing.class);
                intent.putExtra(Constants.IntentKeys.BOOKME_TICKETING_TYPE,"buses");
                finish();
                break;
            case Constants.FLOW_ID_BOOKME_AIR:
                intent = new Intent(CategoryMenuActivity.this, Ticketing.class);
                intent.putExtra(Constants.IntentKeys.BOOKME_TICKETING_TYPE,"flights");
                break;
            case Constants.FLOW_ID_BOOKME_EVENT:
                intent = new Intent(CategoryMenuActivity.this, Ticketing.class);
                intent.putExtra(Constants.IntentKeys.BOOKME_TICKETING_TYPE,"events");
                break;
            case Constants.FLOW_ID_BOOKME_CINEMA:
                intent = new Intent(CategoryMenuActivity.this, Ticketing.class);
                intent.putExtra(Constants.IntentKeys.BOOKME_TICKETING_TYPE,"movies");
                break;
            case Constants.FLOW_ID_BOOKME_HOTEL:
                intent = new Intent(CategoryMenuActivity.this, Ticketing.class);
                intent.putExtra(Constants.IntentKeys.BOOKME_TICKETING_TYPE,"hotels");
                break;
            case Constants.FLOW_ID_CHECK_IBAN:
                intent = new Intent(CategoryMenuActivity.this, MyAccountCheckIban.class);
                finish();
                break;
//            case Constants.FLOW_ID_DEBIT_CARD_ACTIVATION_AND_PIN:
//            case Constants.FLOW_ID_DEBIT_CARD_PIN_CHANGE:
//            case Constants.FLOW_ID_DEBIT_CARD_TEM_PER_BLOCK:
//                intent = new Intent(CategoryMenuActivity.this, DebitCardManagement.class);
//                break;


            default:
                Toast.makeText(CategoryMenuActivity.this,
                        "Feature is not implemented.", Toast.LENGTH_SHORT).show();
                this.finish();
                return;
        }

        if (intent != null) {
            switch (listType) {
                case LIST_TYPE_CATEGORY:
                    intent.putExtra(Constants.IntentKeys.CATEGORY_MODEL,
                            categoryModel);
                    break;
                case LIST_TYPE_PRODUCT:
                    intent.putExtra(Constants.IntentKeys.CATEGORY_MODEL,
                            categoryModel);
                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL,
                            productModel);
                    break;
            }

            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }

    private void renderListMenu(final CategoryModel categoryModel, final int flowType) {
        listCategories = new ArrayList<>();
        CategoryModel category;
        ProductModel product;

        int size = 0;
        String[] values = null;
        String[] logos = null;

        this.flowType = flowType;

        switch (flowType) {
            case LIST_TYPE_PRODUCT:
                size = categoryModel.getProductList().size();
                values = new String[size];
                logos = new String[size];
                for (int i = 0; i < size; i++) {
                    product = categoryModel.getProductList().get(i);
                    values[i] = product.getName();
                    logos[i] = product.getURL();
                    optionsMap.put(i, product);
                }
                if (checkForSimilarItem(logos))
                    logos = null;
                break;

            case LIST_TYPE_CATEGORY:
                size = categoryModel.getCategoryList().size();
                values = new String[size];
                logos = new String[size];
                for (int i = 0; i < size; i++) {
                    category = categoryModel.getCategoryList().get(i);
                    values[i] = category.getName();
                    logos[i] = category.getIcon();
                    optionsMap.put(i, category);
                }
                if (checkForSimilarItem(logos)) logos = null;
                listCategories = categoryModel.getCategoryList();
                break;

            case LIST_TYPE_HYBRID:
                int sizeCatList = categoryModel.getCategoryList().size();
                int sizePrdList = categoryModel.getProductList().size();

                size = sizeCatList + sizePrdList;
                values = new String[size];
                logos = new String[size];

                for (int i = 0; i < sizeCatList; i++) {
                    category = categoryModel.getCategoryList().get(i);
                    values[i] = category.getName();
                    logos[i] = category.getIcon();
                    optionsMap.put(i, category);
                }
                if (checkForSimilarItem(logos))
                    logos = null;

                listCategories = categoryModel.getCategoryList();

                for (int i = sizeCatList, j = 0; j < sizePrdList; j++, i++) {
                    product = categoryModel.getProductList().get(j);
                    values[i] = product.getName();
                    logos[i] = product.getURL();
                    optionsMap.put(i, product);
                }
                break;
        }

//        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
//
//        for (int i = 0; i < values.length; i++) {
//
//            ImageView imageView = findViewById(R.id.image);
//            HashMap<String, String> hm = new HashMap<>();
//            hm.put("icon", Integer.toString(R.drawable.list_icon_arrow));
//            hm.put("image",Integer.toString(R.drawable.list_icon_arrow));
//            hm.put("txtOption", values[i]);
//            aList.add(hm);
//        }
//
//        final String[] from = {"icon", "txtOption","image"};
//        final int[] to = {R.id.icon, R.id.txtOption,R.id.image};


        SubMenuAdapter adapter = new SubMenuAdapter(getBaseContext(), values, logos);

        ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.optionsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Object obj = optionsMap.get(position);

            switch (flowType) {
                case LIST_TYPE_PRODUCT:
                    productModel = (ProductModel) obj;
                    flowId = Byte.parseByte(productModel.getFlowId());
                    executeFlow(flowId, LIST_TYPE_PRODUCT);
                    break;
                case LIST_TYPE_CATEGORY:
                    if (listCategories != null && listCategories.size() > 0) {
                        hereWeGoAgain(listCategories, position);
                    }
                    break;
                case LIST_TYPE_HYBRID:
                    if (obj instanceof CategoryModel) {
                        if (listCategories != null && listCategories.size() > 0) {
                            hereWeGoAgain(listCategories, position);
                        }
                    } else if (obj instanceof ProductModel) {
                        productModel = (ProductModel) obj;
                        flowId = Byte.parseByte(productModel.getFlowId());
                        executeFlow(flowId, LIST_TYPE_PRODUCT);
                    }
                    break;
            }
        });

//        ProductAdaptor adapter = new ProductAdaptor(getBaseContext(), values, logos, optionsMap, flowType);
//        RecyclerView listView = findViewById(R.id.optionsList);
//        listView.setLayoutManager(new LinearLayoutManager(this));
//        listView.setAdapter(adapter);
//        ApplicationData.mBundleCatalog = mBundle;


//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Object obj = optionsMap.get(position);
//
//                switch (flowType) {
//                    case LIST_TYPE_PRODUCT:
//                        productModel = (ProductModel) obj;
//                        flowId = Byte.parseByte(productModel.getFlowId());
//                        executeFlow(flowId, LIST_TYPE_PRODUCT);
//                        break;
//                    case LIST_TYPE_CATEGORY:
//                        if (listCategories != null && listCategories.size() > 0) {
//                            hereWeGoAgain(listCategories, position);
//                        }
//                        break;
//                    case LIST_TYPE_HYBRID:
//                        if (obj instanceof CategoryModel) {
//                            if (listCategories != null && listCategories.size() > 0) {
//                                hereWeGoAgain(listCategories, position);
//                            }
//                        } else if (obj instanceof ProductModel) {
//                            productModel = (ProductModel) obj;
//                            flowId = Byte.parseByte(productModel.getFlowId());
//                            executeFlow(flowId, LIST_TYPE_PRODUCT);
//                        }
//                        break;
//                }
//            }
//        });
    }

    @Override
    public void onItemClick(ProductAdaptor.OriginalViewHolder btnCrossListener, int position, HashMap<Integer, Object> optionsMap, int flowType
            , Context context) {
        this.context = context;
//        btnCrossListener.lyt_parent.setOnClickListener(view -> {
        Object obj = optionsMap.get(position);
        switch (flowType) {
            case LIST_TYPE_PRODUCT: {
                productModel = (ProductModel) obj;
                flowId = Byte.parseByte(productModel.getFlowId());
                executeFlow(flowId, LIST_TYPE_PRODUCT);
                break;
            }
            case LIST_TYPE_CATEGORY:
                if (listCategories != null && listCategories.size() > 0) {
                    hereWeGoAgain(listCategories, position);
                }
                break;
            case LIST_TYPE_HYBRID:
                if (obj instanceof CategoryModel) {
                    if (listCategories != null && listCategories.size() > 0) {
                        hereWeGoAgain(listCategories, position);
                    }
                }
                if (obj instanceof ProductModel) {
                    productModel = (ProductModel) obj;
                    flowId = Byte.parseByte(productModel.getFlowId());
                    executeFlow(flowId, LIST_TYPE_PRODUCT);
                }
                break;
        }
//        });
    }

    private void hereWeGoAgain(ArrayList<CategoryModel> listCategories, int position) {
        Intent intent = new Intent(CategoryMenuActivity.this, CategoryMenuActivity.class);
        intent.putExtras(mBundle);
        intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS, position);
        intent.putExtra(Constants.IntentKeys.LIST_CATEGORIES, listCategories);
        startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    private void fetchIntents() {
        menuItemPosition = mBundle.getInt(Constants.IntentKeys.MENU_ITEM_POS);
        listCategories = new ArrayList<CategoryModel>();
        listCategories = (ArrayList<CategoryModel>) mBundle.get(Constants.IntentKeys.LIST_CATEGORIES);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPinResetCounterEnable) {
            ApplicationData.resetPinRetryCount();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public boolean checkForSimilarItem(String[] input) {

        String compare = input[0];

        for (int i = 1; i < input.length; i++) {

            if (compare != null && input[i] != null && !input[i].equals(compare)) {
                return false;
            } else if (compare != null && !compare.equals("null") && !compare.equals("")) {
                return false;
            } else if (input[i] != null && !input[i].equals("null") && !input[i].equals("")) {
                return false;
            }
        }
        return true;
    }

}