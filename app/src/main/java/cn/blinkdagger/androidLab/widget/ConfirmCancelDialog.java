package cn.blinkdagger.androidLab.widget;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.blinkdagger.androidLab.R;

/**
 * @Author ls
 * @Date 2018/11/24
 * @Description
 * @Version
 */
public class ConfirmCancelDialog extends DialogFragment {

    /**
     * 只有一个确认按钮的确认弹出框
     */
    public static final int STYLE_CONFIRM = 0;

    /**
     * 带有确认取消按钮的确认弹出框
     */
    public static final int STYLE_CONFIRM_CANCEL = 1;

    /**
     * 没有按钮的消息弹出框，将会自动消失
     */
    public static final int STYLE_AUTO_DISMISS = 2;

    /**
     * 带有输入框的确认取消弹出框
     */
    public static final int STYLE_WITH_INPUT = 3;


    protected static final String KEY_TITLE = "key_title";        //对题
    protected static final String KEY_ICON = "key_resource"; //对话框图标id
    protected static final String KEY_MESSAGE = "key_message";    //对话框内容
    protected static final String KEY_TIPS = "key_tips";            //对话框描述
    protected static final String KEY_LEFT_BTN_TEXT = "c_left_btn_text";//对话框【左边】按钮文字
    protected static final String KEY_RIGHT_BTN_TEXT = "c_right_btn_text";//对话框【右边】按钮文字


    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //设置dialogfragment全屏
        getDialog().getWindow().getDecorView().setPadding(0, 0, 0, 0); //消除边距
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(lp);

        return inflater.inflate(R.layout.dialog_confirm_cancel, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTV = view.findViewById(R.id.dialog_title_tv);
        TextView messageTV = view.findViewById(R.id.dialog_message_tv);
        TextView messageTipsTV = view.findViewById(R.id.dialog_message_tips_tv);
        Button cancelBTN = view.findViewById(R.id.dialog_cancel_tv);
        Button confirmBTN = view.findViewById(R.id.dialog_confirm_tv);
        ImageView contentIV = view.findViewById(R.id.dialog_content_iv);

    }


    public static class Builder {
        private Bundle mmBundle;

        private View.OnClickListener mmLeftBtnClickListener;
        private View.OnClickListener mmRightBtnClickListener;

        private DialogInterface.OnCancelListener mmOnCancelListener;
        private DialogInterface.OnDismissListener mmOnDismissListener;
        private DialogInterface.OnShowListener mmOnShowListener;
    }


}
