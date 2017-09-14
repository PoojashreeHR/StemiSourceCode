package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.RegisteredUserDetails;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 20-07-2017.
 */

public class DBforUserDetails extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "dbStemiapp";

    private static final String TABLE_NAME = "dbuserDetails";
    private static final String MED_TABLE_NAME = "dbMedicationDetails";

    //column names for User Details
    private static final String KEY_ID = "id";
    private static final String USER_UID = "uniqueId";
    private static final String USER_NAME = "name";
    private static final String USER_AGE = "age";
    private static final String USER_GENDER = "gender";
    private static final String USER_PHONE = "phone";
    private static final String USER_ADDRESS = "address";
    private static final String USER_HEIGHT = "height";
    private static final String USER_WEIGHT = "weight";
    private static final String USER_WAIST = "waist";
    private static final String DO_YOU_SMOKE = "doYouSmoke";
    private static final String HAD_HEART_ATTACK = "hadHeartAttack";
    private static final String HAVE_DIABETES = "haveDiabetes";
    private static final String HIGH_BLOOD_PRESSURE = "highBloodPressure";
    private static final String HIGH_CHOLESTEROL = "highCholesterol";
    private static final String HAD_STROKE = "hadStroke";
    private static final String HAVE_ASTHMA = "haveAsthma";
    private static final String FAMILY_HEALTH = "familyHealth";
    private static final  String USER_PROFILR_URL = "profileUrl";


    //Column name for Medication
    private static final String MED_KEY_ID = "id";
    private  static final String MED_MEDICINE_DETAILS = "medicineDetails";
    private  static final String RELATED_PERSON = "relatedPerson";
  /*  private  static final String MED_MEDICINE_MORNING = "medicineMorning";
    private  static final String MED_MORNING_TIME = "medicineMorningTime";
    private  static final String MED_MEDICINE_AFTERNOON = "medicineAfternoon";
    private  static final String MED_NOON_TIME = "medicineNoonTime";
    private  static final String MED_MEDICINE_NIGHT = "medicineNight";
    private  static final String MED_NIGHT_TIME = "medicineNightTime";
    private  static final String MED_MEDICINE_KEY_ID = "id";
    private  static final String MED_MEDICINE_DAYS = "medicineDays";
    private  static final String MED_REMINDER = "medicineRemainder";
*/

    //sql query to creating User Details table in database
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + USER_UID + " TEXT,"
            + USER_NAME + " TEXT,"
            + USER_AGE + " TEXT,"
            + USER_GENDER + " TEXT,"
            + USER_PHONE + " TEXT,"
            + USER_ADDRESS + " REAL,"
            + USER_HEIGHT + " TEXT,"
            + USER_WEIGHT + " TEXT,"
            + USER_WAIST + " TEXT,"
            + DO_YOU_SMOKE + " TEXT,"
            + HAD_HEART_ATTACK + " TEXT,"
            + HAVE_DIABETES + " TEXT,"
            + HIGH_BLOOD_PRESSURE + " TEXT,"
            + HIGH_CHOLESTEROL + " TEXT,"
            + HAD_STROKE + " TEXT,"
            + HAVE_ASTHMA + " TEXT,"
            + FAMILY_HEALTH + " TEXT, "
            + USER_PROFILR_URL + " TEXT"
            + ")";

    //sql query to creating Medicine Details table in database

    private static final String CREATE_MEDICINE_TABLE = "CREATE TABLE " + MED_TABLE_NAME + "("
            + MED_KEY_ID + " INTEGER PRIMARY KEY,"
            + MED_MEDICINE_DETAILS + " TEXT,"
            + RELATED_PERSON + " TEXT"
            + ")";

    public DBforUserDetails(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_MEDICINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MED_TABLE_NAME);
        onCreate(db);
    }
