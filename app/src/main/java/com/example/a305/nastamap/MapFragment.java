package com.example.a305.nastamap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private UiSettings mUiSettings;
    private GoogleMap map;
    private MapView mapview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mapview = (MapView) rootView.findViewById(R.id.mapView);
        mapview.onCreate(savedInstanceState);

        mapview.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapview.getMapAsync(this);

        return rootView;
    }

    public void onMapReady(GoogleMap mMap) {
        map = mMap;

        // For showing a move to my location button
        //googleMap.setMyLocationEnabled(true);

        map.setMapType(GoogleMap.MAP_TYPE_NONE);

        mUiSettings = map.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        // Add a marker and move the camera
        LatLng point = new LatLng(66.5, 25.7);
        map.addMarker(new MarkerOptions().position(point).title("hello world"));

        String mUrl = "http://a.tile.openstreetmap.org/{z}/{x}/{y}.png";
        NastaTileProvider nastatile = new NastaTileProvider(256, 256, mUrl);
        TileOverlay to = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(nastatile));

        to.setTransparency(0);
        to.setVisible(true);
        to.setZIndex(1000);

        map.moveCamera(CameraUpdateFactory.zoomTo(10));
        map.moveCamera(CameraUpdateFactory.newLatLng(point));
    }
}