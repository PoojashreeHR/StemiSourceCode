package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.databases.DBForTrackActivities;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.UserEventDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 26-07-2017.
 */

public class ExerciseFragment extends Fragment implements View.OnClickListener, TrackActivity.OnBackPressedListener {


    Boolean checkClicked = false;

    @BindView(R.id.ll_walking)LinearLayout llWalking;
    @BindView(R.id.ll_cycling)LinearLayout llCycling;
    @BindView(R.id.ll_swimming)LinearLayout llSwimming;
    @BindView(R.id.ll_aerobics)LinearLayout llAerobics;
    @BindView(R.id.ll_others)LinearLayout llOthers;

    @BindView(R.id.iv_walking)ImageView ivWalking;
    @BindView(R.id.iv_cycling)ImageView ivCycling;
    @BindView(R.id.iv_swimming)ImageView ivSwimming;
    @BindView(R.id.iv_aerobics)ImageView ivAerobics;
    @BindView(R.id.iv_others)ImageView ivOthers;

    @BindView(R.id.excerciseLayout)LinearLayout excerciseLayout;

    @BindView(R.id.tv_excercise_today)
    TextView tvExcerciseToday;

    DBForTrackActivities dbForTrackActivities;
    AppSharedPreference appSharedPreference;

    String savedDate;
    public ExerciseFragment() {
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
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ButterKnife.bind(this,view);

        CommonUtils.setRobotoRegularFonts(getActivity(),tvExcerciseToday);

        ((TrackActivity) getActivity()).setActionBarTitle("Execise");
        dbForTrackActivities = new DBForTrackActivities();
        appSharedPreference = new AppSharedPreference(getActivity());

        llWalking.setOnClickListener(this);
        llCycling.setOnClickListener(this);
        llSwimming.setOnClickListener(this);
        llAerobics.setOnClickListener(this);
        llOthers.setOnClickListener(this);

        callSavedMethod();
        tvExcerciseToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Picker Dialog");
            }
        });
        ((TrackActivity) getActivity()).setOnBackPressedListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_walking:
                if(checkClicked){
                    ivWalking.setBackgroundResource(R.drawable.ic_unchecked);
                    ivWalking.setTag(0);
                    checkClicked = false;
                }else {
                    ivWalking.setBackgroundResource(R.drawable.ic_checked);
                    ivWalking.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }break;
            case R.id.ll_cycling:
                if(checkClicked){
                    ivCycling.setBackgroundResource(R.drawable.ic_unchecked);
                    ivCycling.setTag(0);
                    checkClicked = false;
                }else {
                    ivCycling.setBackgroundResource(R.drawable.ic_checked);
                    ivCycling.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }
                break;
            case R.id.ll_swimming:
                if(checkClicked){
                    ivSwimming.setBackgroundResource(R.drawable.ic_unchecked);
                    ivSwimming.setTag(0);
                    checkClicked = false;
                }else {
                    ivSwimming.setBackgroundResource(R.drawable.ic_checked);
                    ivSwimming.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }
                break;
            case R.id.ll_aerobics:
                if(checkClicked){
                    ivAerobics.setBackgroundResource(R.drawable.ic_unchecked);
                    ivAerobics.setTag(0);
                    checkClicked = false;
                }else {
                    ivAerobics.setBackgroundResource(R.drawable.ic_checked);
                    ivAerobics.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }
                break;
            case R.id.ll_others:
                if(checkClicked){
                    ivOthers.setBackgroundResource(R.drawable.ic_unchecked);
                    ivOthers.setTag(0);
                    checkClicked = false;
                }else {
                    ivOthers.setBackgroundResource(R.drawable.ic_checked);
                    ivOthers.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }
                break;
        }
    }

    public void callSavedMethod(){
        if (tvExcerciseToday.getText().equals("Today  ")){
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            savedDate = todaysDate;
        }else {
            savedDate = tvExcerciseToday.getText().toString();
        }

        if(dbForTrackActivities.getDate(savedDate)){
            ArrayList<UserEventDetails> eventDetails = dbForTrackActivities.getDetails(appSharedPreference.getProfileName(AppConstants.PROFILE_NAME),savedDate,1);
            if(eventDetails != null){
                if(eventDetails.get(0).getIswalked().equals("true")){
                    ivWalking.setBackgroundResource(R.drawable.ic_checked);
                    ivWalking.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }else {
                    ivWalking.setBackgroundResource(R.drawable.ic_unchecked);
                    ivWalking.setTag(0);
                    checkClicked = false;
                }

                if(eventDetails.get(0).getIsCycled().equals("true")){
                    ivCycling.setBackgroundResource(R.drawable.ic_checked);
                    ivCycling.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }else {
                    ivCycling.setBackgroundResource(R.drawable.ic_unchecked);
                    ivCycling.setTag(0);
                    checkClicked = false;
                }

                if(eventDetails.get(0).getIsSwimmed().equals("true")){
                    ivSwimming.setBackgroundResource(R.drawable.ic_checked);
                    ivSwimming.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }else {
                    ivSwimming.setBackgroundResource(R.drawable.ic_unchecked);
                    ivSwimming.setTag(0);
                    checkClicked = false;
                }

                if(eventDetails.get(0).getDoneAerobics().equals("true")){
                    ivAerobics.setBackgroundResource(R.drawable.ic_checked);
                    ivAerobics.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }else {
                    ivAerobics.setBackgroundResource(R.drawable.ic_unchecked);
                    ivAerobics.setTag(0);
                    checkClicked = false;
                }
                if(eventDetails.get(0).getOthers().equals("true")){
                    ivOthers.setBackgroundResource(R.drawable.ic_checked);
                    ivWalking.setTag(R.drawable.ic_checked);
                    checkClicked = true;
                }else {
                    ivOthers.setBackgroundResource(R.drawable.ic_unchecked);
                    ivOthers.setTag(0);
                    checkClicked = false;
                }
            }
        }else {
            ivWalking.setBackgroundResource(R.drawable.ic_unchecked);
            ivCycling.setBackgroundResource(R.drawable.ic_unchecked);
            ivSwimming.setBackgroundResource(R.drawable.ic_unchecked);
            ivAerobics.setBackgroundResource(R.drawable.ic_unchecked);
            ivOthers.setBackgroundResource(R.drawable.ic_unchecked);

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
            tvExcerciseToday.setText(stDate);
            callSavedMethod();

        }
    }

    public void storeData(){

        if(appSharedPreference.getProfileName(AppConstants.PROFILE_NAME)!= null){
            TrackActivity.userEventDetails.setUid(appSharedPreference.getProfileName(AppConstants.PROFILE_NAME));
        }
        if (tvExcerciseToday.getText().equals("Today  ")){
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            TrackActivity.userEventDetails.setDate(todaysDate);
        }else {
            TrackActivity.userEventDetails.setDate(tvExcerciseToday.getText().toString());
        }
        if(ivWalking.getTag().equals(R.drawable.ic_checked)){
            TrackActivity. userEventDetails.setIswalked("true");
        }else {
            TrackActivity. userEventDetails.setIswalked("false");

        }
        if(ivCycling.getTag().equals(R.drawable.ic_checked)){
            TrackActivity.userEventDetails.setIsCycled("true");
        }else {
            TrackActivity.userEventDetails.setIsCycled("false");

        }

        if(ivSwimming.getTag().equals(R.drawable.ic_checked)){
            TrackActivity.userEventDetails.setIsSwimmed("true");
        }else {
            TrackActivity.userEventDetails.setIsSwimmed("false");
        }

        if(ivAerobics.getTag().equals(R.drawable.ic_checked)){
            TrackActivity. userEventDetails.setDoneAerobics("true");
        }else {
            TrackActivity. userEventDetails.setDoneAerobics("false");
        }

        if(ivOthers.getTag().equals(R.drawable.ic_checked)){
            TrackActivity. userEventDetails.setOthers("true");
        }else {
            TrackActivity. userEventDetails.setOthers("false");
        }

      //  boolean count = dbForTrackActivities.isTableEmpty();
        boolean date = dbForTrackActivities.getDate(TrackActivity.userEventDetails.getDate());
            if (!date) {
                dbForTrackActivities.addEntry(TrackActivity.userEventDetails);
                EventBus.getDefault().post(new MessageEvent("Hello!"));
            } else {
                int c = dbForTrackActivities.isEntryExists(TrackActivity.userEventDetails,1,getActivity());

            }
    }
  /*  @Override
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        excerciseLayout.setFocusableInTouchMode(true);
        excerciseLayout.requestFocus();
        excerciseLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    storeData();
                    //((TrackActivity) getActivity()).showFragment(new TrackFragment());
                    Toast.makeText(getActivity(), "You pressed Back", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }*/
    @Override
    public void doBack() {
        if(ivWalking.getTag().equals(R.drawable.ic_checked) || ivCycling.getTag().equals(R.drawable.ic_checked)
                || ivSwimming.getTag().equals(R.drawable.ic_checked) || ivAerobics.getTag().equals(R.drawable.ic_checked)
                || ivOthers.getTag().equals(R.drawable.ic_checked)){
            storeData();
        }else {
            EventBus.getDefault().post(new MessageEvent("Hello!"));

        }
    }

}
