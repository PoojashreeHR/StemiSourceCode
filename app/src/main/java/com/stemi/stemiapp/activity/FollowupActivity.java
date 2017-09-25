package com.stemi.stemiapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.databases.FollowupsDB;
import com.stemi.stemiapp.utils.GlobalClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowupActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txt_followup_date)
    TextView followupDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup);
        ButterKnife.bind(this);

        toolbar.setTitle("Follow up");
        toolbar.setTitleTextColor(getResources().getColor(R.color.appBackground));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        FollowupsDB followupsDB = new FollowupsDB(this);
        String nextDate = followupsDB.getNextFollowupDate(GlobalClass.userID);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date nextDateObj = simpleDateFormat.parse(nextDate);
            SimpleDateFormat commonDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            followupDate.setText(commonDateFormat.format(nextDateObj));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
