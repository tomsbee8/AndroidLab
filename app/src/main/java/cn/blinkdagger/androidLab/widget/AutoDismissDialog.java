package cn.blinkdagger.androidLab.widget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
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
import android.widget.ImageView;
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
public class AutoDismissDialog extends DialogFragment {

    private static final String TAG = "ConfirmDialog";

    protected static final String KEY_TITLE = "TITLE";                      //对话框标题
    protected static final String KEY_IMAGE_ICON = "IMAGE_RES_ID";          //对话框图标Id
    protected static final String KEY_MESSAGE = "MESSAGE";                  //对话框内容
    protected static final String KEY_CARD_RADIUS = "DIALOG_RADIUS";        //对话框圆角半径
    protected static final String KEY_MARGIN_LEFT = "MARGIN_LEFT";          //对话框左边距[将覆盖宽度百分比设置]
    protected static final String KEY_MARGIN_RIGHT = "MARGIN_RIGHT";        //对话框右边距[将覆盖宽度百分比设置]
    protected static final String KEY_WIDTH_PERCENT = "WIDTH_PERCENT";      //对话框宽度百分比
    protected static final String KEY_MATERIAL_STYLE = "MATERIAL_STYLE";    //对话框原生风格
    protected static final String KEY_AUTO_DISMISS_DURATION = "DURATION";   //对话框显示时间


    private DialogInterface.OnCancelListener mOnCancelListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnShowListener mOnShowListener;

    private int marginLeft;
    private int marginRight;
    private float dialogWidthPercent;
    private long dialogDismissDuration;


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
            dialogDismissDuration = bundle.getLong(KEY_AUTO_DISMISS_DURATION, 0);

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
        if(dialogDismissDuration >0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, dialogDismissDuration);
        }

        return inflater.inflate(R.layout.dialog_auto_dismiss, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTV = view.findViewById(R.id.dialog_title_tv);
        TextView messageTV = view.findViewById(R.id.dialog_message_tv);
        ImageView contentIV = view.findViewById(R.id.dialog_content_iv);
        CardView cardView = view.findViewById(R.id.dialog_card_view);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        CharSequence title = bundle.getCharSequence(KEY_TITLE);
        CharSequence message = bundle.getCharSequence(KEY_MESSAGE);
        Boolean materialStyle = bundle.getBoolean(KEY_MATERIAL_STYLE);
        int imageIcon = bundle.getInt(KEY_IMAGE_ICON, 0);
        int cardRadius = bundle.getInt(KEY_CARD_RADIUS);


        if (materialStyle) {
            titleTV.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            messageTV.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        } else {
            titleTV.setGravity(Gravity.CENTER);
            messageTV.setGravity(Gravity.CENTER);
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

        if (imageIcon == 0) {
            contentIV.setVisibility(View.GONE);
        } else {
            contentIV.setVisibility(View.VISIBLE);
            contentIV.setImageResource(imageIcon);
        }
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
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                if(mOnShowListener!=null){
                    mOnShowListener.onShow(dialog);
                }
            }
        });
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

        public AutoDismissDialog.Builder setOnConfirmClickListener(View.OnClickListener onConfirmClickListener) {
            this.onConfirmClickListener = onConfirmClickListener;
            return this;
        }

        public AutoDismissDialog.Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public AutoDismissDialog.Builder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            this.onShowListener = onShowListener;
            return this;
        }

        public AutoDismissDialog.Builder setTitle(CharSequence title) {
            mBundle.putCharSequence(KEY_TITLE, title);
            return this;
        }

        public AutoDismissDialog.Builder setMessage(CharSequence message) {
            mBundle.putCharSequence(KEY_MESSAGE, message);
            return this;
        }

        public AutoDismissDialog.Builder setCardRadius(int cardRadius) {
            mBundle.putInt(KEY_CARD_RADIUS, cardRadius);
            return this;
        }

        public AutoDismissDialog.Builder setBgImageId(@DrawableRes int bgImageId) {
            mBundle.putInt(KEY_IMAGE_ICON, bgImageId);
            return this;
        }

        public AutoDismissDialog.Builder setCancelable(boolean cancelable) {
            dialog.getDialog().setCancelable(cancelable);
            return this;
        }

        public AutoDismissDialog.Builder setHorizontalMargin(int left, int right) {
            mBundle.putInt(KEY_MARGIN_LEFT, left);
            mBundle.putInt(KEY_MARGIN_RIGHT, right);
            return this;
        }

        public AutoDismissDialog.Builder setWidthPercent(@FloatRange(from = 0, to = 1.0) float marginPercent) {
            mBundle.putFloat(KEY_WIDTH_PERCENT, marginPercent);
            return this;
        }

        public AutoDismissDialog.Builder setMaterialStyle(boolean materialStyle) {
            mBundle.putBoolean(KEY_MATERIAL_STYLE, materialStyle);
            return this;
        }

        public AutoDismissDialog.Builder setAutoDismissDuration(long duration) {
            mBundle.putLong(KEY_AUTO_DISMISS_DURATION, duration);
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
