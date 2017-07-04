package com.andnux.core.utils;

import android.view.View;

import com.andnux.core.annotation.EnventBase;
import com.andnux.core.annotation.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Shenbin on 17/6/17.
 */

public class InjectUtils {

    public  static void  inject(Object context){

        injectContentView(context);
        injectField(context);
        injectEnvent(context);
    }

    /**
     * 注入事件
     * @param context
     */
    private static void injectEnvent(Object context) {
        try {
            Class<?> aClass = context.getClass();
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                Annotation[] methodAnnotations = method.getAnnotations();
                for (Annotation annotation : methodAnnotations) {
                    Class<? extends Annotation> aClass1 = annotation.annotationType();
                    EnventBase enventBase = aClass1.getAnnotation(EnventBase.class);
                    if (enventBase == null){
                        continue;
                    }
                    String listenerCall = enventBase.listenerCall(); //call
                    Class<?> listenerClass = enventBase.listenerClass(); //class
                    String listenerMethod = enventBase.listenerMethod(); //set
                    Method value = aClass1.getMethod("value");
                    int[] values = (int[]) value.invoke(annotation);
                    for (int id : values) {
                        Method viewById = aClass.getMethod("findViewById", int.class);
                        View view = (View) viewById.invoke(context,id);
                        EnventHandler mEnventHandler = new EnventHandler(context,listenerCall,method);
                        Object instance = Proxy.newProxyInstance(aClass.getClassLoader(), new Class[]{listenerClass}, mEnventHandler);
                        Method declaredMethod = view.getClass().getMethod(listenerMethod, listenerClass);
                        declaredMethod.invoke(view,instance);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 注入控件
     * @param context
     */
    private static void injectField(Object context) {
       try {
           Class<?> aClass = context.getClass();
           Field[] fields = aClass.getDeclaredFields();
           for (Field field : fields) {
               Inject inject = field.getAnnotation(Inject.class);
               if (inject != null){
                   Method viewById = aClass.getMethod("findViewById", int.class);
                   View view = (View) viewById.invoke(context,inject.value());
                   field.setAccessible(true);
                   field.set(context,view);
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    /**
     * 注入视图
     * @param context
     */
    private static void injectContentView(Object context) {
        try {
            Class<?> aClass = context.getClass();
            Inject annotation = aClass.getAnnotation(Inject.class);
            if (annotation != null){
                Method method = aClass.getMethod("setContentView", int.class);
                if (method != null){
                    method.invoke(context,annotation.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
