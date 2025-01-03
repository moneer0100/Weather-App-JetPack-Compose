package com.example.weatherappjetpackconpose.model.netWork

import Forecast
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeatherCurrent(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("lang") language: String?,
        @Query("units") units: String?,
        @Query("appid") apikey:String="01d2594caa518733be3b09877d6712ad"

    ): CurrentForcast
    @GET("forecast")
    suspend fun getweatherForcast(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("lang") language: String?,
        @Query("units") units: String?,
        @Query("appid") apikey:String="01d2594caa518733be3b09877d6712ad"
    ):Forecast
}