package com.voluminous;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
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

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=AIzaSyDsJPHOAEYfVLDtHj1mBm0ocOQVAsd_Nwk";
    private final int CONTENT_ID = R.id.content;
    private ArrayList<SearchResult> searchResults;
    private MaterialToolbar searchbar;
    private TextInputLayout searchbar_search;
    private TextInputEditText searchbar_input;
    private FloatingActionButton navigation_fab;
    private Fragment home_fragment;
    private Fragment library_fragment;
    private Fragment filters_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_fragment = new HomeFragment();
        library_fragment = new LibraryFragment();
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

        filters_fragment = new FiltersFragment();
        searchbar = findViewById(R.id.searchbar);
        searchbar.findViewById(R.id.searchbar_filters).setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(CONTENT_ID);
            if(currentFragment instanceof FiltersFragment) {
                getSupportFragmentManager().beginTransaction().remove(filters_fragment).addToBackStack("filters").commit();
            } else if (getSupportFragmentManager().findFragmentByTag("filters")==null) {
                getSupportFragmentManager().beginTransaction().add(CONTENT_ID, filters_fragment).commit();
            } else {
                getSupportFragmentManager().popBackStack("filters", 0);
            }
        });
        searchbar_search = findViewById(R.id.searchbar_search);
        searchbar_input = findViewById(R.id.searchbar_input);
        searchbar_input.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
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
        return null;
    }
}