package com.example.weatherappjetpackconpose.view


import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
fun Location(lat: Double, long: Double) {

        Text(modifier = Modifier.padding(30.dp).fillMaxSize(),
            text = "$lat / $long",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )
    }
