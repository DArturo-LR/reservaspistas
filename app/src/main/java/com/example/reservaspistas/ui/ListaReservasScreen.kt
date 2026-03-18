package com.example.reservaspistas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reservaspistas.data.Reserva
import com.example.reservaspistas.ui.theme.*
import com.example.reservaspistas.viewmodel.ReservaViewModel

@Composable
fun ListaReservasScreen(
    viewModel: ReservaViewModel,
    onEditar: (Reserva) -> Unit,
    onVolver: () -> Unit
) {
    val reservas by viewModel.listaReservas.collectAsState()
    var textoBusqueda by remember { mutableStateOf("") }
    var reservaAEliminar by remember { mutableStateOf<Reserva?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {
        // Encabezado
        Surface(color = Blanco, shadowElevation = 2.dp) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                TextButton(
                    onClick = onVolver,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("← Volver", color = Azul, fontSize = 14.sp)
                }
                Text(
                    "Listado de Reservas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Negro
                )
                Text("${reservas.size} reservas registradas", fontSize = 13.sp, color = Gris)
            }
        }

        // Buscador
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                placeholder = { Text("Buscar por nombre...", color = Gris, fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )
            Button(
                onClick = { viewModel.buscar(textoBusqueda) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text("Buscar")
            }
        }

        // Lista de reservas
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(reservas) { reserva ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Blanco),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Info principal
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                reserva.nombre,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = Negro
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Pista ${reserva.pista}  •  ${reserva.fecha}  •  ${reserva.hora}",
                                fontSize = 13.sp,
                                color = Gris
                            )
                            Text(
                                "${reserva.cantidadJugadores} jugadores",
                                fontSize = 12.sp,
                                color = Gris
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            // Badge estado
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (reserva.estado == "Activa")
                                    Verde.copy(alpha = 0.1f)
                                else
                                    Gris.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    reserva.estado,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (reserva.estado == "Activa") Verde else Gris
                                )
                            }
                        }

                        // Botones acción
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = { onEditar(reserva) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = AzulClaro)
                            }
                            IconButton(onClick = { reservaAEliminar = reserva }) {
                                Text("🗑", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    // Confirmación eliminar
    reservaAEliminar?.let { reserva ->
        AlertDialog(
            onDismissRequest = { reservaAEliminar = null },
            title = { Text("Eliminar reserva", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar la reserva de ${reserva.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarReserva(reserva)
                    reservaAEliminar = null
                }) {
                    Text("Eliminar", color = Rojo, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { reservaAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
