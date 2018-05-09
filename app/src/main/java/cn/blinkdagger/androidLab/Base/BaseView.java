package cn.blinkdagger.androidLab.Base;

/**
 * 类描述：基本视图
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public interface BaseView {

    /**
     * 显示加载失败
     * @param msg
     */
    void showError(String msg);

    /**
     * 显示加载中
     * @param msg
     */
    void showLoading(String msg);

    /**
     * 显示数据为空
     * @param msg
     */
    void showEmpty(String msg);

    /**
     * 显示没有网络连接
     * @param msg
     */
    void showNoNetwork(String msg);

}
