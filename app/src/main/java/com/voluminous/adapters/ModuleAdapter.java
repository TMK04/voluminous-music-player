package com.voluminous.adapters;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.voluminous.Media;
import com.voluminous.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleHolder> {
    private final ArrayList<Media> module_items;
    public interface OnSearchResultClickListener {
        void onSearchResultClick(int position);
    }
    private final OnSearchResultClickListener onSearchResultClickListener;
    public static class ModuleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatImageView art;
        AppCompatTextView title, subtitle;
        OnSearchResultClickListener onSearchResultClickListener;

        public ModuleHolder(View itemView, OnSearchResultClickListener onSearchResultClickListener) {
            super(itemView);
            art = itemView.findViewById(R.id.module_item_art);
            title = itemView.findViewById(R.id.module_item_title);
            subtitle = itemView.findViewById(R.id.module_item_subtitle);
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

    public ModuleAdapter(ArrayList<Media> module_items, OnSearchResultClickListener onSearchResultClickListener) {
        this.module_items = module_items;
        this.onSearchResultClickListener = onSearchResultClickListener;
    }

    @NotNull
    @Override
    public ModuleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_module_item, parent, false);
        return new ModuleHolder(view, onSearchResultClickListener);
    }

    @Override
    public void onBindViewHolder(@NotNull ModuleHolder holder, int position) {
        Media module_item = module_items.get(position);

        int art_size = context.getResources().getDimensionPixelSize(R.dimen.size_module_art);
        Picasso.get().load(module_item.getTHUMBNAIL_URL()).resize(art_size, art_size).centerCrop().into(holder.art);

        holder.title.setText(module_item.getTITLE());
        holder.subtitle.setText(module_item.getSUBTITLE());
    }

    @Override
    public int getItemCount() {
        if(module_items != null) {
            return module_items.size();
        }
        return 0;
    }
}