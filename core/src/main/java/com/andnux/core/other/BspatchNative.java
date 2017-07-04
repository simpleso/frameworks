package com.andnux.core.other;

/**
 * Created by Shenbin on 17/6/11.
 */

public class BspatchNative {

    static {
        System.loadLibrary("bspatch");
    }

    public native int bspatch(String oldfile ,String newfile ,String patchfile);

}
