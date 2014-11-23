package com.example.maksim.lesson6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends BaseAdapter {

    List<Feed> feeds;
    Context context;
    LayoutInflater inflater;

    public FeedAdapter(List<Feed> feeds, Context context) {
        this.feeds = feeds;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return feeds.size();
    }

    @Override
    public Object getItem(int position) {
        return feeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.feed, null, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        Feed feed = feeds.get(position);
        title.setText(feed.getTitle());

        return convertView;
    }

}