package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.databases.DBForTrackActivities;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    AppSharedPreference appSharedPreference;
    DBForTrackActivities dbForTrackActivities;

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
                if(!todaysWeight.getText().toString().equals("")) {
                    float bmiValue = calculateBMI(todaysWeight.getText().toString(), appSharedPreference.getUserHeight(AppConstants.USER_HEIGHT));
                    bmiCount = String.format("%.2f",bmiValue);
                    String string = interpretBMI(bmiValue);
                    BmiValue.setText("Your BMI is " + bmiCount);
                    bmiResult.setText(string);
                    learnMore.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getActivity(), "Please enter your weight!!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void doBack() {
        if(!todaysWeight.getText().toString().equals("")){
            SaveData();
        }else {
            EventBus.getDefault().post(new MessageEvent("Hello!"));

        }
    }


    public class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);

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
            tvWeightToday.setText(stDate);

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
        TrackActivity.userEventDetails.setUid(appSharedPreference.getProfileName(AppConstants.PROFILE_NAME));
        if (tvWeightToday.getText().equals("Today  ")){
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            TrackActivity.userEventDetails.setDate(todaysDate);
        }else {
            TrackActivity.userEventDetails.setDate(tvWeightToday.getText().toString());
        }
        TrackActivity.userEventDetails.setTodaysWeight(todaysWeight.getText().toString());
        if(bmiCount == null){
            float bmiValue = calculateBMI(todaysWeight.getText().toString(), appSharedPreference.getUserHeight(AppConstants.USER_HEIGHT));
            bmiCount = String.format("%.2f",bmiValue);
        }
        TrackActivity.userEventDetails.setBmiValue(bmiCount);

        boolean date = dbForTrackActivities.getDate(TrackActivity.userEventDetails.getDate());
        if (!date) {
            dbForTrackActivities.addEntry(TrackActivity.userEventDetails);
            ((TrackActivity) getActivity()).showFragment(new TrackFragment());
        } else {
            int c = dbForTrackActivities.isEntryExists(TrackActivity.userEventDetails,4,getActivity());

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
