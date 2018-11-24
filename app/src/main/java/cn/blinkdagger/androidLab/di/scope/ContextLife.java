package cn.blinkdagger.androidLab.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 类描述：作用域注解
 * 创建人：ls
 * 创建时间：2017/5/2
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface ContextLife {
    String value() default "Application";
}
