package com.example.akshaypall.traveltracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshaypall on 2015-07-17.
 */
public class MemoriesDataSource {
    private DbHelper mDbHelper;
    private String[] allColumns = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_LATITUDE,
            DbHelper.COLUMN_LONGITUDE,
            DbHelper.COLUMN_CITY,
            DbHelper.COLUMN_COUNTRY,
            DbHelper.COLUMN_NOTES
    };

    public MemoriesDataSource (Context context) {
        mDbHelper = DbHelper.getInstance(context);
    }

    public void createMemory (Memory memory) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_LATITUDE, memory.lattitude);
        values.put(DbHelper.COLUMN_LONGITUDE, memory.longitude);
        values.put(DbHelper.COLUMN_CITY, memory.city);
        values.put(DbHelper.COLUMN_COUNTRY, memory.country);
        values.put(DbHelper.COLUMN_NOTES, memory.note);

        //to write all the data onto the database using the mapping scheme form the ContentValues
        memory.id = mDbHelper.getWritableDatabase().insert(DbHelper.MEMORIES_TABLE_NAME, null, values);
    }

    public List<Memory> getAllMemories() {
        Cursor cursor = allMemoriesCursor();
        return cursorToMemoriesList(cursor);

    }

    public Cursor allMemoriesCursor() {
        return mDbHelper.getReadableDatabase().query(DbHelper.MEMORIES_TABLE_NAME, allColumns, null, null, null, null, null);
    }

    public List<Memory> cursorToMemoriesList (Cursor cursor) {
        List<Memory> memories = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            memories.add(cursorToMemory(cursor));
            cursor.moveToNext();
        }

        return memories;
    }

    private Memory cursorToMemory (Cursor cursor) {
        Memory memory = new Memory();
        memory.id = cursor.getLong(0);
        memory.lattitude = cursor.getDouble(1);
        memory.longitude = cursor.getDouble(2);
        memory.city = cursor.getString(3);
        memory.country = cursor.getString(4);
        memory.note = cursor.getString(5);

        return memory;
    }

    public void updateMemory (Memory memory) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_LATITUDE, memory.lattitude);
        values.put(DbHelper.COLUMN_LONGITUDE, memory.longitude);
        values.put(DbHelper.COLUMN_CITY, memory.city);
        values.put(DbHelper.COLUMN_COUNTRY, memory.country);
        values.put(DbHelper.COLUMN_NOTES, memory.note);

        String[] whereArgs = {String.valueOf(memory.id)};
        mDbHelper.getWritableDatabase().update(DbHelper.MEMORIES_TABLE_NAME, values, DbHelper.COLUMN_ID+"=?", whereArgs);
    }

    public void deleteMemory (Memory memory) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_LATITUDE, memory.lattitude);
        values.put(DbHelper.COLUMN_LONGITUDE, memory.longitude);
        values.put(DbHelper.COLUMN_CITY, memory.city);
        values.put(DbHelper.COLUMN_COUNTRY, memory.country);
        values.put(DbHelper.COLUMN_NOTES, memory.note);

        String[] whereArgs = {String.valueOf(memory.id)};
        mDbHelper.getWritableDatabase().delete(mDbHelper.MEMORIES_TABLE_NAME, mDbHelper.COLUMN_ID+"=?", whereArgs);
    }
}
