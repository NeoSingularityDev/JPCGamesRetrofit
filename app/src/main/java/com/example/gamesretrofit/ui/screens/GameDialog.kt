package com.example.gamesretrofit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gamesretrofit.data.*
import com.example.gamesretrofit.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameFormDialog(
    developers: List<DeveloperDTO>,
    initial: GameDTO?,
    onDismiss: () -> Unit,
    onSaved: (GameDTO) -> Unit
) {
    val api = RetrofitClient.api
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var desc by remember { mutableStateOf(initial?.description ?: "") }
    var yearText by remember { mutableStateOf(initial?.year?.toString() ?: "") }
    var selectedDeveloper by remember { mutableStateOf<DeveloperDTO?>(initial?.developer ?: developers.firstOrNull()) }
    var expandedDev by remember { mutableStateOf(false) }
    var saving by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 8.dp) {
            Column(modifier = Modifier.padding(16.dp).widthIn(min = 300.dp)) {
                Text(text = if (initial == null) "Crear Juego" else "Editar Juego", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") }, singleLine = true)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = yearText, onValueChange = { yearText = it.filter { it.isDigit() } }, label = { Text("Año") }, singleLine = true)
                Spacer(Modifier.height(8.dp))

                ExposedDropdownMenuBox(expanded = expandedDev, onExpandedChange = { expandedDev = !expandedDev }) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedDeveloper?.name ?: "Seleccionar desarrolladora",
                        onValueChange = {},
                        label = { Text("Desarrolladora") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDev) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expandedDev, onDismissRequest = { expandedDev = false }) {
                        developers.forEach { dev ->
                            DropdownMenuItem(text = { Text(dev.name) }, onClick = {
                                selectedDeveloper = dev
                                expandedDev = false
                            })
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cancelar", modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Cancelar")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        if (title.isBlank() || selectedDeveloper == null) return@Button
                        saving = true
                        val body = GameRequest(
                            title = title,
                            description = desc.takeIf { it.isNotBlank() },
                            year = yearText.takeIf { it.isNotBlank() }?.toIntOrNull(),
                            developer_id = selectedDeveloper!!.id
                        )

                        if (initial == null) {
                            api.createGame(body).enqueue(object : Callback<GameDTO> {
                                override fun onResponse(call: Call<GameDTO>, response: Response<GameDTO>) {
                                    response.body()?.let { onSaved(it) }
                                    saving = false
                                }
                                override fun onFailure(call: Call<GameDTO>, t: Throwable) { saving = false }
                            })
                        } else {
                            api.updateGame(initial.id, body).enqueue(object : Callback<GameDTO> {
                                override fun onResponse(call: Call<GameDTO>, response: Response<GameDTO>) {
                                    response.body()?.let { onSaved(it) }
                                    saving = false
                                }
                                override fun onFailure(call: Call<GameDTO>, t: Throwable) { saving = false }
                            })
                        }
                    }) {
                        if (saving) CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        else {
                            Icon(Icons.Default.Done, contentDescription = "Guardar", modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}
