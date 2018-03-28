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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gson = gsonBuilder.create();

        gson = new GsonBuilder()
                .registerTypeAdapter(Feed.class, new Feed.FeedDeserilizer())
                .create();

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

    public void fetchFeedback() {
        //feeds.clear();
        addRequest(Request.Method.GET, null, onLoaded, false);
    }

    public void addRequest(int method, String id, Response.Listener<String> go, boolean body) {

        progressBar.setVisibility(View.VISIBLE);

        String url = "http://10.0.2.2/apigility/public/feedback";
        if (id != null) {
            url += "/" + id;
        }
        Log.i("addRequest", url);

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

    /*
    public void removeFeedback(String id) {
        addRequest(Request.Method.DELETE, id, onPosted, false);
    }

    public void setFeedback() {
        addRequest(Request.Method.POST, null, onPosted, true);
    }

    public void patchFeedback() {
        //String id2 = f.getId();
        //f.setId(null);
        //addRequest(Request.Method.PATCH, id2, onPosted, true);
    }
    */

    public final Response.Listener<String> onPosted = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("onPosted", response);
            abstractDone();
            progressBar.setVisibility(View.INVISIBLE);
            refresh();
        }
    };

    public final Response.Listener<String> onLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("onLoaded", response);

            //Apigility feed = gson.fromJson(response, Apigility.class);
            //feeds.addAll(feed.get_embedded().getFeedback());

            abstractDone();
            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    public void abstractDone() {

    }

    public void refresh() {

    }

    public final Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("onPostsError", error.toString());
            progressBar.setVisibility(View.INVISIBLE);
        }
    };
}