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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.AnimateHorizontalProgressBar;
import com.stemi.stemiapp.databases.DBForTrackActivities;
import com.stemi.stemiapp.model.DataPassListener;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.UserEventDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

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

public class StressFragment extends Fragment implements AppConstants, TrackActivity.OnBackPressedListener {

    @BindView(R.id.tv_food_today) TextView tvFoodToday;
    @BindView(R.id.seekbar) SeekBar mSeekLin;
    @BindView(R.id.ll_seekbar)LinearLayout seekbarText;

    String savedDate;

    AppSharedPreference appSharedPreference;
    String stressCount = null;
    DBForTrackActivities dbForTrackActivities;
    int progresValue;
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
                Toast.makeText(getActivity(),"seekbar touch stopped! "+ progresValue + "/" + seekBar.getMax(), Toast.LENGTH_SHORT).show();
            }
        });

        addLabelsBelowSeekBar();
        tvFoodToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Picker Dialog");
            }
        });
        ((TrackActivity) getActivity()).setActionBarTitle("Stress");
        ((TrackActivity) getActivity()).setOnBackPressedListener(this);

        return view;
    }

    @Override
    public void doBack() {
        if(stressCount != null){
            storeData();
        }else {
            EventBus.getDefault().post(new MessageEvent("Hello!"));
            ((TrackActivity) getActivity()).setActionBarTitle("Track");
        }
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

        if(dbForTrackActivities.getDate(savedDate)) {
            ArrayList<UserEventDetails> eventDetails = dbForTrackActivities.getDetails(appSharedPreference.getProfileName(AppConstants.PROFILE_NAME), savedDate, 2);
            if (eventDetails.get(0).getStressCount() != null) {
                mSeekLin.setProgress(Integer.parseInt(eventDetails.get(0).getStressCount()));
            }
        }else {
            mSeekLin.setProgress(0);
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
            tvFoodToday.setText(stDate);
            callSavedMEthod();
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
            textView.setLayoutParams((count == maxCount - 1) ? getLayoutParams(0.0f) : getLayoutParams(1.0f));
        }

    }

    LinearLayout.LayoutParams getLayoutParams(float weight) {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight);
    }

    public void storeData(){
        TrackActivity.userEventDetails.setUid(appSharedPreference.getProfileName(PROFILE_NAME));
        if (tvFoodToday.getText().equals("Today  ")){
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            TrackActivity.userEventDetails.setDate(todaysDate);
        }else {
            TrackActivity.userEventDetails.setDate(tvFoodToday.getText().toString());
        }
        TrackActivity.userEventDetails.setStressCount(stressCount);

        boolean date = dbForTrackActivities.getDate(TrackActivity.userEventDetails.getDate());
        if (!date) {
            dbForTrackActivities.addEntry(TrackActivity.userEventDetails);
            EventBus.getDefault().post(new MessageEvent("Hello!"));
            ((TrackActivity) getActivity()).setActionBarTitle("Track");
        } else {
            int c = dbForTrackActivities.isEntryExists(TrackActivity.userEventDetails,2,getActivity());
            ((TrackActivity) getActivity()).setActionBarTitle("Track");

        }
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
