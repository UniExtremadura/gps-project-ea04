package es.unex.nbafantasy.bd.elemBD

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Jugador(
    @PrimaryKey(autoGenerate = true) var jugadorId: Long?,
    val nombre: String = "",
    val apellido: String = "",
    val equipo: String? = "",
    val conferencia:  String? = "",
    val posicion:  String? = "",
    val alturaPulgadas: Double? = 0.0,
    val puntosPartido: Double = 0.0,
    val taponesPartido: Double = 0.0,
    val rebotesPartido: Double = 0.0,
    val robosPartido: Double = 0.0,
    val asistenciasPartido: Double = 0.0,
    val minutosPartido: String = "",
    val erroresPartido: Double = 0.0,
    val mediaGeneralPartido: Double = 0.0,
):Serializable