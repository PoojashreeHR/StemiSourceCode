package com.stemi.stemiapp.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.MapsActivity;
import com.stemi.stemiapp.activity.TrackActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pooja on 24-07-2017.
 */

public class HospitalFragment extends Fragment {
    private LocationManager lm;
    private LatLng currentLoc;

    public HospitalFragment() {
        // Required empty public constructor
    }


    RelativeLayout locateHospitalLayout;


    RelativeLayout uploadDataLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hospital, container, false);
        //ButterKnife.bind(getActivity());

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 500, new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                Log.e("MapsActivity","location changed "+location.getLatitude()+" "+location.getLongitude());
//                currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String s) {
//
//            }
//

        locateHospitalLayout = (RelativeLayout) view.findViewById(R.id.rl_locate_hosp);
        locateHospitalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationServices();
            }
        });

        return view;
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if (getActivity() != null) {
            if (menuVisible) {
                ((TrackActivity) getActivity()).setActionBarTitle("Hospital");

            }
        }
        super.setMenuVisibility(menuVisible);
    }

    private void checkLocationServices() {

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog;
            // dialog = new AlertDialog.Builder(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                dialog = new AlertDialog.Builder(getActivity());
            }
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        } else {
            openMapsActivity(gps_enabled, network_enabled);
        }
    }

    private void openMapsActivity(boolean gps_enabled, boolean network_enabled) {
        Location currentLoc = null;
//        if (gps_enabled) {
//            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
//                    PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            currentLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        } else {
//            currentLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        }
        currentLoc = getLastKnownLocation();
        Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
        mapIntent.putExtra("lat", currentLoc.getLatitude());
        mapIntent.putExtra("lon", currentLoc.getLongitude());
        startActivity(mapIntent);
    }

    private Location getLastKnownLocation() {

        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return;
            }
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}
