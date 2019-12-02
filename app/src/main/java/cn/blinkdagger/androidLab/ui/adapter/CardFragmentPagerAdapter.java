package cn.blinkdagger.androidLab.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ls
 * @Date 2018/11/12
 * @Description
 * @Version
 */

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<CardFragment> mFragments;

    public CardFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            addCardFragment(new CardFragment());
        }
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        mFragments.add(fragment);
    }

}

