package com.example.a305.nastamap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a305.nastamap.apifeed.Feed;
import com.example.a305.nastamap.apifeed.HalJson;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private UiSettings mUiSettings;
    private GoogleMap map;
    private MapView mapview;
    public HalJson hal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        getActivity().setTitle("Map");

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

        //map.setMapType(GoogleMap.MAP_TYPE_NONE);

        mUiSettings = map.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        // Add a marker and move the camera
        LatLng point = new LatLng(66.5, 25.7);

        //String mUrl = "http://a.tile.openstreetmap.org/{z}/{x}/{y}.png";
        //NastaTileProvider nastatile = new NastaTileProvider(256, 256, mUrl);
        //TileOverlay to = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(nastatile));

        ArrayList<LatLng> coordList = new ArrayList<LatLng>();

        map.moveCamera(CameraUpdateFactory.zoomTo(10));
        map.moveCamera(CameraUpdateFactory.newLatLng(point));

        if (hal != null) {
            for (Feed feed : hal.getEmbedded().getFeed()) {

                if (feed.getMessage().contains("location")) {
                    point = new LatLng(Double.valueOf(feed.getLat()), Double.valueOf(feed.getLon()));
                    coordList.add(point);
                } else {
                    if (feed.getLat() != null) {
                        point = new LatLng(Double.valueOf(feed.getLat()), Double.valueOf(feed.getLon()));

                        MarkerOptions marker = new MarkerOptions()
                                .position(point)
                                .title(feed.getSensor())
                                .snippet(feed.getMessage())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                        if (feed.getMessage().contains("alert")) {
                            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }
                        map.addMarker(marker);
                    }
                }
            }
        }

        map.addPolyline(new PolylineOptions()
                .addAll(coordList)
                //.width(25)
                .color(Color.BLUE)
                .geodesic(true));
    }



    public void setHalJson(HalJson hal) {
        this.hal = hal;
        Log.d("FRAGMENT","setHalJson");
    }
}