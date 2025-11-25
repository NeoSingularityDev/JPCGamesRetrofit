package com.example.gamesretrofit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gamesretrofit.data.DeveloperDTO
import com.example.gamesretrofit.ui.theme.orangeColor
import com.example.gamesretrofit.ui.theme.redColor

@Composable
fun DeveloperCard(dev: DeveloperDTO, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(dev.name, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text("País: ${dev.country ?: "-"}")

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = onEdit,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = orangeColor
                    )
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Editar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = redColor
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar")
                }
            }
        }
    }
}
