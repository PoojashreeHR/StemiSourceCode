package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pooja on 26-07-2017.
 */

public class WeightFragment  extends Fragment implements View.OnClickListener {
    @BindView(R.id.tv_weight_today) TextView tvWeightToday;
    @BindView(R.id.bt_calculatebmi)Button btCalculateBmi;
    @BindView(R.id.et_todayweight)EditText todaysWeight;
    @BindView(R.id.bmiValue) TextView BmiValue;
    @BindView(R.id.bmiResult) TextView bmiResult;
    @BindView(R.id.learn_more)TextView learnMore;
    AppSharedPreference appSharedPreference;
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
        btCalculateBmi.setOnClickListener(this);
        tvWeightToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Picker Dialog");
            }
        });
        ((TrackActivity) getActivity()).setActionBarTitle("Food");

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
                    float bmiValue = calculateBMI(todaysWeight.getText().toString(), "127");
                    String s= String.format("%.2f",bmiValue);
                    String string = interpretBMI(bmiValue);
                    BmiValue.setText("Your BMI is " + s);
                    bmiResult.setText(string);
                    learnMore.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getActivity(), "Please enter your weight!!", Toast.LENGTH_SHORT).show();
                }
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
            String date1 = day + "-" + (month+1) + "-" + year;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
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
}
