package com.example.weatherappjetpackconpose.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherappjetpackconpose.viewModel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherNavigation(viewModel: HomeViewModel, locationState: Pair<Double, Double>,address:String) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = listOf(
                    BottomNavItem(label = "Home", icon = Icons.Filled.Home, route = "home"),
                    BottomNavItem(label = "Favorites", icon = Icons.Filled.Favorite, route = "favorites"),
                    BottomNavItem(label = "Alerts", icon = Icons.Filled.Notifications, route = "alerts"),
                    BottomNavItem(label = "Settings", icon = Icons.Filled.Settings, route = "settings")
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF90CAF9), Color(0xFFBBDEFB))
                    )
                )
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    Home(viewModel = viewModel, locationState = locationState,address)
                }
                composable(
                    route = "home/{lat}/{lon}",
                    arguments = listOf(
                        navArgument("lat") { type = NavType.StringType },
                        navArgument("lon") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
                    val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull() ?: 0.0
                    Home(viewModel = viewModel, locationState = lat to lon,address)
                }

                composable("favorites") {
                    FavoritesScreen(navController=navController,viewModel=viewModel)
                }
                composable("alerts") {
                    AlertsScreen(navController=navController,viewModel=viewModel)
                }
                composable("settings") {
                    SettingsScreen(viewModel)
                }

                composable("googleMapScreen/{sourceScreen}") { backStackEntry ->
                    val sourceScreen = backStackEntry.arguments?.getString("sourceScreen") ?: "favScreen"
                    GoogleMapScreen(
                        navController = navController,
                        viewModel = viewModel,
                        onLocationSelected = { latLng ->  navController.previousBackStackEntry?.savedStateHandle?.set("location", latLng)
                            navController.popBackStack() },
                        sourceScreen = sourceScreen
                    )
                }

            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF0288D1),
        contentColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) },
                selected = currentDestination == item.route,
                onClick = {
                    if (currentDestination != item.route) {
                        navController.navigate(item.route) {

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)
