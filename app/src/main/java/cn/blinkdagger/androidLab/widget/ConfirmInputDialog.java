package cn.blinkdagger.androidLab.widget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
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
public class ConfirmInputDialog extends DialogFragment {

    private static final String TAG = "ConfirmInputDialog";

    protected static final String KEY_TITLE = "TITLE";                      //对话框标题
    protected static final String KEY_MESSAGE = "MESSAGE";                  //对话框内容
    protected static final String KEY_LEFT_BTN_TEXT = "LEFT_BUTTON_TEXT";   //对话框左边按钮文字
    protected static final String KEY_RIGHT_BTN_TEXT = "RIGHT_BUTTON_TEXT"; //对话框右边按钮文字
    protected static final String KEY_LEFT_TEXT_COLOR = "BUTTON_TEXT_COLOR"; //对话框左边按钮颜色
    protected static final String KEY_RIGHT_TEXT_COLOR = "BUTTON_TEXT_COLOR";//对话框右边按钮颜色
    protected static final String KEY_CARD_RADIUS = "DIALOG_RADIUS";        //对话框圆角半径
    protected static final String KEY_MARGIN_LEFT = "MARGIN_LEFT";          //对话框左边距[将覆盖宽度百分比设置]
    protected static final String KEY_MARGIN_RIGHT = "MARGIN_RIGHT";        //对话框右边距[将覆盖宽度百分比设置]
    protected static final String KEY_WIDTH_PERCENT = "WIDTH_PERCENT";      //对话框宽度百分比
    protected static final String KEY_MATERIAL_STYLE = "MATERIAL_STYLE";    //对话框原生风格

    private OnActionButtonClickListener onLeftButtonClickListener;
    private OnActionButtonClickListener onRightButtonClickListener;
    private OnInitEditViewListener onInitEditViewListener;

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

