package com.andnux.core.http.impl;

import android.os.Handler;
import android.util.Log;

import com.andnux.core.http.ICalback;
import com.andnux.core.http.IHttpProxy;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Shenbin on 17/6/19.
 */

public class OkhttpProxy implements IHttpProxy {

    private static final String TAG = "OkhttpProxy";
    private final long DEFAULT_TIMEOUT = 10;
    private Handler mHandler;
    private OkHttpClient mClient;

    public OkhttpProxy() {
        mClient = new OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .sslSocketFactory(createSSLSocketFactory())
        .hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).build();
        mHandler = new Handler();
    }

    public class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    @Override
    public void get(String url, Map<String, Object> paramet, final ICalback calback) {
        final Request req = new Request.Builder()
                .get()
                .url(url)
                .addHeader("User-Agent","user")
                .build();
        mClient.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (calback != null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            calback.onFailed(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                if (response.isSuccessful()){
                    if (calback != null) {
                        try {
                            final String string = response.body().string();
                            Log.d(TAG, "response " + string);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    calback.onSuccess(string);
                                }
                            });
                        } catch (final IOException e) {
                            e.printStackTrace();
                          mHandler.post(new Runnable() {
                              @Override
                              public void run() {
                                  calback.onFailed(e.getMessage());
                              }
                          });
                        }
                    }
                }else {
                    if (calback != null){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                calback.onFailed(response.message());
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void post(String url, Map<String, Object> paramet,  final ICalback calback) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (String s : paramet.keySet()) {
            builder.addFormDataPart(s,paramet.get(s).toString());
        }
        Request req = new Request.Builder()
                .post(builder.build())
                .url(url)
                .addHeader("User-Agent","user")
                .build();
        mClient.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (calback != null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            calback.onFailed(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                if (response.isSuccessful()){
                    if (calback != null) {
                        try {
                            final String string = response.body().string();
                            Log.d(TAG, "response " + string);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    calback.onSuccess(string);
                                }
                            });
                        } catch (final IOException e) {
                            e.printStackTrace();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    calback.onFailed(e.getMessage());
                                }
                            });
                        }
                    }
                }else {
                    if (calback != null){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                calback.onFailed(response.message());
                            }
                        });
                    }
                }
            }
        });
    }
}
