package com.example.weatherappjetpackconpose.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.weatherappjetpackconpose.model.pojo.Alert
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.util.*

@Composable
fun GoogleMapScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
    onLocationSelected: (LatLng) -> Unit,
    sourceScreen: String
) {
    var alarmCalendar by remember { mutableStateOf<Calendar?>(null) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
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
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

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
                    selectedLocation?.let {
                        // Trigger the location selection dialog
                        showDialog = true
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

        // Show an alert dialog when a location is selected
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            selectedLocation?.let {
                                onLocationSelected(it)
                                when (sourceScreen) {
                                    "favScreen" -> navController.navigate("favorites")
                                    "alertsScreen" -> navController.navigate("alerts")
                                    else -> navController.popBackStack()
                                }

                                // Show DatePickerDialog when the user confirms the location
                                showTimePicker = true
                                showDialog = false
                            }
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Confirm Location") },
                text = { Text("Do you want to set an alarm for this location?") }
            )
        }

        if (showTimePicker) {
            android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    // Once the date is selected, show TimePicker
                    TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            // Adjust hour for AM/PM format
                            val isPM = hourOfDay >= 12
                            val hourIn12Format = if (isPM) hourOfDay - 12 else hourOfDay
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, hourIn12Format)
                            calendar.set(Calendar.MINUTE, minute)
                            calendar.set(Calendar.SECOND, 0)
                            alarmCalendar = calendar // Save the selected time
                            // Call the function to set the alarm
                            alarmCalendar?.let {
                                setAlarm(context, selectedLocation, it)
                                selectedLocation?.let { location ->
                                    viewModel.insertAlert(
                                        alert = Alert(
                                            id = 0,
                                            lon = location.longitude,
                                            lat = location.latitude,
                                            start = it.timeInMillis.toDouble(),
                                            end = 0.0,
                                            Kind = "Alarm"
                                        )
                                    )
                                }
                            }
                        },
                        selectedDate.get(Calendar.HOUR),
                        selectedDate.get(Calendar.MINUTE),
                        false // false for 12-hour format (true is for 24-hour format)
                    ).show()

                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}

@SuppressLint("ScheduleExactAlarm")
fun setAlarm(context: Context, location: LatLng?, alarmCalendar: Calendar) {
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

    // Schedule alarm for the selected time
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.timeInMillis, pendingIntent)
    Log.d("Alarm", "Alarm set for: ${alarmCalendar.timeInMillis}")
}
