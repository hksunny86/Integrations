package com.inov8.agentmate.activities.proofOfLife;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.SegmentModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PreferenceConnector;
import com.inov8.jsblmfs.R;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Utility.testValidity;

public class ProofOfLifeInputActivity extends BaseCommunicationActivity implements LocationListener {

    private ProductModel product;
    private TextView lblField1, lblField2, lblSpinner1, lblHeading;
    private EditText input1, input2;
    private Spinner inputSpinner1;
    private Button btnNext;
    private Handler handler;
    private String macAddress, message;
    ArrayAdapter<String> segmentsAdapter;
    private int currCommand;
    private Runnable run;
    private String cmob, cnic, segmentId;
    private boolean stop = true;
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_of_life_input);
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);

            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText(product.getName());

            lblField1 = (TextView) findViewById(R.id.lblField1);
            lblField1.setText("Mobile Number");
            lblField1.setVisibility(View.VISIBLE);

            input1 = (EditText) findViewById(R.id.input1);
            input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_MOBILE)});
            input1.setVisibility(View.VISIBLE);
            disableCopyPaste(input1);

            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField2.setText("CNIC");
            lblField2.setVisibility(View.VISIBLE);

            input2 = (EditText) findViewById(R.id.input2);
            input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_CNIC)});
            input2.setVisibility(View.VISIBLE);
            disableCopyPaste(input2);

            lblSpinner1 = (TextView) findViewById(R.id.lblSpinner1);
            lblSpinner1.setText("Segment");
            lblSpinner1.setVisibility(View.VISIBLE);

            inputSpinner1 = findViewById(R.id.inputSpinner1);
            inputSpinner1.setVisibility(View.VISIBLE);

            if (ApplicationData.listSegments == null) {
                currCommand = Constants.CMD_FETCH_SEGMENTS;
                processRequest();
            } else {
                ArrayList<String> segmentsList = new ArrayList<>();
                for (int i = 0; i < ApplicationData.listSegments.size(); i++) {
                    segmentsList.add(ApplicationData.listSegments.get(i).getName());
                }
                segmentsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, segmentsList);
                inputSpinner1.setAdapter(segmentsAdapter);
            }

            btnNext = (Button) findViewById(R.id.btnNext);

            btnNext.setOnClickListener(v -> {
                if (!testValidity(input1))
                    return;

                    if (input1.getText().toString().length() < Constants.MAX_LENGTH_MOBILE) {
                        input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                        return;
                    }

                    if (input1.getText().toString().charAt(0) != '0' || input1.getText().toString().charAt(1) != '3') {
                        input1.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                        return;
                    }

                if (!testValidity(input2))
                    return;

                if (input2.getText().toString().length() < Constants.MAX_LENGTH_CNIC) {
                    input2.setError(Constants.Messages.INVALID_CNIC);
                    return;
                }

                macAddress = PreferenceConnector.readString(ProofOfLifeInputActivity.this, PreferenceConnector.MAC_ADDRESS, null);
                if (macAddress == null) {
                    if (getMacAddress() == null)
                        macAddress = "";
                }

                currCommand = Constants.CMD_PROOF_OF_LIFE_INFO;
                processRequest();
            });

            addAutoKeyboardHideFunction();

            fetchLocation();

        } catch (Exception ex) {
            ex.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
        headerImplementation();
        addAutoKeyboardHideFunctionScrolling();
    }

    private void fetchLocation() {

        showLoading("Please Wait", "fetching your location");

        handler = new Handler();
        handler.postDelayed(run = () -> {

            if (stop) {
                locationManager.removeUpdates(locationListener);
                hideLoading();
                loadingDialog = null;
                createAlertDialog("Unable to fetch your location, please change your location and try again.", Constants.KEY_LIST_ALERT, (dialogInterface, i) -> goToMainMenu());
            }
        }, 60000);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0f,
                this
        );

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0f,
                this
        );
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
                handler.removeCallbacks(run);
                stop = false;
            }
        }
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
            new HttpAsyncTask(ProofOfLifeInputActivity.this).execute(Constants.CMD_FETCH_SEGMENTS + "");
        } else if (currCommand == Constants.CMD_PROOF_OF_LIFE_INFO) {
            cmob = input1.getText().toString();
            cnic = input2.getText().toString();
            segmentId = ApplicationData.listSegments.get(inputSpinner1.getSelectedItemPosition()).getCode();
            new HttpAsyncTask(ProofOfLifeInputActivity.this).execute(currCommand + "", product.getId(), cmob,
                    cnic, segmentId,
                    ApplicationData.latitude, ApplicationData.longitude, android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL, macAddress);
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
                    inputSpinner1.setAdapter(segmentsAdapter);

                } else if (currCommand == Constants.CMD_PROOF_OF_LIFE_INFO) {
               message  = table.get(Constants.ATTR_SMSMSG).toString();

                    Intent intent = new Intent(getApplicationContext(), ProofOfLifeActivity.class);
                    intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, product);
                    intent.putExtra(Constants.IntentKeys.CMOB, cmob);
                    intent.putExtra(Constants.IntentKeys.CNIC, cnic);
                    intent.putExtra(Constants.IntentKeys.SEGMENT_CODE,segmentId );
                    intent.putExtra(Constants.IntentKeys.MSG, message);
                    intent.putExtra(Constants.IntentKeys.MAC_ADDRESS, macAddress);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }
            hideLoading();
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


    public String getMacAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    macAddress = null;
                    return macAddress;
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02x:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                macAddress = buf.toString();
                PreferenceConnector.writeString(ProofOfLifeInputActivity.this, PreferenceConnector.MAC_ADDRESS, macAddress);
                return macAddress;
            }
        } catch (Exception ex) {

        }
        macAddress = null;
        return macAddress;
    }
}