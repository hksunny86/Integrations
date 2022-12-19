package com.inov8.jsblconsumer.activities.faq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.LoginActivity;
import com.inov8.jsblconsumer.activities.myAccount.MyAccountBalanceInquiryActivity;
import com.inov8.jsblconsumer.adapters.FaqListAdapter;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.FaqsModel;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.FaqListViewExpanded;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.PreferenceConnector;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.inov8.jsblconsumer.util.ApplicationData.isLogin;

public class FaqsActivity extends BaseCommunicationActivity {
    private TextView tvHeading;
    private ImageView ivIcon;
    private List<String> listQuestions;
    private List<String> listAnswers;
    private Map<String, List<String>> faqsCollection;
    private FaqListViewExpanded listViewFaqs;
    private ArrayList<FaqsModel> listFaqs;
    private Button btnOk;
    private String strVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        addAutoKeyboardHideFunctionScrolling();
        headerImplementation();
        setUI();

        strVersion = PreferenceConnector.readString(FaqsActivity.this,
                PreferenceConnector.FAQ_VERSION, Constants.DEFAULT_FAQ_VERSION);

        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    FaqsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        } else {
            processRequest();
        }


    }


    @Override
    public void processRequest() {
        showLoading(getString(R.string.please_wait), getString(R.string.processing));
        new HttpAsyncTask(FaqsActivity.this).execute(Constants.CMD_FAQS + "", strVersion);
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            if (response != null) {
                XmlParser xmlParser = new XmlParser();
                String xmlResponse = response.getXmlResponse();
                Hashtable<?, ?> table = xmlParser
                        .convertXmlToTable(xmlResponse);
                if (table != null
                        && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<MessageModel> list = (List<MessageModel>) table
                            .get(Constants.KEY_LIST_ERRORS);
                    MessageModel messageModel = (MessageModel) list.get(0);

                    dialogGeneral = popupDialogs.createAlertDialog(messageModel.getDescr(), AppMessages.ALERT_HEADING,
                            FaqsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                    dialogGeneral.dismiss();

                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


                } else if (table != null
                        && table.containsKey(Constants.KEY_LIST_MSGS)) {
                    List<MessageModel> list = (List<MessageModel>) table
                            .get(Constants.KEY_LIST_MSGS);
                    MessageModel messageModel = (MessageModel) list.get(0);


                    dialogGeneral = popupDialogs.createAlertDialog(messageModel.getDescr(), AppMessages.ALERT_HEADING,
                            FaqsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                    dialogGeneral.dismiss();

                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

//                    View.OnClickListener clickListenerToMenu = new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            finish();
//                        }
//                    };
//
//                    PopupDialogs.createAlertDialog(messageModel.getDescr(), getString(R.string.alertNotification),
//                            FaqsActivity.this, clickListenerToMenu, PopupDialogs.Status.ERROR);
                } else {
                    PreferenceConnector.writeString(this,
                            PreferenceConnector.FAQ_VERSION,
                            ApplicationData.faqVersion);
                    listFaqs = (ArrayList<FaqsModel>) table
                            .get(Constants.KEY_LIST_FAQS);

                    if (listFaqs != null && listFaqs.size() > 0) {
                        ApplicationData.listFaqs = new ArrayList<FaqsModel>();
                        ApplicationData.listFaqs.addAll(listFaqs);

                        String result = response.getXmlResponse().substring(
                                response.getXmlResponse().indexOf(
                                        "<" + XmlConstants.Tags.FAQS),
                                response.getXmlResponse().indexOf(
                                        "</" + XmlConstants.Tags.FAQS + ">")
                                        + ("</" + XmlConstants.Tags.FAQS + ">")
                                        .length());
                        if (result != null) {

                            PreferenceConnector.writeString(this,
                                    PreferenceConnector.FAQS_DATA, result);
                        }
                    } else {// use saved catalog

                        String result = PreferenceConnector.readString(this,
                                PreferenceConnector.FAQS_DATA, "");
                        if (result != null) {
                            ApplicationData.listFaqs = new ArrayList<FaqsModel>();
                            ApplicationData.listFaqs.addAll(xmlParser
                                    .parseLocalFaqs(result));
                            listFaqs = ApplicationData.listFaqs;
                        }
                    }
                    processNext();
                }
            }
            hideLoading();
        } catch (Exception exp) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
//                    View.OnClickListener clickListenerToMenu = new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            finish();
//                        }
//                    };


                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING,
                            FaqsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


//                    PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, getString(R.string.alertNotification),
//                            FaqsActivity.this, clickListenerToMenu, PopupDialogs.Status.ERROR);

                }
            });
            AppLogger.e(exp);
        }
        hideLoading();
    }

    @Override
    public void processNext() {
        btnOk.setVisibility(View.INVISIBLE);
        createQuestionsList();
        createFaqsCollection();

        final FaqListAdapter faqsListAdapter = new FaqListAdapter(this,
                listQuestions, faqsCollection);
        listViewFaqs.setAdapter(faqsListAdapter);
        listViewFaqs.setGroupIndicator(null);
        btnOk.setVisibility(View.VISIBLE);
    }

    private void createQuestionsList() {
        listQuestions = new ArrayList<String>();

        for (int i = 0; i < listFaqs.size(); i++) {
            listQuestions.add(listFaqs.get(i).getQuestion());
        }
    }

    private void createFaqsCollection() {
        faqsCollection = new LinkedHashMap<String, List<String>>();

        for (int i = 0; i < listFaqs.size(); i++) {
            String answer = null;
            answer = listFaqs.get(i).getAnswer();
            loadChild(answer);
            faqsCollection.put(listQuestions.get(i), listAnswers);
        }
    }

    private void loadChild(String answer) {
        listAnswers = new ArrayList<String>();
        listAnswers.add(answer);
    }

    private void setUI() {
        tvHeading = (TextView) findViewById(R.id.lblHeading);
        ivIcon = (ImageView) findViewById(R.id.icon);
        listViewFaqs = (FaqListViewExpanded) findViewById(R.id.listFaqs);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnHome.setVisibility(View.GONE);

        tvHeading.setText(getString(R.string.faqs));
        ivIcon.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.heading_icon_faqs));
        btnOk.setVisibility(View.INVISIBLE);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    goToMainMenu();
                } else {
                    Intent intent = new Intent(FaqsActivity.this,
                            LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

    }
}