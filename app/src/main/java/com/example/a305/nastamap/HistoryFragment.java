package com.example.a305.nastamap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

        return rootView;
    }

    public void setHalJson(HalJson hal) {
        this.hal = hal;
        Log.d("FRAGMENT","setHalJson");
    }
}