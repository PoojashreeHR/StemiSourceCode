package com.stemi.stemiapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.MapsActivity;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.BetterSpinner;
import com.stemi.stemiapp.databases.UserDetailsTable;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Pooja on 24-07-2017.
 */

public class SOSFragment extends Fragment implements View.OnClickListener, TrackActivity.OnBackPressedListener,AppConstants {
    @BindView(R.id.bt_share_location)
    Button shateLocation;
    @BindView(R.id.rl_call)
    RelativeLayout rlCall;
    @BindView(R.id.rl_locateMap)
    RelativeLayout rlLocateMap;

    AppSharedPreference appSharedPreference;
    UserDetailsTable dBforUserDetails;
    BetterSpinner personSpinner;
    ArrayList<String> personName;
    private ImageView pick_location;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SOSFragment";
    int PLACE_PICKER_REQUEST = 1;
    private EditText etLocation;
    String selectedPersonName = null;
    private double latitude, longitude;
    GoogleMap googleMap;

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
        ButterKnife.bind(this, view);

        rlCall.setOnClickListener(this);
        rlLocateMap.setOnClickListener(this);
        shateLocation.setOnClickListener(this);
        // ((TrackActivity) getActivity()).setActionBarTitle("SOS");
        appSharedPreference = new AppSharedPreference(getActivity());
        personSpinner = (BetterSpinner) view.findViewById(R.id.person_Spinner);
        //pick_location = (ImageView) view.findViewById(R.id.pick_location);
        etLocation = (EditText) view.findViewById(R.id.et_location);
        dBforUserDetails = new UserDetailsTable(getActivity());
        personName = new ArrayList<>();
        personName = dBforUserDetails.getRecords();

        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationServices();
            }
        });
        ((TrackActivity) getActivity()).getViewPager().setPagingEnabled(false);

        ((TrackActivity) getActivity()).setOnBackPressedListener(this);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, personName);
            personSpinner.setAdapter(arrayAdapter);
            if(personName.size() == 1){
                selectedPersonName = personName.get(0).toString();
                personSpinner.setText(selectedPersonName);
            }

            personSpinner.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    selectedPersonName = personSpinner.getText().toString();
                    Log.e("TAG", "Text is :" + personSpinner.getText().toString());
                }
            });
        Log.e(TAG, "onCreateView: SOS Fragment" + dBforUserDetails.getRecords());
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (getActivity() != null) {
            if (menuVisible) {
                ((TrackActivity) getActivity()).setActionBarTitle("SOS");

            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;

                String toastMsg = String.format("Place: %s", place.getAddress());
                etLocation.setText(place.getAddress());
            }
        }
    }

    private void openPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            if (checkPlayServices()) {
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

    private void checkLocationServices() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
            openPlacePicker();
        }
    }

    public boolean ValidateFields() {
        Boolean valid = true;

        String location = etLocation.getText().toString();
        if(selectedPersonName == null || selectedPersonName.equals("")){
            valid = false;
            Toast.makeText(getActivity(), "Select person name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(location)){
            valid = false;
            Toast.makeText(getActivity(), "Select Location", Toast.LENGTH_SHORT).show();        }

        return valid;
    }

    @Override
    public void onResume() {
        super.onResume();
        //((TrackActivity) getActivity()).setActionBarTitle("SOS");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_share_location:
                if (ValidateFields()) {
                    PackageManager manager = getContext().getPackageManager();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);


                    String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "Location" +  ")" ;

                    //String shareBodyText = "Hi pooja";
                    String shareBodyText =  selectedPersonName + " has chest pain and might be suffering from a heart attack.Please send help to "+etLocation.getText().toString()+"\n\n"+ geoUri;
                    //   shareBodyText.append(Uri.parse(uri));
                    /*SmsManager smsManager = SmsManager.getDefault();
                    String destination = "https://www.google.co.in/";
                    smsManager.sendTextMessage(destination, null, shareBodyText, null, null);
*/
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);




/*

                    String SMS_SENT = "SMS_SENT";
                    String SMS_DELIVERED = "SMS_DELIVERED";

                    PendingIntent sentPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SMS_SENT), 0);
                    PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SMS_DELIVERED), 0);

                    ArrayList<String> smsBodyParts = smsManager.divideMessage(shareBodyText);
                    ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
                    ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();

                    for (int i = 0; i < smsBodyParts.size(); i++) {
                        sentPendingIntents.add(sentPendingIntent);
                        deliveredPendingIntents.add(deliveredPendingIntent);
                    }

// For when the SMS has been sent
                    getActivity().registerReceiver(new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            switch (getResultCode()) {
                                case Activity.RESULT_OK:
                                    Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                    Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmsManager.RESULT_ERROR_NO_SERVICE:
                                    Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmsManager.RESULT_ERROR_NULL_PDU:
                                    Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmsManager.RESULT_ERROR_RADIO_OFF:
                                    Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }, new IntentFilter(SMS_SENT));
*/


                    if (sharingIntent.resolveActivity(manager) != null) {
                        startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                    } else {
                        Log.d(TAG, "No Intent available to handle action");
                    }
                }
                break;

            case R.id.rl_call:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                if(appSharedPreference.getAmbulanceNum(AMBULANCE_NUMB) != ""
                        || appSharedPreference.getAmbulanceNum(AMBULANCE_NUMB) == null){
                    String call = appSharedPreference.getAmbulanceNum(AMBULANCE_NUMB);
                    callIntent.setData(Uri.parse("tel:"+call));
                }else {
                    callIntent.setData(Uri.parse("tel:"));
                }

                startActivity(callIntent);
/*                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:108"));
                startActivity(intent);*/
                break;

            case R.id.rl_locateMap:

                String location = etLocation.getText().toString();
                if(TextUtils.isEmpty(location)){
                    etLocation.setError("Location is required");
                }else {
                    Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
                    mapIntent.putExtra("lat", latitude);
                    mapIntent.putExtra("lon", longitude);
                    startActivity(mapIntent);
                    break;
                }
        }
    }

    @Override
    public void doBack() {
        EventBus.getDefault().post(new MessageEvent("Hello!"));
        View v = TrackActivity.tabLayout.getTabAt(2).getCustomView();
        if (v instanceof TextView) {
            TextView textView = (TextView) v;
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            for (Drawable drawable : textView.getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.clearColorFilter();
                }
                //drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.appBackground), PorterDuff.Mode.SRC_IN));
            }
        }
        getActivity().finish();
    }
}
