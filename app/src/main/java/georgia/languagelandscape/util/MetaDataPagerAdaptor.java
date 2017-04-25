package georgia.languagelandscape.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import georgia.languagelandscape.fragments.MetaDataFieldFragment;

public class MetaDataPagerAdaptor extends FragmentStatePagerAdapter {

    private static final int NUM_Q = 10;
    private SparseArray<Fragment> registeredFragment = new SparseArray<>();

    public MetaDataPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MetaDataFieldFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return NUM_Q;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragment.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragment.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragment.get(position);
    }
}
