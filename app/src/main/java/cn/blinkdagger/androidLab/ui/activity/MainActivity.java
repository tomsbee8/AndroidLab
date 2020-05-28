package cn.blinkdagger.androidLab.ui.activity;

import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.blinkdagger.android.qrcode.QRCaptureActivity;
import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.entity.MainItem;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.ui.adapter.MainAdapter;

public class MainActivity extends BaseActivity implements MainAdapter.OnMainItemClickListener{

    private RecyclerView mainRV;
    private List<MainItem> mainItemList;
    private MainAdapter mainAdapter;

    @Override
    public int getContentLayout() {
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
        setNavigationIconInvisible();
        setToolbarTitle("AndroidLab");

        mainItemList =new ArrayList<MainItem>(){{
            add(new MainItem(1,"SelectBarView"));
            add(new MainItem(1,"TipView"));
            add(new MainItem(1,"TipViewMenu"));
            add(new MainItem(1,"CustomPopup"));
            add(new MainItem(1,"CollaspingTab"));
            add(new MainItem(1,"DataBinding"));
            add(new MainItem(1,"DynamicBlur"));
            add(new MainItem(1,"CommonDialog"));
            add(new MainItem(1,"HorizontalStepView"));
            add(new MainItem(1,"QRCodeScan"));
            add(new MainItem(1,"SkinMode"));
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
                startActivity(new Intent(this,DialogExampleActivity.class));
                break;
            case 8:
                startActivity(new Intent(this,StepViewActivity.class));
                break;
            case 9:
                startActivity(new Intent(this, QRCaptureActivity.class));
                break;
            case 10:
                startActivity(new Intent(this, SkinModeActivity.class));
                break;
        }
    }
}
