
package com.example.a305.nastamap.apifeed;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class Feed {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sensor")
    @Expose
    private String sensor;

    @SerializedName("payload")
    @Expose
    private String payload;

    //@SerializedName("payload")
    //@Expose
    //public JsonObject payload;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("_links")
    @Expose
    private Links links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(JsonObject payload) {
        this.payload = payload.getAsString();
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getSnippet() {
        return "" + this.getTime() + "\n" + this.getMessage() + "\n" + this.getPayload();
    }

    public static class FeedDeserilizer implements JsonDeserializer<Feed> {

        @Override
        public Feed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            //Log.d("FeedDeserilizer","deserialize");
            Feed feed = new Gson().fromJson(json, Feed.class);
            JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("payload")) {
                JsonElement elem = jsonObject.get("payload");
                //Log.d("FeedDeserilizer",elem.toString());
                feed.setPayload(elem.getAsString());
                //Log.d("FeedDeserilizer","has payload " + elem.getAsString());
            }
            return feed;
        }
    }

}
