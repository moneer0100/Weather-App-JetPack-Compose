package com.example.weatherappjetpackconpose.view

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherappjetpackconpose.R
import com.example.weatherappjetpackconpose.model.pojo.CurrentForcast
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel
import java.util.*

@Composable
fun SettingsScreen(temperatureViewModel: HomeViewModel) {
    val context = LocalContext.current
    val savedLanguage = remember { mutableStateOf(getSavedPreference(context, "selected_language", "English")) }
    val savedTemp = remember { mutableStateOf(getSavedPreference(context, "selected_temp", "Celsius")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB2DFDB))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            SettingsCard(
                cardTitle = stringResource(id = R.string.language),
                options = listOf("English", "Arabic"),
                defaultSelectedOption = savedLanguage.value,
                onOptionSelected = { option ->
                    savedLanguage.value = option
                    savePreference(context, "selected_language", option)
                    changeLocale(option, context)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsCard(
                cardTitle = stringResource(id = R.string.temperature),
                options = listOf("Celsius", "Fahrenheit", "Kelvin"),
                defaultSelectedOption = savedTemp.value,
                onOptionSelected = { option ->
                    savedTemp.value = option
                    savePreference(context, "selected_temp", option)
                    temperatureViewModel.setTemperatureScale(option)
                }
            )
        }
    }
}

@Composable
fun SettingsCard(
    cardTitle: String,
    options: List<String>,
    defaultSelectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val selectedOption = remember { mutableStateOf(defaultSelectedOption) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .selectableGroup()
        ) {
            Text(text = cardTitle, modifier = Modifier.padding(bottom = 8.dp))
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selectedOption.value == option,
                        onClick = {
                            selectedOption.value = option
                            onOptionSelected(option)
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Blue)
                    )
                    Text(text = option, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

fun getSavedPreference(context: Context, key: String, defaultValue: String): String {
    val sharedPref = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return sharedPref.getString(key, defaultValue) ?: defaultValue
}

fun savePreference(context: Context, key: String, value: String) {
    val sharedPref = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    sharedPref.edit().putString(key, value).apply()
}

fun changeLocale(languageCode: String, context: Context) {
    val locale = when (languageCode) {
        "Arabic" -> Locale("ar")
        "English" -> Locale("en")
        else -> Locale.getDefault()
    }

    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    (context as? Activity)?.window?.decorView?.requestLayout()
}
