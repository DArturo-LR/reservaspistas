package com.example.reservaspistas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class Reserva(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val pista: String,

    val fecha: String,

    val hora: String,

    val cantidadJugadores: Int,

    val estado: String = "Activa"
)