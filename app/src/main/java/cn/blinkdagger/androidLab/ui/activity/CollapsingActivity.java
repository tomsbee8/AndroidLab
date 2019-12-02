package cn.blinkdagger.androidLab.ui.activity;

import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.ui.fragment.MaximListFragment;
import cn.blinkdagger.androidLab.ui.adapter.BaseFragmentAdapter;

public class CollapsingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    String[] mTitles = new String[]{"主页", "微博", "相册"};
    Fragment mainFragment, weiboFragment, galleryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.coll_toolbar_layout);
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);

        setupToolBar();
        setupViewPager();

    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
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
