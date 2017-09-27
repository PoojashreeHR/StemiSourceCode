package com.stemi.stemiapp.databases;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.fragments.TrackFragment;
import com.stemi.stemiapp.model.BloodTestResult;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.TrackMedication;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by praburaam on 04/09/17.
 */

public class TrackMedicationDB {
    Context mContext;
/*    private static final int DATABASE_VERSION = GlobalClass.DB_VERSION;

    private static final String DATABASE_NAME = GlobalClass.DB_NAME;*/

    public static final String TABLE_MEDICATION = "tbl_medication";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_DATE_TIME = "_datetime";
    public static final String COLUMN_HAD_MEDICINES = "hadMedicines";

    public static final String DATABASE_CREATE = "create table "
            + TABLE_MEDICATION + "( " + COLUMN_USER_ID
            + " text not null , " + COLUMN_DATE_TIME
            + " text not null , "+ COLUMN_HAD_MEDICINES
            + " integer );";

  /*  public TrackMedicationDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }*/

/*    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        onCreate(sqLiteDatabase);
    }*/
    public TrackMedicationDB(Context mContext){
        this.mContext = mContext;
    }

    public void addEntry(TrackMedication stress){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String dateTime = simpleDateFormat.format(stress.getDateTime());
        if(isEntryExists(stress.getUserId(), dateTime)){
            Log.e("Add Entry", "Updating entry");
            buidDialog(mContext,stress.getUserId(), simpleDateFormat.format(stress.getDateTime()),stress);
        }else {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            try {

                ContentValues cv = new ContentValues();
                cv.put(COLUMN_USER_ID, stress.getUserId());
                cv.put(COLUMN_DATE_TIME, simpleDateFormat.format(stress.getDateTime()));
                cv.put(COLUMN_HAD_MEDICINES, stress.isHadAllMedicines());

                long i = db.insertOrThrow(TABLE_MEDICATION, null, cv);
                Log.e("StatsFragment", "i = " + i);
                EventBus.getDefault().post(new MessageEvent("Hello!"));
                ((TrackActivity) mContext ).setActionBarTitle("Track");

            } catch (Exception e) {
                Log.e("StatsFragment", "Exception e = " + e.getLocalizedMessage());
            }
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public boolean isEntryExists(String userId, String dateTag){
        String query = "SELECT * FROM "+ TABLE_MEDICATION +" WHERE "+ COLUMN_USER_ID+" = '"+userId+"' AND "
                +COLUMN_DATE_TIME+" = '"+dateTag+"'";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int i = c.getCount();
        int count = 0;
        try{
           // c.moveToFirst();
            while(!c.isAfterLast()){
                count++;
                c.moveToNext();
            }

        }
        catch(Exception e){
            Log.e("Error Msg", e.getLocalizedMessage());
        }
        c.close();
        return count>0;
    }

    public void updateEntry(String userId, String dateTag, TrackMedication stress){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, userId);
            cv.put(COLUMN_DATE_TIME, dateTag);
            cv.put(COLUMN_HAD_MEDICINES, stress.isHadAllMedicines());
            String whereClause = COLUMN_USER_ID+" = '"+userId+"' AND "+COLUMN_DATE_TIME+" = '"+dateTag+"'";
            db.update(TABLE_MEDICATION, cv, whereClause, null);
        }
        catch(Exception e){
            Log.e(TAG, e.getLocalizedMessage());
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public boolean isTableEmpty(){
        String query = "SELECT * FROM "+ TABLE_MEDICATION;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
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


    public void buidDialog(final Context mContext, final String userId, final String date, final TrackMedication trackMedication){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("AlertDialog with No Buttons");
        builder.setMessage("Old Entry Exist !! Do you want to save this?");
        //Yes Button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateEntry(userId, date,trackMedication);
                EventBus.getDefault().post(new MessageEvent("Hello!"));
                Log.i("Code2care ", "Yes button Clicked!");
            }
        });

        //No Button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Code2care ","No button Clicked!");
                dialog.dismiss();
                ((TrackActivity) mContext).showFragment(new TrackFragment());
                ((TrackActivity) mContext).setActionBarTitle("Track");


            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(mContext.getResources().getColor(R.color.appBackground));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(mContext.getResources().getColor(R.color.appBackground));
    }

    public void createIfNotExists() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_MEDICATION;
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                cursor.moveToNext();
            }

            cursor.close();
        } catch (Exception e) {
            if (e.getLocalizedMessage().contains("no such table")) {
               // onCreate(db);
            }
        }
    }
}
