package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.utils.GlobalClass;

import java.util.ArrayList;

/**
 * Created by Pooja on 10-10-2017.
 */

public class MedicineDetailsTable {
    public static final String TABLE_NAME =  "dbMedicineTrack";

    private static final String MED_KEY_ID = "id";
    private  static final String USER_ID = "uid";
    private static final String DATE = "medicineDate";
    private  static final String MEDICINE_TAKEN = "medicineTaken";
    private static final String MEDICINE_DETAILS = "medicineDetails";

    public static final String CREATE_MEDICINE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + MED_KEY_ID + " INTEGER PRIMARY KEY,"
            + MEDICINE_TAKEN + " TEXT,"
            + MEDICINE_DETAILS + " TEXT,"
            + DATE + " TEXT,"
            + USER_ID + " TEXT"
            + ")";

    public ArrayList<String> getAllMedicineDEtails(String userId, String date){
        ArrayList<String> data=new ArrayList<String>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = '" + userId + "' AND "
                + DATE + " = '" + date+ "'";        Cursor cursor = db.rawQuery(query,null);
        //Cursor cursor = db.query(MED_TABLE_NAME, new String[]{MED_MEDICINE_DETAILS},null, null, null, null, null);
        String fieldToAdd = null;
        while(cursor.moveToNext()) {
            fieldToAdd = cursor.getString(1);
            Gson gsonObj = new Gson();
            MedicineDetails medicineDetails = gsonObj.fromJson(fieldToAdd, MedicineDetails.class);
            String MedicineString = gsonObj.toJson(medicineDetails);
            data.add(MedicineString);
        }
        return data;
    }

    public boolean getDate(String date1,String userId){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = '" + userId + "'";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        boolean count = false;
        try{
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                String date = c.getString(2);
                if(date.equals(date1)){
                    count = true;
                    break;
                }else {
                    c.moveToNext();
                }
            }
        }
        catch(Exception e){
        }
        return count;
    }

    public void addMedicineWithDate(String date, MedicineDetails Medicine){
        String MedicineString = new Gson().toJson(Medicine);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(MEDICINE_DETAILS, MedicineString);
        values.put(DATE, date);
        values.put(USER_ID, GlobalClass.userID);

        long id = db.insert(TABLE_NAME, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        DatabaseManager.getInstance().closeDatabase();
/*

            long count = db.update(MED_TABLE_NAME, cv, MED_MEDICINE_DETAILS + "='" + old + "'" ,null);
            Log.e(TAG, "getMedicineToEdit: " + count );
*/

    }

}
