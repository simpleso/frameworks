package com.andnux.core.http;

import java.util.Map;

/**
 * Created by Shenbin on 17/6/19.
 */

public interface IHttpProxy {

    void  get(String url, Map<String,Object> paramet, final ICalback calback);

    void  post(String url, Map<String,Object> paramet,final ICalback calback);

}
