package cn.blinkdagger.androidLab.UI.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/1/24
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private Fragment[] fragments;

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public BaseFragmentAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
