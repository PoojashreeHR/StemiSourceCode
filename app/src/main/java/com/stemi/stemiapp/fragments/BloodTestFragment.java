package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.databases.BloodTestDB;
import com.stemi.stemiapp.model.BloodTestResult;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pooja on 26-07-2017.
 */

public class BloodTestFragment extends Fragment implements TrackActivity.OnBackPressedListener,AppConstants {

    private static final String TAG ="BloodTest";
    @BindView(R.id.tv_dateText)TextView tvBloodTestDate;
    @BindView(R.id.et_haemoglobin)EditText etHaemoglobin;
    @BindView(R.id.et_ureaCreatinine)EditText etUreaCreatinine;
    @BindView(R.id.et_cholesterol)EditText etCholesterol;
    @BindView(R.id.et_HDL)EditText etHdl;
    @BindView(R.id.et_LDL)EditText etLdl;
    @BindView(R.id.et_Triglycerides)EditText etTriglycerides;
    @BindView(R.id.et_RPG)EditText etRpg;
    @BindView(R.id.et_FPG)EditText etFpg;
    @BindView(R.id.et_PPG)EditText etPpg;

    @BindView(R.id.date_left_btn)ImageView btLeftArrow;
    @BindView(R.id.date_right_btn)ImageView btRightArrow;

    private int monthIndex = 0;
    BloodTestDB bloodTestDB;
    AppSharedPreference appSharedPreference;
    Context context;
    boolean fieldsOK;
    BloodTestResult bloodTestResult;
    public BloodTestFragment() {
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
        View view = inflater.inflate(R.layout.frgment_bloodtest, container, false);
        ButterKnife.bind(this,view);

        appSharedPreference = new AppSharedPreference(getActivity());
        bloodTestDB = new BloodTestDB();
        bloodTestResult = new BloodTestResult();
        ((TrackActivity) getActivity()).setOnBackPressedListener(this);
        ((TrackActivity) getActivity()).setActionBarTitle("Food");
        setupDate();
        btLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthIndex--;
                setupDate();
            }
        });

        btRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthIndex++;
                setupDate();
            }
        });
        return view;
    }

    private boolean validate(EditText[] fields) {
        Boolean valid = true;
        for (int i = 0; i < fields.length; i++) {
            Log.e(TAG, "validate: "+fields.length );
            EditText currentField = fields[i];
            if (!currentField.getText().toString().isEmpty()) {
                valid = false;
            }
        }
        return valid;
    }
    @Override
    public void doBack() {
        fieldsOK = validate(new EditText[]{etHaemoglobin, etUreaCreatinine, etCholesterol, etHdl,etLdl,etTriglycerides,etRpg,etFpg,etPpg});
        if(!fieldsOK){
               AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
              // builder.setTitle("Do you want to ");
               builder.setMessage("You entered some data!! You want to save?");
               //Yes Button
               builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       saveData();
                       EventBus.getDefault().post(new MessageEvent("Hello!"));
                       Log.i("Code2care ", "Yes button Clicked!");
                   }
               });
               //No Button
               builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                       EventBus.getDefault().post(new MessageEvent("Hello!"));
                       Log.i("Code2care ","No button Clicked!");

                   }
               });


           AlertDialog alertDialog = builder.create();
               alertDialog.show();
               Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
               nbutton.setTextColor(getActivity().getResources().getColor(R.color.appBackground));
               Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
               pbutton.setTextColor(getActivity().getResources().getColor(R.color.appBackground));
       }else {
            EventBus.getDefault().post(new MessageEvent("Hello!"));
        }

    }

    private void setupDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthIndex);

        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
        String dateStr = simpleDateFormat.format(date);

        tvBloodTestDate.setText(dateStr);
       // loadStatsForDate(dateStr);
    }

    public void saveData(){
        bloodTestResult.setHeamoglobin(Double.parseDouble(etHaemoglobin.getText().toString()));
        bloodTestResult.setUreaCreatinine(Double.parseDouble(etUreaCreatinine.getText().toString()));
        bloodTestResult.setTotalCholestrol(Double.parseDouble(etCholesterol.getText().toString()));
        bloodTestResult.setHighDensityLipoProtein(Double.parseDouble(etHdl.getText().toString()));
        bloodTestResult.setLowDensityLipoProtein(Double.parseDouble(etLdl.getText().toString()));
        bloodTestResult.setTriglycerides(Double.parseDouble(etTriglycerides.getText().toString()));
        bloodTestResult.setRandomPlasmaGlucose(Double.parseDouble(etRpg.getText().toString()));
        bloodTestResult.setFastingPlasmaGlucose(Double.parseDouble(etFpg.getText().toString()));
        bloodTestResult.setPostPrandialPlasmaGlucose(Double.parseDouble(etPpg.getText().toString()));
        bloodTestResult.setUid(appSharedPreference.getProfileName(AppConstants.PROFILE_NAME));
        bloodTestResult.setDate(tvBloodTestDate.getText().toString());

        bloodTestDB.addEntry(bloodTestResult.getUid(),bloodTestResult.getDate(), bloodTestResult);
      //  ((TrackActivity) getActivity()).showFragment(new TrackFragment());

       /* if (tvBloodTestDate.getText().equals("Today  ")){
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            TrackActivity.userEventDetails.setDate(todaysDate);
        }else {*/

        //}
    //  //  boolean date = bloodTestDB.getDate(TrackActivity.userEventDetails.getDate());

        }

}
