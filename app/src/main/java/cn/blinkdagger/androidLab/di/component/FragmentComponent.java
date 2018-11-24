package cn.blinkdagger.androidLab.di.component;

import android.app.Activity;
import android.support.v4.app.Fragment;


import cn.blinkdagger.androidLab.di.module.FragmentModule;
import cn.blinkdagger.androidLab.di.scope.FragmentScope;
import dagger.Component;

/**
 * 类描述：通用Fragment的注入器（将会注入Presenter）
 * 创建人：ls
 * 创建时间：2017/5/2
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    /**
     * 在具体页面（Fragment）实现此方法后，Presenter就被实例化了
     * @param dailyFragment
     */
    void inject(Fragment dailyFragment);

}
