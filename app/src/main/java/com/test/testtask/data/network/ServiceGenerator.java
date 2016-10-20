package com.test.testtask.data.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private final static String ROOT_URL = "http://jsonplaceholder.typicode.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder sBuilder = new Retrofit.Builder()
            .baseUrl(ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);

        Retrofit retrofit = sBuilder
                .client(httpClient.build())
                .build();
        return retrofit.create(serviceClass);
    }

}
