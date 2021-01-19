package cn.blinkdagger.androidLab.base

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.http.CustomException
import cn.blinkdagger.pagestatemanage.PageStateMachine
import cn.blinkdagger.pagestatemanage.PageStateManager
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * @Author ls
 * @Date  2020/10/12
 * @Description
 * @Version
 */
abstract class BaseVMActivity <VM : BaseViewModel> : BaseActivity() {

    protected var mPageStateManager : PageStateManager? = null

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        initVM()
        super.onCreate(savedInstanceState)
        initPageState()
        startObserve()
    }

    override fun onStart() {
        super.onStart()
    }

    abstract fun  providerVMClass() : Class<VM>

    private fun initVM() {
        providerVMClass()?.let {
            viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    protected fun initPageState(){
        if(initPageStateContentViewId() != 0 ){
            mPageStateManager = PageStateMachine.with(this)
                .setContentViewId(initPageStateContentViewId()) // 设置内容View的Id，如果不设置将会把内容View默认为根布局
                .setShowLoadingWhenCreate(false)// 设置是否在初始化的时候显示加载中
                .setLoadingLayout(R.layout.layout_conent_loading) //设置加载中状态布局资源ID
                .setEmptyLayout(R.layout.layout_conent_empty)//设置空数据状态布局资源ID
                .get()
        }
    }

    open fun initPageStateContentViewId() : Int{
        return  0
    }


    protected fun showContentEmpty(){
        mPageStateManager?.showLoadEmpty()
    }

    protected fun showContentData() {
        mPageStateManager?.showContent()
    }

    private fun startObserve() {
        //处理一些通用异常，比如网络超时等
        viewModel.run {
            getErrorLiveData().observe(this@BaseVMActivity, Observer {
                onRequestError(it)
            })
            getCompleteLiveData().observe(this@BaseVMActivity, Observer {
                onRequestComplete(it)
            })
        }
    }

    open fun onRequestComplete(it: Int?) {

    }

    open fun onRequestError(exception: Exception?) {
        //处理一些已知异常
        exception?.apply {
            exception.printStackTrace()
            when (this) {
                is CustomException ->{
                    Toast.makeText(applicationContext, (exception as CustomException).errorMsg,Toast.LENGTH_SHORT).show()
                }
                is SocketTimeoutException ->{
                    Toast.makeText(applicationContext, R.string.error_time_out_exception,Toast.LENGTH_SHORT).show()
                }
                is HttpException -> {
                    Toast.makeText(applicationContext, R.string.error_service_unavailable,Toast.LENGTH_SHORT).show()
                }
                is IOException ->{
                    Toast.makeText(applicationContext, R.string.error_network_exception_toast,Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(applicationContext, R.string.error_unknown_exception,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroy() {

        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}