package com.example.a305.nastamap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

public class MainActivity extends AbstractVolleyActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Integer currentFragment = null;
    public String clientIdAdd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        clientIdAdd += String.valueOf(System.currentTimeMillis() / 1000L);

        setProgressBar(R.id.progressBar);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            currentFragment = savedInstanceState.getInt("currentFragment");
            ip = savedInstanceState.getString("ip");
            fetchInit();
        } else {
            // Probably initialize members with default values for a new instance
        }

        String url = "http://wirma.plab.fi/cached_geojson/0/condition/";
        addRequest(Request.Method.GET, url, null, onGeoJSONLoaded, false);

        startFragment(currentFragment);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentFragment",currentFragment);
        outState.putString("ip",ip);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        boolean isPage = true;
        if (id == R.id.nav_prev) {
            isPage = fetchPrev();
        } else if (id == R.id.nav_next) {
            isPage = fetchNext();
        } else if (id == R.id.nav_last) {
            isPage = fetchLast();
        } else {
            startFragment(id);
        }

        if (!isPage) {
            Toast.makeText(this,"Cant't go further", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startFragment(Integer id) {
        currentFragment = id;
        Log.d("FRAGMENT"," ... startFragment ...");

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment != null) {
            Log.d("Fragment","fragment not null");
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        //manager = this.getSupportFragmentManager();
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment


        if (id == null) {
            RealTimeFragment realfragment = new RealTimeFragment();
            realfragment.addToClientId(clientIdAdd);
            ft.replace(R.id.fragment_container, realfragment);
        } else if (id == R.id.nav_map) {
            MapFragment mapfragment = new MapFragment();
            ft.replace(R.id.fragment_container, mapfragment);
            mapfragment.setHalJson(hal);
            mapfragment.setGeoJSON(geoJSON);
        } else if (id == R.id.nav_history) {
            HistoryFragment hfragment = new HistoryFragment();
            ft.replace(R.id.fragment_container, hfragment);
            hfragment.setHalJson(hal);
        } else if (id == R.id.nav_real) {
            RealTimeFragment realfragment = new RealTimeFragment();
            realfragment.addToClientId(clientIdAdd);
            ft.replace(R.id.fragment_container, realfragment);
        } else {
            return;
        }

        // Complete the changes added above
        ft.commit();
    }

    @Override
    public void abstractDone() {
        if (!isInit) {
            startFragment(currentFragment);
        } else {
            Toast.makeText(this,"Connected to nastalaatikko\n"+ip, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void abstractError() {
        Toast.makeText(this,"Request error", Toast.LENGTH_LONG).show();
    }

    /*
    private void fetchPostsFromFile() {

        String response = readRawTextFile(getApplicationContext(), R.raw.data);
        Log.d("Response", response);
        hal = gson.fromJson(response, HalJson.class);

        Log.d("feeds from api: ", String.valueOf(hal.getEmbedded().getFeed().size()));
    }
    */
}
