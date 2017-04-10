package georgia.languagelandscape;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import georgia.languagelandscape.data.Recording;

public class RecordingActivity extends BaseActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    public static final int REQUEST_RECORD_AUDIO = 1001;
    public static final int CACHE_SIZE = 1024;
    private boolean audioPermissionGranted = false;
    private static String audioFileName = null;
    private static boolean canRecord = true;
    private static boolean canPlay = true;
    private long recordingTime = 0L;
    private Recording recording = null;
    private String defaultRecordingTitle = "New Recording";
    private String recordingTitle = null;
    private ArrayList<String> recordingLanguages = null;
    private String audioCacheFilePath = null;
    private String defaultAudioFormat = ".3gp";
    private String audioInternalFilePath = null;
    private File audioInternalFileDir = null;
    private String recordingDescription = null;
    private ArrayList<String> recordingSpeaker = null;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private static String location = null;

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;

    private Location currentLocation = null;
    private FloatingActionButton recordButton = null;
    private MediaRecorder recorder = null;
    private FloatingActionButton playButton = null;
    private MediaPlayer player = null;
    private TextView recordingTimer = null;
    private Handler handler = null;
    private Runnable timerRunnable = null;
    private TextView recordingName = null;
    private TextView recordingDate = null;
    private TextView recordingLocation = null;
    private EditText userDefinedName = null;
    private EditText userDefinedLanguages = null;
    private EditText userDefinedDescription = null;
    private EditText userDefinedSpeakers = null;
    private Button doneButton = null;
    private TextInputLayout nameInputLayout = null;
    private TextInputLayout languageInputLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: clean the file stored in the cache
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_nav_drawer);
        super.onDrawerCreated();

        recordingTimer = (TextView) findViewById(R.id.timer);
        recordingName = (TextView) findViewById(R.id.recording_name);
        recordingDate = (TextView) findViewById(R.id.recording_date);
        recordingLocation = (TextView) findViewById(R.id.recording_location);
        nameInputLayout = (TextInputLayout) findViewById(R.id.userDefined_name_textInput_layout);
        recordingLanguages = new ArrayList<>();
        recordingSpeaker = new ArrayList<>();

        userDefinedName = (EditText) findViewById(R.id.userDefined_name);
        userDefinedLanguages = (EditText) findViewById(R.id.userDefined_languages);
        userDefinedDescription = (EditText) findViewById(R.id.userDefined_description);
        userDefinedSpeakers = (EditText) findViewById(R.id.userDefined_speakers);

        try {
            audioInternalFilePath = getFilesDir().getCanonicalPath() + "/recordings";
            audioInternalFileDir = new File(audioInternalFilePath);
            if (!audioInternalFileDir.exists()) {
                boolean fileCreated = audioInternalFileDir.mkdir();
                Log.d(LOG_TAG, "internal audio file dir created: " + String.valueOf(fileCreated));
            }
            Log.d(LOG_TAG, "internal audio file dir already exist");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int numDup = checkDuplication(defaultRecordingTitle);
        recordingTitle = numDup == 0 ? defaultRecordingTitle : defaultRecordingTitle + " " + numDup;
        recordingName.setText(recordingTitle);
        nameInputLayout.setHint(recordingTitle);

        longitude = getIntent().getExtras().getDouble(MapActivity.GEO_LONGITUDE);
        latitude = getIntent().getExtras().getDouble(MapActivity.GEO_LATITUDE);
        location = updateLocation(longitude, latitude);
        recordingLocation.setText(location);

        doneButton = (Button) findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if nothing has been recorded
                File from = new File(audioFileName);
                if (!from.exists()) {
                    finish();
                    return;
                }
                // mandatory fields need to have info
                String languages = userDefinedLanguages.getText().toString();
                if (languages.equals("")) {
                    userDefinedLanguages.requestFocus();
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(view, 0);
                    }
//                        languageInputLayout.setError("Which language did you speak?");
                    return;
                } else {
                    recordingLanguages.add(languages);
                }

                String speakers = userDefinedSpeakers.getText().toString();
                if (speakers.equals("")) {
                    userDefinedSpeakers.requestFocus();
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(view, 0);
                    }
