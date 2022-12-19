package com.inov8.jsblconsumer.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;

import androidx.appcompat.widget.AppCompatImageView;

import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.contactus.ContactUsActivity;
import com.inov8.jsblconsumer.activities.forgotMpin.ForgotMpinInputActivity;
import com.inov8.jsblconsumer.activities.forgotPassword.ForgotPasswordMobileInputActivity;
import com.inov8.jsblconsumer.activities.loginPinChange.LoginPinChangeInputActivity;
import com.inov8.jsblconsumer.activities.mangers.SmsReceiver;
import com.inov8.jsblconsumer.activities.mangers.SmsUpdater;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountChangePinActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.openAccount.OpenAccountBvsOtpActivity;
import com.inov8.jsblconsumer.model.AdModel;
import com.inov8.jsblconsumer.model.BankAccountModel;
import com.inov8.jsblconsumer.model.BankModel;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AesEncryptor;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.PasswordControllerLogin;
import com.inov8.jsblconsumer.util.PreferenceConnector;
import com.inov8.jsblconsumer.util.Utility;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class LoginActivity extends BaseCommunicationActivity implements SmsUpdater {
    private LinearLayout logo;
    private EditText mobileId, mobileNo;
    private TextView tvContactUs;
    private String strUsername;
    private String strPassword;
    private String strIsRooted;
    private String strAndroidVersion;
    private String strDeviceVendor;
    private String strDeviceModel;
    private String strDeviceNetwork;
    private String strUniqueDeivceID;
    private String strOtp;
    private Dialog otpDialog;
    private String strAction;
    private boolean isOtpResend = false;
    Button btnResend;
    CountDownTimer countDownTimer;
    private Button btnLogin, btnForgot;
    private View layoutBottom;
    private SmsReceiver mSmsReceiver;
    private boolean isSmsReceiverRegistered = false;
    private EditText etOtp;
    private String mobileIdStr, mobileNoStr;
    private CheckBox checkTermsAndConditions;
    private TextView termsAndConditions;
    //    private final int REQUEST_CODE_PERMISSIONS = 100;
//    private String[] permissions = new String[]{
//            Manifest.permission.RECEIVE_SMS};
    private TextView headerText;
    private PasswordControllerLogin passwordController;


    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        boolean isFromAccountOpen = intent.getBooleanExtra("fromOpenAccount", false);

        boolean isHra = intent.getBooleanExtra(Constants.IntentKeys.HRA, false);

        TextView hintPassword = (TextView) findViewById(R.id.hint_text_otp);
        if (isFromAccountOpen && !isHra) {
            findViewById(R.id.tvIVRHint).setVisibility(View.INVISIBLE);
            hintPassword.setText(getString(R.string.password_hint_login_open_account));
        }

//        checkPermissions();
        headerImplementation();
        ApplicationData.isLogin = false;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        passwordController = new PasswordControllerLogin(findViewById(R.id.layoutPassword), false, false, this);

//        FirebaseCrash.report(new Exception("app crash otpDialog"));

//        bottomBarImplementation(this);

        btnForgot = (Button) findViewById(R.id.btnForgot);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        mobileId = (EditText) findViewById(R.id.etMobileId);
        mobileNo = (EditText) findViewById(R.id.etMobileNo);
        disableCopyPaste(mobileId);
        disableCopyPaste(mobileNo);
//        mobileNo.setEnabled(false);
        tvContactUs = (TextView) findViewById(R.id.tvContactUs);


        strAndroidVersion = Build.VERSION.RELEASE;
        strDeviceVendor = Build.MANUFACTURER;
        strDeviceModel = Build.MODEL;
        strDeviceNetwork = getMobileNetwork();
        strUniqueDeivceID = Settings.Secure.getString(
                LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        String mobileCode = PreferenceConnector.readString(this, PreferenceConnector.MOBILE_ID, "");
        String mobileNumber = PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NO, "");

        if (mobileCode.length() > 0 && mobileNumber.length() > 0) {
            mobileId.setText(mobileCode);
            mobileNo.setText(mobileNumber);
            passwordController.setRequestFocus();
        }

        tvContactUs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ContactUsActivity.class);
                intent.putExtra(Constants.IntentKeys.HEADING, getResources().getString(R.string.contact_us));
                intent.putExtra(Constants.IntentKeys.ICON, R.drawable.heading_icon_contact);


                startActivity(intent);
            }
        });

        mobileId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() == 4) {
                    mobileNo.requestFocus();
//                  mobileNo.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 7) {
                    passwordController.setRequestFocus();
                } else if (s.length() == 0) {
                    mobileId.requestFocus();
                    mobileId.setSelection(mobileId.getText().length());
//                    mobileNo.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//
//        password.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 6) {
//                    hideKeyboard(password);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        btnForgot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordMobileInputActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(LoginActivity.this, MyAccountChangePinActivity.class);
//                intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, new ProductModel("10028", Constants.FLOW_ID_GENERATE_MPIN + "", "SET MPIN"));
//                intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.SET_MPIN);
//                intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.IGNORE_AND_GOTO_MENU);
//                intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, false);
//                intent.putExtra(Constants.IntentKeys.MPIN_FROM_LOGIN, true);
//                startActivity(intent);
//                finish();

            }
        });

        checkTermsAndConditions = (CheckBox) findViewById(R.id.checkTermsAndConditions);

        termsAndConditions = findViewById(R.id.termsHyperText);
        termsAndConditions.setClickable(true);
        termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href= " + Constants.URL_TERMS_OF_USE + "> terms and conditions </a>";
        termsAndConditions.setText(Html.fromHtml(text));

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                strUsername = mobileId.getText() + "" + mobileNo.getText() + "";
                strPassword = passwordController.getPassword();
                hideKeyboard(v);
                boolean flag = true;

                if (TextUtils.isEmpty(mobileId.getText())) {
                    mobileId.setError(AppMessages.ERROR_EMPTY_FIELD);
                    return;
                }
                if (mobileId.length() < 4) {
                    mobileId.setError(AppMessages.INVALID_MOBILE_ID_LENGTH);
                    return;
                }
                if (mobileId.getText().charAt(0) != '0' || mobileId.getText().charAt(1) != '3') {
                    mobileId.setError(AppMessages.ERROR_MOBILE_NO_START);
                    return;
                }
                if (TextUtils.isEmpty(mobileNo.getText())) {
                    mobileNo.setError(AppMessages.ERROR_EMPTY_FIELD);
                    return;
                }
                if (mobileNo.length() < 7) {
                    mobileNo.setError(AppMessages.INVALID_MOBILE_NO_LENGTH);
                    return;
                }
                if (TextUtils.isEmpty(strPassword)) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.ERROR_EMPTY_PIN, AppMessages.ALERT_HEADING,
                            LoginActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                    return;
                }
                if (strPassword.length() < Constants.LOGIN_PIN_LENGTH) {


                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INVALID_LOGIN_PIN_LENGTH, AppMessages.ALERT_HEADING,
                            LoginActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                    return;
                }

                if (!checkTermsAndConditions.isChecked()) {
                    dialogGeneral = popupDialogs.createAlertDialog("Please agree to the Terms and Conditions.", AppMessages.ALERT_HEADING,
                            LoginActivity.this, getString(R.string.ok), v1 -> dialogGeneral.dismiss(), false, PopupDialogs.Status.ERROR, false, null);
                    return;
                }

                passwordController.resetPassword();

                PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.IS_ROOTED, Constants.DEVICE_NOT_ROOTED);
                strIsRooted = Constants.DEVICE_NOT_ROOTED;
                ApplicationData.userId = mobileId.getText().toString() + mobileNo.getText().toString();
                ApplicationData.customerMobileNumber = mobileId.getText().toString() + mobileNo.getText().toString();
                mobileIdStr = mobileId.getText().toString();
                mobileNoStr = mobileNo.getText().toString();
                processRequest();

            }
        });

