package com.andnux.core.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Shenbin on 17/6/17.
 */

public class EnventHandler implements InvocationHandler {
    private  Object mObject;
    private  String mString;
    private  Method mMethod;

    public EnventHandler(Object object, String string, Method method) {
        mObject = object;
        mString = string;
        mMethod = method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (mString.equals(method.getName())){
            return mMethod.invoke(mObject,args);
        }else {
            return method.invoke(proxy,args);
        }
    }
}
