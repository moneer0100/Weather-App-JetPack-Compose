package com.example.weatherappjetpackconpose.view


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Location(lat: Double, long: Double, address:String) {

        Text(modifier = Modifier.padding(30.dp).fillMaxSize(),
            text = address,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )
    }
