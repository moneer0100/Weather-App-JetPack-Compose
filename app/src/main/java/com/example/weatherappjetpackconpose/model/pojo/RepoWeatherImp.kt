package com.example.weatherappjetpackconpose.model.pojo

import Forecast
import com.example.weatherappjetpackconpose.model.netWork.WeatherRemteInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoWeatherImp @Inject constructor(private val weatherInterface: WeatherRemteInterface):RepoWeatherInterface {
    override suspend fun getWeatherCurrent(
        lat: Double?,
        long: Double?,
        language: String,
        units: String?
    ): Flow<CurrentForcast> {
        return  flowOf(weatherInterface.getcurrent(lat,long,language,units))
    }

    override suspend fun getWeatherForecast(
        lat: Double?,
        long: Double?,
        language: String,
        units: String?
    ): Flow<Forecast> {
        return flowOf( weatherInterface.getWeatherForcast(lat,long,language,units))
    }
}