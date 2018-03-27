package com.example.a305.nastamap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private UiSettings mUiSettings;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        //SupportMapFragment mapFragment = (SupportMapFragment) view.getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        return view;
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