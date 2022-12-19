package com.inov8.jsblconsumer.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.inov8.jsblconsumer.activities.contactus.ContactUsActivity;
import com.inov8.jsblconsumer.activities.faq.FaqsActivity;
import com.inov8.jsblconsumer.activities.locator.LocatorActivity;
import com.inov8.jsblconsumer.activities.locator.LocatorComingSoonActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountChangePinActivity;
import com.inov8.jsblconsumer.activities.retailPayment.retailpaymentmpass.RetailPaymentActivity;
import com.inov8.jsblconsumer.activities.retailPayment.retailpaymentmpass.RetailPaymentComfirmationMPassActivity;
import com.inov8.jsblconsumer.activities.retailPayment.retailpaymentmpass.RetailPaymentReceiptActivity;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AesEncryptor;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.PasswordController;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.bouncycastle.crypto.tls.ContentType.alert;

public class BaseActivity extends FragmentActivity implements View.OnClickListener {

    public Bundle mBundle;
    public ProgressDialog loadingDialog;
    private Dialog dialog;
    public Dialog dialogGeneral;
    public TextView textViewAvailableBalance, tvAvailableBalancePoint,tvIbanNumber,
            txtviewName, txtViewTitle, txtViewSubTitle, headerText, textViewAccountTittle;
    protected ImageView ivHeaderLogo;
    protected AppCompatImageView btnSignout, btnHome, btnBack, ivHeaderLog;
    protected View viewLeft;
    public int mListItemHieght = 22;
    public int mListViewOptionItemHeight = 44;

    private int keyboardHeight = 0;
    private String encryptedPin;
    private long mDeBounce = 0;
    private float mActionDownX, mActionDownY = 0;
    private boolean isScrolling = false;

