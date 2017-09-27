package com.stemi.stemiapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.model.HeartSymptomsModel;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.TyprfaceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pooja on 28-08-2017.
 */

public class HeartSymptomsFragment extends Fragment implements TrackActivity.OnBackPressedListener {
    RecyclerView heartSymptomsRecycler;
    HeartSymptomsAdapter heartSymptomsAdapter;
    private List<HeartSymptomsModel> heartSymptomsModel;
    @BindView(R.id.tv_ask_help)TextView askHelp;
    TrackActivity trackActivity;

    public HeartSymptomsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_heart_symptoms, container, false);
        ButterKnife.bind(this,view);

        trackActivity = new TrackActivity();
        heartSymptomsModel = new ArrayList<>();
        heartAttackSymptomsData();

        ((TrackActivity) getActivity()).setOnBackPressedListener(this);
        heartSymptomsRecycler = (RecyclerView)view.findViewById(R.id.heartSymptomsRecycler);
        heartSymptomsAdapter = new HeartSymptomsAdapter(heartSymptomsModel, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        heartSymptomsRecycler.setLayoutManager(mLayoutManager);
        heartSymptomsRecycler.setAdapter(heartSymptomsAdapter);
        askHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TrackActivity)getActivity()).showFragment(new SOSFragment());
                trackActivity.ubTateTab(2,getActivity());

            }
        });
        return view;
    }

    private void heartAttackSymptoms(int img, String symptoms, String symptomsNumb) {
        HeartSymptomsModel symptomsModel = new HeartSymptomsModel();
        symptomsModel.setSymptomsImg(img);
        symptomsModel.setSymptoms(symptoms);
        symptomsModel.setSymptomNumb(symptomsNumb);
        heartSymptomsModel.add(symptomsModel);
    }
    //Setting question and adding it to recyclerview

    private void heartAttackSymptomsData() {
        heartAttackSymptoms(R.drawable.symptom_1,getResources().getString(R.string.heart_pain),"1");
        heartAttackSymptoms(R.drawable.symptom_2,getResources().getString(R.string.jaw_pain),"2");
        heartAttackSymptoms(R.drawable.symptom_3,getResources().getString(R.string.shortnessOfBreath),"3");
        heartAttackSymptoms(R.drawable.symptom_4,getResources().getString(R.string.lightheadednes),"4");
        heartAttackSymptoms(R.drawable.symptom_5,getResources().getString(R.string.vomitting),"5");
    }

    @Override
    public void doBack() {
        EventBus.getDefault().post(new MessageEvent("Hello!"));
    }

    public class HeartSymptomsAdapter extends RecyclerView.Adapter<HeartSymptomsAdapter.MyViewHolder> {
        Context mContext;
        private List<HeartSymptomsModel> heartAttackSymptoms;
        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView symptoms;
            TextView symptomsNumb;
            CircleImageView symptomsImg;

            public MyViewHolder(View view) {
                super(view);
                symptoms = (TextView) view.findViewById(R.id.tvSymptoms);
                symptomsNumb = (TextView) view.findViewById(R.id.tvSymptomsNumb);
                symptomsImg = (CircleImageView) view.findViewById(R.id.symtonsImg);
            }
        }

        public HeartSymptomsAdapter(List<HeartSymptomsModel> heartAttackSynmptoms, Context context) {
            this.heartAttackSymptoms = heartAttackSynmptoms;
            this.mContext = context;
        }

        @Override
        public HeartSymptomsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.heart_symptoms_single_layout, parent, false);

            return new HeartSymptomsAdapter.MyViewHolder(itemView);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(final HeartSymptomsAdapter.MyViewHolder holder, final int position) {
            CommonUtils.setRobotoRegularFonts(getActivity(),holder.symptoms);
            holder.symptomsImg.setBackground(getResources().getDrawable(heartAttackSymptoms.get(position).getSymptomsImg()));
            holder.symptoms.setText(heartAttackSymptoms.get(position).getSymptoms());
            holder.symptomsNumb.setText(heartAttackSymptoms.get(position).getSymptomNumb());
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }


}
