package com.voluminous;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleArtViewHolder> {
    public class ModuleArtViewHolder extends RecyclerView.ViewHolder {
        public ModuleArtViewHolder(View itemView) {
            super(itemView);
        }
    }

    public ModuleAdapter(Context context) {

    }

    @Override
    public ModuleArtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ModuleArtViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
