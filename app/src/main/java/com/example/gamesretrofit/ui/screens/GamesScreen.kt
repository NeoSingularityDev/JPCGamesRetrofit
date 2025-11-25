package com.example.gamesretrofit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamesretrofit.data.DeveloperDTO
import com.example.gamesretrofit.data.GameDTO
import com.example.gamesretrofit.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen() {
    val api = RetrofitClient.api

    var games by remember { mutableStateOf(listOf<GameDTO>()) }
    var developers by remember { mutableStateOf(listOf<DeveloperDTO>()) }
    var loading by remember { mutableStateOf(true) }
    var showForm by remember { mutableStateOf(false) }
    var editingGame by remember { mutableStateOf<GameDTO?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showSuccess(msg: String) { scope.launch { snackbarHostState.showSnackbar(msg) } }
    fun showError(msg: String) { scope.launch { snackbarHostState.showSnackbar(msg) } }

    fun loadGames() {
        loading = true
        try {
            api.getGames().enqueue(object : Callback<List<GameDTO>> {
                override fun onResponse(call: Call<List<GameDTO>>, response: Response<List<GameDTO>>) {
                    games = response.body()?.sortedBy { it.id } ?: emptyList()
                    loading = false
                    showSuccess("Juegos cargados")
                }
                override fun onFailure(call: Call<List<GameDTO>>, t: Throwable) {
                    loading = false
                    showError("Error al cargar juegos")
                }
            })
        } catch (e: Exception) {
            loading = false
            showError("Excepción: ${e.message}")
        }
    }

    fun loadDevelopers() {
        try {
            api.getDevelopers().enqueue(object : Callback<List<DeveloperDTO>> {
                override fun onResponse(call: Call<List<DeveloperDTO>>, response: Response<List<DeveloperDTO>>) {
                    developers = response.body() ?: emptyList()
                }
                override fun onFailure(call: Call<List<DeveloperDTO>>, t: Throwable) { /* noop */ }
            })
        } catch (_: Exception) {}
    }

    LaunchedEffect(Unit) {
        loadDevelopers()
        loadGames()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Juegos") },
                actions = {
                    IconButton(onClick = { loadGames() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { editingGame = null; showForm = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    items(games) { game ->
                        GameCard(
                            game = game,
                            onEdit = { editingGame = game; showForm = true },
                            onDelete = {
                                try {
                                    api.deleteGame(game.id).enqueue(object : Callback<Map<String, String>> {
                                        override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                                            games = games.filter { it.id != game.id }
                                            showSuccess("Eliminado correctamente")
                                        }
                                        override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                                            showError("Error al eliminar")
                                        }
                                    })
                                } catch (e: Exception) {
                                    showError("Excepción: ${e.message}")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            if (showForm) {
                GameFormDialog(
                    developers = developers,
                    initial = editingGame,
                    onDismiss = { showForm = false },
                    onSaved = { saved ->
                        val exists = games.any { it.id == saved.id }
                        games = if (exists) games.map { if (it.id == saved.id) saved else it } else (games + saved).sortedBy { it.id }
                        showForm = false
                        showSuccess(if (exists) "Actualizado correctamente" else "Creado correctamente")
                    }
                )
            }
        }
    }
}
