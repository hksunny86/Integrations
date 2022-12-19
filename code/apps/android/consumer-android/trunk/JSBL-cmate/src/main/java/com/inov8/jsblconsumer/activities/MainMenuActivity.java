package com.inov8.jsblconsumer.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.inov8.jsblconsumer.activities.debitCard.DebitCardIssuance;
import com.inov8.jsblconsumer.activities.debitCardActivation.TermsConditionsDebitCardActivity;
import com.inov8.jsblconsumer.adapters.MainMenuImageAdapter;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.adapters.SubMenuRecyclerViewAdapter;
import com.inov8.jsblconsumer.components.imageslider.CirclePageIndicator;
import com.inov8.jsblconsumer.components.imageslider.ImageSliderManager;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.Hashtable;
import java.util.List;

public class MainMenuActivity extends BaseCommunicationActivity {
    private MainMenuImageAdapter imageAdapter;
    public GridView gridview;
    private String tillBalance;
    private Dialog dialog;
    private int curCmd;
    private View layoutBottom;
    private ViewPager viewPager;
    Bundle bundle;
    private RecyclerView rvMainMenu;

    private VideoView videoView;
    private View view;
    private RelativeLayout sliderView, layoutAd;
    private CirclePageIndicator pageIndicator;
    private ImageSliderManager imageSliderManager;
    private int catPostion;

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //   add();


