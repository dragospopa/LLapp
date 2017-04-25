package georgia.languagelandscape.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import georgia.languagelandscape.R;

/**
 * RecordFragment is where user maker recordings.
 * The fragment layout contains three buttons, one for record,
 * one for playback and one for saving the recording.
 *
 * When a user makes a recording, he/she can discard the current
 * recording by making a new one. Alternatively, he/she can play & pause the
 * recording just made and decide to save it.
 *
 * When a user decides to save the recording, {@link MetaDataFragment} is load
 * for user to enter information about the recording.
 * Other meta-data such as location and date is handled and automatically added
 * to the recording by this fragment and its listener.
 *
 * Activities that contains this fragment must implements
 * {@link RecordFragmentListener} interface to interact with
 * record, play and save events.
 */
public class RecordFragment extends Fragment {

    private Context context;
    private long recordingTime = 0L;
    private boolean canRecord = true;
    public static final String TAG = "RecordFragment";
    public static final String ARGS_LOCATIOIN = "location args";
    public static final String ARGS_TITLE = "title args";

    private TextView recordingName = null;
    private TextView recordingDate = null;
    private TextView recordingLocation = null;
    private TextView recordingTimer = null;
    private Handler handler = null;
    private Runnable timerRunnable = null;
    private Button recordButton = null;
    private Button playButton = null;
    private Button saveButton = null;


    public RecordFragment() {
    }

    public static RecordFragment newInstance(String location, String title) {
        RecordFragment recordFragment = new RecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RecordFragment.ARGS_LOCATIOIN, location);
        bundle.putString(RecordFragment.ARGS_TITLE, title);
        recordFragment.setArguments(bundle);

        return recordFragment;
    }

    public interface RecordFragmentListener {
        public boolean onRecordClick(boolean canRecord);

        public void onPlayClick();

        public void onSaveClick();

        public void setDateString(String dateString);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, container, false);
        String location = getArguments().getString(ARGS_LOCATIOIN);
        String title = getArguments().getString(ARGS_TITLE);

        recordingTimer = (TextView) view.findViewById(R.id.timer);
        recordingName = (TextView) view.findViewById(R.id.recording_name);
        recordingDate = (TextView) view.findViewById(R.id.recording_date);
        recordingLocation = (TextView) view.findViewById(R.id.recording_location);
        recordingLocation.setText(location);
        recordingName.setText(title);

        Date currentDateTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        String date = formatter.format(currentDateTime);
        recordingDate.setText(date);
        // pass the date to its listener for storage.
        ((RecordFragmentListener) context).setDateString(date);

        // the handler for displaying recording time dynamically
        handler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - recordingTime;
                int seconds = (int) (elapsed / 1000);
                int millis = (int) (elapsed % 1000) / 10;
                int minutes = seconds / 60;
                seconds = seconds % 60;

                recordingTimer.setText(String.format("%02d:%02d.%02d", minutes, seconds, millis));
                handler.postDelayed(this, 0);
            }
        };

        recordButton = (Button) view.findViewById(R.id.recorder);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success;
                success = ((RecordFragmentListener) context).onRecordClick(canRecord);
                // if for any reason attempt to record fails, return
                if (!success) {
                    return;
                }
                recordingTime = System.currentTimeMillis();
                handler.postDelayed(timerRunnable, 0);

                // set button image according to the state of recorder
                if (canRecord) {
                    recordButton.setBackgroundResource(R.drawable.ic_stop_black_24dp);
                } else {
                    recordButton.setBackgroundResource(R.drawable.ic_fiber_manual_record_black_24dp);
                    handler.removeCallbacks(timerRunnable);
                }
                canRecord = !canRecord;
                saveButton.setClickable(canRecord);
                playButton.setClickable(canRecord);
            }
        });

        playButton = (Button) view.findViewById(R.id.player);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecordFragmentListener) context).onPlayClick();
            }
        });

        saveButton = (Button) view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecordFragmentListener) context).onSaveClick();
            }
        });

        return view;
    }

    /**
     * Callback when its listener plays or pauses the recording
     *
     * @param paused the state of recording: playing or paused
     */
    public void notifyRecordingPlaying(boolean paused) {
        if (!paused) {
            playButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        } else if (paused) {
            playButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        }
        canRecord = paused;
        recordButton.setClickable(canRecord);
        saveButton.setClickable(canRecord);
    }

    /**
     * Callback called by its listener that the recording has finished playing.
     */
    public void notifyRecordingCompleted() {
        playButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        canRecord = true;
        saveButton.setClickable(true);
        recordButton.setClickable(true);
    }
}
