package com.example.weatherappjetpackconpose.model.dp

import com.example.weatherappjetpackconpose.model.pojo.Alert
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import kotlinx.coroutines.flow.Flow

interface LocalWeatherInterface {
  fun getAllFav():Flow<List<FavouriteWeather>>
  suspend fun insertToDataBase(favouriteWeather: FavouriteWeather)
  suspend fun deleteFromDataBase(favouriteWeather: FavouriteWeather)
  //Alert
  fun getAlert():Flow<List<Alert>>
  suspend fun insertAlert(alert: Alert)
  suspend fun deleteAlert(alert: Alert)
}