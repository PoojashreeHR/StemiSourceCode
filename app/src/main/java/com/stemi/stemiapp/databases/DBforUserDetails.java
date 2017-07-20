package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.stemi.stemiapp.model.RegisteredUserDetails;

/**
 * Created by Pooja on 20-07-2017.
 */

public class DBforUserDetails extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "dbStemiapp";

    private static final String TABLE_NAME = "dbuserDetails";
    //column names
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

    //sql query to creating table in database
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
            + FAMILY_HEALTH + " TEXT"
            + ")";

    public DBforUserDetails(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean isExist(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME +  " WHERE "+ uid + "="+ "'" + 0 +"'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;

    }

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

        long id = db.insert(TABLE_NAME, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        db.close();

        //see that all database connection stuff is inside this method
        //so we don't need to open and close db connection outside this class

    }
}
