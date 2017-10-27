package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.AnswerTemplateView;
import com.stemi.stemiapp.databases.DBForTrackActivities;
import com.stemi.stemiapp.databases.TrackSmokingDB;
import com.stemi.stemiapp.model.DataSavedEvent;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.SetTimeEvent;
import com.stemi.stemiapp.model.TrackSmoking;
import com.stemi.stemiapp.model.UserEventDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pooja on 26-07-2017.
 */

public class SmokingFragment  extends Fragment implements TrackActivity.OnBackPressedListener {
    private static final String TAG = "SMOKE FRAGMENT";
    @BindView(R.id.tv_smoke_today) TextView tvSmokeToday;
    @BindView(R.id.smokeToday) AnswerTemplateView smokeToday;
    @BindView(R.id.tv_howMany) EditText howMany;
    @BindView(R.id.smoking_save)
    Button btnSmokingSave;
    AppSharedPreference appSharedPreference;

    DBForTrackActivities dbForTrackActivities;

    String savedDate;
    String responseChange;
    private boolean alreadySaved;
    private TrackSmoking trackSmoking;

    // AnswerTemplateView answerTemplateView;
    public SmokingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_smoke, container, false);
        ButterKnife.bind(this,view);
        dbForTrackActivities = new DBForTrackActivities();
        appSharedPreference = new AppSharedPreference(getActivity());

        CommonUtils.setRobotoRegularFonts(getActivity(),tvSmokeToday);
        smokeToday.setResponseChangedListener(new AnswerTemplateView.ResponseChangedListener() {
            @Override
            public void onResponse(String response) {
                responseChange = smokeToday.getResponse();
                Log.e(TAG, "onResponse: "+response );
                if(smokeToday.getResponse().equals("YES")){
                    howMany.setEnabled(true);
                    howMany.setAlpha(1f);
                }else {
                    howMany.setEnabled(false);
                    howMany.setAlpha(.6f);
                }
            }
        });


        tvSmokeToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Picker Dialog");
            }
        });

        ((TrackActivity) getActivity()).setOnBackPressedListener(this);
        ((TrackActivity) getActivity()).setActionBarTitle("Smoking");

        alreadySaved = false;

        btnSmokingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalClass.userID != null) {
                    if (smokeToday.getResponse() == null || smokeToday.getResponse().equals("")) {
                        EventBus.getDefault().post(new MessageEvent("Hello!"));
                        //   ((TrackActivity) getActivity()).setActionBarTitle("Track");
                    } else if (howMany.isEnabled() && howMany.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please enter how many", Toast.LENGTH_LONG).show();
                    } else {
                        saveData();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please add profile details first", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(SmokingFragment.this).commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TrackActivity) getActivity()).setActionBarTitle("Smoking");
        callSavedMethod();
    }

    @Override
    public void doBack() {
        EventBus.getDefault().post(new MessageEvent("Hello!"));
    }

    @Override
    public void onStart() {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.e("fragment", "SmokingFragment onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.e("fragment", "SmokingFragment onDestroy()");
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetTimeEvent(SetTimeEvent event){
        tvSmokeToday.setText(event.getDate());
        callSavedMethod();
    }

    public void saveAllData() {
        if(GlobalClass.userID !=null) {
            if (!alreadySaved) {
                if (smokeToday.getResponse() == null || smokeToday.getResponse().equals("")) {
                    alreadySaved = false;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                }
                Log.e("fragment", "SmokingFragment saveAllData()");
                if (howMany.isEnabled() && howMany.getText().toString().equals("")) {
                    alreadySaved = false;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                }else {
                    saveData();
                }
                //saveData();
                alreadySaved = true;
            }
        }
        else{
           // Toast.makeText(getActivity(), "Please add profile details first", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveUserData() {
        TrackSmokingDB trackSmokingDB = new TrackSmokingDB(getActivity());
        trackSmokingDB.addEntry(trackSmoking);
    }

    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);
            datepickerdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            if(view.isShown()) {
                Date parseDate = null;
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                cal.set(Calendar.MONTH, (month));
                String month_name = month_date.format(cal.getTime());

                Log.e("", "" + month_name);

                String date1 = day + " " + month_name + " " + year;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                try {
                    parseDate = dateFormat.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String stDate = dateFormat.format(parseDate); //2016/11/16 12:08:43
                Log.e("Comparing Date :", " Your date is correct");
                EventBus.getDefault().post(new SetTimeEvent(0, stDate));
            }
//            tvSmokeToday.setText(stDate);
//            callSavedMethod();
        }
    }

    public void saveData() {
        TrackActivity.userEventDetails.setUid(GlobalClass.userID);
        TrackSmokingDB trackSmokingDB = new TrackSmokingDB(getActivity());
        trackSmoking = new TrackSmoking();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        trackSmoking.setUserId(GlobalClass.userID);
        if (tvSmokeToday.getText().equals("Today  ")){
            Date dt = new Date();
            // set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            TrackActivity.userEventDetails.setDate(todaysDate);
            trackSmoking.setDateTime(dt);
        }else {
            try {
                Date selectedDate = dateFormat.parse(tvSmokeToday.getText().toString());
                trackSmoking.setDateTime(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TrackActivity.userEventDetails.setDate(tvSmokeToday.getText().toString());
        }
        TrackActivity.userEventDetails.setSmokeToday(responseChange);


        if(responseChange.equals("NO")){
            TrackActivity.userEventDetails.setHowMany("0");
            trackSmoking.setSmoked(false);
        }else {
            TrackActivity.userEventDetails.setHowMany(howMany.getText().toString());
            trackSmoking.setSmoked(true);
        }


        EventBus.getDefault().post(new DataSavedEvent(""));

        boolean date = dbForTrackActivities.getDate(TrackActivity.userEventDetails.getDate(),GlobalClass.userID);
        if (!date) {
            dbForTrackActivities.addEntry(TrackActivity.userEventDetails);
             EventBus.getDefault().post(new MessageEvent("Hello!"));
            //((TrackActivity) getActivity()).setActionBarTitle("Track");
            saveUserData();
        } else {
            int c = dbForTrackActivities.isEntryExists(TrackActivity.userEventDetails,3,getActivity());

        }
    }


    public void callSavedMethod(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date


        if (tvSmokeToday.getText().equals("Today  ")){
            Date dt = new Date();

            String todaysDate = dateFormat.format(dt); // parse it like
            savedDate = todaysDate;

        }else {
            savedDate = tvSmokeToday.getText().toString();
            try {
                Date selectedDate = dateFormat.parse(tvSmokeToday.getText().toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(dbForTrackActivities.getDate((savedDate),GlobalClass.userID)) {
            ArrayList<UserEventDetails> eventDetails = dbForTrackActivities.getDetails(GlobalClass.userID, savedDate, 3);
            Log.e("db", "eventDetails = "+ new Gson().toJson(eventDetails));
            if (eventDetails.size() > 0 && eventDetails.get(0).getSmokeToday() != null) {
                smokeToday.setResponse(eventDetails.get(0).getSmokeToday());
                if(eventDetails.get(0).getSmokeToday().equals("YES")){
                    howMany.setText(eventDetails.get(0).getHowMany());
                }else if(eventDetails.get(0).getSmokeToday().equals("NO")){
                    howMany.setText(null);
                    howMany.setEnabled(false);
                    howMany.setAlpha(.6f);
                }else{
                    howMany.setText(null);
                    smokeToday.setResponse("NULL");
                    responseChange = "NULL";
                }
            }
            else{
                howMany.setText(null);
                smokeToday.setResponse("NULL");
                responseChange = "NULL";
            }
        }else {
            howMany.setText(null);
            smokeToday.setResponse("NULL");
            responseChange = "NULL";
        }
    }
}
