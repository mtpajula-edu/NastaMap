package com.example.a305.nastamap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a305.nastamap.apifeed.Feed;
import com.example.a305.nastamap.apifeed.HalJson;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by mikko.pajula on 27/03/2018.
 */

public class HistoryFragment extends Fragment {

    public HalJson hal;
    public ListView mListView;
    public FeedListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_realtime, container, false);

        Log.d("FRAGMENT","onCreateView HistoryFragment");

        getActivity().setTitle("History");

        mListView = (ListView) rootView.findViewById(R.id.real_list);
        ArrayList<Feed> f = new ArrayList<Feed>();
        adapter = new FeedListAdapter(getActivity().getApplicationContext(), f);
        mListView.setAdapter(adapter);

        if (hal != null) {
            for (Feed feed : hal.getEmbedded().getFeed()) {
                Log.d("feed", feed.getSensor());
                adapter.listData.add(0, feed);
            }
            adapter.notifyDataSetChanged();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Feed feed = (Feed) adapter.getItemAtPosition(position);
                //Toast.makeText(getActivity(),feed.getSensor(), Toast.LENGTH_LONG).show();

                if (feed.getSensor().contains("camera")) {
                    openWebFragment(feed.getPayload());
                }
            }
        });

        return rootView;
    }

    public void setHalJson(HalJson hal) {
        this.hal = hal;
        Log.d("FRAGMENT","setHalJson");
    }

    public void openWebFragment(String path) {
        WebFragment wf = new WebFragment();

        String url = "http://" + ((MainActivity)getActivity()).getIp() + path;
        wf.setUrl(url);


        FragmentTransaction ft = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
        //FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container,wf);
        ft.addToBackStack(null);
        ft.commit();
    }

}