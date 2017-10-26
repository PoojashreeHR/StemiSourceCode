package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.stemi.stemiapp.model.UserEventDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @BindView(R.id.bloodSaveButton)Button bloodSaveButton;
    @BindView(R.id.date_left_btn)ImageView btLeftArrow;
    @BindView(R.id.date_right_btn)ImageView btRightArrow;

    String empty;
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
       // ((TrackActivity) getActivity()).setActionBarTitle("Blood Test Report");
        setupDate();
        btLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthIndex--;
                setupDate();
            }
        });
        callSavedMethod();
        btRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monthIndex <= -1) {
                    monthIndex++;
                    setupDate();
                }
            }
        });

        bloodSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fieldsOK = validate(new EditText[]{etHaemoglobin, etUreaCreatinine, etCholesterol, etHdl, etLdl, etTriglycerides, etRpg, etFpg, etPpg});
                if (!fieldsOK) {
                    saveData();
                }else {
                    Toast.makeText(getActivity(), "Enter data to save", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private boolean validate(EditText[] fields) {
        Boolean valid = true;
        for (int i = 0; i < fields.length; i++) {
            Log.e(TAG, "validate: "+fields.length );
            EditText currentField = fields[i];
            if(!currentField.getText().toString().isEmpty()){
                valid = false;
            }
            if(currentField.getText().toString().endsWith(".")){
                valid = false;
                currentField.setError("Error");
                Toast.makeText(getActivity(),"Please Enter correct value",Toast.LENGTH_LONG).show();
            }
            if(currentField.getText().toString().startsWith("-")){
                valid = false;
                currentField.setError("Error");
                Toast.makeText(getActivity(),"Negative numbers are not allowed",Toast.LENGTH_LONG).show();
            }
        }
        return valid;
    }
    @Override
    public void doBack() {
        EventBus.getDefault().post(new MessageEvent("Hello!"));
      //  getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
      //  ((TrackActivity) getActivity()).setActionBarTitle("Track");

    }

    public String CheckEmptyOrNot(String st){
        String sts = "";
        if(st != null){
            if(!st.matches("0")){
                return st;
            }else {
                return sts;
            }
        }else{
            return sts;
        }
    }

    public void callSavedMethod(){
        if(bloodTestDB.isEntryExists(GlobalClass.userID, tvBloodTestDate.getText().toString())){
            BloodTestResult bloodTestResults = bloodTestDB.getEntry(GlobalClass.userID, tvBloodTestDate.getText().toString());
            if(bloodTestResults != null){
                DecimalFormat decimalFormat = new DecimalFormat();
                String haemoglobin = CheckEmptyOrNot(decimalFormat.format(bloodTestResults.getHeamoglobin()));
                etHaemoglobin.setText(haemoglobin);

                String urea = CheckEmptyOrNot(decimalFormat.format(bloodTestResults.getUreaCreatinine()));
                etUreaCreatinine.setText(urea);

                String cholesterol = CheckEmptyOrNot(decimalFormat.format(bloodTestResults.getTotalCholestrol()));
                etCholesterol.setText(cholesterol);

                String triglycerides = CheckEmptyOrNot(decimalFormat.format(bloodTestResults.getTriglycerides()));
                etTriglycerides.setText(triglycerides);

                String hdl = CheckEmptyOrNot(decimalFormat.format(bloodTestResults.getHighDensityLipoProtein()));
                etHdl.setText(hdl);

                String st3 = CheckEmptyOrNot(decimalFormat.format(bloodTestResults.getLowDensityLipoProtein()));
                etLdl.setText(st3);

                String st2 = CheckEmptyOrNot(decimalFormat.format(bloodTestResults.getRandomPlasmaGlucose()));
                etRpg.setText(st2);

                String st1 = CheckEmptyOrNot(decimalFormat.format(bloodTestResults.getFastingPlasmaGlucose()));
                etFpg.setText(st1);

                String st = CheckEmptyOrNot(decimalFormat.format(bloodTestResults.getPostPrandialPlasmaGlucose()));
                etPpg.setText(st);
            }
        }else {
            etHaemoglobin.setText(null);
            etUreaCreatinine.setText(null);
            etCholesterol.setText(null);
            etTriglycerides.setText(null);
            etHdl.setText(null);
            etLdl.setText(null);
            etRpg.setText(null);
            etFpg.setText(null);
            etPpg.setText(null);
        }
    }
    private void setupDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthIndex);

        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
        String dateStr = simpleDateFormat.format(date);

        Date date1 = new Date();
        String currentDate = simpleDateFormat.format(date1);

        tvBloodTestDate.setText(dateStr);
        callSavedMethod();
       // loadStatsForDate(dateStr);
    }
    public void saveAllData() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
    double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        }
        else return 0;
    }



    public void saveData(){
        bloodTestResult.setHeamoglobin(ParseDouble(etHaemoglobin.getText().toString()));
        bloodTestResult.setUreaCreatinine(ParseDouble(etUreaCreatinine.getText().toString()));
        bloodTestResult.setTotalCholestrol(ParseDouble(etCholesterol.getText().toString()));
        bloodTestResult.setHighDensityLipoProtein(ParseDouble(etHdl.getText().toString()));
        bloodTestResult.setLowDensityLipoProtein(ParseDouble(etLdl.getText().toString()));
        bloodTestResult.setTriglycerides(ParseDouble(etTriglycerides.getText().toString()));
        bloodTestResult.setRandomPlasmaGlucose(ParseDouble(etRpg.getText().toString()));
        bloodTestResult.setFastingPlasmaGlucose(ParseDouble(etFpg.getText().toString()));
        bloodTestResult.setPostPrandialPlasmaGlucose(ParseDouble(etPpg.getText().toString()));
        bloodTestResult.setUid(GlobalClass.userID);
        bloodTestResult.setDate(tvBloodTestDate.getText().toString());

        if(bloodTestDB.isEntryExists(GlobalClass.userID,tvBloodTestDate.getText().toString())){
            buidDialog(getActivity());
        }else {
            bloodTestDB.addEntry(bloodTestResult.getUid(), bloodTestResult.getDate(), bloodTestResult);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            EventBus.getDefault().post(new MessageEvent("Hello!"));
           // ((TrackActivity) getActivity()).setActionBarTitle("Track");
        }
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
    public void buidDialog(final Context mContext){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //builder.setTitle("");
        builder.setCancelable(false);

        builder.setMessage("Do you wish to update?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bloodTestDB.updateEntry(GlobalClass.userID,tvBloodTestDate.getText().toString(),bloodTestResult);
                EventBus.getDefault().post(new MessageEvent("Hello!"));
            //    ((TrackActivity) mContext).setActionBarTitle("Track");
                Log.i("Code2care ", "Yes button Clicked!");
            }
        });

        //No Button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Code2care ","No button Clicked!");
                dialog.dismiss();
                ((TrackActivity) mContext).showFragment(new TrackFragment());
             //   ((TrackActivity) mContext).setActionBarTitle("Track");

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(mContext.getResources().getColor(R.color.appBackground));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(mContext.getResources().getColor(R.color.appBackground));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TrackActivity) getActivity()).setActionBarTitle("Blood Test Report");
    }
}
