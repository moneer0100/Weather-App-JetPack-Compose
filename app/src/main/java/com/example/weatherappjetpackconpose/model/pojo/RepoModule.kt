package com.example.weatherappjetpackconpose.model.pojo

import com.example.weatherappjetpackconpose.model.dp.Dao
import com.example.weatherappjetpackconpose.model.dp.LocalWeatherImp
import com.example.weatherappjetpackconpose.model.dp.LocalWeatherInterface
import com.example.weatherappjetpackconpose.model.netWork.ApiService
import com.example.weatherappjetpackconpose.model.netWork.WeatherRemoteImp
import com.example.weatherappjetpackconpose.model.netWork.WeatherRemteInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Singleton
    @Provides
    fun provideWeatherRemoteImp(apiService: ApiService): WeatherRemteInterface {
        return WeatherRemoteImp(apiService)
    }
    @Singleton
    @Provides
    fun provideWeatherLocalImp(dao: Dao):LocalWeatherInterface{
return LocalWeatherImp(dao)
    }
}