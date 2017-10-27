package com.stemi.stemiapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;

import com.stemi.stemiapp.activity.UpdateableFragment;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.databases.TrackExerciseDB;
import com.stemi.stemiapp.databases.TrackMedicationDB;
import com.stemi.stemiapp.databases.TrackSmokingDB;
import com.stemi.stemiapp.databases.TrackStressDB;
import com.stemi.stemiapp.databases.TrackWeightDB;
import com.stemi.stemiapp.databases.UserDetailsTable;
import com.stemi.stemiapp.model.DataSavedEvent;
import com.stemi.stemiapp.model.HeartSymptomsModel;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.model.apiModels.StatusMessageResponse;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.rest.ApiClient;
import com.stemi.stemiapp.rest.ApiInterface;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 24-07-2017.
 */

public class LearnFragment extends Fragment implements AppConstants, UpdateableFragment, View.OnClickListener{
    @BindView(R.id.tvTips) TextView tvTips;
    @BindView(R.id.tvExpandable) TextView tvExpandableSymptoms;

    @BindView(R.id.desc_exercise) TextView tvDescExercise;
    @BindView(R.id.desc_medication) TextView tvDescMedication;
    @BindView(R.id.desc_smoking) TextView tvDescSmoking;
    @BindView(R.id.desc_stress) TextView tvDescStress;
    @BindView(R.id.desc_weight) TextView tvDescWeight;
    @BindView(R.id.pager_tabDots)TabLayout tabLayout;
    private List<HeartSymptomsModel> heartSymptomsModel;

    @BindView(R.id.learn_medication)RelativeLayout learnMedication;
    @BindView(R.id.learn_weight)RelativeLayout learnWeight;
    @BindView(R.id.learn_smoking)RelativeLayout learnSmoking;
    @BindView(R.id.learn_exercise)RelativeLayout learnExercise;
    @BindView(R.id.learn_stress)RelativeLayout learnStress;

    @BindView(R.id.banner_view_pager)ViewPager bannerViewPager;

