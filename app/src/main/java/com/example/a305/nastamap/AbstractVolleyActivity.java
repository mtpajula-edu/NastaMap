package com.example.a305.nastamap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a305.nastamap.apifeed.Feed;
import com.example.a305.nastamap.apifeed.HalJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by a305 on 14/03/2018.
 */


public class AbstractVolleyActivity extends AppCompatActivity {

    public RequestQueue requestQueue;
    public Gson gson;
    public HashMap<String, String> headers;
    //public Feedback f = new Feedback();
    //public ArrayList<Feedback> feeds = new ArrayList<Feedback>();
    public ProgressBar progressBar;
    public String ip = "";
    public HalJson hal;
    public boolean isInit = false;
    public JSONObject geoJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json; charset=utf-8");
    }

    public static String readRawTextFile(Context ctx, int resId)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1)
            {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (Exception e) {
            return null;
        }
        return byteArrayOutputStream.toString();
    }


    public void setProgressBar(int pb) {
        progressBar = (ProgressBar) findViewById(pb);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void fetchInit() {
        isInit = true;
        String url = "http://" + ip + "/apigility/public/index.php/feed";
        addRequest(Request.Method.GET, url, null, onLoaded, false);
    }

    public boolean fetchNext() {
        isInit = false;
        try {
            addRequest(Request.Method.GET, hal.getLinks().getNext().getHref(), null, onLoaded, false);
            return true;
        } catch (Exception e) {
            Log.e("Fetch",e.toString());
        }
        return false;
    }

    public boolean fetchPrev() {
        isInit = false;
        try {
            addRequest(Request.Method.GET, hal.getLinks().getPrev().getHref(), null, onLoaded, false);
            return true;
        } catch (Exception e) {
            Log.e("Fetch",e.toString());
        }
        return false;
    }

    public boolean fetchLast() {
        isInit = false;
        try {
            addRequest(Request.Method.GET, hal.getLinks().getLast().getHref(), null, onLoaded, false);
            return true;
        } catch (Exception e) {
            Log.e("Fetch",e.toString());
        }
        return false;
    }

    public void setIp(String ip) {
        if (!ip.equals(this.ip)) {
            Log.d("IP",ip);
            this.ip = ip;
            fetchInit();
        }
    }

    public String getIp() {
        return ip;
    }

    public void addRequest(int method, String url, String id, Response.Listener<String> go, boolean body) {

        progressBar.setVisibility(View.VISIBLE);

        //String url = "http://" + ip + "/apigility/public/index.php/feed";
        if (id != null) {
            url += "/" + id;
        }
        Log.i("REQUEST", url);

        if (body) {
            StringRequest request = new StringRequest(method, url, go, onError) {
                public Map<String, String> getHeaders() {
                    return headers;
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    byte[] body = new byte[0];
                    /*
                    try {
                        Gson gson = new GsonBuilder().create();
                        String newData = gson.toJson(f);
                        Log.i("getBody", newData);

                        body = newData.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // problems with converting our data into UTF-8 bytes
                        Log.e("getBody",e.toString());
                    }
                    */
                    return body;
                }
            };
            requestQueue.add(request);
        } else {
            StringRequest request = new StringRequest(method, url, go, onError) {
                public Map<String, String> getHeaders() {
                    return headers;
                }
            };
            requestQueue.add(request);
        }
    }

    public final Response.Listener<String> onPosted = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
        Log.d("onPosted", response);
        abstractDone();
        //progressBar.setVisibility(View.INVISIBLE);
        refresh();
        }
    };

    public final Response.Listener<String> onLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("onLoaded", response);

            hal = gson.fromJson(response, HalJson.class);
            Log.d("feeds from api: ", String.valueOf(hal.getEmbedded().getFeed().size()));


            abstractDone();

            if (isInit) {
                fetchLast();
            }

            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    public final Response.Listener<String> onGeoJSONLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("onLoaded", response);


            try {
                geoJSON = new JSONObject(response);
            } catch (Exception e) {
                Log.e("GEOJSON","Failed to parse json from request");
            }

            abstractDone();

            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    public void abstractDone() {

    }

    public void abstractError() {

    }

    public void refresh() {

    }

    public final Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        Log.e("onPostsError", error.toString());
        progressBar.setVisibility(View.INVISIBLE);
        abstractError();
        }
    };
}