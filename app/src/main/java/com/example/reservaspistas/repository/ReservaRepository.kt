package com.example.reservaspistas.repository

import com.example.reservaspistas.data.Reserva
import com.example.reservaspistas.data.ReservaDao
import kotlinx.coroutines.flow.Flow

class ReservaRepository(private val dao: ReservaDao) {

    val todasLasReservas: Flow<List<Reserva>> = dao.obtenerReservas()

    suspend fun insertar(reserva: Reserva): Boolean {
        val reservasExistentes = dao.obtenerReservasPorPistaYFecha(reserva.pista, reserva.fecha)

        val nuevaHoraEnMinutos = convertirHoraAMinutos(reserva.hora)
        for (existente in reservasExistentes) {
            val existenteHoraEnMinutos = convertirHoraAMinutos(existente.hora)
            val diferencia = Math.abs(nuevaHoraEnMinutos - existenteHoraEnMinutos)
            if (diferencia < 90) {
                return false
            }
        }

        dao.insertarReserva(reserva)
        return true
    }

    private fun convertirHoraAMinutos(hora: String): Int {
        val partes = hora.split(":")
        return partes[0].toInt() * 60 + partes[1].toInt()
    }

    suspend fun eliminar(reserva: Reserva) {
        dao.eliminarReserva(reserva)
    }

    suspend fun actualizar(reserva: Reserva): Boolean {
        val reservasExistentes = dao.obtenerReservasPorPistaYFecha(reserva.pista, reserva.fecha)
        val nuevaHoraEnMinutos = convertirHoraAMinutos(reserva.hora)

        for (existente in reservasExistentes) {
            if (existente.id != reserva.id) {
                val existenteHoraEnMinutos = convertirHoraAMinutos(existente.hora)
                if (Math.abs(nuevaHoraEnMinutos - existenteHoraEnMinutos) < 90) {
                    return false
                }
            }
        }
        dao.actualizarReserva(reserva)
        return true
    }

    fun buscarPorNombre(nombre: String): Flow<List<Reserva>> {
        return dao.buscarPorNombre(nombre)
    }
}