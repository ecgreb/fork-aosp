package com.example.forkaosp;

import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.tangram.HttpHandler;
import com.mapzen.tangram.LngLat;
import com.mapzen.tangram.MapController;
import com.mapzen.tangram.MapData;
import com.mapzen.tangram.MapView;
import com.mapzen.tangram.Properties;
import com.mapzen.tangram.Tangram;

import com.squareup.okhttp.Callback;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MapsActivity extends AppCompatActivity {

    private MapView map;
    private MapController mapController;
    private LostApiClient lostApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        map = (MapView) findViewById(R.id.map);
        mapController = new MapController(this, map);

        initLostApiClient();

        final Button findMe = (Button) findViewById(R.id.find_me);
        findMe.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onFindMeButtonClick();
            }
        });

        HttpHandler handler = new HttpHandler() {
            @Override
            public boolean onRequest(String url, Callback cb) {
                url += "?api_key=vector-tiles-tyHL4AY";
                return super.onRequest(url, cb);
            }

            @Override
            public void onCancel(String url) {
                url += "?api_key=vector-tiles-tyHL4AY";
                super.onCancel(url);
            }
        };

        mapController.setHttpHandler(handler);
    }

    private void initLostApiClient() {
        lostApiClient = new LostApiClient.Builder(this).build();
        lostApiClient.connect();
    }

    private void onFindMeButtonClick() {
        Location location = com.mapzen.android.lost.api.LocationServices.FusedLocationApi
                .getLastLocation();

        if (location != null) {
            LngLat current = new LngLat(location.getLongitude(), location.getLatitude());
            mapController.setMapPosition(current);
            final MapData marker = new MapData("touch");
            Tangram.addDataSource(marker);
            Properties props = new Properties();
            props.set("type", "point");
            marker.addPoint(props, current);
        }
    }
}
