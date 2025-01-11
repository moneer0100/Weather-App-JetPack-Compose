package com.example.weatherappjetpackconpose.view

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
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
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel
import java.util.*

@Composable
fun SettingsScreen(temperatureViewModel: HomeViewModel) {

    val context = LocalContext.current
    var selectedLanguage by remember {
        mutableStateOf(getSavedLanguage(context)) // استرجاع اللغة المحفوظة
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB2DFDB))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()) // Adding scroll functionality
        ) {
            // Card 1 (Language)
            SettingsCard(
                cardTitle = stringResource(id = R.string.language),
                options = listOf("English", "Arabic"),
                cardNumber = 1,
                defaultSelectedOption = selectedLanguage,
                onOptionSelected = { option ->
                    selectedLanguage = option
                    ChangeLocale(option, context) // Change locale when language is selected
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card 2 (Location)
            SettingsCard(
                cardTitle = stringResource(id = R.string.location),
                options = listOf("Gps", "Map"),
                cardNumber = 2,
                defaultSelectedOption = "Gps",
                onOptionSelected = { option -> }
            )

            Spacer(modifier = Modifier.height(16.dp)) // Space between cards

            // Card 3 (Temperature)
            SettingsCard(
                cardTitle = stringResource(id = R.string.temperature),
                options = listOf("Celsius", "Fahrenheit", "Kelvin"),
                cardNumber = 3,
                defaultSelectedOption = "Celsius",
                onOptionSelected = { option ->
                    temperatureViewModel.setTemperatureScale(option)
                }
            )
        }
    }
}

// دالة لاسترجاع اللغة المحفوظة من SharedPreferences
fun getSavedLanguage(context: Context): String {
    val sharedPref = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return sharedPref.getString("selected_language", "English") ?: "English"
}

@Composable
fun SettingsCard(
    cardTitle: String,
    options: List<String>,
    cardNumber: Int,
    defaultSelectedOption: String, // Default option passed as a parameter
    onOptionSelected: (String) -> Unit
) {
    // Set the default selected option
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

            // Create RadioButtons dynamically from the options list
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selectedOption.value == option,
                        onClick = {
                            selectedOption.value = option
                            onOptionSelected(option) // Update the selected option
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Blue)
                    )
                    Text(text = option, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }

}
fun ChangeLocale(languageCode: String, context: Context) {
    val sharedPref = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putString("selected_language", languageCode)
    editor.apply()

    val configuration = context.resources.configuration
    val locale = when (languageCode) {
        "Arabic" -> Locale("ar") // كود اللغة العربية
        "English" -> Locale("en") // كود اللغة الإنجليزية
        else -> Locale.getDefault()
    }

    Locale.setDefault(locale)

    // تعيين اللغة الجديدة على التكوين
    configuration.setLocale(locale)

    // تحديث التكوين في الموارد النظامية
    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

    // إعلام النظام بتغيير التكوين، بدون الحاجة لإعادة إنشاء النشاط
    val activity = context as? Activity
    activity?.window?.decorView?.requestLayout()
}





