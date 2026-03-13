package com.example.reservaspistas.repository

import com.example.reservaspistas.data.Reserva
import com.example.reservaspistas.data.ReservaDao
import kotlinx.coroutines.flow.Flow

class ReservaRepository(private val dao: ReservaDao) {

    val todasLasReservas: Flow<List<Reserva>> = dao.obtenerReservas()

    suspend fun insertar(reserva: Reserva): Boolean {

        val existe = dao.existeReservaActiva(
            reserva.pista,
            reserva.fecha,
            reserva.hora
        )

        return if (existe == 0) {
            dao.insertarReserva(reserva)
            true
        } else {
            false
        }
    }

    suspend fun eliminar(reserva: Reserva) {
        dao.eliminarReserva(reserva)
    }

    suspend fun actualizar(reserva: Reserva) {
        dao.actualizarReserva(reserva)
    }

    fun buscarPorNombre(nombre: String): Flow<List<Reserva>> {
        return dao.buscarPorNombre(nombre)
    }
}