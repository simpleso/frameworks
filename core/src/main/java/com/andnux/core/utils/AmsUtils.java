package com.andnux.core.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Shenbin on 17/6/24.
 */

public class AmsUtils {

    private Class<?> proxyActivity;
    private Context context;
    private Object ActivityThreadValue;

    public AmsUtils(Class<?> proxyActivity, Context context) {
        this.proxyActivity = proxyActivity;
        this.context = context;
    }

    /**
     * 替换意图对象
     */
    public  void hookAms(){
        try {
            Log.d("andnux","start hook Ams");
            Class<?> ActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            Field gDefault = ActivityManagerNativeClass.getDeclaredField("gDefault");
            gDefault.setAccessible(true);
            Object defaultValue = gDefault.get(null);
            Class<?> SingletonClass = Class.forName("android.util.Singleton");
            Field instance = SingletonClass.getDeclaredField("mInstance");
            instance.setAccessible(true);
            Object iActivityManagerValue = instance.get(defaultValue);
            Class<?> iActivityManager = Class.forName("android.app.IActivityManager");
            AsmInvocationHandler asmInvocationHandler = new AsmInvocationHandler(iActivityManagerValue);
            Object o = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{iActivityManager}, asmInvocationHandler);
            instance.set(defaultValue,o);
            Log.d("andnux","end hook Ams");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class  AsmInvocationHandler implements InvocationHandler{

        private Object iActivityManagerValue;

        public AsmInvocationHandler(Object iActivityManagerValue) {
            this.iActivityManagerValue = iActivityManagerValue;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //偷天换日
            Log.d("andnux",method.getName());
            if ("startActivity".contains(method.getName())){
                Intent intent = null;
                int index = 0;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof  Intent){
                        intent = (Intent) args[i]; //非法
                        index = i;
                        break;
                    }
                }
                Intent proxyIntent = new Intent();
                ComponentName cmp = new ComponentName(context,proxyActivity);
                proxyIntent.setComponent(cmp);
                proxyIntent.putExtra("oldIntent",intent);
                args[index] = proxyIntent;
                return  method.invoke(iActivityManagerValue,args);
            }
            return method.invoke(iActivityManagerValue,args);
        }
    }

    public  void hookSystemHandler(){
        try {
            Class<?> ActivityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThread = ActivityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThread.setAccessible(true);
            ActivityThreadValue = sCurrentActivityThread.get(null);
            Field h = ActivityThreadClass.getDeclaredField("mH");
            h.setAccessible(true);
            Handler handler = (Handler) h.get(ActivityThreadValue);
            Field callback = Handler.class.getDeclaredField("mCallback");
            callback.setAccessible(true);
            callback.set(handler,new ActivityThreadHandler(handler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class  ActivityThreadHandler implements  Handler.Callback{

        private  Handler mHandler;

        public ActivityThreadHandler(Handler handler) {
            mHandler = handler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            //替换回来
            if (msg.what == 100){
                hadleActivityThread(msg);
            }
            mHandler.handleMessage(msg);
            return true;
        }
    }

    private void hadleActivityThread(Message msg) {
        try {
            Object obj = msg.obj;
            Field intent = obj.getClass().getDeclaredField("intent");
            intent.setAccessible(true);
            Intent  proxyIntent = (Intent) intent.get(obj);
            Intent parcelable = proxyIntent.getParcelableExtra("oldIntent");
            if (parcelable!=null){
                proxyIntent.setComponent(parcelable.getComponent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
