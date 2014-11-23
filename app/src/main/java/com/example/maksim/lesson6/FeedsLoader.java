package com.example.maksim.lesson6;

import android.content.AsyncTaskLoader;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FeedsLoader extends AsyncTaskLoader <List <Feed>>{

    private int id;
    List <Feed> feeds;

    FeedsLoader(Context context, int id) {
        super(context);
        this.id = id;
    }

    @Override
    public List <Feed> loadInBackground() {
        feeds = new ArrayList<Feed>();
        Uri uri = ContentUris.withAppendedId(MyContentProvider.FEEDS_CONTENT_URI, id);
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String link = cursor.getString(4);
            String title = cursor.getString(2);
            String description = cursor.getString(3);
            feeds.add(new Feed(link, title, description));
            cursor.moveToNext();
        }
        cursor.close();
        return feeds;
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
