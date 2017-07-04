package com.andnux.core.utils;

import android.graphics.Bitmap;

/**
 * Created by Shenbin on 17/6/22.
 */

public class ImageUtils {

    static {
        System.loadLibrary("jpeg");
    }

    public static native int cjpegMain(int argc, String[] argv);

    public static native int djpegMain(int argc, String[] argv);

    public  static  native void compress(Bitmap bitmap,int quality,int w,int h,String path,boolean optimize);

}
