package cn.blinkdagger.androidLab.widget;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
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
public class ConfirmCancelDialog extends DialogFragment {

    private static final String TAG = "ConfirmCancelDialog";

    protected static final String KEY_TITLE = "TITLE";                      //对话框标题
    protected static final String KEY_IMAGE_ICON = "IMAGE_RES_ID";          //对话框图标Id
    protected static final String KEY_MESSAGE = "MESSAGE";                  //对话框内容
    protected static final String KEY_MESSAGE_TIPS = "MESSAGE_TIPS";        //对话框描述
    protected static final String KEY_LEFT_BTN_TEXT = "LEFT_BUTTON_TEXT";   //对话框左边按钮文字
    protected static final String KEY_RIGHT_BTN_TEXT = "RIGHT_BUTTON_TEXT"; //对话框右边按钮文字
    protected static final String KEY_LEFT_TEXT_COLOR = "BUTTON_TEXT_COLOR"; //对话框左边按钮颜色
    protected static final String KEY_RIGHT_TEXT_COLOR = "BUTTON_TEXT_COLOR"; //对话框右边按钮颜色
    protected static final String KEY_CARD_RADIUS = "DIALOG_RADIUS";        //对话框圆角半径
    protected static final String KEY_MARGIN_LEFT = "MARGIN_LEFT";          //对话框左边距[将覆盖宽度百分比设置]
    protected static final String KEY_MARGIN_RIGHT = "MARGIN_RIGHT";        //对话框右边距[将覆盖宽度百分比设置]
    protected static final String KEY_WIDTH_PERCENT = "WIDTH_PERCENT";      //对话框宽度百分比
    protected static final String KEY_MATERIAL_STYLE = "MATERIAL_STYLE";    //对话框原生风格

    private View.OnClickListener onLeftButtonClickListener;
    private View.OnClickListener onRightButtonClickListener;

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
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
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

