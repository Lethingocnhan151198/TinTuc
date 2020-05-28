package com.example.tintc.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL ="https://newsapi.org/v2/";
    private static ApiClient apiClient;
    private static Retrofit retrofit;

    private ApiClient(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)        // using retrofit call Api
                .addConverterFactory(GsonConverterFactory.create()) // Using Gson convert data from WebClient
                .build();
    }
    public  static  synchronized ApiClient getInstance(){  // viết lớp singleton để sử dụng nhiều lần
        if(apiClient == null){
            apiClient = new ApiClient();
        }
        return apiClient;
    }
    public ApiQueryData getData(){
        return retrofit.create(ApiQueryData.class);
    }

}
