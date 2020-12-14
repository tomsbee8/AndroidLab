package cn.blinkdagger.androidLab.ui.activity

import android.content.Context
import android.content.Intent
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.base.BaseActivity

/**
 * @Author ls
 * @Date  2020/12/11
 * @Description
 * @Version
 */
class VideoPlayerActivity : BaseActivity() {

    companion object{

        @JvmStatic
        fun start(context: Context){
            val intent = Intent(context, VideoPlayerActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
    }

    override fun initData() {
    }
}