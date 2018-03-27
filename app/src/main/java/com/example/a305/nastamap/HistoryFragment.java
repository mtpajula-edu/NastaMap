package com.example.a305.nastamap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mikko.pajula on 27/03/2018.
 */

public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_history, container, false);

        //TextView tvBig = (TextView) rootView.findViewById(R.id.textView8);
        Log.d("FRAGMENT","onCreateView HistoryFragment");

        return rootView;
    }
}