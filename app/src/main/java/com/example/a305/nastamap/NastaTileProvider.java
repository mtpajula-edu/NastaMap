package com.example.a305.nastamap;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mikko.pajula on 27/03/2018.
 */

public class NastaTileProvider extends UrlTileProvider {

    private String baseUrl;

    public NastaTileProvider(int width, int height, String url) {
        super(width, height);
        this.baseUrl = url;
    }

    @Override
    public URL getTileUrl(int x, int y, int zoom) {
        try {
            return new URL(baseUrl.replace("{z}", ""+zoom).replace("{x}",""+x).replace("{y}",""+y));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}