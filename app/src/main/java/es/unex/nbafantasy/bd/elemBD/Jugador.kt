package es.unex.nbafantasy.bd.elemBD

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Jugador (
    @PrimaryKey(autoGenerate = true) var jugadorId: Long?,
    val nombre: String = "",
    val apellido: String = "",
    val equipo: String = "",
    val conferencia:  String = "",
    val posicion:  String = "",
    val alturaPulgadas: Long = 0,
    val puntosPartido: Long = 0,
    val taponesPartido:Long = 0,
    val rebotesPartido: Long = 0,
    val robosPartido: Long = 0,
    val asistenciasPartido: Long = 0,
    val minutosPartido: Long = 0,
    val erroresPartido: Long = 0,
    val mediaGeneralPartido: Long = 0,
):Serializable