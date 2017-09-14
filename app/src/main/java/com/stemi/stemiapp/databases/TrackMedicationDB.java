package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stemi.stemiapp.model.TrackMedication;
import com.stemi.stemiapp.utils.GlobalClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by praburaam on 04/09/17.
 */

public class TrackMedicationDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = GlobalClass.DB_VERSION;

    private static final String DATABASE_NAME = GlobalClass.DB_NAME;

    public static final String TABLE_MEDICATION = "tbl_medication";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_DATE_TIME = "datetime";
    public static final String COLUMN_HAD_MEDICINES = "hadMedicines";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_MEDICATION + "( " + COLUMN_USER_ID
            + " text not null , " + COLUMN_DATE_TIME
            + " text not null , "+ COLUMN_HAD_MEDICINES
            + " integer );";

    public TrackMedicationDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_MEDICATION);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(TrackMedication stress){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, stress.getUserId());
            cv.put(COLUMN_DATE_TIME, simpleDateFormat.format(stress.getDateTime()));
            cv.put(COLUMN_HAD_MEDICINES, stress.isHadAllMedicines());

           long i = db.insertOrThrow(TABLE_MEDICATION, null, cv);
            Log.e("StatsFragment", "i = "+i);
        }
        catch(Exception e){
            Log.e("StatsFragment", "Exception e = "+e.getLocalizedMessage());
        }
        db.close();
    }

    public boolean isTableEmpty(){
        String query = "SELECT * FROM "+ TABLE_MEDICATION;
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        try{

            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                count++;
                c.moveToNext();
            }
        }
        catch(Exception e){

        }
        return count==0;
    }

    public List<TrackMedication> getAllInfo(String userid){
        List<TrackMedication> medicationList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM "+ TABLE_MEDICATION +" WHERE "+COLUMN_USER_ID+" = '"+userid+"' "
                    +"ORDER BY "+COLUMN_DATE_TIME;

            Cursor rows = db.rawQuery(query, null);
            rows.moveToFirst();

            while(!rows.isAfterLast()){
                TrackMedication medication = new TrackMedication();
                medication.setUserId(rows.getString(rows.getColumnIndex(COLUMN_USER_ID)));

                int withinlimitValue = rows.getInt(rows.getColumnIndex(COLUMN_HAD_MEDICINES));
                medication.setHadAllMedicines(withinlimitValue == 1);

                String timestamp = rows.getString(rows.getColumnIndex(COLUMN_DATE_TIME));

                Date dateValue = simpleDateFormat.parse(timestamp);
                medication.setDateTime(dateValue);

                medicationList.add(medication);
                rows.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return medicationList;
    }
    public List<TrackMedication> getMedicationTrackingInfo(String userid, String date1, String date2){
        List<TrackMedication> medicationList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM "+ TABLE_MEDICATION +" WHERE "+COLUMN_DATE_TIME
                    +" BETWEEN '"+date1+"' AND '"+ date2
                    +"' AND "+COLUMN_USER_ID+" = '"+userid+"' ";

            Cursor rows = db.rawQuery(query, null);
            rows.moveToFirst();

            while(!rows.isAfterLast()){
                TrackMedication medication = new TrackMedication();
                medication.setUserId(rows.getString(rows.getColumnIndex(COLUMN_USER_ID)));

                int withinlimitValue = rows.getInt(rows.getColumnIndex(COLUMN_HAD_MEDICINES));
                medication.setHadAllMedicines(withinlimitValue == 1);

                String timestamp = rows.getString(rows.getColumnIndex(COLUMN_DATE_TIME));

                Date dateValue = simpleDateFormat.parse(timestamp);
                medication.setDateTime(dateValue);

                medicationList.add(medication);
                rows.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return medicationList;
    }
}
