package georgia.languagelandscape.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.data.User;
import georgia.languagelandscape.database.RecordingDataSource;
import georgia.languagelandscape.util.RecordingAdaptor;

public class RecordingsListFragment extends Fragment{

    private List<Recording> recordings = null;
    private Context context = null;
    RecordingDataSource dataSource;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recording_list, container, false);
        ArrayList<String> language = new ArrayList<>();
        language.add("English");
        User user = new User("franktest@gmail.com", "passwd", "frankie");
        ArrayList<String> speaker = new ArrayList<>();
        speaker.add("me");

        recordings = new ArrayList<>();
        recordings.add(new Recording("test 1", 2020, "nmb", 20.0191, -17.374, "London, England",
                language, "20/03/2016", user, speaker));
        recordings.add(new Recording("test 2", 2020, "cnmb", 20.0191, -17.374, "London, England",
                language, "20/03/2016", user, speaker));
        recordings.add(new Recording("test 3", 2020, "ndyd", 20.0191, -17.374, "London, England",
                language, "20/03/2016", user, speaker));
        recordings.add(new Recording("test 4", 2020, "gnm", 20.0191, -17.374, "London, England",
                language, "20/03/2016", user, speaker));
        recordings.add(new Recording("test 5", 2020, "yo", 20.0191, -17.374, "London, England",
                language, "20/03/2016", user, speaker));
        recordings.add(new Recording("test 6", 2020, "cngsb", 20.0191, -17.374, "London, England",
                language, "20/03/2016", user, speaker));
        recordings.add(new Recording("test 7", 2020, "sb", 20.0191, -17.374, "London, England",
                language, "20/03/2016", user, speaker));
        recordings.add(new Recording("test 8", 2020, "dzt", 20.0191, -17.374, "London, England",
                language, "20/03/2016", user, speaker));
        recordings.add(new Recording("test 9", 2020, "mmp", 20.0191, -17.374, "London, England",
                language, "20/03/2016", user, speaker));


//        dataSource = new RecordingDataSource(context);
//        dataSource.open();
//        recordings = dataSource.getAllReordings();
//        dataSource.close();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.myrecording_list);
        recyclerView.setAdapter(new RecordingAdaptor(context, recordings));
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
