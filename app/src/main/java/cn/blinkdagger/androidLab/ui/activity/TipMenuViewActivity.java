package cn.blinkdagger.androidLab.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.widget.TipMenuView;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/6/1
 * 修改人：
 * 修改时间：扩展工作：横向，三角形圆角
 * 修改备注：
 */

public class TipMenuViewActivity extends BaseActivity {

    private PopupWindow mTipMenuWindow;
    private TipMenuView tipMenuView;
    private FloatingActionButton fab;

    @Override
    protected int getLayout() {
        return R.layout.activity_tip_menu_view;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        fab = findViewById(R.id.fab);
        tipMenuView = findViewById(R.id.tip_menu_view);
    }

    @Override
    protected void initData() {
        setToolbarTitle("TipMenuView");
        tipMenuView.setItems("我的搜藏","我的评论","我的浏览");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOperateTipMenu();
            }
        });
    }

    // 显示更多操作按钮
    private void showOperateTipMenu() {
        if (mTipMenuWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.popup_order_operate_tipmenu, null);
            TipMenuView tipMenuView = (TipMenuView) contentView.findViewById(R.id.tip_menu);
            tipMenuView.setItems("我的搜藏","我的评论","我的浏览");
            tipMenuView.setOnItemClickListener(new TipMenuView.OnItemClickListener() {
                @Override
                public void onItemClick(String name, int position) {
                    if (mTipMenuWindow != null && mTipMenuWindow.isShowing()) {
                        mTipMenuWindow.dismiss();
                    }
                }
            });
            mTipMenuWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mTipMenuWindow.setContentView(contentView);
            mTipMenuWindow.setBackgroundDrawable(new BitmapDrawable());
            mTipMenuWindow.setFocusable(false);
            mTipMenuWindow.setOutsideTouchable(false);
        }
        if (!mTipMenuWindow.isShowing()) {
            int[] location = new int[2];
            fab.getLocationOnScreen(location);
            // 计算popupwindow的高度
            mTipMenuWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popHeight = mTipMenuWindow.getContentView().getMeasuredHeight();
            int popWidth = mTipMenuWindow.getContentView().getMeasuredWidth();
            // 显示在正上方
            mTipMenuWindow.showAtLocation(fab, Gravity.NO_GRAVITY,
                    location[0] + (fab.getWidth() - popWidth) / 2, location[1] - popHeight);
        } else {
            mTipMenuWindow.dismiss();
        }
    }
}
