package com.inov8.jsblconsumer.activities.schedule;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.TransactionReceiptActivity;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.model.ScheduleModel;
import com.inov8.jsblconsumer.model.TransactionModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.jsblconsumer.util.Constants.FLOW_ID_SI_SCHEDULE;

public class ScheduleActivity extends BaseCommunicationActivity {

    private ScheduleModel scheduleModel;
    private Spinner spinnerRecur;
    private TextView tvStartDate, tvEndDate;
    private AppCompatImageView ivDateStart, ivDateEnd;
    private RadioButton rbDateEnd, rbNoOfOccurrences;
    private AppCompatEditText etNoOfOccurrences;
    private RelativeLayout rlStartDate;
    private ConstraintLayout clEndDate;
    private Button btnNext;
    private ArrayList<String> spinnerItems;
    private String strRecur, strStartDate, strEndDate, strNoOfOccurrences;
    private DatePickerDialog startDatePicker, endDatePicker;
    private int year;
    private int month;
    private int day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_schedule);

        fetchIntents();
        checkSoftKeyboardD();
        spinnerItems = new ArrayList<String>();
        spinnerItems.add("Weekly");
        spinnerItems.add("Monthly");

        spinnerRecur = (Spinner) findViewById(R.id.spinner_recur);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecur.setAdapter(adapter);
        spinnerRecur.setVisibility(View.VISIBLE);
        spinnerRecur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                strRecur = spinnerItems.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rlStartDate = (RelativeLayout) findViewById(R.id.rl_startDate);
        rlStartDate.setOnClickListener(view -> {

        });

        clEndDate = (ConstraintLayout) findViewById(R.id.cl_endDate);
        clEndDate.setOnClickListener(view -> {

        });

        rbDateEnd = (RadioButton) findViewById(R.id.rbEndDate);
        rbDateEnd.setOnCheckedChangeListener((compoundButton, b) -> {

        });

        rbNoOfOccurrences = (RadioButton) findViewById(R.id.rbNoOfOccurrences);
        rbNoOfOccurrences.setOnCheckedChangeListener((compoundButton, b) -> {

        });

        etNoOfOccurrences = (AppCompatEditText) findViewById(R.id.etNoOfOccurrences);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(view -> {
            long dateDifference = Utility.dateDifference(tvStartDate
                    .getText().toString(), tvEndDate.getText().toString());

            if (rbDateEnd.isChecked()) {

                if (dateDifference < 0) {

                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.ERROR_GREATER_START_DATE, AppMessages.ALERT_HEADING,
                            ScheduleActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                    return;
                }
            }

            if (rbNoOfOccurrences.isChecked()) {
                if (TextUtils.isEmpty(etNoOfOccurrences.getText().toString())) {
                    etNoOfOccurrences.setError(AppMessages.ERROR_EMPTY_FIELD);
                    etNoOfOccurrences.requestFocus();
                    return;
                }

                if (etNoOfOccurrences.getText().toString().equals("0")) {
                    etNoOfOccurrences.setError("Enter Valid number of Occurrence.");
                    etNoOfOccurrences.requestFocus();
                    return;
                }
                if (strRecur.equals("Weekly")) {
                    if (Integer.parseInt(etNoOfOccurrences.getText().toString()) > 52) {
                        etNoOfOccurrences.setError("Number of Occurrence Should be from 1 to 52.");
                        etNoOfOccurrences.requestFocus();
                        return;
                    }

                } else if (strRecur.equals("Monthly")) {
                    if (Integer.parseInt(etNoOfOccurrences.getText().toString()) > 12) {
                        etNoOfOccurrences.setError("Number of Occurrence Should be from 1 to 12.");
                        etNoOfOccurrences.requestFocus();
                        return;
                    }

                }

            }
            askMpin(mBundle, TransactionReceiptActivity.class, false);
        });

        addAutoKeyboardHideFunction();
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();

        setCnicExpiry();
        setRadioEndDate();
        setRadioNoOfOccurance();
    }

    private void fetchIntents() {
        scheduleModel = (ScheduleModel) mBundle.get(Constants.IntentKeys.SCHEDULE_MODEL);
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    ScheduleActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        }

        showLoading("Please Wait", "Processing...");

        new HttpAsyncTask(ScheduleActivity.this).execute(
                Constants.CMD_SCHEDULE_PAYMENT + "",
                getEncryptedMpin(), scheduleModel.strProductId, scheduleModel.rcmob,
                scheduleModel.strAccountNumber, scheduleModel.strAccountTitle, scheduleModel.bankImd,
                scheduleModel.strCommissionAmount, scheduleModel.strCommissionAmountF,
                scheduleModel.strCharges, scheduleModel.strChargesF, scheduleModel.strTotalAmount,
                scheduleModel.strTotalAmountF, scheduleModel.strAmount, scheduleModel.strAmountF,
                scheduleModel.bankName, scheduleModel.branchName, scheduleModel.iban, scheduleModel.crDr,
                scheduleModel.coreactl, scheduleModel.purposeCode != null ? scheduleModel.purposeCode : "");
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

                dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                        ScheduleActivity.this, getString(R.string.ok), new View.OnClickListener() {
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


            }
            else if (table.containsKey(Constants.KEY_LIST_MSGS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);
                dialogGeneral = PopupDialogs.createSuccessDialog("Successful!", message.getDescr(), this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGeneral.cancel();
                        goToMainMenu();
                    }
                }, null);

            }

            else {
                if (table.containsKey(Constants.KEY_LIST_TRANS)) {
                    @SuppressWarnings("unchecked")
                    List<TransactionModel> list = (List<TransactionModel>) table.get(Constants.KEY_LIST_TRANS);
                    TransactionModel transaction = list.get(0);

                    Intent intent = new Intent(getApplicationContext(), TransactionReceiptActivity.class);
                    intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL, transaction);
                    mBundle.putByte(Constants.IntentKeys.FLOW_ID, FLOW_ID_SI_SCHEDULE);
                    intent.putExtras(mBundle);
                    startActivity(intent);
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

    private void setCnicExpiry() {

        setStartDatePicker();
        setEndDatePicker();

        rlStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (startDatePicker != null && !startDatePicker.isShowing()) {
                    startDatePicker.show();
                }
            }
        });

        clEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (endDatePicker != null && !endDatePicker.isShowing()) {
                    endDatePicker.show();
                }
            }
        });

    }

    private void setStartDatePicker() {

        final Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_MONTH,1);

        final long minDate, maxdate;
        minDate = c1.getTimeInMillis();


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        maxdate = c.getTimeInMillis();


        startDatePicker = new DatePickerDialog(
                ScheduleActivity.this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1)
                                + "-" + year);
                        String monthst = (monthOfYear + 1) + "";
                        String day = dayOfMonth + "";

                        if ((dayOfMonth + "").length() == 1) {
                            day = "0" + dayOfMonth;
                        }
                        if (((monthOfYear + 1) + "").length() == 1) {
                            monthst = "0" + (monthOfYear + 1);
                        }

