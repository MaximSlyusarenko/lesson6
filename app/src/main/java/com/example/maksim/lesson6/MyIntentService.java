package com.example.maksim.lesson6;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("Service");
    }

    public static final String key = "com.example.maksim.lesson6.MyIntentService";
    ResultReceiver receiver;
    Bundle bundle;

    @Override
    public void onHandleIntent(Intent intent) {
        String channel = intent.getStringExtra("url");
        int id = intent.getIntExtra("id", -1);
        receiver = intent.getParcelableExtra("receiver");
        try {
            URL url = new URL(channel);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            InputStream is = connection.getInputStream();
            SAXParser parser = factory.newSAXParser();
            RSSHandler handler = new RSSHandler(id);
            parser.parse(is, handler);
            receiver.send(1, bundle);
        } catch(MalformedURLException e) {
            Log.i("Parser Exception", "MalformedURLException");
            receiver.send(-1, bundle);
        } catch (IOException e) {
            Log.i("Parser Exception", "IOException");
            receiver.send(-1, bundle);
        } catch (ParserConfigurationException e) {
            Log.i("Parser Exception", "ParserConfigurationException");
            receiver.send(-1, bundle);
        } catch (SAXException e) {
            Log.i("Parser Exception", "SAXException");
            receiver.send(-1, bundle);
        }
    }

    public class RSSHandler extends DefaultHandler {

        private StringBuilder builder = new StringBuilder();
        private String link;
        private String description;
        private String title;
        private int id;
        ContentValues cv;

        RSSHandler(int id) {
            this.id = id;
        }

        @Override
        public void startDocument() {
            for (int i = 0; i < ChannelActivity.channels.size(); i++) {
                Uri uri = ContentUris.withAppendedId(MyContentProvider.FEEDS_CONTENT_URI, i + 1);
                getContentResolver().delete(uri, null, null);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            builder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            name = name.toLowerCase();
            super.endElement(uri, localName, name);
            if (link != null) {
                if (name.equals("link")) {
                    link = builder.toString();
                    cv.put("link", link);
                } else if (name.equals("title")) {
                    title = builder.toString();
                    cv.put("title", title);
                } else if (name.equals("description")) {
                    description = builder.toString();
                    cv.put("description", description);
                } else if (name.equals("item")) {
                    Uri uri1 = ContentUris.withAppendedId(MyContentProvider.FEEDS_CONTENT_URI, id);
                    getContentResolver().insert(uri1, cv);
                }
                builder.setLength(0);
            }
        }

        @Override
        public void startElement(String uri, String name, String localName, Attributes attributes) throws SAXException {
            name = name.toLowerCase();
            super.startElement(uri, name, localName, attributes);
            if (name.equals("item")) {
                cv = new ContentValues();
                cv.put("channel_id", id);
                link = "";
                description = "";
                title = "";
            }
        }

    }
}
