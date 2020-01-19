package cn.blinkdagger.androidLab.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 类描述：控制订阅的生命周期
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class RxPresenter<T : BaseView?> : BasePresenter<T> {
    protected var mView: T? = null
    protected var mCompositeSubscription: CompositeDisposable? = null
    protected fun unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription!!.dispose()
        }
    }

    protected fun addSubscrebe(subscription: Disposable?) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeDisposable()
        }
        mCompositeSubscription!!.add(subscription!!)
    }

    override fun attachView(view: T) {
        mView = view
    }

    override fun detachView() {
        mView = null
        unSubscribe()
    }
}