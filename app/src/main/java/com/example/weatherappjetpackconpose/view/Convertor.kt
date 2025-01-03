package com.example.weatherappjetpackconpose.view

import kotlin.math.roundToInt

fun kelvinToCelsius(kelvin: Float): Int = (kelvin - 273.15f).roundToInt()
fun kelvinToFahrenheit(kelvin: Float): Int = ((kelvin - 273.15f) * 9 / 5 + 32).roundToInt()
enum class TemperatureUnit {
    C, F,K
}
