package com.example.gamesretrofit.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Retrofit/Laravel App") },
        actions = {

            TextButton(onClick = { navController.navigate("games") }) {
                Text("Games")
            }

            TextButton(onClick = { navController.navigate("developers") }) {
                Text("Developers")
            }

            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(if (isDarkTheme) "Tema Claro" else "Tema Oscuro") },
                    onClick = {
                        onToggleTheme()
                        menuExpanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Salir") },
                    onClick = {
                        menuExpanded = false
                        exitProcess(0)
                    }
                )
            }
        }
    )
}

