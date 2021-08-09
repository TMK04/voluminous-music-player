package com.voluminous.adapters;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.voluminous.Media;
import com.voluminous.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsHolder> {
    private final ArrayList<Media> search_results;
    public interface OnSearchResultClickListener {
        void onSearchResultClick(int position);
    }
    private final OnSearchResultClickListener onSearchResultClickListener;
    public static class SearchResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatImageView art;
        AppCompatTextView title, subtitle;
        AppCompatCheckBox checkbox;
        OnSearchResultClickListener onSearchResultClickListener;

        public SearchResultsHolder(View itemView, OnSearchResultClickListener onSearchResultClickListener) {
            super(itemView);
            art = itemView.findViewById(R.id.search_result_art);
            title = itemView.findViewById(R.id.search_result_title);
            subtitle = itemView.findViewById(R.id.search_result_subtitle);
            checkbox = itemView.findViewById(R.id.search_result_checkbox);
            art.setOnClickListener(this);
            title.setOnClickListener(this);
            this.onSearchResultClickListener = onSearchResultClickListener;
        }
        @Override
        public void onClick(View v) {
            onSearchResultClickListener.onSearchResultClick(getAbsoluteAdapterPosition());
        }
    }
    private Context context;

    public SearchResultsAdapter(ArrayList<Media> search_results, OnSearchResultClickListener onSearchResultClickListener) {
        this.search_results = search_results;
        this.onSearchResultClickListener = onSearchResultClickListener;
    }

    @NotNull
    @Override
    public SearchResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_search_result, parent, false);
        return new SearchResultsHolder(view, onSearchResultClickListener);
    }

    @Override
    public void onBindViewHolder(@NotNull SearchResultsHolder holder, int position) {
        Media search_result = search_results.get(position);

        int art_size = context.getResources().getDimensionPixelSize(R.dimen.button3XL);
        RequestCreator picasso_request = Picasso.get().load(search_result.getTHUMBNAIL_URL()).resize(art_size, art_size).centerCrop();
        if(search_result.getTYPE().equals("channel")) picasso_request.transform(new CropCircleTransformation());
        picasso_request.into(holder.art);

        holder.title.setText(search_result.getTITLE());
        holder.subtitle.setText(search_result.getSUBTITLE());
        switch(search_result.getTYPE()) {
            case "video":
                holder.checkbox.setBackground(AppCompatResources.getDrawable(context, R.drawable.selector_like));
                break;
            case "channel":
                holder.checkbox.setBackground(AppCompatResources.getDrawable(context, R.drawable.selector_follow));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(search_results != null) {
            return search_results.size();
        }
        return 0;
    }
}