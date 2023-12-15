package es.unex.nbafantasy.bd.roomBD

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador

@Dao
interface JugadorEquipoDao {
    @Query("SELECT * FROM JugadorEquipo")
    fun getAllJugadorEquipo(): LiveData<List<JugadorEquipo>>
    /**
     * Obtiene la lista de jugadores asociados a un usuario específico.
     *
     * @param usuarioId El identificador único del usuario.
     * @return Una lista de objetos [JugadorEquipo] que pertenecen al usuario especificado.
     */
    @Query("SELECT * FROM jugadorequipo WHERE usuarioId = :usuarioId")
    suspend fun getJugadoresByUsuario(usuarioId: Long): List<JugadorEquipo>

    @Query(" SELECT Jugador.* FROM Jugador INNER JOIN JugadorEquipo ON Jugador.jugadorId = JugadorEquipo.jugadorId WHERE JugadorEquipo.usuarioId = :usuarioId")
    fun getJugadoresByUsuariolive(usuarioId: Long): LiveData<List<Jugador>>

    /**
     * Inserta una asociación de usuario y jugador en la tabla de jugadorEquipo.
     *
     * @param jugadorEquipo El objeto [JugadorEquipo] que representa la relación entre un usuario y un jugador.
     * @return El identificador único de la asociación recién insertada.
     */
    @Insert
    suspend fun insertar(jugadorEquipo: JugadorEquipo): Long

    /**
     * Elimina una asociación de usuario y jugador de la tabla de jugadorEquipo.
     *
     * @param usuarioId El identificador único del usuario.
     * @param jugadorId El identificador único del jugador.
     */
    @Query("DELETE FROM jugadorEquipo WHERE usuarioId = :usuarioId AND jugadorId = :jugadorId")
    suspend fun eliminar(usuarioId: Long, jugadorId: Long)

    /**
     * Borra todas las asociaciones de un usuario con jugadores de la tabla de jugadorEquipo.
     *
     * @param usuarioId El identificador único del usuario.
     */
    @Query("DELETE FROM jugadorEquipo WHERE usuarioId = :usuarioId")
    suspend fun deleteAll(usuarioId: Long)

}