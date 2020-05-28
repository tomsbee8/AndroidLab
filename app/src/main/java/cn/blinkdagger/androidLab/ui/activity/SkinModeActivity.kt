package cn.blinkdagger.androidLab.ui.activity

import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.base.BaseActivity
import kotlinx.android.synthetic.main.activity_skin_mode.*


/**
 * @Author ls
 * @Date  2020/5/27
 * @Description
 * @Version
 */
class SkinModeActivity : BaseActivity() {


    override fun getContentLayout(): Int {
        return R.layout.activity_skin_mode
    }

    override fun initView() {
        setToolbarTitle("NightMode")
        setModeTV(AppCompatDelegate.getDefaultNightMode())
        skin_action_tv.setOnClickListener {
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun initData() {

    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
    }

    private fun setModeTV(mode: Int) {
        when (mode) {
            AppCompatDelegate.MODE_NIGHT_YES -> skin_mode_tv.text = getString(R.string.app_current_mode, "深色模式")
            AppCompatDelegate.MODE_NIGHT_NO -> skin_mode_tv.text = getString(R.string.app_current_mode, "浅色模式")
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> skin_mode_tv.text = getString(R.string.app_current_mode, "跟随省电模式")
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> skin_mode_tv.text = getString(R.string.app_current_mode, "跟随系统")
        }
    }
}