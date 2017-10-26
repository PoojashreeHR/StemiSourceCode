package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.GlobalClass;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 20-07-2017.
 */

public class UserDetailsTable {
    public static final String TABLE_NAME = "dbuserDetails";
    private final String loginId;


    public UserDetailsTable(Context ctx){
        AppSharedPreference appSharedPreference = new AppSharedPreference(ctx);
        loginId = appSharedPreference.getLoginId();
    }

    //column names for User Details
    private static final String KEY_ID = "id";
    private static final String LOGIN_ID = "loginId";
    private static final String USER_UID = "uniqueId";
    private static final String USER_NAME = "name";
    private static final String USER_AGE = "age";
    private static final String USER_GENDER = "gender";
    private static final String USER_PHONE = "phone";
    private static final String USER_EMAIL = "email";
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

    //sql query to creating User Details table in database
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + LOGIN_ID+" TEXT,"
            + USER_UID + " TEXT,"
            + USER_NAME + " TEXT,"
            + USER_AGE + " TEXT,"
            + USER_GENDER + " TEXT,"
            + USER_PHONE + " TEXT,"
            + USER_EMAIL + " TEXT,"
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


    public String isExist(String userName) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String string = null;
        Log.e(TAG, "isExist: " +"SELECT " + USER_NAME + " FROM " + TABLE_NAME + " WHERE " + "UPPER("+ USER_NAME + ")" + "=" + "\"" + userName.toUpperCase() + "\"" + " AND " + LOGIN_ID + " = "+ "'" +loginId + "'", null);
        Cursor cur = db.rawQuery("SELECT " + USER_NAME + " FROM " + TABLE_NAME + " WHERE " + "UPPER("+ USER_NAME + ")" + "=" + "\"" + userName.toUpperCase() + "\"" + " AND " + LOGIN_ID + "= "+  "'"+ loginId + "'", null);
        if (cur.moveToFirst()){
            for (int i = 0; i < cur.getCount(); i++){
                string = cur.getString(cur.getColumnIndex(USER_NAME));
                cur.moveToNext();
            }
        }
        cur.close();
        DatabaseManager.getInstance().closeDatabase();
        return string;
    }

    // To get Number of profile
    public long getProfilesCount(String loginId) {
        long cnt=0;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+LOGIN_ID+" = '"+loginId+"'";
        Cursor cursor = db.rawQuery(sql,null);
        Log.e(TAG, "getProfilesCount: FROM DB: "+cursor.getCount());
        cnt = cursor.getCount();
       // Cursor cursor = db.query(TABLE_NAME, new String[]{USER_NAME},LOGIN_ID+" = '"+loginId+"'", null, null, null, null);
      /*  while(cursor.moveToFirst()) {
            Log.e(TAG, "getProfilesCount: FROM DB: "+cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cnt = cursor.getString(cursor.getColumnIndex(LOGIN_ID));
                cursor.moveToNext();
            }
          //  cnt = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
            //db.close();
        }
        long count = Long.parseLong(cnt);*/
        cursor.close();
        return cnt;
    }

    //function for adding the note to database
    public String addEntry(RegisteredUserDetails registeredUserDetails,Context context) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
      /*  long count = getProfilesCount();
        count = count+1;
        String uid = registeredUserDetails.getUniqueId()+"_"+ count;*/
        values.put(USER_UID,registeredUserDetails.getUniqueId());
        values.put(LOGIN_ID, loginId);
        values.put(USER_NAME,registeredUserDetails.getName());
        values.put(USER_AGE,registeredUserDetails.getAge());
        values.put(USER_GENDER,registeredUserDetails.getGender());
        values.put(USER_PHONE,registeredUserDetails.getPhone());
        values.put(USER_EMAIL, registeredUserDetails.getEmail());
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
        DatabaseManager.getInstance().closeDatabase();
       // Toast.makeText(context, "One row added successfully "+ id, Toast.LENGTH_SHORT).show();

        //see that all database connection stuff is inside this method
        //so we don't need to open and close db connection outside this class
        return registeredUserDetails.getUniqueId();

    }


    public void removeUserDetails(String name) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_NAME, USER_NAME + " = ?", new String[] { name });
        DatabaseManager.getInstance().closeDatabase();
    }


    public ArrayList<String> getRecords(){
        ArrayList<String> data=new ArrayList<String>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{USER_NAME},LOGIN_ID+" = '"+loginId+"'", null, null, null, null);
        String fieldToAdd=null;
        while(cursor.moveToNext()){
            fieldToAdd=cursor.getString(0);
            data.add(fieldToAdd);
        }
        cursor.close();  // dont forget to close the cursor after operation done
        return data;
    }



    public RegisteredUserDetails getUserDetails(String userId){
        RegisteredUserDetails registeredUserDetails = new RegisteredUserDetails();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        try{
            String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+USER_UID+" ='"+userId+"'"
                    +" AND "+LOGIN_ID+" = '"+loginId+"'";

            Log.e("db", "query = "+sql);
            Cursor cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();

            boolean dataAvailable = false;

            while(!cursor.isAfterLast()){
                dataAvailable = true;
                registeredUserDetails.setAddress(cursor.getString(cursor.getColumnIndex(USER_ADDRESS)));
                registeredUserDetails.setAge(cursor.getString(cursor.getColumnIndex(USER_AGE)));
                registeredUserDetails.setBlood_pressure(cursor.getString(cursor.getColumnIndex(HIGH_BLOOD_PRESSURE)));
                registeredUserDetails.setCholesterol(cursor.getString(cursor.getColumnIndex(HIGH_CHOLESTEROL)));
                registeredUserDetails.setDiabetes(cursor.getString(cursor.getColumnIndex(HAVE_DIABETES)));
                registeredUserDetails.setDo_you_smoke(cursor.getString(cursor.getColumnIndex(DO_YOU_SMOKE)));
                registeredUserDetails.setEmail(cursor.getString(cursor.getColumnIndex(USER_EMAIL)));
                registeredUserDetails.setFamily_had_heart_attack(cursor.getString(cursor.getColumnIndex(FAMILY_HEALTH)));
                registeredUserDetails.setGender(cursor.getString(cursor.getColumnIndex(USER_GENDER)));
                registeredUserDetails.setHad_paralytic_stroke(cursor.getString(cursor.getColumnIndex(HAD_STROKE)));
                registeredUserDetails.setHave_asthma(cursor.getString(cursor.getColumnIndex(HAVE_ASTHMA)));
                registeredUserDetails.setHeart_attack(cursor.getString(cursor.getColumnIndex(HAD_HEART_ATTACK)));
                registeredUserDetails.setHeight(cursor.getString(cursor.getColumnIndex(USER_HEIGHT)));
                registeredUserDetails.setImgUrl(cursor.getString(cursor.getColumnIndex(USER_PROFILR_URL)));
                registeredUserDetails.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                registeredUserDetails.setPhone(cursor.getString(cursor.getColumnIndex(USER_PHONE)));
                registeredUserDetails.setUniqueId(cursor.getString(cursor.getColumnIndex(USER_UID)));
                registeredUserDetails.setWaist(cursor.getString(cursor.getColumnIndex(USER_WAIST)));
                registeredUserDetails.setWeight(cursor.getString(cursor.getColumnIndex(USER_WEIGHT)));
                cursor.moveToNext();
            }

            if(!dataAvailable){
                return null;
            }

            Log.e("db", new Gson().toJson(registeredUserDetails));
        }
        catch(Exception e){

        }
        return registeredUserDetails;
    }

    public void createIfNotExists() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_NAME;
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

    public List<RegisteredUserDetails> getAllUsers() {
       List<RegisteredUserDetails> registeredUserDetailsList = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        try{
            String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+LOGIN_ID+" = '"+loginId+"'";
            Cursor cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                RegisteredUserDetails registeredUserDetails = new RegisteredUserDetails();
                registeredUserDetails.setAddress(cursor.getString(cursor.getColumnIndex(USER_ADDRESS)));
                registeredUserDetails.setAge(cursor.getString(cursor.getColumnIndex(USER_AGE)));
                registeredUserDetails.setBlood_pressure(cursor.getString(cursor.getColumnIndex(HIGH_BLOOD_PRESSURE)));
                registeredUserDetails.setCholesterol(cursor.getString(cursor.getColumnIndex(HIGH_CHOLESTEROL)));
                registeredUserDetails.setDiabetes(cursor.getString(cursor.getColumnIndex(HAVE_DIABETES)));
                registeredUserDetails.setDo_you_smoke(cursor.getString(cursor.getColumnIndex(DO_YOU_SMOKE)));
                registeredUserDetails.setEmail(cursor.getString(cursor.getColumnIndex(USER_EMAIL)));
                registeredUserDetails.setFamily_had_heart_attack(cursor.getString(cursor.getColumnIndex(FAMILY_HEALTH)));
                registeredUserDetails.setGender(cursor.getString(cursor.getColumnIndex(USER_GENDER)));
                registeredUserDetails.setHad_paralytic_stroke(cursor.getString(cursor.getColumnIndex(HAD_STROKE)));
                registeredUserDetails.setHave_asthma(cursor.getString(cursor.getColumnIndex(HAVE_ASTHMA)));
                registeredUserDetails.setHeart_attack(cursor.getString(cursor.getColumnIndex(HAD_HEART_ATTACK)));
                registeredUserDetails.setHeight(cursor.getString(cursor.getColumnIndex(USER_HEIGHT)));
                registeredUserDetails.setImgUrl(cursor.getString(cursor.getColumnIndex(USER_PROFILR_URL)));
                registeredUserDetails.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                registeredUserDetails.setPhone(cursor.getString(cursor.getColumnIndex(USER_PHONE)));
                registeredUserDetails.setUniqueId(cursor.getString(cursor.getColumnIndex(USER_UID)));
                Log.e("TrackActivity",registeredUserDetails.getUniqueId());
                registeredUserDetails.setWaist(cursor.getString(cursor.getColumnIndex(USER_WAIST)));
                registeredUserDetails.setWeight(cursor.getString(cursor.getColumnIndex(USER_WEIGHT)));
                registeredUserDetailsList.add(registeredUserDetails);
                cursor.moveToNext();
            }
        }
        catch(Exception e){

        }
        return registeredUserDetailsList;
    }

    public String updateEntry(RegisteredUserDetails registeredUserDetails, Context context) {
        Log.e("db", "RegisteredUserDetails = "+ new Gson().toJson(registeredUserDetails));
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
      /*  long count = getProfilesCount();
        count = count+1;
        String uid = registeredUserDetails.getUniqueId()+"_"+ count;*/
        //values.put(USER_UID,registeredUserDetails.getUniqueId());
        values.put(LOGIN_ID, loginId);
        values.put(USER_NAME,registeredUserDetails.getName());
        values.put(USER_AGE,registeredUserDetails.getAge());
        values.put(USER_GENDER,registeredUserDetails.getGender());
        values.put(USER_PHONE,registeredUserDetails.getPhone());
        values.put(USER_EMAIL, registeredUserDetails.getEmail());
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

        long id = db.update(TABLE_NAME,values,USER_UID+" = '"+registeredUserDetails.getUniqueId()+"'",null);
        Log.e("db", "update condition = "+ USER_UID+" = '"+registeredUserDetails.getUniqueId()+"'");
        Log.e("db", "addDataTODb: " + id);
        //closing the database connection
        DatabaseManager.getInstance().closeDatabase();
        return registeredUserDetails.getUniqueId();
    }

    public void deleteEntry(String userId){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_NAME,USER_UID+" = '"+userId+"'",null);
        DatabaseManager.getInstance().closeDatabase();
    }
}
