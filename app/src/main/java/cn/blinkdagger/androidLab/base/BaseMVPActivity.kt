package cn.blinkdagger.androidLab.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 类描述：基本视图
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
abstract class BaseMVPActivity<T : BasePresenter<*>?> : AppCompatActivity(), BaseView {
    protected var mPresenter: T? = null
    override fun onStart() {
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewAndData()
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
        if (mPresenter != null) {
            mPresenter?.detachView()
        }
    }

    override fun showError(msg: String?) {}
    override fun showLoading(msg: String?) {}
    override fun showEmpty(msg: String?) {}
    override fun showNoNetwork(msg: String?) {}
    protected abstract fun initInject()
    protected abstract val layout: Int
    protected abstract fun initViewAndData()
}