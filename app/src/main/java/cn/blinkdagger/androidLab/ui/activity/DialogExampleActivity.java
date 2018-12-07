package cn.blinkdagger.androidLab.ui.activity;


import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.utils.DensityUtil;
import cn.blinkdagger.androidLab.widget.AutoDismissDialog;
import cn.blinkdagger.androidLab.widget.ConfirmCancelDialog;
import cn.blinkdagger.androidLab.widget.ConfirmDialog;
import cn.blinkdagger.androidLab.widget.ConfirmInputDialog;

import static android.os.Environment.DIRECTORY_MUSIC;

/**
 * @Author ls
 * @Date 2018/11/14
 * @Description
 * @Version
 */
public class DialogExampleActivity extends BaseActivity implements View.OnClickListener {
    private TextView confirmTV;
    private TextView confirmCancelTV;
    private TextView confirmInputTV;
    private TextView confirmDismissTV;

    @Override
    protected int getLayout() {
        return R.layout.activity_dialog_example;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        confirmTV = findViewById(R.id.confirm_tv);
        confirmCancelTV = findViewById(R.id.confirm_cancel_tv);
        confirmInputTV = findViewById(R.id.confirm_input_tv);
        confirmDismissTV = findViewById(R.id.confirm_dismiss_tv);
    }

    @Override
    protected void initData() {
        setToolbarTitle("DialogExample");
        confirmTV.setOnClickListener(this);
        confirmCancelTV.setOnClickListener(this);
        confirmInputTV.setOnClickListener(this);
        confirmDismissTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_tv:
                new ConfirmDialog.Builder()
                        .setMessage("你的密码已经重置成功，请重新登录！")
                        .setConfirmText("确认")
                        .setConfirmTextColor(getResources().getColor(R.color.common_c11))
                        .setTitle("标题")
                        .setCardRadius(DensityUtil.dp2px(this, 2))
                        .setCancelable(false)
                        .setMaterialStyle(true)
                        .build()
                        .show(getSupportFragmentManager());
                break;
            case R.id.confirm_cancel_tv:
                new ConfirmCancelDialog.Builder()
                        .setMessage("你的密码已经重置成功，请重新登录！")
                        .setLeftActionText("取消")
                        .setRightActionText("确认")
                        .setTitle("标题")
                        .setCardRadius(DensityUtil.dp2px(this, 2))
                        .setCancelable(false)
                        .setMaterialStyle(true)
                        .build()
                        .show(getSupportFragmentManager());
                break;
            case R.id.confirm_input_tv:
                new ConfirmInputDialog.Builder()
                        .setMessage("请修改你的登录密码：")
                        .setLeftActionText("取消")
                        .setRightActionText("确认")
                        .setTitle("提示")
                        .setCardRadius(DensityUtil.dp2px(this, 2))
                        .setCancelable(false)
                        .setMaterialStyle(true)
                        .setOnInitEditViewListener(new ConfirmInputDialog.OnInitEditViewListener() {
                            @Override
                            public void initEditView(EditText editText) {

                            }
                        })
                        .setOnLeftActionListener(new ConfirmInputDialog.OnActionButtonClickListener() {
                            @Override
                            public void onButtonClick(Editable editable) {
                                Toast.makeText(DialogExampleActivity.this, editable.toString(),Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnRightActionListener(new ConfirmInputDialog.OnActionButtonClickListener() {
                            @Override
                            public void onButtonClick(Editable editable) {
                                Toast.makeText(DialogExampleActivity.this, "确认："+editable.toString(),Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build()
                        .show(getSupportFragmentManager());
                break;
            case R.id.confirm_dismiss_tv:
                new AutoDismissDialog.Builder()
                        .setBgImageId(R.drawable.su)
                        .setMessage("你的密码已经重置成功，请重新登录！")
                        .setTitle("标题")
                        .setCardRadius(DensityUtil.dp2px(this, 2))
                        .setCancelable(false)
                        .setMaterialStyle(true)
                        .build()
                        .show(getSupportFragmentManager());

                Log.e("内部目录：", Environment.getDataDirectory().getAbsolutePath());
                Log.e("内部目录：", Environment.getDownloadCacheDirectory().getAbsolutePath());
                Log.e("内部目录：", Environment.getRootDirectory().getAbsolutePath());

                Log.e("外部存储公有：", Environment.getExternalStorageDirectory().getAbsolutePath());
                Log.e("外部存储公有【音乐】：", Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC).getAbsolutePath());

                Log.e("外部存储APP私有：", getExternalCacheDir().getAbsolutePath());
                Log.e("外部存储APP私有：", getExternalFilesDir("data").getAbsolutePath());

                break;
        }
    }
}
