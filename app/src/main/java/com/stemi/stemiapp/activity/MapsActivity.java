package com.stemi.stemiapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.loaders.HospitalsLoader;
import com.stemi.stemiapp.model.apiModels.Hospital;
import com.stemi.stemiapp.model.apiModels.NearestHospitalResponse;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<NearestHospitalResponse>,
        LocationListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private double latitude, longitude;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        latitude = getIntent().getDoubleExtra("lat", 0.0);
        longitude = getIntent().getDoubleExtra("lon", 0.0);
        token = new AppSharedPreference(this).getUserToken(AppConstants.USER_TOKEN);

        Log.e(TAG, "latitude = " + latitude);
        Log.e(TAG, "longitude = " + longitude);
        Log.e(TAG, "token = " + token);

        if (latitude != 0.0 && longitude != 0.0) {
            getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        moveToCurrentLoc();
    }

    private void moveToCurrentLoc() {
        LatLng currentLoc = new LatLng(latitude, longitude);
        BitmapDescriptor currentLocIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location);
        mMap.addMarker(new MarkerOptions().position(currentLoc).icon(currentLocIcon));
        Log.e(TAG, "Moving to current location");
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }


    @Override
    public Loader<NearestHospitalResponse> onCreateLoader(int i, Bundle bundle) {
        Log.e(TAG, "Calling loader - HospitalsLoader");
        return new HospitalsLoader(this, token, latitude, longitude);
    }

    @Override
    public void onLoadFinished(Loader<NearestHospitalResponse> loader, NearestHospitalResponse hospitals) {
        if (hospitals != null) {
            if (hospitals.getStatus().equalsIgnoreCase("success")) {
                Log.e(TAG, "hospitals.size() = " + hospitals.getHospitalList().size());
                for (Hospital hospital : hospitals.getHospitalList()) {
                    LatLng latLng = new LatLng(hospital.getLatitude(), hospital.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(hospital.getHospitalName());
                    markerOptions.snippet(hospital.getHospitalAddress());
                    mMap.addMarker(markerOptions);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<NearestHospitalResponse> loader) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "location = " + new Gson().toJson(location));
        Log.e(TAG, "latitude = " + latitude + " longitude = " + longitude);
        if (location != null) {
            if (latitude == 0.0 || longitude == 0.0) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                moveToCurrentLoc();
                getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
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
}
