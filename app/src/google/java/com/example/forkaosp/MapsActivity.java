package com.example.forkaosp;

import com.mapzen.android.lost.api.LostApiClient;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LostApiClient lostApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initGoogleApiClient();
        //initLostApiClient();

        final Button findMe = (Button) findViewById(R.id.find_me);
        findMe.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onFindMeButtonClick();
            }
        });
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override public void onConnected(Bundle bundle) {
                        LocationRequest locationRequest = LocationRequest.create();
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                                locationRequest, new LocationListener() {
                                    @Override public void onLocationChanged(Location location) {
                                        // Prime the pump.
                                    }
                                });
                    }

                    @Override public void onConnectionSuspended(int i) {
                    }
                })
                .addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override public void onConnectionFailed(
                                    ConnectionResult connectionResult) {
                                Log.e("SXSW", "Connection failed!");
                            }
                        })
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    private void initLostApiClient() {
        lostApiClient = new LostApiClient.Builder(this).build();
        lostApiClient.connect();
    }

    private void onFindMeButtonClick() {
        Location location = com.google.android.gms.location.LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);

        //Location location = com.mapzen.android.lost.api.LocationServices.FusedLocationApi
        //        .getLastLocation();

        if (location != null) {
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(current).title("You are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
