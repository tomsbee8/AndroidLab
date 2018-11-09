package cn.blinkdagger.androidLab.UI.activity;

import java.util.ArrayList;

import cn.blinkdagger.androidLab.Base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.Widget.HorizontalStepView;

/**
 * @Author ls
 * @Date 2018/11/9
 * @Description
 * @Version
 */
public class StepViewActivity extends BaseActivity{

    private HorizontalStepView horizontalStepView;

    @Override
    protected int getLayout() {
        return R.layout.activity_step_view;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        horizontalStepView =findViewById(R.id.step_view);
        horizontalStepView.setStepTextList(new ArrayList<CharSequence>(){
            {
                add("V1");
                add("V2");
                add("V3");
                add("V4");
                add("V5");
            }
        });
    }

    @Override
    protected void initData() {

    }
}
