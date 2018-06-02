package cn.blinkdagger.androidLab.DI.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 类描述：作用域注解,来管理每个对象实例的生命周期。
 * 创建人：ls
 * 创建时间：2017/5/2
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

@Scope
@Retention(RUNTIME)
public @interface FragmentScope {
}
