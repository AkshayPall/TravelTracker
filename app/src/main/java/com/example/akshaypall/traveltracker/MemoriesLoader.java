package com.example.akshaypall.traveltracker;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by rohitsharma on 2015-07-18.
 */
public class MemoriesLoader extends DbCursorLoader {

    private MemoriesDataSource mDataSource;

    public MemoriesLoader (Context context, MemoriesDataSource memoriesDataSource) {
        super(context);
        mDataSource = memoriesDataSource;

    }

    @Override
    protected Cursor loadCursor() {
        return mDataSource.allMemoriesCursor();
    }
}
