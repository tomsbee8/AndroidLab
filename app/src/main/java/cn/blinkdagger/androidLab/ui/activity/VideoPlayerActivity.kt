package cn.blinkdagger.androidLab.ui.activity

import android.content.ClipData
import android.content.Context
import android.content.Intent
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.base.BaseActivity
import cn.blinkdagger.androidLab.entity.TabItem
import cn.blinkdagger.androidLab.extend.*
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Permissions

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

        val s = arrayListExtra<TabItem>("")

        val abs = intent.getParcelableArrayListExtra<TabItem>("ss")

        launchActivity<DrawerActivity>("abc" to "abc" )
        launchActivity<DrawerActivity>()
        launchActivityForResult<DrawerActivity>{

        }

    }

    override fun initData() {
    }
}