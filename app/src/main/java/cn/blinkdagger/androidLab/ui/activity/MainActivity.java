package cn.blinkdagger.androidLab.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.entity.MainItem;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.ui.adapter.MainAdapter;

public class MainActivity extends BaseActivity implements MainAdapter.OnMainItemClickListener{

    private RecyclerView mainRV;
    private List<MainItem> mainItemList;
    private MainAdapter mainAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        mainRV =findViewById(R.id.main_rv);
    }

    @Override
    protected void initData() {
        setNavigationIconInvisiable();
        setToolbarTitle("AndroidLab");

        mainItemList =new ArrayList<MainItem>(){{
            add(new MainItem(1,"SelectBarView"));
            add(new MainItem(1,"TipView"));
            add(new MainItem(1,"TipViewmenu"));
            add(new MainItem(1,"CustomPopup"));
            add(new MainItem(1,"CollaspingTab"));
            add(new MainItem(1,"DataBinding"));
            add(new MainItem(1,"DynamicBlur"));
            add(new MainItem(1,"HorizontalStepView"));
        }};
        GridLayoutManager gridLayoutManager =new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        mainRV.setLayoutManager(gridLayoutManager);
        mainAdapter =new MainAdapter(this,mainItemList);
        mainAdapter.setOnItemClickListener(this);
        mainRV.setAdapter(mainAdapter);
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case 0:
                startActivity(new Intent(this,SelectBarViewActivity.class));
                break;
            case 1:
                startActivity(new Intent(this,TipViewActivity.class));
                break;
            case 2:
                startActivity(new Intent(this,TipMenuViewActivity.class));
                break;
            case 3:
                startActivity(new Intent(this,CustomPopupWindowActivity.class));
                break;
            case 4:
                startActivity(new Intent(this,CollapsingActivity.class));
                break;
            case 5:
                startActivity(new Intent(this,BindingListActivity.class));
                break;
            case 6:
                startActivity(new Intent(this,DynamicBlurActivity.class));
                break;
            case 7:
                startActivity(new Intent(this,CoordinateActivity.class));
                break;
        }
    }
}
