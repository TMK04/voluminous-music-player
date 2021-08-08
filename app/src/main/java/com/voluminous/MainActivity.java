package com.voluminous;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int CONTENT_ID = R.id.content;
    private Fragment home_fragment;
    private Fragment library_fragment;
    private Fragment filters_fragment;
    private MaterialToolbar searchbar;
    private TextInputLayout searchbar_search;
    private TextInputEditText searchbar_input;
    private FloatingActionButton navigation_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_fragment = new HomeFragment();
        library_fragment = new LibraryFragment();
        filters_fragment = new FiltersFragment();

        getSupportFragmentManager().beginTransaction().replace(CONTENT_ID, home_fragment).commit();

        navigation_fab = findViewById(R.id.navigation_fab);
        navigation_fab.setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(CONTENT_ID);
            if(currentFragment instanceof HomeFragment) {
                getSupportFragmentManager().beginTransaction().replace(CONTENT_ID, library_fragment).commit();
                v.setActivated(true);
            } else {
                getSupportFragmentManager().beginTransaction().replace(CONTENT_ID, home_fragment).commit();
                v.setActivated(false);
            }
        });

        searchbar = findViewById(R.id.searchbar);
        searchbar.findViewById(R.id.searchbar_filters).setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(CONTENT_ID);
            if(currentFragment instanceof FiltersFragment) {
                getSupportFragmentManager().beginTransaction().remove(filters_fragment).addToBackStack("filters").commit();
            } else if (getSupportFragmentManager().findFragmentByTag("filters")==null) {
                getSupportFragmentManager().beginTransaction().add(CONTENT_ID, filters_fragment, "filters").commit();
            } else {
                getSupportFragmentManager().popBackStack("filters", 0);
            }
        });
        searchbar_search = findViewById(R.id.searchbar_search);
        searchbar_input = findViewById(R.id.searchbar_input);
        searchbar_input.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                getSearchResults(v.getText().toString());
                clearFocus(v);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    clearFocus(v);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void clearFocus(View v) {
        v.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private ArrayList<SearchResult> getSearchResults(String query) {
        ArrayList<SearchResult> searchResults = new ArrayList<>();
        String sUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=";
        try {
            //Read API Key from assets/api_key.json
            InputStream inputStream = getAssets().open("api_key.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String key = new JSONObject(new String (buffer, StandardCharsets.UTF_8)).getString("key");
            if(key.equals("")) throw new IOException("Make sure there is an \"assets/api_key.json\" file");

            //No query, no search results.
            if(query.equals("")) {
                sUrl += "&q=" + query;
            } else throw new IOException();

            FiltersFragment filters_fragment = (FiltersFragment) getSupportFragmentManager().findFragmentByTag("filters");
            if (filters_fragment != null) sUrl += filters_fragment.getFilters();

            JSONArray searchResultsArray = new JSONObject(IOUtils.toString(new URL(sUrl), StandardCharsets.UTF_8)).getJSONArray("items");
            for(int i = 0; i < searchResultsArray.length(); i++) {
                JSONObject item = searchResultsArray.getJSONObject(i);

                JSONObject idObject = item.getJSONObject("id");
                String type = idObject.getString("kind").substring(7);
                String id = idObject.getString(type + "Id");

                JSONObject snippet = item.getJSONObject("snippet");
                String img_url = snippet.getJSONObject("thumbnails").getJSONObject("default").getString("url");
                String title = snippet.getString("title");
                String subtitle = type.equals("video") ? snippet.getString("channelTitle") : null;

                SearchResult searchResult = new SearchResult(type, id, title, subtitle, img_url);
                searchResults.add(searchResult);
                Log.d("SEARCH", searchResult.toString());
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
        return searchResults;
    }
}