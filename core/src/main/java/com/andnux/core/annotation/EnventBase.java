package com.andnux.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Shenbin on 17/6/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnventBase {
    String listenerMethod(); //set方法
    Class<?> listenerClass();//对应的接口
    String listenerCall(); //回调方法名
}
