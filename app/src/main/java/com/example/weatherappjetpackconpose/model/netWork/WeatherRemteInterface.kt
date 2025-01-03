package com.example.weatherappjetpackconpose.model.netWork

import Forecast
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast

interface WeatherRemteInterface {
suspend fun getcurrent(
    lat:Double?,
    long: Double?,
    language: String?,
    units: String?
):CurrentForcast
suspend fun getWeatherForcast(
    lat:Double?,
    long: Double?,
    language: String?,
    units: String?
):Forecast
}