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
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.model.BloodTestResult;
import com.stemi.stemiapp.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pooja on 26-07-2017.
 */

public class BloodTestFragment extends Fragment implements TrackActivity.OnBackPressedListener {

    @BindView(R.id.et_haemoglobin)EditText etHaemoglobin;
    @BindView(R.id.et_ureaCreatinine)EditText etUreaCreatinine;
    @BindView(R.id.et_HDL)EditText etHdl;
    @BindView(R.id.et_LDL)EditText etLdl;
    @BindView(R.id.et_Triglycerides)EditText Triglycerides;
    @BindView(R.id.et_RPG)EditText etRpg;
    @BindView(R.id.et_FPG)EditText etFpg;
    @BindView(R.id.et_PPG)EditText etPpg;

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
        fieldsOK = validate(new EditText[]{etHaemoglobin, etUreaCreatinine, etHdl,etLdl,Triglycerides,etRpg,etFpg,etPpg});

        ((TrackActivity) getActivity()).setOnBackPressedListener(this);
        ((TrackActivity) getActivity()).setActionBarTitle("Food");

        return view;
    }

    private boolean validate(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().length() <= 0) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void doBack() {
       if(!fieldsOK){
               AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
              // builder.setTitle("Do you want to ");
               builder.setMessage("You entered some data!! Really want to go back?");
               //Yes Button
               builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       EventBus.getDefault().post(new MessageEvent("Hello!"));
                       //Toast.makeText(getActivity(),"Yes button Clicked",Toast.LENGTH_LONG).show();
                       Log.i("Code2care ", "Yes button Clicked!");
                   }
               });
               //No Button
               builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                       // Toast.makeText(getActivity(),"No button Clicked",Toast.LENGTH_LONG).show();
                       Log.i("Code2care ","No button Clicked!");

                   }
               });


           AlertDialog alertDialog = builder.create();
               alertDialog.show();
               Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
               nbutton.setTextColor(getActivity().getResources().getColor(R.color.appBackground));
               Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
               pbutton.setTextColor(getActivity().getResources().getColor(R.color.appBackground));
       }
    }


}