//                        cardExpriryFormatted = year + "-" + monthst + "-" + day;
                    }
                }, year, month, day);

        startDatePicker.getDatePicker().setMinDate(minDate);
        startDatePicker.getDatePicker().setMaxDate(maxdate);

//        String monthst = (month + 1) + "";
//        String daylocal = day + "";
//
//        if ((day + "").length() == 1) {
//            daylocal = "0" + day;
//        }
//        if (((month + 1) + "").length() == 1) {
//            monthst = "0" + (month + 1);
//        }
//        cardExpriryFormatted = year + "-" + monthst + "-" + daylocal;

//        }
    }

    private void setEndDatePicker() {

        final Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_MONTH,1);

        final long minDate, maxdate;
        minDate = c1.getTimeInMillis();


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        maxdate = c.getTimeInMillis();


        endDatePicker = new DatePickerDialog(
                ScheduleActivity.this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1)
                                + "-" + year);
                        String monthst = (monthOfYear + 1) + "";
                        String day = dayOfMonth + "";

                        if ((dayOfMonth + "").length() == 1) {
                            day = "0" + dayOfMonth;
                        }
                        if (((monthOfYear + 1) + "").length() == 1) {
                            monthst = "0" + (monthOfYear + 1);
                        }

//                        cardExpriryFormatted = year + "-" + monthst + "-" + day;
                    }
                }, year, month, day);

        endDatePicker.getDatePicker().setMinDate(minDate);
        endDatePicker.getDatePicker().setMaxDate(maxdate);

//        String monthst = (month + 1) + "";
//        String daylocal = day + "";
//
//        if ((day + "").length() == 1) {
//            daylocal = "0" + day;
//        }
//        if (((month + 1) + "").length() == 1) {
//            monthst = "0" + (month + 1);
//        }
//        cardExpriryFormatted = year + "-" + monthst + "-" + daylocal;

//        }
    }

    protected void setRadioEndDate() {
        rbDateEnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbNoOfOccurrences.setChecked(false);
                    etNoOfOccurrences.setEnabled(false);
                } else {
                    rbNoOfOccurrences.setChecked(true);
                    etNoOfOccurrences.setEnabled(true);
                }
            }
        });
    }

    protected void setRadioNoOfOccurance() {
        rbNoOfOccurrences.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbDateEnd.setChecked(false);
                    clEndDate.setEnabled(false);
                } else {
                    rbDateEnd.setChecked(true);
                    clEndDate.setEnabled(true);
                }
            }
        });
    }
    public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
        processRequest();
    }

}
