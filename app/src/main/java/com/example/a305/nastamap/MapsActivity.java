package com.example.a305.nastamap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AbstractVolleyActivity implements OnMapReadyCallback {

    private UiSettings mUiSettings;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        mUiSettings = map.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng point = new LatLng(66.5, 25.7);
        map.addMarker(new MarkerOptions().position(point).title("hello world"));

        map.moveCamera(CameraUpdateFactory.zoomTo(10));
        map.moveCamera(CameraUpdateFactory.newLatLng(point));
    }
}
