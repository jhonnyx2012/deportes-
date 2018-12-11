package com.core.data.remote;

import com.core.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jhonnybarrios on 01-09-17
 */

public class ApiServiceFactory {
    public static <T> T build(OkHttpClient client, Class<T> serviceClass,String urlBase) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlBase)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    public static OkHttpClient buildOkHttpClient(Interceptor... interceptors ) {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        //builder.readTimeout(10, TimeUnit.SECONDS);
        //builder.connectTimeout(5, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        return  builder.build();
    }
}