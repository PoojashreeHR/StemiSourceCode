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
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;

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
        ((TrackActivity) getActivity()).setActionBarTitle("Blood Test Report");
        setupDate(0);
        btLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthIndex--;
                setupDate(1);
            }
        });
        callSavedMethod();
        btRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthIndex++;
                setupDate(2);
            }
        });

        bloodSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fieldsOK = validate(new EditText[]{etHaemoglobin, etUreaCreatinine, etCholesterol, etHdl, etLdl, etTriglycerides, etRpg, etFpg, etPpg});
                if (!fieldsOK) {
                    saveData();
                }else {
                    Toast.makeText(getActivity(), "Enter some data to save", Toast.LENGTH_SHORT).show();
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
            if (!currentField.getText().toString().isEmpty()) {
                valid = false;
            }
        }
        return valid;
    }
    @Override
    public void doBack() {
        EventBus.getDefault().post(new MessageEvent("Hello!"));
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        ((TrackActivity) getActivity()).setActionBarTitle("Track");

    }

    public String CheckEmptyOrNot(String st){
        if(st != null || !st.matches("0.0")){
            return st;
        }else{
            return null;
        }
    }

    public void callSavedMethod(){
        if(bloodTestDB.isEntryExists(GlobalClass.userID, tvBloodTestDate.getText().toString())){
            BloodTestResult bloodTestResults = bloodTestDB.getEntry(GlobalClass.userID, tvBloodTestDate.getText().toString());
            if(bloodTestResults != null){
                etHaemoglobin.setText(""+bloodTestResults.getHeamoglobin());
                etUreaCreatinine.setText(""+bloodTestResults.getUreaCreatinine());
                String st = CheckEmptyOrNot(""+bloodTestResults.getTotalCholestrol());
                etCholesterol.setText(st);
                etTriglycerides.setText(""+bloodTestResults.getTriglycerides());
                etHdl.setText(""+bloodTestResults.getHighDensityLipoProtein());
                etLdl.setText(""+bloodTestResults.getLowDensityLipoProtein());
                etRpg.setText(""+bloodTestResults.getRandomPlasmaGlucose());
                etFpg.setText(""+bloodTestResults.getFastingPlasmaGlucose());
                etPpg.setText(""+bloodTestResults.getPostPrandialPlasmaGlucose());
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
    private void setupDate(int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthIndex);

        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
        String dateStr = simpleDateFormat.format(date);

        Date date1 = new Date();
        String currentDate = simpleDateFormat.format(date1);
        if(value == 2){
           if(date.after(date1)){
               Toast.makeText(getActivity(), "You cannot enter data for future date", Toast.LENGTH_SHORT).show();
               monthIndex = 0;
           }else {
               tvBloodTestDate.setText(dateStr);
           }
        }else {
            tvBloodTestDate.setText(dateStr);
        }


        callSavedMethod();
       // loadStatsForDate(dateStr);
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
            ((TrackActivity) getActivity()).setActionBarTitle("Track");
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
        builder.setMessage("Do you wish to update?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bloodTestDB.updateEntry(GlobalClass.userID,tvBloodTestDate.getText().toString(),bloodTestResult);
                EventBus.getDefault().post(new MessageEvent("Hello!"));
                ((TrackActivity) mContext).setActionBarTitle("Track");
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
                ((TrackActivity) mContext).setActionBarTitle("Track");

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(mContext.getResources().getColor(R.color.appBackground));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(mContext.getResources().getColor(R.color.appBackground));
    }

}
