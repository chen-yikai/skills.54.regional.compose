package dev.eliaschen.xml.skills54regionalflutter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Root()
        }
    }
}

@Composable
fun Root() {
    var navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.List.route) {
        composable(
            Screen.Home.route + "/{${NavArg.location.name}}",
            arguments = listOf(navArgument(NavArg.location.name) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val location = backStackEntry.arguments?.getString(NavArg.location.name)
            HomeScreen(navController, location ?: "taipei")
        }
        composable(Screen.List.route) {
            ListScreen(navController)
        }
        composable(Screen.Setting.route) {
            SettingScreen()
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object List : Screen("list")
    object Setting : Screen("setting")
}

enum class NavArg {
    location
}