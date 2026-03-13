package com.example.reservaspistas.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservaspistas.data.Reserva
import com.example.reservaspistas.repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservaViewModel(private val repository: ReservaRepository) : ViewModel() {

    private val _listaReservas = MutableStateFlow<List<Reserva>>(emptyList())
    val listaReservas: StateFlow<List<Reserva>> = _listaReservas

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    init {
        cargarReservas()
    }

    fun cargarReservas() {
        viewModelScope.launch {
            repository.todasLasReservas.collect {
                _listaReservas.value = it
            }
        }
    }

    fun insertarReserva(reserva: Reserva) {
        viewModelScope.launch {

            val resultado = repository.insertar(reserva)

            if (resultado) {
                _mensaje.value = "Reserva agregada"
            } else {
                _mensaje.value = "Ya existe una reserva activa para esa pista en esa fecha y hora"
            }
        }
    }

    fun eliminarReserva(reserva: Reserva) {
        viewModelScope.launch {
            repository.eliminar(reserva)
        }
    }

    fun actualizarReserva(reserva: Reserva) {
        viewModelScope.launch {
            repository.actualizar(reserva)
        }
    }

    fun buscar(nombre: String) {
        viewModelScope.launch {
            repository.buscarPorNombre(nombre).collect {
                _listaReservas.value = it
            }
        }
    }

    fun mostrarMensaje(texto: String) {
        _mensaje.value = texto
    }
}