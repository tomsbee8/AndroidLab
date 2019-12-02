package cn.blinkdagger.androidLab.widget;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.utils.ScreenUtil;

/**
 * @Author ls
 * @Date 2018/11/24
 * @Description
 * @Version
 */
public class ConfirmDialog extends DialogFragment {

    private static final String TAG = "ConfirmDialog";

    protected static final String KEY_TITLE = "TITLE";                      //对话框标题
    protected static final String KEY_IMAGE_ICON = "IMAGE_RES_ID";          //对话框图标Id
    protected static final String KEY_MESSAGE = "MESSAGE";                  //对话框内容
    protected static final String KEY_MESSAGE_TIPS = "MESSAGE_TIPS";        //对话框描述
    protected static final String KEY_BTN_TEXT = "BUTTON_TEXT";             //对话框按钮文字
    protected static final String KEY_BTN_TEXT_COLOR = "BUTTON_TEXT_COLOR"; //对话框按钮颜色
    protected static final String KEY_CARD_RADIUS = "DIALOG_RADIUS";        //对话框圆角半径
    protected static final String KEY_MARGIN_LEFT = "MARGIN_LEFT";          //对话框左边距[将覆盖宽度百分比设置]
    protected static final String KEY_MARGIN_RIGHT = "MARGIN_RIGHT";        //对话框右边距[将覆盖宽度百分比设置]
    protected static final String KEY_WIDTH_PERCENT = "WIDTH_PERCENT";      //对话框宽度百分比
    protected static final String KEY_MATERIAL_STYLE = "MATERIAL_STYLE";    //对话框原生风格

    private View.OnClickListener onConfirmClickListener;

    private DialogInterface.OnCancelListener mOnCancelListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnShowListener mOnShowListener;

    private int marginLeft;
    private int marginRight;
    private float dialogWidthPercent;


    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            marginLeft = bundle.getInt(KEY_MARGIN_LEFT, 0);
            marginRight = bundle.getInt(KEY_MARGIN_RIGHT, 0);
            dialogWidthPercent = bundle.getFloat(KEY_WIDTH_PERCENT, 0.85f);
        }
        getDialog().getWindow().getDecorView().setPadding(marginLeft, 0, marginRight, 0); //消除边距
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();

        if (getActivity() != null && marginLeft == 0 && marginRight == 0) {
            lp.width = (int) (ScreenUtil.getScreenWidth(getActivity()) * dialogWidthPercent);
        } else {
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
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
        TextView confirmTV = view.findViewById(R.id.dialog_confirm_tv);
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
        Boolean materialStyle = bundle.getBoolean(KEY_MATERIAL_STYLE);
        int imageIcon = bundle.getInt(KEY_IMAGE_ICON, 0);
        int btnTextColor = bundle.getInt(KEY_BTN_TEXT_COLOR);
        int cardRadius = bundle.getInt(KEY_CARD_RADIUS);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) confirmTV.getLayoutParams();

        if (materialStyle) {
            titleTV.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            messageTV.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            lp.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
            confirmTV.setLayoutParams(lp);
        } else {
            titleTV.setGravity(Gravity.CENTER);
            messageTV.setGravity(Gravity.CENTER);
            lp.gravity = Gravity.CENTER;
            confirmTV.setLayoutParams(lp);
        }

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
        if (imageIcon == 0) {
            contentIV.setVisibility(View.GONE);
        } else {
            contentIV.setVisibility(View.VISIBLE);
            contentIV.setImageResource(imageIcon);
        }
        if (btnTextColor != 0) {
            confirmTV.setTextColor(btnTextColor);
        }
        confirmTV.setText(btnText);
        confirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmClickListener != null) {
                    onConfirmClickListener.onClick(v);
                }
                dismissAllowingStateLoss();
            }
        });
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
        private ConfirmDialog dialog;
        private Bundle mBundle;

        private View.OnClickListener onConfirmClickListener;

        private DialogInterface.OnDismissListener onDismissListener;
        private DialogInterface.OnShowListener onShowListener;

        public Builder() {
            mBundle = new Bundle();
            dialog = new ConfirmDialog();
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

        public Builder setConfirmTextColor(@ColorInt int btnTextColor) {
            mBundle.putInt(KEY_BTN_TEXT_COLOR, btnTextColor);
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

        public Builder setCancelable(boolean cancelable) {
            dialog.setCancelable(cancelable);
            return this;
        }

        public Builder setHorizontalMargin(int left, int right) {
            mBundle.putInt(KEY_MARGIN_LEFT, left);
            mBundle.putInt(KEY_MARGIN_RIGHT, right);
            return this;
        }

        public Builder setWidthPercent(@FloatRange(from = 0, to = 1.0) float marginPercent) {
            mBundle.putFloat(KEY_WIDTH_PERCENT, marginPercent);
            return this;
        }

        public Builder setMaterialStyle(boolean materialStyle) {
            mBundle.putBoolean(KEY_MATERIAL_STYLE, materialStyle);
            return this;
        }

        public ConfirmDialog build() {
            dialog.setArguments(mBundle);
            dialog.setOnDismissListener(onDismissListener);
            dialog.setOnShowListener(onShowListener);
            dialog.setOnConfirmClickListener(onConfirmClickListener);
            return dialog;
        }
    }
}
