package com.example.weatherappjetpackconpose.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlertTable")
data class Alert(
    @PrimaryKey(autoGenerate = true)
  var id :Long,
  val lat:Double,
    val lon:Double,
    val start:Double,
    val end:Double,
    val Kind:String

)