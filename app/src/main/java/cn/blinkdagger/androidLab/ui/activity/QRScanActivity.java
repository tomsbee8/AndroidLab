package cn.blinkdagger.androidLab.ui.activity;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.base.BaseActivity;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/3/5
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class QRScanActivity extends BaseActivity{


    @Override
    public int getContentLayout() {
        return R.layout.activity_dynamic_blur;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        setToolbarTitle("DynamicBlur");

    }

}
