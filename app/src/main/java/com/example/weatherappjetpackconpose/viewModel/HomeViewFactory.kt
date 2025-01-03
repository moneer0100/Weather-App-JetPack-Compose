package com.example.weatherappjetpackconpose.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappjetpackconpose.model.pojo.RepoWeatherImp
import javax.inject.Inject

class HomeViewFactory @Inject constructor(private val repoWeatherImp: RepoWeatherImp ):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repoWeatherImp) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class not found")
        }
    }
}