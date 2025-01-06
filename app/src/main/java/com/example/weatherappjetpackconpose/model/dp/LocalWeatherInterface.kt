package com.example.weatherappjetpackconpose.model.dp

import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import kotlinx.coroutines.flow.Flow

interface LocalWeatherInterface {
  fun getAllFav():Flow<List<FavouriteWeather>>
  suspend fun insertToDataBase(favouriteWeather: FavouriteWeather)
  suspend fun deleteFromDataBase(favouriteWeather: FavouriteWeather)
}