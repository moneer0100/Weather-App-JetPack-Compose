package com.example.weatherappjetpackconpose.model.dp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Database(entities = [FavouriteWeather::class], version = 1)
abstract class AppDataBase:RoomDatabase(){
abstract fun weatherApp():Dao

}
@Module
@InstallIn(SingletonComponent::class)
object DataBaseClient{
    @Volatile
 private var instance:AppDataBase?=null
@Singleton
@Provides
 fun getInstance(
    @ApplicationContext
    context: Context):AppDataBase{

    return instance?: synchronized(this){
        instance?:Room.databaseBuilder(context
            ,AppDataBase::class.java
            ,"favWeather")
            .build().also { instance=it }

    }
}
    @Singleton
    @Provides
    fun provideDao(database: AppDataBase): Dao {
        return database.weatherApp()
    }
}