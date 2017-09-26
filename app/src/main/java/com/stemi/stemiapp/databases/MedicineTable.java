package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.GlobalClass;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 19-09-2017.
 */

public class MedicineTable {

    public static final String MED_TABLE_NAME = "dbMedicationDetails";

    //Column name for Medication
    private static final String MED_KEY_ID = "id";
    private  static final String MED_MEDICINE_DETAILS = "medicineDetails";
    private  static final String RELATED_PERSON = "relatedPerson";
    private static final String DATE = "medicineDate";


    public static final String CREATE_MEDICINE_TABLE = "CREATE TABLE " + MED_TABLE_NAME + "("
            + MED_KEY_ID + " INTEGER PRIMARY KEY,"
            + MED_MEDICINE_DETAILS + " TEXT,"
            + DATE + " TEXT,"
            + RELATED_PERSON + " TEXT"
            + ")";



    public void addMedicineDetails(MedicineDetails medicineDetails,String personName) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        //long count = getProfilesCount();
        //count = count+1;
        //   values.put(MED_KEY_ID,);
        String MedicineString = new Gson().toJson(medicineDetails);
        values.put(MED_MEDICINE_DETAILS, MedicineString);
        values.put(RELATED_PERSON, personName);
        values.put(DATE,medicineDetails.getDate());

        long id = db.insert(MED_TABLE_NAME, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        DatabaseManager.getInstance().closeDatabase();
    }
    //see that all database connection stuff is inside this method
    //so we don't need to open and close db connection outside this class

    // To get Number of profile
    public long getMedicineDetailsCount() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, MED_TABLE_NAME);
        //db.close();
        return cnt;
    }

    //Get all Medicine Details
    public ArrayList<String> getMedicine(String userId,String time){
        ArrayList<String> data=new ArrayList<String>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String query = "SELECT * FROM "+ MED_TABLE_NAME +" WHERE "+ RELATED_PERSON +" = '"+userId+"'";
        Cursor cursor = db.rawQuery(query,null);
        //Cursor cursor = db.query(MED_TABLE_NAME, new String[]{MED_MEDICINE_DETAILS},null, null, null, null, null);
        String fieldToAdd = null;
        while(cursor.moveToNext()){
            fieldToAdd = cursor.getString(1);
            Gson gsonObj = new Gson();
            MedicineDetails medicineDetails = gsonObj.fromJson(fieldToAdd , MedicineDetails.class);

            if(medicineDetails.getMedicineMorning().equals(time)){
                String MedicineString = gsonObj.toJson(medicineDetails);
                data.add(MedicineString);
            }
            if(medicineDetails.getMedicineAfternoon().equals(time)){
                String MedicineString = gsonObj.toJson(medicineDetails);
                data.add(MedicineString);
            }
            if(medicineDetails.getMedicineNight().equals(time)){
                String MedicineString = gsonObj.toJson(medicineDetails);
                data.add(MedicineString);
            }

        }
        cursor.close();  // dont forget to close the cursor after operation done
        return data;
    }

    //calling from splash screen to delete unwanted data from DB
    public void removeMedicalData(String userId) {
        ArrayList<String> data=new ArrayList<String>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        //SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM "+ MED_TABLE_NAME +" WHERE "+ RELATED_PERSON +" = '"+ userId+"'";
        Cursor cursor = db.rawQuery(query,null);
        String fieldToAdd = null;
        while(cursor.moveToNext()) {

            fieldToAdd = cursor.getString(1);
            Gson gsonObj = new Gson();
            MedicineDetails medicineDetails = gsonObj.fromJson(fieldToAdd, MedicineDetails.class);

            if(medicineDetails.getMedicineMorning().equals("") &&
                    medicineDetails.getMedicineAfternoon().equals("")
                    && medicineDetails.getMedicineNight().equals("")){
                db.delete(MED_TABLE_NAME, MED_MEDICINE_DETAILS + " ='" + fieldToAdd + "'", null);
            }
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    //Remove all medical details of particular person
    public void removeMedicalDetails(String name) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(MED_TABLE_NAME, RELATED_PERSON + " = ?", new String[] { name });
        DatabaseManager.getInstance().closeDatabase();
    }


    //Update medicine details from recyclerview

    public void updateMedicine(ArrayList<MedicineDetails> deletedList, Context context, int id) {
        AppSharedPreference appSharedPreference = new AppSharedPreference(context);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        //SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM "+ MED_TABLE_NAME +" WHERE "+ RELATED_PERSON +" = '"+ GlobalClass.userID +"'";
        Cursor cursor = db.rawQuery(query,null);
        String fieldToAdd = null;

        for (int i = 0; i < deletedList.size(); i++) {
            while (cursor.moveToNext()) {
                fieldToAdd = cursor.getString(1);
                Gson gsonObj = new Gson();
                MedicineDetails medicineDetails = gsonObj.fromJson(fieldToAdd, MedicineDetails.class);

                if (medicineDetails.equals(deletedList.get(i))
                        && GlobalClass.userID.equals(deletedList.get(i).getPersonName())) {
                    String old = new Gson().toJson(deletedList.get(i));
                    if(id == 1) {
                        for (int j = 0; j < deletedList.size(); j++) {
                            deletedList.get(j).setMedicineMorning("");
                            deletedList.get(j).setMedicineMorningTime("");
                        }
                    }else if(id == 2){
                        for (int j = 0; j < deletedList.size(); j++) {
                            deletedList.get(j).setMedicineAfternoon("");
                            deletedList.get(j).setMedicineNoonTime("");
                        }
                    }else if(id == 3){
                        for (int j = 0; j < deletedList.size(); j++) {
                            deletedList.get(j).setMedicineNight("");
                            deletedList.get(j).setMedicineNightTime("");
                        }
                    }
                    String s = new Gson().toJson(deletedList.get(i));

                    ContentValues cv = new ContentValues();
                    cv.put(MED_MEDICINE_DETAILS,s);
                    cv.put(RELATED_PERSON,deletedList.get(i).getPersonName());

                    long l = db.update(MED_TABLE_NAME, cv, MED_MEDICINE_DETAILS + "='" + old
                            + "' AND "+ RELATED_PERSON + "='"+
                            appSharedPreference.getProfileName(AppConstants.PROFILE_NAME) + "'",null);
                    Log.e(TAG, "removeMedicine: "+l );

                }else {
                    Log.e(TAG, "removeMedicine: "+"Nothing Deleted " );
                }
            }
            cursor.close();  //
            DatabaseManager.getInstance().closeDatabase();
        }
    }


    //Get single medicine Details
    public void getMedicineToEdit(ArrayList<MedicineDetails> beforeEditData,MedicineDetails afterEdit){

        String MedicineString = new Gson().toJson(afterEdit);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String old = new Gson().toJson(beforeEditData.get(0));
        ContentValues cv = new ContentValues();
        cv.put(MED_MEDICINE_DETAILS, MedicineString);

        long count = db.update(MED_TABLE_NAME, cv, MED_MEDICINE_DETAILS + "='" + old + "'" ,null);
        Log.e(TAG, "getMedicineToEdit: " + count );
        DatabaseManager.getInstance().closeDatabase();
    }
}
