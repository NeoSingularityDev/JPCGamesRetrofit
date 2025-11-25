package com.example.gamesretrofit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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

@Composable
fun DeveloperFormDialog(
    initial: DeveloperDTO?,
    onDismiss: () -> Unit,
    onSaved: (DeveloperDTO) -> Unit
) {
    val api = RetrofitClient.api
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var country by remember { mutableStateOf(initial?.country ?: "") }
    var saving by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(Modifier.padding(16.dp).widthIn(min = 300.dp)) {
                Text(if (initial == null) "Crear Desarrolladora" else "Editar Desarrolladora")
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, singleLine = true)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(country, { country = it }, label = { Text("País") }, singleLine = true)
                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cancelar", modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Cancelar")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        if (name.isBlank()) return@Button
                        saving = true
                        val body = DeveloperRequest(name = name, country = country.takeIf { it.isNotBlank() })

                        if (initial == null) {
                            api.createDeveloper(body).enqueue(object : Callback<DeveloperDTO> {
                                override fun onResponse(call: Call<DeveloperDTO>, response: Response<DeveloperDTO>) {
                                    response.body()?.let(onSaved)
                                    saving = false
                                }
                                override fun onFailure(call: Call<DeveloperDTO>, t: Throwable) { saving = false }
                            })
                        } else {
                            api.updateDeveloper(initial.id, body).enqueue(object : Callback<DeveloperDTO> {
                                override fun onResponse(call: Call<DeveloperDTO>, response: Response<DeveloperDTO>) {
                                    response.body()?.let(onSaved)
                                    saving = false
                                }
                                override fun onFailure(call: Call<DeveloperDTO>, t: Throwable) { saving = false }
                            })
                        }
                    }) {
                        if (saving) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
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