//        tvTermsLink.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideKeyboard(v);
//                renderWebView(Constants.URL_TERMS_OF_USE, getString(R.string.terms_and_conditions), R.drawable.heading_icon_terms);
//            }
//        });
        addAutoKeyboardHideFunction();
        setIPChangerDialog();
    }

    private String getMobileNetwork() {
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tel.getNetworkOperator();
        networkOperator = tel.getNetworkOperatorName();
        return networkOperator;
    }

    private void setIPChangerDialog() {
        logo = (LinearLayout) findViewById(R.id.ivLoginLogo);
        if (ApplicationData.isCustomIP) {
            logo.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {
                    Utility.createIPChangerDialog(LoginActivity.this);
                    return true;
                }
            });
        }
        logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
    }

    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    LoginActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        try {

            showLoading("Please Wait", "Authenticating...");
            initializeSmsReciever();
            String encryptedPin = AesEncryptor.encrypt(strPassword);

            PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.MOBILE_ID, mobileId.getText().toString());
            PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.MOBILE_NO, mobileNo.getText().toString());

            new HttpAsyncTask(LoginActivity.this).execute(
                    Constants.CMD_LOGIN + "",
                    strUsername,
                    encryptedPin,
                    PreferenceConnector.readString(this, PreferenceConnector.CATALOG_VERSION, "-1"),
                    strIsRooted,
                    strAndroidVersion,
                    strDeviceModel,
                    strDeviceVendor,
                    strDeviceNetwork,
                    strUniqueDeivceID);
            if (ApplicationData.isDummyFlow) {
                Constants.isLogin = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetFields() {
        passwordController.resetPassword();
        mobileNo.setText("");
        mobileId.setText("");
        mobileId.requestFocus();
    }

    public void processResponse(HttpResponseModel response) {
        if (response == null) {
            return;
        }
        try {
            if (otpDialog != null) {
                otpDialog.dismiss();
            }
            hideLoading();
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table.containsKey(Constants.KEY_LIST_MSGS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                if (message.code == null) {

                    dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                            LoginActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                    try {
                                        showLoading("Please Wait", "Authenticating...");
                                        String encryptedPin = AesEncryptor.encrypt(strPassword);
                                        new HttpAsyncTask(LoginActivity.this).execute(
                                                Constants.CMD_LOGIN + "",
                                                strUsername,
                                                encryptedPin,
                                                PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.CATALOG_VERSION, "-1"),
                                                strIsRooted,
                                                strAndroidVersion,
                                                strDeviceModel,
                                                strDeviceVendor,
                                                strDeviceNetwork,
                                                strUniqueDeivceID);
                                        if (ApplicationData.isDummyFlow) {
                                            Constants.isLogin = 0;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, false, PopupDialogs.Status.SUCCESS, false, null);


                } else {
                    otpDialog = popupDialogs.otpPopupDialog(message.getCode(), message.getDescr(), LoginActivity.this, new PopupDialogs.OnCustomClickListener() {

                        @Override
                        public void onClick(View v, Object obj) {
                            showLoading("Please Wait", "Authenticating...");
                            String encryptedPin = AesEncryptor.encrypt(strPassword);
                            new HttpAsyncTask(LoginActivity.this).execute(
                                    Constants.CMD_LOGIN + "",
                                    strUsername,
                                    encryptedPin,
                                    PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.CATALOG_VERSION, "-1"),
                                    strIsRooted,
                                    strAndroidVersion,
                                    strDeviceModel,
                                    strDeviceVendor,
                                    strDeviceNetwork,
                                    strUniqueDeivceID);
                            if (ApplicationData.isDummyFlow) {
                                Constants.isLogin = 1;
                            }
                        }
                    }, true);
                    etOtp = (EditText) otpDialog.findViewById(R.id.txtPin);
                }
            } else if (table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                hideLoading();
                resetFields();
                if (message.getCode().equals(Constants.ErrorCodes.LOGIN_FAILED_LEVEL2)
                        && Integer.parseInt(message.getLevel()) == Constants.VersionUsageLevel.BLOCK) {
                    PreferenceConnector.writeInteger(
                            LoginActivity.this,
                            PreferenceConnector.VERSION_USAGE_LEVEL,
                            Constants.VersionUsageLevel.BLOCK);
                    checkAppVersionUsageLevel(message.getDescr());
                } else if (message.getCode().equals(Constants.ErrorCodes.LOGIN_OTP_EXPIRE_ERROR)) {


                    dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                            LoginActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


//                    PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification), LoginActivity.this, PopupDialogs.Status.ERROR);

                } else if (message.getCode().equals(Constants.ErrorCodes.ACCOUNT_BLOCKED)) {


                    dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                            LoginActivity.this, getString(R.string.reset_pin), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, LoginPinChangeInputActivity.class);
                                    intent.putExtra(Constants.IntentKeys.MOBILE_ID, mobileIdStr);
                                    intent.putExtra(Constants.IntentKeys.MOBILE_NO, mobileNoStr);
                                    startActivity(intent);
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


//                    PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification), LoginActivity.this, PopupDialogs.Status.ERROR);

                } else if (message.getCode().equals(Constants.ErrorCodes.LOGIN_OTP_VERIFICATION) ||
                        message.getCode().equals(Constants.ErrorCodes.LOGIN_OTP_ERROR)) {
                    otpDialog = popupDialogs.otpPopupDialog(message.getCode(), message.getDescr(), LoginActivity.this, new PopupDialogs.OnCustomClickListener() {

                        @Override
                        public void onClick(View v, Object obj) {
                            showLoading("Please Wait", "Authenticating...");
                            if (v.getId() == R.id.btnOK) {
                                strOtp = (String) obj;
                                if (ApplicationData.isDummyFlow) {
                                    if (strOtp.equals("1111")) {
                                        Constants.isOTP = 1;
                                    } else {
                                        if (Constants.otpCount % 3 != 0) {
                                            Constants.isOTP = 0;
                                        } else {
                                            Constants.isOTP = 2;
                                        }
                                    }
                                }
                                new HttpAsyncTask(LoginActivity.this).execute(
                                        Constants.CMD_LOGIN_OTP_VERIFICATION + "",
                                        strUsername,
                                        AesEncryptor.encrypt(strOtp),
                                        PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.CATALOG_VERSION, "-1"),
                                        strIsRooted,
                                        strAndroidVersion,
                                        strDeviceModel,
                                        strDeviceVendor,
                                        strDeviceNetwork,
                                        strUniqueDeivceID);
                                Constants.otpCount++;
                            }
                        }
                    }, true);
                    etOtp = (EditText) otpDialog.findViewById(R.id.txtPin);
                } else if (message.getCode().equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_VERIFICATION) ||
                        message.getCode().equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_RESEND) ||
                        message.getCode().equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_REGENERATION) ||
                        message.getCode().equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_ERROR) ||
                        message.getCode().equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_EXPIRE_ERROR)) {
                    otpDialog = popupDialogs.otpPopupDialog(message.getCode(), message.getDescr(), LoginActivity.this, new PopupDialogs.OnCustomClickListener() {

                        @Override
                        public void onClick(View v, Object obj) {
                            showLoading("Please Wait", "Authenticating...");
                            if (v.getId() == R.id.btnOK) {
                                strOtp = (String) obj;
                                if (ApplicationData.isDummyFlow) {
                                    if (strOtp.equals("1111")) {
                                        Constants.isOTP = 1;
                                    } else {
                                        if (Constants.otpCount % 3 != 0) {
                                            Constants.isOTP = 0;
                                        } else {
                                            Constants.isOTP = 2;
                                        }
                                    }
                                }
                                strAction = "0";
                                new HttpAsyncTask(LoginActivity.this).execute(
                                        Constants.CMD_DEVICE_UPDATE_OTP_VERIFICATION + "",
                                        strAction,
                                        strUniqueDeivceID,
                                        AesEncryptor.encrypt(strOtp),
                                        strUsername,
                                        PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.CATALOG_VERSION, "-1"));
                                Constants.otpCount++;
                            } else if (v.getId() == R.id.btnResend) {
                                if (ApplicationData.isDummyFlow) {
                                    Constants.isOTP = 3;
                                }
                                isOtpResend = true;
                                strAction = "1";
                                new HttpAsyncTask(LoginActivity.this).execute(
                                        Constants.CMD_DEVICE_UPDATE_OTP_VERIFICATION + "",
                                        strAction,
                                        strUniqueDeivceID,
                                        strUsername,
                                        PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.CATALOG_VERSION, "-1"));
                            } else {
                                if (ApplicationData.isDummyFlow) {
                                    Constants.isOTP = 4;
                                }
                                strAction = "2";
                                new HttpAsyncTask(LoginActivity.this).execute(
                                        Constants.CMD_DEVICE_UPDATE_OTP_VERIFICATION + "",
                                        strAction,
                                        strUniqueDeivceID,
                                        strUsername,
                                        PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.CATALOG_VERSION, "-1"));
                            }
                        }
                    }, true);
                    etOtp = (EditText) otpDialog.findViewById(R.id.txtPin);
                    btnResend = (Button) otpDialog.findViewById(R.id.btnResend);
                    if (countDownTimer!=null)
                        countDownTimer.cancel();
                    if (isOtpResend)
                        timerCall();
                } else {

                    dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                            LoginActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                }
            } else {


                ApplicationData.isDestroyed = false;

                @SuppressWarnings("unchecked")
                ArrayList<CategoryModel> listCatalog = (ArrayList<CategoryModel>) (table.get(Constants.KEY_LIST_CATALOG));

                String strCatCode = PreferenceConnector.readString(this, PreferenceConnector.CATALOG_VERSION, "-1");
                String strCatCodeCurrent = table.get(Constants.KEY_CAT_VER) != null ? table.get(Constants.KEY_CAT_VER).toString() : "";

                if (listCatalog == null && !strCatCodeCurrent.equals("") && !strCatCode.equals(strCatCodeCurrent)) {
                    ApplicationData.listCategories = new ArrayList<CategoryModel>();
                    String result = "<categories>  </categories>";
                    if (result != null) {
                        PreferenceConnector.writeString(
                                this,
                                PreferenceConnector.CATALOG_VERSION,
                                table.get(Constants.KEY_CAT_VER).toString());
                        PreferenceConnector.writeString(this, PreferenceConnector.CATALOG_DATA, result);
                    }

                } else if (listCatalog != null && listCatalog.size() > 0) {
                    ApplicationData.listCategories = new ArrayList<CategoryModel>();
                    ApplicationData.listCategories.addAll(listCatalog);

                    String result = response.getXmlResponse().substring(
                            response.getXmlResponse().indexOf("<categories>"),
                            response.getXmlResponse().indexOf("</categories>") + "</categories>".length());
                    if (result != null) {
                        PreferenceConnector.writeString(
                                this,
                                PreferenceConnector.CATALOG_VERSION,
                                table.get(Constants.KEY_CAT_VER).toString());
                        PreferenceConnector.writeString(this, PreferenceConnector.CATALOG_DATA, result);
                    }
                } else    // use saved catalog
                {
                    String result = PreferenceConnector.readString(this, PreferenceConnector.CATALOG_DATA, "");
                    if (result != null) {
                        ApplicationData.listCategories = new ArrayList<CategoryModel>();
                        ApplicationData.listCategories.addAll(xmlParser.parseLocalCatelog(result));
                    }
                }
                ArrayList<?> listBanks = (ArrayList<?>) table.get(Constants.KEY_LIST_USR_BANK);
                if (listBanks != null && listBanks.size() > 0) {

                } else {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.BANK_ACCOUNT_ERROR, AppMessages.ALERT_HEADING,
                            LoginActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                    resetFields();
                    hideLoading();
                    return;
                }

                BankModel bank = (BankModel) listBanks.get(0);
                ArrayList<?> listBankAccounts = null;

                if (bank.bankAccs != null) {
                    listBankAccounts = (ArrayList<?>) bank.bankAccs;
                }

                if (listBankAccounts != null && listBankAccounts.size() > 0) {
                    ApplicationData.listBankAccounts = (ArrayList<?>) listBankAccounts;
                } else {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.BANK_ACCOUNT_ERROR, AppMessages.ALERT_HEADING,
                            LoginActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                    resetFields();
                    hideLoading();
                    return;
                }

                BankAccountModel account = (BankAccountModel) ApplicationData.listBankAccounts.get(0);


                if (table.get(XmlConstants.Attributes.ATYPE) != null) {
                    ApplicationData.agentAccountType = Integer.parseInt(table.get(XmlConstants.Attributes.ATYPE).toString());
                    switch (ApplicationData.agentAccountType) {
                        case Constants.AgentAccountType.HANDLER:
                            break;

                        case Constants.AgentAccountType.NORMAL:
                            if (table.get("BAL") != null) {
                                ApplicationData.balance = table.get("BAL").toString();
                                ApplicationData.formattedBalance = table.get("BALF").toString();
                                ApplicationData.accoutTittle = table.get("FNAME").toString() + " " + table.get("LNAME").toString();
                            } else {
                                PopupDialogs.createAlertDialog(AppMessages.FETCH_BALANCE_ERROR,
                                        getString(R.string.alertNotification), LoginActivity.this, PopupDialogs.Status.ERROR);
                                resetFields();
                                hideLoading();
                                return;
                            }
                            break;

                        case Constants.AgentAccountType.CONSUMER:
                            if (table.get("BAL") != null) {
                                ApplicationData.balance = table.get("BAL").toString();
                                ApplicationData.formattedBalance = table.get("BALF").toString();
                                ApplicationData.accoutTittle = table.get("FNAME").toString() + " " + table.get("LNAME").toString();

                            } else {

                                dialogGeneral = popupDialogs.createAlertDialog(AppMessages.FETCH_BALANCE_ERROR, AppMessages.ALERT_HEADING,
                                        LoginActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogGeneral.dismiss();
                                            }
                                        }, false, PopupDialogs.Status.ERROR, false, null);

//                                PopupDialogs.createAlertDialog(AppMessages.FETCH_BALANCE_ERROR,
//                                        getString(R.string.alertNotification), LoginActivity.this, PopupDialogs.Status.ERROR);
                                resetFields();
                                hideLoading();
                            }
                            break;
                    }
                }

                populateMyAccountMenu();
                ApplicationData.firstName = table.get("FNAME").toString();
                ApplicationData.lastName = table.get("LNAME").toString();
                ApplicationData.cnic = table.get("CNIC").toString();
                ApplicationData.mobileNo = table.get("MOBN").toString();
                ApplicationData.isPinChangeRequired = table.get("IPCR").toString();
                ApplicationData.senderIban = table.get("SENDER_IBAN").toString();
