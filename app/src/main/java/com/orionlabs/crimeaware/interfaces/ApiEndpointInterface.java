package com.orionlabs.crimeaware.interfaces;

import com.orionlabs.crimeaware.models.CrimeIncident;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndpointInterface {


    @GET("crimes/major")
    Call<List<CrimeIncident>> requestCrimesList(@Query("dateOccuredStart") String dateOccurentStart, @Query("dateOccuredEnd") String dateOccuredEnd, @Query("offset") long offset,@Query("max") long max);
}
