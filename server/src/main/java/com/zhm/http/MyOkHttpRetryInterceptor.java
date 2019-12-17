package com.zhm.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * 拦截http请求，对失败的请求进行重试
 *
 * @Description
 * @Author bob
 * @Date 2018/7/09 9:37
 **/
public class MyOkHttpRetryInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(MyOkHttpRetryInterceptor.class);

    public int executionCount;//最大重试次数
    private long retryInterval;//重试的间隔

    MyOkHttpRetryInterceptor(Builder builder) {
        this.executionCount = builder.executionCount;
        this.retryInterval = builder.retryInterval;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int retryNum = 0;
        while (!response.isSuccessful() && retryNum <= executionCount) {
            logger.info("intercept Request is not successful - {}", retryNum);
            System.out.println("重发了" + retryNum);
            final long nextInterval = getRetryInterval();
            try {
                logger.info("Wait for {}", nextInterval);
                Thread.sleep(nextInterval);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedIOException();
            }
            retryNum++;
            // retry the request
            response = chain.proceed(request);
        }
        return response;
    }

    /**
     * retry间隔时间
     */
    public long getRetryInterval() {
        return this.retryInterval;
    }

    public static final class Builder {
        private int executionCount;
        private long retryInterval;

        public Builder() {
            executionCount = 3;
            retryInterval = 1000;
        }

        public MyOkHttpRetryInterceptor.Builder executionCount(int executionCount) {
            this.executionCount = executionCount;
            return this;
        }

        public MyOkHttpRetryInterceptor.Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public MyOkHttpRetryInterceptor build() {
            return new MyOkHttpRetryInterceptor(this);
        }
    }

}