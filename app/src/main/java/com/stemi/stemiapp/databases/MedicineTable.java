package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.MedicinesTakenOrNot;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.GlobalClass;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 19-09-2017.
 */

public class MedicineTable {

    public static final String MED_TABLE_NAME = "dbMedicationDetails";

    //Column name for Medication
    private static final String MED_KEY_ID = "id";
    private static final String MED_MEDICINE_DETAILS = "medicineDetails";
    private static final String RELATED_PERSON = "relatedPerson";
    private static final String DATE = "medicineDate";


    public static final String CREATE_MEDICINE_TABLE = "CREATE TABLE " + MED_TABLE_NAME + "("
            + MED_KEY_ID + " INTEGER PRIMARY KEY,"
            + MED_MEDICINE_DETAILS + " TEXT,"
            + DATE + " TEXT,"
            + RELATED_PERSON + " TEXT"
            + ")";



    public void addMedicineDetails(MedicineDetails medicineDetails, String personName) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        String MedicineString = new Gson().toJson(medicineDetails);
        values.put(MED_MEDICINE_DETAILS, MedicineString);
        values.put(RELATED_PERSON, personName);
        values.put(DATE, medicineDetails.getDate());

        long id = db.insert(MED_TABLE_NAME, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        DatabaseManager.getInstance().closeDatabase();
    }


    public boolean getDate(String date1,String userId){
        String query = "SELECT * FROM " + MED_TABLE_NAME + " WHERE " + RELATED_PERSON + " = '" + userId +"' AND "
                +DATE+" = '"+date1+"'";
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
/*
    public void updateMedicineData(MedicineDetails medicineDetails, int value, boolean checked) {
        MedicineDetails meds = new MedicineDetails();



        meds.setDate(medicineDetails.getDate());
        meds.setMoringChecked(medicineDetails.getMoringChecked());
        meds.setAfternoonChecked(medicineDetails.getAfternoonChecked());
        meds.setNightChecked(medicineDetails.getNightChecked());
        meds.setAllChecked(medicineDetails.getAllChecked());
        meds.setMedicineNightTime(medicineDetails.getMedicineNightTime());
        meds.setMedicineNoonTime(medicineDetails.getMedicineNoonTime());
        meds.setMedicineAfternoon(medicineDetails.getMedicineAfternoon());
        meds.setMedicineMorningTime(medicineDetails.getMedicineMorningTime());
        meds.setPersonName(medicineDetails.getPersonName());
        meds.setMedicineDays(medicineDetails.getMedicineDays());
        meds.setMedicineColor(medicineDetails.getMedicineColor());
        meds.setMedicineType(medicineDetails.getMedicineType());
        meds.setMedicineName(medicineDetails.getMedicineName());
        meds.setMedicineRemainder(medicineDetails.getMedicineRemainder());
        meds.setMedicineMorning(medicineDetails.getMedicineMorning());
        meds.setMedicineNight(medicineDetails.getMedicineNight());
        // meds.setDate(medicineDetails.getDate());


        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String medName = medicineDetails.getMedicineName();
        String medDate = medicineDetails.getDate();
        String query = "SELECT * FROM " + MED_TABLE_NAME;

        MedicineDetails stringMedicine = null;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {

            Gson gsonObj = new Gson();

            String medValue = cursor.getString(cursor.getColumnIndex(MED_MEDICINE_DETAILS));
            stringMedicine = gsonObj.fromJson(medValue, MedicineDetails.class);
            if (stringMedicine.getMedicineName().equals(medName)
                    && stringMedicine.getDate().equals(medDate)) {
                switch (value) {
                    case 1:
                        medicineDetails.setMoringChecked(checked);
                        break;
                    case 2:
                        medicineDetails.setAfternoonChecked(checked);
                        break;
                    case 3:
                        medicineDetails.setNightChecked(checked);
                        break;
                }

                break;
            }
        }

        ContentValues cv = new ContentValues();
        cv.put(MED_MEDICINE_DETAILS, new Gson().toJson(medicineDetails));
        long l = db.update(MED_TABLE_NAME, cv, "'" + new Gson().toJson(stringMedicine) + "' = '" +
                new Gson().toJson(meds) + "'", null);
        Log.e(TAG, "updateMedicineData: " + l);


    }

*/

    // To get Number of profile
    public long getMedicineDetailsCount() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, MED_TABLE_NAME);
        //db.close();
        return cnt;
    }


