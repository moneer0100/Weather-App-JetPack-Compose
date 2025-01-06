package com.example.weatherappjetpackconpose.view

import Forecast
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.weatherappjetpackconpose.R
import com.example.weatherappjetpackconpose.model.netWork.ResponseState
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast

import com.example.weatherappjetpackconpose.viewModel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(viewModel: HomeViewModel = viewModel(), locationState: Pair<Double, Double>) {
    val currentForecast by viewModel.currentState.collectAsState()
    val forecastWeather by viewModel.forecastState.collectAsState()

            LazyColumn() {
                item {
                    when (currentForecast) {
                        is ResponseState.Loading -> {
                            LoadingState()
                        }
                        is ResponseState.Success -> {
                            val weatherData = (currentForecast as ResponseState.Success<CurrentForcast>).data
                            WeatherContent(weatherData,
                                lat = locationState.first,
                                long = locationState.second)
                        }
                        is ResponseState.Error -> {
                            Text("Error loading current weather data", color = Color.Red, textAlign = TextAlign.Center)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp)) // Adding space between current weather and forecast
                    when (forecastWeather) {
                        is ResponseState.Loading -> {
                            LoadingState()
                        }
                        is ResponseState.Success -> {
                            val forecast = (forecastWeather as ResponseState.Success<Forecast>).data
                            ForecastWeather(forecast,
                                lat=locationState.first,
                                lon=locationState.second)
                        }
                        is ResponseState.Error -> {
                            Text("Error loading forecast data", color = Color.Red, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }





@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}
