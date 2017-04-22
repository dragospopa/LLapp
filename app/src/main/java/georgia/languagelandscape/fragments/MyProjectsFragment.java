package georgia.languagelandscape.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Project;
import georgia.languagelandscape.database.ProjectDataSource;
import georgia.languagelandscape.util.ProjectAdaptor;

public class MyProjectsFragment extends Fragment {

    private Context context;
    private ProjectDataSource datasource;

    public MyProjectsFragment() {
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        datasource = new ProjectDataSource(context);
        datasource.open();
        List<Project> projects = datasource.getOwnedProjects();
        datasource.close();

        View view = inflater.inflate(R.layout.fragment_my_projects, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.myprojects_recycler);
        recyclerView.setAdapter(new ProjectAdaptor(context, projects));
        datasource = null;
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