        return inflater.inflate(R.layout.dialog_confirm_input, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTV = view.findViewById(R.id.dialog_title_tv);
        TextView messageTV = view.findViewById(R.id.dialog_message_tv);
        final EditText inputET = view.findViewById(R.id.dialog_input_et);
        TextView leftActionTV = view.findViewById(R.id.dialog_left_action_tv);
        TextView rightActionTV = view.findViewById(R.id.dialog_right_action_tv);
        View centerLine = view.findViewById(R.id.dialog_center_line);
        CardView cardView = view.findViewById(R.id.dialog_card_view);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        CharSequence title = bundle.getCharSequence(KEY_TITLE);
        CharSequence message = bundle.getCharSequence(KEY_MESSAGE);
        CharSequence leftBtnText = bundle.getCharSequence(KEY_LEFT_BTN_TEXT);
        CharSequence rightBtnText = bundle.getCharSequence(KEY_RIGHT_BTN_TEXT);
        Boolean materialStyle = bundle.getBoolean(KEY_MATERIAL_STYLE);
        int leftBtnColor = bundle.getInt(KEY_LEFT_TEXT_COLOR);
        int rightBtnColor = bundle.getInt(KEY_RIGHT_TEXT_COLOR);
        int cardRadius = bundle.getInt(KEY_CARD_RADIUS);


        if (materialStyle) {
            titleTV.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

            ConstraintLayout.LayoutParams leftLp = (ConstraintLayout.LayoutParams) leftActionTV.getLayoutParams();
            leftLp.rightToLeft = R.id.dialog_right_action_tv;
            leftActionTV.setLayoutParams(leftLp);
            centerLine.setVisibility(View.GONE);
        } else {
            titleTV.setGravity(Gravity.CENTER);
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
        if (leftBtnColor != 0) {
            leftActionTV.setTextColor(leftBtnColor);
        }
        if (rightBtnColor != 0) {
            rightActionTV.setTextColor(rightBtnColor);
        }

        if(onInitEditViewListener!=null){
            onInitEditViewListener.initEditView(inputET);
        }
        leftActionTV.setText(leftBtnText);
        rightActionTV.setText(rightBtnText);
        leftActionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftButtonClickListener != null) {
                    onLeftButtonClickListener.onButtonClick(inputET.getText());
                }
                dismissAllowingStateLoss();
            }
        });
        rightActionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightButtonClickListener != null) {
                    onRightButtonClickListener.onButtonClick(inputET.getText());
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

    public void setOnLeftButtonClickListener(OnActionButtonClickListener onLeftButtonClickListener) {
        this.onLeftButtonClickListener = onLeftButtonClickListener;
    }

    public void setOnRightButtonClickListener(OnActionButtonClickListener onRightButtonClickListener) {
        this.onRightButtonClickListener = onRightButtonClickListener;
    }

    public void setOnInitEditViewListener(OnInitEditViewListener onInitEditViewListener) {
        this.onInitEditViewListener = onInitEditViewListener;
    }

    /**
     * 用于初始化EditText
     */
    public interface  OnInitEditViewListener{
        void initEditView(EditText editText);
    }

    /**
     * 用于监听EditText
     */
    public interface  OnActionButtonClickListener{
        void onButtonClick(Editable editable);
    }

    public static class Builder {
        private ConfirmInputDialog dialog;
        private Bundle mBundle;

        private OnActionButtonClickListener onLeftActionListener;
        private OnActionButtonClickListener onRightActionListener;
        private OnInitEditViewListener onInitEditViewListener;

        private DialogInterface.OnDismissListener onDismissListener;
        private DialogInterface.OnShowListener onShowListener;

        public Builder() {
            mBundle = new Bundle();
            dialog = new ConfirmInputDialog();
        }

        public ConfirmInputDialog.Builder setOnLeftActionListener(OnActionButtonClickListener onLeftActionListener) {
            this.onLeftActionListener = onLeftActionListener;
            return this;
        }

        public ConfirmInputDialog.Builder setOnRightActionListener(OnActionButtonClickListener onRightActionListener) {
            this.onRightActionListener = onRightActionListener;
            return this;
        }

        public ConfirmInputDialog.Builder setOnInitEditViewListener(OnInitEditViewListener onInitEditViewListener) {
            this.onInitEditViewListener = onInitEditViewListener;
            return this;
        }

        public ConfirmInputDialog.Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public ConfirmInputDialog.Builder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            this.onShowListener = onShowListener;
            return this;
        }

        public ConfirmInputDialog.Builder setTitle(CharSequence title) {
            mBundle.putCharSequence(KEY_TITLE, title);
            return this;
        }

        public ConfirmInputDialog.Builder setMessage(CharSequence message) {
            mBundle.putCharSequence(KEY_MESSAGE, message);
            return this;
        }

        public ConfirmInputDialog.Builder setLeftActionText(CharSequence btnString) {
            mBundle.putCharSequence(KEY_LEFT_BTN_TEXT, btnString);
            return this;
        }

        public ConfirmInputDialog.Builder setRightActionText(CharSequence btnString) {
            mBundle.putCharSequence(KEY_RIGHT_BTN_TEXT, btnString);
            return this;
        }

        public ConfirmInputDialog.Builder setLeftActionTextColor(@ColorInt int btnTextColor) {
            mBundle.putInt(KEY_LEFT_TEXT_COLOR, btnTextColor);
            return this;
        }

        public ConfirmInputDialog.Builder setRightActionTextColor(@ColorInt int btnTextColor) {
            mBundle.putInt(KEY_RIGHT_TEXT_COLOR, btnTextColor);
            return this;
        }

        public ConfirmInputDialog.Builder setCardRadius(int cardRadius) {
            mBundle.putInt(KEY_CARD_RADIUS, cardRadius);
            return this;
        }

        public ConfirmInputDialog.Builder setCancelable(boolean cancelable) {
            dialog.setCancelable(cancelable);
            return this;
        }

        public ConfirmInputDialog.Builder setHorizontalMargin(int left, int right) {
            mBundle.putInt(KEY_MARGIN_LEFT, left);
            mBundle.putInt(KEY_MARGIN_RIGHT, right);
            return this;
        }

        public ConfirmInputDialog.Builder setWidthPercent(@FloatRange(from = 0, to = 1.0) float marginPercent) {
            mBundle.putFloat(KEY_WIDTH_PERCENT, marginPercent);
            return this;
        }

        public ConfirmInputDialog.Builder setMaterialStyle(boolean materialStyle) {
            mBundle.putBoolean(KEY_MATERIAL_STYLE, materialStyle);
            return this;
        }

        public ConfirmInputDialog build() {
            dialog.setArguments(mBundle);
            dialog.setOnDismissListener(onDismissListener);
            dialog.setOnShowListener(onShowListener);
            dialog.setOnLeftButtonClickListener(onLeftActionListener);
            dialog.setOnRightButtonClickListener(onRightActionListener);
            dialog.setOnInitEditViewListener(onInitEditViewListener);
            return dialog;
        }
    }
}
