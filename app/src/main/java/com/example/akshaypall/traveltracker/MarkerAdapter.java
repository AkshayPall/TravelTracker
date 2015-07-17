package com.example.akshaypall.traveltracker;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by akshaypall on 2015-07-14.
 */
public class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater mLayoutInflater;
    private HashMap<String, Memory> mMemories;
    private View mView;

    MarkerAdapter(LayoutInflater layoutInflater, HashMap<String, Memory> memoryHashMap) {
        mLayoutInflater = layoutInflater;
        mMemories = memoryHashMap;
    }

    //to replace the entire marker dialoguebox type bubble with your own view. Now you can also make it any shape you want
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    //to return a new view for elements INSIDE the small dialogbox type bubble that protrudes from markers
    @Override
    public View getInfoContents(Marker marker) {
        if (mView == null) {
            mView = mLayoutInflater.inflate(R.layout.marker, null);
        }

        Memory memory = mMemories.get(marker.getId());

        TextView markerTitle = (TextView)mView.findViewById(R.id.marker_title);
        markerTitle.setText(memory.country);
        TextView markerSnippet = (TextView)mView.findViewById(R.id.marker_snippet);
        markerSnippet.setText(memory.city);
        TextView marketNotes = (TextView)mView.findViewById(R.id.marker_notes);
        marketNotes.setText(memory.note);

        return mView;
    }
}
