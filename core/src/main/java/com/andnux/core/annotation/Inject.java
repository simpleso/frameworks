package com.andnux.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Shenbin on 17/6/17.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    int value();
}
