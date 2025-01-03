package com.example.weatherappjetpackconpose.model.netWork

import Forecast
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast
import javax.inject.Inject
import javax.inject.Singleton


class WeatherRemoteImp @Inject constructor(private val apiService: ApiService):WeatherRemteInterface {
    override suspend fun getcurrent(
        lat: Double?,
        long: Double?,
        language: String?,
        units: String?
    ): CurrentForcast {
        return apiService.getWeatherCurrent(lat,long,language,units)
    }
    override suspend fun getWeatherForcast(
        lat: Double?,
        long: Double?,
        language: String?,
        units: String?
    ): Forecast {
        return apiService.getweatherForcast(lat,long,language,units)
    }
}