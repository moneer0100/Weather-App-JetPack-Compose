package com.example.weatherappjetpackconpose.view

import Forecast
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

@Composable
fun Home(viewModel: HomeViewModel = viewModel()) {
    val currentForecast by viewModel.currentState.collectAsState()
    val forecastWeather by viewModel.forecastState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentForecast(lat = 0.0, long = 0.0, language = "", unites = "")
        viewModel.getForecastWeather(lat = 0.0, long = 0.0, language = "", unites = "")
    }

    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Favorites", "Alerts", "Settings")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Favorite,
        Icons.Filled.Notifications,
        Icons.Filled.Settings
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = items,
                icons = icons,
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF90CAF9), Color(0xFFBBDEFB))
                    )
                )
                .padding(innerPadding)
        ) {
            // LazyColumn to display weather content
            LazyColumn() {
                item {
                    when (currentForecast) {
                        is ResponseState.Loading -> {
                            LoadingState()
                        }
                        is ResponseState.Success -> {
                            val weatherData = (currentForecast as ResponseState.Success<CurrentForcast>).data
                            WeatherContent(weatherData)
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
                            ForecastWeather(forecast)
                        }
                        is ResponseState.Error -> {
                            Text("Error loading forecast data", color = Color.Red, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<String>,
    icons: List<ImageVector>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF0288D1),
        contentColor = Color.White
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(imageVector = icons[index], contentDescription = item) },
                label = { Text(text = item) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
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