    public int mAmountFieldLength = 7;
    public long mMaxAmount = 9999999;
    public PopupDialogs popupDialogs;
    private PasswordController passwordController;
    protected View bottomBar, bottomBarQR;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        popupDialogs = new PopupDialogs(this);
        mBundle = getIntent().getExtras();
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        screenDesity();
    }

    public void screenDesity() {
        int density = getResources().getDisplayMetrics().densityDpi;

        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                mListItemHieght = 22;
                mListViewOptionItemHeight = 47;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                mListItemHieght = 22;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                mListItemHieght = 22;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                mListItemHieght = 40;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                mListItemHieght = 47;
                break;
        }
    }

    public void goToLogin() {
        ApplicationData.isLogin = false;
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void gotoSplash() {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToMainMenu() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void goToStart() {
        Intent intent;
        if (ApplicationData.isLogin) {
            intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void goToMainMenu(Bundle bundle) {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void loginPage() {
        mBundle.putBoolean(Constants.IntentKeys.SESSION_OUT, true);
        goToMainMenu(mBundle);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean haveInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public void showLoading(String title, String message) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setTitle(title);
            loadingDialog.setMessage(message);
            loadingDialog.setCancelable(false);
            loadingDialog.setIndeterminate(false);
        }
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog.cancel();
        }
    }

    public void loadBalance() {
        textViewAvailableBalance = (TextView) findViewById(R.id.tvAvailableBalance);
        textViewAccountTittle = (TextView) findViewById(R.id.tvAccountTittle);
        tvAvailableBalancePoint = (TextView) findViewById(R.id.tvAvailableBalancePoint);
        tvIbanNumber = (TextView) findViewById(R.id.tvIbanNumber);
        String balance = ApplicationData.formattedBalance;
        tvAvailableBalancePoint.setText(StringUtils.right(balance, 3));
        int legth = balance.length();
        balance = balance.substring(0, legth - 3);
        textViewAvailableBalance.setText(balance);
        textViewAccountTittle.setText(ApplicationData.accoutTittle);
        tvIbanNumber.setText(ApplicationData.senderIban);
    }

    public void askMpin(final Bundle bundle, final Class<?> nextClass,
                        final boolean closeAcitivty) {
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
//        int setterHeight = 0;

        final Dialog dialog = new Dialog(BaseActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_pin);
        dialog.setCancelable(!closeAcitivty);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(R.color.dialog_background));
        dialog.show();


//        final RelativeLayout layoutRoot = (RelativeLayout) dialog
//                .findViewById(R.id.layout_root);
//
//        layoutRoot.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//                        Rect r = new Rect();
//                        layoutRoot.getWindowVisibleDisplayFrame(r);
//
//                        int screenHeight = layoutRoot.getRootView().getHeight();
//                        keyboardHeight = screenHeight - (r.bottom - r.top);
//                        AppLogger.i("Keyboard Size : " + keyboardHeight);
//                    }
//                });
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

//        setterHeight = height - keyboardHeight;

//        params.leftMargin = 10;
//        params.rightMargin = 10;
//        params.topMargin = (int) (setterHeight * 0.18);
//        layoutRoot.setLayoutParams(params);


        passwordController = new PasswordController(dialog.findViewById(R.id.layoutFPin), false, false, BaseActivity.this);

        final EditText userInput = (EditText) dialog
                .findViewById(R.id.editTextDialogUserInput);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);


        Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
        btnOk.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                hideKeyboard(arg0);
                if (TextUtils.isEmpty(passwordController.getPassword())) {
//                    passwordController.setError(AppMessages.ERROR_EMPTY_FIELD);

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.ERROR_EMPTY_PIN, AppMessages.ALERT_HEADING,
                            BaseActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                    return;
                } else if (passwordController.getPassword().length() < 4) {
                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.PIN_LENGTH, AppMessages.ALERT_HEADING,
                            BaseActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

//                    passwordController.setError(AppMessages.PIN_LENGTH);
                    return;
                }
                dialog.dismiss();

                try {
                    String encryptedPin = AesEncryptor.encrypt(passwordController.getPassword());
                    setEncryptedMpin(encryptedPin);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (bundle != null & nextClass != null) {
                    mpinProcess(bundle, nextClass);
                } else {
                    mpinProcess(null, null);
                }
            }
        });

        AppCompatImageView btnCance = (AppCompatImageView) dialog.findViewById(R.id.btnClose);
        btnCance.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                dialog.dismiss();
                if (closeAcitivty) {
                    BaseActivity.this.finish();
                }
            }
        });


        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                dialog.dismiss();
                if (closeAcitivty) {
                    BaseActivity.this.finish();
                }
            }
        });
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        showLoading("Please Wait", "Processing...");
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    synchronized (this) {
                        wait(1750);
                    }
                } catch (InterruptedException ex) {
                }

                hideLoading();
                Intent intent = new Intent(BaseActivity.this, nextClass);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                startActivity(intent);
                finish();
            }
        };
        mThread.start();
    }

    public String getEncryptedMpin() {
        return encryptedPin;
    }

    public void setEncryptedMpin(String encryptedMpin) {
        encryptedPin = encryptedMpin;
    }

    public void openMarketUrl() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri
                .parse(Constants.APP_DOWNLOAD_URL));
        startActivity(intent);
    }

    public void titleImplementation(String imgName, String title, String subTitle, Context context) {
        ivHeaderLogo = (ImageView) findViewById(R.id.icon);
        txtViewTitle = (TextView) findViewById(R.id.lblHeading);
        txtViewSubTitle = (TextView) findViewById(R.id.lblSubHeading);

        if (imgName != null) {
            int idDrawable = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
            ivHeaderLogo.setImageDrawable(AppCompatResources.getDrawable(context, idDrawable));
            ivHeaderLogo.setColorFilter
                    (ContextCompat.getColor(getApplicationContext(), R.color.menu_icon_normal));
//            ivHeaderLogo.setImageResource(context.getResources().getIdentifier(imgName, "drawable", context.getPackageName()));
        }
        txtViewTitle.setText(title);
        if (subTitle != null) {
            txtViewSubTitle.setText(subTitle);
            txtViewSubTitle.setVisibility(View.VISIBLE);
        }
    }

    public void headerImplementation() {
        headerText = (TextView) findViewById(R.id.headerText);
        ivHeaderLog = (AppCompatImageView) findViewById(R.id.ivHeaderLogo);
        btnBack = (AppCompatImageView) findViewById(R.id.btnBack);
        btnHome = (AppCompatImageView) findViewById(R.id.btnHome);
        btnSignout = (AppCompatImageView) findViewById(R.id.btnSignout);
        viewLeft = findViewById(R.id.viewLeft);

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
//            btnHome.setVisibility(View.VISIBLE);
            btnSignout.setVisibility(View.VISIBLE);
            btnHome.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(v);
                    ApplicationData.webUrl = null;
                    Intent intent = new Intent(BaseActivity.this, MainMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }

//        btnSignout.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    //Do something on touch
//                    Toast.makeText(BaseActivity.this,"up",Toast.LENGTH_SHORT).show();
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    //Do something in release
//                    Toast.makeText(BaseActivity.this,"down",Toast.LENGTH_SHORT).show();
//                }
//
//
//                return true;
//            }
//        });

        btnSignout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                signOut();

//                dialog = PopupDialogs.createConfirmationDialog(
//                        getString(R.string.logout__text),
//                        getString(R.string.alertNotification), BaseActivity.this,
//                        new OnClickListener() {
//                            @Override
//                            public void onClick(View arg0) {
//                                if (!haveInternet()) {
//                                    PopupDialogs.createAlertDialog(
//                                            AppMessages.INTERNET_CONNECTION_PROBLEM,
//                                            AppMessages.ALERT_HEADING, BaseActivity.this,
//                                            PopupDialogs.Status.ERROR);
//                                } else
//                                    {
//                                    dialog.cancel();
//                                    Intent intent = new Intent(BaseActivity.this, SignOutActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }
//                        }, "Yes",
//                        new OnClickListener() {
//                            @Override
//                            public void onClick(View arg0) {
//                                dialog.cancel();
//                            }
//                        }, "NO", PopupDialogs.Status.INFO);
            }
        });
    }

    public void bottomBarImplementation(final Context context, final String id) {

        LinearLayout layoutMyAccount = (LinearLayout) findViewById(R.id.layoutMyAccount);
        LinearLayout layoutHome = (LinearLayout) findViewById(R.id.layoutHome);
        LinearLayout layoutNearByAgent = (LinearLayout) findViewById(R.id.layoutNearByAgent);
//        LinearLayout layoutQRCode = findViewById(R.id.layoutQRCode);
        LinearLayout layoutMore = (LinearLayout) findViewById(R.id.layoutMore);
        AppCompatImageView ivQRCode = (AppCompatImageView) findViewById(R.id.btnQRCode);

        this.context = context;


        layoutMyAccount.setOnClickListener(BaseActivity.this);
        layoutHome.setOnClickListener(BaseActivity.this);
        layoutNearByAgent.setOnClickListener(BaseActivity.this);
        ivQRCode.setOnClickListener(BaseActivity.this);
        layoutMore.setOnClickListener(BaseActivity.this);


        layoutMyAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof CategoryMenuActivity && id.equals("33")) {
                } else if (context instanceof MainMenuActivity) {
                    Intent intent = new Intent(BaseActivity.this, CategoryMenuActivity.class);
                    intent.putExtra(Constants.IntentKeys.LIST_CATEGORIES, ApplicationData.listCategoriesMyAccount);
                    intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS, 0);
                    startActivity(intent);
                } else {

                    ApplicationData.isMyAccountOpen = true;
                    Intent intent = new Intent(BaseActivity.this, MainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });

