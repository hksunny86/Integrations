package com.inov8.agentmate.activities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.myAccount.MyAccountChangePinActivity;
import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.agentmate.bvs.BvsActivity;
import com.inov8.agentmate.model.BankAccountModel;
import com.inov8.agentmate.model.BankModel;
import com.inov8.agentmate.model.CategoryModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AesEncryptor;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.PreferenceConnector;
import com.inov8.agentmate.util.RootUtil;
import com.inov8.agentmate.util.Utility;
import com.inov8.agentmate.util.XmlConstants;
import com.inov8.jsbl.sco.R;

import static com.inov8.agentmate.util.Utility.testValidity;

public class LoginActivity extends BaseCommunicationActivity {
    private EditText username, password;
    private String strUsername, strPassword, strIsRooted, strOtp, strAction,
            strAndroidVersion, strDeviceVendor, strDeviceModel,
            strDeviceNetwork, strUniqueDeivceID;
    private Button btnLogin;
    private BankModel bank;
    private BankAccountModel account;
    private TextView lblVersion;
    private boolean storagePermission = false;
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA,
    Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE };
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);

        ApplicationData.isLogin = false;

        btnLogin = (Button) findViewById(R.id.btnLogin);
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        lblVersion = findViewById(R.id.lblVersion);

        strAndroidVersion = Build.VERSION.RELEASE;
        strDeviceVendor = Build.MANUFACTURER;
        strDeviceModel = Build.MODEL;
        strDeviceNetwork = getMobileNetwork();
        strUniqueDeivceID = Settings.Secure.getString(
                LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ApplicationData.deviceId = strUniqueDeivceID;

        lblVersion.setText("App Version: " + Constants.APPLICATION_VERSION);

        String user = PreferenceConnector.readString(this,
                PreferenceConnector.USER_ID, "");
        if (user.length() > 0) {
            username.setText(user);
            password.requestFocus();
        }

        checkPermissions();

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                strUsername = username.getText() + "";
                strPassword = password.getText() + "";
                hideKeyboard(v);
                boolean flag = true;

                if (!testValidity(username)) {
                    return;
                }
                if (username.length() < Constants.USERNAME_LENGTH) {
                    username.setError(Constants.Messages.INVALID_USERNAME);
                    flag = false;
                    return;
                }
                if (!testValidity(password)) {
                    return;
                }
                if (password.length() < Constants.PASSWORD_LENGTH) {
                    password.setError(Constants.Messages.INVALID_PASSWORD);
                    flag = false;
                    return;
                }

                if (flag) {
                    if (PreferenceConnector.readString(LoginActivity.this,
                            PreferenceConnector.IS_ROOTED, "0").equals(
                            Constants.DEVICE_NOT_ROOTED)) {
                        if (RootUtil.isDeviceRooted()) {
                            PreferenceConnector.writeString(LoginActivity.this,
                                    PreferenceConnector.IS_ROOTED,
                                    Constants.DEVICE_ROOTED);
                            strIsRooted = Constants.DEVICE_ROOTED;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Utility.createAlertDialog(
                                            Constants.Messages.DEVICE_IS_ROOTED,
                                            Constants.Messages.ALERT_HEADING,
                                            LoginActivity.this,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    dialog.dismiss();
                                                    processRequest();
                                                }
                                            },
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    PreferenceConnector
                                                            .writeString(
                                                                    LoginActivity.this,
                                                                    PreferenceConnector.IS_ROOTED,
                                                                    Constants.DEVICE_NOT_ROOTED);
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                            });
                        } else {
                            PreferenceConnector.writeString(LoginActivity.this,
                                    PreferenceConnector.IS_ROOTED,
                                    Constants.DEVICE_NOT_ROOTED);
                            strIsRooted = Constants.DEVICE_NOT_ROOTED;
                            processRequest();
                        }
                    } else {
                        PreferenceConnector.writeString(LoginActivity.this,
                                PreferenceConnector.IS_ROOTED,
                                Constants.DEVICE_ROOTED);
                        strIsRooted = Constants.DEVICE_ROOTED;
                        processRequest();
                    }
                }
            }
        });

        addAutoKeyboardHideFunction();
        resetVersionUsageData();
        checkAppVersionUsageLevel(null);
        setIPChangerDialog();
    }

    private void setIPChangerDialog() {
        if (ApplicationData.isCustomIP) {
            LinearLayout logo = (LinearLayout) findViewById(R.id.parent_layout);
            logo.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {
                    Utility.createIPChangerDialog(LoginActivity.this);
                    return true;
                }
            });
        }

    }

    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(),
                    Constants.Messages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            showLoading("Please Wait", "Authenticating...");

            String encryptedPin = AesEncryptor.encrypt(strPassword);

            PreferenceConnector.writeString(LoginActivity.this,
                    PreferenceConnector.USER_ID, strUsername);
            new HttpAsyncTask(LoginActivity.this).execute(
                    Constants.CMD_LOGIN + "",
                    strUsername,
                    encryptedPin,
                    PreferenceConnector
                            .readString(
                                    LoginActivity.this,
                                    PreferenceConnector.CATALOG_VERSION,
                                    "-1"), strIsRooted,
                    strAndroidVersion, strDeviceModel,
                    strDeviceVendor, strDeviceNetwork,
                    strUniqueDeivceID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetFields() {
        password.setText("");
    }

    public void processResponse(HttpResponseModel response) {
        if (response == null) {
            return;
        }
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table.containsKey(Constants.KEY_LIST_MSGS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                PopupDialogs.otpPopupDialog(message.getCode(),
                        message.getDescr(), PopupDialogs.Status.LOCK, LoginActivity.this,
                        new PopupDialogs.OnCustomClickListener() {
                            @Override
                            public void onClick(View v, Object obj) {
                                showLoading("Please Wait", "Authenticating...");
                                String encryptedPin = AesEncryptor
                                        .encrypt(strPassword);
                                PreferenceConnector.writeString(
                                        LoginActivity.this,
                                        PreferenceConnector.USER_ID,
                                        strUsername);
                                new HttpAsyncTask(LoginActivity.this).execute(
                                        Constants.CMD_LOGIN + "",
                                        strUsername,
                                        encryptedPin,
                                        PreferenceConnector
                                                .readString(
                                                        LoginActivity.this,
                                                        PreferenceConnector.CATALOG_VERSION,
                                                        "-1"), strIsRooted,
                                        strAndroidVersion, strDeviceModel,
                                        strDeviceVendor, strDeviceNetwork,
                                        strUniqueDeivceID);
                                if (ApplicationData.isDummyFlow) {
                                    Constants.LOGIN_RESPONSE_TYPE = 1;
                                }
                            }
                        });
            } else if (table.containsKey("errs")) {
                List<?> list = (List<?>) table.get("errs");
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                resetFields();
                if (message.getCode().equals(
                        Constants.ErrorCodes.LOGIN_FAILED_LEVEL2)
                        && Integer.parseInt(message.getLevel()) == Constants.VersionUsageLevel.BLOCK) {

                    PreferenceConnector.writeInteger(LoginActivity.this,
                            PreferenceConnector.VERSION_USAGE_LEVEL,
                            Constants.VersionUsageLevel.BLOCK);
                    checkAppVersionUsageLevel(message.getDescr());
                } else if (message.getCode().equals(
                        Constants.ErrorCodes.OTP_VERIFICATION)
                        || message.getCode().equals(
                        Constants.ErrorCodes.OTP_RESEND)
                        || message.getCode().equals(
                        Constants.ErrorCodes.OTP_REGENERATION)
                        || message.getCode().equals(
                        Constants.ErrorCodes.OTP_ERROR)
                        || message.getCode().equals(
                        Constants.ErrorCodes.OTP_ERROR_FINAL)) {
                    PopupDialogs.otpPopupDialog(message.getCode(),
                            message.getDescr(), PopupDialogs.Status.LOCK, LoginActivity.this,
                            new PopupDialogs.OnCustomClickListener() {

                                @Override
                                public void onClick(View v, Object obj) {
                                    showLoading("Please Wait",
                                            "Authenticating...");
                                    if (v.getId() == R.id.btnOK) {
                                        Constants.otpCount++;
                                        strOtp = (String) obj;
                                        strAction = Constants.ACTION_VERIFY;
                                        new HttpAsyncTask(LoginActivity.this)
                                                .execute(
                                                        Constants.CMD_OTP_VERIFICATION
                                                                + "",
                                                        strAction,
                                                        strUniqueDeivceID,
                                                        AesEncryptor
                                                                .encrypt(strOtp),
                                                        strUsername);
                                    } else if (v.getId() == R.id.btnResend) {
                                        if (ApplicationData.isDummyFlow) {
                                            Constants.OTP_RESPONSE_TYPE = 3;
                                        }
                                        strAction = Constants.ACTION_RESEND;
                                        new HttpAsyncTask(LoginActivity.this)
                                                .execute(
                                                        Constants.CMD_OTP_VERIFICATION
                                                                + "",
                                                        strAction,
                                                        strUniqueDeivceID, "",
                                                        strUsername);
                                    } else {
                                        if (ApplicationData.isDummyFlow) {
                                            Constants.OTP_RESPONSE_TYPE = 4;
                                        }
                                        strAction = Constants.ACTION_REGENERATE;
                                        new HttpAsyncTask(LoginActivity.this)
                                                .execute(
                                                        Constants.CMD_OTP_VERIFICATION
                                                                + "",
                                                        strAction,
                                                        strUniqueDeivceID, "",
                                                        strUsername);
                                    }
                                }
                            });
                } else {
                    createAlertDialog(message.getDescr(), Constants.Messages.ALERT_HEADING);
                }
            } else {
                ApplicationData.isDestroyed = false;
                hideLoading();

                @SuppressWarnings("unchecked")
                ArrayList<CategoryModel> listCatalog = (ArrayList<CategoryModel>) (table
                        .get(Constants.KEY_LIST_CATALOG));
                if (listCatalog != null && listCatalog.size() > 0) {
                    ApplicationData.listCategories = new ArrayList<CategoryModel>();
                    ApplicationData.listCategories.addAll(listCatalog);

                    String result = response.getXmlResponse().substring(
                            response.getXmlResponse().indexOf("<categories>"),
                            response.getXmlResponse().indexOf("</categories>")
                                    + "</categories>".length());
                    if (result != null) {
                        PreferenceConnector.writeString(this,
                                PreferenceConnector.CATALOG_VERSION,
                                table.get(Constants.KEY_CAT_VER).toString());
                        PreferenceConnector.writeString(this,
                                PreferenceConnector.CATALOG_DATA, result);
                    }
                } else {// use saved catalog

                    String result = PreferenceConnector.readString(this,
                            PreferenceConnector.CATALOG_DATA, "");
                    if (result != null) {
                        ApplicationData.listCategories = new ArrayList<CategoryModel>();
                        ApplicationData.listCategories.addAll(xmlParser
                                .parseLocalCatalog(result));
                    }
                }

                ArrayList<?> listBanks = (ArrayList<?>) table
                        .get(Constants.KEY_LIST_USR_BANK);
                if (listBanks != null && listBanks.size() > 0) {
                    bank = (BankModel) listBanks.get(0);

                    ArrayList<?> listBankAccounts = null;
                    if (table.get(Constants.KEY_BANK_ACC_LIST) != null) {
                        listBankAccounts = (ArrayList<?>) table
                                .get(Constants.KEY_BANK_ACC_LIST);

                        if (listBankAccounts != null && listBankAccounts.size() > 0) {
                            ApplicationData.listBankAccounts = (ArrayList<?>) listBankAccounts;
                            account = (BankAccountModel) ApplicationData.listBankAccounts
                                    .get(0);
                        }
                    }
                }

                ApplicationData.userId = username.getText().toString();

                if (table.get(Constants.ATTR_ATYPE) != null) {
                    ApplicationData.agentAccountType = Integer.parseInt(table
                            .get(Constants.ATTR_ATYPE).toString());

                    switch (ApplicationData.agentAccountType) {
                        case Constants.AgentAccountType.HANDLER:
                            break;

                        case Constants.AgentAccountType.NORMAL:
                            if (table.get("BAL") != null) {
                                ApplicationData.balance = table.get("BAL")
                                        .toString();
                                ApplicationData.formattedBalance = table
                                        .get("BALF").toString();
                            } else {
                                createAlertDialog(
                                        "Unable to fetch the user balance.",
                                        "Alert");
                                resetFields();
                                hideLoading();
                                return;
                            }
                            break;
                    }
                }

                // load my account static data
                populateCashServicesMenu();
                populateMoneyTransferMenu();
                populateMyAccountMenu();
                // populateHraRegistrationMenu();

                ApplicationData.firstName = table.get("FNAME").toString();
                ApplicationData.lastName = table.get("LNAME").toString();
                ApplicationData.isPinChangeRequired = table.get("IPCR").toString();
                ApplicationData.agentMobileNumber = table.get(Constants.ATTR_AMOB).toString();

                if (table.containsKey(XmlConstants.ATTR_BVSE)) {
                    ApplicationData.isAgentAllowedBvs = table.get(XmlConstants.ATTR_BVSE).toString();
                } else
                    ApplicationData.isAgentAllowedBvs = "0";

                if (table.containsKey(XmlConstants.ATTR_IS_CNIC_EXPIRY_INPUT_REQUIRED)) {
                    if (table.get(XmlConstants.ATTR_IS_CNIC_EXPIRY_INPUT_REQUIRED).toString().equals("1"))
                        ApplicationData.isCnicExpiryRequired = true;
                    else
                        ApplicationData.isCnicExpiryRequired = false;
                } else {
                    ApplicationData.isCnicExpiryRequired = true;
                }

                if (table.containsKey(XmlConstants.ATTR_AGENT_AREA_NAME))
                    ApplicationData.agentAreaName = table.get(XmlConstants.ATTR_AGENT_AREA_NAME).toString();
                else
                    ApplicationData.agentAreaName = Constants.AreaName.PUNJAB;

                if (bank != null) {
                    ApplicationData.bankId = bank.id;
                }

                if (account != null) {
                    ApplicationData.accountId = account.getId();
                }

                if (table.get("IPCR").toString().equals("1")
                        && (table.get("IMPCR") != null && table.get("IMPCR")
                        .toString().equals("1"))) {
                    Intent intent = new Intent(LoginActivity.this,
                            MyAccountChangePinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL,
                            new ProductModel("10027",
                                    Constants.FLOW_ID_CHANGE_PIN + "",
                                    "Change Login PIN"));
                    intent.putExtras(mBundle);
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, "0");
                    intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, true);

                    startActivity(intent);
                    finish();

                } else if (table.get("IPCR").toString().equals("1")) {
                    Intent intent = new Intent(LoginActivity.this,
                            MyAccountChangePinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL,
                            new ProductModel("10027",
                                    Constants.FLOW_ID_CHANGE_PIN + "",
                                    "Change Login PIN"));
                    intent.putExtras(mBundle);
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, "0");
                    ApplicationData.isLogin = false;
                    startActivity(intent);
                    finish();
                } else if (table.get("IMPCR") != null
                        && table.get("IMPCR").toString().equals("1")) {
                    Intent intent = new Intent(LoginActivity.this,
                            MyAccountChangePinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL,
                            new ProductModel("10028",
                                    Constants.FLOW_ID_CHANGE_PIN + "",
                                    "Change MPIN"));
                    intent.putExtras(mBundle);
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, "1");
                    ApplicationData.isLogin = false;
                    startActivity(intent);
                    finish();
                } else if (table.get("IS_MIGRATED") != null && table.get("IS_MIGRATED").toString().equals("1")) {
                    Intent intent = new Intent(LoginActivity.this,
                            MyAccountChangePinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL,
                            new ProductModel("10028",
                                    Constants.FLOW_ID_CHANGE_PIN + "",
                                    "Change MPIN"));
                    intent.putExtras(mBundle);
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.SET_MPIN);
                    ApplicationData.isLogin = true;
                    startActivity(intent);
                    finish();
                } else {
                    // application version usage level set
                    if (!processVersionUsageLevel(table))
                        return;

                    Intent intent = new Intent(LoginActivity.this,
                            MainMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ApplicationData.isLogin = true;
                    startActivity(intent);
                    finish();
                }
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            resetFields();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
        }
    }

    private void populateCashServicesMenu() {
        CategoryModel categoryCashServices;
        for (int i = 0; i < ApplicationData.listCategories.size(); i++) {
            categoryCashServices = ApplicationData.listCategories.get(i);
            if (categoryCashServices.getId().equals(Constants.CATEGORY_ID_CASH_OUT)) {
                ArrayList<CategoryModel> categoryList;
                ArrayList<ProductModel> productList;

                if (categoryCashServices.getCategoryList() != null) {
                    categoryList = categoryCashServices.getCategoryList();
                } else {
                    categoryList = new ArrayList<CategoryModel>();
                    categoryCashServices.setCategoryList(categoryList);
                }

                if (categoryCashServices.getProductList() != null) {
                    productList = categoryCashServices.getProductList();
                    while(productList.size()!=0){
                        productList.remove(0);
                    }
                }
                else{
                    productList = new ArrayList<ProductModel>();
                    categoryCashServices.setProductList(productList);
                }

                if (categoryCashServices.getIsProduct().equals("1"))
                    categoryCashServices.setIsProduct("0");

                ProductModel prdMobNumber = new ProductModel(
                        Constants.PRODUCT_ID_CASH_OUT + "",
                        Constants.FLOW_ID_CASH_OUT_BY_IVR + "",
                        "By IVR");

                ProductModel prdTransactionId = new ProductModel(
                        Constants.PRODUCT_ID_CASH_OUT_BY_TRX_ID + "",
                        Constants.FLOW_ID_CASH_OUT_BY_TRX_ID + "",
                        "By Transaction ID");

                if(productList.size()==0) {
                    categoryCashServices.addProduct(prdMobNumber);
                    categoryCashServices.addProduct(prdTransactionId);
                }
                else
                    categoryCashServices.addProduct(prdTransactionId);
                break;
            }
        }
    }

    private void populateMoneyTransferMenu() {
        CategoryModel category;
        for (int i = 0; i < ApplicationData.listCategories.size(); i++) {
            category = ApplicationData.listCategories.get(i);
            if (category.getId().equals(Constants.CATEGORY_ID_MONEY_TRANSFER)) {
                ArrayList<CategoryModel> categoryList;
                if (category.getCategoryList() != null) {
                    categoryList = category.getCategoryList();
                } else {
                    categoryList = new ArrayList<CategoryModel>();
                    category.setCategoryList(categoryList);
                }

                CategoryModel catReceiveMoney = new CategoryModel(
                        Constants.CATEGORY_ID_RECEIVE_MONEY + "",
                        "Receive Money", "", "0", category);
                category.addCategory(catReceiveMoney);

                CategoryModel catSenderRedeem = new CategoryModel(
                        Constants.CATEGORY_ID_SENDER_REDEEM + "",
                        "Sender Redeem", "", "1", catReceiveMoney);
                ProductModel prdSenderRedeem = new ProductModel(
                        Constants.PRODUCT_ID_SEND_MONEY + "",
                        Constants.FLOW_ID_RM_SENDER_REDEEM + "",
                        "Sender Redeem");
                catSenderRedeem.addProduct(prdSenderRedeem);

                CategoryModel catReceiveCash = new CategoryModel(
                        Constants.CATEGORY_ID_RECEIVE_CASH + "",
                        "Receive Cash", "", "1", catReceiveMoney);
                ProductModel prdReceiveCash = new ProductModel(
                        Constants.PRODUCT_ID_SEND_MONEY + "",
                        Constants.FLOW_ID_RM_RECEIVE_CASH + "", "Receive Cash");
                catReceiveCash.addProduct(prdReceiveCash);

                catReceiveMoney.addCategory(catSenderRedeem);
                catReceiveMoney.addCategory(catReceiveCash);
            }
        }
    }

    private void populateHraRegistrationMenu() {
        CategoryModel category;
        boolean found = false;
        for (int i = 0; i < ApplicationData.listCategories.size(); i++) {
            category = ApplicationData.listCategories.get(i);
            if (category.getId().equals(Constants.CATEGORY_ID_HRA_REGISTRATION)) {
                found = true;
                ArrayList<CategoryModel> categoryList;
                if (category.getCategoryList() != null) {
                    categoryList = category.getCategoryList();
                } else {
                    categoryList = new ArrayList<CategoryModel>();
                    category.setCategoryList(categoryList);
                }

                ProductModel prdHraREgistration = new ProductModel(
                        Constants.PRODUCT_ID_HRA_REGISTRATION + "",
                        Constants.FLOW_ID_HRA_REGISTRATION + "", "HRA Registration");
                category.addProduct(prdHraREgistration);

                ApplicationData.listCategories.add(category);
            }
        }
        if(!found){
            CategoryModel categoryHraRegistration = new CategoryModel(Constants.CATEGORY_ID_HRA_REGISTRATION,
                    "HRA Registration",
                    "main_icon_hra_registration.png", "1", null);

            ProductModel prdHraREgistration = new ProductModel(
                    Constants.PRODUCT_ID_HRA_REGISTRATION + "",
                    Constants.FLOW_ID_HRA_REGISTRATION + "", "HRA Registration");
            categoryHraRegistration.addProduct(prdHraREgistration);

            ApplicationData.listCategories.add(categoryHraRegistration);
        }
    }

    private void populateMyAccountMenu() {
        CategoryModel  categoryMyAccount = new CategoryModel("7", "My Account",
                "main_icon_my_account.png", "0", null);

        CategoryModel categoryChangePin = new CategoryModel("14", "Change PIN",
                "", "0", categoryMyAccount);
        ProductModel productChangePin = new ProductModel(
                Constants.PRODUCT_ID_CHANGE_LOGIN_PIN,
                Constants.FLOW_ID_CHANGE_PIN + "", "Change Login PIN");
        ProductModel productChangeMpin = new ProductModel(
                Constants.PRODUCT_ID_CHANGE_MPIN, Constants.FLOW_ID_CHANGE_PIN
                + "", "Change MPIN");
        categoryChangePin.addProduct(productChangePin);
        categoryChangePin.addProduct(productChangeMpin);

        switch (ApplicationData.agentAccountType) {
            case Constants.AgentAccountType.HANDLER:
                break;

            case Constants.AgentAccountType.NORMAL:
                CategoryModel categoryBalanceInquiry = new CategoryModel("13",
                        "Balance Inquiry", "", "0", categoryMyAccount);
                ProductModel productBlbBalanceInquiry = new ProductModel(
                        Constants.PRODUCT_ID_BLB_BALANCE_INQUIRY,
                        Constants.FLOW_ID_BALANCE_INQUIRY + "", "BLB A/C");
                ProductModel productCoreBalanceInquiry = new ProductModel(
                        Constants.PRODUCT_ID_CORE_BALANCE_INQUIRY,
                        Constants.FLOW_ID_BALANCE_INQUIRY + "", "JSBL Core A/C");
                categoryBalanceInquiry.addProduct(productBlbBalanceInquiry);
                categoryBalanceInquiry.addProduct(productCoreBalanceInquiry);

                categoryMyAccount.addCategory(categoryBalanceInquiry);

                CategoryModel categoryMiniStatement = new CategoryModel("15",
                        "Mini Statement", "", "0", categoryMyAccount);
                ProductModel productBlbMiniStatement = new ProductModel(
                        Constants.PRODUCT_ID_BLB_MINI_STATEMENT,
                        Constants.FLOW_ID_MINI_STATEMENT + "", "BLB A/C");
                categoryMiniStatement.addProduct(productBlbMiniStatement);
                categoryMyAccount.addCategory(categoryMiniStatement);
                break;
        }

        categoryMyAccount.addCategory(categoryChangePin);
        ApplicationData.listCategories.add(categoryMyAccount);
    }

    @Override
    protected void onResume() {
        SharedPreferences preferences = getSharedPreferences("Session_js_bank",
                MODE_PRIVATE);
        preferences.edit().putLong("pause_time", 0).commit();
        ApplicationData.resetPinRetryCount();

        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        super.onPause();
    }

    public void processNext() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void checkAppVersionUsageLevel(String msg) {

        int level = PreferenceConnector.readInteger(LoginActivity.this,
                PreferenceConnector.VERSION_USAGE_LEVEL, 0);

        if (level == Constants.VersionUsageLevel.NORMAL) {

        } else if (level == Constants.VersionUsageLevel.CRITICAL) {

        } else if (level == Constants.VersionUsageLevel.BLOCK) {
            btnLogin.setEnabled(false);
            showConfirmationDialog(
                    "Update AGENTmate!",
                    msg != null ? msg
                            : Constants.Messages.APPLICATION_VERSION_LEVEL_OBSOLETE,
                    null,
                    "",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            hideLoading();
                            openMarketUrl();
                            finish();
                        }
                    },
                    "Download"
            );

        } else if (level == Constants.VersionUsageLevel.OBSOLETE) {
            btnLogin.setEnabled(false);
            showConfirmationDialog("Update AGENTmate!",
                    Constants.Messages.APPLICATION_VERSION_LEVEL_OBSOLETE,

                    null, "", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            finish();
                            openMarketUrl();
                        }
                    }, "Download"

            );
        }
    }

    private boolean processVersionUsageLevel(Hashtable<?, ?> table) {
        int level = Integer.parseInt((String) table.get("APUL"));

        PreferenceConnector.writeInteger(LoginActivity.this,
                PreferenceConnector.VERSION_USAGE_LEVEL, level);

        if (level == Constants.VersionUsageLevel.NORMAL) {
            return true;
        } else if (level == Constants.VersionUsageLevel.CRITICAL) {

            showConfirmationDialog("Critical",
                    Constants.Messages.APPLICATION_VERSION_LEVEL_CRITICAL,

                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            Intent intent = new Intent(LoginActivity.this,
                                    MainMenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ApplicationData.isLogin = true;
                            startActivity(intent);
                            finish();

                        }
                    }, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            Intent intent = new Intent(LoginActivity.this,
                                    MainMenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ApplicationData.isLogin = true;
                            startActivity(intent);
                            finish();
                            hideLoading();
                            openMarketUrl();
                        }
                    }, "Download");
            return false;
        } else if (level == Constants.VersionUsageLevel.OBSOLETE) {

            btnLogin.setEnabled(false);
            showConfirmationDialog("Error Obsolete version.!",
                    Constants.Messages.APPLICATION_VERSION_LEVEL_OBSOLETE,

                    null, "", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            hideLoading();
                            finish();
                            openMarketUrl();
                        }
                    }, "Download"

            );
            return false;
        } else {
            return false;
        }
    }

    private String getMobileNetwork() {
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tel.getNetworkOperatorName();
        return networkOperator;
    }

    private void resetVersionUsageData() {

        try {
            if (PreferenceConnector.readFloat(LoginActivity.this,
                    PreferenceConnector.APP_VERSION, 1.0f) != Float.valueOf(Constants.APPLICATION_VERSION)) {

                PreferenceConnector.writeInteger(LoginActivity.this,
                        PreferenceConnector.VERSION_USAGE_LEVEL, 0);

                PreferenceConnector.writeString(LoginActivity.this,
                        PreferenceConnector.APP_VERSION,
                        Constants.APPLICATION_VERSION);
            }
        }
        catch(Exception e){
            PreferenceConnector.clearPreferences(LoginActivity.this);
        }
        if (!PreferenceConnector.readString(LoginActivity.this,
                PreferenceConnector.APP_VERSION, "1.0").equals(Constants.APPLICATION_VERSION)) {

            PreferenceConnector.writeInteger(LoginActivity.this,
                    PreferenceConnector.VERSION_USAGE_LEVEL, 0);

            PreferenceConnector.writeString(LoginActivity.this,
                    PreferenceConnector.APP_VERSION,
                    Constants.APPLICATION_VERSION);
        }
    }

    private void checkPermissions() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            storagePermission = true;
            return;
        }

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            } else {
                if (p.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    storagePermission = true;
                }
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(AppMessages.ALERT_HEADING)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {

                if (grantResults.length > 0) {

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {

                    } else if (grantResults[1] == PackageManager.PERMISSION_GRANTED && permissions[1].equals(Manifest.permission.CALL_PHONE)) {

                    } else if (grantResults[2] == PackageManager.PERMISSION_GRANTED && permissions[2].equals(Manifest.permission.CAMERA)) {

                    } else if (grantResults[3] == PackageManager.PERMISSION_GRANTED && permissions[3].equals(Manifest.permission.CALL_PHONE)) {

                    } else if (grantResults[4] == PackageManager.PERMISSION_GRANTED && permissions[4].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Permissions denied.", Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}