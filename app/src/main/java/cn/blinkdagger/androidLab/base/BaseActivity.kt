package cn.blinkdagger.androidLab.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.utils.SystemBarUtil

/**
 * 类描述：基本视图
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
abstract class BaseActivity : AppCompatActivity() {
    private var mToolbar: Toolbar? = null
    private var mContentRL: RelativeLayout? = null
    override fun onStart() {
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentLayout)
        setToolBar()
        setStatusBar()
        initView()
        initData()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @get:LayoutRes
    protected abstract val layout: Int

    @get:LayoutRes
    protected val contentLayout: Int
        protected get() = if (useToolbar()) {
            R.layout.common_toolbar_container
        } else {
            layout
        }

    protected open fun setStatusBar() {
        SystemBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorPrimary), true)
    }

    protected abstract fun useToolbar(): Boolean
    protected abstract fun initView()
    protected abstract fun initData()
    protected fun setToolBar() {
        if (useToolbar()) {
            mContentRL = findViewById<View>(R.id.common_content_rl) as RelativeLayout
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val contentView = inflater.inflate(layout, null)
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            mContentRL!!.addView(contentView, params)
            mToolbar = findViewById<View>(R.id.common_tb) as Toolbar
            setSupportActionBar(mToolbar)
            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setDisplayShowTitleEnabled(false)
            }
            mToolbar!!.setNavigationOnClickListener { onNavigationIconClick() }
        }
    }

    protected fun setToolbarTitle(charSequence: CharSequence?) {
        setToolbarTitle(charSequence, true)
    }

    protected fun setToolbarTitle(charSequence: CharSequence?, centerHorizontal: Boolean) {
        if (mToolbar != null) {
            val titleTV = mToolbar!!.findViewById<TextView>(R.id.common_center_tv)
            if (centerHorizontal) {
                titleTV.text = charSequence
            } else {
                mToolbar!!.title = charSequence
            }
        }
    }

    protected fun setNavigationIconInvisiable() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    protected open fun onNavigationIconClick() {
        finish()
    }
}