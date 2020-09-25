package com.example.covid_19symptomtrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Database extends SQLiteOpenHelper implements BaseColumns {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Roy.db";
    public static final String TABLE_NAME = "COVID_SYMPTOMS";
    public static final String COLUMN_1 = "HEART_RATE";
    public static final String COLUMN_2 = "RESPIRATORY_RATE";
    public static final String COLUMN_3 = "NAUSEA";
    public static final String COLUMN_4 = "HEADACHE";
    public static final String COLUMN_5 = "DIARRHEA";
    public static final String COLUMN_6 = "SORE_THROAT";
    public static final String COLUMN_7 = "FEVER";
    public static final String COLUMN_8 = "MUSCLE_ACHE";
    public static final String COLUMN_9 = "LOSS_OF_SMELL_OR_TASTE";
    public static final String COLUMN_10 = "COUGH";
    public static final String COLUMN_11 = "SHORTNESS_OF_BREATH";
    public static final String COLUMN_12 = "FEELING_TIRED";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_1 + " FLOAT, " + COLUMN_2 + " FLOAT, " + COLUMN_3 + " INTEGER, " + COLUMN_4 + " INTEGER, " + COLUMN_5 + " INTEGER, " + COLUMN_6 + " INTEGER, " + COLUMN_7 + " INTEGER, " + COLUMN_8 + " INTEGER, " + COLUMN_9 + " INTEGER, " + COLUMN_10 + " INTEGER, " + COLUMN_11 + " INTEGER, " + COLUMN_12 + " INTEGER)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public String heartRate = "0";
    public String respiratoryRate = "0";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void setHeartRate(String heartRate){
        this.heartRate = heartRate;
    }

    public void setRespiratoryRate(String respiratoryRate){
        this.respiratoryRate = respiratoryRate;
    }

    public void insertData(int[] ratingList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_1, heartRate);
        values.put(COLUMN_2, respiratoryRate);
        values.put(COLUMN_3, ratingList[0]);
        values.put(COLUMN_4, ratingList[1]);
        values.put(COLUMN_5, ratingList[2]);
        values.put(COLUMN_6, ratingList[3]);
        values.put(COLUMN_7, ratingList[4]);
        values.put(COLUMN_8, ratingList[5]);
        values.put(COLUMN_9, ratingList[6]);
        values.put(COLUMN_10, ratingList[7]);
        values.put(COLUMN_11, ratingList[8]);
        values.put(COLUMN_12, ratingList[9]);
        db.insert(TABLE_NAME, null, values);
    }
}