        return inflater.inflate(R.layout.dialog_confirm_cancel, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTV = view.findViewById(R.id.dialog_title_tv);
        TextView messageTV = view.findViewById(R.id.dialog_message_tv);
        TextView messageTipsTV = view.findViewById(R.id.dialog_message_tips_tv);
        TextView leftActionTV = view.findViewById(R.id.dialog_left_action_tv);
        TextView rightActionTV = view.findViewById(R.id.dialog_right_action_tv);
        View centerLine = view.findViewById(R.id.dialog_center_line);
        ImageView contentIV = view.findViewById(R.id.dialog_content_iv);
        CardView cardView = view.findViewById(R.id.dialog_card_view);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        CharSequence title = bundle.getCharSequence(KEY_TITLE);
        CharSequence message = bundle.getCharSequence(KEY_MESSAGE);
        CharSequence messageTips = bundle.getCharSequence(KEY_MESSAGE_TIPS);
        CharSequence leftBtnText = bundle.getCharSequence(KEY_LEFT_BTN_TEXT);
        CharSequence rightBtnText = bundle.getCharSequence(KEY_RIGHT_BTN_TEXT);
        Boolean materialStyle = bundle.getBoolean(KEY_MATERIAL_STYLE);
        int imageIcon = bundle.getInt(KEY_IMAGE_ICON, 0);
        int leftBtnColor = bundle.getInt(KEY_LEFT_TEXT_COLOR);
        int rightBtnColor = bundle.getInt(KEY_RIGHT_TEXT_COLOR);
        int cardRadius = bundle.getInt(KEY_CARD_RADIUS);


        if (materialStyle) {
            titleTV.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            messageTV.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

            ConstraintLayout.LayoutParams leftLp = (ConstraintLayout.LayoutParams) leftActionTV.getLayoutParams();
            leftLp.rightToLeft = R.id.dialog_right_action_tv;
            leftActionTV.setLayoutParams(leftLp);
            centerLine.setVisibility(View.GONE);

        } else {
            titleTV.setGravity(Gravity.CENTER);
            messageTV.setGravity(Gravity.CENTER);
            ConstraintLayout.LayoutParams leftLp = (ConstraintLayout.LayoutParams) leftActionTV.getLayoutParams();
            leftLp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            leftLp.rightToLeft = R.id.dialog_center_line;
            leftActionTV.setLayoutParams(leftLp);

            ConstraintLayout.LayoutParams rightLp = (ConstraintLayout.LayoutParams) rightActionTV.getLayoutParams();
            rightLp.leftToLeft = R.id.dialog_center_line;
            rightActionTV.setLayoutParams(rightLp);
            centerLine.setVisibility(View.VISIBLE);

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
        if (leftBtnColor != 0) {
            leftActionTV.setTextColor(leftBtnColor);
        }
        if (rightBtnColor != 0) {
            rightActionTV.setTextColor(rightBtnColor);
        }
        leftActionTV.setText(leftBtnText);
        rightActionTV.setText(rightBtnText);
        leftActionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftButtonClickListener != null) {
                    onLeftButtonClickListener.onClick(v);
                }
                dismissAllowingStateLoss();
            }
        });
        rightActionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightButtonClickListener != null) {
                    onRightButtonClickListener.onClick(v);
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

    public void setOnLeftButtonClickListener(View.OnClickListener onLeftButtonClickListener) {
        this.onLeftButtonClickListener = onLeftButtonClickListener;
    }

    public void setOnRightButtonClickListener(View.OnClickListener onRightButtonClickListener) {
        this.onRightButtonClickListener = onRightButtonClickListener;
    }

    public static class Builder {
        private ConfirmCancelDialog dialog;
        private Bundle mBundle;

        private View.OnClickListener onLeftActionListener;
        private View.OnClickListener onRightActionListener;

        private DialogInterface.OnDismissListener onDismissListener;
        private DialogInterface.OnShowListener onShowListener;

        public Builder() {
            mBundle = new Bundle();
            dialog = new ConfirmCancelDialog();
        }

        public void setOnLeftActionListener(View.OnClickListener onLeftActionListener) {
            this.onLeftActionListener = onLeftActionListener;
        }

        public void setOnRightActionListener(View.OnClickListener onRightActionListener) {
            this.onRightActionListener = onRightActionListener;
        }

        public ConfirmCancelDialog.Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public ConfirmCancelDialog.Builder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            this.onShowListener = onShowListener;
            return this;
        }

        public ConfirmCancelDialog.Builder setTitle(CharSequence title) {
            mBundle.putCharSequence(KEY_TITLE, title);
            return this;
        }

        public ConfirmCancelDialog.Builder setMessage(CharSequence message) {
            mBundle.putCharSequence(KEY_MESSAGE, message);
            return this;
        }

        public ConfirmCancelDialog.Builder setMessageTips(CharSequence messageTips) {
            mBundle.putCharSequence(KEY_MESSAGE_TIPS, messageTips);
            return this;
        }

        public ConfirmCancelDialog.Builder setLeftActionText(CharSequence btnString) {
            mBundle.putCharSequence(KEY_LEFT_BTN_TEXT, btnString);
            return this;
        }

        public ConfirmCancelDialog.Builder setRightActionText(CharSequence btnString) {
            mBundle.putCharSequence(KEY_RIGHT_BTN_TEXT, btnString);
            return this;
        }

        public ConfirmCancelDialog.Builder setLeftActionTextColor(@ColorInt int btnTextColor) {
            mBundle.putInt(KEY_LEFT_TEXT_COLOR, btnTextColor);
            return this;
        }

        public ConfirmCancelDialog.Builder setRightActionTextColor(@ColorInt int btnTextColor) {
            mBundle.putInt(KEY_RIGHT_TEXT_COLOR, btnTextColor);
            return this;
        }

        public ConfirmCancelDialog.Builder setCardRadius(int cardRadius) {
            mBundle.putInt(KEY_CARD_RADIUS, cardRadius);
            return this;
        }

        public ConfirmCancelDialog.Builder setBgImageId(@DrawableRes int bgImageId) {
            mBundle.putInt(KEY_IMAGE_ICON, bgImageId);
            return this;
        }

        public ConfirmCancelDialog.Builder setCancelable(boolean cancelable) {
            dialog.setCancelable(cancelable);
            return this;
        }

        public ConfirmCancelDialog.Builder setHorizontalMargin(int left, int right) {
            mBundle.putInt(KEY_MARGIN_LEFT, left);
            mBundle.putInt(KEY_MARGIN_RIGHT, right);
            return this;
        }

        public ConfirmCancelDialog.Builder setWidthPercent(@FloatRange(from = 0, to = 1.0) float marginPercent) {
            mBundle.putFloat(KEY_WIDTH_PERCENT, marginPercent);
            return this;
        }

        public ConfirmCancelDialog.Builder setMaterialStyle(boolean materialStyle) {
            mBundle.putBoolean(KEY_MATERIAL_STYLE, materialStyle);
            return this;
        }

        public ConfirmCancelDialog build() {
            dialog.setArguments(mBundle);
            dialog.setOnDismissListener(onDismissListener);
            dialog.setOnShowListener(onShowListener);
            dialog.setOnLeftButtonClickListener(onLeftActionListener);
            dialog.setOnRightButtonClickListener(onRightActionListener);
            return dialog;
        }
    }


}
