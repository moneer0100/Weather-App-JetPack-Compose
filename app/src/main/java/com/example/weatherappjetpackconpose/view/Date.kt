package com.example.weatherappjetpackconpose.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Date(){
    val currentDate = LocalDate.now()

    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
    Text(
        text = formattedDate,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center
    )
}