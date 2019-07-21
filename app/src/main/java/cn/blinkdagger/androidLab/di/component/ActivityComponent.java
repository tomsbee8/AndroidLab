package cn.blinkdagger.androidLab.di.component;

import android.app.Activity;


import cn.blinkdagger.androidLab.di.module.ActivityModule;
import cn.blinkdagger.androidLab.di.scope.ActivityScope;
import dagger.Component;

/**
 * 类描述：通用Activity的注入器（将会注入Presenter）
 * 创建人：ls
 * 创建时间：2017/5/2
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    /**
     * 在具体页面（Activity）调用此方法后，Presenter就被实例化了
     *
     * @param detailActivity
     */
    void inject(Activity detailActivity);



}
