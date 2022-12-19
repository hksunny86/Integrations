//package com.inov8.timepeyconsumer.activities.debitCardIssuance;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.andreabaccega.widget.FormEditText;
//import com.inov8.timepeyconsumer.activities.BaseCommunicationActivity;
//import com.inov8.timepeyconsumer.mfs.R;
//import com.inov8.timepeyconsumer.model.CardApplicantTypeModel;
//import com.inov8.timepeyconsumer.model.CardCategoryModel;
//import com.inov8.timepeyconsumer.model.CardRankModel;
//import com.inov8.timepeyconsumer.model.CardTypeModel;
//import com.inov8.timepeyconsumer.model.HttpResponseModel;
//import com.inov8.timepeyconsumer.model.MessageModel;
//import com.inov8.timepeyconsumer.model.ProductModel;
//import com.inov8.timepeyconsumer.model.TransactionInfoModel;
//import com.inov8.timepeyconsumer.net.HttpAsyncTask;
//import com.inov8.timepeyconsumer.parser.XmlParser;
//import com.inov8.timepeyconsumer.ui.components.PopupDialogs;
//import com.inov8.timepeyconsumer.util.AppMessages;
//import com.inov8.timepeyconsumer.util.ApplicationData;
//import com.inov8.timepeyconsumer.util.Constants;
//import com.inov8.timepeyconsumer.util.XmlConstants;
//
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//
//public class DebitCardIssuanceActivity extends BaseCommunicationActivity {
//    private TextView lblHeading;
//    private Button btnNext;
//    private ProductModel mProduct;
//    private Spinner spinnerCategory, spinnerType, spinnerRank,
//            spinnerApplicantType;
//    private TextView lblBranch, lblCardNumber;
//    private FormEditText inputMobileNumber, inputBranchCode, inputCardNumber;
//    ArrayList<CardCategoryModel> cardCategoryList;
//    ArrayList<CardTypeModel> cardTypeList;
//    ArrayList<CardRankModel> cardRankList;
//    ArrayList<CardApplicantTypeModel> cardApplicantTypeList;
//    ArrayList<String> categoryNames, typeNames, rankNames, applicantNames;
//    private String strCategory, strType, strRank, strApplicantTypeName,
//            strApplicantType, strMobileNumber, strBranchCode, strCardNumber,
//            cmsisdn;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.acitivity_debit_card_issuance_input);
//
//        try {
//            fetchIntents();
//
//            lblHeading = (TextView) findViewById(R.id.lblHeading);
//            lblHeading.setText("Debit Card Issuance");
//
//            lblBranch = (TextView) findViewById(R.id.lblBranch);
//            lblCardNumber = (TextView) findViewById(R.id.lblCardNumber);
//
//            inputMobileNumber = (FormEditText) findViewById(R.id.inputMobileNumber);
//
//            if (cmsisdn != null) {
//                inputMobileNumber.setText(cmsisdn);
//            }
//
//            inputBranchCode = (FormEditText) findViewById(R.id.inputBranchCode);
//            inputCardNumber = (FormEditText) findViewById(R.id.inputCardNumber);
//
//            spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
//            spinnerType = (Spinner) findViewById(R.id.spinnerType);
//            spinnerRank = (Spinner) findViewById(R.id.spinnerRank);
//            spinnerRank.setVisibility(View.GONE);
//            spinnerApplicantType = (Spinner) findViewById(R.id.spinnerApplicantType);
//
//            loadSpinners();
//
//            lblBranch.setVisibility(View.GONE);
//            inputBranchCode.setVisibility(View.GONE);
//            lblCardNumber.setVisibility(View.GONE);
//            inputCardNumber.setVisibility(View.GONE);
//
//            spinnerCategory
//                    .setOnItemSelectedListener(new OnItemSelectedListener() {
//                        public void onItemSelected(AdapterView<?> parent,
//                                                   View view, int position, long id) {
//                            int categoryId = Integer
//                                    .parseInt(((CardCategoryModel) (parent
//                                            .getItemAtPosition(position)))
//                                            .getId());
//                            setInputsViaCategory(categoryId);
//                        }
//
//                        public void onNothingSelected(AdapterView<?> parent) {
//                        }
//                    });
//
//            btnNext = (Button) findViewById(R.id.btnNext);
//            btnNext.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    if (!inputMobileNumber.testValidity()
//                            || ((inputBranchCode.getVisibility() == View.VISIBLE) && (!inputBranchCode
//                            .testValidity()))
//                            || ((inputCardNumber.getVisibility() == View.VISIBLE) && (!inputCardNumber
//                            .testValidity()))) {
//                        Toast.makeText(DebitCardIssuanceActivity.this,
//                                "Please fill all the required fields.",
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    if (inputBranchCode.getVisibility() == View.GONE
//                            && inputCardNumber.getVisibility() == View.GONE) {
//                        Toast.makeText(DebitCardIssuanceActivity.this,
//                                "Please select category.", Toast.LENGTH_SHORT)
//                                .show();
//                        return;
//                    }
//
//                    strMobileNumber = inputMobileNumber.getText().toString();
//
//                    if (strMobileNumber.length() < 11) {
//                        Toast.makeText(DebitCardIssuanceActivity.this,
//                                AppMessages.INVLAID_MOBILE_NUMBER,
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    int position = spinnerCategory.getSelectedItemPosition();
//                    strCategory = ((CardCategoryModel) (spinnerCategory
//                            .getAdapter().getItem(position))).getId();
//
//                    position = spinnerType.getSelectedItemPosition();
//                    strType = ((CardTypeModel) (spinnerType.getAdapter()
//                            .getItem(position))).getId();
//
//                    position = spinnerRank.getSelectedItemPosition();
//                    strRank = ((CardRankModel) (spinnerRank.getAdapter()
//                            .getItem(position))).getId();
//
//                    position = spinnerApplicantType.getSelectedItemPosition();
//                    strApplicantType = ((CardApplicantTypeModel) (spinnerApplicantType
//                            .getAdapter().getItem(position))).getId();
//                    strApplicantTypeName = ((CardApplicantTypeModel) (spinnerApplicantType
//                            .getAdapter().getItem(position))).getName();
//
//                    strCardNumber = inputCardNumber.getText().toString();
//                    strBranchCode = inputBranchCode.getText().toString();
//
//                    hideKeyboard(v);
//
//                    processRequest();
//                }
//            });
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//        headerImplementation();
//    }
//
//    @Override
//    public void processRequest() {
//        if (!haveInternet()) {
//            Toast.makeText(getApplication(),
//                    AppMessages.INTERNET_CONNECTION_PROBLEM,
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            showLoading("Please Wait", "Processing...");
//            new HttpAsyncTask(DebitCardIssuanceActivity.this).execute(
//                    Constants.CMD_DEBIT_CARD_ISSUANCE_INFO + "",
//                    mProduct.getId(), strCategory, strType, strRank,
//                    strApplicantType, strMobileNumber, strCardNumber,
//                    strBranchCode);
//        } catch (Exception e) {
//            hideLoading();
//            PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE,
//                    getString(R.string.alertNotification), DebitCardIssuanceActivity.this, PopupDialogs.Status.ERROR);
//
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void processResponse(HttpResponseModel response) {
//        try {
//            XmlParser xmlParser = new XmlParser();
//            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
//                    .getXmlResponse());
//            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
//                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
//                MessageModel message = (MessageModel) list.get(0);
//
//                PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification), DebitCardIssuanceActivity.this, PopupDialogs.Status.ERROR);
//
//            } else {
//                TransactionInfoModel trnModel = TransactionInfoModel
//                        .getInstance();
//
//                String branch = "";
//                String trxid = "";
//                switch (Integer.parseInt(strCategory)) {
//                    case Constants.DEBIT_CARD_CATEGORY_PERSONALIZED:
//                        branch = table.get(XmlConstants.Attributes.BNAME).toString();
//                        break;
//
//                    case Constants.DEBIT_CARD_CATEGORY_NONPERSONALIZED:
//                        trxid = table.get(XmlConstants.Attributes.TRXID).toString();
//                        break;
//                }
//
//                trnModel.setDebitCardIssuanceInfo(
//                        table.get(XmlConstants.Attributes.CNAME).toString(),
//                        strCategory, table.get(XmlConstants.Attributes.CTNAME)
//                                .toString(), strType,
//                        // table.get(Constants.ATTR_RNAME).toString(), strRank,
//                        "", "", strApplicantTypeName, strApplicantType, table
//                                .get(XmlConstants.Attributes.MOBN).toString(), table
//                                .get(XmlConstants.Attributes.ACTITLE).toString(),
//                        strCardNumber, table.get(XmlConstants.Attributes.TXAM)
//                                .toString(), table.get(XmlConstants.Attributes.TXAMF)
//                                .toString(), table.get(XmlConstants.Attributes.TPAM)
//                                .toString(), table.get(XmlConstants.Attributes.TPAMF)
//                                .toString(), table.get(XmlConstants.Attributes.CAMT)
//                                .toString(), table.get(XmlConstants.Attributes.CAMTF)
//                                .toString(), table.get(XmlConstants.Attributes.TAMT)
//                                .toString(), table.get(XmlConstants.Attributes.TAMTF)
//                                .toString(), strBranchCode, branch, trxid);
//
//                Intent intent = new Intent(getApplicationContext(),
//                        DebitCardIssuanceConfirmationActivity.class);
//                intent.putExtra(Constants.IntentKeys.TRANSACTION_INFO_MODEL,
//                        trnModel);
//                intent.putExtras(mBundle);
//                startActivity(intent);
//            }
//        } catch (Exception e) {
//            hideLoading();
//            PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE,
//                    getString(R.string.alertNotification), DebitCardIssuanceActivity.this, PopupDialogs.Status.ERROR);
//
//            e.printStackTrace();
//        }
//        hideLoading();
//    }
//
//    @Override
//    public void processNext() {
//    }
//
//    private void fetchIntents() {
//        mProduct = (ProductModel) mBundle
//                .get(Constants.IntentKeys.PRODUCT_MODEL);
//        cmsisdn = mBundle.getString(XmlConstants.Attributes.CMSISDN);
//    }
//
//    private void loadSpinners() {
//        cardCategoryList = ApplicationData.listCardCategories;
//        cardTypeList = ApplicationData.listCardTypes;
//        cardRankList = ApplicationData.listCardRanks;
//        cardApplicantTypeList = ApplicationData.listCardApplicantTypes;
//
//        ArrayAdapter<CardCategoryModel> dataAdapterCategories = new ArrayAdapter<CardCategoryModel>(
//                this, android.R.layout.simple_spinner_item, cardCategoryList);
//        dataAdapterCategories
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCategory.setAdapter(dataAdapterCategories);
//
//        ArrayAdapter<CardTypeModel> dataAdapterTypes = new ArrayAdapter<CardTypeModel>(
//                this, android.R.layout.simple_spinner_item, cardTypeList);
//        dataAdapterTypes
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerType.setAdapter(dataAdapterTypes);
//
//        ArrayAdapter<CardRankModel> dataAdapterRanks = new ArrayAdapter<CardRankModel>(
//                this, android.R.layout.simple_spinner_item, cardRankList);
//        dataAdapterRanks
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerRank.setAdapter(dataAdapterRanks);
//
//        ArrayAdapter<CardApplicantTypeModel> dataAdapterApplicantTypes = new ArrayAdapter<CardApplicantTypeModel>(
//                this, android.R.layout.simple_spinner_item,
//                cardApplicantTypeList);
//        dataAdapterApplicantTypes
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerApplicantType.setAdapter(dataAdapterApplicantTypes);
//    }
//
//    private void setInputsViaCategory(int categoryId) {
//        switch (categoryId) {
//            case Constants.DEBIT_CARD_CATEGORY_PERSONALIZED:
//                lblBranch.setVisibility(View.VISIBLE);
//                inputBranchCode.setVisibility(View.VISIBLE);
//                lblCardNumber.setVisibility(View.GONE);
//                inputCardNumber.setVisibility(View.GONE);
//                break;
//            case Constants.DEBIT_CARD_CATEGORY_NONPERSONALIZED:
//                lblBranch.setVisibility(View.GONE);
//                inputBranchCode.setVisibility(View.GONE);
//                lblCardNumber.setVisibility(View.VISIBLE);
//                inputCardNumber.setVisibility(View.VISIBLE);
//                break;
//            default:
//                lblBranch.setVisibility(View.GONE);
//                inputBranchCode.setVisibility(View.GONE);
//                lblCardNumber.setVisibility(View.GONE);
//                inputCardNumber.setVisibility(View.GONE);
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        goToMainMenu();
//    }
//}