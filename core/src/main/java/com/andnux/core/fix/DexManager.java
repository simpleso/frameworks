package com.andnux.core.fix;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;

/**
 * Created by Shenbin on 17/6/15.
 */

public class DexManager {

    static {
        System.loadLibrary("fix");
    }

    private Context mContext;
    private int versionCode = -1;

    public DexManager(Context context) {
       this.mContext = context;
       versionCode = Build.VERSION.SDK_INT;
    }

    public  void  loadDex(File dex){
        try {
            String opt = new File(mContext.getCacheDir(), "opt").getAbsolutePath();
            DexFile dexFile = DexFile.loadDex(dex.getAbsolutePath(),opt,Context.MODE_PRIVATE);
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()){
                String className = entries.nextElement();
                Log.d("DexManager", "className = " + className);
                Class fixClass = dexFile.loadClass(className,mContext.getClassLoader());
                fixClass(fixClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fixClass(Class fixClass) {
        try {
        Method[] methods = fixClass.getDeclaredMethods();
            for (Method m : methods) {
                Relpace relpace = m.getAnnotation(Relpace.class);
                if (relpace == null){
                    continue;
                }
                String wrongClazz = relpace.clazz();
                String wrongMethod = relpace.method();
//                String[] parameter = relpace.parameter();
                Class wrongClass  = Class.forName(wrongClazz);
                Method method = wrongClass.getMethod(wrongMethod, m.getParameterTypes());
                Log.d("andnux","method = "+ method);
                Log.d("andnux","m = "+ m);
                fixNative(versionCode,method,m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public native void fixNative(int vercode,Method wrongmethod, Method method);

    private Class [] toClass(String [] strings) throws ClassNotFoundException {
        Class [] classes = new Class[strings.length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = Class.forName(strings[i]);
        }
        return classes;
    }
}