    ImagesPagerAdapter pagerAdapter;
    ApiInterface apiInterface;
    AppSharedPreference appSharedPreference;
    public LearnFragment() {
        // Required empty public constructor
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
    public void onDataSaved(DataSavedEvent event){
        updateSelf();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        ButterKnife.bind(this,view);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appSharedPreference = new AppSharedPreference(getActivity());
        callGetTipOfTheDay();
        heartSymptomsModel = new ArrayList<>();
//        ((TrackActivity) getActivity()).setActionBarTitle("Learn");

        CommonUtils.setRobotoLightFonts(getActivity(),tvTips);
        CommonUtils.setRobotoRegularFonts(getActivity(),tvExpandableSymptoms);
        tvExpandableSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TrackActivity) getActivity()).showFragment(new HeartSymptomsFragment());
            }
        });
        heartAttackSymptomsData();
        pagerAdapter = new ImagesPagerAdapter(getActivity(),heartSymptomsModel);
        bannerViewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(bannerViewPager);

        learnMedication.setOnClickListener(this);
        learnWeight.setOnClickListener(this);
        learnSmoking.setOnClickListener(this);
        learnExercise.setOnClickListener(this);
        learnStress.setOnClickListener(this);

        return view;
    }

    private void heartAttackSymptoms(int img, String symptoms, String symptomsNumb) {
        HeartSymptomsModel symptomsModel = new HeartSymptomsModel();
        symptomsModel.setSymptomsImg(img);
        symptomsModel.setSymptoms(symptoms);
        symptomsModel.setSymptomNumb(symptomsNumb);
        heartSymptomsModel.add(symptomsModel);

    }
    private void heartAttackSymptomsData() {
        heartAttackSymptoms(R.drawable.symptom_1,getResources().getString(R.string.heart_pain),"1");
        heartAttackSymptoms(R.drawable.symptom_2,getResources().getString(R.string.jaw_pain),"2");
        heartAttackSymptoms(R.drawable.symptom_3,getResources().getString(R.string.shortnessOfBreath),"3");
        heartAttackSymptoms(R.drawable.symptom_4,getResources().getString(R.string.lightheadednes),"4");
        heartAttackSymptoms(R.drawable.symptom_5,getResources().getString(R.string.vomitting),"5");

    }

    private void populatePerformanceTexts() {
        TrackMedicationDB trackMedicationDB = new TrackMedicationDB(getActivity());
        int medNoOfDays = trackMedicationDB.getNumberOfDays(GlobalClass.userID);
        tvDescMedication.setText("You've been right on track for "+medNoOfDays+" days with your medication!");

        TrackWeightDB trackWeightDB = new TrackWeightDB(getActivity());
        int weight = trackWeightDB.getLastKnownWeight(GlobalClass.userID);

        if(weight == 0){
            UserDetailsTable userDetailsTable = new UserDetailsTable(getActivity());
            RegisteredUserDetails userDetails = userDetailsTable.getUserDetails(GlobalClass.userID);
            if(userDetails != null && userDetails.getWeight() != null) {
                weight = Integer.parseInt(userDetails.getWeight());
            }
        }

        String weightStr;
        if(weight >= getUpperWeight()){
            weightStr = "Oh no! You're in the overweight weight range as per your BMI";
        }
        else if(weight <= getLowerWeight()){
            weightStr = "Oh no! You're in the low weight range as per your BMI";
        }
        else{
            weightStr = "You're in the ideal weight range as per your BMI";
        }
        tvDescWeight.setText(weightStr);

        TrackSmokingDB trackSmokingDB = new TrackSmokingDB(getActivity());
        int smokingNoDays = trackSmokingDB.getNumberOfDays(GlobalClass.userID);
        String smokingText = "Bravo! "+smokingNoDays+" day(s) since you last smoked";
        tvDescSmoking.setText(smokingText);

        TrackExerciseDB trackExerciseDB = new TrackExerciseDB(getActivity());
        int exerciseDays = trackExerciseDB.getNumberOfWeeks(GlobalClass.userID);
        String exerciseText = "Amazing! You've been exercising regularly for "+exerciseDays+" weeks.";
        tvDescExercise.setText(exerciseText);

        TrackStressDB trackStressDB = new TrackStressDB(getActivity());
        int stressCount = trackStressDB.getNumberOfDays(GlobalClass.userID);
        String stressText = "Cool as a cucumber! Your stress levels haven't spiked in "+stressCount+" days";
        tvDescStress.setText(stressText);
    }

    private double getUpperWeight() {
        return 24.9 * (GlobalClass.heightInM * GlobalClass.heightInM);
    }

    private double getLowerWeight(){
        return 18.5 * (GlobalClass.heightInM * GlobalClass.heightInM);
    }

    public void callGetTipOfTheDay(){

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(new Date());
        Log.e(TAG, "onCreateView: "+ formattedDate );

        String token = appSharedPreference.getUserToken(USER_TOKEN);
        Log.e(TAG, "callGetTipOfTheDay: "+token );
        Call<StatusMessageResponse> call = apiInterface.callTipOfTheDAy(formattedDate, token);
        call.enqueue(new Callback<StatusMessageResponse>() {
            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                if(response.isSuccessful()){

                    if(response.body().getMessage() != null &&  !TextUtils.isEmpty(response.body().getMessage() )){
                        appSharedPreference.setLastTip(response.body().getMessage());
                    }

                    tvTips.setText(appSharedPreference.getLastTip());
                }else {
                    Log.e(TAG, "onResponse: SOMETHING WRONG" );
                    tvTips.setText(appSharedPreference.getLastTip());
                }
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                tvTips.setText(appSharedPreference.getLastTip());
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(getActivity() != null){
            if(menuVisible){
                ((TrackActivity) getActivity()).setActionBarTitle("Learn");
                updateSelf();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.learn_medication:
                ((TrackActivity) getActivity()).showFragment(new MedicationFragment());
                break;
            case R.id.learn_weight:
                ((TrackActivity) getActivity()).showFragment(new WeightFragment());
                break;
            case R.id.learn_smoking:
                ((TrackActivity) getActivity()).showFragment(new SmokingFragment());
                break;
            case R.id.learn_exercise:
                ((TrackActivity) getActivity()).showFragment(new ExerciseFragment());
                break;
            case R.id.learn_stress:
                ((TrackActivity) getActivity()).showFragment(new StressFragment());
                break;
        }
    }

    /**
     * place the images in ViewPager.
     */
    public class ImagesPagerAdapter extends PagerAdapter {
        final Context mContext;
        final LayoutInflater mLayoutInflater;
        final List<HeartSymptomsModel> mResources;

        ImagesPagerAdapter(Context context, List<HeartSymptomsModel> mResources) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mResources = mResources;
        }

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.heart_symptoms_single_layout, container, false);
            TextView symptoms = (TextView) itemView.findViewById(R.id.tvSymptoms);
            TextView symptomsNumb = (TextView) itemView.findViewById(R.id.tvSymptomsNumb);
            CircleImageView symptomsImg = (CircleImageView) itemView.findViewById(R.id.symtonsImg);

            symptomsImg.setBackgroundResource(mResources.get(position).getSymptomsImg());
            symptoms.setText(mResources.get(position).getSymptoms());
            symptomsNumb.setText(mResources.get(position).getSymptomNumb());
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    @Override
    public void onResume() {
       // ((TrackActivity) getActivity()).setActionBarTitle("Learn");
        populatePerformanceTexts();
        super.onResume();
        //((TrackActivity) getActivity()).setActionBarTitle("Learn");
    }

    @Override
    public void updateSelf() {
        populatePerformanceTexts();
    }
}
