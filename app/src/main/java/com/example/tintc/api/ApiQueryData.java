package com.example.tintc.api;

import com.example.tintc.model.Headline;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiQueryData {

    @GET("everything")
    Call<Headline> getEverythingData(  // tra từ khóa trong tìm kiếm
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<Headline> getDataFromDomains(
            @Query("q") String q,
            @Query("domains") String query,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<Headline> getDataDomain(
            @Query("domains") String domains,
            @Query("apiKey") String apiKey
    );
}
