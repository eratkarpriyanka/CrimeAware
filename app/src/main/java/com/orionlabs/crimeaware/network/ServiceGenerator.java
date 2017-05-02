package com.orionlabs.crimeaware.network;


import com.orionlabs.crimeaware.interfaces.ApiEndpointInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    // Trailing slash is needed
    public static final String BASE_URL = "http://api1.chicagopolice.org/clearpath/api/1.0/";

    public static ApiEndpointInterface createService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpointInterface apiService = retrofit.create(ApiEndpointInterface.class);
        return apiService;
    }
}
