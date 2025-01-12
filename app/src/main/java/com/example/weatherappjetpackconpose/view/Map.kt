package com.example.weatherappjetpackconpose.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.weatherappjetpackconpose.model.pojo.Alert
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.android.compose.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleMapScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
    onLocationSelected: (LatLng) -> Unit,
    sourceScreen: String
) {
    var alarmCalendar by remember { mutableStateOf<Calendar?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasLocationPermission = isGranted }

    if (!hasLocationPermission) {
        LaunchedEffect(Unit) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    selectedLocation?.let { location ->
                        val address = getAddressFromLatLng(context, location)
                        if (sourceScreen == "alerts") {
                            showDialog = true
                            // هنا تقدر تفتح الـ TimePicker عند اختيار المكان
                            alarmCalendar = Calendar.getInstance()
                            showTimePicker = true
                            showDatePicker = true
                        } else if (sourceScreen == "favorites") {
                            address?.let {
                                val favourite = FavouriteWeather(
                                    id = 0,
                                    address = it,
                                    lat = location.latitude,
                                    lon = location.longitude
                                )
                                viewModel.insertToDataBase(favourite)

                                val alert = alarmCalendar?.timeInMillis?.let { it1 ->
                                    Alert(
                                        id = 0,
                                        lat = location.latitude,
                                        lon = location.longitude,
                                        Kind = "Alarm",
                                        start = it1.toDouble(),
                                        end = 0.0
                                    )
                                }
                                if (alert != null) {
                                    viewModel.insertAlert(alert)
                                }
                            }
                            onLocationSelected(location)
                            navController.navigate("favorites")
                        } else {
                            onLocationSelected(location)
                            when (sourceScreen) {
                                "AlertScreen" -> navController.navigate("alerts")
                                else -> navController.popBackStack()
                            }
                        }
                    }
                },
                enabled = selectedLocation != null
            ) {
                Text(text = "Confirm Location")
            }
        }
    ) { innerPadding ->

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 10f)
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true),
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            onMapClick = { latLng -> selectedLocation = latLng }
        ) {
            selectedLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Selected Location"
                )
            }
        }
        if (showDatePicker) {
            android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    alarmCalendar?.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }
                    showDatePicker = false
                    showTimePicker = true // Open TimePicker after selecting date
                },
                alarmCalendar?.get(Calendar.YEAR) ?: Calendar.getInstance().get(Calendar.YEAR),
                alarmCalendar?.get(Calendar.MONTH) ?: Calendar.getInstance().get(Calendar.MONTH),
                alarmCalendar?.get(Calendar.DAY_OF_MONTH) ?: Calendar.getInstance()
                    .get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        // Time Picker Dialog to select time
        if (showTimePicker) {
            LaunchedEffect(showTimePicker) {
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        alarmCalendar?.apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                        }
                        showTimePicker = false

                        // Confirm Alarm Dialog
                        alarmCalendar?.let {
                            val formattedTime = String.format(
                                Locale.getDefault(),
                                "%02d:%02d %s",
                                if (hourOfDay > 12) hourOfDay - 12 else hourOfDay,
                                minute,
                                if (hourOfDay >= 12) "PM" else "AM"
                            )
                            MaterialAlertDialogBuilder(context)
                                .setTitle("Confirm Alarm")
                                .setMessage("Alarm set for: $formattedTime")
                                .setPositiveButton("Confirm") { _, _ ->
                                    // حفظ التنبيه في قاعدة البيانات
                                    alarmCalendar?.let { calendar ->
                                        val alert = Alert(
                                            id = 0,
                                            lat = selectedLocation?.latitude ?: 0.0,
                                            lon = selectedLocation?.longitude ?: 0.0,
                                            Kind = "Alarm",
                                            start = calendar.timeInMillis.toDouble(),
                                            end = 0.0
                                        )
                                        viewModel.insertAlert(alert)

                                        // بعد حفظ التنبيه، انتقل إلى صفحة المنبه
                                        navController.navigate("alerts")
                                    }
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                        }
                    },
                    alarmCalendar?.get(Calendar.HOUR_OF_DAY) ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    alarmCalendar?.get(Calendar.MINUTE) ?: Calendar.getInstance().get(Calendar.MINUTE),
                    false
                ).show()
            }
        }

    }
}


@SuppressLint("ScheduleExactAlarm")
fun setAlarm(context: Context, location: LatLng?, alarmCalendar: Calendar, viewModel: HomeViewModel) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (location == null) return

    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("LATITUDE", location.latitude)
        putExtra("LONGITUDE", location.longitude)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Set the alarm
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.timeInMillis, pendingIntent)
    Log.d("Alarm", "Alarm set for: ${alarmCalendar.timeInMillis}")

}

fun getAddressFromLatLng(context: Context, latLng: LatLng): String? {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        addresses?.firstOrNull()?.getAddressLine(0)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
