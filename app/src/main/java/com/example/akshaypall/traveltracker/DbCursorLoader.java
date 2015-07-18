package com.example.akshaypall.traveltracker;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by akshaypall on 2015-07-18.
 */
public abstract class DbCursorLoader extends AsyncTaskLoader<Cursor> {
    private Cursor mCursor;

    public DbCursorLoader (Context context) {
        super(context);
    }

    protected abstract Cursor loadCursor();

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = loadCursor();
        if (cursor != null) cursor.getCount();
        return cursor;
    }

    @Override
    public void deliverResult(Cursor data) {
        Cursor oldCursor = mCursor;
        mCursor = data;

        if (isStarted()) super.deliverResult(data);

        if (oldCursor != null && data != oldCursor) {

        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) deliverResult(mCursor);
        if (takeContentChanged() || mCursor == null) forceLoad(); //forceload = RELOAD the data
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        super.onCanceled(data);
        if (data != null) onReleaseResource(data);
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mCursor != null) onReleaseResource(mCursor);
        mCursor = null;
    }

    private void onReleaseResource(Cursor cursor) {
        if(!cursor.isClosed()) cursor.close();
    }
}
