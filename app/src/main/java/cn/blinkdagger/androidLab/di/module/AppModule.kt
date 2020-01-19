package cn.blinkdagger.androidLab.di.module

import cn.blinkdagger.androidLab.AndroidLabApplication
import cn.blinkdagger.androidLab.di.scope.ContextLife
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * 类描述：Module类（里面的方法专门提供依赖）
 * 创建人：ls
 * 创建时间：2017/5/2
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
@Module
class AppModule(private val application: AndroidLabApplication) {
    @Provides
    @Singleton
    @ContextLife("Application")
    fun provideApplicationContext(): AndroidLabApplication {
        return application
    }

}