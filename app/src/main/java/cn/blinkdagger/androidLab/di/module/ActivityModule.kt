package cn.blinkdagger.androidLab.di.module

import android.app.Activity
import cn.blinkdagger.androidLab.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * 类描述：Module类（里面的方法专门提供依赖）
 * 创建人：ls
 * 创建时间：2017/5/2
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
@Module
class ActivityModule(private val mActivity: Activity) {
    @Provides
    @ActivityScope
    fun provideActivity(): Activity {
        return mActivity
    }

}