//Calling from AddMedicineFragment to avoid duplicate mecicines
    public String isExist(MedicineDetails medicineDetails, String medicineName) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String string = null;
        String query = "SELECT * FROM " + MED_TABLE_NAME + " WHERE " + RELATED_PERSON + " = '" + GlobalClass.userID + "'";
        Cursor cursor = db.rawQuery(query, null);
        String fieldToAdd = null;
        while (cursor.moveToNext()) {
            fieldToAdd = cursor.getString(cursor.getColumnIndex(MED_MEDICINE_DETAILS));
            Gson gsonObj = new Gson();
            MedicineDetails stringMedicine = gsonObj.fromJson(fieldToAdd, MedicineDetails.class);

            if(stringMedicine.getMedicineMorning()!=null) {
                for (int i = 0; i < stringMedicine.getMedicineMorning().size(); i++) {
                    if (stringMedicine.getMedicineMorning().get(i).getMedName().equals(medicineName)) {
                        string = medicineName;
                    }
                }
            }
            if(stringMedicine.getMedicineAfternoon()!=null) {
                for (int i = 0; i < stringMedicine.getMedicineAfternoon().size(); i++) {
                    if (stringMedicine.getMedicineAfternoon().get(i).getMedName().equals(medicineName)) {
                        string = medicineName;
                    }
                }
            }

            if(stringMedicine.getMedicineNight()!=null) {
                for (int i = 0; i < stringMedicine.getMedicineNight().size(); i++) {
                    if (stringMedicine.getMedicineNight().get(i).getMedName().equals(medicineName)) {
                        string = medicineName;
                    }
                }
            }
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return string;
    }
