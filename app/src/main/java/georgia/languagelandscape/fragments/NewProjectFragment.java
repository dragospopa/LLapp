/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Project;
import georgia.languagelandscape.data.User;
import georgia.languagelandscape.database.ProjectDataSource;

/**
 * The data entry Fragment for a new Project.
 *
 * It has edittext for data entry of a Project object,
 * and by checking the mandatory fields and validity of the inputs it creates
 * a new Project object and insert it into the database.
 *
 * The new Project created will not be associated with any recording,
 * but user can choose to add any recording into a Project.
 *
 * Since the Project created will be inserted into database directly,
 * this fragment class is handling all the user input on its own
 * without the need for a listener.
 */
public class NewProjectFragment extends Fragment implements MyProjectsFragment.OnFragmentInteractionListener {

    private FragmentManager fm = null;
    private FragmentTransaction ft = null;
    private EditText full_name;
    private EditText short_name;
    private EditText description;
    private EditText users;
    private EditText languages;
    private Button addButton;

    private OnFragmentInteractionListener mListener;
    private Context context;
    private Project project = null;

    public NewProjectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_project, container, false);

        full_name = (EditText) view.findViewById(R.id.editText_full_name);
        short_name = (EditText) view.findViewById(R.id.editText_short_name);
        description = (EditText) view.findViewById(R.id.editText_description);
        users = (EditText) view.findViewById(R.id.editText_users);
        languages = (EditText) view.findViewById(R.id.editText_languages);

        full_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        return short_name.requestFocus();
                    default:
                        return false;
                }
            }
        });

        short_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        return users.requestFocus();
                    default:
                        return false;
                }
            }
        });

        users.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        return languages.requestFocus();
                    default:
                        return false;
                }
            }
        });

        languages.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        return description.requestFocus();
                    default:
                        return false;
                }
            }
        });

        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        description.clearFocus();
                        InputMethodManager imm = (InputMethodManager)
                                context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
                    default:
                        return false;
                }
            }
        });

        addButton = (Button) view.findViewById(R.id.button_add_project);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // check mandatory fields: full name and short name
                String fullName = full_name.getText().toString();
                if (fullName.equals("")) {
                    full_name.requestFocus();
                    View view = ((FragmentActivity) context).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)
                                context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(view, 0);
                    }
                    return;
                }

                // TODO: check for replicated short names
                String shortName = short_name.getText().toString();
                if (shortName.equals("")) {
                    short_name.requestFocus();
                    View view = ((FragmentActivity) context).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)
                                context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(view, 0);
                    }
                    return;
                }

                // TODO: search for users in the future
                String userName = users.getText().toString();
                List<User> contributor = new ArrayList<>();

                // TODO: split the languages into separate individuals
                String languageInput = languages.getText().toString();
                List<String> projectLanguage = new ArrayList<>();
                projectLanguage.add(languageInput);
                String descriptionInput = description.getText().toString();

                project = new Project();
                project.setFullName(fullName);
                project.setShortName(shortName);
                project.setDescription(descriptionInput);
                project.setOwner(new User(null, null, "Frankie")); // dummy user
                project.setContributors(contributor);
                project.setLanguages(projectLanguage);
                project.setDescription(descriptionInput);
                project.setRecordings(null);

                // insert into internal database
                ProjectDataSource dataSource = new ProjectDataSource(context);
                dataSource.open();
                dataSource.createProject(project);
                dataSource.close();

                MyProjectsFragment myProjectsFragment = new MyProjectsFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.content_replace, myProjectsFragment);
                ft.commit();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
