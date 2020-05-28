package cn.blinkdagger.androidLab.ui.activity;

import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.widget.TipView;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/6/1
 * 修改人：
 * 修改时间：
 * 修改备注：扩展工作：横向，三角形圆角
 */

public class TipViewActivity extends BaseActivity {

    private TipView tipView;

    @Override
    public int getContentLayout() {
        return R.layout.activity_tip_view;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        setToolbarTitle("TipView");
        tipView =findViewById(R.id.tip_view);
    }

    @Override
    protected void initData() {

    }
}