        if (ApplicationData.isMyAccountOpen) {
            ApplicationData.isMyAccountOpen = false;
            Intent intent = new Intent(MainMenuActivity.this, CategoryMenuActivity.class);
            intent.putExtra(Constants.IntentKeys.LIST_CATEGORIES, ApplicationData.listCategoriesMyAccount);
            intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS, 0);
            startActivity(intent);
        }


        bundle = new Bundle();
        if (ApplicationData.isDestroyed) {
            this.finish();
        } else {
            setContentView(R.layout.activity_main_menu);

            view = findViewById(R.id.layoutParent);
            bottomBar = (View) findViewById(R.id.bottomBar);
            layoutBottom = (View) findViewById(R.id.bottomBarQR);
            headerImplementation();
            bottomBarImplementation(MainMenuActivity.this, "");
            checkSoftKeyboardD();


            try {

                showAdd();

//                MainMenuViewPagerAdapter mainMenuViewPagerAdapter = new MainMenuViewPagerAdapter(getSupportFragmentManager(), ApplicationData.listCategories, bundle);
//                viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//
//                TabLayout tabLayout = (TabLayout) findViewById(R.id.layoutTab);
//                tabLayout.setupWithViewPager(viewPager, true);
//                viewPager.setAdapter(mainMenuViewPagerAdapter);
//                tabLayout.setupWithViewPager(viewPager);


                // refresh balance
                AppCompatImageView btnRefreshBalance = (AppCompatImageView) findViewById(R.id.btnBalanceRefresh);
                btnRefreshBalance.setVisibility(View.VISIBLE);
                btnRefreshBalance.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        curCmd = Constants.CMD_CHECK_BALANCE;
                        processRequest();
//                        askMpin(null, null, false);
                    }
                });

                btnBack = (AppCompatImageView) findViewById(R.id.btnBack);
                btnBack.setVisibility(View.GONE);
                btnHome = (AppCompatImageView) findViewById(R.id.btnHome);
                btnHome.setVisibility(View.GONE);

                viewLeft.setVisibility(View.VISIBLE);
                loadBalance();


            } catch (Exception ex) {
                ex.getMessage();
            }
        }

        if (getIntent().getExtras() != null
                && getIntent().getExtras().getString(
                Constants.IntentKeys.FLAG_TRANSITION) != null) {

            String intentKey = getIntent().getExtras().getString(
                    Constants.IntentKeys.FLAG_TRANSITION);

            if (intentKey.equals(Constants.IntentKeys.FLAG_LOGIN)) {
                Intent intent = new Intent(MainMenuActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        exitFromMainMenu();

//        if (ApplicationData.isMyAccountOpen) {
//            ApplicationData.isMyAccountOpen=false;
//            Intent intent = new Intent(MainMenuActivity.this, CategoryMenuActivity.class);
//            intent.putExtra(Constants.IntentKeys.LIST_CATEGORIES, ApplicationData.listCategoriesMyAccount);
//            intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS, 0);
//            startActivity(intent);
//        }
    }

    private void exitFromMainMenu() {
        boolean sessionOut = mBundle.getBoolean(Constants.IntentKeys.SESSION_OUT);
        if (sessionOut) {
            startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
            finish();
        }
        mBundle.putBoolean(Constants.IntentKeys.SESSION_OUT, false);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {

        ApplicationData.resetPinRetryCount();
        ApplicationData.webUrl = null;
        if (imageSliderManager != null) {
            imageSliderManager.onResume();
        }

        if (videoView != null) {
            videoView.start();
        }

        super.onResume();


    }

    @Override
    public void onPause() {
        if (imageSliderManager != null) {
            imageSliderManager.onPause();
        }

        if (videoView != null) {
            videoView.pause();
        }
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        signOut();
//        super.onBackPressed();
//
    }

    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        curCmd = Constants.CMD_CHECK_BALANCE;
        processRequest();
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            PopupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM,
                    AppMessages.ALERT_HEADING, MainMenuActivity.this, PopupDialogs.Status.ERROR);
            return;
        }

        showLoading("Please Wait", "Processing...");

        switch (curCmd) {
            case Constants.CMD_CHECK_BALANCE:
                new HttpAsyncTask(MainMenuActivity.this).execute(
                        curCmd + "", getEncryptedMpin(), "1",
                        ApplicationData.userId, ApplicationData.accountId, "");
                break;
            case Constants.CMD_DEBIT_CARD:

                showLoading("Please Wait", "Processing...");
                new HttpAsyncTask(MainMenuActivity.this).execute(Constants.CMD_DEBIT_CARD + "");

                break;
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
                    .getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                final MessageModel message = (MessageModel) list.get(0);

//                dialogGeneral = PopupDialogs.createAlertDialog(message.getDescr(), getString(R.string.alertNotification),
//                        MainMenuActivity.this, new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogGeneral.cancel();
//
//                                if (message != null
//                                        && message.getCode() != null
//                                        && message.getCode().equals(
//                                        Constants.ErrorCodes.INTERNAL)) {
//                                    goToMainMenu();
//                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
//                                    askMpin(mBundle, TransactionReceiptActivity.class, false);
//                                }
//
//                            }
//                        }, PopupDialogs.Status.ERROR);

                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        MainMenuActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGeneral.dismiss();
                                if (message != null
                                        && message.getCode() != null
                                        && message.getCode().equals(
                                        Constants.ErrorCodes.INTERNAL)) {
                                    goToMainMenu();
                                } else if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {
                                    askMpin(mBundle, TransactionReceiptActivity.class, false);
                                }
                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);


            } else {
                switch (curCmd) {
                    case Constants.CMD_CHECK_BALANCE:
                        // app balance
                        ApplicationData.formattedBalance = table.get(
                                XmlConstants.Attributes.BALF).toString();
                        loadBalance();
                        break;
                    case Constants.CMD_DEBIT_CARD: {
                        if (table.containsKey(Constants.ATTR_CNIC)) {

                            if (table.containsKey("FEE")) {
                                ApplicationData.fee = table.get("FEE").toString();
                            }

//                            askMpin(mBundle, null, false);
                            Intent intent = new Intent(MainMenuActivity.this, TermsConditionsDebitCardActivity.class);
                            startActivityForResult(intent, Constants.CMD_DEBIT_CARD);
                        }
                        break;
                    }

//                    case Constants.CMD_TILL_BALANCE:
//                        dialog.dismiss();
//                        loadingDialog.dismiss();
//                        ApplicationData.isTillBalanceRequired = 0;
//                        break;
                }
            }
            hideLoading();
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
        hideLoading();
    }

    @Override
    public void processNext() {
    }


    private void showAdd() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        double height = displayMetrics.heightPixels;
        int margin = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics));
        params.setMargins(margin, 0, margin, 0);
