/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import georgia.languagelandscape.data.Recording;

/**
 * MarkerDialogActivity uses DialogActivity style and will be created
 * when a marker is clicked.
 *
 * It displays detailed information about a recording with the ability to play.
 * This fragment contains a seekbar which we used to visually track the progress
 * of playing the recording.
 * User may drag seekbar to change playing progress.
 */
public class MarkerDialogActivity extends AppCompatActivity {

    private long duration = 0L;
    private Recording recording;
    private static final String LOG_MARKERS = "markers";
    private Handler playtimeHandler;
    private Handler completionHandler;
    private Thread progressBarThread;
    private Runnable progressBarRunnable;
    private Runnable playtimeRunnable;
    private Runnable completionRunnable;
    private TextView marker_playtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_marker_dialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView marker_title = (TextView) findViewById(R.id.marker_title);
        TextView marker_duration = (TextView) findViewById(R.id.marker_duration);
        marker_playtime = (TextView) findViewById(R.id.marker_playtime);
        TextView marker_date_content = (TextView) findViewById(R.id.marker_date_content);
        TextView marker_language_content = (TextView) findViewById(R.id.marker_language_content);
        TextView marker_location_content = (TextView) findViewById(R.id.marker_location_content);
        TextView marker_speaker_content = (TextView) findViewById(R.id.marker_speaker_content);
        final Button playButton = (Button) findViewById(R.id.marker_play);
        final SeekBar playProgressBar = (SeekBar) findViewById(R.id.marker_playProgress);

        playProgressBar.setMax(1000);

        recording = getIntent().getExtras().getParcelable(Recording.PARCEL_KEY);

        marker_title.setText(recording.getTitle());
        marker_date_content.setText(recording.getDate());
        marker_location_content.setText(recording.getLocation());

        duration = recording.getDuration();
        int seconds = (int) (duration / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        marker_duration.setText(String.format("%02d:%02d", minutes, seconds));

        marker_language_content.setText(recording.getLanguage().get(0));
        marker_speaker_content.setText(recording.getSpeakers().get(0));

        progressBarRunnable = new Runnable() {
            @Override
            public void run() {
                while (!recording.isCompleted() && !progressBarThread.isInterrupted()) {
                    int progress = 0;
                    try {
                        progress = recording.getCurrentPlaytime();
                    } catch (IllegalStateException e) {
                        // this thread is not interrupted when the player is released
                        Log.d(LOG_MARKERS, "hit the illegal state again");
                        break;
                    }
                    if (progress == -1) {
                        break;
                    }
                    int percentage = (int) Math.round((progress / ((double) duration)) * 1000);
                    playProgressBar.setProgress(percentage);
                }
                playProgressBar.setProgress(0);
            }
        };

        playtimeRunnable = new Runnable() {
            @Override
            public void run() {
                int playtime = recording.getCurrentPlaytime();
                int seconds = playtime / 1000;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                marker_playtime.setText(String.format("%02d:%02d", minutes, seconds));
                playtimeHandler.postDelayed(this, 0);
            }
        };

        completionRunnable = new Runnable() {
            @Override
            public void run() {
                if (recording.isCompleted()) {
                    playButton.setBackgroundResource(R.drawable.play);
                    playtimeHandler.removeCallbacks(playtimeRunnable);
                    progressBarThread.interrupt();
                    completionHandler.removeCallbacks(this);
                } else {
                    completionHandler.postDelayed(this, 0);
                }
            }
        };

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording.play(recording.getCurrentPlaytime());
                if (!recording.isPaused()) {
                    playButton.setBackgroundResource(R.drawable.pause_icon);
                } else if (recording.isPaused()) {
                    playButton.setBackgroundResource(R.drawable.play);
                }

                playtimeHandler = new Handler();
                playtimeHandler.postDelayed(playtimeRunnable, 0);

                completionHandler = new Handler();
                completionHandler.postDelayed(completionRunnable, 0);

                progressBarThread = new Thread(progressBarRunnable);
                progressBarThread.start();
            }
        });

        playProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int playtime;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    playtime = (int) Math.round(((double) progress / seekBar.getMax()) * duration);
                    int seconds = playtime / 1000;
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    marker_playtime.setText(String.format("%02d:%02d", minutes, seconds));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (progressBarThread != null) {
                    progressBarThread.interrupt();
                }
                if (playtimeHandler != null) {
                    playtimeHandler.removeCallbacks(playtimeRunnable);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                recording.setCurrentPlaytime(playtime);
                progressBarThread = new Thread(progressBarRunnable);
                progressBarThread.start();
                if (playtimeHandler != null) {
                    playtimeHandler.postDelayed(playtimeRunnable, 0);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressBarThread != null) {
            progressBarThread.interrupt();
        }
        recording.stop();
        if (playtimeHandler != null) {
            playtimeHandler.removeCallbacks(playtimeRunnable);
        }
        if (completionHandler != null) {
            completionHandler.removeCallbacks(completionRunnable);
        }
    }
}