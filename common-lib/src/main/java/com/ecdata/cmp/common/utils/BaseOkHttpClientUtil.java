package com.ecdata.cmp.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.error.HttpUtilServiceException;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ecdata.cmp.common.enums.ResultEnum.FAIL_HTTP_UTIL_REQUEST;

/**
 * @author xuxinsheng
 * @since 2019-09-18
 */
@Slf4j
@Component
public final class BaseOkHttpClientUtil {

    private BaseOkHttpClientUtil() {

    }

    /**
     * sslContext
     */
    private static SSLContext sslContext = null;

    /**
     * 超时时间
     */
    private static final long TIMEOUT = 60000L;

    /**
     * 忽略所有https证书
     */
    private static void overlookCard(X509TrustManager trustManager) {
        final TrustManager[] trustAllCerts = new TrustManager[]{trustManager};
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            log.error("ssl出现异常" + e);
        }

    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        overlookCard(trustManager);
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLogInterceptor())
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .hostnameVerifier((hostname, session) -> true).build();
    }


    /**
     * get方法
     *
     * @param url      url
     * @param username 用户名
     * @param password 密码
     * @return 结果信息
     * @throws IOException io异常
     */
    public static String get(String url, String username, String password) throws IOException {
        String auth = username + ":" + password;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", String.format("Basic %s", new String(Base64.getEncoder().encode(auth.getBytes()))))
                .addHeader("cache-control", "no-cache")
                .build();
        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * get方法
     *
     * @param url    url
     * @param header 头信息
     * @return 返回结果
     * @throws IOException io异常
     */
    public static String get(String url, Map<String, String> header) throws IOException {

        Request.Builder builder = new Request.Builder().url(url).get();

        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new IOException("Unexpected code " + response);
        }
    }

    public static String getWithCookie(String url, Map<String, String> params, CookieJar cookieJar) throws Exception {
        url += "?" + Joiner.on("&").withKeyValueSeparator("=").join(params);
        Request.Builder builder = new Request.Builder().url(url).get();
        log.info("url{}",url);
        Request request = builder.build();

        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        overlookCard(trustManager);


        Call call = new OkHttpClient.Builder()
                .addInterceptor(new HttpLogInterceptor())
                .followRedirects(false).followSslRedirects(false).cookieJar(cookieJar)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .hostnameVerifier((hostname, session) -> true)
                .build().newCall(request);

        Response response = call.execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("invoke error, url={}, body={}", url, body);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * native get方法
     *
     * @param url    url
     * @param header 头信息
     * @return 返回结果
     * @throws IOException io异常
     */
    public static Response nativeGet(String url, Map<String, String> header) throws IOException {
        Request.Builder builder = new Request.Builder().url(url).get();

        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        return getUnsafeOkHttpClient().newCall(request).execute();
    }

    /**
     * post方法
     *
     * @param url          url
     * @param params       请求JSON参数
     * @param header       头信息
     * @return 返回信息
     * @throws IOException io异常
     */
    public static String post(String url, JSONObject params, Map<String, String> header) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody formBody = RequestBody.create(mediaType, params.toJSONString());

        Request.Builder builder = new Request.Builder().url(url).post(formBody);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            //addHeader("cache-control", "no-cache")
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new HttpUtilServiceException(FAIL_HTTP_UTIL_REQUEST, "Unexpected code " + response.message());
        }
    }

    /**
     * @param url      url
     * @param reqParam key-value参数
     * @param header   请求头
     * @return 返回信息
     * @throws IOException io异常
     */
    public static String post(String url, Map<String, String> reqParam, Map<String, String> header) throws IOException {

        //添加参数
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : reqParam.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = builder.build();
        //添加Header
        Request.Builder requestBuilder = new Request.Builder().url(url).post(formBody);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            //addHeader("cache-control", "no-cache")
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();

        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * post方法
     *
     * @param url          url
     * @param bodyStr      body字符串
     * @param header       头信息
     * @param mediaTypeStr 媒体类型字符串
     * @return 返回信息
     * @throws IOException io异常
     */
    public static String post(String url, String bodyStr, Map<String, String> header, String mediaTypeStr) throws IOException {
        MediaType mediaType = MediaType.parse(mediaTypeStr);
        RequestBody formBody = RequestBody.create(mediaType, bodyStr);

        Request.Builder builder = new Request.Builder().url(url).post(formBody);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            //addHeader("cache-control", "no-cache")
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * post方法
     *
     * @param url          url
     * @param bodyStr      body字符串
     * @param mediaTypeStr 媒体类型字符串
     * @return 返回信息
     * @throws IOException io异常
     */
    public static Response post(String url, String bodyStr, String mediaTypeStr) throws IOException {
        MediaType mediaType = MediaType.parse(mediaTypeStr);
        RequestBody formBody = RequestBody.create(mediaType, bodyStr);

        Request.Builder builder = new Request.Builder().url(url).post(formBody);
        Request request = builder.build();
        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        if (response.isSuccessful()) {
            return response;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, response);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * post方法获取response
     *
     * @param url          url
     * @param bodyStr      body字符串
     * @param mediaTypeStr 媒体类型字符串
     * @return 返回信息
     * @throws IOException io异常
     */
    public static Response postResponse(String url, String bodyStr, Map<String, String> header, String mediaTypeStr) throws IOException {
        MediaType mediaType = MediaType.parse(mediaTypeStr);
        RequestBody formBody = RequestBody.create(mediaType, bodyStr);

        Request.Builder builder = new Request.Builder().url(url).post(formBody);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            //addHeader("cache-control", "no-cache")
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        if (response.isSuccessful()) {
            return response;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, response);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * delete
     * @param url       url
     * @param header    头信息
     * @return 返回信息
     * @throws IOException io异常
     */
    public static String delete(String url, Map<String, String> header) throws IOException {
        //添加Header
        Request.Builder requestBuilder = new Request.Builder().url(url).delete();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            //addHeader("cache-control", "no-cache")
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();

        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new HttpUtilServiceException(FAIL_HTTP_UTIL_REQUEST, "Unexpected code " + response.message());
        }
    }

    /**
     * delete
     * @param url       url
     * @param params    json参数
     * @param header    头信息
     * @return  返回信息
     * @throws IOException  io异常
     */
    public static String delete(String url, JSONObject params, Map<String, String> header) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody formBody = RequestBody.create(mediaType, params.toJSONString());

        //添加Header
        Request.Builder requestBuilder = new Request.Builder().url(url).delete(formBody);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            //addHeader("cache-control", "no-cache")
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();

        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * put
     * @param url       url
     * @param reqParam  请求参数
     * @param header    头信息
     * @return  返回信息
     * @throws IOException  io异常
     */
    public static String put(String url, JSONObject reqParam, Map<String, String> header) throws IOException {
        MediaType json = MediaType.parse("application/json");
        RequestBody formBody = RequestBody.create(json, String.valueOf(reqParam));

        Request.Builder builder = new Request.Builder().url(url).put(formBody);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            //addHeader("cache-control", "no-cache")
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new HttpUtilServiceException(FAIL_HTTP_UTIL_REQUEST, "Unexpected code " + response.message());
        }
    }

    /**
     * put
     * @param url       url
     * @param file      文件
     * @param header    头信息
     * @return  返回信息
     * @throws IOException  io异常
     */
    public static String put(String url, File file, Map<String, String> header) throws IOException {

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody fileBody = RequestBody.create(mediaType, file);

        Request.Builder builder = new Request.Builder().url(url).put(fileBody);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * put方法
     *
     * @param url          url
     * @param bodyStr      body字符串
     * @param header       头信息
     * @param mediaTypeStr 媒体类型字符串
     * @return 返回信息
     * @throws IOException io异常
     */
    public static String put(String url, String bodyStr, Map<String, String> header, String mediaTypeStr) throws IOException {
        MediaType mediaType = MediaType.parse(mediaTypeStr);
        RequestBody formBody = RequestBody.create(mediaType, bodyStr);

        //添加Header
        Request.Builder requestBuilder = new Request.Builder().url(url).put(formBody);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            //addHeader("cache-control", "no-cache")
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();

        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * patch方法
     *
     * @param url          url
     * @param bodyStr      body字符串
     * @param header       头信息
     * @param mediaTypeStr 媒体类型字符串
     * @return 返回信息
     * @throws IOException io异常
     */
    public static String patch(String url, String bodyStr, Map<String, String> header, String mediaTypeStr) throws IOException {
        MediaType mediaType = MediaType.parse(mediaTypeStr);
        RequestBody formBody = RequestBody.create(mediaType, bodyStr);

        //添加Header
        Request.Builder requestBuilder = new Request.Builder().url(url).patch(formBody);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            //addHeader("cache-control", "no-cache")
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();

        Response response = getUnsafeOkHttpClient().newCall(request).execute();
        String body = response.body() != null ? response.body().string() : "";
        if (response.isSuccessful()) {
            return body;
        } else {
            log.error("调用{}异常, 返回信息:{}", url, body);
            throw new IOException("Unexpected code " + response);
        }
    }

}
