package com.andnux.core.utils;

import android.graphics.Bitmap;

/**
 * Created by Shenbin on 17/6/28.
 */

public class GifHandder {

    static {
        System.loadLibrary("gif");
    }

    private  long gifPoint;

    private GifHandder( long gifPoint) {
        this.gifPoint = gifPoint;
    }

    public  static native long loadGif(String path);
    public  static native int  getWidth(long point);
    public  static native int  getHeight(long point);
    public  static native int  getNextTime(long point);
    public  static native int  updateFrame(long point, Bitmap bitmap);

    public long getGifPoint() {
        return gifPoint;
    }

    public  static GifHandder load(String path){
        long gifHander = loadGif(path);
        GifHandder handder = new GifHandder(gifHander);
        return  handder;
    }
}
