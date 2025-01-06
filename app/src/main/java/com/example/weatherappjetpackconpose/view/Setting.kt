package com.example.weatherappjetpackconpose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



    @Composable
    fun SettingsScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFB2DFDB))
        ) {
            Text(text = "Settings Screen", modifier = Modifier.padding(16.dp))
        }
    }
