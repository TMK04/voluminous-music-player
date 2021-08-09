package com.voluminous;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.voluminous.fragments.FiltersFragment;
import com.voluminous.fragments.HomeFragment;
import com.voluminous.fragments.LibraryFragment;
import com.voluminous.fragments.SearchResultsFragment;
import com.voluminous.tasks.PlayTask;
import com.voluminous.tasks.SearchTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int CONTENT_ID = R.id.content;
    private FragmentManager fm;
    private final HomeFragment HOME_FRAGMENT = new HomeFragment();
    private final LibraryFragment LIBRARY_FRAGMENT = new LibraryFragment();
    private FiltersFragment filters_fragment = new FiltersFragment();
    private AppCompatTextView navigation_title, navigation_subtitle;
    private YouTubePlayerView player_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(CONTENT_ID, HOME_FRAGMENT).commit();

        MaterialToolbar searchbar = findViewById(R.id.searchbar);
        TextInputEditText searchbar_input = findViewById(R.id.searchbar_input);
        TextInputLayout searchbar_search = findViewById(R.id.searchbar_search);
        searchbar.findViewById(R.id.searchbar_filters).setOnClickListener(v -> {
            Fragment currentFragment = fm.findFragmentById(CONTENT_ID);
            String tag = "filters";

            if(currentFragment instanceof FiltersFragment) {
                fm.beginTransaction().remove(filters_fragment)
                        .addToBackStack(tag).commit();
            } else if (fm.findFragmentByTag(tag)==null) {
                fm.beginTransaction().add(CONTENT_ID, filters_fragment, tag).commit();
            } else {
                fm.popBackStack(tag, 1);
            }
        });
        searchbar_input.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                Voluminous.clearFocus(v);
                String query = v.getText().toString();
                if(!query.isEmpty()) {
                    ArrayList<Media> searchResults = getSearchResults(query);
                    SearchResultsFragment search_results_fragment = new SearchResultsFragment(searchResults);
                    fm.beginTransaction().replace(CONTENT_ID, search_results_fragment)
                            .addToBackStack(null).commit();
                }
                return true;
            }
            return false;
        });
        searchbar_search.setStartIconOnClickListener(v -> searchbar_input.onEditorAction(EditorInfo.IME_ACTION_SEARCH));

        FloatingActionButton navigation_fab = findViewById(R.id.navigation_fab);
        navigation_fab.setOnClickListener(v -> {
            Fragment currentFragment = fm.findFragmentById(CONTENT_ID);
            if(currentFragment instanceof HomeFragment) {
                fm.beginTransaction().replace(CONTENT_ID, LIBRARY_FRAGMENT)
                        .addToBackStack(currentFragment.getTag()).commit();
                v.setActivated(true);
            } else {
                fm.beginTransaction().replace(CONTENT_ID, HOME_FRAGMENT)
                        .addToBackStack(currentFragment!=null ? currentFragment.getTag() : null).commit();
                v.setActivated(false);
            }
        });

        player_view = findViewById(R.id.player_view);
        startPlayThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player_view.release();
    }

    //EditTexts exit focus when clicked outside
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    Voluminous.clearFocus(v);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private ArrayList<Media> getSearchResults(String query) {
        FiltersFragment filters_fragment = (FiltersFragment) fm.findFragmentByTag("filters");
        if (filters_fragment != null) query += filters_fragment.getFilters();

        SearchTask searchTask = new SearchTask(this, query);
        Thread searchThread = new Thread(searchTask);
        searchThread.start();

        try {
            searchThread.join(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return searchTask.getSearchResults();
    }

    private void startPlayThread() {
        PlayTask playTask = new PlayTask(this);
        Thread playThread = new Thread(playTask);
        playThread.start();
    }
}