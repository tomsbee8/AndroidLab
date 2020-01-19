package cn.blinkdagger.androidLab.base

/**
 * 类描述：基本视图
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
interface BaseView {
    /**
     * 显示加载失败
     * @param msg
     */
    fun showError(msg: String?)

    /**
     * 显示加载中
     * @param msg
     */
    fun showLoading(msg: String?)

    /**
     * 显示数据为空
     * @param msg
     */
    fun showEmpty(msg: String?)

    /**
     * 显示没有网络连接
     * @param msg
     */
    fun showNoNetwork(msg: String?)
}