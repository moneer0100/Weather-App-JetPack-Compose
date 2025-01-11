package com.example.weatherappjetpackconpose.model.dp

import com.example.weatherappjetpackconpose.model.pojo.Alert
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalWeatherImp @Inject constructor(private val dao: Dao):LocalWeatherInterface {
    override fun getAllFav(): Flow<List<FavouriteWeather>> {
       return dao.getFavoriteWeather()
    }

    override suspend fun insertToDataBase(favouriteWeather: FavouriteWeather) {
       dao.insertWeather(favouriteWeather)
    }

    override suspend fun deleteFromDataBase(favouriteWeather: FavouriteWeather) {
      dao.deleteWeather(favouriteWeather)
    }

    override fun getAlert(): Flow<List<Alert>> {
        return dao.getAlert()
    }

    override suspend fun insertAlert(alert: Alert) {
      dao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
      dao.deleteAlert(alert)
    }
}