package com.example.maksim.lesson6;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks <List <Feed>> {

    Intent intent;
    public static ListView view;
    public static Context context;
    String url;
    int id;
    FeedAdapter adapter;
    MyBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        view = (ListView) findViewById(R.id.listView);
        intent = new Intent(this, WebActivity.class);
        url = getIntent().getStringExtra("url");
        id = getIntent().getIntExtra("id", -1);
        adapter = new FeedAdapter(new ArrayList<Feed>(), this);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedAdapter adapter = (FeedAdapter) parent.getAdapter();
                Feed feed = (Feed) adapter.getItem(position);
                if (!feed.link.isEmpty()) {
                    intent.putExtra("url", feed.getLink());
                    intent.putExtra("description", feed.getDescription());
                    startActivity(intent);
                }
            }
        });
        receiver = new MyBroadcastReceiver(new Handler(), this);
        Intent intent2 = new Intent(this, MyIntentService.class);
        intent2.putExtra("url", url);
        intent2.putExtra("id", id);
        intent2.putExtra("receiver", receiver);
        startService(intent2);
        getLoaderManager().initLoader(1, null, MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader <List <Feed>> onCreateLoader(int i, Bundle bundle) {
        return new FeedsLoader(this, id);
    }

    @Override
    public void onLoadFinished(Loader <List <Feed>> listLoader, List <Feed> feeds1) {
        adapter.feeds = feeds1;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader <List <Feed>> listLoader) {
        adapter = new FeedAdapter(new ArrayList<Feed>(), this);
        view.setAdapter(adapter);
    }

    public class MyBroadcastReceiver extends ResultReceiver {

        public MyBroadcastReceiver(Handler handler, Context context1) {
            super(handler);
        }

        @Override
        public void onReceiveResult(int code, Bundle bundle) {
            if (code == 1) {
                getLoaderManager().restartLoader(1, null, MainActivity.this);
            } else {
                adapter.feeds = new ArrayList<Feed>();
                adapter.feeds.add(new Feed("", "No Internet Connection or it isn't RSS channel", ""));
                adapter.notifyDataSetChanged();
            }
        }
    }
}