//                        userDefinedSpeakers.setError("Please specify the speaker.");
                    return;
                } else {
                    recordingSpeaker.add(speakers);
                }

                String description = userDefinedDescription.getText().toString();
                recordingDescription = description.equals("") ? null : description;

                String title = userDefinedName.getText().toString();
                recordingTitle = title.equals("") ? defaultRecordingTitle : title;

                /* copy the file from cache to internal storage */
                int numDup;
                if (recordingTitle.matches(defaultRecordingTitle + " [0-9]+$")) {
                    numDup = checkDuplication(defaultRecordingTitle);
                    recordingTitle =
                            numDup == 0 ? defaultRecordingTitle : defaultRecordingTitle + " " + numDup;
                } else {
                    numDup = checkDuplication(recordingTitle);
                    recordingTitle =
                            numDup == 0 ? recordingTitle : recordingTitle + " " + numDup;
                }
                File to = new File(audioInternalFileDir, recordingTitle + defaultAudioFormat);
                audioFileName = to.getAbsolutePath();
                boolean success = from.renameTo(to);

                /* build the recording class from data collected */
                if (success) {
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(audioFileName);
                    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long duration = Long.parseLong(durationStr);
                    recording = new Recording();
                    recording.setDuration(duration);
                    recording.setTitle(recordingTitle);
                    recording.setDate(recordingDate.getText().toString());
                    recording.setDescription(recordingDescription);
                    recording.setLanguage(recordingLanguages);
                    recording.setSpeakers(recordingSpeaker);
                    recording.setLatitude(latitude);
                    recording.setLongitude(longitude);
                    recording.setLocation(location);
                    Log.i(LOG_TAG, recording.toString());
                    // TODO: set the uploader in the future
                    // TODO: take to the list of recordings the user has

                } else {
                    if (!canPlay){
                        stopPlaying();
                    }
                }
            }
        });

        userDefinedName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        userDefinedLanguages.requestFocus();
                        return true;
                    default:
                        return false;
                }
            }
        });
        userDefinedLanguages.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        userDefinedSpeakers.requestFocus();
                        return true;
                    default:
                        return false;
                }
            }
        });

        userDefinedSpeakers.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        userDefinedDescription.requestFocus();
                        return true;
                    default:
                        return false;
                }
            }
        });

        userDefinedDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        userDefinedDescription.clearFocus();
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

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

        try {
            audioCacheFilePath = getCacheDir().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioFileName = audioCacheFilePath + "/" + System.currentTimeMillis() + defaultAudioFormat;

        recordButton = (FloatingActionButton) findViewById(R.id.recorder);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(canRecord);
            }
        });

        playButton = (FloatingActionButton) findViewById(R.id.player);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(canPlay);
            }
        });

        Date currentDateTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        recordingDate.setText(formatter.format(currentDateTime));
    }

    private int checkDuplication(final String recordingTitle) {
        Log.d(LOG_TAG, "Duplicated files: ");
        String[] recordings = audioInternalFileDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(defaultAudioFormat)
                        && name.startsWith(recordingTitle);
            }
        });
        for (String recording : recordings) {
            Log.i(LOG_TAG, recording);
        }
        return recordings.length;
    }

    private void onPlay(boolean canPlay) {
        if (canPlay)
            startPlaying();
        else
            stopPlaying();
    }

    private void stopPlaying() {
        player.release();
        player = null;
        canPlay = true;
        doneButton.setClickable(true);
        recordButton.setClickable(true);
    }

    private void startPlaying() {
        canPlay = false;
        doneButton.setClickable(true);
        recordButton.setClickable(false);

        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                canPlay = true;
                recordButton.setClickable(true);
            }
        });

        try {
            player.setDataSource(audioFileName);
            player.prepare();
        } catch (IOException e) {
            Log.i(LOG_TAG, "playing failed");
        }
        player.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void onRecord(boolean canRecord) {
        if (canRecord) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void stopRecording() {
        canRecord = true;
        doneButton.setClickable(true);
        playButton.setClickable(true);

        recorder.stop();
        handler.removeCallbacks(timerRunnable);
        recorder.release();
        recorder = null;
        /*
        * Animation:
        *   -the screen contracts to the top
        *   -edit|play|delete buttons appear at the bottom of the waveform
        *   -a list of information about the new recording will appear down below the waveform
        *   -save & [save and] upload buttons
        * */
    }

    private void startRecording() {
        if (!audioPermissionGranted) {
            int permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.RECORD_AUDIO);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO);
                return;
            } else {
                audioPermissionGranted = true;
            }
        }

        canRecord = false;
        doneButton.setClickable(false);
        playButton.setClickable(false);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(audioFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.i(LOG_TAG, "prepare() failed.");
        }
        recordingTime = System.currentTimeMillis();
        handler.postDelayed(timerRunnable, 0);
        recorder.start();
    }

    private String updateLocation(double longitude, double latitude) {
        Geocoder gcd = new Geocoder(this.getBaseContext(), Locale.getDefault());
        String cityName = null;
        String countryName = null;
        String location = null;
        try {
            List<Address> addresses = gcd.getFromLocation(
                    latitude,
                    longitude,
                    1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
                countryName = addresses.get(0).getCountryName();
                location = cityName + ", " + countryName;
                return location;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    private boolean askLocationPermission() {
//        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
//            Toast.makeText(this, "This app only works on devices with usable external storage",
//                    Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    audioPermissionGranted = true;
                } else {
                    //TODO: pop up a box with two buttons: 'cancel' and 'settings'
                    Toast.makeText(this,
                            "Unable to record.\nGo to Settings > Privacy to change the permission",
                            Toast.LENGTH_SHORT).show();
//                    finish();
                }
        }
    }
}