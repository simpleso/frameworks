package com.andnux.core.http;

import java.util.Map;

/**
 * Created by Shenbin on 17/6/19.
 */

public class HttpHelper {

    private static final HttpHelper ourInstance = new HttpHelper();

    private  IHttpProxy mProxy;

    public static HttpHelper getInstance() {
        return ourInstance;
    }

    private HttpHelper() {
    }

    public  void init(IHttpProxy httpProxy){
        this.mProxy = httpProxy;
    }

    public void  get(String url, Map<String,Object> paramet, ICalback calback){
        if (mProxy == null){
            new RuntimeException("you must call HttpHelper.getInstance().init(IHttpProxy httpProxy)");
            return;
        }
        String urlParamet = appendUrlParamet(url,paramet);
        mProxy.get(urlParamet,null,calback);
    }

    private String appendUrlParamet(String url, Map<String, Object> paramet) {
        StringBuffer buff = new StringBuffer(url);
        if (paramet == null){
            return buff.toString();
        }
        buff.append("?");
        for (String s : paramet.keySet()) {
            buff.append(s);
            buff.append("=");
            buff.append(paramet.get(s));
        }
        return buff.toString();
    }

    public void  post(String url, Map<String,Object> paramet,ICalback calback){
        if (mProxy == null){
            new RuntimeException("you must call HttpHelper.getInstance().init(IHttpProxy httpProxy)");
            return;
        }
        mProxy.post(url,paramet,calback);
    }
}
