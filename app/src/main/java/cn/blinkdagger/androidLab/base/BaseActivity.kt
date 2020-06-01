package cn.blinkdagger.androidLab.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.utils.SystemBarUtil
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


/**
 * 类描述：基本视图
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
abstract class BaseActivity : AppCompatActivity() {
    private var mLastDayNightMode = 0
    var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentLayoutId())
        mLastDayNightMode = AppCompatDelegate.getDefaultNightMode()
        setStatusBar()
        initToolbar()
        initView()
        initData()
    }

    override fun onStart() {
        AnkoLogger(javaClass.simpleName.take(23)).info { "onDestroy" }
        super.onStart()
    }

    override fun onRestart() {
        AnkoLogger(javaClass.simpleName.take(23)).info { "onDestroy" }
        super.onRestart()
        if (Build.VERSION.SDK_INT < 24 && mLastDayNightMode != AppCompatDelegate.getDefaultNightMode()) {
            recreate()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onPause() {
        AnkoLogger(javaClass.simpleName.take(23)).info { "onDestroy" }
        super.onPause()
    }

    override fun onResume() {
        AnkoLogger(javaClass.simpleName.take(23)).info { "onDestroy" }
        super.onResume()
    }

    override fun onStop() {
        AnkoLogger(javaClass.simpleName.take(23)).info { "onStop" }
        super.onStop()
    }

    override fun onDestroy() {
        AnkoLogger(javaClass.simpleName.take(23)).info { "onDestroy" }
        super.onDestroy()
    }

    @LayoutRes
    abstract fun getContentLayout(): Int

    protected open fun useToolbar(): Boolean = true
    protected abstract fun initView()
    protected abstract fun initData()

    private fun getContentLayoutId(): Int {
        return if (useToolbar()) {
            R.layout.root_toolbar_container
        } else {
            getContentLayout()
        }
    }


    protected open fun setStatusBar() {
        SystemBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorPrimary), true)
    }

    private fun initToolbar() {
        if (useToolbar()) {
            findViewById<LinearLayout>(R.id.root_layout)?.run {
                val contentView = layoutInflater.inflate( getContentLayout(),this,false)
                addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
            mToolbar = findViewById<Toolbar>(R.id.common_tool_bar)
            setSupportActionBar(mToolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false)
            }
            mToolbar?.setNavigationOnClickListener { onNavigationIconClick() }
        }
    }

    protected fun setToolbarTitle(charSequence: CharSequence?) {
        setToolbarTitle(charSequence, true)
    }

    protected fun setToolbarTitle(charSequence: CharSequence?, centerHorizontal: Boolean = true) {
        mToolbar?.let {
            if (centerHorizontal) {
                it.findViewById<TextView>(R.id.common_center_tv)?.text =  charSequence
            } else {
                it.title = charSequence
            }
        }
    }

    protected fun setNavigationIconInvisible() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    protected open fun onNavigationIconClick() {
        finish()
    }
}