package com.inov8.jsblconsumer.activities.locator;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class FusedLocationService implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String TAG = "FusedLocationService";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final int MINIMUM_DISPLACEMENT = 5;
    public static final int REQUEST_CHECK_SETTINGS = 0 * 1;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private LocationSettingsRequest locationSettingsRequest;
    private Location location;
    private Activity activity;

    public FusedLocationService(Activity activity) {

        this.activity = activity;

        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(MINIMUM_DISPLACEMENT);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        locationSettingsRequest = builder.build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        googleApiClient, locationSettingsRequest);
        result.setResultCallback(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "I'm connected now");
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSIONS);
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }



    @Override
    public void onLocationChanged(Location location) {

        removeUpdates();
        this.location = location;
        ((LocationCallback)activity).locationFetched(location);
    }




    public Location getLocation() {
        return this.location;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(activity,"GPS connection has been suspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(activity,connectionResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSIONS);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try {
                    status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    public void removeUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, FusedLocationService.this);
    }
}
