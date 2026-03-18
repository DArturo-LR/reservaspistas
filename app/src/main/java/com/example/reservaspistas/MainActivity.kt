package com.example.reservaspistas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reservaspistas.data.AppDatabase
import com.example.reservaspistas.data.Reserva
import com.example.reservaspistas.repository.ReservaRepository
import com.example.reservaspistas.ui.ListaReservasScreen
import com.example.reservaspistas.ui.ReservaScreen
import com.example.reservaspistas.viewmodel.ReservaViewModel
import com.example.reservaspistas.viewmodel.ReservaViewModelFactory
import com.example.reservaspistas.ui.InicioScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ReservaRepository(database.reservaDao())
        val factory = ReservaViewModelFactory(repository)

        setContent {
            val viewModel: ReservaViewModel = viewModel(factory = factory)

            // Estados de navegación
            var pantalla by remember { mutableStateOf("inicio") }
            var reservaEditar by remember { mutableStateOf<Reserva?>(null) }

            when (pantalla) {
                "inicio" -> InicioScreen(
                    viewModel = viewModel,
                    onIrARegistro = {
                        reservaEditar = null
                        pantalla = "form"
                    },
                    onIrALista = { pantalla = "lista" }
                )

                "form" -> ReservaScreen(
                    viewModel = viewModel,
                    reservaEditar = reservaEditar,
                    onIrALista = { pantalla = "lista" },
                    onCancelar = { pantalla = "inicio" }
                )

                "lista" -> ListaReservasScreen(
                    viewModel = viewModel,
                    onEditar = {
                        reservaEditar = it
                        pantalla = "form"
                    },
                    onVolver = {
                        pantalla = "inicio"
                    }
                )
            }
        }
    }
}