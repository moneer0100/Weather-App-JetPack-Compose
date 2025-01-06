package com.example.weatherappjetpackconpose.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName="favorite")
data class FavouriteWeather(
    @PrimaryKey(autoGenerate = true)
    var id:Long
    , val address:String,
    val lat:Double,
    var lon:Double

    )