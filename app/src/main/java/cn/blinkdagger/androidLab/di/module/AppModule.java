package cn.blinkdagger.androidLab.di.module;




import javax.inject.Singleton;

import cn.blinkdagger.androidLab.AndroidLabApplication;
import cn.blinkdagger.androidLab.di.scope.ContextLife;
import dagger.Module;
import dagger.Provides;

/**
 * 类描述：Module类（里面的方法专门提供依赖）
 * 创建人：ls
 * 创建时间：2017/5/2
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

@Module
public class AppModule {
    private final AndroidLabApplication application;

    public AppModule(AndroidLabApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @ContextLife("Application")
    AndroidLabApplication provideApplicationContext() {
        return application;
    }

}
