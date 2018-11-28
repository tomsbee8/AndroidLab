package cn.blinkdagger.androidLab.ui.activity;


import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.utils.DensityUtil;
import cn.blinkdagger.androidLab.widget.ConfirmDialog;
import cn.blinkdagger.androidLab.widget.HorizontalStepView;

/**
 * @Author ls
 * @Date 2018/11/14
 * @Description
 * @Version
 */
public class DialogExampleActivity extends BaseActivity implements View.OnClickListener{
    private TextView confirmTV;
    private TextView confirmCancelTV;
    private TextView confirmInputTV;
    private TextView confirmDismissTV;

    @Override
    protected int getLayout() {
        return R.layout.activity_dialog_example;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        confirmTV = findViewById(R.id.confirm_tv);
        confirmCancelTV = findViewById(R.id.confirm_cancel_tv);
        confirmInputTV = findViewById(R.id.confirm_input_tv);
        confirmDismissTV = findViewById(R.id.confirm_dismiss_tv);
    }

    @Override
    protected void initData() {
        setToolbarTitle("DialogExample");
        confirmTV.setOnClickListener(this);
        confirmCancelTV.setOnClickListener(this);
        confirmInputTV.setOnClickListener(this);
        confirmDismissTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_tv:
                new ConfirmDialog.Builder()
                        .setMessage("你的密码已经重置成功，请重新使用兴迷吗登录！")
                        .setConfirmText("确认")
                        .setTitle("提示")
                        .setCardRadius(DensityUtil.dp2px(this,2))
                        .build()
                        .show(getSupportFragmentManager());
                break;
            case R.id.confirm_cancel_tv:
                break;
            case R.id.confirm_input_tv:
                break;
            case R.id.confirm_dismiss_tv:
                break;
        }
    }
}
