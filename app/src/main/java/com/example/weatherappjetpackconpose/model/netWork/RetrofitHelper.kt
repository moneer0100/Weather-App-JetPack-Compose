package com.example.weatherappjetpackconpose.model.netWork

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitHelper {
    private const val BASE_URL="https://api.openweathermap.org/data/2.5/"
@Singleton
@Provides
fun provideRetrofit():Retrofit{
return Retrofit.Builder()
    .baseUrl(BASE_URL)
    . addConverterFactory(GsonConverterFactory
        .create()).build()
}
@Singleton
@Provides
fun ProvideAppService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}
}