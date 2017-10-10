package com.stemi.stemiapp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.databases.BloodTestDB;
import com.stemi.stemiapp.databases.TrackExerciseDB;
import com.stemi.stemiapp.databases.TrackMedicationDB;
import com.stemi.stemiapp.databases.TrackSmokingDB;
import com.stemi.stemiapp.databases.TrackStressDB;
import com.stemi.stemiapp.databases.TrackWeightDB;
import com.stemi.stemiapp.graphs.FlaggedDataPoint;
import com.stemi.stemiapp.graphs.XAxisDateFormatter;
import com.stemi.stemiapp.graphs.YAxisValueFormatter;
import com.stemi.stemiapp.model.BloodTestResult;
import com.stemi.stemiapp.model.TrackExercise;
import com.stemi.stemiapp.model.TrackMedication;
import com.stemi.stemiapp.model.TrackSmoking;
import com.stemi.stemiapp.model.TrackStress;
import com.stemi.stemiapp.model.TrackWeight;
import com.stemi.stemiapp.samples.ChartSampleActivity;
import com.stemi.stemiapp.utils.GlobalClass;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pooja on 26-07-2017.
 */

public class StatusFragment extends Fragment {
    Button button;
    private TabLayout tabs;
    private RelativeLayout healthLayout, weightLayout, bloodTestLayout;
    private GraphView healthGraph;
    private PointsGraphSeries<DataPointInterface> medSeries, exerSeries, smokingSeries, stressSeries;
    private DataPointInterface[] medPoints, exerPoints, smokingPoints, stressPoints;
    private GraphView weightGraph;
    private Button btnCal;
    private ImageView btnLeftArrow, btnRightArrow;
    private int monthIndex = 0;
    private TextView dateText;

    @BindView(R.id.txt_heamoglobin)
     TextView txtHeamoglobin;

    @BindView(R.id.txt_urea_creatinine)
     TextView txtUreaCreatinine;

    @BindView(R.id.txt_total_cholestrol)
     TextView txtTotalCholestrol;

    @BindView(R.id.txt_high_density_lipo)
     TextView txtHighDesnityLipo;

    @BindView(R.id.txt_low_density_lipo)
     TextView txtLowDensityLipo;

    @BindView(R.id.txt_triglycerides)
     TextView txtTriglycerides;

    @BindView(R.id.txt_random_plasma_glucose)
     TextView txtRandomPlasmaGlucose;

    @BindView(R.id.txt_fasting_plasma_glucose)
     TextView txtFastingPlasmaGlucose;

    @BindView(R.id.txt_post_prandial_plasma_glucose)
     TextView txtPostPrandialPlasmaGlucose;
    private RelativeLayout stressLayout;
    private GraphView stressGraph;
    private final String TAG = "Graphs";

