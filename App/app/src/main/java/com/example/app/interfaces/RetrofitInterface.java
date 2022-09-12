package com.example.app.interfaces;

import com.example.app.data.LocationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("maps/api/geocode/json")
    Call<LocationResponse> getGoogleLocation
            (        @Query("latlng") String latlng,
                     @Query("key") String key);
}
