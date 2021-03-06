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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;

import georgia.languagelandscape.R;

//import android.support.v4.app.Fragment;


/**
 * One of the generic subclass of {@link Fragment} uesed to display information about the app
 * Display the index page for the links to each sub-help page
 */
public class HelpFragment extends Fragment implements MyProjectsFragment.OnFragmentInteractionListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentManager fm = null;
    private FragmentTransaction ft = null;

    private OnFragmentInteractionListener mListener;

    public HelpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        String frameVideo = "<html><body> <iframe width=\"320\" height=\"315\" src=\"https://www.youtube.com/embed/p4QOfawX-yU\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        View rootview = inflater.inflate(R.layout.fragment_help, container, false);

        WebView display = (WebView) rootview.findViewById(R.id.webView);
        display.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = display.getSettings();
        webSettings.setJavaScriptEnabled(true);
        display.loadData(frameVideo, "text/html", "utf-8");


        TextView tv1= (TextView) rootview.findViewById(R.id.textView1);
        TextView tv2= (TextView) rootview.findViewById(R.id.textView2);
        TextView tv3= (TextView) rootview.findViewById(R.id.textView3);
        TextView tv4= (TextView) rootview.findViewById(R.id.textView4);
        TextView tv5= (TextView) rootview.findViewById(R.id.textView5);
        TextView tv6= (TextView) rootview.findViewById(R.id.textView6);
        TextView tv7= (TextView) rootview.findViewById(R.id.textView7);
        TextView tv8= (TextView) rootview.findViewById(R.id.textView8);
        TextView tv9= (TextView) rootview.findViewById(R.id.textView9);
        tv1.setFocusableInTouchMode(false);
        tv1.setOnClickListener(this);
        tv2.setFocusableInTouchMode(false);
        tv2.setOnClickListener(this);
        tv3.setFocusableInTouchMode(false);
        tv3.setOnClickListener(this);
        tv4.setFocusableInTouchMode(false);
        tv4.setOnClickListener(this);
        tv5.setFocusableInTouchMode(false);
        tv5.setOnClickListener(this);
        tv6.setFocusableInTouchMode(false);
        tv6.setOnClickListener(this);
        tv7.setFocusableInTouchMode(false);
        tv7.setOnClickListener(this);
        tv8.setFocusableInTouchMode(false);
        tv8.setOnClickListener(this);
        tv9.setFocusableInTouchMode(false);
        tv9.setOnClickListener(this);

        return rootview;

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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.textView1:
                HelpMakeRecordingFragment myProjectsFragment= new HelpMakeRecordingFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                Log.d("dcf","da");
                ft.replace(R.id.content_replace, myProjectsFragment);
                ft.commit();
                break;
            case R.id.textView2:
                HelpEditUploadRecordingFragment myProjectsFragment2= new HelpEditUploadRecordingFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                Log.d("dcf","da");
                ft.replace(R.id.content_replace, myProjectsFragment2);
                ft.commit();
                break;
            case R.id.textView3:
                HelpAddRecordingFragment myProjectsFragment3= new HelpAddRecordingFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                Log.d("dcf","da");
                ft.replace(R.id.content_replace, myProjectsFragment3);
                ft.commit();
                break;
            case R.id.textView4:
                HelpAddSpeakerFragment myProjectsFragment4= new HelpAddSpeakerFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                Log.d("dcf","da");
                ft.replace(R.id.content_replace, myProjectsFragment4);
                ft.commit();
                break;
            case R.id.textView5:
                HelpAddLanguageFragment myProjectsFragment5= new HelpAddLanguageFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                Log.d("dcf","da");
                ft.replace(R.id.content_replace, myProjectsFragment5);
                ft.commit();
                break;
            case R.id.textView6:
                HelpAddProjectFragment myProjectsFragment6= new HelpAddProjectFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                Log.d("dcf","da");
                ft.replace(R.id.content_replace, myProjectsFragment6);
                ft.commit();
                break;
            case R.id.textView7:
                HelpAdvancedSearchFragment myProjectsFragment7= new HelpAdvancedSearchFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                Log.d("dcf","da");
                ft.replace(R.id.content_replace, myProjectsFragment7);
                ft.commit();
                break;
            case R.id.textView8:
                HelpLanguagePageFragment myProjectsFragment8= new HelpLanguagePageFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                Log.d("dcf","da");
                ft.replace(R.id.content_replace, myProjectsFragment8);
                ft.commit();
                break;
            case R.id.textView9:
                HelpRegistrationPassFragment myProjectsFragment9= new HelpRegistrationPassFragment();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                Log.d("dcf","da");
                ft.replace(R.id.content_replace, myProjectsFragment9);
                ft.commit();
                break;
                /*case R.id.item2:
                    //Do what you want for create_button
                    break;
                default:
                    break;*/
        }


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }


}