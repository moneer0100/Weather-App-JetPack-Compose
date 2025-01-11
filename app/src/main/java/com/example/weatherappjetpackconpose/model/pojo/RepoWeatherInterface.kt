package com.example.weatherappjetpackconpose.model.pojo

import Forecast
import kotlinx.coroutines.flow.Flow

interface RepoWeatherInterface {
suspend fun getWeatherCurrent(
    lat:Double?,
    long: Double?,
    language: String,
    units: String?
): Flow<CurrentForcast>
suspend fun getWeatherForecast(
    lat:Double?,
    long: Double?,
    language: String,
    units: String?
):Flow<Forecast>
//fav
fun getAllFav():Flow<List<FavouriteWeather>>
suspend fun insertToDataBase(favouriteWeather: FavouriteWeather)
suspend fun deleteFromDataBase(favouriteWeather: FavouriteWeather)
    //Alert
    fun getAlert():Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)
}