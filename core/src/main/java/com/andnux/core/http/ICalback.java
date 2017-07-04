package com.andnux.core.http;

/**
 * Created by Shenbin on 17/6/19.
 */

public interface ICalback {

    void  onSuccess(String result);

    void  onFailed(String error);
}
