package com.voluminous;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private ArrayList<SearchResult> searchResults;
    private OnSearchResultListener onSearchResultListener;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatImageView art;
        AppCompatTextView title, subtitle;
        AppCompatCheckBox checkbox;
        OnSearchResultListener onSearchResultListener;


        public ViewHolder(View itemView, OnSearchResultListener _onSearchResultListener) {
            super(itemView);
            art = itemView.findViewById(R.id.search_result_art);
            title = itemView.findViewById(R.id.search_result_title);
            subtitle = itemView.findViewById(R.id.search_result_subtitle);
            checkbox = itemView.findViewById(R.id.search_result_checkbox);
            onSearchResultListener = _onSearchResultListener;
        }
        @Override
        public void onClick(View v) {
            onSearchResultListener.onSearchResultClick(getAbsoluteAdapterPosition());
        }
    }
    public interface OnSearchResultListener {
        void onSearchResultClick(int position);
    }

    public SearchResultsAdapter(ArrayList<SearchResult> _searchResults, OnSearchResultListener _onSearchResultListener) {
        searchResults = _searchResults;
        onSearchResultListener = _onSearchResultListener;
    }

    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_result, parent, false);
        return new ViewHolder(view, onSearchResultListener);
    }

    @Override
    public void onBindViewHolder(SearchResultsAdapter.ViewHolder holder, int position) {
        holder.title.setText(searchResults.get(position).toString());
        holder.subtitle.setText(searchResults.get(position).toString());
    }

    @Override
    public int getItemCount() { return searchResults.size(); }
}