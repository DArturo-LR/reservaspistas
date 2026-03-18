package com.example.reservaspistas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reservaspistas.viewmodel.ReservaViewModel

@Composable
fun InicioScreen(
    viewModel: ReservaViewModel,
    onIrARegistro: () -> Unit,
    onIrALista: () -> Unit
) {
    val reservas by viewModel.listaReservas.collectAsState()
    val activas = reservas.count { it.estado == "Activa" }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bowling Center", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Resumen de Ocupación", style = MaterialTheme.typography.titleLarge)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Total Reservas: ${reservas.size}")
                Text("Reservas Activas: $activas")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onIrARegistro, modifier = Modifier.fillMaxWidth()) {
            Text("Nueva Reserva")
        }

        OutlinedButton(onClick = onIrALista, modifier = Modifier.fillMaxWidth()) {
            Text("Ver Todas las Reservas")
        }
    }
}