    public StatusFragment() {
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
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        ButterKnife.bind(this,view);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        healthLayout = (RelativeLayout) view.findViewById(R.id.health_layout);
        weightLayout = (RelativeLayout) view.findViewById(R.id.weight_layout);
        bloodTestLayout = (RelativeLayout) view.findViewById(R.id.blood_test_layout);
        stressLayout = (RelativeLayout) view.findViewById(R.id.stress_layout);

        healthGraph = (GraphView) view.findViewById(R.id.health_graph);
        weightGraph = (GraphView) view.findViewById(R.id.weight_graph);
        stressGraph = (GraphView) view.findViewById(R.id.stress_graph);
        btnLeftArrow = (ImageView) view.findViewById(R.id.date_left_btn);
        btnRightArrow = (ImageView) view.findViewById(R.id.date_right_btn);

        dateText = (TextView) view.findViewById(R.id.date_text);

        btnLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthIndex--;
                setupDate();
            }
        });

        btnRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthIndex++;
                setupDate();
            }
        });

        tabs.addTab(tabs.newTab().setText("Health"));
        tabs.addTab(tabs.newTab().setText("Stress"));
        tabs.addTab(tabs.newTab().setText("Weight"));
        tabs.addTab(tabs.newTab().setText("Blood Test"));

        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.appBackground));
        tabs.setTabTextColors(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.appBackground));

        //populateDummyData();
        populateHealthGraph();
       // populateDummyData();
        populateStressGraph();
        populateWeightGraph();
        setupDate();

        ((TrackActivity)getActivity()).getViewPager().setPagingEnabled(false);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Health")){
                    bloodTestLayout.setVisibility(View.GONE);
                    weightLayout.setVisibility(View.GONE);
                    stressLayout.setVisibility(View.GONE);
                    healthLayout.setVisibility(View.VISIBLE);
                   // populateDummyData();
                   // populateHealthGraph();
                }
                else if(tab.getText().equals("Stress")){
                    healthLayout.setVisibility(View.GONE);
                    bloodTestLayout.setVisibility(View.GONE);
                    weightLayout.setVisibility(View.GONE);
                    stressLayout.setVisibility(View.VISIBLE);
                }
                else if(tab.getText().equals("Weight")){
                    healthLayout.setVisibility(View.GONE);
                    stressLayout.setVisibility(View.GONE);
                    bloodTestLayout.setVisibility(View.GONE);
                    weightLayout.setVisibility(View.VISIBLE);
                }
                else{
                    healthLayout.setVisibility(View.GONE);
                    stressLayout.setVisibility(View.GONE);
                    weightLayout.setVisibility(View.GONE);
                    bloodTestLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void populateStressGraph() {
        TrackStressDB trackStressDB = new TrackStressDB(getActivity());
        List<TrackStress> trackStressList = trackStressDB.getAllInfo(GlobalClass.userID);
        Log.e(TAG, "trackStressList.size() = "+trackStressList.size());

        DataPointInterface[] meditationPoints = new FlaggedDataPoint[trackStressList.size()];
        DataPointInterface[] yogaPoints = new FlaggedDataPoint[trackStressList.size()];
        DataPointInterface[] hobbyPoints = new FlaggedDataPoint[trackStressList.size()];

        int i=0;
        for(TrackStress trackStress : trackStressList){
            Log.e("db", new Gson().toJson(trackStress));
            yogaPoints[i] = new FlaggedDataPoint(trackStress.getDateTime(), 1, trackStress.isYoga());
            meditationPoints[i] = new FlaggedDataPoint(trackStress.getDateTime(), 2, trackStress.isMeditation());
            hobbyPoints[i] = new FlaggedDataPoint(trackStress.getDateTime(), 3, trackStress.isHobbies());
            i++;
        }

        PointsGraphSeries<DataPointInterface> meditationSeries = new PointsGraphSeries<>(meditationPoints);
        PointsGraphSeries<DataPointInterface> yogaSeries = new PointsGraphSeries<>(yogaPoints);
        PointsGraphSeries<DataPointInterface> hobbySeries = new PointsGraphSeries<>(hobbyPoints);


        //meditationSeries.setDrawDataPoints(true);
        //meditationSeries.setColor(getResources().getColor(R.color.appBackground));
        //meditationSeries.setBackgroundColor(R.color.appBackground);
        meditationSeries.setTitle("Meditation");
        stressGraph.addSeries(meditationSeries);

        meditationSeries.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(10);
                FlaggedDataPoint flaggedDataPoint = (FlaggedDataPoint) dataPoint;
                if(flaggedDataPoint.getFlag()) {
                    paint.setColor(getResources().getColor(R.color.appBackground));
                }
                else{
                    paint.setColor(Color.GRAY);
                }
                canvas.drawCircle(x,y,10,paint);
            }
        });


        //yogaSeries.setDrawDataPoints(true);
        //yogaSeries.setColor(getResources().getColor(R.color.colorGreen));
        yogaSeries.setTitle("Yoga");

        yogaSeries.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(10);
                FlaggedDataPoint flaggedDataPoint = (FlaggedDataPoint) dataPoint;
                if(flaggedDataPoint.getFlag()) {
                    paint.setColor(getResources().getColor(R.color.colorGreen));
                }
                else{
                    paint.setColor(Color.GRAY);
                }
                canvas.drawCircle(x,y,10,paint);
            }
        });

        stressGraph.addSeries(yogaSeries);

        //hobbySeries.setColor(getResources().getColor(R.color.blue));
        hobbySeries.setTitle("Hobbies");

        hobbySeries.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(10);
                FlaggedDataPoint flaggedDataPoint = (FlaggedDataPoint) dataPoint;
                if(flaggedDataPoint.getFlag()) {
                    paint.setColor(getResources().getColor(R.color.blue));
                }
                else{
                    paint.setColor(Color.GRAY);
                }
                canvas.drawCircle(x,y,10,paint);
            }
        });


        stressGraph.addSeries(hobbySeries);

