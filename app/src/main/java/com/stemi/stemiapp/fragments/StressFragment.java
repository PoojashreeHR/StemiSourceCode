package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.AnimateHorizontalProgressBar;
import com.stemi.stemiapp.databases.DBForTrackActivities;
import com.stemi.stemiapp.databases.TrackStressDB;
import com.stemi.stemiapp.model.DataPassListener;
import com.stemi.stemiapp.model.DataSavedEvent;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.SetTimeEvent;
import com.stemi.stemiapp.model.TrackStress;
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

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 26-07-2017.
 */

public class StressFragment extends Fragment implements AppConstants, TrackActivity.OnBackPressedListener, View.OnClickListener {

    @BindView(R.id.tv_food_today) TextView tvFoodToday;
    @BindView(R.id.seekbar) SeekBar mSeekLin;
    @BindView(R.id.seekbarText)LinearLayout seekbarText;
    @BindView(R.id.stress_save) Button btnStressSave;


    @BindView(R.id.ll_yoga)LinearLayout llYoga;
    @BindView(R.id.ll_hobies)LinearLayout llHobies;
    @BindView(R.id.ll_meditation)LinearLayout llMeditation;

    @BindView(R.id.iv_yoga)ImageView ivYoga;
    @BindView(R.id.iv_meditation)ImageView ivMeditation;
    @BindView(R.id.iv_hobbies)ImageView ivHobbies;
    String savedDate;

    AppSharedPreference appSharedPreference;
    String stressCount = null;
    DBForTrackActivities dbForTrackActivities;
    int progresValue;
    private boolean alreadySaved;
    private TrackStress trackStress;

