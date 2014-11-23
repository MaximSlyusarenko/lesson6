package com.example.maksim.lesson6;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import android.database.SQLException;
import android.util.Log;

import java.util.HashMap;

public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.maksim.lesson6";
    private static final String FEEDS_PATH = "feeds";
    private static final String CHANNELS_PATH = "channels";
    public static final Uri CHANNELS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CHANNELS_PATH);
    public static final Uri FEEDS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FEEDS_PATH);
    private static final int URI_CHANNELS = 0;
    private static final int URI_CHANNELS_ID = 1;
    private static final int URI_FEEDS = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, CHANNELS_PATH, URI_CHANNELS);
        uriMatcher.addURI(AUTHORITY, CHANNELS_PATH + "/#", URI_CHANNELS_ID);
        uriMatcher.addURI(AUTHORITY, FEEDS_PATH + "/#", URI_FEEDS);
    }

    SQLiteHelper helper;

    public boolean onCreate() {
        helper = new SQLiteHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case URI_CHANNELS:
                builder.setTables(CHANNELS_PATH);
                break;
            case URI_FEEDS:
                builder.setTables(FEEDS_PATH);
                String id = uri.getLastPathSegment();
                builder.appendWhere("channel_id = " + id);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues cv) {
        SQLiteDatabase db = helper.getWritableDatabase();
        final String tableName;
        switch (uriMatcher.match(uri)) {
            case URI_CHANNELS:
                tableName = CHANNELS_PATH;
                break;
            case URI_FEEDS:
                tableName = FEEDS_PATH;
                cv.put("channel_id", uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        long rowId = db.insert(tableName, null, cv);
        Uri resultUri = ContentUris.withAppendedId(uri, rowId);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int cnt;
        switch (uriMatcher.match(uri)) {
            case URI_CHANNELS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = "_id =" + id;
                } else {
                    selection = selection + " AND _id = " + id;
                }
                cnt = db.delete(CHANNELS_PATH, selection, selectionArgs);
                break;
            case URI_FEEDS:
                String id1 = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = "channel_id = " + id1;
                } else {
                    selection = selection + "AND channel_id = " + id1;
                }
                Log.i("SELECTION", selection);
                cnt = db.delete(FEEDS_PATH, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues cv, String selection, String[] selectionArgs) {
        throw new IllegalArgumentException("No update");
    }
}