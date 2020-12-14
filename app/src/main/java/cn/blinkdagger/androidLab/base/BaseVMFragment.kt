package com.aventlabs.hbdj.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.http.CustomException
import cn.blinkdagger.pagestatemanage.PageStateMachine
import cn.blinkdagger.pagestatemanage.PageStateManager
import cn.blinkdagger.pagestatemanage.ShowStateListener
import io.reactivex.functions.Action
import retrofit2.HttpException
import java.io.IOException

/**
 * @Author ls
 * @Date  2020/10/13
 * @Description
 * @Version
 */
abstract class BaseVMFragment<VM : BaseViewModel> : Fragment(), ShowStateListener {

    protected var mPageStateManager: PageStateManager? = null

    protected lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPageState()
        initVM()
        initView()
        initData()
        startObserve()
    }


    protected fun initPageState() {
        if(initPageStateContentViewId() != 0 ) {
            mPageStateManager = PageStateMachine.with(this)
                .setContentViewId(initPageStateContentViewId()) // 设置内容View的Id，如果不设置将会把内容View默认为根布局
                .setShowLoadingWhenCreate(true)
                .setLoadingLayout(R.layout.layout_conent_loading) //设置加载中状态布局资源ID
                .setEmptyLayout(R.layout.layout_conent_empty)//设置空数据状态布局资源ID
                .setShowStateListener(this)
                .get()
        }
    }

    open protected fun initPageStateContentViewId(): Int {
        return 0
    }

    protected fun showContentEmpty() {
        mPageStateManager?.showLoadEmpty()
    }

    protected fun showContentData() {
        mPageStateManager?.showContent()
    }

    private fun initVM() {
        providerVMClass()?.let {
            viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[it]
            lifecycle.addObserver(viewModel)
        }
    }

    open fun providerVMClass(): Class<VM>? = null
    open fun startObserve() {}

    open fun onRequestComplete(it: Int?) {}

    open fun onRequestError(exception: Exception?) {
        //处理一些已知异常
        exception?.apply {
            exception.printStackTrace()
            when (this) {
                is CustomException -> {
                    Toast.makeText(activity, (exception as CustomException).errorMsg, Toast.LENGTH_SHORT).show()
                }
                is HttpException -> {
                    Toast.makeText(activity, R.string.error_service_unavailable, Toast.LENGTH_SHORT).show()
                }
                is IOException -> {
                    Toast.makeText(activity, R.string.error_network_exception_toast, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(activity, R.string.error_unknown_exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 必须实现的方法
     */
    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        if (this::viewModel.isInitialized)
            lifecycle.removeObserver(viewModel)
    }

    override fun onShowCustomState(stateCode: Int, loadingView: View?) {
    }

    override fun onShowLoadEmpty(emptyView: View?) {
    }

    override fun onShowLoadFailed(failedView: View?) {
    }

    override fun onShowLoading(loadingView: View?) {
    }

}