    public StressFragment() {
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
        View view = inflater.inflate(R.layout.fragment_stress, container, false);
        ButterKnife.bind(this,view);

        CommonUtils.setRobotoRegularFonts(getActivity(),tvFoodToday);
        appSharedPreference = new AppSharedPreference(getActivity());
        dbForTrackActivities = new DBForTrackActivities();
        mSeekLin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresValue = progress ;
               //Toast.makeText(getActivity(),"seekbar progress: " + progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(getActivity(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                stressCount = String.valueOf(progresValue);
            //    Toast.makeText(getActivity(),"seekbar touch stopped! "+ progresValue + "/" + seekBar.getMax(), Toast.LENGTH_SHORT).show();
            }
        });
        alreadySaved = false;

        llYoga.setOnClickListener(this);
        llMeditation.setOnClickListener(this);
        llHobies.setOnClickListener(this);

        addLabelsBelowSeekBar();
        tvFoodToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Picker Dialog");
            }
        });
    //    ((TrackActivity) getActivity()).setActionBarTitle("Stress");
        ((TrackActivity) getActivity()).setOnBackPressedListener(this);

        btnStressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalClass.userID != null) {
                    if (stressCount != null) {
                        storeData();
                    } else {
                        EventBus.getDefault().post(new MessageEvent("Hello!"));
                        //   ((TrackActivity) getActivity()).setActionBarTitle("Track");
                    }
                }else {
                    Toast.makeText(getActivity(), "Please add profile details first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void doBack() {
        EventBus.getDefault().post(new MessageEvent("Hello!"));
    }

    public void callSavedMEthod(){
        if (tvFoodToday.getText().equals("Today  ")){
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            savedDate = todaysDate;
        }else {
            savedDate = tvFoodToday.getText().toString();
        }

        if(dbForTrackActivities.getDate((savedDate),GlobalClass.userID)) {
            ArrayList<UserEventDetails> eventDetails = dbForTrackActivities.getDetails(GlobalClass.userID, savedDate, 2);
            if (eventDetails.size() > 0 && eventDetails.get(0).getStressCount() != null) {
                mSeekLin.setProgress(Integer.parseInt(eventDetails.get(0).getStressCount()));
                stressCount = eventDetails.get(0).getStressCount();
                if(eventDetails.get(0).getYoga().equals("true")){
                    ivYoga.setBackgroundResource(R.drawable.ic_checked_1);
                    ivYoga.setTag(R.drawable.ic_checked_1);
                }else {
                    ivYoga.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivYoga.setTag(0);
                }

                if(eventDetails.get(0).getMeditation().equals("true")){
                    ivMeditation.setBackgroundResource(R.drawable.ic_checked_1);
                    ivMeditation.setTag(R.drawable.ic_checked_1);
                }else {
                    ivMeditation.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivMeditation.setTag(0);
                }

                if(eventDetails.get(0).getHobbies().equals("true")){
                    ivHobbies.setBackgroundResource(R.drawable.ic_checked_1);
                    ivHobbies.setTag(R.drawable.ic_checked_1);
                }else {
                    ivHobbies.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivHobbies.setTag(0);
                }

            }else {
                stressCount = "0";
                ivYoga.setBackgroundResource(R.drawable.ic_unchecked_1);
                ivYoga.setTag(0);
                ivHobbies.setBackgroundResource(R.drawable.ic_unchecked_1);
                ivHobbies.setTag(0);
                ivMeditation.setBackgroundResource(R.drawable.ic_unchecked_1);
                ivMeditation.setTag(0);
                mSeekLin.setProgress(0);
            }
        }else {
            stressCount = "0";
            ivYoga.setBackgroundResource(R.drawable.ic_unchecked_1);
            ivYoga.setTag(0);
            ivHobbies.setBackgroundResource(R.drawable.ic_unchecked_1);
            ivHobbies.setTag(0);
            ivMeditation.setBackgroundResource(R.drawable.ic_unchecked_1);
            ivMeditation.setTag(0);
            mSeekLin.setProgress(0);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_yoga:
                if (ivYoga.getTag().equals(R.drawable.ic_checked_1)) {
                    ivYoga.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivYoga.setTag(0);
                } else {
                    ivYoga.setBackgroundResource(R.drawable.ic_checked_1);
                    ivYoga.setTag(R.drawable.ic_checked_1);
                }
                break;
            case R.id.ll_meditation:
                if (ivMeditation.getTag().equals(R.drawable.ic_checked_1)) {
                    ivMeditation.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivMeditation.setTag(0);
                } else {
                    ivMeditation.setBackgroundResource(R.drawable.ic_checked_1);
                    ivMeditation.setTag(R.drawable.ic_checked_1);
                }
                break;
            case R.id.ll_hobies:
                if (ivHobbies.getTag().equals(R.drawable.ic_checked_1)) {
                    ivHobbies.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivHobbies.setTag(0);
                } else {
                    ivHobbies.setBackgroundResource(R.drawable.ic_checked_1);
                    ivHobbies.setTag(R.drawable.ic_checked_1);
                }
                break;
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
        Log.e(TAG, "setMenuVisibility: StressFragment : Stop");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetTimeEvent(SetTimeEvent event){
        tvFoodToday.setText(event.getDate());
        callSavedMEthod();
    }

    public void saveAllData() {
        if(GlobalClass.userID != null) {
            if (!alreadySaved) {
                Log.e("fragment", "StressFragment saveAllData()");
                if (stressCount != null) {
                    storeData();
                    alreadySaved = true;
                }else {
                    alreadySaved = false;
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                }

            }
        }
        else{
           // Toast.makeText(getActivity(), "Please add profile details first", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveUserData() {
        TrackStressDB trackStressDB = new TrackStressDB(getActivity());
        trackStressDB.addEntry(trackStress);
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
//
        }
    }

    private void addLabelsBelowSeekBar() {
        int maxCount = 6;
        for (int count = 0; count < 6; count++) {
            TextView textView = new TextView(getActivity());
            textView.setText(String.valueOf(count));
            textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            textView.setGravity(Gravity.LEFT);
            seekbarText.addView(textView);
            textView.setTextSize(17f);
            textView.setLayoutParams((count == maxCount - 1) ? getLayoutParams(0.0f) : getLayoutParams(1.0f));
        }

    }
    LinearLayout.LayoutParams getLayoutParams(float weight) {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight);
    }

    public void storeData(){
        Log.e("db", "storeData() in StressFragment");
        TrackActivity.userEventDetails.setUid(GlobalClass.userID);
        TrackStressDB trackStressDB = new TrackStressDB(getActivity());
        trackStress = new TrackStress();
        trackStress.setUserId(GlobalClass.userID);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        if (tvFoodToday.getText().equals("Today  ")){
            Date dt = new Date();
            // set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            TrackActivity.userEventDetails.setDate(todaysDate);
            trackStress.setDateTime(dt);
        }
        else {
            TrackActivity.userEventDetails.setDate(tvFoodToday.getText().toString());
            try {
                Date selectedDate = dateFormat.parse(tvFoodToday.getText().toString());
                trackStress.setDateTime(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        TrackActivity.userEventDetails.setStressCount(stressCount);

        if(ivYoga.getTag().equals(R.drawable.ic_checked_1)){
            TrackActivity. userEventDetails.setYoga("true");
            trackStress.setYoga(true);
        }else {
            TrackActivity. userEventDetails.setYoga("false");
            trackStress.setYoga(false);
        }
        if(ivMeditation.getTag().equals(R.drawable.ic_checked_1)){
            TrackActivity.userEventDetails.setMeditation("true");
            trackStress.setMeditation(true);
        }else {
            TrackActivity.userEventDetails.setMeditation("false");
            trackStress.setMeditation(false);
        }
        if(ivHobbies.getTag().equals(R.drawable.ic_checked_1)){
            TrackActivity.userEventDetails.setHobbies("true");
            trackStress.setHobbies(true);
        }else {
            TrackActivity.userEventDetails.setHobbies("false");
            trackStress.setHobbies(false);
        }
        Log.e("db", new Gson().toJson(trackStress));

        if(trackStress.isHobbies() || trackStress.isMeditation() || trackStress.isYoga()){
            trackStress.setStressed(false);
        }
        else{
            trackStress.setStressed(true);
        }


        EventBus.getDefault().post(new DataSavedEvent(""));

        boolean date = dbForTrackActivities.getDate(TrackActivity.userEventDetails.getDate(),GlobalClass.userID);
        Log.e("db", "Stress TrackActivity.userEventDetails.getDate() = "+ TrackActivity.userEventDetails.getDate());
        if (!date) {
            dbForTrackActivities.addEntry(TrackActivity.userEventDetails);
            EventBus.getDefault().post(new MessageEvent("Hello!"));
         //   ((TrackActivity) getActivity()).setActionBarTitle("Track");
            saveUserData();
        } else {
            int c = dbForTrackActivities.isEntryExists(TrackActivity.userEventDetails,2,getActivity());
           // ((TrackActivity) getActivity()).setActionBarTitle("Track");

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TrackActivity) getActivity()).setActionBarTitle("Stress");
        callSavedMEthod();
    }

    /*    @Override
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        mSeekLin.setFocusableInTouchMode(true);
        mSeekLin.requestFocus();
        mSeekLin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if( mSeekLin.requestFocus()){
                        storeData();
                    }else {
                        ((TrackActivity) getActivity()).showFragment(new TrackFragment());
                    }
                    Toast.makeText(getActivity(), "You pressed Back", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }*/

}
