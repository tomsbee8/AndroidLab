package cn.blinkdagger.androidLab.ui.activity;

import android.widget.Toast;

import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.widget.SelectBarView;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/6/1
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class SelectBarViewActivity extends BaseActivity {

    private SelectBarView barView;

    @Override
    protected int getLayout() {
        return R.layout.activity_selectbar_view;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        barView = findViewById(R.id.select_bar_view);
    }

    @Override
    protected void initData() {
        setToolbarTitle("SelectBarView");
        barView.setItemGroup("群聊", "评论", "通知");
        barView.setOnItemCheckedChangeListener(new SelectBarView.OnItemCheckedChangeListener() {
            @Override
            public void onItemClick(int index) {
                if (index == 0) {
                    Toast.makeText(SelectBarViewActivity.this,"群聊",Toast.LENGTH_LONG).show();
                }else if(index == 1){
                    Toast.makeText(SelectBarViewActivity.this,"评论",Toast.LENGTH_LONG).show();
                }else if(index == 2){
                    Toast.makeText(SelectBarViewActivity.this,"通知",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
