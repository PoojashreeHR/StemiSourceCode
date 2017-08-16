package com.stemi.stemiapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.BetterSpinner;
import com.stemi.stemiapp.customviews.SpinnerWithHint;
import com.stemi.stemiapp.databases.DBforUserDetails;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * Created by Pooja on 24-07-2017.
 */

public class SOSFragment extends Fragment {
    DBforUserDetails dBforUserDetails;
    BetterSpinner personSpinner;
    ArrayList<String> personName;
    public SOSFragment() {
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
        View view = inflater.inflate(R.layout.fragment_sos, container, false);
        personSpinner = (BetterSpinner) view.findViewById(R.id.person_Spinner);
        dBforUserDetails = new DBforUserDetails(getActivity());
        personName = new ArrayList<>();
        personName = dBforUserDetails.getRecords();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, personName );
        personSpinner.setAdapter(arrayAdapter);

        Log.e(TAG, "onCreateView: SOS Fragment"+ dBforUserDetails.getRecords() );
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if(getActivity() != null){
            if(menuVisible){
                ((TrackActivity) getActivity()).setActionBarTitle("SOS");

            }
        }
        super.setMenuVisibility(menuVisible);
    }
}
