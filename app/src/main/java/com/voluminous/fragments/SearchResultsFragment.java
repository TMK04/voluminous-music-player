package com.voluminous.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.voluminous.Media;
import com.voluminous.R;
import com.voluminous.Voluminous;
import com.voluminous.adapters.SearchResultsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResultsFragment extends Fragment implements SearchResultsAdapter.OnSearchResultClickListener {

    private final ArrayList<Media> search_results;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference queue_reference = db.collection("Queue");

    public SearchResultsFragment(ArrayList<Media> searchResults) {
        this.search_results = searchResults;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        RecyclerView searchRecyclerView = rootView.findViewById(R.id.search_results);

        SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(search_results, this);
        searchRecyclerView.setAdapter(searchResultsAdapter);
        return rootView;
    }

    @Override
    public void onSearchResultClick(int position) {
        Media searchResult = search_results.get(position);

        Map<String, Object> queue_song = new HashMap<>();
        queue_song.put(Media.KEY_TITLE, searchResult.getTITLE());
        queue_song.put(Media.KEY_SUBTITLE, searchResult.getSUBTITLE());
        queue_song.put(Media.KEY_THUMBNAIL_URL, searchResult.getTHUMBNAIL_URL());
        queue_song.put(Media.KEY_THUMBNAIL_URL_HIGH, searchResult.getTHUMBNAIL_URL_HIGH());
        queue_reference.document(searchResult.getID()).set(queue_song)
            .addOnSuccessListener(unused -> Toast.makeText(Voluminous.getContext(), "Added to Queue", Toast.LENGTH_SHORT).show())
            .addOnFailureListener(Throwable::printStackTrace);
    }
}