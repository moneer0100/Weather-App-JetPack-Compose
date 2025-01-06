package com.example.weatherappjetpackconpose.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val MY_LOCATION_PERMISSION_ID = 5005
    private var isLocationReceived = false
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            val viewModel: HomeViewModel = hiltViewModel()
            val locationState = remember { mutableStateOf(Pair(0.0, 0.0)) }
//            Home(viewModel, locationState = locationState.value)
            WeatherNavigation(viewModel,locationState=locationState.value)
            LaunchedEffect(Unit) {
                checkLocationSettings(viewModel, locationState)
            }


        }
    }


    private fun checkLocationSettings(viewModel: HomeViewModel, locationState: MutableState<Pair<Double, Double>>) {
        if (checkLocationPermissions()) {
            if (isLocationEnabled()) {
                getFreshLocation(viewModel, locationState)
            } else {
                enableLocationService()
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation(viewModel: HomeViewModel, locationState: MutableState<Pair<Double, Double>>) {

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000) // تحديث كل 5 ثوانٍ
            .setMinUpdateIntervalMillis(2000) 
            .setMaxUpdateDelayMillis(10000)
            .setWaitForAccurateLocation(true)
            .build()


        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.locations.forEach { location ->
                        latitude = location.latitude
                        longitude = location.longitude
                        locationState.value = Pair(latitude, longitude)
                        getAddressFromLatLong(this@MainActivity, latitude, longitude)

                        // تحديث الموقع في الـ ViewModel
                        viewModel.updateLocation(latitude, longitude, "en", "metric")
                    }
                }
            },
            Looper.myLooper()
        )
    }

    private fun enableLocationService() {
        val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
        Log.i("MainActivity", "Location service is disabled, please enable it.")
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), MY_LOCATION_PERMISSION_ID
        )
    }


    // Function to get the address in the background using coroutines
    private  fun getAddressFromLatLong(context: Context, latitude: Double, longitude: Double): String {
        return try {
          // Run in background thread
                val geocoder = Geocoder(context, Locale.getDefault())
                val addressList = geocoder.getFromLocation(latitude, longitude, 1)
                if (addressList?.isNotEmpty() == true) {
                    val address = addressList?.get(0)
                    "${address?.getAddressLine(0)}, ${address?.locality}, ${address?.adminArea}"
                } else {
                    "Address not found"
                }

        } catch (e: TimeoutException) {
            Log.e("GeocoderError", "Timeout occurred while fetching address: ${e.message}")
            "Address fetch timed out"
        } catch (e: Exception) {
            Log.e("GeocoderError", "Error fetching address: ${e.message}")
            "Unable to get address"
        }
    }


}






