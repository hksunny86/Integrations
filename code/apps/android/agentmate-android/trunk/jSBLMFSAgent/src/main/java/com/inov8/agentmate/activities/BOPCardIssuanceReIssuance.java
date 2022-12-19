package com.inov8.agentmate.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.SegmentModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsblmfs.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Utility.testValidity;

public class BOPCardIssuanceReIssuance extends BaseCommunicationActivity implements LocationListener {

    private ProductModel product;
    private Handler handler;
    private LocationManager locationManager;
    private EditText mobileNumebr, cnic, debitCardNumber;
    private TextView lblHeading;
    private LocationListener locationListener;
    private boolean stop = true;
    private Runnable run;
    private Button btnNext;
    private String cnicStg, cmob, dcno, segmentCode;
    private int currCommand;
    private ArrayAdapter<String> segmentsAdapter;
    private Spinner segmentsSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bop_card_issuance_re_issuance);
        try {
            fetchIntents();
            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("BOP Card Issuance / Re Issuance");

            segmentsSpinner = findViewById(R.id.input_spinner_segment_number);
            mobileNumebr = findViewById(R.id.input_mobile_number);
            cnic = findViewById(R.id.input_cnic);
            debitCardNumber = findViewById(R.id.input_debit_card);

            mobileNumebr.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_MOBILE)});
            cnic.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CNIC)});
            debitCardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CARD)});

            btnNext = findViewById(R.id.btnNext);
            btnNext.setOnClickListener(v -> {
                if (!testValidity(mobileNumebr))
                    return;

                if (mobileNumebr.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
                    mobileNumebr.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                    return;
                }

                if (mobileNumebr.getText().toString().charAt(0) != '0' || mobileNumebr.getText().toString().charAt(1) != '3') {
                    mobileNumebr.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                    return;
                }

                if (!testValidity(cnic))
                    return;

                if (cnic.getText().toString().length() < Constants.MAX_LENGTH_CNIC) {
                    cnic.setError(Constants.Messages.INVALID_CNIC);
                    return;
                }

                if (!testValidity(debitCardNumber))
                    return;

                if (debitCardNumber.getText().toString().length() < Constants.MAX_LENGTH_CARD) {
                    debitCardNumber.setError(Constants.Messages.INVALID_CARD);
                    return;
                }

                currCommand = Constants.CMD_BOP_ISSUANCE_REISSUANCE_INFO;
                processRequest();
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }

        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
 //       statusCheck();
        //    setupLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        statusCheck();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            setupLocationUpdates();
        }
    }

    private void buildAlertMessageNoGps() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
//                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
//        final AlertDialog alert = builder.create();
//        alert.show();
        createAlertDialog("Your GPS seems to be disabled, Enable it to continue.",
                Constants.KEY_LIST_ALERT, (dialogInterface, i) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)));
    }

    @Override
    public void processRequest() {
        if (!haveInternet()) {
            Toast.makeText(getApplication(),
                    Constants.Messages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading("Please Wait", "Processing...");
        if (currCommand == Constants.CMD_FETCH_SEGMENTS) {
            new HttpAsyncTask(BOPCardIssuanceReIssuance.this).execute(
                    Constants.CMD_FETCH_SEGMENTS + "");
        } else if (currCommand == Constants.CMD_BOP_ISSUANCE_REISSUANCE_INFO) {
            new HttpAsyncTask(BOPCardIssuanceReIssuance.this).execute(
                    Constants.CMD_BOP_ISSUANCE_REISSUANCE_INFO + "", product.getId(), mobileNumebr.getText().toString(), cnic.getText().toString(),
                    debitCardNumber.getText().toString(), "03", ApplicationData.listSegments.get(segmentsSpinner.getSelectedItemPosition()).getCode(),
                    ApplicationData.latitude, ApplicationData.longitude, ApplicationData.userId);
        }
    }

    @Override
    public void processResponse(HttpResponseModel response) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
            if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                MessageModel message = (MessageModel) list.get(0);
                createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {
                    goToMainMenu();
                });
            } else if (table != null && table.containsKey(Constants.KEY_LIST_MSGS)) {
                hideLoading();
                List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                MessageModel message = (MessageModel) list.get(0);

                createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT, (dialogInterface, i) -> {

                });
            } else {

                if (currCommand == Constants.CMD_FETCH_SEGMENTS) {
                    ApplicationData.listSegments = (ArrayList<SegmentModel>) table.get(Constants.TAG_SEGMENTS);
                    ArrayList<String> segmentsList = new ArrayList<>();
                    for (int i = 0; i < ApplicationData.listSegments.size(); i++) {
                        segmentsList.add(ApplicationData.listSegments.get(i).getName());
                    }
                    segmentsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, segmentsList);
                    segmentsSpinner.setAdapter(segmentsAdapter);
                } else if (currCommand == Constants.CMD_BOP_ISSUANCE_REISSUANCE_INFO) {
                    cnicStg = table.get(Constants.ATTR_CNIC).toString();
                    cmob = table.get(Constants.ATTR_CMOB).toString();
                    dcno = table.get(Constants.ATTR_DCNO).toString();
                    segmentCode = table.get(Constants.ATTR_THIRD_PARTY_CUST_SEGMENT_CODE).toString();

                    Intent intent = new Intent(getApplicationContext(), BOPCardIssuanceReIssuanceConfirmationActivity.class);
                    intent.putExtra(Constants.IntentKeys.CNIC, cnicStg);
                    intent.putExtra(Constants.IntentKeys.DCNO, dcno);
                    intent.putExtra(Constants.IntentKeys.CMOB, cmob);
                    intent.putExtra(Constants.IntentKeys.SEGMENT_CODE, segmentCode);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
        }
        hideLoading();
    }

    @Override
    public void processNext() {

    }

    private void setupLocationUpdates() {

        showLoading("Please Wait", "fetching your location");

        handler = new Handler();
        handler.postDelayed(run = () -> {

            if (stop) {
                locationManager.removeUpdates(locationListener);
                hideLoading();
                loadingDialog = null;
                createAlertDialog("Unable to fetch your location, please change your location and try again.",
                        Constants.KEY_LIST_ALERT, (dialogInterface, i) -> goToMainMenu());
            }
        }, 60000);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this);
        locationListener = this;
    }

    @Override
    public void onLocationChanged(Location location) {
        hideLoading();
        loadingDialog = null;
        if (stop) {
            if (location != null) {
                ApplicationData.latitude = String.valueOf(location.getLatitude());
                ApplicationData.longitude = String.valueOf(location.getLongitude());
                locationManager.removeUpdates(this);
                stop = false;
                if (ApplicationData.listSegments == null) {
                    currCommand = Constants.CMD_FETCH_SEGMENTS;
                    processRequest();
                } else {
                    ArrayList<String> segmentsList = new ArrayList<>();
                    for (int i = 0; i < ApplicationData.listSegments.size(); i++) {
                        segmentsList.add(ApplicationData.listSegments.get(i).getName());
                    }
                    segmentsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, segmentsList);
                    segmentsSpinner.setAdapter(segmentsAdapter);
                }
            }
        }
    }

    private void fetchIntents() {
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}