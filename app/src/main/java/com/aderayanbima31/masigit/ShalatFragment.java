package com.aderayanbima31.masigit;


import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aderayanbima31.masigit.utils.GetLocation;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Bima.
 */
public class ShalatFragment extends Fragment {

    private static final long MIN_TIME_BW_UPDATES = 0;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    TextView fajr, sunrise, dhuhr, asr, sunset, maghrib, isha;

    /*TextView fajr = (TextView) getActivity().findViewById(R.id.textView1);
    TextView sunrise = (TextView) getActivity().findViewById(R.id.textView2);
    TextView dhuhr = (TextView) getActivity().findViewById(R.id.textView3);
    TextView asr = (TextView) getActivity().findViewById(R.id.textView4);
    TextView sunset = (TextView) getActivity().findViewById(R.id.textView5);
    TextView maghrib = (TextView) getActivity().findViewById(R.id.textView6);
    TextView isha = (TextView) getActivity().findViewById(R.id.textView7);
    */
    Spinner calculationMethod;
    Context mContext;

    LocationManager mLocationManager;
    private boolean canGetLocation;

    public ShalatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shalat, container, false);





        variableInitializations();
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // checkIfGPSIsOn(manager,this);
//        Location lastLoc = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        final double lat = lastLoc.getLatitude();
//        final double lng = lastLoc.getLongitude();
        final double timeZone = (TimeZone.getTimeZone(Time.getCurrentTimezone()).getOffset(System.currentTimeMillis())) / (1000.0 * 60.0 * 60.0);

        GetLocation getlocation = new GetLocation(mContext);

        final double lat =getlocation.getLatitude();
        final double lng = getlocation.getLongitude();

        // Location myLocation = getLastKnownLocation();
        // final double lat = myLocation.getLatitude();
        // final double lng = myLocation.getLongitude();

        Toolbar myToolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
        //setSupportActionBar(myToolbar);
        final SharedPreferences getData = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        getData.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("TestPref", key);
                String test = sharedPreferences.getString(key, "Karachi");
                if (test.contentEquals("Jafari")) {
                    Toast.makeText(getActivity().getApplication(), "Jafari", Toast.LENGTH_LONG).show();
                    Log.d("JafariToast", "Jafari");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Jafari, PrayTime.Hanafi, lat, lng, timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                } else if (test.contentEquals("Karachi")) {
                    Toast.makeText(getActivity().getApplication(), "Karachi", Toast.LENGTH_LONG).show();
                    Log.d("JafariToast", "Karachi");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Karachi, PrayTime.Hanafi, lat, lng, timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                } else if (test.contentEquals("ISNA")) {
                    Toast.makeText(getActivity().getApplication(), "ISNA", Toast.LENGTH_LONG).show();
                    Log.d("JafariToast", "ISNA");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.ISNA, PrayTime.Hanafi, lat, lng, timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                } else if (test.contentEquals("MWL")) {
                    Toast.makeText(getActivity().getApplication(), "MWL", Toast.LENGTH_LONG).show();
                    Log.d("JafariToast", "MWL");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.MWL, PrayTime.Hanafi, lat, lng, timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                } else if (test.contentEquals("Makkah")) {
                    Toast.makeText(getActivity().getApplication(), "Makkah", Toast.LENGTH_LONG).show();
                    Log.d("JafariToast", "Makkah");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Makkah, PrayTime.Hanafi, lat, lng, timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                } else if (test.contentEquals("Egypt")) {
                    Toast.makeText(getActivity().getApplication(), "Egypt", Toast.LENGTH_LONG).show();
                    Log.d("JafariToast", "Egypt");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Egypt, PrayTime.Hanafi, lat, lng, timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                } else {
                    Toast.makeText(getActivity().getApplication(), "Tehran", Toast.LENGTH_LONG).show();
                    Log.d("JafariToast", "tehran");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Tehran, PrayTime.Hanafi, lat, lng, timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                }

            }
        });

        String getCalMethod = getData.getString("calMethod", "Karachi");
        if (getCalMethod.contentEquals("Karachi")) {

        }


        Log.d("TimeZone", "" + timeZone);
        //create LatLng
        LatLng lastLatLng = new LatLng(lat, lng);
        ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Jafari, PrayTime.Shafii, lat, lng, timeZone);

        fajr.setText(prayerTimes.get(0));
        sunrise.setText(prayerTimes.get(1));
        dhuhr.setText(prayerTimes.get(2));
        asr.setText(prayerTimes.get(3));
        sunset.setText(prayerTimes.get(4));
        maghrib.setText(prayerTimes.get(5));
        isha.setText(prayerTimes.get(6));

        Log.d("LatLng in Salah", lastLatLng.toString());
        String lats = String.valueOf(lat);
        String lngs = String.valueOf(lng);
        Toast.makeText(getActivity(), lats + lngs + lastLatLng.toString(), Toast.LENGTH_LONG).show();
        // txt.setText(lastLoc.toString());



        return v;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void clearFields() {
        fajr.setText("");
        sunrise.setText("");
        dhuhr.setText("");
        asr.setText("");
        sunset.setText("");
        maghrib.setText("");
        isha.setText("");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent prefs = new Intent(getActivity(),ShalatFragment.class);
            startActivity(prefs);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void variableInitializations() {
        fajr = (TextView) getActivity().findViewById(R.id.textView1);
        sunrise = (TextView) getActivity().findViewById(R.id.textView2);
        dhuhr = (TextView) getActivity().findViewById(R.id.textView3);
        asr = (TextView) getActivity().findViewById(R.id.textView4);
        sunset = (TextView) getActivity().findViewById(R.id.textView5);
        maghrib = (TextView) getActivity().findViewById(R.id.textView6);
        isha = (TextView) getActivity().findViewById(R.id.textView7);

        mContext = getActivity();

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, Url.namazTimingsBaseUrl, null,
                new com.android.volley.Response.Listener<JSONObject>(){
                    public void onResponse(JSONObject response) {
                        Log.d("NAMAZTIMINGSXHANCE", response.toString());
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("NAMAZTIMINGSXHANCE", error.toString());
                    }
                });
        queue.add(request);
    }

    private ArrayList<String> extractPrayerTimings(int calMethod, int juristic, double lat, double lng, double timeZone) {
        PrayTime prayers = new PrayTime();


        prayers.setTimeFormat(prayers.Time12);

        prayers.setCalcMethod(calMethod);
        prayers.setAsrJuristic(juristic);
        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal,
                lat, lng, timeZone);
        ArrayList<String> prayerNames = prayers.getTimeNames();

        for (int i = 0; i < prayerTimes.size(); i++) {
            //   System.out.println(prayerNames.get(i) + " - " + prayerTimes.get(i));
            Log.d("Namaz", prayerTimes.get(i));


        }


        return prayerTimes;
    }

