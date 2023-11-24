package es.unex.nbafantasy.bd.elemBD

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

import androidx.room.ForeignKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["usuarioId"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ResultadoPartido(
    @PrimaryKey(autoGenerate = true) var resultadoPartidoId: Long?,
    val usuarioId: Long,
    val misPuntos: Double?,
    val puntosRivales: Double?,
    val estadoResultado: String?
): Serializable