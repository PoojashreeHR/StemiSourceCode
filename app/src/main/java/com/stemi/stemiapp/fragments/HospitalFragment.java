package com.stemi.stemiapp.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.FollowupActivity;
import com.stemi.stemiapp.activity.MapsActivity;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.databases.DataUploadedDB;
import com.stemi.stemiapp.databases.FollowupsDB;
import com.stemi.stemiapp.databases.UserDetailsTable;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.model.apiModels.StatusMessageResponse;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.rest.ApiClient;
import com.stemi.stemiapp.rest.ApiInterface;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.GlobalClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pooja on 24-07-2017.
 */

public class HospitalFragment extends Fragment implements TrackActivity.OnScanCompletionListener {
    private static final String TAG = "HospitalFragment";
    private LocationManager lm;
    private LatLng currentLoc;
    private IntentIntegrator qrScan;
    private ApiInterface apiInterface;
    private RelativeLayout followupLayout;

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

        locateHospitalLayout = (RelativeLayout) view.findViewById(R.id.rl_locate_hosp);
        locateHospitalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationServices();
            }
        });

        uploadDataLayout = (RelativeLayout) view.findViewById(R.id.rl_upload_data);
        uploadDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataUploadedDB dataUploadedDB = new DataUploadedDB(getActivity());
                if (GlobalClass.userID == null) {
                    Toast.makeText(getActivity(), "You've to create a profile first !", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean uploadStatus = dataUploadedDB.getUploadStatus(GlobalClass.userID);
                if (!uploadStatus) {
                    openQRScanningActivity();
                } else {
                    Toast.makeText(getActivity(), "You've already uploaded data !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        followupLayout = (RelativeLayout) view.findViewById(R.id.rl_followup);
        followupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextFollowupDate();
            }
        });

        qrScan = new IntentIntegrator(getActivity());


        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        return view;
    }

    private void openQRScanningActivity() {
        qrScan.initiateScan();
    }

    boolean isVisible;

    @Override
    public void onResume() {
        super.onResume();
       // if (isVisible)
            //((TrackActivity) getActivity()).setActionBarTitle("Hospital");
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if (getActivity() != null) {
            if (menuVisible) {
                isVisible = true;
                ((TrackActivity) getActivity()).setActionBarTitle("Hospital");

            }
        }
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
        currentLoc = getLastKnownLocation();
        Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
        //mapIntent.putExtra("lat", currentLoc.getLatitude());
        //mapIntent.putExtra("lon", currentLoc.getLongitude());
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((TrackActivity) context).setOnScanCompletedListener(this);
    }

    @Override
    public void scanCompleted(String contents) {
        Toast.makeText(getActivity(), "Hospital id = " + contents, Toast.LENGTH_SHORT).show();
        Log.e("MapsActivity", "contents = " + contents);
        CommonUtils.showLoadingProgress(getActivity());
        //contents = "153";
        AppSharedPreference appSharedPreference = new AppSharedPreference(getActivity());
        String token = appSharedPreference.getUserToken(AppConstants.USER_TOKEN);

        UserDetailsTable userDetailTable = new UserDetailsTable(getActivity());
        RegisteredUserDetails userDetails = userDetailTable.getUserDetails(GlobalClass.userID);

        Log.e("MapsActivity", "token = " + token);
        Call<StatusMessageResponse> apiCall = apiInterface.callUploadDataApi(token, contents,
                userDetails.getName(),
                userDetails.getAge(),
                userDetails.getGender(),
                userDetails.getPhone(),
                userDetails.getAddress(),
                userDetails.getHeight(),
                userDetails.getWeight(),
                userDetails.getWaist(),
                userDetails.getDo_you_smoke(),
                userDetails.getHeart_attack(),
                userDetails.getDiabetes(),
                userDetails.getBlood_pressure(),
                userDetails.getCholesterol(),
                userDetails.getHad_paralytic_stroke(),
                userDetails.getHave_asthma(),
                userDetails.getFamily_had_heart_attack());

        apiCall.enqueue(new Callback<StatusMessageResponse>() {
            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                Log.e("MapsActivity", new Gson().toJson(response.body()));
                CommonUtils.hideLoadingProgress();
                Toast.makeText(getActivity(), "User Data Uploaded !", Toast.LENGTH_SHORT).show();
                setFollowupDates();
                updateUserDataUploaded();
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                CommonUtils.hideLoadingProgress();
                Log.e("MapsActivity", t.getMessage());
                Toast.makeText(getActivity(), "Data Upload failed !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserDataUploaded() {
        DataUploadedDB dataUploadedDB = new DataUploadedDB(getActivity());
        dataUploadedDB.addEntry(GlobalClass.userID, true);
    }

    private void showNextFollowupDate() {
        FollowupsDB followupsDB = new FollowupsDB(getActivity());
        String nextDate = followupsDB.getNextFollowupDate(GlobalClass.userID);

        if (nextDate == null) {
            Toast.makeText(getActivity(), "You've to upload your data first !!!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), FollowupActivity.class);
            startActivity(intent);
        }
    }

    private void setFollowupDates() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        addEvent(calendar.getTime());

        calendar.add(Calendar.MONTH, 1);
        addEvent(calendar.getTime());

        Calendar calendarSecondary = Calendar.getInstance();
        for (int i = 1; i <= 20; i++) {
            calendarSecondary.add(Calendar.MONTH, 3);
            addEvent(calendarSecondary.getTime());
        }


    }

    private void addEvent(Date date) {
        ContentResolver cr = getActivity().getContentResolver();
        Uri eventUriString = Uri.parse("content://com.android.calendar/events");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String datePart = simpleDateFormat.format(date);

        String dateTime = datePart + " 09:00 AM";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        Date modifiedDate = null;
        try {
            modifiedDate = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ContentValues eventValues = new ContentValues();

        eventValues.put(CalendarContract.Events.CALENDAR_ID, 1); // id, We need to choose from our mobile for primary its 1
        eventValues.put(CalendarContract.Events.TITLE, "Check-up");
        eventValues.put(CalendarContract.Events.DESCRIPTION, "You have a follow up checkup scheduled today");

        eventValues.put(CalendarContract.Events.DTSTART, modifiedDate.getTime());
        long endTime = modifiedDate.getTime() + (60 * 60 * 1000);
        eventValues.put(CalendarContract.Events.DTEND, endTime);

        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true

        Uri eventUri = cr.insert(eventUriString, eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        ContentValues reminders = new ContentValues();
        reminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        reminders.put(CalendarContract.Reminders.MINUTES, 30);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

        FollowupsDB followupsDB = new FollowupsDB(getActivity());
        followupsDB.addEntry(GlobalClass.userID, modifiedDate);

    }
}
