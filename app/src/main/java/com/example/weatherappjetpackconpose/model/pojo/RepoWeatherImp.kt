package com.example.weatherappjetpackconpose.model.pojo

import Forecast
import com.example.weatherappjetpackconpose.model.dp.LocalWeatherInterface
import com.example.weatherappjetpackconpose.model.netWork.WeatherRemteInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoWeatherImp @Inject constructor(
    private val weatherInterface: WeatherRemteInterface
,private val localWeatherInterface: LocalWeatherInterface)
    :RepoWeatherInterface {
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
        //Fav
    override fun getAllFav(): Flow<List<FavouriteWeather>> {
     return localWeatherInterface.getAllFav()
    }

    override suspend fun insertToDataBase(favouriteWeather: FavouriteWeather) {
    return localWeatherInterface.insertToDataBase(favouriteWeather)
    }

    override suspend fun deleteFromDataBase(favouriteWeather: FavouriteWeather) {
    return localWeatherInterface.deleteFromDataBase(favouriteWeather)
    }
    //Alert
    override fun getAlert(): Flow<List<Alert>> {
    return localWeatherInterface.getAlert()
    }

    override suspend fun insertAlert(alert: Alert) {
        return localWeatherInterface.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        return localWeatherInterface.deleteAlert(alert)
    }
}