//                ApplicationData.customerMobileNumber = table.get("MOBN").toString();
                ApplicationData.bankId = bank.id;
                ApplicationData.accountId = account.getId();


                if (table.get(XmlConstants.Attributes.ATTR_ADTYPE) != null)
                    ApplicationData.adType = Integer.parseInt(table.get(XmlConstants.Attributes.ATTR_ADTYPE).toString());
                if (table.get(XmlConstants.Attributes.ATTR_VIDEOLINK) != null)
                    ApplicationData.videoLink = table.get(XmlConstants.Attributes.ATTR_VIDEOLINK).toString();


                if (table.get("IPCR") != null
                        && table.get("IPCR").toString().equals("1")
                        && (table.get("PGR") != null
                        && table.get("PGR").toString().equals("1"))) {
                    hideLoading();
                    ApplicationData.isLogin = false;
                    Intent intent = new Intent(LoginActivity.this, MyAccountChangePinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(
                            Constants.IntentKeys.PRODUCT_MODEL,
                            new ProductModel("10027", Constants.FLOW_ID_CHANGE_PIN + "", "Change Login Password"));
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.LOGIN_PIN);
                    intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.SET_MPIN);
                    intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, true);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    finish();
                } else if (table.get("IPCR") != null
                        && table.get("IPCR").toString().equals("1")
                        && (table.get("IMPCR") != null
                        && table.get("IMPCR").toString().equals("1"))) {
                    hideLoading();
                    ApplicationData.isLogin = false;
                    Intent intent = new Intent(LoginActivity.this, MyAccountChangePinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(
                            Constants.IntentKeys.PRODUCT_MODEL,
                            new ProductModel("10027", Constants.FLOW_ID_CHANGE_PIN + "", "Change Login Password"));
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.LOGIN_PIN);
                    intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.MPIN);
                    intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, true);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else if (table.get("PGR") != null && table.get("PGR").toString().equals("1")) {
                    hideLoading();
                    ApplicationData.isLogin = false;
                    Intent intent = new Intent(LoginActivity.this, MyAccountChangePinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, new ProductModel("10029", Constants.FLOW_ID_CHANGE_PIN + "", "Set MPIN"));
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.SET_MPIN);
                    intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.IGNORE_AND_GOTO_MENU);
                    intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, false);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else if (table.get("IPCR") != null && table.get("IPCR").toString().equals("1")) {
                    hideLoading();
                    ApplicationData.isLogin = false;
                    Intent intent = new Intent(LoginActivity.this, MyAccountChangePinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, new ProductModel("10027", Constants.FLOW_ID_CHANGE_PIN + "", "Change Login PIN"));
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.LOGIN_PIN);
                    intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, false);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    finish();
                } else if (table.get("IMPCR") != null && table.get("IMPCR").toString().equals("1")) {

                    hideLoading();
                    ApplicationData.isLogin = false;
                    Intent intent = new Intent(LoginActivity.this, MyAccountChangePinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(
                            Constants.IntentKeys.PRODUCT_MODEL,
                            new ProductModel("10028", Constants.FLOW_ID_CHANGE_PIN + "", "Change MPIN"));
                    intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.MPIN);
                    intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.IGNORE_AND_GOTO_MENU);
                    intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, false);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    finish();
                } else if (table.get("IS_MIGRATED") != null && table.get("IS_MIGRATED").toString().equals("1")) {
                    if (table.get(XmlConstants.KEY_LIST_ADS) != null) {
                        @SuppressWarnings("unchecked")
                        ArrayList<AdModel> listAds = (ArrayList<AdModel>) (table.get(XmlConstants.KEY_LIST_ADS));
                        ApplicationData.listAds = new ArrayList<AdModel>();
                        ApplicationData.listAds.addAll(listAds);
                    }
                    if (table.get("IS_SET_MPIN_LATER") != null && table.get("IS_SET_MPIN_LATER").toString().equals("1")) {
                        dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.APPLICATION_VERSION_LEVEL_CRITICAL, AppMessages.ALERT_HEADING,
                                LoginActivity.this, "DOWNLOAD", "OK", new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ApplicationData.isMpinSet = false;
                                        ApplicationData.isLogin = true;
                                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                        openMarketUrl();
                                    }
                                }, new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.cancel();
                                        ApplicationData.isMpinSet = false;
                                        ApplicationData.isLogin = true;
                                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                    }
                                }, false, PopupDialogs.Status.ALERT, false);
                    } else {
                        hideLoading();
                        ApplicationData.isLogin = false;
                        Intent intent = new Intent(LoginActivity.this, MyAccountChangePinActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(
                                Constants.IntentKeys.PRODUCT_MODEL,
                                new ProductModel("10028", Constants.FLOW_ID_CHANGE_PIN + "", "SET MPIN"));
                        intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.SET_MPIN);
                        intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.IGNORE_AND_GOTO_MENU);
                        intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, false);
                        intent.putExtra(Constants.IntentKeys.MPIN_FROM_LOGIN, true);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                        finish();
                    }

                } else {


                    if (table.get(XmlConstants.KEY_LIST_ADS) != null) {
                        @SuppressWarnings("unchecked")
                        ArrayList<AdModel> listAds = (ArrayList<AdModel>) (table.get(XmlConstants.KEY_LIST_ADS));
                        ApplicationData.listAds = new ArrayList<AdModel>();
                        ApplicationData.listAds.addAll(listAds);
                    }


                    // application version usage level set
                    if (!processVersionUsageLevel(table))
                        return;

                    hideLoading();
                    ApplicationData.isMpinSet = true;
                    removeSetMpin();
                    ApplicationData.isLogin = true;
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            resetFields();
        }
    }

    private void timerCall() {
        btnResend.setEnabled(false);
        isOtpResend=false;
        countDownTimer =  new CountDownTimer(Constants.OTP_RESEND_TIMER, 1000) { //Timer for 60 seconds
            public void onTick(long millisUntilFinished) {
                btnResend.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                btnResend.setEnabled(true);
                btnResend.setText("Resend");
            }
        }.start();
    }

    private void populateMyAccountMenu() {

        boolean isMyAccountCatFound = false;

        ProductModel productBlbMiniStatement = new ProductModel(
                Constants.PRODUCT_ID_BLB_MINI_STATEMENT,
                Constants.FLOW_ID_MINI_STATEMENT + "",
                "BB Account");
        ProductModel productBlbBalanceInquiry = new ProductModel(
                Constants.PRODUCT_ID_BLB_BALANCE_INQUIRY,
                Constants.FLOW_ID_BALANCE_INQUIRY + "",
                "BB Account");


        ProductModel productHRAMiniStatement = new ProductModel(
                Constants.PRODUCT_ID_HRA_MINI_STATEMENT,
                Constants.FLOW_ID_MINI_STATEMENT + "",
                "HRA");
        ProductModel productHRABalanceInquiry = new ProductModel(
                Constants.PRODUCT_ID_HRA_BALANCE_INQUIRY,
                Constants.FLOW_ID_BALANCE_INQUIRY + "",
                "HRA");


        ProductModel productChangePassword = new ProductModel(
                Constants.PRODUCT_ID_CHANGE_LOGIN_PASSWORD,
                Constants.FLOW_ID_CHANGE_PIN + "",
                "Change Login PIN");

        ProductModel productChangeMpin = new ProductModel(
                Constants.PRODUCT_ID_CHANGE_MPIN,
                Constants.FLOW_ID_CHANGE_PIN + "",
                "Change MPIN");

        ProductModel productMyLimits = new ProductModel(
                Constants.PRODUCT_ID_MY_LIMITS,
                Constants.FLOW_ID_MY_LIMITS + "",
                "My Limits");

        ProductModel productGenerateMpin = new ProductModel(
                "1541",
                Constants.FLOW_ID_GENERATE_MPIN + "",
                "Set MPIN");

//        ProductModel productHRAAccount = new ProductModel(
//                Constants.PRODUCT_ID_HRA_ACCOUNT,
//                Constants.FLOW_ID_HRA_ACCOUNT + "",
//                "HRA Account Opening");

        ProductModel productL0toL1LAccount = new ProductModel(
                Constants.PRODUCT_ID_L0_TO_L1_ACCOUNT,
                Constants.FLOW_ID_L0_TO_L1_ACCOUNT + "",
                "Update L0 to L1");

        ProductModel productForgotMpin = new ProductModel(
                Constants.PRODUCT_ID_FORGOT_MPIN,
                Constants.FLOW_ID_FORGOT_MPIN + "",
                "Forgot MPIN");

        ProductModel productCheckIban = new ProductModel(
                Constants.PRODUCT_ID_CHECK_IBAN,
                Constants.FLOW_ID_CHECK_IBAN + "",
                "View My IBAN");


        for (int i = 0; i < ApplicationData.listCategories.size(); i++) {

            if (ApplicationData.listCategories.get(i).getId() != null &&
                    ApplicationData.listCategories.get(i).getId().equals(Constants.CATEGORY_ID_MY_ACCOUNT)) {

                isMyAccountCatFound = true;

                ApplicationData.listCategoriesMyAccount = new ArrayList<>();
                ApplicationData.listCategoriesMyAccount.add(ApplicationData.listCategories.get(i));


                ApplicationData.listCategories.remove(i);

                CategoryModel categoryMiniStatement = new CategoryModel("13", "Mini Statement", "", "0", ApplicationData.listCategories.get(i));
                categoryMiniStatement.addProduct(productBlbMiniStatement);
                categoryMiniStatement.addProduct(productHRAMiniStatement);

                CategoryModel categoryChangeLoginPassword = new CategoryModel("14", "Change Login PIN.", "", "1", ApplicationData.listCategories.get(i));
                categoryChangeLoginPassword.addProduct(productChangePassword);

                CategoryModel categoryChangeMpin = new CategoryModel("15", "Change MPIN", "", "1", ApplicationData.listCategories.get(i));
                categoryChangeMpin.addProduct(productChangeMpin);

                CategoryModel categoryCheckBalance = new CategoryModel("16", "Check Balance", "", "0", ApplicationData.listCategories.get(i));
                categoryCheckBalance.addProduct(productBlbBalanceInquiry);
                categoryCheckBalance.addProduct(productHRABalanceInquiry);

                CategoryModel categoryMyLimits = new CategoryModel("17", "My Limits", "", "1", ApplicationData.listCategories.get(i));
                categoryMyLimits.addProduct(productMyLimits);

                CategoryModel categoryGenerateMpin = new CategoryModel("18", "Set MPIN", "", "1", ApplicationData.listCategories.get(i));
                categoryGenerateMpin.addProduct(productGenerateMpin);

//                CategoryModel categoryHRAAccount = new CategoryModel("19", "HRA Account Opening ", "", "1", ApplicationData.listCategories.get(i));
//                categoryHRAAccount.addProduct(productHRAAccount);

                CategoryModel categoryL0toL1Account = new CategoryModel("20", "Update L0 to L1", "", "1", ApplicationData.listCategories.get(i));
                categoryL0toL1Account.addProduct(productL0toL1LAccount);
                CategoryModel categoryForgotLogin = new CategoryModel("22", "Forgot MPIN", "", "1", ApplicationData.listCategories.get(i));
                categoryForgotLogin.addProduct(productForgotMpin);
                CategoryModel categoryCheckIban = new CategoryModel("23", "View My IBAN", "", "1", ApplicationData.listCategories.get(i));
                categoryCheckIban.addProduct(productCheckIban);


                CategoryModel categoryManagePin = new CategoryModel("21", "PIN Management", "", "0", ApplicationData.listCategories.get(i));
                categoryManagePin.addCategory(categoryChangeLoginPassword);
                categoryManagePin.addCategory(categoryChangeMpin);
                categoryManagePin.addCategory(categoryGenerateMpin);
                categoryManagePin.addCategory(categoryForgotLogin);

//                ApplicationData.listCategoriesMyAccount.add(categoryMyAccount);


                ApplicationData.listCategoriesMyAccount.get(0).addCategory(categoryMiniStatement);
                ApplicationData.listCategoriesMyAccount.get(0).addCategory(categoryManagePin);
//                ApplicationData.listCategoriesMyAccount.get(i).addCategory(categoryChangeLoginPassword);
//                ApplicationData.listCategoriesMyAccount.get(i).addCategory(categoryChangeMpin);
//                ApplicationData.listCategoriesMyAccount.get(i).addCategory(categoryGenerateMpin);
                ApplicationData.listCategoriesMyAccount.get(0).addCategory(categoryCheckBalance);
                ApplicationData.listCategoriesMyAccount.get(0).addCategory(categoryCheckIban);
                ApplicationData.listCategoriesMyAccount.get(0).addCategory(categoryMyLimits);
//                ApplicationData.listCategoriesMyAccount.get(i).addCategory(categoryHRAAccount);
                ApplicationData.listCategoriesMyAccount.get(0).addCategory(categoryL0toL1Account);
            }
        }

        if (!isMyAccountCatFound) {

            CategoryModel categoryMyAccount = new CategoryModel("33", "My Account", "main_icon_my_account.png", "0", null);

            CategoryModel categoryMiniStatement = new CategoryModel("13", "Mini Statement", "", "0", categoryMyAccount);
            categoryMiniStatement.addProduct(productBlbMiniStatement);
            categoryMiniStatement.addProduct(productHRAMiniStatement);

            CategoryModel categoryChangeLoginPassword = new CategoryModel("14", "Change Login PIN", "", "1", categoryMyAccount);
            categoryChangeLoginPassword.addProduct(productChangePassword);

            CategoryModel categoryChangeMpin = new CategoryModel("15", "Change MPIN", "", "1", categoryMyAccount);
            categoryChangeMpin.addProduct(productChangeMpin);

            CategoryModel categoryCheckBalance = new CategoryModel("16", "Check Balance", "", "0", categoryMyAccount);
            categoryCheckBalance.addProduct(productBlbBalanceInquiry);
            categoryCheckBalance.addProduct(productHRABalanceInquiry);

            CategoryModel categoryMyLimits = new CategoryModel("17", "My Limits", "", "1", categoryMyAccount);
            categoryMyLimits.addProduct(productMyLimits);

            CategoryModel categoryGenerateMpin = new CategoryModel("18", "Set MPIN", "", "1", categoryMyAccount);
            categoryGenerateMpin.addProduct(productGenerateMpin);


//            CategoryModel categoryHRAAccount = new CategoryModel("19", "HRA Account Opening ", "", "1", categoryMyAccount);
//            categoryHRAAccount.addProduct(productHRAAccount);


            CategoryModel categoryL0toL1Account = new CategoryModel("20", "Update L0 to L1", "", "1", categoryMyAccount);
            categoryL0toL1Account.addProduct(productL0toL1LAccount);

            CategoryModel categoryForgotLogin = new CategoryModel("22", "Forgot MPIN", "", "1", categoryMyAccount);
            categoryForgotLogin.addProduct(productForgotMpin);

            CategoryModel categoryCheckIban = new CategoryModel("23", "View My IBAN", "", "1", categoryMyAccount);
            categoryCheckIban.addProduct(productCheckIban);


            CategoryModel categoryManagePin = new CategoryModel("21", "PIN Management", "", "0", categoryMyAccount);
            categoryManagePin.addCategory(categoryChangeLoginPassword);
            categoryManagePin.addCategory(categoryChangeMpin);
            categoryManagePin.addCategory(categoryGenerateMpin);
            categoryManagePin.addCategory(categoryForgotLogin);


            categoryMyAccount.addCategory(categoryMiniStatement);
            categoryMyAccount.addCategory(categoryManagePin);
//            categoryMyAccount.addCategory(categoryChangeLoginPassword);
//            categoryMyAccount.addCategory(categoryChangeMpin);
//            categoryMyAccount.addCategory(categoryGenerateMpin);
            categoryMyAccount.addCategory(categoryCheckBalance);
            categoryMyAccount.addCategory(categoryCheckIban);
            categoryMyAccount.addCategory(categoryMyLimits);
//            categoryMyAccount.addCategory(categoryHRAAccount);
            categoryMyAccount.addCategory(categoryL0toL1Account);
            ApplicationData.listCategoriesMyAccount = new ArrayList<>();

            ApplicationData.listCategoriesMyAccount.add(categoryMyAccount);
        }
    }


    @Override
    protected void onResume() {
        SharedPreferences preferences = getSharedPreferences("Session_timepey", MODE_PRIVATE);
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
        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void checkAppVersionUsageLevel(String msg) {

        int level = PreferenceConnector.readInteger(LoginActivity.this, PreferenceConnector.VERSION_USAGE_LEVEL, 0);

        if (level == Constants.VersionUsageLevel.NORMAL) {

        } else if (level == Constants.VersionUsageLevel.CRITICAL) {

        } else if (level == Constants.VersionUsageLevel.BLOCK) {
            btnLogin.setEnabled(false);


            dialogGeneral = popupDialogs.createAlertDialog((msg != null ? msg : AppMessages.APPLICATION_VERSION_LEVEL_OBSOLETE), AppMessages.ALERT_HEADING,
                    LoginActivity.this, getString(R.string.download), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                            hideLoading();
                            openMarketUrl();
                            finish();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

        } else if (level == Constants.VersionUsageLevel.OBSOLETE) {
            btnLogin.setEnabled(false);


            dialogGeneral = popupDialogs.createAlertDialog((msg != null ? msg : AppMessages.APPLICATION_VERSION_LEVEL_OBSOLETE), AppMessages.ALERT_HEADING,
                    LoginActivity.this, getString(R.string.download), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                            hideLoading();
                            finish();
                            openMarketUrl();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

        }
    }

    private boolean processVersionUsageLevel(Hashtable<?, ?> table) {
        int level = Integer.parseInt((String) table.get("APUL"));

        PreferenceConnector.writeInteger(LoginActivity.this, PreferenceConnector.VERSION_USAGE_LEVEL, level);

        if (level == Constants.VersionUsageLevel.NORMAL) {
            return true;
        } else if (level == Constants.VersionUsageLevel.CRITICAL) {

            dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.APPLICATION_VERSION_LEVEL_CRITICAL, AppMessages.ALERT_HEADING,
                    LoginActivity.this, "DOWNLOAD", "OK", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ApplicationData.isLogin = true;
                            ApplicationData.isMpinSet = true;
                            removeSetMpin();
                            startActivity(intent);
                            finish();
                            hideLoading();
                            openMarketUrl();
                        }
                    }, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.cancel();
                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ApplicationData.isLogin = true;
                            ApplicationData.isMpinSet = true;
                            removeSetMpin();
                            startActivity(intent);
                            finish();

                        }
                    }, false, PopupDialogs.Status.ALERT, false);

            return false;
        } else if (level == Constants.VersionUsageLevel.OBSOLETE) {
            btnLogin.setEnabled(false);

            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.APPLICATION_VERSION_LEVEL_OBSOLETE, AppMessages.ALERT_HEADING,
                    LoginActivity.this, getString(R.string.download), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                            hideLoading();
                            finish();
                            openMarketUrl();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);


            dialogGeneral = PopupDialogs.createAlertDialog(AppMessages.APPLICATION_VERSION_LEVEL_OBSOLETE,
                    getString(R.string.alertNotification), LoginActivity.this, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.cancel();
                            hideLoading();
                            finish();
                            openMarketUrl();
                        }
                    }, "DOWNLOAD", PopupDialogs.Status.ERROR);

            return false;
        } else {
            return false;
        }
    }

    private void resetVersionUsageData() {
        if (PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.APP_VERSION, "1.0f").equals(Constants.APPLICATION_VERSION)) {
            PreferenceConnector.writeInteger(LoginActivity.this, PreferenceConnector.VERSION_USAGE_LEVEL, 0);
            PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.APP_VERSION, Constants.APPLICATION_VERSION);
        }
    }


    private void initializeSmsReciever() {

        mSmsReceiver = new SmsReceiver(this);

        if (!isSmsReceiverRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(mSmsReceiver, filter);
            isSmsReceiverRegistered = true;
        }

    }

    private void unregisterSmsReceiver() {
        if (isSmsReceiverRegistered) {
            unregisterReceiver(mSmsReceiver);
            isSmsReceiverRegistered = false;
        }
    }

    @Override
    public void gotSms(String otp) {
        if (otp != null && popupDialogs.passwordController != null) {
            popupDialogs.passwordController.setOTP(otp);
//            etOtp.setText(otp);
            unregisterSmsReceiver();
        }
        this.strOtp = otp;
    }

//    private void checkPermissions() {
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return;
//        }
//
//        int result;
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String p : permissions) {
//            result = ContextCompat.checkSelfPermission(this, p);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(p);
//            }
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSIONS);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    public void headerImplementationLogin() {
        btnBack = (AppCompatImageView) findViewById(R.id.btnBack);
        btnHome = (AppCompatImageView) findViewById(R.id.btnHome);
        btnSignout = (AppCompatImageView) findViewById(R.id.btnSignout);
        viewLeft = findViewById(R.id.viewLeft);

        findViewById(R.id.ivHeaderLogo).setVisibility(View.GONE);
        headerText = (TextView) findViewById(R.id.headerText);
        headerText.setVisibility(View.VISIBLE);
        headerText.setText(getString(R.string.login));

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                ApplicationData.isWebViewOpen = false;
                ApplicationData.webUrl = null;
                finish();
            }
        });

        if (!ApplicationData.isLogin) {
            btnHome.setVisibility(View.GONE);
            btnSignout.setVisibility(View.GONE);
        } else {
            btnHome.setVisibility(View.VISIBLE);
            btnSignout.setVisibility(View.VISIBLE);
            btnHome.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationData.webUrl = null;
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }

        btnSignout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                signOut();
            }
        });
    }


}