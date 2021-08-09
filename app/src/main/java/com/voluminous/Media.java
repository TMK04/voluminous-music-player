package com.voluminous;

public class Media {
    private final String TYPE, ID,
            TITLE, SUBTITLE, DESCRIPTION,
            THUMBNAIL_URL, THUMBNAIL_URL_HIGH;
    public static final String KEY_TITLE = "song_name",
            KEY_SUBTITLE = "artist",
            KEY_THUMBNAIL_URL = "thumbnail_url",
            KEY_THUMBNAIL_URL_HIGH = "thumbnail_url_high";
    private boolean saved = false;

    public Media(String type, String id, String title, String subtitle, String description, String thumbnail_url, String thumbnail_url_high) {
        TYPE = type;
        ID = id;
        TITLE = title;
        SUBTITLE = subtitle;
        DESCRIPTION = description;
        THUMBNAIL_URL = thumbnail_url;
        THUMBNAIL_URL_HIGH = thumbnail_url_high;
    }

    public String getTYPE() {
        return TYPE;
    }

    public String getID() {
        return ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public String getSUBTITLE() {
        return SUBTITLE;
    }

    public String getTHUMBNAIL_URL() {
        return THUMBNAIL_URL;
    }

    public String getTHUMBNAIL_URL_HIGH() {
        return THUMBNAIL_URL_HIGH;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved() {
        saved = !saved;
    }
}