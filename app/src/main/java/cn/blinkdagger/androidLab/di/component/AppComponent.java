package cn.blinkdagger.androidLab.di.component;



import javax.inject.Singleton;

import cn.blinkdagger.androidLab.AndroidLabApplication;
import cn.blinkdagger.androidLab.di.module.AppModule;
import cn.blinkdagger.androidLab.di.scope.ContextLife;
import dagger.Component;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2017/5/2
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ContextLife("Application")
    AndroidLabApplication getAppContext();  // 提供App的Context

}
