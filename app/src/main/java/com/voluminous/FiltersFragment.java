package com.voluminous;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.util.Locale;

public class FiltersFragment extends Fragment implements LabelFormatter {
    private MaterialCheckBox checkbox_artists;
    private MaterialCheckBox checkbox_songs;
    private RangeSlider slider_song_length;
    private AppCompatTextView reset_filters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);

        checkbox_artists = view.findViewById(R.id.checkbox_artists);
        checkbox_songs = view.findViewById(R.id.checkbox_songs);
        slider_song_length = view.findViewById(R.id.slider_song_length);
        slider_song_length.setLabelFormatter(this);
        reset_filters = view.findViewById(R.id.reset_filters);
        reset_filters.setOnClickListener(v -> setDefault());
        
        setDefault();

        return view;
    }

    @Override
    public String getFormattedValue(float value) {
        int minutes = (int) Math.floor(value / 60);
        int seconds = (int) value % 60;
        String secondsString = String.format(Locale.getDefault(), "%02d", seconds);
        return minutes + ":" + secondsString;
    }

    private void setDefault() {
        checkbox_artists.setChecked(true);
        checkbox_songs.setChecked(true);
        slider_song_length.setValues(30f, 600f);
    }

    public String getFilters() {
        String suffix;
        String type = (checkbox_artists.isChecked() ? "channel" : "") + (checkbox_songs.isChecked() ? ",video" : "");
        suffix = (type.equals("") ? "" : "&type=") + type;
        return suffix;
    }
}