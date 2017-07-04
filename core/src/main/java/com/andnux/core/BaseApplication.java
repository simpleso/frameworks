package com.andnux.core;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.andnux.core.activity.AmsProxyActivity;
import com.andnux.core.http.HttpHelper;
import com.andnux.core.http.impl.VolleyProxy;
import com.andnux.core.other.CrashHelper;
import com.andnux.core.skin.SkinManager;
import com.andnux.core.utils.AmsUtils;
import com.andnux.core.utils.FileUtils;

import java.io.File;

/**
 * Created by Shenbin on 17/6/15.
 */

public class BaseApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
        AmsUtils utils =new AmsUtils(AmsProxyActivity.class,this);
        utils.hookAms();
        utils.hookSystemHandler();
        HttpHelper.getInstance().init(new VolleyProxy(this));
        CrashHelper helper = CrashHelper.getCrashHelper();
        String path = FileUtils.getSDCardRoot()+ File.separator+getPackageName();
        helper.init(this, path);
    }
}