//        AppCompatImageButton btnTerms = (AppCompatImageButton) findViewById(R.id.btnTerms);
//        btnTerms.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                if (context instanceof LocatorComingSoonActivity) {
//                    finish();
//                }
//                renderWebView(Constants.URL_TERMS_OF_USE, getResources().getString(R.string.terms_and_conditions), R.drawable.heading_icon_terms);
//            }
//        });
//
//        AppCompatImageButton btnUserGuide = (AppCompatImageButton) findViewById(R.id.btnUserGuide);
//        btnUserGuide.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                if (context instanceof LocatorComingSoonActivity) {
//                    finish();
//                }
//                renderWebView(Constants.URL_USER_GUIDE, getResources().getString(R.string.user_guide), R.drawable.heading_icon_user_guide);
//            }
//        });
//
//        AppCompatImageButton btnLocator = (AppCompatImageButton) findViewById(R.id.btnLocator);
//        btnLocator.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
////                Toast.makeText(BaseActivity.this, "Coming Soon...", Toast.LENGTH_SHORT).show();
////                if (context instanceof LocatorComingSoonActivity) {
////                } else {
////                    Intent intent = new Intent(BaseActivity.this, LocatorComingSoonActivity.class);
////                    startActivity(intent);
////                }
//
//                Intent intent = new Intent(BaseActivity.this,
//                        LocatorActivity.class);
//                startActivity(intent);
//                if (ApplicationData.isWebViewOpen) {
//                    ApplicationData.isWebViewOpen = false;
//                    ApplicationData.webUrl = null;
//                    finish();
//                }
//            }
//        });
//
//        AppCompatImageButton btnFaqs = (AppCompatImageButton) findViewById(R.id.btnFaqs);
//        btnFaqs.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                if (context instanceof LocatorComingSoonActivity) {
//                    finish();
//                }
//                Intent intent = new Intent(BaseActivity.this,
//                        FaqsActivity.class);
//                startActivity(intent);
//                if (ApplicationData.isWebViewOpen) {
//                    ApplicationData.isWebViewOpen = false;
//                    ApplicationData.webUrl = null;
//                    finish();
//                }
//            }
//        });
//
//        AppCompatImageButton btnContactUs = (AppCompatImageButton) findViewById(R.id.btnContactUs);
//        btnContactUs.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                if (context instanceof LocatorComingSoonActivity) {
//                    finish();
//                }
//                Intent intent = new Intent(BaseActivity.this,
//                        ContactUsActivity.class);
//
//                intent.putExtra(Constants.IntentKeys.HEADING, getResources().getString(R.string.contact_us));
//                intent.putExtra(Constants.IntentKeys.ICON, R.drawable.heading_icon_contact);
//
//                startActivity(intent);
//                if (ApplicationData.isWebViewOpen) {
//                    ApplicationData.isWebViewOpen = false;
//                    ApplicationData.webUrl = null;
//                    finish();
//                }
//            }
//        });
    }

    public void renderWebView(String url, String heading, int icon) {
        if ((ApplicationData.webUrl != null)
                && (url.equals(ApplicationData.webUrl))) {
        } else {
            if (ApplicationData.isWebViewOpen) {
                ApplicationData.isWebViewOpen = false;
                ApplicationData.webUrl = null;
//                finish();
            }
            Intent intent = new Intent(BaseActivity.this, WebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.IntentKeys.WEB_URL, url);
            intent.putExtra(Constants.IntentKeys.HEADING, heading);
            intent.putExtra(Constants.IntentKeys.ICON, icon);
            startActivity(intent);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getSharedPreferences("Session_js_bank",
                MODE_PRIVATE);
        preferences.edit().putLong("pause_time", 0).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("Session_js_bank",
                MODE_PRIVATE);
        long openTime = System.currentTimeMillis();
        preferences.edit().putLong("pause_time", openTime).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        long sec = Constants.SESSION_TIMEEOUT * 1000; // Milliseconds in a day. Define this as a constant
        SharedPreferences preferences = getSharedPreferences("Session_js_bank", MODE_PRIVATE);
        long openTime = System.currentTimeMillis();
        long lastTime = preferences.getLong("pause_time", 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        if (ApplicationData.isLogin) {
            if (lastTime != 0) {
                if ((openTime - lastTime) > sec) {
                    // if any transaction is in process then close loading
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        hideLoading();
                        dialogGeneral = popupDialogs.createAlertDialog(AppMessages.SESSION_EXPIRED, AppMessages.ALERT_HEADING,
                                BaseActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!haveInternet()) {
                                            PopupDialogs.createAlertDialog(
                                                    AppMessages.INTERNET_CONNECTION_PROBLEM,
                                                    AppMessages.ALERT_HEADING, BaseActivity.this,
                                                    PopupDialogs.Status.ERROR);
                                        } else {
                                            dialog.cancel();
                                            Intent intent = new Intent(BaseActivity.this, SignOutActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                        dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


//                        dialog = PopupDialogs.createAlertDialog(
//                                AppMessages.SESSION_EXPIRED,
//                                getString(R.string.alertNotification),
//                                BaseActivity.this, new OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        if (!haveInternet()) {
//                                            PopupDialogs.createAlertDialog(
//                                                    AppMessages.INTERNET_CONNECTION_PROBLEM,
//                                                    AppMessages.ALERT_HEADING, BaseActivity.this,
//                                                    PopupDialogs.Status.ERROR);
//                                        } else {
//                                            dialog.cancel();
//                                            Intent intent = new Intent(BaseActivity.this, SignOutActivity.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    }
//                                },
//                                PopupDialogs.Status.ERROR);
                    } else {

                        dialogGeneral = popupDialogs.createAlertDialog(AppMessages.SESSION_EXPIRED, AppMessages.ALERT_HEADING,
                                BaseActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogGeneral.dismiss();
                                        Intent intent = new Intent(BaseActivity.this, SignOutActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


                        // create alert dialog so user can safely signout
//                        dialog = PopupDialogs.createAlertDialog(
//                                AppMessages.SESSION_EXPIRED,
//                                getString(R.string.alertNotification),
//                                BaseActivity.this, new OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog.cancel();
//                                        Intent intent = new Intent(BaseActivity.this, SignOutActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                },
//                                PopupDialogs.Status.ERROR);
                    }
                }
            }
        }
    }

    public void gotoAccountOpening() {
        Intent intent = new Intent(getApplicationContext(),
                MainMenuActivity.class);
        intent.putExtra(Constants.IntentKeys.FLAG_TRANSITION,
                Constants.IntentKeys.FLAG_OPEN_ACCOUNT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void addAutoKeyboardHideFunction() {
        View parentLayout = findViewById(R.id.parent_layout);
        parentLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });
    }

    public void addAutoKeyboardHideFunctionScrolling() {
        View parentLayout = findViewById(R.id.parent_layout);
        parentLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });

        View parentLayoutScroll = findViewById(R.id.scView);
        if (parentLayoutScroll != null) {
            parentLayoutScroll.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (Math.abs(mDeBounce - motionEvent.getEventTime()) < 250) {
                        // Ignore if it's been less then 250ms since
                        // the item was last clicked
                        return true;
                    }

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        isScrolling = true;

                        mActionDownX = motionEvent.getX();
                        mActionDownY = motionEvent.getY();
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE
                            && (Math.abs(mActionDownX - motionEvent.getX()) > 6 || Math
                            .abs(mActionDownY - motionEvent.getY()) > 6)) {

                        isScrolling = false;
                    }

                    int intCurrentY = Math.round(motionEvent.getY());
                    int intCurrentX = Math.round(motionEvent.getX());
                    int intStartY = motionEvent.getHistorySize() > 0 ? Math
                            .round(motionEvent.getHistoricalY(0)) : intCurrentY;
                    int intStartX = motionEvent.getHistorySize() > 0 ? Math
                            .round(motionEvent.getHistoricalX(0)) : intCurrentX;

                    if ((motionEvent.getAction() == MotionEvent.ACTION_UP)
                            && (Math.abs(intCurrentX - intStartX) < 3)
                            && (Math.abs(intCurrentY - intStartY) < 3)) {
                        if (mDeBounce > motionEvent.getDownTime()) {
                            // Still got occasional duplicates without this
                            return false;
                        }

                        // Handle the click
                        if (isScrolling) {
                            hideKeyboard(view);
                        }

                        mDeBounce = motionEvent.getEventTime();
                        return false;
                    }
                    return false;
                }
            });
        }
    }

    protected void signOut() {


        dialogGeneral = popupDialogs.createConfirmationDialog(getString(R.string.logout__text), AppMessages.ALERT_HEADING,
                BaseActivity.this, getString(R.string.yes), getString(R.string.no), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!haveInternet()) {
                            goToLogin();
                        } else {
                            Intent intent = new Intent(BaseActivity.this, SignOutActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        dialogGeneral.dismiss();
                    }
                }, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.dismiss();


                    }
                }, false, PopupDialogs.Status.ALERT, false);


