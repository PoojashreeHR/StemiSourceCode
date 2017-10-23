package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.TabSelectedListener;
import com.stemi.stemiapp.databases.DBForTrackActivities;
import com.stemi.stemiapp.databases.TrackWeightDB;
import com.stemi.stemiapp.model.DataSavedEvent;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.SetTimeEvent;
import com.stemi.stemiapp.model.TrackWeight;
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

public class WeightFragment  extends Fragment implements View.OnClickListener,TrackActivity.OnBackPressedListener {
    @BindView(R.id.tv_weight_today) TextView tvWeightToday;
    @BindView(R.id.bt_calculatebmi)Button btCalculateBmi;
    @BindView(R.id.et_todayweight)EditText todaysWeight;
    @BindView(R.id.bmiValue) TextView BmiValue;
    @BindView(R.id.bmiResult) TextView bmiResult;
    @BindView(R.id.learn_more)TextView learnMore;
    String bmiCount = null;

    @BindView(R.id.ll_bmiCalculation)LinearLayout bmiLayout;
    String savedDate;
    AppSharedPreference appSharedPreference;
    DBForTrackActivities dbForTrackActivities;
    private boolean alreadySaved;

    TabSelectedListener tabSelectedListener;
    private TrackWeight trackWeight;

    public WeightFragment() {
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
        View view = inflater.inflate(R.layout.fragment_weight, container, false);
        ButterKnife.bind(this,view);
        appSharedPreference = new AppSharedPreference(getActivity());
        dbForTrackActivities = new DBForTrackActivities();

        callSavedMethod();
        CommonUtils.setRobotoRegularFonts(getActivity(),tvWeightToday);
        btCalculateBmi.setOnClickListener(this);
        tvWeightToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Picker Dialog");
            }
        });
        ((TrackActivity) getActivity()).setActionBarTitle("Weight");
        ((TrackActivity) getActivity()).setOnBackPressedListener(this);

        alreadySaved = false;
        tabSelectedListener =  new TabSelectedListener(getActivity(),new WebViewFragment());

        return view;
    }


    //Calculate BMI
    private float calculateBMI (String wt, String ht) {
        float weight = Float.parseFloat(wt);
        float height = (Float.parseFloat(ht))/100;
        return (float) (weight / (height * height));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_calculatebmi:

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                if(!todaysWeight.getText().toString().equals("")) {
                    if(validateField()) {

                        if(appSharedPreference.getUserHeight(AppConstants.USER_HEIGHT) != null){
                            float bmiValue = calculateBMI(todaysWeight.getText().toString(), appSharedPreference.getUserHeight(AppConstants.USER_HEIGHT));
                            bmiCount = String.format("%.2f", bmiValue);
                            String string = interpretBMI(bmiValue);
                            BmiValue.setText("Your BMI is " + bmiCount);
                            bmiResult.setText(string);
                            bmiLayout.setVisibility(View.VISIBLE);
                        }
                        else{
                            Toast.makeText(getActivity(), "Height is undefined ! Please add profile first", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(getActivity(), "Please enter your weight!!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void doBack() {
        if(GlobalClass.userID != null) {
        if(!todaysWeight.getText().toString().equals("")){
            if(validateField()) {
                SaveData();
            }
        }else {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            EventBus.getDefault().post(new MessageEvent("Hello!"));
            ((TrackActivity) getActivity()).setActionBarTitle("Track");

            }
        }
        else{
            Toast.makeText(getActivity(), "Please add profile details first", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean validateField(){
        Boolean valid = true;
        String weight = todaysWeight.getText().toString();
        if (Integer.parseInt(weight) < 20 || Integer.parseInt(weight) > 200) {
            todaysWeight.setError("Enter valid weight");
            valid = false;
        } else {
            todaysWeight.setError(null);
        }
        return valid;
    }

    public void callSavedMethod() {
        if (tvWeightToday.getText().equals("Today  ")) {
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            savedDate = todaysDate;
        } else {
            savedDate = tvWeightToday.getText().toString();
        }

        if (dbForTrackActivities.getDate((savedDate), GlobalClass.userID)) {
            ArrayList<UserEventDetails> eventDetails = dbForTrackActivities.getDetails(appSharedPreference.getProfileName(AppConstants.PROFILE_NAME), savedDate, 4);
            if (eventDetails.size() > 0 && eventDetails.get(0).getTodaysWeight() != null) {
                todaysWeight.setText(eventDetails.get(0).getTodaysWeight());
                bmiLayout.setVisibility(View.GONE);
            }
            else {
                todaysWeight.setText(null);
                bmiLayout.setVisibility(View.GONE);
            }
        }else {
            bmiLayout.setVisibility(View.GONE);
            todaysWeight.setText(null);
        }
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetTimeEvent(SetTimeEvent event){
        tvWeightToday.setText(event.getDate());
        callSavedMethod();
    }

    public void saveAllData() {
        if(GlobalClass.userID != null) {
            if (!alreadySaved) {
                Log.e("fragment", "WeightFragment saveAllData()");
                SaveData();
                alreadySaved = true;
            }
        }
        else{
            //Toast.makeText(getActivity(), "Please add profile details first", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveUserData() {
        TrackWeightDB trackWeightDB = new TrackWeightDB(getActivity());
        trackWeightDB.addEntry(trackWeight);
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
            Date parseDate = null;
            Calendar cal=Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
            cal.set(Calendar.MONTH,(month));
            String month_name = month_date.format(cal.getTime());

            Log.e("",""+month_name);

            String date1 = day + " " + month_name + " " + year;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            try {
                parseDate = dateFormat.parse(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String stDate= dateFormat.format(parseDate); //2016/11/16 12:08:43
            Log.e("Comparing Date :"," Your date is correct");
            EventBus.getDefault().post(new SetTimeEvent(0,stDate));
        }
    }

    // Interpret what BMI means
    private String interpretBMI(float bmiValue) {

        if (bmiValue < 16) {
            return "Severely underweight";
        } else if (bmiValue < 18.5) {

            return "Which means you are underweight";
        } else if (bmiValue < 25) {

            return "Which means you are normal";
        } else if (bmiValue < 30) {
            return "Which means you are overweight";
        } else {
            return "Which means you are obese";
        }
    }

    public void SaveData(){

        Log.e("db", "SaveData() is called");
        TrackActivity.userEventDetails.setUid(GlobalClass.userID);
        TrackWeightDB trackWeightDB = new TrackWeightDB(getActivity());

        trackWeight = new TrackWeight();
        trackWeight.setUserId(GlobalClass.userID);
        trackWeight.setWeight(Integer.parseInt(todaysWeight.getText().toString()));

        if (tvWeightToday.getText().equals("Today  ")){
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            TrackActivity.userEventDetails.setDate(todaysDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);
            trackWeight.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
            trackWeight.setMonthIndex(calendar.get(Calendar.MONTH) + 1);
        }else {
            TrackActivity.userEventDetails.setDate(tvWeightToday.getText().toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            try {
                Date selectedDate = dateFormat.parse(tvWeightToday.getText().toString());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate);
                trackWeight.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
                trackWeight.setMonthIndex(calendar.get(Calendar.MONTH) + 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        Log.e("db", new Gson().toJson(trackWeight));

        EventBus.getDefault().post(new DataSavedEvent(""));
        TrackActivity.userEventDetails.setTodaysWeight(todaysWeight.getText().toString());
        if(bmiCount == null){
            if(appSharedPreference.getUserHeight(AppConstants.USER_HEIGHT) != null) {
                float bmiValue = calculateBMI(todaysWeight.getText().toString(), appSharedPreference.getUserHeight(AppConstants.USER_HEIGHT));
                bmiCount = String.format("%.2f", bmiValue);
            }
            else{
                bmiCount = "0";
            }
        }
        TrackActivity.userEventDetails.setBmiValue(bmiCount);

        boolean date = dbForTrackActivities.getDate(TrackActivity.userEventDetails.getDate(),GlobalClass.userID);
        if (!date) {
            dbForTrackActivities.addEntry(TrackActivity.userEventDetails);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            ((TrackActivity) getActivity()).showFragment(new TrackFragment());
            ((TrackActivity) getActivity()).setActionBarTitle("Track");
            saveUserData();


        } else {
            int c = dbForTrackActivities.isEntryExists(TrackActivity.userEventDetails,4,getActivity());
            ((TrackActivity) getActivity()).setActionBarTitle("Track");
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();


        }
    }

/*    @Override
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        todaysWeight.setFocusableInTouchMode(true);
        todaysWeight.setFocusableInTouchMode(true);
        todaysWeight.requestFocus();
        todaysWeight.requestFocus();
        todaysWeight.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if(todaysWeight.getText().toString().equals("")){
                        ((TrackActivity) getActivity()).showFragment(new TrackFragment());
                    }else {
                        SaveData();

                    }
                   // ((TrackActivity) getActivity()).showFragment(new TrackFragment());
                    Toast.makeText(getActivity(), "You pressed Back", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }*/
}
