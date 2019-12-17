package com.zhm.http;

import okhttp3.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 接口调用类用于对外接口的使用(初级)
 * Created by 赵红明 on 2019/11/11.
 */
@Component
public class OkhttpUtil {

   private static Logger logger= LoggerFactory.getLogger(OkhttpUtil.class);

    private static OkHttpClient client;
    private static OkhttpUtil okhttpUtil;

    @PostConstruct
    public void init() {
        okhttpUtil = this;
    }
    static {
        MyOkHttpRetryInterceptor myOkHttpRetryInterceptor = new MyOkHttpRetryInterceptor.Builder()
                .executionCount(0)
                .retryInterval(1000)
                .build();
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(myOkHttpRetryInterceptor)
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(40,TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
    }

    /**
     * get方法（HttpClient）
     * @param url
     * @return
     */
    public static String getUrl(String url,String json){
        String srtResult = "";
        try{
            long d1=(new Date()).getTime();
            if(json!=null){
                url=url+ URLEncoder.encode(json);
            }
            CloseableHttpClient httpCilent2 = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)   //设置连接超时时间
                    .setConnectionRequestTimeout(5000) // 设置请求超时时间
                    .setSocketTimeout(5000)
                    .setRedirectsEnabled(true)//默认允许自动重定向
                    .build();
            HttpGet httpGet2 = new HttpGet(url);
            httpGet2.setConfig(requestConfig);
            try {
                HttpResponse httpResponse = httpCilent2.execute(httpGet2);
                logger.info("response statusCode :"+httpResponse.getStatusLine().getStatusCode());
                if(httpResponse.getStatusLine().getStatusCode() == 200){
                    srtResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
                }else if(httpResponse.getStatusLine().getStatusCode() == 400){
                    srtResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果...
                }else if(httpResponse.getStatusLine().getStatusCode() == 500){
                    srtResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
                }
                logger.info("接口返回的值："+srtResult);
                long d2=(new Date()).getTime();
                Integer txt=(int)(d2-d1);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("发送数据异常,",e.getCause());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("推送异常",e.getMessage());
        }

        return srtResult;
    }
    public static Response post(String url, Headers headers, String body, MediaType mediaType) throws IOException {
        RequestBody requestBody = RequestBody.create(mediaType, body);
        Request request = new Request.Builder().url(url).headers(headers).post(requestBody).build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response postForm(String url, Headers headers, FormBody body) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if(headers != null) {
            requestBuilder.headers(headers);
        }
        if(body != null) {
            requestBuilder.post(body);
        }
        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response get(String url, Headers headers) throws IOException {
        Request request = new Request.Builder().url(url).headers(headers).get().build();
        Response response = client.newCall(request).execute();
        return response;
    }



    public static Response get(String url, Map<String, String> params, Cookie cookie) throws IOException {
        String requestUrl = buildUrl(url, params);
        return get(requestUrl, cookie);
    }

    public static Response get(String url, Map<String, String> params, String cookieName, String cookieValue) throws IOException {
        String requestUrl = buildUrl(url, params);
        return get(requestUrl, cookieName, cookieValue);
    }

    public static String buildUrl(String url, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(url);
        if(params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            Iterator<Map.Entry<String, String>> entryIterator = params.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<String, String> entry = entryIterator.next();
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                if(entryIterator.hasNext()) {
                    urlBuilder.append("&");
                }
            }
        }
        String requestUrl = urlBuilder.toString();
        return requestUrl;
    }

    public static Response get(String url, Cookie cookie) throws IOException {
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie.name() + "=" + cookie.name()).get().build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response get(String url, String cookieName, String cookieValue) throws IOException {
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookieName + "=" + cookieValue).get().build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response postJson(String url, String body) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, body);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
