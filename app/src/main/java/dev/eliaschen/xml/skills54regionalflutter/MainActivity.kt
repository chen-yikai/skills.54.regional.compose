package dev.eliaschen.xml.skills54regionalflutter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.List.route) {
                composable(Screen.Home.route) {
                    HomeScreen(navController)
                }
                composable(Screen.List.route) {
                    ListScreen()
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object List : Screen("list")
}

