package com.stemi.stemiapp.samples;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by praburaam on 10/08/17.
 */

public class SqliteDataSource extends SQLiteOpenHelper {

    public static final String TABLE_FOOD = "food";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_DATE_TIME = "datetime";
    public static final String COLUMN_WITHIN_LIMIT = "withinlimit";

    private static String DBNAME = "stemi";
    private static int DBVERSION = 1;


    private static final String DATABASE_CREATE = "create table "
            + TABLE_FOOD + "( " + COLUMN_USER_ID
            + " text not null , " + COLUMN_DATE_TIME
            + " text not null , "+COLUMN_WITHIN_LIMIT
            + " integer );";

    public SqliteDataSource(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(TrackFood food){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, food.getUserId());
            cv.put(COLUMN_DATE_TIME, simpleDateFormat.format(food.getDateTime()));
            cv.put(COLUMN_WITHIN_LIMIT, food.isWithinLimit());

            db.insert(TABLE_FOOD, null, cv);
        }
        catch(Exception e){

        }
        db.close();
    }

    public List<TrackFood> getFoodTrackingInfo(String userid, String date1, String date2){
        List<TrackFood> foodList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getReadableDatabase();
        try {
           // String date1_timestamp = simpleDateFormat.parse(date1);
           // String date2_timestamp = simpleDateFormat.parse(date2);
            String query = "SELECT * FROM "+TABLE_FOOD+" WHERE "+COLUMN_DATE_TIME
                    +" BETWEEN '"+date1+"' AND '"+ date2
                    +"' AND "+COLUMN_USER_ID+" = '"+userid+"' ";

            Cursor rows = db.rawQuery(query, null);
            rows.moveToFirst();

            while(!rows.isAfterLast()){
                TrackFood food = new TrackFood();
                food.setUserId(rows.getString(rows.getColumnIndex(COLUMN_USER_ID)));

                int withinlimitValue = rows.getInt(rows.getColumnIndex(COLUMN_WITHIN_LIMIT));
                food.setWithinLimit(withinlimitValue == 1);

                String timestamp = rows.getString(rows.getColumnIndex(COLUMN_DATE_TIME));

                Date dateValue = simpleDateFormat.parse(timestamp);
                food.setDateTime(dateValue);

                foodList.add(food);
                rows.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodList;
    }

    public boolean isTableEmpty(){
        String query = "SELECT * FROM "+TABLE_FOOD;
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

}
