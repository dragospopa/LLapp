package georgia.languagelandscape.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.database.RecordingDataSource;
import georgia.languagelandscape.util.RecordingAdaptor;

public class RecordingsListFragment extends Fragment {

    public static final String FRAG_TAG = "recordingListFragment";
    private Context context = null;
    RecordingDataSource dataSource = null;

    public RecordingsListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataSource = new RecordingDataSource(context);
        dataSource.open();
        List<Recording> recordingFromDB = dataSource.getAllRecordings();
        dataSource.close();

        int layoutId = recordingFromDB.size() == 0 ?
                R.layout.fragment_empty_recordings_list : R.layout.fragment_recording_list;
        View view = inflater.inflate(layoutId, container, false);

        if (layoutId != R.layout.fragment_empty_recordings_list) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.myrecording_recycler);
            recyclerView.setAdapter(new RecordingAdaptor(context, recordingFromDB));
        }
        dataSource = null;
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dataSource != null) {
            dataSource.open();
        }
    }
}
