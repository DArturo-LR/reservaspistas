package com.example.reservaspistas.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reservaspistas.data.Reserva
import com.example.reservaspistas.viewmodel.ReservaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaScreen(viewModel: ReservaViewModel) {

    val reservas by viewModel.listaReservas.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var pista by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var jugadores by remember { mutableStateOf("") }

    var nombreBusqueda by remember { mutableStateOf("") }

    var estado by remember { mutableStateOf("Activa") }
    var expanded by remember { mutableStateOf(false) }

    val estados = listOf("Activa", "Inactiva")

    var reservaEditando by remember { mutableStateOf<Reserva?>(null) }

    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarTimePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Gestión de Reservas", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreBusqueda,
            onValueChange = { nombreBusqueda = it },
            label = { Text("Buscar por nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Row {

            Button(onClick = {
                viewModel.buscar(nombreBusqueda)
            }) {
                Text("Buscar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                viewModel.cargarReservas()
            }) {
                Text("Mostrar todas")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pista,
            onValueChange = { pista = it },
            label = { Text("Pista") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = jugadores,
            onValueChange = { jugadores = it },
            label = { Text("Cantidad jugadores") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { mostrarDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {

            if (fecha.isEmpty())
                Text("Seleccionar Fecha")
            else
                Text("Fecha: $fecha")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { mostrarTimePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {

            if (hora.isEmpty())
                Text("Seleccionar Hora")
            else
                Text("Hora: $hora")
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                estados.forEach {

                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            estado = it
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                if (
                    nombre.isBlank() ||
                    pista.isBlank() ||
                    fecha.isBlank() ||
                    hora.isBlank() ||
                    jugadores.isBlank()
                ) {
                    viewModel.mostrarMensaje("Todos los campos son obligatorios")
                    return@Button
                }

                val reserva = Reserva(
                    id = reservaEditando?.id ?: 0,
                    nombre = nombre,
                    pista = pista,
                    fecha = fecha,
                    hora = hora,
                    cantidadJugadores = jugadores.toInt(),
                    estado = estado
                )

                if (reservaEditando == null) {
                    viewModel.insertarReserva(reserva)
                } else {
                    viewModel.actualizarReserva(reserva)
                    reservaEditando = null
                }

                nombre = ""
                pista = ""
                fecha = ""
                hora = ""
                jugadores = ""

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            if (reservaEditando == null)
                Text("Agregar Reserva")
            else
                Text("Actualizar Reserva")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(mensaje)

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {

            items(reservas) { reserva ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Text("Cliente: ${reserva.nombre}")
                        Text("Pista: ${reserva.pista}")
                        Text("Fecha: ${reserva.fecha}")
                        Text("Hora: ${reserva.hora}")
                        Text("Jugadores: ${reserva.cantidadJugadores}")
                        Text("Estado: ${reserva.estado}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {

                            Button(
                                onClick = {

                                    nombre = reserva.nombre
                                    pista = reserva.pista
                                    fecha = reserva.fecha
                                    hora = reserva.hora
                                    jugadores = reserva.cantidadJugadores.toString()
                                    estado = reserva.estado

                                    reservaEditando = reserva
                                }
                            ) {
                                Text("Editar")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    viewModel.eliminarReserva(reserva)
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDatePicker) {

        val dateState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },

            confirmButton = {

                TextButton(
                    onClick = {

                        val millis = dateState.selectedDateMillis

                        if (millis != null) {

                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            fecha = formatter.format(Date(millis))
                        }

                        mostrarDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = { mostrarDatePicker = false }
                ) {
                    Text("Cancelar")
                }
            }

        ) {

            DatePicker(state = dateState)
        }
    }

    if (mostrarTimePicker) {

        val timeState = rememberTimePickerState()

        AlertDialog(
            onDismissRequest = { mostrarTimePicker = false },

            confirmButton = {

                TextButton(
                    onClick = {

                        val hour = timeState.hour
                        val minute = timeState.minute

                        hora = String.format("%02d:%02d", hour, minute)

                        mostrarTimePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = { mostrarTimePicker = false }
                ) {
                    Text("Cancelar")
                }
            },

            text = {
                TimePicker(state = timeState)
            }
        )
    }
}
