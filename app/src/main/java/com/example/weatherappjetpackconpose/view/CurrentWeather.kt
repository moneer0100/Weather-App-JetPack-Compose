package com.example.weatherappjetpackconpose.view

import Forecast
import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast


// Current Weather Content
@Composable
fun WeatherContent(currentForecast: CurrentForcast) {
    val iconCode = currentForecast.weather.getOrNull(0)?.icon
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    Log.d("moneer", "WeatherContent:$iconUrl")

    val tempInKelvin = currentForecast.main.temp
    val tempInCelsius = kelvinToCelsius(tempInKelvin.toFloat())

    Column(
        modifier = Modifier
            .fillMaxSize()

      ,  horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Cairo, Egypt",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "December 31, 2024",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))


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
                    text = currentForecast.weather[0].description.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF0288D1)
                )
                Text(
                    text = "$tempInCelsius/${TemperatureUnit.C}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    }

}




