package com.example.a305.nastamap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private UiSettings mUiSettings;
    private GoogleMap map;
    private MapView mapview;
    public HalJson hal;
    public View rootView;
    public Map<String, JSONObject> geoJsons = new HashMap<String, JSONObject>();
    public Map<String, GeoJsonLayer> geoJsonLayers = new HashMap<String, GeoJsonLayer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        getActivity().setTitle(getResources().getString(R.string.map_fragment_title));

        mapview = (MapView) rootView.findViewById(R.id.mapView);
        mapview.onCreate(savedInstanceState);

        //Button b = (Button) rootView.findViewById(R.id.map_button);
        //b.setOnClickListener(this);

        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                //RadioButton rb = (RadioButton) rootView.findViewById(checkedId);
                //Log.d("FRAGMENT",String.valueOf(rb.getText()));
                setCheckedLayerOnMap();

                /*
                for (Map.Entry<String, GeoJsonLayer> entry : geoJsonLayers.entrySet()) {

                    Log.d("FRAGMENT",String.valueOf(rb.getText()));
                    Log.d("FRAGMENT",entry.getKey());

                    if (entry.getKey().equals(String.valueOf(rb.getText()))) {
                        entry.getValue().addLayerToMap();
                    } else {
                        entry.getValue().removeLayerFromMap();
                    }
                }*/
            }
        });

        mapview.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapview.getMapAsync(this);

        return rootView;
    }

    public void setCheckedLayerOnMap() {
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        Integer radioButtonID = radioGroup.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) radioGroup.findViewById(radioButtonID);

        for (Map.Entry<String, GeoJsonLayer> entry : geoJsonLayers.entrySet()) {

            Log.d("FRAGMENT",String.valueOf(rb.getText()));
            Log.d("FRAGMENT",entry.getKey());

            if (entry.getKey().equals(String.valueOf(rb.getText()))) {
                entry.getValue().addLayerToMap();
            } else {
                entry.getValue().removeLayerFromMap();
            }
        }
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

        geoJsonLayers.clear();
        for (Map.Entry<String, JSONObject> entry : geoJsons.entrySet()) {
            addGeoJsonToMap(entry.getKey(), entry.getValue());
        }

        setCheckedLayerOnMap();
    }

    public void addGeoJsonToMap(String name, JSONObject geoJSON) {
        GeoJsonLayer layer = new GeoJsonLayer(map, geoJSON);

        for (Map.Entry<String, GeoJsonLayer> entry : geoJsonLayers.entrySet()) {
            entry.getValue().removeLayerFromMap();
        }

        if (name.equals("condition")) {
            for (GeoJsonFeature feature : layer.getFeatures()) {
            //Log.d("GEOJSON","sensorvalue_range: " + feature.getProperty("sensorvalue_range"));

                GeoJsonLineStringStyle lineStringStyle = new GeoJsonLineStringStyle();
                /*
                Tiekuntokoodit:
                0 = 'data not available'
                1 = 'dry'
                2 = 'moist'
                3 = 'wet'
                4 = 'slush'
                5 = 'ice'
                6 = 'snow'
                 */

                switch (feature.getProperty("sensorvalue_range")) {
                    case "0":
                        lineStringStyle.setColor(Color.TRANSPARENT);
                        break;
                    case "1":
                        lineStringStyle.setColor(Color.GREEN);
                        break;
                    case "2":
                        lineStringStyle.setColor(Color.parseColor("#cccc00"));
                        break;
                    case "3":
                        lineStringStyle.setColor(Color.YELLOW);
                        break;
                    case "4":
                        lineStringStyle.setColor(Color.parseColor("#ff9900"));
                        break;
                    case "5":
                        lineStringStyle.setColor(Color.RED);
                        break;
                    case "6":
                        lineStringStyle.setColor(Color.WHITE);
                        break;
                }

                feature.setLineStringStyle(lineStringStyle);
            }

            //layer.addLayerToMap();

        } else if (name.equals("friction")) {
            for (GeoJsonFeature feature : layer.getFeatures()) {
                GeoJsonLineStringStyle lineStringStyle = new GeoJsonLineStringStyle();
                /*
                kitkakoodit:
                1 = 'worst'
                2 = ''
                3 = ''
                4 = 'best'
                 */

                switch (feature.getProperty("sensorvalue_range")) {
                    case "0":
                        lineStringStyle.setColor(Color.TRANSPARENT);
                        break;
                    case "1":
                        lineStringStyle.setColor(Color.RED);
                        break;
                    case "2":
                        lineStringStyle.setColor(Color.parseColor("#ff9900"));
                        break;
                    case "3":
                        lineStringStyle.setColor(Color.parseColor("#cccc00"));
                        break;
                    case "4":
                        lineStringStyle.setColor(Color.GREEN);
                        break;
                }

                feature.setLineStringStyle(lineStringStyle);
            }
    }

        Log.d("GEOJSON","put: " + name);
        geoJsonLayers.put(name, layer);
    }

    public void setGeoJsons(Map<String, JSONObject> geoJsons) {
        this.geoJsons = geoJsons;
        Log.d("FRAGMENT","setGeoJsons");
    }

    public void setHalJson(HalJson hal) {
        this.hal = hal;
        Log.d("FRAGMENT","setHalJson");
    }

    public void onClick(View view) {
        Log.d("FRAGMENT","onMapButtonClick");
    }
}