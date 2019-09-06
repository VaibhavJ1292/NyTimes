package com.nytimes.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.nytimes.BuildConfig;
import com.nytimes.util.NetworkUtils;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RetrofitClientHelper {
    private static final String HEADER_KEY_AUTH = "Authorization";

    private static RetrofitClientHelper mRetrofitClientHolder;
    private Context mContext;
    private OkHttpClient httpClient;
    private NetWorkService businessNetworkService;

    public static RetrofitClientHelper getInstance() {
        if (mRetrofitClientHolder == null) {
            synchronized (RetrofitClientHelper.class) {
                if (mRetrofitClientHolder == null) {
                    mRetrofitClientHolder = new RetrofitClientHelper();
                }
            }
        }
        return mRetrofitClientHolder;
    }

    public NetWorkService getNetworkService() {
        return businessNetworkService;
    }

    public void setContext(Context context) {
        mContext = context;
        Retrofit customerRetrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient(mContext))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        businessNetworkService = customerRetrofit.create(NetWorkService.class);
    }

    /**
     * To create OkHttp Client add cache for offline mode storage
     * @param mContext -Context
     * @return - OkHttpClient
     */
    private OkHttpClient getHttpClient(Context mContext) {
        if (httpClient == null) {
            final int CONNECT_TIMEOUT = 60;
            final int READ_TIMEOUT = 60;

            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.addInterceptor(new Interceptor());
            builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);

            long cacheSize = (5 * 1024 * 1024);
            Cache cache = new Cache(mContext.getCacheDir(), cacheSize);
            builder.cache(cache);
            builder.addInterceptor(chain -> {
                Request request = chain.request();

                if (NetworkUtils.isNetworkConnected(mContext)) {
                    request = request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build();
                } else {
                    request = request.newBuilder().header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build();
                }
                return chain.proceed(request);
            });
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            }

            httpClient = builder.build();
        }
        return httpClient;
    }

    /**
     * To check header authorization in intercept request
     */
    private class Interceptor implements okhttp3.Interceptor {
        @Override
        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
            Request oldRequest = chain.request();
            Request.Builder newRequestBuilder = oldRequest.newBuilder();
            if (oldRequest.header(HEADER_KEY_AUTH) != null) {
                newRequestBuilder.removeHeader(HEADER_KEY_AUTH);
            } else {
                newRequestBuilder.addHeader("Authorization", " Bearer ");
            }

            okhttp3.Response response = chain.proceed(newRequestBuilder.build());

            if (BuildConfig.DEBUG) {
                Log.d(RetrofitClientHelper.class.getSimpleName(), response.peekBody(1024).string());
            }

            return response;
        }
    }
}
