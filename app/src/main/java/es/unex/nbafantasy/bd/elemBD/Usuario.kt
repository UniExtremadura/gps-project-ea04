package es.unex.nbafantasy.bd.elemBD

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Usuario(
    @PrimaryKey(autoGenerate = true) var usuarioId: Long?,
    val nombre: String = "",
    val contrasena: String = ""
) : Serializable

