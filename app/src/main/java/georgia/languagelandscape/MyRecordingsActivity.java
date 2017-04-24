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

    @Override
    public void onRenameClick(Recording recording, String toName, int adaptorPosition) {
        Log.i("dialog", "changing recording: " + recording.getTitle());
        Log.i("dialog", recording.getFilePath());

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
