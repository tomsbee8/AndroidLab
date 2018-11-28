package cn.blinkdagger.androidLab.widget;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import cn.blinkdagger.androidLab.R;

/**
 * @Author ls
 * @Date 2018/11/24
 * @Description
 * @Version
 */
public class ConfirmDialog extends DialogFragment {

    private static final String TAG = "ConfirmDialog";

    protected static final String KEY_TITLE = "key_title";                //对话框标题
    protected static final String KEY_IMAGE_ICON = "key_image_id";        //对话框图标id
    protected static final String KEY_MESSAGE = "key_message";            //对话框内容
    protected static final String KEY_MESSAGE_TIPS = "key_message_tips";  //对话框描述
    protected static final String KEY_BTN_TEXT = "c_right_btn_text";      //对话框【右边】按钮文字
    protected static final String KEY_CARD_RADIUS = "c_right_btn_text";   //对话框【右边】按钮文字

    private View.OnClickListener onConfirmClickListener;

    private DialogInterface.OnCancelListener mOnCancelListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnShowListener mOnShowListener;

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
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(lp);
        getDialog().getWindow().setGravity(Gravity.CENTER);

        return inflater.inflate(R.layout.dialog_confirm, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTV = view.findViewById(R.id.dialog_title_tv);
        TextView messageTV = view.findViewById(R.id.dialog_message_tv);
        TextView messageTipsTV = view.findViewById(R.id.dialog_message_tips_tv);
        Button confirmBTN = view.findViewById(R.id.dialog_confirm_btn);
        ImageView contentIV = view.findViewById(R.id.dialog_content_iv);
        CardView cardView = view.findViewById(R.id.dialog_card_view);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        CharSequence title = bundle.getCharSequence(KEY_TITLE);
        CharSequence message = bundle.getCharSequence(KEY_MESSAGE);
        CharSequence messageTips = bundle.getCharSequence(KEY_MESSAGE_TIPS);
        CharSequence btnText = bundle.getCharSequence(KEY_BTN_TEXT);
        int imageIcon = bundle.getInt(KEY_IMAGE_ICON);
        int cardRadius = bundle.getInt(KEY_CARD_RADIUS);


        if (TextUtils.isEmpty(title)) {
            titleTV.setVisibility(View.GONE);
        } else {
            titleTV.setVisibility(View.VISIBLE);
            titleTV.setText(title);
        }

        if (TextUtils.isEmpty(message)) {
            messageTV.setVisibility(View.GONE);
        } else {
            messageTV.setVisibility(View.VISIBLE);
            messageTV.setText(message);
        }

        if (TextUtils.isEmpty(messageTips)) {
            messageTipsTV.setVisibility(View.GONE);
        } else {
            messageTipsTV.setVisibility(View.VISIBLE);
            messageTipsTV.setText(messageTips);
        }
        if (imageIcon > 0) {
            contentIV.setVisibility(View.GONE);
        } else {
            contentIV.setVisibility(View.VISIBLE);
            contentIV.setImageResource(imageIcon);
        }
        confirmBTN.setText(btnText);
        cardView.setRadius(cardRadius);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mOnCancelListener != null) {
            mOnCancelListener.onCancel(dialog);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setOnShowListener(mOnShowListener);
    }


    public void show(FragmentManager fm) {
        if (fm == null) {
            Logger.e(TAG, "show error : fragment manager is null.");
            return;
        }
        this.show(fm, TAG);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener mOnCancelListener) {
        this.mOnCancelListener = mOnCancelListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }

    public void setOnShowListener(DialogInterface.OnShowListener mOnShowListener) {
        this.mOnShowListener = mOnShowListener;
    }

    public void setOnConfirmClickListener(View.OnClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public static class Builder {
        private Bundle mBundle;

        private View.OnClickListener onConfirmClickListener;

        private DialogInterface.OnDismissListener onDismissListener;
        private DialogInterface.OnShowListener onShowListener;

        public Builder() {
            mBundle = new Bundle();
        }

        public Builder setOnConfirmClickListener(View.OnClickListener onConfirmClickListener) {
            this.onConfirmClickListener = onConfirmClickListener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            this.onShowListener = onShowListener;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            mBundle.putCharSequence(KEY_TITLE, title);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mBundle.putCharSequence(KEY_MESSAGE, message);
            return this;
        }

        public Builder setMessageTips(CharSequence messageTips) {
            mBundle.putCharSequence(KEY_MESSAGE_TIPS, messageTips);
            return this;
        }

        public Builder setConfirmText(CharSequence btnString) {
            mBundle.putCharSequence(KEY_BTN_TEXT, btnString);
            return this;
        }

        public Builder setCardRadius(int cardRadius) {
            mBundle.putInt(KEY_CARD_RADIUS, cardRadius);
            return this;
        }

        public Builder setBgImageId(@DrawableRes int bgImageId) {
            mBundle.putInt(KEY_IMAGE_ICON, bgImageId);
            return this;
        }

        public ConfirmDialog build() {
            final ConfirmDialog dialog = new ConfirmDialog();
            dialog.setArguments(mBundle);
            dialog.setOnDismissListener(onDismissListener);
            dialog.setOnShowListener(onShowListener);
            dialog.setOnConfirmClickListener(onConfirmClickListener);
            return dialog;
        }
    }
}