// For User Details
    public String isExist(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String string = null;
        Log.e(TAG, "isExist: " + "SELECT " + USER_NAME + " FROM " + TABLE_NAME + " WHERE " + USER_NAME + "=" + "'" + userName + "'", null);
        Cursor cur = db.rawQuery("SELECT " + USER_NAME + " FROM " + TABLE_NAME + " WHERE " + "UPPER("+ USER_NAME + ")" + "=" + "\"" + userName.toUpperCase() + "\"", null);
        if (cur.moveToFirst()){
            for (int i = 0; i < cur.getCount(); i++){
             string = cur.getString(cur.getColumnIndex(USER_NAME));
             cur.moveToNext();
            }
        }
        cur.close();
        db.close();
        return string;
    }

    // To get Number of profile
    public long getProfilesCount() {
       SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        //db.close();
        return cnt;
    }

    //function for adding the note to database
    public void addEntry(RegisteredUserDetails registeredUserDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long count = getProfilesCount();
        count = count+1;
        values.put(USER_UID,registeredUserDetails.getUniqueId()+"_"+ count);
        values.put(USER_NAME,registeredUserDetails.getName());
        values.put(USER_AGE,registeredUserDetails.getAge());
        values.put(USER_GENDER,registeredUserDetails.getGender());
        values.put(USER_PHONE,registeredUserDetails.getPhone());
        values.put(USER_ADDRESS,registeredUserDetails.getAddress());
        values.put(USER_HEIGHT,registeredUserDetails.getHeight());
        values.put(USER_WEIGHT,registeredUserDetails.getWeight());
        values.put(USER_WAIST,registeredUserDetails.getWaist());
        values.put(DO_YOU_SMOKE,registeredUserDetails.getDo_you_smoke());
        values.put(HAD_HEART_ATTACK,registeredUserDetails.getHeart_attack());
        values.put(HAVE_DIABETES,registeredUserDetails.getDiabetes());
        values.put(HIGH_BLOOD_PRESSURE,registeredUserDetails.getBlood_pressure());
        values.put(HIGH_CHOLESTEROL,registeredUserDetails.getCholesterol());
        values.put(HAD_STROKE,registeredUserDetails.getHad_paralytic_stroke());
        values.put(HAVE_ASTHMA, registeredUserDetails.getHave_asthma());
        values.put(FAMILY_HEALTH,registeredUserDetails.getFamily_had_heart_attack());
        values.put(USER_PROFILR_URL,registeredUserDetails.getImgUrl());

        long id = db.insert(TABLE_NAME, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        db.close();

        //see that all database connection stuff is inside this method
        //so we don't need to open and close db connection outside this class

    }


    public void removeNote(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, USER_NAME + " = ?", new String[] { name });
        db.close();
    }


    public ArrayList<String> getRecords(){
        ArrayList<String> data=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{USER_NAME},null, null, null, null, null);
        String fieldToAdd=null;
        while(cursor.moveToNext()){
            fieldToAdd=cursor.getString(0);
            data.add(fieldToAdd);
        }
        cursor.close();  // dont forget to close the cursor after operation done
        return data;
    }

    public void addMedicineDetails(String medicineDetails,String personName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //long count = getProfilesCount();
        //count = count+1;
        //   values.put(MED_KEY_ID,);
        values.put(MED_MEDICINE_DETAILS, medicineDetails);
        values.put(RELATED_PERSON, personName);

        long id = db.insert(MED_TABLE_NAME, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        db.close();
    }
        //see that all database connection stuff is inside this method
        //so we don't need to open and close db connection outside this class

        // To get Number of profile
        public long getMedicineDetailsCount() {
            SQLiteDatabase db = this.getReadableDatabase();
            long cnt  = DatabaseUtils.queryNumEntries(db, MED_TABLE_NAME);
            //db.close();
            return cnt;
        }

    public ArrayList<String> getMedicine(){
        ArrayList<String> data=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MED_TABLE_NAME, new String[]{MED_MEDICINE_DETAILS},null, null, null, null, null);
        String fieldToAdd=null;
        while(cursor.moveToNext()){
            fieldToAdd=cursor.getString(0);
            data.add(fieldToAdd);
        }
        cursor.close();  // dont forget to close the cursor after operation done
        return data;
    }


    public void removeMedicalDetails(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MED_TABLE_NAME, RELATED_PERSON + " = ?", new String[] { name });
        db.close();
    }

}
