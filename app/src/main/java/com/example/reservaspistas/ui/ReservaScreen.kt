package com.example.reservaspistas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

    var nombre   by remember { mutableStateOf("") }
    var pista    by remember { mutableStateOf("") }
    var fecha    by remember { mutableStateOf("") }
    var hora     by remember { mutableStateOf("") }
    var jugadores by remember { mutableStateOf(1) }
    var estado   by remember { mutableStateOf("Activa") }

    var expanded by remember { mutableStateOf(false) }
    val estados = listOf("Activa", "Finalizada")
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(mensaje) {
        if (mensaje.isNotEmpty()) {
            snackbarHostState.showSnackbar(mensaje, duration = SnackbarDuration.Short)
            viewModel.mostrarMensaje("")
        }
    }

    LaunchedEffect(reservaEditar) {
        if (reservaEditar != null) {
            nombre    = reservaEditar.nombre
            pista     = reservaEditar.pista
            fecha     = reservaEditar.fecha
            hora      = reservaEditar.hora
            jugadores = reservaEditar.cantidadJugadores
            estado    = reservaEditar.estado
        } else {
            nombre = ""; pista = ""; fecha = ""; hora = ""; jugadores = 1; estado = "Activa"
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = GrisClaro,
        topBar = {
            Surface(shadowElevation = 2.dp, color = Blanco) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onCancelar) {
                        Text("← Volver", color = Azul)
                    }
                    Text(
                        if (reservaEditar == null) "Nueva Reserva" else "Editar Reserva",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Negro
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Nombre
            CampoTexto("Nombre del Cliente", nombre, { nombre = it }, "Ej: Juan Pérez")

            // Pista
            CampoTexto("Número de Pista", pista, { pista = it }, "Ej: 3")

            // Jugadores con +/-
            Text("Cantidad de Jugadores", fontSize = 13.sp, color = Gris, fontWeight = FontWeight.Medium)
            Card(
                colors = CardDefaults.cardColors(containerColor = Blanco),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilledTonalButton(
                        onClick = { if (jugadores > 1) jugadores-- },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(44.dp),
                        contentPadding = PaddingValues(0.dp),
                        enabled = jugadores > 1
                    ) {
                        Text("−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "$jugadores",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Azul
                    )
                    FilledTonalButton(
                        onClick = { if (jugadores < 10) jugadores++ },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(44.dp),
                        contentPadding = PaddingValues(0.dp),
                        enabled = jugadores < 10
                    ) {
                        Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Fecha y hora
            Text("Fecha y Hora", fontSize = 13.sp, color = Gris, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = { mostrarDatePicker = true },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(if (fecha.isEmpty()) "📅 Seleccionar" else "📅 $fecha", fontSize = 13.sp)
                }
                OutlinedButton(
                    onClick = { mostrarTimePicker = true },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(if (hora.isEmpty()) "🕐 Seleccionar" else "🕐 $hora", fontSize = 13.sp)
                }
            }

            // Estado
            Text("Estado", fontSize = 13.sp, color = Gris, fontWeight = FontWeight.Medium)
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = estado,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
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

            Spacer(modifier = Modifier.height(8.dp))

            // Guardar
            Button(
                onClick = {
                    if (nombre.isBlank() || pista.isBlank() || fecha.isBlank() || hora.isBlank()) {
                        viewModel.mostrarMensaje("Todos los campos son obligatorios")
                        return@Button
                    }
                    val reserva = Reserva(
                        id = reservaEditar?.id ?: 0,
                        nombre = nombre, pista = pista, fecha = fecha, hora = hora,
                        cantidadJugadores = jugadores, estado = estado
                    )
                    if (reservaEditar == null) {
                        viewModel.insertarReserva(reserva)
                    } else {
                        viewModel.actualizarReserva(reserva)
                        onIrALista()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    if (reservaEditar == null) "Guardar Reserva" else "Actualizar Cambios",
                    fontSize = 15.sp, fontWeight = FontWeight.SemiBold
                )
            }

            TextButton(onClick = onCancelar, modifier = Modifier.fillMaxWidth()) {
                Text("Cancelar", color = Gris)
            }
        }
    }

    if (mostrarDatePicker) {
        val dateState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val hoy = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
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
                        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        cal.timeInMillis = millis
                        val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        fmt.timeZone = TimeZone.getTimeZone("UTC")
                        fecha = fmt.format(cal.time)
                    }
                    mostrarDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = dateState) }
    }

    if (mostrarTimePicker) {
        val timeState = rememberTimePickerState(is24Hour = true)
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

@Composable
fun CampoTexto(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column {
        Text(label, fontSize = 13.sp, color = Gris, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Gris) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )
    }
}
