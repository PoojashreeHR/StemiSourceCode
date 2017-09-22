package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stemi.stemiapp.StemiApplication;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.App;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.GlobalClass;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 20-07-2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = GlobalClass.DB_VERSION;
    private static final String DATABASE_NAME = GlobalClass.DB_NAME;


    public DBHelper() {super((StemiApplication.getContext()), DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDetailsTable.CREATE_TABLE);
        db.execSQL(MedicineTable.CREATE_MEDICINE_TABLE);
        db.execSQL(DBForTrackActivities.CREATE_USER_ACTIVITY_TABLE);
        db.execSQL(BloodTestTable.CREATE_BLOOD_TEST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserDetailsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MedicineTable.MED_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBForTrackActivities.USER_ACTIVITIES);
        db.execSQL("DROP TABLE IF EXISTS " + BloodTestTable.BLOOD_TEST_TABLE);
        onCreate(db);
    }

}
