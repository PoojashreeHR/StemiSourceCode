package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.databases.DBForTrackActivities;
import com.stemi.stemiapp.databases.TrackExerciseDB;
import com.stemi.stemiapp.model.DataSavedEvent;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.SetTimeEvent;
import com.stemi.stemiapp.model.TrackExercise;
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

public class ExerciseFragment extends Fragment implements View.OnClickListener, TrackActivity.OnBackPressedListener {



    @BindView(R.id.ll_walking)
    LinearLayout llWalking;
    @BindView(R.id.ll_cycling)
    LinearLayout llCycling;
    @BindView(R.id.ll_swimming)
    LinearLayout llSwimming;
    @BindView(R.id.ll_aerobics)
    LinearLayout llAerobics;
    @BindView(R.id.ll_others)
    LinearLayout llOthers;

    @BindView(R.id.iv_walking)
    ImageView ivWalking;
    @BindView(R.id.iv_cycling)
    ImageView ivCycling;
    @BindView(R.id.iv_swimming)
    ImageView ivSwimming;
    @BindView(R.id.iv_aerobics)
    ImageView ivAerobics;
    @BindView(R.id.iv_others)
    ImageView ivOthers;

/*
    @BindView(R.id.excerciseLayout)
    LinearLayout excerciseLayout;
*/

    @BindView(R.id.tv_excercise_today)
    TextView tvExcerciseToday;

    @BindView(R.id.exercise_save)
    Button btnExerciseSave;

    DBForTrackActivities dbForTrackActivities;
    AppSharedPreference appSharedPreference;

    TrackActivity trackActivity;
    String savedDate;
    private boolean alreadySaved;
    private TrackExercise trackExercise;

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
        ButterKnife.bind(this, view);

        CommonUtils.setRobotoRegularFonts(getActivity(), tvExcerciseToday);
        //((TrackActivity) getActivity()).setActionBarTitle("Exercise");
        dbForTrackActivities = new DBForTrackActivities();
        appSharedPreference = new AppSharedPreference(getActivity());
        trackActivity = new TrackActivity();

        llWalking.setOnClickListener(this);
        llCycling.setOnClickListener(this);
        llSwimming.setOnClickListener(this);
        llAerobics.setOnClickListener(this);
        llOthers.setOnClickListener(this);


        tvExcerciseToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Picker Dialog");
            }
        });
        ((TrackActivity) getActivity()).setOnBackPressedListener(this);
        alreadySaved = false;


        btnExerciseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalClass.userID != null) {
                    if (ivWalking.getTag().equals(R.drawable.ic_checked_1) || ivCycling.getTag().equals(R.drawable.ic_checked_1)
                            || ivSwimming.getTag().equals(R.drawable.ic_checked_1) || ivAerobics.getTag().equals(R.drawable.ic_checked_1)
                            || ivOthers.getTag().equals(R.drawable.ic_checked_1)) {
                        storeData();
                    } else {
                        if (tvExcerciseToday.getText().equals("Today  ")) {
                            Date dt = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
                            String todaysDate = dateFormat.format(dt); // parse it like
                            savedDate = todaysDate;
                        } else {
                            savedDate = tvExcerciseToday.getText().toString();
                        }

                        if (dbForTrackActivities.getDate((savedDate), GlobalClass.userID)) {
                            storeData();
                        }
                        else {
                            EventBus.getDefault().post(new MessageEvent("Hello!"));
                            //  ((TrackActivity) getActivity()).setActionBarTitle("Track");
                        }
                    }
                }else {
                    Toast.makeText(getActivity(), "Please add profile details first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_walking:
                if (ivWalking.getTag().equals(R.drawable.ic_checked_1)) {
                    ivWalking.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivWalking.setTag(0);
                } else {
                    ivWalking.setBackgroundResource(R.drawable.ic_checked_1);
                    ivWalking.setTag(R.drawable.ic_checked_1);
                }
                break;
            case R.id.ll_cycling:
                if (ivCycling.getTag().equals(R.drawable.ic_checked_1)) {
                    ivCycling.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivCycling.setTag(0);

                } else {
                    ivCycling.setBackgroundResource(R.drawable.ic_checked_1);
                    ivCycling.setTag(R.drawable.ic_checked_1);

                }
                break;
            case R.id.ll_swimming:
                if (ivSwimming.getTag().equals(R.drawable.ic_checked_1)) {
                    ivSwimming.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivSwimming.setTag(0);
                } else {
                    ivSwimming.setBackgroundResource(R.drawable.ic_checked_1);
                    ivSwimming.setTag(R.drawable.ic_checked_1);
                }
                break;
            case R.id.ll_aerobics:
                if (ivAerobics.getTag().equals(R.drawable.ic_checked_1)) {
                    ivAerobics.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivAerobics.setTag(0);
                } else {
                    ivAerobics.setBackgroundResource(R.drawable.ic_checked_1);
                    ivAerobics.setTag(R.drawable.ic_checked_1);
                }
                break;
            case R.id.ll_others:
                if (ivOthers.getTag().equals(R.drawable.ic_checked_1)) {
                    ivOthers.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivOthers.setTag(0);
                } else {
                    ivOthers.setBackgroundResource(R.drawable.ic_checked_1);
                    ivOthers.setTag(R.drawable.ic_checked_1);
                }
                break;
        }
    }

    public void callSavedMethod() {
        if(tvExcerciseToday.getText() != null) {
            if (tvExcerciseToday.getText().equals("Today  ")) {
                Date dt = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
                String todaysDate = dateFormat.format(dt); // parse it like
                savedDate = todaysDate;
            } else {
                savedDate = tvExcerciseToday.getText().toString();
            }
        }

       // Log.e("db", "appSharedPreference.getProfileName(AppConstants.PROFILE_NAME) = "+appSharedPreference.getProfileName(AppConstants.PROFILE_NAME) );
        Log.e("db", "GlobalClass.userID = "+ GlobalClass.userID);

        if (dbForTrackActivities.getDate((savedDate), GlobalClass.userID)) {
            ArrayList<UserEventDetails> eventDetails = dbForTrackActivities.getDetails(GlobalClass.userID, savedDate, 1);
            Log.e("db","eventDetails = "+new Gson().toJson(eventDetails));
            if (eventDetails != null && eventDetails.size() > 0 ) {
                if (eventDetails.get(0).getIswalked() == null) {
                    ivWalking.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivWalking.setTag(0);
                } else if (eventDetails.get(0).getIswalked().equals("true")) {
                    ivWalking.setBackgroundResource(R.drawable.ic_checked_1);
                    ivWalking.setTag(R.drawable.ic_checked_1);
                } else {
                    ivWalking.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivWalking.setTag(0);
                }

                if (eventDetails.get(0).getIsCycled() == null) {
                    ivCycling.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivCycling.setTag(0);
                } else if (eventDetails.get(0).getIsCycled().equals("true")) {
                    ivCycling.setBackgroundResource(R.drawable.ic_checked_1);
                    ivCycling.setTag(R.drawable.ic_checked_1);
                } else {
                    ivCycling.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivCycling.setTag(0);
                }

                if (eventDetails.get(0).getIsSwimmed() == null) {
                    ivSwimming.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivSwimming.setTag(0);
                } else if (eventDetails.get(0).getIsSwimmed().equals("true")) {
                    ivSwimming.setBackgroundResource(R.drawable.ic_checked_1);
                    ivSwimming.setTag(R.drawable.ic_checked_1);
                } else {
                    ivSwimming.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivSwimming.setTag(0);
                }

                if (eventDetails.get(0).getDoneAerobics() == null) {
                    ivAerobics.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivAerobics.setTag(0);
                } else if (eventDetails.get(0).getDoneAerobics().equals("true")) {
                    ivAerobics.setBackgroundResource(R.drawable.ic_checked_1);
                    ivAerobics.setTag(R.drawable.ic_checked_1);
                } else {
                    ivAerobics.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivAerobics.setTag(0);
                }

                if (eventDetails.get(0).getOthers() == null) {
                    ivOthers.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivOthers.setTag(0);
                } else if (eventDetails.get(0).getOthers().equals("true")) {
                    ivOthers.setBackgroundResource(R.drawable.ic_checked_1);
                    ivWalking.setTag(R.drawable.ic_checked_1);
                } else {
                    ivOthers.setBackgroundResource(R.drawable.ic_unchecked_1);
                    ivOthers.setTag(0);
                }
            }
        } else {
            ivWalking.setBackgroundResource(R.drawable.ic_unchecked_1);
            ivWalking.setTag(0);
            ivCycling.setBackgroundResource(R.drawable.ic_unchecked_1);
            ivCycling.setTag(0);
            ivSwimming.setBackgroundResource(R.drawable.ic_unchecked_1);
            ivSwimming.setTag(0);
            ivAerobics.setBackgroundResource(R.drawable.ic_unchecked_1);
            ivAerobics.setTag(0);
            ivOthers.setBackgroundResource(R.drawable.ic_unchecked_1);
            ivOthers.setTag(0);


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
    public void onSetTimeEvent(SetTimeEvent event) {
        tvExcerciseToday.setText(event.getDate());
        callSavedMethod();
    }

    public void saveAllData() {
//        if(GlobalClass.userID != null) {
//            if (!alreadySaved) {
//                Log.e("fragment", "ExerciseFragment saveAllData()");
//                if (ivWalking.getTag().equals(R.drawable.ic_checked_1) || ivCycling.getTag().equals(R.drawable.ic_checked_1)
//                        || ivSwimming.getTag().equals(R.drawable.ic_checked_1) || ivAerobics.getTag().equals(R.drawable.ic_checked_1)
//                        || ivOthers.getTag().equals(R.drawable.ic_checked_1)) {
//                    storeData();
//                    alreadySaved = true;
//                }else {
//                    alreadySaved = false;
//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//                        fm.popBackStack();
//                    }
//                }
//              //  getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
//
//            }
//        }
//        else {
//            //Toast.makeText(getActivity(), "Please add profile details first", Toast.LENGTH_SHORT).show();
//        }
    }

    public void saveUserData() {
        TrackExerciseDB trackExerciseDB = new TrackExerciseDB(getActivity());
        trackExerciseDB.addEntry(trackExercise);
    }

    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
            datepickerdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datepickerdialog;
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
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

        }
    }

    public void storeData() {
        TrackExerciseDB trackExerciseDB = new TrackExerciseDB(getActivity());
        trackExercise = new TrackExercise();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        trackExercise.setUserId(GlobalClass.userID);

         TrackActivity.userEventDetails.setUid(GlobalClass.userID);

        if (tvExcerciseToday.getText().equals("Today  ")) {
            Date dt = new Date();
            // set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            TrackActivity.userEventDetails.setDate(todaysDate);
            trackExercise.setDateTime(dt);
        } else {
            TrackActivity.userEventDetails.setDate(tvExcerciseToday.getText().toString());
            try {
                Date selectedDate = dateFormat.parse(tvExcerciseToday.getText().toString());
                trackExercise.setDateTime(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        boolean isExercised = false;

        boolean isWalking = false;
        if (ivWalking.getTag().equals(R.drawable.ic_checked_1)) {
            TrackActivity.userEventDetails.setIswalked("true");
            isWalking = true;
            Log.e(TAG, "isExercised = " + isExercised);
        } else {
            TrackActivity.userEventDetails.setIswalked("false");
            isWalking = false;

        }


        boolean isCycling = false;
        if (ivCycling.getTag().equals(R.drawable.ic_checked_1)) {
            TrackActivity.userEventDetails.setIsCycled("true");
            isCycling = true;
        } else {
            TrackActivity.userEventDetails.setIsCycled("false");
            isCycling = false;

        }


        boolean isSwimming = false;
        if (ivSwimming.getTag().equals(R.drawable.ic_checked_1)) {
            TrackActivity.userEventDetails.setIsSwimmed("true");
            isSwimming = true;
        } else {
            TrackActivity.userEventDetails.setIsSwimmed("false");
            isSwimming = false;
        }


        boolean isAerobics = false;
        if (ivAerobics.getTag().equals(R.drawable.ic_checked_1)) {
            TrackActivity.userEventDetails.setDoneAerobics("true");
            isAerobics = true;
        } else {
            TrackActivity.userEventDetails.setDoneAerobics("false");
            isAerobics = false;
        }

        boolean isOthers = false;
        if (ivOthers.getTag().equals(R.drawable.ic_checked_1)) {
            TrackActivity.userEventDetails.setOthers("true");
            isOthers = true;
        } else {
            TrackActivity.userEventDetails.setOthers("false");
            isOthers = false;
        }

        isExercised = isWalking || isCycling || isSwimming || isAerobics || isOthers;
        trackExercise.setExercised(isExercised);
        Log.e(TAG, "isExercised = " + isExercised);

        //  boolean count = dbForTrackActivities.isTableEmpty();
        boolean date = dbForTrackActivities.getDate(TrackActivity.userEventDetails.getDate(), GlobalClass.userID);
        if (!date) {
            dbForTrackActivities.addEntry(TrackActivity.userEventDetails);
            EventBus.getDefault().post(new MessageEvent("Hello!"));
          //  ((TrackActivity) getActivity()).setActionBarTitle("Track");
            saveUserData();
        } else {
            int c = dbForTrackActivities.isEntryExists(TrackActivity.userEventDetails, 1, getActivity());
        }
        Log.e("db", "Saving TrackActivity.userEventDetails = "+new Gson().toJson(TrackActivity.userEventDetails));

        EventBus.getDefault().post(new DataSavedEvent(""));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TrackActivity) getActivity()).setActionBarTitle("Exercise");
        callSavedMethod();
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
        EventBus.getDefault().post(new MessageEvent("Hello!"));
    }

}
