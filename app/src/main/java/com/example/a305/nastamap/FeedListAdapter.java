package com.example.a305.nastamap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a305.nastamap.apifeed.Feed;

import java.util.ArrayList;

public class FeedListAdapter  extends BaseAdapter {
    public ArrayList<Feed> listData;
    private LayoutInflater layoutInflater;

    public FeedListAdapter(Context aContext, ArrayList<Feed> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_feed, null);
            holder = new ViewHolder();
            holder.sensor  = (TextView) convertView.findViewById(R.id.txt_title);
            holder.message = (TextView) convertView.findViewById(R.id.txt_message);
            holder.time    = (TextView) convertView.findViewById(R.id.txt_time);
            holder.payload = (TextView) convertView.findViewById(R.id.txt_payload);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sensor.setText(listData.get(position).getSensor());
        holder.message.setText(listData.get(position).getMessage());
        holder.time.setText(listData.get(position).getTime());
        holder.payload.setText(listData.get(position).getPayload());

        return convertView;
    }

    static class ViewHolder {
        TextView sensor;
        TextView message;
        TextView time;
        TextView payload;
    }
}