/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import georgia.languagelandscape.fragments.MetaDataFieldFragment;

/**
 * The simple {@link FragmentStatePagerAdapter} subclass.
 *
 * The {@link #getRegisteredFragment(int)} is a small hack
 * that help us to get the registered fragment at any given page,
 * without accidentally referring to a null fragment that has been
 * destroyed by the pager.
 * Hence we can reference to any views inside that fragment.
 *
 * function {@link #getRegisteredFragment(int)} is from
 * <a http://stackoverflow.com/questions/8785221/retrieve-a-fragment-from-a-viewpager/15261142#15261142">Stack Overflow</a>
 */
public class MetaDataPagerAdaptor extends FragmentStatePagerAdapter {

    public static final int NUM_Q = 10;
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
