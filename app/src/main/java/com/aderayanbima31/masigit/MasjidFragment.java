package com.aderayanbima31.masigit;


import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.aderayanbima31.masigit.MasjidFragment;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Basil.
 */
public class MasjidFragment extends Fragment implements LocationListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public MasjidFragment() {
        // Required empty public constructor

    }

    int userIcon, masjidIcon, drinkIcon, shopIcon, otherIcon;

    //the map
    //the map
    private GoogleMap googleMap;
    MapView mMapView;
    private Button start;
    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;


    //location manager
    private LocationManager locMan;

    //user marker
    private Marker userMarker;

    //places of interest
    private Marker[] placeMarkers;
    //max
    private final int MAX_PLACES = 20;//most returned from google
    //marker options
    private MarkerOptions[] places;

    Context context;

    private boolean updateFinished = true;

    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]
            {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.READ_SMS
            };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_masjid, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map_masjid);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();

        }

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                float zoomLevel = 20.0f;
                googleMap = mMap;
                String vicinity = "";
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                //float zoomLevel=17.0f;
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap, zoomLevel));
                googleMap.setMyLocationEnabled(true);
                //mMap.getMaxZoomLevel(zoomLevel);





                // For dropping a marker at a point on the Map

                LatLng masjidraya = new LatLng(-6.921658, 107.606512);
                googleMap.addMarker(new MarkerOptions().position(masjidraya).title("Masjid Raya Bandung").snippet("Jl. Asia Afrika, Balonggede, Regol, Kota Bandung, Jawa Barat 40251").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                //googleMap.addMarker(new MarkerOptions().position(masjidraya).title("Masjid Raya Bandung").snippet("Jl. Asia Afrika, Balonggede, Regol, Kota Bandung, Jawa Barat 40251").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(masjidraya).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                LatLng masjid2 = new LatLng(-6.898952, 107.585126);
                googleMap.addMarker(new MarkerOptions().position(masjid2).title("Masjid Habiburahman").snippet("Jl. Kapten Tata Natanegara, Pajajaran, Cicendo, Kota Bandung, Jawa Barat 40141").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition2 = new CameraPosition.Builder().target(masjid2).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2));

                LatLng masjid3 = new LatLng(-6.893650, 107.611211);
                googleMap.addMarker(new MarkerOptions().position(masjid3).title("Masjid Salman ITB").snippet("Jl. Ganeca No.7, Lb. Siliwangi, Coblong, Kota Bandung, Jawa Barat 40132").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition3 = new CameraPosition.Builder().target(masjid3).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition3));

                LatLng masjid4 = new LatLng(-6.929602, 107.716749);
                googleMap.addMarker(new MarkerOptions().position(masjid4).title("Masjid Al-Huda Cipadung").snippet("Komplek Cipadung Permai Cibiru Bandung, West Java, Cipadung, Cibiru, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition4 = new CameraPosition.Builder().target(masjid4).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition4));

                LatLng masjid5 = new LatLng(-6.931479, 107.717392);
                googleMap.addMarker(new MarkerOptions().position(masjid5).title("Masjid Ikomah UIN Bandung").snippet("Jl. A.H. Nasution, Cipadung, Cibiru, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition5 = new CameraPosition.Builder().target(masjidraya).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition5));

                LatLng masjid6 = new LatLng(-6.900733, 107.625904);
                googleMap.addMarker(new MarkerOptions().position(masjid6).title("Masjid PUSDAI ( Pusat Dakwah Islam )").snippet("Jl. Diponegoro No.63, Cihaur Geulis, Cibeunying Kaler, Kota Bandung, Jawa Barat 40115").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition6 = new CameraPosition.Builder().target(masjid6).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition6));

                LatLng masjid7 = new LatLng(-6.931417, 107.716779);
                googleMap.addMarker(new MarkerOptions().position(masjidraya).title("Masjid Kifayatul Achyar").snippet("Jl. Raya Cipadung, Cipadung, Cibiru, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition7 = new CameraPosition.Builder().target(masjid7).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition7));

                LatLng masjid8 = new LatLng(-6.929406, 107.711524);
                googleMap.addMarker(new MarkerOptions().position(masjid8).title("Masjid As-siraj").snippet("Jl. A.H. Nasution No.274, Cipadung, Panyileukan, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition8 = new CameraPosition.Builder().target(masjid8).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition8));

                LatLng masjid9 = new LatLng(-6.907961, 107.627894);
                googleMap.addMarker(new MarkerOptions().position(masjid9).title("Masjid Al Lathiif").snippet("Jl. Saninten No. 2, RT. 01 / RW. 05, Cihapit, Bandung Wetan, Cihapit, Bandung Wetan, Kota Bandung, Jawa Barat 40114").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition9 = new CameraPosition.Builder().target(masjid9).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition9));


                LatLng masjid10 = new LatLng(-6.904678, 107.621651);
                googleMap.addMarker(new MarkerOptions().position(masjid10).title("Masjid Istiqamah").snippet("Jalan Taman Citarum No.1, Citarum, Bandung Wetan, Kota Bandung, Jawa Barat 40115").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition10 = new CameraPosition.Builder().target(masjid10).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition10));

                LatLng masjid11 = new LatLng(-6.925945, 107.635426);
                googleMap.addMarker(new MarkerOptions().position(masjid11).title("Masjid Agung Trans Studio Bandung").snippet("Pusat Terpadu Transtudio, Jalan Gatot Subroto No. 289, Cibangkong,  Batununggal, Kota Bandung, Jawa Barat 40273").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition11 = new CameraPosition.Builder().target(masjid11).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition11));

                LatLng masjid12 = new LatLng(-6.927175, 107.716767);
                googleMap.addMarker(new MarkerOptions().position(masjid12).title("Masjid Attolibin MAN 2 Kota Bandung").snippet("Jl. Desa Cipadung No.73, Cipadung, Cibiru, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition12 = new CameraPosition.Builder().target(masjid12).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition12));

                LatLng masjid13 = new LatLng(-6.913678, 107.700842 );
                googleMap.addMarker(new MarkerOptions().position(masjid13).title("Masjid Besar Ujung Berung").snippet("Jl. Kaum Kulon No.16, Cigending, Ujung Berung, Kota Bandung, Jawa Barat 40611").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition13 = new CameraPosition.Builder().target(masjid13).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition13));

                LatLng masjid14 = new LatLng(-6.931711, 107.719619);
                googleMap.addMarker(new MarkerOptions().position(masjid14).title("Masjid Al Hidayah").snippet("Jalan Manisi 6, RT.03/RW.04, Pasir Biru, Cibiru, Kota Bandung, Jawa Barat 40615").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition14 = new CameraPosition.Builder().target(masjid10).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition10));


                LatLng masjid15 = new LatLng(-6.930070, 107.721549);
                googleMap.addMarker(new MarkerOptions().position(masjid15).title("Masjid Nurul Amal").snippet("Pasir Biru, Cibiru, Kota Bandung, Jawa Barat 40615").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition15 = new CameraPosition.Builder().target(masjid15).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition15));


                LatLng masjid16 = new LatLng(-6.921309, 107.678371);
                googleMap.addMarker(new MarkerOptions().position(masjid16).title("masjid kifayatul achyar").snippet("RT 3 RW 11, Kelurahan, Jl. Permata Tamansari II, Cisaranten Kulon, Arcamanik, Bandung City, West Java 40293").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition16 = new CameraPosition.Builder().target(masjid16).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition16));

                LatLng masjid17 = new LatLng(-6.932131, 107.715714);
                googleMap.addMarker(new MarkerOptions().position(masjid17).title("masjid kifayatul achyar").snippet("JL. Raya Cipadung, 02/05, Cipadung, Cibiru, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition17 = new CameraPosition.Builder().target(masjid17).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition17));

                LatLng masjid18 = new LatLng(-6.904140, 107.669966);
                googleMap.addMarker(new MarkerOptions().position(masjid18).title("yayasan masjid nusantara").snippet("Jl. A.H. Nasution No. 131, Karang Pamulang, Mandalajati, Karang Pamulang, Mandalajati, Kota Bandung, Jawa Barat 40195").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition18 = new CameraPosition.Builder().target(masjid18).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition18));

                LatLng masjid19 = new LatLng(-6.927187, 107.716763);
                googleMap.addMarker(new MarkerOptions().position(masjid19).title("Masjid Attolibin MAN 2 Kota Bandung").snippet("Jl. Desa Cipadung No.73, Cipadung, Cibiru, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition19 = new CameraPosition.Builder().target(masjid19).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition19));

                LatLng masjid20 = new LatLng(-6.929398, 107.711527);
                googleMap.addMarker(new MarkerOptions().position(masjid20).title("Masjid As-siraj").snippet("Jl. A.H. Nasution No.274, Cipadung, Panyileukan, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition20 = new CameraPosition.Builder().target(masjid20).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition20));

                LatLng masjid21 = new LatLng(-6.955427, 107.710183);
                googleMap.addMarker(new MarkerOptions().position(masjid21).title("Masjid Abu Bakar Ash Shiddiq").snippet("Rancanumpang, Gedebage, Rancanumpang, Bandung, Kota Bandung, Jawa Barat 40292").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition21 = new CameraPosition.Builder().target(masjid21).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition21));

                LatLng masjid22 = new LatLng(-6.910724, 107.608587);
                googleMap.addMarker(new MarkerOptions().position(masjid22).title("Masjid Agung Al-Ukhuwwah").snippet("Jalan Wastukencana No.27, Babakan Ciamis, Sumur Bandung, Babakan Ciamis, Sumur Bandung, Kota Bandung, Jawa Barat 40117").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition22 = new CameraPosition.Builder().target(masjid22).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition22));

                LatLng masjid23 = new LatLng(-6.930298, 107.642366);
                googleMap.addMarker(new MarkerOptions().position(masjid23).title("Masjid Ash Shiddiq").snippet("Jl. Kebon Gedang, RT. 02 / RW. 10, Maleer, Bandung, Kota Bandung, Jawa Barat 40274").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition23 = new CameraPosition.Builder().target(masjid23).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition23));

                LatLng masjid24 = new LatLng(-6.920986, 107.700575);
                googleMap.addMarker(new MarkerOptions().position(masjid24).title("Masjid Manshurin").snippet("Jl. Sindang Sari No.81, Cipadung Kulon, Panyileukan, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition24 = new CameraPosition.Builder().target(masjid24).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition24));

                LatLng masjid25 = new LatLng(-6.894585, 107.602286);
                googleMap.addMarker(new MarkerOptions().position(masjid25).title("Masjid Besar Cipaganti").snippet("Jalan Cipaganti No.85, Pasteur, Bandung, Pasteur, Sukajadi, Kota Bandung, Jawa Barat 40161").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition25 = new CameraPosition.Builder().target(masjid25).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition25));

                LatLng masjid26 = new LatLng(-6.919224, 107.607536);
                googleMap.addMarker(new MarkerOptions().position(masjid26).title("Masjid Al - Imtizaj").snippet("Jl. ABC No.8, Braga, Sumur Bandung, Kota Bandung, Jawa Barat 40111").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition26 = new CameraPosition.Builder().target(masjid26).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition26));

                LatLng masjid27 = new LatLng(-6.914377, 107.617483);
                googleMap.addMarker(new MarkerOptions().position(masjid27).title("Masjid Al Kautsar").snippet("Jalan Sumbawa No.9, Merdeka, Sumur Bandung, Merdeka, Sumur Bandung, Kota Bandung, Jawa Barat 40113").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition27 = new CameraPosition.Builder().target(masjid27).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition27));

                LatLng masjid28 = new LatLng(-6.931640, 107.721777);
                googleMap.addMarker(new MarkerOptions().position(masjid28).title("Masjid Miftahusaadah").snippet("Pasir Biru, Cibiru, Kota Bandung, Jawa Barat 40615").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition28 = new CameraPosition.Builder().target(masjid28).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition28));

                LatLng masjid29 = new LatLng(-6.942693, 107.714426);
                googleMap.addMarker(new MarkerOptions().position(masjid29).title("Masjid Raya Al Hasan").snippet("Jl. Raya Panyileukan Blok G1 No.2, Cipadung Kidul, Panyileukan, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition29 = new CameraPosition.Builder().target(masjid29).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition29));

                LatLng masjid30 = new LatLng(-6.882049, 107.610705);
                googleMap.addMarker(new MarkerOptions().position(masjid30).title("Masjid LIPI").snippet("Jl. Sangkuriang, Komplek LIPI Bandung, Dago, Coblong, Dago, Coblong, Kota Bandung, Jawa Barat 40135").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition30 = new CameraPosition.Builder().target(masjid30).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition30));

                LatLng masjid31 = new LatLng(-6.935005, 107.735558);
                googleMap.addMarker(new MarkerOptions().position(masjid31).title("Masjid Baitul Manshurin").snippet("Cinunuk, Cileunyi, Bandung, Jawa Barat 40624").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition31 = new CameraPosition.Builder().target(masjid31).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition31));

                LatLng masjid32 = new LatLng(-6.909976, 107.615912);
                googleMap.addMarker(new MarkerOptions().position(masjid32).title("Masjid Junudurrahmah").snippet("Jl. Aceh, Merdeka, Sumur Bandung, Kota Bandung, Jawa Barat 40113").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition32 = new CameraPosition.Builder().target(masjid32).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition32));

                LatLng masjid33 = new LatLng(-6.910619, 107.716143);
                googleMap.addMarker(new MarkerOptions().position(masjid33).title("Masjid Al Ittihad").snippet("Kompleks Pesanggrahan Indah, Pasanggrahan, Ujung Berung, Pasanggrahan, Bandung, Kota Bandung, Jawa Barat 40617").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition33 = new CameraPosition.Builder().target(masjid33).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition33));

                LatLng masjid34 = new LatLng(-6.958122, 107.707454);
                googleMap.addMarker(new MarkerOptions().position(masjid34).title("Masjid Umar Bin Khattab").snippet("Komplek Griya Cempaka, Jalan Utsman Bin Affan No. 90, Rancanumpang, Gedebage, Rancanumpang, Bandung, Kota Bandung, Jawa Barat 40613").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition34 = new CameraPosition.Builder().target(masjid34).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition34));

                LatLng masjid35 = new LatLng(-6.931261, 107.639769);
                googleMap.addMarker(new MarkerOptions().position(masjid35).title("Masjid Al Latief").snippet("Jl. Maler IV, 03/02, Gumuruh, Batununggal, Kota Bandung, Jawa Barat 40274").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition35 = new CameraPosition.Builder().target(masjid35).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition35));

                LatLng masjid36 = new LatLng(-6.895787, 107.660217);
                googleMap.addMarker(new MarkerOptions().position(masjid36).title("Masjid Al-Jabbar").snippet("Jatihandap, Mandalajati, Kota Bandung, Jawa Barat 40195").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition36 = new CameraPosition.Builder().target(masjid36).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition36));

                LatLng masjid37 = new LatLng(-6.946723, 107.626785);
                googleMap.addMarker(new MarkerOptions().position(masjid37).title("Masjid Al-Fajr").snippet("Jl. Cijagra No.39, Cijagra, Lengkong, Kota Bandung, Jawa Barat 40265").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition37 = new CameraPosition.Builder().target(masjid37).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition37));

                LatLng masjid38 = new LatLng(-6.938011, 107.689531);
                googleMap.addMarker(new MarkerOptions().position(masjid38).title("Masjid Assalam PTA Bandung").snippet("Jl. Soekarno Hatta No.714, Babakan Penghulu, Bojongloa Kidul, Kota Bandung, Jawa Barat 40235").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition38 = new CameraPosition.Builder().target(masjid38).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition38));

                LatLng masjid39 = new LatLng(-6.914483, 107.707343);
                googleMap.addMarker(new MarkerOptions().position(masjid39).title("Masjid Al Liqo").snippet("Jl. Neglasari 1 01/04, Pasanggrahan, Bandung, Kota Bandung, Jawa Barat 40617").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition39 = new CameraPosition.Builder().target(masjid39).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition39));

                LatLng masjid40 = new LatLng(-6.930455, 107.619078);
                googleMap.addMarker(new MarkerOptions().position(masjid40).title("Masjid Mujahidin Bandung").snippet("Jl. Sancang No.6, Burangrang, Lengkong, Kota Bandung, Jawa Barat 40262").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition40 = new CameraPosition.Builder().target(masjid40).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition40));

                LatLng masjid41 = new LatLng(-6.943496, 107.683832);
                googleMap.addMarker(new MarkerOptions().position(masjid41).title("Masjid Al Adlha").snippet("Komplek Riung Bandung, Jl. Riung Tineung, Cisaranten Kidul, Gedebage, Kota Bandung, Jawa Barat 40295").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition41 = new CameraPosition.Builder().target(masjid41).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition41));

                LatLng masjid42 = new LatLng(-6.953687, 107.672473);
                googleMap.addMarker(new MarkerOptions().position(masjid42).title("Masjid Al Mujahadah").snippet("Jl. Sharon Boulevard Sel., Cipamokolan, Rancasari, Kota Bandung, Jawa Barat 40292").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition42 = new CameraPosition.Builder().target(masjid42).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition42));

                LatLng masjid43 = new LatLng(-6.917997, 107.675098);
                googleMap.addMarker(new MarkerOptions().position(masjid43).title("Masjid AL-HIDAYAH").snippet("Jl. Arcamanik Endah, Sukamiskin, Arcamanik, Kota Bandung, Jawa Barat 40293").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition43 = new CameraPosition.Builder().target(masjid43).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition43));

                LatLng masjid44 = new LatLng(-6.954476, 107.639866);
                googleMap.addMarker(new MarkerOptions().position(masjid44).title("Masjid Agung Buahbatu").snippet("Jalan Marga Cinta No. 2, Cijaura, Buahbatu, Cijaura, Buahbatu, Garut, Jawa Barat 40287").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition44 = new CameraPosition.Builder().target(masjid44).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition44));

                LatLng masjid45 = new LatLng(-6.918045, 107.612232);
                googleMap.addMarker(new MarkerOptions().position(masjid45).title("Masjid Lautze 2 Bandung").snippet("Jl. Tamblong No.27, Braga, Sumur Bandung, Kota Bandung, Jawa Barat 40111").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition45 = new CameraPosition.Builder().target(masjid45).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition45));

                LatLng masjid46 = new LatLng(-6.948201, 107.715850);
                googleMap.addMarker(new MarkerOptions().position(masjid46).title("Masjid Al Inayah BHC Bandung").snippet("Cibiru Hilir, Cileunyi, Bandung, Jawa Barat 40626").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition46 = new CameraPosition.Builder().target(masjid46).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition46));

                LatLng masjid47 = new LatLng(-6.953345, 107.668312);
                googleMap.addMarker(new MarkerOptions().position(masjid47).title("Masjid Baiturrahman").snippet("Jl. Saturnus Ujung No.26, Manjahlega, Rancasari, Kota Bandung, Jawa Barat 40286").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition47 = new CameraPosition.Builder().target(masjid47).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition47));

                LatLng masjid48 = new LatLng(-6.919332, 107.672977);
                googleMap.addMarker(new MarkerOptions().position(masjid48).title("Masjid Al Syifa Wal Hidayah").snippet("Jalan Atletik I, Sukamiskin, Arcamanik, Sukamiskin, Bandung, Kota Bandung, Jawa Barat 40293").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition48 = new CameraPosition.Builder().target(masjid48).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition48));

                LatLng masjid49 = new LatLng(-6.960941, 107.640784);
                googleMap.addMarker(new MarkerOptions().position(masjid49).title("Masjid Baburrahman").snippet("Komplek Buah Batu Regency, Jl. Terusan Buah Batu, Kujangsari, Bandung, Kota Bandung, Jawa Barat 40287").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition49 = new CameraPosition.Builder().target(masjid49).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition49));

                LatLng masjid50 = new LatLng(-6.923236, 107.671933);
                googleMap.addMarker(new MarkerOptions().position(masjid50).title("Masjid Nurul Hidayah").snippet("Jalan Arcamanik Indah, 02/10, Cisaranten Endah, Arcamanik, Kota Bandung, Jawa Barat 40294").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition50 = new CameraPosition.Builder().target(masjid50).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition50));

                LatLng masjid51 = new LatLng(-6.924222, 107.662477);
                googleMap.addMarker(new MarkerOptions().position(masjid51).title("Masjid Al Muhajirin").snippet("Jl. Jayapura, 05/10, Antapani Kidul, Bandung, Kota Bandung, Jawa Barat 40291").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition51 = new CameraPosition.Builder().target(masjid51).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition51));

                LatLng masjid52 = new LatLng(-6.961025, 107.684769);
                googleMap.addMarker(new MarkerOptions().position(masjid52).title("Masjid Al Wahid").snippet("JL Gedebage, Bandung Inten Indah Blok 04/10, Derwati, Rancasari, Kota Bandung, Jawa Barat 40292").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition52 = new CameraPosition.Builder().target(masjid52).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition52));

                LatLng masjid53 = new LatLng(-6.899464, 107.670013);
                googleMap.addMarker(new MarkerOptions().position(masjid53).title("Masjid Al Hasanah").snippet("Jl. Cikadut 04/12, Karang Pamulang, Bandung, Kota Bandung, Jawa Barat 40195").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition53 = new CameraPosition.Builder().target(masjid53).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition53));

                LatLng masjid54 = new LatLng(-6.863622, 107.589932);
                googleMap.addMarker(new MarkerOptions().position(masjid54).title("Masjid Daarut Tauhiid Bandung").snippet("Jalan Geger Kalong Girang No.40, Isola, Sukasari, Isola, Sukasari, Kota Bandung, Jawa Barat 40154").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition54 = new CameraPosition.Builder().target(masjid54).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition54));

                LatLng masjid55 = new LatLng(-6.917638, 107.646042);
                googleMap.addMarker(new MarkerOptions().position(masjid55).title("Masjid Al Multazam").snippet("JL. Soma, 04/02, Babakan Sari, Kiaracondong, Kota Bandung, Jawa Barat 40283").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition55 = new CameraPosition.Builder().target(masjid55).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition55));

                LatLng masjid56 = new LatLng(-6.907533, 107.724123);
                googleMap.addMarker(new MarkerOptions().position(masjid56).title("Masjid Sabilillah Cilengkrang Bandung").snippet("Jl. Cilengkrang I, Cisurupan, Cibiru, Kota Bandung, Jawa Barat 40614").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition56 = new CameraPosition.Builder().target(masjid56).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition56));

                LatLng masjid57 = new LatLng(-6.889532, 107.664190);
                googleMap.addMarker(new MarkerOptions().position(masjid57).title("Masjid Jami Al Mukhtar, Jatihandap Bandung").snippet("Jl. Jatihandap, Jatihandap, Mandalajati, Bandung, Jawa Barat 40193").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition57 = new CameraPosition.Builder().target(masjid57).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition57));

                LatLng masjid58 = new LatLng(-6.930242, 107.653939);
                googleMap.addMarker(new MarkerOptions().position(masjid58).title("Masjid Al-Hanifah Lemah Hegar,kiaracondong Bandung 40285").snippet("Sukapura, Kiaracondong, Kota Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition58 = new CameraPosition.Builder().target(masjid58).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition58));

                LatLng masjid59 = new LatLng(-6.908564, 107.654113);
                googleMap.addMarker(new MarkerOptions().position(masjid59).title("Mesjid Qaf").snippet("Jalan Kalijati Indah Barat No.8, Antapani Kulon, Antapani, Antapani Kulon, Bandung, Kota Bandung, Jawa Barat 40291").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition59 = new CameraPosition.Builder().target(masjid59).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition59));

                LatLng masjid60 = new LatLng(-6.895416, 107.626754107);
                googleMap.addMarker(new MarkerOptions().position(masjid60).title("Masjid Sabulusalam").snippet("Jalan Gagak, RT. 005/05, Sadang Serang, Bandung, Kota Bandung, Jawa Barat 40133").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition60 = new CameraPosition.Builder().target(masjid60).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition60));

                LatLng masjid61 = new LatLng(-6.812111, 107.618318);
                googleMap.addMarker(new MarkerOptions().position(masjid61).title("Masjid Besar Lembang").snippet("Jl. Raya Lembang No.295, Jayagiri, Lembang, Kabupaten Bandung Barat, Jawa Barat 40153").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition61 = new CameraPosition.Builder().target(masjid61).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition61));

                LatLng masjid62 = new LatLng(-6.946657, 107.626732);
                googleMap.addMarker(new MarkerOptions().position(masjid62).title("Masjid Al-Fajr").snippet("Jl. Cijagra No.39, Cijagra, Lengkong, Kota Bandung, Jawa Barat 40265").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition62 = new CameraPosition.Builder().target(masjid62).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition62));

                LatLng masjid63 = new LatLng(-6.997786, 107.568761);
                googleMap.addMarker(new MarkerOptions().position(masjid63).title("Masjid Al Muhajirin").snippet("Gading Junti Asri, Sangkanhurip, Katapang, Bandung, Jawa Barat 40921").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition63 = new CameraPosition.Builder().target(masjid63).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition63));

                LatLng masjid64 = new LatLng(-6.958642, 107.659700);
                googleMap.addMarker(new MarkerOptions().position(masjid64).title("Masjid Cijawura").snippet("Jl. Rancabolang, Margasari, Buahbatu, Kota Bandung, Jawa Barat 40286").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition64 = new CameraPosition.Builder().target(masjid64).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition64));

                LatLng masjid65 = new LatLng(-6.898979, 107.697008);
                googleMap.addMarker(new MarkerOptions().position(masjid65).title("Masjid Baitul Mu'min").snippet("Jalan Sekehaji Blok G, RW. 15, Jatiendah, Cilengkrang, Jatiendah, Cilengkrang, Bandung, Jawa Barat 40616").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition65 = new CameraPosition.Builder().target(masjid65).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition65));

                LatLng masjid66 = new LatLng(-6.940326, 107.644415);
                googleMap.addMarker(new MarkerOptions().position(masjid66).title("Masjid Nurul Hakim").snippet("Jl. Sekejati No.22, Kb. Kangkung, Kiaracondong, Kota Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition66 = new CameraPosition.Builder().target(masjid66).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition66));

                LatLng masjid67 = new LatLng(-6.931267, 107.639790);
                googleMap.addMarker(new MarkerOptions().position(masjid67).title("Masjid Al Latief").snippet("Jl. Maler IV, 03/02, Gumuruh, Batununggal, Kota Bandung, Jawa Barat 40274").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition67 = new CameraPosition.Builder().target(masjid67).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition67));

                LatLng masjid68 = new LatLng(-6.930250, 107.653966);
                googleMap.addMarker(new MarkerOptions().position(masjid68).title("Masjid Al-Hanifah ").snippet("Sukapura, Kiaracondong, Kota Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition68 = new CameraPosition.Builder().target(masjid68).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition68));

                LatLng masjid69 = new LatLng(-6.933965, 107.623818);
                googleMap.addMarker(new MarkerOptions().position(masjid69).title("Masjid AHMAD DAHLAN").snippet("Jl. K.H. Ahmad Dahlan No.59, Turangga, Lengkong, Kota Bandung, Jawa Barat 40264").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition69 = new CameraPosition.Builder().target(masjid69).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition69));

                LatLng masjid70 = new LatLng(-6.941118, 107.649254);
                googleMap.addMarker(new MarkerOptions().position(masjid70).title("Masjid Al-Muamalah").snippet("Sukapura, Kiaracondong, Kota Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition70 = new CameraPosition.Builder().target(masjid70).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition70));

                LatLng masjid71 = new LatLng(-6.940328, 107.644428);
                googleMap.addMarker(new MarkerOptions().position(masjid71).title("Masjid Nurul Hakim").snippet("Jl. Sekejati No.22, Kb. Kangkung, Kiaracondong, Kota Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition71 = new CameraPosition.Builder().target(masjid71).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition71));

                LatLng masjid72 = new LatLng(-6.934491, 107.592350);
                googleMap.addMarker(new MarkerOptions().position(masjid72).title("Masjid Al-Bayyinah").snippet("Jl. Peta No.154, Suka Asih, Bojongloa Kaler, Kota Bandung, Jawa Barat 40233").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition72 = new CameraPosition.Builder().target(masjid72).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition72));

                LatLng masjid73 = new LatLng(-6.968053, 107.624321);
                googleMap.addMarker(new MarkerOptions().position(masjid73).title("Masjid Al-Khoer").snippet("Jl. Mengger Hilir No.24, Sukapura, Dayeuhkolot, Bandung, Jawa Barat 40267").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition73 = new CameraPosition.Builder().target(masjid73).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition73));

                LatLng masjid74 = new LatLng(-6.977597, 107.769524);
                googleMap.addMarker(new MarkerOptions().position(masjid74).title("Masjid Ar-Rozaq").snippet("Jelegong, Rancaekek, Bandung, Jawa Barat 40394").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition74 = new CameraPosition.Builder().target(masjid74).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition74));

                LatLng masjid75 = new LatLng(-6.977235, 107.769183);
                googleMap.addMarker(new MarkerOptions().position(masjid75).title("Masjid Al Hikmah").snippet("Jelegong, Rancaekek, Bandung, Jawa Barat 40394").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition75 = new CameraPosition.Builder().target(masjid75).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition75));

                LatLng masjid76 = new LatLng(-6.977600, 107.769513);
                googleMap.addMarker(new MarkerOptions().position(masjid76).title("Masjid Al-Furqon").snippet("JL. Papanggungan, 02/09, Jelegong, Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition76 = new CameraPosition.Builder().target(masjid76).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition76));

                LatLng masjid77 = new LatLng(-6.973345, 107.775495);
                googleMap.addMarker(new MarkerOptions().position(masjid77).title("Masjid Al-Hidayah").snippet("KP. Bojong Melati, Rancaekek, Jelegong, Rancaekek, Bandung, Jawa Barat 40394").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition77 = new CameraPosition.Builder().target(masjid77).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition77));

                LatLng masjid78 = new LatLng(-7.005264, 107.609382);
                googleMap.addMarker(new MarkerOptions().position(masjid78).title("Masjid At-Tauhid").snippet("Malakasari, Baleendah, Bandung, Jawa Barat 40375").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition78 = new CameraPosition.Builder().target(masjid78).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition78));

                LatLng masjid79 = new LatLng(-6.899106, 107.607975);
                googleMap.addMarker(new MarkerOptions().position(masjid79).title("Masjid Al-Islam").snippet("Jl. Kebon Bibit No.12, Tamansari, Bandung Wetan, Kota Bandung, Jawa Barat 40116").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition79 = new CameraPosition.Builder().target(masjid79).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition79));

                LatLng masjid80 = new LatLng(-6.916888, 107.633461);
                googleMap.addMarker(new MarkerOptions().position(masjid80).title("Masjid Ar-Risalah").snippet("Jl. Cianjur No.2, Kacapiring, Batununggal, Kota Bandung, Jawa Barat 40271").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition80 = new CameraPosition.Builder().target(masjid80).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition80));

                LatLng masjid81 = new LatLng(-7.114678, 107.769017);
                googleMap.addMarker(new MarkerOptions().position(masjid81).title("Masjid Miftahul Haq").snippet("Ibun, Bandung, Jawa Barat 40384").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition81 = new CameraPosition.Builder().target(masjid81).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition81));

                LatLng masjid82 = new LatLng(-6.876140, 107.617866);
                googleMap.addMarker(new MarkerOptions().position(masjid82).title("Masjid Ar-Rohman").snippet("JL. Ir.H. Juanda, RT. 03/01, Dago, Coblong, Kota Bandung, Jawa Barat 40135").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition82 = new CameraPosition.Builder().target(masjid82).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition82));

                LatLng masjid83 = new LatLng(-6.937447, 107.640266);
                googleMap.addMarker(new MarkerOptions().position(masjid83).title("Masjid Al-Litifurrohman").snippet("Jl. Binong Jati No.152, Binong, Batununggal, Kota Bandung, Jawa Barat 40275").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition83 = new CameraPosition.Builder().target(masjid83).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition83));

                LatLng masjid84 = new LatLng(-6.928444, 107.673790);
                googleMap.addMarker(new MarkerOptions().position(masjid84).title("Masjid Asy-Syifa").snippet("JL. Cingised, 01/06, Cisaranten Endah, Arcamanik, Kota Bandung, Jawa Barat 40293").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition84 = new CameraPosition.Builder().target(masjid84).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition84));

                LatLng masjid85 = new LatLng(-6.917998, 107.675093);
                googleMap.addMarker(new MarkerOptions().position(masjid85).title("Masjid Al-Hidayah").snippet("Jl. Arcamanik Endah, Sukamiskin, Arcamanik, Kota Bandung, Jawa Barat 40293").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition85 = new CameraPosition.Builder().target(masjid85).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition85));

                LatLng masjid86 = new LatLng(-6.924382, 107.610395);
                googleMap.addMarker(new MarkerOptions().position(masjid86).title("Masjid Al-Mubin").snippet("JL. Pangarang Dalam, RT. 03/09, Cikawao, Lengkong, Kota Bandung, Jawa Barat 40261").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition86 = new CameraPosition.Builder().target(masjid86).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition86));

                LatLng masjid87 = new LatLng(-6.910056, 107.657775);
                googleMap.addMarker(new MarkerOptions().position(masjid87).title("Masjid Al-Basits").snippet("Jl. Sukapura No.06/01, Antapani Kulon, Antapani, Kota Bandung, Jawa Barat 40291").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition87 = new CameraPosition.Builder().target(masjid87).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition87));

                LatLng masjid88 = new LatLng(-6.925689, 107.609603);
                googleMap.addMarker(new MarkerOptions().position(masjid88).title("Masjid As-Salam").snippet("JL. Sasakgantung, RT. 05/04, Balonggede, Regol, Kota Bandung, Jawa Barat 40251").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition88 = new CameraPosition.Builder().target(masjid88).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition88));

                LatLng masjid89 = new LatLng(-6.927119, 107.605518);
                googleMap.addMarker(new MarkerOptions().position(masjid89).title("Masjid Nurul Hidayah").snippet("Jl. Raden Dewi Sartika No.87, Balonggede, Regol, Kota Bandung, Jawa Barat 40251").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition89 = new CameraPosition.Builder().target(masjid89).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition89));

                LatLng masjid90 = new LatLng(-6.914730, 107.609812);
                googleMap.addMarker(new MarkerOptions().position(masjid90).title("Masjid Darul Hasanah	").snippet("Jl. Merdeka No.18-20, Babakan Ciamis, Sumur Bandung, Kota Bandung, Jawa Barat 40117").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition90 = new CameraPosition.Builder().target(masjid90).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition90));

                LatLng masjid91 = new LatLng(-6.889178, 107.629334);
                googleMap.addMarker(new MarkerOptions().position(masjid91).title("Masjid Al-Khoeriyah").snippet("Jl. Cibeunying Kolot No.28, Cigadung, Cibeunying Kaler, Kota Bandung, Jawa Barat 40133").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition91 = new CameraPosition.Builder().target(masjid91).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition91));

                LatLng masjid92 = new LatLng(-6.887691, 107.625752);
                googleMap.addMarker(new MarkerOptions().position(masjid92).title("Masjid Al-Ikhlas").snippet("Sekeloa, Coblong, Kota Bandung, Jawa Barat 40133").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition92 = new CameraPosition.Builder().target(masjid92).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition92));

                LatLng masjid93 = new LatLng(-6.879623, 107.627854);
                googleMap.addMarker(new MarkerOptions().position(masjid93).title("Masjid At-Taqwa").snippet("Jl. Alfa, Cigadung, Cibeunying Kaler, Kota Bandung, Jawa Barat 40191").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition93 = new CameraPosition.Builder().target(masjid93).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition93));

                LatLng masjid94 = new LatLng(-6.939176, 107.622664);
                googleMap.addMarker(new MarkerOptions().position(masjid94).title("Masjid Darussalam").snippet("Jl. Biduri No.15, Cijagra, Lengkong, Kota Bandung, Jawa Barat 40265").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition94 = new CameraPosition.Builder().target(masjid94).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition94));

                LatLng masjid95 = new LatLng(-6.932831, 107.658676);
                googleMap.addMarker(new MarkerOptions().position(masjid95).title("Masjid Baitul Janah").snippet("Komplek Taman Raflesia, Jl. Kawaluyaan, Kiara Condong, Sukapura, Kiaracondong, Kota Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition95 = new CameraPosition.Builder().target(masjid95).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition95));

                LatLng masjid96 = new LatLng(-6.937873, 107.652995);
                googleMap.addMarker(new MarkerOptions().position(masjid96).title("Masjid Miftahul Jannah").snippet("Jalan Komplek KPAD Pindad Timur No.71, Sukapura, Kiaracondong, Kota Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition96 = new CameraPosition.Builder().target(masjid96).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition96));

                LatLng masjid97 = new LatLng(-6.929511, 107.658380);
                googleMap.addMarker(new MarkerOptions().position(masjid97).title("Masjid Darut Taufiq").snippet("Lemah Hegar RT.13/RW.04, Sukapura, Kiaracondong, Sukapura, Kiaracondong, Kota Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition97 = new CameraPosition.Builder().target(masjid97).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition97));

                LatLng masjid98 = new LatLng(-6.930242, 107.653942);
                googleMap.addMarker(new MarkerOptions().position(masjid98).title("Masjid Aahl-Hanif").snippet("Sukapura, Kiaracondong, Kota Bandung, Jawa Barat 40285").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition98 = new CameraPosition.Builder().target(masjid98).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition98));

                LatLng masjid99 = new LatLng(-6.936987, 107.655268);
                googleMap.addMarker(new MarkerOptions().position(masjid99).title("Masjid Al-Muhajirin").snippet("Jl. Kawaluyaan II, Jatisari, Buahbatu, Kota Bandung, Jawa Barat 40286").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition99 = new CameraPosition.Builder().target(masjid99).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition99));

                LatLng masjid100 = new LatLng(-7.008360, 107.657383);
                googleMap.addMarker(new MarkerOptions().position(masjid100).title("Masjid Matlaulhuda").snippet("Manggahang, Baleendah, Bandung, Jawa Barat 40375").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_masjid2)));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition100 = new CameraPosition.Builder().target(masjid100).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition100));








            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkPermission()) {
            Log.e("Permission: ", "Granted");
        } else {
            Log.e("Permission: ", "DENIED");
        }

        mGoogleApiClient.connect();
    }

    private boolean checkPermission() {

        int result;

        List<String> ListPermission = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                ListPermission.add(p);
            }
        }

        if (!ListPermission.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), ListPermission.toArray(
                    new String[ListPermission.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //KODINGAN AWAL
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        /*if (mLastLocation != null)
        {
            LatLng latLng = new LatLng(mLastLocation.getAltitude(), mLastLocation.getLongitude());

            //FUNGSI UNTUK MENAMBAHKAN MARKER PADA MAPS
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            googleMap.addMarker(markerOptions);
        }
*/

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permissions: ", "Granted");
                } else {
                    Log.e("Permissions: ", "DENIED");
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap mMap) {

        googleMap = mMap;

        //UNTUK PENAMBAHAN TEMPAT SECARA TEMBAK

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

    }
}
