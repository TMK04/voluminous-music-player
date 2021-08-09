package com.voluminous.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.voluminous.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayTask implements Runnable, YouTubePlayerListener {
    private final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private final CollectionReference QUEUE_REFERENCE = DB.collection("Queue");
    private final AppCompatActivity ACTIVITY;
    private ArrayList<QueryDocumentSnapshot> queue = new ArrayList<>();
    private int index = 0;
    private YouTubePlayerView player_view;
    private BottomAppBar navigation;
    private AppCompatTextView navigation_title, navigation_subtitle;
    private CircularProgressIndicator navigation_trackbar;

    public PlayTask(AppCompatActivity activity) {
        this.ACTIVITY = activity;
    }

    @Override
    public void run() {
        QUEUE_REFERENCE.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                try {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        queue.add(document);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        player_view = ACTIVITY.findViewById(R.id.player_view);
        player_view.addYouTubePlayerListener(this);
        player_view.enableBackgroundPlayback(true);
        player_view.addYouTubePlayerListener(this);

        navigation = ACTIVITY.findViewById(R.id.navigation);
        navigation.findViewById(R.id.navigation_play).setOnClickListener(v -> {
            if(v.isActivated()) {
                player_view.getYouTubePlayerWhenReady(YouTubePlayer::pause);
            } else player_view.getYouTubePlayerWhenReady(YouTubePlayer::play);
        });
        navigation.findViewById(R.id.navigation_like).setOnClickListener(v -> v.setActivated(!v.isActivated()));
        navigation_title = navigation.findViewById(R.id.navigation_title);
        navigation_subtitle = navigation.findViewById(R.id.navigation_subtitle);
        navigation.findViewById(R.id.navigation_skip_next).setOnClickListener(v -> {
            if(index < queue.toArray().length) {
                index++;
                player_view.getYouTubePlayerWhenReady(player -> player.loadVideo(queue.get(index).getId(), 0));
            }
        });
    }

    @Override
    public void onApiChange(@NotNull YouTubePlayer player) {

    }

    @Override
    public void onCurrentSecond(@NotNull YouTubePlayer player, float v) {
    }

    @Override
    public void onError(@NotNull YouTubePlayer player, @NotNull PlayerConstants.PlayerError error) {

    }

    @Override
    public void onPlaybackQualityChange(@NotNull YouTubePlayer player, @NotNull PlayerConstants.PlaybackQuality quality) {

    }

    @Override
    public void onPlaybackRateChange(@NotNull YouTubePlayer player, @NotNull PlayerConstants.PlaybackRate rate) {

    }

    @Override
    public void onReady(@NotNull YouTubePlayer player) {
        player.cueVideo(queue.get(index).getId(), 0);
    }

    @Override
    public void onStateChange(@NotNull YouTubePlayer player, @NotNull PlayerConstants.PlayerState state) {
        try {
            if (state == PlayerConstants.PlayerState.PLAYING) {
                navigation.findViewById(R.id.navigation_play).setActivated(true);
                navigation_title.setText(queue.get(index).getData().get("song_name").toString());
                navigation_subtitle.setText(queue.get(index).getData().get("artist").toString());
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        navigation.findViewById(R.id.navigation_play).setActivated(false);
    }

    @Override
    public void onVideoDuration(@NotNull YouTubePlayer player, float v) {

    }

    @Override
    public void onVideoId(@NotNull YouTubePlayer player, @NotNull String s) {

    }

    @Override
    public void onVideoLoadedFraction(@NotNull YouTubePlayer player, float v) {

    }
}