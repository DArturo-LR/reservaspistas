package com.example.reservaspistas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.reservaspistas.data.Reserva
import com.example.reservaspistas.viewmodel.ReservaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaScreen(
    viewModel: ReservaViewModel,
    reservaEditar: Reserva? = null,
    onIrALista: () -> Unit,
    onCancelar: () -> Unit
) {
    val mensaje by viewModel.mensaje.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var pista by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var jugadores by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Activa") }

    var expanded by remember { mutableStateOf(false) }
    val estados = listOf("Activa", "Finalizada")

    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarTimePicker by remember { mutableStateOf(false) }

    // Efecto para mostrar el Snackbar cuando cambia el mensaje en el ViewModel
    LaunchedEffect(mensaje) {
        if (mensaje.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = mensaje,
                duration = SnackbarDuration.Short
            )
            viewModel.mostrarMensaje("") // Limpiar mensaje después de mostrarlo
        }
    }

    LaunchedEffect(reservaEditar) {
        if (reservaEditar != null) {
            nombre = reservaEditar.nombre
            pista = reservaEditar.pista
            fecha = reservaEditar.fecha
            hora = reservaEditar.hora
            jugadores = reservaEditar.cantidadJugadores.toString()
            estado = reservaEditar.estado
        } else {
            nombre = ""; pista = ""; fecha = ""; hora = ""; jugadores = ""; estado = "Activa"
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = if (reservaEditar == null) "Registro de Reservas" else "Editar Reserva",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pista,
                onValueChange = { pista = it },
                label = { Text("Número de Pista") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = jugadores,
                onValueChange = { jugadores = it },
                label = { Text("Cantidad de Jugadores") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { mostrarDatePicker = true }, modifier = Modifier.weight(1f)) {
                    Text(if (fecha.isEmpty()) "Fecha" else fecha)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { mostrarTimePicker = true }, modifier = Modifier.weight(1f)) {
                    Text(if (hora.isEmpty()) "Hora" else hora)
                }
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
                    label = { Text("Estado de la Reserva") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    estados.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = { estado = it; expanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || pista.isBlank() || fecha.isBlank() || hora.isBlank() || jugadores.isBlank()) {
                        viewModel.mostrarMensaje("Todos los campos son obligatorios")
                        return@Button
                    }

                    val calendarActual = Calendar.getInstance()
                    val sdfFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val sdfHora = SimpleDateFormat("HH:mm", Locale.getDefault())
                    
                    try {
                        val fechaSeleccionada = sdfFecha.parse(fecha)
                        val horaSeleccionada = sdfHora.parse(hora)
                        
                        val calendarSeleccionado = Calendar.getInstance()
                        if (fechaSeleccionada != null && horaSeleccionada != null) {
                            val calFecha = Calendar.getInstance()
                            calFecha.time = fechaSeleccionada
                            val calHora = Calendar.getInstance()
                            calHora.time = horaSeleccionada
                            
                            calendarSeleccionado.set(
                                calFecha.get(Calendar.YEAR),
                                calFecha.get(Calendar.MONTH),
                                calFecha.get(Calendar.DAY_OF_MONTH),
                                calHora.get(Calendar.HOUR_OF_DAY),
                                calHora.get(Calendar.MINUTE)
                            )
                        }

                        if (calendarSeleccionado.before(calendarActual)) {
                            viewModel.mostrarMensaje("No puedes programar una reserva para una fecha u hora pasada")
                            return@Button
                        }
                    } catch (e: Exception) {
                        viewModel.mostrarMensaje("Error al validar fecha y hora")
                        return@Button
                    }

                    val reserva = Reserva(
                        id = reservaEditar?.id ?: 0,
                        nombre = nombre,
                        pista = pista,
                        fecha = fecha,
                        hora = hora,
                        cantidadJugadores = jugadores.toIntOrNull() ?: 1,
                        estado = estado
                    )

                    if (reservaEditar == null) {
                        viewModel.insertarReserva(reserva)
                    } else {
                        viewModel.actualizarReserva(reserva)
                        onIrALista()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (reservaEditar == null) "Guardar Reserva" else "Actualizar Cambios")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cancelar y Volver")
            }
        }
    }

    if (mostrarDatePicker) {
        val initialDateMillis = remember(fecha) {
            if (fecha.isNotEmpty()) {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                try {
                    sdf.parse(fecha)?.time
                } catch (e: Exception) {
                    System.currentTimeMillis()
                }
            } else {
                System.currentTimeMillis()
            }
        }

        val dateState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val hoy = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
                    return utcTimeMillis >= hoy
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                        dateState.selectedDateMillis?.let { millis ->
                            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            calendar.timeInMillis = millis

                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            formatter.timeZone = TimeZone.getTimeZone("UTC")
                            fecha = formatter.format(calendar.time)
                        }
                        mostrarDatePicker = false
                    }) { Text("OK") }
            }
        ) { DatePicker(state = dateState) }
    }

    if (mostrarTimePicker) {
        val initialHour = remember(hora) {
            if (hora.contains(":")) hora.split(":")[0].toIntOrNull() ?: 0 else 12
        }
        val initialMinute = remember(hora) {
            if (hora.contains(":")) hora.split(":")[1].toIntOrNull() ?: 0 else 0
        }

        val timeState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { mostrarTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    hora = String.format("%02d:%02d", timeState.hour, timeState.minute)
                    mostrarTimePicker = false
                }) { Text("OK") }
            },
            text = { TimePicker(state = timeState) }
        )
    }
}
