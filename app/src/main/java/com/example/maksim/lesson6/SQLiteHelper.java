package com.example.maksim.lesson6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME =  "dataBase";
    public static final String FEEDS_TABLE = "feeds";
    public static final String CHANNELS_TABLE = "channels";
    public static final String COLUMN_ID = "_id";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_CHANNELS_BASE = "create table " + CHANNELS_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + NAME + " text not null, "
            + LINK + " text not  null" + ");";
    public static final String CREATE_FEEDS_BASE = "create table " + FEEDS_TABLE + "("
            + "channel_id integer, "
            + COLUMN_ID + " integer primary key autoincrement, "
            + TITLE + " text not null, "
            + DESCRIPTION + " text not null, "
            + LINK + " text not null" + ");";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
       dataBase.execSQL(CREATE_CHANNELS_BASE);
        for (int i = 0; i < ChannelActivity.channels.size(); i++) {
            dataBase.execSQL("insert into channels (name, link) values (?, ?)", new String[]{ChannelActivity.channels.get(i).getName(), ChannelActivity.channels.get(i).getLink()});
        }
       dataBase.execSQL(CREATE_FEEDS_BASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
        dataBase.execSQL("drop table if exists " + CHANNELS_TABLE);
        dataBase.execSQL("drop table if exists " + FEEDS_TABLE);
        onCreate(dataBase);
    }
}
