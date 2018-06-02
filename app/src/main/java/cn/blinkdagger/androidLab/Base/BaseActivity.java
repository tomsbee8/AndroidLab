package cn.blinkdagger.androidLab.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.Utils.SystemBarUtil;

/**
 * 类描述：基本视图
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RelativeLayout mContentRL;

    private Unbinder mUnBinder;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());

        setToolBar();

        mUnBinder = ButterKnife.bind(this);

        initView();

        setStatusBar();

        initData();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    protected abstract @LayoutRes
    int getLayout();

    protected @LayoutRes
    int getContentLayout() {
        if (useToolbar()) {
            return R.layout.common_toolbar_container;
        } else {
            return getLayout();
        }
    }

    protected void setStatusBar() {
        SystemBarUtil.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), true);
    }

    protected abstract boolean useToolbar();

    protected abstract void initView();

    protected abstract void initData();

    protected void setToolBar() {

        if (useToolbar()) {
            mContentRL = (RelativeLayout) findViewById(R.id.common_content_rl);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(getLayout(), null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mContentRL.addView(contentView, params);

            mToolbar = (Toolbar) findViewById(R.id.common_tb);
            setSupportActionBar(mToolbar);
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
            }

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationIconClick();
                }
            });
        }

    }

    protected void setToolbarTitle(CharSequence charSequence) {
        if (mToolbar != null) {
            TextView titleTV = mToolbar.findViewById(R.id.common_center_tv);
            titleTV.setText(charSequence);
        }
    }

    protected void setNavigationIconInvisiable() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    protected void onNavigationIconClick() {
        finish();
    }

}
