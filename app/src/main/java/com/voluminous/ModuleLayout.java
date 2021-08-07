package com.voluminous;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ModuleLayout extends ConstraintLayout {

    public ModuleLayout(Context context) {
        super(context);
        init(context);
    }

    public ModuleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ModuleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ModuleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.merge_module, this);


        TextView title = (TextView) findViewById(R.id.module_title);
        Button more = (Button) findViewById(R.id.module_more);
        RecyclerView scroll = (RecyclerView) findViewById(R.id.module_scroll);

        String mIdString = idToString();
        title.setText(mIdString);
    }

    private String idToString() {
        int id = getId();
        String[] idSplit = getResources().getResourceEntryName(id).split("_");

        for(int i = 0; i < idSplit.length; i++) {
            idSplit[i] = idSplit[i].substring(0,1).toUpperCase() + idSplit[i].substring(1);
        }

        return String.join(" ", idSplit);
    }
}