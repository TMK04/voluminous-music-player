package com.voluminous.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.voluminous.R;

import java.util.List;

public class FiltersFragment extends Fragment implements LabelFormatter {
    private MaterialCheckBox checkbox_artists;
    private MaterialCheckBox checkbox_songs;
    private RangeSlider slider_duration;
    private AppCompatTextView reset_filters;
    private static final String[] duration_values = {"short", "medium", "long"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);

        checkbox_artists = view.findViewById(R.id.checkbox_artists);
        checkbox_songs = view.findViewById(R.id.checkbox_songs);
        slider_duration = view.findViewById(R.id.slider_song_length);
        slider_duration.setLabelFormatter(this);
        reset_filters = view.findViewById(R.id.reset_filters);
        reset_filters.setOnClickListener(v -> setDefault());
        
        setDefault();

        return view;
    }

    @Override
    public String getFormattedValue(float value) {
        return duration_values[(int)value];
    }

    private void setDefault() {
        checkbox_artists.setChecked(true);
        checkbox_songs.setChecked(true);
        slider_duration.setValues(0f, 2f);
    }

    public String getFilters() {
        String suffix = "";

        String type = (checkbox_artists.isChecked() ? "channel" : "") + (checkbox_songs.isChecked() ? ",video" : "");
        if(type.equals("")) return suffix;
        suffix += "&type=" + type;

        suffix += "&duration=";
        List<Float> slider_duration_values = slider_duration.getValues();
        for(float i = slider_duration_values.get(0); i < duration_values.length ; i++) {
            suffix += duration_values[(int)i];
        }
        return suffix;
    }
}