

package com.example.weatherappjetpackconpose.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*



@SuppressLint("MissingPermission")
@Composable
fun GoogleMapScreen(navController: NavHostController,viewModel: HomeViewModel,onLocationSelected: (LatLng) -> Unit) {
    val context = LocalContext.current

    // Permission State
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher for Permission Request
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    // Request location permission if not granted
    if (!hasLocationPermission) {
        LaunchedEffect(Unit) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }


    Scaffold(
        bottomBar = {
            Button(
                onClick = { selectedLocation?.let{onLocationSelected(it)
                    navController.navigate("favorites")
                    viewModel.insertToDataBase(weather = FavouriteWeather(
                        id=0,
                        address = ""
                        ,lat=it.latitude
                        , lon = it.longitude))
                } },
                enabled = selectedLocation != null
            ) {
                Text(text = "Confirm Location")
            }
        }
    ) { innerPadding ->

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 10f) // Example: San Francisco
        }

        // GoogleMap Composable
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true),
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            onMapClick = { latLng ->
                selectedLocation = latLng
            }
        ) {
            // Add Marker if a location is selected
            selectedLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Selected Location"
                )
            }
        }
    }
}
