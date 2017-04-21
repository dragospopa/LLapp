package georgia.languagelandscape.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Projects;

public class NewProjectFragment extends Fragment implements MyProjectsFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout container;

    private FragmentManager fm = null;
    private FragmentTransaction ft = null;

    EditText full_name;
    EditText short_name;
    EditText description;
    EditText users;
    EditText languages;
    String tv_project_full_name;
    String tv_project_short_name;
    String tv_project_description;
    String tv_project_users;
    String tv_project_languages;
    String string;
    final Projects projects = new Projects();

    private OnFragmentInteractionListener mListener;
    private Context context;

    public NewProjectFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewProjectFragment newInstance(String param1, String param2) {
        NewProjectFragment fragment = new NewProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Button addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

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
                addItem(string);
                MyProjectsFragment myProjectsFragment = new MyProjectsFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.content_replace, myProjectsFragment);
                ft.commit();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void addItem(String name) {
        tv_project_full_name = String.valueOf(full_name.getText());
        tv_project_short_name = String.valueOf(short_name.getText());
        tv_project_description = String.valueOf(description.getText());
        tv_project_users = String.valueOf(users.getText());
        tv_project_languages = String.valueOf(languages.getText());
        string = "Full name: " + tv_project_full_name + "\n" + "Short name: " + tv_project_short_name + "\n" + "Description: " + tv_project_description + "\n" + "Users: " + tv_project_users + "\n" + "Languages: " + tv_project_languages;
        projects.addItem(string);
    }

}
