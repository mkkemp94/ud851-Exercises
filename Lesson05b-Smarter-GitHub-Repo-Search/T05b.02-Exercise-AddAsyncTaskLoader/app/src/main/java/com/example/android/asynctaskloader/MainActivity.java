/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.asynctaskloader;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.asynctaskloader.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

// TODO (1) implement LoaderManager.LoaderCallbacks<String> on MainActivity
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    // TODO (28) Remove the key for storing the search results JSON
    /* A constant to save and restore the JSON that is being displayed */
    //private static final String SEARCH_RESULTS_RAW_JSON = "results";

    // TODO (2) Create a constant int to uniquely identify your loader. Call it GITHUB_SEARCH_LOADER
    private final int GITHUB_SEARCH_LOADER = 22;

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);

            // TODO (26) Remove the code that retrieves the JSON
            //String rawJsonSearchResults = savedInstanceState.getString(SEARCH_RESULTS_RAW_JSON);

            mUrlDisplayTextView.setText(queryUrl);
            // TODO (25) Remove the code that displays the JSON
            //mSearchResultsTextView.setText(rawJsonSearchResults);
        }

        // TODO (24) Initialize the loader with GITHUB_SEARCH_LOADER as the ID, null for the bundle, and this for the context

        // Bundle is null here. What this does is starts a loader which has a null bundle,
        // Ultimately the loader sets the text for me and the loader's data doesn't get lost
        getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally request that an AsyncTaskLoader performs the GET request.
     */
    private void makeGithubSearchQuery() {

        // User enters in a search query to the edit text. This method is called
        // when the search button is clicked. First, get the query.
        String githubQuery = mSearchBoxEditText.getText().toString();

        // TODO (17) If no search was entered, indicate that there isn't anything to search for and return

        // Check if it's empty. If so, return nothing.
        if (TextUtils.isEmpty(githubQuery)) {
            mUrlDisplayTextView.setText("No query enterred. Nothing to search for.");
            return;
        }

        // If it's not empty, build a url using the buildUrl method in NetworkUtils
        // Now we have a url pointing to wherever our query sends us
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);

        // Set the first TextView to show the url.
        mUrlDisplayTextView.setText(githubSearchUrl.toString());

        // TODO (18) Remove the call to execute the AsyncTask

        // TODO (19) Create a bundle called queryBundle
        // TODO (20) Use putString with SEARCH_QUERY_URL_EXTRA as the key and the String value of the URL as the value

        // Make a bundle and put the URL into it
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, githubSearchUrl.toString());

        // TODO (21) Call getSupportLoaderManager and store it in a LoaderManager variable
        // TODO (22) Get our Loader by calling getLoader and passing the ID we specified
        // TODO (23) If the Loader was null, initialize it. Else, restart it.

        // Now get the loader manager
        LoaderManager loaderManager = getSupportLoaderManager();

        // And get the loader that will load stuff from github
        Loader<String> githubSearchLoader = getSupportLoaderManager().getLoader(GITHUB_SEARCH_LOADER);

        if (githubSearchLoader == null) {
            // If it doesn't already exist, start up that loader, passing in the bundle we made
            loaderManager.initLoader(GITHUB_SEARCH_LOADER, queryBundle, this);

        } else {
            // If it does exist, restart it
            loaderManager.restartLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
        }
    }

    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showJsonDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the JSON data is visible */
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    // TODO (3) Override onCreateLoader

    // LOADER TIME

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        // Within onCreateLoader
        // TODO (4) Return a new AsyncTaskLoader<String> as an anonymous inner class with this as the constructor's parameter

        // This is onCreateLoader. SO. Create a loader. This will be an inner class.
        return new AsyncTaskLoader<String>(this) {

            // TODO (5) Override onStartLoading

            @Override
            protected void onStartLoading() {

                // Within onStartLoading

                // TODO (6) If args is null, return.

                // TODO (7) Show the loading indicator

                // TODO (8) Force a load
                // END - onStartLoading

                // Check the bundle. If nothing was passed to load with, just return.
                if (args == null) {
                    return;
                }

                // We're loading! Show a loading indicator.
                mLoadingIndicator.setVisibility(View.VISIBLE);

                // Make sure we load. Force that load.
                forceLoad();
            }

            // TODO (9) Override loadInBackground
            @Override
            public String loadInBackground() {

                // Within loadInBackground
                // TODO (10) Get the String for our URL from the bundle passed to onCreateLoader

                // TODO (11) If the URL is null or empty, return null

                // TODO (12) Copy the try / catch block from the AsyncTask's doInBackground method
                // END - loadInBackground

                // Loading now. Get the data we're using to load from the bundle.
                // In this case, it is a url generated by our search query term.
                String searchUrl = args.getString(SEARCH_QUERY_URL_EXTRA);

                // If that url doesn't exist or is empty... nothing to load, so return.
                if (searchUrl == null || TextUtils.isEmpty(searchUrl)) {
                    return null;
                }

                try {
                    // Just kidding. We had a string of a url before. Make it an actual url.
                    URL url = new URL(searchUrl);

                    // Call that url, do networking stuff, and return a response in a string.
                    // This response should be the json data.
                    return NetworkUtils.getResponseFromHttpUrl(url);

                } catch (IOException e) {
                    e.printStackTrace();

                    // If the URL failed to be created from the string, return.
                    return null;
                }
            }
        };
    }

    // TODO (13) Override onLoadFinished
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        // Okay, now the load is finished!

        // The loading indicator doesn't need to be displayed anymore.
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        // Make sure the data exists and isn't blank
        // The data is just the json response we got above.
        if (data != null && !data.equals("")) {

            // Make sure the data is showing and not the error message.
            showJsonDataView();

            // Just kidding. NOW show the resulting data.
            mSearchResultsTextView.setText(data);
        } else {
            showErrorMessage();
        }

        // Within onLoadFinished
        // TODO (14) Hide the loading indicator

        // TODO (15) Use the same logic used in onPostExecute to show the data or the error message
        // END - onLoadFinished
    }

    // TODO (16) Override onLoaderReset as it is part of the interface we implement, but don't do anything in this method
    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String queryUrl = mUrlDisplayTextView.getText().toString();
        outState.putString(SEARCH_QUERY_URL_EXTRA, queryUrl);

        // TODO (27) Remove the code that persists the JSON
        /*
        String rawJsonSearchResults = mSearchResultsTextView.getText().toString();
        outState.putString(SEARCH_RESULTS_RAW_JSON, rawJsonSearchResults);
        */
    }
}