//        dialogGeneral = popupDialogs.createAlertDialog(getString(R.string.logout__text), AppMessages.ALERT_HEADING,
//                BaseActivity.this, getString(R.string.yes), new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (!haveInternet()) {
//                            goToLogin();
//                        } else {
//                            Intent intent = new Intent(BaseActivity.this, SignOutActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
//                        }
//                        dialogGeneral.dismiss();
//                    }
//                }, false, PopupDialogs.Status.ERROR, true, null);

//        dialog = PopupDialogs.createConfirmationDialog(
//                getString(R.string.logout__text),
//                getString(R.string.alertNotification), BaseActivity.this,
//                new OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        if (!haveInternet()) {
////                            PopupDialogs.createAlertDialog(
////                                    AppMessages.INTERNET_CONNECTION_PROBLEM,
////                                    AppMessages.ALERT_HEADING, BaseActivity.this,
////                                    PopupDialogs.Status.ERROR);
//                            goToLogin();
////                            dialogGeneral = PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
////                                    AppMessages.ALERT_HEADING, BaseActivity.this, new View.OnClickListener() {
////                                        @Override
////                                        public void onClick(View v) {
////                                            dialogGeneral.cancel();
////                                            loginPage();
////                                        }
////                                    }, PopupDialogs.Status.ERROR);
//
//
//                        } else {
//                            dialog.cancel();
//                            Intent intent = new Intent(BaseActivity.this, SignOutActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                }, "Yes",
//                new OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        dialog.cancel();
//                    }
//                }, "NO", PopupDialogs.Status.INFO);
    }

    protected void deleteAgentmatePics() {
        new DeleteAgentmatePictures().execute();
    }

    @Override
    public void onClick(View view) {


        Intent intent;
        switch (view.getId()) {

            case R.id.layoutHome:

                if (context instanceof MainMenuActivity) {
                } else {
                    goToMainMenu();
                }
                break;

            case R.id.layoutNearByAgent:
                intent = new Intent(BaseActivity.this, LocatorActivity.class);
                startActivity(intent);
                if (ApplicationData.isWebViewOpen) {
                    ApplicationData.isWebViewOpen = false;
                    ApplicationData.webUrl = null;
                    finish();
                }

                break;
            case R.id.btnQRCode:

                if (context instanceof RetailPaymentComfirmationMPassActivity || context instanceof RetailPaymentReceiptActivity || context instanceof RetailPaymentActivity) {
//                    ivQRCode

                } else {
                    if(ApplicationData.isLogin== true && !ApplicationData.isMpinSet)
                    {
                        setMpinDialog(true);
                        return;
                    }
                    intent = new Intent(BaseActivity.this, RetailPaymentActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.layoutMore:

                if (context instanceof BottomBarActivity) {

                } else {
                    intent = new Intent(BaseActivity.this, BottomBarActivity.class);
                    startActivity(intent);
                }

//                dialogGeneral = popupDialogs.createDialogMore(BaseActivity.this);
//
//
//                dialogGeneral.findViewById(R.id.llContactUs).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(BaseActivity.this,
//                                ContactUsActivity.class);
//
//                        intent.putExtra(Constants.IntentKeys.HEADING, getResources().getString(R.string.contact_us));
//                        intent.putExtra(Constants.IntentKeys.ICON, R.drawable.heading_icon_contact);
//
//                        startActivity(intent);
//                        if (ApplicationData.isWebViewOpen) {
//                            ApplicationData.isWebViewOpen = false;
//                            ApplicationData.webUrl = null;
//                            finish();
//                        }
//
//                        dialogGeneral.dismiss();
//
//
//                    }
//                });
//                dialogGeneral.findViewById(R.id.llHelp).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(BaseActivity.this,
//                                FaqsActivity.class);
//                        startActivity(intent);
//                        if (ApplicationData.isWebViewOpen) {
//                            ApplicationData.isWebViewOpen = false;
//                            ApplicationData.webUrl = null;
//                            finish();
//                        }
//                        dialogGeneral.dismiss();
//
//
//                    }
//                });

                break;
        }
    }

    protected class DeleteAgentmatePictures extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showLoading("Please Wait", "Processing...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "JS_Consumer_Pictures");
                if (directory.isDirectory()) {
                    String[] children = directory.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(directory, children[i]).delete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            hideLoading();
        }
    }

    public final void setKeyboardListener(final LoginActivity.OnKeyboardVisibilityListener listener) {
        final View activityRootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private final int DefaultKeyboardDP = 100;
            // From @nathanielwolf answer...  Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
            private final int EstimatedKeyboardDP = DefaultKeyboardDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect r = new Rect();
            private boolean wasOpened;

            @Override
            public void onGlobalLayout() {
                // Convert the dp to pixels
                int estimatedKeyboardHeight = (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, activityRootView.getResources().getDisplayMetrics());

                // Conclude whether the keyboard is shown or not.
                activityRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == wasOpened) {
                    AppLogger.i("Keyboard state Ignoring global layout change...");
                    return;
                }

                wasOpened = isShown;
                listener.onVisibilityChanged(isShown);
            }
        });
    }

    public interface OnKeyboardVisibilityListener {
        void onVisibilityChanged(boolean visible);
    }

    protected void checkSoftKeyboard(final View layoutBottom) {

        setKeyboardListener(new OnKeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                AppLogger.i("##Visible: " + visible);
                if (visible) {
//                    layoutBottom.setVisibility(View.GONE);
                } else {
//                    layoutBottom.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    protected void checkSoftKeyboardD() {

        setKeyboardListener(new OnKeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                AppLogger.i("##Visible: " + visible);
                if (visible) {

                    findViewById(R.id.bottomBar).setVisibility(View.GONE);
                    findViewById(R.id.bottomBarQR).setVisibility(View.GONE);

//                    layoutBottom.setVisibility(View.GONE);
//                    layoutQr.setVisibility(View.GONE);

                } else {
                    findViewById(R.id.bottomBar).setVisibility(View.VISIBLE);
                    findViewById(R.id.bottomBarQR).setVisibility(View.VISIBLE);
                }

            }
        });
    }


    public void populateData(ListViewExpanded listView, String[] labels,
                             String[] data) {
        ArrayList<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < data.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("label", labels[i]);
            hm.put("data", data[i]);
            aList.add(hm);
        }

        String[] from = {"label", "data"};
        int[] to = {R.id.txtLabel, R.id.txtData};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
                R.layout.list_item_with_data, from, to);

        listView.setAdapter(adapter);
    }

    protected void focusAndShowKeyboard(Context context, EditText editText) {

        editText.requestFocus();
        editText.setSelection(editText.getText().length());
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


//        tf.getText().insert(tf.getSelectionStart(), "whatever");
//        editText.setSelection(pos);
//        editText.getText().insert(editText.getSelectionStart(), "whatever");
    }


    public void disableCopyPaste(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                focusAndShowKeyboard(view.getContext(), editText);
                // Consume the event.
                return true;
            }
        });
    }
    public void setMpinDialog(boolean isFromQR){
        dialogGeneral = popupDialogs.createConfirmationDialog(AppMessages.SET_MPINS, AppMessages.ALERT_HEADING,
                BaseActivity.this, "SET MPIN", "CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();
                        Intent intent = new Intent(BaseActivity.this, MyAccountChangePinActivity.class);
                        intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, new ProductModel("10028", Constants.FLOW_ID_GENERATE_MPIN + "", "SET MPIN"));
                        intent.putExtra(Constants.IntentKeys.PIN_CHANGE_TYPE, Constants.SET_MPIN);
                        intent.putExtra(Constants.IntentKeys.NEXTPIN_CHANGE_TYPE, Constants.IGNORE_AND_GOTO_MENU);
                        intent.putExtra(Constants.IntentKeys.BOTH_PIN_CHANGE, false);
                        startActivity(intent);
                        if(!isFromQR)
                        finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();
                        ApplicationData.isMpinSet = false;
                        // login flow will work on this one
                        if(!isFromQR)
                        finish();

                    }
                }, false, PopupDialogs.Status.ALERT, false);
    }
    public void removeSetMpin(){
        for (int i = 0; i < ApplicationData.listCategoriesMyAccount.get(0).getCategoryList().size(); i++) {
            if(ApplicationData.listCategoriesMyAccount.get(0).getCategoryList().get(i).getId() == "21"){
                for (int j = 0; j < ApplicationData.listCategoriesMyAccount.get(0).getCategoryList().get(i).getCategoryList().size(); j++) {
                    if(ApplicationData.listCategoriesMyAccount.get(0).getCategoryList().get(i).getCategoryList().get(j).getId() == "18") {
                        ApplicationData.listCategoriesMyAccount.get(0).getCategoryList().get(i).getCategoryList().remove(j);
                    }
                }
            }
        }
    }
}


