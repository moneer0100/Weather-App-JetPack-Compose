package com.example.weatherappjetpackconpose.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.weatherappjetpackconpose.model.netWork.ResponseState
import com.example.weatherappjetpackconpose.model.pojo.Alert
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel
import com.google.android.gms.maps.model.LatLng


@Composable
fun AlertsScreen(navController: NavHostController, viewModel: HomeViewModel) {

    val alertState by viewModel.alertState.collectAsState()
    val currentForecastState by viewModel.currentState.collectAsState()
    LaunchedEffect(true) {
        viewModel.getAlert()
        viewModel.getCurrentForecast(lat = 0.0,0.0,"","")
    }

    when (alertState) {
        is ResponseState.Loading -> {
            Text(text = "Loading Alerts...")
        }
        is ResponseState.Error -> {
            Text(text = "Error: ${(alertState as ResponseState.Error).message}")
        }
        is ResponseState.Success -> {
            val alertLocation = (alertState as ResponseState.Success).data
            val forecast = when (currentForecastState) {
                is ResponseState.Success -> (currentForecastState as ResponseState.Success).data
                else -> null}
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFCDD2))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    alertLocation.forEach { location ->
                        AlertCard(
                            alert = location, // Ensure this is the correct object type
                                currentForecast = forecast,
                            onClick = {
                                navController.navigate("home/${location.lat}/${location.lon}")
                            },
                            onDelete = {
                                viewModel.deleteAlert(location)
                                Toast.makeText(navController.context, "Deleted ${location.id}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(onClick = { navController.navigate("googleMapScreen/favScreen") }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(text = "Go To Map")
                    }
                }
            }
        }
    }
}

@Composable
fun AlertCard(alert: Alert, currentForecast: CurrentForcast?, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = alert.Kind,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${alert.lat},${alert.lon}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                currentForecast?.weather?.get(0)?.let {
                    Text(text = it.description,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = Color.Gray)
                }

            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}
