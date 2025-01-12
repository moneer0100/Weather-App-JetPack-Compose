package com.example.weatherappjetpackconpose.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.weatherappjetpackconpose.model.netWork.ResponseState
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel

@Composable
fun FavoritesScreen(navController: NavHostController, viewModel: HomeViewModel) {
   val favoriteWeatherState  by viewModel.favState.collectAsState()
    val currentForcast by viewModel.currentState.collectAsState()
 LaunchedEffect(true) {
     viewModel.getAllFav()


 }
    when (favoriteWeatherState) {
        is ResponseState.Loading -> {
            // Show a loading indicator or message here
            Text(text = "Loading favorites...")
        }
        is ResponseState.Error -> {
            // Show error message
            Text(text = "Error: ${(favoriteWeatherState as ResponseState.Error).message}")
        }
        is ResponseState.Success -> {
            val favoriteLocations = (favoriteWeatherState as ResponseState.Success).data
            val currentForcast2=(currentForcast as ResponseState.Success).data
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFCDD2))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    favoriteLocations.forEach { location ->
                        FavoriteCard(
                            currentForcast2 = currentForcast2,
                            favoriteWeather = location,
                            onClick = {
                                navController.navigate("home/${location.lat}/${location.lon}")
                            },
                            onDelete = {

                                viewModel.deleteFromDataBase(location)
                                Toast.makeText(navController.context, "Deleted ${location.address}", Toast.LENGTH_SHORT).show()
                            }

                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(onClick = { navController.navigate("googleMapScreen/favorites") }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(text = "Go To Map")
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteCard(favoriteWeather: FavouriteWeather,onClick:()->Unit, onDelete: () -> Unit,currentForcast2:CurrentForcast) {
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
                    text = favoriteWeather.address,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text =currentForcast2.weather[0].description,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(text = "")

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