//        stressGraph.getLegendRenderer().setVisible(true);
//        stressGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);


        List<String> yAxes = new ArrayList<>();
        yAxes.add("");
        yAxes.add("Yoga");
        yAxes.add("Meditation");
        yAxes.add("Hobbies");

        stressGraph.getGridLabelRenderer().setLabelFormatter(new YAxisValueFormatter(getActivity(), yAxes));
        stressGraph.getGridLabelRenderer().setHumanRounding(false);
        stressGraph.getGridLabelRenderer().setNumHorizontalLabels(5);
        stressGraph.getGridLabelRenderer().setNumVerticalLabels(4);

        stressGraph.getViewport().setYAxisBoundsManual(true);
        stressGraph.getViewport().setMinY(0);
        stressGraph.getViewport().setMaxY(3);
        stressGraph.getViewport().setXAxisBoundsManual(true);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);
        stressGraph.getViewport().setMinX(cal.getTime().getTime());
        cal.add(Calendar.DATE, 10);
        stressGraph.getViewport().setMaxX(cal.getTime().getTime());
        stressGraph.getViewport().setScrollable(true);
    }

    private void setupDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthIndex);

        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
        String dateStr = simpleDateFormat.format(date);

        dateText.setText(dateStr);
        loadStatsForDate(dateStr);
    }

    private void loadStatsForDate(String dateStr) {
        BloodTestDB bloodTestDB = new BloodTestDB();
        BloodTestResult bloodTestResult = bloodTestDB.getEntry(GlobalClass.userID, dateStr);

        txtHeamoglobin.setText(String.format("%.1f",bloodTestResult.getHeamoglobin()));
        txtUreaCreatinine.setText(String.format("%.1f",bloodTestResult.getUreaCreatinine()));
        txtTotalCholestrol.setText(String.format("%.1f",bloodTestResult.getTotalCholestrol()));
        txtHighDesnityLipo.setText(String.format("%.1f",bloodTestResult.getHighDensityLipoProtein()));
        txtLowDensityLipo.setText(String.format("%.1f",bloodTestResult.getLowDensityLipoProtein()));
        txtTriglycerides.setText(String.format("%.1f",bloodTestResult.getTriglycerides()));
        txtRandomPlasmaGlucose.setText(String.format("%.1f",bloodTestResult.getRandomPlasmaGlucose()));
        txtFastingPlasmaGlucose.setText(String.format("%.1f",bloodTestResult.getFastingPlasmaGlucose()));
        txtPostPrandialPlasmaGlucose.setText(String.format("%.1f",bloodTestResult.getPostPrandialPlasmaGlucose()));
    }

    private void populateWeightGraph() {

        TrackWeightDB trackWeightDB = new TrackWeightDB(getActivity());
        List<TrackWeight> trackWeightList = trackWeightDB.getEntries(GlobalClass.userID);
        Log.e("StatsFragment", "trackWeightList.size() = "+trackWeightList.size());
        DataPointInterface[] weightData = new DataPoint[trackWeightList.size()];
        int j = 0;
        for(TrackWeight weight : trackWeightList){
            weightData[j] = new DataPoint(weight.getMonthIndex(), weight.getWeight());
            j++;
        }

        DataPointInterface[] weightUpperLimitData = new DataPoint[12];
        DataPointInterface[] weightLowerLimitData = new DataPoint[12];
        for(int i=0; i<weightUpperLimitData.length; i++){

            weightUpperLimitData[i] = new DataPoint(i+1, getUpperWeight());
            weightLowerLimitData[i] = new DataPoint(i+1, getLowerWeight());
        }
        LineGraphSeries<DataPointInterface> weightGraphSeries = new LineGraphSeries<>(weightData);
        weightGraphSeries.setDrawDataPoints(true);
        weightGraph.addSeries(weightGraphSeries);


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(getResources().getColor(R.color.colorGreen));
        paint.setPathEffect(new DashPathEffect(new float[]{5,10,15,20}, 0));

        LineGraphSeries<DataPointInterface> upperGraphSeries = new LineGraphSeries<>(weightUpperLimitData);
        upperGraphSeries.setCustomPaint(paint);
        //upperGraphSeries.setDrawBackground(true);
        //upperGraphSeries.setBackgroundColor(Color.parseColor("#3300ff00"));
        weightGraph.addSeries(upperGraphSeries);

        LineGraphSeries<DataPointInterface> lowerGraphSeries = new LineGraphSeries<>(weightLowerLimitData);
        lowerGraphSeries.setCustomPaint(paint);
        //lowerGraphSeries.setDrawBackground(true);
        //lowerGraphSeries.setBackgroundColor(Color.parseColor("#33cccccc"));
        weightGraph.addSeries(lowerGraphSeries);

        weightGraph.getViewport().setXAxisBoundsManual(true);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(weightGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"JAN","FEB","MAR"
        ,"APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"});
        weightGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        weightGraph.getGridLabelRenderer().setNumHorizontalLabels(6);

    }

    private double getUpperWeight() {
        return 24.9 * (GlobalClass.heightInM * GlobalClass.heightInM);
    }

    private double getLowerWeight(){
        return 18.5 * (GlobalClass.heightInM * GlobalClass.heightInM);
    }

    private void populateHealthGraph() {
        populateDatapoints();
        medSeries = new PointsGraphSeries<>(medPoints);
        healthGraph.addSeries(medSeries);

        exerSeries = new PointsGraphSeries<>(exerPoints);
        healthGraph.addSeries(exerSeries);

       // stressSeries = new PointsGraphSeries<>(stressPoints);
       // healthGraph.addSeries(stressSeries);

        smokingSeries = new PointsGraphSeries<>(smokingPoints);
        healthGraph.addSeries(smokingSeries);

        medSeries.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(10);
                FlaggedDataPoint flaggedDataPoint = (FlaggedDataPoint) dataPoint;
                if(flaggedDataPoint.getFlag()) {
                    paint.setColor(Color.GREEN);
                }
                else{
                    paint.setColor(Color.GRAY);
                }
                canvas.drawCircle(x,y,10,paint);
                //canvas.drawLine(x-20, y-20, x+20, y+20, paint);
                //canvas.drawLine(x+20, y-20, x-20, y+20, paint);
            }
        });

        exerSeries.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(10);
                FlaggedDataPoint flaggedDataPoint = (FlaggedDataPoint) dataPoint;
                if(flaggedDataPoint.getFlag()) {
                    paint.setColor(Color.YELLOW);
                }
                else{
                    paint.setColor(Color.GRAY);
                }

