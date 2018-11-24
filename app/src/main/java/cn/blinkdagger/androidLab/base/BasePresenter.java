package cn.blinkdagger.androidLab.base;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2016/11/22
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();
}
