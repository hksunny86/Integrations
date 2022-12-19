package com.inov8.jsblconsumer.activities.locator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.adapters.LocationListAdapter;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.LocationModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.net.HttpAsyncTask;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.OnItemCustomClickListener;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.util.XmlConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class LocatorActivity extends BaseCommunicationActivity implements
        OnItemCustomClickListener, LocationCallback {


    private int totalCount = -1;
    LocationListAdapter adapter;
    private int pageNo = 1;


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static final int REQUEST_LOCATION = 2;

    public static final int LOCATOR_TYPE_ATM = 0;
    public static final int LOCATOR_TYPE_BRANCH = 1;
    public static final int LOCATOR_TYPE_AGENT = 2;
    public static final String KEY_RADIUS_5KM = "5";
    public static final String KEY_RADIUS_10KM = "10";
    public static final String KEY_RADIUS_15KM = "15";
    public static final String KEY_RADIUS_20KM = "20";
    public static final String KEY_LOCATOR_BRANCH = "Branch";
    public static final String KEY_LOCATOR_ATM = "ATM";
    public static final String KEY_LOCATOR_AGENT = "AGENT";
    private static final HashMap<String, Integer> hashMap = new HashMap<String, Integer>(
            6);

    static {
        hashMap.put(KEY_RADIUS_5KM, R.id.radio_5km);
        hashMap.put(KEY_RADIUS_10KM, R.id.radio_10km);
        hashMap.put(KEY_RADIUS_15KM, R.id.radio_15km);
        hashMap.put(KEY_RADIUS_20KM, R.id.radio_20km);
        hashMap.put(KEY_LOCATOR_BRANCH, R.id.radio_branch);
        hashMap.put(KEY_LOCATOR_ATM, R.id.radio_atm);
        hashMap.put(KEY_LOCATOR_AGENT, R.id.radio_agent);
    }

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 20;
    private int locatorType, markerIcon, previousLocatorType;
    private boolean isActivityFinished;
    private double currentLatitude, currentLongitude;
    private String strRadius, strLocator, strLatitude, strLongitude;
    private List<LocationModel> listLocations;
    private RecyclerView recyclerViewLocations;
    private SupportMapFragment fragmentMapView;
    private GoogleMap map;
    private LinearLayout mapLayout;
    private Button btnDistanceSelector, btnLocatorOptionSelector;
    private Dialog dialog = null;
    private ImageButton btnExpand, btnCollapse;
    private FusedLocationService locationService;
    RelativeLayout rl_location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
        isActivityFinished = false;


        headerImplementation();
        btnHome.setVisibility(View.GONE);

        listLocations = new ArrayList<>();

        RelativeLayout layoutMain = (RelativeLayout) findViewById(R.id.layout_main);
        setLocator(LOCATOR_TYPE_BRANCH);

        strRadius = KEY_RADIUS_5KM;

        recyclerViewLocations = (RecyclerView) findViewById(R.id.listLocations);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerViewLocations.setLayoutManager(linearLayoutManager);


        btnDistanceSelector = (Button) findViewById(R.id.btnDistance);
        btnDistanceSelector.setVisibility(View.VISIBLE);
//        btnDistanceSelector.setEnabled(false);

        btnLocatorOptionSelector = (Button) findViewById(R.id.btnLocatorOptions);
//        btnLocatorOptionSelector.setVisibility(View.VISIBLE);
        btnLocatorOptionSelector.setEnabled(false);
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    LocatorActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogGeneral.dismiss();
                            goToStart();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        } else {


            if (map == null) {
                fragmentMapView = ((SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentMapView));
                fragmentMapView.getMapAsync(new OnMapReadyCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        map = googleMap;
                        map.getUiSettings().setCompassEnabled(false);
                        map.getUiSettings().setMyLocationButtonEnabled(true);
                        map.getUiSettings().setZoomControlsEnabled(true);
//                        map.setMyLocationEnabled(true);


                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            rl_location = (RelativeLayout) findViewById(R.id.rl_location);
                            locationService = new FusedLocationService(LocatorActivity.this);
                        } else {
                            checkPermissions();
                        }

                    }
                });
            }

        }

        btnDistanceSelector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = PopupDialogs.createRadioDialog(
                        getString(R.string.heading_distance_option),
                        new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group,
                                                         int checkedId) {
                                if (dialog != null && dialog.isShowing()) {
                                    if (checkedId == R.id.radio_5km) {
                                        strRadius = KEY_RADIUS_5KM;
                                    } else if (checkedId == R.id.radio_10km) {
                                        strRadius = KEY_RADIUS_10KM;
                                    } else if (checkedId == R.id.radio_15km) {
                                        strRadius = KEY_RADIUS_15KM;
                                    } else if (checkedId == R.id.radio_20km) {
                                        strRadius = KEY_RADIUS_20KM;
                                    }
                                    dialog.dismiss();
                                    listLocations.clear();
                                    totalCount = -1;
                                    pageNo = 1;
                                    processRequest();
                                }
                            }
                        }, LocatorActivity.this, PopupDialogs.Status.DISTANCE, hashMap
                                .get(strRadius));
            }
        });

        btnLocatorOptionSelector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = PopupDialogs.createRadioDialog(
                        getString(R.string.heading_locator_option),
                        new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group,
                                                         int checkedId) {
                                if (dialog != null && dialog.isShowing()) {
                                    if (checkedId == R.id.radio_atm) {
                                        setLocator(LOCATOR_TYPE_ATM);
                                    } else if (checkedId == R.id.radio_branch) {
                                        setLocator(LOCATOR_TYPE_BRANCH);
                                    } else if (checkedId == R.id.radio_agent) {
                                        setLocator(LOCATOR_TYPE_AGENT);
                                    }
                                    dialog.dismiss();
                                    processRequest();
                                }
                            }
                        }, LocatorActivity.this, PopupDialogs.Status.LOCATOR, hashMap
                                .get(strLocator));
            }
        });

        btnExpand = (ImageButton) findViewById(R.id.btnExpand);
        btnCollapse = (ImageButton) findViewById(R.id.btnCollapse);

        btnExpand.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCollapse.setVisibility(ImageButton.VISIBLE);
                btnExpand.setVisibility(ImageButton.INVISIBLE);
                expandMapView();
            }
        });

        btnCollapse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnExpand.setVisibility(ImageButton.VISIBLE);
                btnCollapse.setVisibility(ImageButton.INVISIBLE);
                collapseMapView();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    public void expandMapView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 2;
        mapLayout = (LinearLayout) findViewById(R.id.mapviewLayout);
        mapLayout.setLayoutParams(params);
    }

    public void collapseMapView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        params.weight = 1;
        mapLayout = (LinearLayout) findViewById(R.id.mapviewLayout);
        mapLayout.setLayoutParams(params);

    }

    public void setLatLng() {
        strLatitude = String.valueOf(currentLatitude);
        strLongitude = String.valueOf(currentLongitude);
    }

    private void loadLocationsOnMap() {
//        collapseMapView();

        map.clear();

        for (int i = 0; i < listLocations.size(); i++) {
            LocationModel location = (LocationModel) listLocations.get(i);
            map.addMarker(new MarkerOptions()
                    .title(location.getName())
                    .position(
                            new LatLng(Double.parseDouble(location
                                    .getLatitude()), Double
                                    .parseDouble(location.getLongitude())))
                    .icon(BitmapDescriptorFactory.fromResource(markerIcon))
                    .snippet(location.getAdd()));


        }

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to
                // Mountain View
                .zoom(13) // Sets the zoom
                .tilt(20) // Sets the tilt of the camera to 30 degrees
                .build(); // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant

            return;
        }
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.setMyLocationEnabled(true);


    }

    private void setLocator(int locatorType) {
        previousLocatorType = this.locatorType;
        switch (locatorType) {
            case LOCATOR_TYPE_BRANCH:
                this.locatorType = LOCATOR_TYPE_BRANCH;
                markerIcon = R.drawable.bank_icon;
                strLocator = KEY_LOCATOR_BRANCH;
                break;
            case LOCATOR_TYPE_ATM:
                this.locatorType = LOCATOR_TYPE_ATM;
                markerIcon = R.drawable.atm_icon;
                strLocator = KEY_LOCATOR_ATM;
                break;
            case LOCATOR_TYPE_AGENT:
                this.locatorType = LOCATOR_TYPE_AGENT;
                markerIcon = R.drawable.agent_icon;
                strLocator = KEY_LOCATOR_AGENT;
                break;
        }
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        rl_location = (RelativeLayout) findViewById(R.id.rl_location);
        locationService = new FusedLocationService(this);

//        map.setMyLocationEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Display UI and wait for user interaction

                finish();
                return;
            } else {
                showSettingsAlert();
            }

        } else {

            rl_location = (RelativeLayout) findViewById(R.id.rl_location);
            locationService = new FusedLocationService(this);
        }
    }


    public void showSettingsAlert() {

        OnClickListener enableLocationListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Settings.ACTION_APPLICATION_SETTINGS);
                startActivity(intent);
                finish();
            }
        };

        OnClickListener cancelListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        PopupDialogs.createConfirmationDialog(
                AppMessages.LOCATION_PERMISSION_NOT_AVAILABLE,
                AppMessages.HEADING_LOCATION_PERMISSION, LocatorActivity.this,
                enableLocationListener, "SETTINGS", cancelListener, "CANCEL",
                PopupDialogs.Status.ALERT);
    }

    @Override
    public void onItemClicked(View view, int position) {
        LocationModel locationModel = (LocationModel) listLocations
                .get(position);

        Intent intent = new Intent(LocatorActivity.this,
                DirectionsActivity.class);
//        intent.putExtras(bundle);
        intent.putExtra(Constants.IntentKeys.CURRENT_LATITUDE,
                currentLatitude);
        intent.putExtra(Constants.IntentKeys.CURRENT_LONGITUDE,
                currentLongitude);
        intent.putExtra(Constants.IntentKeys.TARGET_LOCATION,
                locationModel);
        intent.putExtra(Constants.IntentKeys.LOCATOR_TYPE, locatorType);
        startActivity(intent);
    }

    @Override
    public void processRequest() {

        final int pageSize = 8;
        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    LocatorActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);
        } else {
            showLoading(getString(R.string.please_wait), getString(R.string.processing));
            new HttpAsyncTask(LocatorActivity.this).execute(Constants.CMD_LOCATOR + "",
                    strRadius + "", strLatitude, strLongitude, "2" + "", pageNo + "", pageSize + "");
        }
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
                    List<?> list = (List<?>) table
                            .get(Constants.KEY_LIST_ERRORS);
                    MessageModel messageModel = (MessageModel) list.get(0);


                    dialogGeneral = popupDialogs.createAlertDialog(messageModel.getDescr(), AppMessages.ALERT_HEADING,
                            LocatorActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    goToStart();
                                    dialogGeneral.dismiss();

                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


                    setLocator(previousLocatorType);
                } else if (table.containsKey(Constants.KEY_LIST_MSGS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_MSGS);
                    MessageModel message = (MessageModel) list.get(0);

                    setCurrentLocation();


                    dialogGeneral = popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                            LocatorActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);

                } else {


                    totalCount = Integer.parseInt(table.get(XmlConstants.Attributes.TOTAL_COUNT).toString());

                    List<LocationModel> listLocations1 = (List<LocationModel>) table
                            .get(Constants.KEY_LIST_LOCATIONS);


                    listLocations.addAll(listLocations1);
                    AppLogger.i("\nLocations :"
                            + String.valueOf(listLocations.size()));
                    processNext();
                }
            }
            hideLoading();
        } catch (Exception exp) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();


                    dialogGeneral = popupDialogs.createAlertDialog(AppMessages.EXCEPTION, AppMessages.ALERT_HEADING,
                            LocatorActivity.this, getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogGeneral.dismiss();
                                    goToStart();
                                }
                            }, false, PopupDialogs.Status.ERROR, false, null);


