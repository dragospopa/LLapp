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
                ((MetaDataFragmentListener) context).clearFocus();
                ((MetaDataFragmentListener) context).onFinishClick();
            }
        });

        return view;
    }

    public void focusAt(int which) {
        pager.setCurrentItem(which);
        MetaDataFieldFragment fieldFragment =
                (MetaDataFieldFragment) adaptor.getRegisteredFragment(which);
        fieldFragment.focusField();
    }
}
