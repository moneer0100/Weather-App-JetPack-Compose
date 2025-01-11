package com.example.weatherappjetpackconpose.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel


// Current Weather Content
@SuppressLint("StateFlowValueCalledInComposition", "SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherContent(currentForecast: CurrentForcast, lat: Double, lon: Double,viewModel: HomeViewModel,address:String) {
    val iconCode = currentForecast.weather.getOrNull(0)?.icon
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    val convertedTempr by viewModel.convertedTemperature.collectAsState()

     val temperatureInCelsius = currentForecast.main.temp
  val convertedTemp = viewModel.convertTemperature(temperatureInCelsius)


    Column(
        modifier = Modifier
            .fillMaxSize()

      ,  horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Location(lat = lat, long =lon ,address)
        Date()


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = "weather Icon",
                    modifier = Modifier.size(120.dp)
                )

                Text(
                    text = currentForecast.weather[0].description,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF0288D1)
                )
                Text(
                    text = "${convertedTemp}°${viewModel.selectedTemperatureScale.value}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    }

}




