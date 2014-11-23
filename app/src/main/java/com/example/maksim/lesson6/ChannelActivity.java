package com.example.maksim.lesson6;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ChannelActivity extends Activity implements LoaderManager.LoaderCallbacks <List <Channel>>{

    public static List<Channel> channels = new ArrayList<Channel>();
    ChannelAdapter adapter;
    public static ListView view;
    Intent intent;
    ContentValues cv = new ContentValues();
    int ids = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channels);
        view = (ListView) findViewById(R.id.listView);
        getLoaderManager().initLoader(0, null, this);
        channels.add(new Channel(1, "http://lenta.ru/rss/news", "Lenta.ru all news"));
        channels.add(new Channel(2, "http://habrahabr.ru/rss/hubs/", "Habrahabr"));
        channels.add(new Channel(3, "http://feeds.bbci.co.uk/news/world/europe/rss.xml", "BBC News"));
        channels.add(new Channel(4, "http://www.ifmo.ru/module/rss.php", "ITMO news"));
        intent = new Intent(ChannelActivity.this, MainActivity.class);
        adapter = new ChannelAdapter(channels, this);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelAdapter adapter = (ChannelAdapter) parent.getAdapter();
                Channel channel = (Channel) adapter.getItem(position);
                if (!channel.link.isEmpty()) {
                    intent.putExtra("url", channel.getLink());
                    intent.putExtra("id", channel.getId());
                    intent.putExtra("do", "something");
                    startActivity(intent);
                }
            }
        });
        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelAdapter adapt = (ChannelAdapter) parent.getAdapter();
                Uri uri = Uri.parse(MyContentProvider.CHANNELS_CONTENT_URI.toString() + "/" + channels.get(position).getId());
                getContentResolver().delete(uri, null, null);
                getLoaderManager().restartLoader(0, null, ChannelActivity.this);
                channels.remove(position);
                adapt.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void onAdd(View view1) {
        EditText newLink = (EditText) findViewById(R.id.newChannelLink);
        EditText newName = (EditText) findViewById(R.id.newChannelName);
        String link = newLink.getText().toString();
        String name = newName.getText().toString();
        newLink.setText("");
        newName.setText("");
        cv.put("name", name);
        cv.put("link", link);
        getContentResolver().insert(MyContentProvider.CHANNELS_CONTENT_URI, cv);
        getLoaderManager().restartLoader(0, null, ChannelActivity.this);
        channels.add(new Channel(ids, link, name));
        ids++;
        adapter.notifyDataSetChanged();
    }

    @Override
    public Loader<List <Channel>> onCreateLoader(int i, Bundle bundle) {
        return new ChannelsLoader(this);
    }

    @Override
    public void onLoadFinished (Loader <List <Channel>> listLoader, List <Channel> channels) {
        adapter.channels = channels;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader <List <Channel>> listLoader) {
        adapter = new ChannelAdapter(new ArrayList<Channel>(), this);
        view.setAdapter(adapter);
    }

}