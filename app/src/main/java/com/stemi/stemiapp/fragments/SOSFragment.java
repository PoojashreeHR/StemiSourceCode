package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.BetterSpinner;
import com.stemi.stemiapp.databases.DBforUserDetails;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Pooja on 24-07-2017.
 */

public class SOSFragment extends Fragment {
    DBforUserDetails dBforUserDetails;
    BetterSpinner personSpinner;
    ArrayList<String> personName;
    private ImageView pick_location;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SOSFragment";
    int PLACE_PICKER_REQUEST = 1;
    private EditText etLocation;

    public SOSFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sos, container, false);
        personSpinner = (BetterSpinner) view.findViewById(R.id.person_Spinner);
        //pick_location = (ImageView) view.findViewById(R.id.pick_location);
        etLocation = (EditText)view.findViewById(R.id.et_location);
        dBforUserDetails = new DBforUserDetails(getActivity());
        personName = new ArrayList<>();
        personName = dBforUserDetails.getRecords();

        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationServices();
            }
        });
        ((TrackActivity)getActivity()).getViewPager().setPagingEnabled(true);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, personName );
        personSpinner.setAdapter(arrayAdapter);

        Log.e(TAG, "onCreateView: SOS Fragment"+ dBforUserDetails.getRecords() );
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if(getActivity() != null){
            if(menuVisible){
                ((TrackActivity) getActivity()).setActionBarTitle("SOS");

            }
        }
        super.setMenuVisibility(menuVisible);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getAddress());
                etLocation.setText(place.getAddress());
            }
        }
    }

    private void openPlacePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            if(checkPlayServices()){
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            }

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                //finish();
            }
            return false;
        }
        return true;
    }

    private void checkLocationServices(){
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
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
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
        }
        else{
            openPlacePicker();
        }
    }
}
