package cn.blinkdagger.androidLab.UI.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.Utils.SystemBarUtil;
import cn.blinkdagger.androidLab.widget.CustomPopupWindow;
import cn.blinkdagger.androidLab.widget.SelectBarView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SelectBarView barView;
    private TextView clickTv;
    private TextView scrollTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barView = findViewById(R.id.main_bar);
        clickTv = findViewById(R.id.click_tv);
        clickTv = findViewById(R.id.click_tv);
        scrollTv = findViewById(R.id.scroll_tv);

        barView.setItemGroup("群聊", "品论", "通知");
        barView.setOnItemCheckedChangeListener(new SelectBarView.OnItemCheckedChangeListener() {
            @Override
            public void onItemClick(int index) {
                if (index == 1) {
                    Intent intent = new Intent(MainActivity.this, cn.blinkdagger.androidLab.UI.Activity.ImmersiveModeActivity.class);
                    startActivity(intent);
                }else if(index == 2){
                    Intent intent = new Intent(MainActivity.this, cn.blinkdagger.androidLab.UI.Activity.DrawerActivity.class);
                    startActivity(intent);
                }
            }
        });
        clickTv.setOnClickListener(this);
        scrollTv.setOnClickListener(this);

        SystemBarUtil.setTranslucentStatusBar(this,true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.click_tv:
                new CustomPopupWindow.Builder(this)
                        .setContentView(R.layout.popup_test_layout)
                        .setFocusable(true)
                        .setOnKeyBackDissmiss(true)
                        .setOnOutsideTouchDissmiss(false)
                        .setOnShowBgAlpha(0.5f)
                        .setOnShowChangeBg(false)
                        .build()
                        .showAsDropDown(clickTv);
                break;
            case R.id.scroll_tv:
                Intent intent = new Intent(this, cn.blinkdagger.androidLab.UI.Activity.CollapsingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
