package com.andnux.core.http;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Shenbin on 17/6/19.
 */

public abstract class HttpCalback <T> implements ICalback {

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Class<?> aClass = analysisClassInfo(this);
        T o = (T) gson.fromJson(result, aClass);
        onSuccess(o);
    }

    public abstract  void onSuccess(T result);

    public   Class<T> analysisClassInfo(Object obj){
        Type superclass = obj.getClass().getGenericSuperclass();
        Type[] arguments = ((ParameterizedType) superclass).getActualTypeArguments();
        return (Class<T>) arguments[0];
    }
}
