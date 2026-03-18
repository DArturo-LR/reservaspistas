package com.example.reservaspistas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reservaspistas.ui.theme.*
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
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text("🎳", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Bowling Center",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Azul
        )
        Text(
            "Sistema de Reservas",
            fontSize = 14.sp,
            color = Gris
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tarjeta resumen
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${reservas.size}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Azul
                    )
                    Text("Total", fontSize = 12.sp, color = Gris)
                }
                Divider(
                    modifier = Modifier
                        .height(50.dp)
                        .width(1.dp),
                    color = GrisClaro
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$activas",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Verde
                    )
                    Text("Activas", fontSize = 12.sp, color = Gris)
                }
                Divider(
                    modifier = Modifier
                        .height(50.dp)
                        .width(1.dp),
                    color = GrisClaro
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${reservas.size - activas}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gris
                    )
                    Text("Finalizadas", fontSize = 12.sp, color = Gris)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onIrARegistro,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("+ Nueva Reserva", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onIrALista,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Ver Reservas", fontSize = 15.sp)
        }
    }
}
