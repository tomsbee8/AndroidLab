package cn.blinkdagger.androidLab.base

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
interface BasePresenter<T : BaseView?> {
    fun attachView(view: T)
    fun detachView()
}