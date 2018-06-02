package cn.blinkdagger.androidLab.UI.activity;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import cn.blinkdagger.androidLab.Base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.Widget.CustomPopupWindow;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/6/1
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class CustomPopupWindowActivity extends BaseActivity {

    private FloatingActionButton actionButton;

    @Override
    protected int getLayout() {
        return R.layout.activity_custom_popup_window;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        setToolbarTitle("CustomPopupWindow");
        actionButton = findViewById(R.id.fab);
    }

    @Override
    protected void initData() {
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomPopupWindow.Builder(CustomPopupWindowActivity.this)
                        .setContentView(R.layout.popup_test_layout)
                        .setFocusable(true)
                        .setOnKeyBackDissmiss(true)
                        .setOnOutsideTouchDissmiss(false)
                        .setOnShowBgAlpha(0.5f)
                        .setOnShowChangeBg(false)
                        .build()
                        .showAsDropDown(actionButton);
            }
        });
    }
}
