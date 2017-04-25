package georgia.languagelandscape;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.database.RecordingDataSource;
import georgia.languagelandscape.fragments.DeleteDialogFragment;
import georgia.languagelandscape.fragments.RenameDialogFragment;
import georgia.languagelandscape.util.RecordingAdaptor;

/**
 * Handles callback form {@link RecordingAdaptor} when the popup window for
 * detailed information about a recording is shown.
 */
public class MyRecordingsActivity extends BaseActivity
        implements RenameDialogFragment.RenameDialogListener,
        DeleteDialogFragment.DeleteDialogListener{

    public static boolean recordingPlaying = false;
    private FrameLayout rootLayout = null;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecordings_nav_drawer);
        super.onDrawerCreated();
    }

    /**
     * Rename the recording and its recording file at internal storage. Update the database
     * with the renamed recording. Doing so, update the list view with this new name.
     *
     * The new name is guaranteed to be a valid string for a recording, but the method
     * still need to call {@link #checkDuplication(String)} to check for duplicated names
     * in the internal storage to avoid naming conflict.
     *
     * The method get the adaptor for the RecyclerView and use it to update view.
     *
     * @param recording recording to be renamed
     * @param toName the new name
     * @param adaptorPosition pointing to the view in RecyclerView showing
     *                        this recording information
     */
    @Override
    public void onRenameClick(Recording recording, String toName, int adaptorPosition) {

        if (toName.equals(recording.getTitle())) {
            return;
        }

        String audioInternalFilePath = null;
        File audioInternalFileDir = null;
        try {
            audioInternalFilePath = getFilesDir().getCanonicalPath() + "/recordings";
            audioInternalFileDir = new File(audioInternalFilePath);
            String[] recordings = audioInternalFileDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(Recording.defaultAudioFormat);
                }
            });
            for (String record : recordings) {
                Log.i("dialog", "existing recording:" + record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numDup;
        String recordingFileName;
        File from = new File(recording.getFilePath());
        numDup = checkDuplication(toName);
        recordingFileName =
                numDup == 0 ? toName : toName + " " + numDup;

        File to = new File(audioInternalFileDir, recordingFileName + Recording.defaultAudioFormat);
        boolean success = from.renameTo(to);
        Log.i("dialog", "renamed: " + String.valueOf(success));
        if (success) {
            recording.setTitle(toName);
            recording.setFilePath(to.getAbsolutePath());

            recyclerView = (RecyclerView) findViewById(R.id.myrecording_recycler);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            TextView title = (TextView) layoutManager
                    .findViewByPosition(adaptorPosition)
                    .findViewById(R.id.recordingList_title);
            title.setText(toName);

            /* change the database */
            RecordingDataSource dataSource = new RecordingDataSource(this);
            dataSource.open();
            dataSource.deleteRecording(recording.getRecordingID());
            dataSource.insertRecording(recording);
            dataSource.close();
        }
    }

    /**
     * Helper for checking recording name duplication in internal storage.
     *
     * @param recordingTitle the recording name to be checked
     * @return number of duplicated file name
     */
    private int checkDuplication(final String recordingTitle) {
        String audioInternalFilePath = null;
        File audioInternalFileDir = null;
        try {
            audioInternalFilePath = getFilesDir().getCanonicalPath() + "/recordings";
            audioInternalFileDir = new File(audioInternalFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] recordings = audioInternalFileDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(Recording.defaultAudioFormat)
                        && name.startsWith(recordingTitle);
            }
        });

        return recordings.length;
    }

    /**
     * Delete the recording from database and the internal storage. In doing so,
     * remove the view for this recording from the list view as well.
     *
     * @param id id of the recording to be deleted. This will be used in RecordingDatasource
     * @param title title of the recording. Will be used to find file path to be deleted
     * @param index index of the recording in the list
     */
    @Override
    public void onDeleteClick(String id, String title, final int index) {

        String audioInternalFilePath = null;
        File audioInternalFileDir = null;
        try {
            audioInternalFilePath = getFilesDir().getCanonicalPath() + "/recordings";
            audioInternalFileDir = new File(audioInternalFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File fileToDelete = new File(audioInternalFileDir, title + Recording.defaultAudioFormat);
        boolean deleted = fileToDelete.delete();

        if (deleted) {
            RecordingDataSource dataSource = new RecordingDataSource(this);
            dataSource.open();
            dataSource.deleteRecording(id);
            dataSource.close();

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myrecording_recycler);
            RecordingAdaptor adapter = (RecordingAdaptor) recyclerView.getAdapter();
            int recordingCount = adapter.getItemCount();
            adapter.removeRecording(index);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            for (int position = index + 1; position < recordingCount; position++) {
                TextView count = (TextView) layoutManager
                        .findViewByPosition(position)
                        .findViewById(R.id.recordingList_count);
                count.setText(String.format("%s - ", String.valueOf(position)));
            }
        }
    }
}