/*
    public boolean getDate(String date1, String userId) {
        String query = "SELECT * FROM " + MED_TABLE_NAME + " WHERE " + RELATED_PERSON + " = '" + userId + "'";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        boolean count = false;
        try {
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String date = c.getString(2);
                if (date.equals(date1)) {
                    count = true;
                    break;
                } else {
                    c.moveToNext();
                }
            }
        } catch (Exception e) {
        }
        return count;
    }*/

    //Calling from MedicineFragment to get medicines for particular date
    public ArrayList<String> getAllMedicineDEtails(String userId, String date){
        ArrayList<String> data=new ArrayList<String>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String query = "SELECT * FROM " + MED_TABLE_NAME + " WHERE " + RELATED_PERSON + " = '" + userId + "' AND "
                + DATE + " = '" + date+ "'";
        Log.e("db", "query = "+query);
        Cursor cursor = db.rawQuery(query,null);
        //Cursor cursor = db.query(MED_TABLE_NAME, new String[]{MED_MEDICINE_DETAILS},null, null, null, null, null);
        String fieldToAdd = null;
        while(cursor.moveToNext()) {
            fieldToAdd = cursor.getString(cursor.getColumnIndex(MED_MEDICINE_DETAILS));
            Gson gsonObj = new Gson();
            MedicineDetails medicineDetails = gsonObj.fromJson(fieldToAdd, MedicineDetails.class);
            String MedicineString = gsonObj.toJson(medicineDetails);
            Log.e("db", "MedicineString = "+ MedicineString);
            data.add(MedicineString);
        }
        return data;
    }

    public void updateDataWithDate(String personName, String date, MedicineDetails medicineDetails){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String query = "SELECT * FROM " + MED_TABLE_NAME + " WHERE " + RELATED_PERSON + " = '" + personName + "' AND "
                + DATE + " = '" + date+ "'";

        ContentValues cv = new ContentValues();
        cv.put(MED_MEDICINE_DETAILS,new Gson().toJson(medicineDetails));
       long l =  db.update(MED_TABLE_NAME,cv, RELATED_PERSON + " = '" + personName + "' AND "
                + DATE + " = '" + date+ "'", null);
        Log.e(TAG, "updateDataWithDate: "+l );


    }

    //calling from MedicineFragment Info button to getMedicine Details
    public ArrayList<String> getMedicine(String userId, String time,String date) {
        ArrayList<String> data = new ArrayList<String>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String query = "SELECT * FROM " + MED_TABLE_NAME + " WHERE " + RELATED_PERSON + " = '" + userId +"' AND "
                + DATE + " = '" + date+ "'";

        Cursor cursor = db.rawQuery(query, null);

        //Cursor cursor = db.query(MED_TABLE_NAME, new String[]{MED_MEDICINE_DETAILS},null, null, null, null, null);
        String fieldToAdd = null;
        while (cursor.moveToNext()) {
            fieldToAdd = cursor.getString(cursor.getColumnIndex(MED_MEDICINE_DETAILS));
            Gson gsonObj = new Gson();
            MedicineDetails medicineDetails = gsonObj.fromJson(fieldToAdd, MedicineDetails.class);

            if(medicineDetails.getMedicineMorning() != null) {
                for (int i = 0; i < medicineDetails.getMedicineMorning().size(); i++) {
                    String MedicineString = gsonObj.toJson(medicineDetails);
                    data.add(MedicineString);
                }
            }else {
                Log.e(TAG, "getMedicine: Morning Empty" );
            }
            if(medicineDetails.getMedicineAfternoon() !=null ) {
                for (int i = 0; i < medicineDetails.getMedicineAfternoon().size(); i++) {
                    String MedicineString = gsonObj.toJson(medicineDetails);
                    data.add(MedicineString);
                }
            }else {
                Log.e(TAG, "getMedicine: Afternoon Empty" );

            }
            if(medicineDetails.getMedicineNight() !=null) {
                for (int i = 0; i < medicineDetails.getMedicineNight().size(); i++) {
                    String MedicineString = gsonObj.toJson(medicineDetails);
                    data.add(MedicineString);
                }
            }else {
                Log.e(TAG, "getMedicine: Night Empty" );

            }
        }
        cursor.close();  // dont forget to close the cursor after operation done
        return data;
    }

    //calling from splash screen to delete unwanted data from DB
    public void removeMedicalData(String userId) {
        ArrayList<String> data = new ArrayList<String>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        //SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + MED_TABLE_NAME + " WHERE " + RELATED_PERSON + " = '" + userId + "'";
        Cursor cursor = db.rawQuery(query, null);
        String fieldToAdd = null;
        while (cursor.moveToNext()) {

            fieldToAdd = cursor.getString(1);
            Gson gsonObj = new Gson();
            MedicineDetails medicineDetails = gsonObj.fromJson(fieldToAdd, MedicineDetails.class);

            if (medicineDetails.getMedicineMorning().equals("") &&
                    medicineDetails.getMedicineAfternoon().equals("")
                    && medicineDetails.getMedicineNight().equals("")) {
                db.delete(MED_TABLE_NAME, MED_MEDICINE_DETAILS + " ='" + fieldToAdd + "'", null);
            }
        }
        DatabaseManager.getInstance().closeDatabase();
    }


    //Remove all medical details of particular person
    public void removeMedicalDetails(String name) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(MED_TABLE_NAME, RELATED_PERSON + " = ?", new String[]{name});
        DatabaseManager.getInstance().closeDatabase();

    }


    //Update medicine details from recyclerview

    public void updateMedicine(ArrayList<MedicineDetails> deletedList, Context context, int id) {
        AppSharedPreference appSharedPreference = new AppSharedPreference(context);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        //SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + MED_TABLE_NAME + " WHERE " + RELATED_PERSON + " = '" + GlobalClass.userID + "'";
        Cursor cursor = db.rawQuery(query, null);
        String fieldToAdd = null;

        for (int i = 0; i < deletedList.size(); i++) {
            while (cursor.moveToNext()) {
                fieldToAdd = cursor.getString(1);
                Gson gsonObj = new Gson();
                MedicineDetails medicineDetails = gsonObj.fromJson(fieldToAdd, MedicineDetails.class);

                if (medicineDetails.equals(deletedList.get(i))
                        && GlobalClass.userID.equals(deletedList.get(i).getPersonName())) {
                    String old = new Gson().toJson(deletedList.get(i));
                    if (id == 1) {
                       /* for (int j = 0; j < deletedList.size(); j++) {
                            deletedList.get(j).setMedicineMorning("");
                            deletedList.get(j).setMedicineMorningTime("");
                        }
                    } else if (id == 2) {
                        for (int j = 0; j < deletedList.size(); j++) {
                            deletedList.get(j).setMedicineAfternoon("");
                            deletedList.get(j).setMedicineNoonTime("");
                        }
                    } else if (id == 3) {
                        for (int j = 0; j < deletedList.size(); j++) {
                            deletedList.get(j).setMedicineNight("");
                            deletedList.get(j).setMedicineNightTime("");
                        }*/
                    }
                    String s = new Gson().toJson(deletedList.get(i));

                    ContentValues cv = new ContentValues();
                    cv.put(MED_MEDICINE_DETAILS, s);
                    cv.put(RELATED_PERSON, deletedList.get(i).getPersonName());

                    long l = db.update(MED_TABLE_NAME, cv, MED_MEDICINE_DETAILS + "='" + old
                            + "' AND " + RELATED_PERSON + "='" +
                            GlobalClass.userID + "'", null);
                    Log.e(TAG, "removeMedicine: " + l);

                } else {
                    Log.e(TAG, "removeMedicine: " + "Nothing Deleted ");
                }
            }
            cursor.close();  //
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public boolean isMedicineExist(String date){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String query = "SELECT * FROM " + MED_TABLE_NAME + " WHERE " + RELATED_PERSON + " = '" + GlobalClass.userID + "' AND "
                + DATE + " = '" + date+ "'";
        Cursor cursor = db.rawQuery(query,null);
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


    //Get single medicine Details
    public void getMedicineToEdit(ArrayList<MedicineDetails> beforeEditData, MedicineDetails afterEdit) {
        String old = "";
        String MedicineString = new Gson().toJson(afterEdit);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        for (int i = 0; i < beforeEditData.size(); i++) {
            old = new Gson().toJson(beforeEditData.get(i));
        }
        ContentValues cv = new ContentValues();
        cv.put(MED_MEDICINE_DETAILS, MedicineString);

        long count = db.update(MED_TABLE_NAME, cv, MED_MEDICINE_DETAILS + "='" + old + "'", null);
        Log.e(TAG, "getMedicineToEdit: " + count);
        DatabaseManager.getInstance().closeDatabase();
    }
}
