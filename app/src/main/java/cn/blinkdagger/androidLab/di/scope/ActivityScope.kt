package cn.blinkdagger.androidLab.di.scope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

/**
 * 类描述：作用域注解,来管理每个对象实例的生命周期。
 * 创建人：ls
 * 创建时间：2017/5/2
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class ActivityScope