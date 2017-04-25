package georgia.languagelandscape;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.data.User;
import georgia.languagelandscape.database.RecordingDataSource;
import georgia.languagelandscape.fragments.MetaDataFieldFragment;
import georgia.languagelandscape.fragments.MetaDataFragment;
import georgia.languagelandscape.fragments.RecordFragment;
import georgia.languagelandscape.util.MetaDataPagerAdaptor;

/**
 * The listener for {@link RecordFragment}, {@link MetaDataFragment}.
 */
public class RecordingActivity extends BaseActivity
        implements RecordFragment.RecordFragmentListener,
        MetaDataFragment.MetaDataFragmentListener,
        MetaDataFieldFragment.MetaDataFieldFragmentListener {

    private static final String LOG_TAG = "AudioRecordTest";
    public static final int REQUEST_RECORD_AUDIO = 1001;
    public static final int CACHE_SIZE = 1024;
    private boolean audioPermissionGranted = false;
    private static String audioFileName = null;
    private static boolean canPlay = true;
    private Recording recording = null;
    private Recording tempRecording = null;
    private String recordingTitle = null;
    private ArrayList<String> recordingLanguages = null;
    private String audioCacheFilePath = null;
    private String audioInternalFilePath = null;
    private File audioInternalFileDir = null;
    private String recordingDescription = null;
    private String recordingDateString = null;
    private String recordingLanguageString = "";
    private String recordingSpeakerString = "";
    private String recordingPublicEditString = "";
    private ArrayList<String> recordingSpeaker = null;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private static String location = null;
    private static boolean active = false;
    private FragmentManager fragmentManager;
    private Handler completionHandler;
    private Runnable completionRunnable;

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: clean the file stored in the cache
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_nav_drawer);
        super.onDrawerCreated();

        active = true;
        longitude = getIntent().getExtras().getDouble(MapActivity.GEO_LONGITUDE);
        latitude = getIntent().getExtras().getDouble(MapActivity.GEO_LATITUDE);
        location = updateLocation(longitude, latitude);

        recordingLanguages = new ArrayList<>();
        recordingSpeaker = new ArrayList<>();

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
        int numDup = checkDuplication(Recording.defaultRecordingTitle);
        recordingTitle = numDup == 0 ?
                Recording.defaultRecordingTitle :
                Recording.defaultRecordingTitle + " " + numDup;

        fragmentManager = getSupportFragmentManager();
        RecordFragment recordFragment = RecordFragment.newInstance(location, recordingTitle);
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, recordFragment, RecordFragment.TAG)
                .commit();

        // the handler that check whether or not a recording has finished playing yet
        completionHandler = new Handler();
        completionRunnable = new Runnable() {
            @Override
            public void run() {
                if (tempRecording.isCompleted()) {
                    RecordFragment recordFragment =
                            (RecordFragment) fragmentManager.findFragmentByTag(RecordFragment.TAG);
                    recordFragment.notifyRecordingCompleted();
                    completionHandler.removeCallbacks(this);
                } else {
                    completionHandler.postDelayed(this, 0);
                }
            }
        };

        try {
            audioCacheFilePath = getCacheDir().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioFileName = audioCacheFilePath + "/" + System.currentTimeMillis() + Recording.defaultAudioFormat;
    }

    /**
     * Helper for checking recording name duplication in internal storage.
     *
     * @param recordingTitle the recording name to be checked
     * @return number of duplicated file name
     */
    private int checkDuplication(final String recordingTitle) {
        Log.d(LOG_TAG, "Duplicated files: ");
        String[] recordings = audioInternalFileDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(Recording.defaultAudioFormat)
                        && name.startsWith(recordingTitle);
            }
        });
        for (String recording : recordings) {
            Log.i(LOG_TAG, recording);
        }
        return recordings.length;
    }

    private boolean onRecord(boolean canRecord) {
        if (canRecord) {
            return startRecording();
        } else {
            return stopRecording();
        }
    }

    private boolean stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        tempRecording = new Recording();
        tempRecording.setFilePath(audioFileName);
        return true;
    }

    /**
     * Prepare the MediaRecorder and record.
     * First check permission to record audio.
     *
     * @return true if recorder started recording
     *         false otherwise
     */
    private boolean startRecording() {
        if (!audioPermissionGranted) {
            int permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.RECORD_AUDIO);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO);
                return false;
            } else {
                audioPermissionGranted = true;
            }
        }

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(audioFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.i(LOG_TAG, "prepare() failed.");
            return false;
        }
        recorder.start();
        return true;
    }

    /**
     * Given the longitude and latitude, uses Geocoder to calculate location.
     *
     * @param longitude geo-longitude
     * @param latitude geo-latitude
     * @return a string representing the location
     *         in the form of "city name, country name".
     */
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

    public static boolean isActive() {
        // called by other activities to see if this one is alive
        return active;
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
                }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;

        if (tempRecording != null) {
            tempRecording.stop();
        }
        if (completionHandler != null) {
            completionHandler.removeCallbacks(completionRunnable);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        active = true;
    }

    @Override
    public boolean onRecordClick(boolean canRecord) {
        return onRecord(canRecord);
    }

    @Override
    public void onPlayClick() {
        if (tempRecording == null) return;
        tempRecording.play(tempRecording.getCurrentPlaytime());
        RecordFragment recordFragment =
                (RecordFragment) fragmentManager.findFragmentByTag(RecordFragment.TAG);
        boolean paused = tempRecording.isPaused();
        recordFragment.notifyRecordingPlaying(paused);
        if (!paused) {
            completionHandler.postDelayed(completionRunnable, 0);
        } else {
            completionHandler.removeCallbacks(completionRunnable);
        }
    }

    @Override
    public void onSaveClick() {
        File from = new File(audioFileName);
        if (!from.exists()) {
            finish();
            return;
        }
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, new MetaDataFragment(), MetaDataFragment.TAG)
                .addToBackStack(RecordFragment.TAG)
                .commit();

    }

    @Override
    public void setDateString(String dateString) {
        recordingDateString = dateString;
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void clearFocus() {
        View view = getCurrentFocus();
        if (view != null) {
            view.clearFocus();
        }
    }

    /**
     * Callback by the {@link MetaDataFragment}
     * First check the mandatory field inputs. If any of them is empty, call
     * {@link MetaDataFragment#focusAt(int)} to focus to the question page
     * we want.
     *
     * When all mandatory fields are ready, instantiate a new recording data
     * and set its corresponding fields.
     *
     * Finally, insert the recording into database and start {@link MyRecordingsActivity}.
     */
    @Override
    public void onFinishClick() {
        if (recordingLanguageString.equals("")) {
            MetaDataFragment dataFragment =
                    (MetaDataFragment) fragmentManager.findFragmentByTag(MetaDataFragment.TAG);
            dataFragment.focusAt(MetaDataFieldFragment.languages);
            return;
        } else if (recordingSpeakerString.equals("")) {
            MetaDataFragment dataFragment =
                    (MetaDataFragment) fragmentManager.findFragmentByTag(MetaDataFragment.TAG);
            dataFragment.focusAt(MetaDataFieldFragment.speakers);
            return;
        } else {
            recordingLanguages.add(recordingLanguageString);
            recordingSpeaker.add(recordingSpeakerString);
        }

        if (recordingPublicEditString.equals("")) {
            MetaDataFragment dataFragment =
                    (MetaDataFragment) fragmentManager.findFragmentByTag(MetaDataFragment.TAG);
            dataFragment.focusAt(MetaDataFieldFragment.publicEdit);
            return;
        }

        recordingTitle = recordingTitle.equals("") ? Recording.defaultRecordingTitle : recordingTitle;

        /* copy the file from cache to internal storage */
        int numDup;
        String recordingFileName;
        if (recordingTitle.matches(Recording.defaultRecordingTitle + " [0-9]+$")) {
            numDup = checkDuplication(Recording.defaultRecordingTitle);
            recordingFileName = numDup == 0 ?
                    Recording.defaultRecordingTitle :
                    Recording.defaultRecordingTitle + " " + numDup;
        } else {
            numDup = checkDuplication(recordingTitle);
            recordingFileName =
                    numDup == 0 ? recordingTitle : recordingTitle + " " + numDup;
        }

        File from = new File(audioFileName);
        File to = new File(audioInternalFileDir, recordingFileName + Recording.defaultAudioFormat);
        audioFileName = to.getAbsolutePath();
        boolean success = from.renameTo(to);

        /* build the recording class from data collected */
        if (success) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(audioFileName);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(durationStr);
            recording = new Recording();
            recording.setRecordingID();
            recording.setDuration(duration);
            recording.setTitle(recordingTitle);
            recording.setDate(recordingDateString);
            recording.setDescription(recordingDescription);
            recording.setLanguage(recordingLanguages);
            recording.setSpeakers(recordingSpeaker);
            recording.setLatitude(latitude);
            recording.setLongitude(longitude);
            recording.setLocation(location);
            // TODO: set the uploader in the future
            recording.setUploader(new User("franktest@gmail.com", "passwd", "frankie"));
            recording.setFilePath(audioFileName);
            RecordingDataSource dataSource = new RecordingDataSource(RecordingActivity.this);
            dataSource.open();
            dataSource.insertRecording(recording);
            dataSource.close();

            Intent intent = new Intent(RecordingActivity.this, MyRecordingsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Callback by {@link MetaDataFieldFragment}
     * Called whenever an edittext lose focus (i.e. a possible new
     * user input).
     *
     * Saves the user input as Strings for future reference.
     *
     * @param inputString user input from the EditText
     * @param which pointing to the question being answered
     */
    @Override
    public void onUserInput(String inputString, int which) {
        Log.d("test", "question " + which + " :" + inputString);
        switch (which) {
            case MetaDataFieldFragment.name:
                recordingTitle = inputString;
                break;
            case MetaDataFieldFragment.languages:
                recordingLanguageString = inputString;
                break;
            case MetaDataFieldFragment.speakers:
                recordingSpeakerString = inputString;
                break;
            case MetaDataFieldFragment.description:
                recordingDescription = inputString;
                break;
            case MetaDataFieldFragment.publicEdit:
                recordingPublicEditString = inputString;
                break;
            default:
                break;
        }
    }

    /**
     * Callback by {@link MetaDataFieldFragment}.
     * Focus the current page to the next page.
     * If the current page is at the end, call {@link #onFinishClick()}.
     *
     * @param currentItem current page number
     */
    @Override
    public void moveToNext(int currentItem) {
        if (currentItem == MetaDataPagerAdaptor.NUM_Q - 1) {
            // user on the last question, ready to finish
            onFinishClick();
        } else {
            MetaDataFragment dataFragment =
                    (MetaDataFragment) fragmentManager.findFragmentByTag(MetaDataFragment.TAG);
            dataFragment.focusAt(currentItem + 1);
        }
    }
}