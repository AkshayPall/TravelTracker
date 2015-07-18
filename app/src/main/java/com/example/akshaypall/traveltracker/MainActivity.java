package com.example.akshaypall.traveltracker;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity implements MemoryAlertFragment.Listener, OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "MainActivity";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, Memory> mMemories = new HashMap<>();
    private MemoriesDataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDataSource = new MemoriesDataSource(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setInfoWindowAdapter(new MarkerAdapter(getLayoutInflater(), mMemories));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(this);
        new AsyncTask<Void, Void, List<Memory>>() {
            @Override
            protected List<Memory> doInBackground(Void... params) {
                return mDataSource.getAllMemories();
            }

            @Override
            protected void onPostExecute(List<Memory> memories) {
                //super.onPostExecute(memories);
                onFetchedMemories(memories);
            }
        }.execute();
    }

    private void onFetchedMemories(List<Memory> memories) {
        for (Memory memory: memories) {
            addMarker(memory);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Memory memory = new Memory();

        updateMemoryPosition(memory, latLng);


        MemoryAlertFragment.newInstance(memory).show(getFragmentManager(), MemoryAlertFragment.MEMORY_KEY);
    }

    private void updateMemoryPosition(Memory memory, LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> matches = null;

        try {
            matches = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address bestMatch = (matches.isEmpty() ? null: matches.get(0));
        if (bestMatch != null) {
            int maxLines = bestMatch.getMaxAddressLineIndex();

            memory.country = bestMatch.getAddressLine(maxLines);
            memory.city = bestMatch.getAddressLine(maxLines-1);
            memory.lattitude = latLng.latitude;
            memory.longitude = latLng.longitude;
        }
    }

    private void addGoogleAPIClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        final Memory memory = mMemories.get(marker.getId());
        String[] actions = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(memory.city+", "+memory.country)
                .setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete action has index 1
                        if (which == 1) {
                            mDataSource.deleteMemory(memory);
                            marker.remove();
                        }
                    }
                });

                /*.setPositiveButton(getString(R.string.remove_dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //If Remove Pressed
                        mDataSource.deleteMemory(memory);
                        marker.remove();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if Cancel Pressed
                    }
                });*/
        builder.create().show();
    }

    @Override
    public void onSaveClicked(Memory memory) {
        addMarker(memory);

        mDataSource.createMemory(memory);
    }

    private void addMarker(Memory memory) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(new LatLng(memory.lattitude, memory.longitude)));

        mMemories.put(marker.getId(), memory);
    }

    @Override
    public void onCancelClicked(Memory memory) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Memory memory = mMemories.get(marker.getId());
        updateMemoryPosition(memory, marker.getPosition());
        mDataSource.updateMemory(memory);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}