//                Path path = new Path();
//                path.setFillType(Path.FillType.EVEN_ODD);
//                path.lineTo(x+10, y+10);
//                path.lineTo(x-10, y-10);
//                path.lineTo(x, y);
//                path.close();
//
//                canvas.drawPath(path, paint);

                canvas.drawCircle(x,y,10,paint);
                //canvas.drawLine(x-20, y-20, x+20, y+20, paint);
                //canvas.drawLine(x+20, y-20, x-20, y+20, paint);
            }
        });

       /* stressSeries.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(10);
                FlaggedDataPoint flaggedDataPoint = (FlaggedDataPoint) dataPoint;
                if(flaggedDataPoint.getFlag()) {
                    paint.setColor(Color.BLUE);
                }
                else{
                    paint.setColor(Color.GRAY);
                }
                canvas.drawRect(x-10,y-10,x+10,y+10, paint);
                //canvas.drawLine(x-20, y-20, x+20, y+20, paint);
                //canvas.drawLine(x+20, y-20, x-20, y+20, paint);
            }
        });*/

        smokingSeries.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(10);
                FlaggedDataPoint flaggedDataPoint = (FlaggedDataPoint) dataPoint;
                if(flaggedDataPoint.getFlag()) {
                    paint.setColor(Color.RED);
                }
                else{
                    paint.setColor(Color.GRAY);
                }
                //canvas.drawCircle(x,y,10,paint);
                canvas.drawLine(x-10, y-10, x+10, y+10, paint);
                canvas.drawLine(x+10, y-10, x-10, y+10, paint);
            }
        });

        List<String> yAxes = new ArrayList<>();
        yAxes.add("");
        yAxes.add("Medicines");
        yAxes.add("Exercise");
       // yAxes.add("Stress");
        yAxes.add("Smoking");

        healthGraph.getGridLabelRenderer().setLabelFormatter(new YAxisValueFormatter(getActivity(), yAxes));
        healthGraph.getGridLabelRenderer().setHumanRounding(false);
        healthGraph.getGridLabelRenderer().setNumHorizontalLabels(5);
        healthGraph.getGridLabelRenderer().setNumVerticalLabels(4);

        healthGraph.getViewport().setYAxisBoundsManual(true);
        healthGraph.getViewport().setMinY(0);
        healthGraph.getViewport().setMaxY(3);
        healthGraph.getViewport().setXAxisBoundsManual(true);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);
        healthGraph.getViewport().setMinX(cal.getTime().getTime());
        cal.add(Calendar.DATE, 10);
        healthGraph.getViewport().setMaxX(cal.getTime().getTime());
        healthGraph.getViewport().setScrollable(true);
    }

    private void populateDatapoints() {
        TrackMedicationDB trackMedicationDB = new TrackMedicationDB(getActivity());
        List<TrackMedication> trackMedicationList = trackMedicationDB.getAllInfo(GlobalClass.userID);
        Log.e("StatsFragment", "trackMedicationList.size() = "+trackMedicationList.size());
        int i=0;
        medPoints = new FlaggedDataPoint[trackMedicationList.size()];
        for(TrackMedication medication : trackMedicationList){
            medPoints[i] = new FlaggedDataPoint(medication.getDateTime(),1,medication.isHadAllMedicines());
            i++;
        }

        TrackExerciseDB trackExerciseDB = new TrackExerciseDB(getActivity());
        List<TrackExercise> trackExerciseList = trackExerciseDB.getAllInfo(GlobalClass.userID);

        int j=0;
        exerPoints = new FlaggedDataPoint[trackExerciseList.size()];
        for(TrackExercise exercise : trackExerciseList){
            exerPoints[j] = new FlaggedDataPoint(exercise.getDateTime(),2,exercise.isExercised());
            j++;
        }

        TrackSmokingDB trackSmokingDB = new TrackSmokingDB(getActivity());
        List<TrackSmoking> trackSmokingList = trackSmokingDB.getAllInfo(GlobalClass.userID);
        Log.e("StatsFragment","trackSmokingList.size() = "+trackSmokingList.size());

        int k=0;
        smokingPoints = new FlaggedDataPoint[trackSmokingList.size()];
        for(TrackSmoking smoking : trackSmokingList){
            smokingPoints[k] = new FlaggedDataPoint(smoking.getDateTime(), 3, smoking.isSmoked());
            k++;
        }

        TrackStressDB trackStressDB = new TrackStressDB(getActivity());
        List<TrackStress> trackStressList = trackStressDB.getAllInfo(GlobalClass.userID);

        int l=0;
        stressPoints = new FlaggedDataPoint[trackStressList.size()];
        for(TrackStress stress : trackStressList){
            stressPoints[l] = new FlaggedDataPoint(stress.getDateTime(), 4, stress.isStressed());
            l++;
        }

    }

    private void populateDummyData(){
        TrackWeightDB trackWeightDB = new TrackWeightDB(getActivity());
        TrackWeight trackWeight = new TrackWeight();
        trackWeight.setUserId(GlobalClass.userID);
        trackWeight.setMonthIndex(1);
        trackWeight.setWeight(65);
        trackWeight.setYear("2017");
        trackWeightDB.addEntry(trackWeight);


        trackWeight = new TrackWeight();
        trackWeight.setUserId(GlobalClass.userID);
        trackWeight.setMonthIndex(2);
        trackWeight.setWeight(63);
        trackWeight.setYear("2016");
        trackWeightDB.addEntry(trackWeight);

        trackWeight = new TrackWeight();
        trackWeight.setUserId(GlobalClass.userID);
        trackWeight.setMonthIndex(3);
        trackWeight.setWeight(67);
        trackWeight.setYear("2017");
        trackWeightDB.addEntry(trackWeight);

        trackWeight = new TrackWeight();
        trackWeight.setUserId(GlobalClass.userID);
        trackWeight.setMonthIndex(4);
        trackWeight.setWeight(69);
        trackWeight.setYear("2017");
        trackWeightDB.addEntry(trackWeight);

        trackWeight = new TrackWeight();
        trackWeight.setUserId(GlobalClass.userID);
        trackWeight.setMonthIndex(5);
        trackWeight.setWeight(72);
        trackWeight.setYear("2017");
        trackWeightDB.addEntry(trackWeight);

       /* BloodTestDB bloodTestDB = new BloodTestDB(getActivity());
        BloodTestResult bloodTestResult = new BloodTestResult();
        bloodTestResult.setHeamoglobin(9);
        bloodTestResult.setUreaCreatinine(1.6);
        bloodTestResult.setTotalCholestrol(21);
        bloodTestResult.setHighDensityLipoProtein(1.7);
        bloodTestResult.setLowDensityLipoProtein(1.6);
        bloodTestResult.setTriglycerides(35);
        bloodTestResult.setRandomPlasmaGlucose(25);
        bloodTestResult.setFastingPlasmaGlucose(10);
        bloodTestResult.setPostPrandialPlasmaGlucose(12);
        bloodTestDB.addEntry(GlobalClass.userID, "Sep 2017", bloodTestResult);*/



//        TrackStressDB trackStressDB = new TrackStressDB(getActivity());
//        TrackStress trackStress = new TrackStress();
//        Calendar calendar = Calendar.getInstance();
//        trackStress.setUserId(GlobalClass.userID);
//        trackStress.setDateTime(calendar.getTime());
//        trackStress.setMeditationHrs(0.5);
//        trackStress.setYogaHrs(0.25);
//        trackStressDB.addEntry(trackStress);
//
//        calendar.add(Calendar.DATE, -1);
//        trackStress.setUserId(GlobalClass.userID);
//        trackStress.setDateTime(calendar.getTime());
//        trackStress.setMeditationHrs(1);
//        trackStress.setYogaHrs(0.5);
//        trackStressDB.addEntry(trackStress);
//
//        calendar.add(Calendar.DATE, -1);
//        trackStress.setUserId(GlobalClass.userID);
//        trackStress.setDateTime(calendar.getTime());
//        trackStress.setMeditationHrs(1);
//        trackStress.setYogaHrs(1.25);
//        trackStressDB.addEntry(trackStress);
//
//        calendar.add(Calendar.DATE, -1);
//        trackStress.setUserId(GlobalClass.userID);
//        trackStress.setDateTime(calendar.getTime());
//        trackStress.setMeditationHrs(2);
//        trackStress.setYogaHrs(3);
//        trackStressDB.addEntry(trackStress);

//        TrackMedicationDB trackMedicationDB = new TrackMedicationDB(getActivity());
//        TrackMedication trackMedication = new TrackMedication();
//        trackMedication.setDateTime(new Date());
//        trackMedication.setUserId(GlobalClass.userID);
//        trackMedication.setHadAllMedicines(true);
//        trackMedicationDB.addEntry(trackMedication);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, -5);
//        trackMedication.setDateTime(calendar.getTime());
//        trackMedication.setUserId(GlobalClass.userID);
//        trackMedication.setHadAllMedicines(false);
//        trackMedicationDB.addEntry(trackMedication);
//
//        calendar.add(Calendar.DATE, -5);
//        trackMedication.setDateTime(calendar.getTime());
//        trackMedication.setUserId(GlobalClass.userID);
//        trackMedication.setHadAllMedicines(true);
//        trackMedicationDB.addEntry(trackMedication);
//
//        calendar.add(Calendar.DATE, -5);
//        trackMedication.setDateTime(calendar.getTime());
//        trackMedication.setUserId(GlobalClass.userID);
//        trackMedication.setHadAllMedicines(true);
//        trackMedicationDB.addEntry(trackMedication);
//
//        calendar.add(Calendar.DATE, -5);
//        trackMedication.setDateTime(calendar.getTime());
//        trackMedication.setUserId(GlobalClass.userID);
//        trackMedication.setHadAllMedicines(false);
//        trackMedicationDB.addEntry(trackMedication);

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if(getActivity() != null){
            if(menuVisible){
                ((TrackActivity) getActivity()).setActionBarTitle("Statistics");

            }
        }
        super.setMenuVisibility(menuVisible);
    }
}
