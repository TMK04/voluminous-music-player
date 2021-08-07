package com.voluminous;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SearchResultsFragment extends Fragment implements SearchResultsAdapter.OnSearchResultListener {

    private ArrayList<SearchResult> searchResults = new ArrayList<>();

    public SearchResultsFragment() { super(R.layout.fragment_search_results); }

    @Override
    public void onSearchResultClick(int position) {
        SearchResult searchResult = searchResults.get(position);
    }
}