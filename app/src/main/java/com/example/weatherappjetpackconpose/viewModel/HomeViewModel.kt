package com.example.weatherappjetpackconpose.viewModel

import Forecast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherappjetpackconpose.model.netWork.ResponseState
import com.example.weatherappjetpackconpose.model.pojo.Alert
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import com.example.weatherappjetpackconpose.model.pojo.RepoWeatherImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(private val repoWeatherImp: RepoWeatherImp):ViewModel() {
    private val _curentState= MutableStateFlow<ResponseState<CurrentForcast>>(ResponseState.Loading)
    val currentState=_curentState.asStateFlow()
fun getCurrentForecast(lat:Double,long:Double,language: String,unites:String){
    viewModelScope.launch(Dispatchers.IO){
        repoWeatherImp.getWeatherCurrent(lat,long,language,unites)
            ?.catch { error->_curentState.value =ResponseState.Error(error) }
            ?.collect{data->_curentState.value=ResponseState.Success(data)}
    }
}
    private val _forecastState= MutableStateFlow<ResponseState<Forecast>>(ResponseState.Loading)
    val forecastState=_forecastState.asStateFlow()
    fun getForecastWeather(lat:Double,long: Double,language: String,unites: String){
        viewModelScope.launch(Dispatchers.IO){
            repoWeatherImp.getWeatherForecast(lat,long,language,unites)
                ?.catch { error->_forecastState.value=ResponseState.Error(error) }
                ?.collect{data->_forecastState.value=ResponseState.Success(data)}
        }
    }
    fun updateLocation(latitude: Double, longitude: Double,language: String,unites: String) {
        getCurrentForecast(latitude, longitude,language,unites)
        getForecastWeather(latitude, longitude,language,unites)
    }
    ///fav
    fun insertToDataBase(weather: FavouriteWeather){
        viewModelScope.launch(Dispatchers.IO){
        repoWeatherImp.insertToDataBase(weather)
    }
    }
    fun deleteFromDataBase(weather: FavouriteWeather){
        viewModelScope.launch(Dispatchers.IO){
            repoWeatherImp.deleteFromDataBase(weather)
        }
    }
    private val _favState= MutableStateFlow<ResponseState<List<FavouriteWeather>>>(ResponseState.Loading)
    val favState=_favState.asStateFlow()

    fun getAllFav(){
        viewModelScope.launch(Dispatchers.IO){
            repoWeatherImp.getAllFav().catch { error->
                _favState.value=ResponseState.Error(error)
            }
                .collect{data->_favState.value=ResponseState.Success(data)}
        }
    }
    //Alert
    private val _alertState= MutableStateFlow<ResponseState<List<Alert>>>(ResponseState.Loading)
    val alertState=_alertState.asStateFlow()
    fun getAlert(){
        viewModelScope.launch(Dispatchers.IO){
            repoWeatherImp.getAlert().catch { error->_alertState.value=ResponseState.Error(error) }
                ?.collect{data->_alertState.value=ResponseState.Success(data)}
        }
    }
    fun insertAlert(alert: Alert){
    viewModelScope.launch(Dispatchers.IO){
        repoWeatherImp.insertAlert(alert)
    }

    }

    fun deleteAlert(alert: Alert){
        viewModelScope.launch(Dispatchers.IO){
            repoWeatherImp.deleteAlert(alert)
        }
    }
    var selectedTemperatureScale = mutableStateOf("Celsius")

    // Function to convert the temperature based on the selected scale
    fun convertTemperature(temp: Double): Double {
        return when (selectedTemperatureScale.value) {
            "C" -> temp // already in Celsius
            "F" -> (temp * 9/5) + 32 // Celsius to Fahrenheit
            "K" -> temp + 273.15 // Celsius to Kelvin
            else -> temp
        }
    }


    fun setTemperatureScale(scale: String) {
        selectedTemperatureScale.value = scale
    }
}
