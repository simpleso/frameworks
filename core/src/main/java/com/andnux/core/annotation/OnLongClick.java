package com.andnux.core.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Shenbin on 17/6/17.
 */
@EnventBase(
        listenerMethod = "setOnLongClickListener",
        listenerClass = View.OnLongClickListener.class,
        listenerCall = "onLongClick")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnLongClick {
    int [] value();
}
