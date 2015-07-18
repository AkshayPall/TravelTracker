package com.example.akshaypall.traveltracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by akshaypall on 2015-07-17.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_FILE_NAME = "traveltracker.db";
    private static final int DATABASE_VERSION = 1;

    //DATABASE name
    public static final String MEMORIES_TABLE_NAME = "memories";
    //DATABASE column names!
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_NOTES = "notes";

    private static DbHelper singleton = null;

    public synchronized static DbHelper getInstance(Context context) {
        if (singleton == null) singleton = new DbHelper(context.getApplicationContext());
        return singleton;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    @Override //test
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                MEMORIES_TABLE_NAME +" (" +
                COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LATITUDE+" DOUBLE, " +
                COLUMN_LONGITUDE+" DOUBLE, " +
                COLUMN_CITY+" TEXT, " +
                COLUMN_COUNTRY+" TEXT, " +
                COLUMN_NOTES+" TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //clears all the data from the database
        //NOTE: DO NOT DO THIS IN AN ACTUAL APPLICATION; this is just for testing purposes
        db.execSQL("DROP TABLE IF EXISTS "+MEMORIES_TABLE_NAME);
        onCreate(db);
    }
}
