package com.voluminous.tasks;

import android.content.Context;

import com.voluminous.Media;

import org.apache.commons.io.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SearchTask implements Runnable {
    private ArrayList<Media> searchResults = null;
    private final Context CONTEXT;
    private final String QUERY;

    public SearchTask(Context context, String query) {
        CONTEXT = context;
        QUERY = query;
    }

    @Override
    public void run() {
        URL url = SearchTask.queryToUrl(CONTEXT, QUERY);
        if(url==null) return;
        searchResults = SearchTask.urlToSearchResults(url);
    }

    public static URL queryToUrl(Context context, String query) {
        String sUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50";
        URL url = null;
        try {
            //Read API Key from assets/api_key.json
            InputStream inputStream = context.getAssets().open("api_key.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String key = new JSONObject(new String (buffer, StandardCharsets.UTF_8)).getString("key");
            if(!key.equals("")) {
                sUrl += "&key=" + key;
            } else throw new IOException("Make sure there is an \"assets/api_key.json\" file");

            //No query, no search results.
            if(!query.equals("")) {
                sUrl += "&q=" + query;
            } else throw new IOException();

            url = new URL(sUrl);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static ArrayList<Media> urlToSearchResults(URL url) {
        ArrayList<Media> _searchResults = new ArrayList<>();
        try {
            JSONArray searchResultsArray = new JSONObject(IOUtil.toString(url.openStream())).getJSONArray("items");
            for (int i = 0; i < searchResultsArray.length(); i++) {
                JSONObject item = searchResultsArray.getJSONObject(i);

                String type = item.getJSONObject("id").getString("kind").substring(8); // Removes "youtube#" prefix
                String id = item.getJSONObject("id").getString(type + "Id");

                JSONObject snippet = item.getJSONObject("snippet");
                String title = snippet.getString("title");
                String subtitle = type.equals("video") ? snippet.getString("channelTitle") : null;
                String description = snippet.getString("description");
                String thumbnail_url = snippet.getJSONObject("thumbnails").getJSONObject("default").getString("url");
                String thumbnail_url_high = snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                Media searchResult = new Media(type, id, title, subtitle, description, thumbnail_url, thumbnail_url_high);
                _searchResults.add(searchResult);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return _searchResults;
    }

    public ArrayList<Media> getSearchResults() {
        return searchResults;
    }
}