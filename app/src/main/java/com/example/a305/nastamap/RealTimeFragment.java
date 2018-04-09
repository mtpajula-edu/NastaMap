package com.example.a305.nastamap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.a305.nastamap.apifeed.Feed;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RealTimeFragment extends Fragment {

    public MqttAndroidClient mqttAndroidClient;
    public String serverUri;
    public String clientId = "";
    public String subscriptionTopic;
    public String mqttUsername;
    public String mqttPassword;
    public ListView mListView;
    public Gson gson;
    public FeedListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serverUri         = getResources().getString(R.string.mqtt_server_url);
        subscriptionTopic = getResources().getString(R.string.mqtt_topic);
        mqttUsername      = getResources().getString(R.string.mqtt_username);
        mqttPassword      = getResources().getString(R.string.mqtt_password);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_realtime, container, false);

        Log.d("FRAGMENT","onCreateView RealTimeFragment");

        //clientId += String.valueOf(System.currentTimeMillis() / 1000L);
        Log.d("MQTT clientId",clientId);

        getActivity().setTitle(getResources().getString(R.string.feed_fragment_title));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        // ListView
        mListView = (ListView) rootView.findViewById(R.id.real_list);
        //mListView.setAdapter(adapter);

        ArrayList<Feed> f = new ArrayList<Feed>();
        adapter = new FeedListAdapter(getActivity().getApplicationContext(), f);
        mListView.setAdapter(adapter);

        // Create the client!
        mqttAndroidClient = new MqttAndroidClient(getActivity().getApplicationContext(), serverUri, clientId);

        // CALLBACKS, these will take care of the connection if something unexpected happen

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                }

                addToHistory(R.string.mqtt_connected);
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory(R.string.mqtt_connection_failed);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                addToHistory(new String(message.getPayload()));
                // THIS VARIABLE IS THE JSON DATA. you can use GSON to get the needed
                // data (temperature for example) out of it, and show it in a textview or something else
                String result = new String(message.getPayload());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        // set up connection settings
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(mqttUsername);
        mqttConnectOptions.setPassword(mqttPassword.toCharArray());

        try {
            addToHistory(R.string.mqtt_connecting);
            Log.d("MQTT","password: " + mqttPassword);

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory(R.string.mqtt_connection_failed);
                    addToHistory(exception.getMessage());
                }
            });

        } catch (MqttException ex){
            ex.printStackTrace();
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Feed feed = (Feed) adapter.getItemAtPosition(position);

                if (feed.getSensor().contains("camera")) {
                    openWebFragment(feed.getPayload());
                }
            }
        });

        return rootView;
    }

    public void addToClientId(String add) {
        clientId = "a:ylowmv:" + add;
    }

    private void addToHistory(Integer stringId) {
        if (getContext() != null) {
            addToHistory(getContext().getResources().getString(stringId));
        }
    }

    private void addToHistory(String mainText){
        Log.d("addToHistory", mainText);

        Feed feed = new Feed();
        feed.setPayload(mainText);
        feed.setSensor("");

        try {
            feed = gson.fromJson(mainText, Feed.class);
        } catch (JsonSyntaxException e) {
            Log.d("addToHistory", "Not feed object");
        }

        if (feed.getSensor().contains("ip")) {
            ((MainActivity)getActivity()).setIp(feed.getPayload());
            return;
        }

        adapter.listData.add(0, feed);
        // next thing you have to do is check if your adapter has changed

        if (adapter.listData.size() > 10) {
            adapter.listData.remove(10);
        }

        adapter.notifyDataSetChanged();
        //Feed postsList = gson.fromJson(mainText, Feed.class);
    }


    // subscriber method
    public void subscribeToTopic(){
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    addToHistory(R.string.mqtt_subscribed);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory(R.string.mqtt_subscribe_failed);
                }
            });
        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            addToHistory(R.string.mqtt_subscribe_failed);
        }
    }

    public void openWebFragment(String path) {
        WebFragment wf = new WebFragment();

        String url = "http://" + ((MainActivity)getActivity()).getIp() + path;
        wf.setUrl(url);


        FragmentTransaction ft = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,wf);
        ft.addToBackStack(null);
        ft.commit();
    }
}