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
import com.example.gamesretrofit.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevelopersScreen() {
    val api = RetrofitClient.api

    var developers by remember { mutableStateOf(listOf<DeveloperDTO>()) }
    var loading by remember { mutableStateOf(true) }
    var showForm by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<DeveloperDTO?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showSuccess(msg: String) { scope.launch { snackbarHostState.showSnackbar(msg) } }
    fun showError(msg: String) { scope.launch { snackbarHostState.showSnackbar(msg) } }

    fun loadDevelopers() {
        loading = true
        try {
            api.getDevelopers().enqueue(object : Callback<List<DeveloperDTO>> {
                override fun onResponse(call: Call<List<DeveloperDTO>>, response: Response<List<DeveloperDTO>>) {
                    developers = response.body()?.sortedBy { it.id } ?: emptyList()
                    loading = false
                    showSuccess("Desarrolladoras cargadas")
                }
                override fun onFailure(call: Call<List<DeveloperDTO>>, t: Throwable) {
                    loading = false
                    showError("Error al cargar desarrolladoras")
                }
            })
        } catch (e: Exception) {
            loading = false
            showError("Excepción: ${e.message}")
        }
    }

    LaunchedEffect(Unit) { loadDevelopers() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Desarrolladoras") },
                actions = {
                    IconButton(
                        onClick = { loadDevelopers() })
                    {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { editing = null; showForm = true }) {
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
                LazyColumn(Modifier.fillMaxSize().padding(12.dp)) {
                    items(developers) { dev ->
                        DeveloperCard(
                            dev,
                            onEdit = { editing = dev; showForm = true },
                            onDelete = {
                                try {
                                    api.deleteDeveloper(dev.id).enqueue(object : Callback<Map<String, String>> {
                                        override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                                            developers = developers.filter { it.id != dev.id }
                                            showSuccess("Eliminada correctamente")
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
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            if (showForm) {
                DeveloperFormDialog(
                    initial = editing,
                    onDismiss = { showForm = false },
                    onSaved = { saved ->
                        val exists = developers.any { it.id == saved.id }
                        developers = if (exists) developers.map { if (it.id == saved.id) saved else it } else (developers + saved).sortedBy { it.id }
                        showForm = false
                        showSuccess(if (exists) "Actualizado correctamente" else "Creado correctamente")
                    }
                )
            }
        }
    }
}
