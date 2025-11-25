package com.example.gamesretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gamesretrofit.components.AppTopBar
import com.example.gamesretrofit.ui.screens.DevelopersScreen
import com.example.gamesretrofit.ui.screens.GamesScreen
import com.example.gamesretrofit.ui.theme.GamesRetrofitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {

    val navController = rememberNavController()
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }

    GamesRetrofitTheme(darkTheme = isDarkTheme) {

        Scaffold(
            topBar = {
                AppTopBar(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = "games",
                modifier = Modifier.padding(padding)
            ) {
                composable("games") {
                    GamesScreen()
                }

                composable("developers") {
                    DevelopersScreen()
                }
            }
        }
    }
}
