package com.example.android.newsapp2;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String USGS_REQUEST_URL = "https://content.guardianapis.com/search?";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mAdapter;
    private ListView listView;
    private TextView mEmptyStateTextView;
    private int pageNumber = 1;
    private boolean thumbnails;
    private String page = "1";

    @Override
    protected void onResume() {
        UpdateView();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProgressBar(true);

        listView = findViewById(R.id.news_list_view);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        if (networkIsConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            showProgressBar(false);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        thumbnails = sharedPrefs.getBoolean(getString(R.string.settings_show_thumbnails_key), true);

        mAdapter = new NewsAdapter(this, new ArrayList<News>(), thumbnails);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        TextView pageNumberTextView = findViewById(R.id.page_number);
        pageNumberTextView.setText(page);

        buttonNext();
        buttonPrevious();
    }




    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String pageSize = sharedPrefs.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "e6b96746-0297-491e-bbe1-cbccaaa275c4");
        uriBuilder.appendQueryParameter("from-date", "2015-01-01");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-fields", "trailText,thumbnail");
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("page", page);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_news);

        mAdapter.clear();
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
//        UpdateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean networkIsConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void buttonNext() {
        Button next = findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                pageNumber = pageNumber + 1;
                page = String.valueOf(pageNumber);
                getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                TextView pageNumberTextView = findViewById(R.id.page_number);
                pageNumberTextView.setText(page);
            }
        });
    }

    private void buttonPrevious() {
        Button previous = findViewById(R.id.button_previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageNumber > 1) {
                    showProgressBar(true);
                    pageNumber = pageNumber - 1;
                    page = String.valueOf(pageNumber);
                    getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    TextView pageNumberTextView = findViewById(R.id.page_number);
                    pageNumberTextView.setText(page);
                }
            }
        });

    }

    private void showProgressBar(boolean show) {
        ProgressBar loadingIndicator = findViewById(R.id.loading_indicator);
        if (show) {
            loadingIndicator.setVisibility(View.VISIBLE);
        } else loadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void UpdateView() {
        showProgressBar(true);
        mEmptyStateTextView.setText(null);
        if (networkIsConnected()) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            thumbnails = sharedPrefs.getBoolean(getString(R.string.settings_show_thumbnails_key), true);
            mAdapter = new NewsAdapter(this, new ArrayList<News>(), thumbnails);
            listView.setAdapter(mAdapter);
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
        } else {
            mAdapter.clear();
            showProgressBar(false);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UpdateView();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}