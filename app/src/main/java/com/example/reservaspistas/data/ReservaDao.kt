package com.example.reservaspistas.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {

    @Insert
    suspend fun insertarReserva(reserva: Reserva)

    @Delete
    suspend fun eliminarReserva(reserva: Reserva)

    @Update
    suspend fun actualizarReserva(reserva: Reserva)

    @Query("SELECT * FROM reservas")
    fun obtenerReservas(): Flow<List<Reserva>>

    @Query("SELECT * FROM reservas WHERE nombre LIKE '%' || :nombre || '%'")
    fun buscarPorNombre(nombre: String): Flow<List<Reserva>>

    @Query("SELECT * FROM reservas WHERE pista = :pista AND fecha = :fecha AND estado = 'Activa'")
    suspend fun obtenerReservasPorPistaYFecha(pista: String, fecha: String): List<Reserva>

    @Query("""
        SELECT COUNT(*) FROM reservas 
        WHERE pista = :pista 
        AND fecha = :fecha 
        AND hora = :hora 
        AND estado = 'Activa'
    """)
    suspend fun existeReservaActiva(
        pista: String,
        fecha: String,
        hora: String
    ): Int
}