//                    OnClickListener clickListener = new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            goToStart();
//                        }
//                    };
//
//                    PopupDialogs.createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, getString(R.string.alertNotification),
//                            LocatorActivity.this, clickListener, PopupDialogs.Status.ERROR);
                }
            });
            AppLogger.e(exp);
        }
    }

    @Override
    public void processNext() {


        switch (locatorType) {
            case LOCATOR_TYPE_BRANCH:
                btnLocatorOptionSelector.setText("Branch");
                break;
            case LOCATOR_TYPE_ATM:
                btnLocatorOptionSelector.setText("ATM");
                break;
            case LOCATOR_TYPE_AGENT:
                btnLocatorOptionSelector.setText("Agent");
                break;
        }


        if (pageNo == 1) {
            show();
        } else {
            adapter.notifyDataSetChanged();
            loadLocationsOnMap();
        }

        btnDistanceSelector.setText(strRadius + "KM");
//        loadLocationsOnMap();


//        LocationListAdapter adapter = new LocationListAdapter(
//                getApplicationContext(), listLocations, LocatorActivity.this);
//        recyclerViewLocations.setAdapter(adapter);

        btnDistanceSelector.setEnabled(true);
        btnLocatorOptionSelector.setEnabled(true);
    }

    @Override
    public void locationFetched(Location location) {

        if (!isActivityFinished) {

            rl_location.setVisibility(View.GONE);

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            setLatLng();
            processRequest();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityFinished = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FusedLocationService.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        locationService.startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    private void show() {


//        imgAdapter.setClickListener(this);
//        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
//        itemAnimator.setAddDuration(1000);
//        itemAnimator.setRemoveDuration(1000);


        final GridLayoutManager gridLayoutManager = new GridLayoutManager(LocatorActivity.this, 1);
        adapter = new LocationListAdapter(getApplicationContext(), listLocations, LocatorActivity.this);
        recyclerViewLocations.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);


//        recyclerViewLocations.setLayoutManager(linearLayoutManager);
//        recyclerViewLocations.setItemAnimator(new DefaultItemAnimator());
//        rv.setAdapter(adapter);

        RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                if (!loadingDialog.isShowing()) {
                    if (listLocations.size() != 0 && listLocations.size() < totalCount) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                            pageNo = pageNo + 1;
                            processRequest();
                        }
                    }
                }
            }
        };


        recyclerViewLocations.addOnScrollListener(recyclerViewOnScrollListener);
        recyclerViewLocations.setItemAnimator(itemAnimator);
        recyclerViewLocations.setAdapter(adapter);

        map.clear();

        loadLocationsOnMap();


    }


    private void locateBrands() {


//        final int pageSize = 100;
//        String city = "";
//
//        processRequest();

//            if(filterList.size() == 0 || filterList.size()>=pageSize) {
//                String message = "Checking in " + proximity_radius + "KM radius.";


//                NetworkCalls call = new NetworkCalls(this, this, BuildConstants.LOYALITY_URL);
//                call.execute(Constants.WebServices.GET_NEARBY_MERCHANTS,
//                        categoryID,
//                        proximity_radius, ApplicationData.latitude, ApplicationData.longitude,
//                        Constants.MERCHANTS_LOCATION_TYPE, city, merchantId, String.valueOf(pageNo),
//                        String.valueOf(pageSize));

//        }
    }

    private void setCurrentLocation() {

        btnDistanceSelector.setText(strRadius + "KM");
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to
                // Mountain View
                .zoom(13) // Sets the zoom
                .tilt(20) // Sets the tilt of the camera to 30 degrees
                .build(); // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant

            return;
        }
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.setMyLocationEnabled(true);


    }
}