package com.inov8.jsblconsumer.activities.locator;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.model.LocationModel;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DirectionsActivity extends BaseActivity {
    private int locatorType, markerIcon;
    private SupportMapFragment fragmentMapView;
    private GoogleMap map;
    private double currentLatitude, targetLatitude;
    private double currentLongitude, targetLongitude;
    private LocationModel locationModel;
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        headerImplementation();
        fetchIntents();

        targetLatitude = Double.parseDouble(locationModel.getLatitude());
        targetLongitude = Double.parseDouble(locationModel.getLongitude());

        switch (locatorType) {
            case LocatorActivity.LOCATOR_TYPE_BRANCH:
                markerIcon = R.drawable.bank_icon;
                break;
            case LocatorActivity.LOCATOR_TYPE_ATM:
                markerIcon = R.drawable.atm_icon;
                break;
            case LocatorActivity.LOCATOR_TYPE_AGENT:
                markerIcon = R.drawable.agent_icon;
                break;
        }

        if (!haveInternet()) {
            dialogGeneral = popupDialogs.createAlertDialog(AppMessages.INTERNET_CONNECTION_PROBLEM, AppMessages.ALERT_HEADING,
                    DirectionsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            dialogGeneral.dismiss();
                        }
                    }, false, PopupDialogs.Status.ERROR, false, null);

            return;
        } else {
            if (map == null) {
                fragmentMapView = ((SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentMapView));
                fragmentMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        map = googleMap;
                        map.getUiSettings().setCompassEnabled(false);
                        map.getUiSettings().setZoomControlsEnabled(true);

                        LatLng lastLatLng = new LatLng(targetLatitude, targetLongitude);

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(lastLatLng) // Sets the center of the map to
                                // Mountain View
                                .zoom(15) // Sets the zoom
                                .tilt(30) // Sets the tilt of the camera to 30 degrees
                                .build(); // Creates a CameraPosition from the builder

                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));

                        // target tag
                        map.addMarker(new MarkerOptions().title(locationModel.getName())
                                .position(new LatLng(targetLatitude, targetLongitude))
                                .icon(BitmapDescriptorFactory.fromResource(markerIcon))
                                .snippet(locationModel.getAdd()));

                        // current location tag
                        map.addMarker(new MarkerOptions()
                                .title("Current Location")
                                .position(new LatLng(currentLatitude, currentLongitude))
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.current_pin)));

                        new connectAsyncTask(makeURL(currentLatitude, currentLongitude,
                                targetLatitude, targetLongitude)).execute();
                    }
                });
            }
        }

//        if (map != null) {
//
//        }
    }

    // new methods
    public String makeURL(double sourcelat, double sourcelog, double destlat,
                          double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    // drawing of poly line
    public void drawPath(String result) {
        try {
            // Transform the string into a json object
            JSONObject json = new JSONObject(result);

            JSONArray routeArray = json.getJSONArray("routes");

            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                map.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(6).color(Color.BLUE).geodesic(true));
            }

        } catch (JSONException e) {

            if (counter >= 3) {
                counter=0;
                dialogGeneral = popupDialogs.createAlertDialog("Unable to fetch route. Please try again.", AppMessages.ALERT_HEADING,
                        DirectionsActivity.this, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                dialogGeneral.dismiss();

                            }
                        }, false, PopupDialogs.Status.ERROR, false, null);
            } else {
                counter++;
                new connectAsyncTask(makeURL(currentLatitude, currentLongitude,
                        targetLatitude, targetLongitude)).execute();
            }


        }
    }

    // decode poly
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    private void fetchIntents() {
        locatorType = mBundle.getInt(Constants.IntentKeys.LOCATOR_TYPE);
        currentLatitude = mBundle
                .getDouble(Constants.IntentKeys.CURRENT_LATITUDE);
        currentLongitude = mBundle
                .getDouble(Constants.IntentKeys.CURRENT_LONGITUDE);
        locationModel = (LocationModel) mBundle
                .get(Constants.IntentKeys.TARGET_LOCATION);
    }

    // async
    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        String url;
        private ProgressDialog progressDialog;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DirectionsActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                drawPath(result);
            }
        }
    }

    // json parser class
    public class JSONParser {

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // constructor
        public JSONParser() {
        }


        private String readStream(InputStream stream) throws IOException {

            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            BufferedOutputStream out = null;
            try {
                int length = 0;
                out = new BufferedOutputStream(byteArray);
                while ((length = stream.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                out.flush();
                return byteArray.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }


        public String getJSONFromUrl(String url1) {


            InputStream is = null;
            try {

                URL url = new URL(url1);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    throw new IOException("Got response code " + responseCode);
                }
                is = conn.getInputStream();
                return readStream(is);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;

        }


//            // Making HTTP request
//            try {
//                // defaultHttpClient
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpPost httpPost = new HttpPost(url);
//
//                org.apache.http.HttpResponse httpResponse = httpClient
//                        .execute(httpPost);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                is = httpEntity.getContent();
//
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(is, "iso-8859-1"), 8);
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//
//                json = sb.toString();
//                is.close();
//            } catch (Exception e) {
//                Log.e("Buffer Error", "Error converting result " + e.toString());
//            }
//            return json;
//        }
    }
}