package com.voluminous;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Voluminous extends Application {
    private static Application voluminous;
    private static String key;

    @Override
    public void onCreate() {
        super.onCreate();
        voluminous = this;
        key = getKey(getContext());
    }

    public static Application getApplication() {
        return voluminous;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    public static String getName() {
        ApplicationInfo applicationInfo = getContext().getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getContext().getString(stringId);
    }

    public static void clearFocus(View v) {
        v.clearFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static String getKey() {
        return key;
    }

    private static String getKey(Context context) {
        String api_key = null;
        try {
            //Read API Key from assets/api_key.json
            InputStream inputStream = context.getAssets().open("api_key.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            api_key = new JSONObject(new String (buffer, StandardCharsets.UTF_8)).getString("key");
            if(api_key.equals("")) throw new IOException("Make sure there is an \"assets/api_key.json\" file");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return api_key;
    }
}