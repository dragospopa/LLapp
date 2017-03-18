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
import georgia.languagelandscape.util.RecordingAdaptor;

public class RecordingsListFragment extends Fragment{

    private List<Recording> recordings = null;
    private Context context = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recording_list, container, false);
        // TODO: get the recording from local database;

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.myrecording_list);
        recyclerView.setAdapter(new RecordingAdaptor(context, recordings));
        return view;
    }
}
