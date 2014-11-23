package com.example.maksim.lesson6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ChannelAdapter extends BaseAdapter {

    List<Channel> channels;
    Context context;
    LayoutInflater inflater;

    public ChannelAdapter(List<Channel> feeds, Context context) {
        this.channels = feeds;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return channels.size();
    }

    @Override
    public Object getItem(int position) {
        return channels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.channel, null, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        Channel channel = channels.get(position);
        name.setText(channel.getName());

        return convertView;
    }

}