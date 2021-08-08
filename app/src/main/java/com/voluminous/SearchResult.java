package com.voluminous;

public class SearchResult {
    private final String TYPE, ID,
            TITLE, SUBTITLE,
            IMG_URL;

    public SearchResult(String type, String id, String title, String subtitle, String img_url) {
        TYPE = type;
        ID = id;
        IMG_URL = img_url;
        TITLE = title;
        SUBTITLE = subtitle;
    }
}
