package com.andnux.core.fix;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Shenbin on 17/6/15.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Relpace {
    String clazz(); //类名
    String method(); //方法名
//    String [] parameter(); //参数全类名
}
