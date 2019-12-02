package cn.blinkdagger.androidLab.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.AnimatorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 类描述：通用PopupWindow
 * 创建人：ls
 * 创建时间：2017/12/7
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class CustomPopupWindow extends PopupWindow {

    private Activity activity;
    private float onShowBgAlpha;//显示的时候背景的透明度
    private boolean onShowChangeBg;//显示的时候背景透明度是否变化
    private boolean onDissmisChangeBg;//消失的时候背景透明度是否变化


    private CustomPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setOnShowBgAlpha(@FloatRange(from = 0f, to = 1.0f) float onShowBgAlpha) {
        this.onShowBgAlpha = onShowBgAlpha;
    }

    public void setOnShowChangeBg(boolean onShowChangeBg) {
        this.onShowChangeBg = onShowChangeBg;
    }

    public void setOnDissmisChangeBg(boolean onDissmisChangeBg) {
        this.onDissmisChangeBg = onDissmisChangeBg;
    }

    public void showChangeBg() {
        if (onShowChangeBg) {
            if (activity != null) {
                setBackgroundAlpha(activity, onShowBgAlpha);
            }
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        showChangeBg();
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        showChangeBg();
    }

    @Override
    public void dismiss() {
        if (onDissmisChangeBg) {
            if (activity != null) {
                setBackgroundAlpha(activity, 1.0f);
            }
        }
        super.dismiss();
    }

    public static class Builder {

        private Activity activity;
        private View contentView;// 内容视图
        private int width = ViewGroup.LayoutParams.WRAP_CONTENT;//宽度
        private int height = ViewGroup.LayoutParams.WRAP_CONTENT;//高度
        private @StyleRes int anmationStyle;// 动画
        private boolean focusable;//是否可以获取焦点
        private boolean outsideTouchable;//外部是否可以点击
        private boolean onShowChangeBg = true; //显示的时候背景透明度是否变透明【默认变化】
        private boolean onDissmisChangeBg = true;//消失的时候背景透明度是否还原【默认还原】
        private float onShowBgAlpha = 0.5f;//显示的时候背景的透明度
        private boolean onKeyBackDissmiss = false;//是否拦截返回键消失【默认不拦截】
        private OnDismissListener onDismissListener;// 消失监听事件

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setContentView(@LayoutRes int contentViewId) {
            this.contentView = activity.getLayoutInflater().inflate(contentViewId, null);
            return this;
        }

        public Builder setContentView(View contentView, int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setContentView(@LayoutRes int contentViewId, int width, int height) {
            this.contentView = activity.getLayoutInflater().inflate(contentViewId, null);
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setAnmationStyle(@AnimatorRes int anmationStyle) {
            this.anmationStyle = anmationStyle;
            return this;
        }

        public Builder setFocusable(boolean focusable) {
            this.focusable = focusable;
            return this;
        }

        public Builder setOutsideTouchable(boolean outsideTouchable) {
            this.outsideTouchable = outsideTouchable;
            return this;
        }

        public Builder setOnShowChangeBg(boolean onShowChangeBg) {
            this.onShowChangeBg = onShowChangeBg;
            this.focusable = true;
            return this;
        }

        public Builder setOnDissmisChangeBg(boolean onDissmisChangeBg) {
            this.onDissmisChangeBg = onDissmisChangeBg;
            return this;
        }

        public Builder setOnShowBgAlpha(@FloatRange(from = 0f, to = 255) float onShowBgAlpha) {
            this.onShowBgAlpha = onShowBgAlpha;
            return this;
        }

        public void setOnDismissListener(OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
        }

        /**
         * 设置点击外部本身是否消失
         *
         * @param onOutsideTouchDissmiss
         */
        public Builder setOnOutsideTouchDissmiss(boolean onOutsideTouchDissmiss) {
            if (onOutsideTouchDissmiss) {
                this.outsideTouchable = true;
                this.focusable = true;
            } else {
                this.outsideTouchable = false;
                this.focusable = false;
            }
            return this;
        }

        /**
         * 设置点击返回键本身是否消失
         *
         * @return
         */
        public Builder setOnKeyBackDissmiss(boolean onKeyBackDissmiss) {
            this.onKeyBackDissmiss = onKeyBackDissmiss;
            return this;
        }

        public CustomPopupWindow build() {
            if (contentView == null) {
                throw new NullPointerException("contentView con't be null ");
            }
            final CustomPopupWindow popupWindow = new CustomPopupWindow(contentView, width, height);
            popupWindow.setActivity(this.activity);
            popupWindow.setOutsideTouchable(this.outsideTouchable);
            popupWindow.setFocusable(this.focusable);
            popupWindow.setOnDissmisChangeBg(this.onDissmisChangeBg);
            popupWindow.setOnShowChangeBg(this.onShowChangeBg);
            popupWindow.setOnShowBgAlpha(this.onShowBgAlpha);
            popupWindow.setOnDismissListener(this.onDismissListener);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            if (onKeyBackDissmiss) {
                this.contentView.setFocusable(true); // 这个很重要
                this.contentView.setFocusableInTouchMode(true);
                this.contentView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            popupWindow.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
            }

            return popupWindow;
        }
    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    private void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);

    }
}
