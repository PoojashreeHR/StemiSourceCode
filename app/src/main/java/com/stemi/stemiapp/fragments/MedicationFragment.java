package com.stemi.stemiapp.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.databases.DBforUserDetails;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;

import org.json.JSONObject;

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

public class MedicationFragment extends Fragment implements AppConstants {
    @BindView(R.id.tv_medication_today) TextView tvMedicationToday;
    @BindView(R.id.bt_addNewMedicine) Button addNewMedicine;


    AppSharedPreference appSharedPreference;
    DBforUserDetails dBforUserDetails;
    long getCount;
    public MedicationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_medication, container, false);
        ButterKnife.bind(this,view);

        dBforUserDetails = new DBforUserDetails(getActivity());
      //  dBforUserDetails.removeMedicalDetails("Pooja");

        appSharedPreference = new AppSharedPreference(getActivity());
        long count = dBforUserDetails.getMedicineDetailsCount();
        Log.e(TAG, "onCreateView: Profile Count" + count );

        if(count > 0) {
            ArrayList<String> medicine = dBforUserDetails.getMedicine();
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.imageTypeLayout);
            for (int i = 0; i < medicine.size(); i++) {
                int colorOfMedicine;
                ImageView image = new ImageView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
                params.setMargins(10, 20, 10, 20);
                image.setLayoutParams(params);

                //image.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                String s = medicine.get(i);
                Gson gsonObj = new Gson();
                MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
                Log.e(TAG, "onCreateView: " + medicineDetails);
                if (medicineDetails.getMedicineMorning().equals("Morning")) {
                    String medicineType = medicineDetails.getMedicineType();
                    if(medicineType.equals("Tablet")){
                        colorOfMedicine = medicineDetails.getMedicineColor();
                        image.setBackgroundResource(R.drawable.ic_tablet);
                        Drawable drawable = image.getResources().getDrawable(R.drawable.ic_tablet);
                        drawable.clearColorFilter();
                        image.invalidate();
                        drawable.setColorFilter(new PorterDuffColorFilter(colorOfMedicine, PorterDuff.Mode.SRC_IN));
                        layout.addView(image);
                    }else {
                        Log.e(TAG, "onCreateView: " + "NO TABLET " );
                    }

                    if(medicineType.contains("Capsule")){

                        image.setBackgroundResource(R.drawable.ic_capsule);
                        layout.addView(image);
                    }
                    if(medicineType.contains("Syrup")){

                        image.setBackgroundResource(R.drawable.ic_syrup);
                        // GradientDrawable gd = (GradientDrawable) image.getBackground().getCurrent();
                        //gd.setColor(getResources().getColor(colorOfMedicine));
                        // Adds the view to the layout
                        layout.addView(image);

                    }
                }
            }
        }

            tvMedicationToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialogfragment = new DatePickerDialogClass();
                    dialogfragment.show(getActivity().getFragmentManager(), "Date Picker Dialog");
                }
            });

            addNewMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TrackActivity) getActivity()).showFragment(new AddMedicineFragment());
                }
            });
            ((TrackActivity) getActivity()).setActionBarTitle("Medication");
            return view;
    }

    public void setMedicineColor(int colorOfMedicine, ImageView image,MedicineDetails medicineDetails){

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
            String Date1 = day + "-" + (month+1) + "-" + year;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                parseDate = dateFormat.parse(Date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String stDate= dateFormat.format(parseDate); //2016/11/16 12:08:43
            Log.e("Comparing Date :"," Your date is correct");
            tvMedicationToday.setText(stDate);

        }
    }

}