//        ivLogo.setLayoutParams(params);


        rvMainMenu = (RecyclerView) findViewById(R.id.recylerView);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainMenuActivity.this);
//                recylerView.setLayoutManager(linearLayoutManager);
//                MiniStatementRecyclerViewAdapter miniStatementRecyclerViewAdapter = new MiniStatementRecyclerViewAdapter(MainMenuActivity.this,stringArrayList);
//                recylerView.setAdapter(miniStatementRecyclerViewAdapter);


        SubMenuRecyclerViewAdapter adapter = new SubMenuRecyclerViewAdapter(this, ApplicationData.listCategories);
        rvMainMenu.setLayoutManager(new GridLayoutManager(this, 4));
        rvMainMenu.setAdapter(adapter);


        layoutAd = (RelativeLayout) findViewById(R.id.layoutAd);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rvMainMenu.setLayoutManager(linearLayoutManager);

        ViewGroup.LayoutParams headerParams, footerParams, adsParams, gridViewParams;
        adsParams = layoutAd.getLayoutParams();

// Changes the height and width to the specified *pixels*
        height = height - ((height / 13) + (height / 13)); // subtracting header and bottom heights
        adsParams.height = (int) ((height * 3) / 10);

        layoutAd.setLayoutParams(adsParams);


        initAdLayout();
    }


    private void initAdLayout() {
        sliderView = (RelativeLayout) findViewById(R.id.sliderView);
        videoView = (VideoView) findViewById(R.id.videoView);

//        ViewTreeObserver vto = gridView.getViewTreeObserver();
//        ViewTreeObserver vto = listViewMainMenu.getViewTreeObserver();
        ViewTreeObserver vto = rvMainMenu.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {

                sliderView = (RelativeLayout) findViewById(R.id.sliderView);
                videoView = (VideoView) findViewById(R.id.videoView);

                int height = sliderView.getHeight();

                final RelativeLayout.LayoutParams[] params = {new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT)};
                params[0] = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params[0].topMargin = height - 50;

                pageIndicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);
                pageIndicator.setLayoutParams(params[0]);
                return true;
            }
        });

        switch (ApplicationData.adType) {
            case Constants.AD_TYPE_SLIDER:
                videoView.setVisibility(View.GONE);
                loadSlider();
                break;
            case Constants.AD_TYPE_VIDEO:
                sliderView.setVisibility(View.GONE);
                loadVideo();
                break;
        }
    }

    private void loadSlider() {
        sliderView.setVisibility(View.VISIBLE);
        imageSliderManager = new ImageSliderManager(MainMenuActivity.this,
                view, ApplicationData.listAds);
    }

    private void loadVideo() {
        videoView.setVisibility(View.VISIBLE);

        /**
         * To Embed Video String frameVideo =
         * "<iframe src=\"https://player.vimeo.com/video/144590794\" width=\"500\" height=\"281\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>"
         * ;
         *DirectionsActivity
         * videoView.setWebViewClient(new WebViewClient() {
         *
         * @Override public boolean shouldOverrideUrlLoading(WebView view,
         *           String url) { return false; } }); WebSettings webSettings =
         *           videoView.getSettings();
         *           webSettings.setJavaScriptEnabled(true);
         *           videoView.loadData(frameVideo, "text/html; charset=utf-8",
         *           "utf-8");
         */

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        /**
         * Video Format should be mp4
         */
        Uri video = Uri.parse(ApplicationData.videoLink);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);

                videoView.setBackground(null);
            }
        });
    }

    public void debitCardIssuenceCheck(int postion) {
        curCmd = Constants.CMD_DEBIT_CARD;
        catPostion = postion;
        processRequest();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == Constants.CMD_DEBIT_CARD) {
            Intent i = new Intent(MainMenuActivity.this, DebitCardIssuance.class);
            i.putExtra(Constants.IntentKeys.LIST_CATEGORIES, ApplicationData.listCategories);
            i.putExtra(Constants.IntentKeys.MENU_ITEM_POS, catPostion);
            startActivity(i);
        }
    }
}