//    public Location getLocation() {
//        Location location;
//        try {
//            mLocationManager = (LocationManager) mContext
//                    .getSystemService(LOCATION_SERVICE);
//
//            // getting GPS status
//            boolean isGPSEnabled = mLocationManager
//                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            // getting network status
//            boolean isNetworkEnabled = mLocationManager
//                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                // no network provider is enabled
//            } else {
//                this.canGetLocation = true;
//                double latitude;
//                double longitude;
//                if (isNetworkEnabled) {
//                    mLocationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    Log.d("Network", "Network Enabled");
//                    if (mLocationManager != null) {
//                        location = mLocationManager
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
//                // if GPS Enabled get lat/long using GPS Services
//                if (isGPSEnabled) {
//                    if (location == null) {
//                        mLocationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.d("GPS", "GPS Enabled");
//                        if (mLocationManager != null) {
//                            location = mLocationManager
//                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if (location != null) {
//                                latitude = location.getLatitude();
//                                longitude = location.getLongitude();
//                            }
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return location;
//    }

//    private Location getLastKnownLocation() {
//        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//
//        List<String> providers = mLocationManager.getProviders(true);
//        Location bestLocation = null;
//        for (String provider : providers) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Location l = mLocationManager.getLastKnownLocation(provider);
//            if (l == null) {
//                continue;
//            }
//            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                // Found best last known location: %s", l);
//                bestLocation = l;
//            }
//        }
//        return bestLocation;
//    }



    protected void checkIfGPSIsOn(LocationManager manager, Context context) {
        if (!manager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Location Manager");
            builder.setMessage("Masjid Locator requires a device with GPS to work");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
            builder.create().show();
        } else {

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Ask the user to enable GPS
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Location Manager").setCancelable(false)
                        .setMessage("Masjid Locator would like to enable your device's GPS?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Launch settings, allowing user to make a change

//                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            activity.startActivity(i);
                                // Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            Intent i = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                            startActivity(i);
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                            intent.setData(uri);
//                            startActivity(intent);
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                                //   startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //No location service, no Activity
                                getActivity().finish();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();
            }
        }
    }

}
