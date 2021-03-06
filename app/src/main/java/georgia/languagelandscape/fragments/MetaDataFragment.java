/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import georgia.languagelandscape.R;
import georgia.languagelandscape.util.MetaDataPagerAdaptor;

/**
 * The Fragment that contains the ViewPager
 * for displaying meta-data questions of a new recording one at each page
 *
 * Activities that contains this fragment must implements
 * {@link MetaDataFragmentListener} interface to interact with finish button click
 */
public class MetaDataFragment extends Fragment {

    private Context context;
    private ViewPager pager;
    private Button finishButton;
    private MetaDataPagerAdaptor adaptor;

    public static final String TAG = "MetaDataFragment";

    public MetaDataFragment() {
    }

    public interface MetaDataFragmentListener {
        public void clearFocus();

        public void onFinishClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meta_viewpager, container, false);

        pager = (ViewPager) view.findViewById(R.id.pager);
        finishButton = (Button) view.findViewById(R.id.finish);

        adaptor = new MetaDataPagerAdaptor(getChildFragmentManager());
        pager.setAdapter(adaptor);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // clear the edittext focus so that the onFocusChange callback can be triggered
                ((MetaDataFragmentListener) context).clearFocus();
                ((MetaDataFragmentListener) context).onFinishClick();
            }
        });

        return view;
    }

    /**
     * Called by the parent activity to move the current page to specified one;
     * mostly because some mandatory information of a recording is not entered by
     * the user.
     *
     * @param which the position of the page to go to.
     */
    public void focusAt(int which) {
        pager.setCurrentItem(which);
        MetaDataFieldFragment fieldFragment =
                (MetaDataFieldFragment) adaptor.getRegisteredFragment(which);
        fieldFragment.focusField();
    }
}
