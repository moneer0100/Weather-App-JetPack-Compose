package com.example.weatherappjetpackconpose.model.dp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherappjetpackconpose.model.pojo.Alert
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    //favourit
    @Query("SELECT * FROM favorite")
    fun getFavoriteWeather(): Flow<List<FavouriteWeather>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(product: FavouriteWeather)
    @Delete
    suspend fun deleteWeather(product: FavouriteWeather)
    ///Alert
    @Query("SELECT * FROM AlertTable")
    fun getAlert():Flow<List<Alert>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlert(alert: Alert)
    @Delete
    suspend fun deleteAlert(alert: Alert)
}