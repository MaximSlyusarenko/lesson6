package com.example.maksim.lesson6;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ChannelsLoader extends AsyncTaskLoader <List <Channel>>{

    ChannelsLoader(Context context) {
        super(context);
    }

    @Override
    public List <Channel> loadInBackground() {
        List <Channel> channels = new ArrayList<Channel>();
        Cursor cursor = getContext().getContentResolver().query(MyContentProvider.CHANNELS_CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String link = cursor.getString(2);
            String name = cursor.getString(1);
            int id = cursor.getInt(0);
            channels.add(new Channel(id, link, name));
            cursor.moveToNext();
        }
        cursor.close();
        return channels;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
