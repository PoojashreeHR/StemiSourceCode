package com.stemi.stemiapp.databases;

/**
 * Created by Pooja on 20-09-2017.
 */

public class BloodTestTable {

    public static final String BLOOD_TEST_TABLE = "bloodTestTbl";

    //Column name for Medication
    private static final String BLOOD_KEY_ID = "testKeyId";
    private static final String TEST_DATE = "bloodTestDate";
    private  static final String PERSON_UID = "personUid";
    private  static final String BLOOD_REPORT = "bloodReport";


    public static final String CREATE_BLOOD_TEST_TABLE = "CREATE TABLE " + BLOOD_TEST_TABLE + "("
            + BLOOD_KEY_ID + " INTEGER PRIMARY KEY,"
            + TEST_DATE + " TEXT,"
            + PERSON_UID + " TEXT,"
            + BLOOD_REPORT + " TEXT"
            + ")";

}
