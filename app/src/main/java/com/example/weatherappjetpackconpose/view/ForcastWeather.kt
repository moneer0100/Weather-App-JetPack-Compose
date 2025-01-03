package com.example.weatherappjetpackconpose.view

import Forecast
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastWeather(forecast: Forecast) {
    Text(
        text = "Today's Forecast (Every 3 Hours)",
        style = MaterialTheme.typography.titleMedium,
        color = Color.White
    )
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        forecast.list.filterIndexed { index, forecastItem ->

            index % 3 == 0 || LocalDateTime.parse(
                forecastItem.dtTxt,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            ).hour % 3 == 0
        }.forEach { hourlyForecast ->
            val iconCode = hourlyForecast.weather.firstOrNull()?.icon ?: ""
            val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
            val tempInCelsius = kelvinToCelsius(hourlyForecast.main.temp.toFloat())


            val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
            val time = LocalDateTime.parse(hourlyForecast.dtTxt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .format(timeFormatter)

        Card(
                modifier = Modifier.size(120.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = time, // Show time with AM/PM
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    AsyncImage(
                        model = iconUrl,
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "$tempInCelsius°${TemperatureUnit.C}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF0288D1)
                    )
                }
            }
        }
    }

    Text(
        text = "5-Day Forecast",
        style = MaterialTheme.typography.titleMedium,
        color = Color.White
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val groupedForecast = forecast.list.groupBy {
            LocalDate.parse(it.dtTxt.substring(0, 10))
        }

        groupedForecast.entries.take(5).forEach { (date, dailyForecasts) ->
            val dayOfWeek = date.dayOfWeek.name // Get the name of the week
            val tempMaxInCelsius = dailyForecasts.maxOf { kelvinToCelsius(it.main.tempMax.toFloat()) }
            val tempMinInCelsius = dailyForecasts.minOf { kelvinToCelsius(it.main.tempMin.toFloat()) }
            val iconCode = dailyForecasts.firstOrNull()?.weather?.firstOrNull()?.icon ?: ""
            val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$dayOfWeek ",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(text = "$date", style = MaterialTheme.typography.bodyLarge)

                    }
                    AsyncImage(
                        model = iconUrl,
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = " $tempMaxInCelsius°${TemperatureUnit.C}/$tempMinInCelsius°${TemperatureUnit.C}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Blue,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(Color(0xFFFCE4EC))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    }
                }
            }
        }
    }

