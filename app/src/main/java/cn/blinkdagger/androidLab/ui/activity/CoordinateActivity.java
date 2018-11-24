package cn.blinkdagger.androidLab.ui.activity;


import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;


import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.utils.SystemBarUtil;

/**
 * @Author ls
 * @Date 2018/11/14
 * @Description
 * @Version
 */
public class CoordinateActivity  extends BaseActivity{
    private Toolbar mToolbar;

    @Override
    protected int getLayout() {
        return R.layout.activity_coordinate;
    }

    @Override
    protected boolean useToolbar() {
        return false;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mToolbar = findViewById(R.id.toolbar_grade);
        setSupportActionBar(mToolbar);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setStatusBar() {
        SystemBarUtil.setTransparent(this);
    }
}
