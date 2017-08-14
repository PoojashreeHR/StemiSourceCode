package com.stemi.stemiapp.samples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.stemi.stemiapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartSampleActivity extends AppCompatActivity {

    private int startIndex = -1;
    private LineChart chart;
    private String userid = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_sample);

        chart = (LineChart) findViewById(R.id.chart);
        Button prevButton = (Button)findViewById(R.id.prevButton);
        Button nextButton = (Button)findViewById(R.id.nextButton);

        populateFoodsDB();//populate dummy values

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIndex--;
                rePopulateChart();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startIndex < -1) {
                    startIndex++;
                    rePopulateChart();
                }
            }
        });



        rePopulateChart();

    }

    private void rePopulateChart() {

        List<ILineDataSet> dataSets = new ArrayList<>();

        for(int j=1;j<=4;j++){
            List<Entry> entries = new ArrayList<>();

            for(int i=0;i<7;i++){
                entries.add(new Entry(i,j));
            }

            LineDataSet dataSet = new LineDataSet(entries, "");
            dataSet.setColor(this.getResources().getColor(android.R.color.transparent));
            List<Integer> colors = new ArrayList<Integer>();
            colors.add(this.getResources().getColor(R.color.colorGreen));
            List<Integer> colorsList = getColorsFor(j);
            if(colorsList.size() != 0){
                dataSet.setCircleColors(colorsList);
            }
            dataSet.setCircleRadius(8f);
            dataSet.setDrawValues(false);
            dataSets.add(dataSet);
        }

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        // Displaying an image instead of circle
        // Bitmap starBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.green_tick);
        // chart.setRenderer(new ImageLineChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), starBitmap));
        chart.invalidate();


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        List<String> datesList = populateDates(startIndex);

        xAxis.setValueFormatter(new XAxisValueFormatter(datesList));
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1.0f);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1.0f);
        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add("Food ");
        strings.add("Smoking ");
        strings.add("Exercise ");
        strings.add("Medication ");

        yAxis.setValueFormatter(new XAxisValueFormatter(strings));

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);
        chart.setDescription(null);
        chart.invalidate();
    }

    private List<Integer> getColorsFor(int index) {
        List<Integer> colors = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 7*(startIndex+1));
        Date date = c.getTime();
        List<Boolean> data = getDataFromDB(index, date);
        // List<Boolean> data = getDataFromDB(index, new Date());
        if(data.size() == 0){
            for(int i=0; i<7; i++) {
                colors.add(this.getResources().getColor(R.color.colorGrey));
            }
        }
        else {
            for (int i = 0; i < 7; i++) {
                if ( data.size()<= i || !data.get(i) ) {
                    colors.add(this.getResources().getColor(R.color.colorGrey));
                } else {
                    colors.add(this.getResources().getColor(R.color.colorGreen));
                }
            }
        }
        return colors;
    }

    private List<Boolean> getDataFromDB(int index, Date date) {
        //int[] data = {1,1,0,1,1,0,1};
        List<Boolean> dataSet = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -6);
        Date startDate = c.getTime();
        c.add(Calendar.DATE, 6);
        Date endDate = c.getTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = format.format(startDate);
        String endDateStr = format.format(endDate);

        SqliteDataSource sqliteDataSource = new SqliteDataSource(this);
        List<TrackFood> foodList = sqliteDataSource.getFoodTrackingInfo(userid, startDateStr, endDateStr);

        for(TrackFood food: foodList){
            dataSet.add(food.isWithinLimit());
        }
        return dataSet;
    }

    private List<String> populateDates(int startFactor) {
        List<String> dates = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int toSubtract = dayOfWeek - 1;
        // c.add(Calendar.DATE, -toSubtract);
        c.add(Calendar.DATE, 7*startFactor);

        for(int i=0; i<7; i++) {
            c.add(Calendar.DATE, 1);
            Date calDate = c.getTime();
            String dateStr = simpleDateFormat.format(calDate);
            dates.add(dateStr);
        }
        return dates;
    }

    private void populateFoodsDB(){
        //List<TrackFood> foodList = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -7);

        SqliteDataSource sqliteDataSource = new SqliteDataSource(this);
        if(sqliteDataSource.isTableEmpty()){


            TrackFood food = new TrackFood();
            c.add(Calendar.DATE, 1);
            food.setUserId(userid);
            food.setDateTime(c.getTime());
            food.setWithinLimit(true);
            sqliteDataSource.addEntry(food);
            //foodList.add()


            c.add(Calendar.DATE, 1);
            food.setUserId(userid);
            food.setDateTime(c.getTime());
            food.setWithinLimit(false);
            sqliteDataSource.addEntry(food);


            c.add(Calendar.DATE, 1);
            food.setUserId(userid);
            food.setDateTime(c.getTime());
            food.setWithinLimit(true);
            sqliteDataSource.addEntry(food);


            c.add(Calendar.DATE, 1);
            food.setUserId(userid);
            food.setDateTime(c.getTime());
            food.setWithinLimit(false);
            sqliteDataSource.addEntry(food);


            c.add(Calendar.DATE, 1);
            food.setUserId(userid);
            food.setDateTime(c.getTime());
            food.setWithinLimit(false);
            sqliteDataSource.addEntry(food);

            c.add(Calendar.DATE, 1);
            food.setUserId(userid);
            food.setDateTime(c.getTime());
            food.setWithinLimit(true);
            sqliteDataSource.addEntry(food);

            c.add(Calendar.DATE, 1);
            food.setUserId(userid);
            food.setDateTime(c.getTime());
            food.setWithinLimit(true);
            sqliteDataSource.addEntry(food);

        }
    }
}
