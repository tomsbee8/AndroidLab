package cn.blinkdagger.androidLab.ui.activity;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.ui.adapter.BaseFragmentAdapter;
import cn.blinkdagger.androidLab.ui.fragment.MaximListFragment;

public class CollapsingActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    String[] mTitles = new String[]{"主页", "微博", "相册"};
    Fragment mainFragment, weiboFragment, galleryFragment;


    @Override
    public int getContentLayout() {
        return R.layout.activity_scroll;
    }

    @Override
    protected boolean useToolbar() {
        return false;
    }

    @Override
    protected void initView() {
        collapsingToolbarLayout = findViewById(R.id.coll_toolbar_layout);
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);

        setupToolBar();
        setupViewPager();
    }

    @Override
    protected void initData() {

    }

    private void setupToolBar() {
        setSupportActionBar(getMToolbar());
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setupViewPager() {
        mainFragment = MaximListFragment.newInstance(0);
        weiboFragment = MaximListFragment.newInstance(1);
        galleryFragment = MaximListFragment.newInstance(2);

        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(), new Fragment[]{mainFragment, weiboFragment, galleryFragment}, mTitles);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Toast.makeText(this, mTitles[position], Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
