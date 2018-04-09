package com.example.a305.nastamap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebFragment extends Fragment {

    public String url = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_webpage, container, false);

        Log.d("FRAGMENT", "onCreateView WebFragment");

        WebView mWebView = (WebView) rootView.findViewById(R.id.webview);

        getActivity().setTitle(getResources().getString(R.string.web_fragment_title));

        if(url != null){
            mWebView.loadUrl(url);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }

        return rootView;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
