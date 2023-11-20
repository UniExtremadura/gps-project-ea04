package es.unex.nbafantasy.bd.elemBD

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    primaryKeys = ["usuarioId", "jugadorId"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["usuarioId"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Jugador::class,
            parentColumns = ["jugadorId"],
            childColumns = ["jugadorId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)

data class JugadorEquipo(
    val usuarioId: Long?,
    val jugadorId: Long?
) : Serializable



