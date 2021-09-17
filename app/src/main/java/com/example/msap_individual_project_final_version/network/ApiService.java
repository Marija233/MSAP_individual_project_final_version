package com.example.msap_individual_project_final_version.network;

import java.util.Map;

import com.example.msap_individual_project_final_version.model.EarthquakeBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("/fdsnws/event/1/query?")
    Call<EarthquakeBean> getEarthQuakes(
            @Query("format") String format,
            @Query("minmagnitude") String minMagnitude,
            @Query("maxmagnitude") String maxMagnitude);

    @GET("/fdsnws/event/1/query?")
    Call<EarthquakeBean> getEarthQuakes(@QueryMap Map<String,String> parameters);

}